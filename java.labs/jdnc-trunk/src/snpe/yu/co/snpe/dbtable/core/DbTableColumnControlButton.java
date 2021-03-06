
package yu.co.snpe.dbtable.core;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.action.ActionContainerFactory;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.TableColumnModelExt;

/**
 * This class is installed in the upper right corner of the table and is a
 * control which allows for toggling the visibilty of individual columns.
 * 
 * TODO: the table reference is a potential leak
 * 
 * TODO: no need to extend JButton - use non-visual controller returning
 * a JComponent instead.
 *
 * add snpe -  work with many columns
 * 
 */
public final class DbTableColumnControlButton extends JButton {

    /** exposed for testing. */
    //protected JPopupMenu popupMenu = null;
	private ScrollableMenu scrollableMenu = null;
    /** the table which is controlled by this. */
    private JXTable table;
    /** a marker to auto-recognize actions which should be added to the popup */
    public static final String COLUMN_CONTROL_MARKER = "column.";
    /** the list of actions for column menuitems.*/
    private List<ColumnVisibilityAction> columnVisibilityActions;

    public DbTableColumnControlButton(JXTable table, Icon icon) {
        super();
        init();
        setAction(createControlAction(icon));
        installTable(table);
    }

    public void updateUI() {
        super.updateUI();
        setMargin(new Insets(1, 2, 2, 1)); // Make this LAF-independent
        if (scrollableMenu != null) {
            // JW: Hmm, not really working....
            scrollableMenu.updateUI();
        }
    }

   
//-------------------------- Action in synch with column properties
    /**
     * A specialized action which takes care of keeping in synch with
     * TableColumn state.
     * 
     * NOTE: client must call releaseColumn if this action is no longer needed!
     * 
     */
    public class ColumnVisibilityAction extends AbstractActionExt {

        private TableColumn column;

        private PropertyChangeListener columnListener;

        public ColumnVisibilityAction(TableColumn column) {
            super((String) null);
            setStateAction();
            installColumn(column);
        }

        /**
         * 
         * release listening to column. Client must call this method if the
         * action is no longer needed. After calling it the action must not be
         * used any longer.
         */
        public void releaseColumn() {
            column.removePropertyChangeListener(columnListener);
            column = null;
        }

        public boolean isEnabled() {
            return super.isEnabled() && canControl();
        }

        private boolean canControl() {
            return (column instanceof TableColumnExt);
        }

        public void itemStateChanged(final ItemEvent e) {
            if (canControl()) {
                if ((e.getStateChange() == ItemEvent.DESELECTED)
                        //JW: guarding against 1 leads to #212-swingx: setting
                        // column visibility programatically fails if
                        // the current column is the second last visible
                        // guarding against 0 leads to hiding all columns
                        // by deselecting the menu item. 
                        // TODO further check Rob's basic idea to distinguish
                        // event sources instead of unconditionally reselect!
                        && (table.getColumnCount() <= 1)) {
                    reselect();
                } else {
                    ((TableColumnExt) column)
                            .setVisible(e.getStateChange() == ItemEvent.SELECTED);
                }
            }
        }

        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub

        }

        private void updateSelected() {
            boolean visible = true;
            if (canControl()) {
                visible = ((TableColumnExt) column).isVisible();
            }
            setSelected(visible);
        }

        private void reselect() {
            firePropertyChange("selected", null, Boolean.TRUE);
        }

        // -------------- init
        private void installColumn(TableColumn column) {
            this.column = column;
            column.addPropertyChangeListener(getColumnListener());
            setName(String.valueOf(column.getHeaderValue()));
            setActionCommand(column.getIdentifier());
            updateSelected();
        }

        private PropertyChangeListener getColumnListener() {
            if (columnListener == null) {
                columnListener = createPropertyChangeListener();
            }
            return columnListener;
        }

