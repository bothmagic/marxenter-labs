package org.jdesktop.incubator.menu;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/*
 * Menu builder for both JMenu and JPopMenu to make it easier to create either main menus and/or
 * context menus from action descriptions.
 *
 * (copied from my commons project).
 * 
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 08-Nov-2004
 * Time: 17:12:57
 */

public class MenuFactory {
    public static final String SEPARATOR = "---";

    static JComponent createMenuComponent(JComponent menu, Collection items) {
        MenuAdapter menuAdapter = new MenuAdapter(menu);
        for (Iterator it = items.iterator(); it.hasNext();) {
            Object item = (Object) it.next();
            if (item instanceof Action) {
                menuAdapter.add((Action) item);
            } else if (item instanceof Action[]) {
                Action[] actions = (Action[]) item;
                if (actions.length > 1) {
                    String name = (String) actions[0].getValue(Action.NAME);
                    JMenu subMenu = menuAdapter.addSubMenu(name);
                    List<Action> keys = Arrays.asList(actions).subList(1, actions.length);
                    createMenuComponent(subMenu, keys);
                }
            } else if (item instanceof JMenuItem) {
                menuAdapter.add((JMenuItem) item);
            } else if (item instanceof JSeparator) {
                menuAdapter.addSeparator();
            }
        }
        return trim(menuAdapter);
    }

    static JComponent createMenuComponent(JComponent menu,
                                          ActionMap actionMap,
                                          List actions) {
        MenuAdapter menuAdapter = new MenuAdapter(menu);
        for (Object menuElement : actions) {
            if (MenuFactory.SEPARATOR.equals(menuElement)) {
                menuAdapter.addSeparator();
            } else if (menuElement instanceof Object[]) {
                Object[] items = (Object[]) menuElement;
                if (items.length > 1) {
                    JMenu subMenu = new JMenu(items[0].toString());
                    List<Object> keys = Arrays.asList(items).subList(1, items.length);
                    createMenuComponent(subMenu, actionMap, keys);
                    if (subMenu.getMenuComponents().length > 0) {
                        menuAdapter.addSubMenu(subMenu);
                    }
                }
            } else if (menuElement instanceof List) {
                List items = (List) menuElement;
                if (items.size() > 1) {
                    JMenu subMenu = new JMenu(items.get(0).toString());
                    List<List> keys = Arrays.asList(items).subList(1, items.size());
                    createMenuComponent(subMenu, actionMap, keys);
                    if (subMenu.getMenuComponents().length > 0) {
                        menuAdapter.addSubMenu(subMenu);
                    }
                }
            } else {
                Action action = actionMap.get(menuElement);
                if (action != null) {
                    menuAdapter.add(action);
                }
            }
        }
        return trim(menuAdapter);
    }

    /**
     * Removes leading & trailing separators (if any).
     *
     * @param menuAdapter the source menu via our MenuAdapter
     * @return menu sans pointless separators (that separate nothing)
     */
    static JComponent trim(MenuAdapter menuAdapter) {
        int count = menuAdapter.getMenuComponentCount();
        if (count > 0) {
            Component firstComponent = menuAdapter.getMenuComponent(0);
            if (firstComponent instanceof JSeparator) {
                menuAdapter.remove(0);
            }
            int last = menuAdapter.getMenuComponentCount() - 1;
            Component lastComponent = menuAdapter.getMenuComponent(last);
            if (lastComponent instanceof JSeparator) {
                menuAdapter.remove(last);
            }
            if (count > menuAdapter.getMenuComponentCount())
                trim(menuAdapter);
        }
        return menuAdapter.getComponent();
    }

    public static JMenu createJMenu(Collection items) {
        return (JMenu) createMenuComponent(new JMenu(), items);
    }

    public static JMenu createJMenu(String name, Collection items) {
        return (JMenu) createMenuComponent(new JMenu(name), items);
    }

    /**
     * @deprecated not very useful as we can't represent ordered menu
     */
    public static JMenu createJMenu(ActionMap actionMap, Object... keys) {
        return (JMenu) createMenuComponent(new JMenu(),
                actionMap, Arrays.asList(keys));
    }

    /**
     * @deprecated not very useful as we can't represent ordered menu
     */
    public static JMenu createJMenu(ActionMap actionMap, List keys) {
        return (JMenu) createMenuComponent(new JMenu(), actionMap, keys);
    }

    public static JPopupMenu createJPopupMenu(Collection items) {
        return (JPopupMenu) createMenuComponent(new JPopupMenu(), items);
    }

    public static JPopupMenu createJPopupMenu(String name, Collection items) {
        return (JPopupMenu) createMenuComponent(new JPopupMenu(name), items);
    }

    public static JPopupMenu createJPopupMenu(ActionMap actionMap, Object... keys) {
        return createJPopupMenu(actionMap, Arrays.asList(keys));
    }

    public static JPopupMenu createJPopupMenu(ActionMap actionMap, List keys) {
        return (JPopupMenu) createMenuComponent(new JPopupMenu(), actionMap, keys);
    }

    public static boolean isEmpty(Container menu) {
        Component[] components = menu.getComponents();
        for (int i = 0, n = components.length; i < n; i++) {
            if (components[i] instanceof Action || components[i] instanceof JMenuItem) {
                return false;
            } else if (components[i] instanceof JMenu
                    && !isEmpty((Container) components[i])) {
                return false;
            }
        }
        return true;
    }
}
