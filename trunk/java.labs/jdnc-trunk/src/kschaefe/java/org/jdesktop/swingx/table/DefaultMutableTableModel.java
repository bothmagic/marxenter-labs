package org.jdesktop.swingx.table;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * An empty subclass of {@code DefaultTableModel} that implements the
 * {@code MutableTableModel} interface.
 * 
 * @author Karl Schaefer
 */
@SuppressWarnings({ "serial", "unchecked" })
public class DefaultMutableTableModel extends DefaultTableModel implements
        MutableTableModel<Vector, Vector> {
}
