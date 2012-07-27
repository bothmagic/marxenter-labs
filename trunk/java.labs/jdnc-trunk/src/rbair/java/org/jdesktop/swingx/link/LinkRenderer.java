/*
 * $Id: LinkRenderer.java 760 2005-12-02 22:46:20Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.link;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.AbstractCellEditor;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.RolloverProducer;

/**
 * A Renderer/Editor for Links.
 * 
 * internally uses JXHyperlink for both (Note: don't reuse the same
 * instance for both functions).
 * 
 * PENDING: make renderer respect selected cell state.
 * 
 * @author Jeanette Winzenburg
 */
public class LinkRenderer extends AbstractCellEditor implements
        TableCellRenderer, TableCellEditor, ListCellRenderer {

    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    /**
     * Created and managed internally to give the "hyperlink" look
     */
    private JXHyperlink linkButton;
    /**
     * Provided by the developer to indicate what should be done when something
     * is executed, as well as the text to display, etc
     */
    private LinkAction linkAction;

    public LinkRenderer() {
        this(null);
    }

    public LinkRenderer(LinkAction visitingDelegate) {
        linkAction = visitingDelegate;
        linkButton = createHyperlink(linkAction);
        linkButton.addActionListener(createEditorActionListener());
    }

    /**
     * @return
     */
    private JXHyperlink createHyperlink(LinkAction linkAction) {
        return new JXHyperlink(linkAction) {
            @Override
            public void updateUI() {
                super.updateUI();
                setBorderPainted(true);
                setOpaque(true);
            }
        };
    }

    public void setVisitingDelegate(LinkAction visitingDelegate) {
        linkAction = visitingDelegate;
        linkButton.setAction(linkAction);
    }
    
    public LinkAction getVisitingDelegate() {
        return linkAction;
    }

    public Component getListCellRendererComponent(JList list, Object value, 
            int index, boolean isSelected, boolean cellHasFocus) {
        updateLinkAction(value);
        Point p = (Point) list
            .getClientProperty(RolloverProducer.ROLLOVER_KEY);
        if (/*cellHasFocus ||*/ (p != null && (p.y >= 0) && (p.y == index))) {
             linkButton.getModel().setRollover(true);
        } else {
             linkButton.getModel().setRollover(false);
        }
        updateSelectionColors(list, isSelected);
        updateFocusBorder(cellHasFocus);
        return linkButton;
    }
    

    private void updateSelectionColors(JList table, boolean isSelected) {
        if (isSelected) {
            // linkButton.setForeground(table.getSelectionForeground());
            linkButton.setBackground(table.getSelectionBackground());
        } else {
            // linkButton.setForeground(table.getForeground());
            linkButton.setBackground(table.getBackground());
        }

    }
    
    private void updateLinkAction(Object value) {
        if (linkAction == null) {
            linkAction = new LinkAction(value == null ? "" : value.toString()) {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Broken Link");
                }
            };
            linkButton.setAction(linkAction);
        } else if (linkAction instanceof URLLinkAction) {
            URLLinkAction a = (URLLinkAction)linkAction;
            //if value is a URL or a URLLinkAction, get the relevant url and
            //use that. Otherwise, call toString() on value and try that. If
            //that doesn't work, or value is null, then set the URL to be null
            if (value instanceof URL) {
                a.setURL((URL)value);
                a.setName(((URL)value).toString());
            } else if (value instanceof URLLinkAction) {
                a.setURL(((URLLinkAction)value).getURL());
                a.setName(((URLLinkAction)value).getName());
            } else if (value != null) {
                try {
                    a.setURLString(value.toString());
                } catch (Exception e) {
                    a.setURL(null);
                    a.setName(value.toString());
                }
            } else {
                a.setURL(null);
                a.setName("");
            }
        } else {
            linkAction.setName(value == null ? "" : value.toString());
        }
    }

//------------------------ TableCellRenderer
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        updateLinkAction(value);
        Point p = (Point) table
                .getClientProperty(RolloverProducer.ROLLOVER_KEY);
        if (/*hasFocus || */(p != null && (p.x >= 0) && (p.x == column) && (p.y == row))) {
             linkButton.getModel().setRollover(true);
        } else {
             linkButton.getModel().setRollover(false);
        }
        updateSelectionColors(table, isSelected);
        updateFocusBorder(hasFocus);
        return linkButton;
    }

    private void updateSelectionColors(JTable table, boolean isSelected) {
            if (isSelected) {
//                linkButton.setForeground(table.getSelectionForeground());
                linkButton.setBackground(table.getSelectionBackground());
            }
            else {
//                linkButton.setForeground(table.getForeground());
                linkButton.setBackground(table.getBackground());
            }
    
    }

    private void updateFocusBorder(boolean hasFocus) {
        if (hasFocus) {
            linkButton.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        } else {
            linkButton.setBorder(noFocusBorder);
        }

        
    }

//-------------------------- TableCellEditor
    
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        updateLinkAction(value);
        linkButton.getModel().setRollover(true); 
        updateSelectionColors(table, isSelected);
        return linkButton;
    }

    public Object getCellEditorValue() {
        return linkAction instanceof URLLinkAction ? ((URLLinkAction)linkAction).getURL() : linkAction.getName();
    }

    private ActionListener createEditorActionListener() {
        ActionListener l = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();

            }

        };
        return l;
    }


}
