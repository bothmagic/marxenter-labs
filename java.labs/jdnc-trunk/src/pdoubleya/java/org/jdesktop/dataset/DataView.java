package org.jdesktop.dataset;



/**
 * A DataView is a DataTable that acts as a decorator around another DataTable, which itself
 * might be a view. Each DataView can act on the underlying table's information providing filtering
 * capability to the client of the view. In this abstract DataView, all methods in DataTable
 * are overridden and calls passed to the source DataTable; subclasses should override those methods
 * they wish to provide special behavior for. By convention, DataViews are non-destructive of the
 * contents in the underlying DataTable, that is, DataViews provide a filtering mechanism.
 *
 * @author Patrick Wright
 */
//TODO:
//event listeners (and re-broadcasting)
//property change listeners (and re-broadcasting)
//model/view row mapping (needed for sorters)
//should views support append operations and delete operations? or are they read-only?
public abstract class DataView extends DataTable {
    protected DataTable sourceTable;
    
    /** Creates a new instance of DataView */
    public DataView(DataTable source) {
        super(source.getDataSet());
        this.sourceTable = source;
    }
    
    public int modelIndex(int viewIndex) {
        if ( getSource() instanceof DataTable ) {
            return viewIndex;
        } else {
            return ((DataView)getSource()).modelIndex(viewIndex);
        }
    }
    
    public int viewIndex(int modelIndex) {
        if ( getSource() instanceof DataTable ) {
            return modelIndex;
        } else {
            return ((DataView)getSource()).viewIndex(modelIndex);
        }
    }
    
    public DataSet getDataSet() {
        return sourceTable.getDataSet();
    }
    
    public void setDataProvider(DataProvider dataProvider) {
        sourceTable.setDataProvider(dataProvider);
    }
    
    public DataProvider getDataProvider() {
        return sourceTable.getDataProvider();
    }
    
    public void setName(String name) {
        sourceTable.setName(name);
    }
    
    public String getName() {
        return sourceTable.getName();
    }
    
    public DataColumn getColumn(String colName) {
        return sourceTable.getColumn(colName);
    }
    
    public java.util.List<DataColumn> getColumns() {
        return sourceTable.getColumns();
    }
    
    public DataColumn createColumn() {
        return sourceTable.createColumn();
    }
    
    public DataColumn createColumn(String colName) {
        return sourceTable.createColumn(colName);
    }
    
    public void createColumns(String... colNames) {
        sourceTable.createColumns(colNames);
    }
    
    public void dropColumn(String colName) {
        sourceTable.dropColumn(colName);
    }
    
    public DataSelector createSelector(String name) {
        return sourceTable.createSelector(name);
    }
    
    public DataSelector createSelector() {
        return sourceTable.createSelector();
    }
    
    public DataSelector getSelector(String name) {
        return sourceTable.getSelector(name);
    }
    
    public java.util.List<DataSelector> getSelectors() {
        return sourceTable.getSelectors();
    }
    
    public void dropSelector(DataSelector selector) {
        sourceTable.dropSelector(selector);
    }
    
    public void dropSelector(String selectorName) {
        sourceTable.dropSelector(selectorName);
    }
    
    public void setAppendRowSupported(boolean appendRowSupported) {
        sourceTable.setAppendRowSupported(appendRowSupported);
    }
    
    public boolean isAppendRowSupported() {
        return sourceTable.isAppendRowSupported();
    }
    
    public void setDeleteRowSupported(boolean deleteRowSupported) {
        sourceTable.setDeleteRowSupported(deleteRowSupported);
    }
    
    public boolean isDeleteRowSupported() {
        return sourceTable.isDeleteRowSupported();
    }
    
    public DataRow appendRow() {
        return sourceTable.appendRow();
    }
    
    public DataRow appendRowNoEvent() {
        return sourceTable.appendRowNoEvent();
    }
    
    public Object getValue(int index, String columnName) {
        return sourceTable.getValue(index, columnName);
    }
    
    public Object getValue(DataRow row, DataColumn col) {
        return sourceTable.getValue(row, col);
    }
    
    public void setValue(int index, String columnName, Object value) {
        sourceTable.setValue(index, columnName, value);
    }
    
    public void load() {
        sourceTable.load();
    }
    
    public void loadAndWait() {
        sourceTable.loadAndWait();
    }
    
    public void refresh() {
        sourceTable.refresh();
    }
    
    public void refreshAndWait() {
        sourceTable.refreshAndWait();
    }
    
    public void clear() {
        sourceTable.clear();
    }
    
    public java.util.List<DataRow> getRows() {
        return sourceTable.getRows();
    }
    
    public int getRowCount() {
        return sourceTable.getRowCount();
    }
    
    public DataRow getRow(int index) {
        return sourceTable.getRow(index);
    }
    
    public void deleteRow(DataRow row) {
        sourceTable.deleteRow(row);
    }
    
    public void discardRow(int rowIndex) {
        sourceTable.discardRow(rowIndex);
    }
    
    public void deleteRow(int rowIndex) {
        sourceTable.deleteRow(rowIndex);
    }
    
    public void discardRow(DataRow row) {
        sourceTable.discardRow(row);
    }
    
    public void save() {
        sourceTable.save();
    }
    
    public void saveAndWait() {
        sourceTable.saveAndWait();
    }
    
    protected DataTable getBaseSource() {
        if ( sourceTable instanceof DataView ) {
            return ((DataView)sourceTable).getSource();
        } else {
            return sourceTable;
        }
    }
    
    protected DataTable getSource() {
        return sourceTable;
    }
    
    protected int indexOfRow(DataRow row) {
        return sourceTable.indexOfRow(row);
    }
    
    public void addPropertyChangeListener(String property, java.beans.PropertyChangeListener listener) {
        sourceTable.addPropertyChangeListener(property, listener);
    }
    
    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        sourceTable.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(String propertyName, java.beans.PropertyChangeListener listener) {
        sourceTable.removePropertyChangeListener(propertyName, listener);
    }
    
    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        sourceTable.removePropertyChangeListener(listener);
    }
    
    public void addDataTableListener(org.jdesktop.dataset.event.DataTableListener listener) {
        sourceTable.addDataTableListener(listener);
    }
    
    public void removeDataTableListener(org.jdesktop.dataset.event.DataTableListener listener) {
        sourceTable.removeDataTableListener(listener);
    }
    
    public void fireDataTableChanged(org.jdesktop.dataset.event.TableChangeEvent evt) {
        sourceTable.fireDataTableChanged(evt);
    }
}
