/*
 * Created on 22.01.2008
 *
 */
package org.jdesktop.swingx.calendar.jsr310;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
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
import javax.time.calendar.Clock;
import javax.time.calendar.DateProvider;
import javax.time.calendar.DayOfWeek;
import javax.time.calendar.LocalDate;
import javax.time.calendar.MonthOfYear;
import javax.time.calendar.OffsetDateTime;

import org.jdesktop.swingx.JXHyperlink;
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

    protected LocalDate selectedDate;

    private Font detailsFont;


    public TableFactory(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        org.jdesktop.swingx.JXMonthView monthView = new org.jdesktop.swingx.JXMonthView();
        detailsFont = monthView.getFont();
        derivedFont = monthView.getFont().deriveFont(Font.BOLD);
        paddingBoxBorder = BorderFactory.createEmptyBorder(
                monthView.getBoxPaddingY() + 1, monthView.getBoxPaddingX(), 
                monthView.getBoxPaddingY() + 1, monthView.getBoxPaddingX() + 2);
        underlineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK);
        monthPaddingBorder = BorderFactory.createEmptyBorder(
                10, 5, 10, 5); 
            
    }
    
    public JXTable createConfigureDetailsTable(OffsetDateTimeSpinnerModel spinner, AbstractHyperlinkAction<OffsetDateTime> action) {
        String name = spinner.getName();
        if ("monthDetails".equals(name)) {
            LinkTableModel model = new DateTableModel(spinner, action);
            return createConfigureMonthDetailsTable(model);
        } else if ("yearDetails".equals(name)) {
            LinkTableModel model = new MonthTableModel(spinner, action);
            return createConfigureYearDetailsTable(model);
        } else if ("decadeDetails".equals(name)) {
            LinkTableModel model = new YearTableModel(spinner, action);
            return createConfigureDecadeDetailsTable(model);
        }
        throw new IllegalArgumentException("unknown spinner identifier: " + name);
        
    }
    
