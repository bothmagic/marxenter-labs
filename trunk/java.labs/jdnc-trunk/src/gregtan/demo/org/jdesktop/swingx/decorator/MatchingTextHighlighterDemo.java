/*
 * $Id: MatchingTextHighlighterDemo.java 2827 2008-10-22 12:09:58Z gregtan $
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
 * 
 */
package org.jdesktop.swingx.decorator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.painter.MattePainter;

public class MatchingTextHighlighterDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setLayout(new BorderLayout());
                
                JXTable table = createTable();
                JTextField field = new JTextField();
                applyHighlighters(table, field);
                
                f.add(new JScrollPane(table), BorderLayout.CENTER);
                f.add(field, BorderLayout.NORTH);
                f.pack();
                f.setVisible(true);
            }
        });
    }
    
    private static JXTable createTable() {
        Object[][] data = {
                { "Apple", "Yum" },
                { "Banana", "Very Yum" },
                { "Broccoli", "Average" },
                { "Carrot", "Very Yuk" },
                { "Chocolate", "Extremely Yum" }
        };
        JXTable table = new JXTable(data, new Object[] { "Food", "Rating" });
        
        return table;
    }
    
    private static void applyHighlighters(JXTable table, final JTextField field) {
        // Add an icon to the rating column
        Icon icon = new Icon() {
          @Override
            public int getIconHeight() {
                return 8;
            }
          
          @Override
            public int getIconWidth() {
                return 8;
            }
          
          @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
              g.setColor(Color.GREEN);
              g.fillRect(x, y, getIconWidth(), getIconHeight());
            }
        };
        
        IconHighlighter iconHighlighter = new IconHighlighter(
                new HighlightPredicate.ColumnHighlightPredicate(1), icon);
        table.addHighlighter(iconHighlighter);
        
        final MatchingTextHighlighter matchingHighlighter = new MatchingTextHighlighter();
        matchingHighlighter.setPainter(new MattePainter<JLabel>(Color.YELLOW));
        table.addHighlighter(matchingHighlighter);
        
        DocumentListener l = new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                // NO-OP
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateHighlighter(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateHighlighter(e);
            }
            
            private void updateHighlighter(DocumentEvent e) {
                try {
                    Document d = e.getDocument();
                    String text = d.getText(0, d.getLength());
                    SearchPredicate predicate = new SearchPredicate(
                            Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE), 
                            PatternPredicate.ALL);
                    matchingHighlighter.setHighlightPredicate(predicate);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }
        };
        
        field.getDocument().addDocumentListener(l);
    }
}
