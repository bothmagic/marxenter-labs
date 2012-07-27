/*
 * $Id: Application.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Window;
import java.beans.Beans;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import org.jdesktop.jdnc.incubator.rbair.swing.actions.ActionManager;

/**
 * <p/>
 * Class which represents central state and properties for a single client
 * application which can be either a standalone Java application (typically
 * initiated using Java WebStart) or a set of one or more Java applets which
 * share the same code base.  There should only be a single Application instance
 * per client application.</p>
 * <p/>
 * This class also encapsulates any functionality which has variable API
 * between applets and Java WebStart applications so that UI components can
 * reliably talk to a single interface for such services.</p>
 *
 * @author Amy Fowler
 * @author Richard Bair
 * @version 1.0
 */
public class Application {
    // Index values for toplevels array
    private static final int WINDOWS = 0;
    private static final int APPLETS = 1;

    private static Map appMap = new Hashtable();
    private static Map imageCache = new HashMap();

    private ActionMap actionMap;

    // The ActionManager instance
    // TODO: investigate if this introduces a circular dependency.
    private ActionManager manager;

    private Vector toplevel[] = new Vector[2];

    private List selectionListeners;

    /**
     * Version of the application.  Defaults to 0.0.0.0
     */
    private Version version = new Version(0, 0, 0, 0);
    /**
     * Name of the application.  Ex. Big Email Server
     */
    private String name = "";
    /**
     * Title to use for the application (may be different from the application's name). If this is null,
     * then the application's name will be used for the title
     */
    private String title = "JDNC Application";
    /**
     * Name of the manufacturer.  Ex. Email Server Enterprises
     */
    private String manufacturer = "";
    /**
     * Copyright statement.  Ex. This code is protected by all international and US copyright laws.
     */
    private String copyrightStatement = "";
    /**
     * Copyright date.  Ex. Jan 1, 2003
     */
    private Date copyrightDate = new Date();
    /**
     * Website for the application.  Ex. http://www.email-server-enterprises.com/bes
     */
    private URL website;
    /**
     * Email address for inquiries etc. regarding the application.  Ex. bes@email-server-enterprises.com
     */
    private URL email;
    /**
     * Small application icon.
     * If set, this image will be displayed in the titlebar of all UI
     * windows shown by this application.  The placement of this image
     * within the titlebar is Look and Feel dependent.
     */
    private Icon icon12x12;
    /**
     * Medium size application icon
     */
    private Icon icon24x24;
    /**
     * Large application icon
     */
    private Icon icon32x32;
    /**
     * Very Large application icon
     */
    private Icon icon48x48;
    /**
     * Splash Screen image.
     */
    private Image splashImage;
    /**
     * The user who is logged in to the application, or who is running the application.  Certain applications
     * may have many simultaneous user's logged in on the same actual running instance.  If this is the case,
     * it is recommended to subclass this class, or make the appropriate patch and submit it :-)
     */
    private User user = new DefaultUser();
    /**
     * This application variable denotes whether the application is running in debug
     * mode or not.  The application is responsible for setting this variable.  It
     * can be set in code, or the application can have code to check the command line
     * for a debug switch, etc.
     */
    private boolean debug = false;
    /**
     * The main frame for the application. Useful for popping open dialogs etc.
     */
    private Frame mainFrame;
    /**
     * Optional connection to the database.
     */
    private static Connection connection;
    /**
     * Optional datasource to the database. This is the javax.sql.DataSource.
     */
    private DataSource datasource;
    /**
     * A properties hash map that contains miscellaneous props that the application wants to keep track of. These are indexed by
     * Strings, and the values are just Objects.
     */
    private Map properties = new HashMap();
    /**
     * The name of the current working directory for this application (this would primarily be changed when the user
     * uses a file open dialog or some such thing)
     */
    private String workingDirectory;
    /**
     * The base URL for this application.
     * TODO This is really just for applets, no?
     */
    private URL baseURL;
    /**
     * The global <code>ProgressManager</code> instance to be used by components
     * within the scope of this Application for managing progress notification
     * for background threads and/or tasks.
     */
    private ProgressManager progressManager = new ProgressManager();

    /**
     * Private constructor so that an Application can't be directly instantated.
     */
    public Application() {
    }

    /**
     * Factory method for obtaining the Application instance associated with this
     * application.  The Application object will be instantiated if it does not
     * already exist. This method is intended for use by standalone applications
     * where there may be one and only one JDNCapp instance.
     *
     *
     * TODO what would happen if the string theone was used as a key by some
     * application calling getInstance(Object key)? Would it fail?
     * @return Application instance for application
     */
    public static Application getInstance() {
        return getInstance("theone");
    }

