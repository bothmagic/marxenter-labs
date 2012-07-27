/*
 * $Id: JXSlider.java 1767 2007-09-24 21:05:09Z osbald $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import org.jdesktop.swingx.event.MarkerChangeEvent;
import org.jdesktop.swingx.event.SliderChangeEvent;
import org.jdesktop.swingx.event.SliderChangeListener;
import org.jdesktop.swingx.plaf.JXSliderAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.XSliderUI;
import org.jdesktop.swingx.slider.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Defines an extension to the standard JSlider. This extension adds support for an 'infinate' range, markers which can
 * be placed on the slider track and an adjustable view of the data. Due to the inherent complexity of allowing an
 * extreemly large number of markers within the model there is added abstractation around the mutation of these values.
 * The MarkerMutator class is responsible for managing the editable state and mutation of values within MarkerGroups. As
 * there can be many values and MarkerGroups as part of the same JXSlider model a selection/focus mechanism is present
 * to determin which MarkerGroup and value is currently in focus or active for user modification.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see SliderModel
 * @see MarkerMutator
 * @see MarkerSelectionModel
 * @see SliderValueRenderer
 * @see SliderTrackRenderer
 * @see Projection
 */

//NB RJO - Simply a tidied up version of matts original without the timingframework imports

public class JXSlider extends JXComponent {

