/*
 * PaintPicker.java
 *
 * Created on July 19, 2006, 7:13 PM
 */

package org.jdesktop.beans.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXGradientChooser;
import org.jdesktop.swingx.color.EyeDropperColorChooserPanel;
import org.jdesktop.swingx.graphics.PaintUtils;

/**
 *
 * @author  joshy
 */
public class PaintPicker extends javax.swing.JPanel {
    Component lastPickerUsed = null;
    Paint selectedPaint = Color.PINK;
    JXGradientChooser gradientPicker;
    /** Creates new form PaintPicker */
    public PaintPicker() {
        initComponents();
        
        // set up the color picker
        lastPickerUsed = colorPicker;
        colorPicker.addChooserPanel(new EyeDropperColorChooserPanel());
        ColorListener colorListener = new ColorListener();
        colorPicker.getSelectionModel().addChangeListener(colorListener);
        alphaSlider.addChangeListener(colorListener);
        
        // set up the gradient picker
        gradientPicker = new JXGradientChooser();
        tabbedPane.addTab("Gradient",gradientPicker);
        gradientPicker.addPropertyChangeListener("gradient",new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                lastPickerUsed = gradientPicker;
                setPaint(gradientPicker.getGradient());
            }
        });
        
        // update when the tabs change
        tabbedPane.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                lastPickerUsed = tabbedPane.getSelectedComponent();
                Paint old = selectedPaint;
                if(lastPickerUsed == colorPickerParent) {
                    selectedPaint = colorPicker.getSelectionModel().getSelectedColor();
                }
                if(lastPickerUsed == gradientPicker) {
                    selectedPaint = gradientPicker.getGradient();
                }
                firePropertyChange("paint",old,selectedPaint);
            }
        });
        
    }
    
    public Paint getPaint() {
        return selectedPaint;
    }
    
    public void addPicker(JComponent comp, String title) {
        tabbedPane.addTab(title,comp);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tabbedPane = new javax.swing.JTabbedPane();
        colorPickerParent = new javax.swing.JPanel();
        colorPicker = new javax.swing.JColorChooser();
        jLabel1 = new javax.swing.JLabel();
        alphaSlider = new javax.swing.JSlider();

        setLayout(new java.awt.GridBagLayout());

        colorPickerParent.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 10.0;
        colorPickerParent.add(colorPicker, gridBagConstraints);

        jLabel1.setText("Alpha:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        colorPickerParent.add(jLabel1, gridBagConstraints);

        alphaSlider.setMaximum(255);
        alphaSlider.setValue(255);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        colorPickerParent.add(alphaSlider, gridBagConstraints);

        tabbedPane.addTab("Color", colorPickerParent);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 10.0;
        add(tabbedPane, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    public void setPaint(Paint paint) {
        if(paint == selectedPaint) {
            return;
        }
        Paint old = selectedPaint;
        selectedPaint = paint;
        if(paint instanceof Color) {
            tabbedPane.setSelectedComponent(colorPickerParent);
            colorPicker.setColor((Color)paint);
            alphaSlider.setValue(((Color)paint).getAlpha());
        }
        if(paint instanceof MultipleGradientPaint) {
            tabbedPane.setSelectedComponent(gradientPicker);
            gradientPicker.setGradient((MultipleGradientPaint)paint);
        }
        firePropertyChange("paint", old, selectedPaint);
    }

    // return a paint suitable for display in a property sheet preview
    Paint getDisplayPaint(Rectangle box) {
        if(getPaint() instanceof MultipleGradientPaint) {
            return getFlatGradient(gradientPicker, box.getWidth());
        }
        return getPaint();
    }
    
    /**
     * Creates a flat version of the current gradient. This is a linear gradient
     * from 0.0 to length,0. This gradient is suitable for drawing previews of
     * the real gradient.
     *
     * @param length
     * @return a gradient Paint with values between 0.0 and length
     */
    private MultipleGradientPaint getFlatGradient(JXGradientChooser chooser, double length) {
        MultipleGradientPaint gp = chooser.getGradient();

        // set up the data for the gradient
        float[] fractions = gp.getFractions();
        Color[] colors = gp.getColors();

        // fill in the gradient
        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float((float) length, 0);
        MultipleGradientPaint paint = new LinearGradientPaint(
                (float) start.getX(),
                (float) start.getY(),
                (float) end.getX(),
                (float) end.getY(),
                fractions, colors);
        return paint;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider alphaSlider;
    public javax.swing.JColorChooser colorPicker;
    private javax.swing.JPanel colorPickerParent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    private class ColorListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            lastPickerUsed = colorPicker;
            Paint old = selectedPaint;
            selectedPaint = PaintUtils.setAlpha(colorPicker.getSelectionModel().getSelectedColor(), alphaSlider.getValue());
            firePropertyChange("paint", old, selectedPaint);
        }
    }

    
}
