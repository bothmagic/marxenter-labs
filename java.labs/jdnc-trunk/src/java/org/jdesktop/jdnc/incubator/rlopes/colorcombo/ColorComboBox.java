/*
 * ColorComboBox.java
 *
 * Created on 24 de Fevereiro de 2005, 22:17
 */

package org.jdesktop.jdnc.incubator.rlopes.colorcombo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jdesktop.jdnc.incubator.bierhance.IncompatibleLookAndFeelException;
import org.jdesktop.jdnc.incubator.rlopes.MultiItemComboBox;
import org.jdesktop.jdnc.incubator.rlopes.smiliescombo.SmilieComboBoxModel;
import org.jdesktop.jdnc.incubator.rlopes.smiliescombo.SmilieRenderer;

/**
 *
 * @author Lopes
 */
public class ColorComboBox extends MultiItemComboBox {

    public static final int DEFAULT_COLUMN_COUNT = 8;
    public static final int DEFAULT_ROW_COUNT = 5;
    
    protected ColorItem overItem;
    protected boolean showCustomColorButton;
    
	public ColorComboBox() throws IncompatibleLookAndFeelException {
		super();
        
        overItem = null;
        showCustomColorButton = true; 
        setPopupColumnCount(DEFAULT_COLUMN_COUNT);
        setPopupRowCount(DEFAULT_ROW_COUNT);

        popupPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                Component comp = popupPanel.getComponentAt(evt.getPoint());
                
                if (comp != null) {
                    if (comp instanceof ColorItem) {
                        overItem = (ColorItem) comp;
                        setSelectedItem(overItem.getColor());
                        hidePopup();
                    }
                }
            }
		});
        
        popupPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent evt) {
                Component comp = popupPanel.getComponentAt(evt.getPoint());
                
                if (comp != null) {
                    if (comp instanceof ColorItem) {
                        if (overItem != null) {
                            overItem.setOver(false);
                        }
                        overItem = (ColorItem) comp;
                        overItem.setOver(true);
                        if (selectOnMouseOver) {
                            setSelectedItem(overItem.getColor());
                        }
                    }
                }
            }            
        });
       
        setPreferredSize(new Dimension(50, 30));
        
        JPanel popupComponent = new JPanel();
        popupComponent.setLayout(new BorderLayout());
        popupComponent.add(popupPanel, BorderLayout.CENTER);
		setPopupComponent(popupComponent);
        setRenderer(new ColorComboBoxRenderer());

        
        // key movement support
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                processKeyOnPopup(event);
                event.consume();
            }
        });
        
        
        populatePopup();        
	}
    
    public boolean getShowCustomColorButton() {
        return showCustomColorButton;
    }
    
    public void setShowCustomColorButton(boolean newValue) {
        showCustomColorButton = newValue;
    }
    
    protected void populatePopup() {
        final ColorComboBoxModel model = new ColorComboBoxModel();
        int n = model.getSize();
        final int extraItemIndex = n;
        for (int i=0; i<n; i++) {
            popupPanel.add(new ColorItem((Color) model.getElementAt(i)));
        }

        if (showCustomColorButton) {            
            JComponent popupComponent = getPopupComponent();
            JButton customColorButton = new JButton("Choose ...");
            customColorButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    Color selectedColor = (Color) getSelectedItem();
                    Color choosedColor = JColorChooser.showDialog(null, "Choose Color...", selectedColor);
                    if (choosedColor != null) {     
                        if (model.getElementAt(extraItemIndex) != null) {
                            model.removeElementAt(extraItemIndex);
                        }
                        model.insertElementAt(choosedColor, extraItemIndex);                        
                        setSelectedItem(choosedColor);
                        hidePopup();
                    }
                }
            });
            popupComponent.add(customColorButton, BorderLayout.SOUTH);
        }
        
        setModel(model);
    }
    
    
    protected synchronized void processKeyOnPopup(KeyEvent event) {
        int keyCode = event.getKeyCode();
        ColorComboBoxModel model = (ColorComboBoxModel) getModel();
        
        int selectedIndex;
        
        if (overItem != null) {
            selectedIndex = model.getIndexOf(overItem.getColor());
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
                    setSelectedItem(overItem.getColor());
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
                        
            overItem = (ColorItem) popupPanel.getComponent(newIndex);
            overItem.setOver(true);

            if (selectOnKeyPress) {
                setSelectedIndex(newIndex);
            }
        }
    }
    
}