    /**
     * Factory method for obtaining the Application instance associated with the
     * application designated by the specified key.  The Application object will
     * be instantiated if it does not already exist. This method is intended
     * for use by applets, where there may be multiple Application instances in
     * a running VM.
     *
     * @param key object designating the application
     * @return Application instance for application
     */
    public static Application getInstance(Object key) {
        Application app = null;
        synchronized (appMap) {
            app = (Application) appMap.get(key);
            if (app == null) {
                try {
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
                    app = (Application) Beans.instantiate(cl,
                            "org.jdesktop.jdnc.incubator.rbair.swing.Application");
                    appMap.put(key, app);
                } catch (Exception ex) {
                    // XXX eception
                    ex.printStackTrace();
                }
                //REMIND(aim): memory leak - when to remove the app entry for applets
            }
        }
        return app;
    }

    /**
     * Convenience method for getting the JDNCapp instance given
     * a component instance. The component instance must be contained in
     * a containent hierarchy which has either a Window or an Applet instance
     * as the root.
     *
     * @param c the ui component
     * @return Application instance for the specified component's application
     */
    public static Application getApp(Component c) {
        Application app = null;
        Container parent = c instanceof Container ? (Container) c : c.getParent();
        while (parent != null) {
            if (parent instanceof Window) {
                app = findApp(WINDOWS, parent);
                break;
            } else if (parent instanceof Applet) {
                app = findApp(APPLETS, parent);
                break;
            } else {
                parent = parent.getParent();
            }
        }
        // There is no app associated with this component.
        // Create one and register it with the root container
        if (app == null) {
            Component p = SwingUtilities.getRoot(c);
            if (p != null) {
                app = Application.getInstance(p);
                if (p instanceof Applet) {
                    app.registerApplet((Applet) p);
                } else {
                    app.registerWindow((Window) p);
                }
            }
        }
        return app;
    }

    private static Application findApp(int type, Component c) {
        Application app = null;
        Iterator apps = appMap.values().iterator();
        while (app == null && apps.hasNext()) {
            Application a = (Application) apps.next();
            if (a.toplevel[type] != null) {
                if (a.toplevel[type].contains(c)) {
                    app = a;
                    break;
                }
            }
        }
        return app;
    }

    /**
     * Return the action manager for this application.
     *
     * @return the action manager instance
     */
    public ActionManager getActionManager() {
	if (manager == null) {
	    manager = new ActionManager();
	}
	return manager;
    }
    
    /**
     * @return the ProgressManager
     */
    public ProgressManager getProgressManager() {
        return progressManager;
    }

    /**
     * Will retrieve the applet base url if this application is running in an
     * applet. Otherwise, it will try to retrieve the base URL of the
     * xml configuration file.
     */
    public static URL getBaseURL(Object obj) {
        URL url = null;
        if (obj instanceof Component) {
            Container parent = SwingUtilities.getAncestorOfClass(JApplet.class,
                    (Component) obj);
            if (parent != null) {
                JApplet applet = (JApplet) parent;
                url = applet.getDocumentBase();
            } else {
                /*WebStartContext context = WebStartContext.getInstance();
                // FIXME: this should be spawned in a SwingWorker.
                url = context.getDocumentBase();*/
            }
            if (url == null) {
                Application app = Application.getApp((Component) obj);
                if (app != null) {
                    url = app.getBaseURL();
                }
            }
        }
        if (url == null) {
            url = Application.getInstance().getBaseURL();
        }
        return url;
    }

    /**
     * Fetches a url of a resource value using the clasloader and
     * relative path of obj.
     * Will first try to load from the classpath, then the direct url and then
     * look for a base url.
     */
    public static URL getURL(String value, Object obj) {
        URL url = getURLResource(value, obj);
        if (url == null) {
            try {
                url = new URL(value);
            } catch (MalformedURLException ex) {
                // fall through
            }
        }
        if (url == null) {
            URL base = Application.getBaseURL(obj);
            if (base != null) {
                try {
                    url = new URL(base, value);
                } catch (MalformedURLException e) {
                    // fall through
                }
            }
        }
        if (url == null) {
            // System.err.println("getURL: no url for value: " + value + " obj: " + obj);
        }
        return url;
    }

    public static URL getURLResource(String value, Object obj) {
        return obj.getClass().getResource(value);
    }


