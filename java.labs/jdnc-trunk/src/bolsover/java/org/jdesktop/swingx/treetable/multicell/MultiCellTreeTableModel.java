/*
 * $Id: MultiCellTreeTableModel.java 989 2006-12-28 11:56:54Z bolsover $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

/*
 * $Log$
 * Revision 1.1  2006/12/28 11:56:54  bolsover
 * *** empty log message ***
 *
 * @author David Bolsover
 *
 */

package org.jdesktop.swingx.treetable.multicell;


import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

public class MultiCellTreeTableModel extends DefaultTreeTableModel{
    private  static MultiCellTableModel mctm;
    private  static JXTable jxt;
    private  static MultiCellTableModel mctm1;
    private  static JXTable jxt1;
    private  static JScrollPane scrollPane1;

    
    
    private static HighlighterPipeline doHighligters(){
        Highlighter[] lighters = new Highlighter[] {new AlternateRowHighlighter(
                new Color(0xF0, 0xF0, 0xE0),Color.white, null)
        };
        return new HighlighterPipeline(lighters);
    }
    
    /**
     * Creates a new instance of MultiCellTreeTableModel
     */
    public MultiCellTreeTableModel(TreeNode root) {
        super(root);
        
    }
    
    /*
     * Initialise static variables
     */
    static {
        // use seaparate varialbles for header data tables
        mctm1 = new MultiCellTableModel();
        jxt1 = new JXTable(mctm1);
        ((DefaultTableCellRenderer)jxt1.getCellRenderer(0,0)).setHorizontalAlignment(JLabel.RIGHT);
        jxt1.setHighlighters(doHighligters());
        scrollPane1 = new JScrollPane();
        scrollPane1.setViewportView(jxt1);
        scrollPane1.setVerticalScrollBarPolicy(scrollPane1.VERTICAL_SCROLLBAR_NEVER);
        // body data tables
        mctm = new MultiCellTableModel();
        jxt = new JXTable(mctm);
        ((DefaultTableCellRenderer)jxt.getCellRenderer(0,0)).setHorizontalAlignment(JLabel.RIGHT);
        jxt.setHighlighters(doHighligters());
    }
    
    
    public int getColumnCount(){
        return 3;
    }
    
    public String getColumnName(int column){
        switch(column){
            case 0: return "Tree";
            case 1: return "Qty Per";
            case 2: return "Detail";
        }
        return "";
    }
    
    
    
    public Object getValueAt(Object node, int column) {
        final MultiCellData data = (MultiCellData) ((DefaultMutableTreeNode)node).getUserObject();
        switch(column) {
            case 0: return data.toString();
            case 1: return data.getQtyPer();
            case 2:
                if (data.isShowColHeader()){
                    mctm1.setData(data);
                    return scrollPane1;
                } else {
                    mctm.setData(data);
                    return jxt;
                }
        }
        return null;
    }
    
    
}
