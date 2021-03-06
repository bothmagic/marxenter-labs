/*
 * $Id: FilterPipeline.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.decorator;

import java.util.BitSet;
import java.util.List;
import java.util.Vector;

import javax.swing.event.EventListenerList;

/**
 * <p>A <b><code>FilterPipeline</code></b> is used to define the set of
 * {@link org.jdesktop.swing.decorator.Filter filters}
 * for a data-aware component such as a {@link org.jdesktop.swing.JXList} or a
 * {@link org.jdesktop.swing.JXTable}. Filtering involves interposing one or
 * more filters in a {@link org.jdesktop.swing.decorator.FilterPipeline} between
 * a data model and a view to change the apparent order and/or number of records
 * in the data model. The order of filters in the filter pipeline determines the
 * order in which each filter is applied. The output from one filter in the
 * pipeline is piped as the input to the next filter in the pipeline.</p>
 *
 * <pre>
 * 	{@link org.jdesktop.swing.decorator.Filter}[]	filters = new {@link org.jdesktop.swing.decorator.Filter}[] {
 * 		new {@link org.jdesktop.swing.decorator.PatternFilter}("S.*", 0, 1),	// regex, matchflags, column
 * 		new {@link org.jdesktop.swing.decorator.ShuttleSorter}(1, false),	// column 1, descending
 * 		new {@link org.jdesktop.swing.decorator.ShuttleSorter}(0, true),	// column 0, ascending
 * 	};
 * 	{@link org.jdesktop.swing.decorator.FilterPipeline}	pipeline = new {@link org.jdesktop.swing.decorator.FilterPipeline}(filters);
 * 	{@link org.jdesktop.swing.JXTable}	table = new {@link org.jdesktop.swing.JXTable}(model);
 *	table.setFilters(pipeline);
 * </pre>
 *
 * This is all you need to do in order to use <code>FilterPipeline</code>. Most
 * of the methods in this class are only for advanced developers who want to write
 * their own filter subclasses and want to override the way a filter pipeline works.
 *
 * @author Ramesh Gupta
 * @see org.jdesktop.swing.decorator.Filter
 */
public class FilterPipeline {
    protected EventListenerList		listenerList = new EventListenerList();
    private ComponentAdapter	adapter = null;
    private Sorter					sorter = null;
    private final Filter[]			filters;

    /**
     * Constructs a new <code>FilterPipeline</code> populated with the specified
     * filters that are applied in the order they appear in the list. Since filters
     * maintain state about the view to which they are attached, an instance of
     * a filter may not ever be used in more than one pipeline.
     *
     * @param inList array of filters
     */
    public FilterPipeline(Filter[] inList) {
        filters = reorderSorters(inList, locateSorters(inList));

        for (int i = 0; i < filters.length; i++) {
            if (filters[i].order < 0) {
                filters[i].order = i;
            }
            else {
                throw new IllegalArgumentException("Element " + i +
                    " is part of another pipeline.");
            }
        }
        // individual filters not bound until adapter is bound
    }

    /**
     * Sets the sorter that the output of the filter pipeline is piped through.
     * This is the sorter that is installed interactively on a view by a user
     * action. This is an implementation detail.
     *
     * @param sorter the interactive sorter, if any; null otherwise.
     */
    void setSorter(Sorter sorter) {
        this.sorter = sorter;
    }

    /**
     * Assigns a {@link org.jdesktop.swing.decorator.ComponentAdapter} to this
     * pipeline if no adapter has previously been assigned to the pipeline. Once an
     * adapter has been assigned to this pipeline, any attempt to change that will
     * cause an exception to be thrown.
     *
     * @param adapter the <code>ComponentAdapter</code> to assign
     * @throws IllegalArgumentException if adapter is null
     * @throws IllegalStateException if an adapter is already assigned to this
     * pipeline and the new adapter is not the same the existing adapter
     */
     public final void assign(ComponentAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("null adapter");
        }

