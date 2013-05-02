/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * @author marxma
 */
public class CommandFactory {

    public final static int API_VERSION_1 = 1;
    public final static int API_VERSION_2 = 2;
    
    public static CommandFactory INSTANCE = new CommandFactory();
    private Map<String, String> tokenMap = new HashMap<String, String>();
    private Map<String, XmlRpcClient> clientMap = new HashMap<String, XmlRpcClient>();
    private Map<String, Connection> connectionMap = new HashMap<String, Connection>();
    private Map<String, String> userMap = new HashMap<String, String>();
    private Map<String, String> pwdMap = new HashMap<String, String>();

    private CommandFactory() {
    }

    public void login(String serverUrl, String user, String password) throws XmlRpcException, MalformedURLException {
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setConnectionTimeout(2000);
            config.setReplyTimeout(2000);
            //config.setEnabledForExceptions(true);
            //config.setEnabledForExtensions(true);
            config.setServerURL(new URL(serverUrl + "/rpc/xmlrpc"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            clientMap.put(serverUrl, client);
            String token = (String) client.execute("confluence1.login", new Object[]{user, password});
            tokenMap.put(serverUrl, token);
            userMap.put(serverUrl, user);
            pwdMap.put(serverUrl, password);
        } catch (org.apache.xmlrpc.XmlRpcException ex) {
            Logger.getLogger(CommandFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new XmlRpcException(ex.getMessage(), ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(CommandFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
    public void dblogin(String serverUrl, String jdbcUrl, String user, String password) 
            throws SQLException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection conn = DriverManager.getConnection(jdbcUrl,
                    user, password);
            connectionMap.put(serverUrl, conn);
            Logger.getLogger(CommandFactory.class.getName()).log(Level.FINEST, "create connection for server " + serverUrl);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CommandFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public String getUser(String serverUrl) {
        return userMap.get(serverUrl);
    }
    
    public String getPassword(String serverUrl) {
        return pwdMap.get(serverUrl);
    }
    
    public String convertToStorageFormat(String server, String wikiMarkup) throws XmlRpcException {
        return (String)execute(server, "convertWikiToStorageFormat", Arrays.asList(new Object[] {wikiMarkup}), API_VERSION_2);
    }
    
    public Object execute(String serverUrl, String command, List params, int apiVersion) throws XmlRpcException {
        XmlRpcClient client = clientMap.get(serverUrl);
        String token = tokenMap.get(serverUrl);
        if (client == null || token == null) {
            throw new IllegalArgumentException("no client for server " + serverUrl + "found. Did you get logged in?");
        }
        params = new ArrayList(params);
        params.add(0, token);
        Object result = null;
        if (apiVersion == 1) {
            try {
                result = client.execute("confluence1."+command, params);
                
            } catch (org.apache.xmlrpc.XmlRpcException ex) {
                throw new XmlRpcException(ex.getMessage(), ex);
            }
        } else {
            try {
                result = client.execute("confluence2."+command, params);
            } catch (org.apache.xmlrpc.XmlRpcException ex) {
                throw new XmlRpcException(ex.getMessage(), ex);
            }
        }
        return result;
    }

    public String getToken(String serverUrl) {
        return tokenMap.get(serverUrl);
    }

    public Map<String, String> getPage(String server, String spaceId, String pageTitle, int apiVersion) throws XmlRpcException {
        Map<String, String> srcPageInfo = null;
        srcPageInfo = (Map<String, String>) execute(server, "getPage", Arrays.asList(new Object[]{spaceId, pageTitle}), apiVersion);
        return srcPageInfo;
    }


    public Map<String, String> getPage(String server, String pageId, int apiVersion) throws XmlRpcException {
        Map<String, String> srcPageInfo = null;    
        srcPageInfo = (Map<String, String>) execute(server, "getPage", Arrays.asList(new Object[]{pageId}), apiVersion);
        return srcPageInfo;
    }
    
    public void close() {
        for (Entry<String, Connection> entry : connectionMap.entrySet()) {
            try {
                entry.getValue().close();
            } catch (SQLException ex) {
                Logger.getLogger(CommandFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Connection getConnection(String server) {
        return connectionMap.get(server);
    }
}
