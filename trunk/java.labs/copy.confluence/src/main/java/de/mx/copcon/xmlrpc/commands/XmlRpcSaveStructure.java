/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.xmlrpc.commands;

import de.mx.copcon.InformationCallback;
import de.mx.copcon.Command;
import de.mx.copcon.xmlrpc.RpcCommandException;
import de.mx.copcon.CommandFactory;
import de.mx.copcon.XmlRpcException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marxma
 */
public class XmlRpcSaveStructure implements Command {

    private List<String> pageStructure;
    private String server;
    private String space;
    private String mainPage;

    public XmlRpcSaveStructure(List<String> pageStructure, String server, String space, String page) {
        this.pageStructure = pageStructure;
        this.server = server;
        this.space = space;
        this.mainPage = page;
    }

    @Override
    public void run(InformationCallback info) {

        CommandFactory xmlrpc = CommandFactory.INSTANCE;

        for (String path : pageStructure) {

            StringTokenizer tok = new StringTokenizer(path, "//");
            String page = tok.nextToken();
            String parent = mainPage;
            while (tok.hasMoreTokens()) {
                parent = page;
                page = tok.nextToken();
            }

            Map<String, String> parentInfo = null;
            if (parent != null) {
                try {
                    parentInfo = xmlrpc.getPage(server, space, parent, CommandFactory.API_VERSION_2);
                } catch (XmlRpcException ex) {
                    if (info != null) {
                        info.addError("could not retrieve page " + server + "/" + space + "/" + parent, ex);
                    }
                }

            }

            
            try {
                //Map<String, String> pageInfo = null;
                //pageInfo = 
                        xmlrpc.getPage(server, space, page, CommandFactory.API_VERSION_2);
                Logger.getLogger(XmlRpcSaveStructure.class.getName()).log(Level.FINEST, "page exist");
                continue;
            } catch (XmlRpcException ex) {
                //pageInfo = null;
            }

            Map<String, String> newPageInfo = new HashMap<String, String>();
            //newPageInfo.putAll(parentInfo);
            newPageInfo.put("title", page);
            newPageInfo.put("space", space);
            newPageInfo.put("content", "");

            if (parent != null) {
                newPageInfo.put("parentId", parentInfo.get("id"));
            }
            try {
                Map<String, String> p = (Map<String, String>) xmlrpc.execute(server,
                        "storePage", Arrays.asList(new Object[]{newPageInfo}), CommandFactory.API_VERSION_2);
                
                
                
            } catch (XmlRpcException ex) {
                if (info != null) {
                        info.addError("could not store page " + server + "/" + space + "/" + page, ex);
                    }
            }
        }

    }
}
