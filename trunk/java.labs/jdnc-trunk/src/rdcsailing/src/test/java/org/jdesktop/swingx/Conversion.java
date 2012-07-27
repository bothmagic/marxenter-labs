package org.jdesktop.swingx;

import java.awt.Color;
import java.text.NumberFormat;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * Created by IntelliJ IDEA.
 * User: rcuprak
 * Date: 8/19/11
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class Conversion extends JFrame {



    public static void main (String args[]) throws Exception {

        NumberFormat nf = NumberFormat.getInstance();


        StyleContext context = new StyleContext();
        DefaultStyledDocument doc = new DefaultStyledDocument(context);
        JTextPane textPane = new JTextPane(doc);

        Style style = context.addStyle("NewStyle",null);
        style.addAttribute(StyleConstants.Background, Color.RED);

        doc.insertString(0,"hello world",style);

        JTextArea area = new JTextArea();
        area.getDocument();
        System.out.println("Document: " + area.getDocument().getClass().getName());


        double d = 4.99;


        NumberFormat f = NumberFormat.getInstance();

        String str = f.format(d);

        System.out.println("D: " + str);
    }

}
