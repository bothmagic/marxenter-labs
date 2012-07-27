/*
 * Created on 30.10.2007
 *
 */
package org.jdesktop.beansbindingx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.BindingListener;
import org.jdesktop.beansbinding.PropertyStateEvent;
import org.jdesktop.beansbinding.Binding.SyncFailure;

/**
 * Simplistic "Beanifier" of BindingGroup. Emergency wrapper around BindingGroup
 * to get simple dirty/bound notification of the group as a whole.
 * 
 * Further simplification:
 * - add/remove only with unbound bindings. This keeps the control here.
 * - 
 */
public class BindingGroupBean extends AbstractBean {
    
    private static final Logger LOG = Logger.getLogger(BindingGroupBean.class
            .getName());
    BindingGroup bindingGroup;
    boolean dirty;
    private boolean bound;
    Map<Binding, SyncFailure> failures;
    Map<Binding, PropertyStateEvent> targetChanges;
    private boolean singleSourceType;

    public BindingGroupBean() {
        this(false);
    }
    
    public BindingGroupBean(boolean singleSourceType) {
        bindingGroup = new BindingGroup();
        bindingGroup.addBindingListener(new NotificationHelper());
        this.singleSourceType = singleSourceType; 
    }

    /**
     * Returns a boolean to denote if any of the contained Bindings is unsynched.
     * 
     * @return true if one or more Bindings have uncommitted values or synchFailures,
     * false otherwise.
     */
    public boolean isDirty() {
        return dirty;
    }

    
    private void setDirty(boolean dirty) {
        if (isDirty() == dirty) return;
        this.dirty = dirty;
        firePropertyChange("dirty", !dirty, dirty);
    }

    
    public boolean isBound() {
        return bound;
    }
    
    /**
     * Adds the binding to the group. 
     * @param binding
     * @throws IllegalStateException if this group is bound.
     * @throws IllegalStateException is the binding is bound.
     */
    public void addBinding(Binding binding) {
        throwIfBound();
        throwIfBound(binding);
        bindingGroup.addBinding(binding);
    }

    /**
     * Returns a list of all {@code Bindings} in this group. Order is undefined.
     * Returns an empty list if the group contains no {@code Bindings}.
     *
     * @return a list of all {@code Bindings} in this group.
     */
    public List<Binding> getBindings() {
        return bindingGroup.getBindings();
    }

//--------------------- bulk operations on all bindings    
    /**
     * Calls saveAndNotify for each contained binding.
     */
    public void saveAndNotify() {
        throwIfUnbound();
        for (Binding binding : getBindings()) {
            binding.saveAndNotify();
        }
    }
    
    /**
     * Sets the same source object to all contained bindings.
     * As a side-effect, tries to guarantee a default unreadableSourceValue.
     * 
     * Note: this is allowed only if the source properties of all bindings 
     * have a similar enough source property.
     * 
     * Think: should be doable only if bound? 
     * @param source
     */
    public void setSourceObject(Object source) {
        boolean wasBound = isBound();
        if (isBound()) unbind(); 
        ensureSourceUnreadableValueSet(source);
        for (Binding binding : getBindings()) {
            binding.setSourceObject(source);
        }
        if (wasBound) bind();
    }
    
    /**
     * Sets a flag to control the auto-handling of SourceUnreadableValues.
     * The default value is false. 
     * 
     * @param enabled
     */
    public void setSingleSourceType(boolean enabled) {
        if (isSingleSourceType() == enabled) return;
        this.singleSourceType = enabled;
        firePropertyChange("defaultSourceUnreadableValues", !enabled, enabled);
    }

    private boolean isSingleSourceType() {
        // TODO Auto-generated method stub
        return singleSourceType;
    }

    /**
     * tries to set a default unreadableSourceValue property for all
     * contained bindings.
     * 
     * @param source the source object to take as reference. If null, 
     *   the first not-null source object in the list of bindings is
     *   used. Does nothing if the fallback is null as well.
     */
    private void ensureSourceUnreadableValueSet(Object source) {
        if (!isSingleSourceType()) return;
        if (areUnreadableSourceValueSet()) return;
        if (source == null) {
            source = getSourceObjectFromBindings();
        }
        // back out if there is no not-null source
        if (source == null) return;
        for (Binding binding : getBindings()) {
            if (!binding.isSourceUnreadableValueSet()) {
                Object nullValue = null;
                Class<?> writeType = binding.getSourceProperty().getWriteType(source);
                if (Boolean.class.isAssignableFrom(writeType) 
                        || (writeType.isPrimitive() && writeType == Boolean.TYPE)) {
                    nullValue = Boolean.FALSE;
                }  
                // TODO: handle other primitives
                binding.setSourceUnreadableValue(nullValue);
            }
        }
    }


    private Object getSourceObjectFromBindings() {
        for (Binding binding : getBindings()) {
            if (binding.getSourceObject() != null) {
                return binding.getSourceObject();
            }
        }
        return null;
    }

