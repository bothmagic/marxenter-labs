/*
 * $Id: BasicScrollMapPopup.java 3296 2010-08-03 17:52:57Z kschaefe $
 *
 * Copyright 20102006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXScrollMap;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.ShapePainter;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.HorizontalAlignment;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.VerticalAlignment;
import org.jdesktop.swingx.util.Contract;

/**
 * The default implementation of a {@code ScrollMapPopup}.
 * 
 * @author kschaefer
 */
public class BasicScrollMapPopup extends JPopupMenu implements ScrollMapPopup {
    private static class ClippingShapePainter extends ShapePainter {
        public ClippingShapePainter(Shape shape, Paint paint) {
            super(shape, paint);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Shape provideShape(Graphics2D g, Object comp, int width, int height) {
            Area a = new Area(g.getClip());
            a.subtract(new Area(getShape()));
            
            return a;
        }
    }
    
    private class Handler implements MouseListener, MouseMotionListener, PropertyChangeListener,
            Serializable {
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();

            if ("lightWeightPopupEnabled".equals(propertyName)) {
                setLightWeightPopupEnabled(scrollMap.isLightWeightPopupEnabled());
            }
        }
        
        public void mouseDragged(MouseEvent e) {
            if (!isVisible()) {
                return;
            }
            
            Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), imagePanel);
            updateRectangle(p);
            