    /**
     * Defines the orientation for a slider.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    public static enum Orientation {
        /**
         * A horizontal orientation for the slider.
         */
        HORIZONTAL,
        /**
         * A vertical orientation for the slider.
         */
        VERTICAL
    }

    /**
     * The components UI class ID.
     */
    private static final String uiClassId = "XSliderUI";

    static {
        LookAndFeelAddons.contribute(new JXSliderAddon());
    }

    /**
     * Single change event as the only property is the source.
     */
    private ChangeEvent event = null;

    /**
     * Forwards events formt he model to observers of this class.
     */
    private ModelAdapter modelAdapter;

    /**
     * The sliders model.
     */
    private SliderModel model;

    /**
     * The class responsible for mutating the model markers.
     */
    private MarkerMutator mutator;

    /**
     * Defines the model used to determin the selected marker value.
     */
    private MarkerSelectionModel markerSelectionModel;

    /**
     * The renderer for the sliders values.
     */
    private SliderValueRenderer valueRenderer;

    /**
     * The renderer for the sliders track.
     */
    private SliderTrackRenderer trackRenderer;

    /**
     * The model projection.
     */
    private Projection projection;

    /**
     * The orientation of the slider.
     */
    private Orientation orientation;

    /**
     * Simple editable property to allow global editable state.
     */
    private boolean editable = true;


    /**
     * Create a new JXSlider with a default model.
     */
    public JXSlider() {
        this(0, 100, 50);
    }

    /**
     * Create a new JXSlider with the given minimum, maximum and value properties. The value is represented as a
     * ValueMarkerGroup which is then added to this model.
     *
     * @param minimum The minimum vlaue for this model.
     * @param maximum The maximum value for this model.
     * @param value   The value for a single ValueMarkerGroup instance.
     * @see ValueMarkerGroup
     */
    public JXSlider(long minimum, long maximum, long value) {
        this(new DefaultSliderModel(minimum, maximum, value));
    }


    /**
     * Create a new JXSlider with the given minimum, maximum and value properties. The value is represented as a
     * ValueMarkerGroup which is then added to this model.
     *
     * @param minimum     The minimum vlaue for this model.
     * @param maximum     The maximum value for this model.
     * @param value       The value for a single ValueMarkerGroup instance.
     * @param orientation The orientation for the slider.
     * @see ValueMarkerGroup
     */
    public JXSlider(long minimum, long maximum, long value, Orientation orientation) {
        this(new DefaultSliderModel(minimum, maximum, value), orientation);
    }


    /**
     * Create a new HORIZONTAL JXSlider with the given model. If null a default model will be created.
     *
     * @param model The model for this slider.
     */
    public JXSlider(SliderModel model) {
        this(model, Orientation.HORIZONTAL);
    }


    /**
     * Create a new JXSlider with the given model and orientation.
     *
     * @param model       The model for this slider.
     * @param orientation The orientation of the slider. If null HORIZONTAL will be used.
     */
    public JXSlider(SliderModel model, Orientation orientation) {
        super();
        setModel(model);
        if (orientation == null) {
            orientation = Orientation.HORIZONTAL;
        }
        if (getMarkerGroupCount() > 0) {
            MarkerGroup g = getMarkerGroup(0);
            MarkerRange r = g.getMarkers(getMinimum(), getMaximum());
            long size = r.getSize();
            if (size > 0) {
                setMarkerSelectionModel(new DefaultMarkerSelectionModel(getMarkerGroup(0), r.get(0)));
            } else {
                setMarkerSelectionModel(new DefaultMarkerSelectionModel(getMarkerGroup(0)));
            }
        } else {
            setMarkerSelectionModel(new DefaultMarkerSelectionModel());
        }
        setMutator(new DefaultMarkerMutator());
        setOrientation(orientation);
        this.projection = FullProjection.getInstance();
        updateUI();
    }


    /**
     * Get the model representing this class. This will never return null.
     *
     * @return The model for this class.
     */
    public SliderModel getModel() {
        return model;
    }


    /**
     * Set the model for this class. If null is passed then a new default model will be created and set.
     *
     * @param model The model containing this classes information.
     */
    public void setModel(SliderModel model) {
        if (model == null) {
            model = new DefaultSliderModel();
        }
        SliderModel old = getModel();
        if (old != null) {
            old.removeSliderChangeListener(modelAdapter);
        }

        this.model = model;
        model = getModel();

        if (modelAdapter == null) {
            modelAdapter = new ModelAdapter();
        }
        model.addSliderChangeListener(modelAdapter);
        firePropertyChange("model", old, model);
    }


    /**
     * Get the minimum vlaue for the model.
     *
     * @return The models minimum value.
     */
    public long getMinimum() {
        return getModel().getMinimum();
    }


    /**
     * Get the maximum value from the model of this component.
     *
     * @return The models maximum.
     */
    public long getMaximum() {
        return getModel().getMaximum();
    }


    /**
     * Get the number of MarkerGroups set on the model.
     *
     * @return The models Group count.
     */
    public int getMarkerGroupCount() {
        return getModel().getMarkerGroupCount();
    }


    /**
     * Get the MarkerGroup from the model at the given index.
     *
     * @param index the index of the group to get.
     * @return The requested group from the model.
     */
    public MarkerGroup getMarkerGroup(int index) {
        return getModel().getMarkerGroup(index);
    }


    /**
     * get the selected value for the marker group. This will throw an IllegalStateException if no value is selected.
     *
     * @return The selected marker value.
     */
    public long getMarkerSelectedValue() {
        if (getMarkerSelectionModel() == null) {
            throw new IllegalStateException("cannot get the selected value if none exists");
        }
        return getMarkerSelectionModel().getSelectedValue();
    }


    /**
     * Get whether a value is selected in the marker group.
     *
     * @return {@code true} if a value is selected.
     */
    public boolean isMarkerValueSelected() {
        return getMarkerSelectionModel() != null && getMarkerSelectionModel().isValueSelected();
    }


    /**
     * Gets the active group for this model. This generally delegates to the selection model.
     *
     * @return The group that is active.
     */
    public MarkerGroup getMarkerSelectedGroup() {
        return getMarkerSelectionModel() == null ? null : getMarkerSelectionModel().getSelectedGroup();
    }


    /**
     * Returns the renderer used to represent the values of the model. This may return null values.
     *
     * @return The renderer used to display the models values.
     */
    public SliderValueRenderer getValueRenderer() {
        return valueRenderer;
    }


    /**
     * Set the renderer used to represent the values of the model. If set to null it is unlikely that values will be
     * renderered to the screen but this may be handled specifically by the look and feels ui delegate for this class.
     *
     * @param valueRenderer The value renderer.
     */
    public void setValueRenderer(SliderValueRenderer valueRenderer) {
        SliderValueRenderer old = getValueRenderer();
        this.valueRenderer = valueRenderer;
        firePropertyChange("valueRenderer", old, getValueRenderer());
    }


    /**
     * Get the orientation for this JXSlider. This will never return null.
     *
     * @return The slider orientation.
     */
    public Orientation getOrientation() {
        return orientation;
    }


    /**
     * Set the orientation for this JXSlider. If set to null HORIZONTAL will be used.
     *
     * @param orientation The orientation for the slider.
     */
    public void setOrientation(Orientation orientation) {
        if (orientation == null) {
            orientation = Orientation.HORIZONTAL;
        }
        Orientation old = getOrientation();
        this.orientation = orientation;
        firePropertyChange("orientation", old, getOrientation());
    }


    /**
     * Get the projection of the model for display.
     *
     * @return The model projection.
     */
    public Projection getProjection() {
        return projection;
    }


    /**
     * Set the Projection to use to refine the displayable values of the model. If set to null a FullProjection instance
     * is used.
     *
     * @param projection The projection to use to restrict the model.
     */
    public void setProjection(Projection projection) {
        if (projection == null) {
            projection = FullProjection.getInstance();
        }
        Projection old = getProjection();
        this.projection = projection;
        firePropertyChange("projection", old, getProjection());
    }


    /**
     * Get the class responsible for rendering the sliders track. This may return a null value.
     *
     * @return The sliders track renderer.
     */
    public SliderTrackRenderer getTrackRenderer() {
        return trackRenderer;
    }


    /**
     * Set the renderer to use to display the sliders track. If null is set then no track will be painted.
     *
     * @param trackRenderer The sliders track renderer.
     */
    public void setTrackRenderer(SliderTrackRenderer trackRenderer) {
        SliderTrackRenderer old = getTrackRenderer();
        this.trackRenderer = trackRenderer;
        firePropertyChange("trackRenderer", old, getTrackRenderer());
    }


    /**
     * Get the MarkerMutator used to modify the values of the model. This can be null and in this case the XSlider is
     * non-editable.
     *
     * @return The marker mutator.
     */
    public MarkerMutator getMutator() {
        return mutator;
    }


    /**
     * Set the mutator used to change the values of the model. This can be set to null and in this case signifys that
     * the slider is non-editable.
     *
     * @param mutator The mutator used to alter the model.
     */
    public void setMutator(MarkerMutator mutator) {
        MarkerMutator old = getMutator();
        this.mutator = mutator;
        firePropertyChange("mutator", old, getMutator());
    }


    /**
     * Returns whether the valueAdjusting property for the given value is true or false. If true this means that the
     * given value is still being adjusted (i.e. while dragging the thumb) and certain performace optimisations can be
     * implemented.
     *
     * @param group The group the value originated from.
     * @param value The value to check.
     * @return {@code true} if the given value is still being modified.
     */
    public boolean isValueAdjusting(MarkerGroup group, long value) {
        boolean result = false;
        if (group instanceof AbstractMarkerGroup) {
            result = ((AbstractMarkerGroup) group).isValueAdjusting(value);
        }
        return result;
    }


    /**
     * Get whether this slider is editable. Editable state is determined by both the editable proeprty and the mutator
     * property. No JXSlider can be editable if its MarkerMutator is null. If this method returns true this is no
     * guarentee that any markers within this slider are editable.
     *
     * @return {@code true} if there is a chance this slider is editable.
     */
    public boolean isEditable() {
        return getMutator() != null && editable;
    }


    /**
     * Returns whether the given MarkerGroups value is editable.
     *
     * @param group The marker group to check.
     * @param value The value to edit.
     * @return {@code true} if the given value of the marker group can be modified.
     */
    public boolean isEditable(MarkerGroup group, long value) {
        assert isEditable() && getMutator() != null : "if isEditable returns true then getMutator cannot return null";
        return isEditable() && getMutator().isMutable(getModel(), group, value);
    }


    /**
     * Set whether this slider is editable. Setting to true is only a hint that the slider may be edited as the editable
     * state relies on other factors including the state of the mutator on this slider. Setting to false will ensure
     * that this slider cannot be modified via user interaction.
     *
     * @param editable The new editable state.
     */
    public void setEditable(boolean editable) {
        boolean old = isEditable();
        this.editable = editable;
        firePropertyChange("editable", old, isEditable());
    }


    /**
     * Returns the index of the given marker group in this sliders model or -1 if not found.
     *
     * @param group The marker gorup to find.
     * @return The index of the group in the model.
     */
    public int indexOfMarker(MarkerGroup group) {
        int index = -1;
        SliderModel model = getModel();
        if (model instanceof AbstractSliderModel) {
            index = ((AbstractSliderModel) model).indexOf(group);
        } else {
            for (int i = 0, n = model.getMarkerGroupCount(); i < n; i++) {
                if (model.getMarkerGroup(i) == group) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }


    /**
     * Get the selection model for the marker values.
     *
     * @return The model used to determin the active marker group value.
     */
    public MarkerSelectionModel getMarkerSelectionModel() {
        return markerSelectionModel;
    }


    /**
     * Set the model used to determin the active marker group value. Setting to null will effectively mean that slider
     * becomes uneditable.
     *
     * @param markerSelectionModel The marker value selection model.
     */
    public void setMarkerSelectionModel(MarkerSelectionModel markerSelectionModel) {
        MarkerSelectionModel old = getMarkerSelectionModel();
        if (old != null) {
            old.removeChangeListener(modelAdapter);
        }
        this.markerSelectionModel = markerSelectionModel;
        markerSelectionModel = getMarkerSelectionModel();
        if (markerSelectionModel != null) {
            if (modelAdapter == null) {
                modelAdapter = new ModelAdapter();
            }
            markerSelectionModel.addChangeListener(modelAdapter);
        }
        firePropertyChange("markerSelectionModel", old, markerSelectionModel);
    }


    /**
     * Returns the class id of this component.
     *
     * @return {@code "XSliderUI"}.
     */
    @Override
    public String getUIClassID() {
        return uiClassId;
    }


    /**
     * Returns the ui for this component.
     *
     * @return The ui for this component.
     */
    @Override
    public XSliderUI getUI() {
        return (XSliderUI) super.ui;
    }


    /**
     * Add a listener to this class for notification of changes to the MarkerGroup list and the minimum and maximum
     * values. {@code null} values will be ignored.
     *
     * @param l The slider listener.
     */
    public void addSliderChangeListener(SliderChangeListener l) {
        listenerList.add(SliderChangeListener.class, l);
    }


    /**
     * Remove a listener from this class previously added via the addSliderChangeListener method. {@code null} values
     * will be ignored.
     *
     * @param l The listener to remove.
     */
    public void removeSliderChangeListener(SliderChangeListener l) {
        listenerList.remove(SliderChangeListener.class, l);
    }


    /**
     * Gets an array of all listeners added to this class via the addSliderChangeListener method. This will never return
     * null but may return an empty array.
     *
     * @return The list of added listeners.
     */
    public SliderChangeListener[] getSliderChangeListeners() {
        return listenerList.getListeners(SliderChangeListener.class);
    }


    /**
     * Notify listeners of changes to the model.
     */
    protected void fireSliderRangeChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        SliderChangeEvent event = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SliderChangeListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new SliderChangeEvent(this);
                }
                ((SliderChangeListener) listeners[i + 1]).sliderRangeChanged(event);
            }
        }
    }


    /**
     * Notify listeners of changes to the model.
     *
     * @param index0 The first changed index.
     * @param index1 The last changed index.
     */
    protected void fireSliderMarkerGroupAdded(int index0, int index1) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        SliderChangeEvent event = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SliderChangeListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new SliderChangeEvent(this, SliderChangeEvent.Type.MARKER_GROUP_ADDED, index0, index1);
                }
                ((SliderChangeListener) listeners[i + 1]).sliderMarkerGroupAdded(event);
            }
        }
    }


    /**
     * Notify listeners of changes to the model.
     *
     * @param index0 The first changed index.
     * @param index1 The last changed index.
     */
    protected void fireSliderMarkerGroupRemoved(int index0, int index1) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        SliderChangeEvent event = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SliderChangeListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new SliderChangeEvent(this, SliderChangeEvent.Type.MARKER_GROUP_REMOVED, index0, index1);
                }
                ((SliderChangeListener) listeners[i + 1]).sliderMarkerGroupRemoved(event);
            }
        }
    }


    /**
     * Notify listeners of changes to the model.
     *
     * @param index0 The first changed index.
     * @param index1 The last changed index.
     */
    protected void fireSliderMarkerGroupChanged(int index0, int index1) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        SliderChangeEvent event = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SliderChangeListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new SliderChangeEvent(this, SliderChangeEvent.Type.MARKER_GROUP_CHANGED, index0, index1);
                }
                ((SliderChangeListener) listeners[i + 1]).sliderMarkerGroupChanged(event);
            }
        }
    }


    /**
     * Add a change listener for notification of changes the seleciton of marker values. Null values will be uignored.
     *
     * @param l The change listener to add.
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }


    /**
     * remove the given change listener from notificatio of selection change. Null vlaues will be ignored.
     *
     * @param l The change listener to remove.
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }


    /**
     * Get the list of all change listeners added via addChangeListener. This will never be null.
     *
     * @return the list of change listeners.
     */
    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(ChangeListener.class);
    }


    /**
     * Fired when the selection model changes.
     */
    protected void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(event);
            }
        }
    }


    protected void updateMarkerSelection(SliderChangeEvent e) {
        switch (e.getType()) {
            case MARKER_GROUP_ADDED:
                if (getMarkerGroupCount() == 1) {
                    if (getMarkerSelectionModel() != null) {
                        getMarkerSelectionModel().setSelectedGroup(getMarkerGroup(0));
                    }
                }
                break;
            case MARKER_GROUP_REMOVED:
                if (getMarkerGroupCount() == 0) {
                    if (getMarkerSelectionModel() != null) {
                        getMarkerSelectionModel().setSelectedGroup(null);
                    } else {
                        MarkerGroup g = getMarkerSelectedGroup();
                        if (g != null) {
                            SliderModel model = getModel();
                            int index = -1;
                            if (model instanceof AbstractSliderModel) {
                                index = ((AbstractSliderModel) model).indexOf(g);
                            } else {
                                for (int i = 0, n = model.getMarkerGroupCount(); i < n; i++) {
                                    if (g == model.getMarkerGroup(i)) {
                                        index = i;
                                        break;
                                    }
                                }
                            }
                            if (index < 0) { // if not found remove selection
                                getMarkerSelectionModel().setSelectedGroup(null);
                            }
                        }
                    }
                }
                break;
            case MARKER_GROUP_CHANGED:
                if (isMarkerValueSelected()) {
                    MarkerChangeEvent cause = e.getCause();
                    assert cause != null;

                    long oldVal = cause.getOldValue();
                    if (getMarkerSelectedValue() == oldVal) {
                        getMarkerSelectionModel().setSelectedValue(cause.getNewValue());
                    }
                }

                break;
        }
    }


    /**
     * Listeners for forwarding events from the model to any listeners added on this controller class.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private class ModelAdapter implements SliderChangeListener, ChangeListener {
        public void sliderRangeChanged(SliderChangeEvent e) {
            fireSliderRangeChanged();
        }

        public void sliderMarkerGroupAdded(SliderChangeEvent e) {
            updateMarkerSelection(e);
            fireSliderMarkerGroupAdded(e.getIndex0(), e.getIndex1());
        }

        public void sliderMarkerGroupRemoved(SliderChangeEvent e) {
            updateMarkerSelection(e);
            fireSliderMarkerGroupRemoved(e.getIndex0(), e.getIndex1());
        }

        public void sliderMarkerGroupChanged(SliderChangeEvent e) {
            updateMarkerSelection(e);
            fireSliderMarkerGroupChanged(e.getIndex0(), e.getIndex1());
        }

        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    }

}
