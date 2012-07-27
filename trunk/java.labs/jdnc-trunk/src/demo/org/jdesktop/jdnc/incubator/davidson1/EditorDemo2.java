/*
 * $Id: EditorDemo2.java 147 2004-10-29 17:01:32Z davidson1 $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

import org.jdesktop.swing.Application;
import org.jdesktop.swing.JXEditorPane;
import org.jdesktop.swing.JXFrame;
import org.jdesktop.swing.JXRootPane;
import org.jdesktop.swing.JXStatusBar;

import org.jdesktop.swing.actions.AbstractActionExt;
import org.jdesktop.swing.actions.ActionFactory;
import org.jdesktop.swing.actions.ActionManager;

// Experimental xml stuff
import org.jdesktop.jdnc.incubator.davidson1.actions.XMLActionFactory;

/**
 * A variation on the editor demo in the JDNC swingx sub-project that can
 * contruct actions/menus/toolbars/popups from the JDNC XML schema
 *
 * @author Mark Davidson
 */
public class EditorDemo2 {

    private JXFrame frame;

    public EditorDemo2() {
        initActions();
        initUI();

        frame.setVisible(true);
    }

    /**
     * Load all the managed actions into the action manager. This is property
     * oriented and kind of tedious. This is the sort of problem that that xml is good for.
     */
    protected void initActions() {
        XMLActionFactory factory = XMLActionFactory.getInstance();
        factory.loadActions(EditorDemo2.class.getResource("resources/editorDemo.jdnc"));
    }

    protected void initUI() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    EditorDemo2 demo = new EditorDemo2();
                }
            });
    }
}

