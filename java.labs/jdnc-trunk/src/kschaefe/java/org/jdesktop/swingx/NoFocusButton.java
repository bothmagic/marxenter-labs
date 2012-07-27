/**
 * 
 */
package org.jdesktop.swingx;

/**
 * {@code NoFocusButton} is a useful button subclass that ensures that it will
 * never recieve focus. Such non-focusing buttons are useful in tool bars and
 * title bars.
 * <p>
 * Inspired by
 * {@code javax.swing.plaf.basic.BasicInternalFrameTitlePane.NoFocusButton}.
 */
public class NoFocusButton extends JXButton {

    /**
     * 
     */
    public NoFocusButton() {
    }

    /**
     * @param text
     */
    public NoFocusButton(String text) {
        super(text);
    }

    /**
     * Overridden to ensure that the focus can never be painted.
     * 
     * @return {@code false}
     */
    public final boolean isFocusPainted() {
        return false;
    }

    /**
     * Overridden to ensure that this component is not traversable.
     * 
     * @return {@code false}
     */
    public final boolean isFocusTraversable() {
        return false;
    }

    /**
     * Overridden to ensure that this component is not focusable.
     * 
     * @return {@code false}
     */
    public final boolean isFocusable() {
        return false;
    }

    /**
     * Overridden to ensure that this component cannot request focus.
     * 
     * @return {@code false}
     */
    public final boolean isRequestFocusEnabled() {
        return false;
    }

    /**
     * Overridden to ensure that this component cannot request focus.
     */
    public final void requestFocus() {
        // no-op
    }

    /**
     * Overridden to ensure that this component cannot request focus.
     * 
     * @param temporary
     *            unused
     * @return {@code false}
     */
    public final boolean requestFocus(boolean temporary) {
        return false;
    }

    /**
     * Overridden to ensure that this component cannot request focus.
     * 
     * @return {@code false}
     */
    public final boolean requestFocusInWindow() {
        return false;
    }

    /**
     * Overridden to ensure that this component cannot request focus.
     * 
     * @param temporary
     *            unused
     * @return {@code false}
     */
    protected final boolean requestFocusInWindow(boolean temporary) {
        return false;
    }
}
