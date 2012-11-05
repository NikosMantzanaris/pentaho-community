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
 * Copyright 2012 Pentaho Corporation.  All rights reserved.
 *
*/
package org.pentaho.platform.web.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.api.engine.IPluginManager;
import org.pentaho.platform.engine.core.system.PentahoSystem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * User: nbaker
 * Date: 3/13/12
 */
public class RequireJSServlet extends ServletBase{

  @Override
  public Log getLogger() {
    return LogFactory.getLog(getClass());
  }


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    handleRequest(req, resp);
  }

  public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

    resp.setContentType("text/javascript"); //$NON-NLS-1$

    StringBuilder output = new StringBuilder();
        output.append("pen.require.config({\n")
          .append("    waitSeconds: 30,\n")
          .append("    paths: {\n");

    IPluginManager manager = PentahoSystem.get(IPluginManager.class);
    List<String> plugins = manager.getRegisteredPlugins();
    boolean first = true;
    for (String plugin : plugins) {
      String amdRoot = (String) manager.getPluginSetting(plugin, "amd-root-path", null);
      String amdRootName = (String) manager.getPluginSetting(plugin, "amd-root-namespace", null);

      String[] amdRootSplit = amdRoot == null ? null : amdRoot.split(",");
      String[] amdRootNameSplit = amdRootName == null ? null : amdRootName.split(",");
      if (amdRootSplit != null && amdRootNameSplit != null) {
        if (amdRootSplit.length != amdRootNameSplit.length) {
          throw new IOException("Length mismatch: amd-root-path & amd-root-namespace properties should have the same number of declarations");
        }
        for (int i = 0; i < amdRootSplit.length; i ++) {
          if(!first){
            output.append(",\n");
          } else {
            first = false;
          }
          appendAmdPath(output, req.getContextPath(), amdRootSplit[i], amdRootNameSplit[i]);
        }
      }
    }
    output.append(
    "  }\n" +
    " }\n);");
    resp.getOutputStream().print(output.toString());
  }

  private void appendAmdPath(StringBuilder output, String contextPath, String amdRoot, String amdRootName) {
    if(amdRoot != null && amdRootName != null){
      output.append("'"+amdRootName.trim()+ "' : '"+contextPath+"/"+amdRoot.trim()+"'");
    }
  }


}
