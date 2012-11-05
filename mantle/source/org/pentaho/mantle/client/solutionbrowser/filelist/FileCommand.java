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
 *
 * Created Mar 25, 2008
 * @author Michael D'Amour
 */
package org.pentaho.mantle.client.solutionbrowser.filelist;

import org.pentaho.mantle.client.commands.DeleteFileCommand;
import org.pentaho.mantle.client.commands.FilePropertiesCommand;
import org.pentaho.mantle.client.commands.NewFolderCommand;
import org.pentaho.mantle.client.commands.ShareFileCommand;
import org.pentaho.mantle.client.solutionbrowser.IFileSummary;
import org.pentaho.mantle.client.solutionbrowser.IFileSummaryProvider;
import org.pentaho.mantle.client.solutionbrowser.SolutionBrowserPerspective;
import org.pentaho.mantle.client.solutionbrowser.fileproperties.FilePropertiesDialog;
import org.pentaho.mantle.client.solutionbrowser.scheduling.ScheduleHelper;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;

public class FileCommand implements Command {

  public static enum COMMAND {
    RUN, EDIT, DELETE, PROPERTIES, BACKGROUND, NEWWINDOW, SCHEDULE_CUSTOM, SCHEDULE_NEW, SUBSCRIBE, SHARE, EDIT_ACTION, CREATE_FOLDER
  };

  COMMAND mode = COMMAND.RUN;
  PopupPanel popupMenu;
  private IFileSummaryProvider fileSummaryProvider;
  private IFileSummary fileSummary;

  /**
   * Suitable when an {@code IFileSummary} instance is not available at construction time. 
   */
  public FileCommand(COMMAND inMode, PopupPanel popupMenu, IFileSummaryProvider fileSummaryProvider) {
    this.mode = inMode;
    this.popupMenu = popupMenu;
    this.fileSummaryProvider = fileSummaryProvider;
  }
  
  /**
   * Suitable when an {@code IFileSummary} instance is available at construction time. 
   */
  public FileCommand(COMMAND inMode, PopupPanel popupMenu, IFileSummary fileSummary) {
    this.mode = inMode;
    this.popupMenu = popupMenu;
    this.fileSummary = fileSummary;
  }

  public void execute() {
    if (popupMenu != null) {
      popupMenu.hide();
    }

    SolutionBrowserPerspective sbp = SolutionBrowserPerspective.getInstance();

    if (mode == COMMAND.RUN || mode == COMMAND.BACKGROUND || mode == COMMAND.NEWWINDOW) {
      FilesListPanel flp = sbp.getFilesListPanel();
      sbp.openFile("/" + flp.getSelectedFileItem().getSolution() + flp.getSelectedFileItem().getPath(), flp.getSelectedFileItem().getName(), flp.getSelectedFileItem().getLocalizedName(), mode);
    } else if (mode == COMMAND.PROPERTIES) {
      IFileSummary summary = fileSummary;
      if (summary == null) {
        summary = fileSummaryProvider.getFileSummary();
      }
      new FilePropertiesCommand(summary, FilePropertiesDialog.Tabs.GENERAL).execute();
    } else if (mode == COMMAND.EDIT) {
      sbp.editFile();
    } else if (mode == COMMAND.DELETE) {
      IFileSummary summary = fileSummary;
      if (summary == null) {
        summary = fileSummaryProvider.getFileSummary();
      }
      new DeleteFileCommand(summary).execute();
    } else if (mode == COMMAND.CREATE_FOLDER) {
      IFileSummary summary = fileSummary;
      new NewFolderCommand(summary).execute();
    } else if (mode == COMMAND.SCHEDULE_NEW) {
      ScheduleHelper.createSchedule(sbp.getFilesListPanel().getSelectedFileItem());
    } else if (mode == COMMAND.SHARE) {
      new ShareFileCommand().execute();
    }
  }

}