/*
 * Created on 22.10.2004
 *
 */
package org.jdesktop.jdnc.incubator.rbair.masterdetail.kleopatra;

import javax.swing.table.AbstractTableModel;

import org.jdesktop.jdnc.incubator.rbair.swing.data.*;

/** extracted from rbair...TableBinding.
 * 
 */
public class WDataModelTableModel extends AbstractTableModel {
	private DataModel dm;
	private String[] fieldNames;
	
	public WDataModelTableModel(final DataModel dm) {
		this.dm = dm;
		fieldNames = dm.getFieldNames();
		installDataModelListener();
	}

  public WDataModelTableModel(final DataModel dm, final String[] visibleFieldNames) {
		this.dm = dm;
		fieldNames = visibleFieldNames/*dm.getFieldNames()*/;
		//register with the data model
		installDataModelListener();
	}
	
	public Class getColumnClass(int columnIndex) {
		return dm.getMetaData(fieldNames[columnIndex]).getElementClass();
	}
	
	public String getColumnName(int column) {
		//its possible that the meta data hasn't shown up yet. In this
		//case, use the field name until the meta data arrives
		MetaData md = dm.getMetaData(fieldNames[column]);
		return md == null ? fieldNames[column] : md.getLabel();
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return !dm.getMetaData(fieldNames[columnIndex]).isReadOnly();
	}
	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		dm.setValue(fieldNames[columnIndex], aValue, rowIndex);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return dm.getRecordCount();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return fieldNames == null ? dm.getFieldCount() : fieldNames.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		return dm.getValue(fieldNames[columnIndex], rowIndex);
	}

  private void installDataModelListener() {
    DataModelListener dataModelListener = createDataModelListener(dm);
    dm.addDataModelListener(dataModelListener);
  }

	
	private DataModelListener createDataModelListener(final DataModel dm) {
    //register with the data model
		DataModelListener dataModelListenerAdapter = new DataModelListener() {
			/**
			 * @inheritDoc
			 */
			public void modelChanged(ModelChangeEvent e) {
				fireTableDataChanged();
			}

			public void valueChanged(ValueChangeEvent e) {
				fireTableDataChanged();
			}

			public void metaDataChanged(MetaDataChangeEvent e) {
				fieldNames = dm.getFieldNames();
				fireTableStructureChanged();
			}
		};
    return dataModelListenerAdapter;
  }
	
}