            if (scrollMap.isSynchronizedScrolling()) {
                updateViewportViewPosition();
            }
        }
        
        public void mouseReleased(MouseEvent e) {
            updateViewportViewPosition();
        }
        
        private void updateViewportViewPosition() {
            JViewport vp = scrollMap.getViewport();
            
            if (vp == null) {
                return;
            }
            
            Rectangle r = (Rectangle) shapePainter.getShape();
            Point p = r.getLocation();
            
            p.x *= Math.round((float) vp.getWidth() / r.width);
            p.y *= Math.round((float) vp.getHeight() / r.height);
            
            //ensure rounding doesn't add bogus pixel gaps
            Component c = vp.getView();
            p.x = Math.min(p.x, c.getWidth() - vp.getWidth());
            p.y = Math.min(p.y, c.getHeight() - vp.getHeight());
            
            vp.setViewPosition(p);
        }
        
        public void mousePressed(MouseEvent e) { }
        
        public void mouseClicked(MouseEvent e) { }

        public void mouseEntered(MouseEvent e) { }

        public void mouseExited(MouseEvent e) { }

        public void mouseMoved(MouseEvent e) { }
    }

    private static Border BORDER = new LineBorder(Color.BLACK, 1);

    /**
     * The scroll map for this popup.
     */
    protected JXScrollMap scrollMap;
    
    /**
     * A painter for drawing an image of the viewport view.
     */
    protected ImagePainter imagePainter;
    
    /**
     * A painter for drawing the view rectangle on over the view image.
     */
    protected ShapePainter shapePainter;
    
    /**
     * The container housing the painters.
     */
    protected JXPanel imagePanel;

    private Handler handler;

    private MouseListener mouseListener;
    
    private MouseMotionListener mouseMotionListener;

    private PropertyChangeListener propertyChangeListener;

    /**
     * Creates a popup for the specified scroll map.
     * 
     * @param scrollMap
     *            the scroll map to create a popup for
     * @throws NullPointerException
     *             if {@code scrollMap} is {@code null}
     */
    public BasicScrollMapPopup(JXScrollMap scrollMap) {
        super();
        setName("ScrollMapPopup.popup");
        this.scrollMap = Contract.asNotNull(scrollMap, "scrollMap cannot be null");

        setLightWeightPopupEnabled(scrollMap.isLightWeightPopupEnabled());

        imagePainter = new ImagePainter();
        shapePainter = new ClippingShapePainter(null, ColorUtil.setAlpha(Color.GRAY, 128));
        shapePainter.setHorizontalAlignment(HorizontalAlignment.LEFT);
        shapePainter.setVerticalAlignment(VerticalAlignment.TOP);
        imagePanel = createImagePanel();
        
        configurePopup();
        
        installListeners();
    }

    /**
     * Creates the image painter container.
     * 
     * @return the container to hold the painters
     */
    protected JXPanel createImagePanel() {
        JXPanel p = new JXPanel() {
            @Override
            public Dimension getPreferredSize() {
                BufferedImage img = imagePainter.getImage();
                
                if (img == null) {
                    return new Dimension();
                }
                
                return new Dimension(img.getWidth(), img.getHeight());
            }
        };
        p.setOpaque(false);
        p.setBackgroundPainter(new CompoundPainter<Object>(imagePainter, shapePainter));
        p.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        
        return p;
    }
    
    /**
     * Installs any required listeners on the scroll map.
     */
    protected void installListeners() {
        if ((propertyChangeListener = createPropertyChangeListener()) != null) {
            scrollMap.addPropertyChangeListener(propertyChangeListener);
        }
        
        if ((mouseListener = createMouseListener()) != null) {
            scrollMap.addMouseListener(mouseListener);
        }
        
        if ((mouseMotionListener = createMouseMotionListener()) != null) {
            scrollMap.addMouseMotionListener(mouseMotionListener);
        }
    }

    /**
     * Creates the mouse listener which will be added to the scroll map.
     * <p>
     * <strong>Warning:</strong> When overriding this method, make sure to maintain the existing
     * behavior.
     * 
     * @return a {@code MouseListener} which will be added to the scroll map or {@code null}
     */
    protected MouseListener createMouseListener() {
        return getHandler();
    }

    /**
     * Creates the mouse motion listener which will be added to the scroll map.
     * <p>
     * <strong>Warning:</strong> When overriding this method, make sure to maintain the existing
     * behavior.
     * 
     * @return a <code>MouseMotionListener</code> which will be added to the scroll map or {@code
     *         null}
     */
    protected MouseMotionListener createMouseMotionListener() {
        return getHandler();
    }

    /**
     * Creates the property change listener which will be added to the scroll map.
     * <p>
     * <strong>Warning:</strong> When overriding this method, make sure to maintain the existing
     * behavior.
     * 
     * @return a {@code PropertyChangeListener} which will be added to the scroll map or {@code
     *         null}
     */
    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    /**
     * Configures the popup portion of the scroll map. This method is called when the UI class is
     * created.
     */
    protected void configurePopup() {
        setLayout(new BorderLayout());
        setBorderPainted(true);
        setBorder(BORDER);
        setOpaque(false);
        add(imagePanel);
        setDoubleBuffered(true);
        setFocusable(false);
    }

    private Point getPopupLocation() {
        Dimension d = scrollMap.getSize();
        setSize(imagePanel.getPreferredSize());
        
        Point p = new Point();
        
        p.x = d.width >> 1;
        p.y = d.height >> 1;
        
        p.x -= getWidth() >> 1;
        p.y -= getHeight() >> 1;
        
        return p;
    }

    private BufferedImage createImage() {
        Component c = scrollMap.getViewport().getView();
        
        Dimension mps = scrollMap.getMaximumPopupSize();
        double sx = mps.getWidth() / c.getWidth();
        double sy = mps.getHeight() / c.getHeight();
        double scale = Math.min(sx, sy); 
        
        BufferedImage img = GraphicsUtilities.createCompatibleImage((int) (c.getWidth() * scale),
                (int) (c.getHeight() * scale));

        Graphics2D g = img.createGraphics();
        
        try {
            g.scale(scale, scale);
            c.paint(g);
        } finally {
            g.dispose();
        }
        
        return img;
    }
    
    private Shape createShape() {
        JViewport vp = scrollMap.getViewport();
        Component c = vp.getView();
        
        Dimension mps = scrollMap.getMaximumPopupSize();
        double sx = mps.getWidth() / c.getWidth();
        double sy = mps.getHeight() / c.getHeight();
        double scale = Math.min(sx, sy); 
        
        Point p = vp.getViewPosition();
        
        int x = (int) Math.round(p.x * scale);
        int y = (int) Math.round(p.y * scale);
        int w = (int) Math.round(vp.getWidth() * scale);
        int h = (int) Math.round(vp.getHeight() * scale);
        
        Rectangle r = new Rectangle(x, y, w, h);
        
        return r;
    }

    private void centerMouseOrUpdateRectangle() {
        if (BasicMouseUtilities.canMoveMouse()) {
            Rectangle r = (Rectangle) shapePainter.getShape();
            Point p = r.getLocation();
            p.x += r.width >> 1;
            p.y += r.height >> 1;
            
            BasicMouseUtilities.moveMouseTo(p, imagePanel);
        } else {
            Point p = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(p, imagePanel);
            updateRectangle(p);
        }
    }
    
    void updateRectangle(Point pointInPopup) {
        Rectangle r = (Rectangle) shapePainter.getShape();
        
        pointInPopup.x -= r.width >> 1;
        pointInPopup.y -= r.height >> 1;
        
        pointInPopup.x = Math.max(Math.min(pointInPopup.x, imagePanel.getWidth() - r.width), 0);
        pointInPopup.y = Math.max(Math.min(pointInPopup.y, imagePanel.getHeight() - r.height), 0);
        
        r.setLocation(pointInPopup);
        imagePanel.repaint();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        imagePainter.setImage(createImage());
        shapePainter.setShape(createShape());
        
        Point location = getPopupLocation();
        show(scrollMap, location.x, location.y);
        
        centerMouseOrUpdateRectangle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide() {
        setVisible(false);
        shapePainter.setShape(null);
        imagePainter.setImage(null);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstallingUI() {
        scrollMap.removeMouseMotionListener(mouseMotionListener);
        scrollMap.removeMouseListener(mouseListener);
        scrollMap.removePropertyChangeListener(propertyChangeListener);
    }
}
