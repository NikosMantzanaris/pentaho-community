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
package org.pentaho.mantle.client.commands;

import java.util.List;

import org.pentaho.gwt.widgets.client.dialogs.IDialogCallback;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;
import org.pentaho.gwt.widgets.client.filechooser.FileChooser.FileChooserMode;
import org.pentaho.gwt.widgets.client.filechooser.FileChooserDialog;
import org.pentaho.gwt.widgets.client.filechooser.FileChooserListener;
import org.pentaho.gwt.widgets.client.tabs.PentahoTab;
import org.pentaho.mantle.client.MantleApplication;
import org.pentaho.mantle.client.dialogs.WaitPopup;
import org.pentaho.mantle.client.messages.Messages;
import org.pentaho.mantle.client.objects.SolutionFileInfo;
import org.pentaho.mantle.client.solutionbrowser.SolutionBrowserPerspective;
import org.pentaho.mantle.client.solutionbrowser.SolutionDocumentManager;
import org.pentaho.mantle.client.solutionbrowser.tabs.IFrameTabPanel;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Document;

public class SaveCommand extends AbstractCommand {

  boolean isSaveAs = false;

  private String name;
  private String solution;
  private String path;
  private SolutionFileInfo.Type type;
  private String tabName;

  public SaveCommand() {
  }

  public SaveCommand(boolean isSaveAs) {
    this.isSaveAs = isSaveAs;
  }

  protected void performOperation() {
    performOperation(true);
  }

