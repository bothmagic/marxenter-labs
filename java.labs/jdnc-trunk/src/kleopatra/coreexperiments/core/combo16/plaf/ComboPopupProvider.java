/*
 * Created on 19.03.2009
 *
 */
package core.combo16.plaf;

import javax.swing.plaf.basic.ComboPopup;

/**
 * Interface that provider of a ComboBox must implement. A typical provider is
 * a ComboBoxUI implementation. As core XXComboBoxUI have a protected field with no
 * getter, we must subclass each and make the popup accessible.<p>
 * 
 * Consumers can be f.i. enhanced combo actions.
 *  
 */
public interface ComboPopupProvider {
    ComboPopup getComboPopup();
}
