package org.jdesktop.jdnc.incubator.vprise.i18n;

import java.util.*;
import java.text.*;

/**
 * The purpose of this class is to encapsulate some of the "oddities" of the
 * resource bundle and simplify the whole process of localizing an application.
 * This class uses resource bundles internally and simplifies working with them.
 * While normally statics are bad for simplicities sake this class uses static
 * methods since this sort of class is used throughout the application.
 *
 * @author Shai Almog
 */
public class Resources {
    /**
     * This is the resource bundle used throughout the application
     */
    private BundleWrapper bundle = new BundleWrapper();
    
    private static final Resources INSTANCE = new Resources();
    
    private Resources() {}
    
    /**
     * The name of the resource bundle is initialized by the main process.
     */
    public static void addBundle(String bundleName, Locale locale, ClassLoader loader) {
        INSTANCE.bundle.addBundle(ResourceBundle.getBundle(bundleName, locale, loader));
    }
    
    /**
     * The name of the resource bundle is initialized by the main process.
     */
    public static void addBundle(String bundleName, Locale locale) {
        INSTANCE.bundle.addBundle(ResourceBundle.getBundle(bundleName, locale));
    }
    
    /**
     * The name of the resource bundle is initialized by the main process.
     */
    public static void addBundle(String bundleName) {
        INSTANCE.bundle.addBundle(ResourceBundle.getBundle(bundleName));
    }

    /**
     * Returns the string matching the given key in the resource bundle
     */
    public static String getString(String key) {
        return(INSTANCE.bundle.getString(key));
    }

    /**
     * Returns the string matching the given key in the resource bundle, if
     * a key does not exist return the default string
     */
    public static String getString(String key, String defaultString) {
        if(INSTANCE.bundle.contains(key)) {
            return(INSTANCE.bundle.getString(key)); 
        } else {
            return(defaultString);
        }
    }

    /**
     * Returns the string matching the given key in the resource bundle formatted
     * with the paramters
     */
    public static String getString(String key, Object[] param) {
        return(MessageFormat.format(INSTANCE.bundle.getString(key), param));
    }
    
    /**
     * Returns the string matching the given key in the resource bundle formatted
     * with the paramters, if a key does not exist return the default string
     */
    public static String getString(String key, String defaultString, Object[] param) {
        if(INSTANCE.bundle.contains(key)) {
            return(MessageFormat.format(INSTANCE.bundle.getString(key), param));
        } else {
            return(MessageFormat.format(defaultString, param));
        }
    }
    
    /**
     * This class implements a resource bundle that plays as a bridge between
     * several property resource bundles because some idiot chose to make 
     * ResourceBundle.setParent() a protected method...
     */
    static class BundleWrapper extends ResourceBundle {
        /**
         * The keys of the resource bundle
         */
        private Map keys = new HashMap();
        
        public void addBundle(ResourceBundle bundle) {
            Enumeration enumeration = bundle.getKeys();
            while(enumeration.hasMoreElements()) {
                Object key = enumeration.nextElement();
                keys.put(key, bundle.getObject((String)key));
            }
        }
        
        /**
         * Returns true if the given key is in the bundle
         */
        public boolean contains(String key) {
            return keys.containsKey(key);
        }
        
        public Enumeration getKeys() {
            return(Collections.enumeration(keys.keySet()));
        }
        
        protected Object handleGetObject(String key) {
            // never return null, this simplifies development and allows 
            // us to work without the appropriate value
            Object value = keys.get(key);
            if(value != null) { 
                return value;
            } else {
                return key;
            }
        }        
    }
}
