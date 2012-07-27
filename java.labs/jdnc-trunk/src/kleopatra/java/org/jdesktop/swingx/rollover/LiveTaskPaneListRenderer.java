/*
 * Created on 20.12.2006
 *
 */
package org.jdesktop.swingx.rollover;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JList;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

/**
 * Quick check if new swingx renderer support can handle "live" renderers. <p>
 * 
 * The new interface LiveRolloverRenderer has to be implemented by all 
 * collaborators in the chain: componentController, rendererController, 
 * DefaultXXRenderer. Additionally, the concrete cellContext needs a new property 
  * "live" property. Might be migrated to super cellContext?
 */
public class LiveTaskPaneListRenderer extends DefaultListRenderer implements 
   LiveRolloverRenderer {
    
    public LiveTaskPaneListRenderer(LiveTaskPaneProvider renderingController) {
        super(renderingController);
        cellContext = new LiveListCellContext();
   }

    public LiveTaskPaneListRenderer() {
        this(new LiveTaskPaneProvider());
    }


    public Component getRolloverListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        ((LiveListCellContext) cellContext).installContext(list, value, index, 0, isSelected, cellHasFocus, true, true, true);
        componentController.getRendererComponent(cellContext);
        return getLiveRendererComponent();
    }

    public JComponent getLiveRendererComponent() {
        return ((LiveRolloverRenderer) componentController).getLiveRendererComponent();
    }
    
}
