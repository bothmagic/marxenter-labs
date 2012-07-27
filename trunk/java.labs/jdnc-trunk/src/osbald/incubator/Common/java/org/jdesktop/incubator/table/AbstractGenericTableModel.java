package org.jdesktop.incubator.table;

import org.jdesktop.incubator.util.DynamicIntArray;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 27-Nov-2006
 * Time: 11:16:28
 */

public abstract class AbstractGenericTableModel<T> extends AbstractTableModel {
    protected List<T> values = new ArrayList<T>();

    public AbstractGenericTableModel() {
    }

    public AbstractGenericTableModel(T... values) {
        this(new ArrayList<T>(Arrays.asList(values)));
    }

    public AbstractGenericTableModel(Collection<? extends T> values) {
        this(new ArrayList<T>(values));
    }

    public AbstractGenericTableModel(List<T> values) {
        this.values = values;
    }

    public abstract String getColumnName(int column);

    protected List<T> getValues() {
        return Collections.unmodifiableList(values);
    }

    public void setValues(Collection<? extends T> values) {
        setValues(new ArrayList<T>(values));
    }

    public void setValues(List<T> values) {
        this.values = values;
        fireTableDataChanged();
    }

    public int getRowCount() {
        return values.size();
    }

    public T getValue(int row) {
        return values.get(row);
    }

    public int indexOf(T value) {
        return getValues().indexOf(value);
    }

    public void add(T value) {
        values.add(value);
        int row = getRowCount() - 1;
        fireTableRowsInserted(row, row);
    }

    public void update(T oldvalue, T newValue) {
        int row = values.indexOf(oldvalue);
        values.set(row, newValue);
        fireTableRowsUpdated(row, row);
    }

    public void remove(int row) {
        values.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void remove(T value) {
        removeAll(Collections.singletonList(value));
    }

    //TODO try complicated version below (updates more efficient for big jobs?)
    public void removeAll(Collection<?> values) {
        if (values.size() > 0) {
            this.values.removeAll(values);
            fireTableDataChanged();
        }
    }

    //TODO not tested! *replaces* removeAll above
    public boolean removeAllInstances(Collection<?> c) {
        boolean modified = false;
        if (c.size() > 0) {
            DynamicIntArray removed = new DynamicIntArray(10, c.size());
            for (int i = values.size() - 1; i >= 0; i--) {
                if (c.contains(values.get(i))) {
                    values.remove(i);
                    removed.add(i);
                    modified = true;
                }
            }
            // let listeners know how our model has changed
            Collection<int[]> sequences =
                    TableUtils.collateSequences(removed.toArray());
            for (int[] range : sequences) {
                fireTableRowsDeleted(range[0], range[range.length - 1]);
            }
        }
        return modified;
    }

    //TODO not tested!
    public Collection<T> removeAll(int... rows) {
        if (rows == null || rows.length == 0) {
            return Collections.emptyList();
        }
        Collection<T> removed = new ArrayList<T>(rows.length);
        Collection<int[]> sequences = TableUtils.collateSequences(rows);
        for (int[] range : sequences) {
            for (int i = range[range.length - 1]; i >= 0; i--) {
                removed.add(this.values.remove(i));
            }
            fireTableRowsDeleted(range[0], range[range.length - 1]);
        }
        return removed;
    }

    public void clear() {
        values.clear();
        fireTableDataChanged();
    }
    
}
