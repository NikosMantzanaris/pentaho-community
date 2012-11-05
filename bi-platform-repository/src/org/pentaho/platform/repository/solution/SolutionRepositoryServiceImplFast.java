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
 * @created Jul 12, 2005 
 * @author James Dixon, Angelo Rodriguez, Steven Barkdull
 */
package org.pentaho.platform.repository.solution;

import java.io.InputStream;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.pentaho.platform.api.engine.ICacheManager;
import org.pentaho.platform.api.engine.IContentInfo;
import org.pentaho.platform.api.engine.IFileInfo;
import org.pentaho.platform.api.engine.IPentahoRequestContext;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.api.engine.IPluginManager;
import org.pentaho.platform.api.engine.IPluginOperation;
import org.pentaho.platform.api.engine.ISolutionFile;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.engine.core.system.PentahoRequestContextHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.util.messages.LocaleHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SolutionRepositoryServiceImplFast extends SolutionRepositoryServiceImpl {

  private static final String REPOSITORY_SERVICE_FILEINFO_CACHE_REGION = "repository-service-fileinfo-cache";

  static {
    // The SolutionRepositoryService creates/uses the ICacheManager from PentahoSystem to create a new
    // cache region specifically for the caching of the solution repository document. This is not put
    // into a session cache intentionally. Client tools like PRD do not maintain a session and would
    // thus never have any benefit from this. Since we are using a cache manager, if the cache is
    // unused long enough entries will age out.

    // We are caching the solution repository document on a per-user basis, as required, because the
    // document is that user's view of the repository, with respect to ACLs.

    // Upon publish, reload, or reset repository calls this cache is cleared in the reset method
    // of SolutionRepositoryBase.
    ICacheManager cacheManager = PentahoSystem.getCacheManager(null);
    if (!cacheManager.cacheEnabled(ISolutionRepository.REPOSITORY_SERVICE_CACHE_REGION)) {
      cacheManager.addCacheRegion(ISolutionRepository.REPOSITORY_SERVICE_CACHE_REGION);
    }    
    if (!cacheManager.cacheEnabled(REPOSITORY_SERVICE_FILEINFO_CACHE_REGION)) {
      cacheManager.addCacheRegion(REPOSITORY_SERVICE_FILEINFO_CACHE_REGION);
    }
  }
  
  public SolutionRepositoryServiceImplFast() {
    super();
  }

  protected void processRepositoryFile(IPentahoSession session, boolean isAdministrator, ISolutionRepository repository, Node parentNode, ISolutionFile file,
      String[] filters) {

    final String name = file.getFileName();

    // MDD 10/16/2008 Not always.. what about 'system'
    if (name.startsWith("system") || name.startsWith("tmp") || name.startsWith(".")) { //$NON-NLS-1$
      // slip hidden files (starts with .)
      // skip the system & tmp dir, we DO NOT ever want this to hit the client
      return;
    }

     if (!accept(isAdministrator, repository, file)) {
      // we don't want this file, skip to the next one
      return;
    }
    
      if (file.isDirectory()) {
        // we always process directories
          
        // maintain legacy behavior
        if (repository.getRootFolder(ISolutionRepository.ACTION_EXECUTE).getFullPath().equals(file.getFullPath())) {
          // never output the root folder as part of the repo doc; skip root and process its children
          ISolutionFile[] children = file.listFiles();
          for (ISolutionFile childSolutionFile : children) {
            processRepositoryFile(session, isAdministrator, repository, parentNode, childSolutionFile, filters);
          }
          return;
        }
        
      Element child = null;
      final String key = file.getFullPath() + file.getLastModified() + LocaleHelper.getLocale();
      ICacheManager cacheManager = PentahoSystem.getCacheManager(null);
      if (cacheManager != null && cacheManager.cacheEnabled(REPOSITORY_SERVICE_FILEINFO_CACHE_REGION)) {
        child = (Element) cacheManager.getFromRegionCache(REPOSITORY_SERVICE_FILEINFO_CACHE_REGION, key);
      }
      if (child == null) {
        child = parentNode instanceof Document ? ((Document) parentNode).createElement("file") : parentNode.getOwnerDocument().createElement("file"); //$NON-NLS-1$ //$NON-NLS-2$
        try {
          String localizedName = repository.getLocalizedFileProperty(file, "name", ISolutionRepository.ACTION_EXECUTE); //$NON-NLS-1$
          child.setAttribute("localized-name", localizedName == null || "".equals(localizedName) ? name : localizedName); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (Exception e) {
          child.setAttribute("localized-name", name); //$NON-NLS-1$
        }
        try {
          String visible = repository.getLocalizedFileProperty(file, "visible", ISolutionRepository.ACTION_EXECUTE); //$NON-NLS-1$
          child.setAttribute("visible", visible == null || "".equals(visible) ? "false" : visible); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        } catch (Exception e) {
          e.printStackTrace();
          child.setAttribute("visible", "false"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        String description = repository.getLocalizedFileProperty(file, "description", ISolutionRepository.ACTION_EXECUTE); //$NON-NLS-1$
        child.setAttribute("description", description == null || "".equals(description) ? name : description); //$NON-NLS-1$ //$NON-NLS-2$
        child.setAttribute("name", name); //$NON-NLS-1$
        child.setAttribute("isDirectory", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        child.setAttribute("lastModifiedDate", "" + file.getLastModified()); //$NON-NLS-1$ //$NON-NLS-2$
        if (cacheManager != null && cacheManager.cacheEnabled(REPOSITORY_SERVICE_FILEINFO_CACHE_REGION)) {
          cacheManager.putInRegionCache(REPOSITORY_SERVICE_FILEINFO_CACHE_REGION, key, child);
        }
      } else {
        Element newChild = parentNode instanceof Document ? ((Document) parentNode).createElement("file") : parentNode.getOwnerDocument().createElement("file"); //$NON-NLS-1$//$NON-NLS-2$
        NamedNodeMap attributes = child.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
          Node attribute = attributes.item(i);
          newChild.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
        }
        child = newChild;
      }
      parentNode.appendChild(child);
  
          
        ISolutionFile[] children = file.listFiles();
        for (ISolutionFile childSolutionFile : children) {
          processRepositoryFile(session, isAdministrator, repository, child, childSolutionFile, filters);
        }
      } else {     
        InputStream pluginInputStream = null;
        try {
          int lastPoint = name.lastIndexOf('.');
          String extension = ""; //$NON-NLS-1$
          if (lastPoint != -1) {
            // ignore anything with no extension
            extension = name.substring(lastPoint + 1).toLowerCase();
          }
    
          // xaction and URL support are built in
          boolean addFile = acceptFilter(name, filters) || "xaction".equals(extension) || "url".equals(extension); //$NON-NLS-1$ //$NON-NLS-2$
          boolean isPlugin = false;
          // see if there is a plugin for this file type
          IPluginManager pluginManager = PentahoSystem.get(IPluginManager.class, session);
          if (pluginManager != null) {
            Set<String> types = pluginManager.getContentTypes();
            isPlugin = types != null && types.contains(extension);
            addFile |= isPlugin;
          }
    
          if (addFile) {

          ICacheManager cacheManager = PentahoSystem.getCacheManager(null);
          Element child = null;
          final String key = file.getFullPath() + file.getLastModified() + LocaleHelper.getLocale();
          if (cacheManager != null && cacheManager.cacheEnabled(REPOSITORY_SERVICE_FILEINFO_CACHE_REGION)) {
            child = (Element) cacheManager.getFromRegionCache(REPOSITORY_SERVICE_FILEINFO_CACHE_REGION, key);
          }
          if (child == null) {
            child = parentNode instanceof Document ? ((Document) parentNode).createElement("file") : parentNode.getOwnerDocument().createElement("file"); //$NON-NLS-1$ //$NON-NLS-2$
            if (cacheManager != null && cacheManager.cacheEnabled(REPOSITORY_SERVICE_FILEINFO_CACHE_REGION)) {
              cacheManager.putInRegionCache(REPOSITORY_SERVICE_FILEINFO_CACHE_REGION, key, child);
            }
          } else {
            Element newChild = parentNode instanceof Document ? ((Document) parentNode).createElement("file") : parentNode.getOwnerDocument().createElement("file"); //$NON-NLS-1$ //$NON-NLS-2$

            NamedNodeMap attributes = child.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
              Node attribute = attributes.item(i);
              newChild.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
            }

            parentNode.appendChild(newChild);
            return;
          }

            parentNode.appendChild(child);
            IFileInfo fileInfo = null;
            try {
                // the visibility flag for action-sequences is controlled by
                // /action-sequence/documentation/result-type
                // and we should no longer be looking at 'visible' because it was
                // never actually used!
                String visible = "none".equals(repository.getLocalizedFileProperty(file, "documentation/result-type", ISolutionRepository.ACTION_EXECUTE)) ? "false" : "true"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                child.setAttribute("visible", (visible == null || "".equals(visible) || "true".equals(visible)) ? "true" : "false");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            } catch (Exception e) {
              child.setAttribute("visible", "true"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            if (name.endsWith(".xaction")) { //$NON-NLS-1$
              // add special props?
              // localization..
            } else if (name.endsWith(".url")) { //$NON-NLS-1$
    
              // add special props
              String props = new String(file.getData());
              StringTokenizer tokenizer = new StringTokenizer(props, "\n"); //$NON-NLS-1$
              while (tokenizer.hasMoreTokens()) {
                String line = tokenizer.nextToken();
                int pos = line.indexOf('=');
                if (pos > 0) {
                  String propname = line.substring(0, pos);
                  String value = line.substring(pos + 1);
                  if ((value != null) && (value.length() > 0) && (value.charAt(value.length() - 1) == '\r')) {
                    value = value.substring(0, value.length() - 1);
                  }
                  if ("URL".equalsIgnoreCase(propname)) { //$NON-NLS-1$
                    child.setAttribute("url", value); //$NON-NLS-1$
                  }
                }
              }
            } else if (isPlugin) {
              // must be a plugin - make it look like a URL
              try {
                // get the file info object for this file
                // not all plugins are going to actually use the inputStream, so we have a special
                // wrapper inputstream so that we can pay that price when we need to (2X speed boost)
                pluginInputStream = new PluginFileInputStream(repository, file);
                fileInfo = pluginManager.getFileInfo(extension, session, file, pluginInputStream);
                String handlerId = pluginManager.getContentGeneratorIdForType(extension, session);
                String fileUrl = pluginManager.getContentGeneratorUrlForType(extension, session);
                String solution = file.getSolutionPath();
                String path = ""; //$NON-NLS-1$
                IPentahoRequestContext requestContext = PentahoRequestContextHolder.getRequestContext();
                String contextPath = requestContext.getContextPath();            
                if (solution.startsWith(ISolutionRepository.SEPARATOR + "")) { //$NON-NLS-1$
                  solution = solution.substring(1);
                }
                int pos = solution.indexOf(ISolutionRepository.SEPARATOR);
                if (pos != -1) {
                  path = solution.substring(pos + 1);
                  solution = solution.substring(0, pos);
                }
                String url = null;
                if (!"".equals(fileUrl)) { //$NON-NLS-1$
                  url = contextPath +  fileUrl //$NON-NLS-1$
                      + "?solution=" + solution + "&path=" + path + "&action=" + name; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                } else {
                  IContentInfo info = pluginManager.getContentInfoFromExtension(extension, session);
                  for (IPluginOperation operation : info.getOperations()) {
                    if (operation.getId().equalsIgnoreCase("RUN")) { //$NON-NLS-1$
                      String command = operation.getCommand();
                      command = command.replaceAll("\\{solution\\}", solution); //$NON-NLS-1$
                      command = command.replaceAll("\\{path\\}", path); //$NON-NLS-1$
                      command = command.replaceAll("\\{name\\}", name); //$NON-NLS-1$
                      url = contextPath + command; //$NON-NLS-1$
                      break;
                    }
                  }
                  if (url == null) {
                    url = contextPath + "content/" + handlerId + "?solution=" + solution + "&path=" + path + "&action=" + name; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                  }
                }
                child.setAttribute("url", url); //$NON-NLS-1$
                
                String paramServiceUrl = contextPath + "content/" + handlerId + "?solution=" + solution + "&path=" + path + "&action=" + name; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                
                child.setAttribute("param-service-url", paramServiceUrl); //$NON-NLS-1$
                
              } catch (Throwable t) {
                t.printStackTrace();
              }
    
            }
    
            // localization
            try {
              String localizedName = null;
              if (name.endsWith(".url")) { //$NON-NLS-1$
                localizedName = repository.getLocalizedFileProperty(file, "url_name", ISolutionRepository.ACTION_EXECUTE); //$NON-NLS-1$
              } else if (fileInfo != null) {
                localizedName = fileInfo.getTitle();
              } else {
                localizedName = repository.getLocalizedFileProperty(file, "title", ISolutionRepository.ACTION_EXECUTE); //$NON-NLS-1$
              }
              child
                  .setAttribute("localized-name", localizedName == null || "".equals(localizedName) ? name : localizedName); //$NON-NLS-1$ //$NON-NLS-2$
            } catch (Exception e) {
              child.setAttribute("localized-name", name); //$NON-NLS-1$
            }
            try {
              // only folders, urls and xactions have descriptions
              if (name.endsWith(".url")) { //$NON-NLS-1$
                String url_description = repository.getLocalizedFileProperty(file, "url_description", ISolutionRepository.ACTION_EXECUTE); //$NON-NLS-1$
                String description = repository.getLocalizedFileProperty(file, "description", ISolutionRepository.ACTION_EXECUTE); //$NON-NLS-1$
                if (url_description == null && description == null) {
                  child.setAttribute("description", name); //$NON-NLS-1$
                } else {
                  child.setAttribute("description", url_description == null || "".equals(url_description) ? description //$NON-NLS-1$ //$NON-NLS-2$
                      : url_description);
                }
              } else if (name.endsWith(".xaction")) { //$NON-NLS-1$
                String description = repository.getLocalizedFileProperty(file, "description", ISolutionRepository.ACTION_EXECUTE); //$NON-NLS-1$
                child.setAttribute("description", description == null || "".equals(description) ? name : description); //$NON-NLS-1$ //$NON-NLS-2$
              } else if (fileInfo != null) {
                child.setAttribute("description", fileInfo.getDescription()); //$NON-NLS-1$
              } else {
                child.setAttribute("description", name); //$NON-NLS-1$
              }
            } catch (Exception e) {
              child.setAttribute("description", "xxxxxxx"); //$NON-NLS-1$ //$NON-NLS-2$
            }
    
            // add permissions for each file/folder
            child.setAttribute("name", name); //$NON-NLS-1$
            child.setAttribute("isDirectory", "" + file.isDirectory()); //$NON-NLS-1$ //$NON-NLS-2$
            child.setAttribute("lastModifiedDate", "" + file.getLastModified()); //$NON-NLS-1$ //$NON-NLS-2$
          }
        } finally {
          IOUtils.closeQuietly(pluginInputStream);
        }
      } // else isfile
  }


}
