package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.LayoutManager2;

/**
 * Currently just a placehoder for a frame to contain a JXStatusBeanBar. Will probably be moved to
 * JXFrame or JXRootPane.
 * 
 * @author Patrick Wright
 */
public class JXStatusFrame extends JXFrame {
    private Object lock = new Object();    
    private JXStatusBeanBar statusBar;
    
    /** Creates a new instance of JXStatusFrame */
    public JXStatusFrame() {
    }
    
    public JXStatusFrame(String title) {
        super(title);
    }    
    
    public void installStatusBar(JXStatusBeanBar bar) {
        synchronized(lock) {
            LayoutManager2 layout = (LayoutManager2)getLayout();
            if ( ! ( layout instanceof BorderLayout )) {
                setLayout(new BorderLayout());
            }
            add(bar, BorderLayout.SOUTH);
        }
    }
    
}
