/*
 * Created on 22.06.2006
 *
 */
package org.jdesktop.swingx.table;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Window;
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
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXDialog;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.sort.RowFilters;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * An alternative ColumnControl popping up a dialog with two list
 * for moving hidden/visible columns around.
 * 
 * This is a quick proof-of-concept implementation, showing some
 * rough edges, f.i. 
 * 
 * <li> a general issue with BoundAction and callbacks on handlers: the handler
 *   class must be a public toplevel class 
 *     
 * 
 * @author Jeanette Winzenburg
 */
public class DialogColumnControlButton extends ColumnControlButton {

    public DialogColumnControlButton(JXTable table, Icon icon) {
        super(table, icon);
    }
    
    @Override
    protected ColumnControlPopup createColumnControlPopup() {
        return new DialogControlPopup();
    }

    
    public static class DialogControlPopup implements ColumnControlPopup {

        ActionListModel model;
        JXList visibleColumns;
        JXList hiddenColumns;
        JComponent content;
        
        private MouseListener toggleVisibilityListener;
        private JButton hideButton;
        private JButton showButton;
        
        public DialogControlPopup() {
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
            visibleColumns = new JXList(true);
            hiddenColumns = new JXList(true);
            // TODO: add and wire buttons
            hideButton = new JButton(">");
            showButton = new JButton("<");
            // wire everything
            install(visibleColumns, true);
            install(hiddenColumns, false);
        }
        
