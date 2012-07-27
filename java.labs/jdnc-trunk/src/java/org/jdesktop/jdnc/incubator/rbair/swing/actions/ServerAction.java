/*
 * $Id: ServerAction.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

/**
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 */
package org.jdesktop.jdnc.incubator.rbair.swing.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.AccessControlException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.jdesktop.jdnc.incubator.rbair.swing.Application;


/**
 * An action which will invoke an http POST operation.
 *
 * @author Mark Davidson
 */
public class ServerAction extends AbstractAction {
    // Server action support

    private static final String PARAMS = "action-params";
    private static final String HEADERS = "action-headers";
    private static final String URL = "action-url";

    private static final String URL_CACHE = "_URL-CACHE__";

    public ServerAction() {
	this("action");
    }

    public ServerAction(String name) {
	super(name);
    }

    /**
     * @param name display name of the action
     * @param command the value of the action command key
     */
    public ServerAction(String name, String command) {
	this(name, command, null);
    }

    public ServerAction(String name, Icon icon) {
	super(name, icon);
    }

    /**
     * @param name display name of the action
     * @param command the value of the action command key
     * @param icon icon to display
     */
    public ServerAction(String name, String command, Icon icon) {
	super(name, icon);
	putValue(Action.ACTION_COMMAND_KEY, command);
    }

    /**
     * Set the url for the action.
     * <p>
     * @param url a string representation of the url
     */
    public void setURL(String url) {
	putValue(URL, url);
	putValue(URL_CACHE, null);
    }

    public String getURL() {
	return (String)getValue(URL);
    }

    private Map getParams() {
	return (Map)getValue(PARAMS);
    }

    private void setParams(Map params) {
	putValue(PARAMS, params);
    }

    /**
     * Adds a name value pair which represents a url parameter in an http
     * POST request.
     */
    public void addParam(String name, String value) {
	Map params = getParams();
	if (params == null) {
	    params = new HashMap();
	    setParams(params);
	}
	params.put(name, value);
    }

    /**
     * Return a parameter value corresponding to name or null if it doesn't exist.
     */
    public String getParamValue(String name) {
	Map params = getParams();
	return params == null ? null : (String)params.get(name);
    }

    /**
     * Return a set of parameter names or null if there are no params
     */
    public Set getParamNames() {
	Map params = getParams();
	return params == null ? null : params.keySet();
    }

    private Map getHeaders() {
	return (Map)getValue(HEADERS);
    }

    private void setHeaders(Map headers) {
	putValue(HEADERS, headers);
    }

    /**
     * Adds a name value pair which represents a url connection request property.
     * For example, name could be "Content-Type" and the value could be
     * "application/x-www-form-urlencoded"
     */
    public void addHeader(String name, String value) {
	Map map = getHeaders();
	if (map != null) {
	    map = new HashMap();
	    setHeaders(map);
	}
	map.put(name, value);
    }

    /**
     * Return a header value corresponding to name or null if it doesn't exist.
     */
    public String getHeaderValue(String name) {
	Map headers = getHeaders();
	return headers == null ? null : (String)headers.get(name);
    }

    /**
     * Return a set of parameter names or null if there are no params
     */
    public Set getHeaderNames() {
	Map headers = getHeaders();
	return headers == null ? null : headers.keySet();
    }

