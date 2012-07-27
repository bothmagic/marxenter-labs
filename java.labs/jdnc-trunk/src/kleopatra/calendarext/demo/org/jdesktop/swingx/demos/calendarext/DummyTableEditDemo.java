/*
 * Created on 15.10.2010
 *
 */
package org.jdesktop.swingx.demos.calendarext;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;

import com.sun.swingset3.DemoProperties;

@DemoProperties(
        value = "JXTable bug- editable?",
        category = "Controls",
        description = "Quick check - Issue #??-swingx: JXTable not editable in web context.",
        sourceFiles = {
            "org/jdesktop/swingx/demos/calendarext/DummyTableEditDemo.java"
        }
    )

public class DummyTableEditDemo extends JPanel {

    public DummyTableEditDemo() {
        setLayout(new BorderLayout());
        JXTable table = new JXTable(new DefaultTableModel(20, 5));
        add(new JScrollPane(table));
    }
}
