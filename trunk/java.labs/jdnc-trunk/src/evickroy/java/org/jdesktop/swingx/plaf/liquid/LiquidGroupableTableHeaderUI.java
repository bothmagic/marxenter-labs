/*
 * $Id: LiquidGroupableTableHeaderUI.java 694 2005-09-22 03:58:44Z evickroy $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.liquid;

import org.jdesktop.swingx.plaf.basic.BasicGroupableTableHeaderUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import javax.swing.event.*;
import java.awt.event.*;

import org.jdesktop.swingx.table.*;
import org.jdesktop.swingx.JXGroupableTableHeader;

import com.birosoft.liquid.skin.Skin;

/**
 *
 * @author erik
 */
public class LiquidGroupableTableHeaderUI extends BasicGroupableTableHeaderUI {
    private Skin skin;

    /** Creates a new instance of LiquidGroupableTableHeaderUI */
    public LiquidGroupableTableHeaderUI() {
    }

    /**
     * Listener for table header.
     * 
     * @author Kirill Grouchnikov
     */
//    private static class TableHeaderListener implements ListSelectionListener {
            /**
             * The associated table header UI.
             */
//            private LiquidGroupableTableHeaderUI ui;

            /**
             * Simple constructor.
             * 
             * @param ui
             *            The associated table header UI
             */
//            public TableHeaderListener(LiquidGroupableTableHeaderUI ui) {
//                    this.ui = ui;
//            }

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
             */
//            public void valueChanged(ListSelectionEvent e) {
//                    if (ui.header == null)
//                            return;
//                    if (ui.header.isValid())
//                            ui.header.repaint();
//            }
//    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
     */
    public static ComponentUI createUI(JComponent h) {
            LiquidGroupableTableHeaderUI result = new LiquidGroupableTableHeaderUI();
            JTableHeader tableHeader = (JTableHeader) h;
            TableColumnModel columnModel = tableHeader.getColumnModel();
            if (columnModel != null) {
                    ListSelectionModel lsm = columnModel.getSelectionModel();
                    if (lsm != null) {
//                            lsm.addListSelectionListener(new TableHeaderListener(result));
                    }
            }
            return result;
    }

    public Skin getSkin() {
        if (skin == null) {
            skin = new Skin("tableheader.png", 8, 4, 13, 4, 10);
        }

        return skin;
    }

    protected void paintCell(Graphics graphics, Rectangle cellRect, int columnIndex) {
        TableColumn column = header.getColumnModel().getColumn(columnIndex);
        int index = 0;

        if (columnIndex == columnSelected && columnGroupSelected == null) {
            index = 1;
        }
        Component component = getHeaderRenderer(columnIndex);

        getSkin().draw(graphics, index, cellRect.x, cellRect.y, cellRect.width,
            cellRect.height);

        rendererPane.paintComponent(graphics, component, header, cellRect.x, cellRect.y,
                                    cellRect.width, cellRect.height, true);
    }


    protected void paintCell(Graphics graphics, Rectangle cellRect, ColumnGroup group) {
        int index = 0;
        Component component = getHeaderRenderer(group);

        if( columnGroupSelected == group ) {
            index = 1;
        }

        getSkin().draw(graphics, index, cellRect.x, cellRect.y, cellRect.width,
            cellRect.height);

        rendererPane.paintComponent(graphics, component, header, cellRect.x, cellRect.y,
                                    cellRect.width, cellRect.height, true);
    }
}
