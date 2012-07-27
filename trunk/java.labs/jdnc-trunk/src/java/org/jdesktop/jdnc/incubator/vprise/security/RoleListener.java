package org.jdesktop.jdnc.incubator.vprise.security;

/**
 * This listener allows us to recive events whenever the user role changes
 *
 * @author Shai Almog
 */
public interface RoleListener {    
    /**
     * Invoked when the user logs out/logs in successfully
     */
    public void rolesChanged(RoleEvent ev);
}
