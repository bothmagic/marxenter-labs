/*
 * $Id: WebStartContext.java 34 2004-09-07 23:05:36Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing;

import java.net.URL;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

/**
 * A class that provides the similar services to AppletContext for Java WebStarted
 * applications.
 */
class WebStartContext {

    private static WebStartContext INSTANCE;

    private WebStartContext() {}

    public static WebStartContext getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new WebStartContext();
	}
	return INSTANCE;
    }

    /**
     * Retrieves the web start basic service.
     * @return the basic service instance or null if it is not available.
     */
    BasicService getBasicService() {
	BasicService bs = null;
	try {
	    bs = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
	} catch (UnavailableServiceException ex) {
	    // XXX - handle better.
	    System.err.println("WebStartContext - BasicServiceUnavaiable");
	}
	return bs;
    }
    
    public void showDocument(URL url, String target) {
	BasicService bs = getBasicService();
	if (bs != null) {
	    if (bs.showDocument(url) == false) {
		// XXX -
		System.err.println("WebStartContext - Error showing url: " + url);
	    }
	}
    }

    /**
     * Returns the codebase URL of the application. The code base is either specified 
     * directly in the JNLP file or it's the location of the jar file containing
     * the main class of the application.
     * 
     * @return the url of the code base or null.
     */
    public URL getDocumentBase() {
	BasicService bs = getBasicService();
	if (bs != null) {
	    return bs.getCodeBase();
	}
	return null;
    }
}
