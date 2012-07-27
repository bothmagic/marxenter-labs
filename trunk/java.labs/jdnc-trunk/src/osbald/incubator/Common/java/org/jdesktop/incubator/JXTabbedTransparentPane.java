package org.jdesktop.incubator;

import javax.swing.*;
import java.awt.*;

/**
 * If isTransparentPreferred() (defaults to true) knocks out the opacity of any tab components added.
 * Make this a client property of a real JXTabbedPane? would expect painter support (for what tabs too?) maybe
 * vertical tab support? default JTabbedPane vertical tabs are pretty cruddy.
 * <p/>
 * Thinks could just do this with my ComponentFactory.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston (www.osbald.co.uk)
 * Date: 26-Jan-2005
 * Time: 11:02:43
 */

public class JXTabbedTransparentPane extends JTabbedPane {
    private boolean preferredOpaque = false;

    public boolean isPreferredOpaque() {
        return preferredOpaque;
    }

    public void setPreferredOpaque(boolean b) {
        //TODO cycle through children and change opacity (post-init)? too unexpected?
        this.preferredOpaque = b;
    }

    /**
     * @deprecated use isPreferredOpaque() instead (bad naming)
     */
    public boolean isTransparentPreferred() {
        return isPreferredOpaque();
    }

    /**
     * @deprecated use setPreferredOpaque() instead (bad naming)
     */
    public void setTransparentPreferred(boolean b) {
        setPreferredOpaque(b);
    }

    protected void setDefaultOpacity(Component component) {
        if (component instanceof JComponent) {
            ((JComponent) component).setOpaque(isPreferredOpaque());
        }
    }

    public void addTab(String title, Icon icon, Component component, String tip) {
        super.addTab(title, icon, component, tip);
        setDefaultOpacity(component);
    }

    public void addTab(String title, Icon icon, Component component) {
        super.addTab(title, icon, component);
        setDefaultOpacity(component);
    }

    public void addTab(String title, Component component) {
        super.addTab(title, component);
        setDefaultOpacity(component);
    }

    public Component add(Component component) {
        Component c = super.add(component);
        setDefaultOpacity(component);
        return c;
    }

    public Component add(String title, Component component) {
        Component c = super.add(title, component);
        setDefaultOpacity(component);
        return c;
    }

    public Component add(Component component, int index) {
        Component c = super.add(component, index);
        setDefaultOpacity(component);
        return c;
    }

    public void add(Component component, Object constraints) {
        super.add(component, constraints);
        setDefaultOpacity(component);
    }

    public void add(Component component, Object constraints, int index) {
        super.add(component, constraints, index);
        setDefaultOpacity(component);
    }
}
