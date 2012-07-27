/*
/*
 * $Id: WSDataProvider.java 734 2005-10-16 02:56:20Z rbair $
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

import java.util.Set;
import org.jdesktop.dataset.DataCommand;
import org.jdesktop.dataset.DataConnection;
import org.jdesktop.dataset.DataProvider;
import org.jdesktop.dataset.DataTable;
import org.jdesktop.dataset.provider.LoadTask;
import org.jdesktop.dataset.provider.SaveTask;

/**
 * Base class for DataProvider implementations that leverage web services. In
 * Particular, this base class provides support for the DataSet Web Services
 * Specification, 1.0.
 *
 * @author rbair
 */
public class WSDataProvider extends DataProvider {
    
    /** Creates a new instance of WebServiceDataProvider */
    public WSDataProvider() {
    }

    /** TODO Needs to be added to the DataProvider class */
//    public void abort() {
//	//returned to uninitialized state
//    }
//    
//    protected void getAllResponseHeaders() {} //only valid after send completes, returns map<String,Object>
//    public void getResponseHeader(String header) {}
//    //method: GET, POST, PUT, PROPFIND. url: request url. async: default true. user: optional. password: optional
//    //must call send after calling this. NOTE: This is part of the WebServiceDataConnection!
//    public void open(String method, String url, boolean async, String user, String password) {}
//    /*
//	This method takes one optional parameter, which is the requestBody to use.
//        The acceptable VARIANT input types are BSTR, SAFEARRAY of UI1 (unsigned bytes), 
//        IDispatch to an XML Document Object Model (DOM) object, and IStream *. You can 
//        use only chunked encoding (for sending) when sending IStream * input types. The 
//        component automatically sets the Content-Length header for all but IStream * 
//        input types.
//
//	If the input type is a BSTR, the response is always encoded as UTF-8. The caller
//        must set a Content-Type header with the appropriate content type and include a 
//        charset parameter.
//
//	If the input type is a SAFEARRAY of UI1, the response is sent as is without 
//        additional encoding. The caller must set a Content-Type header with the 
//        appropriate content type.
//
//	If the input type is an XML DOM object, the response is encoded according to
//        the encoding attribute on the <? declaration in the document. If there is no
//        XML declaration or encoding attribute, UTF-8 is assumed.
//
//	If the input type is an IStream *, the response is sent as is without 
//        additional encoding. The caller must set a Content-Type header with the 
//        appropriate content type
//     */
//    public void send() {}
//    public void send(Object obj) {} //String, DOM, byte[], InputStream
//    public void setRequestHeader(String header, Object value) {}
//    
//    public void addReadyStateChangeListener() {}
//      //handleStateChanged()
//    public void removeReadyStateChangeListener() {}
//    public void getReadyState() {} //read-only
//      //0: uninitialized, 1: loading, 2: loaded, 3: interactive, 4: completed
//    public void getResponseBody() {}  //read-only
//      //array of unsigned(!) bytes
//    public void getResponseStream() {} //read-only
//      //input stream
//    public void getResponseText() {} //read-only
//      //String
//    public void getResponseXml() {} //read-only
//      //String -- or some other representation of an XML document?
//    public void getStatus() {} //read-only
//      //Standard HTTP Status code -- from HttpURLConnection
//    public void getStatusText() {} //read-only
//      //String
    
    public DataCommand getCommand() {
	return (WSCommand)super.getCommand();
    }

    public DataConnection getConnection() {
	return (WSDataConnection)super.getConnection();
    }    

    protected SaveTask createSaveTask(Set<DataTable> tables) {
        return new SaveTask(tables) {
            protected void saveData(Set<DataTable> tables) throws Exception {
            }
        };
    }

    protected LoadTask createLoadTask(Set<DataTable> tables) {
        return new LoadTask(tables) {
            protected void readData(Set<DataTable> tables) throws Exception {
            }

            protected void loadData(LoadTask.LoadItem[] items) {
            }
        };
    }
}
