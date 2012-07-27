/*
 * Created on 21.06.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.appframework.swingx.BBColumnFactory;
import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableCollections.ObservableListHelper;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.table.ColumnFactory;

/**
 * Bind album list and navigation ComboBox. 
 * Issue: no detail binding for combo, need custom renderer.
 */
public class SimpleCombo2Binding {

    private JComponent content;
    private JComboBox comboBox;
    private JTextField textField;
    private JTextField colorField;
    private Person person;


    private void bindBasics() {
        BindingGroup context = new BindingGroup();
        comboBox.setEditable(true);
        JComboBoxBinding comboBoxBinding = SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
                ColorDesc.getColorList(), comboBox);
        context.addBinding(comboBoxBinding);
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                comboBox, BeanProperty.create("selectedItem"),
                person, BeanProperty.create("colorDesc")));
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                person, BeanProperty.create("name"), 
                textField, BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST")));
        // WRITE doesn't make sense -- the colorDesc is immutable
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
//                comboBox, BeanProperty.create("selectedItem.description"),
                person, BeanProperty.create("colorDesc.description"), 
                colorField, BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST")));
        colorField.setEditable(false);
        context.bind();
        StringValue sv = new StringValue() {

            Property property = BeanProperty.create("description");
            @SuppressWarnings("unchecked")
            public String getString(Object source) {
                return (source == null) ? "" :
                        StringValues.TO_STRING.getString(property.getValue(source)); 
            }
            
        };
        comboBox.setRenderer(new DefaultListRenderer(sv));
    }

    private void initData() {
        person = Person.persons[0];
    }

    private JComponent getContent() {
        if (content == null) {
            ColumnFactory.setInstance(new BBColumnFactory());
            initComponents();
            content = build();
            initData();
            bindBasics();
        }
        return content;
    }

    private JComponent build() {
        JComponent comp = Box.createVerticalBox();
        comp.add(new JScrollPane(comboBox));
        comp.add(colorField);
        comp.add(textField);
        return comp;
    }


    private void initComponents() {
        comboBox = new JComboBox();
        textField = new JTextField();
        colorField = new JTextField();
    }

    public static class Person extends AbstractBean {
        static Person[] persons = new Person[] {
            new Person("firstname", ColorDesc.colors[0]),
            new Person("secondname", ColorDesc.colors[1]),
            new Person("thirdname", ColorDesc.colors[2]),
        };
        
        static List<Person> personList = Arrays.asList(persons);
        
        static List<Person> getPersonList() {
            ObservableListHelper<Person> helper = ObservableCollections.observableListHelper(personList);
            return helper.getObservableList();
        };
        
        String name;
        ColorDesc colorDesc;
        public Person(String name, ColorDesc colorDesc) {
            this.name = name;
            this.colorDesc = colorDesc;
        }
        public ColorDesc getColorDesc() {
            return colorDesc;
        }
        public void setColorDesc(ColorDesc colorDesc) {
            ColorDesc old = getColorDesc();
            this.colorDesc = colorDesc;
            firePropertyChange("colorDesc", old, getColorDesc());
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            String old = getName();
            this.name = name;
            firePropertyChange("name", old, getName());
        }
        
        
    }
    public static class ColorDesc  {
        static ColorDesc[] colors = new ColorDesc[] {
            new ColorDesc(300, "first"),
            new ColorDesc(400, "second"),
            new ColorDesc(500, "third"),
        };

        static List<ColorDesc> colorList = Arrays.asList(colors);
        
        static List<ColorDesc> getColorList() {
            ObservableListHelper<ColorDesc> helper = ObservableCollections.observableListHelper(colorList);
            return helper.getObservableList();
        };

        int color;
        String description;

        public ColorDesc(int color, String description) {
            this.color = color;
            this.description = description;
        }

        public int getColor() {
            return color;
        }

        public String getDescription() {
            return description;
        }

//        @Override
//        public boolean equals(Object other) {
//            return other instanceof ColorDesc
//            && ((ColorDesc)other).color == color;
//        }
//        
//        public String toString() {
//            return getDescription();
//        }
    }

    public static void main(String[] args) {
        
        final JXFrame frame = new JXFrame("Bind Album List", true);
        ColumnFactory.setInstance(new BBColumnFactory());
        frame.add(new SimpleCombo2Binding().getContent());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               frame.pack();
//               frame.setSize(800, 600);
               frame.setLocationRelativeTo(null);
               frame.setVisible(true);
            }
        });
    }

}
