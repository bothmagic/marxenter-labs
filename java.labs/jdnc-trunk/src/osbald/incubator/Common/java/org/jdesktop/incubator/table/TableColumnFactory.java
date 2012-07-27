package org.jdesktop.incubator.table;

import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * ColumnFactory for configuring which SwingX TableColumnExt columns will be hidden and/or which shown.
 * The factory needs to be passed a list of heading identifiers which is the actual names of the header values.
 * i.e. The columns displayed hading or it's internationalised string (barring any other ID I've missed?).
 */

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 03-Sep-2007
 * Time: 14:56:26
 */

public class TableColumnFactory extends ColumnFactory {
    protected Collection<String> shownColumns = new ArrayList<String>();
    protected Collection<String> hiddenColumns = new ArrayList<String>();

    public TableColumnFactory(String... showColumns) {
        this(Arrays.asList(showColumns));
    }

    public TableColumnFactory(Collection<String> showColumns) {
        setShownColumns(showColumns);
    }

    public static TableColumnFactory showColumns(String... columns) {
        TableColumnFactory columnFactory = new TableColumnFactory();
        columnFactory.setShownColumns(Arrays.asList(columns));
        return columnFactory;
    }

    public static TableColumnFactory hideColumns(String... columns) {
        TableColumnFactory columnFactory = new TableColumnFactory();
        columnFactory.setHiddenColumns(Arrays.asList(columns));
        return columnFactory;
    }

    protected void setShownColumns(Collection<String> columns) {
        this.shownColumns = columns;
    }

    protected void setHiddenColumns(Collection<String> columns) {
        this.hiddenColumns = columns;
    }

    @Override
    public void configureTableColumn(TableModel model, TableColumnExt columnExt) {
        super.configureTableColumn(model, columnExt);
        if (columnExt.getIdentifier() == null) {
            columnExt.setIdentifier(columnExt.getHeaderValue());
        }
        String header = (String) columnExt.getIdentifier();
        columnExt.setVisible(isVisible(header));
    }

    protected boolean isVisible(String header) {
        boolean visible = true;
        if (!shownColumns.isEmpty()) {
            visible = shownColumns.contains(header);
        }
        if (!hiddenColumns.isEmpty()) {
            visible &= !hiddenColumns.contains(header);
        }
        return visible;
    }
}