	// also assign individual filters when adapter is bound
        if (this.adapter == null) {
            this.adapter = adapter;
            for (int i = 0; i < filters.length; i++) {
                filters[i].assign(this);
                filters[i].assign(adapter);
            }
        }
        else if (this.adapter != adapter){
            throw new IllegalStateException("Can't bind to a different adapter");
        }
    }

    /**
     * Returns true if this pipeline contains the specified filter;
     * otherwise it returns false.
     *
     * @param filter filter whose membership in this pipeline is tested
     * @return true if this pipeline contains the specified filter;
     * otherwise it returns false
     */
    boolean contains(Filter filter) {
        return (filter.order >= 0) &&
            	(filters.length > 0) && (filters[filter.order] == filter);
    }

    /**
     * Returns the first filter, if any, in this pipeline, or null, if there are
     * no filters in this pipeline.
     *
     * @return the first filter, if any, in this pipeline, or null, if there are
     * no filters in this pipeline
     */
    Filter first() {
        return (filters.length > 0) ? filters[0] : null;
    }

    /**
     * Returns the last filter, if any, in this pipeline, or null, if there are
     * no filters in this pipeline.
     *
     * @return the last filter, if any, in this pipeline, or null, if there are
     * no filters in this pipeline
     */
    Filter last() {
        return (filters.length > 0) ? filters[filters.length - 1] : null;
    }

    /**
     * Returns the filter after the supplied filter in this pipeline,
     * or null, if there aren't any filters after the supplied filter.
     *
     * @param filter a filter in this pipeline
     * @return the filter after the supplied filter in this pipeline,
     * or null, if there aren't any filters after the supplied filter
     */
    Filter next(Filter filter) {
        return last().equals(filter) ? null : filters[filter.order + 1];
    }

    /**
     * Returns the filter before the supplied filter in this pipeline,
     * or null, if there aren't any filters before the supplied filter.
     *
     * @param filter a filter in this pipeline
     * @return the filter before the supplied filter in this pipeline,
     * or null, if there aren't any filters before the supplied filter
     */
    Filter previous(Filter filter) {
        return first().equals(filter) ? null : filters[filter.order - 1];
    }

    /**
     * Called when the specified filter has changed.
     * Cascades <b><code>filterChanged</code></b> notifications to the next
     * filter in the pipeline after the specified filter. If the specified filter
     * is the last filter in the pipeline, this method broadcasts a
     * <b><code>filterChanged</code></b> notification to all
     * <b><code>PipelineListener</code></b> objects registered with this pipeline.
     *
     * @param filter a filter in this pipeline that has changed in any way
     */
    protected void filterChanged(Filter filter) {
        Filter	next = contains(filter) ? next(filter) : null;
        if (next == null) {
            if (sorter == null) {
                fireContentsChanged();
            }
            else {
                sorter.refresh();	// cascade to interposed sorter
            }
        }
        else {
            next.refresh();	// Cascade to next filter
        }
    }

/*
    public int getInputSize() {
        return adapter.getRowCount();
    }
*/
    int getInputSize(Filter filter) {
        if (contains(filter)) {
            Filter	previous = previous(filter);
            if (previous == null) {
                return adapter.getRowCount();
            }
            else {
                return previous.getSize();
            }
        }
        else if (filter.getPipeline() == this) {
            return getOutputSize();
        }

        return 0;
    }

    /**
     * Returns the number of records in the filtered view.
     *
     * @return the number of records in the filtered view
     */
    public int getOutputSize() {
        Filter last = last();
        return (last == null) ? adapter.getRowCount() : last.getSize();
    }

