/*
 * $Id: Filter.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.decorator;

/**
 * <p>A <b><code>Filter</code></b> is used to filter the data presented in a
 * data-aware component such as a {@link org.jdesktop.swing.JXList} or a
 * {@link org.jdesktop.swing.JXTable}. Filtering involves interposing one or
 * more filters in a {@link org.jdesktop.swing.decorator.FilterPipeline} between
 * a data model and a view to change the apparent order and/or number of records
 * in the data model.</p>
 *
 * @author Ramesh Gupta
 * @see org.jdesktop.swing.decorator.FilterPipeline
 * @see org.jdesktop.swing.JXTable
 */
public abstract class Filter {
    private final int column;		// in model coordinates
    private FilterPipeline		pipeline = null;
    protected ComponentAdapter	adapter = null;	/** @todo make private */
    int order = -1;	// package private

    /**
     * Constructs a new filter for the first column of a data model (in model coordinates).
     */
    public Filter() {
        this(0);
    }

    /**
     * Constructs a new filter for the specified column of a data model (in model coordinates).
     *
     * @param col column index in model coordinates
     */
    public Filter(int col) {
        column = col;
        init();
    }

    /**
     * Provides filter-specific initialization. Called from the <code>Filter</code>
     * constructor.
     */
    protected abstract void init();

    /**
     * Resets the internal row mappings from this filter to the previous filter.
     */
    protected abstract void reset();

    /**
     * Generates the row mappings from the previous filter to this filter.
     */
    protected abstract void generateMappingFromPrevious();

    /**
     * Performs the filter operation defined by this filter.
     */
    protected abstract void filter();

    /**
     * Refreshes the internal state of the filter, performs the {@link #filter() filter}
     * operation and regenerates row mappings from the previous filter. If this
     * filter is bound to a filter pipeline (as most filters are), it also triggers a
     * {@link org.jdesktop.swing.decorator.FilterPipeline#filterChanged(org.jdesktop.swing.decorator.Filter) filterChanged}
     * notification.
     */
    public void refresh() {
        refresh(true);
    }

    /**
     * Refreshes the internal state of the filter, optionally resetting the
     * cache of existing row mappings from this filter to the previous filter.
     * Always performs the {@link #filter() filter} operation and regenerates
     * row mappings from the previous filter. If this filter is bound to a filter
     * pipeline (as most filters are), it also triggers a
     * {@link org.jdesktop.swing.decorator.FilterPipeline#filterChanged(org.jdesktop.swing.decorator.Filter) filterChanged}
     * notification.
     *
     * @param reset true if existing row mappings from this filter to the previous
     * filter should be reset; false, if the existing row mappings should be preserved.
     */
    protected void refresh(boolean reset) {
        if (reset) {
            reset();
        }

        filter();
        generateMappingFromPrevious();

        // trigger direct notification; will cascade to next in pipeline, if any
        if (pipeline != null) {
            if (pipeline.contains(this)) {
                pipeline.filterChanged(this);
                return;
            }
        }
        if (adapter != null) {
            adapter.refresh();
        }
    }

    /**
     * Binds this filter to the specified <code>ComponentAdapter</code>.
     * Called by {@link org.jdesktop.swing.decorator.FilterPipeline#bind(org.jdesktop.swing.decorator.ComponentAdapter) FilterPipeline.bind()}.
     *
     * @param adapter adapter that this filter is bound to
     */
    final void assign(ComponentAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("null adapter");
        }

        if (this.adapter == null) {
            this.adapter = adapter;
        }
        else if (this.adapter != adapter){
            throw new IllegalStateException("Already bound to another adapter");
        }
    }

    /**
     * Binds this filter to the specified filter pipeline.
     * Called by {@link org.jdesktop.swing.decorator.FilterPipeline#bind(org.jdesktop.swing.decorator.ComponentAdapter) FilterPipeline.bind()}.
     *
     * @param pipeline the filter pipeline that this filter is bound to
     */
    final void assign(FilterPipeline pipeline) {
        /** NOTE: JXTable.resetSorter may pass in null for filter pipeline!
        if (pipeline == null) {
            throw new IllegalArgumentException("null pipeline");
        }
		*/

        if ((this.pipeline == null) || (pipeline == null)) {
            this.pipeline = pipeline;
        }
        else if (this.pipeline != pipeline) {
            throw new IllegalStateException("Already bound to another pipeline");
        }
    }