    /**
     * Returns a boolean to indicate if all contained bindings
     * have a unreadable source set.
     * 
     * @return true if all bindings have the unreadableSource set, 
     *    false otherwise
     */
    private boolean areUnreadableSourceValueSet() {
        for (Binding binding : getBindings()) {
            if (!binding.isSourceUnreadableValueSet()) return false;
        }
        return true;
    }

    /**
     * Binds all contained Bindings. Set's the bound property to true.
     */
    public void bind() {
        throwIfBound();
        bindingGroup.bind();
        // PENDING: handle synchfailures during bind
        setBound(true);
    }

    /**
     * Unbinds all contained Bindings. Set's the bound property to false.
     */
    public void unbind() {
        throwIfUnbound();
        bindingGroup.unbind();
        setBound(false);
    }
    
    private void setBound(boolean bound) {
        if (isBound() == bound) return;
        this.bound = bound;
        firePropertyChange("bound", !bound, bound);
        // PENDING: handle synchfailures during bind
        setDirty(false);
    }


    /**
     * Throws an IllegalStateException if the {@code Binding} is bound.
     * Useful for calling at the beginning of method implementations that
     * shouldn't be called when the {@code Binding} is bound.
     *
     * @throws IllegalStateException if the {@code Binding} is bound.
     */
    private void throwIfBound(Binding binding) {
        if (binding.isBound()) {
            throw new IllegalStateException("Can not call this method on a bound binding");
        }
    }

    /**
     * Throws an IllegalStateException if the {@code Binding} is bound.
     * Useful for calling at the beginning of method implementations that
     * shouldn't be called when the {@code Binding} is bound.
     *
     * @throws IllegalStateException if the {@code Binding} is bound.
     */
    private void throwIfBound() {
        if (isBound()) {
            throw new IllegalStateException("Can not call this method on a bound binding");
        }
    }

    /**
     * Throws an IllegalStateException if the {@code Binding} is bound.
     * Useful for calling at the beginning of method implementations that
     * shouldn't be called when the {@code Binding} is bound.
     *
     * @throws IllegalStateException if the {@code Binding} is bound.
     */
    private void throwIfUnbound() {
        if (!isBound()) {
            throw new IllegalStateException("Can not call this method on a bound binding");
        }
    }

//------------------ reacting to notification from BindingListener
    
    /**
     * Removes all stored failures/changes for the given binding 
     * sets the group's properties appropriately. 
     * 
     * Here: removes failures and targetChanged and reset's the 
     * groups dirty flag if both are empty. 
     * @param binding
     */
    private void removeSynced(Binding binding) {
        getFailures().remove(binding);
        getTargetChanges().remove(binding);
        boolean clean = getFailures().isEmpty() && getTargetChanges().isEmpty();
        setDirty(!clean);
    }

    
    /**
     * Stores the received failure for future use and 
     * sets the group's properties appropriately. 
     * 
     * Here: only keeps the last failure and sets the dirty property
     * to true.
     * 
     * @param binding
     * @param failure
     */
    private void addFailure(Binding binding, SyncFailure failure) {
        getFailures().put(binding, failure);
        setDirty(true);
    }
    
    /**
     * Stores the received changed for future use and sets the
     * sets the group's properties appropriately. 
     * 
     * Here: only keeps the last change and set the dirty property 
     * to true. 
     * 
     * @param binding
     * @param failure
     */
    private void addTargetChange(Binding binding, PropertyStateEvent event) {
        getTargetChanges().put(binding, event);
        setDirty(true);
    }

    private Map<Binding, SyncFailure> getFailures() {
        if (failures == null) {
            failures = new HashMap<Binding, SyncFailure>();
        }
        return failures;
    }

    private Map<Binding, PropertyStateEvent> getTargetChanges() {
        if (targetChanges == null) {
            targetChanges = new HashMap<Binding, PropertyStateEvent>();
        }
        return targetChanges;
    }

    
    /**
     * Helper class to keep track of changed/failed binding synchs. 
     * Currently listens to targetChanged/syncFailure/synced and updates
     * the bean's internal state accordingly.
     * 
     * 
     */
    public class NotificationHelper implements BindingListener {
        public void bindingBecameBound(Binding binding) {
            // TODO Auto-generated method stub

        }

        public void bindingBecameUnbound(Binding binding) {
            // TODO Auto-generated method stub

        }

        public void sourceChanged(Binding binding, PropertyStateEvent event) {
            // TODO Auto-generated method stub

        }

        public void syncFailed(Binding binding, SyncFailure failure) {
            addFailure(binding, failure);
         }

        public void targetChanged(Binding binding, PropertyStateEvent event) {
            if (event.getValueChanged()) {
                addTargetChange(binding, event);
            }
            
        }

        public void synced(Binding binding) {
            removeSynced(binding);
        }
        
    }


}
