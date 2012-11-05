package org.pentaho.platform.engine.core.system.objfac;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * User: nbaker
 * Backport of org.springframework.context.support.SimpleThreadScope from Spring 3.0
 * Date: 11/1/11
 */
public class SimpleThreadScope implements Scope{

  private static final Log logger = LogFactory.getLog(SimpleThreadScope.class);

  private static final ThreadLocal<Map<String, Object>> threadScope =
      new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
          return new HashMap<String, Object>();
        }
      };

  public Object get(String name, ObjectFactory objectFactory) {
    Map<String, Object> scope = threadScope.get();
    Object object = scope.get(name);
    if (object == null) {
      object = objectFactory.getObject();
      scope.put(name, object);
    }
    return object;
  }

  public Object remove(String name) {
    Map<String, Object> scope = threadScope.get();
    return scope.remove(name);
  }

  public void registerDestructionCallback(String name, Runnable callback) {
    logger.warn("SimpleThreadScope does not support descruction callbacks. " +
        "Consider using a RequestScope in a Web environment.");
  }

  public Object resolveContextualObject(String key) {
    return null;
  }

  public String getConversationId() {
    return Thread.currentThread().getName();
  }

}
