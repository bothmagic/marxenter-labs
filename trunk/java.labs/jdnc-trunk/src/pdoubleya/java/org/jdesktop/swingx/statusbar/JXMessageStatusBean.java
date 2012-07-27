/*
 * JXMessageStatusBean.java
 *
 * Created on August 19, 2005, 1:19 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.swingx.statusbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.jdesktop.swingx.util.EventQueueUtils;

/**
 *
 * @author patrick
 */
public class JXMessageStatusBean extends JXLabelStatusBean {
    private StatusMessage statusMessageMonitor;
    private static final int DEFAULT_DELAY = 3000;
    
    /**
     * Creates a new instance of JXMessageStatusBean 
     */
    public JXMessageStatusBean() {
        this("Ready");        
    }
    
    public JXMessageStatusBean(String restingMessage) {
        this(restingMessage, DEFAULT_DELAY);
    }

    public JXMessageStatusBean(String restingMessage, int delayBeforeReset) {
        this.statusMessageMonitor = new StatusMessage(restingMessage, delayBeforeReset);      
        setStatus(restingMessage);
    }

    // run on EDT thread
    public void setStatus(final String text) {
        statusMessageMonitor.newMessage(text);
    }
    
    // run on EDT thread
    public void clearStatus() {
        statusMessageMonitor.reset();
    }
    
    class StatusMessage implements ActionListener {
        Timer statusMessageTimer;
        String defaultMessage;
        long lastChangedAt;
        long messageLife;
        
        StatusMessage(String defaultMessage, int delayBeforeReset) {
            this.statusMessageTimer = new Timer(100, this);
            this.defaultMessage = defaultMessage;
            this.messageLife = delayBeforeReset;
        }
        
        void stopTimer() {
            if ( statusMessageTimer.isRunning() ) statusMessageTimer.stop();
        }
        
        void newMessage(final String text) {
            lastChangedAt = System.currentTimeMillis();
            EventQueueUtils.invokeNowOrLater(new Runnable() {
                public void run() {
                    getLabel().setText(text);
                }
            });
            if ( ! statusMessageTimer.isRunning()) {
                statusMessageTimer.restart();
            }
        }
        
        void reset() {
            newMessage(defaultMessage);
            statusMessageTimer.stop();
        }
        
        public void actionPerformed(ActionEvent evt) {
            if ( System.currentTimeMillis() - lastChangedAt >= messageLife ) {
                newMessage(defaultMessage);
            }
        }
    }
}
