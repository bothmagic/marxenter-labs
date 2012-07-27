/*
 * $Id: MultiCellTableModel.java 2570 2008-07-28 12:49:52Z kleopatra $
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
 * Revision 1.2  2007/01/08 09:49:39  kleopatra
 * copied and modified bolsover multicell code
 * added and used RenderingTableController
 *
 * Revision 1.1  2006/12/28 11:56:54  bolsover
 * *** empty log message ***
 *
 * @author David Bolsover
 *
 */

package bolsover.tableasrenderer;


import javax.swing.table.AbstractTableModel;


public class MultiCellTableModel  extends AbstractTableModel{
    
    private MultiCellData data;
    
    /**
     * Creates a new instance of MultiCellTableModel
     */
    public MultiCellTableModel() {
        
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0: return "";
            case 1: return "Period 1";
            case 2: return "Period 2";
            case 3: return "Period 3";
        }
       return null;
    }
    
    public int getRowCount() {
        return 4;
    }
    
    public int getColumnCount() {
        return 4;
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                switch(rowIndex){
                    case 0: return "Qty On Hand";
                    case 1: return "Qty On Supply";
                    case 2: return "Qty On Demand";
                    case 3: return "Balance";
                }
                return null;
            case 1:
                switch(rowIndex){
                    case 0: return data.getQtyOnHand();
                    case 1: return data.getQtyOnSupplyPeriod1();
                    case 2: return data.getQtyOnDemandPeriod1();
                    case 3: return data.getQtyEndPeriod1();
                }
                return null;
            case 2:
                switch(rowIndex){
                    case 0: return data.getQtyEndPeriod1();
                    case 1: return data.getQtyOnSupplyPeriod2();
                    case 2: return data.getQtyOnDemandPeriod2();
                    case 3: return data.getQtyEndPeriod2();
                }
                return null;
            case 3:
                switch(rowIndex){
                    case 0: return data.getQtyEndPeriod2();
                    case 1: return data.getQtyOnSupplyPeriod3();
                    case 2: return data.getQtyOnDemandPeriod3();
                    case 3: return data.getClosingBalance();
                }
                return null;
        }
        return null;
        
    }
    
    public MultiCellData getData() {
        return data;
    }
    
    public void setData(MultiCellData data) {
        this.data = data;
    }
    
}
