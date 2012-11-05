/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2005 - 2009 Pentaho Corporation.  All rights reserved.
 *
*/
package org.pentaho.test.platform.plugin;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import mondrian.olap.Util;
import mondrian.olap.Util.PropertyList;
import mondrian.xmla.DataSourcesConfig.DataSource;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.platform.api.data.IDatasourceService;
import org.pentaho.platform.api.engine.ICacheManager;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.api.engine.ISolutionEngine;
import org.pentaho.platform.api.engine.IPentahoDefinableObjectFactory.Scope;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.core.system.StandaloneSession;
import org.pentaho.platform.engine.core.system.boot.PlatformInitializationException;
import org.pentaho.platform.engine.security.SecurityHelper;
import org.pentaho.platform.engine.services.connection.datasource.dbcp.JndiDatasourceService;
import org.pentaho.platform.engine.services.solution.SolutionEngine;
import org.pentaho.platform.plugin.action.mondrian.catalog.IMondrianCatalogService;
import org.pentaho.platform.plugin.action.mondrian.catalog.MondrianCatalog;
import org.pentaho.platform.plugin.action.mondrian.catalog.MondrianCatalogHelper;
import org.pentaho.platform.plugin.action.mondrian.catalog.MondrianCatalogServiceException;
import org.pentaho.platform.plugin.action.mondrian.catalog.MondrianDataSource;
import org.pentaho.platform.plugin.action.mondrian.catalog.MondrianSchema;
import org.pentaho.platform.plugin.services.connections.mondrian.MDXConnection;
import org.pentaho.platform.plugin.services.connections.sql.SQLConnection;
import org.pentaho.platform.repository.solution.filebased.FileBasedSolutionRepository;
import org.pentaho.test.platform.engine.core.MicroPlatform;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

@SuppressWarnings("nls")
public class MondrianCatalogHelperTest {

  private MicroPlatform microPlatform;