/*
    protected boolean memberOf(FilterPipeline pipeline) {
        if (order < 0) {
            return false;
        }

        return pipeline == null ? false : pipeline.isMember(this);
    }
*/
    protected FilterPipeline getPipeline() {
        return pipeline;
    }

    /**
     * Returns the model index of the column that this filter has been bound to.
     *
     * @return the model index of the column that this filter has been bound to
     */
    public int getColumnIndex() {
        return column;	// model coordinates
    }

    public String getColumnName() {
        if (adapter == null) {
            return "Column " + column;	// in model coordinates :-(
        }
        else {
            int	viewColumnIndex = adapter.modelToView(getColumnIndex());
            return viewColumnIndex < 0 ?
                "" : adapter.getColumnName(viewColumnIndex);
        }
    }

/*
    public void contentsChanged(PipelineEvent ev) {
		// Do nothing
    }
*/

    /**
     * Convert row index from view coordinates to model coordinates
     * accounting for the presence of sorters and filters.
     *
     * @param row row index in view coordinates
     * @return row index in model coordinates
     */
    public int convertRowIndexToModel(int row) {
        if ((order == 0) || (pipeline == null)) {
            return translateToPreviousFilter(row);
        }
        else {
            return pipeline.convertRowIndexToModel(this, translateToPreviousFilter(row));
        }
    }

    /**
     * Convert row index from model coordinates to view coordinates
     * accounting for the presence of sorters and filters.
     *
     * @param row row index in model coordinates
     * @return row index in view coordinates
     */
    public int convertRowIndexToView(int row) {
        if ((order == 0) || (pipeline == null)) {
            return translateFromPreviousFilter(row);
        }
        else {
            return translateFromPreviousFilter(pipeline.convertRowIndexToView(this, row));
        }
    }

    /**
     * Returns the number of records that remain in this filter's "view"
     * after the input records have been filtered.
     *
     * @return the number of records that remain in this filter's "view"
     * after the input records have been filtered
     */
    public abstract int getSize();

    /**
     * Returns the row in this filter that maps to the specified row in the
     * previous filter. If there is no previous filter in the pipeline, this returns
     * the row in this filter that maps to the specified row in the data model.
     * This method is called from
     * {@link org.jdesktop.swing.decorator.Filter#convertRowIndexToView(int) convertRowIndexToView}
     *
     * @param row a row index in the previous filter's "view" of the data model
     * @return the row in this filter that maps to the specified row in
     * the previous filter
     */
    protected abstract int translateFromPreviousFilter(int row);

    /**
     * Returns the row in the previous filter that maps to the specified row in
     * this filter. If there is no previous filter in the pipeline, this returns
     * the row in the data model that maps to the specified row in this filter.
     * This method is called from
     * {@link org.jdesktop.swing.decorator.Filter#convertRowIndexToModel(int) convertRowIndexToModel}
     *
     * @param row a row index in this filter's "view" of the data model
     * @return the row in the previous filter that maps to the specified row in
     * this filter
     */
    protected abstract int translateToPreviousFilter(int row);

    /**
     * Returns the value at the specified row and column.
     *
     * @param row row index in view coordinates
     * @param column column index in model coordinates
     * @return the value at the specified row and column
     */
	public Object getValueAt(int row, int column) {
        if ((order == 0) || (pipeline == null)) {
            return adapter.getValueAt(translateToPreviousFilter(row), adapter.modelToView(column));
        }
        else {
            return pipeline.getInputValueFor(this, translateToPreviousFilter(row), column);
        }
    }

    /**
     * Sets the specified value as the new value for the cell identified by the
     * specified row and column index.
     *
     * @param aValue new value for the specified cell
     * @param row row index in view coordinates
     * @param column column index in model coordinates
     */
    public void setValueAt(Object aValue, int row, int column) {
        if ((order == 0) || (pipeline == null)) {
            adapter.setValueAt(aValue, translateToPreviousFilter(row), adapter.modelToView(column));
        }
        else {
            pipeline.setInputValueFor(aValue, this, translateToPreviousFilter(row), column);
        }
    }

    public boolean isCellEditable(int row, int column) {
        if ((order == 0) || (pipeline == null)) {
            return adapter.isCellEditable(row, adapter.modelToView(column));
        }
        else {
            return pipeline.isInputEditableFor(this, row, column);
        }
    }

    /**
     * Returns the number of records that are processed by this filter.
     *
     * @return the number of records that are processed by this filter
     */
    protected int getInputSize() {
        return pipeline == null ? adapter == null ?
            	0 : adapter.getRowCount() : pipeline.getInputSize(this);
    }

    /**
     * Returns the value of the cell at the specified row and column (in model coordinates).
     *
     * @param row in the coordinates of what is the filter's "view" of the model
     * @param column in model coordinates
     * @return the value of the cell at the specified row and column (in model coordinates)
     */
    protected Object getInputValue(int row, int column) {
        if (pipeline != null) {
            return pipeline.getInputValueFor(this, row, column);
        }
        else if (adapter != null) {
            return adapter.getValueAt(row, adapter.modelToView(column));
        }

        return null;
    }

}

