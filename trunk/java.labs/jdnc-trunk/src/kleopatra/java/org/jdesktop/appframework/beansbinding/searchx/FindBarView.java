/*
 * Created on 14.09.2007
 *
 */
package org.jdesktop.appframework.beansbinding.searchx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jdesktop.appframework.FormView;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

public class FindBarView implements FormView {

    private FindModel findModel;

    protected JLabel searchLabel;
    protected JTextField searchField;
    protected JCheckBox matchCheck;
    protected JCheckBox wrapCheck;
    protected JCheckBox backCheck;

    protected JButton findNext;
    protected JButton findPrevious;

    protected Color previousBackgroundColor;
    protected Color previousForegroundColor;

    // PENDING: need to read from UIManager
    protected Color notFoundBackgroundColor = Color.decode("#FF6666");
    protected Color notFoundForegroundColor = Color.white;

    private JComponent content;

    public FindBarView(FindModel findModel) {
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
        findNext.setAction(map.get("findNext"));
        findPrevious.setAction(map.get("findPrevious"));
        searchField.setAction(map.get("match"));
    }

//----------------- manage found notification
    
    public void setUnfound(boolean found) {
        Color foreground = found ? notFoundForegroundColor : previousForegroundColor ;
        Color background = found ? notFoundBackgroundColor : previousBackgroundColor;
        searchField.setForeground(foreground);
        searchField.setBackground(background);
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
        return "findView";
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
        previousBackgroundColor = searchField.getBackground();
        previousForegroundColor = searchField.getForeground();
        matchCheck = new JCheckBox();
        matchCheck.setName("matchCase");
        wrapCheck = new JCheckBox();
        wrapCheck.setName("wrapSearch");
        backCheck = new JCheckBox();
        backCheck.setName("backwardsSearch");
        findNext = new JButton();
        findPrevious = new JButton();
    }

    private JComponent build() {
        initComponents();
        JComponent panel = new JPanel();
        panel.setName("searchTitle");
        panel.setLayout(new FlowLayout(SwingConstants.LEADING));
        panel.add(searchLabel);
        panel.add(new JLabel(":"));
        panel.add(new JLabel("  "));
        panel.add(searchField);
        panel.add(findNext);
        panel.add(findPrevious);
        return panel;
    }


}
