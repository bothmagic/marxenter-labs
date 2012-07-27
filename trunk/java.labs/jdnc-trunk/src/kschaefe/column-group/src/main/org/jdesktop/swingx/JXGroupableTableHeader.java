/*
 * $Id: JXGroupableTableHeader.java,v 1.7 2006/03/31 06:51:23 evickroy Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import javax.swing.plaf.TableHeaderUI;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.plaf.GroupableTableHeaderUI;
import org.jdesktop.swingx.plaf.JXGroupableTableHeaderAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

/**
 * JXGroupableTableHeader
 * 
 * @author Karl Schaefer
 * @author Erik Vickroy (original ideas)
 */
public class JXGroupableTableHeader extends JXTableHeader {

    public final static String muiClassID = "swingx/GroupableTableHeaderUI";

    // ensure at least the default ui is registered
    static {
        LookAndFeelAddons.contribute(new JXGroupableTableHeaderAddon());
    }

    public JXGroupableTableHeader() {
        super();
    }

    public JXGroupableTableHeader(TableColumnModel columnModel) {
        super(columnModel);
    }

//    /**
//     * {@inheritDoc}
//     */
//    public int columnAtPoint(Point point) {
//        return ((GroupableTableHeaderUI) getUI()).columnAtPoint(point);
//    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUI() {
        setUI((TableHeaderUI) LookAndFeelAddons.getUI(this, TableHeaderUI.class));
    }

    
    /**
     * Sets the L&F object that renders this component.
     * 
     * @param ui
     *            the <code>TaskPaneUI</code> L&F object
     * @see javax.swing.UIDefaults#getUI
     * @beaninfo bound: true
     *          hidden: true
     *     description: The UI object that implements the header's LookAndFeel.
     */
    public void setUI(TableHeaderUI ui) {
        if (!(ui instanceof GroupableTableHeaderUI)) {
            throw new Error("yuck!"); //FIXME real error here
        }
        
        super.setUI(ui);
    }

    /**
     * Returns the name of the L&F class that renders this component.
     * 
     * @return the string {@link #uiClassID}
     * @see javax.swing.JComponent#getUIClassID
     * @see javax.swing.UIDefaults#getUI
     */
    public String getUIClassID() {
        return muiClassID;
    }

}
