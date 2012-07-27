package org.jdesktop.jdnc.incubator.vprise.util;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.beans.*;
import java.lang.reflect.*;

/**
 * This class simplifies several operations and tasks that need to be performed
 * differently on every platform. It allows us to detect the underlying platform
 * for special cases and allows us to perform platform specific operations
 * simply without special compilation or deployment complexities.
 *
 * @author Shai Almog
 */
public class PlatformUtility {
    private static final PlatformUtility INSTANCE = new PlatformUtility();
    private static final String OS_NAME = System.getProperty("os.name").toUpperCase(); 
    private static final String OS_VERSION = System.getProperty("os.version");
    private static final boolean WINDOWS = OS_NAME.indexOf("WINDOWS") > -1;
    private static final boolean MAC = System.getProperty("mrj.version") != null;
    private static final boolean LINUX = OS_NAME.indexOf("LINUX") > -1;
    private static final boolean UNIX = (!MAC) && (!LINUX) && (File.separatorChar == '/');
    private static final boolean OS2 = OS_NAME.indexOf("OS/2") > -1;
    private static final String PLATFORM_NAME = initOSName();
    
    private static final String initOSName() {
        if(WINDOWS) {
            return "Windows";
        }
        if(MAC) {
            return "Mac";
        }
        if(LINUX) {
            return "Linux";
        }
        if(UNIX) {
            return "Unix";
        }
        if(OS2) {
            return "OS/2";
        }
        return "Unknown";
    }
    
    private PlatformUtility() {}
    
    public static PlatformUtility getInstance() {
        return INSTANCE;
    }
    public boolean isMac() {
        return MAC;
    }
    public boolean isWindows() {
        return WINDOWS;
    }
    public boolean isUnix() {
        return UNIX;
    }
    public boolean isLinux() {
        return LINUX;
    }
    public boolean isOS2() {
        return OS2;
    }
    public boolean hasExitMenu() {
        return !MAC;
    }
    public ResourceBundle getBundle(String name) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(PLATFORM_NAME + name);
            return new MultiResourceBundle(new ResourceBundle[] {bundle, ResourceBundle.getBundle(name)});
        } catch(MissingResourceException err) {
            return ResourceBundle.getBundle(name);
        }
    }
    public ResourceBundle getBundle(String name, Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(PLATFORM_NAME + name, locale);
            return new MultiResourceBundle(new ResourceBundle[] {bundle, ResourceBundle.getBundle(name, locale)});
        } catch(MissingResourceException err) {
            return ResourceBundle.getBundle(name);
        }
    }
    
    static class MultiResourceBundle extends ResourceBundle {
        private ResourceBundle[] bundles;
        private Collection keys = new ArrayList();
        MultiResourceBundle(ResourceBundle[] bundles) {
            this.bundles = bundles;
            for(int iter = 0 ; iter < bundles.length ; iter++) {
                Enumeration e = bundles[iter].getKeys();
                while(e.hasMoreElements()) {
                    Object current = e.nextElement();
                    if(!keys.contains(current)) {
                        keys.add(current);
                    }
                }
            }
        }
        public Enumeration getKeys() {
            return Collections.enumeration(keys);
        }
        protected Object handleGetObject(String key) {
            for(int iter = 0 ; iter < bundles.length ; iter++) {
                try {
                    Object o = bundles[iter].getObject(key);
                    if(o != null) {
                        return o;
                    }
                } catch(Exception err) {
                }
            }
            return null;
        }
    }
    
    public void closeDialogWithEscape(final RootPaneContainer wnd) {
        Action action = new AbstractAction() {
                public void actionPerformed(ActionEvent arg0) {
                        ((java.awt.Window)wnd).dispose();
                }
        };
        JRootPane rootPane = wnd.getRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.getActionMap().put(action, action);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, action);
    }   
    
    public JComponent createPair(String label, char mnemonic, final JComponent cmp) {
        Box box = new Box(BoxLayout.X_AXIS);
        final JLabel l = new JLabel(label);
        l.setDisplayedMnemonic(mnemonic);
        l.setLabelFor(cmp);
        box.add(l);
        box.add(Box.createHorizontalStrut(5));
        box.add(cmp);
        cmp.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                l.setEnabled(cmp.isEnabled());
            }
        });
        return box;
    }
 
    public void bindAboutMac(final ActionListener listener) {
        bindMacCallback(listener, "com.apple.mrj.MRJAboutHandler", "registerAboutHandler");
    }
    
    public void bindQuitMac(final ActionListener listener) {
        bindMacCallback(listener, "com.apple.mrj.MRJQuitHandler", "registerPrefsHandler");
    }
    
    public void bindPrefsMac(final ActionListener listener) {
        bindMacCallback(listener, "com.apple.mrj.MRJPrefsHandler", "registerQuitHandler");
    }
    
    private void bindMacCallback(final ActionListener listener, String intefaceName, String methodName) {
        try {
            if(MAC) {
                Class interfaceClass = Class.forName(intefaceName);
                Object f = Proxy.newProxyInstance(getClass().getClassLoader(),
                                new Class[] { interfaceClass },
                                new InvocationHandler() {
                                    public Object invoke(Object proxy, Method method, Object[] args) {
                                        listener.actionPerformed(null);
                                        return null;
                                    }
                                });
                Class util = Class.forName("com.apple.mrj.MRJApplicationUtils");
                Method m = util.getMethod(methodName, new Class[] {interfaceClass});
                m.invoke(null, new Object[] { f });
            }
        } catch(Exception err) {
            err.printStackTrace();
            return;
        }
    }
}