  /**
   * Makes a copy of the test-datasources.xml so the test can write to it and muck it up.
   */
  public File setUpTempFile() {
    InputStream src = null;
    OutputStream dest = null;
    File destFile = null;
    try {
      src = new FileInputStream(PentahoSystem.getApplicationContext().getSolutionPath("test/analysis/test-helper-datasources.xml"));
      destFile = File.createTempFile("test-helper-datasources", ".xml");
      dest = new FileOutputStream(destFile);
      IOUtils.copy(src, dest);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    IOUtils.closeQuietly(src);
    IOUtils.closeQuietly(dest);
    return destFile;
  }
  
  /**
   * Makes a copy of the test-empty-datasources.xml so the test can write to it and muck it up.
   */
  public File setUpEmptyTempFile() {
    InputStream src = null;
    OutputStream dest = null;
    File destFile = null;
    try {
      src = new FileInputStream(PentahoSystem.getApplicationContext().getSolutionPath("test/analysis/test-empty-datasources.xml"));
      destFile = File.createTempFile("test-empty-datasources", ".xml");
      dest = new FileOutputStream(destFile);
      IOUtils.copy(src, dest);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    IOUtils.closeQuietly(src);
    IOUtils.closeQuietly(dest);
    return destFile;
  }

  private File setUp_testDataSourceInfoMergeDotXml() {
    InputStream src = null;
    OutputStream dest = null;
    File destFile = null;
    try {
      src = new FileInputStream(PentahoSystem.getApplicationContext().getSolutionPath("test/analysis/test-dataSourceInfoMerge.xml"));
      destFile = File.createTempFile("test-dataSourceInfoMerge", ".xml");
      destFile.deleteOnExit();
      dest = new FileOutputStream(destFile);
      IOUtils.copy(src, dest);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    IOUtils.closeQuietly(src);
    IOUtils.closeQuietly(dest);
    return destFile;
  }

  @Before
  public void init0() {
    microPlatform = new MicroPlatform("test-src/solution");
    microPlatform.define(ISolutionEngine.class, SolutionEngine.class);
    microPlatform.define(ISolutionRepository.class, FileBasedSolutionRepository.class);
    microPlatform.define(IMondrianCatalogService.class, MondrianCatalogHelper.class, Scope.GLOBAL);
    microPlatform.define("connection-SQL", SQLConnection.class);
    microPlatform.define("connection-MDX", MDXConnection.class);
    microPlatform.define(IDatasourceService.class, JndiDatasourceService.class, Scope.GLOBAL);
    try {
      microPlatform.start();
    } catch (PlatformInitializationException ex) {
      Assert.fail();
    }
    File destFile = setUpTempFile();
    MondrianCatalogHelper catalogService = (MondrianCatalogHelper)PentahoSystem.get(IMondrianCatalogService.class);
    catalogService.setDataSourcesConfig("file:" + destFile.getAbsolutePath());
    catalogService.setUseSchemaNameAsCatalogName(false);
    // JNDI
    System.setProperty("java.naming.factory.initial", "org.osjava.sj.SimpleContextFactory");
    System.setProperty("org.osjava.sj.root", "test-src/solution/system/simple-jndi");
    System.setProperty("org.osjava.sj.delimiter", "/");
    
    // Clear up the cache
    final ICacheManager cacheMgr = PentahoSystem.getCacheManager(createSession("joe", "ceo"));
    cacheMgr.clearRegionCache(MondrianCatalogHelper.MONDRIAN_CATALOG_CACHE_REGION);
  }
  
  public IPentahoSession createSession(String uname, String... authorities) {
    StandaloneSession session = new StandaloneSession();
    session.setAuthenticated(uname); 
    
    GrantedAuthority[] auths = new GrantedAuthority[authorities.length];
    for (int i=0; i<authorities.length; i++) {
      auths[i] = new GrantedAuthorityImpl(authorities[i]);
    }

    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(uname, "none", auths
    );
    // We now have a credential. We need to bind it into the IPentahoSession
    SecurityHelper.setPrincipal(auth, session);
    // We should be good to go now...
    return session;
  }
  
  @Test
  public void testEmptydatasources() {
    File destFile = setUpEmptyTempFile();
    MondrianCatalogHelper catalogService = (MondrianCatalogHelper)PentahoSystem.get(IMondrianCatalogService.class);
    catalogService.setDataSourcesConfig("file:" + destFile.getAbsolutePath());
    catalogService.setUseSchemaNameAsCatalogName(false);
    MondrianCatalogHelper helper = (MondrianCatalogHelper)PentahoSystem.get(IMondrianCatalogService.class);
    List<MondrianCatalog> list = helper.listCatalogs(createSession("joe", "ceo"), false);
    Assert.assertEquals(0, list.size());
  }

  /**
   * This is a test for <a href="http://jira.pentaho.com/browse/PSW-109">
   * http://jira.pentaho.com/browse/PSW-109</a>.
   * 
   * <p>When publishing and overwriting a schema, the properties in
   * the DataSourceInfo XML element of the Catalog element are wiped
   * and overwritten. They need to be merged/updated instead.
   */
  @Test
  public void testDataSourceInfoMerge() {
    // Boilerplate code
    File destFile = setUp_testDataSourceInfoMergeDotXml();
    MondrianCatalogHelper catalogService = (MondrianCatalogHelper)PentahoSystem.get(IMondrianCatalogService.class);
    catalogService.setDataSourcesConfig("file:" + destFile.getAbsolutePath());
    catalogService.setUseSchemaNameAsCatalogName(false);
    IMondrianCatalogService helper = PentahoSystem.get(IMondrianCatalogService.class);
    final IPentahoSession pentahoSession = createSession("joe", "ceo");
    // Actual test.
    final MondrianCatalog origCat =
      helper.getCatalog("FooBar", pentahoSession);
    final PropertyList dataSourceInfo =
      Util.parseConnectString(origCat.getDataSourceInfo());
    // Baconating...
    dataSourceInfo.put("Bacon", "true");
    final MondrianCatalog cat =
      new MondrianCatalog(
        origCat.getName(),
        dataSourceInfo.toString(),
        origCat.getDefinition(),
        origCat.getDataSource(),
        origCat.getSchema());
    helper.addCatalog(cat, true, pentahoSession);
    // Baconation Complete.
    final MondrianCatalog mergedCatalog =
      helper.getCatalog("FooBar", pentahoSession);
    Assert.assertNotNull(mergedCatalog);
    Assert.assertEquals(
      "FooBar", mergedCatalog.getName());
    // Make sure everything is baconated
    Assert.assertTrue(mergedCatalog.getDataSourceInfo().contains("Bacon=true"));
    // Make sure everything is chunky as well.
    Assert.assertTrue(mergedCatalog.getDataSourceInfo().contains("Chunky=true"));
  }

  @Test
  public void testGetCatalog() {
    IPentahoSession session = this.createSession("joe", "ceo", "Admin", "Authenticated");
    MondrianCatalogHelper helper = (MondrianCatalogHelper)PentahoSystem.get(IMondrianCatalogService.class);;
    MondrianCatalog mc = helper.getCatalog("SteelWheels3", session);
    Assert.assertNotNull(mc);
  }
  
  @Test
  public void testRemoveCatalog() throws Exception {
    IPentahoSession session = this.createSession("joe", "ceo", "Admin", "Authenticated");
    MondrianCatalogHelper helper = (MondrianCatalogHelper)PentahoSystem.get(IMondrianCatalogService.class);

    // add a new catalog with a new schema file
    MondrianSchema schema = new MondrianSchema("ToRemoveCatalog", null);
    MondrianDataSource ds = new MondrianDataSource(
        "Provider=Mondrian;DataSource=Pentaho",
        "Pentaho BI Platform Datasources",
        "http://localhost:8080/pentaho/Xmla?userid=joe&amp;password=password", 
        "Provider=Mondrian", // no default jndi datasource should be specified
        "PentahoXMLA", 
        DataSource.PROVIDER_TYPE_MDP, 
        DataSource.AUTH_MODE_UNAUTHENTICATED, 
        null
      );

    FileInputStream fis = new FileInputStream(PentahoSystem.getApplicationContext().getSolutionPath("test/charts/steelwheels.mondrian.xml"));
    File file = new File(PentahoSystem.getApplicationContext().getSolutionPath("test/charts/steelwheels2.mondrian.xml"));
    FileOutputStream fos = new FileOutputStream(file);
    IOUtils.copy(fis, fos);
    
    Assert.assertTrue(file.exists());
    
    MondrianCatalog cat = new MondrianCatalog("ToRemoveCatalog", "Provider=mondrian;DataSource=SampleDataTest",
        "solution:test/charts/steelwheels2.mondrian.xml", ds, schema);

    helper.addCatalog(cat, true, session);
    List<MondrianCatalog> cats = helper.listCatalogs(session, false);
    Assert.assertEquals(5, cats.size());
    
    helper.removeCatalog("ToRemoveCatalog", session);

    cats = helper.listCatalogs(session, false);

    Assert.assertEquals(4, cats.size());
    
    Assert.assertTrue(!file.exists());
  }

  @Test
  public void testListCatalogs() throws Exception {
    IPentahoSession session = this.createSession("joe", "ceo", "Admin", "Authenticated");
    MondrianCatalogHelper helper = (MondrianCatalogHelper)PentahoSystem.get(IMondrianCatalogService.class);
    List<MondrianCatalog> cats = helper.listCatalogs(session, false);
    Assert.assertEquals(4, cats.size());
  }

  @Test
  public void testJndiOnly() throws Exception {
    IPentahoSession session = this.createSession("joe", "ceo", "Admin", "Authenticated");
    MondrianCatalogHelper helper = (MondrianCatalogHelper)PentahoSystem.get(IMondrianCatalogService.class);
    List<MondrianCatalog> cats = helper.listCatalogs(session, true);
    Assert.assertEquals(2, cats.size());
  }

  @Test
  public void testAddCatalogNoOverwrite() throws Exception {
    IPentahoSession session = this.createSession("joe", "ceo", "Admin", "Authenticated");
    MondrianCatalogHelper helper = (MondrianCatalogHelper)PentahoSystem.get(IMondrianCatalogService.class);

    MondrianSchema schema = new MondrianSchema("SteelWheels3", null);
    MondrianDataSource ds = new MondrianDataSource(
        "Provider=Mondrian;DataSource=SteelWheels3",
        "Pentaho BI Platform Datasources",
        "http://localhost:8080/pentaho/Xmla?userid=joe&amp;password=password", 
        "Provider=mondrian;DataSource=SampleData;",
        "PentahoXMLA", 
        DataSource.PROVIDER_TYPE_MDP, 
        DataSource.AUTH_MODE_UNAUTHENTICATED, 
        null
      );

    MondrianCatalog cat = new MondrianCatalog("SteelWheels3", null,
        "solution:test/charts/steelwheels.mondrian.xml", ds, schema);

    try {
      helper.addCatalog(cat, false, session);
      Assert.fail("expected exception");
    } catch (MondrianCatalogServiceException e) {
      Assert.assertEquals(MondrianCatalogServiceException.Reason.ALREADY_EXISTS, e.getReason());
    }
    List<MondrianCatalog> cats = helper.listCatalogs(session, false);
    Assert.assertEquals(4, cats.size());
  }
  
  @Test
  public void testAddCatalogOverwrite() throws Exception {
    IPentahoSession session = this.createSession("joe", "ceo", "Admin", "Authenticated");
    MondrianCatalogHelper helper = (MondrianCatalogHelper)PentahoSystem.get(IMondrianCatalogService.class);

    MondrianSchema schema = new MondrianSchema("SteelWheels3", null);
    MondrianDataSource ds = new MondrianDataSource(
        "Provider=Mondrian;DataSource=SteelWheels3",
        "Pentaho BI Platform Datasources",
        "http://localhost:8080/pentaho/Xmla?userid=joe&amp;password=password", 
        "Provider=mondrian;DataSource=SampleData;",
        "PentahoXMLA", 
        DataSource.PROVIDER_TYPE_MDP, 
        DataSource.AUTH_MODE_UNAUTHENTICATED, 
        null
      );

    MondrianCatalog cat = new MondrianCatalog("SteelWheels3", "Provider=mondrian;DataSource=SampleDataTest",
        "solution:test/charts/steelwheels.mondrian.xml", ds, schema);

    helper.addCatalog(cat, true, session);
    
    List<MondrianCatalog> cats = helper.listCatalogs(session, false);
    Assert.assertEquals(4, cats.size());
    
    MondrianCatalog catalog = helper.getCatalog("SteelWheels3", session);
    Assert.assertEquals("Provider=mondrian;DataSource=SampleDataTest", catalog.getDataSourceInfo());
  }
  
  @Test
  public void testAddCatalogLeadingSlash() throws Exception {
    IPentahoSession session = this.createSession("joe", "ceo", "Admin", "Authenticated");
    MondrianCatalogHelper helper = (MondrianCatalogHelper)PentahoSystem.get(IMondrianCatalogService.class);

    MondrianSchema schema = new MondrianSchema("SteelWheels4", null);
    MondrianDataSource ds = new MondrianDataSource(
        "Provider=Mondrian;DataSource=SteelWheels4",
        "Pentaho BI Platform Datasources",
        "http://localhost:8080/pentaho/Xmla?userid=joe&amp;password=password", 
        "Provider=mondrian;DataSource=SampleData;",
        "PentahoXMLA", 
        DataSource.PROVIDER_TYPE_MDP, 
        DataSource.AUTH_MODE_UNAUTHENTICATED, 
        null
      );

    MondrianCatalog cat = new MondrianCatalog("SteelWheels4", "Provider=mondrian;DataSource=SampleData;",
        "solution:/test/charts/steelwheels.mondrian.xml", ds, schema);

    helper.addCatalog(cat, true, session);
    List<MondrianCatalog> cats = helper.listCatalogs(session, false);
    Assert.assertEquals(4, cats.size());
    
    MondrianCatalog catalog = helper.getCatalog("SteelWheels4", session);
    Assert.assertEquals("Provider=mondrian;DataSource=SampleData", catalog.getDataSourceInfo());
  }

}