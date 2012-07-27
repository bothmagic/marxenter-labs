/*
 * DemoFrame.java
 *
 * Created on 20 de Maio de 2005, 23:58
 */

package org.jdesktop.jdnc.incubator.rlopes.closeabletabbedpane;
/*
import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;
import com.jgoodies.plaf.plastic.theme.SkyRed;
*/
import javax.swing.JOptionPane;
import javax.swing.UIManager;


/**
 *
 * @author  Ricardo Lopes
 */
public class DemoFrame extends javax.swing.JFrame {
    
    /**
     * Creates new form DemoFrame 
     */
    public DemoFrame() {
        initComponents();
        ((FlashCloseableTabbedPane) jTabbedPane1).addCloseableTabbedPaneListener(new CloseableTabbedPaneListener() {
            public boolean closeTab(int tabIndex) {
                if (JOptionPane.showConfirmDialog(DemoFrame.this, "Close Tab ?", "Question", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    System.err.println("Close tab index : " + tabIndex + " aborted...");
                    return false;
                }
                
                System.err.println("Closing tab index : " + tabIndex);
                return true;
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jTabbedPane1 = new FlashCloseableTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(450, 320));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(450, 320));
        jTabbedPane1.addTab("tab1", jPanel1);
        
        jTabbedPane1.addTab("tab2", jPanel2);
        
        jTabbedPane1.addTab("tab3", jPanel4);
        
        jTabbedPane1.addTab("tab4", jPanel5);
        
        jTabbedPane1.addTab("tab5", jPanel6);
        
        jTabbedPane1.addTab("tab6", jPanel7);
        
        jTabbedPane1.addTab("tab7", jPanel8);
        
        jTabbedPane1.addTab("tab8", jPanel9);
        
        jTabbedPane1.addTab("tab9", jPanel10);
        
        jTabbedPane1.addTab("tab10", jPanel11);
        
        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        
        jLabel1.setText("Tab Index :");
        jPanel3.add(jLabel1);
        
        jSpinner1.setValue(3);
        jPanel3.add(jSpinner1);
        
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Flash Tab");
        jPanel3.add(jCheckBox1);
        
        jButton1.setText("Do It");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                jButton1ActionPerformed(event);
            }
        });
        
        jPanel3.add(jButton1);
        
        getContentPane().add(jPanel3, java.awt.BorderLayout.NORTH);
        
        pack();
    }
// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent event) {//GEN-FIRST:event_jButton1ActionPerformed
        ((FlashCloseableTabbedPane) jTabbedPane1).setState((Integer)jSpinner1.getValue(), jCheckBox1.isSelected());
        if (jCheckBox1.isSelected()) {
            System.err.println("Flash tab index : " + (Integer)jSpinner1.getValue());
        } else {
            System.err.println("Stop Flashing tab index : " + (Integer)jSpinner1.getValue());
        }
    }//GEN-LAST:event_jButton1ActionPerformed
        
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
/*		
        try {
            PlasticXPLookAndFeel.setMyCurrentTheme(new SkyRed());
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }  
*/      
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DemoFrame().setVisible(true);
            }
        });
    }
    
// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTabbedPane jTabbedPane1;
// End of variables declaration//GEN-END:variables
    
}