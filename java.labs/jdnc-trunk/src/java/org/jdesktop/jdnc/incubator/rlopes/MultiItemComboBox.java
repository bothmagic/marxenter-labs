/*
 * MultiItemComboBox.java
 *
 * Created on 4 de Março de 2005, 23:02
 */

package org.jdesktop.jdnc.incubator.rlopes;

import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.JPanel;
import org.jdesktop.jdnc.incubator.bierhance.*;

/**
 *
 * @author Ricardo Lopes
 */
/**
 * @todo move key movement support to this class instead of having almost the same code on the descendant classes
 *
 */
public abstract class MultiItemComboBox extends JComponentComboBox {
    
    protected static final int DEFAULT_COLUMN_COUNT = 5;
    protected static final int DEFAULT_ROW_COUNT = 5;

    
    protected boolean selectOnMouseOver;
    protected boolean selectOnKeyPress;
    protected int popupColumnCount;
    protected int popupRowCount;
    protected JPanel popupPanel;    

    
    /** Creates a new instance of MultiItemComboBox */
    public MultiItemComboBox() throws IncompatibleLookAndFeelException {
        super();
        
        selectOnMouseOver = false;
        selectOnKeyPress = true;
        popupColumnCount = DEFAULT_COLUMN_COUNT;
        popupRowCount = DEFAULT_ROW_COUNT;
                
        popupPanel = new JPanel();
        popupPanel.setLayout(new GridLayout(popupRowCount, popupColumnCount));
        
        setPopupComponent(popupPanel);        
    }
    
    
    public boolean isSelectOnMouseOver() {
        return selectOnMouseOver;
    }
    
    public void setSelectOnMouseOver(boolean newValue) {
        selectOnMouseOver = newValue;
    }    
    
    public boolean isSelectOnKeyPress() {
        return selectOnKeyPress;
    }
    
    public void setSelectOnKeyPress(boolean newValue) {
        selectOnKeyPress = newValue;
    }
    
    public int getPopupColumnCount() {
        return popupColumnCount;
    }
    
    public void setPopupColumnCount(int newValue) {
        popupColumnCount = newValue;
        popupPanel.setLayout(new GridLayout(popupRowCount, popupColumnCount));
        // @todo check if we have to call invalidate 
    }
    
    public int getPopupRowCount() {
        return popupRowCount;
    }
    
    public void setPopupRowCount(int newValue) {
        popupRowCount = newValue;
        popupPanel.setLayout(new GridLayout(popupRowCount, popupColumnCount));
        // @todo check if we have to call invalidate 
    }
    
    
    
    
    protected Point convertIndexToXY(int index) {
        // GridLayout orientation is left-to-right
        int x = index % popupColumnCount;
        int y = index / popupColumnCount;
        return new Point(x, y);
    }
    
    protected int convertXYToIndex(Point p) {
        // GridLayout orientation is left-to-right
        return p.x + p.y * popupColumnCount;
    }
    
}

