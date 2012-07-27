package org.jdesktop.incubator.list;

import javax.swing.*;
import java.util.Collection;

/**
 * Stand-in for the missing MutableListModel..?
 *
 * @see MutableComboBoxModel
 */

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 02-Feb-2006
 * Time: 11:12:41
 */

public interface EditableListModel<T> extends ListModel {

    boolean isEditable(int index);

    void setValues(Collection<T> values);

    boolean add(T value);

    boolean addAll(Collection<T> values);

    boolean insert(int index, T value);

    boolean replace(T source, T target);

    T replace(int index, T target);

    boolean remove(T item);

    T remove(int index);

    Collection<T> removeAll(Collection<T> targets);

    Collection<T> removeAll(int[] rows);

    void clear();
}
