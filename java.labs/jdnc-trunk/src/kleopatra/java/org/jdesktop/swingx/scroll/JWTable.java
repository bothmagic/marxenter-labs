/*
 * Created on 29.06.2007
 *
 */
package org.jdesktop.swingx.scroll;

import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;

/**
 * Overridden tooltip to examine which column we are over.<p>
 * 
 * Temporarily overridden the cell/pixel coordinate transformations.
 * Removed again - from the table perspective its view is monotone.
 * UIDelegate gets confused otherwise.
 * 
 */
public class JWTable extends JXTable {
private static final Logger LOG = Logger.getLogger(JWTable.class.getName());
    public JWTable(TableModel model) {
        super(model);
    }
    
    @Override
    public String getToolTipText(MouseEvent event) {
        int column = columnAtPoint(event.getPoint());
        return " " + getColumn(column).getHeaderValue(); 
    }

//    @Override
//    public int columnAtPoint(Point point) {
//        if (getParent() instanceof JXViewport) {
//            point = ((JXViewport) getParent()).toRealViewCoordinates(point);
//        }
//        // hardcoded first two fixed columns
//        // TODO Auto-generated method stub
//        int column = super.columnAtPoint(point);
//        return column;
//    }

//    @Override
//    public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
//        Rectangle r = super.getCellRect(row, column, includeSpacing);
//        if (getParent() instanceof JXViewport) {
//            JXViewport viewport = (JXViewport) getParent();
//            Point left = r.getLocation();
//            int fixed = viewport.getFixedWidth();
//            if (left.x + r.width < fixed) {
//                return r;
//            } 
//            Point loc = getLocation();
//            if (loc.x < 0) {
//                left.x += loc.x;
//                if (left.x + r.width < fixed) {
//                    r.x = 0;
//                    r.width = 0;
//                }
//            }
//        }
//        return r;
//    }
//
    
}
