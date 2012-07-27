/*
 * $Id: JXInspectionPane.java 2636 2008-08-06 09:29:51Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jdesktop.swingx.inspection.DefaultInspectionValueRenderer;
import org.jdesktop.swingx.inspection.DetailInspector;
import org.jdesktop.swingx.inspection.Details;
import org.jdesktop.swingx.inspection.InspectionValueRenderer;
import org.jdesktop.swingx.plaf.JXInspectionPaneAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

/**
 * Defines a simple component that displays the details of a set of items.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @todo Add orientation support
 * @todo Add editable support
 */
public class JXInspectionPane extends JXComponent {
    private static final String uiClassID = "InspectionPaneUI";
    static {
        LookAndFeelAddons.contribute(new JXInspectionPaneAddon());
    }





    /**
     * Defines the orientation for this inspection panel.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    public static enum Orientation {
        /**
         * Horizontal orientation, this will generally be wider than taller.
         */
        HORIZONTAL,
        /**
         * Vertical orientation, this will generally be taller than wider.
         */
        VERTICAL
    }







    // CACHE - The following variables are for caching and performance purposes.
    private transient Details detailsCache = null;
    // END CACHE

    private ModelAdapter modelAdapter; // binds the models to this component for event forwarding

    private ListModel dataModel; // contains the items to inspect
    private DetailInspector inspector; // creates the details for the items
    private Orientation orientation; // landscape or portrait.
    private InspectionValueRenderer valueRenderer; // renders detail values.

    /**
     * Creates a new inspection pane with a default inspector.
     */
    public JXInspectionPane() {
        super();
        inspector = null; // new DefaultDetailInspector();
        dataModel = new DefaultListModel();
        orientation = Orientation.HORIZONTAL;
        valueRenderer = new DefaultInspectionValueRenderer();

        updateUI();
    }





    /**
     * Creates a new inspection pane with the given data model.
     *
     * @param dataModel The model of items to inspect.
     */
    public JXInspectionPane(ListModel dataModel) {
        this();
        setDataModel(dataModel);
    }





    /**
     * Create a new inspection pane using the given inspector.
     *
     * @param inspector The inspector to use to inspect the items.
     */
    public JXInspectionPane(DetailInspector inspector) {
        this();
        setInspector(inspector);
    }





    /**
     * @return {@code "InspectionPaneUI"}.
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }





    /**
     * Set the renderer to use to render the values of the details for this inspector. If set to null the default
     * renderer will be created and used.
     *
     * @param valueRenderer The renderer for the detail values.
     */
    public void setValueRenderer(InspectionValueRenderer valueRenderer) {
        if (valueRenderer == null) {
            valueRenderer = new DefaultInspectionValueRenderer();
        }
        InspectionValueRenderer old = getValueRenderer();
        this.valueRenderer = valueRenderer;
        firePropertyChange("valueRenderer", old, getValueRenderer());
    }





    /**
     * Get the renderer for the detail values of this inspector.
     *
     * @return The renderer for detail values.
     */
    public InspectionValueRenderer getValueRenderer() {
        return valueRenderer;
    }





    /**
     * Get the orientation for the layout of the component. This is not null.
     *
     * @return The orientation.
     */
    public Orientation getOrientation() {
        return orientation;
    }





    /**
     * Set the orientation of the layout of this component.
     *
     * @param orientation The orientation for the component.
     * @throws NullPointerException if the given value is null.
     */
    public void setOrientation(Orientation orientation) {
        if (orientation == null) {
            throw new NullPointerException("orientation cannot be null");
        }
        Orientation old = getOrientation();
        this.orientation = orientation;
        firePropertyChange("orientation", old, getOrientation());
    }





    /**
     * Gets the details to display as part of this inspection. By default this will cache the details.
     *
     * @return The details to display.
     */
    public Details getDetails() {
        Details result;
        if (detailsCache == null) {
            DetailInspector inspector = getInspector();
            detailsCache = inspector == null ? null : createDetails(inspector);
        }
        result = detailsCache;
        return result;
    }





    /**
     * Get the inspector to use to create the details for this component. This will never return null.
     *
     * @return The inspector.
     * @see #setInspector
     */
    public DetailInspector getInspector() {
        return inspector;
    }





    /**
     * Set the inspector to use to create details for this component. If null is given then the default DetailInspector
     * will be created and used.
     *
     * @param inspector The inspector to use to create the Details.
     * @see #getInspector()
     */
    public void setInspector(DetailInspector inspector) {
        if (inspector == null) {
            inspector = null; // new DefaultDetailInspector();
        }

        DetailInspector old = getInspector();
        this.inspector = inspector;

        invalidateDetails();

        firePropertyChange("inspector", old, getInspector());
    }





    /**
     * Called to create the details for this component. The returned Details object should not be null.
     *
     * @param inspector The inspector to use to create the Details. This will not be null.
     * @return The details to represent the dataModel.
     */
    protected Details createDetails(DetailInspector inspector) {
        ListModel dataModel = getDataModel();
        Object[] items = new Object[dataModel.getSize()];
        for (int i = 0, n = items.length; i < n; i++) {
            items[i] = dataModel.getElementAt(i);
        }

        return inspector.inspect(items);
    }





    /**
     * Invalidates any cached details objects. Call this if the details should change but the dataModel has not changed.
     */
    public void invalidateDetails() {
        detailsCache = null;
    }





    /**
     * Get the model containing the items this inspection pane inspects. This will never be null.
     *
     * @return The model containing the inspected items.
     */
    public ListModel getDataModel() {
        return dataModel;
    }





    /**
     * Set the model used to contain the items to be inspected. this method does not support null values.
     *
     * @param dataModel The model to use to contain the items to inspect.
     */
    public void setDataModel(ListModel dataModel) {
        if (dataModel == null) {
            throw new NullPointerException("dataModel cannot be null");
        }
        ListModel old = getDataModel();
        if (modelAdapter != null) {
            old.removeListDataListener(modelAdapter);
        }
        this.dataModel = dataModel;
        dataModel = getDataModel();
        if (dataModel != null) {
            if (modelAdapter == null) {
                modelAdapter = new ModelAdapter();
            }

            dataModel.addListDataListener(modelAdapter);
        }
        if (dataModel != old) {
            invalidateDetails();
        }
        firePropertyChange("dataModel", old, dataModel);
    }





    /**
     * Add a listener to be notified when the inspected items change.
     *
     * @param l The listener.
     */
    public void addListDataListener(ListDataListener l) {
        listenerList.add(ListDataListener.class, l);
    }





    /**
     * Remove a listener added via {@link #addListDataListener}
     *
     * @param l The listener.
     */
    public void removeListDataListener(ListDataListener l) {
        listenerList.remove(ListDataListener.class, l);
    }





    /**
     * Get the listeners added via {@link #addListDataListener}. The result is guaranteed to be non-null.
     *
     * @return The list of listeners.
     */
    public ListDataListener[] getListDataListeners() {
        return listenerList.getListeners(ListDataListener.class);
    }





    protected void fireIntervalAdded(Object source, int index0, int index1) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent event = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalAdded(event);
            }
        }
    }





    protected void fireIntervalRemoved(Object source, int index0, int index1) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent event = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalRemoved(event);
            }
        }
    }





    protected void fireContentsChanged(Object source, int index0, int index1) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent event = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).contentsChanged(event);
            }
        }
    }





    /**
     * Binds the containing controllers models to the container. This forwards events fired so that observers need not
     * worry about model changes.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     * @version 1.0
     */
    private final class ModelAdapter implements ListDataListener {
        public void intervalAdded(ListDataEvent e) {
            invalidateDetails();
            fireIntervalAdded(JXInspectionPane.this, e.getIndex0(), e.getIndex1());
        }





        public void intervalRemoved(ListDataEvent e) {
            invalidateDetails();
            fireIntervalRemoved(JXInspectionPane.this, e.getIndex0(), e.getIndex1());
        }





        public void contentsChanged(ListDataEvent e) {
            invalidateDetails();
            fireContentsChanged(JXInspectionPane.this, e.getIndex0(), e.getIndex1());
        }

    }
}
