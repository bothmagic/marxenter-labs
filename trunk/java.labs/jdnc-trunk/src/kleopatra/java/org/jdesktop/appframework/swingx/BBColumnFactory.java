/*
 * Created on 27.08.2007
 *
 */
package org.jdesktop.appframework.swingx;

import javax.swing.table.TableModel;

import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 * custom ColumnFactory to parse the default headers produced
 * by column binding.
 */
public class BBColumnFactory extends ColumnFactory {

    @Override
    public void configureTableColumn(TableModel model, TableColumnExt columnExt) {
        super.configureTableColumn(model, columnExt);
        String title = columnExt.getTitle();
        int start = title.indexOf('[');
        int end = title.indexOf(']');
        if ((start < 0) || (end < 0))
            return;
        String extract = title.substring(start + 1, end);
        extract = extract.substring(0, 1).toUpperCase() + extract.substring(1);
        columnExt.setHeaderValue(extract);
    }

}
