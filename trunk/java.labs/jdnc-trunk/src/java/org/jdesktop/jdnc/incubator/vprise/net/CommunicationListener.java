package org.jdesktop.jdnc.incubator.vprise.net;

/**
 * This interface can be implemented by anyone interested in reciving server
 * communication events. Events are guaranteed to be on the Swing event
 * thread and thus are threadsafe.
 *
 * @author Shai Almog
 */
public interface CommunicationListener {
    /**
     * Invoked when the server responds to a specific user query. 
     */
    public void serverResponse(CommunicationEvent ev);
    
    /**
     * Invoked when the server requires the user to login in order to proceed
     */
    public void loginRequired(CommunicationEvent ev);

    /**
     * Invoked when the server pushes data to the client
     */
    public void serverPush(CommunicationEvent ev);

    /**
     * Invoked in case of a communication problem with the server
     */
    public void communicationError(CommunicationEvent ev);
}
