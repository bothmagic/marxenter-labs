package notanissue;

import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/*
 * Created by IntelliJ IDEA.
 * User: rosbaldeston
 * Date: 27-Jun-2007
 * Time: 17:42:35
 */

public class JXTableIssues {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JXTableIssues();
            }
        });
    }

    public JXTableIssues() {
        Frame frame = createFrame();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    Frame createFrame() {
        final JFrame frame = new JFrame("Fun with JXTable Ctrl+F Find");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent content = (JComponent) frame.getContentPane();
        JXTable table = new JXTable(new MyTableModel());
        table.setColumnControlVisible(true);
        JCheckBox checkBox = new JCheckBox(message);
        checkBox.setOpaque(false);

        JTabbedPane tabbedPane = new JTabbedPane();
        content.add(tabbedPane);
        tabbedPane.addTab("Places to go", new JScrollPane(table));
        tabbedPane.addTab("About", checkBox);
        return frame;
    }

    class MyTableModel extends AbstractTableModel {
        List<Locale> values = Arrays.asList(Locale.getAvailableLocales());

        public int getRowCount() {
            return values.size();
        }

        public int getColumnCount() {
            return 4;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Locale locale = values.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return locale.getDisplayLanguage();
                case 1:
                    return locale.getLanguage();
                case 2:
                    return locale.getDisplayCountry();
                case 3:
                    return locale.getCountry();
            }
            return "";
        }
    }

    String message = "<html>Is it too fancy? Is it fancy enough?<br> I can make it less fancy.<br> Or I can make it more fancy.<br>" +
            "Or I can smash it to bits with a hammer.<br> Or I can smash in your head with a hammer.<br>" +
            "The choice is yours.<br><br> But if you do not make a choice, I will make a choice on your behalf.</html>";
}
