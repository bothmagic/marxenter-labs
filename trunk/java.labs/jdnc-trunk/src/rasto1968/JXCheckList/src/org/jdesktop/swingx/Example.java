/*
 * $Id: Example.java 1371 2007-06-04 12:34:18Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 * A quick test.
 *
 * @author Rob Stone
 */
public final class Example
{    
    private static class MyListCellRenderer extends DefaultListCellRenderer {
        MetalComboBoxIcon icon=new MetalComboBoxIcon();
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            // Proves that the checkbox list cell renderer can be customized
            final Component component=super.getListCellRendererComponent(list, value==null ? "empty" : value.toString() + " foobar",  index, isSelected, cellHasFocus);
            ((JLabel)component).setIcon(icon);
            return component;
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Set the user interface defaults
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Ignore
                }
                final JFrame frame=new JFrame("JXCheckList Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                final JXCheckList list=new JXCheckList(new String[] {"One", "Two", "Three", "Four", "Five"});
                list.setCellRenderer(new MyListCellRenderer()); // Uncomment to show cell renderer customization
                list.setSingleClickCheck(true);
                list.setHighlighters(HighlighterFactory.createSimpleStriping(HighlighterFactory.CLASSIC_LINE_PRINTER));
                frame.add(new JScrollPane(list));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
