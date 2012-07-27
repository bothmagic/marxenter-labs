/*
 * FlashCloseableTabbedPane.java
 *
 * Created on 20 de Maio de 2005, 23:54
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.jdnc.incubator.rlopes.closeabletabbedpane;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.UIManager;


/**
 *
 * @author Ricardo Lopes
 */
public class FlashCloseableTabbedPane extends CloseableTabbedPane {
    
    public static final int FLASH_TAB_INTERVAL_MILIS = 500;
    public static final Color DEFAULT_FLASH_COLOR = Color.RED;
    
    private Color normalColor;
    private Color flashColor;
    private boolean stopFlashingOnSelect;
    private Map<Integer, FlashThread> threadsMap;
    
    /** Creates a new instance of FlashCloseableTabbedPane */
    public FlashCloseableTabbedPane() {
        super();
        normalColor = UIManager.getColor("TabbedPane.background");        
        flashColor = DEFAULT_FLASH_COLOR;
        stopFlashingOnSelect = true;
        threadsMap = new ConcurrentHashMap<Integer, FlashThread>();
    }

    public synchronized void setState(int index, boolean isFlashing) {
        if (isFlashing) {
            setBackgroundAt(index, flashColor);
            if (!threadsMap.containsKey(index)) {
                FlashThread flashThread = new FlashThread(index);
                threadsMap.put(index,  flashThread);
                flashThread.start();
            }
        } else {
            setBackgroundAt(index,  normalColor);
            if (threadsMap.containsKey(index)) {
                FlashThread flashThread = threadsMap.remove(index);
                flashThread.stopThread();
            }
        }
    }

    public void setStopFlashingOnSelect(boolean lStopFlashingOnSelect) {
        stopFlashingOnSelect = lStopFlashingOnSelect;
    }
    
    public boolean getStopFlashingOnSelect() {
        return stopFlashingOnSelect;
    }
    
    public void setFlashColor(Color lColor) {
        flashColor = lColor;
    }
    
    public Color getFlashColor() {
        return flashColor;
    }
        
    
    protected void tabRemoved(int lCloseTabIndex) {
        for (Integer tabIndex : threadsMap.keySet()) {
            if (tabIndex > lCloseTabIndex) {
                setState(tabIndex, false); // this will kill the thread
                setState(tabIndex-1,  true); // create a new one with the previous index
            } else if (tabIndex == lCloseTabIndex) {
                setState(lCloseTabIndex, false); // this will kill the thread
            }
        }
        
    }

    /**
     * Override this method to know when the tab has been removed, first the super method is called, then the threads are verified.
     * All other methods : remove(Component) , remove(int) , removeAll() call this method, so we only have to verride this one.
     */
    public synchronized void removeTabAt(int index) {
        super.removeTabAt(index);
        tabRemoved(index);
    }

    
    class FlashThread extends Thread {
        
        private int index;
        private boolean isStopped;
        private boolean isFlashing;
        
        public FlashThread(int lIndex) {
            index = lIndex;
            isStopped = false;
            isFlashing = false;
        }
        
        public void stopThread() {
            isStopped = true;
            setBackgroundAt(index, normalColor);
        }
        
        public void run() {
            while (!isStopped) {
                if ((stopFlashingOnSelect) && (index == getSelectedIndex())) {
                    setState(index, false);
                    return;
                }
                
                if (isFlashing) {
                    setBackgroundAt(index, normalColor);
                } else {
                    setBackgroundAt(index, flashColor);
                }
                isFlashing = !isFlashing;
                try {
                    sleep(FLASH_TAB_INTERVAL_MILIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

