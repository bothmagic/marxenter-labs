package org.jdesktop.jdnc.incubator.vprise.actions;

import org.jdesktop.jdnc.incubator.vprise.i18n.*;
import org.jdesktop.jdnc.incubator.vprise.util.*;
import org.jdesktop.swing.form.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

/**
 * This is a generic exit action that should be invoked whenever the
 * user tries to close a window, it should also be placed within the
 * toolbar/menu bar.
 * The action tries to do the "right thing(tm)" by verifying that no
 * open Windows have modified data. This class also acts as a Window
 * listener to determine if the window has modified data and to determine
 * whether this is the last Window closing thus triggering an application
 * exit.
 *
 * @author Shai Almog
 */
public class ExitAction extends AbstractAction implements WindowListener {
    
    /** Creates a new instance of ExitAction */
    public ExitAction() {
        if(PlatformUtility.getInstance().isMac()) {
            // notice that mac's don't use mnemonics
            putValue(NAME, Resources.getString("action.quit", "Quit"));
            putValue(SHORT_DESCRIPTION, Resources.getString("action.quit.short", "Quit Application"));
            putValue(LONG_DESCRIPTION, Resources.getString("action.quit.long", "Quit Application"));
        } else {
            putValue(NAME, Resources.getString("action.exit", "Exit"));
            putValue(SHORT_DESCRIPTION, Resources.getString("action.exit.short", "Exit Application"));
            putValue(LONG_DESCRIPTION, Resources.getString("action.exit.long", "Exit Application"));
            putValue(MNEMONIC_KEY, new Integer(Resources.getString("action.exit.mnemonic", "x").charAt(0)));
        }
    }

    /**
     * Returns true if the user should bind the exit action to a menu.
     * Otherwise the platform is probably responsible for the exit action
     * and doesn't require the user to bind it (such as a Mac that handles
     * its own Quit action).
     */
    public boolean bind() {
        if(PlatformUtility.getInstance().isMac()) {
            PlatformUtility.getInstance().bindQuitMac(this);
            return false;
        } 
        return true;        
    }

    private void promptExit(Component cmp) {
        Component[] frames = JFrame.getFrames();
        for(int iter = 0 ; iter < frames.length ; iter++) {
            JForm form = findForm(frames[iter]);
            if((form != null) && (form.isModified())) {
                int result = JOptionPane.showConfirmDialog(cmp, Resources.getString("action.exit.are_you_sure", "Are you sure"), 
                            (String)getValue(NAME), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if(result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                return;
            }
        }
        System.exit(0);
    }
    
    public void actionPerformed(ActionEvent ev) {
        promptExit((Component)ev.getSource());
    }
    
    private JForm findForm(Component cmp) {
        if(cmp instanceof JForm) {
            return (JForm)cmp;
        }
        if(cmp instanceof Container) {
            Component[] cmps = ((Container)cmp).getComponents();
            for(int iter = 0 ; iter < cmps.length ; iter++) {
                JForm form = findForm(cmps[iter]);
                if(form != null) {
                    return form;
                }
            }
        }
        return null;
    }

    public void windowClosing(WindowEvent windowEvent) {
        if(JFrame.getFrames().length > 1) {
            JForm form = findForm((Component)windowEvent.getSource());
            if(form.isModified()) {
                int result = JOptionPane.showConfirmDialog((JComponent)windowEvent.getSource(), 
                            Resources.getString("action.exit.unsaved_are_you_sure", "You have unsaved changes.\nAre you sure"), 
                            Resources.getString("action.exit.unsaved", "You Have Unsaved Changes"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if(result == JOptionPane.YES_OPTION) {
                    ((Window)windowEvent.getSource()).dispose();
                }
            }
        } else {
            promptExit((Component)windowEvent.getSource());
        }
    }

    
    
    /**
     * Part of the Window listener interface
     */
    public void windowOpened(WindowEvent windowEvent) {
    }

    /**
     * Part of the Window listener interface
     */
    public void windowIconified(WindowEvent windowEvent) {
    }

    /**
     * Part of the Window listener interface
     */
    public void windowDeiconified(WindowEvent windowEvent) {
    }

    /**
     * Part of the Window listener interface
     */
    public void windowDeactivated(WindowEvent windowEvent) {
    }

    /**
     * Part of the Window listener interface
     */
    public void windowClosed(WindowEvent windowEvent) {
    }

    /**
     * Part of the Window listener interface
     */
    public void windowActivated(WindowEvent windowEvent) {
    }
}
