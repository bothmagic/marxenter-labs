package notanissue;

import org.jdesktop.beans.AbstractBean;
import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.SwingBindings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class SelectedTest {
    static final Object[] items = TimeZone.getAvailableIDs();
    MyModel model = new MyModel();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SelectedTest();
            }
        });
    }

    public SelectedTest() {
        JFrame f = new JFrame(this.getClass().getName());
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JList list = new JList();
        list.setVisibleRowCount(10);
        final JComboBox selectItemComboBox = new JComboBox(items);
        f.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));
        f.getContentPane().add(new JLabel("Select A List Item"));
        f.getContentPane().add(selectItemComboBox);
        f.getContentPane().add(new JScrollPane(list));
        selectItemComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setSelected(selectItemComboBox.getSelectedItem());
            }
        });

        BindingGroup group = new BindingGroup();
        BeanProperty valuesProperty = BeanProperty.create("values");
        group.addBinding(SwingBindings.createJListBinding(UpdateStrategy.READ_WRITE, model, valuesProperty, list));
        BeanProperty selectedProperty = BeanProperty.create("selected");
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                list, BeanProperty.create("selectedElement_IGNORE_ADJUSTING"),
                model, selectedProperty));
        group.bind();

        f.pack();
        f.setVisible(true);
    }

    public static class MyModel extends AbstractBean {
        public List values = ObservableCollections.observableList(Arrays.asList(items));
        public Object selected;

        public List getValues() {
            return values;
        }

        public void setValues(List values) {
            firePropertyChange("values", this.values, this.values = values);
        }

        public Object getSelected() {
            return selected;
        }

        public void setSelected(Object selected) {
            firePropertyChange("selected", this.selected, this.selected = selected);
        }
    }

    ;
}
