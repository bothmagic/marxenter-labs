/*
 * $Id: JXMenu.java 3299 2010-08-03 18:05:06Z kschaefe $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.MenuElement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.PopupMenuUI;

import org.jdesktop.swingx.action.AbstractActionExt;

/**
 * {@code JXMenu} is a menu that can create checkbox and radio button menu items
 * from {@code AbstractActionExt} actions. If the {@code AbstractActionExt}
 * passed to this menu is a state-based action and has a group that is not
 * {@code null}, then {@code add(Action)} will add a
 * {@code JRadioButtonMenuItem} to the menu. If the {@code AbstractActionExt}
 * passed to this menu is a state-based action and has a group that is
 * {@code null}, then {@code add(Action)} will add a {@code JCheckBoxMenuItem}
 * to the menu. All other actions are treated normally and will result in a
 * {@code JMenuItem} being added to the menu.
 * 
 * @see JMenu
 * @see AbstractActionExt
 * 
 * @author Karl Schaefer
 */
public class JXMenu extends JMenu {
    private JXPopupMenu popupMenu;
    private Map<String, ButtonGroup> groups;
    private Point customMenuLocation;
    private int itemDisplayCount;
    
    public JXMenu() {
        super();
        init();
    }

    public JXMenu(Action a) {
        super(a);
        init();
    }

    public JXMenu(String s, boolean b) {
        super(s, b);
        init();
    }

    public JXMenu(String s) {
        super(s);
        init();
    }

    private void init() {
        popupMenu = createDelegatePopupMenu();
        
        if (popupMenu == null) {
            throw new NullPointerException(
                    "createDelegatePopupMenu() cannot return null");
        }
    }
    
