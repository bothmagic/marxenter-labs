/*
 * Created on 30.06.2009
 *
 */
package rasto1968.checklist;

import javax.swing.ListModel;

/**
 * A {@code ListModel} that maintains a selection state for each contained element.
 * This model is useful when creating a check list.<p>
 * 
 * JW: Adding the checkable property (how-ever it will be named :-) on the interface-level
 * would allow the Factory/Handler to be implemented against that instead of against a 
 * specific implementation. Custom implementations are free to follow any of the most common
 * approaches, namely 
 * 
 * <ul>
 * <li> wrap the user object (aka: real item) into a node
 * <li> keep a parallel structure (f.i. ListSelectionModel, List) for the selection state
 * <li> use some inherent property of the item
 * </ul>
 * 
 */
public interface CheckableListModel extends ListModel {

    /**
     * Determines if the element at the specified index is selected.
     * 
     * @param index
     *            the index to query
     * @return {@code true} if the element is selected; {@code false} otherwise
     */
    public boolean isChecked(int index);

    /**
     * Updates the selection state of the element at the specified index if the 
     * selection state is editable, does nothing otherwise. Notifies ListDataListeners on
     * change.<p>
     * 
     * PENDING JW: need for more specific notification?
     * 
     * @param index
     *            the index to update
     * @param checked
     *            the new selection state
     */
    public void setChecked(int index, boolean checked);

    /**
     * Determines if the selection state of the element at the specified index is editable.
     * 
     * @param index
     *            the index to query
     * @return {@code true} if the element is checkable; {@code false} otherwise
     */
    public boolean isCheckedEditable(int index);


    
}
