/*
 * SimpleDemoPanel.java
 *
 * Created on April 16, 2005, 9:33 AM
 */

package org.jdesktop.demo;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import org.jdesktop.demo.DemoPanel;

/**
 * DemoPanel Template, containing a minimal set of methods necessary for inclusion
 * in the SwingX demos.
 */
public class SimpleDemoPanel extends DemoPanel {
    
    /**
     * Creates new form JXDatePickerDemoPanel 
     */
    public SimpleDemoPanel() {
        setName("Simple Demo");
        initComponents();
    }

    public String getHtmlDescription() {
        return "<html><b>Simple Demo Description</b><br/>" +
                "Describe the purpose of the demo here" +
                "</html>";
    }
    
    public String getInformationTitle() {
        return "Simple Demo Title Bar";
    }
    
    public String getName() {
        return "Simple Demo Name";
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        JLabel fooLabel = new JLabel("Simple Demo Content Area");
        setLayout(new BorderLayout());
        add(fooLabel, BorderLayout.CENTER);
    }
}
