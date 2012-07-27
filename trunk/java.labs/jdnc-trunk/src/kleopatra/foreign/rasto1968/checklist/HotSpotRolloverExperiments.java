/*
 * Created on 02.07.2009
 *
 */
package rasto1968.checklist;

import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.BooleanValue;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.LabelProvider;
import org.jdesktop.swingx.renderer.MappedValue;

public class HotSpotRolloverExperiments extends InteractiveTestCase {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(HotSpotRolloverExperiments.class.getName());
    
    public static void main(String[] args) {
        HotSpotRolloverExperiments test = new HotSpotRolloverExperiments();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void interactiveHotSpotRollover() {
        JXList list = createCheckableList(createListModel(0, 10));
        JXFrame frame = showWithScrollingInFrame(list, "hotspot rollover");
        show(frame, 200, 400);
    }
    /**
     * Creates and returns  JXList with in-build hotspot detection.
     * Fake the factory.
     * 
     * @param model
     * @return
     */
    private JXList createCheckableList(ListModel model) {
        JXCustomRolloverList list = new JXCustomRolloverList();
        list.setAutoCreateRowSorter(true);
        list.setRolloverEnabled(true);
        list.setModel(new CheckListModel(model));
        ContainerProvider provider = new ContainerProvider(new LabelProvider(), CHECKBOX_PROVIDER);
        list.setCellRenderer(new DefaultListRenderer(provider));
        return list;
    }
    
    // c&p from CheckListFactory
    @SuppressWarnings("serial")
    private static final ComponentProvider<?> CHECKBOX_PROVIDER = new CheckBoxProvider(
            new MappedValue(null, null, new BooleanValue() {
                @Override
                public boolean getBoolean(Object value) {
                    if (value instanceof CheckListModel.SelectableNode) {
                        return ((CheckListModel.SelectableNode) value).isSelected();
                    }
                    return false;
                }

            })) {
        @Override
        protected boolean getValueAsBoolean(CellContext context) {
            if ((context.getRow() >= 0) && context.getComponent() instanceof JXList) {
                JXList list = (JXList) context.getComponent();
                ListModel model = list.getModel();
                
                //must check size of model; prototype on empty model will OOB otherwise
                if (model.getSize() > 0 && model instanceof CheckListModel) {
                    int modelIndex = list.convertIndexToModel(context.getRow());
                    return ((CheckListModel) model).isChecked(modelIndex);
                }
            }
            return super.getValueAsBoolean(context);
        }

    };

    private DefaultListModel createListModel(int start, int lenght) {
        DefaultListModel model = new DefaultListModel();
        for (int i = start; i < lenght; i++) {
            model.addElement(i);
        }
        return model;
    }

    
}
