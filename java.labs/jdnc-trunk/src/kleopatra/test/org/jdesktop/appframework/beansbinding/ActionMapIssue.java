/*
 * Created on 16.10.2009
 *
 */
package org.jdesktop.appframework.beansbinding;

import javax.swing.Action;
import javax.swing.ActionMap;

import org.jdesktop.application.Application;

/**
 * Demonstrates that the ActionManager looses Action state
 * after garbage collection.
 *
 * Issue #82-jsr296:
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.4 $
 */
public final class ActionMapIssue extends Application {


    Action action;
    
    public static void main(String[] args) {
        Application.launch(ActionMapIssue.class, args);
    }
    
    
    @Override
    protected void startup() {
        ActionProvider target = new ActionProvider();
        
        // Print the initial action name and enablement
        printAction(target);

        // Change the action name and enablement and print it again
        getContext().getActionMap(target).get("pause").putValue(Action.NAME, "continue");
        getContext().getActionMap(target).get("pause").setEnabled(false);
        action = getContext().getActionMap(target).get("pause");
        printAction(target);
        
        // Collect garbage and print the action again
        System.gc();
        printAction(target);
//        printAction(action);
    }
    
    
    private void printAction(Object actionTarget) {
        ActionMap map = getContext().getActionMap(actionTarget);
        Action action = map.get("pause");
        printAction(action);
    }


    /**
     * @param action
     */
    private void printAction(Action action) {
        String name = (String) action.getValue(Action.NAME);
        System.out.println();
        System.out.println("pause.Action.name=" + name);
        System.out.println("pause.Action.enabled=" + action.isEnabled());
    }
    
    
    // Add the following to the resources:
    // pause.Action.name=Pause
    private final static class ActionProvider {

        @org.jdesktop.application.Action
        public void pause() {
            // Do nothing.
        }

    }


}
