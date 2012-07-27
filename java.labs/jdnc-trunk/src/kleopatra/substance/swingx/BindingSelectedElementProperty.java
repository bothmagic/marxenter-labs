/*
 * Created on 18.12.2009
 *
 */
package swingx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;

public class BindingSelectedElementProperty extends InteractiveTestCase {

    public static void main(String[] args) {
        BindingSelectedElementProperty test = new BindingSelectedElementProperty();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public void interactiveBindingCellProperty() {
        List<MyBean> beans = new ArrayList();
        for (int i = 0; i < 5; i++) {
            beans.add(new MyBean());
        }
        
        JTable table = new JTable();
        JTextField field = new JTextField(20);
        
        BindingGroup group = new BindingGroup();
        JTableBinding tableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE,
                beans, table);
        tableBinding.addColumnBinding(BeanProperty.create("value"));
        group.addBinding(tableBinding);
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, 
                table, ELProperty.create("${selectedElement.value}"), 
                field, BeanProperty.create("text")));
        group.bind();
        
        JXFrame frame = wrapWithScrollingInFrame(table, "binding");
        addStatusComponent(frame, field);
        show(frame);

    }
    
    public static class MyBean extends AbstractBean {
        private String value = "bean " + new Date();

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            Object oldValue = getValue();
            this.value = value;
            firePropertyChange("value", oldValue, getValue());
            
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }
        
        
        
        
    }
}
