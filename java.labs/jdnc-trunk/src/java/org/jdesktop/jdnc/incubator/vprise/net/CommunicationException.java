package org.jdesktop.jdnc.incubator.vprise.net;

/**
 * This exception is thrown to indicate a communcation error
 *
 * @author Shai Almog
 */
public class CommunicationException extends Exception {
    
    private String serverErrorCode;
    
    /** Creates a new instance of CommunicationException */
    public CommunicationException(String serverErrorCode) {
        this.serverErrorCode = serverErrorCode;
    }
    
    public String getServerErrorCode() {
        return(serverErrorCode);
    }
}
