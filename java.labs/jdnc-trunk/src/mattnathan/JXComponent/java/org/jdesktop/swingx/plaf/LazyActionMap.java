/*
 * $Id: LazyActionMap.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ActionMapUIResource;

/**
 * This class is based on the javax.swing.plaf.basic.LazyActionMap but adds type-safety and allows public access to its
 * methods
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class LazyActionMap extends ActionMapUIResource {
    /**
     * Object to invoke <code>loadActionMap</code> on.
     */
    private transient ActionMapLoader _loader;

    /**
     * Installs an ActionMap that will be populated by invoking the
     * <code>loadActionMap</code> method on the specified Class
     * when necessary.
     * <p>
     * This should be used if the ActionMap can be shared.
     *
     * @param c JComponent to install the ActionMap on.
     * @param loader Class object that gets loadActionMap invoked
     *                    on.
     * @param defaultsKey Key to use to defaults table to check for
     *        existing map and what resulting Map will be registered on.
     */
    public static void installLazyActionMap(JComponent c, ActionMapLoader loader,
                                            String defaultsKey) {
        ActionMap map = (ActionMap) UIManager.get(defaultsKey);
        if (map == null) {
            map = new LazyActionMap(loader);
            UIManager.getLookAndFeelDefaults().put(defaultsKey, map);
        }
        SwingUtilities.replaceUIActionMap(c, map);
    }





    /**
     * Returns an ActionMap that will be populated by invoking the <code>loadActionMap</code> method on the specified
     * Class when necessary.
     *
     * <p> This should be used if the ActionMap can be shared.
     *
     * @param loader Class object that gets loadActionMap invoked on.
     * @param defaultsKey Key to use to defaults table to check for existing map and what resulting Map will be
     *   registered on.
     * @return ActionMap
     */
    public static ActionMap getActionMap(ActionMapLoader loader,
                                         String defaultsKey) {
        ActionMap map = (ActionMap) UIManager.get(defaultsKey);
        if (map == null) {
            map = new LazyActionMap(loader);
            UIManager.getLookAndFeelDefaults().put(defaultsKey, map);
        }
        return map;
    }





    private LazyActionMap(ActionMapLoader loader) {
        _loader = loader;
    }





    public void put(Action action) {
        put(action.getValue(Action.NAME), action);
    }





    @Override
    public void put(Object key, Action action) {
        loadIfNecessary();
        super.put(key, action);
    }





    @Override
    public Action get(Object key) {
        loadIfNecessary();
        return super.get(key);
    }





    @Override
    public void remove(Object key) {
        loadIfNecessary();
        super.remove(key);
    }





    @Override
    public void clear() {
        loadIfNecessary();
        super.clear();
    }





    @Override
    public Object[] keys() {
        loadIfNecessary();
        return super.keys();
    }





    @Override
    public int size() {
        loadIfNecessary();
        return super.size();
    }





    @Override
    public Object[] allKeys() {
        loadIfNecessary();
        return super.allKeys();
    }





    @Override
    public void setParent(ActionMap map) {
        loadIfNecessary();
        super.setParent(map);
    }





    private void loadIfNecessary() {
        if (_loader != null) {
            ActionMapLoader loader = _loader;
            _loader = null;
            loader.loadActionMap(this);
        }
    }





    /**
     * Interface for loading an action map.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    public static interface ActionMapLoader {
        /**
         * Load actions into the given map.
         *
         * @param map The map to load actions into.
         * @see LazyActionMap#put(Action)
         */
        void loadActionMap(LazyActionMap map);
    }
}