        private PropertyChangeListener createPropertyChangeListener() {
            PropertyChangeListener l = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if ("visible".equals(evt.getPropertyName())) {
                        updateSelected();
                    } else if ("headerValue".equals(evt.getPropertyName())) {
                        setName(String.valueOf(evt.getNewValue()));
                    }
                }

            };
            return l;
        }

    }

    /** 
     * open the popup. 
     * 
     * hmmm... really public?
     * 
     *
     */ 
    public void togglePopup() {
    	JPopupMenu popup = scrollableMenu.getPopupMenu();
        if (popup.isVisible()) {
            popup.setVisible(false);
        } else if (popup.getComponentCount() > 0) {
            Dimension buttonSize = getSize();
            int xPos = getComponentOrientation().isLeftToRight() ?
                    buttonSize.width - popup.getPreferredSize().width : 0;
             //popupMenu.show(this, xPos, buttonSize.height);
             popup.show(this, xPos, buttonSize.height);
        }
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
        scrollableMenu.applyComponentOrientation(o);
    }

//-------------------------- updates from table propertyChangelistnere
    
    /**
     * adjust internal state to after table's column model property has changed.
     * Handles cleanup of listeners to the old/new columnModel (listens to the
     * new only if we can control column visibility) and content of popupMenu.
     * 
     * @param oldModel
     */
    private void updateFromColumnModelChange(TableColumnModel oldModel) {
        if (oldModel != null) {
            oldModel.removeColumnModelListener(columnModelListener);
        }
        populatePopupMenu();
        if (canControl()) {
            table.getColumnModel().addColumnModelListener(columnModelListener);
        }
    }
    
    protected void updateFromTableEnabledChanged() {
        getAction().setEnabled(table.isEnabled());
        
    }
    /**
     * Method to check if we can control column visibility POST: if true we can
     * be sure to have an extended TableColumnModel
     * 
     * @return
     */
    private boolean canControl() {
        return table.getColumnModel() instanceof TableColumnModelExt;
    }
 
