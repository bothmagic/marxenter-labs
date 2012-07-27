/*
 * Created on 22.06.2007
 *
 */
package org.jdesktop.beansbindingx;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

/**
 * 
 */
public class LabelHandler {
    
    public void add(JLabel label, JComponent component) {
        label.setLabelFor(component);
        Binding binding =  Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                component, BeanProperty.create("enabled"), 
                label, BeanProperty.create("enabled"));
        binding.bind();
    }
}
