/*
 * $Id: ListBinding.java 128 2004-10-18 20:06:27Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModelListener;
import org.jdesktop.jdnc.incubator.rbair.swing.data.MetaDataChangeEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.data.ModelChangeEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.data.ValueChangeEvent;

public class ListBinding extends AbstractBinding {
    
    private JList list;
    private boolean indexIsChanging = false;

    public ListBinding(final JList list, final DataModel model, String fieldName) {
        super(list, model, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
		list.setModel(new DataModelListModel());
		//listen for changes in the list selection and maintain the
		//synchronicity with the data model
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && !indexIsChanging) {
					model.setRecordIndex(list.getSelectedIndex());
				}
			}
		});
		//add a property change listener to listen for row change events
		//in the data model. Set the currently selected row to be the
		//same as the current row in the data model
		model.addPropertyChangeListener("recordIndex", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() != evt.getOldValue()
					&& evt.getNewValue() instanceof Number) {
					Number n = (Number)evt.getNewValue();
					indexIsChanging = true;
					if (n ==  null) {
						list.setSelectedIndex(-1);
					} else {
						list.setSelectedIndex(n.intValue());
					}
					indexIsChanging = false;
				}
			}
		});
    }

    public boolean isModified() {
        return false;
    }

    public boolean isValid() {
        return true;
    }

    public JComponent getComponent() {
        return list;
    }

    protected void setComponent(JComponent component) {
        list = (JList)component;
    }

    protected Object getComponentValue(){
//	ListModel model = list.getModel();
//	Class klazz = metaData.getElementClass();
//
//	if (klazz.equals(List.class)) {
//	    List lvalue = new ArrayList();
//	    for (int i = 0, size = model.getSize(); i < size; i++) {
//		lvalue.add(model.getElementAt(i));
//	    }
//	    return lvalue;
//	} 
//	else if (klazz.isArray()) {
//
//	    // XXX we lose the array type, use a generic Object[]
//	    int size = model.getSize();
//	    Object[] values = new Object[size];
//	    for (int i = 0; i < size; i++) {
//		values[i] = model.getElementAt(i);
//	    }
//	    return values;
//	}
	return null;
    }

    protected void setComponentValue(Object value) {
//	Class klazz = metaData.getElementClass();
//	if (klazz.equals(List.class)) {
//	    List lvalue = (List)value;
//	    if (lvalue != null) {
//		list.setListData(lvalue.toArray());
//	    }
//	}
//	else if (klazz.isArray()) {
//	    Object[] arrayValue = (Object[])value;
//
//	    if (arrayValue != null) {
//		list.setListData(arrayValue);
//	    } else {
//		// Empty the list.
//		list.setModel(new DefaultListModel());
//	    }
//	}
    }

	private class DataModelListModel extends AbstractListModel {

		public DataModelListModel() {
			dataModel.addDataModelListener(new DataModelListener() {
				/* (non-Javadoc)
				 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataModelListener#valueChanged(org.jdesktop.jdnc.incubator.rbair.swing.data.ValueChangeEvent)
				 */
				public void valueChanged(ValueChangeEvent e) {
					fireContentsChanged(list, 0, dataModel.getRecordCount() - 1);
				}

				/* (non-Javadoc)
				 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataModelListener#modelChanged(org.jdesktop.jdnc.incubator.rbair.swing.data.ModelChangeEvent)
				 */
				public void modelChanged(ModelChangeEvent e) {
					//refresh
					fireContentsChanged(list, 0, dataModel.getRecordCount() - 1);
					
					//set the selected item
					if (getSize() > 0 && list.getSelectedIndex() == -1) {
						list.setSelectedIndex(0);
					}
				}

				/* (non-Javadoc)
				 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataModelListener#metaDataChanged(org.jdesktop.jdnc.incubator.rbair.swing.data.MetaDataChangeEvent)
				 */
				public void metaDataChanged(MetaDataChangeEvent e) {
					fireContentsChanged(list, 0, dataModel.getRecordCount() - 1);
				}
			});
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.ListModel#getSize()
		 */
		public int getSize() {
			return dataModel.getRecordCount();
		}

		/* (non-Javadoc)
		 * @see javax.swing.ListModel#getElementAt(int)
		 */
		public Object getElementAt(int index) {
			return dataModel.getValue(getFieldName(), index);
		}
		
	}
}

