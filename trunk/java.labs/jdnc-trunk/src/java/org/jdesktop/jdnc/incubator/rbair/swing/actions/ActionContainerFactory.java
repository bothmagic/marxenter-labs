/*
 * $Id: ActionContainerFactory.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

/**
 * @(#)ActionContainerFactory.java	1.5 03/06/03
 *
 * Copyright 2003 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 */
package org.jdesktop.jdnc.incubator.rbair.swing.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.*;

/**
 * Creates user interface elements based on action-lists managed
 * in an ActionManager.
 * <p>
 * This class can be used as a general component factory which will construct 
 * components from Actions if the <code>create&lt;comp&gt;(Action,...)</code>
 * methods are used.
 * 
 * @see ActionManager
 */
public class ActionContainerFactory {

    private ActionManager manager;

    // Map between group id + component and the ButtonGroup
    private Map groupMap;

    // Pass -Ddebug=true to enable debugging
    private boolean DEBUG = false;


    /**
     * Creates an container factory which uses managed actions.
     *
     * @param manager use the actions managed with this manager for
     *                constructing ui componenents.
     */
    public ActionContainerFactory(ActionManager manager) {
	setActionManager(manager);
    }

    /**
     * Gets the ActionManager instance. If the ActionManager has not been explicitly
     * set then the default ActionManager instance will be used.
     *
     * @return the ActionManager used by the ActionContainerFactory.
     * @see #setActionManager
     */
    public ActionManager getActionManager() {
	if (manager == null) {
	    manager = ActionManager.getInstance();
	}
	return manager;
    }

    /**
     * Sets the ActionManager instance that will be used by this 
     * ActionContainerFactory
     */
    public void setActionManager(ActionManager manager) {
	this.manager = manager;
    }

    /**
     * Constructs a toolbar from an action-list id. By convention,
     * the identifier of the main toolbar should be "main-toolbar"
     *
     * @param list a list of action ids used to construct the toolbar.
     * @return the toolbar or null
     */
    public JToolBar createToolBar(Object[] list) {
	return createToolBar(Arrays.asList(list));
    }

    /**
     * Constructs a toolbar from an action-list id. By convention,
     * the identifier of the main toolbar should be "main-toolbar"
     *
     * @param list a list of action ids used to construct the toolbar.
     * @return the toolbar or null
     */
    public JToolBar createToolBar(List list) {
	JToolBar toolbar = new JToolBar();
	Iterator iter = list.iterator();
	while(iter.hasNext()) {
	    Object element = iter.next();

	    if (element == null) {
		toolbar.addSeparator();
	    } else {
		toolbar.add(createButton(element, toolbar));
	    }
	}
	return toolbar;
    }


    /**
     * Constructs a popup menu from an action-list id.
     *
     * @param list a list of action ids used to construct the popup.
     * @return the popup or null
     */
    public JPopupMenu createPopup(Object[] list) {
	return createPopup(Arrays.asList(list));
    }

    /**
     * Constructs a popup menu from an action-list id.
     *
     * @param list a list of action ids used to construct the popup.
     * @return the popup or null
     */
    public JPopupMenu createPopup(List list) {
	JPopupMenu popup = new JPopupMenu();
	Iterator iter = list.iterator();
	while(iter.hasNext()) {
	    Object element = iter.next();

	    if (element == null) {
		popup.addSeparator();
	    } else if (element instanceof List) {
		JPopupMenu newPopup = createPopup((List)element);
		if (newPopup != null) {
		    popup.add(newPopup);
		}
	    } else if (element instanceof Object[]) {
		JPopupMenu newPopup = createPopup((Object[])element);
		if (newPopup != null) {
		    popup.add(newPopup);
		}
	    } else {
		popup.add(createMenuItem(element, popup));
	    }
	}
	return popup;
    }

    /**
     * Constructs a menu tree from a list of actions or lists of lists or actions.
     *
     * @param list a list which represents the root item.
     * @return a menu bar which represents the menu bar tree
     */
    public JMenuBar createMenuBar(List list) {
	JMenuBar menubar = new JMenuBar();
	JMenu menu = null;

	Iterator iter = list.iterator();
	while(iter.hasNext()) {
	    Object element = iter.next();

	    if (element == null) {
		if (menu != null) {
		    menu.addSeparator();
		}
	    } else if (element instanceof List) {
		menu = createMenu((List)element);
		if (menu != null) {
		    menubar.add(menu);
		}
	    } else  {
		if (menu != null) {
		    menu.add(createMenuItem(element, menu));
		}
	    }
	}
	return menubar;
    }


