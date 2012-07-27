/*
 * Created on 18.07.2006
 *
 */
package org.jdesktop.swingx.rollover;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.BorderFactory;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;


/**
 * a quick implementation of a rollover-enabled ListCellRenderer using a 
 * JXTaskPane. Also has experimental support for a real live component,
 * added to the list on cell-enter and removed on cell-exit.
 */
public class LiveTaskPaneProvider extends ComponentProvider<JXTaskPane> 
    implements LiveRolloverRenderer {
    private static final Logger LOG = Logger
            .getLogger(LiveTaskPaneProvider.class.getName());
    private AbstractHyperlinkAction<SampleTaskPaneModel> linkAction;
    private JXTaskPane liveTaskPane;
    private SampleTaskPaneModel liveTaskPaneModel;
    private PropertyChangeListener taskPanePropertyListener;

    public LiveTaskPaneProvider() {
        super();
        liveTaskPane = createRendererComponent();
        liveTaskPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED), liveTaskPane.getBorder()));
    }

    // ----------- implement RolloverRenderer
    public void doClick() {
        if (!isEnabled()) return;
        getTaskPaneModelLinkAction().actionPerformed(null);
    }

    public boolean isEnabled() {
        return true;
    }

    // ---------- implement ext RolloverRenderer
    
    
    public JXTaskPane getLiveRendererComponent() {
        return liveTaskPane;
    }

    // ------------- implement abstract methods of RendererComponentController
    
    @Override
    protected void configureState(CellContext context) {
        // TODO Auto-generated method stub
    }

    @Override
    protected JXTaskPane createRendererComponent() {
        JXTaskPane taskPane = new JXTaskPane();
        taskPane.setAnimated(false);
        taskPane.setScrollOnExpand(false);
        taskPane.setCollapsed(true);
        return taskPane;
    }

    @Override
    protected void format(CellContext context) {
        if (((LiveListCellContext) context).isLive() && isEnabled()) {
            configureLiveRenderingComponent(context.getValue());
        } else {
            configureDeadRenderingComponent(context.getValue());
        }
    }

//--------------------- configure dead renderer
    
    private void configureDeadRenderingComponent(Object value) {
        JXTaskPane taskPane = rendererComponent;
        if (value instanceof SampleTaskPaneModel) {
            configureTaskPane(taskPane, (SampleTaskPaneModel) value);
        } else {
            unconfigureTaskPane(taskPane, value);
        }
    }
    
//--------------------- configure live renderer
    
    private void configureLiveRenderingComponent(Object value) {
        JXTaskPane taskPane = liveTaskPane;
        if (value instanceof SampleTaskPaneModel) {
            configureLiveTaskPane(taskPane, value);
        } else {
            unconfigureTaskPane(taskPane, value);
        }
    }

    private void configureLiveTaskPane(JXTaskPane taskPane, Object value) {
        if (liveTaskPaneModel != null) {
            taskPane.removePropertyChangeListener(getTaskPanePropertyChangeListener());
            liveTaskPaneModel = null;
        }
        liveTaskPaneModel = (SampleTaskPaneModel) value;
        configureTaskPane(taskPane, liveTaskPaneModel);
        taskPane.addPropertyChangeListener(getTaskPanePropertyChangeListener());
    }

    private PropertyChangeListener getTaskPanePropertyChangeListener() {
        if (taskPanePropertyListener == null) {
            taskPanePropertyListener = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if ("collapsed".equals(evt.getPropertyName())) {
                        updateModelExpansion((Boolean) evt.getNewValue());
                    }
                    
                }
                
            };
        }
        return taskPanePropertyListener;
    }

    protected void updateModelExpansion(Boolean collapsed) {
//        if (liveTaskPaneModel == null) return;
        liveTaskPaneModel.setExpanded(!collapsed);
        // validation problems of the live component
        // the "usual suspects" didn't help ... leave it for now...
        
    }

//--------------------- utility methods for both live and dead component
    
    private void configureTaskPane(JXTaskPane taskPane, SampleTaskPaneModel model) {
        taskPane.removeAll();
        for (Action item : model.getActions()) {
            // obviously this is a no-no in the real world 
            taskPane.add(item);
        }
        taskPane.setTitle(getString(model));
        taskPane.setCollapsed(!model.isExpanded());
        getTaskPaneModelLinkAction().setTarget(model);
        
    }
    private void unconfigureTaskPane(JXTaskPane taskPane, Object value) {
        taskPane.removeAll();
        taskPane.setTitle(getString(value));
        taskPane.setCollapsed(true);
    }


    private AbstractHyperlinkAction<SampleTaskPaneModel> getTaskPaneModelLinkAction() {
        if (linkAction == null) {
            linkAction = new AbstractHyperlinkAction<SampleTaskPaneModel>() {

                public void actionPerformed(ActionEvent e) {
                    boolean expanded = getTarget().isExpanded();
                    getTarget().setExpanded(!expanded);
                    LOG.info("expanded: " + getTarget().getTitle());
                    
                }
                
            };
        }
        return linkAction;
    }

    @Override
    protected void configureVisuals(CellContext context) {
    }


}
