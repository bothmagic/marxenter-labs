/*
 * $Id: MonthViewDemo.java 1177 2009-11-05 15:44:18Z kleopatra $
 *
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.demos.calendarext;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.JSpinner.DefaultEditor;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXDateChooser;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.binding.DisplayInfoConverter;
import org.jdesktop.swingx.binding.LabelHandler;
import org.jdesktop.swingx.calendar.CalendarCellState;
import org.jdesktop.swingx.calendar.CalendarHighlightPredicates;
import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.DateSelectionModel;
import org.jdesktop.swingx.calendar.DaySelectionModel;
import org.jdesktop.swingx.calendar.CalendarHighlightPredicates.DayOfWeekHighlightPredicate;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.HighlightPredicate.AndHighlightPredicate;
import org.jdesktop.swingx.demos.calendarext.MonthViewDemoUtils.DayOfWeekConverter;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingxset.util.DemoUtils;
import org.jdesktop.swingxset.util.DisplayValues;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sun.swingset3.DemoProperties;

/**
 * A demo for the {@code JXCalendar}.
 *
 * @author Jeanette Winzenburg, Berlin
 * @author Karl George Schaefer
 * @author Joshua Outwater (original JXMonthViewDemoPanel)
 */
@DemoProperties(
    value = "JXCalendar (basic)",
    category = "Controls",
    description = "Demonstrates JXCalendar, a monthly calendar display.",
    sourceFiles = {
        "org/jdesktop/swingx/demos/calendarext/CalendarBaseDemo.java",
        "org/jdesktop/swingx/JXCalendar.java",
        "org/jdesktop/swingx/calendar/CalendarStringValues.java",
        "org/jdesktop/swingx/calendar/CalendarHighlightPredicates.java",
        "org/jdesktop/swingx/calendar/PageAdapter.java",
        "org/jdesktop/swingx/plaf/basic/BasicCalendarUI.java",
        "org/jdesktop/swingx/plaf/basic/PagingAnimator.java"
    }
)
@SuppressWarnings("serial")
public class CalendarBaseDemo extends JPanel {
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(CalendarBaseDemo.class
            .getName());
    
    private LabelHandler labelHandler;
    
    private JXCalendar calendarView;

    private JComboBox dayOfWeekComboBox;

    private JCheckBox weekNumberBox;
    private JCheckBox customDayHeader;
    
    private JXDateChooser flaggedDates;

    private JXDateChooser unselectableDates;

    private JXDateChooser upperBound;

    private JXDateChooser lowerBound;

    private JComboBox highlighterCombo;

    
    public CalendarBaseDemo() {
        initComponents();
        configureDisplayProperties();
        //DemoUtils.injectResources(this);
        bind();
    }

    /**
     * Demo specific properties.
     */
    private void configureDisplayProperties() {
        DemoUtils.setSnippet("Custom visuals by Highlighter", highlighterCombo);
        DemoUtils.setSnippet("Custom visuals for flagged dates", flaggedDates.getEditor());
        DemoUtils.setSnippet("Custom cell content", customDayHeader);
        DemoUtils.setSnippet("Scrolling Animation", calendarView);
        
    }


//---------------------- bind
    
    @SuppressWarnings("unchecked")
    private void bind() {
        new CalendarViewDemoControl();

        // PENDING JW: this does not survive a change in Locale - 
        // revisit if we add changing Locale to the demo
        Calendar calendar = calendarView.getPage();
        // start of week == first day of week in the calendar's coordinate space
        CalendarUtils.startOfWeek(calendar);
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < 7; i++) {
            model.addElement(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        dayOfWeekComboBox.setModel(model);
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        dayOfWeekComboBox.setRenderer(new DefaultListRenderer(
                new FormatStringValue(format)));
        Converter<?, ?> days = new DayOfWeekConverter(calendar);

        // PENDING JW: re-visit distribution of binding control ...
        // this is quite arbitrary (time of coding ;-)
        // roughly: simple direct bindings go here, bindings with
        // addition logic or indirection go into the controller
        BindingGroup group = new BindingGroup();
        Binding dayOfWeek = Bindings.createAutoBinding(READ, 
                dayOfWeekComboBox, BeanProperty.create("selectedItem"),
                calendarView, BeanProperty.create("firstDayOfWeek"));
        dayOfWeek.setConverter(days);
        group.addBinding(dayOfWeek);
       
        group.addBinding(Bindings.createAutoBinding(READ, 
                weekNumberBox, BeanProperty.create("selected"),
                calendarView, BeanProperty.create("showingWeekNumber")));

        group.addBinding(Bindings.createAutoBinding(READ, 
                upperBound, BeanProperty.create("date"),
                calendarView, BeanProperty.create("upperBound")));

        group.addBinding(Bindings.createAutoBinding(READ, 
                lowerBound, BeanProperty.create("date"),
                calendarView, BeanProperty.create("lowerBound")));
        
        group.bind();
        
    }

    
    private LabelHandler getLabelHandler() {
        if (labelHandler == null) {
            labelHandler = new LabelHandler();
        }
        return labelHandler;
    }

    

//--------------------- MonthViewDemoControl
    
    private DefaultComboBoxModel getHighlighterModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(new DisplayInfo<Highlighter>("none", null));
        // <snip> Custom visuals by Highlighter
        // ColorHighlighter for week ends
        Highlighter weekend = new ColorHighlighter(
                new DayOfWeekHighlightPredicate(Calendar.SATURDAY, Calendar.SUNDAY),
             null, Color.MAGENTA, null, Color.MAGENTA);
        model.addElement(new DisplayInfo<Highlighter>("Weekend", weekend));
        // week striping 
        model.addElement(new DisplayInfo<Highlighter>("Week Striping", 
                createWeekStriping()));
        // </snip>
        return model;
    }