    /**
     * Creates and returns a menu from a List which represents actions, separators
     * and sub-menus. The menu
     * constructed will have the attributes from the first action in the List.
     * Subsequent actions in the list represent menu items.
     *
     * @param list a list of action ids used to construct the menu and menu items.
     *             the first element represents the action used for the menu,
     * @return the constructed JMenu or null
     */
    public JMenu createMenu(List list) {
	// The first item will be the action for the JMenu
	Action action = getAction(list.get(0));
	if (action == null) {
	    return null;
	}
	JMenu menu = new JMenu(action);

	// The rest of the items represent the menu items.
	Iterator iter = list.listIterator(1);
	while(iter.hasNext()) {
	    Object element = iter.next();
	    if (element == null) {
		menu.addSeparator();
	    } else if (element instanceof List) {
		JMenu newMenu = createMenu((List)element);
		if (newMenu != null) {
		    menu.add(newMenu);
		}
	    } else  {
		menu.add(createMenuItem(element, menu));
	    }
	}
	return menu;
    }


    /**
     * Convenience method to get the action from an ActionManager.
     */
    private Action getAction(Object id) {
	Action action = getActionManager().getAction(id);
	if (action == null) {
	    throw new RuntimeException("ERROR: No Action for " + id);
	}
	return action;
    }

    /**
     * Returns the button group corresponding to the groupid
     *
     * @param groupid the value of the groupid attribute for the action element
     * @param container a container which will further identify the ButtonGroup
     */
    private ButtonGroup getGroup(String groupid, JComponent container) {
	if (groupMap == null) {
	    groupMap = new HashMap();
	}
	int intCode = groupid.hashCode();
	if (container != null) {
	    intCode ^= container.hashCode();
	}
	Integer hashCode = new Integer(intCode);

	ButtonGroup group = (ButtonGroup)groupMap.get(hashCode);
	if (group == null) {
	    group = new ButtonGroup();
	    groupMap.put(hashCode, group);
	}
	return group;
    }

    /**
     * Creates a menu item based on the attributes of the action element.
     * Will return a JMenuItem, JRadioButtonMenuItem or a JCheckBoxMenuItem
     * depending on the context of the Action.
     *
     * @return a JMenuItem or subclass depending on type.
     */
    private JMenuItem createMenuItem(Object id, JComponent container) {
	return createMenuItem(getAction(id), container);
    }


    /**
     * Creates a menu item based on the attributes of the action element.
     * Will return a JMenuItem, JRadioButtonMenuItem or a JCheckBoxMenuItem
     * depending on the context of the Action.
     *
     * @param action a mangaged Action
     * @param container the parent container may be null for non-group actions.
     * @return a JMenuItem or subclass depending on type.
     */
    public JMenuItem createMenuItem(Action action, JComponent container) {
	JMenuItem menuItem = null;
	if (action instanceof AbstractActionExt) {
	    AbstractActionExt ta = (AbstractActionExt)action;

	    if (ta.isStateAction()) {
		String groupid = (String)ta.getGroup();
		if (groupid != null) {
		    // If this action has a groupid attribute then it's a
		    // GroupAction
		    menuItem = createRadioButtonMenuItem(getGroup(groupid, container),
							 (AbstractActionExt)action);
		} else {
		    menuItem = createCheckBoxMenuItem((AbstractActionExt)action);
		}
	    }
	}

	if (menuItem == null) {
	    menuItem= new JMenuItem(action);
	    configureMenuItem(menuItem, action);
	}
	return menuItem;
    }

    /**
     * Creates a menu item based on the attributes of the action.
     * Will return a JMenuItem, JRadioButtonMenuItem or a JCheckBoxMenuItem
     * depending on the context of the Action.
     *
     * @param action an action used to create the menu item
     * @return a JMenuItem or subclass depending on type.
     */
    public JMenuItem createMenuItem(Action action) {
	return createMenuItem(action, null);
    }


    /**
     * Creates a button based on the attributes of the action element.
     * Will return a JButton or a JToggleButton.
     */
    private AbstractButton createButton(Object id, JComponent container) {
	return createButton(getAction(id), container);
    }

    /**
     * Creates a button based on the attributes of the action. If the container
     * parameter is non-null then it will be used to uniquely identify
     * the returned component within a ButtonGroup. If the action doesn't
     * represent a grouped component then this value can be null.
     *
     * @param action an action used to create the button
     * @param container the parent container to uniquely identify
     *        grouped components or null
     * @return will return a JButton or a JToggleButton.
     */
    public AbstractButton createButton(Action action, JComponent container) {
	if (action == null) {
	    return null;
	}

	AbstractButton button = null;
	if (action instanceof AbstractActionExt) {
	    // Check to see if we should create a toggle button
	    AbstractActionExt ta = (AbstractActionExt)action;

	    if (ta.isStateAction()) {
		// If this action has a groupid attribute then it's a
		// GroupAction
		String groupid = (String)ta.getGroup();
		if (groupid == null) {
		    button = createToggleButton(ta);
		} else {
		    button = createToggleButton(ta, getGroup(groupid, container));
		}
	    }
	}

	if (button == null) {
	    // Create a regular button
	    button = new JButton(action);
	    configureButton(button, action);
	}
	return button;
    }

