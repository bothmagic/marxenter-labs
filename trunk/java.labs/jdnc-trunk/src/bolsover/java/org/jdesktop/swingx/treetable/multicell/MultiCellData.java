/*
 * $Id: MultiCellData.java 989 2006-12-28 11:56:54Z bolsover $
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

/**
 * @author David Bolsover
 */
public class MultiCellData {
    private boolean showColHeader = false;
    private String partId;
    private String description;
    private String nodeLabel;
    private int qtyPer;
    private int safetyStockQty;
    private int qtyOnHand;
    private int qtyOnSupplyPeriod1;
    private int qtyOnDemandPeriod1;
    private int qtyEndPeriod1;
    private int qtyOnSupplyPeriod2;
    private int qtyOnDemandPeriod2;
    private int qtyEndPeriod2;
    private int qtyOnSupplyPeriod3;
    private int qtyOnDemandPeriod3;
    private int qtyEndPeriod3;
    private int closingBalance;
    /**
     * Creates a new instance of MultiCellData
     */
    public MultiCellData() {
    }
    
    public String getNodeLabel() {
        return nodeLabel;
    }
    
    public void setNodeLabel(String val) {
        this.nodeLabel = val;
    }
    
    public int getQtyPer() {
        return qtyPer;
    }
    
    public void setQtyPer(int qtyPer) {
        this.qtyPer = qtyPer;
    }
    
    Object getSafetyStockQty() {
        return safetyStockQty;
    }
    
    public int getQtyOnHand() {
        return qtyOnHand;
    }
    
    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }
    
    public int getQtyOnSupplyPeriod1() {
        return qtyOnSupplyPeriod1;
    }
    
    public void setQtyOnSupplyPeriod1(int qtyOnSupplyPeriod1) {
        this.qtyOnSupplyPeriod1 = qtyOnSupplyPeriod1;
    }
    
    public int getQtyOnDemandPeriod1() {
        return qtyOnDemandPeriod1;
    }
    
    public void setQtyOnDemandPeriod1(int qtyOnDemandPeriod1) {
        this.qtyOnDemandPeriod1 = qtyOnDemandPeriod1;
    }
    
    public int getQtyEndPeriod1() {
        return qtyEndPeriod1;
    }
    
    public void setQtyEndPeriod1(int qtyEndPeriod1) {
        this.qtyEndPeriod1 = qtyEndPeriod1;
    }
    
    public int getQtyOnSupplyPeriod2() {
        return qtyOnSupplyPeriod2;
    }
    
    public void setQtyOnSupplyPeriod2(int qtyOnSupplyPeriod2) {
        this.qtyOnSupplyPeriod2 = qtyOnSupplyPeriod2;
    }
    
    public int getQtyOnDemandPeriod2() {
        return qtyOnDemandPeriod2;
    }
    
    public void setQtyOnDemandPeriod2(int qtyOnDemandPeriod2) {
        this.qtyOnDemandPeriod2 = qtyOnDemandPeriod2;
    }
    
    public int getQtyEndPeriod2() {
        return qtyEndPeriod2;
    }
    
    public void setQtyEndPeriod2(int qtyEndPeriod2) {
        this.qtyEndPeriod2 = qtyEndPeriod2;
    }
    
    public int getQtyOnSupplyPeriod3() {
        return qtyOnSupplyPeriod3;
    }
    
    public void setQtyOnSupplyPeriod3(int qtyOnSupplyPeriod3) {
        this.qtyOnSupplyPeriod3 = qtyOnSupplyPeriod3;
    }
    
    public int getQtyOnDemandPeriod3() {
        return qtyOnDemandPeriod3;
    }
    
    public void setQtyOnDemandPeriod3(int qtyOnDemandPeriod3) {
        this.qtyOnDemandPeriod3 = qtyOnDemandPeriod3;
    }
    
    public int getQtyEndPeriod3() {
        return qtyEndPeriod3;
    }
    
    public void setQtyEndPeriod3(int qtyEndPeriod3) {
        this.qtyEndPeriod3 = qtyEndPeriod3;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSafetyStockQty(int safetyStockQty) {
        this.safetyStockQty = safetyStockQty;
    }

    public int getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(int closingBalance) {
        this.closingBalance = closingBalance;
    }
    
    public String toString(){
        return partId + " - " + description;
    }

    public boolean isShowColHeader() {
        return showColHeader;
    }

    public void setShowColHeader(boolean showColHeader) {
        this.showColHeader = showColHeader;
    }
            
}
