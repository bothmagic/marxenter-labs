package org.jdesktop.jdnc.incubator.vprise.form;

/**
 * This template allows a user to customize the look of a form. This template
 * includes reasonable default values that will produce a good looking form
 * layout.
 *
 * @author Shai Almog
 */
public class FormTemplate {
    /**
     * The spacing to the x position of the first label
     */
    private int initialX = 5;
    
    /**
     * The spacing to the y position of the first label
     */
    private int initialY = 5;

    /**
     * Padding on the x axis
     */
    private int xPad = 3;

    /**
     * Padding on the y axis
     */
    private int yPad = 3;
    
    /**
     * The number of columns in the form
     */
    private int columns = 1;
    
    /**
     * The spacing to the x position of the first label
     */
    public int getInitialX() {
        return initialX;
    }

    /**
     * The spacing to the x position of the first label
     */
    public void setInitialX(int initialX) {
        this.initialX = initialX;
    }

    /**
     * The spacing to the y position of the first label
     */
    public int getInitialY() {
        return initialY;
    }

    /**
     * The spacing to the y position of the first label
     */
    public void setInitialY(int initialY) {
        this.initialY = initialY;
    }

    /**
     * The number of columns in the form
     */
    public int getColumns() {
        return columns;
    }

    /**
     * The number of columns in the form
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Padding on the x axis
     */
    public int getXPad() {
        return xPad;
    }

    /**
     * Padding on the x axis
     */
    public void setXPad(int xPad) {
        this.xPad = xPad;
    }

    /**
     * Padding on the y axis
     */
    public int getYPad() {
        return yPad;
    }

    /**
     * Padding on the y axis
     */
    public void setYPad(int yPad) {
        this.yPad = yPad;
    }
}
