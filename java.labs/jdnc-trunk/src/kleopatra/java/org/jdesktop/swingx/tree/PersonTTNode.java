/*
 * Created on 09.08.2007
 *
 */
package org.jdesktop.swingx.tree;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

public class PersonTTNode extends AbstractMutableTreeTableNode {

    private static final int LAST_IDX = 0;

    private static final int FIRST_IDX = 1;

    private static final int MIDDLE_IDX = 2;

    private static final int PHONE_IDX = 3;

    PersonTTNode(Person userObject) {
        super(userObject);
    }

    /**
     * Tells if a column can be edited.
     */
    @Override
    public boolean isEditable(int column) {
        return true;
    }

    /**
     * Called when done editing a cell from {@link DefaultTreeTableModel}.
     */
    @Override
    public void setValueAt(Object value, int column) {
        if (getUserObject() instanceof Person) {
            Person person = (Person) getUserObject();
            switch (column) {
            case FIRST_IDX:
                person.setFirstName(value.toString());
                break;
            case MIDDLE_IDX:
                person.setMiddleName(value.toString());
                break;
            case LAST_IDX:
                person.setLastName(value.toString());
                break;
            case PHONE_IDX:
                person.setPhoneNbr(value.toString());
                break;
            }
        }
    }

    /**
     * must override this for setValue from {@link DefaultTreeTableModel} 
     * to work properly!
     */
    public int getColumnCount() {
        return 4;
    }

    /**
     * Called when done editing a cell from {@link DefaultTreeTableModel}.
     */
    public Object getValueAt(int column) {
        Object toBeDisplayed = "n/a";

        if (getUserObject() instanceof Person) {
            Person person = (Person) getUserObject();
            switch (column) {
            case LAST_IDX:
                toBeDisplayed = person.getLastName();
                break;
            case FIRST_IDX:
                toBeDisplayed = person.getFirstName();
                break;
            case MIDDLE_IDX:
                toBeDisplayed = person.getMiddleName();
                break;
            case PHONE_IDX:
                toBeDisplayed = person.getPhoneNbr();
                break;
            }
        }
        return toBeDisplayed;
    }

}
