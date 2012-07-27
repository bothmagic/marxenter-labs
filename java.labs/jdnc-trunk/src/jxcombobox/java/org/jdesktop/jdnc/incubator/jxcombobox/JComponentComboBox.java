package org.jdesktop.jdnc.incubator.jxcombobox;

import javax.swing.*;

/**
 * <p>
 * This is an enhanced combobox that lets the developer replace the
 * component that is shown inside the popup menu.
 * </p>
 * <p>
 * Usage example:
 * </p>
 * <code>
 * JComponentComboBox comboBox = new JComponentComboBox();<br/>
 * JComponent yourPopupComponent = [...];<br/>
 * try {<br/>
 * &nbsp;&nbsp;comboBox.setPopupComponent(yourPopupComponent);<br/>
 * } catch (IncompatibleLookAndFeelException e) {<br/>
 * &nbsp;&nbsp;[...]<br/>
 * }<br/>
 * </code>
 * <p><i>
 * This class is based on the standard {@link javax.swing.JComboBox JComboBox}
 * and can possibly fail to replace the popup component on special look&feel
 * implementations. However, no such look&feel is known up to now.
 * </i></p>
 *
 * @author Thomas Bierhance
 */
public class JComponentComboBox extends JComboBox {
    JComponent component;
    
    /**
     * Creates a new JComponentComboBox object.
     */
    public JComponentComboBox() {
        super();
    }
    
    /**
     * This method tries to access the combobox' ComboPopup object.
     * It does so by looking for an instance of
     * {@link javax.swing.plaf.basic.BasicComboPopup BasicComboPopup}
     * using the {@link javax.accessibility Accessibility API}.
     * @return the combobox' ComboPopup object
     */
    private javax.swing.plaf.basic.BasicComboPopup getComboPopup() {
        for (int i=0, n=getUI().getAccessibleChildrenCount(this); i<n; i++) {
            Object component = getUI().getAccessibleChild(this, i);
            if (component instanceof javax.swing.plaf.basic.BasicComboPopup) {
                return (javax.swing.plaf.basic.BasicComboPopup) component;
            }
        }
        return null;
    }
    
    /**
     * Returns the component that is displayed inside the combobox' popup
     * menu. However, if no custom component has been set using
     * {@link #setPopupComponent(javax.swing.JComponent) setPopupComponent}
     * this method returns <code>null</code>.
     * @return the component that is displayed inside the popup menu
     * or <code>null</code> if no such component has been specifically set.
     */
    public JComponent getPopupComponent() {
        return component;
    }
    
    /**
     * Sets the component that is displayed inside the combobox' popup menu.
     * @param component the component that should be displayed inside the popup menu
     * @throws org.jdesktop.jdnc.incubator.bierhance.IncompatibleLookAndFeelException if the popup component could not be set due to the look&amp;feel in use.
     */
    public void setPopupComponent(JComponent component) throws IncompatibleLookAndFeelException {
        javax.swing.plaf.basic.BasicComboPopup comboPopup=getComboPopup();
        // getComboPopup() returns null if it can not find a BasicComboPopup
        if (comboPopup!=null) {
            comboPopup.removeAll();
            comboPopup.add(component);
            this.component = component;
        } else {
            throw new IncompatibleLookAndFeelException("Could not modify the combo box' popup menu: Try a different look&feel.");
        }
    }
    
    public void updateUI() {
        // let JComboBox update its UI
        super.updateUI();
        // By now, most likely the popup menu now has been reseted by the default UI.
        // Therefore, the custom component has been removed from the popup menu
        // and the component's UI hasn't been updated.
        try {
            if (component!=null) {
                // update the component's UI
                // component.updateUI() would not update components inside simple components (e.g. based on JPanel)
                // possible alternative: call this.add(component) inside setPopupComponent
                SwingUtilities.updateComponentTreeUI(component);
                // reset the popup component to the custom component
                setPopupComponent(component);
            }
        } catch(IncompatibleLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }
}