/*
 * $Id: SubstanceGroupableTableHeaderUI.java 787 2006-03-31 04:27:43Z evickroy $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
//package org.jvnet.substance;
package org.jdesktop.swingx.plaf.substance;

import org.jvnet.substance.color.ColorScheme;

import javax.swing.plaf.basic.BasicTableHeaderUI;
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

import org.jvnet.substance.*;

import org.jdesktop.swingx.table.*;
import org.jdesktop.swingx.plaf.basic.BasicGroupableTableHeaderUI;

/**
 *
 * @author erik
 */
public class SubstanceGroupableTableHeaderUI extends BasicGroupableTableHeaderUI {
    
    /** Creates a new instance of SubstanceGroupableTableHeaderUI */
    public SubstanceGroupableTableHeaderUI() {
    }

    /**
     * Delegate for painting the background.
     */
    private static SubstanceGradientBackgroundDelegate backgroundDelegate = new SubstanceGradientBackgroundDelegate();

    /**
     * Listener for table header.
     * 
     * @author Kirill Grouchnikov
     */
    private static class TableHeaderListener implements ListSelectionListener {
            /**
             * The associated table header UI.
             */
            private SubstanceGroupableTableHeaderUI ui;

            /**
             * Simple constructor.
             * 
             * @param ui
             *            The associated table header UI
             */
            public TableHeaderListener(SubstanceGroupableTableHeaderUI ui) {
                    this.ui = ui;
            }

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
             */
            public void valueChanged(ListSelectionEvent e) {
                    if (ui.header == null)
                            return;
                    if (ui.header.isValid())
                            ui.header.repaint();
            }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
     */
    public static ComponentUI createUI(JComponent h) {
            SubstanceGroupableTableHeaderUI result = new SubstanceGroupableTableHeaderUI();
            JTableHeader tableHeader = (JTableHeader) h;
            TableColumnModel columnModel = tableHeader.getColumnModel();
            if (columnModel != null) {
                    ListSelectionModel lsm = columnModel.getSelectionModel();
                    if (lsm != null) {
                            lsm.addListSelectionListener(new TableHeaderListener(result));
                    }
            }
            return result;
    }

    protected void paintCell(Graphics graphics, Rectangle cellRect, int columnIndex) {
        TableColumn column = header.getColumnModel().getColumn(columnIndex);
        Component component = getHeaderRenderer(columnIndex);
        boolean isSelected = (columnSelected == columnIndex ? true : false);
        
        ColorScheme colorScheme = isSelected ? 
            SubstanceLookAndFeel.getColorScheme() : 
            SubstanceLookAndFeel.getMetallicColorScheme();
	backgroundDelegate.update(graphics, component, cellRect, colorScheme, true);

        rendererPane.paintComponent(graphics, component, header, cellRect.x, cellRect.y,
                                    cellRect.width, cellRect.height, true);
    }


    protected void paintCell(Graphics graphics, Rectangle cellRect, ColumnGroup group) {
        Component component = getHeaderRenderer(group);
        boolean isSelected = (columnGroupSelected == group ? true : false);

        ColorScheme colorScheme = isSelected ? 
            SubstanceLookAndFeel.getColorScheme() : 
            SubstanceLookAndFeel.getMetallicColorScheme();
	backgroundDelegate.update(graphics, component, cellRect, colorScheme, true);

        rendererPane.paintComponent(graphics, component, header, cellRect.x, cellRect.y,
                                    cellRect.width, cellRect.height, true);
    }
}