    public static Image getImage(String name, Object obj) {
        Icon icon = getIcon(name, obj);
        if (icon != null) {
            return ((ImageIcon) icon).getImage();
        } else {
            return null;
        }
    }

    public static Icon getIcon(String name, Object obj) {
        Icon icon = null;

        if (name == null || (icon = (Icon) imageCache.get(name)) != null) {
            return icon;
        }

        URL fileLoc = getURL(name, obj);
        if (fileLoc != null && (icon = new ImageIcon(fileLoc)) != null) {
            imageCache.put(name, icon);
        }
        return icon;
    }

    /**
     * Shows the URL at the target.
     */
    public void showDocument(URL url, String target) {
        Iterator iter = getApplets();
        if (iter != null) {
            // Get the first applet
            Applet applet = (Applet) iter.next();
            AppletContext context = applet.getAppletContext();
            // if target is _self, _parent, _top, _blank, show the document in
            // a browser.
            // FIXME: this should be spawned in a SwingWorker.
            context.showDocument(url, target);
        } else {
            /*WebStartContext context = WebStartContext.getInstance();
            // FIXME: this should be spawned in a SwingWorker.
            context.showDocument(url, target);*/
        }
    }

    /**
     * Sets the &quot;version&quot; property of this application.
     *
     * @param versionString string containing the version of this application
     */
    public void setVersionString(String versionString) {
        version = Version.decode(versionString);
    }

    /**
     * @return String containing the version of this application
     */
    public String getVersionString() {
        return version.toString();
    }

    /**
     * @return
     */
    public Date getCopyrightDate() {
        return copyrightDate;
    }

    /**
     * @return
     */
    public String getCopyrightStatement() {
        return copyrightStatement;
    }

    /**
     * @return
     */
    public URL getEmail() {
        return email;
    }

    /**
     * @return
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the &quot;title&quot; property of this application.
     *
     * @param title string containing the title of this application
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return String containing the title of this application
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return
     */
    public Version getVersion() {
        return version;
    }

    /**
     * @return
     */
    public URL getWebsite() {
        return website;
    }

    /**
     * @param date
     */
    public void setCopyrightDate(Date date) {
        copyrightDate = date;
    }

    /**
     * @param string
     */
    public void setCopyrightStatement(String string) {
        copyrightStatement = string;
    }

    /**
     * @param url
     */
    public void setEmail(URL url) {
        email = url;
    }

