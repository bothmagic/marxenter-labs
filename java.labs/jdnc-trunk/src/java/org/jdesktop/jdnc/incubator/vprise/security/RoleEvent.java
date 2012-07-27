package org.jdesktop.jdnc.incubator.vprise.security;

import java.util.*;
import java.util.EventObject;

/**
 * The event sent to the role listener indicating that the roles have changed
 *
 * @author Shai Almog
 */
public class RoleEvent extends EventObject {    
    private String[] roles;
    
    /** Creates a new instance of CommunicationEvent */
    public RoleEvent(Object source, String[] roles) {
        super(source);
    }
    
    public String[] getRoles() {
        return roles;
    }
}
