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
 * Copyright 2005 - 2008 Pentaho Corporation.  All rights reserved.
 *
 * @created Jul 19, 2005
 * @author James Dixon
 *
 */
package org.pentaho.mantle.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.pentaho.mantle.client.messages.Messages;
import org.pentaho.platform.util.messages.LocaleHelper;
import org.pentaho.platform.util.messages.MessageUtil;

public class ServerMessages {
  private static final String BUNDLE_NAME = "org.pentaho.mantle.public.messages.mantleMessages"; //$NON-NLS-1$

  private static final Map locales = Collections.synchronizedMap(new HashMap());

  protected static Map getLocales() {
    return ServerMessages.locales;
  }

  private static ResourceBundle getBundle() {
    Locale locale = LocaleHelper.getLocale();
    ResourceBundle bundle = (ResourceBundle) ServerMessages.locales.get(locale);
    if (bundle == null) {
      bundle = ResourceBundle.getBundle(ServerMessages.BUNDLE_NAME, locale);
      ServerMessages.locales.put(locale, bundle);
    }
    return bundle;
  }

  public static String getEncodedString(final String rawValue) {
    if (rawValue == null) {
      return (""); //$NON-NLS-1$
    }

    StringBuffer value = new StringBuffer();
    for (int n = 0; n < rawValue.length(); n++) {
      int charValue = rawValue.charAt(n);
      if (charValue >= 0x80) {
        value.append("&#x"); //$NON-NLS-1$
        value.append(Integer.toString(charValue, 0x10));
        value.append(";"); //$NON-NLS-1$
      } else {
        value.append((char) charValue);
      }
    }
    return value.toString();

  }

  public static String getXslString(final String key) {
    String rawValue = ServerMessages.getString(key);
    return ServerMessages.getEncodedString(rawValue);
  }

  public static String getString(final String key) {
    try {
      return ServerMessages.getBundle().getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }

  public static String getString(final String key, final String param1) {
    return MessageUtil.getString(ServerMessages.getBundle(), key, param1);
  }

  public static String getString(final String key, final String param1, final String param2) {
    return MessageUtil.getString(ServerMessages.getBundle(), key, param1, param2);
  }

  public static String getString(final String key, final String param1, final String param2, final String param3) {
    return MessageUtil.getString(ServerMessages.getBundle(), key, param1, param2, param3);
  }

  public static String getString(final String key, final String param1, final String param2, final String param3,
      final String param4) {
    return MessageUtil.getString(ServerMessages.getBundle(), key, param1, param2, param3, param4);
  }

  public static String getErrorString(final String key) {
    return MessageUtil.formatErrorMessage(key, ServerMessages.getString(key));
  }

  public static String getErrorString(final String key, final String param1) {
    return MessageUtil.getErrorString(ServerMessages.getBundle(), key, param1);
  }

  public static String getErrorString(final String key, final String param1, final String param2) {
    return MessageUtil.getErrorString(ServerMessages.getBundle(), key, param1, param2);
  }

  public static String getErrorString(final String key, final String param1, final String param2, final String param3) {
    return MessageUtil.getErrorString(ServerMessages.getBundle(), key, param1, param2, param3);
  }

}