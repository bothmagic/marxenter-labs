/*
 * JXLabelStatusBean.java
 *
 * Created on August 19, 2005, 1:14 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.swingx.statusbar;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;


/**
 *
 * @author patrick
 */
public class JXLabelStatusBean implements StatusBean {
    protected String beanName;
    private JLabel label;
    
    public JXLabelStatusBean() {
        this("Unnamed StatusBean");
    }
    
    /**
     * Creates a new instance of JXLabelStatusBean 
     */
    public JXLabelStatusBean(String name) {
        this.label = new JLabel();
        this.beanName = name;
        
        // CLEAN: until spacing is correct, border makes it look ugly
        this.label.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // TODO: need to set the preferred size based on the text
        this.label.setPreferredSize(new Dimension(75, 19));
    }
    
    public static JXLabelStatusBean newSpacerBean() {
        JXLabelStatusBean bean = new JXLabelStatusBean("spacer");
        bean.label.setText("");
        return new JXLabelStatusBean("spacer");
    }
    
    public String getName() {
        return beanName;
    }
    
    public JComponent getDisplayComponent() {
        return label;
    }
    
    protected JLabel getLabel() {
        return label;
    }
}
