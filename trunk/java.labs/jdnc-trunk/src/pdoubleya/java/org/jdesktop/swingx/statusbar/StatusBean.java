package org.jdesktop.swingx.statusbar;
import javax.swing.JComponent;


/**
 * A <CODE>StatusBean</CODE> is a <CODE>JComponent</CODE> that can be added to a JXStatusBeanBar.
 * Each bean is used to display some information to the user, like a string message, a 
 * clock, etc. The simplest StatusBean implementation is a JXLabelStatusBean, displaying
 * a JLabel.
 * 
 * @author Patrick Wright
 */
public interface StatusBean {
    /**
     * Returns the name of the bean for description. Each bean should have a default name,
     * which the user could override.
     *
     * @return A descriptive name for this bean.
     */
    String getName();
    
    /**
     * Returns the <CODE>JComponent</CODE> that is added to the status bar. There are no restrictions, except
     * that the components are displayed on one single line. They can have listeners, etc. Components
     * set their own size, but position on the status bar is determined on adding the bean to the
     * status bar.
     *
     * @return The <CODE>JComponent</CODE> used for display on the status bar.
     */
    JComponent getDisplayComponent();
}
