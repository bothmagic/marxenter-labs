package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModelListener;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModelListenerAdapter;
import org.jdesktop.jdnc.incubator.rbair.swing.data.MetaData;
import org.jdesktop.jdnc.incubator.rbair.swing.data.MetaDataChangeEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.data.ModelChangeEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.data.ValueChangeEvent;

/**
 * @author Richard Bair
 */
public class TableBinding extends AbstractBinding {
	private JTable table;
	
	/**
	 * @param component
	 * @param dataModel
	 */
	public TableBinding(JTable component, DataModel dataModel) {
		super(component, dataModel, null, TableBinding.AUTO_VALIDATE_NONE);
		//construct a TableModel for the table based on the given dataModel.
		TableModel tm = new DataModelTableModel(dataModel);
		table.setModel(tm);
	}
	
	public TableBinding(JTable component, DataModel dataModel, String[] fieldNames) {
		super(component, dataModel, null, TableBinding.AUTO_VALIDATE_NONE);
		//construct a TableModel for the table based on the given dataModel.
		TableModel tm = new DataModelTableModel(dataModel, fieldNames);
		table.setModel(tm);
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swing.binding.AbstractUIBinding#getBoundComponent()
	 */
	protected JComponent getBoundComponent() {
		return table;
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swing.binding.AbstractUIBinding#setBoundComponent(javax.swing.JComponent)
	 */
	protected void setBoundComponent(JComponent component) {
		if (!(component instanceof JTable)) {
			throw new IllegalArgumentException("TableBindings only accept a JTable or one of its child classes");
		}
		this.table = (JTable)component;
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swing.binding.AbstractUIBinding#getComponentValue()
	 */
	protected Object getComponentValue() {
		//a table component never updates its parent data model in this way
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swing.binding.AbstractUIBinding#setComponentValue(java.lang.Object)
	 */
	protected void setComponentValue(Object value) {
		//means nothing to this binding
	}
	
	private static final class DataModelTableModel extends AbstractTableModel {
		private DataModel dm;
		private String[] fieldNames;
		
		public DataModelTableModel(final DataModel dm) {
			this.dm = dm;
			fieldNames = dm.getFieldNames();
			//register with the data model
			dm.addDataModelListener(new DataModelListenerAdapter() {
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
			});
		}
		
		public DataModelTableModel(final DataModel dm, final String[] visibleFieldNames) {
			this.dm = dm;
			fieldNames = visibleFieldNames/*dm.getFieldNames()*/;
			//register with the data model
			dm.addDataModelListener(new DataModelListener() {
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
			});
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
		
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.binding.AbstractBinding#getComponent()
	 */
	public JComponent getComponent() {
		return getBoundComponent();
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.binding.AbstractBinding#setComponent(javax.swing.JComponent)
	 */
	protected void setComponent(JComponent component) {
		setBoundComponent(component);
	}

}
