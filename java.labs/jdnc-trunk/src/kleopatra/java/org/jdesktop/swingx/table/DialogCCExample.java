package org.jdesktop.swingx.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.icon.ColumnControlIcon;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.TextPainterX;
import org.jdesktop.swingx.util.WindowUtils;

/**
 * Quick driver for alternative ColumnControlButton.
 * 
 * @author Jeanette Winzenburg
 */
public class DialogCCExample {

    JComponent getContent() {
        JXPanel panel = new JXPanel(new BorderLayout());
        JXHeader header = new JXHeader();
        enableHTML(header, false);
        header.setTitle("Custom ColumnControl Popup");
        Icon icon = new ImageIcon(getClass().getResource("/resources/kleopatra.jpg"));
        header.setIcon(icon );
        String description = "This example implements a ColumnControlButton (" +
                        "the control in the trailing upper corner of the scrollpane) " +
                        "with a custom popup. The popup is a dialog with two lists which " +
                        "contain the visible/hidden columns. Toggling visibility is achieved " +
                        "by double-clicking the list item. <br/> ";
        header.setDescription(description);
        panel.add(header, BorderLayout.NORTH);
        JXTable table = createTable();
        table.setColumnControlVisible(true);
        table.setColumnControl(new DialogColumnControlButton(table,
                new ColumnControlIcon()));
        table.setModel(new DefaultTableModel(0, 6));
        JScrollPane pane = new JScrollPane(table);
        panel.add(pane);
        JXHeader footer = new JXHeader();
        enableHTML(footer, false);
        footer.setTitle("Notes: ");
        String footerText = "<html><body>" +
            " <li> code: in the jdnc-incubator, section kleopatra, package table. <br/>" +
            " <li> link: relevant requirement discussion http://forums.java.net/jive/thread.jspa?threadID=7417 <br/>" +
//            " <li> link: <a href=\"http://forums.java.net/jive/thread.jspa?threadID=7417\">relevant requirement discussion </a> <br/>" +
            " <li> technique: use client properties to remember dialog size/location between invocations<br/> " +
            " <li> technique: use filtered JXLists to virtually \"move\" items  <br/> " +
            " <li> technique: use Painter to decorate JXTable background  <br/> " +
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


    private JXTable createTable() {
        JXTable table = new JXTable() {

            Painter painter;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintPainter(g);
            }
            
            protected void paintPainter(Graphics g) {
                if (getRowCount() > 0) return;
               getPainter().paint((Graphics2D) g, this, getWidth(), getHeight());
                
            }

            protected Painter getPainter() {
                if (painter == null) {
                    painter = createPainter();

                }
                return painter;
            }

            private Painter createPainter() {
                TextPainterX painter = new TextPainterX();
                painter.setFont(getFont().deriveFont(Font.BOLD | Font.ITALIC, getFont().getSize2D() * 2));
                painter.setFillPaint(Color.GRAY);
                painter.setText("No Data Available");
//                painter.setLocation(new Point2D.Double(0.2d, 0.3d));
                return painter;
            }

            
        };
        return table;
    }

    public static void main(String[] args) {
        setUpLookAndFeel();
        final JXFrame frame = new JXFrame(
                "JXTable :: Advanced Customization",
                true);
        frame.add(new DialogCCExample().getContent());
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
