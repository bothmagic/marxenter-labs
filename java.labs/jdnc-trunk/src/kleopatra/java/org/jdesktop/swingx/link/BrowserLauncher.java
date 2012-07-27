/*
 * Created on 12.12.2006
 *
 */
package org.jdesktop.swingx.link;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * Copied from painter_work branch OpenBrowserAction (the opening browser part of it).
 * @author joshy
 */
public class BrowserLauncher {

    public boolean doLaunch(URL url) {
        try {
            if(isApplet()) {
                dp("trying applet version");
                openApplet(url);
                dp("succeeded applet version");
            } else if(isWebStart()) {
                dp("trying applet version");
                openWebstart(url);
                dp("succeeded applet version");
            } else if (macosx) {
                dp("trying Mac OS X version");
                openMacOSX(url);
                dp("succeeded mac os x version");
            } else if (linux) {
                dp("trying linux version");
                openLinux(url);
                dp("succeeded linux version");
            } else if (win95) {
                dp("trying win95 version");
                openWin95(url);
                dp("succeeded win95 version");
            } else if (winAny) {
                dp("trying windows any version");
                openWindows(url);
                dp("succeeded windows any version");
            } else {
                dp("trying other version");
                openOther(url);
                dp("succeeded other version");
            }
            return true;
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    /* code to actuall open the browser on all platforms */
    private static final String osName = System.getProperty("os.name" ).toLowerCase();
    
    private static final boolean linux = osName.startsWith("linux");
    private static final boolean macosx = osName.startsWith("mac os x");
    private static final boolean win95 = osName.equals("windows 95");
    private static final boolean winAny = osName.startsWith("windows");
    private static final boolean DEBUG = true;
    private static void dp(String str) {
        if(DEBUG) { System.out.println(str); }
    }
    

    protected void handleError(Throwable thr) {
        System.err.println(thr.getMessage());
        thr.printStackTrace(System.err);
    }
    
    private static void openMacOSX(URL url) throws IOException {
        try {
            dp("trying mac version using FileManager class");
            // Mac OS X can handle file types itself (thank goodness!)
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL",
                    new Class[] {String.class});
            openURL.invoke(null, new Object[] {url.toString()});
        } catch (Throwable thr) {
            dp("trying 'open' command version");
            thr.printStackTrace();
            // if that fails for some reason, try the 'open' commandline command
            Runtime.getRuntime().exec(new String[] {"open", url.toString()});
        }
    }
    
    private void openLinux(URL url) throws IOException, InterruptedException, Exception {
        try {
            // try the xdg service first. part of free desktop?
            Runtime.getRuntime().exec(new String[] {"./xdg-open", url.toString()});
        } catch (Throwable thr) {
            
            // if xdg fails
            // look for one of the browsers
            String[] browsers = { // list of html browsers
                "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++) {
                if (Runtime.getRuntime().exec(
                        new String[] {"which", browsers[count]}).waitFor() == 0) {
                    browser = browsers[count];
                }
            }
            
            if (browser == null) { // got no browser, bummer
                throw new Exception("Could not find web browser");
            } else {// got a browser, run it
                Runtime.getRuntime().exec(new String[] {browser, url.toString()});
            }
        }
    }
    
    private void openWin95(URL url) throws IOException {
        Runtime.getRuntime().exec(new String[] {"command.com", "/C", "start", url.toString()});
    }
    
    private void openWindows(URL url) throws IOException {
        Runtime.getRuntime().exec(new String[] {"cmd.exe", "/C", "start", url.toString()});
    }
    
    private void openOther(URL url) throws IOException {
        Runtime.getRuntime().exec(new String[] {"open", url.toString()});
    }
    
    private boolean isWebStart() {
        try {
            Class svcManager = Class.forName("jnlp.ServiceManager");
            Method lookup = svcManager.getDeclaredMethod("lookup",
                    new Class[] {String.class});
            lookup.invoke(null, new Object[] {"jnlp.BasicService"});
            return true;
        } catch (Throwable thr) {
            return false;
        }
    }
    
    private void openWebstart(URL url) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class svcManager = Class.forName("javax.jnlp.ServiceManager");
        Method lookup = svcManager.getDeclaredMethod("lookup",
                new Class[] {String.class});
        Object basicSvcInst = lookup.invoke(null, new Object[] {"javax.jnlp.BasicService"});
        Class basicSvc = Class.forName("javax.jnlp.BasicService");
        Method showDocument = basicSvc.getDeclaredMethod("showDocument",
                new Class[] {URL.class});
        showDocument.invoke(basicSvcInst, new Object[] {url});
    }

    private boolean isApplet() {
        return false;
    }

    private void openApplet(URL url) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
