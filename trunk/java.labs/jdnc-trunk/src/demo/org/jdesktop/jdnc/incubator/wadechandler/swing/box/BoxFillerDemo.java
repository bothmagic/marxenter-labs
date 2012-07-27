/*
 * $Id: BoxFillerDemo.java 281 2005-01-12 07:56:44Z wadechandler $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
/*
 * BoxFillerDemo.java
 *
 * Created on January 6, 2005, 12:43 AM
 */

package org.jdesktop.jdnc.incubator.wadechandler.swing.box;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *Note that the BoxFiller class is best used from the Palette of an IDE.
 *This way it is quick to throw applications together with a nice expanding gui and still be using all
 *standard packages except for one class.
 * @author  Wade Chandler
 * @version 1.0
 */
public class BoxFillerDemo extends javax.swing.JFrame {
    
    /** Creates new form BoxFillerDemo */
    public BoxFillerDemo() {
        initComponents();
        try
        {
            this.setupDemo();
        }
        catch(Throwable e)
        {
            e.printStackTrace();
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BoxFillerDemo");
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-448)/2, (screenSize.height-586)/2, 448, 586);
    }//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BoxFillerDemo().setVisible(true);
            }
        });
    }
    
    /**
     *Method used to setup the demo.  Note that the BoxFiller class is best used from the Palette of an IDE.
     *This way it is quick to throw applications together with a nice expanding gui and still be using all
     *standard packages except for one class.
     */
    public void setupDemo()
    {
        Container top = this.getContentPane();
        
        BoxFiller explain = new BoxFiller();
        explain.setLayout(new FlowLayout());
        explain.setVerticalStrut(50);
        Font fontExplain = null;
        JLabel labelExplain = new JLabel("This is the BoxFiller Demo [best used visually in an IDE]");
        explain.add(labelExplain);
        top.add(explain);
        
        BoxFiller address = new BoxFiller();
        address.setVerticalStrut(40);
        BorderLayout layoutAddress = new BorderLayout();
        address.setLayout(layoutAddress);
        JLabel labelAddress = new JLabel("Address: ");
        JLabel labelAddressExplain = new JLabel("This sort of looks like a browsers address bar.");
        JTextField textfieldAddress = new JTextField();
        JButton buttonAddress = new JButton("GO");
        address.add(labelAddressExplain, layoutAddress.NORTH);
        address.add(labelAddress, layoutAddress.WEST);
        address.add(textfieldAddress, layoutAddress.CENTER);
        address.add(buttonAddress, layoutAddress.EAST);
        top.add(address);
        
        BoxFiller space1 = new BoxFiller();
        space1.setVerticalStrut(20);
        top.add(space1);
        
        BoxFiller editor = new BoxFiller();
        Dimension dimEditor = new Dimension(200, 200);
        editor.setRigidArea(dimEditor);
        CardLayout layoutEditor = new CardLayout();
        editor.setLayout(layoutEditor);
        JScrollPane spEditor = new JScrollPane();
        JTextArea textareaEditor = new JTextArea();
        textareaEditor.setText("This is a JTextArea in a JScrollPane on a rigid area BoxFiller with a CardLayout");
        textareaEditor.setWrapStyleWord(true);
        textareaEditor.setCaretPosition(0);
        textareaEditor.setLineWrap(true);
        textareaEditor.setEditable(true);
        spEditor.setViewportView(textareaEditor);
        editor.add(spEditor, "0");
        top.add(editor);
        layoutEditor.show(editor, "0");

        BoxFiller spacer2 = new BoxFiller();
        spacer2.setVerticalStrut(10);
        top.add(spacer2);
        
        BoxFiller form1 = new BoxFiller();
        form1.setVerticalStrut(40);
        BorderLayout layoutForm1 = new BorderLayout();
        form1.setLayout(layoutForm1);
        form1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        JLabel labelForm1 = new JLabel("These can also look like forms...using BorderFactory.createEmptyBorder(0,10,0,10) for margins.");
        JTextField textfieldForm1 = new JTextField();
        form1.add(labelForm1, layoutAddress.NORTH);
        form1.add(textfieldForm1, layoutAddress.CENTER);
        top.add(form1);

        BoxFiller spacer3 = new BoxFiller();
        spacer3.setVerticalStrut(10);
        top.add(spacer3);
        
        BoxFiller form2 = new BoxFiller();
        form2.setVerticalStrut(40);
        BorderLayout layoutForm2 = new BorderLayout();
        form2.setLayout(layoutForm2);
        form2.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        JLabel labelForm2 = new JLabel("Name");
        JTextField textfieldForm2 = new JTextField();
        form2.add(labelForm2, layoutAddress.NORTH);
        form2.add(textfieldForm2, layoutAddress.CENTER);
        top.add(form2);        
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
