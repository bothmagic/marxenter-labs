/*
 * SampleDownloadStatusBean.java
 *
 * Created on August 19, 2005, 8:04 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.swingx.statusbar.sample;

import java.awt.Dimension;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.jdesktop.swingx.statusbar.StatusBean;
import org.jdesktop.swingx.util.WindowUtils;

/**
 *
 * @author patrick
 */
public class SampleDownloadStatusBean implements StatusBean {
    private JButton button;
    private String name;
    
    /**
     * Creates a new instance of SampleDownloadStatusBean 
     */
    public SampleDownloadStatusBean() {
        name = "DownloadUpdates";
        button = new JButton();
        
        
        try {
            URL url = this.getClass().getResource("/toolbarButtonGraphics/navigation/Down16.gif");
            ImageIcon icon = new ImageIcon(url);
            this.button.setPreferredSize(new Dimension(19, 19));
            System.out.println("  size: " + this.button.getPreferredSize() + "icon " + icon);
            this.button.setAction(new AbstractAction("", icon) {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JOptionPane.showConfirmDialog(
                            WindowUtils.findJFrame(button),
                            "New updates are available; would you like to download them now?",
                            "Downloads Are Now Available",
                            JOptionPane.YES_NO_OPTION
                            );
                }
                
            });
        } catch (Exception e) {
            System.out.println("Could not load icon for sample download bean");
            e.printStackTrace();
        }
        this.button.setToolTipText("An important update is available");
    }
    
    /**
     * Returns the display component for this bean.
     */
    public JComponent getDisplayComponent() {
        return button;
    }
    
    /**
     * Returns the bean's descriptive name.
     */
    public String getName() {
        return name;
    }
}
