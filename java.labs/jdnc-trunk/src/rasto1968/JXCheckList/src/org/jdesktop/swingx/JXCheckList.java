/*
 * $Id: JXCheckList.java 3285 2010-03-24 09:26:02Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Provides a list control containing check boxes. This is based on the code
 * provided in Swing Hacks (#15) by Joshua Marinacci, but I have tweaked it to
 * work with JXList and to get rid of some visual annoyances (the original version
 * causes all of the check boxes to flicker each time a check box is clicked).
 * Also includes ideas contributed by the swingx user dags (Diego).
 *
 * @author Rob Stone
 */
public class JXCheckList extends JXList implements MouseListener, ActionListener, CheckListListener {
    protected static final Color listForeground;
    protected static final Color listBackground;
    protected static final Color listSelectedForeground;
    protected static final Color listSelectedBackground;

    protected CheckListModel model;
    protected Integer hotSpotWidth;
    protected boolean singleClickCheck;
    protected List<CheckListListener> listeners=new ArrayList<CheckListListener>();

    static {
        final UIDefaults uid=UIManager.getLookAndFeel().getDefaults();
        listForeground=uid.getColor("List.foreground");
        listBackground=uid.getColor("List.background");
        listSelectedForeground=uid.getColor("List.selectionForeground");
        listSelectedBackground=uid.getColor("List.selectionBackground");
    }

    /**
     * A renderer that provides the check box functionality. Note that this
     * class is capable of co-existing with other custom list cell renderers.
     */
    protected class CheckBoxListCellRenderer extends JComponent implements ListCellRenderer {
        private final ListCellRenderer wrappedRenderer;
        private final JCheckBox checkBox=new JCheckBox();

        public CheckBoxListCellRenderer(final JList list, final ListCellRenderer wrappedRenderer) {
            this.wrappedRenderer=wrappedRenderer;
            this.setLayout(new BorderLayout());

            if (wrappedRenderer==null) {
                add(checkBox, BorderLayout.CENTER);
            } else {
                add(checkBox, BorderLayout.WEST);
                add(wrappedRenderer.getListCellRendererComponent(list, null, 0, false, false), BorderLayout.CENTER);
            }
        }

        /**
         * Returns the renderer
         */
        public ListCellRenderer getListCellRenderer() {
            return wrappedRenderer==null ? this : wrappedRenderer;
        }

        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            if (wrappedRenderer==null) {
                checkBox.setText(value.toString());
            } else {
                wrappedRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
            checkBox.setEnabled(JXCheckList.this.isEnabled());
            checkBox.setSelected(model.isChecked(index));
            checkBox.setForeground(isSelected ? listSelectedForeground : listForeground);
            checkBox.setBackground(isSelected ? listSelectedBackground : listBackground);

            return this;
        }

        // Needed for highlighters.
        @Override
        public void setBackground(Color bg) {
            for (Component component : getComponents()) {
                component.setBackground(bg);
            }
        }

        // Needed for highlighters.
        @Override
        public void setForeground(Color fg) {
            for (Component component : getComponents()) {
                component.setForeground(fg);
            }
        }
    }

    /**
     * A class that wraps standard list models to provide the extra functionality
     * required for check boxes.
     */
    protected class WrappedListModel extends DefaultListModel implements ListDataListener, CheckListModel {
        protected ListModel wrappedModel;
        protected List<Boolean> checkedElements;
        protected EventListenerList listenerList=new EventListenerList();

        WrappedListModel(final ListModel wrappedModel) {
            super();
            this.wrappedModel=wrappedModel;
            wrappedModel.addListDataListener(this);
            checkedElements=new ArrayList<Boolean>(wrappedModel.getSize());
            for (int index=0; index<wrappedModel.getSize(); index++) {
                checkedElements.add(Boolean.FALSE);
            }
        }

        /**
         * Indicates whether or not an element in the list is checked.
         * @param index the element to check
         * @return <code>true</code>=checked, <code>false</code>=not checked
         */
        public boolean isChecked(final int index) {
            return checkedElements.get(index);
        }

