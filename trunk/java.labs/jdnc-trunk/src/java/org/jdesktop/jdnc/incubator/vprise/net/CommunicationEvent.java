package org.jdesktop.jdnc.incubator.vprise.net;

import java.util.*;
import java.util.EventObject;

/**
 * The event sent to the communication listener containing the fields and
 * data sent from the 
 *
 * @author Shai Almog
 */
public class CommunicationEvent extends EventObject {
    /**
     * The data from the server if applicable
     */
    private Object transferObject;
    
    /**
     * The request to which this is a response
     */
    private long requestId;

    /**
     * The message that caused this event
     */
    private Object sourceMessage;
    
    /** Creates a new instance of CommunicationEvent */
    public CommunicationEvent(Object transferObject, Object sourceMessage, long requestId) {
        super(CommunicationManager.getInstance());
        this.transferObject = transferObject;
        this.sourceMessage = sourceMessage;
        this.requestId = requestId;
    }
    
    /**
     * The message that caused this event
     */
    public Object getSourceMessage() {
        return(sourceMessage);
    }

    /**
     * The data from the server if applicable
     */
    public Object getTransferObject() {
        return(transferObject);
    }

    /**
     * The request to which this is a response
     */
    public long getRequestId() {
        return(requestId);
    }
    
    /**
     * This method should be invoked with the users login/password when a login
     * required event is sent. This will allow the request to proceed.
     */
    public void login(String username, char[] password) {}
}
