package org.jdesktop.swingx.table;

import org.jdesktop.swingx.decorator.Filter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * This filter implementation will offload its work to another thread if that
 * work is taking too long.  It is designed to allow filtering after each keystroke
 * a user makes into a text field, while still allowing the textfield to seem
 * responsive.
 *
 * When the filter's criteria changes, the filter tries to run as any other.  After
 * a few hundred milliseconds, if processing isn't finished, it continues (possibly
 * leaving the internal model in an inconsistant state.  When processing is finished,
 * the model is updated, and a notification is sent to downstream filters.  If the
 * criteria changes again, while processing is still going, the original processing
 * is abandoned, and the results are ignored.  The user only sees the last sucessfull
 * filter results.
 *
 * Between the time the filtering began running asynchronously, and when in
 * completes, this filter may be holding on to invalid state. In that situation,
 * it is possible that this class will be providing incorrect information (which
 * will be corrected when filtering finishes.  It will, however always return safely.
 *
 * TODO a flag or event can be added to disable the table, or elements of the screen until processing is done... is this necessary?
 *
 * @author Bryan Young
 */
public class AsynchronousFilter extends Filter {

    private static final int TIMEOUT_BEFORE_GOING_ASYNC = 100;

    private ArrayList<Integer> toPrevious = new ArrayList<Integer>();
    private ArrayList<Integer> filterableColumns;
    private InteruptableFilterStrategy strategy;
    private ExecutorService executor = Executors.newFixedThreadPool(1);
    private Future currentTask;

    public AsynchronousFilter(InteruptableFilterStrategy strategy) {
        this.strategy = strategy;
        strategy.init(this);
    }

    /**
     * Between the time the filtering begins, and when in completes, this filter
     * may be holding on to invalid state. In that situation, it is possible that
     * this method will return incorrect information (which will be corrected when
     * filtering finishes.  It will, however always return safely.
     */
    public int getSize() {
        int result = toPrevious.size();
        if (result >= getInputSize())
            return getInputSize();
        else
            return result;
    }

    protected void init() {
    }

    private void setAllColumnsFilterable() {
        filterableColumns = new ArrayList<Integer>();
        int columnCount = adapter.getColumnCount();
        for (int i = 0; i < columnCount; i++)
            filterableColumns.add(i);
    }

    protected void reset() {
        if (filterableColumns == null)
            setAllColumnsFilterable();
        toPrevious.clear();
        int inputSize = getInputSize();
        fromPrevious = new int[inputSize];  // fromPrevious is inherited protected
        for (int i = 0; i < inputSize; i++) {
            fromPrevious[i] = -1;
        }
    }

    protected void filter() {

        if (currentTask != null)
            currentTask.cancel(true);

        // get all values and pass in final (thread-safe) copies
        final int rowCount = getInputSize();
        final ArrayList<Integer> tempToPrevious = new ArrayList<Integer>();
        final int[] tempFromPrevious = new int[rowCount];
        final ArrayList<Integer> tempFilterableColumns = new ArrayList<Integer>(filterableColumns);

        Runnable task = new Runnable() {
            public void run() {

                int current = 0;
                for (int rowIdx = 0; rowIdx < rowCount && !Thread.currentThread().isInterrupted(); rowIdx++) {
                    //pause();
                    for (int colIdx = 0; colIdx < tempFilterableColumns.size(); colIdx++) {
                        if (matchFound(rowIdx, colIdx, tempFilterableColumns)) {
                            tempToPrevious.add(rowIdx);
                            tempFromPrevious[rowIdx] = current++; // generate inverse map entry while we are here
                            break;
                        }
                    }
                }
                if (!currentTask.isCancelled()) {
                    // update the UI from the Event Thread
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            toPrevious = tempToPrevious;
                            fromPrevious = tempFromPrevious;
                            fireFilterChanged();
                        }
                    });
                }
            }
        };

        currentTask = executor.submit(task);

        attemptToRunSynchronously();
    }

    /**
     * Try to let it run synchronously... if it takes more than a few hundred milliseconds, move on.
     * The intention is to minimize the calls to fireFilterChanged().
     */
    private void attemptToRunSynchronously() {
        try {
            currentTask.get(TIMEOUT_BEFORE_GOING_ASYNC, TimeUnit.MILLISECONDS);
            System.out.println("finished synchronously");
        } catch (InterruptedException e) {
            // this shouldn't happen... interuptions are only expected by the next call of filter (which should be from the event queue)
            throw new IllegalStateException("A thread controlled b AsynchronousFilter was interrupted unexpectedly", e);
        } catch (ExecutionException e) {
            // this shouldn't happen... I guess if there were a runtime exception?
            throw new RuntimeException("Unexpected exception while filtering", e);
        } catch (TimeoutException e) {
            // this is expected... ignoring it lets the task finish on its own
            System.out.println("finishing asynchronously");
        }
    }


    /**
     * DataProvider data has a 'happens-before' relationship with the filtering logic because it was submitted to
     * the executor.  This only guarantees visibilty of data submitted at that point or later.  Since dataprovider
     * is being manipulated from the swing event thread, it is not safe from syncronization issues.  Any
     * inconsistencies should report 'safe' values such as an empty string.  Changes to the data provider will
     * cause the filter to re-run shortly, correcting the results.
     * TODO should we note this in the strategy? Is there anything we can do about it to make things less confusing?
     */
    private boolean matchFound(int row, int colIdx, ArrayList<Integer> filterableColumns) {
        // ask the adapter if the column should be included (may not be visible, for example)
        if (!adapter.isTestable(colIdx) || !filterableColumns.contains(colIdx))
            return false;

        Object value = getInputValue(row, colIdx);
        return value != null && strategy.isIncluded(value);
    }

    /**
     * Between the time the filtering begins, and when in completes, this filter
     * may be holding on to invalid state. In that situation, it is possible that
     * this method will return incorrect information (which will be corrected when
     * filtering finishes.  It will, however always return safely.
     */
    protected int mapTowardModel(int row) {
        Integer result = toPrevious.get(row);
        if (result >= getInputSize())
            return getInputSize();
        else
            return result;
    }

    public int getColumnIndex() {
        throw new UnsupportedOperationException();
    }

    public void setColumnIndex(int modelColumn) {
        throw new UnsupportedOperationException();
    }
}