        /**
         * @return an array of booleans indicating which elements are checked.
         */
        public boolean[] getChecked() {
            final boolean[] checked=new boolean[checkedElements.size()];
            for (int index=0; index<checked.length; index++) {
                checked[index]=isChecked(index);
            }
            return checked;
        }

        /**
         * Sets the checked state of the corresponding list element.
         * @param index the element to set
         * @param checked <code>true</code>=checked, <code>false</code>=not checked
         */
        public void setChecked(final int index, final boolean checked) {
            setChecked(index, index, checked);
        }

        /**
         * Sets the checked state of all the list elements.
         * @param checked <code>true</code>=checked, <code>false</code>=not checked
         */
        public void setChecked(final boolean checked) {
            setChecked(0, checkedElements.size(), checked);
        }

        /**
         * Sets the checked state of the specified list elements.
         * @param start the first element to set
         * @param end the last element to set
         * @param checked <code>true</code>=checked, <code>false</code>=not checked
         */
        public void setChecked(final int start, final int end, final boolean checked) {
            if (start<=end && start>=0) {
                for (int index=start; index<=end; index++) {
                    checkedElements.set(index, checked);
                }
                fireCheckListChanged(start, end);
            }
        }

        /**
         * Inverts the state of all the elements in the list.
         */
        public void invert() {
            for (int index=0; index<checkedElements.size(); index++) {
                checkedElements.set(index, !checkedElements.get(index));
            }
            fireCheckListChanged(0, checkedElements.size());
        }

        /**
         * Add a listener
         */
        public void addCheckListListener(final CheckListListener l) {
            listenerList.add(CheckListListener.class, l);
        }

        /**
         * Remove a listener
         */
        public void removeCheckListListener(final CheckListListener l) {
            listenerList.remove(CheckListListener.class, l);
        }

        @Override
        public int getSize() {
            return wrappedModel.getSize();
        }

        @Override
        public Object getElementAt(int index) {
            return wrappedModel.getElementAt(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            wrappedModel.addListDataListener(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            wrappedModel.removeListDataListener(l);
        }

        /**
         * The wrapped model has changed, update our checked list to match.
         */
        public void intervalAdded(ListDataEvent e) {
            while (checkedElements.size()<wrappedModel.getSize()) {
                checkedElements.add(e.getIndex0(), false);
            }
        }

        /**
         * The wrapped model has changed, update our checked list to match.
         */
        public void intervalRemoved(ListDataEvent e) {
            while (checkedElements.size()>wrappedModel.getSize()) {
                checkedElements.remove(e.getIndex0());
            }
        }

        public void contentsChanged(ListDataEvent e) {
            // Do nothing - I think !
        }

        /**
         * Let our listeners know that the check list has changed
         */
        protected void fireCheckListChanged(final int start, final int end) {
            for (CheckListListener listener : listenerList.getListeners(CheckListListener.class)) {
                listener.checkListChanged(JXCheckList.this, start, end);
            }
        }

        /**
         * @return the wrapped list model
         */
        protected ListModel getWrappedModel()
        {
            return wrappedModel;
        }
    }

    /**
     * Initialise the JXCheckList
     */
    protected void initialise()
    {
        // Create and assign the custom renderer
        super.setCellRenderer(new CheckBoxListCellRenderer(this, null));

        // Add a mouse listener - we use this for detecting checkbox clicks
        addMouseListener(this);
        
        // Add a check list listener - we use this to trigger repains
        addCheckListListener(this);

        // Catch the space key being pressed - we use this to toggle checkboxes
        registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JComponent.WHEN_FOCUSED);
    }

    /**
     * Constructs a <code>JXList</code> with an empty model and filters disabled.
     */
    public JXCheckList() {
        this(false);
    }

