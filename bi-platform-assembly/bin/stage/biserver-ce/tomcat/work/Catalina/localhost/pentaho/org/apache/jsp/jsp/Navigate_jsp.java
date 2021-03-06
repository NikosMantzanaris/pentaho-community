/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.32
 * Generated at: 2012-11-01 17:49:15 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.ArrayList;
import java.util.Locale;
import org.pentaho.platform.util.web.SimpleUrlFactory;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.web.http.request.HttpRequestParameterProvider;
import org.pentaho.platform.web.http.session.HttpSessionParameterProvider;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.web.jsp.messages.Messages;
import org.pentaho.platform.web.http.WebTemplateHelper;
import org.pentaho.platform.api.engine.IUITemplater;
import org.pentaho.platform.util.VersionHelper;
import org.pentaho.platform.util.messages.LocaleHelper;
import org.pentaho.platform.api.ui.INavigationComponent;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.web.http.PentahoHttpSessionHelper;
import org.pentaho.platform.api.engine.IMessageFormatter;

public final class Navigate_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/

	response.setCharacterEncoding(LocaleHelper.getSystemEncoding());
	HttpSession httpSession = request.getSession();

	boolean doSplash = httpSession.getAttribute( "dopentahosplash" ) == null || "true".equals( request.getParameter("splash") );
	doSplash = false;
	httpSession.setAttribute( "dopentahosplash", "false" );
	 
	String baseUrl = PentahoSystem.getApplicationContext().getBaseUrl();

	IPentahoSession userSession = PentahoHttpSessionHelper.getPentahoSession( request );
		
	HttpRequestParameterProvider requestParameters = new HttpRequestParameterProvider( request );
	HttpSessionParameterProvider sessionParameters = new HttpSessionParameterProvider( userSession );
	String hrefUrl = baseUrl; //$NON-NLS-1$
	String onClick = ""; //$NON-NLS-1$
	String thisUrl = baseUrl + "Navigate?"; //$NON-NLS-1$

	SimpleUrlFactory urlFactory = new SimpleUrlFactory( thisUrl );
	ArrayList messages = new ArrayList();

	String solution = request.getParameter( "solution" ); //$NON-NLS-1$
	if( "".equals( solution ) ) { //$NON-NLS-1$
		solution = null;
	}
	boolean allowBackNavigation = solution != null;

	INavigationComponent navigate = PentahoSystem.get(INavigationComponent.class, userSession);
	navigate.setHrefUrl(hrefUrl);
	navigate.setOnClick(onClick);
	navigate.setSolutionParamName("solution");
	navigate.setPathParamName("path");
	navigate.setAllowNavigation(new Boolean(allowBackNavigation));
	navigate.setOptions("");
	navigate.setUrlFactory(urlFactory);
	navigate.setMessages(messages);
	// This line will override the default setting of the navigate component
	// to allow debugging of the generated HTML.
	// navigate.setLoggingLevel( org.pentaho.platform.api.engine.ILogger.DEBUG );
	navigate.validate( userSession, null );
	navigate.setParameterProvider( HttpRequestParameterProvider.SCOPE_REQUEST, requestParameters ); //$NON-NLS-1$
	navigate.setParameterProvider( HttpSessionParameterProvider.SCOPE_SESSION, sessionParameters ); //$NON-NLS-1$
	
	String xsl = null;
	String view = request.getParameter("view");//$NON-NLS-1$
	if( view != null ) {
		if( "default".equals( view ) ) { //$NON-NLS-1$
			userSession.removeAttribute( "pentaho-ui-folder-style" ); //$NON-NLS-1$
		} else {
			userSession.setAttribute( "pentaho-ui-folder-style", view );
			navigate.setXsl( "text/html", view ); //$NON-NLS-1$
		}
	} else {
		view = (String) userSession.getAttribute( "pentaho-ui-folder-style" );
		if( view != null ) {
			navigate.setXsl( "text/html", view ); //$NON-NLS-1$
		}
	}
	
	String content = navigate.getContent( "text/html" ); //$NON-NLS-1$
	if( content == null ) {
		StringBuffer buffer = new StringBuffer();
		PentahoSystem.get(IMessageFormatter.class, userSession).formatErrorMessage( "text/html", Messages.getErrorString( "NAVIGATE.ERROR_0001_NAVIGATE_ERROR" ), messages, buffer ); //$NON-NLS-1$ //$NON-NLS-2$
		content = buffer.toString();
	}

	String intro = "";
	String footer = "";
	IUITemplater templater = PentahoSystem.get(IUITemplater.class, userSession );
	if( templater != null ) {
		String sections[] = templater.breakTemplate( "template.html", "", userSession ); //$NON-NLS-1$ //$NON-NLS-2$
		if( sections != null && sections.length > 0 ) {
			intro = sections[0];
		}
		if( sections != null && sections.length > 1 ) {
			footer = sections[1];
		}
	} else {
		intro = Messages.getString( "UI.ERROR_0002_BAD_TEMPLATE_OBJECT" );
	}
      out.print( intro );
      out.write('\r');
      out.write('\n');
      out.print( content );
      out.write('\r');
      out.write('\n');
      out.print( footer );
      out.write("\r\n");
      out.write("\r\n");
 if( doSplash ) { 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<div id=\"splash\" style=\"width:100%;height:340px;position:absolute;top:150px;z-index:10;display:block\">\r\n");
      out.write("  <center>\r\n");
      out.write("  <div style=\"width:480px;height:320px;background-color:white;background-image: url(/pentaho-style/splash.png);border:1px solid #FF6113\">\r\n");
      out.write("    <img src=\"/pentaho-style/active/logo.png\" border=\"0\"/>\r\n");
      out.write("    <p/>\r\n");
      out.write("    <table width=\"470\">\r\n");
      out.write("      <tr>\r\n");
      out.write("        <td>\r\n");
      out.write("    \r\n");
      out.write("          <span style=\"font-weight:bold\">Featuring:</span>\r\n");
      out.write("\t \t  <p/>Business Intelligence Platform - by Pentaho\r\n");
      out.write("\t\t  <br/>HTML, PDF, XLS reporting - by JFreeReport\r\n");
      out.write("\t\t  <br/>Dashboards - By Pentaho\r\n");
      out.write("\t\t  <br/>OLAP Database Server - by Mondrian\r\n");
      out.write("\t\t  <br/>Slice and Dice Analysis - by JPivot\r\n");
      out.write("\t\t  <br/>Workflow Engine - By Enhydra Shark\r\n");
      out.write("\t\t  <br/>Scheduling - By Quartz\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t  </tr>\r\n");
      out.write("\t</table>\r\n");
      out.write("  </div>\r\n");
      out.write("  </center>\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("\t<script type=\"text/javascript\">\r\n");
      out.write("\t\tsetTimeout( \"hideSplash()\", 4500 );\r\n");
      out.write("\t\tfunction hideSplash() {\r\n");
      out.write("\t\t\tdocument.getElementById(\"splash\").style.display=\"none\";\r\n");
      out.write("\t\t}\r\n");
      out.write("\t</script>\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
