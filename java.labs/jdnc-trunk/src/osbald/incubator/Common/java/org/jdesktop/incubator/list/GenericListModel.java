package org.jdesktop.incubator.list;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Provides a basic ListModel implemented by an ArrayList, rather then DefaultListModel's Vector.
 * This implementation doesn't include editing methods, although the values can be changed and
 * will automatically fire a ListDataEvent.CONTENTS_CHANGED event.
 * <p/>
 * Model can be constructed from a Collection, an Array of the specified type or vargs.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 02-Feb-2006
 * Time: 11:31:44
 */

//PENDING as underlying interface isn't generics-aware assigning to interface loses generic info and leads to warnings
//PENDING ..which is lesser evil referring to concrete classes or excess casts and lack of any type safety?

public class GenericListModel<T> extends AbstractListModel {
    /**
     * exposed for subclasses as getter returns an unmodifiable wrapper (defensive)
     */
    protected List<T> values;

    public GenericListModel() {
        this.values = new ArrayList<T>();
    }

    public GenericListModel(Collection<T> values) {
        this.values = new ArrayList<T>(values);
    }

    public GenericListModel(T... values) {
        if (values != null && values.length > 0) {
            //NB consistent in that internal values are always modifiable but wasteful in this context?
            this.values = new ArrayList<T>(Arrays.asList(values));
        } else {
            this.values = new ArrayList<T>();
        }
    }

    public T getElementAt(int index) {
        return this.values.get(index);
    }

    public int getSize() {
        return this.values.size();
    }

    public List<T> getValues() {
        return Collections.unmodifiableList(this.values);
    }

    public void setValues(Collection<T> values) {
        if (values == null) {
            throw new NullPointerException("Cannot set ListModel with null.");
        }
        int previousSize = getSize() - 1;
        if (values.size() > 0) {
            this.values = new ArrayList<T>(values);
        } else {
            this.values = new ArrayList<T>();
        }
        fireContentsChanged(this, 0, previousSize > 0 ? previousSize : 0);
    }

    public boolean contains(T value) {
        return value != null && this.values.contains(value);
    }

    public int indexOf(T value) {
        return value != null ? this.values.indexOf(value) : -1;
    }

    public int indexOf(T value, int fromIndex) {
        if (value != null) {
            for (int i = fromIndex, n = getSize(); i < n; i++) {
                if (value.equals(getElementAt(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(T value) {
        return value != null ? this.values.lastIndexOf(value) : -1;
    }
}
