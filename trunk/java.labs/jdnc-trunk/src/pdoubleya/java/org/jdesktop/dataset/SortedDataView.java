package org.jdesktop.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A DataView that allows sorting by multiple columns; code stolen outright from TableSorter. 
 *
 * @author (the guys who wrote TableSorter)
 * @author (thief) Patrick Wright
 */
public class SortedDataView extends DataView {
    private List<Directive> sortingColumns;
    private Map<Class, Comparator> columnComparators;
    private Row[] viewToModel;
    private int[] modelToView;
    
    public static final int DESCENDING = -1;
    public static final int NOT_SORTED = 0;
    public static final int ASCENDING = 1;
    
    private static Directive EMPTY_DIRECTIVE = new Directive(null, NOT_SORTED);
    
    public static final Comparator COMPARABLE_COMAPRATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    };
    
    public static final Comparator LEXICAL_COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };
    
    public SortedDataView(DataTable source) {
        super(source);
        this.sortingColumns = new ArrayList<Directive>();
        this.columnComparators = new HashMap<Class, Comparator>();
    }
    
    public int modelIndex(int viewIndex) {
        return getViewToModel()[viewIndex].modelIndex;
    }
    
    public int viewIndex(int modelIndex) {
        return getModelToView()[modelIndex];
    }
    
    public DataRow getRow(int row) {
        return getSource().getRow(modelIndex(row));
    }

    public List<DataRow> getRows() {
        List<DataRow> l = new ArrayList<DataRow>(getRowCount());
        for ( int i=0; i < getRowCount(); i++ ) {
            l.add(getRow(i));
        }
        return l;
    }
    
    public Object getValue(int row, String column) {
        return getSource().getValue(modelIndex(row), column);
    }
    
    public void setValue(int row, String column, Object aValue) {
        getSource().setValue(modelIndex(row), column, aValue );
    }
    
    public boolean isSorting() {
        return sortingColumns.size() != 0;
    }
    
    private Directive getDirective(DataColumn column) {
        for (int i = 0; i < sortingColumns.size(); i++) {
            Directive directive = (Directive)sortingColumns.get(i);
            if (directive.column == column) {
                return directive;
            }
        }
        return EMPTY_DIRECTIVE;
    }
    
    public int getSortingStatus(DataColumn column) {
        return getDirective(column).direction;
    }
    
    public void setSortingStatus(DataColumn column, int status) {
        Directive directive = getDirective(column);
        if (directive != EMPTY_DIRECTIVE) {
            sortingColumns.remove(directive);
        }
        if (status != NOT_SORTED) {
            sortingColumns.add(new Directive(column, status));
        }
        sortingStatusChanged();
    }
    
    public void setColumnComparator(Class type, Comparator comparator) {
        if (comparator == null) {
            columnComparators.remove(type);
        } else {
            columnComparators.put(type, comparator);
        }
    }
    
    protected Comparator getComparator(String columnName) {
        Class columnType = getSource().getColumn(columnName).getType();
        Comparator comparator = columnComparators.get(columnType);
        if (comparator != null) {
            return comparator;
        }
        if (Comparable.class.isAssignableFrom(columnType)) {
            return COMPARABLE_COMAPRATOR;
        }
        return LEXICAL_COMPARATOR;
    }
    
    private Row[] getViewToModel() {
        if (viewToModel == null) {
            int tableModelRowCount = getSource().getRowCount();
            viewToModel = new Row[tableModelRowCount];
            for (int row = 0; row < tableModelRowCount; row++) {
                viewToModel[row] = new Row(row);
            }
            
            if (isSorting()) {
                Arrays.sort(viewToModel);
            }
        }
        return viewToModel;
    }
    
    private int[] getModelToView() {
        if (modelToView == null) {
            int n = getViewToModel().length;
            modelToView = new int[n];
            for (int i = 0; i < n; i++) {
                modelToView[modelIndex(i)] = i;
            }
        }
        return modelToView;
    }
    
    private void sortingStatusChanged() {
        clearSortingState();
        // TODO: fireTableDataChanged();
    }
    
    private void cancelSorting() {
        sortingColumns.clear();
        sortingStatusChanged();
    }
    
    private void clearSortingState() {
        viewToModel = null;
        modelToView = null;
    }
    
    private class Row implements Comparable {
        private int modelIndex;
        
        public Row(int index) {
            this.modelIndex = index;
        }
        
        public int compareTo(Object o) {
            int row1 = modelIndex;
            int row2 = ((Row) o).modelIndex;
            
            for (Iterator it = sortingColumns.iterator(); it.hasNext();) {
                Directive directive = (Directive) it.next();
                DataColumn column = directive.column;
                
                Object o1 = getSource().getValue(row1, column.getName());
                Object o2 = getSource().getValue(row2, column.getName());
                
                int comparison = 0;
                // Define null less than everything, except null.
                if (o1 == null && o2 == null) {
                    comparison = 0;
                } else if (o1 == null) {
                    comparison = -1;
                } else if (o2 == null) {
                    comparison = 1;
                } else {
                    comparison = getComparator(column.getName()).compare(o1, o2);
                }
                if (comparison != 0) {
                    return directive.direction == DESCENDING ? -comparison : comparison;
                }
            }
            return 0;
        }
    }
    private static class Directive {
        private DataColumn column;
        private int direction;
        
        public Directive(DataColumn column, int direction) {
            this.column = column;
            this.direction = direction;
        }
    }
}
