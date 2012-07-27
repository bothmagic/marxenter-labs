/*
 * Created on 19.03.2009
 *
 */
package core.combo16.plaf;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import core.combo16.plaf.BasicXComboPopup.MouseEventGrabber;

public class MouseListenerWrapper implements MouseListener {

    private MouseListener wrappee;
    private MouseEventGrabber grabber;

    public MouseListenerWrapper(MouseListener wrappee, MouseEventGrabber grabber) {
        this.wrappee = wrappee;
        this.grabber = grabber;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (shouldDelegate(e)) wrappee.mouseClicked(e);
    }

    private boolean shouldDelegate(MouseEvent e) {
        return grabber == null || !grabber.grabbed(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (shouldDelegate(e)) wrappee.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (shouldDelegate(e))wrappee.mouseExited(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (shouldDelegate(e))wrappee.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (shouldDelegate(e))wrappee.mouseReleased(e);
    }

}
