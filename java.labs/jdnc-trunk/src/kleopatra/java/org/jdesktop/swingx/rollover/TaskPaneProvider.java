/*
 * Created on 23.03.2008
 *
 */
package org.jdesktop.swingx.rollover;

import java.util.logging.Logger;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;

public class TaskPaneProvider extends ComponentProvider<JXTaskPane> {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(TaskPaneProvider.class
            .getName());

    @Override
    protected void configureState(CellContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    protected JXTaskPane createRendererComponent() {
        JXTaskPane taskPane = new JXTaskPane();
        taskPane.setAnimated(false);
        taskPane.setScrollOnExpand(false);
        return taskPane;
    }

    @Override
    protected void format(CellContext context) {
        rendererComponent.setTitle(getValueAsString(context));
    }

    @Override
    protected void configureVisuals(CellContext context) {
    }
    
}
