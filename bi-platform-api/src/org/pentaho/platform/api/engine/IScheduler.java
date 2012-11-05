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
 * Copyright 2008 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.platform.api.engine;

import org.pentaho.platform.api.repository.ISubscription;

/**
 * The IScheduler implementation should be able to take an ISubscription and
 * add it to it's internal scheduling mechanism to cause a trigger in the platform
 * 
 * @deprecated
 * @author dmoran
 *
 */

// TODO: Add more scheduler method signatures here
@Deprecated
public interface IScheduler {

  /**
   * @param subscription
   * @return true if subscription was scheduled properly
   */
  public boolean scheduleSubscription(ISubscription subscription);

}
