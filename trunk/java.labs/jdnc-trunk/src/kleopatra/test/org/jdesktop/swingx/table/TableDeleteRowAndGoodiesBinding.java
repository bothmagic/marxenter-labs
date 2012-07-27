/*
 * Created on 19.03.2010
 *
 */
package org.jdesktop.swingx.table;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultRowSorter;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.sort.TableSortController;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;
 
public class TableDeleteRowAndGoodiesBinding extends JXFrame
{
    private SelectionInList<Person> silPersons;
    private ListModel dataModel;
 
    public TableDeleteRowAndGoodiesBinding()
    {
        loadData();
        initComponents();
    }
 
    private void loadData()
    {
        List<Person> data = new ArrayList<Person>();
 
        data.add(new Person("Smith", "Fred", 24));
        data.add(new Person("Jones", "Bert", 22));
        data.add(new Person("Some", "Berta", 45));
        dataModel = new ArrayListModel<Person>(data);
    }
 
    private void initComponents()
    {
        setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
 
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
 
        getContentPane().setLayout(new BorderLayout());
 
        silPersons = new SelectionInList<Person>(dataModel);
 
        JTable table = new JXTable(new ExampleTableModel(silPersons));
        TableSortController sorter = new TableSortController(table.getModel()) {

//            /** 
//             * @inherited <p>
//             */
//            @Override
//            public int convertRowIndexToModel(int index) {
//                try {
//                    return super.convertRowIndexToModel(index);
//                    
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//                return index;
//            }
//
//            /** 
//             * @inherited <p>
//             */
//            @Override
//            public int convertRowIndexToView(int index) {
//                try {
//                    return super.convertRowIndexToView(index);
//                    
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//                return index;
//            }
            
            
            
        };
//        table.setRowSorter(sorter);
//        table.setAutoCreateRowSorter(true);
//        RowFilter filter = RowFilter.regexFilter(".*");
//        ((DefaultRowSorter<?, ?>) table.getRowSorter()).setRowFilter(filter);
//        table.getRowSorter().toggleSortOrder(0);
        table.setSelectionModel(new SingleListSelectionAdapter(new JXTableSelectionConverter(silPersons.getSelectionIndexHolder(), table)));
//        table.setHighlighters(HighlighterFactory.createAlternateStriping());
 
        JXPanel buttonBar = new JXPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonBar.add(new JButton(new AcnDelete()));
 
        getContentPane().add(BorderLayout.CENTER, table);
        getContentPane().add(BorderLayout.SOUTH, buttonBar);
 
        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }
 
    private class AcnDelete extends AbstractAction
    {
        private AcnDelete()
        {
            super("Delete");
        }
 
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                if(silPersons.hasSelection())
                {
                    Person person = silPersons.getSelection();
 
                    ((ArrayListModel) silPersons.getListModel()).remove(person);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
 
    private void thisWindowClosing(WindowEvent e)
    {
        System.exit(0);
    }
 
    private static class ExampleTableModel extends AbstractTableAdapter<Person>
    {
        private static final String[] COLUMNS = {"Surname", "First Name", "Age"};
 
        private ExampleTableModel(ListModel listModel)
        {
            super(listModel, COLUMNS);
        }
 
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            Person person = getRow(rowIndex);
 
            switch(columnIndex)
            {
                case 0:
                    return person.getSurname();
                case 1:
                    return person.getFirstName();
                case 2:
                    return person.getAge();
                default:
                    throw new IllegalStateException("Unknown column");
            }
        }
    }
 
    public class JXTableSelectionConverter extends AbstractConverter
    {
        private final JTable table;
 
        public JXTableSelectionConverter(final ValueModel selectionIndexHolder, final JTable table)
        {
            super(selectionIndexHolder);
            this.table = table;
        }
 
        public Object convertFromSubject(Object subjectValue)
        {
            int viewIndex = -1;
            int modelIndex = -1;
 
            if (subjectValue != null)
            {
                modelIndex = ((Integer) subjectValue).intValue();
 
                if ((modelIndex >= 0) && (modelIndex < table.getRowSorter().getModelRowCount()))  // && (modelIndex < table.getModel().getRowCount()))
                {
                    viewIndex = table.convertRowIndexToView(modelIndex);
                }
            }
 
            return new Integer(viewIndex);
        }
 
        public void setValue(Object newValue)
        {
            int viewIndex = -1;
            int modelIndex = -1;
 
            if (newValue != null)
            {
                viewIndex = ((Integer) newValue).intValue();
 
                if (viewIndex >= 0)
                {
                    modelIndex = table.convertRowIndexToModel(viewIndex);
                }
            }
 
            subject.setValue(new Integer(modelIndex));
        }
    }
 
    public class Person
    {
        private String surname;
        private String firstName;
        private int age;
 
        public Person(String surname, String firstName, int age)
        {
            this.surname = surname;
            this.firstName = firstName;
            this.age = age;
        }
 
        public String getSurname()
        {
            return surname;
        }
 
        public String getFirstName()
        {
            return firstName;
        }
 
        public int getAge()
        {
            return age;
        }
    }
 
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    new TableDeleteRowAndGoodiesBinding();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
 