    /**
     * @return
     */
    private Highlighter createWeekStriping() {
        // <snip> Custom visuals by Highlighter
        // use factory provides ui-dependent Highlighter
        AbstractHighlighter hl = (AbstractHighlighter) HighlighterFactory
                .createSimpleStriping();
        // tweak to stripe even rows and content-only
        hl.setHighlightPredicate(new AndHighlightPredicate(HighlightPredicate.EVEN, 
                CalendarHighlightPredicates.IS_CONTENT_IN_MONTH_PAGE));
        // </snip>
        return hl;
    }
    
    public class CalendarViewDemoControl extends AbstractBean {
        
        private Date lastFlagged;
        private Date lastUnselectable;
        private Highlighter current;
        private DaySelectionModel flagged;
        private ColorHighlighter flaggedHighlighter;
        private boolean usingCustomStringValue;
        
        
        @SuppressWarnings("unchecked")
        public CalendarViewDemoControl() {

            highlighterCombo.setModel(getHighlighterModel());
            highlighterCombo.setRenderer(new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
            Converter<?, ?> highlighters = new DisplayInfoConverter<Highlighter>();
            
            
            BindingGroup group = new BindingGroup();
            
            Binding highlighter = Bindings.createAutoBinding(READ, 
                    highlighterCombo, BeanProperty.create("selectedItem"), 
                    this, BeanProperty.create("highlighter"));
            highlighter.setConverter(highlighters);
            group.addBinding(highlighter);
            
            group.addBinding(Bindings.createAutoBinding(READ, 
                    customDayHeader, BeanProperty.create("selected"),
                    this, BeanProperty.create("usingCustomStringValue")));
            
            Binding flagged = Bindings.createAutoBinding(READ, 
                    flaggedDates, BeanProperty.create("date"),
                    this, BeanProperty.create("lastFlagged"));
            group.addBinding(flagged);
            
            Binding unselectable = Bindings.createAutoBinding(READ, 
                    unselectableDates, BeanProperty.create("date"),
                    this, BeanProperty.create("lastUnselectable"));
            group.addBinding(unselectable);
            
            group.bind();
            
            // PENDING JW: removed the color selection stuff for now
            // future will be to use highlighters anyway - revisit then
        }
        
        public Highlighter getHighlighter() {
            return current;
        }
        
        /**
         * This is bound to the highlighter combo: actually we get only one
         * @param highlighters
         */
        public void setHighlighter(Highlighter highlighter) {
            // <snip> Custom visuals by Highlighter
            // apply to calendar
            Highlighter old = getHighlighter();
            if (current != null) {
                calendarView.removeHighlighter(current);
            }
            current = highlighter;
            if (current != null) {
                calendarView.addHighlighter(current);
                // trick to ensure flagged is last
                if (flaggedHighlighter != null) {
                    calendarView.removeHighlighter(flaggedHighlighter);
                    calendarView.addHighlighter(flaggedHighlighter);
                }
            }
            // </snip>
            firePropertyChange("highlighters", old, getHighlighter());
        }
        
        /**
         * @return the lastFlagged
         */
        public Date getLastFlagged() {
            return lastFlagged;
        }
        /**
         * @param lastFlagged the lastFlagged to set
         */
        public void setLastFlagged(Date lastFlagged) {
            Date old = getLastFlagged();
            this.lastFlagged = lastFlagged;
            updateFlaggedDates();
            firePropertyChange("lastFlagged", old, getLastFlagged());
        }
        
        
        // <snip> Custom visuals for flagged dates
        private HighlightPredicate createFlaggedPredicate(final DateSelectionModel flagged) {
            // custom HighlightPredicate: true if contained in given selection model
            HighlightPredicate predicate = new HighlightPredicate() {

                @Override
                public boolean isHighlighted(Component renderer,
                        ComponentAdapter adapter) {
                    if (!(adapter.getValue() instanceof Calendar)) return false;
                    return flagged.isSelected(((Calendar) adapter.getValue()).getTime());
                }
                
            };
            return new AndHighlightPredicate(CalendarHighlightPredicates.IS_DAY_ON_PAGE, predicate);
            // </snip>
        }
        
        /**
         * 
         */
        private void updateFlaggedDates() {
            // <snip> Custom visuals for flagged dates
            // client responsibility to keep storage/ of flagged
            // and Highlighter/Predicates to mark
            if (flagged == null) {
                // PENDING JW: need support to keep model properties synched!
                flagged = new DaySelectionModel(calendarView.getLocale());
                flagged.setTimeZone(calendarView.getTimeZone());
                flagged.setSelectionMode(SelectionMode.MULTIPLE_INTERVAL_SELECTION);
                flaggedHighlighter = new ColorHighlighter(HighlightPredicate.NEVER,
                        Color.LIGHT_GRAY, null, Color.GRAY, Color.YELLOW);
            }
            // a trick to make the flagged the last to decide: 
            // remove before adding again
            calendarView.removeHighlighter(flaggedHighlighter);
            if (getLastFlagged() == null) {
                flagged.clearSelection();
            } else {
                flagged.addSelectionInterval(getLastFlagged(), getLastFlagged());
                calendarView.addHighlighter(flaggedHighlighter);
            }
            flaggedHighlighter.setHighlightPredicate(createFlaggedPredicate(flagged));
            // </snip>
        }
        
        
        /**
         * @return the lastUnselectable
         */
        public Date getLastUnselectable() {
            return lastUnselectable;
        }
        /**
         * @param lastUnselectable the lastUnselectable to set
         */
        public void setLastUnselectable(Date lastUnselectable) {
            Date old = getLastUnselectable();
            this.lastUnselectable = lastUnselectable;
            updateLastUnselectable();
            firePropertyChange("lastUnselectable", old, getLastUnselectable());
        }
        
        /**
         * 
         */
        private void updateLastUnselectable() {
            // JW: can't bind directly - it's not a property
            if (getLastUnselectable() == null) {
                calendarView.setUnselectableDates();
                return;
            }
            Set<Date> old = calendarView.getSelectionModel().getUnselectableDates();
            SortedSet<Date> result = new TreeSet<Date>(old);
            result.add(getLastUnselectable());
            calendarView.getSelectionModel().setUnselectableDates(result);
        }

        /**
         * @param usingCustomStringValue the usingCustomStringValue to set
         */
        public void setUsingCustomStringValue(boolean usingCustomStringValue) {
            boolean old = isUsingCustomStringValue();
            this.usingCustomStringValue = usingCustomStringValue;
            updateCustomStringValue();
            firePropertyChange("usingCustomStringValue", old, isUsingCustomStringValue());
        }


        private void updateCustomStringValue() {
            StringValue sv = null;
            // <snip> Custom cell content
            if (isUsingCustomStringValue()) {
                // define a custom StringValue
                 sv = new StringValue() {
                    String[] days = new String[] {"A1", "B2", "C3", "D4", "E5", "F6", "G7"};
                    @Override
                    public String getString(Object value) {
                        if (!(value instanceof Calendar)) return "";
                        int day = ((Calendar) value).get(Calendar.DAY_OF_WEEK);
                        return days[day - 1];
                    }
                    
                };
            } 
            // register the custom converter for a cell state
            calendarView.setStringValue(sv, CalendarCellState.DAY_OF_WEEK_TITLE);
            // </snip>
        }

        /**
         * @return the usingCustomStringValue
         */
        public boolean isUsingCustomStringValue() {
            return usingCustomStringValue;
        }

        
    }

//--------------------- create ui
    

