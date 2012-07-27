/*
 * Created on 26.03.2008
 *
 */
package org.jdesktop.beansbindingx.example;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;

public class BBOConnor extends InteractiveTestCase {

    public static void main(String[] args) {
        BBOConnor test = new BBOConnor();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void interactivePOJOBinding() {
        final Person person = new Person("baby", 0);
        Action action = new AbstractAction("Add year") {

            public void actionPerformed(ActionEvent e) {
                person.setAge(person.getAge() + 1);
                
            }
        };
        JSlider age = new JSlider(0, 10, 0);
        Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ, 
                person, BeanProperty.create("age"),
                age, BeanProperty.create("value"));
        binding.bind();
        JButton button = new JButton(action);
        JPanel panel = new JPanel();
        panel.add(button);
        panel.add(age);
        JCheckBox box = new JCheckBox("something");
        box.addPropertyChangeListener(new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // TODO Auto-generated method stub
                System.out.println(evt.getPropertyName());
            }
        });
        panel.add(box);
        showInFrame(panel, "pojo binding");
    }
    
    public static class Person extends AbstractBean {
        private String name;
        private int age;
        
        public Person() {        
        }
        
        public Person(String name, int age ) {
            this.name = name;
            this.age = age;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public void setAge(int age) {
            int old = getAge();
            this.age = age;
            firePropertyChange("age", old, getAge());
        }
        
        public int getAge() {
            return age;
        }
    }

}
