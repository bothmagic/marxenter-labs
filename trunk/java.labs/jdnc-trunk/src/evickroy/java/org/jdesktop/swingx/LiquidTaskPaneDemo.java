package org.jdesktop.swingx;


import com.birosoft.liquid.LiquidLookAndFeel;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Container;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import org.jdesktop.jdnc.JNTable;
import javax.swing.SpringLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import org.jdesktop.swingx.JXGroupableTableHeader;
import org.jdesktop.swingx.calendar.JXMonthView;
import org.jdesktop.swingx.table.ColumnGroup;
import org.jdesktop.swingx.table.MultiLineTableHeader;

public class LiquidTaskPaneDemo extends JFrame {
    
    LiquidTaskPaneDemo() {
        super( "Liquid TaskPane Delegate Example" );

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 4);
        long[] unselDates = new long[3];
        unselDates[0] = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        unselDates[1] = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        unselDates[2] = cal.getTimeInMillis();
        
        Container pane = this.getContentPane();
        JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
        
        JXTaskPane taskPane = new JXTaskPane();
        taskPane.setTitle("JXMonthView and JXDatePicker");
        taskPaneContainer.add(taskPane);
        JXMonthView monthView = new JXMonthView();

        
        monthView.setUnselectableDates(unselDates);
//        monthView.setFlaggedDates(unselDates);
        taskPane.add(monthView);
        
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.getMonthView().setUnselectableDates(unselDates);
        taskPane.add(datePicker);
        
        taskPane = new JXTaskPane();
        taskPane.setTitle("Task Pane 2");
        taskPane.setSpecial(true);
        taskPaneContainer.add(taskPane);
        JXHyperlink link = new JXHyperlink();
        link.setText("My first link");
        taskPane.add(link);
        
        taskPane = new JXTaskPane();
        taskPane.setTitle("Task Pane 3");
        taskPaneContainer.add(taskPane);
        taskPane.add(new JButton("Push me"));
        taskPane.add(new JTextField("Field 1"));

        pane.add(taskPaneContainer);
        setSize(800, 600);

    }
    
    public static void main(String[] args) {
        try {
//            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//            UIManager.setLookAndFeel("org.jvnet.substance.SubstanceLookAndFeel");
            UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
            LiquidLookAndFeel.setLiquidDecorations(true);
//            LiquidLookAndFeel.setPanelTransparency(false);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        LiquidTaskPaneDemo frame = new LiquidTaskPaneDemo();
        frame.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent e ) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }
}

