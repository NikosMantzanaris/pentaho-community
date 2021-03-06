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
import org.pentaho.platform.api.engine.IMessageFormatter;
import java.util.ArrayList;
import org.pentaho.platform.util.web.SimpleUrlFactory;
import org.pentaho.platform.web.jsp.messages.Messages;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.uifoundation.chart.CategoryDatasetChartComponent;
import org.pentaho.platform.uifoundation.chart.JFreeChartEngine;
import org.pentaho.platform.web.http.request.HttpRequestParameterProvider;
import org.pentaho.platform.web.http.session.HttpSessionParameterProvider;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.web.http.WebTemplateHelper;
import org.pentaho.platform.api.engine.IUITemplater;
import org.pentaho.platform.util.messages.LocaleHelper;
import org.pentaho.commons.connection.IPentahoConnection;
import org.pentaho.commons.connection.IPentahoResultSet;
import org.pentaho.platform.engine.services.connection.PentahoConnectionFactory;
import org.pentaho.platform.util.logging.SimpleLogger;
import org.pentaho.platform.web.http.PentahoHttpSessionHelper;

public final class Chart_jsp extends org.apache.jasper.runtime.HttpJspBase
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
	int chartType = (int)requestParameters.getLongParameter("ChartType", JFreeChartEngine.UNDEFINED_CHART_TYPE); //$NON-NLS-1$
	String chartDefinitionPath = requestParameters.getStringParameter("ChartDefinitionPath", null); //$NON-NLS-1$
	
	String thisUrl = baseUrl + "Chart?"; //$NON-NLS-1$
	String intro = ""; //$NON-NLS-1$
	String footer = ""; //$NON-NLS-1$
	String content = ""; //$NON-NLS-1$

	SimpleUrlFactory urlFactory = new SimpleUrlFactory( thisUrl );
	ArrayList messages = new ArrayList();
	CategoryDatasetChartComponent barChart = new CategoryDatasetChartComponent( chartType, chartDefinitionPath, 600, 400, urlFactory, messages );

    IPentahoConnection connection = PentahoConnectionFactory.getConnection(IPentahoConnection.SQL_DATASOURCE, "SampleData", userSession, userSession); //$NON-NLS-1$
    try {
	    String query = "select department, actual, budget, variance from QUADRANT_ACTUALS"; //$NON-NLS-1$
	
	    IPentahoResultSet results = connection.executeQuery(query);
	    try {
		    
		    barChart.setValues(results);
			barChart.validate( userSession, null );
			
			barChart.setParameterProvider( HttpRequestParameterProvider.SCOPE_REQUEST, requestParameters ); //$NON-NLS-1$
			barChart.setParameterProvider( HttpSessionParameterProvider.SCOPE_SESSION, sessionParameters ); //$NON-NLS-1$
			
			content = barChart.getContent( "text/html" ); //$NON-NLS-1$
			if( content == null ) {
				StringBuffer buffer = new StringBuffer();		
				PentahoSystem.get(IMessageFormatter.class, userSession).formatErrorMessage( "text/html", Messages.getErrorString( "CHART.DISPLAY_ERROR" ), messages, buffer ); //$NON-NLS-1$ //$NON-NLS-2$
				content = buffer.toString();
			}
		
			IUITemplater templater = PentahoSystem.get(IUITemplater.class, userSession );
			if( templater != null ) {
				String sections[] = templater.breakTemplate( "template-document.html", Messages.getString( "CHART.USER_SAMPLES" ), userSession ); //$NON-NLS-1$ //$NON-NLS-2$
				if( sections != null && sections.length > 0 ) {
					intro = sections[0];
				}
				if( sections != null && sections.length > 1 ) {
					footer = sections[1];
				}
			} else {
				intro = Messages.getString( "UI.ERROR_0002_BAD_TEMPLATE_OBJECT" ); //$NON-NLS-1$
			}
	    } finally {
	    	results.close();
	    }
    } finally {
    	connection.close();
    }

      out.print( intro );
      out.write('\r');
      out.write('\n');
      out.print( content );
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
