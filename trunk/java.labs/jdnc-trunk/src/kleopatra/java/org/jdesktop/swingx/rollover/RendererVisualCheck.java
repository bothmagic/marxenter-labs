/*
 * $Id: RendererVisualCheck.java 2465 2008-06-17 09:48:48Z kleopatra $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
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
 *
 */
package org.jdesktop.swingx.rollover;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.incubatorutil.AncientSwingTeam;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.renderer.BooleanValue;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.LabelProvider;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.table.ColumnControlButton;




/**
 * Variants of "live" rollover.
 * 
 * @author Jeanette Winzenburg
 */
public class RendererVisualCheck extends InteractiveTestCase {
    public static void main(String[] args) {
//        setSystemLF(true);
        RendererVisualCheck test = new RendererVisualCheck();
        try {
            test.runInteractiveTests();
//          test.runInteractiveTests(".*Tree.*");
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }
    }

    /**
     * Use a custom button controller to show both checkbox icon and text to
     * render Actions in a JXList.
     */
    public void interactiveTableWithListColumnControl() {
        TableModel model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        JXList list = new JXList();
        // quick-fill and hook to table columns' visibility state
        configureList(list, table, false);
        // a custom rendering button controller showing both checkbox and text
        // a custom rendering button controller showing both checkbox and text
        StringValue sv = new StringValue() {

            public String getString(Object value) {
                if (value instanceof AbstractActionExt) {
                    return ((AbstractActionExt) value).getName();
                }
                return "";
            }
            
        };
        BooleanValue bv = new BooleanValue() {

            public boolean getBoolean(Object value) {
                if (value instanceof AbstractActionExt) {
                    return ((AbstractActionExt) value).isSelected();
                }
                return false;
            }
            
        };
        ComponentProvider wrapper = new CheckBoxProvider(new MappedValue(sv, null, bv), JLabel.LEADING);
        list.setCellRenderer(new DefaultListRenderer(wrapper));
        JXFrame frame = showWithScrollingInFrame(table, list,
                "checkbox list-renderer");
        addStatusMessage(frame, "fake editable list: space/doubleclick on selected item toggles column visibility");
        frame.pack();
    }

    /**
     * Fills the list with a collection of actions (as returned from the 
     * table's column control). Binds space and double-click to toggle
     * the action's selected state.
     * 
     * note: this is just an example to show-off the button renderer in a list!
     * ... it's very dirty!!
     * 
     * @param list
     * @param table
     */
    @SuppressWarnings("all")
    private void configureList(final JXList list, final JXTable table, boolean useRollover) {
        final List<Action> actions = new ArrayList<Action>();
        ColumnControlButton columnControl = new ColumnControlButton(table, null) {

            @Override
            protected void addVisibilityActionItems() {
                actions.addAll(Collections
                        .unmodifiableList(getColumnVisibilityActions()));
            }

        };
        list.setModel(createListeningListModel(actions));
        // action toggling selected state of selected list item
        final Action toggleSelected = new AbstractActionExt(
                "toggle column visibility") {

            public void actionPerformed(ActionEvent e) {
                if (list.isSelectionEmpty())
                    return;
                AbstractActionExt selectedItem = (AbstractActionExt) list
                        .getSelectedValue();
                selectedItem.setSelected(!selectedItem.isSelected());
            }

        };
        if (useRollover) {
            list.setRolloverEnabled(true);
        } else {
            // bind action to space
            list.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),
                    "toggleSelectedActionState");
        }
        list.getActionMap().put("toggleSelectedActionState", toggleSelected);
        // bind action to double-click
        MouseAdapter adapter = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    toggleSelected.actionPerformed(null);
                }
            }

        };
        list.addMouseListener(adapter);

    }
    /**
     * Creates and returns a ListModel containing the given actions. 
     * Registers a PropertyChangeListener with each action to get
     * notified and fire ListEvents.
     * 
     * @param actions the actions to add into the model.
     * @return the filled model.
     */
    private ListModel createListeningListModel(final List<Action> actions) {
        final DefaultListModel model = new DefaultListModel() {

            DefaultListModel reallyThis = this;
            @Override
            public void addElement(Object obj) {
                super.addElement(obj);
                ((Action) obj).addPropertyChangeListener(l);
                
            }
            
            PropertyChangeListener l = new PropertyChangeListener() {
                
                public void propertyChange(PropertyChangeEvent evt) {
                    int index = indexOf(evt.getSource());
                    if (index >= 0) {
                        fireContentsChanged(reallyThis, index, index);
                    }
                }
                
            };
        };
        for (Action action : actions) {
            model.addElement(action);
        }
        return model;
    }


}
