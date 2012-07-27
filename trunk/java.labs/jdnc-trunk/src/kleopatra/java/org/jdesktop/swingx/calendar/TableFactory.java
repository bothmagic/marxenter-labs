/*
 * Created on 22.01.2008
 *
 */
package org.jdesktop.swingx.calendar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SpinnerModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.BorderHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.HyperlinkProvider;
import org.jdesktop.swingx.renderer.LabelProvider;

public class TableFactory {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(TableFactory.class
            .getName());

    private Font derivedFont;
    private Border underlineBorder;
    private Border paddingBoxBorder;
    private Border monthPaddingBorder;
    private JXMonthView monthView;


    public TableFactory(JXMonthView monthView) {
        this.monthView = monthView;
        monthView.setSelectionDate(new Date());
        derivedFont = monthView.getFont().deriveFont(Font.BOLD);
        paddingBoxBorder = BorderFactory.createEmptyBorder(
                monthView.getBoxPaddingY() + 1, monthView.getBoxPaddingX(), 
                monthView.getBoxPaddingY() + 1, monthView.getBoxPaddingX() + 2);
        underlineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK);
        monthPaddingBorder = BorderFactory.createEmptyBorder(
                10, 5, 10, 5); 
            
    }
    
    public JXTable createConfigureDetailsTable(DateSpinner spinner, AbstractHyperlinkAction<Date> action) {
        String name = spinner.getName();
        if ("monthDetails".equals(name)) {
            LinkTableModel model = new DateTableModel(monthView.getCalendar(), spinner, action);
            return createConfigureMonthDetailsTable(model);
        } else if ("yearDetails".equals(name)) {
            LinkTableModel model = new MonthTableModel(monthView.getCalendar(), spinner, action);
            return createConfigureYearDetailsTable(model);
        } else if ("decadeDetails".equals(name)) {
            LinkTableModel model = new YearTableModel(monthView.getCalendar(), spinner, action);
            return createConfigureDecadeDetailsTable(model);
        }
        throw new IllegalArgumentException("unknown spinner identifier: " + name);
        
    }
    
