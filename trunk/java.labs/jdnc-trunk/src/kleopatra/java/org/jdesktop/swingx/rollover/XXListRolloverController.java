/*
 * Created on 19.07.2006
 *
 */
package org.jdesktop.swingx.rollover;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

import javax.swing.JList;

import org.jdesktop.swingx.JXList.DelegatingRenderer;

/**
 * implementation of rolloverController which adds the live
 * renderer to the list. This version uses the SwingX 
 * enhanced renderer support (TaskPane specific).
 */
public class XXListRolloverController<T extends JList> extends
    ListRolloverController<JList> {
    
    private static final Logger LOG = Logger
            .getLogger(XXListRolloverController.class.getName());
    
    Component activeRendererComponent;
    private MouseListener rendererMouseListener;

    @Override
    protected RolloverRenderer getRolloverRenderer(Point location, boolean prepare) {
        DelegatingRenderer renderer = (DelegatingRenderer) component.getCellRenderer();
        if (prepare) {
            Object element = component.getModel().getElementAt(location.y);
            renderer.getListCellRendererComponent(component, element,
                    location.y, false, true);

        }
        return (RolloverRenderer) renderer.getDelegateRenderer();
    }

    @Override
    protected void rollover(Point oldLocation, Point newLocation) {
        // this is not entirely clean: need to handle mouseExit from component
        // as opposed to mouseExit to activeRenderer
        if ((newLocation == null)) return;
        if (activeRendererComponent != null) {
            removeActiveRendererComponent();
        }
        
        if (hasRollover(newLocation)) {
            Rectangle cellBounds = component.getCellBounds(newLocation.y, newLocation.y);
            LiveTaskPaneListRenderer renderer = (LiveTaskPaneListRenderer) getRolloverRenderer(newLocation, false);
            Object element = component.getModel().getElementAt(newLocation.y);
            activeRendererComponent = renderer.getRolloverListCellRendererComponent(component, element,
                    newLocation.y, false, true);
            activeRendererComponent.setBounds(cellBounds);
            activeRendererComponent.addMouseListener(getRendererMouseListener());
            component.add(activeRendererComponent);
            activeRendererComponent.validate();
            
        }
        
    }

    
    @Override
    protected void click(Point location) {
        LOG.info("clicked " + location);
        super.click(location);
    }

    private MouseListener getRendererMouseListener() {
        if (rendererMouseListener == null) {
            rendererMouseListener = new MouseAdapter() {

                @Override
                public void mouseExited(MouseEvent e) {
                    removeActiveRendererComponent();
                }


                @Override
                public void mouseReleased(MouseEvent e) {
                    // force the re-validate and sizing - dirty!
                    int index = component.locationToIndex(e.getPoint());
                    Rectangle cellBounds = component.getCellBounds(index, index);
                    activeRendererComponent.setBounds(cellBounds);
                    component.revalidate();
                    component.repaint();
                }
                
            };
        }
        return rendererMouseListener;
    }

    protected void removeActiveRendererComponent() {
        activeRendererComponent.removeMouseListener(getRendererMouseListener());
        component.remove(activeRendererComponent);
        activeRendererComponent = null;
        component.revalidate();
        component.repaint();
    }
    
}
