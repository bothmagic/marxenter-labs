package org.jdesktop.jdnc.incubator.vprise.net;

import java.util.*;
import java.net.*;
import java.lang.reflect.InvocationTargetException;
import java.io.*;
import javax.swing.*;
import java.beans.*;
import java.awt.event.*;


/**
 * Concrete implementations of this class allow us to abstract the manner in 
 * which server communication occurs and streamline it. The communication manager
 * allows us to treat network communication code as asynchronious messages that
 * appear to the programmerr as simple Swing events that are recived on the Swing
 * thread.
 * The actual network communication is performed on a separate thread and this
 * class encapsulates that work and returns to Swing without user intervention.
 * The current implementation is based on a single thread rather than multiple
 * threads for networking. The reasoning is that a single message to the server
 * might affect the results of the next message, so serializing server messages
 * solves hairy issues such as that.
 * However, there is nothing within the communication manager class that prevents
 * it from parallel messaging and this will probably be added in the future by
 * flagging messages so that they indicate whether they are "read only" or 
 * not and thus write messages will cause serialization of the messages that
 * come after it.
 */
public abstract class CommunicationManager {    
    /**
     * This class works as a singleton. Right now the default is hardcoded
     * to http but this should move to a configuration file or client code.
     */
    private static CommunicationManager instance = new org.jdesktop.jdnc.incubator.vprise.net.http.HttpCommunicationManager();
    
    /**
     * The communication listeners
     */
    private List listeners = new ArrayList();
    
    /**
     * Unique id for every request sent from this client
     */
    private long currentRequestId;

    /**
     * The username for the currently logged in user.
     */
    private String loggedInUserName = null;
    
    /**
     * This is the queue for requests waiting to be sent to the server
     */
    private List queue = Collections.synchronizedList(new ArrayList());
    
    /**
     * The request thread instance that we need to notify
     */ 
    private Thread requestThread = new RequestThread();
    
    protected CommunicationManager() {
        // set a low priority to the thread so it won't hog the CPU from the
        // event thread.
        requestThread.setPriority(Thread.NORM_PRIORITY - 1);
        requestThread.start();
    }
    
    /**
     * Returns the single instance of this class
     */
    public static CommunicationManager getInstance() {
        return(instance);
    }
    
    /**
     * The username for the currently logged in user.
     */
    public String getLoggedInUser() {
        return(loggedInUserName);
    }

    
    /**
     * Implementations may define attributes for the communcation. The HTTP
     * implementation defines one attribute serverURL.
     * Any other key is simply ignored. The reason for using this method rather
     * than System.getProprty() is to avoid the need for security manager 
     * permissions and to allow the application not to depend on API's such
     * as webstart JNLP.
     */
    public void setCommunicationAttribute(String key, String value) {
    }
    
    /**
     * This method performs the given server request and returns the 
     * data received to the appropriate listener. A global event will
     * be fired as well to allow generic error handling to function.
     * This method returns immediately.
     */
    public long request(Object data, CommunicationListener listener) {
        long id = 0;
        synchronized(this) {
            currentRequestId++;
            id = currentRequestId;
        }
        queue.add(0, new Request(id, data, listener));
        synchronized(requestThread) {
            requestThread.notify();
        }
        return(currentRequestId);
    }
    