  protected void performOperation(boolean feedback) {
    final SolutionBrowserPerspective navigatorPerspective = SolutionBrowserPerspective.getInstance();

    retrieveCachedValues(navigatorPerspective.getContentTabPanel().getCurrentFrame());

    SolutionDocumentManager.getInstance().fetchSolutionDocument(new AsyncCallback<Document>() {
      public void onFailure(Throwable caught) {
      }

      public void onSuccess(Document result) {
        if (isSaveAs || name == null) {
          final FileChooserDialog dialog = new FileChooserDialog(FileChooserMode.SAVE, "/", result, false, true); //$NON-NLS-1$
          dialog.setSubmitOnEnter(MantleApplication.submitOnEnter);
          if (isSaveAs) {
            dialog.setTitle(Messages.getString("saveAs")); //$NON-NLS-1$
            dialog.setText(Messages.getString("saveAs")); //$NON-NLS-1$
          } else {
            dialog.setTitle(Messages.getString("save")); //$NON-NLS-1$
          }

          if (!MantleApplication.showAdvancedFeatures) {
            dialog.setShowSearch(false);
          }
          dialog.addFileChooserListener(new FileChooserListener() {

            public void fileSelected(final String solution, final String path, final String name, String localizedFileName) {
              SaveCommand.this.solution = solution;
              SaveCommand.this.path = path;
              SaveCommand.this.name = name;
              SaveCommand.this.type = SolutionFileInfo.Type.XACTION; //$NON-NLS-1$

              tabName = name;
              if (tabName.indexOf("analysisview.xaction") != -1) {
                // trim off the analysisview.xaction from the localized-name
                tabName = tabName.substring(0, tabName.indexOf("analysisview.xaction") - 1);
              } else if (tabName.indexOf("waqr.xaction") != -1) {
                tabName = tabName.substring(0, tabName.indexOf("waqr.xaction") - 1);
              }
              
              if (doesFileExist(navigatorPerspective.getContentTabPanel().getCurrentFrameElementId(), dialog, solution, path, name)) {
                dialog.hide();
                PromptDialogBox overWriteDialog = new PromptDialogBox(Messages.getString("question"), Messages.getString("yes"), Messages.getString("no"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    false, true);
                overWriteDialog.setContent(new Label(Messages.getString("fileExistsOverwrite"), false)); //$NON-NLS-1$
                overWriteDialog.setCallback(new IDialogCallback() {
                  public void okPressed() {
                    doSaveAs(navigatorPerspective.getContentTabPanel().getCurrentFrameElementId(), name, solution, path, type, true);
                    Window.setTitle(Messages.getString("productName") + " - " + name); //$NON-NLS-1$ //$NON-NLS-2$
                  }

                  public void cancelPressed() {
                    dialog.show();
                  }
                });
                overWriteDialog.center();
              } else {
                // Default save as operation should not overwrite any existing file
                doSaveAs(navigatorPerspective.getContentTabPanel().getCurrentFrameElementId(), name, solution, path, type, false);
                Window.setTitle(Messages.getString("productName") + " - " + name); //$NON-NLS-1$ //$NON-NLS-2$
                persistFileInfoInFrame();
                clearValues();
              }
            }

            public void fileSelectionChanged(String solution, String path, String name) {
            }
            public void dialogCanceled(){

            }

          });
          dialog.center();
        } else {
          // This is a save operation, overwrite as necessary
          doSaveAs(navigatorPerspective.getContentTabPanel().getCurrentFrameElementId(), name, solution, path, type, true);
          clearValues();
        }
      }
    }, false);
  }

  /**
   * Checks if the file exists. If the name does not contain an extension we will check all possible extensions provided
   * by {@link #getPossibleExtensions()}, e.g. / + solution + path + 'name.extension'. If {@link #getPossibleExtensions(String)} returns
   * no extensions this will return {@code true} if any file in the solution path has the same simple file name (without extension).
   * 
   * @param elementId Id of the PUC tab containing the frame to look for a possible extensions callback in
   * @param dialog FileChooserDialog to query for existing files.
   * @param solution
   * @param path
   * @param name
   * @return True if the file represented by the solution, path, and name exists with any of the possible extensions
   *         (or the provided extension as part of name). 
   */
  private boolean doesFileExist(final String elementId, final FileChooserDialog dialog, final String solution, final String path, final String name) {
    final String absolutePath = "/" + solution + path; //$NON-NLS-1$
    final String absoluteFileName = absolutePath + (!"".equals(name) ? "/" : "") + name; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    // If we already have an extension check the path and return
    if (name.contains(".")) { //$NON-NLS-1$
      return dialog.doesFileExist(absoluteFileName);
    }

    final JsArrayString possibleExtensions = getPossibleExtensions(elementId);
    if (possibleExtensions != null && possibleExtensions.length() > 0) {
      for (int i = 0; i < possibleExtensions.length(); i ++) {
        String ext = possibleExtensions.get(i);
        if (ext != null && ext.length() > 0 && !ext.startsWith(".")) { //$NON-NLS-1$
          ext = "." + ext; //$NON-NLS-1$
        }
        // If we find a file with the extension 
        if (dialog.doesFileExist(absoluteFileName + ext)) {
          return true;
        }
      }
    } else {
      // No possible extensions provided. We'll consider this file to exist if any other file has the same name without extension.
      // This will prevent any possible accidental overwriting.
      List<String> fileNames = dialog.getFilesInPath(absolutePath);
      for (String fileName : fileNames) {
        final int dotIdx = fileName.indexOf("."); //$NON-NLS-1$
        if (dotIdx != -1) {
          fileName = fileName.substring(0, dotIdx);
        }
        if (fileName.equals(name)) {
          return true;
        }
      }
    }
    // No files found with any of the possible extensions
    return false;
  }
  
  /**
   * @param elementId Id of the PUC tab containing the frame to look for a possible extensions callback in
   * @return All possible extensions provided by the frame.
   */
  private native JsArrayString getPossibleExtensions(String elementId)/*-{
   var frame = $doc.getElementById(elementId);
   frame = frame.contentWindow;
   frame.focus();
   if (frame.gCtrlr && frame.gCtrlr.repositoryBrowserController && frame.gCtrlr.repositoryBrowserController.getPossibleFileExtensions) {
     return frame.gCtrlr.repositoryBrowserController.getPossibleFileExtensions();
   } else if(frame.pivot_initialized) {
     return [".analysisview.xaction"];
   }   
   return [];
  }-*/;
  
  private void persistFileInfoInFrame() {
    SolutionFileInfo fileInfo = new SolutionFileInfo();
    fileInfo.setName(this.name);
    fileInfo.setPath(this.path);
    fileInfo.setSolution(this.solution);
    fileInfo.setType(this.type);
    SolutionBrowserPerspective.getInstance().getContentTabPanel().getCurrentFrame().setFileInfo(fileInfo);
  }

  private void clearValues() {
    name = null;
    solution = null;
    path = null;
    type = null;
  }

  private void retrieveCachedValues(IFrameTabPanel tabPanel) {
    clearValues();
    SolutionFileInfo info = tabPanel.getFileInfo();
    if (info != null) {
      this.name = info.getName();
      this.path = info.getPath();
      this.solution = info.getSolution();
      this.type = info.getType();
    }
  }

  private void doSaveAs(String elementId, String filename, String solution, String path, SolutionFileInfo.Type type, boolean overwrite, boolean showBusy) {
    WaitPopup.getInstance().setVisible(true);
    this.doSaveAs(elementId, filename, solution, path, type, overwrite);
    WaitPopup.getInstance().setVisible(false);
  }

  
  /**
   * This method will call saveReportSpecAs(string filename, string solution, string path, bool overwrite)
   * 
   * @param elementId
   */
  private native void doSaveAs(String elementId, String filename, String solution, String path, SolutionFileInfo.Type type, boolean overwrite)
  /*-{
    var frame = $doc.getElementById(elementId);
    frame = frame.contentWindow;
    frame.focus();                                
                
    if(frame.pivot_initialized) {
      // do jpivot save
      var actualFileName = filename;
      if (filename.indexOf("analysisview.xaction") == -1) {
        actualFileName = filename + ".analysisview.xaction";
      } else {
        // trim off the analysisview.xaction from the localized-name
        filename = filename.substring(0, filename.indexOf("analysisview.xaction")-1);
      }
      frame.controller.saveAs(actualFileName, filename, solution, path, overwrite);
    } else if ((typeof(window[frame.handle_puc_save]) == "undefined")?  false: true) {
      try {
        frame.handle_puc_save(solution, path, name, name, overwrite);
      } catch (e) {
        //TODO: externalize message once a solution to do so is found.
        $wnd.mantle_showMessage("Error","Error encountered while saving: "+e);
      }
    } else {
      // trim off the waqr.xaction from the localized-name (waqr's save will put it back)
      if (filename.indexOf("waqr.xaction") != -1) {
        filename = filename.substring(0, filename.indexOf("waqr.xaction")-1);
      }
      try{

        // tell WAQR to save it's state based on the current page
        var saveFuncName = "savePg"+frame.gCtrlr.wiz.currPgNum;
        var func = frame.gCtrlr[saveFuncName];
        if(func != undefined && typeof func == "function"){
          frame.gCtrlr[saveFuncName]();
        } 

        // Find save type
        var saveType = "html"; 
        try{
          saveType = frame.gCtrlr.wiz.previewTypeSelect.value;
        } catch(e){
          //consume and let default go
        }

        // Perform the save
        frame.gCtrlr.repositoryBrowserController.remoteSave(filename, solution, path, saveType, overwrite);
        this.@org.pentaho.mantle.client.commands.SaveCommand::doTabRename()();
        
      } catch(e){
        //TODO: externalize message once a solution to do so is found.
        $wnd.mantle_showMessage("Error","Error encountered while saving: "+e);
      }
    }
  }-*/;

  // used via JSNI
  @SuppressWarnings("unused")
  private void doTabRename() {
    if (tabName != null) { // Save-As does not modify the name of the tab.
      PentahoTab tab = SolutionBrowserPerspective.getInstance().getContentTabPanel().getSelectedTab();
      tab.setLabelText(tabName);
      tab.setLabelTooltip(tabName);
    }
  }

}
