package org.jdesktop.jdnc.incubator.rbair.caveatemptor;

import org.hibernate.auction.model.Customer;
import org.hibernate.auction.model.CustomerLocation;
import org.jdesktop.jdnc.incubator.rbair.JNForm;
import org.jdesktop.jdnc.incubator.rbair.JNTable;
import org.jdesktop.jdnc.incubator.rbair.swing.Application;
import org.jdesktop.jdnc.incubator.rbair.swing.JXFrame;
import org.jdesktop.jdnc.incubator.rbair.swing.JXRootPane;
import org.jdesktop.jdnc.incubator.rbair.swing.JXSearchPanel;
import org.jdesktop.jdnc.incubator.rbair.swing.binding.BindException;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.JavaBeanDataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.MetaData;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.Filter;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.FilterPipeline;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.PatternFilter;
import org.jdesktop.jdnc.incubator.rbair.swing.form.RowSelector;
import org.jdesktop.jdnc.incubator.rbair.swing.table.TableColumnExt;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author Gilles Philippart
 */
public class DemoCaveatEmptor {


    private static final Color BACKGROUND = new Color(238, 238, 238);
    public static final Color BACKGROUND2 = new Color(236, 236, 237);
    public static final Color BACKGROUND3 = new Color(254, 254, 254);

    public static void main(String[] args) {
        try {
            new DemoCaveatEmptor();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    public DemoCaveatEmptor() throws IntrospectionException {
        Application app = Application.getInstance();
        JXFrame frame = new JXFrame("CaveatEmptor JDNC/Hibernate Demo");
        app.registerWindow(frame);
        JXRootPane rootPane = frame.getRootPaneExt();
        // note: JXFrame should have a setToolBar() method
        JToolBar toolbar = new JToolBar();
        rootPane.setToolBar(toolbar);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setBackground(BACKGROUND);
        frame.getContentPane().add(BorderLayout.CENTER, splitPane);

        MetaData[] metaDatas = new MetaData[]{
            new MetaData("id", String.class, "Customer id"),
            new MetaData("customerLocation.one", String.class, "Customer location one"),
            new MetaData("customerLocation.two", String.class, "Customer location two")
        };
        JavaBeanDataModel dataModel = new JavaBeanDataModel(Customer.class, metaDatas);
        ArrayList beans = new ArrayList();
        beans.add(createCustomer(123445, "Paris", "Dublin"));
        beans.add(createCustomer(12412, "New York", "Geneva"));
        dataModel.appendData(beans);
        JNTable table = initializeTable(new JavaBeanTableModelAdapter(dataModel, new String[]{"id", "customerLocation.one"}));
        splitPane.setTopComponent(table);
        JNForm form = initializeForm(table, dataModel);
        splitPane.setBottomComponent(form);
        frame.pack();
        frame.setVisible(true);
    }

    private Customer createCustomer(int value, String one, String two) {
        Customer customer = new Customer();
        customer.setId(new Long(value));
        CustomerLocation customerLocation = new CustomerLocation();
        customerLocation.setOne(one);
        customerLocation.setTwo(two);
        customer.setCustomerLocation(customerLocation);
        return customer;
    }

    protected JNForm initializeForm(JNTable table, DataModel dataModel) {
        JNForm form = new JNForm();
        form.setBackground(BACKGROUND3);
        // ensure table selection will automatically load selected record into form
        new RowSelector(table.getTable(), dataModel);
        try {
            form.bind(dataModel);
        } catch (BindException e) {
            e.printStackTrace();
        }
        return form;
    }

    protected JNTable initializeTable(TableModel model) {
        JNTable table = new JNTable();
        table.setBackground(BACKGROUND);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setEvenRowBackground(BACKGROUND3);
        table.setShowHorizontalLines(false);
        table.setHasColumnControl(true);
        table.setPreferredVisibleRowCount(12);
        table.setHeaderBackground(BACKGROUND2);

        // need to initGui our own column objects because we want a *re-ordered subset*
        // of those in the model; this is cumbersome -- we need something cleaner
        // to avoid forcing the jtable access and direct TableColumn creation.
        JTable jtable = table.getTable();
        jtable.setAutoCreateColumnsFromModel(false);
        table.setModel(model);

        // "BugID"
        TableColumnExt column = new TableColumnExt(0);
        column.setIdentifier("id");
        table.addColumn(column);
        table.setColumnHorizontalAlignment("id", JLabel.CENTER);

        // "State"
        column = new TableColumnExt(1);
        column.setIdentifier("customerLocation");
        table.addColumn(column);
        table.setColumnHorizontalAlignment("customerLocation", JLabel.CENTER);
        table.setColumnPrototypeValue("customerLocation", "some location");

        return table;
    }

    protected void setupFiltering(JNTable table, JToolBar toolbar) {
        Filter filters[] = new Filter[1];
        filters[0] = new PatternFilter("", Pattern.CASE_INSENSITIVE, 6);
        table.setFilters(new FilterPipeline(filters));
        JXSearchPanel searchPanel = new JXSearchPanel();
        searchPanel.setPatternFilter((PatternFilter) filters[0]);
        searchPanel.setTargetComponent(table.getTable());
        toolbar.add(searchPanel);
    }

}
