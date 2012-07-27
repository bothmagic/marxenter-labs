/*
 * Created on 24.02.2010
 *
 */
package org.jdesktop.swingx.demos;

import org.jdesktop.swingxset.SwingXSet;

public class CalendarDemoApp extends SwingXSet {

    
    /** 
     * @inherited <p>
     * Overridden to hide the selector.
     */
    @Override
    protected void startup() {
        super.startup();
        setSelectorVisible(false);
    }
    
    /** 
     * 
     * @inherited <p>
     * 
     * Overridden to disable font size setting always (looks bad anyway ;-)
     */
    @Override
    public boolean isWindowsOS() {
        return false;
    }



    public static void main(String[] args) {
        launch(CalendarDemoApp.class, args);
    }
}
