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
import org.pentaho.platform.api.engine.ILogger;
import org.pentaho.platform.util.web.SimpleUrlFactory;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.uifoundation.component.xml.InputFormComponent;
import org.pentaho.platform.web.http.request.HttpRequestParameterProvider;
import org.pentaho.platform.web.http.session.HttpSessionParameterProvider;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.web.http.WebTemplateHelper;
import org.pentaho.platform.util.messages.LocaleHelper;
import org.pentaho.platform.web.http.PentahoHttpSessionHelper;
import org.pentaho.platform.api.engine.IMessageFormatter;

public final class Task_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write('\r');
      out.write('\n');

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
	String baseUrl = PentahoSystem.getApplicationContext().getBaseUrl();

	IPentahoSession userSession = PentahoHttpSessionHelper.getPentahoSession( request );
	HttpRequestParameterProvider requestParameters = new HttpRequestParameterProvider( request );
	HttpSessionParameterProvider sessionParameters = new HttpSessionParameterProvider( userSession );
	String hrefUrl = baseUrl + "ViewAction?"; //$NON-NLS-1$
	String onClick = ""; //$NON-NLS-1$
	String thisUrl = baseUrl + "./Navigate?"; //$NON-NLS-1$

	String instanceId = request.getParameter( "instance-id" ); //$NON-NLS-1$
	String solution = request.getParameter( "solution" ); //$NON-NLS-1$
	String path = request.getParameter( "path" ); //$NON-NLS-1$
	String actionName = request.getParameter( "action" ); //$NON-NLS-1$
	String templateName = request.getParameter( "template" ); //$NON-NLS-1$
	String stylesheetName = request.getParameter( "css" ); //$NON-NLS-1$
	
	SimpleUrlFactory urlFactory = new SimpleUrlFactory( thisUrl );

	ArrayList messages = new ArrayList();

	InputFormComponent inputForm = new InputFormComponent( urlFactory, instanceId, templateName, stylesheetName, solution, path, actionName, messages );
	inputForm.setLoggingLevel( ILogger.DEBUG );

	inputForm.setParameterProvider( HttpRequestParameterProvider.SCOPE_REQUEST, requestParameters ); //$NON-NLS-1$
	inputForm.setParameterProvider( HttpSessionParameterProvider.SCOPE_SESSION, sessionParameters ); //$NON-NLS-1$
	String content = null;
	if( inputForm.validate( userSession, null ) ) {
		content = inputForm.getContent( "text/html" ); //$NON-NLS-1$
	}
	if( content == null ) {
		StringBuffer buffer = new StringBuffer();
		PentahoSystem.get(IMessageFormatter.class, userSession).formatErrorMessage( "text/html", "Could not create inbox task display", messages, buffer ); //$NON-NLS-1$
		content = buffer.toString();
	} else {
		out.print( content );
	}

      out.write('\r');
      out.write('\n');
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
