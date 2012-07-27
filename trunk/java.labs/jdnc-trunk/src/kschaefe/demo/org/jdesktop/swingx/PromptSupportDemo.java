/*
 * $Id: PromptSupportDemo.java 3293 2010-07-16 03:16:09Z kschaefe $
 *
 * Copyright 2010 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.jdesktop.swingx.prompt.PromptSupport.FocusBehavior;
import org.jdesktop.swingx.util.PaintUtils;
import org.jdesktop.swingx.util.WindowUtils;

/**
 * A quick demo to showcase {@code PromptSupport}.
 * 
 * @author kschaefer
 */
public class PromptSupportDemo extends JXFrame {
    @Override
    protected void frameInit() {
        super.frameInit();
        
        setTitle("PromptSupport Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        add(new JLabel("An unprompted textfield:"));
        add(new JTextField(20));
        add(Box.createVerticalStrut(5));
        
        add(new JLabel("A default configured prompt:"));
        JTextField field = new JTextField(20);
        PromptSupport.setPrompt("A Prompt", field);
        add(field);
        add(Box.createVerticalStrut(5));
        
        add(new JLabel("A prompt that stays with focus:"));
        field = new JTextField(20);
        PromptSupport.setPrompt("Show until typing starts", field);
        PromptSupport.setFocusBehavior(FocusBehavior.SHOW_PROMPT, field);
        add(field);
        add(Box.createVerticalStrut(5));
        
        add(new JLabel("A prompt with a custom font style:"));
        field = new JTextField(20);
        PromptSupport.setPrompt("A Custom Font Prompt", field);
        PromptSupport.setFontStyle(Font.BOLD | Font.ITALIC, field);
        add(field);
        add(Box.createVerticalStrut(5));
        
        add(new JLabel("A prompt with a custom foreground and background style:"));
        field = new JTextField(20);
        PromptSupport.init("Custom Prompt Colors", Color.GREEN, Color.BLACK, field);
        add(field);
        add(Box.createVerticalStrut(5));
        
        add(new JLabel("A prompt with a background painter:"));
        field = new JTextField(20);
        PromptSupport.init("Painter Background", Color.BLACK, null, field);
        PromptSupport.setBackgroundPainter(new MattePainter(PaintUtils.AERITH, true), field);
        add(field);
        add(Box.createVerticalStrut(5));
        
        add(new JLabel("A transparent checker painter over a custom background color:"));
        field = new JTextField(20);
        PromptSupport.init("Blue background with painter over top", Color.WHITE, new Color(0, 0, 127), field);
        PromptSupport.setBackgroundPainter(new MattePainter(ColorUtil.getCheckerPaint(new Color(0, 0, 0, 0), Color.GRAY, 8), true), field);
        add(field);
        
        pack();
    }
    
    /**
     * Application entry point.
     * 
     * @param args
     *            unused
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) { }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PromptSupportDemo demo = new PromptSupportDemo();
                demo.setLocation(WindowUtils.getPointForCentering(demo));
                demo.setVisible(true);
            }
        });
    }
}
