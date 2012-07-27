/*
 * Created on 16.04.2009
 *
 */
package org.jdesktop.swingx.event;

import java.awt.Component;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;

import org.jdesktop.swingx.SwingXUtilities;

/**
 * An implementation of InputEventDispatcher which maps focusEvents received
 * from a container hierarchy to a bound read-only property. Registered
 * PropertyChangeListeners are notified if the focus is transfered into/out of
 * the hierarchy of a given root.
 * <p>
 * 
 * F.i, client code which wants to get notified if focus enters/exits the hierarchy below
 * panel would install the dispatcher like:
 * 
 * <pre>
 * &lt;code&gt;
 *         // add some components inside
 *         panel.add(new JTextField(&quot;something to .... focus&quot;));
 *         panel.add(new JXDatePicker(new Date()));
 *         JComboBox combo = new JComboBox(new Object[] {&quot;dooooooooo&quot;, 1, 2, 3, 4 });
 *         combo.setEditable(true);
 *         panel.add(new JButton(&quot;something else to ... focus&quot;));
 *         panel.add(combo);
 *         panel.setBorder(new TitledBorder(&quot;has focus dispatcher&quot;));
 *         // register the compound dispatcher
 *         AbstractInputEventDispatcher focusDispatcher = new CompoundFocusEventDispatcher(panel);
 *         panel.setInputEventDispatcher(focusDispatcher);
 *         PropertyChangeListener l = new PropertyChangeListener() {
 * 
 *             public void propertyChange(PropertyChangeEvent evt) {
 *                 // do something useful here
 *                 
 *             }};
 *         focusDispatcher.addPropertyChangeListener(l);    
 *         
 * &lt;/code&gt;
 * </pre>
 * 
 * 
 * Note: this is effective only if the root supports InputEventDispatcher.
 * <p>
 * 
 * PENDING JW: formally add InputEventDispatcherAware
 * 
 * 
 */
public class CompoundFocusEventDispatcher extends AbstractInputEventDispatcher {
    private boolean focused;

    private JComponent root;

    /**
     * Instantiates a dispatcher with the given component as root of the
     * hierarchy to listen to.
     * 
     * @param root the root of the component hierarchy to listen for focus
     *        events.
     */
    public CompoundFocusEventDispatcher(JComponent root) {
        this.root = root;
    }

    /**
     * Return true if the root or any of its descendants is focused. This is a
     * read-only bound property, that is property change event is fired if focus
     * is transfered into/out of root's hierarchy.
     * 
     * @return a boolean indicating whether or not any component in the
     *         container hierarchy below root is permanent focus owner.
     */
    public boolean isFocused() {
        return focused;
    }

    private void setFocused(boolean focused) {
        boolean old = isFocused();
        this.focused = focused;
        firePropertyChange("focused", old, isFocused());
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to map FocusEvents to focused property. It the event is
     * permanent, the property is set to true or false on FOCUS_GAINED always or
     * on FOCUS_LOST if the opposite component is not descending from root.
     */
    @Override
    protected void processFocusEvent(FocusEvent e) {
        if (e.isTemporary())
            return;
        if (FocusEvent.FOCUS_GAINED == e.getID()) {
            setFocused(true);
        } else {
            Component opposite = e.getOppositeComponent();
            setFocused(SwingXUtilities.isDescendingFrom(opposite, root));
        }
    }

}