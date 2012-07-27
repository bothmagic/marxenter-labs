package org.jdesktop.incubator.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Provides an EditableListModel version of GnericListModel.
 * Will fire the appropriate ListDataEvents when modifications to the model are made.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 02-Feb-2006
 * Time: 11:12:41
 */

public class GenericEditableListModel<T> extends GenericListModel<T> implements EditableListModel<T> {

    public GenericEditableListModel() {
    }

    public GenericEditableListModel(Collection<T> values) {
        super(values);
    }

    public GenericEditableListModel(T... values) {
        super(values);
    }

    public boolean isEditable(int index) {
        return true;
    }

    public boolean add(T value) {
        boolean result;
        if (result = this.values.add(value)) {
            fireIntervalAdded(this, getSize() - 1, getSize() - 1);
        }
        return result;
    }

    public boolean addUnique(T value) {
        return indexOf(value) < 0 && add(value);
    }

    public boolean addAll(Collection<T> values) {
        int index = getSize();
        boolean result;
        if (result = this.values.addAll(values)) {
            fireIntervalAdded(this, index, getSize() - 1);
        }
        return result;
    }

    public boolean addAll(T... values) {
        return addAll(Arrays.asList(values));
    }

    public boolean insert(int index, T value) {
        if (index > -1 && index < getSize()) {
            this.values.add(index, value);       // NB no return value?!
            fireIntervalAdded(this, index, index);
            return value.equals(getElementAt(index));
        }
        return false;
    }

    public T replace(int index, T value) {
        if (isEditable(index)) {
            T result = this.values.set(index, value);
            if (result != null) {
                fireContentsChanged(this, index, index);
            }
            return result;
        } else {
            return null;
        }
    }

    public boolean replace(T source, T target) {
        int index = indexOf(source);
        return index > -1 && source.equals(replace(index, target));
    }

    public boolean remove(T target) {
        int index = indexOf(target);
        return index > -1 && target.equals(remove(index));
    }

    public T remove(int index) {
        if (isEditable(index)) {
            T result = values.remove(index);
            fireIntervalRemoved(this, index, index);
            return result;
        } else {
            return null;
        }
    }

    /*
    // lazy developer impl
    public Collection<T> removeAll(Collection<T> targets) {
        if (targets.size() > 0) {
            if (this.values.removeAll(targets)) {
                fireContentsChanged(this, 0, getSize() - 1);
                return targets;
            }
        }
        return Collections.emptyList();
    }
    */

    // the complicated version
    // TODO benchmark alternatives for large numbers of rows?

    public Collection<T> removeAll(Collection<T> targets) {
        int index, count = 0;
        int[] rows = new int[targets.size()];
        Arrays.fill(rows, -1);
        for (T target : targets) {
            if ((index = findIndex(rows, 0, target)) > -1) {
                rows[count++] = index;
            }
        }
        if (count == rows.length) {
            return removeAll(rows);
        } else {
            int[] slice = new int[count];
            System.arraycopy(rows, 0, slice, 0, count);
            return removeAll(slice);
        }
    }

    // if duplicates are found tries to find index of next occurrence (or should we nuke all?)
    int findIndex(int[] rows, int start, T target) {
        int index = indexOf(target, start);
        if (index > -1) {
            if (!containsRow(rows, index)) {
                return index;
            } else {
                return findIndex(rows, index + 1, target);
            }
        }
        return -1;
    }

    // prevents duplicate row indexes
    boolean containsRow(int[] rows, int row) {
        for (int i = rows.length - 1; i > -0; i--) {
            if (row == rows[i]) {
                return true;
            }
        }
        return false;
    }

    public Collection<T> removeAll(T... targets) {
        if (targets != null && targets.length > 0) {
            return removeAll(Arrays.asList(targets));
        } else {
            return Collections.emptyList();
        }
    }

    // try to batch interval updates as much as possible
    public Collection<T> removeAll(int[] rows) {
        if (rows.length == 0) {
            return Collections.emptyList();
        }
        Arrays.sort(rows);
        Collection<T> removed = new ArrayList<T>(rows.length);
        int highIndex = rows.length - 1;
        for (int i = highIndex; i > -1 && rows[i] > -1; i--) {
            removed.add(this.values.remove(rows[i]));
            if (i < highIndex && rows[i + 1] - rows[i] > 1) {
                fireIntervalRemoved(this, rows[i + 1], rows[highIndex]);
                highIndex = i;
            }
        }
        if (!removed.isEmpty()) {
            fireIntervalRemoved(this, rows[0], rows[highIndex]);
        }
        return removed;
    }

    public void clear() {
        int lastIndex = getSize() - 1;
        if (lastIndex > -1) {
            this.values.clear();
            fireIntervalRemoved(this, 0, lastIndex);
        }
    }
}
