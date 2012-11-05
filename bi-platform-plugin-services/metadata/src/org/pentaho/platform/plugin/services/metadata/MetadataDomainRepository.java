/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License, version 2 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/gpl-2.0.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * Copyright 2007 - 2008 Pentaho Corporation.  All rights reserved.
 *  
 */
package org.pentaho.platform.plugin.services.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.repository.DomainAlreadyExistsException;
import org.pentaho.metadata.repository.DomainIdNullException;
import org.pentaho.metadata.repository.DomainStorageException;
import org.pentaho.metadata.repository.FileBasedMetadataDomainRepository;
import org.pentaho.metadata.util.LocalizationUtil;
import org.pentaho.metadata.util.XmiParser;
import org.pentaho.platform.api.engine.IAclHolder;
import org.pentaho.platform.api.engine.IFileFilter;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.api.engine.ISolutionFile;
import org.pentaho.platform.api.engine.ISolutionFilter;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.services.metadata.MetadataPublisher;

import org.pentaho.platform.plugin.services.metadata.messages.Messages;

import org.pentaho.platform.util.messages.LocaleHelper;

/**
 * This is the platform implementation of the IMetadataDomainRepository.
 * 
 * TODO: Update this class to use CacheControl, getting created per session per
 * Marc's input
 * 
 * @author Will Gorman (wgorman@pentaho.com)
 */
public class MetadataDomainRepository extends FileBasedMetadataDomainRepository {

   private static final String LEGACY_LOCATION = "__LEGACY_LOCATION_"; //$NON-NLS-1$

   protected final Log logger = LogFactory
         .getLog(MetadataDomainRepository.class);

   private static String LEGACY_XMI_FILENAME = "metadata.xmi"; //$NON-NLS-1$

   private static String LEGACY_XMI_FILE_EXTENSION = ".xmi";

   public static String PROPERTIES_FILE_EXTENSION = ".properties"; //$NON-NLS-1$

   public static final int[] ACCESS_TYPE_MAP = new int[] {
         IAclHolder.ACCESS_TYPE_READ, IAclHolder.ACCESS_TYPE_WRITE,
         IAclHolder.ACCESS_TYPE_UPDATE, IAclHolder.ACCESS_TYPE_DELETE,
         IAclHolder.ACCESS_TYPE_ADMIN, IAclHolder.ACCESS_TYPE_ADMIN };

   private static final String DOMAIN_FOLDER = "system/metadata/domains"; //$NON-NLS-1$

   //
   // Use this approach for accessing the session object when
   // updated to use the CacheManager
   //
   // private IPentahoSession session;
   //
   // public void setSession(IPentahoSession session) {
   // this.session = session;
   // }
   //
   // public IPentahoSession getSession() {
   // return session;
   // }

   public IPentahoSession getSession() {
      return PentahoSessionHolder.getSession();
   }

   protected File getDomainsFolder() {
    String domainsFolder = PentahoSystem.getApplicationContext().getSolutionPath(DOMAIN_FOLDER);

      File folder = new File(domainsFolder);
      return folder;
   }

   public Set<String> getDomainIds() {
      // double check that all the legacy domains are loaded
    // this is necessary because the reloading of domains might have been done by 
      // a user who might not have permissions to all solutions

    // the super call is made to avoid having it called twice, the logic in getDomainIds
      // calls reload if the map is empty.
      synchronized (domains) {
         super.reloadDomains();
         reloadLegacyDomains(false);
      }
      return super.getDomainIds();
   }

   public void reloadDomains() {
      synchronized (domains) {
         // first reload new metadata domains
         super.reloadDomains();
         // then populate the legacy domains
         reloadLegacyDomains(true);
      }
   }

   public void removeDomain(String domainId) {
      synchronized (domains) {
         // determine if domain is legacy or not
         Domain domain = domains.get(domainId);

         if (domain.getProperty(LEGACY_LOCATION) != null) {
            // this is an xmi based domain, remove it
            removeLegacyDomain(domainId);
         } else {
            super.removeDomain(domainId);
         }
      }
   }

