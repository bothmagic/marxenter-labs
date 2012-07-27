package org.jdesktop.incubator.menu;

import javax.swing.*;
import java.awt.*;

/*
 * Adapter used by MenuFactory for both JMenu and JPopMenu to make it easier to create either main menus and/or
 * context menus from descriptions. Because JMenu and JPopupMenu classes aren't related, but probably should have been.
 *
 * (copied from my commons project).
 *
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 08-Nov-2004
 * Time: 17:12:57
 */

public class MenuAdapter {

    private JComponent component;

    public static final String MENUITEM = "MenuItem";

    public MenuAdapter(JComponent component) {
        assert component instanceof JMenu || component instanceof JPopupMenu;
        this.component = component;
    }

    public JComponent getComponent() {
        return component;
    }

    public JMenu getJMenu() throws ClassCastException {
        return (JMenu) component;
    }

    public JPopupMenu getJPopupMenu() throws ClassCastException {
        return (JPopupMenu) component;
    }

    public JMenuItem add(JMenuItem menuItem) {
        if (component instanceof JMenu) {
            return ((JMenu) component).add(menuItem);
        } else {
            return ((JPopupMenu) component).add(menuItem);
        }
    }

    public JMenu addSubMenu(String name) {
        return addSubMenu(new JMenu(name));
    }

    public JMenu addSubMenu(JMenu subMenu) {
        if (component instanceof JMenu) {
            ((JMenu) component).add(subMenu);
        } else {
            subMenu.setBackground(component.getBackground());
            ((JPopupMenu) component).add(subMenu);
        }
        return subMenu;
    }

    /**
     * Creates a MenuItem from an action, setting the Name to the MenuItem value if present.
     * Often reuse same actions in buttons and menus, but menu presentation may benefit from a longer descriptor.
     */
    public JMenuItem add(Action action) {
        JMenuItem menuItem;
        if (component instanceof JMenu) {
            menuItem = ((JMenu) component).add(action);
        } else {
            menuItem = ((JPopupMenu) component).add(action);
        }
        String menuName = (String) action.getValue(MenuAdapter.MENUITEM);
        if (menuName != null) {
            menuItem.setText(menuName);
        }
        return menuItem;
    }

    public JMenuItem add(String name) {
        if (component instanceof JMenu) {
            return ((JMenu) component).add(name);
        } else {
            return ((JPopupMenu) component).add(name);
        }
    }

    public JMenuItem insert(JMenuItem menuItem, int pos) {
        if (component instanceof JMenu) {
            return ((JMenu) component).insert(menuItem, pos);
        } else {
            ((JPopupMenu) component).insert(menuItem, pos);
            return menuItem;
        }
    }

    public JMenuItem insert(Action action, int pos) {
        if (component instanceof JMenu) {
            return ((JMenu) component).insert(action, pos);
        } else {
            JMenuItem menuItem = new JMenuItem(action);
            return insert(menuItem, pos);
        }
    }

    public void addSeparator() {
        if (component instanceof JMenu) {
            JMenu menu = ((JMenu) component);
            if (menu.getComponentCount() > 0) {
                menu.addSeparator();
            }
        } else {
            JPopupMenu menu = ((JPopupMenu) component);
            if (menu.getComponentCount() > 0) {
                menu.addSeparator();
            }
        }
    }

    public void insertSeparator(int pos) {
        if (component instanceof JMenu) {
            ((JMenu) component).insertSeparator(pos);
        } else {
            ((JPopupMenu) component).insert(new JPopupMenu.Separator(), pos);
        }
    }

    public JMenuItem getItem(int pos) {
        if (component instanceof JMenu) {
            return ((JMenu) component).getItem(pos);
        } else {
            return (JMenuItem) ((JPopupMenu) component).getComponent(pos);
        }
    }

    public void remove(JMenuItem menuItem) {
        if (component instanceof JMenu) {
            ((JMenu) component).remove(menuItem);
        } else {
            ((JPopupMenu) component).remove(menuItem);
        }
    }

    public void remove(int pos) {
        if (component instanceof JMenu) {
            ((JMenu) component).remove(pos);
        } else {
            ((JPopupMenu) component).remove(pos);
        }
    }

    public void removeAll() {
        if (component instanceof JMenu) {
            ((JMenu) component).removeAll();
        } else {
            ((JPopupMenu) component).removeAll();
        }
    }

    public Component[] getMenuComponents() {
        if (component instanceof JMenu) {
            return ((JMenu) component).getMenuComponents();
        } else {
            return ((JPopupMenu) component).getComponents();
        }
    }

    public Component getMenuComponent(int pos) {
        if (component instanceof JMenu) {
            return ((JMenu) component).getMenuComponent(pos);
        } else {
            return ((JPopupMenu) component).getComponent(pos);
        }
    }

    public int getMenuComponentCount() {
        if (component instanceof JMenu) {
            return ((JMenu) component).getMenuComponentCount();
        } else {
            return ((JPopupMenu) component).getComponentCount();
        }
    }

    /* TODO will need another adaptor here MenuListener<->PopupMenuListener
    public void addMenuListener(MenuListener listener) {
        if (component instanceof JMenu) {
            ((JMenu) component).addMenuListener(listener);
        } else {
            ((JPopupMenu) component).addPopupMenuListener(listener);
        }
    }

    public void removeMenuListener(MenuListener listener) {
        if (component instanceof JMenu) {
            ((JMenu) component).removeMenuListener(listener);
        } else {
            ((JPopupMenu) component).removePopupMenuListener(listener);
        }
    }

    public MenuListener[] getMenuListeners() {
        if (component instanceof JMenu) {
            return ((JMenu) component).getMenuListeners();
        } else {
            return ((JPopupMenu) component).getPopupMenuListeners();
        }
    }
    */
}
