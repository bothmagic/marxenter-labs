import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CheesyDisabledLabelsIssue {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CheesyDisabledLabelsIssue();
            }
        });
    }

    public CheesyDisabledLabelsIssue() {
        JFrame frame = new JFrame("Can we remove the labels cheesy 3D effect");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JMenuBar menubar = new JMenuBar();
        JMenu menu = menubar.add(new JMenu("File"));
        frame.setJMenuBar(menubar);

        JComponent content = (JComponent) frame.getContentPane();
        JTable table = new JTable();
        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {"Mary had", "It was lovely"},
                        {"a little ", "with the roast "},
                        {"lamb,", "potatoes and gravy."},
                        {null, ""}
                },
                new String[]{
                        "1", "2"
                }) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        content.add(table);
        table.getSelectionModel().setSelectionInterval(0, 0);

        // OK admittedly this step looks like a contrived example, but the propertysheet forces
        // enablement state for me as my value objects won't have setters (reflection magic)
        // ..this just duplicates the scenario (nb propertysheet disabled color is an even lighter shade?!)
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setEnabled(false);
        table.setDefaultRenderer(Object.class, cellRenderer);

        frame.pack();
        frame.setSize(new Dimension(640, 480));
        frame.setVisible(true);
    }
}
