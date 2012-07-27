package org.jdesktop.jdnc.incubator.vprise.auth;

/**
 * A set of empty implementations of the LoginListener interface
 *
 * @author Shai Almog
 */
public class LoginAdapter implements LoginListener {
    /**
     *  Called by the <strong>JXLoginPanel</strong> in the event of a
     *  successful login.
     * 
     * @param source panel that fired the event
     */
    public void loginSucceeded(LoginEvent source) {
    }

    /**
     *  Called by the <strong>JXLoginPanel</strong> when the Authentication
     *  operation is started.
     * @param source panel that fired the event
     */
    public void loginStarted(LoginEvent source) {
    }

    /**
     *  Called by the <strong>JXLoginPanel</strong> in the event of a login failure
     * 
     * @param source panel that fired the event
     */
    public void loginFailed(LoginEvent source) {
    }

    /**
     *  Called by the <strong>JXLoginPanel</strong> in the event of a login
     *  cancellation by the user.
     * 
     * @param source panel that fired the event
     */
    public void loginCanceled(LoginEvent source) {
    }    
}
