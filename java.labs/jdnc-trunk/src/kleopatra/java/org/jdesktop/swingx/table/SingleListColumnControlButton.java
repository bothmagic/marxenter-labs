/*
 * Created on 22.06.2006
 *
 */
package org.jdesktop.swingx.table;

import java.awt.ComponentOrientation;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.renderer.BooleanValue;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.renderer.StringValue;

/**
 * An alternative ColumnControl popping up a dialog with one list
 * and "fake editable" checkbox items for columns.
 * 
 * This is a quick proof-of-concept implementation, using the new 
 * SwingX renderer support.
 * 
 * <li> a general issue with BoundAction and callbacks on handlers: the handler
 *   class must be a public toplevel class 
 *     
 * 
 * @author Jeanette Winzenburg
 */
public class SingleListColumnControlButton extends ColumnControlButton {

    public SingleListColumnControlButton(JXTable table, Icon icon) {
        super(table, icon);
    }
    
    @Override
    protected ColumnControlPopup createColumnControlPopup() {
        return new ListControlPopup();
    }

    
    public static class ListControlPopup implements ColumnControlPopup {

        ActionListModel model;
        JXList visibleColumns;
        JComponent content;
        
        private MouseListener toggleVisibilityListener;
        
        public ListControlPopup() {
            this.init();
        }

        //----------------- init and configure view
        
        /**
         * one-for-all: inits model, bindable views and configures/binds all.
         * 
         */
        private void init() {
            // create the shared model
            model = new ActionListModel();
            // create the components which need to be bound
            visibleColumns = new JXList();
            // wire everything
            install(visibleColumns, true);
        }
        
        /**
         * 
         * @param columns
         * @param matchSelected
         */
        private void install(final JXList columns, boolean matchSelected) {
            columns.setModel(model);
            columns.setCellRenderer(getActionCellRenderer());
            columns.addMouseListener(getMouseListener());
            // action toggling selected state of selected list item
            final Action toggleSelected = new AbstractActionExt(
                    "toggle column visibility") {

                public void actionPerformed(ActionEvent e) {
                    if (columns.isSelectionEmpty())
                        return;
                    AbstractActionExt selectedItem = (AbstractActionExt) columns
                            .getSelectedValue();
                    selectedItem.setSelected(!selectedItem.isSelected());
                }

            };
            // bind action to space
            columns.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),
                    "toggleSelectedActionState");
            columns.getActionMap().put("toggleSelectedActionState", toggleSelected);
            columns.setToolTipText("Toggle Column Visibility - double click/press space");

        }

        /**
         * Lazily creates and returns the shared mouseListener for both
         * lists.
         *  
         * @return a Mouselistener to trigger item moves.
         */
        private MouseListener getMouseListener() {
            if (toggleVisibilityListener == null) {
             toggleVisibilityListener = new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        Action action =  ((JComponent) e.getSource()).getActionMap().get("toggleSelectedActionState");
                        if (action != null) {
                            action.actionPerformed(null);
                        }
                    }
                }
                
             };
            }
            return toggleVisibilityListener;
        }


        /**
         * creates and returns a cellRenderer specialized on AbstractActionExt.
         * 
         * @return a listCellRenderer specialized on AbstractActionExt.
         */
        private ListCellRenderer getActionCellRenderer() {
            StringValue sv = new StringValue() {

                public String getString(Object value) {
                    if (value instanceof AbstractActionExt) {
                        return ((AbstractActionExt) value).getName();
                    }
                    return "";
                }
                
            };
            BooleanValue bv = new BooleanValue() {

                public boolean getBoolean(Object value) {
                    if (value instanceof AbstractActionExt) {
                        return ((AbstractActionExt) value).isSelected();
                    }
                    return false;
                }
                
            };

            ComponentProvider provider = new CheckBoxProvider(new MappedValue(sv, null, bv), JLabel.LEADING);
            
            return new DefaultListRenderer(provider);
        }

        //------------------------ implement ColumnControlPopup
        
        public void applyComponentOrientation(ComponentOrientation o) {
            // TODO: implement
        }

        public void updateUI() {
            // TODO: implement
        }
        /**
         * pops up a modal dialog to move columns from visible to hidden 
         * and vice versa.
         * 
         */
        public void toggleVisibility(JComponent owner) {
            Frame frame = JOptionPane.getRootFrame();
            Window window = SwingUtilities.getWindowAncestor(owner);
            if (window instanceof Frame) {
                frame = (Frame) window;
            }
            JDialog dialog = new JDialog(frame, true);
            dialog.add(getContent());
            dialog.setTitle("Columns");
            dialog.pack();
            
            Rectangle buttonRect = owner.getBounds();
            // hmmm... something fishy going on if rectangle point is re-used?
            Point b = new Point(buttonRect.getLocation());
            b.move(buttonRect.width, 0);
            SwingUtilities.convertPointToScreen(b, owner);
            dialog.setLocation(b);
            dialog.setVisible(true);
        }


        public void removeAll() {
            model.removeAll();
        }
        
        public void addAdditionalActionItems(List<? extends Action> actions) {
            // do nothing
        }

        public void addVisibilityActionItems(List<? extends AbstractActionExt> actions) {
            model.setActions(actions);
        }

        //-------------------- init ui
       /**
        * lazily creates and returns the content component for showing
        * in the dialog.
        * @return the content of the dialog.
        */
        private JComponent getContent() {
            if (content == null) {
                content = buildContent();
            }
            return content;
        }
       
        protected JComponent buildContent() {
            JComponent left = Box.createVerticalBox();
            left.add(new JScrollPane(visibleColumns));
            return left;
      }
    }
    /**
     * A custom ListModel, taking a List of ColumnVisibilityActions and
     * registering PropertyChangeListener on each element.
     */
    public static class ActionListModel extends AbstractListModel {

        List<Action> actions;
        private PropertyChangeListener actionPropertyChangeListener;
        
        public void setActions(List<? extends Action> elements) {
            this.removeAll();
            getActions().addAll(elements);
            for (Action action : actions) {
                bind(action);
            }
            if (getSize() > 0) {
                fireIntervalAdded(this, 0, getSize() - 1);
            }
        }
        

        public void removeAll() {
            int oldSize = getSize();
            if (oldSize == 0) return;
            for (Action value : actions) {
                release(value);
            }
            getActions().clear();
            fireIntervalRemoved(this, 0, oldSize - 1);
        }

        protected List<Action> getActions() {
            if (actions == null) {
                actions = new ArrayList<Action>();
            }
            return actions;
        }
        
        private void release(Action value) {
            value.removePropertyChangeListener(getActionPropertyChangeListener());
        }

        private void bind(Action action) {
            action.addPropertyChangeListener(getActionPropertyChangeListener());
            
        }

        private PropertyChangeListener getActionPropertyChangeListener() {
            if (actionPropertyChangeListener == null) {
                actionPropertyChangeListener = createActionPropertyChangeListener();
            }
            return actionPropertyChangeListener;
        }

        private PropertyChangeListener createActionPropertyChangeListener() {
            PropertyChangeListener listener = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    fireElementChanged(evt.getSource());
                    
                }
                
            };
            return listener;
        }

        protected void fireElementChanged(Object source) {
            int index = getActions().indexOf(source);
            if (index >= 0) {
                fireContentsChanged(this, index, index);
            }
            
        }

        //------------------ implement ListModel
        
        public int getSize() {
            return getActions().size();
        }

        public Object getElementAt(int index) {
            return getActions().get(index);
        }
        
    }


}