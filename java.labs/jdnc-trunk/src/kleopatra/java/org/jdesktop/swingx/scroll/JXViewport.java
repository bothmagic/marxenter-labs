/*
 * Created on 29.06.2007
 *
 */
package org.jdesktop.swingx.scroll;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.ViewportLayout;

/**
 * 
 */
public class JXViewport extends JViewport {
    private static final Logger LOG = Logger.getLogger(JXViewport.class
            .getName());
    
    JComponent glassPane;
    
    public JXViewport() {
        super();
        // trying to intercept mouseEvents on fixed columns:
        // the coordinates are for scrolled-under, must be
        // re-directed to fixed columns.
        // dead end: can't add a second child to viewport
        // addImpl is overridden
        glassPane = createGlassPane();
    }
    
    private JComponent createGlassPane() {
        JComponent comp = new JPanel();
        return comp;
    }
    
    /**
     * dead end: super re-directs to setView which calls super.addImpl.
     */
    @Override
    protected void addImpl(Component child, Object constraints, int index) {
        super.addImpl(child, constraints, index);
    }

    /**
     * brute force painting: transform the graphics.
     */
    @Override
    public void paint(Graphics g) {
//        super.paint(g);
        Graphics2D g2 = (Graphics2D) g.create();
        Point p = getViewPosition();
        g2.translate(-p.x , -p.y);
        getView().paint(g2);
        g2.translate(p.x, 0);
        g2.setClip(0, p.y, getFixedWidth(), getHeight());
        getView().paint(g2);
        g2.dispose();
    }

    /**
     * Sets the view coordinates that appear in the upper left
     * hand corner of the viewport, does nothing if there's no view.
     *
     * @param p  a <code>Point</code> object giving the upper left coordinates
     */
    @Override
    public void setViewPosition(Point p) 
    {
        
        Component view = getView();
        if (view == null) {
            return;
        }

        int oldX, oldY, x = p.x, y = p.y;

        /* Collect the old x,y values for the views location
         * and do the song and dance to avoid allocating 
         * a Rectangle object if we don't have to.
         */
        if (view instanceof JComponent) {
            JComponent c = (JComponent)view;
            oldX = c.getX();
            oldY = c.getY();
        }
        else {
            Rectangle r = view.getBounds();
            oldX = r.x;
            oldY = r.y;
        }

        /* The view scrolls in the opposite direction to mouse 
         * movement.
         */
        int newX = -x;
        int newY = -y;
        
        if ((oldX != newX) || (oldY != newY)) {
            // don't get fancy
            view.setLocation(newX, newY);
            fireStateChanged();
        }
    }

    
    @Override
    public Dimension toViewCoordinates(Dimension size) {
        return super.toViewCoordinates(size);
    }

    /**
     * logical coordinates are assumed to be continous, no
     * jumps.
     */
    @Override
    public Point toViewCoordinates(Point p) {
//        if (getView() == null) return super.toViewCoordinates(p);
//        Point viewP = getView().getLocation();
//        LOG.info("viewPosition: " + viewP);
//        if (viewP.x < 0) {
//            Point moved = new Point(p);
//            moved.translate(-viewP.x, 0);
//            if (p.x < getFixedWidth()) {
//                
//                return moved;
//            }
//        }
        return super.toViewCoordinates(p);
    }

    /**
     * c&p, removed call to private validateView and tried to 
     * take fixedWidth into play.
     */
    @Override
    public void scrollRectToVisible(Rectangle contentRect) {
        super.scrollRectToVisible(contentRect);
        Component view = getView();

        if (view == null) {
            return;
        } else {
//            if (!view.isValid()) {
//                // If the view is not valid, validate. scrollRectToVisible
//                // may fail if the view is not valid first, contentRect
//                // could be bigger than invalid size.
//                validateView();
//            }
            int     dx = 0, dy = 0;

            dx = positionAdjustmentX(getWidth(), contentRect.width, contentRect.x);
            dy = positionAdjustment(getHeight(), contentRect.height, contentRect.y);
            LOG.info("dx/y " + dx + "/" + dy);
            if (dx != 0 || dy != 0) {
                Point viewPosition = getViewPosition();
                Dimension viewSize = view.getSize();
                int startX = viewPosition.x;
                int startY = viewPosition.y;
                Dimension extent = getExtentSize();

                viewPosition.x -= dx;
                viewPosition.y -= dy;
                // Only constrain the location if the view is valid. If the
                // the view isn't valid, it typically indicates the view
                // isn't visible yet and most likely has a bogus size as will
                // we, and therefore we shouldn't constrain the scrolling
                if (view.isValid()) {
                    if (getParent().getComponentOrientation().isLeftToRight()) {
                        if (viewPosition.x + extent.width > viewSize.width) {
                            viewPosition.x = Math.max(0, viewSize.width - extent.width);
                        } else if (viewPosition.x < 0) {
                            viewPosition.x = 0;
                        }
                    } else {
                        if (extent.width > viewSize.width) {
                            viewPosition.x = viewSize.width - extent.width;
                        } else {
                            viewPosition.x = Math.max(0, Math.min(viewSize.width - extent.width, viewPosition.x));
                        }
                    }
                    if (viewPosition.y + extent.height > viewSize.height) {
                        viewPosition.y = Math.max(0, viewSize.height -
                                                  extent.height);
                    }
                    else if (viewPosition.y < 0) {
                        viewPosition.y = 0;
                    }
                }
                if (viewPosition.x != startX || viewPosition.y != startY) {
                    setViewPosition(viewPosition);
                    // NOTE: How JViewport currently works with the
                    // backing store is not foolproof. The sequence of
                    // events when setViewPosition
                    // (scrollRectToVisible) is called is to reset the
                    // views bounds, which causes a repaint on the
                    // visible region and sets an ivar indicating
                    // scrolling (scrollUnderway). When
                    // JViewport.paint is invoked if scrollUnderway is
                    // true, the backing store is blitted.  This fails
                    // if between the time setViewPosition is invoked
                    // and paint is received another repaint is queued
                    // indicating part of the view is invalid. There
                    // is no way for JViewport to notice another
                    // repaint has occured and it ends up blitting
                    // what is now a dirty region and the repaint is
                    // never delivered.
                    // It just so happens JTable encounters this
                    // behavior by way of scrollRectToVisible, for
                    // this reason scrollUnderway is set to false
                    // here, which effectively disables the backing
                    // store.
                    scrollUnderway = false;
                }
            }
        }
//        super.scrollRectToVisible(contentRect);
    }

