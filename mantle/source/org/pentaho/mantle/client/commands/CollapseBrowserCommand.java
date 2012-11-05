package org.pentaho.mantle.client.commands;

import org.pentaho.mantle.client.service.EmptyCallback;
import org.pentaho.mantle.client.service.MantleServiceCache;
import org.pentaho.mantle.client.solutionbrowser.SolutionBrowserPerspective;

/**
 * User: nbaker
 * Date: 3/17/12
 */
public class CollapseBrowserCommand extends AbstractCommand{
  public CollapseBrowserCommand() {
  }

  protected void performOperation() {
    performOperation(false);
  }

  protected void performOperation(boolean feedback) {
    final SolutionBrowserPerspective solutionBrowserPerspective = SolutionBrowserPerspective.getInstance();
    solutionBrowserPerspective.setNavigatorShowing(false);
    MantleServiceCache.getService().setShowNavigator(false, EmptyCallback.getInstance());
  }
}
