/*
 * $Id: JXCompoundToolBar.java 2355 2008-03-27 03:10:59Z kschaefe $
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


/**
 * {@code JXCompoundToolBar} is a tool bar that is designed to contain other
 * tool bars. It uses the tool bar's name to produce a tab that expands or
 * collapses the tool bar. Only one child tool bar is expanded at any given
 * time, but the user can collapse a tool bar by expanding a different one. This
 * can also be done programmatically with {@code expand(String)}. If this
 * {@code JXCompoundToolBar} only manages one tool bar, then it will appear to
 * be a normal {@code JXToolBar}.
 * 
 * @author Karl Schaefer
 */
public class JXCompoundToolBar extends JXToolBar {
//
//    /**
//     * Creates a new tool bar with a {@code HORIZONTAL} orientation.
//     */
//    public JXCompoundToolBar() {
//        super();
//    }
//
//    /**
//     * Creates a new tool bar with the specified {@code orientation}. The
//     * {@code orientation} must be either {@code HORIZONTAL} or {@code VERTICAL}.
//     * 
//     * @param orientation
//     *            the orientation desired
//     * @throws IllegalArgumentException
//     *             if the orientation is neither {@code HORIZONTAL} nor
//     *             {@code VERTICAL}.
//     */
//    public JXCompoundToolBar(int orientation) {
//        super(orientation);
//    }
//
//    /**
//     * Creates a new tool bar with the specified {@code name}. The name is used
//     * as the title of the undocked tool bar. The default orientation is
//     * {@code HORIZONTAL}.
//     * 
//     * @param name
//     *            the name of the tool bar
//     */
//    public JXCompoundToolBar(String name) {
//        super(name);
//    }
//
//    /**
//     * Creates a new tool bar with a specified {@code name} and
//     * {@code orientation}. All other constructors defer to this constructor.
//     * The {@code orientation} must be either {@code HORIZONTAL} or
//     * {@code VERTICAL}.
//     * 
//     * @param name
//     *            the name of the tool bar
//     * @param orientation
//     *            the orientation desired
//     * @throws IllegalArgumentException
//     *             if the orientation is neither {@code HORIZONTAL} nor
//     *             {@code VERTICAL}.
//     */
//    public JXCompoundToolBar(String name, int orientation) {
//        super(name, orientation);
//    }
//
//    /**
//     * Creates a new tool bar to add actions if the tool bar does not exist.
//     * 
//     * @param name
//     *            the name of the tool bar
//     * @return a internal tool bar that is managed by this compound tool bar
//     */
//    protected JXToolBar createToolBar(String name) {
//        return new JXToolBar(name, getOrientation());
//    }
//
//    /**
//     * Adds a new {@code JXButton} to the default tool bar for dispatching the
//     * action.
//     * 
//     * @impl defers to {@link #add(Action, String)}
//     * @param a
//     *            the {@code Action} object to add as a new menu item
//     * @return the new button which dispatches the action
//     */
//    @Override
//    public JXButton add(Action a) {
//        return add(a, getName());
//    }
//
//    /**
//     * Adds a new {@code JXButton} to the named tool bar for dispatching the
//     * action.
//     * 
//     * @param a
//     *            the {@code Action} object to add as a new menu item
//     * @param name
//     *            the name of the toolbar to add the button to
//     * @return the new button which dispatches the action
//     */
//    public JXButton add(Action a, String name) {
//        JXButton b = createActionComponent(a);
//        b.setAction(a);
//        add(b, name);
//        return b;
//    }
//
//    @Override
//    protected void addImpl(Component comp, Object constraints, int index) {
//        // TODO check constraints for possible illegal args
//        if (comp instanceof JToolBar) {
//            JToolBar tb = (JToolBar) comp;
//            tb.setFloatable(false);
//            tb.setBorder(new IconBorder(new TextIcon(tb.getName()), SwingConstants.LEADING, 0));
//
//            super.addImpl(comp, constraints, index);
//        } else {
//            JToolBar toolBar = getToolBar((String) constraints);
//
//            if (toolBar == null) {
//                toolBar = createToolBar((String) constraints);
//                add(toolBar);
//            }
//
//            toolBar.add(comp, null, index);
//        }
//    }
//
//    /**
//     * Appends a separator of default size to the end of the default tool bar.
//     * The default size is determined by the current look and feel.
//     * 
//     * @impl this defers to {@link #addSeparator(Dimension, String)
//     */
//    @Override
//    public void addSeparator() {
//        addSeparator(null, getName());
//    }
//
//    public void addSeparator(String name) {
//        addSeparator(null, name);
//    }
//
//    /**
//     * Appends a separator of a specified size to the end
//     * of the default tool bar.
//     *
//     * @impl this defers to {@link #addSeparator(Dimension, String)
//     * @param size the {@code Dimension} of the separator
//     */
//    @Override
//    public void addSeparator(Dimension size) {
//        addSeparator(size, getName());
//    }
//
//    public void addSeparator(Dimension size, String name) {
//        add(new Separator(size), name);
//    }
//
//    public void remove(String name) {
//        synchronized (getTreeLock()) {
//            // find toolbar by name and remove it
//        }
//    }
//
//    /**
//     * Returns a tool bar managed by this compound tool bar or {@code null} if
//     * no such named tool bar exists.
//     * 
//     * @param name
//     *            the {@code name} of the tool bar to find
//     * @return the named tool bar or {@code null} if it does not exist
//     */
//    public JToolBar getToolBar(String name) {
//        Component[] children = getComponents();
//
//        for (Component child : children) {
//            if (child instanceof JToolBar) {
//                String s = ((JToolBar) child).getName();
//
//                if (s == null && name == null) {
//                    return (JToolBar) child;
//                } else if (s != null && s.equals(name)) {
//                    return (JToolBar) child;
//                }
//
//            }
//        }
//
//        return null;
//    }
}
