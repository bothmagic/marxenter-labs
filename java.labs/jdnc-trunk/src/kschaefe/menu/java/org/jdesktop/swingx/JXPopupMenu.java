/*
 * $Id: JXPopupMenu.java 3299 2010-08-03 18:05:06Z kschaefe $
 * 
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.jdesktop.swingx.action.AbstractActionExt;

/**
 * {@code JXPopupMenu} is a popup menu that can create checkbox and radio button
 * menu items from {@code AbstractActionExt} actions. If the
 * {@code AbstractActionExt} passed to this popup is a state-based action and
 * has a group that is not {@code null}, then {@code add(Action)} will add a
 * {@code JRadioButtonMenuItem} to the popup. If the {@code AbstractActionExt}
 * passed to this popup is a state-based action and has a group that is
 * {@code null}, then {@code add(Action)} will add a {@code JCheckBoxMenuItem}
 * to the popup. All other actions are treated normally and will result in a
 * {@code JMenuItem} being added to the popup.
 * <p>
 * {@code JXPopupMenu} is a scrollable menu. The top and bottom "menu items" can
 * be configured to be scrolling buttons, like the arrow buttons on a
 * {@code JScrollBar}. If scrolling is enabled, the center contents (those
 * items between the scroll buttons) can be scrolled by clicking or, for
 * continuous scrolling, pressing the scrolling button. Standard arrow key
 * navigation will also scroll the menu.
 * 
 * @see JPopupMenu
 * @see AbstractActionExt
 * 
 * @author Karl Schaefer
 */
public class JXPopupMenu extends JPopupMenu {
    private static class UpIcon implements Icon {

        /**
         * {@inheritDoc}
         */
        public int getIconHeight() {
            return 8;
        }

        /**
         * {@inheritDoc}
         */
        public int getIconWidth() {
            return 7;
        }

        /**
         * {@inheritDoc}
         */
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.drawPolyline(new int[] { 1, 3, 5 }, new int[] { 3, 1, 3 }, 3);
            g.drawPolyline(new int[] { 1, 3, 5 }, new int[] { 6, 4, 6 }, 3);
        }
    }

    private static class DownIcon implements Icon {

        /**
         * {@inheritDoc}
         */
        public int getIconHeight() {
            return 8;
        }

        /**
         * {@inheritDoc}
         */
        public int getIconWidth() {
            return 7;
        }

        /**
         * {@inheritDoc}
         */
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.drawPolyline(new int[] { 1, 3, 5 }, new int[] { 1, 3, 1 }, 3);
            g.drawPolyline(new int[] { 1, 3, 5 }, new int[] { 4, 6, 4 }, 3);
        }
    }

    private class ScrollingActions extends UIAction {
        private Action a;
        
        public ScrollingActions(Action a) {
            super((String) a.getValue(NAME));
            this.a = a;
        }

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent e) {
            if (getName() == "selectPrevious") {
                selectPrevious();
                
                a.actionPerformed(e);
            } else if (getName() == "selectNext") {
                selectNext();
                
                a.actionPerformed(e);
            }
        }
        
        public Action getDelegate() {
            return a;
        }
    }
    
    private class ActionInstaller implements PopupMenuListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            //does nothing
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            ActionMap map = (ActionMap) UIManager.get("PopupMenu.actionMap");
            
            ScrollingActions selectPrevious = (ScrollingActions) map.get("selectPrevious");
            Action a = selectPrevious.getDelegate();
            map.put(a.getValue(Action.NAME), a);
            
            ScrollingActions selectNext = (ScrollingActions) map.get("selectNext");
            a = selectNext.getDelegate();
            map.put(a.getValue(Action.NAME), a);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            ActionMap map = (ActionMap) UIManager.get("PopupMenu.actionMap");
            
            Action selectPrevious = map.get("selectPrevious");
            Action a = new ScrollingActions(selectPrevious);
            map.put(a.getValue(Action.NAME), a);
            
            Action selectNext = map.get("selectNext");
            a = new ScrollingActions(selectNext);
            map.put(a.getValue(Action.NAME), a);
            
            //resets the scrollpoint to the first index
            scrollPoint = 0;
        }
    }
    
    private static final long serialVersionUID = -529042262145585307L;

    private transient Point lastInvokingLocation;

    private Map<String, ButtonGroup> groups;

    private int itemDisplayCount;

    private int scrollPoint;

    private List<Component> children;

    private JMenuItem up;

    private JMenuItem down;

    /**
     * Constructs a {@code JXPopupMenu} without an "invoker".
     */
    public JXPopupMenu() {
        this(null);
    }

    /**
     * Constructs a {@code JXPopupMenu} with the specified title.
     * 
     * @param label
     *            the string that a UI may use to display as a title for the
     *            popup menu.
     */
    public JXPopupMenu(String label) {
        this(label, 0);
    }

    /**
     * Constructs a {@code JXPopupMenu} with the specified title. If
     * {@code itemDisplayCount} is greater than 0, this popup menu will be a
     * scrolling popup menu that displays {@code itemDisplayCount} number of
     * menu items.
     * 
     * @param label
     *                the string that a UI may use to display as a title for the
     *                popup menu.
     * @param itemDisplayCount
     *                the number of menu items to display, or 0 to display all
     *                menu items
     */
    public JXPopupMenu(String label, int itemDisplayCount) {
        super(label);
        
        up = new JXScrollMenuItem(new UpIcon());
        down = new JXScrollMenuItem(new DownIcon());

        setItemDisplayCount(itemDisplayCount);
        scrollPoint = 0;
        groups = new HashMap<String, ButtonGroup>();
        children = new ArrayList<Component>();

        up.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                scrollUp();
            }
            