    /**
     * Creates the {@code JXPopupMenu} used by this menu to display the menu items.
     * 
     * @return the popup used to display the menu items
     */
    protected JXPopupMenu createDelegatePopupMenu() {
        JXPopupMenu popup = new JXPopupMenu();
        popup.setItemDisplayCount(4);
        popup.setInvoker(this);
        popupListener = createWinListener(popupMenu);
        
        return popup;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    //overridden to avoid calling ensurePopupMenuCreated()
    public Component add(Component c) {
        //copied from Container
        addImpl(c, null, -1);
        return c;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    //overridden to avoid calling ensurePopupMenuCreated()
    public Component add(Component c, int index) {
        //copied from Container
        addImpl(c, null, index);
        return c;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //overridden to avoid calling ensurePopupMenuCreated()
    public JMenuItem add(JMenuItem menuItem) {
        //copied from Container
        addImpl(menuItem, null, -1);
        return menuItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //overridden to avoid calling ensurePopupMenuCreated()
    public void addSeparator() {
        popupMenu.addSeparator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        popupMenu.addImpl(comp, constraints, index);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    //overridden to avoid calling ensurePopupMenuCreated()
    public JMenuItem insert(Action a, int pos) {
        //fix bug where insert does not call createActionComponent
        JMenuItem mi = createActionComponent(a);
        mi.setAction(a);
        
        return insert(mi, pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //overridden to avoid calling ensurePopupMenuCreated()
    public JMenuItem insert(JMenuItem mi, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        popupMenu.insert(mi, pos);
        
        return mi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //overridden to avoid calling ensurePopupMenuCreated()
    public void insert(String s, int pos) {
        insert(new JMenuItem(s), pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //overridden to avoid calling ensurePopupMenuCreated()
    public void insertSeparator(int index) {
        popupMenu.insert(new JPopupMenu.Separator(), index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(JMenuItem item) {
        popupMenu.remove(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(int pos) {
        popupMenu.remove(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Component c) {
        popupMenu.remove(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll() {
        popupMenu.removeAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMenuComponentCount() {
        return popupMenu.getComponentCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getMenuComponent(int n) {
        return popupMenu.getComponent(n);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component[] getMenuComponents() {
        return popupMenu.getComponents();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuElement[] getSubElements() {
        return new MenuElement[] { popupMenu };
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected JMenuItem createActionComponent(Action a) {
        JMenuItem mi = null;

        if (a instanceof AbstractActionExt) {
            AbstractActionExt ext = (AbstractActionExt) a;

            if (ext.isStateAction()) {
                String groupId = (String) ext.getGroup();

                if (groupId != null) {
                    mi = new JRadioButtonMenuItem() {
                        @Override
                        protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
                            PropertyChangeListener pcl = createActionChangeListener(this);
                            
                            if (pcl == null) {
                                pcl = super.createActionPropertyChangeListener(a);
                            }
                            
                            return pcl;
                        }
                    };
                    
                    mi.setSelected(ext.isSelected());
                    
                    ButtonGroup group = groups.get(groupId);
                    
                    if (group == null) {
                        group = new ButtonGroup();
                        groups.put(groupId, group);
                    }
                    
                    group.add(mi);
                } else {
                    mi = new JCheckBoxMenuItem() {
                        @Override
                        protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
                            PropertyChangeListener pcl = createActionChangeListener(this);
                            
                            if (pcl == null) {
                                pcl = super.createActionPropertyChangeListener(a);
                            }
                            
                            return pcl;
                        }
                    };
                    
                    mi.setSelected(ext.isSelected());
                }
            }
        }

        if (mi == null) {
            mi = super.createActionComponent(a);
        } else {
            mi.setHorizontalTextPosition(SwingConstants.TRAILING);
            mi.setVerticalTextPosition(SwingConstants.CENTER);
        }

        return mi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //overridden to avoid calling ensurePopupMenuCreated()
    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }
    
    /**
     * {@inheritDoc}
     */
    //overridden because customMenuLocation is not expose to subclasses
    //does need to be if you can set a custom JPopupMenu
    @Override
    public void setMenuLocation(int x, int y) {
        customMenuLocation = new Point(x, y);
        
        if (popupMenu != null)
            popupMenu.setLocation(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
        
        popupMenu.applyComponentOrientation(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setComponentOrientation(ComponentOrientation o) {
        super.setComponentOrientation(o);
        
        popupMenu.setComponentOrientation(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //overridden to avoid calling ensurePopupMenuCreated()
    public boolean isPopupMenuVisible() {
        return popupMenu.isVisible();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //overridden to avoid calling ensurePopupMenuCreated()
    public void setPopupMenuVisible(boolean b) {
        boolean isVisible = isPopupMenuVisible();
        
        if (b != isVisible && (isEnabled() || !b)) {
            
            if ((b==true) && isShowing()) {
                // Set location of popupMenu (pulldown or pullright)
                Point p = customMenuLocation == null
                        ? getPopupMenuOrigin()
                        : customMenuLocation;
                
                getPopupMenu().show(this, p.x, p.y);
            } else {
                getPopupMenu().setVisible(false);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUI() {
        setUI((MenuItemUI) UIManager.getUI(this));

        if (popupMenu != null) {
            popupMenu.setUI((PopupMenuUI) UIManager.getUI(popupMenu));
        }
    }

    /**
     * Gets the number of items that this popup menu can display. A return value
     * of 0, signifies that all menu items are displayable.
     * 
     * @return the number of displayable items or 0 if all items are displayable
     */
    public int getItemDisplayCount() {
        return itemDisplayCount;
    }

    /**
     * Sets the number of items displayable by this popup menu without
     * scrolling.
     * 
     * @param itemDisplayCount
     *                the count of displayable items
     * @throws IllegalArgumentException
     *                 if {@code itemDisplayCount} is less than 0
     */
    public void setItemDisplayCount(int itemDisplayCount) {
        if (itemDisplayCount < 0) {
            throw new IllegalArgumentException(
                    "itemDisplayCount cannot be negative");
        }

        int oldCount = getItemDisplayCount();
        this.itemDisplayCount = itemDisplayCount;

        firePropertyChange("itemDisplayCount", oldCount, getItemDisplayCount());
    }
}
