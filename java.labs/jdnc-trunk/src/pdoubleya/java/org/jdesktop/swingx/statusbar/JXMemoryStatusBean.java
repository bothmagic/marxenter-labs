package org.jdesktop.swingx.statusbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import org.jdesktop.swingx.util.WindowUtils;


/**
 * A status bar bean that displays memory used in a progress bar--e.g. visually. Use of this is almost
 * completely automatic, except you can control how often the memory stats are updated. Double-clicking on
 * the bean makes a call to the garbage collector, and displays a dialog showing the current memory usage.
 * @author Patrick Wright
 */
public class JXMemoryStatusBean implements StatusBean {
    /**
     * For synchronizing blocks.
     */
    public Object lock = new Object();
    /**
     * The progress bar used to display memory used.
     */
    private JProgressBar memoryBar;
    /**
     * Time, in milliseconds, between updates to the memory usage. Defaults to 5 seconds.
     */
    private int updateDelay;
    /**
     * Our Timer that calls back to update the memory usage.
     */
    private Timer memoryTimer;
    /**
     * The ActionListener called by the timer for updates.
     */
    private MemoryTimerListener memoryTimerListener;
    /**
     * Descriptive name of this bean.
     */
    private String name;
    /**
     * NumberFormat string to display memory usage in tooltips and dialog.
     */
    private NumberFormat numberFormat;
    
    
    /**
     * Creates a new memory usage bean with an update every 5 seconds.
     */
    public JXMemoryStatusBean() {
        this(5 * 1000);
    }
    
    /**
     * Creates a new memory usage bean with an update the specified interval.
     * @param updateDelay Time, in milliseconds, between updates to the memory usage.
     */
    public JXMemoryStatusBean(int updateDelay) {
        this.name = "MemoryBar";
        this.updateDelay = updateDelay;
        this.numberFormat = new DecimalFormat("#,###K");
        this.memoryTimerListener= new MemoryTimerListener();
        this.memoryBar = new JProgressBar();
        
        // TODO: need to set the preferred size based on the date format (which affects length)
        this.memoryBar.setPreferredSize(new Dimension(75, 19));
        
        initMemoryBar();
    }
    
    /**
     * Returns the display component for this bean.
     */
    public JComponent getDisplayComponent() {
        return memoryBar;
    }
    
    /**
     * Returns the bean's descriptive name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Starts the updates.
     */
    private void startClock() {
        synchronized (lock) {
            if ( memoryTimer == null ) {
                memoryTimer = new Timer(updateDelay, memoryTimerListener);
                memoryTimerListener.actionPerformed(null);
            }
            if ( memoryTimer != null && ! memoryTimer.isRunning()) {
                memoryTimer.start();
            }
        }
    }
    
    /*
     * Stops the live updates
     */
    private void stopClock() {
        synchronized (lock) {
            if ( memoryTimer != null && memoryTimer.isRunning()) {
                memoryTimer.stop();
                memoryTimer = null;
            }
        }
    }
    
    private void initMemoryBar() {
        memoryBar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if ( e.getClickCount() > 1 ) {
                    int used = memoryTimerListener.usedMemory();
                    System.gc();
                    int saved = used - memoryTimerListener.usedMemory();
                    JOptionPane.showMessageDialog(
                            WindowUtils.findJFrame(memoryBar),
                            "Freed " + numberFormat.format(saved) +
                            "; total in use: " + numberFormat.format(memoryTimerListener.usedMemory()) +
                            ", "+ numberFormat.format(memoryTimerListener.totalMemory()) + " available.",
                            "Memory Released",
                            JOptionPane.INFORMATION_MESSAGE
                            );
                }
            }
            // HACK: trying to avoid setting the tooltip text on every memory update...
            public void mouseEntered(java.awt.event.MouseEvent e) {
                super.mouseEntered(e);
                memoryBar.setToolTipText(memoryTimerListener.getToolTipText());
            }
            
        });
        memoryTimerListener.init();
        startClock();
    }
    
    // used to receive pings from the timer, to update the current clock
    // all units are converted from bytes to K (used, free, total)
    private class MemoryTimerListener implements ActionListener {
        private final Runtime runtime = Runtime.getRuntime();
        private int totalMemory;
        private long lastCallAt;
        
        
        MemoryTimerListener() {}
        
        public void init() {
            memoryBar.setMinimum(0);
            totalMemory = (int)(runtime.totalMemory() / 1024);
            memoryBar.setMaximum(totalMemory());
            lastCallAt = System.currentTimeMillis();
        }
        
        public int usedMemory() {
            return totalMemory - freeMemory();
        }
        
        public int totalMemory() {
            return (int)(runtime.totalMemory() / 1024);
        }
        
        public int freeMemory() {
            return (int)(runtime.freeMemory() / 1024);
        }
        
        // do this here to avoid creating a string on every ping
        public String getToolTipText() {
            // TODO: could probably store this and only update every X seconds...
            return "Using " + numberFormat.format(usedMemory()) +
                    "/" + numberFormat.format(totalMemory()) +
                    "; " + numberFormat.format(freeMemory()) + " free";
        }
        
        public void actionPerformed(ActionEvent e) {
            int used = usedMemory();
            long now = System.currentTimeMillis();
            if ( now - lastCallAt > 60000 ) {
                totalMemory = totalMemory();
                memoryBar.setMaximum(totalMemory);
            }
            
            memoryBar.setValue(used);
            lastCallAt = now;
        }
    }
}
