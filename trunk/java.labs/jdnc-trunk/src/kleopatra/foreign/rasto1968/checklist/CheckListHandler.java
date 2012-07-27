/*
 * $Id: CheckListHandler.java 3312 2010-11-03 10:55:40Z kleopatra $
 *
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package rasto1968.checklist;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.ListModel;

import org.jdesktop.swingx.JXList;

/**
 * {@code CheckListHandler} is a pseudo-UI-delegate that provides mechanisms for checking,
 * unchecking, and toggling check mark selection states. It can be installed on all {@code JXList}s
 * configured as check lists.
 */
public final class CheckListHandler extends MouseAdapter implements ActionListener {
    private static final CheckListHandler INSTANCE = new CheckListHandler();
    
    private static Logger logger = Logger.getLogger(CheckListHandler.class.getName());
    
    private CheckListHandler() {
        //does nothing
    }

    /**
     * Retrieves the {@code CheckListHandler}.
     * 
     * @return the handler for check lists
     */
    public static CheckListHandler getInstance() {
        return INSTANCE;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // JW: actionListers must cope with null event
        // just bail out, can't do anything
        if (e == null) return;
        Object source = e.getSource();
        
        if (source instanceof JXList) {
            JXList list = (JXList) source;
            ListModel lm = list.getModel();
            
            if (lm instanceof CheckListModel) {
                CheckListModel checkListModel = (CheckListModel) lm;
                
                for (int index : list.getSelectedIndices()) {
                    checkListModel.toggleChecked(list.convertIndexToModel(index));
                }
            } else {
                logger.warning("invalid model for JXList: " + list);
            }
        } else {
            logger.warning("invalid source: " + source);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Object source = e.getSource();
        
        if (source instanceof JXList) {
            JXList list = (JXList) source;
            Point pt = e.getPoint();
            int index = list.locationToIndex(pt);
            
            if (index < 0) {
                return;
            }
            
            // PENDING JW: check if coordinate system okay?
            // index = view coordinate
            ListModel lm = list.getModel();
            Component c = list.getCellRenderer().getListCellRendererComponent(list,
                    // here the model is messaged with the view coordinate
                    // better use list.getElementAt
                    lm.getElementAt(index), index, list.isSelectedIndex(index),
                    list.hasFocus() && (list.getLeadSelectionIndex() == index));
            
            //if the correct renderer is not installed, just toggle
            boolean toggle = true;
            
            if (c instanceof HotSpotAware) {
                pt.y -= list.getCellBounds(index, index).y;
                toggle = ((HotSpotAware) c).isHotSpot(pt);
            }
            
            if (!toggle) {
                return;
            }
            
            if (lm instanceof CheckListModel) {
                CheckListModel checkListModel = (CheckListModel) lm;
                // correct conversion 
                checkListModel.toggleChecked(list.convertIndexToModel(index));
                
                e.consume();
            } else {
                logger.warning("invalid model for JXList: " + list);
            }
        } else {
            logger.warning("invalid source: " + source);
        }
    }
}
