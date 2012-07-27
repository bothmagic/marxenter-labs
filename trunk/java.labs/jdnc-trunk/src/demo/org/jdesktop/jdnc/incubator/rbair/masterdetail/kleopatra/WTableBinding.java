package org.jdesktop.jdnc.incubator.rbair.masterdetail.kleopatra;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

//import org.jdesktop.jdnc.incubator.kleopatra.swing.data.DataConstants;
//import org.jdesktop.jdnc.incubator.kleopatra.swing.data.OverviewDataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.binding.AbstractBinding;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;

/**
 * @author Jeanette Winzenburg
 */
public class WTableBinding extends AbstractBinding {
  private JTable table;
  public WTableBinding(JTable component, DataModel dataModel,
      String fieldName) {
    super(component, dataModel, fieldName, AUTO_VALIDATE_NONE);
//    initSelection(dataModel);
    initModel();

  }

  private void initModel() {
    // hmm, should work without??
     pull(); 
//    Object columns = dataModel.getValue(OverviewDataModel.COLUMN_MODEL);
//    if (columns instanceof TableColumnModel) {
//      getTable().setAutoCreateColumnsFromModel(false);
//      getTable().setColumnModel((TableColumnModel) columns);
//    }
  }

  public JComponent getComponent() {
    return table;
  }

  protected void setComponent(JComponent component) {
    this.table = (JTable) component;
  }

  protected Object getComponentValue() {
    return (table != null) ? table.getModel() : null;
  }

  protected void setComponentValue(Object value) {
    if (table == null) {
      return;
    }
    table.setModel((TableModel) value);
  }

  private void initSelection(DataModel dataModel) {
//    Object selection = dataModel.getValue(DataConstants.SELECTION_MODEL);
//    if (selection instanceof ListSelectionModel) {
//      getTable().setSelectionModel((ListSelectionModel) selection);
//    }
  }

  private JTable getTable() {
    return (JTable) getComponent();
  }

}
