package org.pentaho.test.platform.web.ui;

import com.mockrunner.mock.web.MockHttpSession;
import org.junit.BeforeClass;
import org.pentaho.platform.api.engine.*;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.engine.core.system.StandaloneSession;
import org.pentaho.platform.engine.security.SecurityHelper;
import org.pentaho.platform.engine.services.solution.SolutionEngine;
import org.pentaho.platform.plugin.services.pluginmgr.DefaultPluginManager;
import org.pentaho.platform.plugin.services.pluginmgr.SystemPathXmlPluginProvider;
import org.pentaho.platform.plugin.services.pluginmgr.servicemgr.DefaultServiceManager;
import org.pentaho.platform.repository.solution.filebased.FileBasedSolutionRepository;
import org.pentaho.platform.web.servlet.ThemeServlet;
import org.pentaho.test.platform.engine.core.BaseTest;
import org.pentaho.test.platform.engine.core.MicroPlatform;
import org.pentaho.test.platform.web.doubles.PentahoSessionDouble;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

/**
 * User: nbaker
 * Date: 5/24/11
 */
public class TestThemeResource {

  private MicroPlatform microPlatform;
  private StandaloneSession session;
  private DefaultPluginManager pluginManager;

  @BeforeClass
  public void setup(){

    microPlatform = new MicroPlatform("plugin-mgr/test-res/PluginManagerTest/");
    microPlatform.define(ISolutionEngine.class, SolutionEngine.class);
    microPlatform.define(ISolutionRepository.class, FileBasedSolutionRepository.class);
    microPlatform.define(IPluginProvider.class, SystemPathXmlPluginProvider.class);
    microPlatform.define(IServiceManager.class, DefaultServiceManager.class, IPentahoDefinableObjectFactory.Scope.GLOBAL);

    session = new StandaloneSession();
    pluginManager = new DefaultPluginManager();

  }

  public void testThemes() throws Exception {

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpSession httpSession = new MockHttpSession();
    httpSession.setAttribute("pentaho-session", session); //$NON-NLS-1$
    request.setSession(httpSession);
    MockHttpServletResponse response = new MockHttpServletResponse();
    ThemeServlet themeServlet = new ThemeServlet();
    themeServlet.handleRequest(request, response);

    response.getWriter().flush();
    String responseContent = response.getContentAsString();
    
  }
}
