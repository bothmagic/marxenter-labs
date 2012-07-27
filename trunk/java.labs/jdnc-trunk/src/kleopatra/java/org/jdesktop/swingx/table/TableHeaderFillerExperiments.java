/*
 * Created on 09.09.2008
 *
 */
package org.jdesktop.swingx.table;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.incubatorutil.AncientSwingTeam;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;

public class TableHeaderFillerExperiments extends InteractiveTestCase {
    public static void main(String[] args) {
        TableHeaderFillerExperiments test = new TableHeaderFillerExperiments();
        setSystemLF(true);
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void interactiveFillerCore() {
        final JTable table = new JTable(new AncientSwingTeam());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JXFrame frame = wrapWithScrollingInFrame(table, "Fred's headerFiller in core");
        new TableHeaderFiller(table);
        Action action = new AbstractAction("remove selected column") {

            public void actionPerformed(ActionEvent e) {
                int selected = table.getSelectedColumn();
                if (selected < 0) return;
                table.removeColumn(table.getColumnModel().getColumn(selected));
                
            }
            
        };
        addAction(frame, action);
        Action duplicate =  new AbstractAction("duplicate selected column") {

            public void actionPerformed(ActionEvent e) {
                int selected = table.getSelectedColumn();
                if (selected < 0) return;
                
                TableColumn column = table.getColumnModel().getColumn(selected);
                table.addColumn(new TableColumn(column.getModelIndex()));
                
            }
            
        };
        addAction(frame, duplicate);
        addComponentOrientationToggle(frame);
        show(frame);
    }

    public void interactiveFillerSwingX() {
        final JXTable table = new JXTable(new AncientSwingTeam());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setColumnControlVisible(true);
        JXFrame frame = wrapWithScrollingInFrame(table, "Fred's headerFiller in swingx");
        new TableHeaderFiller(table);
        Action action = new AbstractAction("remove selected column") {

            public void actionPerformed(ActionEvent e) {
                int selected = table.getSelectedColumn();
                if (selected < 0) return;
                table.removeColumn(table.getColumnModel().getColumn(selected));
                
            }
            
        };
        addAction(frame, action);
        Action duplicate =  new AbstractAction("duplicate selected column") {

            public void actionPerformed(ActionEvent e) {
                int selected = table.getSelectedColumn();
                if (selected < 0) return;
                TableColumn column = table.getColumnModel().getColumn(selected);
                table.addColumn(table.getColumnFactory().createAndConfigureTableColumn(
                        table.getModel(), column.getModelIndex()));
                
            }
            
        };
        addAction(frame, duplicate);
        addComponentOrientationToggle(frame);
        show(frame);
    }


}
