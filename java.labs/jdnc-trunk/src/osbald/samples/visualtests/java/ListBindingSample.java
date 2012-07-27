import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Created by IntelliJ IDEA.
 * User: rosbaldeston
 * Date: 16-Oct-2007
 * Time: 10:21:08
 */

public class ListBindingSample extends AbstractBean {
    BindingGroup context = new BindingGroup();
    JList list;
    String selected;

    List<String> defaults1 = Arrays.asList("Adam Barlow",
            "Amy Barlow",
            "David Barlow",
            "Deirdre Barlow",
            "Frank Barlow",
            "Ida Barlow",
            "Irma Barlow",
            "Janet Barlow",
            "Ken Barlow",
            "Susan Barlow",
            "Tracy Barlow",
            "Valerie Barlow");

    List<String> defaults2 = Arrays.asList(
            "Janice Battersby",
            "Leanne Battersby",
            "Toyah Battersby",
            "Chesney Battersby-Brown",
            "Cilla Battersby-Brown",
            "Les Battersby-Brown");

    ObservableCollections.ObservableListHelper<String> helper =
            ObservableCollections.observableListHelper(new ArrayList<String>(defaults1));
    List values = helper.getObservableList();
    List<Reference> history = new ArrayList<Reference>();

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ListBindingSample();
            }
        });
    }

    public ListBindingSample() {
        final JFrame frame = new JFrame("Binding Experiment");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(createView());
        bind();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    JComponent createView() {
        JPanel content = new JPanel(new BorderLayout(5, 5));
        list = new JList();
        list.setVisibleRowCount(8);
        content.add(new JScrollPane(list));

        final JToggleButton changeButton = new JToggleButton(new AbstractAction("Change Data") {
            public void actionPerformed(ActionEvent e) {
                if (((JToggleButton) e.getSource()).isSelected()) {
                    setValues(defaults2);
                } else {
                    setValues(defaults1);
                }
            }
        });
        content.add(changeButton, BorderLayout.PAGE_END);
        return content;
    }

    void bind() {

        //alt. JListBinding listBinding = SwingBindings.createJListBinding(AutoBinding.UpdateStrategy.READ_WRITE, values, list);

        BeanProperty<Object, List<String>> itemsProperty = BeanProperty.create("values");
        JListBinding listBinding = SwingBindings.createJListBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, itemsProperty, list);

        context.addBinding(listBinding);

        context.addBinding(Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
                list, BeanProperty.create("selectedElement_IGNORE_ADJUSTING"),
                this, BeanProperty.create("selected")));
        context.bind();
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public List getValues() {
        return values;
    }

    public void setValues(List values) {
/* alt.
        this.values.clear();
        this.values.addAll(values);
*/
        history.add(new WeakReference(this.values));
        firePropertyChange("values", this.values, this.values = ObservableCollections.observableList(values));

        new Thread(new Runnable() {
            public void run() {
                System.gc();
            }
        }).start();
    }
}
