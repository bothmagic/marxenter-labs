/*
 * Created on 18.07.2006
 *
 */
package org.jdesktop.swingx.rollover;


import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.ListModel;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.incubatorutil.AncientSwingTeam;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.renderer.StringValue;

/**
 * a quick test driver to excercize the TaskPaneRenderer.
 * 
 * @author Jeanette Winzenburg
 */
public class RolloverExperiments extends InteractiveTestCase {
    private static final Logger LOG = Logger
            .getLogger(RolloverExperiments.class.getName());
    public static void main(String[] args) {
        setSystemLF(true);
        RolloverExperiments test = new RolloverExperiments();
        try {
            test.runInteractiveTests();
//            test.runInteractiveTests(".*Editor.*");
//            test.runInteractiveTests(".*RToL.*");
          } catch (Exception e) {
              System.err.println("exception when executing interactive tests:");
              e.printStackTrace();
          }

    }
    
    /**
     * Rollover editor. Use custom RolloverController.
     */
    public void interactiveEditor() {
        AncientSwingTeam model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        table.getColumnExt(0).setEditable(false);
        Set<Object> colors = new HashSet<Object>();
        for (int i = 0; i < model.getRowCount(); i++) {
           colors.add(model.getValueAt(i, 2)); 
        }
        JComboBox box = new JComboBox(colors.toArray());
        table.getColumnExt(2).setCellEditor(new DefaultCellEditor(box));
        
        RolloverController<JXTable> controller = new EditingRolloverController();
        controller.install(table);
        showWithScrollingInFrame(table, "");
        
    }
    
    /**
     * RolloverController implementing fully interactive JXTable 
     * (aka MouseOver editing) as described in the blog at 
     * palantirtech.com
     */
    public static class EditingRolloverController extends RolloverController<JXTable> {


        @Override
        protected void rollover(Point oldLocation, Point newLocation) {
            if ((newLocation == null) || (newLocation.x < 0)
                    || !component.hasFocus()) return;
            component.editCellAt(newLocation.y, newLocation.x);
        }
        
        /**
         * {@inheritDoc} <p>
         * Overridden to set the surrendersFocus property to true.
         */
        @Override
        public void install(JXTable table) {
            super.install(table);
            table.setSurrendersFocusOnKeystroke(true);
        }


        //----- implements/overrides to do nothing in this context
        @Override
        protected Point getFocusedCell() {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        protected RolloverRenderer getRolloverRenderer(Point location,
                boolean prepare) {
            // TODO Auto-generated method stub
            return null;
        }

        /**
         * Overridden to do nothing.
         */
        @Override
        protected void registerExecuteButtonAction() {
        }

        /**
         * Overridden to do nothing.
         */
        @Override
        protected void unregisterExecuteButtonAction() {
        }
        
        
    }
    /**
     * use TaskPaneRenderer in JXList.
     *
     */
    public void interactiveTaskPaneRenderer() {
        final JXList list = new JXList(createTaskPaneModel(10));
        list.setCellRenderer(createRolloverRenderer());
        list.setFixedCellHeight(-1);
        list.setRolloverEnabled(true);
        JXFrame frame = wrapWithScrollingInFrame(list, "TaskPane as rendering component in list");
        frame.setVisible(true);
        Action action = new AbstractActionExt("toggle selected expand") {

            public void actionPerformed(ActionEvent e) {
                int selected = list.getSelectedIndex();
                LOG.info("selected " + selected);
                if (selected >= 0) {
                    SampleTaskPaneModel model = (SampleTaskPaneModel) list.getElementAt(selected);
                    model.setExpanded(!model.isExpanded());
                }
                
            }
            
        };
        addAction(frame, action);
    }

    private LiveTaskPaneListRenderer createRolloverRenderer() {
        LiveTaskPaneListRenderer renderer = new LiveTaskPaneListRenderer();
        StringValue sv = new StringValue() {
            
            public String getString(Object value) {
                if (value instanceof SampleTaskPaneModel) {
                    return ((SampleTaskPaneModel) value).getTitle();
                }
                return "";
            }
            
        };
        renderer.getComponentProvider().setStringValue(sv);
        return renderer;
    }

    /**
     * use TaskPaneRenderer in JXList with enhanced RolloverController.
     *
     */
    public void interactiveTaskPaneRendererWithRolloverAdded() {
        final JXList list = new JXList(createTaskPaneModel(10)) {

            @Override
            protected ListRolloverController createLinkController() {
                return new XXListRolloverController();
            }

            
        };
        list.setCellRenderer(createRolloverRenderer());
        list.setFixedCellHeight(-1);
        list.setRolloverEnabled(true);
        JXFrame frame = wrapWithScrollingInFrame(list, "live taskPaneRenderer in list");
        frame.setVisible(true);
        Action action = new AbstractActionExt("toggle selected expand") {

            public void actionPerformed(ActionEvent e) {
                int selected = list.getSelectedIndex();
                LOG.info("selected " + selected);
                if (selected < 0) selected = 2;
                if (selected >= 0) {
                    SampleTaskPaneModel model = (SampleTaskPaneModel) list.getElementAt(selected);
                    model.setExpanded(!model.isExpanded());
                }
                
            }
            
        };
        addAction(frame, action);
    }


    
    private ListModel createTaskPaneModel(int count) {
        final DefaultListModel l = new ContentListeningListModel();
        for (int i = 0; i < count; i++) {
            SampleTaskPaneModel item = new SampleTaskPaneModel();
            item.setExpanded(false);
            item.setTitle("TaskPane - " + i); 
            List<Action> actions = createItemList(i);
            item.setActions(actions);
            l.addElement(item);
        }
        return l;
    }

    private List<Action> createItemList(int i) {
        List<Action> items = new ArrayList<Action>();
        for (int j = 0; j < i; j++) {
            items.add(createAction(i, j));
        }
        return items;
    }

    private Action createAction(int i, int j) {
        Action action = new AbstractAction("taskPane - " + i + " :: action at - " + j) {

            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        };
        return action;
    }

    public static class ContentListeningListModel extends DefaultListModel {
        private PropertyChangeListener p;

        @Override
        public void addElement(Object obj) {
            super.addElement(obj);
            if (obj instanceof AbstractBean) {
                ((AbstractBean) obj).addPropertyChangeListener(getPropertyChangeListener());
            }
        }

        protected void fireContentPropertyChanged(PropertyChangeEvent evt) {
            int index = indexOf(evt.getSource());
            fireContentsChanged(this, index, index);

        }
        protected PropertyChangeListener getPropertyChangeListener() {
            if (p == null) {
                 p = new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        fireContentPropertyChanged(evt);
                    }
                };
            }
            return p;
        }
        
    }
}
