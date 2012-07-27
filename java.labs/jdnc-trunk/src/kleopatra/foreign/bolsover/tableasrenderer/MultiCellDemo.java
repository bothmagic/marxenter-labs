/*
 * $Id: MultiCellDemo.java 2570 2008-07-28 12:49:52Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

/*
 * $Log$
 * Revision 1.1  2008/07/28 12:49:52  kleopatra
 * cleanup
 * - removed/deprecated out-dated examples
 * - moved tableasrenderer from test to foreign
 *
 * Revision 1.2  2008/02/18 15:18:00  kleopatra
 *
 * sanity: synched kleopatra incubator section
 *
 * Revision 1.1  2007/04/11 11:59:34  kleopatra
 *
 * synched kleopatra section ...
 *
 * Revision 1.3  2007/01/26 13:48:55  kleopatra
 * updated kleopatra incubator to latest renderer api
 *
 * Revision 1.2  2007/01/08 09:49:39  kleopatra
 * copied and modified bolsover multicell code
 * added and used RenderingTableController
 *
 * Revision 1.1  2006/12/28 11:56:53  bolsover
 * *** empty log message ***
 *
 * @author David Bolsover
 *
 */

package bolsover.tableasrenderer;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.util.Arrays;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;


public class MultiCellDemo extends javax.swing.JFrame {
    private static  MultiCellDemo INSTANCE =  new  MultiCellDemo();
    
    /**
     * Creates new form MultiCellDemo
     */
    public MultiCellDemo() {
        initComponents();
        setTitle("MultiCell - separated view");
        jXTreeTable1.setTreeTableModel(constructDemoModel());
        jXTreeTable1.setRootVisible(true);
        jXTreeTable1.setShowsRootHandles(true);
        jXTreeTable1.setBackground(HighlighterFactory.LEDGER);
        TableCellRenderer renderer = new DefaultTableRenderer(new TableProvider());
        jXTreeTable1.getColumn(2).setCellRenderer(renderer);
        // renderer for col1 - just want to center the text
        jXTreeTable1.getColumn(1).setCellRenderer(new DefaultTableRenderer(null, JLabel.CENTER));
         // fix the row height - ? is there a better ? dynamic way?
        Component tableComponent = jXTreeTable1.prepareRenderer(renderer, 0, 2);
        jXTreeTable1.setRowHeight(tableComponent.getPreferredSize().height);
        jXTreeTable1.packColumn(2, -1);
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
        DataTreeTableNode root = new DataTreeTableNode();
        DataTreeTableNode child;
        DataTreeTableNode child1;
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
        child = new DataTreeTableNode();
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
        mcd.setQtyEndPeriod2(222);
        mcd.setQtyOnSupplyPeriod3(22);
        mcd.setQtyOnDemandPeriod3(22);
        mcd.setQtyEndPeriod3(22);
        mcd.setClosingBalance(22);
        child.setUserObject(mcd);
        root.add(child);
        child = new DataTreeTableNode();
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
        mcd.setQtyEndPeriod2(223);
        mcd.setQtyOnSupplyPeriod3(22);
        mcd.setQtyOnDemandPeriod3(22);
        mcd.setQtyEndPeriod3(22);
        mcd.setClosingBalance(22);
        child.setUserObject(mcd);
        root.add(child);
        child = new DataTreeTableNode();
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
        mcd.setQtyEndPeriod2(224);
        mcd.setQtyOnSupplyPeriod3(22);
        mcd.setQtyOnDemandPeriod3(22);
        mcd.setQtyEndPeriod3(22);
        mcd.setClosingBalance(22);
        child.setUserObject(mcd);
        root.add(child);
        child1 = new DataTreeTableNode();
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
        mcd.setQtyEndPeriod2(225);
        mcd.setQtyOnSupplyPeriod3(22);
        mcd.setQtyOnDemandPeriod3(22);
        mcd.setQtyEndPeriod3(22);
        mcd.setClosingBalance(22);
        child1.setUserObject(mcd);
        child.add(child1);
        
        TreeTableModel model = new DefaultTreeTableModel(root, 
                Arrays.asList(new String[]{"Tree", "Qty Per", "Detail"}));

        return model;
    }
 
    public static class DataTreeTableNode extends AbstractMutableTreeTableNode {

//        pu
//        public DataTreeTableNode(MultiCellData data) {
//            super(data);
//        }
        
        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int column) {
            if (getData() == null) return null;
            switch(column) {
            case 0: return getData().toString();
            case 1: return getData().getQtyPer();
            case 2: return getData();
        }
        return null;
        }

        private MultiCellData getData() {
            return (MultiCellData) getUserObject();
        }
        
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

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
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
