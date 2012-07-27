package org.jdesktop.jdnc.incubator.vprise.forms;

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.ComponentOrientation;
import java.awt.Component;
import org.jdesktop.jdnc.incubator.vprise.actions.*;
import org.jdesktop.jdnc.incubator.vprise.form.*;
import org.jdesktop.jdnc.incubator.vprise.model.*;
import org.jdesktop.swing.*;
import org.jdesktop.swing.actions.*;
import org.jdesktop.swing.data.*;
import org.jdesktop.swing.form.*;

import org.jdesktop.jdnc.incubator.vprise.net.*;

/**
 * Main class for a demo test class. This is mostly a copy and paste of 
 * my current code for a simple application
 */
public class Main implements Runnable {
    private boolean bindExit = false;

    
    public static void main(String[] argv) {
        SwingUtilities.invokeLater(new Main());
    }

    /**
     * Main entry point to start the application, placed on the Swing thread
     */
    public void run() {
        try {
            initActions();
            FormFactory.setDefaultFormFactory(new BoxFormFactory());

            JXFrame frame = new JXFrame("Viniasa");
            //frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            JXRootPane root = frame.getRootPaneExt();

            root.setJMenuBar(createMenus());
            root.setToolBar(createToolBar());
            root.setStatusBar(new JXStatusBar());

            FormTemplate template = new FormTemplate();
            template.setColumns(2);
            Address address = new Address(1, "Shai", "Almog", "", "Street", "1", "Tel-Aviv", "0000", "", "Israel");
            VJavaBeanDataModel model = new VJavaBeanDataModel(Address.class, address);
            //model.setFieldOrder(new String[] {"id", "firstName", "surname", "addressLine", "street", "buildingNumber", 
            //            "city", "zipcode", "state", "country"});
            model.setFieldOrder(new String[] {"Id", "First Name", "Surname", 
                    "Address Info", "Street", "Building", "City", "Zip Code", 
                    "State", "Country"});
            VForm frm = new VForm(model, template);
            root.addComponent(frm);
            
            frame.setVisible(true);
        } catch(Exception err) {
            err.printStackTrace();
        }
    }
    
    /**
     * Initializes the action bindings
     */
    private void initActions() {
        ActionManager manager = Application.getInstance().getActionManager();
        manager.addAction(ActionFactory.createTargetableAction("file-menu", "File", "F"));
        manager.addAction(ActionFactory.createTargetableAction("edit-menu", "Edit", "E"));
        manager.addAction(ActionFactory.createTargetableAction("help-menu", "Help", "H"));

        ExitAction e = new ExitAction();
        if(e.bind()) {
            bindExit = true;
            manager.addAction("exit-command", e);
        }
        manager.addAction("bidi-command", createBidiAction());

        action("cut-to-clipboard", "Cut", "C", "Cut to clipboard",
               "/toolbarButtonGraphics/general/Cut16.gif",
               "/toolbarButtonGraphics/general/Cut24.gif", null, false);

        action("copy-to-clipboard", "Copy", "P", "Copy to clipboard",
               "/toolbarButtonGraphics/general/Copy16.gif",
               "/toolbarButtonGraphics/general/Copy24.gif", null, false);

        action("paste-from-clipboard", "Paste", "T", "Paste to clipboard",
               "/toolbarButtonGraphics/general/Paste16.gif",
               "/toolbarButtonGraphics/general/Paste24.gif", null, false);

        action("undo", "Undo", "U", "Reverse a transaction",
               "/toolbarButtonGraphics/general/Undo16.gif",
               "/toolbarButtonGraphics/general/Undo24.gif", null, false);

        action("redo", "Redo", "R", "Restore an undone transaction",
               "/toolbarButtonGraphics/general/Redo16.gif",
               "/toolbarButtonGraphics/general/Redo24.gif", null, false);

        action("find", "Find", "F", "Find an item",
               "/toolbarButtonGraphics/general/Find16.gif",
               "/toolbarButtonGraphics/general/Find24.gif", null, false);

        action("print", "Print", "P", "Print the document",
               "/toolbarButtonGraphics/general/Print16.gif",
               "/toolbarButtonGraphics/general/Print24.gif", null, false);
        
        action("about", "About", "A", "About this application",
                "/toolbarButtonGraphics/general/About16.gif",
                "/toolbarButtonGraphics/general/About24.gif", null, false);
    }
    
    private JMenuBar createMenus() {
        // Build the menu bar. Menus are lists of lists of action ids.
        List file = new ArrayList();
        file.add("file-menu");
        file.add("bidi-command");
        file.add("find");
        file.add("print");
        file.add(null);
        if(bindExit) {
            file.add("exit-command");
        }

        List edit = new ArrayList();
        edit.add("edit-menu");
        edit.add("cut-to-clipboard");
        edit.add("copy-to-clipboard");
        edit.add("paste-from-clipboard");
        edit.add(null);
        edit.add("undo");
        edit.add("redo");

        List help = new ArrayList();
        help.add("help-menu");
        help.add("about");

        List list = new ArrayList();
        list.add(file);
        list.add(edit);
        list.add(help);

        ActionManager manager = Application.getInstance().getActionManager();
        return manager.getFactory().createMenuBar(list);
    }

    private JToolBar createToolBar() {
        List list = new ArrayList();

        // Create toolbar
        list.add("cut-to-clipboard");
        list.add("copy-to-clipboard");
        list.add("paste-from-clipboard");
        list.add(null);
        list.add("bidi-command");
        if(bindExit) {
            list.add(null);
            list.add("exit-command");
        }

        // Use the factory to create the toolbar.
        ActionManager manager = Application.getInstance().getActionManager();
        return manager.getFactory().createToolBar(list);
    }
    
    /**
     * Creates the action object that allows us toggle the BiDi state of the 
     * application. This would not be usable in a normal application, bidi
     * is closely tied to the locale.
     */
    private Action createBidiAction() {
        class BidiAction extends AbstractAction {
            public BidiAction() {
                putValue(NAME, "Bidi");
                putValue(SHORT_DESCRIPTION, "Toggle bidi");
                putValue(LONG_DESCRIPTION, "Reverse the UI for bidi aware languages");
                putValue(MNEMONIC_KEY, new Integer('b'));
            }
            
            public void actionPerformed(ActionEvent ev) {
                Component c = SwingUtilities.windowForComponent((Component)ev.getSource());
                if(c.getComponentOrientation() == ComponentOrientation.LEFT_TO_RIGHT) {
                    c.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                } else {
                    c.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                }
                c.invalidate();
                c.validate();
                c.repaint();
            }
        };
        return new BidiAction();
    }

    /**
     * Convenience method to create actions with all the attributes. A factory
     * method for other factory methods.
     */
    private AbstractActionExt action(String id, String name, String mnemonic, String desc,
                          String smicon, String lgicon, String keystroke, boolean toggle) {
        AbstractActionExt action = ActionFactory.createTargetableAction(id,
                                                       name, mnemonic, toggle);

        ActionFactory.decorateAction(action, desc, desc, null, null,
                                     //Application.getIcon(smicon, this),
                                     //Application.getIcon(lgicon, this),
                                     KeyStroke.getKeyStroke(keystroke));

        ActionManager manager = Application.getInstance().getActionManager();
        return (AbstractActionExt)manager.addAction(action);
    }
}