    /**
     * Invokes the server operation when the action has been invoked.
     */
    public void actionPerformed(ActionEvent evt) {
	URL execURL = (URL)getValue(URL_CACHE);
	if (execURL == null && !"".equals(getURL())) {
	    try {
		String url = getURL();
		if (url.startsWith("http")) {
		    execURL = new URL(url);
		} else {
		    // Create the URL based on the enclosing applet.
		    execURL = Application.getURL(url, this);
		}
		if (execURL == null) {
		    // XXX TODO: send a message
		    return;
		} else {
		    // Cache this value.
		    putValue(URL_CACHE, execURL);
		}

		/*
		if (Debug.debug) {
		    System.out.println("ServerAction: URL created: " + execURL.toString());
		}
		*/
	    } catch (MalformedURLException ex) {
		ex.printStackTrace();
	    }
	}

	try {
	    URLConnection uc = execURL.openConnection();

	    // Get all the header name/value pairs ans set the request headers
	    Set headerNames = getHeaderNames();
	    if (headerNames != null && !headerNames.isEmpty()) {
		Iterator iter = headerNames.iterator();
		while (iter.hasNext()) {
		    String name = (String)iter.next();
		    uc.setRequestProperty(name, getHeaderValue(name));
		}
	    }
	    uc.setUseCaches(false);
	    uc.setDoOutput(true);

	    ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512);
	    PrintWriter out = new PrintWriter(byteStream, true);
	    out.print(getPostData());
	    out.flush();

	    // POST requests must have a content-length.
	    String length = String.valueOf(byteStream.size());
	    uc.setRequestProperty("Content-length", length);

	    // Write POST data to real output stream.
	    byteStream.writeTo(uc.getOutputStream());

	    BufferedReader buf = null;
	    if (uc instanceof HttpURLConnection) {
		HttpURLConnection huc = (HttpURLConnection)uc;
		int code = huc.getResponseCode();
		String message = huc.getResponseMessage();

		// Handle the result.
		if (code < 400) {
		    // action succeeded send to status bar
		    // XXX TODO: setStatusMessage(createMessage(code, message));
		    // Format the response
		    // TODO: This should load asychnonously
		    buf = new BufferedReader(new InputStreamReader(uc.getInputStream()));

		} else {
		    // action has failed show dialog
		    // XXX TODO: setStatusMessage(createMessage(code, message));
		    buf = new BufferedReader(new InputStreamReader(huc.getErrorStream()));
		}
		String line;

		StringBuffer buffer = new StringBuffer();
		while ((line = buf.readLine()) != null) {
            // RG: Fix for J2SE 5.0; Can't cascade append() calls because
            // return type in StringBuffer and AbstractStringBuilder are different
		    buffer.append(line);
            buffer.append('\n');
		}
		if (Debug.debug) {
		    // XXX So now that we have results in the StringBuffer, we should do something
		    // with it.
		    System.out.println(buffer.toString());
		}
	    }
	} catch (UnknownHostException uhe) {
	    Debug.printException("UnknownHostException detected. Could it be a proxy issue?\n" +
				 uhe.getMessage(), uhe);
	} catch (AccessControlException aex) {
	    Debug.printException("AccessControlException detected\n" +
			    aex.getMessage(), aex);
	} catch (IOException ex) {
	    Debug.printException("IOException detected\n" +
				 ex.getMessage(), ex);
	}
    }

    /**
     * Retrieves a string which represents the parameter data for a server action.
     * @return a string of name value pairs prefixed by a '?' and delimited by an '&'
     */
    private String getPostData() {
	// Write the data into local buffer
	StringBuffer postData = new StringBuffer();

	// TODO: the action should be configured to retrieve the data.

	// Get all the param name/value pairs and build the data string
	Set paramNames = getParamNames();
	if (paramNames != null && !paramNames.isEmpty()) {
	    Iterator iter = paramNames.iterator();
        try {
            while (iter.hasNext()) {
                String name = (String) iter.next();
                postData.append('&').append(name).append('=');
                postData.append(getParamValue(name));
            }
        }
        catch (Exception ex) {  // RG: append(char) throws IOException in J2SE 5.0
            /** @todo Log it */
        }
	    // Replace the first & with a ?
	    postData.setCharAt(0, '?');
	}
	if (Debug.debug) {
	    System.out.println("ServerAction: POST data: " + postData.toString());
	}
	return postData.toString();
    }

    /**
     * Retrieves the text from the text component.
     * TODO: should use selection criteria to select text.
     */
    private StringBuffer getDataBuffer() throws UnsupportedEncodingException {
	StringBuffer buffer = new StringBuffer("content=");
	/*
	if (listener != null) {
	    buffer.append(URLEncoder.encode(listener.getText(), "UTF-8"));
	} else {
	    buffer.append("ServerAction ERROR: text component has not been set");
	    }*/
	return buffer;
    }

    /**
     * Creates a human readable message from the server code and message result.
     * @param code an http error code.
     * @param msg server message
     */
    private String createMessage(int code, String msg) {
	StringBuffer buffer = new StringBuffer("The action \"");
	buffer.append(getValue(NAME));

	if (code < 400) {
	    buffer.append("\" has succeeded ");
	} else {
	    buffer.append("\" has failed\nPlease check the Java console for more details.\n");
	}
    // RG: Fix for J2SE 5.0; Can't cascade append() calls because
    // return type in StringBuffer and AbstractStringBuilder are different
	buffer.append("\nServer response:\nCode: ");
    buffer.append(code);
	buffer.append(" Message: ");
    buffer.append(msg);

	return buffer.toString();
    }
}