/*
 * Created on 19.03.2009
 *
 */
package core.combo16.plaf;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 * ComboPopup for use with JXXComboBox. It differs from super in that it forces a
 * actionPerformed on the combo on mouseReleased.<p>
 * 
 * PENDING JW: cleanup on uninstallling - the wrapper still has a reference to handler.
 * 
 */
public class BasicXComboPopup extends BasicComboPopup {

    
    public BasicXComboPopup(JComboBox combo) {
        super(combo);
    }

    @Override
    protected MouseListener createListMouseListener() {
        return new MouseListenerWrapper(super.createListMouseListener(), createMouseEventGrabber());
    }


    private MouseEventGrabber createMouseEventGrabber() {
        MouseEventGrabber grabber = new MouseEventGrabber() {

            @Override
            public boolean grabbed(MouseEvent e) {
                if (!isReleasedOnList(e))
                    return false;
                if (list.getModel().getSize() > 0) {
                    // workaround for cancelling an edited item (bug 4530953)
                    if (comboBox.getSelectedIndex() == list.getSelectedIndex()) {
                        comboBox.getEditor().setItem(list.getSelectedValue());
                    }
                    comboBox.setSelectedIndex(list.getSelectedIndex());
                }
                comboBox.actionPerformed(null);
                return true;
            }

            private boolean isReleasedOnList(MouseEvent e) {
                return MouseEvent.MOUSE_RELEASED == e.getID() && e.getSource() == list;
            }};
        return grabber;
    }


    public static interface MouseEventGrabber {
        boolean grabbed(MouseEvent e);
    }
}