    /**
     * Constructs a <code>JXList</code> that displays the elements in the
     * specified, non-<code>null</code> model and filters disabled.
     *
     * @param dataModel   the data model for this list
     * @exception IllegalArgumentException   if <code>dataModel</code>
     *                                           is <code>null</code>
     */
    public JXCheckList(ListModel dataModel) {
        this(dataModel, false);
    }

    /**
     * Constructs a <code>JXList</code> that displays the elements in
     * the specified array and filters disabled.
     *
     * @param  listData  the array of Objects to be loaded into the data model
     * @throws IllegalArgumentException   if <code>listData</code>
     *                                          is <code>null</code>
     */
    public JXCheckList(Object[] listData) {
        this(listData, false);
    }

    /**
     * Constructs a <code>JXList</code> that displays the elements in
     * the specified <code>Vector</code> and filtes disabled.
     *
     * @param  listData  the <code>Vector</code> to be loaded into the
     *          data model
     * @throws IllegalArgumentException   if <code>listData</code>
     *                                          is <code>null</code>
     */
    public JXCheckList(Vector<?> listData) {
        this(listData, false);
    }


    /**
     * Constructs a <code>JXList</code> with an empty model and
     * filterEnabled property.
     *
     * @param filterEnabled <code>boolean</code> to determine if
     *  filtering/sorting is enabled
     */
    public JXCheckList(boolean filterEnabled) {
        super(filterEnabled);
        initialise();
    }

    /**
     * Constructs a <code>JXList</code> with the specified model and
     * filterEnabled property.
     *
     * @param dataModel   the data model for this list
     * @param filterEnabled <code>boolean</code> to determine if
     *          filtering/sorting is enabled
     * @throws IllegalArgumentException   if <code>dataModel</code>
     *                                          is <code>null</code>
     */
    public JXCheckList(ListModel dataModel, boolean filterEnabled) {
        super(dataModel, filterEnabled);
        initialise();
    }

    /**
     * Constructs a <code>JXList</code> that displays the elements in
     * the specified array and filterEnabled property.
     *
     * @param  listData  the array of Objects to be loaded into the data model
     * @param filterEnabled <code>boolean</code> to determine if filtering/sorting
     *   is enabled
     * @throws IllegalArgumentException   if <code>listData</code>
     *                                          is <code>null</code>
     */
    public JXCheckList(Object[] listData, boolean filterEnabled) {
        super(listData, filterEnabled);
        initialise();
    }

    /**
     * Constructs a <code>JXList</code> that displays the elements in
     * the specified <code>Vector</code> and filtersEnabled property.
     *
     * @param  listData  the <code>Vector</code> to be loaded into the
     *          data model
     * @param filterEnabled <code>boolean</code> to determine if filtering/sorting
     *   is enabled
     * @throws IllegalArgumentException if <code>listData</code> is <code>null</code>
     */
    public JXCheckList(Vector<?> listData, boolean filterEnabled) {
        super(listData, filterEnabled);
        initialise();
    }

    @Override
    public final void setModel(final ListModel model) {
        if (this.model!=null && listeners!=null) {
            for (CheckListListener listener : listeners) {
                this.model.removeCheckListListener(listener);
            }
        }
        if (model instanceof CheckListModel) {
            this.model=(CheckListModel)model;
        } else {
            this.model=new WrappedListModel(model);
        }
        if (listeners!=null) {            
            for (CheckListListener listener : listeners) {
                this.model.addCheckListListener(listener);
            }
        }
        super.setModel(this.model);
    }

    @Override
    public final ListModel getModel() {
        if (model==null) {
            // JList doesn't call its setModel method from its
            // constructor, it sets the field. Because of this we can't
            // rely on our own setModel having been called...
            setModel(super.getModel());
        }

        return model instanceof WrappedListModel ?
            ((WrappedListModel)model).getWrappedModel() : model;
    }

    @Override
    public final void setCellRenderer(ListCellRenderer renderer) {
        super.setCellRenderer(new CheckBoxListCellRenderer(this, renderer));
    }

