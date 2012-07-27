/*
 * Created on 15.10.2009
 *
 */
package org.jdesktop.appframework.beansbinding;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;

/**
 * Experimenting with @Action annotation and its selectedProperty. 
 * Problems:
 * - artefact method toggleSelected, must not do anything
 * - initial value not taken
 */
public class ActionSelected extends SingleFrameApplication {

    JLabel label = new JLabel("something to hide");
    boolean selected = label.isVisible();

    /**
     * Note: this is public as an implementation side-effect. DON'T USE!
     */
    @Action(selectedProperty = "selected")
    public void toggleSelected() {
    }

    /**
     * Returns selected property.
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets selected property, bound property.
     * 
     * @param selected
     */
    public void setSelected(boolean selected) {
        boolean old = isSelected();
        this.selected = selected;
        label.setVisible(selected);
        getMainView().getComponent().revalidate();
        getMainView().getComponent().repaint();
        firePropertyChange("selected", old, isSelected());
    }

    /**
     * Toggle the selected property.
     */
    @Action
    public void toggleExternal() {
        setSelected(!isSelected());
    }
    
    /**
     * Call toggleSelected, does nothing.
     */
    @Action 
    public void toggleAction() {
        toggleSelected();
    }
    
    @Override
    protected void startup() {
        View view = getMainView();
        view.setComponent(createMainPanel());
        firePropertyChange("selected", null, true);
        show(view);
    }

    private JComponent createMainPanel() {
        JComponent panel = Box.createVerticalBox();
        JCheckBox box = new JCheckBox(getAction("toggleSelected"));
        panel.add(box);
        panel.add(new JButton(getAction("toggleExternal")));
        panel.add(new JButton(getAction("toggleAction")));
        panel.add(label);
        return panel;
    }

    private javax.swing.Action getAction(String actionName) {
        return getContext().getActionMap().get(actionName);
    }

    public static void main(String[] args) {
        launch(ActionSelected.class, args);
    }

}
