/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.32
 * Generated at: 2012-11-01 17:49:14 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.web.jsp.messages.Messages;
import org.pentaho.platform.api.engine.IUITemplater;
import org.pentaho.platform.web.http.WebTemplateHelper;
import org.pentaho.platform.util.messages.LocaleHelper;
import org.pentaho.platform.api.util.IVersionHelper;
import org.pentaho.platform.util.web.SimpleUrlFactory;
import org.pentaho.platform.engine.core.solution.SimpleParameterProvider;
import org.pentaho.platform.engine.services.actionsequence.ActionResource;
import org.pentaho.actionsequence.dom.IActionResource;
import org.pentaho.platform.web.http.PentahoHttpSessionHelper;

public final class About_jsp extends org.apache.jasper.runtime.HttpJspBase
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
 *
 * @created Jul 23, 2005 
 * @author James Dixon
 * 
 */
 
	response.setCharacterEncoding(LocaleHelper.getSystemEncoding());
 	String baseUrl = PentahoSystem.getApplicationContext().getBaseUrl();
 
	String path = request.getContextPath();

	IPentahoSession userSession = PentahoHttpSessionHelper.getPentahoSession( request );

	StringBuffer sb = new StringBuffer();
  IVersionHelper versionHelper = PentahoSystem.get(IVersionHelper.class, null);
	String header = Messages.getString( "UI.USER_ABOUT_TITLE", versionHelper.getVersionInformation(PentahoSystem.class) );

	String intro = "";
	String footer = "";
	IUITemplater templater = PentahoSystem.get(IUITemplater.class, userSession );
	if( templater != null ) {

		// Load a template for this web page
		String template = null;
  		try {
  		  String templateName = request.getParameter("template"); //$NON-NLS-1$
  		  if (templateName == null) {
  		    templateName = "system/custom/template-dialog.html"; //$NON-NLS-1$
  		  }
	   		ActionResource resource = new ActionResource( "", IActionResource.SOLUTION_FILE_RESOURCE, "text/xml", templateName ); //$NON-NLS-1$ //$NON-NLS-2$
    			template = PentahoSystem.get(ISolutionRepository.class, userSession).getResourceAsString( resource, ISolutionRepository.ACTION_EXECUTE );
    		} catch (Throwable t) {
    		  // TODO we need to do something here, like log at the very least!
    		  // catching Throwable is likely not optimal either.
    		}

		// Break the template into header and footer sections
		String sections[] = templater.breakTemplateString( template, header, userSession ); //$NON-NLS-1$ //$NON-NLS-2$
		if( sections != null && sections.length > 0 ) {
			intro = sections[0];
		}
		if( sections != null && sections.length > 1 ) {
			footer = sections[1];
		}
	} else {
		intro = Messages.getString( "UI.ERROR_0002_BAD_TEMPLATE_OBJECT" );
	}


      out.write('\n');
      out.write('\n');
      out.print( intro );
      out.write("\n");
      out.write("\n");
      out.write("<table class='content_table' border='0' cellpadding='0' cellspacing='0'\n");
      out.write("\theight='100%'>\n");
      out.write("\t<tr>\n");
      out.write("\t\t<td height='100%' class='contentcell_half_left'>\n");
      out.write("\t\t");

				String copyright = Messages.getString( "UI.USER_COPYRIGHT" );
				String aboutText = Messages.getString( "UI.USER_ABOUT_TEXT", copyright );
				
      out.write(' ');
      out.print( aboutText );
      out.write(" <a href='javascript:void(0);'\n");
      out.write("\t\t\tonclick='javascript:window.open( \"http://community.pentaho.org/contributors/\" );'>");
      out.print(Messages.getString( "UI.USER_SPECIAL_THANKS" ));
      out.write("</a>\n");
      out.write("\t\t</td>\n");
      out.write("\t</tr>\n");
      out.write("</table>\n");
      out.write("\n");
      out.print( footer );
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