    /**
     * Creates a button based on the attributes of the action.
     *     
     * @param action an action used to create the button
     * @return will return a JButton or a JToggleButton.
     */
    public AbstractButton createButton(Action action)  {
	return createButton(action, null);
    }

    /**
     * Adds and configures a toggle button.
     * @param a an abstraction of a toggle action.
     */
    private JToggleButton createToggleButton(AbstractActionExt a)  {
	return createToggleButton(a, null);
    }

    /**
     * Adds and configures a toggle button.
     * @param a an abstraction of a toggle action.
     * @param group the group to add the toggle button or null
     */
    private JToggleButton createToggleButton(AbstractActionExt a, ButtonGroup group)  {
        JToggleButton button = new JToggleButton(a);
	button.addItemListener(a);
        button.setSelected(a.isSelected());
	if (group != null) {
	    group.add(button);
	}
        configureToggleButton(button, a);
	return button;
    }

    /**
     * This method will be called after toggle buttons are created.
     * Override for custom configuration but the overriden method should be called
     * first.
     *
     * @param button the button to be configured
     * @param action the action used to construct the menu item.
     */
    protected void configureToggleButton(JToggleButton button, Action action) {
	configureButton(button, action);

	// The PropertyChangeListener that gets added
	// to the Action doesn't know how to handle the "selected" property change
	// in the meantime, the corect thing to do is to add another PropertyChangeListener
	// to the AbstractActionExt until this is fixed.
	action.addPropertyChangeListener(new ToggleActionPropertyChangeListener(button));
    }


    /**
     * This method will be called after buttons created from an action.
     * Override for custom configuration.
     *
     * @param button the button to be configured
     * @param action the action used to construct the menu item.
     */
    protected void configureButton(AbstractButton button, Action action)  {
	if (action.getValue(Action.SHORT_DESCRIPTION) == null) {
	    button.setToolTipText((String)action.getValue(Action.NAME));
	}
	button.setFocusable(false);

	// Use the large icon for toolbar buttons.
	if (action.getValue(AbstractActionExt.LARGE_ICON) != null) {
	    button.setIcon((Icon)action.getValue(AbstractActionExt.LARGE_ICON));
	}

        // Don't show the text under the toolbar buttons if they have 
	// and icon.
	if (button.getIcon() != null) {
	    button.setText("");
	}
    }

    /**
     * This method will be called after toggle type menu items (like
     * JRadioButtonMenuItem and JCheckBoxMenuItem) are created.
     * Override for custom configuration but the overriden method should be called
     * first.
     *
     * @param menuItem the menu item to be configured
     * @param action the action used to construct the menu item.
     */
    protected static void configureToggleMenuItem(JMenuItem menuItem, Action action) {
	configureMenuItem(menuItem, action);

	// The PropertyChangeListener that gets added
	// to the Action doesn't know how to handle the "selected" property change
	// in the meantime, the corect thing to do is to add another PropertyChangeListener
	// to the AbstractActionExt until this is fixed.
	action.addPropertyChangeListener(new ToggleActionPropertyChangeListener(menuItem));
    }


    /**
     * This method will be called after menu items are created.
     * Override for custom configuration.
     *
     * @param menuItem the menu item to be configured
     * @param action the action used to construct the menu item.
     */
    protected static void configureMenuItem(JMenuItem menuItem, Action action) {
    }

    /**
     * Helper method to add a checkbox menu item.
     */
    private static JCheckBoxMenuItem createCheckBoxMenuItem(AbstractActionExt a) {
        JCheckBoxMenuItem mi = new JCheckBoxMenuItem(a);
        mi.addItemListener(a);
        mi.setSelected(a.isSelected());

        configureToggleMenuItem(mi, a);
	return mi;
    }

    /**
     * Helper method to add a radio button menu item.
     */
    private static JRadioButtonMenuItem createRadioButtonMenuItem(ButtonGroup group,
					  AbstractActionExt a)  {
        JRadioButtonMenuItem mi = new JRadioButtonMenuItem(a);
        mi.addItemListener(a);
        mi.setSelected(a.isSelected());
	if (group != null) {
	    group.add(mi);
	}
        configureToggleMenuItem(mi, a);
	return mi;
    }
}
