/*
 * Created on 12.12.2006
 *
 */
package org.jdesktop.swingx.link;

import java.awt.event.ActionEvent;
import java.net.URL;

import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.hyperlink.LinkModel;

/**
 * A visiting action which can cope with both LinkModel and 
 * URL. <p>
 * 
 * "visting" means that it expects the target to act upon as
 * source in the ActionEvent. LinkModelAction uses this as 
 * delegate for the actual "visit" work to do.
 * 
 */
public class BrowserLinkVisitor extends AbstractActionExt {

    private BrowserLauncher launcher;

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof LinkModel) {
            visit((LinkModel) e.getSource());
        } else if (e.getSource() instanceof URL) {
            visit((URL) e.getSource());
        }    
    }

    protected void visit(LinkModel link) {
        boolean success = visit(link.getURL());
        if (success) {
            link.setVisited(true);
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
