package org.jdesktop.incubator.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/*
 * In theory a slightly more efficient version of AbstractGenericTableModel that trades off
 * some safety precautions. Saving is made by not making a defensive copies of values when set.
 * This also means you have to be extra careful not to update any Lists passed to the model.
 *
 * Note: Only consider when juggling huge table models that update very frequently
 * (like polls & updates every few seconds).
 *
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 03-Jul-2008
 * Time: 11:16:28
 */

/*TODO Re: Slow updates
   Speed saving isn't quite as much as I'd hoped only trashes slightly less heap (no ref copies).
   What else can make large model updates so slow? sorted tables? (JXTable, RowSorter) or
   SelectionModel? (ref Jasper Potts) or simply the RMI/Network? ..all of the above??! <g>
*/

public abstract class UnsafeGenericTableModel<T> extends AbstractGenericTableModel<T> {

    public UnsafeGenericTableModel() {
    }

    public UnsafeGenericTableModel(Collection<? extends T> values) {
        this(new ArrayList<T>(values));
    }

    public UnsafeGenericTableModel(T... values) {
        this(Arrays.asList(values));
    }

    public UnsafeGenericTableModel(List<T> values) {
        this.values = values;
    }

    public void setValues(List<T> values) {
        this.values = values;
        fireTableDataChanged();
    }
}