    /*  
     * experiment with proper scroll adjustment: need to somehow 
     * hook to fixedWidth.
     */
   private int positionAdjustmentX(int parentWidth, int childWidth, int childAt)    {

       //   +-----+
       //   | --- |     No Change
       //   +-----+
       if (childAt >= getFixedWidth() && childWidth + childAt <= parentWidth)    {
           return 0;
       }

       //   +-----+
       //  ---------   No Change
       //   +-----+
       if (childAt <= getFixedWidth() && childWidth + childAt >= parentWidth) {
           return 0;
       }

       //   +-----+
       // --|     |       ?? ambigous target might be fixed part or under it.
       //   +-----+
       if (childAt <= getFixedWidth() && childWidth + childAt <= getFixedWidth()) {
           return 0;
       }

       //   +-----+          +-----+
       //   |   ----    ->   | ----|
       //   +-----+          +-----+
       if (childAt > getFixedWidth() && childWidth <= parentWidth)    {
           return -childAt + parentWidth - childWidth;
       }

       //   +-----+             +-----+
       //   |  --------  ->     |--------
       //   +-----+             +-----+
       if (childAt >= getFixedWidth() && childWidth >= parentWidth)   {
           return -childAt;
       }

       //   +-----+          +-----+
       // ----    |     ->   |---- |
       //   +-----+          +-----+
       if (childAt <= getFixedWidth() && childWidth <= parentWidth)   {
           return -childAt;
       }

       //   +-----+             +-----+
       //-------- |      ->   --------|
       //   +-----+             +-----+
       if (childAt < getFixedWidth() && childWidth >= parentWidth)    {
           return -childAt + parentWidth - childWidth;
       }

       return 0;
   }

    /* c&p 
     * Used by the scrollRectToVisible method to determine the
     *  proper direction and amount to move by. The integer variables are named
     *  width, but this method is applicable to height also. The code assumes that
     *  parentWidth/childWidth are positive and childAt can be negative.
     */
   private int positionAdjustment(int parentWidth, int childWidth, int childAt)    {

       //   +-----+
       //   | --- |     No Change
       //   +-----+
       if (childAt >= 0 && childWidth + childAt <= parentWidth)    {
           return 0;
       }

       //   +-----+
       //  ---------   No Change
       //   +-----+
       if (childAt <= 0 && childWidth + childAt >= parentWidth) {
           return 0;
       }

       //   +-----+          +-----+
       //   |   ----    ->   | ----|
       //   +-----+          +-----+
       if (childAt > 0 && childWidth <= parentWidth)    {
           return -childAt + parentWidth - childWidth;
       }

       //   +-----+             +-----+
       //   |  --------  ->     |--------
       //   +-----+             +-----+
       if (childAt >= 0 && childWidth >= parentWidth)   {
           return -childAt;
       }

       //   +-----+          +-----+
       // ----    |     ->   |---- |
       //   +-----+          +-----+
       if (childAt <= 0 && childWidth <= parentWidth)   {
           return -childAt;
       }

       //   +-----+             +-----+
       //-------- |      ->   --------|
       //   +-----+             +-----+
       if (childAt < 0 && childWidth >= parentWidth)    {
           return -childAt + parentWidth - childWidth;
       }

       return 0;
   }

    /**
     * left-over from experiment with table being aware of its split
     * view. 
     * 
     * @param p
     * @return
     */
    public Point toRealViewCoordinates(Point p) {
        if (getView() == null) return p;
        Point viewportP = getViewPosition();
        if (viewportP.x == 0) {
            return p;
        }
        if (p.x < viewportP.x + getFixedWidth()) {
            p.translate(-viewportP.x, 0);
            return p;
        }
        return p;
    }
    
    
    /**
     * two columns with default width.
     * 
     * @return
     */
    int getFixedWidth() {
        return 150;
    }

    /**
     * repaint complete row to make visuals slightly less weird 
     */
    @Override
    public void repaint(long tm, int x, int y, int w, int h) {
        super.repaint(tm, 0, y, getWidth(), h);
    }


    public class XViewportLayout extends ViewportLayout {

        @Override
        public void layoutContainer(Container parent) {
            super.layoutContainer(parent);
            JXViewport viewport = (JXViewport) parent;
            viewport.glassPane.setBounds(0, 0, viewport.getFixedWidth(), viewport.getHeight());

        }
        
        
    }
}
