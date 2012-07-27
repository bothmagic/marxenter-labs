/*
 * Demo.java
 *
 * Created on December 2, 2005, 3:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx.highlighters2.demo;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.highlighters2.AlternateRowHighlighter;
import org.jdesktop.swingx.highlighters2.CompoundHighlighter;
import org.jdesktop.swingx.highlighters2.JXTable;
import org.jdesktop.swingx.highlighters2.PatternHighlighter;
import org.jdesktop.swingx.highlighters2.RolloverHighlighter;

/**
 *
 * @author rb156199
 */
public class Demo {
    
    public static void main(String... args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JXTable table = new JXTable();
        table.setModel(new DefaultTableModel(new Object[][]{{"Richard", "Bair"}, 
                                                            {"Scott", "Violet"}, 
                                                            {"Romain", "Guy"},
                                                            {"Kathy", "Walrath"},
                                                            {"Tom", "Ball"},
                                                            {"Hans", "Muller"}
                                                            }, new Object[]{"First", "Last"}));
                                                            
        table.setHighlighter(new CompoundHighlighter(AlternateRowHighlighter.CLASSIC_LINE_PRINTER,
                new PatternHighlighter(Color.ORANGE, Color.WHITE, "B.*", 0, 1),
                new RolloverHighlighter(Color.PINK, Color.WHITE)));
        table.setRolloverEnabled(true);
        frame.add(new JScrollPane(table));
        frame.setSize(800,600);
        frame.setVisible(true);
    }
}
