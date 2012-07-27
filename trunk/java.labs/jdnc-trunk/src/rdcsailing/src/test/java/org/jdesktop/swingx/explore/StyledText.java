package org.jdesktop.swingx.explore;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * Styled text - used for demo purposes
 * @author Ryan Cuprak
 */
public class StyledText extends JFrame {

    /**
     * Text pane
     */
    private JTextPane textPane;

    private DefaultStyledDocument document;

    public StyledText() throws Exception {
        StyleContext context = new StyleContext();
        document = new DefaultStyledDocument(context);
        textPane = new JTextPane(document);

        JScrollPane sp = new JScrollPane(textPane);

        Style style1 = context.addStyle("Style1",null);
        style1.addAttribute(StyleConstants.Foreground, Color.RED);

        Style style2 = context.addStyle("Style2",null);
        style2.addAttribute(StyleConstants.Foreground, Color.BLUE);


        document.insertString(0,"Style 1 ",style1);
        document.insertString(document.getLength()," ",null);
        document.insertString(document.getLength()," Style 2",style2);

        Element e = document.getDefaultRootElement();
        recurse(e);


        getContentPane().add(sp);
        pack();
        setVisible(true);
    }

    /**
     * Recurses over the elements.
     * @param e - element
     */
    protected void recurse(Element e) throws Exception {
        System.out.println();
        System.out.print("Element: " + e.getName() + " " + e.getStartOffset() + " " + e.getEndOffset());
        System.out.print("|" +document.getText(e.getStartOffset(),e.getEndOffset()-e.getStartOffset())+"|");
        for(int i = 0; i < e.getElementCount(); i++) {
            Element a = e.getElement(i);
            recurse(a);
        }
        JTextField tf = new JTextField();

    }

    public static void main(String args[]) throws Exception {
        new StyledText();
    }
}
