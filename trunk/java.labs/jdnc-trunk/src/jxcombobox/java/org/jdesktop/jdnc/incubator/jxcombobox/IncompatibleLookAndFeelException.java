package org.jdesktop.jdnc.incubator.jxcombobox;

import javax.swing.UIManager;

/**
 * 
 * @author Thomas Bierhance
 */
public class IncompatibleLookAndFeelException extends Exception {
    
    public IncompatibleLookAndFeelException(String message) {
        super(message);
    }
    
    public String getLookAndFeelName() {
        return UIManager.getLookAndFeel().getName();
    }
    
}
