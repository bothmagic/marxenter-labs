package de.mx.copcon.xmlrpc;

/**
 * Throw when a command not succeed.
 * 
 * @author marxma
 */
public class RpcCommandException extends Exception {

    public RpcCommandException() {
    }

    public RpcCommandException(String message) {
        super(message);
    }

    public RpcCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcCommandException(Throwable cause) {
        super(cause);
    }
    
}
