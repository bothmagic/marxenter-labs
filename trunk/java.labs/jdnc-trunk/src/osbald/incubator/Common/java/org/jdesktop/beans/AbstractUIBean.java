package org.jdesktop.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

/**
 * Differs from org.jdesktop.beans.AbstractBean in that PropertyChangeListeners are guaranteed to be called on the EDT.
 * See org.jdesktop.incubator.beans.EDTPropertyChangeSupport.
 * <p/>
 * Can be a mixed blessing if a developer attempts to call any AbstractBean accessors from such
 * PropertyChangeListeners without appropriate synchronisation ..well no more so than any other multi-threaded code.
 */

//PENDING got some issues with VetoableChangeSupport (unsupported EDT guarantee) see EDTVetoableChangeSupport

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 13-Mar-2007
 * Time: 15:40:05
 */

public class AbstractUIBean {
    /**
     * Helper class that manages all the property change notification machinery.
     * PropertyChangeSupport cannot be extended directly because it requires
     * a bean in the constructor, and the "this" argument is not valid until
     * after super construction. Hence, delegation instead of extension
     */
    private transient PropertyChangeSupport pcs;

    /**
     * Helper class that manages all the veto property change notification machinery.
     */
    private transient VetoableChangeSupport vcs;

    /**
     * Creates a new instance of AbstractBean
     */
    protected AbstractUIBean() {
        this.pcs = new EDTPropertyChangeSupport(this);
        this.vcs = new VetoableChangeSupport(this);      //PENDING how are these supposed to work viz EDT??!
    }