    private void initComponents() {
        calendarView = new JXCalendar();
        calendarView.setName("monthView");
        
        // add to container which doesn't grow the size beyond the pref
        JComponent monthViewContainer = new JXPanel();
        monthViewContainer.add(calendarView);
        
        JPanel monthViewControlPanel = new JXPanel();
        add(monthViewControlPanel, BorderLayout.SOUTH);

        FormLayout formLayout = new FormLayout(
                "f:m:g, l:4dlu:n, f:m:g", // columns
                "c:d:g, t:2dlu:n, t:d:n "
        ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, this);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cc = new CellConstraints();
        
        builder.add(monthViewContainer, cc.xywh(1, 1, 3, 1));
        
        builder.add(createBoxPropertiesPanel(), cc.xywh(1, 3, 1, 1));
        builder.add(createConfigPanel(), cc.xywh(3, 3, 1, 1));
    }
    
    /**
     * @return
     */
    private JComponent createConfigPanel() {
        JXCollapsiblePane painterControl = new JXCollapsiblePane();
        FormLayout formLayout = new FormLayout(
                "5dlu, r:d:n, l:4dlu:n, f:m:g, l:4dlu:n, f:m:g", // columns
                "c:d:n " +
                ", t:4dlu:n, c:d:n " +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n"
        ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, painterControl);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        
        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("configurationSeparator");
        builder.add(areaSeparator, cc.xywh(1, 1, 6, 1));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;

        dayOfWeekComboBox = new JComboBox();
        JLabel dayOfWeekLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), 
                dayOfWeekComboBox, cc.xywh(widgetColumn, currentRow, 3, 1));
        dayOfWeekLabel.setName("dayOfWeekLabel");
        getLabelHandler().add(dayOfWeekLabel, dayOfWeekComboBox);
        currentRow += 2;

//        selectionModes = new JComboBox();
//        JLabel insets = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), 
//                selectionModes, cc.xywh(widgetColumn, currentRow, 3, 1));
//        currentRow += 2;
//        insets.setName("selectionModesLabel");
//        getLabelHandler().add(insets, selectionModes);

        
        unselectableDates = new JXDateChooser();
        JLabel unselectables = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), 
                unselectableDates, cc.xywh(widgetColumn, currentRow, 1, 1));
        unselectables.setName("unselectableDatesLabel");
        getLabelHandler().add(unselectables, unselectableDates);
        flaggedDates = new JXDateChooser();
        builder.add(flaggedDates, cc.xywh(widgetColumn + 2, currentRow, 1, 1));
        currentRow += 2;

        upperBound = new JXDateChooser();
        lowerBound = new JXDateChooser();
        
        JLabel lower = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), 
                lowerBound, cc.xywh(widgetColumn, currentRow, 1, 1));
        lower.setName("lowerBoundsLabel");
        getLabelHandler().add(lower, lowerBound);
        
        builder.add(upperBound, cc.xywh(widgetColumn + 2, currentRow, 1, 1));
        currentRow += 2;
        
        
        return painterControl;
    }




    /**
     * @return
     */
    private JComponent createBoxPropertiesPanel() {
        JXCollapsiblePane painterControl = new JXCollapsiblePane();
        FormLayout formLayout = new FormLayout(
                "5dlu, r:d:n, l:4dlu:n, f:d:n", //l:4dlu:n, f:d:n", // columns
                "c:d:n " +
                ", t:4dlu:n, c:d:n " +
                ", t:4dlu:n, c:d:n " +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n"
                ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, painterControl);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        
        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("monthBoxSeparator");
        builder.add(areaSeparator, cc.xywh(1, 1, 4, 1));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;
        
        highlighterCombo = new JComboBox();
        highlighterCombo.setName("highlighterCombo");
        JLabel insets = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), highlighterCombo,
                cc.xywh(widgetColumn, currentRow, 1, 1));
        insets.setName("highlighterLabel");
        getLabelHandler().add(insets, highlighterCombo);