//--------------------- rendering specials 
    
    private HighlightPredicate getTodayPredicate() {
        HighlightPredicate predicate = new HighlightPredicate() {
            
            public boolean isHighlighted(Component renderer,
                    ComponentAdapter adapter) {
                if (adapter.getValue() instanceof DateProvider) {
                    LocalDate date = ((DateProvider) adapter.getValue()).toLocalDate();
                    return date.equals(Clock.systemDefaultZone().today());
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
                if (adapter.getValue() instanceof DateProvider) {
                    LocalDate date = ((DateProvider) adapter.getValue()).toLocalDate();
                    return date.equals(selectedDate);
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
    private TableCellRenderer createDateRenderer(AbstractHyperlinkAction<OffsetDateTime> linkAction,
            final Border padding, int alignment) {
        
        HyperlinkProvider hp = new HyperlinkProvider(linkAction, OffsetDateTime.class) {
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

    private Highlighter createBoundaryYearHighlighter() {
        HighlightPredicate boundaryPredicate = new HighlightPredicate() {

            public boolean isHighlighted(Component renderer,
                    ComponentAdapter adapter) {
                return (adapter.row == 0 && adapter.column == 0)
                    || (adapter.row == adapter.getRowCount() && adapter.column == adapter.getColumnCount());
            }};
        return new ColorHighlighter(boundaryPredicate, null, Color.GRAY);
        
    }
 //---------------- create and configure details tables 
    
    public JXTable createConfigureMonthDetailsTable(LinkTableModel dateTableModel) {
        
        JXTable table = new JXTable();
        table.setCellSelectionEnabled(true);
        table.setVisibleRowCount(6);
        table.setFont(detailsFont);
        table.setShowGrid(false, false);
        
        configureDayOfWeeksHeader(table);
        table.setDefaultRenderer(OffsetDateTime.class, 
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
        table.setFont(detailsFont);
        table.setShowGrid(false, false);
        table.setTableHeader(null);
        
        table.setDefaultRenderer(OffsetDateTime.class, 
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
        table.setFont(detailsFont);
        table.setShowGrid(false, false);
        table.setTableHeader(null);
        
        table.setDefaultRenderer(OffsetDateTime.class, 
                createDateRenderer(model.selectAction, monthPaddingBorder, JLabel.CENTER));
        table.setHighlighters(createCurrentDateHighlighters());
        table.addHighlighter(createBoundaryYearHighlighter());
        table.setModel(model);
        Component comp = table.prepareRenderer(table.getCellRenderer(1, 1), 1, 1);
        table.setRowHeight(comp.getPreferredSize().height);
        
        return table;
    }
    
    public static abstract class LinkTableModel extends AbstractTableModel {

        protected SpinnerModel current;
        protected AbstractHyperlinkAction<OffsetDateTime> selectAction;
        protected int rowCount;
        protected int columnCount;
        
        public LinkTableModel(SpinnerModel current,
                AbstractHyperlinkAction<OffsetDateTime> zoomIn) {
            init(current, zoomIn);
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
//                calendar.setTime((OffsetDateTime) current.getValue());
//                calendar.set(calendarField, fieldValue);
//                return calendar.getTime();
            }
            return null;
        }

        protected boolean isValidFieldValue(int fieldValue) {
            return true;
        }
        protected abstract int getFieldValue(int rowIndex, int columnIndex);
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return OffsetDateTime.class;
        }

        protected OffsetDateTime getCurrentDateTime() {
            return (OffsetDateTime) current.getValue();
        }
        private void init(SpinnerModel current, AbstractHyperlinkAction<OffsetDateTime> selectAction) {
            this.current = current;
            this.selectAction = selectAction;
            current.addChangeListener(new ChangeListener() {
        
                public void stateChanged(ChangeEvent e) {
                    fireTableDataChanged();
                    
                }
                
            });
        }
        
    }
    
    public static class DateTableModel extends LinkTableModel {
        
        private String[] weekDays;
        private List<String> week;
        
        public DateTableModel(SpinnerModel current, AbstractHyperlinkAction<OffsetDateTime> action) {
            super(current, action);
            columnCount = 7;
            rowCount = 5;
            org.jdesktop.swingx.JXMonthView view = new org.jdesktop.swingx.JXMonthView();
            weekDays = view.getDaysOfTheWeek();
            week = new ArrayList<String>();
            for (String day : weekDays) {
                week.add(day);
            }
        }

        @Override
        public String getColumnName(int column) {
            return week.get(column);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            int fieldValue = getFieldValue(rowIndex, columnIndex);
            if (isValidFieldValue(fieldValue)) {
                return getCurrentDateTime().withDayOfMonth(fieldValue);
            }
            return null;
        }

        
        @Override
        protected boolean isValidFieldValue(int fieldValue) {
            MonthOfYear month = getCurrentDateTime().getMonthOfYear();
            int max = month.lengthInDays(getCurrentDateTime().isLeapYear());
            return (fieldValue > 0 && fieldValue <= max);
        }

        @Override
        protected int getFieldValue(int rowIndex, int columnIndex) {
            OffsetDateTime date = getCurrentDateTime().withDayOfMonth(1);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            
            int dayOfMonth = rowIndex * getColumnCount() + columnIndex + 1 - dayOfWeek.getValue();
            return dayOfMonth;
        }
        
    }

    public static class MonthTableModel extends LinkTableModel {

        public MonthTableModel(SpinnerModel current, 
                AbstractHyperlinkAction<OffsetDateTime> selectAction) {
            super(current, selectAction);
            columnCount = 4;
            rowCount = 3;
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            int fieldValue = getFieldValue(rowIndex, columnIndex);
            if (isValidFieldValue(fieldValue)) {
                return getCurrentDateTime().withMonthOfYear(fieldValue);
            }
            return null;
        }


        @Override
        protected int getFieldValue(int rowIndex, int columnIndex) {
            int monthOfYear = rowIndex * getColumnCount() + columnIndex + 1;
            return monthOfYear;
        }
    }
    
    public static class YearTableModel extends LinkTableModel {

        public YearTableModel(SpinnerModel current, 
                AbstractHyperlinkAction<OffsetDateTime> selectAction) {
            super(current, selectAction);
            columnCount = 4;
            rowCount = 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            int fieldValue = getFieldValue(rowIndex, columnIndex);
            if (isValidFieldValue(fieldValue)) {
                return getCurrentDateTime().withYear(fieldValue);
            }
            return null;
        }

        @Override
        protected int getFieldValue(int rowIndex, int columnIndex) {
            int startOfDecade = startOfDecade();
            int yearOfDecade = rowIndex * getColumnCount() + columnIndex;
            int calendarFieldValue = startOfDecade + yearOfDecade;
            return calendarFieldValue - 1;
        }
        
        public int startOfDecade() {
            int year = getCurrentDateTime().getYear();
            int decadeStart = (year / 10) * 10;
            return decadeStart;
        }
    }
    

}
