/*
 * SmilieComboBox.java
 *
 * Created on 25 de Fevereiro de 2005, 18:29
 */

package org.jdesktop.jdnc.incubator.jxcombobox.smiliescombo;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;
import org.jdesktop.jdnc.incubator.jxcombobox.IncompatibleLookAndFeelException;
import org.jdesktop.jdnc.incubator.jxcombobox.MultiItemComboBox;

/**
 *
 * @author Lopes
 */
public class SmilieComboBox extends MultiItemComboBox {
    
    protected static final int DEFAULT_COLUMN_COUNT = 10;
    protected static final int DEFAULT_ROW_COUNT = 8;
    
    protected SmilieRenderer overItem;
    
    /** Creates a new instance of SmilieComboBox */
    public SmilieComboBox() throws IncompatibleLookAndFeelException {
        super();
        
        setPopupColumnCount(DEFAULT_COLUMN_COUNT);
        setPopupRowCount(DEFAULT_ROW_COUNT);
        
        popupPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                Component comp = popupPanel.getComponentAt(evt.getPoint());
                
                if (comp != null) {
                    if (comp instanceof SmilieRenderer) {
                        SmilieRenderer smilie = (SmilieRenderer) comp;
                        setSelectedItem(smilie.parent);
                        hidePopup();
                    }
                }
            }
        });
        
        popupPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent evt) {
                Component comp = popupPanel.getComponentAt(evt.getPoint());
                
                if (comp != null) {
                    if (comp instanceof SmilieRenderer) {
                        if (overItem != null) {
                            overItem.setOver(false);
                        }
                        overItem = (SmilieRenderer) comp;
                        overItem.setOver(true);
                        if (selectOnMouseOver) {
                            setSelectedItem(overItem.parent);
                        }
                    }
                }
            }
        });
        
        
        // key movement support
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                processKeyOnPopup(event);
                event.consume();
            }
        });
        
        
        populatePopup();
        setRenderer(new SmilieComboBoxRenderer());
    }
    
    protected void populatePopup() {
        SmilieComboBoxModel model = new SmilieComboBoxModel();
        setModel(model);          
        int n = model.getSize();
        for (int i=0; i<n; i++) {
            Smilie smilie = (Smilie) model.getElementAt(i);
            popupPanel.add(smilie.getNewRenderer());
        }
    }       
    
    protected synchronized void processKeyOnPopup(KeyEvent event) {
        int keyCode = event.getKeyCode();
        SmilieComboBoxModel model = (SmilieComboBoxModel) getModel();
        
        int selectedIndex;
        
        if (overItem != null) {
            selectedIndex = model.getIndexOf(overItem.parent);
        } else {
            selectedIndex = getSelectedIndex();
        }
        
        Point coord = convertIndexToXY(selectedIndex);
        
        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (coord.y > 0) {
                    coord.y--;
                } else {
                    return;
                }
                break;
                
            case KeyEvent.VK_DOWN:
                if (coord.y < DEFAULT_ROW_COUNT-1) {
                    coord.y++;
                } else {
                    return;
                }
                break;
                
            case KeyEvent.VK_LEFT:
                if (coord.x > 0) {
                    coord.x--;
                } else {
                    return;
                }
                break;
                
            case KeyEvent.VK_RIGHT:
                if (coord.x < DEFAULT_COLUMN_COUNT-1) {
                    coord.x++;
                } else {
                    return;
                }
                break;
                
            case KeyEvent.VK_ENTER:
                if (overItem != null) { // only happends if the user hits enter immediatly after clicking the combobox arrow button (1º key pressed)
                    setSelectedItem(overItem.parent);
                }
                hidePopup();
                break;
                
            case KeyEvent.VK_ESCAPE:
                hidePopup();
                break;
        }
        
        int newIndex = convertXYToIndex(coord);
        
        /*
         * The first condition checks if an item change was made (not on boundaries and
         * the second condition checks if there are more items (in the case we don't have a square of items)
         */
        if ((newIndex != selectedIndex) && (newIndex < this.getItemCount())) {
            if (overItem != null) {
                overItem.setOver(false);
            }
            
            overItem = (SmilieRenderer) popupPanel.getComponent(newIndex);
            overItem.setOver(true);
            
            if (selectOnKeyPress) {
                setSelectedIndex(newIndex);
            }
        }
    }
    
}

