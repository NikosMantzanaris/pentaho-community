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
 *
 * @created Aug 3, 2005 
 * @author James Dixon
 */

package org.pentaho.platform.web.portal;

import javax.portlet.PortletURL;

import org.pentaho.platform.api.engine.IPentahoUrl;

public class PortletUrl implements IPentahoUrl {

  private PortletURL portletURL;

  public PortletUrl(final PortletURL portletURL) {
    this.portletURL = portletURL;
  }

  public void setParameter(final String name, final String value) {
    portletURL.setParameter(name, value);
  }

  public String getUrl() {
    String url = portletURL.toString();
    if (url.indexOf('?') == -1) {
      return url + '?';
    } else {
      return url;
    }
  }

}