//            @Override
//            public boolean isEnabled() {
//                return scrollPoint > 0;
//            }
        });
        up.setIcon(new UpIcon());
        
        super.addImpl(up, null, -1);

        down.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scrollDown();
            }
        });
        
        super.addImpl(down, null, -1);
        
        addPopupMenuListener(new ActionInstaller());
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
                        protected PropertyChangeListener createActionPropertyChangeListener(
                                Action a) {
                            PropertyChangeListener pcl = createActionChangeListener(this);

                            if (pcl == null) {
                                pcl = super
                                        .createActionPropertyChangeListener(a);
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
                        protected PropertyChangeListener createActionPropertyChangeListener(
                                Action a) {
                            PropertyChangeListener pcl = createActionChangeListener(this);

                            if (pcl == null) {
                                pcl = super
                                        .createActionPropertyChangeListener(a);
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
    public MenuElement[] getSubElements() {
        List<MenuElement> subElements = new ArrayList<MenuElement>();

        for (Component c : getComponents()) {
            if (c instanceof MenuElement && c != up && c != down) {
                subElements.add((MenuElement) c);
            }
        }

        return subElements.toArray(new MenuElement[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        children.add(comp);

        int size = children.size();

        if (getItemDisplayCount() > 0 && size > getItemDisplayCount()) {
            comp.setVisible(false);
        }
        
        super.addImpl(comp, constraints, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(int pos) {
        int i = pos - 1;

        if (pos < children.size()) {
            children.remove(i);
        }

        super.remove(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll() {
        children.clear();

        super.removeAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(int x, int y) {
        if (getInvoker() != null) {
            lastInvokingLocation = new Point(x, y);
        } else {
            lastInvokingLocation = null;
        }

        super.setLocation(x, y);
    }

    /**
     * Scrolls up, exposing the next displayable item.
     */
    public void scrollUp() {
        if (scrollPoint > 0) {
            scrollPoint--;
            children.get(scrollPoint + itemDisplayCount).setVisible(false);
            children.get(scrollPoint).setVisible(true);
        }
    }

    private void selectPrevious() {
        MenuSelectionManager msm = MenuSelectionManager.defaultManager();
        MenuElement path[] = msm.getSelectedPath();
        
        int index = children.indexOf(path[path.length - 1]);

        if (index == 0) {
            int size = children.size();
            int len = size < itemDisplayCount * 2 ? size - itemDisplayCount : itemDisplayCount;
            int lastIndex = size - 1;
            
            for (int i = 0; i < len; i++) {
                children.get(i).setVisible(false);
                children.get(lastIndex - i).setVisible(true);
            }
            
            scrollPoint = size - itemDisplayCount;
        } else if (index == scrollPoint) {
            scrollUp();
        }
    }
    
    /**
     * Scrolls down, exposing the next displayable item.
     */
    public void scrollDown() {
        if (scrollPoint + itemDisplayCount < children.size()) {
            children.get(scrollPoint).setVisible(false);
            children.get(scrollPoint + itemDisplayCount).setVisible(true);
            scrollPoint++;
        }
    }

    private void selectNext() {
        MenuSelectionManager msm = MenuSelectionManager.defaultManager();
        MenuElement path[] = msm.getSelectedPath();
        
        int index = children.indexOf(path[path.length - 1]);
        
        if (index == children.size() - 1) {
            int size = children.size();
            int len = size < itemDisplayCount * 2 ? size - itemDisplayCount : itemDisplayCount;
            int lastIndex = size - 1;
            
            for (int i = 0; i < len; i++) {
                children.get(i).setVisible(true);
                children.get(lastIndex - i).setVisible(false);
            }
            
            scrollPoint = 0;
        } else if (index - itemDisplayCount == scrollPoint - 1) {
            scrollDown();
        }
    }
    
    /**
     * Gets the last location that this menu was invoked from.
     * 
     * @return a point where the menu was activated, or {@code null} if this
     *         menu has never been invoked
     */
    public Point getLastInvokingLocation() {
        return new Point(lastInvokingLocation);
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

        if (itemDisplayCount == 0) {
            for (Component c : getComponents()) {
                c.setVisible(true);
            }

            up.setVisible(false);
            down.setVisible(false);
        } else {
            for (int i = 0; i < getComponentCount(); i++) {
                getComponent(i).setVisible(i < itemDisplayCount);
            }
            
            up.setVisible(true);
            down.setVisible(true);
        }

        firePropertyChange("itemDisplayCount", oldCount, getItemDisplayCount());
    }
}
