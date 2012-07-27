import java.util.List;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.test.ComponentTreeTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;

/*
 * Created by IntelliJ IDEA.
 * User: rosbaldeston
 * Date: 27-Jun-2007
 * Time: 16:07:26
 */

public class JXTreeTableIssues {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JXTreeTableIssues();
            }
        });
    }

    public JXTreeTableIssues() {
        Frame frame = createFrame();
        frame.setSize(800, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    Frame createFrame() {
        final JFrame frame = new JFrame("JTreeTable re-showing invisble columns issue.");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent content = (JComponent) frame.getContentPane();
        final JXTreeTable treeTable = new MyTreeTable();
        final MyComponentTreeTableModel treeTableModel = new MyComponentTreeTableModel(new JFileChooser());
        treeTable.setTreeTableModel(treeTableModel);
        treeTable.setColumnControlVisible(true);
        treeTable.setHorizontalScrollEnabled(true);
        content.add(new JScrollPane(treeTable));

        //treeTable.getColumnExt(1).setVisible(false);
        ColumnFactory columnFactory = new ColumnFactory() {
            @Override
            public void configureTableColumn(TableModel model, TableColumnExt columnExt) {
                super.configureTableColumn(model, columnExt);

                if (columnExt.getModelIndex() == 1) {
                    //columnExt.setVisible(false);
                }
            }

            public void configureColumnWidths(JXTable table, TableColumnExt columnExt) {
                int col = table.convertColumnIndexToView(columnExt.getModelIndex());
                   if ((col >= 0) && ((JXTreeTable) table).isHierarchical(col)) {
                    super.configureColumnWidths(table, columnExt);
                   } else {
                       columnExt.setPreferredWidth(10);
                   }
                int ww = columnExt.getPreferredWidth();
                ww = 0;
            }
        };
        treeTable.setColumnFactory(columnFactory);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                treeTableModel.delayedLoadChildren();
            }
        });
        return frame;
    }

    class MyComponentTreeTableModel extends ComponentTreeTableModel {
        public MyComponentTreeTableModel(Container root) {
            super(root);
        }

        public void delayedLoadChildren() {
            modelSupport.fireTreeStructureChanged(getPathToRoot((Component) root));
        }
    }

    class MyTreeTable extends JXTreeTable {
        protected void initializeColumnWidths() {
            List<TableColumn> tableColumns = getColumns(true);
            for (int i = tableColumns.size() - 1; i >= 0; i--) {
                initializeColumnPreferredWidth(tableColumns.get(i));
            }
        }
    }
}