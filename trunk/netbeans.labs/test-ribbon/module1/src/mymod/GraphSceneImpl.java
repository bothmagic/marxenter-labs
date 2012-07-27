/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymod;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.swing.JComponent;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author marxma
 */
public class GraphSceneImpl extends GraphScene<MyNode, String> {
    
    private LayerWidget mainLayer;
    private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
    
    public GraphSceneImpl() {
        
        mainLayer = new LayerWidget (this);
        addChild (mainLayer);
        getActions().addAction(ActionFactory.createZoomAction());
        
        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {

    public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
        Image dragImage = getImageFromTransferable(transferable);
        JComponent view = getView();
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        Rectangle visRect = view.getVisibleRect();
        view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
        g2.drawImage(dragImage,
                AffineTransform.getTranslateInstance(point.getLocation().getX(),
                point.getLocation().getY()),
                null);
        return ConnectorState.ACCEPT;
    }

    public void accept(Widget widget, Point point, Transferable transferable) {
        Image image = getImageFromTransferable(transferable);
        Widget w = GraphSceneImpl.this.addNode(new MyNode(image));
        w.setPreferredLocation(widget.convertLocalToScene(point));
    }

}));
    }
    
    private Image getImageFromTransferable(Transferable transferable) {
    Object o = null;
    try {
        o = transferable.getTransferData(DataFlavor.imageFlavor);
    } catch (IOException ex) {
        ex.printStackTrace();
    } catch (UnsupportedFlavorException ex) {
        ex.printStackTrace();
    }
    return o instanceof Image ? (Image) o : ImageUtilities.loadImage("org/netbeans/shapesample/palette/shape1.png");
}
   
    @Override
    protected Widget attachNodeWidget(MyNode n) {
        IconNodeWidget widget = new IconNodeWidget(this);
        widget.setImage(n.getImage());
        widget.setLabel(Long.toString(n.hashCode()));
        //double-click, the event is consumed while double-clicking only:
        widget.getLabelWidget().getActions().addAction(editorAction);

    //single-click, the event is not consumed:
    widget.getActions().addAction(createSelectAction()); 

    //mouse-dragged, the event is consumed while mouse is dragged:
    widget.getActions().addAction(ActionFactory.createMoveAction()); 

    //mouse-over, the event is consumed while the mouse is over the widget:
    widget.getActions().addAction(createObjectHoverAction()); 
        mainLayer.addChild(widget);
        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(String e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeSourceAnchor(String e, MyNode n, MyNode n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeTargetAnchor(String e, MyNode n, MyNode n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private class LabelTextFieldEditor implements TextFieldInplaceEditor {

    public boolean isEnabled(Widget widget) {
        return true;
    }

    public String getText(Widget widget) {
        return ((LabelWidget) widget).getLabel();
    }

    public void setText(Widget widget, String text) {
        ((LabelWidget) widget).setLabel(text);
    }

}
    
}
