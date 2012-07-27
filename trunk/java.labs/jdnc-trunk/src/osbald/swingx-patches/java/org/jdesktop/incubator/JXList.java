package org.jdesktop.incubator;

import javax.swing.*;
import java.util.*;

/* RJO: Created out of pure frustration with lack of consistency between JList and JTable methods/models. */

public class JXList extends org.jdesktop.swingx.JXList {

    /* super constructors - nothing interesting here move on */

    public JXList() {
    }

    public JXList(ListModel dataModel) {
        super(dataModel);
    }

    public JXList(ListModel dataModel, boolean filterEnabled) {
        super(dataModel, filterEnabled);
    }

    public JXList(boolean filterEnabled) {
        super(filterEnabled);
    }

    public JXList(Object[] listData) {
        super(listData);
    }

    public JXList(Object[] listData, boolean filterEnabled) {
        super(listData, filterEnabled);
    }

    public JXList(Vector<?> listData) {
        super(listData);
    }

    public JXList(Vector<?> listData, boolean filterEnabled) {
        super(listData, filterEnabled);
    }

    //PENDING: could do with some generic collection & vargs constructors?

    /* ..venting some API frustrations */

    //NB why is ListSelectionModel API so clunky in the first place? (should be using the model directly)

    public int getSelectedRow() {
        return getSelectedIndex();
    }

    public int[] getSelectedRows() {
        return getSelectedIndices();
    }

    public int getSelectedRowCount() {
        ListSelectionModel selectionModel = getSelectionModel();
        int iMin = selectionModel.getMinSelectionIndex();
        int iMax = selectionModel.getMaxSelectionIndex();

        int count = 0;
        for(int i = iMin; i <= iMax; i++) {
            if (selectionModel.isSelectedIndex(i)) {
            count++;
            }
        }
        return count;
    }

    //NB JList has setVisibleRowCount() & JXTable already has setVisibleRowCount() & scrollRowToVisible()

    public void scrollRowToVisible(int row) {
        ensureIndexIsVisible(row);
    }

    //PENDING: deprecate original methods wrapped above?

}
