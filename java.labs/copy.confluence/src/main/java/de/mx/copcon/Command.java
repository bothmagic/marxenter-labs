/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon;

import de.mx.copcon.xmlrpc.RpcCommandException;

/**
 *
 * @author marxma
 */
public interface Command {
    
    
    void run(InformationCallback info) throws RpcCommandException;
    
}
