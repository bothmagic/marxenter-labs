/*
 * Created on 09.09.2008
 *
 */
package org.jdesktop.swingx.table;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Taken from: http://l2fprod.com/blog/2008/08/30/the-fun-of-swing-jtable-column-resizing/
 * 
 * @author Fred Lavigne (l2fprod.com)
 */
public class TableHeaderFiller implements ComponentListener,
    PropertyChangeListener {

  private JTable table;
  private JTableHeader header;
  private List<TableColumn> columns;

  private JTableHeader filler;
  private TableColumn fillerColumn;

  private PropertyChangeListener columnWidthChangedListener = new PropertyChangeListener() {
    public void propertyChange(PropertyChangeEvent evt) {
//      update();
    }
  };

  public TableHeaderFiller(JTable table) {
    this.table = table;

    DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "" },
        0);
    JTable tempTable = new JTable(tableModel);
    filler = tempTable.getTableHeader();
    filler.setReorderingAllowed(false);
    
    for (MouseMotionListener listener : filler.getMouseMotionListeners()) {
      filler.removeMouseMotionListener(listener);
    }
    for (MouseListener listener : filler.getMouseListeners()) {
      filler.removeMouseListener(listener);
    }
    
    fillerColumn = tempTable.getColumnModel().getColumn(0);
    fillerColumn.setResizable(false);

    columns = new ArrayList<TableColumn>();
    installListeners();
    update();
  }

  private void installListeners() {
    header = table.getTableHeader();
    header.add(filler);

    table.addPropertyChangeListener("model", this);
    table.addPropertyChangeListener("tableHeader", this);
    table.addComponentListener(this);
    header.addComponentListener(this);

    TableColumnModel columnModel = header.getColumnModel();
    for (int i = 0, c = columnModel.getColumnCount(); i < c; i++) {
      TableColumn column = columnModel.getColumn(i);
      columns.add(column);
      column.addPropertyChangeListener(columnWidthChangedListener);
    }
  }

  private void uninstallListeners() {
    table.removeComponentListener(this);
    header.removeComponentListener(this);
    header.remove(filler);

    for (TableColumn column : columns) {
      column.removePropertyChangeListener(columnWidthChangedListener);
    }
    columns.clear();
  }

  private void update() {
    int height = header.getHeight();
    int width = header.getWidth();
    TableColumnModel columnModel = header.getColumnModel();
    for (int i = 0, c = columnModel.getColumnCount(); i < c; i++) {
      width -= columnModel.getColumn(i).getWidth();
    }

    filler.setSize(width, height);
    filler.setLocation(header.getWidth() - width, 0);
    fillerColumn.setWidth(width);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    uninstallListeners();
    installListeners();
    update();
  }

  public void componentHidden(ComponentEvent e) {
  }

  public void componentMoved(ComponentEvent e) {
  }

  public void componentResized(ComponentEvent e) {
    update();
  }

  public void componentShown(ComponentEvent e) {
    update();
  }

}