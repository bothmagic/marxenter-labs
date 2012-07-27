package org.jdesktop.swingx.list;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.util.WindowUtils;

/**
 * Quick example for using filters in JXList.<p>
 * 
 * PENDING JW: JXList filtering currently not functional
 * 
 * @author Jeanette Winzenburg
 */
public class DoubleListExample {

    JComponent getContent() {
        JXPanel panel = new JXPanel(new BorderLayout());
        JXHeader header = new JXHeader();
        enableHTML(header, false);
        header.setTitle("Double List to Move Items");
        Icon icon = new ImageIcon(getClass().getResource("/resources/kleopatra.jpg"));
        header.setIcon(icon );
        String description = "This example implements a control to move items " +
                        "from one set to another. Moving is achieved " +
                        "by double-clicking a list item or clicking the appropiated buttons" +
                        "to move the currently selected items.";
        header.setDescription(description);
        panel.add(header, BorderLayout.NORTH);
        DoubleListSelectorModel model = new DoubleListSelectorModel();
        
        model.setDataSource(getClass(), "resources/wikitopics.txt");
        JComponent pane = new DoubleListSelectorView(model).getContent();
        panel.add(pane);
        JXHeader footer = new JXHeader();
        enableHTML(footer, false);
        footer.setTitle("Notes: ");
        String footerText = "<html><body>" +
            " <li> code: in the jdnc-incubator, section kleopatra, package list. <br/>" +
            " <li> technique: use filtered JXLists to virtually \"move\" items  <br/> " +
            "</body></html>";

        footer.setDescription(footerText);
        panel.add(footer, BorderLayout.SOUTH);
        
        return panel;
    }
    /**
     * Hack around functionality regression in reviewed JXHeader.
     * The internal JEditorPane now is a hidden implementation
     * detail (with html disabled) so we need to go dirty.
     * 
     * @param header
     * @param hyperlinkEndabled
     */
    private void enableHTML(JXHeader header, boolean hyperlinkEndabled) {
        JEditorPane editor = null;
        for (int i = 0; i < header.getComponentCount(); i++) {
            if (header.getComponent(i) instanceof JEditorPane) {
                editor = (JEditorPane) header.getComponent(i);
                break;
            }
        }
        if (editor == null) return;
        editor.setContentType("text/html");
    }


    public static void main(String[] args) {
        setUpLookAndFeel();
        final JXFrame frame = new JXFrame(
                "JXList :: Advanced Customization",
                true);
        frame.add(new DoubleListExample().getContent());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setSize(800, 600);
                frame.setLocation(WindowUtils.getPointForCentering(frame));
                frame.setVisible(true);
            }
        });        
    }


    private static void setUpLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
    }

}