   public Domain getDomain(String id) {
      Domain domain = super.getDomain(id);
      if (domain == null) {
         // reload legacy domains if not found
         reloadLegacyDomains(false);
         return super.getDomain(id);
      } else {
         return domain;
      }
   }

   private void removeLegacyDomain(String domainId) {
    ISolutionRepository repo = PentahoSystem.get(ISolutionRepository.class, getSession());
      repo.removeSolutionFile(domainId);
      domains.remove(domainId);
   }

   @SuppressWarnings("unchecked")
   private void reloadLegacyDomains(boolean overwrite) {
      // also load the XMI domains
    ISolutionRepository repo = PentahoSystem.get(ISolutionRepository.class, getSession());
      // filter the repository looking for XMI files
      Document doc = repo.getSolutionTree(ISolutionRepository.ACTION_EXECUTE,
            new ISolutionFilter() {
      public boolean keepFile(ISolutionFile solutionFile, int actionOperation) {
        return solutionFile.isDirectory() || solutionFile.getExtension().equalsIgnoreCase("xmi") || solutionFile.getExtension().equalsIgnoreCase(".xmi"); //$NON-NLS-1$ //$NON-NLS-2$
               }
            });
      int allSuccess = MetadataPublisher.NO_ERROR;
      try {
         // first look for metadata.xmi leaves in solution roots
         // e.g. /tree/pentaho-solutions/somedir/metadata.xmi
      List nodes = doc.selectNodes("/tree/branch/branch/leaf[@isDir='false']"); //$NON-NLS-1$
         for (Object obj : nodes) {
            Element node = (Element) obj;
            Element pathNode = (Element) (node).selectSingleNode("path"); //$NON-NLS-1$
            String path = pathNode.getText();
            // remove the first node
            int idx = path.indexOf(ISolutionRepository.SEPARATOR, 1);
            path = path.substring(idx + 1);
            // make sure the filename is 'metadata.xmi'
            if (path.endsWith(LEGACY_XMI_FILENAME)) {
               if (overwrite || !domains.containsKey(path)) {
                  allSuccess |= loadMetadata(path);
               }
            }
         }

         // now look for */resources/metadata/*.xmi
         // e.g. /tree/pentaho-solutions/some/resources/metadata/*.xmi
      nodes = doc.selectNodes("/tree/branch/branch/branch/branch/leaf[@isDir='false']"); //$NON-NLS-1$
         for (Object obj : nodes) {
            // build up the directory path
            Element node = (Element) obj;
            String dir = node.getParent().attributeValue("id"); //$NON-NLS-1$
            // make sure the parent branch ends with /resources/metadata
            if (dir.endsWith("/resources/metadata")) { //$NON-NLS-1$
               Element pathNode = (Element) (node).selectSingleNode("path"); //$NON-NLS-1$        
               String path = pathNode.getText();
               // remove the first node
               int idx = path.indexOf(ISolutionRepository.SEPARATOR, 1);
               path = path.substring(idx + 1);
               if (overwrite || !domains.containsKey(path)) {
                  allSuccess |= loadMetadata(path);
               }
            }
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private int loadMetadata(final String resourceName) {
      int result = MetadataPublisher.NO_ERROR;
      InputStream xmiInputStream;
      xmiInputStream = null;
    ISolutionRepository repo = PentahoSystem.get(ISolutionRepository.class, getSession());
      if (repo.resourceExists(resourceName, ISolutionRepository.ACTION_EXECUTE)) {
         try {
        xmiInputStream = repo.getResourceInputStream(resourceName, true, ISolutionRepository.ACTION_EXECUTE);
            Domain domain = new XmiParser().parseXmi(xmiInputStream);
            domain.setProperty(LEGACY_LOCATION, resourceName);
        // The domain key is the full path and name of the XMI file within the solution repository
            // e.g. mysolution/resources/metadata/mymodel.xmi or
            // mysolution/metadata.xmi
            domain.setId(resourceName);
            importLocalizations(domain, resourceName);
            domains.put(resourceName, domain);
         } catch (Throwable t) {
        logger.error(Messages.getString("MetadataPublisher.ERROR_0001_COULD_NOT_LOAD", resourceName), t); //$NON-NLS-1$
        throw new RuntimeException(Messages.getString("MetadataPublisher.ERROR_0001_COULD_NOT_LOAD"), t); //$NON-NLS-1$
         } finally {
            if (xmiInputStream != null) {
               try {
                  xmiInputStream.close();
               } catch (IOException ex) {
            logger.error(Messages.getString("MetadataPublisher.ERROR_0001_COULD_NOT_LOAD", resourceName), ex); //$NON-NLS-1$
            throw new RuntimeException(Messages.getString("MetadataPublisher.ERROR_0001_COULD_NOT_LOAD"), ex); //$NON-NLS-1$
               }
            }
         }
      } else {
         return result;
      }
      return result;
   }

  public void storeDomain(Domain domain, boolean overwrite) throws DomainIdNullException, DomainAlreadyExistsException, DomainStorageException {
      synchronized (domains) {
         if (domain.getId() == null) {
        throw new DomainIdNullException(org.pentaho.metadata.messages.Messages.getErrorString("IMetadataDomainRepository.ERROR_0001_DOMAIN_ID_NULL")); //$NON-NLS-1$
         }

         if (!overwrite && domains.get(domain.getId()) != null) {
        throw new DomainAlreadyExistsException(org.pentaho.metadata.messages.Messages.getErrorString("IMetadataDomainRepository.ERROR_0002_DOMAIN_OBJECT_EXISTS", domain.getId())); //$NON-NLS-1$
         }

         if (domain.getProperty(LEGACY_LOCATION) != null) {
            storeLegacyDomain(domain, overwrite);
         } else {
            super.storeDomain(domain, overwrite);
         }
      }
   }

  private void storeLegacyDomain(Domain domain, boolean overwrite) throws DomainIdNullException, DomainAlreadyExistsException, DomainStorageException {

      if (domain.getId() == null) {
      throw new DomainIdNullException(org.pentaho.metadata.messages.Messages.getErrorString("IMetadataDomainRepository.ERROR_0001_DOMAIN_ID_NULL")); //$NON-NLS-1$
      }

      // only allow editing vs. creation
      if (!overwrite) {
      throw new DomainAlreadyExistsException(org.pentaho.metadata.messages.Messages.getErrorString("IMetadataDomainRepository.ERROR_0002_DOMAIN_OBJECT_EXISTS", domain.getId())); //$NON-NLS-1$
      }

      XmiParser parser = new XmiParser();
      String xmi = parser.generateXmi(domain);
      try {
         String domainId = domain.getId();
         // parse the domain id into a path and filename
         // e.g. domainId = mysolution/resources/metadata/mymodel.xmi
         // path = mysolution/resources/metadata
         // fileName = mymodel.xmi
         int idx = domainId.lastIndexOf(ISolutionRepository.SEPARATOR);
         String path = domainId.substring(0, idx);
         String fileName = domainId.substring(idx + 1);
      ISolutionRepository repo = PentahoSystem.get(ISolutionRepository.class, getSession());
      String solutionPath = PentahoSystem.getApplicationContext().getSolutionPath(""); //$NON-NLS-1$
      repo.addSolutionFile(solutionPath, path, fileName, xmi.getBytes(LocaleHelper.getSystemEncoding()), true);

         // adds the domain to the domains list
         domains.put(domain.getId(), domain);

      } catch (Exception e) {
         throw new DomainStorageException(
               Messages
                     .getErrorString(
                           "MetadataDomainRepository.ERROR_0006_FAILED_TO_STORE_LEGACY_DOMAIN", domain.getId()), e); //$NON-NLS-1$
      }
   }

   /**
    * Returns the File object that is the absolute path to the folder that file
    * name is in.
    * 
    * @param String
    *           filename
    * @return File File object of the parent folder of filename.
    **/
   protected ISolutionFile getFolder(String filename)
         throws DomainStorageException {

      ISolutionRepository repo = PentahoSystem.get(ISolutionRepository.class, getSession());
      ISolutionFile solutionFile = repo.getSolutionFile(filename, ISolutionRepository.ACTION_EXECUTE);

      if (solutionFile != null) {
         return solutionFile.retrieveParent();
      } else {
         return null;
      }
   }

   /**
    * Returns the localization portion of the passed fileName.
    * 
    * @param fileName
    *           Example: metadata_EN_US.properties
    * @return String example: EN_US
    */
   protected String getLocaleFromPropertyFilename(String filename, String prefix)
         throws DomainStorageException {

      String locale = "";
      if (!StringUtils.isEmpty(filename)) {
         int startIndex = filename.lastIndexOf(ISolutionRepository.SEPARATOR);

         // increment our start index if we found the last slash
         if (startIndex >= 1) {
            startIndex++;
         } else {
            startIndex = 0;
         }

         int endIndex = filename.indexOf(PROPERTIES_FILE_EXTENSION);

         if (startIndex >= endIndex) {
            throw new DomainStorageException(Messages.getErrorString("MetadataDomainRepository.ERROR_0010_FAILED_TO_RESOLVE_LOCALE", filename), null); //$NON-NLS-1$
         }

         locale = filename.substring(startIndex + prefix.length() + 1, endIndex);
      }
      return locale;
   }

   /**
    * Returns a list of ISolutionFiles. These files are the property files that
    * are associated with the passed resource name
    * 
    * @param resourceName
    *           The name of the resource that has associated property files
    * @return SolutionFile[] Collection of associated property files.
    * @throws DomainStorageException
    */
   protected ISolutionFile[] getLocalePropertyFiles(String resourceName)
         throws DomainStorageException {

      try {

         // get the prefix of the xmiResource file
         int lastSlash = resourceName
               .lastIndexOf(ISolutionRepository.SEPARATOR);
         String filenamePrefix = resourceName.substring((lastSlash > 0 ? lastSlash + 1 : 0), resourceName.indexOf(LEGACY_XMI_FILE_EXTENSION));

         // the parent file is the folder that fileName is in
         ISolutionFile dir = getFolder(resourceName);

         // create the filter and get a list of files - no path to them
         IFileFilter filter = new PropertyFilesFilter(filenamePrefix);
         ISolutionFile[] fileNames = dir.listFiles(filter);

         // if we don't have a list of files
         if (fileNames == null) {

            // create an empty one
            fileNames = new ISolutionFile[0];
         }

         return fileNames;
      } catch (Exception e) {
         throw new DomainStorageException(
               Messages.getErrorString("MetadataDomainRepository.ERROR_0007_FAILED_TO_GET_LOCALIZATION_FILE_LIST", resourceName), e); //$NON-NLS-1$ 
      }
   }

   /**
    * Creates a File filter for discovering localization files.
    */
   private class PropertyFilesFilter implements IFileFilter {

      private String propertiesFilePrefix = null;

      /**
       * Creates the filter with the passed String as the pattern the file names
       * are to start with.
       * 
       * @param propertiesFilePrefix
       */
      public PropertyFilesFilter(String propertiesFilePrefix) {
         this.propertiesFilePrefix = propertiesFilePrefix + "_";
      }

      /**
       * Return s a list of files that start with propertiesFilePrefix and end
       * with PROPERTIES_FILE_EXTENSION.
       */
      public boolean accept(ISolutionFile iSolutionfile) {
         return iSolutionfile.getFileName().startsWith(propertiesFilePrefix)
               && iSolutionfile.getFileName().endsWith(
                     PROPERTIES_FILE_EXTENSION);
      }
   }

   /**
    * Imports the localizations for the passed resourceName into the domain. the
    * property files should be located in the same folder as resourceName
    * following this naming pattern.
    * 
    * If resourceName = "bi-developers/resources/metadata.xmi"
    * 
    * then a United States English property file for it would be
    * "bi-developers/resources/metadata_en_US.properties
    * 
    * Both file names share the string "metadata".
    * 
    * @param domain
    * @param resourceName
    * @throws DomainStorageException
    */
   protected void importLocalizations(Domain domain, String resourceName)
         throws DomainStorageException {

      // the parent file is the folder that fileName is in
      ISolutionFile[] propertiesFiles = getLocalePropertyFiles(resourceName);

      if (propertiesFiles != null) {
         LocalizationUtil localizationUtil = new LocalizationUtil();
        
         String locale = null;
         String propertiesFileNamePrefix = getPropertiesFilePrefix(resourceName);
         ISolutionRepository repo = PentahoSystem.get(
               ISolutionRepository.class, getSession());

         for (ISolutionFile propertyFile : propertiesFiles) {
            InputStream inputStream = null;
            try {
               // We need to get the resource input stream by passing the first
               // parameter as either following examples
               //     solution-folder/metadata_en_US.properties
               //     solution-folder/resources/metadata/mymodel_en_US.properties
               // passing false in the second parameter means to get me the file
               // as named in parameter 1 - do not manipulate the file name I passeds
               inputStream = repo.getResourceInputStream(propertyFile.getSolutionPath().replaceFirst(String.valueOf(ISolutionRepository.SEPARATOR), "") + ISolutionRepository.SEPARATOR + propertyFile.getFileName(), false, ISolutionRepository.ACTION_EXECUTE);
               Properties properties = new Properties();
               properties.load(inputStream);
               if (properties.isEmpty()) {
                  String message = Messages.getErrorString("MetadataDomainRepository.ERROR_0008_EMPTY_LOCALIZATION_PROPERTY_FILE", propertyFile.getFileName()); //$NON-NLS-1$
                  DomainStorageException dse = new DomainStorageException(message, null);
                  throw dse;
               }
               locale = getLocaleFromPropertyFilename(propertyFile.getFileName(), propertiesFileNamePrefix);
               if (logger.isDebugEnabled()) {
                  List<String> messages = localizationUtil.analyzeImport(domain, properties, locale);
                  for (String message : messages) {
                     logger.debug(message);
                  }
               }

               localizationUtil.importLocalizedProperties(domain, properties,
                     locale);
            } catch (FileNotFoundException fnfe) {
               throw new DomainStorageException(Messages.getErrorString("MetadataDomainRepository.ERROR_0009_EXCEPTION_READING_LOCALIZATION_PROPERTY_FILE", propertyFile.getFileName()), fnfe);
            } catch (IOException ioe) {
               throw new DomainStorageException(
                     Messages.getErrorString("MetadataDomainRepository.ERROR_0009_EXCEPTION_READING_LOCALIZATION_PROPERTY_FILE", propertyFile.getFileName()), ioe);
            } finally {
               try {
                  inputStream.close();
               } catch (IOException ioe) {
                  throw new DomainStorageException(Messages.getErrorString("MetadataDomainRepository.ERROR_0009_EXCEPTION_READING_LOCALIZATION_PROPERTY_FILE", propertyFile.getFileName()), ioe);
               }
            }
         }
      }
   }

   /**
    * Returns the prefix that the corresponding properties file should have.
    * 
    * @param String
    *           filename e.g. "/steel-wheels/metadata.xmi"
    * @return String e.g., "metadata"
    */
   protected String getPropertiesFilePrefix(String filename)
         throws DomainStorageException {

      int beginIndex = filename.lastIndexOf('/');

      // increment since we found a space
      if (beginIndex > 0) {
         beginIndex++;
      }

      int endIndex = filename.indexOf(".xmi");

      if (endIndex == 0) {
         return filename.substring(endIndex);
      } else {
         if (beginIndex >= endIndex) {
            throw new DomainStorageException(Messages.getErrorString("MetadataDomainRepository.ERROR_0011_FAILED_TO_RESOLVE_PREFIX_FOR_PROPERTY_FILE", filename), null); //$NON-NLS-1$
         }
         return filename.substring(beginIndex, endIndex);
      }
   }
}
