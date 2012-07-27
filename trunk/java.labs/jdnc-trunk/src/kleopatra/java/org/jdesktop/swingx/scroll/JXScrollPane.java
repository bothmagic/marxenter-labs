/*
 * Created on 02.07.2007
 *
 */
package org.jdesktop.swingx.scroll;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * A scrollPane which uses the JXViewport.
 */
public class JXScrollPane extends JScrollPane {

    public JXScrollPane() {
        super();
        init();
    }
    
    public JXScrollPane(JComponent comp) {
        super(comp);
        init();
        
    }

    private void init() {
    }

    @Override
    protected JViewport createViewport() {
        return new JXViewport();
    }

    
}
