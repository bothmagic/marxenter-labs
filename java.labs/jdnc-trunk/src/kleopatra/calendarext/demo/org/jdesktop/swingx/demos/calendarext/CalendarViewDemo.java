/*
 * $Id: MonthViewExtDemo.java 1226 2009-11-27 14:03:23Z kleopatra $
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.binding.LabelHandler;
import org.jdesktop.swingx.calendar.CalendarHighlightPredicates;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.CompoundHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
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
 * @author Karl George Schaefer
 * @author Joshua Outwater (original JXMonthViewDemoPanel)
 */
@DemoProperties(
    value = "JXCalendar (just showing ;)",
    category = "Controls",
    description = "Demonstrates JXCalendar features ",
    sourceFiles = {
            "org/jdesktop/swingx/demos/calendarext/CalendarViewDemo.java",
        "org/jdesktop/swingx/JXCalendar.java",
        "org/jdesktop/swingx/calendar/PageAdapter.java",
        "org/jdesktop/swingx/calendar/CalendarStringValues.java",
        "org/jdesktop/swingx/plaf/basic/BasicCalendarUI.java",
        "org/jdesktop/swingx/demos/calendarext/resources/CalendarViewDemo.properties"
        
    }
)
@SuppressWarnings("serial")
public class CalendarViewDemo extends JPanel {
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(CalendarViewDemo.class
            .getName());
    
    private JXCalendar calendarView;
    private LabelHandler labelHandler;

    private CalendarDemoControl calendarDemoControl;

    private JComponent calendarBox;
    private JComponent zoomableBox;

    private JComboBox highlighterBox;

    
//---------------------- bind
    
    @SuppressWarnings("unchecked")
    private void bind() {
        calendarDemoControl = new CalendarDemoControl();
        
        // PENDING JW: re-visit distribution of binding control ...
        // this is quite arbitrary (time of coding ;-)
        BindingGroup group = new BindingGroup();
        group.bind();
        
    }

    
    private LabelHandler getLabelHandler() {
        if (labelHandler == null) {
            labelHandler = new LabelHandler();
        }
        return labelHandler;
    }


    
    /**
     * Creates and returns a RenderingHandler which supports adding
     * Highlighters.
     *  
     * @return
     */
    private Highlighter createRenderingHandler() {
        CompoundHighlighter hl = new CompoundHighlighter();
        // <snip> Custom CalendarRenderingHandler
        // use a RenderingHandler which supports adding Highlighters.
        // new style: use highlighter for color config
        hl.addHighlighter(new ColorHighlighter(
                new CalendarHighlightPredicates.DayOfWeekHighlightPredicate(Calendar.SATURDAY), 
                null, Color.BLUE));
        // highlight property is setting opacity to true
        Highlighter transparent = new AbstractHighlighter(HighlightPredicate.IS_SELECTED) {
            
            @Override
            public Component highlight(Component component,
                    ComponentAdapter adapter) {
                // opacity is not one of the properties which are
                // guaranteed to be reset, so we have to do it here
                ((JComponent) component).setOpaque(adapter.getComponent()
                        .isOpaque());
                // call super to apply the highight - which is to
                // set the component's opacity to true
                return super.highlight(component, adapter);
            }
            
            @Override
            protected Component doHighlight(Component component,
                    ComponentAdapter adapter) {
                ((JComponent) component).setOpaque(true);
                return component;
            }
            
        };
        hl.addHighlighter(transparent);
        // </snip>
        return hl;
    }

    private ComboBoxModel createHeaderInfos() {
        // <snip> Custom CalendarHeaderHandler
        // create combo model containing handlers to choose
        DefaultComboBoxModel model = new DefaultComboBoxModel();
//        model.addElement(new DisplayInfo<CalendarHeaderHandler>("base (does nothing)", null));
//        model.addElement(new DisplayInfo<CalendarHeaderHandler>(
//                "default",
//                new SpinningCalendarHeaderHandler()));
//        model.addElement(new DisplayInfo<CalendarHeaderHandler>(
//                "default (customized)",
//                new DemoCalendarHeaderHandler(true, true)));
//        // </snip>
        return model;
        
    }
//--------------------- MonthViewDemoControl
    public class CalendarDemoControl extends AbstractBean {
        
        
        @SuppressWarnings("unchecked")
        public CalendarDemoControl() {
            DemoUtils.setSnippet("Custom CalendarRenderingHandler", calendarBox);
            
            // <snip> Custom CalendarHeaderHandler
            // configure the comboBox
            highlighterBox.setModel(createHeaderInfos());
            highlighterBox.setRenderer(new DefaultListRenderer(
                    DisplayValues.DISPLAY_INFO_DESCRIPTION));
            // </snip>
            
            DemoUtils.setSnippet("Custom CalendarHeaderHandler", highlighterBox, zoomableBox);
            
            BindingGroup group = new BindingGroup();
            
            // </snip>
//            group.addBinding(handlerBinding);
            group.bind();
        }
        



        
        
    }

//--------------------- create ui
    

    private void createMonthViewDemo() {
        calendarView = new JXCalendar();
        calendarView.setName("calendarView");
        calendarView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel monthViewContainer = new JXPanel();
        FormLayout formLayout = new FormLayout(
                "5dlu, c:d:n, l:4dlu:n, f:d:n", // columns
                "c:d:n " +
                ", t:4dlu:n, t:d:n " +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n"
        ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, monthViewContainer);
        builder.setBorder(Borders.DLU4_BORDER);
//        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        
        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("monthViewSeparator");
        builder.add(areaSeparator, cc.xywh(1, 1, 4, 1));
        builder.add(calendarView, cc.xywh(2, 3, 1, 1));
        
        
        add(monthViewContainer, BorderLayout.CENTER);
        
        JComponent monthViewControlPanel = new JXPanel();
        add(monthViewControlPanel, BorderLayout.EAST);
        JComponent extended = createExtendedConfigPanel();
        monthViewControlPanel.add(extended);

    }


    /**
     * @return
     */
    private JComponent createExtendedConfigPanel() {
        JXCollapsiblePane painterControl = new JXCollapsiblePane();
        FormLayout formLayout = new FormLayout(
                "5dlu, r:d:n, l:4dlu:n, f:d:n, l:4dlu:n, f:d:n", // columns
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
        areaSeparator.setName("extendedSeparator");
        builder.add(areaSeparator, cc.xywh(1, 1, 4, 1));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;

        
        calendarBox = new JCheckBox();
        calendarBox.setName("calendarBox");
        builder.add(calendarBox, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;
        
        zoomableBox = new JCheckBox();
        zoomableBox.setName("zoomableBox");
        builder.add(zoomableBox, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;

        highlighterBox = new JComboBox();
        highlighterBox.setName("customHeaderBox");
        JLabel headerBoxLabel = builder.addLabel(
                "", cl.xywh(labelColumn, currentRow, 1, 1),
                highlighterBox, cc.xywh(widgetColumn, currentRow, 1, 1));
        headerBoxLabel.setName("customHeaderBoxLabel");
        getLabelHandler().add(headerBoxLabel, highlighterBox);
        currentRow += 2;
        
        return painterControl;
    }

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(CalendarViewDemo.class.getAnnotation(DemoProperties.class).value());
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new CalendarViewDemo());
                frame.setPreferredSize(new Dimension(800, 600));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    
    public CalendarViewDemo() {
        super(new BorderLayout());
        createMonthViewDemo();
        //DemoUtils.injectResources(this);
        bind();
    }

    
    
}