//--------------------- rendering specials 
    
    private HighlightPredicate getTodayPredicate() {
        HighlightPredicate predicate = new HighlightPredicate() {
            
            public boolean isHighlighted(Component renderer,
                    ComponentAdapter adapter) {
                if (adapter.getValue() instanceof Date) {
                    Calendar calendar = monthView.getCalendar();
                    calendar.setTime((Date) adapter.getValue());
                    CalendarUtils.startOfDay(calendar);
                    return calendar.getTime().equals(monthView.getToday());
                }
                return false;
            }
            
        };
        return predicate;
    }

    private HighlightPredicate getCurrentDatePredicate() {
        HighlightPredicate predicate = new HighlightPredicate() {

            public boolean isHighlighted(Component renderer,
                    ComponentAdapter adapter) {
                if (adapter.getValue() instanceof Date) {
                    Calendar calendar = monthView.getCalendar();
                    calendar.setTime((Date) adapter.getValue());
                    CalendarUtils.startOfDay(calendar);
                    return calendar.getTime().equals(monthView.getSelectionDate());
                }
                return false;
            }
            
        };
        return predicate;
    }
    
    
    /**
     * Renderer for showing the weekdays as header.
     * 
     * @return
     */
    private TableCellRenderer createMonthHeaderRenderer() {
        LabelProvider provider = new LabelProvider(JLabel.TRAILING) {

            @Override
            protected void configureState(CellContext context) {
                super.configureState(context);
                rendererComponent.setFont(derivedFont);
                rendererComponent.setBorder(paddingBoxBorder);
            }
            
        };
        return new DefaultTableRenderer(provider);
    }

    /**
     * Hyperlink Renderer for showing the dates.  
     */
    private TableCellRenderer createDateRenderer(AbstractHyperlinkAction<Date> linkAction,
            final Border padding, int alignment) {
        
        HyperlinkProvider hp = new HyperlinkProvider(linkAction, Date.class) {
            @Override
            protected void configureState(CellContext context) {
                super.configureState(context);
                rendererComponent.setHorizontalAlignment(getHorizontalAlignment());
                rendererComponent.setBorder(padding);
            }

            @Override
            protected JXHyperlink createRendererComponent() {
                JXHyperlink comp = super.createRendererComponent();
                comp.setUnclickedColor(Color.BLACK);
                return comp;
            }
            
            
            
        };
        hp.setHorizontalAlignment(alignment);
        return new DefaultTableRenderer(hp);
    }

    private Highlighter[] createCurrentDateHighlighters() {
        return new Highlighter[] { 
                new ColorHighlighter(getCurrentDatePredicate(), Color.MAGENTA, 
                        null),
                new BorderHighlighter(getTodayPredicate(), 
                        BorderFactory.createLineBorder(Color.BLACK))
                
        };
        
    }

 //---------------- create and configure details tables 
    
    public JXTable createConfigureMonthDetailsTable(LinkTableModel dateTableModel) {
        
        JXTable table = new JXTable();
        table.setCellSelectionEnabled(true);
        table.setVisibleRowCount(6);
        table.setFont(monthView.getFont());
        table.setShowGrid(false, false);
        
        configureDayOfWeeksHeader(table);
        table.setDefaultRenderer(Date.class, 
                createDateRenderer(dateTableModel.selectAction, paddingBoxBorder, JLabel.TRAILING));

        table.setHighlighters(createCurrentDateHighlighters());
        table.setModel(dateTableModel);
        table.packAll();
        Component comp = table.prepareRenderer(table.getCellRenderer(1, 1), 1, 1);
        table.setRowHeight(comp.getPreferredSize().height);
        
        return table;
    }


    private void configureDayOfWeeksHeader(JXTable table) {
        table.getTableHeader().setDefaultRenderer(createMonthHeaderRenderer());
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBorder(underlineBorder);
    }

    public JXTable createConfigureYearDetailsTable(LinkTableModel model) {
        JXTable table = new JXTable();
        table.setCellSelectionEnabled(true);
        table.setFont(monthView.getFont());
        table.setShowGrid(false, false);
        table.setTableHeader(null);
        
        table.setDefaultRenderer(Date.class, 
                createDateRenderer(model.selectAction, monthPaddingBorder, JLabel.CENTER));
        table.setHighlighters(createCurrentDateHighlighters());
        table.setModel(model);
        Component comp = table.prepareRenderer(table.getCellRenderer(1, 1), 1, 1);
        table.setRowHeight(comp.getPreferredSize().height);
        
        return table;
    }

    public JXTable createConfigureDecadeDetailsTable(LinkTableModel model) {
        JXTable table = new JXTable();
        table.setCellSelectionEnabled(true);
        table.setFont(monthView.getFont());
        table.setShowGrid(false, false);
        table.setTableHeader(null);
        
        table.setDefaultRenderer(Date.class, 
                createDateRenderer(model.selectAction, monthPaddingBorder, JLabel.CENTER));
        table.setHighlighters(createCurrentDateHighlighters());
        table.setModel(model);
        Component comp = table.prepareRenderer(table.getCellRenderer(1, 1), 1, 1);
        table.setRowHeight(comp.getPreferredSize().height);
        
        return table;
    }
    
    public static abstract class LinkTableModel extends AbstractTableModel {

        protected Calendar calendar;
        protected SpinnerModel current;
        protected AbstractHyperlinkAction<Date> selectAction;
        protected int calendarField;
        protected int rowCount;
        protected int columnCount;
        
        public LinkTableModel(Calendar calendar, SpinnerModel current,
                AbstractHyperlinkAction<Date> zoomIn) {
            init(calendar, current, zoomIn);
        }

        public int getColumnCount() {
            return columnCount;
        }
        
        public int getRowCount() {
            return rowCount;
        }
        public Object getValueAt(int rowIndex, int columnIndex) {
            int fieldValue = getFieldValue(rowIndex, columnIndex);
            if (isValidFieldValue(fieldValue)) {
                calendar.setTime((Date) current.getValue());
                calendar.set(calendarField, fieldValue);
                return calendar.getTime();
            }
            return null;
        }

        protected boolean isValidFieldValue(int fieldValue) {
            return true;
        }
        protected abstract int getFieldValue(int rowIndex, int columnIndex);
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Date.class;
        }

        private void init(Calendar calendar, SpinnerModel current, AbstractHyperlinkAction<Date> selectAction) {
            this.calendar = calendar;
            this.current = current;
            this.selectAction = selectAction;
            current.addChangeListener(new ChangeListener() {
        
                public void stateChanged(ChangeEvent e) {
                    fireTableDataChanged();
                    
                }
                
            });
            calendarField = Calendar.DAY_OF_MONTH;
        }
        
    }
    
    public static class DateTableModel extends LinkTableModel {
        
        private String[] weekDays;
        private List<String> week;
        public DateTableModel(Calendar calendar, SpinnerModel current, AbstractHyperlinkAction<Date> zoomIn) {
            super(calendar, current, zoomIn);
            columnCount = 7;
            rowCount = 5;
            JXMonthView view = new JXMonthView();
            weekDays = view.getDaysOfTheWeek();
            week = new ArrayList<String>();
            for (int i = calendar.getFirstDayOfWeek() - 1; i < weekDays.length; i++) {
                week.add(weekDays[i]);
            }
            for (int i = 0; i < calendar.getFirstDayOfWeek() - 1; i++) {
                week.add(weekDays[i]);
            }
        }

        @Override
        public String getColumnName(int column) {
            return week.get(column);
        }

        @Override
        protected boolean isValidFieldValue(int fieldValue) {
            calendar.setTime((Date) current.getValue());
            int max = calendar.getActualMaximum(calendarField);
            return (fieldValue > 0 && fieldValue <= max);
        }

        @Override
        protected int getFieldValue(int rowIndex, int columnIndex) {
            calendar.setTime((Date) current.getValue());
            CalendarUtils.startOfMonth(calendar);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
            if (dayOfWeek < 0) {
                dayOfWeek += 7;
            }
            int dayOfMonth = rowIndex * getColumnCount() + columnIndex + 1 - dayOfWeek;
            return dayOfMonth;
        }
        
    }

    public static class MonthTableModel extends LinkTableModel {

        public MonthTableModel(Calendar calendar, SpinnerModel current, 
                AbstractHyperlinkAction<Date> selectAction) {
            super(calendar, current, selectAction);
            calendarField = Calendar.MONTH;
            columnCount = 4;
            rowCount = 3;
        }


        @Override
        protected int getFieldValue(int rowIndex, int columnIndex) {
            int monthOfYear = rowIndex * getColumnCount() + columnIndex;
            return monthOfYear;
        }
    }
    
    public static class YearTableModel extends LinkTableModel {

        public YearTableModel(Calendar calendar, SpinnerModel current, 
                AbstractHyperlinkAction<Date> selectAction) {
            super(calendar, current, selectAction);
            calendarField = Calendar.YEAR;
            columnCount = 4;
            rowCount = 3;
        }


        @Override
        protected int getFieldValue(int rowIndex, int columnIndex) {
            int startOfDecade = startOfDecade();
            int yearOfDecade = rowIndex * getColumnCount() + columnIndex;
            int calendarFieldValue = startOfDecade + yearOfDecade;
            return calendarFieldValue;
        }
        
        public int startOfDecade() {
            calendar.setTime((Date) current.getValue());
            int year = calendar.get(Calendar.YEAR);
            int tenth = year % 10;
            return year - tenth;
        }
    }
    

}
