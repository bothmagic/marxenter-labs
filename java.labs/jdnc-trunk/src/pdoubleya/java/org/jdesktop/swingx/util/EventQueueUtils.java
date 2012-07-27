/*
 * EventQueueUtils.java
 *
 * Created on August 19, 2005, 1:21 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.swingx.util;

import java.awt.EventQueue;

/**
 *
 * @author patrick
 */
public class EventQueueUtils {
    
   public static void invokeNowOrLater(Runnable runnable) {
      if ( EventQueue.isDispatchThread()) {
         runnable.run();
      } else {
         EventQueue.invokeLater(runnable);
      }
   }
    
}
