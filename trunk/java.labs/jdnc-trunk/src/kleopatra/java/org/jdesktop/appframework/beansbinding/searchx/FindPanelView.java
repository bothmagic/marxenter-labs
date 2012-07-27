/*
 * Created on 14.09.2007
 *
 */
package org.jdesktop.appframework.beansbinding.searchx;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jdesktop.appframework.FormView;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

public class FindPanelView implements FormView {

    private FindModel findModel;

    protected JLabel searchLabel;
    protected JTextField searchField;
    protected JCheckBox matchCheck;
    protected JCheckBox wrapCheck;
    protected JCheckBox backCheck;


    private JComponent content;

    public FindPanelView(FindModel findModel) {
        this.findModel = findModel;
    }
    
    private void bindComponents() {
        BindingGroup group = new BindingGroup();
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                findModel, BeanProperty.create("caseSensitive"), 
                matchCheck, BeanProperty.create("selected")
                 ));
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                findModel, BeanProperty.create("backwards"), 
                backCheck, BeanProperty.create("selected")
                 ));
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                findModel, BeanProperty.create("wrapping"), 
                wrapCheck, BeanProperty.create("selected")
                 ));
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                findModel, BeanProperty.create("rawText"), 
                searchField, BeanProperty.create("text")
                 ));
        // found notification
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                findModel, BeanProperty.create("unfound"), 
                this, BeanProperty.create("unfound")
        ));
        group.bind();
        BindingGroup feedbackGroup = new BindingGroup();
        feedbackGroup.bind();
    }

    
    private void bindActions() {
        ApplicationContext context = Application.getInstance().getContext();
        ActionMap map = context.getActionMap(getActionsObject().getClass(), getActionsObject());
        searchField.setAction(map.get("match"));
    }

//----------------- manage found notification
    
    public void setUnfound(boolean found) {
        if (found)  
            JOptionPane.showMessageDialog(getContent(), "Value not found");

    }
    
//---------------------- implement FormView
    
    public Object getActionsObject() {
        return findModel;
    }

    public JComponent getContent() {
        if (content == null) {
            content = build();
            bindComponents();
            bindActions();
        }
        return content;
    }



    public String getName() {
        return "findPanelView";
    }


    private void initComponents() {
        searchLabel = new JLabel();
        searchLabel.setName("searchFieldName");
        searchField = new JTextField(15){
            public Dimension getMaximumSize() {
                Dimension superMax = super.getMaximumSize();
                superMax.height = getPreferredSize().height;
                return superMax;
            }
        };
        matchCheck = new JCheckBox();
        matchCheck.setName("matchCase");
        wrapCheck = new JCheckBox();
        wrapCheck.setName("wrapSearch");
        backCheck = new JCheckBox();
        backCheck.setName("backwardsSearch");
    }

    private JComponent build() {
        initComponents();

        Box lBox = new Box(BoxLayout.LINE_AXIS); 
        lBox.add(searchLabel);
        lBox.add(new JLabel(":"));
        lBox.add(new JLabel("  "));
        lBox.setAlignmentY(Component.TOP_ALIGNMENT);
        Box rBox = new Box(BoxLayout.PAGE_AXIS); 
        rBox.add(searchField);
        rBox.add(matchCheck);
        rBox.add(wrapCheck);
        rBox.add(backCheck);
        rBox.setAlignmentY(Component.TOP_ALIGNMENT);

        JComponent box = new Box(BoxLayout.LINE_AXIS);
        box.add(lBox);
        box.add(rBox);
        box.setName(getName());
        return box;
    }


}