    /**
     * @param string
     */
    public void setManufacturer(String string) {
        manufacturer = string;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param version
     */
    public void setVersion(Version version) {
        this.version = version;
    }

    /**
     * @param url
     */
    public void setWebsite(URL url) {
        website = url;
    }

    /**
     *
     */
    public Icon getIcon12x12() {
        return icon12x12;
    }

    /**
     * @return
     */
    public Icon getIcon24x24() {
        return icon24x24;
    }

    /**
     * @return
     */
    public Icon getIcon32x32() {
        return icon32x32;
    }

    /**
     * @return Returns the icon48x48.
     */
    public Icon getIcon48x48() {
        return icon48x48;
    }

    /**
     * @return Image displayed in the application's splash screen
     */
    public Image getSplashImage() {
        return splashImage;
    }

    /**
     * Sets the &quot;icon12x12&quot; property of this application.
     * If set, this image will be displayed in the titlebar of all UI
     * windows shown by this application.  The placement of this image
     * within the titlebar is Look and Feel dependent.
     *
     * @param icon image displayed in titlebar of application's toplevel windows
     */
    public void setIcon12x12(Icon icon) {
        icon12x12 = icon;
    }

    /**
     * @param icon
     */
    public void setIcon24x24(Icon icon) {
        icon24x24 = icon;
    }

    /**
     * @param icon
     */
    public void setIcon32x32(Icon icon) {
        icon32x32 = icon;
    }

    /**
     * @param icon The icon to set.
     */
    public void setIcon48x48(Icon icon) {
        icon48x48 = icon;
    }

    /**
     * Sets the &quot;splashImage&quot; property of this application.
     * If set, this image will be rendered in the splash screen which
     * is displayed momentarily at application startup while the
     * application initializes.
     *
     * @param splashImage image displayed in the application's splash screen
     */
    public void setSplashImage(Image splashImage) {
        this.splashImage = splashImage;
    }

    /**
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user
     */
    public void setUser(User user) {
        assert user != null;
        this.user = user;
    }

    /**
     * @return Returns the debug.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @param debug The debug to set.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * @return Returns the mainFrame.
     */
    public Frame getMainFrame() {
        return mainFrame;
    }

    /**
     * @param mainFrame The mainFrame to set.
     */
    public void setMainFrame(Frame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * @return Returns the connection.
     */
    public Connection getConnection() {
        if (connection == null && datasource != null) {
            try {
                Connection conn = datasource.getConnection();
                conn.setAutoCommit(true);
                return conn;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return connection;
        }
    }

    /**
     * @param connection The connection to set.
     */
    public static void setConnection(Connection connection) {
        Application.connection = connection;
    }

    /**
     * @return
     */
    public DataSource getDataSource() {
        return datasource;
    }

    /**
     * @param datasource
     */
    public void setDataSource(DataSource datasource) {
        this.datasource = datasource;
    }

    /**
     * Returns the given application level property, based on name, or the default object defObj if the
     * property is not stored in this application object.
     *
     * @param name
     * @param defObj
     * @return
     */
    public Object getProperty(String name, Object defObj) {
        Object obj = properties.get(name);
        return obj == null ? defObj : obj;
    }

    /**
     * Sets the given named property to the given value.
     *
     * @param name
     * @param obj
     */
    public void setProperty(String name, Object obj) {
        properties.put(name, obj);
    }

    /**
     * Returns the working directory. Returns the home directory by default.
     *
     * @return
     */
    public String getWorkingDirectory() {
        return workingDirectory == null ? "." : workingDirectory;
    }

    /**
     * Sets the current working directory to the one given. A null value will reset to the home directory
     *
     * @param directory
     */
    public void setWorkingDirectory(String directory) {
        workingDirectory = directory == null ? "." : directory;
    }

    /**
     * Sets the &quot;baseURL&quot; property of this application.
     *
     * @param baseURL URL of codebase for this application
     */
    public void setBaseURL(URL baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * @return URL of codebase for this application
     */
    public URL getBaseURL() {
        return baseURL;
    }

    /**
     * A little string representation of this application object.
     *
     * @return
     */
    public String asString() {
        return name;
    }

    /**
     * Returns <code>true</code> if running as a standalone application and
     * returns <code>false</code> if running ne or more applets.
     *
     * @return boolean value indicating whether this client is running as a
     *         standalone application
     */
    public boolean isStandalone() {
        return getApplets() == null;
    }

    /**
     * @return boolean value indicating whether or not this application is
     *         running in the security sandbox
     */
    public boolean isRunningInSandbox() {
        boolean inSandbox = false;
        try {
            new File(".");
        } catch (SecurityException e) {
            inSandbox = true;
        }
        return inSandbox;
    }

    /**
     * Registers a window with the application instance.
     */
    public void registerWindow(Window window) {
        register(WINDOWS, window);
    }

    public void unregisterWindow(Window window) {
        unregister(WINDOWS, window);
    }

    public void registerApplet(Applet applet) {
        register(APPLETS, applet);
    }

    public void unregisterApplet(Applet applet) {
        unregister(APPLETS, applet);
    }

    /**
     * Return the action map associated with this application.
     * The action map holds all the global application actions.
     */
    public ActionMap getActionMap() {
        if (actionMap == null) {
            actionMap = new ActionMap();
        }
        return actionMap;
    }

    /**
     * @return iterator containing all applets registered with this app instance
     *         or null if the app was instantiated from a standalone application
     */
    public Iterator getApplets() {
        return getToplevels(APPLETS);
    }

    /**
     * @return iterator containing all windows registered with this app instance
     *         or null if there were no toplevel windows registered
     */
    public Iterator getWindows() {
        return getToplevels(WINDOWS);
    }
//
//    public void addSelectionListener(SelectionListener l) {
//        if (selectionListeners == null) {
//            selectionListeners = new ArrayList();
//        }
//        selectionListeners.add(l);
//    }
//
//    public void removeSelectionListener(SelectionListener l) {
//        if (selectionListeners != null) {
//            selectionListeners.remove(l);
//        }
//    }
//
//    public SelectionListener[] getSelectionListeners() {
//        if (selectionListeners != null) {
//            return (SelectionListener[])selectionListeners.toArray(
//                             new SelectionListener[1]);
//        }
//        return new SelectionListener[0];
//    }

    private void register(int type, Component c) {
        if (toplevel[type] == null) {
            toplevel[type] = new Vector();
        }
        toplevel[type].add(c);
    }

    private void unregister(int type, Component c) {
        if (toplevel[type] != null) {
            toplevel[type].remove(c);
        }
    }

    private Iterator getToplevels(int type) {
        if (toplevel[type] == null) {
            return null;
        }
        return toplevel[type].iterator();
    }
}
