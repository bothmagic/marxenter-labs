import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.util.JVM;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 27-Jun-2008
 * Time: 17:42:35
 */

public class LargeTableModelIssues {

    //NB how to time sorts?
    //NB how to time inserts, updates & deletes?
    //NB might active highlighters effect performance?

    // Sort by D and remove several rows at once can be tardy (for sorted JXTable)
    // Select All (ctrl+A) and Remove is very, very, very slow for both (see pending below?)
    // batching updates would help (albeit lacking API support)

    //NB *Other thoughts*
    //
    //NB Whats up with tables keyboard navigation, why does pgup & pgdown change selection?
    //NB ..same with cursor up/down (though ctrl+ up/down is different same isn't then true for ctrl+ pgup & pgdown?)
    //NB Q. how do you make multi-selections on big tables without a mouse?
    //
    //NB No original (unsorted) column sort toggle for 6.0?
    //NB No click gestures for multiple column sorting under 6.0?
    //NB Q. How to change sort ordering via keyboard navigation only?
    //
    //NB RowSorter not available on other List-like components?
    //NB how do RowSorters & Filers effect beansbindings (given its 1.5 compatible)?
    //NB ..hmmm (aren't selections tied to that crappy one-way synthetic properties kludge?)
    //
    //NB Note difference in row heights between two tables (3 rows diff with same initial data displayed)
    //
    //NB Note not enough scrollbar pixels for every row (drag thumb vs paging)
    //

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LargeTableModelIssues();
            }
        });
    }

    public LargeTableModelIssues() {
        Frame frame = createFrame();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    Frame createFrame() {
        final JFrame frame = new JFrame("Check JXTable sort & update performance with large models");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent content = (JComponent) frame.getContentPane();
        content.setLayout(new GridLayout(1, 2));
        JTable jTable = new JTable();
        JXTable jxTable = new JXTable();
        jxTable.setColumnControlVisible(true);

        content.add(getTablePanel(jTable, "JTable (6.0 RowSorter)"));
        content.add(getTablePanel(jxTable, "JXTable (SwingX filtering)"));

        Collection<RowData> sampleData = getSampleData();
        jTable.setModel(new MyTableModel(new ArrayList<RowData>(sampleData)));
        if (JVM.current().isOneDotSix()) {
            try {
                // jTable.setRowSorter(new TableRowSorter(myModel)); ..the hard way
                Class rowSorterClass = Class.forName("javax.swing.table.TableRowSorter");
                Constructor constructor = rowSorterClass.getConstructor(TableModel.class);
                Method method = JTable.class.getMethod("setRowSorter", Class.forName("javax.swing.RowSorter"));
                Object rowSorter = constructor.newInstance(jTable.getModel());
                method.invoke(jTable, rowSorter);
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        jxTable.setModel(new MyTableModel(new ArrayList<RowData>(sampleData)));
        return frame;
    }

    JComponent getTablePanel(final JTable table, String name) {
        JXTitledPanel titledPanel = new JXTitledPanel(name, new JScrollPane(table));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        JButton addButton = new JButton(new AddAction(table));
        buttonPanel.add(addButton);
        JButton removeButton = new JButton(new RemoveAction(table));
        buttonPanel.add(removeButton);
        buttonPanel.setOpaque(false);
        titledPanel.setRightDecoration(buttonPanel);
        return titledPanel;
    }

    Collection<RowData> getSampleData() {
        Random random = new Random();
        Set<RowData> results = new HashSet<RowData>();
        for (String isoLanguage : Locale.getISOLanguages()) {
            for (String isoCountry : Locale.getISOCountries()) {
                Locale locale = new Locale(isoLanguage, isoCountry);
                results.add(new RowData(locale.toString(),
                        locale.getDisplayLanguage(),
                        locale.getDisplayCountry(),
                        random.nextInt(Integer.MAX_VALUE)));
                results.add(new RowData(locale.getISO3Language() + "_" + locale.getISO3Country(),
                        locale.getDisplayLanguage(),
                        locale.getDisplayCountry(),
                        random.nextInt(Integer.MAX_VALUE)));
                //if (results.size()>10) return results;
            }
        }
        //TODO 'large' might actually be up to ten times more than this (millions rather than a hundred thousand)
        System.out.println(String.format("Generated %,d rows.", results.size()));
        return Collections.unmodifiableCollection(results);
    }

    static class AddAction extends AbstractAction {
        JTable table;
        Random random = new Random();

        public AddAction(JTable table) {
            super("Add");
            this.table = table;
        }

        public void actionPerformed(ActionEvent e) {
            EditableTablemodel tableModel = (EditableTablemodel) table.getModel();
            RowData rowData = new RowData();
            tableModel.add(rowData);
            int index = tableModel.getIndex(rowData);
            if (index >= 0) {
                int row = table.convertRowIndexToView(index);
                table.setRowSelectionInterval(row, row);
                table.changeSelection(row, 0, true, true);
            }
        }
    }

    static class RemoveAction extends AbstractAction {
        JTable table;

        public RemoveAction(JTable table) {
            super("Remove");
            this.table = table;
            setEnabled(false);
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting())
                        setEnabled(RemoveAction.this.table.getSelectedRowCount() > 0);
                }
            });
        }

        public void actionPerformed(ActionEvent e) {
            EditableTablemodel tableModel = (EditableTablemodel) table.getModel();
            int[] selected = table.getSelectedRows();
            if (selected.length > 0) {
                for (int i = selected.length - 1; i >= 0; i--) {
                    selected[i] = table.convertRowIndexToModel(selected[i]);
                }
                tableModel.remove(selected);
            }
        }
    }


    static class RowData {
        String isoCode;
        String country;
        String language;
        int numeric;

        static Random random = new Random();

        public RowData() {
            this(Integer.toHexString(random.nextInt()),
                    Integer.toHexString(random.nextInt()),
                    Integer.toHexString(random.nextInt()),
                    random.nextInt(Integer.MAX_VALUE));
        }

        public RowData(String isoCode, String language, String country, int numeric) {
            this.isoCode = isoCode;
            this.language = language;
            this.country = country;
            this.numeric = numeric;
        }

        public String getCountry() {
            return country;
        }

        public String getIsoCode() {
            return isoCode;
        }

        public String getLanguage() {
            return language;
        }

        public int getNumeric() {
            return numeric;
        }
    }


    static interface EditableTablemodel extends TableModel {
        int getIndex(Object o);

        void add(Object o);

        void remove(int... indexes);
    }


    static class MyTableModel extends AbstractTableModel implements EditableTablemodel {
        List<RowData> values;

        MyTableModel(List<RowData> values) {
            this.values = values;
        }

        public int getRowCount() {
            return values.size();
        }

        public int getColumnCount() {
            return 5;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            RowData rowData = values.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return rowData.getIsoCode();
                case 1:
                    return rowData.getLanguage();
                case 2:
                    return rowData.getCountry();
                case 3:
                    return Integer.toHexString(rowData.hashCode());
                case 4:
                    return rowData.getNumeric();
            }
            return "";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex < 4 ? String.class : Integer.class;
        }

        public void add(Object o) {
            int row = values.size();
            values.add((RowData) o);
            fireTableRowsInserted(row, row);
        }

        public void remove(int... indexes) {
            if (indexes != null) {
                Arrays.sort(indexes);
                for (int i = indexes.length - 1; i >= 0; i--) {
                    values.remove(indexes[i]);
                    //PENDING lacking batch multi-selection update? (events only support a contiguous range)
                    fireTableRowsDeleted(indexes[i], indexes[i]);
                }
            }
        }

        public int getIndex(Object o) {
            return values.indexOf(o);
        }
    }

}