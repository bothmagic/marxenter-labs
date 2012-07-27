/*
 * Created on 11.03.2008
 *
 */
package org.jdesktop.appframework.goodies.album;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;

/**
 * This test demonstrates if JGoodies binding is playing nicely with JXList
 * filtering. I couldn't get this to work for employees, so I decided to try it
 * here first.
 * 
 * @author martins
 */
public class JXListFilterBindingTest {
    public static void main(String[] args) throws Exception {
        // data
        final ArrayListModel<String> dataList = new ArrayListModel<String>();
        for (Map.Entry<Object, Object> entry : System.getProperties()
                .entrySet())
            dataList.add(entry.getKey().toString());

        final DefaultListModel model = new DefaultListModel();
        for (String string : dataList) {
            model.addElement(string);
        }
        final SelectionInList<String> dataSelection = new SelectionInList<String>(
                (ListModel) dataList);
        ValueHolder valueModel = new ValueHolder();
        valueModel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
//                pf.setPattern(evt.getNewValue().toString(),
//                        Pattern.CASE_INSENSITIVE);
            }
        });

        // components
        JFrame frame = new JXFrame("xList", true);
        LayoutManager borderLayout = new BorderLayout();
        frame.setLayout(borderLayout);

        JTextField textBox = new JTextField();
        textBox.setPreferredSize(new java.awt.Dimension(100, 30));
        final JXList list = new JXList();
        final JTextField itemToAdd = new JTextField();
        JButton addItem = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                String newSelection = itemToAdd.getText();
                model.addElement(newSelection);
                dataList.add(newSelection);
//                list.getFilters().flush();
                list.setSelectedValue(newSelection, true);
                //                                pf.refresh();

//                int viewIndex = list.convertIndexToView(dataList
//                        .indexOf(newSelection));
//                if (viewIndex != -1) {
////                    list.setSelectedIndex(viewIndex); // this works
//                      dataSelection.setSelection(newSelection); // this doesn't
//
//                }
            }
        });
        addItem.setText("Add");
        JButton flush = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Object newSelection = dataList
                        .getElementAt(dataList.getSize() - 1);
//                list.getFilters().flush();
                int viewIndex = list.convertIndexToView(dataList
                        .indexOf(newSelection));
                if (viewIndex >= 0) {
                    list.setSelectedIndex(viewIndex);
                }
            }
        });
        flush.setText("flush");
        // events
        list.setAutoCreateRowSorter(true);
//        list.setModel(model);
        Bindings.bind(list, dataSelection);
        Bindings.bind(textBox, valueModel);
//        list.setFilters(new FilterPipeline(pf));
        // layout
        frame.getContentPane().add(textBox, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
        frame.add(itemToAdd, BorderLayout.SOUTH);
        frame.getContentPane().add(addItem, BorderLayout.WEST);
        frame.add(flush, BorderLayout.EAST);
        frame.pack();
        frame.setLocationRelativeTo(null);
        // done
        frame.setVisible(true);
    }
}


