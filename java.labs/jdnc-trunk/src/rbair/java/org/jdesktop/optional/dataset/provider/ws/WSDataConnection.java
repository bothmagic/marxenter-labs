/*
 * $Id: WSDataConnection.java 734 2005-10-16 02:56:20Z rbair $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.optional.dataset.provider.ws;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.jdesktop.dataset.DataConnection;
/**
 *
 * @author rbair
 */
public class WSDataConnection extends DataConnection {
    private URL server;
    private HttpURLConnection conn;
    private InputStream inputStream;
    private OutputStream outputStream;
    
    /** Creates a new instance of WebServiceDataConnection */
    public WSDataConnection(URL serverUrl) {
	this.server = serverUrl;
    }

    protected void disconnect() throws Exception {
	try {
	    inputStream.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    outputStream.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    protected void connect() throws Exception {
	try {
	    conn = (HttpURLConnection)server.openConnection();
	    configureConnection(conn);
	    conn.connect();
	    inputStream = conn.getInputStream();
	    outputStream = conn.getOutputStream();
	} catch (Exception e) {
	    throw new Exception("Failed to open connection to resource " + server, e);
	}
    }
    
    protected void configureConnection(URLConnection connection) {
    }
}
