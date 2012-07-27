package org.jdesktop.jdnc.incubator.vprise.net;

/**
 * Helper class implementing the listener
 *
 * @author Shai Almog
 */
public class CommunicationAdapter implements CommunicationListener {
    
    public void communicationError(CommunicationEvent ev) {
    }
    
    public void serverPush(CommunicationEvent ev) {
    }
    
    public void serverResponse(CommunicationEvent ev) {
    }
    
    public void loginRequired(CommunicationEvent ev) {
    }
    
    /**
     * Invoked when the user logs out/logs in successfully
     */
    public void rolesChanged(CommunicationEvent ev) {}
}
