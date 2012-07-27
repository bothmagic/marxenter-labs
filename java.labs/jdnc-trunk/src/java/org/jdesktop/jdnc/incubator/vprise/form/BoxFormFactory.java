package org.jdesktop.jdnc.incubator.vprise.form;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import org.jdesktop.swing.form.*;
import org.jdesktop.swing.data.MetaData;
import org.jdesktop.jdnc.incubator.vprise.layout.box.*;
import org.jdesktop.jdnc.incubator.vprise.actions.*;

/**
 * This class is intended to replace the DefaultFormFactory (merge with it).
 * It "fixes" the layout within the default form factory both to give the user
 * more control over the look of the form (while still providing a decent default)
 * and it has better i18n support. Specifically BiDi is taken care of automatically
 * by using the BoxLayout internally which is BiDi compliant.
 *
 * @author Shai Almog
 */
public class BoxFormFactory extends DefaultFormFactory {
    private static Icon requiredIcon;
    
    /**
     * This variable needs to be a part of the form. Once the form
     * is refactored this should move there and allow us to customize it.
     */
    private static FormTemplate defaultFormTemplate = new FormTemplate();
            
    static {
        // this line may fail, I had some problems with it...
        URL url = DefaultFormFactory.class.getResource("/org/jdesktop/swingx/form/resources/asterisk.8x8.png");
        requiredIcon = new ImageIcon(url);
    }
    
    /** Creates a new instance of SpringFormFactory */
    public BoxFormFactory() {
    }
    
    
    /**
     * This code is ugly since we have no way of knowing if a component contains an
     * undoable edit dispatching event capability. 
     */
    private void bindComponentUndo(JComponent cmp) {
        try {
            if(cmp instanceof JTextComponent) {
                ((JTextComponent)cmp).getDocument().addUndoableEditListener(UndoAction.getInstance());
                return;
            }
            
            EventSetDescriptor[] desc = Introspector.getBeanInfo(cmp.getClass()).getEventSetDescriptors();
            for (int i = 0; i < desc.length; i++) {
                if ("addUndoableEditListener".equals(desc[i].getAddListenerMethod().getName())) {
                    desc[i].getAddListenerMethod().invoke(cmp, new Object[] {UndoAction.getInstance()});
                    return;
                }
            }

            PropertyUndoableEdit.bind(cmp);
        } catch(Exception err) {
            err.printStackTrace();
        }
    }
    
    public void addComponent(JComponent parent, JComponent component,
                             MetaData metaData) {
        bindComponentUndo(component);
        initializeTrueParent(parent);
        
        Component[] cmps = parent.getComponents();
        if(cmps.length > 1) {
            int count = ((InternalBox)cmps[0]).getComponentCount();
            for(int iter = 1 ; iter < cmps.length ; iter++) {
                if(((InternalBox)cmps[iter]).getComponentCount() < count) {
                    ((InternalBox)cmps[iter]).addComponent(parent, (JComponent)cmps[iter], 
                                component, metaData);
                    return;
                }
            }
        }
        ((InternalBox)cmps[0]).addComponent(parent, (JComponent)cmps[0], 
                    component, metaData);
    }
    
    private BoxLayout initializeTrueParent(JComponent parent) {
        FormTemplate formTemplate;
        if(parent instanceof VForm) {
            formTemplate = ((VForm)parent).getTemplate();
        } else {
            formTemplate = defaultFormTemplate;
        }
        
        LayoutManager layout = parent.getLayout();
        if (!(layout instanceof BoxLayout)) {
            layout = new BoxLayout(parent, BoxLayout.LINE_AXIS);
            parent.setLayout(layout);
            for(int iter = 0 ; iter < formTemplate.getColumns() ; iter++) {
                parent.add(new InternalBox(formTemplate));
            }
        }
        return (BoxLayout)layout;
    }

    /**
     * This class implements the layout within the form
     */
    static class InternalBox extends Box {
        /**
         * These two variables are used for caching and improve preformance
         * as long as forms are created in a single sequence on a single thread
         * (this is unlikely to change). They produce a small leak which can 
         * easily be solved by an event from the FormFactory.
         */
        private JComponent currentParent;
        private Collection currentLabels;

        /**
         * Again this should really be a part of the form
         */
        private FormTemplate formTemplate;
        public InternalBox(FormTemplate formTemplate) {
            super(BoxLayout.Y_AXIS);
            this.formTemplate = formTemplate;
        }
        
        /**
         * Returns a list of the labels within the parent component
         */
        private Collection listLabels(JComponent parent) {
            if(currentParent == parent) {
                return currentLabels;
            }
            currentLabels = new ArrayList();
            Component[] cmps = parent.getComponents();
            for(int iter = 0 ; iter < cmps.length ; iter++) {
                if(cmps[iter] instanceof Box) {
                   // the label should be the second component after the horizontal strut
                   Component current = ((Box)cmps[iter]).getComponent(1);
                   if(current instanceof JLabel) {
                       currentLabels.add(current);
                   }
                }
            }
            return currentLabels;
        }

        public void addComponent(JComponent realParent, JComponent parent, JComponent component,
                                 MetaData metaData) {

            initializeLayout(parent);

            Box row = new Box(BoxLayout.LINE_AXIS);
            JLabel label;

            // the reason for creating two labels (the actual label
            // and a ":" label) is to allow a component to change
            // its orientation after it was created. Otherwise the label
            // would be created in the reverse order and component
            // orientation would have no effect.
            if (metaData.isRequired()) {
                label = new JLabel(metaData.getLabel(), requiredIcon,
                                   JLabel.CENTER);
                label.setHorizontalTextPosition(JLabel.TRAILING);
            }
            else {
                label = new JLabel(metaData.getLabel());
            }
            row.add(Box.createHorizontalStrut(formTemplate.getInitialX()));
            row.add(label);
            row.add(new JLabel(":"));
            row.add(Box.createHorizontalStrut(formTemplate.getXPad()));

            listLabels(parent).add(label);

            Font boldFont = label.getFont().deriveFont(Font.BOLD);
            label.setFont(boldFont);
            label.setHorizontalAlignment(JLabel.TRAILING);
            label.setLabelFor(component);
            row.add(component);

            // make sure to push the component so that if it does not define a large
            // maximum size it will still align correctly
            row.add(Box.createGlue());

            row.add(Box.createHorizontalStrut(formTemplate.getXPad()));
            parent.add(Box.createVerticalStrut(formTemplate.getInitialY()));
            parent.add(row);
            parent.add(Box.createVerticalStrut(formTemplate.getYPad()));

            // align the components according to the labels
            Collection labels = listLabels(parent);
            BoxUtilities.alignWidth(labels);

            // make sure text components don't get out of hand
            if(component instanceof JTextComponent) {
                Dimension max = component.getMaximumSize();
                max.height = component.getPreferredSize().height;
                component.setMaximumSize(max);
            }
        }
        
        private BoxLayout initializeLayout(JComponent parent) {
            LayoutManager layout = parent.getLayout();
            if (!(layout instanceof BoxLayout)) {
                layout = new BoxLayout(parent, BoxLayout.Y_AXIS);
                parent.setLayout(layout);
            }
            return (BoxLayout)layout;
        }
    }
}
