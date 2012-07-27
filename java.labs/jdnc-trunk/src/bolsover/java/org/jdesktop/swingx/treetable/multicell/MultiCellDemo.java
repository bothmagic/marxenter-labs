/*
 * $Id: MultiCellDemo.java 989 2006-12-28 11:56:54Z bolsover $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

/*
 * $Log$
 * Revision 1.1  2006/12/28 11:56:53  bolsover
 * *** empty log message ***
 *
 * @author David Bolsover
 *
 */

package org.jdesktop.swingx.treetable.multicell;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jdesktop.swingx.treetable.TreeTableModel;


public class MultiCellDemo extends javax.swing.JFrame {
    private static  MultiCellDemo INSTANCE =  new  MultiCellDemo();
    
    /**
     * Creates new form MultiCellDemo
     */
    public MultiCellDemo() {
        initComponents();
        jXTreeTable1.setTreeTableModel(constructDemoModel());
        jXTreeTable1.setRootVisible(true);
        jXTreeTable1.setShowsRootHandles(true);
        // renderer for the table (col2)
        MultiCellTableCellRenderer renderer = new   MultiCellTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.RIGHT);
        jXTreeTable1.getColumn(2).setCellRenderer(renderer);
        // renderer for col1 - just want to center the text
        DefaultTableCellRenderer col1Renderer = new DefaultTableCellRenderer();
        jXTreeTable1.getColumn(1).setCellRenderer(col1Renderer);
        col1Renderer.setHorizontalAlignment(JLabel.CENTER);
        // fix the row height - ? is there a better ? dynamic way?
        jXTreeTable1.setRowHeight(90);
        
        centreFrame();
    }
    
    public static MultiCellDemo getInstance(){
        return INSTANCE;
    }
    
    /**
     * Crate and return some test data
     * This data would normally be generated on the fly following calls to a database.
     */
    private TreeTableModel constructDemoModel(){
        // a demo model
        MultiCellData mcd = new MultiCellData();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultMutableTreeNode child;
        DefaultMutableTreeNode child1;
        // root userObject
        mcd.setShowColHeader(true);
        mcd.setPartId("000-000");
        mcd.setDescription("Spitfire");
        mcd.setQtyPer(1);
        mcd.setSafetyStockQty(10);
        mcd.setQtyOnHand(12);
        mcd.setQtyOnSupplyPeriod1(14);
        mcd.setQtyOnDemandPeriod1(7);
        mcd.setQtyEndPeriod1(22);
        mcd.setQtyOnSupplyPeriod2(22);
        mcd.setQtyOnDemandPeriod2(22);
        mcd.setQtyEndPeriod2(22);
        mcd.setQtyOnSupplyPeriod3(22);
        mcd.setQtyOnDemandPeriod3(22);
        mcd.setQtyEndPeriod3(22);
        mcd.setClosingBalance(22);
        root.setUserObject(mcd);
        // various chidren
        child = new DefaultMutableTreeNode();
        mcd = new MultiCellData();
        mcd.setPartId("000-001");
        mcd.setDescription("Right Wing");
        mcd.setQtyPer(1);
        mcd.setSafetyStockQty(10);
        mcd.setQtyOnHand(12);
        mcd.setQtyOnSupplyPeriod1(14);
        mcd.setQtyOnDemandPeriod1(7);
        mcd.setQtyEndPeriod1(22);
        mcd.setQtyOnSupplyPeriod2(22);
        mcd.setQtyOnDemandPeriod2(22);
        mcd.setQtyEndPeriod2(22);
        mcd.setQtyOnSupplyPeriod3(22);
        mcd.setQtyOnDemandPeriod3(22);
        mcd.setQtyEndPeriod3(22);
        mcd.setClosingBalance(22);
        child.setUserObject(mcd);
        root.add(child);
        child = new DefaultMutableTreeNode();
        mcd = new MultiCellData();
        mcd.setPartId("000-002");
        mcd.setDescription("Left Wing");
        mcd.setQtyPer(1);
        mcd.setSafetyStockQty(10);
        mcd.setQtyOnHand(12);
        mcd.setQtyOnSupplyPeriod1(14);
        mcd.setQtyOnDemandPeriod1(7);
        mcd.setQtyEndPeriod1(22);
        mcd.setQtyOnSupplyPeriod2(22);
        mcd.setQtyOnDemandPeriod2(22);
        mcd.setQtyEndPeriod2(22);
        mcd.setQtyOnSupplyPeriod3(22);
        mcd.setQtyOnDemandPeriod3(22);
        mcd.setQtyEndPeriod3(22);
        mcd.setClosingBalance(22);
        child.setUserObject(mcd);
        root.add(child);
        child = new DefaultMutableTreeNode();
        mcd = new MultiCellData();
        mcd.setPartId("000-100");
        mcd.setDescription("Engine");
        mcd.setQtyPer(1);
        mcd.setSafetyStockQty(10);
        mcd.setQtyOnHand(12);
        mcd.setQtyOnSupplyPeriod1(14);
        mcd.setQtyOnDemandPeriod1(7);
        mcd.setQtyEndPeriod1(22);
        mcd.setQtyOnSupplyPeriod2(22);
        mcd.setQtyOnDemandPeriod2(22);
        mcd.setQtyEndPeriod2(22);
        mcd.setQtyOnSupplyPeriod3(22);
        mcd.setQtyOnDemandPeriod3(22);
        mcd.setQtyEndPeriod3(22);
        mcd.setClosingBalance(22);
        child.setUserObject(mcd);
        root.add(child);
        child1 = new DefaultMutableTreeNode();
        mcd = new MultiCellData();
        mcd.setPartId("000-101");
        mcd.setDescription("Piston");
        mcd.setQtyPer(12);
        mcd.setSafetyStockQty(10);
        mcd.setQtyOnHand(12);
        mcd.setQtyOnSupplyPeriod1(14);
        mcd.setQtyOnDemandPeriod1(7);
        mcd.setQtyEndPeriod1(22);
        mcd.setQtyOnSupplyPeriod2(22);
        mcd.setQtyOnDemandPeriod2(22);
        mcd.setQtyEndPeriod2(22);
        mcd.setQtyOnSupplyPeriod3(22);
        mcd.setQtyOnDemandPeriod3(22);
        mcd.setQtyEndPeriod3(22);
        mcd.setClosingBalance(22);
        child1.setUserObject(mcd);
        child.add(child1);
        
        TreeTableModel model = new MultiCellTreeTableModel(root);
        return model;
    }
    
    /**
     * center the frame
     */
    private void centreFrame() throws HeadlessException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jXTreeTable1 = new org.jdesktop.swingx.JXTreeTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MultiCellDemo");
        jScrollPane1.setViewportView(jXTreeTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MultiCellDemo().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXTreeTable jXTreeTable1;
    // End of variables declaration//GEN-END:variables
    
}
