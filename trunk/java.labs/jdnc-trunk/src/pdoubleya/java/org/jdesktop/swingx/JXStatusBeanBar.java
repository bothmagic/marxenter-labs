package org.jdesktop.swingx;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import org.jdesktop.swingx.statusbar.JXLabelStatusBean;
import org.jdesktop.swingx.statusbar.StatusBean;



/**
 * JXStatusBeanBar is a JPanel to be used as a status bar on a JFrame or JXFrame, displaying
 * on or more Statusbeans, e.g. for text messages, a clock, a memory monitor, etc. StatusBeans are
 * displayed on a single line using a GridBagLayout for spacing--each bean can be added with or without
 * weight and anchor restraints. If no restraints are added, each bean is given equal X weight and
 * is anchored left-right in the order added to the status bar.
 * 
 * @author  Patrick Wright
 */
public class JXStatusBeanBar extends javax.swing.JPanel {
    /**
     * List of beans this bar contains.
     */
    private List<StatusBeanContainer> statusBeans;
    
    /**
     * Default constructor. No beans.
     */
    public JXStatusBeanBar() {
        initComponents();
        this.statusBeans = new ArrayList<StatusBeanContainer>();
    }
    
    /**
     * Adds a single bean to the status bar, at the end of the bean list
     * (added to the right of the bar). Position is EAST within the cell occupied by the
     * bean, and component does not expand on resizing the bar.
     * @param bean The StatusBean to add.
     */
    public void addStatusBean(StatusBean bean) {
        this.addStatusBean(bean, 0, GridBagConstraints.NORTHWEST);
    }
    
    /**
     * Adds a single bean to the status bar, at the end of the bean list
     * (added to the right of the bar). Position is EAST within the cell occupied by the
     * bean, and component is sized according to a weight provided; see weightX parameter.
     * 
     * @param bean The StatusBean to add.
     * 
     * @param weightX A weighting parameter to calculate how much horizontal space this
     * component receives on resizing the bar--this is the weightX property
     * of a GridBagConstraints.
     */
    public void addStatusBean(StatusBean bean, double weightX) {
        this.addStatusBean(bean, weightX, GridBagConstraints.NORTHWEST);
    }
    
    /**
     * Adds a single bean to the status bar, at the end of the bean list
     * (added to the right of the bar). Position is EAST within the cell occupied by the
     * bean, and component is sized according to a weight provided; see weightX parameter.
     * 
     * @param anchor The position where the bean within its containing cell; see
     * GridBagConstraings anchor documentation.
     * 
     * @param newBean The StatusBean to add.
     * 
     * @param weightX A weighting parameter to calculate how much horizontal space this
     * component receives on resizing the bar--this is the weightX property
     * of a GridBagConstraints.
     */
    public void addStatusBean(StatusBean newBean, double weightX, int anchor) {
        List<StatusBean> beans = listStatusBeans();
        for ( StatusBean bean : beans ) {
            if ( bean.getName().equals(newBean.getName())) {
                remove(bean);
                break;
            }
        }
        statusBeans.add(new StatusBeanContainer(newBean, weightX, anchor));
        layoutBeans();
    }
    
    /**
     * Returns a List of the currently installed StatusBeans.
     */
    public List<StatusBean> listStatusBeans() {
        List<StatusBean> beans = new ArrayList<StatusBean>();
        for ( StatusBeanContainer bc : statusBeans ) {
            beans.add(bc.bean);
        }
        return beans;
    }
    
    /**
     * Removes a single StatusBean from the bar.
     */
    public void remove(StatusBean bean) {
        // TODO: check similar code--what if it's not there?
        StatusBeanContainer it = null;
        for ( StatusBeanContainer bc : statusBeans ) {
            if ( bc.bean == bean ) {
                it = bc;
            }
        }
        if ( it != null ) {
            statusBeans.remove(bean);
        }
    }
    
    /**
     * Removes the named component from the bar.
     */
    public void remove(String beanName) {
        StatusBeanContainer it = null;
        for ( StatusBeanContainer bc : statusBeans ) {
            if ( bc.bean.getName().equals(beanName)) {
                it = bc;
            }
        }
        if ( it != null ) {
            statusBeans.remove(it);
        }
    }
    
    
    
    // TODO: need component insets
    // general for top, bottom
    // inter-component
    // leftmost and rightmost
    
    /** Lays the beans out in this panel; probably better to synchronize around this. */
    private void layoutBeans() {
        removeAll();
        int cnt = 0;
        double totalXWeight = 0;
        for ( StatusBeanContainer cont : statusBeans ) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.gridx = GridBagConstraints.RELATIVE;
            gbc.gridy = 0;
            
            // TODO: allow b/r/l insets to be specified
            int top = 3;
            int left = 0;
            int bottom = 3;
            int right = 3;
            if ( cnt == 1 ) {
                left = 1;
            } else if ( cnt < statusBeans.size()) {
                left = 0;
                right = 3;
            } else {
                right = 1;
            }
            gbc.insets = new Insets(top, left, bottom, right);
            
            gbc.weightx = cont.gblWeightX;
            totalXWeight += cont.gblWeightX;
            gbc.anchor = cont.gblAnchor;
            add(cont.bean.getDisplayComponent(), gbc);
        }
        if ( totalXWeight < 1 ) {
            Component comp = this.getComponent(0);
            remove(comp);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.gridx = GridBagConstraints.RELATIVE;
            gbc.gridy = 0;
            gbc.insets = new Insets(3, 3, 3, 3);
            gbc.weightx = 1.0D - totalXWeight;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;
            
            add(comp, gbc, 0);
        }
    }
    
    /* A simple container class for beans--stores anchor and weighting information. */
    private class StatusBeanContainer {
        StatusBean bean;
        double gblWeightX;
        int gblAnchor;
        
        StatusBeanContainer(StatusBean bean) {
            this(bean, 0, GridBagConstraints.NORTHWEST);
        }
        StatusBeanContainer(StatusBean bean, double weightX) {
            this(bean, weightX, GridBagConstraints.NORTHWEST);
        }
        StatusBeanContainer(StatusBean bean, double weightX, int anchor) {
            this.bean = bean;
            this.gblWeightX = weightX;
            this.gblAnchor = anchor;
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        
        setLayout(new java.awt.GridBagLayout());
        
    }
// </editor-fold>//GEN-END:initComponents
    
    
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
    
}
