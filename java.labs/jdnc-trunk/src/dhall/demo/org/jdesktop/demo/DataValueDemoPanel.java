/*
 * DataValueDemoPanel.java
 *
 * Created on April 16, 2005, 9:33 AM
 */

package org.jdesktop.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.jdesktop.binding.JavaBeanDataModel;
import org.jdesktop.binding.TabularDataModel;
import org.jdesktop.binding.swingx.BindingFactory;
import org.jdesktop.binding.swingx.BindingHandler;
import org.jdesktop.binding.swingx.DirectTableBinding;
import org.jdesktop.dataset.DataSet;
import org.jdesktop.dataset.DataTable;
import org.jdesktop.dataset.DataValue;
import org.jdesktop.dataset.adapter.TabularDataModelAdapter;
import org.jdesktop.demo.DemoPanel;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXRootPane;
import org.jdesktop.swingx.JXTable;

import static java.awt.GridBagConstraints.*;

/**
 * Demonstrates the use of computed DataValues against an example DataSet.
 */
public class DataValueDemoPanel extends DemoPanel {
    
    /**
     * Creates new form JXDatePickerDemoPanel 
     */
    public DataValueDemoPanel() {
        setName("Simple Demo");
        init();
    }

    public String getHtmlDescription() {
        return "<html><b>DataValue Demo</b><br/>" +
                "Demonstrates the use of DataValues in a DataSet, allowing for new " +
                "computed values to be entered with the databinding parser. " +
                " " +
                " " +
                " " +
                " " +
                "</html>";
    }
    
    public String getInformationTitle() {
        return "DataValue Demo";
    }
    
    public String getName() {
        return "DataValue Demo";
    }

    private void init() {
        // Initialize the models
        DataSet ds = SampleBugDataSet.getDataSet(null);
        DataTable issues = ds.getTable(SampleBugDataSet.ISSUES_TBL);
        TabularDataModel bugModel = new TabularDataModelAdapter(issues);

        final DataValue value = new DataValue(ds);
        JavaBeanDataModel valueModel = null;
        try {
            valueModel = new JavaBeanDataModel(value);
            valueModel.getMetaData("value").setReadOnly(true);
            valueModel.getMetaData("name").setLabel("Name");
            valueModel.getMetaData("expression").setLabel("Expression");
        } catch (IntrospectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Initialize the visual components
        JXTable bugTable = new JXTable();
        JScrollPane scroller = new JScrollPane(bugTable);
        JLabel nameLabel = new JLabel();
        JLabel expressionLabel = new JLabel();
        JLabel valueLabel = new JLabel();
        JTextField nameText = new JTextField(8);
        JTextField expressionText = new JTextField(40);
        final JTextField valueText = new JTextField(20);
        valueText.setEditable(false);

        GridBagConstraints labelCons =
            new GridBagConstraints(RELATIVE, RELATIVE, 1, 1, 0.0, 0.0, WEST, NONE,
                                   new Insets(5, 5, 5, 5), 0, 0);

        GridBagConstraints fieldCons =
            new GridBagConstraints(RELATIVE, RELATIVE, 1, 1, 1.0, 0.0, WEST, HORIZONTAL,
                                   new Insets(5, 5, 5, 5), 0, 0);

        GridBagConstraints lastCons =
            new GridBagConstraints(RELATIVE, RELATIVE, REMAINDER, 1, 4.0, 0.0, WEST, HORIZONTAL,
                                   new Insets(5, 5, 5, 5), 0, 0);

        JPanel valuePanel = new JPanel(new GridBagLayout());
        valuePanel.add(nameLabel, labelCons);
        valuePanel.add(nameText, fieldCons);
        valuePanel.add(valueLabel, labelCons);
        valuePanel.add(valueText, lastCons);
        valuePanel.add(expressionLabel, labelCons);
        valuePanel.add(expressionText, lastCons);
        
        setLayout(new BorderLayout());
        add(scroller, BorderLayout.CENTER);
        add(valuePanel, BorderLayout.SOUTH);

        // initialize the binding infrastructure
        BindingFactory factory = BindingFactory.getInstance();
        BindingHandler bindings = new BindingHandler();
        bindings.setAutoCommit(true);
        bindings.add(new DirectTableBinding(bugTable, bugModel));

        bindings.add(BindingFactory.getInstance().createBinding(
                nameText, valueModel, "name"));
        bindings.add(BindingFactory.getInstance().createMetaBinding(
                nameLabel, valueModel, "name"));
        bindings.add(BindingFactory.getInstance().createBinding(
                expressionText, valueModel, "expression"));
        bindings.add(BindingFactory.getInstance().createMetaBinding(
                expressionLabel, valueModel, "expression"));
        bindings.add(BindingFactory.getInstance().createBinding(
                valueText, valueModel, "value"));
        bindings.add(BindingFactory.getInstance().createMetaBinding(
                valueLabel, valueModel, "value"));

        
        // ------- wire control value changes to presentation changes
        value.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("expression".equals(evt.getPropertyName())) {
                    valueText.setText(value.getValue().toString());
                }
            }
        });
        
        // DH: This echos Jeanette's hack -- "changing the selector value leads to firing
        // tableStructureChanged so we turn auto-creation of tables off once they are bound "
        bugTable.setAutoCreateColumnsFromModel(false);
    }


    public static void main(String args[]) {
        DataValueDemoPanel demo = new DataValueDemoPanel();
        
        JXFrame frame = new JXFrame("DataValueDemoPanel");
        JXRootPane rootPane = frame.getRootPaneExt();

        frame.getContentPane().add(BorderLayout.CENTER, demo);
        frame.pack();
        frame.show();
    }
}
