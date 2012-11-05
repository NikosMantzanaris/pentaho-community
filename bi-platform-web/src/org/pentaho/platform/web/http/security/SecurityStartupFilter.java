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
 * Copyright 2006 - 2009 Pentaho Corporation.  All rights reserved.
 *
 * Created Jan 18, 2006
 * @author mbatchel
 */
package org.pentaho.platform.web.http.security;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.engine.core.solution.PentahoSessionParameterProvider;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.security.SecurityHelper;
import org.pentaho.platform.web.http.PentahoHttpSessionHelper;
import org.springframework.security.context.SecurityContextHolder;

/**
 * Filter that does the following:
 * 
 * <ol>
 *   <li>Synchronize Pentaho session with Spring Security's authenticated user.</li>
 *   <li>Run session startup actions when a user has just logged in. Note that this happens on the request 
 *   <em>after</em> the login form submit request.</li>
 * </ol>
 */
public class SecurityStartupFilter implements Filter {
  private static final Log logger = LogFactory.getLog(SecurityStartupFilter.class);

  /**
   * Lazily initialized since PentahoSystem isn't available when this class is constructed.
   */
  public static String anonymousUser;
  
  private boolean injectAnonymous = true;
  
  public void destroy() {

  }

  public void init(final FilterConfig filterConfig) throws ServletException {
  }

  public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    IPentahoSession userSession = getPentahoSession(request);
    String user = request.getRemoteUser();
    if (user != null) {
      // User is authenticated. Check session to see if the users' startup
      // actions have already been done.
      boolean wasAnonymous = false;
      if (!userSession.isAuthenticated() || isAnonymous(userSession)) {
        if (isAnonymous(userSession)) {
          wasAnonymous = true;
        }
        // the user was not logged in before but is now or user was anonymous....
        userSession.setAuthenticated(user);
      }
      Principal principal = SecurityHelper.getPrincipal(userSession);
      if (principal == null || wasAnonymous) {
        // principal = request.getUserPrincipal();
        principal = SecurityContextHolder.getContext().getAuthentication();
        if (SecurityStartupFilter.logger.isDebugEnabled()) {
          SecurityStartupFilter.logger.debug(principal);
        }
        SecurityHelper.setPrincipal(principal, userSession);
        try {
          // Do the startup actions...
          
          IParameterProvider sessionParameters = new PentahoSessionParameterProvider(userSession);
          PentahoSystem.sessionStartup(userSession, sessionParameters);
        } catch (Exception ex) {
          SecurityStartupFilter.logger.error(ex.getLocalizedMessage(), ex);
          // Yes, keep going, in spite of the error.
        }
      }
      filterChain.doFilter(request, servletResponse);
    } else {
      if (injectAnonymous) {
        userSession.setAuthenticated(getAnonymousUser());
      }
      filterChain.doFilter(request, servletResponse);
    }

  }

  protected IPentahoSession getPentahoSession(final HttpServletRequest request) {
    return PentahoHttpSessionHelper.getPentahoSession(request);
  }

  public void setInjectAnonymous(final boolean injectAnonymous) {
    this.injectAnonymous = injectAnonymous;
  }

  protected String getAnonymousUser() {
    if (anonymousUser == null) {
      anonymousUser = PentahoSystem.getSystemSetting("anonymous-authentication/anonymous-user", "anonymousUser"); //$NON-NLS-1$//$NON-NLS-2$
    }
    return anonymousUser;
  }
  
  protected boolean isAnonymous(final IPentahoSession pentahoSession) {
    return getAnonymousUser().equals(pentahoSession.getName()); 
  }

}
