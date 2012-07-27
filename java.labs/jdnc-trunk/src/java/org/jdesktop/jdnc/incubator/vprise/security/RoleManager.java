package org.jdesktop.jdnc.incubator.vprise.security;

import java.util.*;
import java.beans.*;
import java.awt.event.*;
import javax.swing.*;
import org.jdesktop.jdnc.incubator.vprise.net.*;

/**
 * This class moves the concepts behind role based security from J2EE into the
 * client side. Many user interface elements need to change according to the
 * permissions given for a particular user and this class allows us both to
 * query the server for the assigned roles and to modify some of the functionality
 * accordingly. This class is tightly bound with the networking code.
 *
 * @author Shai Almog
 */
public class RoleManager {
    /**
     * The roles for the current user
     */
    private String[] roles = null;

    /**
     * This object will be used to request the current user role from the server.
     */
    private Object requestRolesMessage;
    
    /**
     * The constructor accepts an object that allows us to request roles from the
     * server. These roles are returned in a response which we must convert to an
     * array of Strings to indicate the roles within the system.
     * This object will be sent through the communication manager and the response
     * returned is assumed to be a String array containing the roles for this user.
     * If this is not the case see the method parseResponse.
     */
    public RoleManager(Object requestRolesMessage) {
        this.requestRolesMessage = requestRolesMessage;
    }
    
    /**
     * This method analyzes a response from the server, it returns the set of
     * roles for a given user. If the response indicates that the user role has
     * changed 
     */
    protected String[] parseResponse(Object response) {
        return (String[]) response;
    }
    
    /**
     * Indicates whether the client side thinks the user is currently
     * logged in or not.
     */
    public boolean isLoggedIn() {
        return((roles != null) && (roles.length > 0));
    }

    /**
     * Loggs off the current user
     */ 
    public void logoff() {
        roles = null;
        fireRolesChanged(null);
    }

    /**
     * Listeners to role changed events
     */
    private List listeners = new ArrayList();
    
    public void addRoleListener(RoleListener listener) {
        listeners.add(listener);
    }
    
    public void removeRoleListener(RoleListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Invoked when the user logs in/out
     */
    protected void fireRolesChanged(RoleEvent ev) {
        if(listeners.size() > 0) {
            RoleListener[] arr = new RoleListener[listeners.size()];
            listeners.toArray(arr);
            for(int iter = 0 ; iter < arr.length ; iter++) {
                arr[iter].rolesChanged(ev);
            }
        }
    }
    
    /**
     * Returns true if the current user is within the given role this method
     * is the equivalent of the isUserInRoles method
     *
     * @see isUserInRoles
     * @param role The role required
     * @param listener A callback that will get invoked if the roles are not 
     *   known at this moment. The listener may be null.
     * @return If the roles are unknown false is returned and a callback will get 
     *   invoked when the status is known. If the roles are known then true
     *   if the role is within the array of roles.
     */
    public boolean isUserInRole(String role, RoleListener listener) {
        return(isUserInRoles(new String[] { role }, listener));
    }

    /**
     * Returns true if the current user is within the given role, this may change
     * since the method returns immediately while asynchroniously querying the server
     * so if a user is indeed within a role the listener will get called back.
     *
     * @param role The set of role required
     * @param listener A callback that will get invoked if the roles are not 
     *   known at this moment. The listener may be null.
     * @return If the roles are unknown false is returned and a callback will get 
     *   invoked when the status is known. If the roles are known then true
     *   if the role is within the array of roles.
     */
    public boolean isUserInRoles(String[] role, final RoleListener listener) {
        if(roles == null) {
            CommunicationManager.getInstance().request(requestRolesMessage, new CommunicationAdapter() {
                public void serverResponse(CommunicationEvent ev) {
                    roles = parseResponse(ev.getTransferObject());
                    Arrays.sort(roles);
                    RoleEvent event = new RoleEvent(RoleManager.this, roles);
                    if(listener != null) {
                        listener.rolesChanged(event);
                    }
                    fireRolesChanged(event);
                }
            });
            return(false);
        }

        Arrays.sort(roles);
        for(int iter = 0 ; iter < role.length ; iter++) {
            if(Arrays.binarySearch(roles, role[iter]) > -1) {
                return(true);
            }
        }
        return(false);
    }

    /**
     * This method creates an action that will only be enabled if the user
     * is within one of the given roles. The action is simply a wrapper
     * around the submitted action.
     */
    public Action createRoleAction(String[] roles, Action internalAction) {
        return(new RoleLimitedAction(roles, internalAction));
    }
    
    /**
     * This class is a wrapper around an action implementation that enables/
     * disables itself according to the permissions for the given role.
     */
    class RoleLimitedAction implements Action, RoleListener {
        /**
         * The set of roles that can invoke this action
         */
        private String[] roles;
        
        /**
         * The internal action
         */
        private Action internalAction;
        
        public RoleLimitedAction(String[] roles, Action internalAction) {
            this.roles = roles;
            this.internalAction = internalAction;
            addRoleListener(this);
            setEnabled(isUserInRoles(roles, null));
        }
        
        /**
         * Adds a PropertyChange listener.
         */
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            internalAction.addPropertyChangeListener(listener);
        }
         
        /**
         * Gets one of this object's properties using the associated key.
         */
        public Object getValue(String key) {
            return(internalAction.getValue(key));
        }
          
        /**
         * Returns the enabled state of the Action.
         */
        public boolean isEnabled() {
            return(internalAction.isEnabled());
        }
          
        /**
         * Sets one of this object's properties using the associated key.
         */
        public void putValue(String key, Object value) {
            internalAction.putValue(key, value);
        }
          
        /**
         * Removes a PropertyChange listener.
         */
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            internalAction.removePropertyChangeListener(listener);
        }

        /**
         * Sets the enabled state of the Action.
         */
        public void setEnabled(boolean b) {
            internalAction.setEnabled(b);
        }

        public void actionPerformed(ActionEvent ev) {
            internalAction.actionPerformed(ev);
        }

        /**
         * Invoked when the user logs out/logs in successfully
         */
        public void rolesChanged(RoleEvent ev) {
            setEnabled(isUserInRoles(roles, this));
        }
    }    
}
