package org.jdesktop.swingx.plaf.liquid;


import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.calendar.JXMonthView;
import org.jvnet.lafplugin.LafComponentPlugin;
import org.jdesktop.swingx.JXGroupableTableHeader;

public class LiquidSwingxPlugin implements LafComponentPlugin {

    public LiquidSwingxPlugin() {
    }
    
    public Object[] getDefaults(Object theme) {
        Object[] defaults = new Object[] {JXGroupableTableHeader.muiClassID,
                                              "org.jdesktop.swingx.plaf.liquid.LiquidGroupableTableHeaderUI",
                                              JXTaskPaneContainer.uiClassID,
                                              "org.jdesktop.swingx.plaf.liquid.LiquidTaskPaneContainerUI",
                                              JXTaskPane.uiClassID,
                                              "org.jdesktop.swingx.plaf.liquid.LiquidTaskPaneUI",
                                              "TaskPane.titleForeground",
                                              new ColorUIResource(SystemColor.menuText),
                                              "TaskPane.titleOver",
                                              new ColorUIResource(new Color(60, 144, 233)),//SystemColor.menuText),

                                              JXHyperlink.uiClassID,
                                              "org.jdesktop.swingx.plaf.basic.BasicHyperlinkUI",
                                              
                                              JXDatePicker.uiClassID,
                                              "org.jdesktop.swingx.plaf.basic.BasicDatePickerUI",
                                              "JXDatePicker.linkFormat",
                                              "Today is {0,date, dd MMMM yyyy}",
                                              "JXDatePicker.longFormat",
                                              "EEE MM/dd/yyyy",
                                              "JXDatePicker.mediumFormat", "MM/dd/yyyy",
                                              "JXDatePicker.shortFormat", "MM/dd",
                                              "JXDatePicker.numColumns", 10,

                                              JXMonthView.uiClassID,
                                              "org.jdesktop.swingx.plaf.liquid.LiquidMonthViewUI",
                                            "JXMonthView.monthStringBackground", new Color(138, 173, 209),
                                            "JXMonthView.monthStringForeground", new Color(68, 68, 68),
                                            "JXMonthView.daysOfTheWeekForeground", new Color(68, 68, 68),
                                            "JXMonthView.weekOfTheYearForeground", new Color(68, 68, 68),
                                            "JXMonthView.unselectableDayForeground", Color.RED,
                                            "JXMonthView.selectedBackground", new Color(197, 220, 240),
                                            "JXMonthView.flaggedDayForeground", Color.RED,
                                            "JXMonthView.font", UIManager.getFont("Button.font"),
                                            "JXMonthView.monthDownFileName", "resources/month-down.png",
                                            "JXMonthView.monthUpFileName", "resources/month-up.png",
                                            "JXMonthView.boxPaddingX", 3,
                                            "JXMonthView.boxPaddingY", 3
        
        };

        return defaults;
    }

    public void initialize() {
    }

    public void uninitialize() {
    }
}