    /**
     * Indicates whether or not an element in the list is checked.
     * @param index the element to check
     * @return <code>true</code>=checked, <code>false</code>=not checked
     */
    public boolean isChecked(final int index) {
        return model.isChecked(index);
    }

    /**
     * @return an array of booleans indicating which elements are checked.
     */
    public boolean[] getChecked() {
        return model.getChecked();
    }

    /**
     * Sets the checked state of the specified list elements.
     * @param start the first element to set
     * @param end the last element to set
     * @param checked <code>true</code>=checked, <code>false</code>=not checked
     */
    public void setChecked(final int start, final int end, final boolean checked) {
        model.setChecked(start, end, checked);
    }

    /**
     * A convenience method that sets the specified entries checked state.
     * @param index the entry to set
     * @param checked <code>true</code>=checked, <code>false</code>=not checked
     */
    public void setChecked(final int index, final boolean checked) {
        setChecked(index, index, checked);
    }

    /**
     * A convenience method that sets all of the entries to the specified state.
     * @param checked <code>true</code>=checked, <code>false</code>=not checked
     */
    public void setChecked(final boolean checked) {
        setChecked(0, model.getSize()-1, checked);
    }

    /**
     * A convenience method that inverts the checked state of the entries.
     */
    public void invert() {
        for (int index=0, size=model.getSize(); index<size; index++) {
            setChecked(index, !isChecked(index));
        }
    }

    /**
     * Add a listener
     * @param listener the listener to add
     */
    public void addCheckListListener(final CheckListListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        model.addCheckListListener(listener);
    }

    /**
     * Remove a listener
     * @param listener the listener to remove
     */
    public void removeCheckListListener(final CheckListListener listener) {
        listeners.remove(listener);
        model.removeCheckListListener(listener);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    /**
     * Determine if a checkbox has been clicked, if it has then toggle its
     * state. Use mouseRelease rather than mouseClicked, because sometimes if
     * the mouse is moved slightly just after clicking then the logic fails and
     * the checkbox state doesn't change.
     * @param e the mouse event
     */
    public void mouseReleased(MouseEvent e) {
        if (e.getButton()==MouseEvent.BUTTON1 && isEnabled()) {
            // get the index of cList from clicked point
            final int index=locationToIndex(e.getPoint());

            // no index ? , then return
            if (index < 0) return;

            if (!singleClickCheck) {
                // get the preferred width of a JCheckBox
                if (hotSpotWidth==null) {
                    hotSpotWidth=new Integer(new JCheckBox().getPreferredSize().width);
                }

                // if mouse click's X position falls outside JCheckBox "hotspot", return
                // becuase mouse was not clicked "inside" JCheckbox
                if (e.getX() > getCellBounds(index, index).x + hotSpotWidth) {
                    return;
                }
            }

            model.setChecked(index, index, !model.isChecked(index));
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
     * Called if the 'space' key is pressed on a checkbox. Toggle its state.
     * @param e the event
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this) && isEnabled()) {
            final int selectedIndices[]=getSelectedIndices();
            for (int index : selectedIndices) {
                model.setChecked(index, index, !model.isChecked(index));
            }
        }
    }

    /**
     * The check list has changed, repaint.
     * @param list the list
     * @param start the first item that has changed
     * @param end the last item that has changed
     */
    public void checkListChanged(final JXCheckList list, final int start, final int end) {
        repaint(getCellBounds(start, end));
    }

    /**
     * @return <code>true</code> if the check list is in single click mode, <code>false</code> if not.
     */
    public boolean isSingleClickCheck()
    {
        return singleClickCheck;
    }

    /**
     * Enable/Disable single click mode. If enabled then a single click will
     * both select and check a list item.
     * @param singleClickCheck <code>true</code> enable single click mode, <code>false</code> disable single click mode.
     */
    public void setSingleClickCheck(final boolean singleClickCheck)
    {
        this.singleClickCheck = singleClickCheck;
    }
}
