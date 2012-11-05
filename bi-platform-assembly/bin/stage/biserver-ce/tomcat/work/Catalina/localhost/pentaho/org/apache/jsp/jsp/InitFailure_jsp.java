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
import org.pentaho.platform.util.messages.LocaleHelper;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.web.jsp.messages.Messages;
import java.util.List;

public final class InitFailure_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html;");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;


	response.setCharacterEncoding(LocaleHelper.getSystemEncoding());
    response.setHeader("Pragma", "no-cache"); // Set standard HTTP/1.0 no-cache header.
    response.setHeader("Cache-Control", "no-store, no-cache, private, must-revalidate, max-stale=0" );
    response.setHeader("Expires", "0");
  	List initializationErrorMessages = PentahoSystem.getInitializationFailureMessages();

      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("  <title>Error Initializing Pentaho</title>\r\n");
      out.write("</head>\r\n");
      out.write("<body bgcolor=\"white\" dir=\"");
      out.print( LocaleHelper.getTextDirection() );
      out.write("\">\r\n");
      out.write("\r\n");
      out.write("  <h2>Pentaho Initialization Exception</h2>\r\n");
      out.write("  <br />\r\n");
      out.write("  <div style='border:2px solid #cccccc'>\r\n");
      out.write("    <table width='100%' border='0'>\r\n");
      out.write("      <tr><td><b>");
      out.print(Messages.getString("InitFailure.USER_ERRORS_DETECTED"));
      out.write("</b></td></tr>\r\n");

  for (int i=0; i<initializationErrorMessages.size(); i++) {

      out.write("\r\n");
      out.write("    <tr><td>");
      out.print(initializationErrorMessages.get(i));
      out.write("</td></tr>\r\n");

  } // end for loop

      out.write("\r\n");
      out.write("    </table>\r\n");
      out.write("    <br />\r\n");
      out.write("      ");
      out.print( Messages.getString("InitFailure.USER_SEE_SERVER_CONSOLE") );
      out.write("\r\n");
      out.write("  </div>\r\n");
      out.write(" </body>\r\n");
      out.write("</html>\r\n");
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
