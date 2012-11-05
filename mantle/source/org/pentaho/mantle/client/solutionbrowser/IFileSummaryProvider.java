package org.pentaho.mantle.client.solutionbrowser;

/**
 * Provides the currently selected file as an {@link IFileSummary}.
 * 
 * @author mlowery
 */
public interface IFileSummaryProvider {
  IFileSummary getFileSummary();
}
