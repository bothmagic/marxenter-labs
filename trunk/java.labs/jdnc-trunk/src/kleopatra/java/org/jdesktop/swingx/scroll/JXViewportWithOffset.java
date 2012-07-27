/*
 * Created on 29.06.2007
 *
 */
package org.jdesktop.swingx.scroll;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;

/**
 * "Fixed columns" - options:
 * 
 * <ol>
 * <li> old hack: internally use additional table for rowheader and move first
 * columns over there.
 * <li> idea: let the viewport (transparently for client code) implement a
 * "scrolling with unscrollable offset"
 * <li> similar to the old hack, but cleaner: implement new component (not a
 * JTable) which has two tables 
 * </ol>
 */
public class JXViewportWithOffset extends JViewport {
    private static final Logger LOG = Logger.getLogger(JXViewportWithOffset.class
            .getName());

    /**
     * Overridden to allow JComponents only. Restriction will be removed in final.
     */
    @Override
    public void setView(Component view) {
        if (!(view instanceof JComponent)) throw 
           new IllegalArgumentException("temporarily: view must be of type JComponent");
        super.setView(createLayeredPane((JComponent) view));
    }


    private JLayeredPane createLayeredPane(JComponent view) {
        ViewportLayer pane = new ViewportLayer();
        pane.setDefaultView(view);
        return pane;
    }


    /**
     * covariant override to return JComponent.
     */
    @Override
    public JComponent getView() {
        ViewportLayer layer = (ViewportLayer) super.getView();
        return layer.getDefaultView();
    }



    
    /**
     * two columns with default width.
     * 
     * @return
     */
    int getFixedWidth() {
        return 150;
    }

    Point getFixedOffset() {
        return new Point(getFixedWidth(), 0);
    }
    
    /**
     * repaint complete row to make visuals slightly less weird 
     */
    @Override
    public void repaint(long tm, int x, int y, int w, int h) {
        super.repaint(tm, 0, y, getWidth(), h);
    }

    public static class ViewportLayer extends JLayeredPane {
        
        
        
        private JComponent defaultView;

        @Override
        public void doLayout() {
            // ensure all layers are sized the same
            Dimension size = getSize();
            Component layers[] = getComponents();
            for(Component layer : layers) {
                layer.setBounds(0, 0, size.width, size.height);
            }                      
        }

        public JComponent getDefaultView() {
            return defaultView;
        }

        public void setDefaultView(JComponent view) {
            if (getDefaultView() != null) {
                remove(getDefaultView());
            }
            if (view != null) {
                add(view, DEFAULT_LAYER);
                
            }
            defaultView = view;
            revalidate();
            
        } 
    }

    public static class XViewportLayoutWithOffset implements LayoutManager {

        public void addLayoutComponent(String name, Component comp) {
            
        }

        public void layoutContainer(Container parent) {
            JXViewportWithOffset vp = (JXViewportWithOffset)parent;
            JComponent view = vp.getView();
            Scrollable scrollableView = null;

            if (view == null) {
                return;
            }
            else if (view instanceof Scrollable) {
                scrollableView = (Scrollable) view;
            }
            
            /* All of the dimensions below are in view coordinates, except
             * vpSize which we're converting.
             */

            Insets insets = vp.getInsets();
            Dimension viewPrefSize = view.getPreferredSize();
            Dimension vpSize = vp.getSize();
            Dimension extentSize = vp.toViewCoordinates(vpSize);
            Dimension viewSize = new Dimension(viewPrefSize);

            if (scrollableView != null) {
                if (scrollableView.getScrollableTracksViewportWidth()) {
                    viewSize.width = vpSize.width;
                }
                if (scrollableView.getScrollableTracksViewportHeight()) {
                    viewSize.height = vpSize.height;
                }
            }

            Point viewPosition = vp.getViewPosition();
        }

        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(4, 4);
        }

        public Dimension preferredLayoutSize(Container parent) {
            Component view = ((JViewport)parent).getView();
            if (view == null) {
                return new Dimension(0, 0);
            }
            else if (view instanceof Scrollable) {
                return ((Scrollable)view).getPreferredScrollableViewportSize();
            }
            else {
                return view.getPreferredSize();
            }
        }

        public void removeLayoutComponent(Component comp) {
            // TODO Auto-generated method stub
            
        }
        
    }

}
