/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.xmlrpc.commands;

import de.mx.copcon.InformationCallback;
import de.mx.copcon.Command;
import de.mx.copcon.CommandFactory;
import de.mx.copcon.XmlRpcException;
import de.mx.copcon.xmlrpc.RpcCommandException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marxma
 */
public class XmlRpcSetPermittion implements Command {

    private String spacekey;
    private String id;
    private List<String> permittions;
    private String server;
    public XmlRpcSetPermittion(String server, String spacekey, String id, List<String> permittions) {
        this.spacekey = spacekey;
        this.id = id;
        this.permittions = permittions;
        this.server = server;
    }
    
    
    @Override
    public void run(InformationCallback info) throws RpcCommandException {
        
        CommandFactory xmlrpc = CommandFactory.INSTANCE;
        try {
            xmlrpc.execute(server, "addPermissionsToSpace", Arrays.asList(
                    new Object[]{permittions, id, spacekey}), CommandFactory.API_VERSION_2);
        } catch (XmlRpcException ex) {
            info.addError("could not set permittions.", ex);
        }
        
    }
    
}
