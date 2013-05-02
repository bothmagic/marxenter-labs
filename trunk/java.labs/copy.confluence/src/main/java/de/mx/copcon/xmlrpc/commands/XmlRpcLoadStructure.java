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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marxma
 */
public class XmlRpcLoadStructure implements Command {

    private String server;
    private String space;
    private String page;
    
    private List<String> pageStructure = new ArrayList<String>();

    /**
     * 
     * @param server
     * @param space
     * @param page 
     */
    public XmlRpcLoadStructure(String server, String space, String page) {
        this.server = server;
        this.space = space;
        this.page = page;
    }

    public List<String> getPageStructure() {
        return pageStructure;
    }
    
    
    @Override
    public void run(InformationCallback info) {
        try {
            CommandFactory xmlrpc = CommandFactory.INSTANCE;
            
            Map<String, String> pageInfo = xmlrpc.getPage(server, space, page, CommandFactory.API_VERSION_1);
            
            Object[] childs = (Object[]) xmlrpc.execute(server, "getChildren", 
                    Arrays.asList(new Object[] {pageInfo.get("id")}), CommandFactory.API_VERSION_1);
            
            loadStructure(xmlrpc, "", childs);
           
        } catch (XmlRpcException ex) {
            
            if (info != null) {
                info.addError("Could not load page "+page, ex);
            }
            
        }
        
    }

    private void loadStructure(CommandFactory xmlrpc, String parent, Object[] childs) throws XmlRpcException {
        
        for (Object child: childs) {
            
            Map<String, String> pageInfo = (Map<String, String>)child;
            String path = parent + "//" + pageInfo.get("title");
            pageStructure.add(path);
            
            loadStructure(xmlrpc, path, (Object[]) xmlrpc.execute(server, "getChildren", 
                    Arrays.asList(new Object[] {pageInfo.get("id")}), CommandFactory.API_VERSION_1));
            
        }
        
        
    }
    
    
    
    
    
}
