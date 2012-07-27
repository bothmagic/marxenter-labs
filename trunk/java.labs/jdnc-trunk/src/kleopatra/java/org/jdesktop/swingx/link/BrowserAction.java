/*
 * Created on 12.12.2006
 *
 */
package org.jdesktop.swingx.link;

import java.awt.event.ActionEvent;
import java.net.URL;

import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;

/**
 * A LinkAction specialized in opening URL in the system browser.
 * 
 */
public class BrowserAction extends AbstractHyperlinkAction<URL> {

    private BrowserLauncher launcher;
    
    public BrowserAction(URL resource) {
        super(resource);
    }

    public BrowserAction() {
        // TODO Auto-generated constructor stub
    }

    public void actionPerformed(ActionEvent e) {
        boolean visited = (getTarget() != null) && (visit(getTarget()));
        if (visited) {
            setVisited(visited);
        }
    }

    private boolean visit(URL url) {
        return getBrowserLauncher().doLaunch(url);
    }
    
    private BrowserLauncher getBrowserLauncher() {
        if (launcher == null) {
            launcher = new BrowserLauncher();
        }
        return launcher;
    }
    

}