        /**
         * 
         * @param columns
         * @param matchSelected
         */
        private void install(JXList columns, boolean matchSelected) {
            columns.setModel(model);
//            columns.setFilters(new FilterPipeline(new Filter[] 
//                    {new SelectedActionFilter(matchSelected)}));
            columns.setCellRenderer(getActionCellRenderer());
            columns.addMouseListener(getMouseListener());
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
                    if (e.getClickCount() == 2) {
                        moveSelectedElements((JXList) e.getSource());
                    }
                }
                
             };
            }
            return toggleVisibilityListener;
        }

        /**
         * Callback method used in listeners to move items from
         * one list to the other.
         * 
         * @param list the source list.
         */
        protected void moveSelectedElements(JXList list) {
            Object[] selected = list.getSelectedValues();
            for (int i = 0; i < selected.length; i++) {
                AbstractActionExt action = (AbstractActionExt) selected[i];
                action.setSelected(!action.isSelected());
            }
            
        }

        /**
         * creates and returns a cellRenderer specialized on AbstractActionExt.
         * 
         * PENDING: use SwingX renderer support.
         * 
         * @return a listCellRenderer specialized on AbstractActionExt.
         */
        private ListCellRenderer getActionCellRenderer() {
            ListCellRenderer l = new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    if (value instanceof AbstractActionExt) {
                        value = ((AbstractActionExt) value).getName();
                    }
                    return super.getListCellRendererComponent(list, value, index, isSelected,
                            cellHasFocus);
                }
                
            };
            return l;
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
            JXDialog findDialog = new JXLocDialog(frame, getContent());
            findDialog.setModal(true);
            findDialog.pack();
            Point location = (Point) getContent().getClientProperty("oldLocation");
            if (location == null) {
                findDialog.setLocationRelativeTo(frame);
            } else {
                findDialog.setLocation(location);
                Dimension dim = (Dimension) getContent().getClientProperty("oldSize");
                if (dim != null) {
                    findDialog.setSize(dim);
                }
            }
            findDialog.setVisible(true);
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
                content = buildContentFormLayout();
            }
            return content;
        }
       
        protected JComponent buildContent() {
            JComponent panel = new JXPanel(new GridLayout(0, 2));
            JComponent left = Box.createVerticalBox();
            left.add(new JLabel("Visible Columns:"));
            left.add(new JScrollPane(visibleColumns));
            JComponent right = Box.createVerticalBox();
            right.add(new JLabel("Hidden Columns:"));
            right.add(new JScrollPane(hiddenColumns));
            panel.add(left);
            panel.add(right);
            panel.setName("Toggle Column Visibility - double click");
            return panel;
      }

        private JComponent buildContentFormLayout() {
            /*
            COLUMN SPECS:
            f:d:g, l:4dluX:n, f:d:n, l:4dluX:n, f:d:g
            ROW SPECS:   
            c:d:n, t:4dluY:n, f:d:g
            
            COLUMN GROUPS:  { {1, 5} }
            ROW GROUPS:     {}
            
            COMPONENT CONSTRAINTS
            ( 1,  1,  1,  1, "d=f, d=c"); javax.swing.JLabel      "Visible Columns"; name=visibleColumnsLabel
            ( 5,  1,  1,  1, "d=f, d=c"); javax.swing.JLabel      "Hidden Columns"; name=hiddenColumnsLabel
            ( 1,  3,  1,  1, "d=f, d=f"); javax.swing.JScrollPane; name=visibleColumns
            ( 3,  3,  1,  1, "d=f, c"); javax.swing.JPanel; name=moveitems
            ( 5,  3,  1,  1, "d=f, d=f"); javax.swing.JScrollPane; name=hiddenColumns
            */
            JXPanel content = new JXPanel();
            FormLayout formLayout = new FormLayout(
                    "f:d:g, l:4dlu:n, f:d:n, l:4dlu:n, f:d:g", // columns
                    "c:d:n, t:4dlu:n, f:d:g" // rows

            );
            formLayout.setColumnGroups(new int[][] { {1, 5} });
            PanelBuilder builder = new PanelBuilder(formLayout, content);
            builder.setDefaultDialogBorder();
            CellConstraints cc = new CellConstraints();
            CellConstraints cl = new CellConstraints();

            builder.addLabel("Visible Columns:", cl.xywh(1, 1, 1, 1),
                    new JScrollPane(visibleColumns), cc.xywh(1, 3, 1, 1));
            builder.addLabel("Hidden Columns:", cl.xywh(5, 1, 1, 1), 
                    new JScrollPane(hiddenColumns), cc.xywh(5, 3, 1, 1));
//            builder.add(buildControl(), cc.xywh(3, 3, 1, 1, "f, c"));
            content.setName("Toggle Column Visibility - double click");
            return content;
            
        }

        private Component buildControl() {
            /*
            COLUMN SPECS:
                f:max(p;10dluX):n

                ROW SPECS:   
                c:d:n, t:4dluY:n, c:d:n

                COLUMN GROUPS:  {}
                ROW GROUPS:     {}

                COMPONENT CONSTRAINTS
                ( 1,  1,  1,  1, "d=f, d=c"); de.kleopatra.view.JButton; name=hide
                ( 1,  3,  1,  1, "d=f, d=c"); de.kleopatra.view.JButton; name=show
            */
            JXPanel buttons = new JXPanel();
            FormLayout formLayout = new FormLayout(
                    "f:max(p;15dlu):n", // columns
                    "c:d:n, t:4dlu:n, c:d:n" // rows
                   
            ); 
            PanelBuilder builder = new PanelBuilder(formLayout, buttons);
            CellConstraints cc = new CellConstraints();
            builder.add(hideButton, cc.xywh(1, 1, 1, 1));
            builder.add(showButton, cc.xywh(1, 3, 1, 1));
            return buttons;
        }

        
    }
    
    /**
     * A filter including either selected or unselected AbstractActionExt.
     */
    // extending PatternFilter is hacking around missing AbstractFilter
    public static class SelectedActionFilter extends RowFilters.GeneralFilter {
        
        private boolean matchSelected;

        public SelectedActionFilter(boolean matchSelected) {
            super();
            this.matchSelected = matchSelected;
        }


        @Override
        protected boolean include(
                Entry<? extends Object, ? extends Object> entry,
                int index) {
            // TODO Auto-generated method stub
            Object value = entry.getValue(0);
            if (value instanceof AbstractActionExt) {
                AbstractActionExt action =  (AbstractActionExt) value;
                return matchSelected ? action.isSelected() :
                    !action.isSelected();
            }
            return false;
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