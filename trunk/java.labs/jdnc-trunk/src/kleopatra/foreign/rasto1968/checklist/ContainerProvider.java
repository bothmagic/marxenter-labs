/*
 * $Id: ContainerProvider.java 3162 2009-06-22 15:55:21Z kschaefe $
 *
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package rasto1968.checklist;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.treetable.TreeTableNode;

import rasto1968.checklist.CheckListModel.SelectableNode;

/**
 * A {@code ComponentProvider} for creating a compound rendering display.
 */
public class ContainerProvider extends ComponentProvider<RenderingPanel> {

    ComponentProvider<JComponent> mainWrappee;

    ComponentProvider<JComponent> leadingWrappee;

    public ContainerProvider(ComponentProvider<? extends JComponent> main,
            ComponentProvider<? extends JComponent> leading) {
        setMainWrappee(main);
        setLeadingWrappee(leading);
    }

    @Override
    public RenderingPanel getRendererComponent(CellContext context) {

        if (context != null) {
            rendererComponent.setLeadingDelegate(getComponent(leadingWrappee, context));
            // PENDING JW: here the provider is not agnostic of the content
            // must not adjust the value before giving it to the leading delegate
            // which is the checkbox. Hmmm ...
            Object oldValue = adjustContextValue(context);
            rendererComponent.setMainDelegate(getComponent(mainWrappee, context));
            restoreContextValue(context, oldValue);
        }
        return super.getRendererComponent(context);
    }

    /**
     * Restores the context value to the old value.
     * 
     * @param context
     *            the CellContext to restore.
     * @param oldValue
     *            the value to restore the context to.
     */
    protected void restoreContextValue(CellContext context, Object oldValue) {
        context.replaceValue(oldValue);
    }

    /**
     * Replace the context's value with the userobject if it's a treenode.
     * <p>
     * Subclasses may override but must guarantee to return the original value for restoring.
     * 
     * @param context
     *            the context to adjust
     * @return the old context value
     */
    protected Object adjustContextValue(CellContext context) {
        Object oldValue = context.getValue();
        if (oldValue instanceof DefaultMutableTreeNode) {
            context.replaceValue(((DefaultMutableTreeNode) oldValue).getUserObject());
        } else if (oldValue instanceof TreeTableNode) {
            TreeTableNode node = (TreeTableNode) oldValue;
            context.replaceValue(node.getUserObject());
        } else if (oldValue instanceof SelectableNode) {
            context.replaceValue(((SelectableNode) oldValue).getUserObject());
        }
        return oldValue;
    }

    private JComponent getComponent(ComponentProvider wrappee, CellContext context) {
        JComponent comp = null;
        if (wrappee != null) {
            comp = wrappee.getRendererComponent(context);
            comp.setBorder(BorderFactory.createEmptyBorder());
        }
        return comp;
    }

    @Override
    protected void configureState(CellContext context) {
    }

    @Override
    protected RenderingPanel createRendererComponent() {
        return new RenderingPanel();
    }

    @Override
    protected void format(CellContext context) {
    }

    public void setLeadingWrappee(ComponentProvider leading) {
        this.leadingWrappee = leading;
    }

    public void setMainWrappee(ComponentProvider main) {
        this.mainWrappee = main;
    }

}
