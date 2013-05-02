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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.modelmbean.XMLParseException;

/**
 *
 * @author marxma
 */
public class XmlRpcSaveSpace implements Command {

    private String spaceKey;
    private String spaceName;
    private String spaceDescription;
    private String server;
    private Map<String, String> space;
    public XmlRpcSaveSpace(String server, String spaceKey, String spaceName, String spaceDescription) {
        this.spaceKey = spaceKey;
        this.spaceName = spaceName;
        this.spaceDescription = spaceDescription;
        this.server = server;
    }

    public Map<String, String> getSpace() {
        return space;
    }
    
    @Override
    public void run(InformationCallback info) throws RpcCommandException {
        space = new HashMap<String, String>();
        space.put("key", spaceKey);
        space.put("name", spaceName);
        space.put("description", spaceDescription);
        
        CommandFactory xmlrpc = CommandFactory.INSTANCE;
        try {
            space = (Map<String, String>)xmlrpc.execute(server, "addSpace", Arrays.asList(new Object[] {space}), CommandFactory.API_VERSION_1);
            Map<String, String> pageMap = xmlrpc.getPage(server, space.get("homePage"), CommandFactory.API_VERSION_2);
            pageMap.put("title", "Home");
            xmlrpc.execute(server, "storePage", Arrays.asList(new Object[] {pageMap}), CommandFactory.API_VERSION_2);
            
        } catch (XmlRpcException ex) {
            info.addError(
                    String.format("Could not create space %s : %s", spaceKey, spaceName), ex);
        }
    }
    
}
