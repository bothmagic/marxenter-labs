/*
 * $Id: JXSummaryTable.java 839 2006-08-03 14:22:35Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.SummaryFilterPipeline;

/**
 * A specialised JXTable that always keeps its last row in the same place regardless
 * of sorting. This row can then be used to display a summary of the other data
 * in the model. This solution was inspired by the description of a similar
 * technique given by 'evickroy' in the following thread at java.net:
 *   http://forums.java.net/jive/thread.jspa?messageID=116447
 *
 * @author Rob Stone
 */
public class JXSummaryTable extends JXTable {
    private boolean summaryOn=true;
    
    /**
     * See JXTable
     */
    public JXSummaryTable() {
        super();
    }
    
    /**
     * See JXTable
     */
    public JXSummaryTable(TableModel dm) {
        super(dm);
    }

    /**
     * See JXTable
     */
    public JXSummaryTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
    }

    /**
     * See JXTable
     */
    public JXSummaryTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
    }
    
    /**
     * See JXTable
     */
    public JXSummaryTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    /**
     * See JXTable
     */
    public JXSummaryTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
    }

    /**
     * See JXTable
     */
    public JXSummaryTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        boolean isCellEditable=super.isCellEditable(row, column);
        return isCellEditable && row<(summaryOn ? getRowCount()-1 : getRowCount());
    }

    /**
     * Sets the filter pipeline for this table. Note that supplying a filter
     * pipeline <b>WILL</b> break the SummaryTable <b>UNLESS</b> the supplied
     * filter pipeline does everything necessary to ensure that the last row is
     * always the last row (ie. make sure any custom sorts always leave the last
     * row in the correct place). See the SummaryFilterPipeline and
     * SummarySorter classes for the details.
     * 
     * There is probably a much, much, better way of doing this, but for me this
     * hack provides all of the necessary functionality.
     *
     * @param pipeline the filter pipeline.
     */
    @Override
    public void setFilters(FilterPipeline pipeline) {
        /**
         * This will all break if a filter pipeline is ever passed in !! For
         * my use case this will never happen
         */
        if (pipeline==null) {
            pipeline=new SummaryFilterPipeline();
        } else {
            if (!(pipeline instanceof SummaryFilterPipeline))
            {
                throw new UnsupportedOperationException("Filters for JXSummaryTable must be subclasses of SummaryFilterPipelin");
            }
        }
        super.setFilters(pipeline);
    }    

    public boolean isSummaryOn()
    {
        return summaryOn;
    }

    public void setSummaryOn(final boolean summaryOn)
    {
        final boolean old=this.summaryOn;
        this.summaryOn = summaryOn;
        if (old!=summaryOn) {
            if (summaryOn) {
                setFilters(null);
            } else {
                super.setFilters(null);
            }
        }
        firePropertyChange("summaryOn", old, summaryOn);
    }
}