    /**
     * Creates a new instance of AbstractBean, using the supplied PropertyChangeSupport and
     * VetoableChangeSupport delegates. Neither of these may be null.
     */
    protected AbstractUIBean(EDTPropertyChangeSupport pcs, VetoableChangeSupport vcs) {
        if (pcs == null) {
            throw new NullPointerException("PropertyChangeSupport must not be null");
        }
        if (vcs == null) {
            throw new NullPointerException("VetoableChangeSupport must not be null");
        }

        this.pcs = pcs;
        this.vcs = vcs;      //PENDING how are these supposed to work viz EDT??!
    }


    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     * The same listener object may be added more than once, and will be called
     * as many times as it is added.
     * If <code>listener</code> is null, no exception is thrown and no action
     * is taken.
     *
     * @param listener The PropertyChangeListener to be added
     */
    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered
     * for all properties.
     * If <code>listener</code> was added more than once to the same event
     * source, it will be notified one less time after being removed.
     * If <code>listener</code> is null, or was never added, no exception is
     * thrown and no action is taken.
     *
     * @param listener The PropertyChangeListener to be removed
     */
    public final void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Returns an array of all the listeners that were added to the
     * PropertyChangeSupport object with addPropertyChangeListener().
     * <p/>
     * If some listeners have been added with a named property, then
     * the returned array will be a mixture of PropertyChangeListeners
     * and <code>PropertyChangeListenerProxy</code>s. If the calling
     * method is interested in distinguishing the listeners then it must
     * test each element to see if it's a
     * <code>PropertyChangeListenerProxy</code>, perform the cast, and examine
     * the parameter.
     * <p/>
     * <pre>
     * PropertyChangeListener[] listeners = bean.getPropertyChangeListeners();
     * for (int i = 0; i < listeners.length; i++) {
     * 	 if (listeners[i] instanceof PropertyChangeListenerProxy) {
     *     PropertyChangeListenerProxy proxy =
     *                    (PropertyChangeListenerProxy)listeners[i];
     *     if (proxy.getPropertyName().equals("foo")) {
     *       // proxy is a PropertyChangeListener which was associated
     *       // with the property named "foo"
     *     }
     *   }
     * }
     * </pre>
     *
     * @return all of the <code>PropertyChangeListeners</code> added or an
     *         empty array if no listeners have been added
     * @see java.beans.PropertyChangeListenerProxy
     */
    public final PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners();
    }

    /**
     * Add a PropertyChangeListener for a specific property.  The listener
     * will be invoked only when a call on firePropertyChange names that
     * specific property.
     * The same listener object may be added more than once.  For each
     * property,  the listener will be invoked the number of times it was added
     * for that property.
     * If <code>propertyName</code> or <code>listener</code> is null, no
     * exception is thrown and no action is taken.
     *
     * @param propertyName The name of the property to listen on.
     * @param listener     The PropertyChangeListener to be added
     */
    public final void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Remove a PropertyChangeListener for a specific property.
     * If <code>listener</code> was added more than once to the same event
     * source for the specified property, it will be notified one less time
     * after being removed.
     * If <code>propertyName</code> is null,  no exception is thrown and no
     * action is taken.
     * If <code>listener</code> is null, or was never added for the specified
     * property, no exception is thrown and no action is taken.
     *
     * @param propertyName The name of the property that was listened on.
     * @param listener     The PropertyChangeListener to be removed
     */
    public final void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Returns an array of all the listeners which have been associated
     * with the named property.
     *
     * @param propertyName The name of the property being listened to
     * @return all of the <code>PropertyChangeListeners</code> associated with
     *         the named property.  If no such listeners have been added,
     *         or if <code>propertyName</code> is null, an empty array is
     *         returned.
     */
    public final PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return pcs.getPropertyChangeListeners(propertyName);
    }

    /**
     * Report a bound property update to any registered listeners.
     * No event is fired if old and new are equal and non-null.
     * <p/>
     * <p/>
     * This is merely a convenience wrapper around the more general
     * firePropertyChange method that takes {@code
     * PropertyChangeEvent} value.
     *
     * @param propertyName The programmatic name of the property
     *                     that was changed.
     * @param oldValue     The old value of the property.
     * @param newValue     The new value of the property.
     */
    protected final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Fire an existing PropertyChangeEvent to any registered listeners.
     * No event is fired if the given event's old and new values are
     * equal and non-null.
     *
     * @param evt The PropertyChangeEvent object.
     */
    protected final void firePropertyChange(PropertyChangeEvent evt) {
        pcs.firePropertyChange(evt);
    }


    /**
     * Report a bound indexed property update to any registered
     * listeners.
     * <p/>
     * No event is fired if old and new values are equal
     * and non-null.
     * <p/>
     * <p/>
     * This is merely a convenience wrapper around the more general
     * firePropertyChange method that takes {@code PropertyChangeEvent} value.
     *
     * @param propertyName The programmatic name of the property that
     *                     was changed.
     * @param index        index of the property element that was changed.
     * @param oldValue     The old value of the property.
     * @param newValue     The new value of the property.
     */
    protected final void fireIndexedPropertyChange(String propertyName, int index,
                                                   Object oldValue, Object newValue) {
        pcs.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    /**
     * Check if there are any listeners for a specific property, including
     * those registered on all properties.  If <code>propertyName</code>
     * is null, only check for listeners registered on all properties.
     *
     * @param propertyName the property name.
     * @return true if there are one or more listeners for the given property
     */
    protected final boolean hasPropertyChangeListeners(String propertyName) {
        return pcs.hasListeners(propertyName);
    }

    /**
     * Check if there are any listeners for a specific property, including
     * those registered on all properties.  If <code>propertyName</code>
     * is null, only check for listeners registered on all properties.
     *
     * @param propertyName the property name.
     * @return true if there are one or more listeners for the given property
     */
    protected final boolean hasVetoableChangeListeners(String propertyName) {
        return vcs.hasListeners(propertyName);
    }

    /**
     * Add a VetoableListener to the listener list.
     * The listener is registered for all properties.
     * The same listener object may be added more than once, and will be called
     * as many times as it is added.
     * If <code>listener</code> is null, no exception is thrown and no action
     * is taken.
     *
     * @param listener The VetoableChangeListener to be added
     */

    public final void addVetoableChangeListener(VetoableChangeListener listener) {
        vcs.addVetoableChangeListener(listener);
    }

    /**
     * Remove a VetoableChangeListener from the listener list.
     * This removes a VetoableChangeListener that was registered
     * for all properties.
     * If <code>listener</code> was added more than once to the same event
     * source, it will be notified one less time after being removed.
     * If <code>listener</code> is null, or was never added, no exception is
     * thrown and no action is taken.
     *
     * @param listener The VetoableChangeListener to be removed
     */
    public final void removeVetoableChangeListener(VetoableChangeListener listener) {
        vcs.removeVetoableChangeListener(listener);
    }

    /**
     * Returns the list of VetoableChangeListeners. If named vetoable change listeners
     * were added, then VetoableChangeListenerProxy wrappers will returned
     * <p/>
     *
     * @return List of VetoableChangeListeners and VetoableChangeListenerProxys
     *         if named property change listeners were added.
     */
    public final VetoableChangeListener[] getVetoableChangeListeners() {
        return vcs.getVetoableChangeListeners();
    }

    /**
     * Add a VetoableChangeListener for a specific property.  The listener
     * will be invoked only when a call on fireVetoableChange names that
     * specific property.
     * The same listener object may be added more than once.  For each
     * property,  the listener will be invoked the number of times it was added
     * for that property.
     * If <code>propertyName</code> or <code>listener</code> is null, no
     * exception is thrown and no action is taken.
     *
     * @param propertyName The name of the property to listen on.
     * @param listener     The VetoableChangeListener to be added
     */

    public final void addVetoableChangeListener(String propertyName,
                                                VetoableChangeListener listener) {
        vcs.addVetoableChangeListener(propertyName, listener);
    }

    /**
     * Remove a VetoableChangeListener for a specific property.
     * If <code>listener</code> was added more than once to the same event
     * source for the specified property, it will be notified one less time
     * after being removed.
     * If <code>propertyName</code> is null, no exception is thrown and no
     * action is taken.
     * If <code>listener</code> is null, or was never added for the specified
     * property, no exception is thrown and no action is taken.
     *
     * @param propertyName The name of the property that was listened on.
     * @param listener     The VetoableChangeListener to be removed
     */

    public final void removeVetoableChangeListener(String propertyName,
                                                   VetoableChangeListener listener) {
        vcs.removeVetoableChangeListener(propertyName, listener);
    }

    /**
     * Returns an array of all the listeners which have been associated
     * with the named property.
     *
     * @param propertyName The name of the property being listened to
     * @return all the <code>VetoableChangeListeners</code> associated with
     *         the named property.  If no such listeners have been added,
     *         or if <code>propertyName</code> is null, an empty array is
     *         returned.
     */
    public final VetoableChangeListener[] getVetoableChangeListeners(String propertyName) {
        return vcs.getVetoableChangeListeners(propertyName);
    }

    /**
     * Report a vetoable property update to any registered listeners.  If
     * anyone vetos the change, then fire a new event reverting everyone to
     * the old value and then rethrow the PropertyVetoException.
     * <p/>
     * No event is fired if old and new are equal and non-null.
     *
     * @param propertyName The programmatic name of the property
     *                     that is about to change..
     * @param oldValue     The old value of the property.
     * @param newValue     The new value of the property.
     * @throws PropertyVetoException if the recipient wishes the property
     *                               change to be rolled back.
     */
    protected final void fireVetoableChange(String propertyName,
                                            Object oldValue, Object newValue)
            throws PropertyVetoException {
        vcs.fireVetoableChange(propertyName, oldValue, newValue);
    }

    /**
     * Fire a vetoable property update to any registered listeners.  If
     * anyone vetos the change, then fire a new event reverting everyone to
     * the old value and then rethrow the PropertyVetoException.
     * <p/>
     * No event is fired if old and new are equal and non-null.
     *
     * @param evt The PropertyChangeEvent to be fired.
     * @throws PropertyVetoException if the recipient wishes the property
     *                               change to be rolled back.
     */
    protected final void fireVetoableChange(PropertyChangeEvent evt)
            throws PropertyVetoException {
        vcs.fireVetoableChange(evt);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AbstractUIBean result = (AbstractUIBean) super.clone();
        result.pcs = new EDTPropertyChangeSupport(result);
        result.vcs = new VetoableChangeSupport(result);     //PENDING how are these supposed to work viz EDT??!
        return result;
    }
}
