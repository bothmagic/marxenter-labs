/*
 * $Id: DefaultListViewModel.java 878 2006-09-22 13:59:58Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *  A default implementation of ListViewModel. Can be easily sub-classed to
 *  provide more specific information.
 *
 * @author Rob Stone
 */
public class DefaultListViewModel extends DefaultTableModel implements ListViewModel
{
    private ImageIcon[] smallIcons;
    private ImageIcon[] largeIcons;

    /**
     * Default constructor
     */
    public DefaultListViewModel()
    {
    }

    /**
     * Create a list view model.
     * @param data the model data
     * @param columnNames the names of the columns as displayed in the details view
     * @param smallIcons the icons to be used for the 'small' icon view modes
     * @param largeIcons the icons to be used for the 'large' icon view modes
     */
    public DefaultListViewModel(
        final Object[][] data,
        final String[] columnNames,
        final ImageIcon[] smallIcons,
        final ImageIcon[] largeIcons)
    {
        super(data, columnNames);
        this.smallIcons=smallIcons;
        this.largeIcons=largeIcons;
    }

    /*
     * See interface
     */
    public String getDisplayValue(final int rowIndex, final int columnIndex)
    {
        return null;
    }
    
    /*
     * See interface
     */
    public ImageIcon getSmallIcon(final int rowIndex)
    {
        return smallIcons==null || rowIndex>smallIcons.length ? null : smallIcons[rowIndex];
    }

    /*
     * See interface
     */
    public ImageIcon getLargeIcon(final int rowIndex)
    {
        return largeIcons==null || rowIndex>largeIcons.length ? null : largeIcons[rowIndex];
    }

    /*
     * See interface
     */
    public String getToolTipText(final int rowIndex, final int columnIndex)
    {
        return null;
    }
}