//  ------------------------ updating the popupMenu
    /**
     * Populates the popup menu with actions related to controlling columns.
     * Adds an action for each columns in the table (including both visible and
     * hidden). Adds all column-related actions from the table's actionMap.
     */
    protected void populatePopupMenu() {
        clearPopupMenu();
        if (canControl()) {
            addColumnMenuItems();
        }
        addColumnActions();
    }

    /**
     * 
     * removes all components from the popup, making sure to release all
     * columnVisibility actions.
     * 
     */
    private void clearPopupMenu() {
        clearColumnVisibilityActions();
        scrollableMenu.removeAll();
    }

    /**
     * release actions and clear list of actions.
     * 
     */
    private void clearColumnVisibilityActions() {
        if (columnVisibilityActions == null)
            return;
        for (Iterator<ColumnVisibilityAction> iter = columnVisibilityActions
                .iterator(); iter.hasNext();) {
            iter.next().releaseColumn();

        }
        columnVisibilityActions.clear();
    }

   
    /**
     * adds a action to toggle column visibility for every column currently in
     * the table. This includes both visible and invisible columns.
     * 
     * pre: canControl()
     * 
     */
    private void addColumnMenuItems() {
        // For each column in the view, add a JCheckBoxMenuItem to popup
        List columns = table.getColumns(true);
        ActionContainerFactory factory = new ActionContainerFactory(null);
        for (Iterator iter = columns.iterator(); iter.hasNext();) {
            TableColumn column = (TableColumn) iter.next();
            ColumnVisibilityAction action = new ColumnVisibilityAction(column);
            getColumnVisibilityActions().add(action);
            scrollableMenu.add(factory.createMenuItem(action));
        }

    }

    private List<ColumnVisibilityAction> getColumnVisibilityActions() {
        if (columnVisibilityActions == null) {
            columnVisibilityActions = new ArrayList<ColumnVisibilityAction>();
        }
        return columnVisibilityActions;
    }

    /**
     * add additional actions from the xtable's actionMap.
     * 
     */
    private void addColumnActions() {
        Object[] actionKeys = getColumnControlActionKeys();
        Arrays.sort(actionKeys);
        List actions = new ArrayList();
        for (int i = 0; i < actionKeys.length; i++) {
            if (isColumnControlActionKey(actionKeys[i])) {
                actions.add(table.getActionMap().get(actionKeys[i]));
            }
        }
        if (actions.size() == 0)
            return;
        if (canControl()) {
            scrollableMenu.addSeparator();
        }
        ActionContainerFactory factory = new ActionContainerFactory(null);
        for (Iterator iter = actions.iterator(); iter.hasNext();) {
            Action action = (Action) iter.next();
            scrollableMenu.add(factory.createMenuItem(action));
        }
    }

    /**
     * @return
     */
    private Object[] getColumnControlActionKeys() {
        Object[] allKeys = table.getActionMap().allKeys();
        List columnKeys = new ArrayList();
        for (int i = 0; i < allKeys.length; i++) {
            if (isColumnControlActionKey(allKeys[i])) {
                columnKeys.add(allKeys[i]);
            }
        }
        return columnKeys.toArray();
    }

    private boolean isColumnControlActionKey(Object actionKey) {
        return (actionKey instanceof String) && ((String) actionKey).startsWith(COLUMN_CONTROL_MARKER);
    }


    //--------------------------- init
    private void installTable(JXTable table) {
        this.table = table;
        table.addPropertyChangeListener(columnModelChangeListener);
        updateFromColumnModelChange(null);
        updateFromTableEnabledChanged();
    }


    /**
     * Initialize the column control button's gui
     */
    private void init() {
        setFocusPainted(false);
        setFocusable(false);
        // create the popup menu
        scrollableMenu = new ScrollableMenu();
        // this is a trick to get hold of the client prop which
        // prevents closing of the popup
        JComboBox box = new JComboBox();
        Object preventHide = box.getClientProperty("doNotCancelPopup");
        putClientProperty("doNotCancelPopup", preventHide);
    }


    /** 
     * the action created for this.
     * 
     * @param icon
     * @return
     */
    private Action createControlAction(Icon icon) {
        Action control = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                togglePopup();
            }

        };
        control.putValue(Action.SMALL_ICON, icon);
        return control;
    }
    
    // -------------------------------- listeners

    private PropertyChangeListener columnModelChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            if ("columnModel".equals(evt.getPropertyName())) {
                updateFromColumnModelChange((TableColumnModel) evt.getOldValue());
            } else if ("enabled".equals(evt.getPropertyName())) {
                updateFromTableEnabledChanged();
            }
        }
    };

    private TableColumnModelListener columnModelListener = new TableColumnModelListener() {
        /** Tells listeners that a column was added to the model. */
        public void columnAdded(TableColumnModelEvent e) {
            // quickfix for #192
            if (!isVisibilityChange(e, true)) {
                populatePopupMenu();
            }
        }

        /** Tells listeners that a column was removed from the model. */
        public void columnRemoved(TableColumnModelEvent e) {
            if (!isVisibilityChange(e, false)) {
                populatePopupMenu();
            }
        }

        /**
         * check if the add/remove event is triggered by a move to/from the
         * invisible columns.
         * 
         * PRE: the event must be received in columnAdded/Removed.
         * 
         * @param e
         *            the received event
         * @param added
         *            if true the event is assumed to be received via
         *            columnAdded, otherwise via columnRemoved.
         * @return
         */
        private boolean isVisibilityChange(TableColumnModelEvent e,
                boolean added) {
            // can't tell
            if (!(e.getSource() instanceof DefaultTableColumnModelExt))
                return false;
            DefaultTableColumnModelExt model = (DefaultTableColumnModelExt) e
                    .getSource();
            if (added) {
                return model.isAddedFromInvisibleEvent(e.getToIndex());
            } else {
                return model.isRemovedToInvisibleEvent(e.getFromIndex());
            }
        }

        /** Tells listeners that a column was repositioned. */
        public void columnMoved(TableColumnModelEvent e) {
        }

        /** Tells listeners that a column was moved due to a margin change. */
        public void columnMarginChanged(ChangeEvent e) {
        }

        /**
         * Tells listeners that the selection model of the TableColumnModel
         * changed.
         */
        public void columnSelectionChanged(ListSelectionEvent e) {
        }
    };

}

