/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License, version 2 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/gpl-2.0.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * Copyright 2005 - 2008 Pentaho Corporation.  All rights reserved.
 *  
 * @created Jul 07, 2008 
 * @author rmansoor
 */
package org.pentaho.platform.engine.services.connection.datasource.dbcp;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.pentaho.platform.api.data.DatasourceServiceException;
import org.pentaho.platform.api.data.IDatasourceService;
import org.pentaho.platform.api.engine.ObjectFactoryException;
import org.pentaho.platform.api.repository.datasource.DatasourceMgmtServiceException;
import org.pentaho.platform.api.repository.datasource.IDatasource;
import org.pentaho.platform.api.repository.datasource.IDatasourceMgmtService;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.services.messages.Messages;

public class NonPooledDatasourceService extends BaseDatasourceService {
  /**
   * Since JNDI is supported different ways in different app servers, it's
   * nearly impossible to have a ubiquitous way to look up a datasource. This
   * method is intended to hide all the lookups that may be required to find a
   * jndi name.
   * 
   * @param dsName
   *            The Datasource name
   * @return DataSource if there is one bound in JNDI
   * @throws NamingException
   */
  public DataSource getDataSource(String dsName) throws DatasourceServiceException {
    DataSource dataSource = null;
    Object foundDs = null;
    if(!cacheManager.cacheEnabled(IDatasourceService.JDBC_DATASOURCE)) {
        cacheManager.addCacheRegion(IDatasourceService.JDBC_DATASOURCE);
      }
      foundDs = cacheManager.getFromRegionCache(IDatasourceService.JDBC_DATASOURCE,dsName);
    if (foundDs != null) {
      return (DataSource) foundDs;
    }
    try {
      IDatasourceMgmtService datasourceMgmtSvc = (IDatasourceMgmtService) PentahoSystem.getObjectFactory().get(IDatasourceMgmtService.class,null); 
      IDatasource datasource = datasourceMgmtSvc.getDatasource(dsName);
      if(datasource != null) {
        dataSource = convert(datasource);
        cacheManager.putInRegionCache(IDatasourceService.JDBC_DATASOURCE,dsName, (DataSource) dataSource);  
      } else {
        throw new DatasourceServiceException(Messages.getErrorString("NonPooledDatasourceService.ERROR_0002_UNABLE_TO_GET_DATASOURCE")); //$NON-NLS-1$
      }
    } catch (ObjectFactoryException objface) {
      throw new DatasourceServiceException(Messages.getErrorString("NonPooledDatasourceService.ERROR_0001_UNABLE_TO_INSTANTIATE_OBJECT"),objface); //$NON-NLS-1$
    } catch (DatasourceMgmtServiceException daoe) {
      throw new DatasourceServiceException(Messages.getErrorString("NonPooledDatasourceService.ERROR_0002_UNABLE_TO_GET_DATASOURCE"),daoe); //$NON-NLS-1$
    }
    return dataSource;
  }

  /**
   * Since JNDI is supported different ways in different app servers, it's
   * nearly impossible to have a ubiquitous way to look up a datasource. This
   * method is intended to hide all the lookups that may be required to find a
   * jndi name, and return the actual bound name.
   * 
   * @param dsName
   *            The Datasource name (like SampleData)
   * @return The bound DS name if it is bound in JNDI (like "jdbc/SampleData")
   * @throws DatasourceServiceException
   */
  public String getDSBoundName(final String dsName) throws DatasourceServiceException {
    return dsName;
  }


  /**
   * Since JNDI is supported different ways in different app servers, it's
   * nearly impossible to have a ubiquitous way to look up a datasource. This
   * method is intended to extract just the regular name of a specified JNDI source.
   * 
   * @param dsName The Datasource name (like "jdbc/SampleData")
   * @return The unbound DS name (like "SampleData")
   */
  public String getDSUnboundName(final String dsName) {
    return dsName;
  }

  private DataSource convert(IDatasource datasource) {
    BasicDataSource basicDatasource = new BasicDataSource();
    basicDatasource.setDriverClassName(datasource.getDriverClass());
    basicDatasource.setMaxActive(datasource.getMaxActConn());
    basicDatasource.setMaxIdle(datasource.getIdleConn());
    basicDatasource.setMaxWait(datasource.getWait());
    basicDatasource.setUrl(datasource.getUrl());
    basicDatasource.setUsername(datasource.getUserName());
    basicDatasource.setPassword(datasource.getPassword());
    basicDatasource.setValidationQuery(datasource.getQuery());
    return basicDatasource;
  }
}