    /**
     * Adds a listener that receives all of the communication events
     */
    public void addCommuncationListener(CommunicationListener listener) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Removes a listener that receives all of the communication events
     */
    public void removeCommuncationListener(CommunicationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Invoked when the server responds to a specific user query. 
     */
    protected void fireServerResponse(CommunicationEvent ev) {
        if(listeners.size() > 0) {
            CommunicationListener[] arr = new CommunicationListener[listeners.size()];
            listeners.toArray(arr);
            for(int iter = 0 ; iter < arr.length ; iter++) {
                arr[iter].serverResponse(ev);
            }
        }
    }
    
    /**
     * Invoked when the server pushes data to the client
     */
    protected void fireServerPush(CommunicationEvent ev) {
        if(listeners.size() > 0) {
            CommunicationListener[] arr = new CommunicationListener[listeners.size()];
            listeners.toArray(arr);
            for(int iter = 0 ; iter < arr.length ; iter++) {
                arr[iter].serverPush(ev);
            }
        }
    }

    /**
     * Invoked in case of a communication problem with the server
     */
    protected void fireCommunicationError(CommunicationEvent ev) {
        if(listeners.size() > 0) {
            CommunicationListener[] arr = new CommunicationListener[listeners.size()];
            listeners.toArray(arr);
            for(int iter = 0 ; iter < arr.length ; iter++) {
                arr[iter].communicationError(ev);
            }
        }
    }
    
    /**
     * This method performs the synchronious request.and returns a response from the server
     */
    protected abstract Object performRequest(Object request) throws CommunicationException;
    
    /**
     * Represents a request to send to the server
     */
    class Request implements Runnable {
        /**
         * This is the event that should be fired at the user
         */
        private CommunicationEvent event;

        /**
         * The data we are requesting from the server
         */
        private Object data;

        /**
         * The listener that will receive the event
         */
        private CommunicationListener listener;
                        
        /**
         * Unique id for this request
         */
        private long requestId;
        
        Request(long requestId, Object data, CommunicationListener listener) {
            this.data = data;
            this.listener = listener;
            this.requestId = requestId;
        }

        /**
         * The data we are requesting from the server
         */
        public Object getData() {
            return(data);
        }
                
        /**
         * Unique id for this request
         */
        public long getRequestId() {
            return(requestId);
        }
        
        /**
         * The listener that will receive the event
         */
        public CommunicationListener getListener() {
            return(listener);
        }

        public void fireEvent(CommunicationEvent event) {
            this.event = event;
            SwingUtilities.invokeLater(this);
        }
        
        /**
         * This method will be invoked within the swing thread
         */
        public void run() {
            Object transfer = event.getTransferObject();
            // if a transfer object is null or an exception then it indicates an error
            if((transfer != null) && (!(transfer instanceof Throwable))) {
                listener.serverResponse(event);
                fireServerResponse(event);
            } else {
                listener.communicationError(event);
                fireCommunicationError(event);
            }
        }
    }

    /**
     * Returns a communication event based on the content of this message
     */
    private CommunicationEvent createEvent(Object response, Object sourceMessage, long id) {
        return( new CommunicationEvent(response, sourceMessage, id));
    }
    
    /**
     * Performs the request in a separate thread and knows how to return
     * the response to the Swing thread
     */
    class RequestThread extends Thread {
        RequestThread() {
        }
        
        public void run() {
            while(requestThread != null) {
                while(queue.isEmpty()) {
                    try {
                        synchronized(this) {
                            wait();
                        }
                    } catch(InterruptedException err) { err.printStackTrace(); }
                }
                Request request = (Request)queue.remove(queue.size() - 1);
                Object response = null;
                CommunicationEvent event;
                try {
                    response = performRequest(request.getData());
                    if(response == null) {
                        continue;
                    }
                    event = createEvent(response, request.getData(), request.getRequestId());
                } catch(CommunicationException err) {
                    event = new CommunicationEvent(err, request.getData(), request.getRequestId());
                }

                request.fireEvent(event);
            }
        }
    }
    
    /**
     * Loggs off the current user
     */ 
    public void logoff() {
        loggedInUserName = null;
    }
    
    /**
     * Tries to login to the server
     */
    protected abstract void login(String username, char[] password) throws CommunicationException;

    /**
     * This method is invoked when it is clear that the user password isn't
     * supplied. It tries to login and reinvokes performRequest()
     */
    protected PasswordAuthentication promptUserPassword() throws CommunicationException {
        return(promptUserPassword(null, null));
    }
    
    /**
     * This method is invoked when it is clear that the user password isn't
     * supplied. It tries to login and reinvokes performRequest()
     */
    protected PasswordAuthentication promptUserPassword(String errorCode, Object[] param) throws CommunicationException {
        PromptPasswordEvent event = new PromptPasswordEvent();
        try {
            SwingUtilities.invokeLater(event);
            synchronized(this) {
                wait();
            }
        } catch(InterruptedException interrupted) {
        } 
        login(event.getUsername(), event.getPassword());
        loggedInUserName = event.getUsername();
        return(new PasswordAuthentication(event.getUsername(), event.getPassword()));
    }

    
    /**
     * This class provides special support for the communication event
     * mechanizem
     */
    class PromptPasswordEvent extends CommunicationEvent implements Runnable {
        /**
         * The username entered withing the login method
         */
        private String username;

        /**
         * The password entered withing the login method
         */
        private char[] password;
        
        public PromptPasswordEvent() {
            super(null, null, -1);
        }

        /**
         * The username entered withing the login method
         */
        public String getUsername() {
            return(username);
        }

        /**
         * The password entered withing the login method
         */
        public char[] getPassword() {
            return(password);
        }

        /**
         * This method should be invoked with the users login/password when a login
         * required event is sent. This will allow the request to proceed.
         */
        public void login(String username, char[] password) {
            this.username = username;
            this.password = password;
            synchronized(CommunicationManager.this) {
                CommunicationManager.this.notify();
            }
        }

        /**
         * This event knows how to send itself to all the listeners. This method
         * is invoked on the swing thread.
         */
        public void run() {
            if(listeners.size() > 0) {
                CommunicationListener[] arr = new CommunicationListener[listeners.size()];
                listeners.toArray(arr);
                for(int iter = 0 ; iter < arr.length ; iter++) {
                    arr[iter].loginRequired(this);
                }
            }
        }        

        public String toString() {
            // in case these objects are null:
            String usernameStr = "" + username;
            String passwordStr = "" + password;
            return(super.toString() + "." + usernameStr + 
                "." + passwordStr);
        }
    }
    
}