//        builder.add(prefColumnSlider,
//                cc.xywh(widgetColumn + 2, currentRow, 1, 1));
        currentRow += 2;
        
//        leadingDaysBox = new JCheckBox();
//        leadingDaysBox.setName("leadingDaysBox");
//        
//        JLabel leadingLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
//                leadingDaysBox, cc.xywh(widgetColumn, currentRow, 1, 1));
//        leadingLabel.setName("leadingDaysLabel");
//        getLabelHandler().add(leadingLabel, leadingDaysBox);
//        
//        trailingDaysBox = new JCheckBox();
//        trailingDaysBox.setName("trailingDaysBox");
//        builder.add(trailingDaysBox, cc.xywh(widgetColumn + 2, currentRow, 1, 1));
//        currentRow += 2;
        
        
        weekNumberBox = new JCheckBox();
        weekNumberBox.setName("weekNumberBox");
      JLabel leadingLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
              weekNumberBox, cc.xywh(widgetColumn, currentRow, 1, 1));
//            leadingLabel.setName("leadingDaysLabel");
//            getLabelHandler().add(leadingLabel, weekNumberBox);
        
        currentRow += 2;

        customDayHeader = new JCheckBox();
        customDayHeader.setName("customDayHeaderBox");
        builder.add(customDayHeader, cc.xywh(widgetColumn, currentRow, 1, 1));
        currentRow += 2;

        return painterControl;
    }

    private JSpinner createSpinner(int min, int max, int value) {
        SpinnerModel model = new SpinnerNumberModel(value, min, max, 1);
        JSpinner spinner = new JSpinner(model);
        ((DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
        return spinner;
        
    }

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(CalendarBaseDemo.class.getAnnotation(DemoProperties.class).value());
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new CalendarBaseDemo());
                frame.setPreferredSize(new Dimension(800, 600));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    
    
    
}
