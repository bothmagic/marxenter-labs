/*
 * $Id: CheckListFactory.java 3312 2010-11-03 10:55:40Z kleopatra $
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

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.ListModel;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.UIAction;
import org.jdesktop.swingx.renderer.BooleanValue;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.LabelProvider;
import org.jdesktop.swingx.renderer.MappedValue;

/**
 * A factory for creating check lists. While it is possible to create a custom JXCheckList subclass,
 * the SwingX approach to the check list is to use a standard JXList with custom cell rendering and
 * a specialized model.
 * 
 * @see JXList
 * @see CheckListModel
 */
public final class CheckListFactory {
    //TODO make these properties public?
    private static final String TOGGLE_ACTION_NAME = "checklist.toggle";
    
    @SuppressWarnings("serial")
    private static final ComponentProvider<?> CHECKBOX_PROVIDER = new CheckBoxProvider(
            new MappedValue(null, null, new BooleanValue() {
                @Override
                public boolean getBoolean(Object value) {
                    if (value instanceof CheckListModel.SelectableNode) {
                        return ((CheckListModel.SelectableNode) value).isSelected();
                    }
                    return false;
                }

            })) {
        @Override
        protected boolean getValueAsBoolean(CellContext context) {
            if ((context.getRow() >= 0) && context.getComponent() instanceof JXList) {
                JXList list = (JXList) context.getComponent();
                ListModel model = list.getModel();
                
                //must check size of model; prototype on empty model will OOB otherwise
                if (model.getSize() > 0 && model instanceof CheckListModel) {
                    int modelIndex = list.convertIndexToModel(context.getRow());
                    return ((CheckListModel) model).isChecked(modelIndex);
                }
            }
            return super.getValueAsBoolean(context);
        }

    };
    
    private static final Action TOGGLE_CHECKLIST = new UIAction(TOGGLE_ACTION_NAME) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            
            if (source instanceof JXList && ((JXList) source).isEnabled()) {
                CheckListHandler.getInstance().actionPerformed(e);
            }
        }
    };
    
    private CheckListFactory() {
        //does nothing
    }

    /**
     * Creates a new {@code JXList} as a check list for the specified model.
     * 
     * @param model
     *            the model backing the check list
     * @return a check list configured {@code JXList}
     * @throws NullPointerException
     *             if {@code model} is {@code null}
     */
    public static JXList create(ListModel model) {
        return create(new JXList(model, true));
    }

    /**
     * Creates a new {@code JXList} as a check list for the specified model, using the specified
     * provider for rendering the user object.
     * 
     * @param model
     *            the model backing the check list
     * @param baseProvider
     *            the component provider to use for the user object
     * @return a check list configured {@code JXList}
     * @throws NullPointerException
     *             if {@code model} is {@code null}
     */
    public static JXList create(ListModel model, ComponentProvider<?> baseProvider) {
        return create(new JXList(model, true), baseProvider);
    }

    /**
     * Configures a {@code JXList} as a check list. This method will alter the model and renderer
     * and install necessary UI support. Users should prefer creating new check lists from a
     * {@link #create(ListModel) model}; this method exists for allowing users to configure {@code
     * JXList}s where the models were created implicitly by the constructor.
     * 
     * @param list
     *            the list to configure
     * @return a check list configured {@code JXList}
     * @throws NullPointerException
     *             if {@code list} is {@code null}
     */
    public static JXList create(JXList list) {
        return create(list, null);
    }

    /**
     * Configures a {@code JXList} as a check list, using the specified provider for rendering the
     * user object. This method will alter the model and renderer and install necessary UI support.
     * Users should prefer creating new check lists from a {@link #create(ListModel) model}; this
     * method exists for allowing users to configure {@code JXList}s where the models were created
     * implicitly by the constructor.
     * 
     * @param list
     *            the list to configure
     * @return a check list configured {@code JXList}
     * @param baseProvider
     *            the component provider to use for the user object
     * @throws NullPointerException
     *             if {@code list} is {@code null}
     */
    public static JXList create(JXList list, ComponentProvider<?> baseProvider) {
        //install model
        ListModel model = list.getModel();
        list.setModel(new CheckListModel(model));
        
        //install UI support
        //TODO should we get this InputMap from the UIDefaults?
        InputMap im = list.getInputMap(JComponent.WHEN_FOCUSED);
        im.put(KeyStroke.getKeyStroke(' '), TOGGLE_ACTION_NAME);
        
        ActionMap am = list.getActionMap();
        am.put(TOGGLE_ACTION_NAME, TOGGLE_CHECKLIST);
        
        list.addMouseListener(CheckListHandler.getInstance());

        //install renderer
        ComponentProvider<?> userObjectProvider = baseProvider == null
                ? new LabelProvider() : baseProvider;
        ContainerProvider provider = new ContainerProvider(userObjectProvider, CHECKBOX_PROVIDER);
        list.setCellRenderer(new DefaultListRenderer(provider));
        
        return list;
    }
}
