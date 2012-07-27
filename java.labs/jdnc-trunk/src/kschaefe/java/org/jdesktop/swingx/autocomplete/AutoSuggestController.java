/**
 * 
 */
package org.jdesktop.swingx.autocomplete;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;


/**
 * TODO destroy on ESC in text component
 * TODO activate on ctr+space in text component
 * TODO add up/down scrolling of list
 * @author Karl George Schaefer
 */
class AutoSuggestController extends FocusAdapter {
    private JTextComponent textComponent;
    private AutoSuggestPopup autoSuggestPopup;
    
    private Popup popup;
    
    /**
     * @param textComponent
     * @param autoSuggestPopup
     */
    public AutoSuggestController(JTextComponent textComponent, AutoSuggestPopup autoSuggestPopup) {
        this.textComponent = textComponent;
        this.autoSuggestPopup = autoSuggestPopup;
    }

    private void createPopup() {
        if (popup != null) {
            destroyPopup();
        }
        
        Point pt = textComponent.getLocationOnScreen();
        
        try {
            Rectangle r = textComponent.modelToView(textComponent.getCaretPosition());
            Point caret = r.getLocation();
            SwingUtilities.convertPointToScreen(caret, textComponent);
            
            //keep the left edge of the text component
            pt.y = caret.y + r.height + 1;
        } catch (BadLocationException e) {
            //if we fail, just place the popup at the bottom
            pt.y += textComponent.getHeight() + 1;
        }
        
        Dimension d = autoSuggestPopup.getPreferredSize();
        d.width = Math.max(d.width, textComponent.getWidth());
        autoSuggestPopup.setPreferredSize(d);
        
        popup = PopupFactory.getSharedInstance()
                .getPopup(textComponent, autoSuggestPopup, pt.x, pt.y);
        popup.show();
    }
    
    private void destroyPopup() {
        popup.hide();
        popup = null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void focusGained(FocusEvent e) {
        //TODO create when typing started; not here
        createPopup();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void focusLost(FocusEvent e) {
        if (e.getOppositeComponent() != textComponent) {
            destroyPopup();
        }
    }
}