/*
    public int getOutputSize(Filter filter) {
        return (isMember(filter)) ? filter.getSize() : 0;
    }
*/
    /**
     * Convert row index from view coordinates to model coordinates
     * accounting for the presence of sorters and filters. This is essentially
     * a pass-through to the {@link org.jdesktop.swing.decorator.Filter#convertRowIndexToModel(int) convertRowIndexToModel}
     * method of the <em>last</em> {@link org.jdesktop.swing.decorator.Filter},
     * if any, in this pipeline.
     *
     * @param row row index in view coordinates
     * @return row index in model coordinates
     */
    public int convertRowIndexToModel(int row) {
        Filter last = last();
        return (last == null) ? row : last.convertRowIndexToModel(row);
    }

    int convertRowIndexToModel(Filter filter, int row) {
        if (contains(filter)) {
            Filter	previous = previous(filter);
            if (previous == null) {
                return row;
            }
            else {
                return previous.convertRowIndexToModel(row);
            }
        }
        else {
            Filter	last = last();
            if (last == null) {
                return row;
            }
            else {
                return last.convertRowIndexToModel(row);
            }
        }
    }

    /**
     * Convert row index from model coordinates to view coordinates
     * accounting for the presence of sorters and filters. This is essentially
     * a pass-through to the {@link org.jdesktop.swing.decorator.Filter#convertRowIndexToView(int) convertRowIndexToModel}
     * method of the <em>last</em> {@link org.jdesktop.swing.decorator.Filter},
     * if any, in this pipeline.
     *
     * @param row row index in model coordinates
     * @return row index in view coordinates
     */
    public int convertRowIndexToView(int row) {
        Filter last = last();
        return (last == null) ? row : last.convertRowIndexToView(row);
    }

    int convertRowIndexToView(Filter filter, int row) {
        if (contains(filter)) {
            Filter	previous = previous(filter);
            if (previous == null) {
                return row;
            }
            else {
                return previous.convertRowIndexToView(row);
            }
        }
        else {
            Filter	last = last();
            if (last == null) {
                return row;
            }
            else {
                return last.convertRowIndexToView(row);
            }
        }
    }

    /**
     * Returns the value of the cell at the specified coordinates.
     *
     * @param row in view coordinates
     * @param column in model coordinates
     * @return the value of the cell at the specified coordinates
     */
    public Object getValueAt(int row, int column) {
        Filter last = last();
        return (last == null) ? null : last.getValueAt(row, column);
    }


    Object getInputValueFor(Filter filter, int row, int column) {
        if (contains(filter)) {
            Filter	previous = previous(filter);
            if (previous == null) {
                return adapter.getValueAt(row, adapter.modelToView(column));
            }
            else {
                return previous.getValueAt(row, column);
            }
        }
		else {
            Filter	last = last();
            if (last == null) {
                return adapter.getValueAt(row, adapter.modelToView(column));
            }
            else {
                return last.getValueAt(row, column);
            }
        }
    }

    public void setValueAt(Object aValue, int row, int column) {
        Filter last = last();
        if (last != null) {
            last.setValueAt(aValue, row, column);
        }
    }

    void setInputValueFor(Object aValue, Filter filter, int row, int column) {
        if (contains(filter)) {
            Filter previous = previous(filter);
            if (previous == null) {
                adapter.setValueAt(aValue, row, adapter.modelToView(column));
            }
            else {
                previous.setValueAt(aValue, row, column);
            }
        }
        else {
            Filter	last = last();
            if (last == null) {
                adapter.setValueAt(aValue, row, adapter.modelToView(column));
            }
            else {
                last.setValueAt(aValue, row, column);
            }
        }
    }

    public boolean isCellEditable(int row, int column) {
        Filter last = last();
        return (last == null) ? false : last.isCellEditable(row, column);
    }

    boolean isInputEditableFor(Filter filter, int row, int column) {
        /** @todo test this. Why are we calling translateToPreviousFilter? */
        int	inputRow = filter.translateToPreviousFilter(row);
        if (contains(filter)) {
            Filter	previous = previous(filter);
            if (previous == null) {
                return adapter.isCellEditable(inputRow, adapter.modelToView(column));
            }
            else {
                return previous.isCellEditable(inputRow, column);
            }
        }
        else {
            Filter	last = last();
            if (last == null) {
                return adapter.isCellEditable(inputRow, adapter.modelToView(column));
            }
            else {
                return last.isCellEditable(inputRow, column);
            }
        }
    }

    /**
     * Flushes the pipeline by initiating a {@link org.jdesktop.swing.decorator.Filter#refresh() refresh}
     * on the <em>first</em> {@link org.jdesktop.swing.decorator.Filter filter},
     * if any, in this pipeline. After that filter has refreshed itself, it sends a
     * {@link #filterChanged(org.jdesktop.swing.decorator.Filter) filterChanged}
     * notification to this pipeline, and the pipeline responds by initiating a
     * {@link org.jdesktop.swing.decorator.Filter#refresh() refresh}
     * on the <em>next</em> {@link org.jdesktop.swing.decorator.Filter filter},
     * if any, in this pipeline. Eventualy, when there are no more filters left
     * in the pipeline, it broadcasts a {@link org.jdesktop.swing.decorator.PipelineEvent}
     * signaling a {@link org.jdesktop.swing.decorator.PipelineEvent#CONTENTS_CHANGED}
     * message to all {@link org.jdesktop.swing.decorator.PipelineListener} objects
     * registered with this pipeline.
     */
    public void flush() {
        if ((filters != null) && (filters.length > 0)) {
            filters[0].refresh();
        }
        else if (sorter != null) {
            sorter.refresh();
        }
    }

    /**
     * Adds a listener to the list that's notified each time there is a change
     * to this pipeline.
     *
     * @param l the <code>PipelineListener</code> to be added
     */
    public void addPipelineListener(PipelineListener l) {
        listenerList.add(PipelineListener.class, l);
    }

    /**
     * Removes a listener from the list that's notified each time there is a change
     * to this pipeline.
     *
     * @param l the <code>PipelineListener</code> to be removed
     */
    public void removePipelineListener(PipelineListener l) {
        listenerList.remove(PipelineListener.class, l);
    }

    /**
     * Returns an array of all the pipeline listeners
     * registered on this <code>FilterPipeline</code>.
     *
     * @return all of this pipeline's <code>PipelineListener</code>s,
     *         or an empty array if no pipeline listeners
     *         are currently registered
     *
     * @see #addPipelineListener
     * @see #removePipelineListener
     */
    public PipelineListener[] getPipelineListeners() {
        return (PipelineListener[]) listenerList.getListeners(
            PipelineListener.class);
    }

    /**
     * Notifies all registered {@link org.jdesktop.swing.decorators.PipelineListener}
     * objects that the contents of this pipeline has changed. The event instance
     * is lazily created.
     */
    protected void fireContentsChanged() {
        Object[] listeners = listenerList.getListenerList();
        PipelineEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PipelineListener.class) {
                if (e == null) {
                    e = new PipelineEvent(this, PipelineEvent.CONTENTS_CHANGED);
                }
                ( (PipelineListener) listeners[i + 1]).contentsChanged(e);
            }
        }
    }

    private List locateSorters(Filter[] inList) {
        BitSet	sortableColumns = new BitSet();	// temporary structure for checking
        List	sorterLocations = new Vector();
        for (int i = 0; i < inList.length; i++) {
            if (inList[i] instanceof Sorter) {
                int	columnIndex = inList[i].getColumnIndex();
                if (columnIndex < 0) {
                    throw new IndexOutOfBoundsException(
                        "Negative column index for filter: " + inList[i]);
                }

                if (sortableColumns.get(columnIndex)) {
                    throw new IllegalArgumentException(
                        "Filter "+ i +" attempting to overwrite sorter for column "
                        + columnIndex);
                }

                sortableColumns.set(columnIndex);		// mark column index
                sorterLocations.add(new Integer(i));	// mark sorter index
                //columnSorterMap.put(new Integer(columnIndex), inList[i]);
            }
        }
        return sorterLocations;
    }

    private Filter[] reorderSorters(Filter[] inList, List sorterLocations) {
        // always returns a new copy of inList
        Filter[]	outList = (Filter[]) inList.clone();

        // Invert the order of sorters, if any, in outList
        int max = sorterLocations.size() - 1;
        for (int i = 0; i <= max; i++) {
            int orig = ((Integer) sorterLocations.get(max - i)).intValue();
            int copy = ((Integer) sorterLocations.get(i)).intValue();
            outList[copy] = inList[orig];
        }

        return outList;
    }
/*
    private void bind(Filter filter) {
        filter.bind(this);
        filter.bind(adapter);
    }
*/
}
