package org.jdesktop.swingx.calendar;

import java.awt.Color;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.Localizable;
import org.jdesktop.swingx.calendar.CalendarStringValues.CalendarStringValue;
import org.jdesktop.swingx.decorator.CompoundHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.PainterHighlighter;
import org.jdesktop.swingx.plaf.UIManagerExt;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.LabelProvider;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.util.Contract;

/**
 * The RenderingHandler responsible for cell rendering. It provides 
 * and configures a rendering component for the given cell of
 * a JXCalendar. <p>
 * 
 * It works on behalf on a single JXCalendar instance and holds its
 * PageAdapter. 
 * 
 * PENDING JW: move into JXCalendar instead of ui - the config state is
 * bound to the concrete component, not to the ui.
 * 
 */
public class RendererHandler  {

    /** default pattern for formatters. */
    public static final String WEEK_OF_YEAR_PATTERN = "w";
    public static final String DAY_OF_WEEK_PATTERN = "E";
    public static final String DAY_PATTERN = "d";
    public static final String MONTH_CELL_PATTERN = "MMM";
    public static final String MONTH_TITLE_PATTERN = "MMMM yyyy";
    public static final String YEAR_PATTERN = "yyyy";
    

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(RendererHandler.class.getName());
    /**
     * The calendarView which cell rendering is controlled by this handler.
     */
    private JXCalendar calendarView;
    /** The CellContext for content and default visual config. */
    private PageCellContext cellContext;
    /** The ComponentAdapter on the calendarView. */
    private PageAdapter pageAdapter;
    /** The providers to use per DayState. */
    private Map<CalendarCellState, ComponentProvider<?>> providers;
    //-------- Highlight properties
//    /** The Painter used for highlighting unselectable dates. */
//    private TextCrossingPainter<?> textCross;
//    /** The foreground color for unselectable date highlight. */
//    private Color unselectableDayForeground;
    private ChangeListener highlighterChangeListener;

    public RendererHandler(JXCalendar calendar) {
        this.calendarView = calendar;
        install();
    }
    
    /**
     * Returns the string value for the given state, guaranteed to be not null.
     * 
     * @param state the type of calendar cell.
     * @return the StringValue for rendering a calendar cell of the given type.
     */
    public StringValue getStringValue(CalendarCellState state) {
        return providers.get(state).getStringValue();
    }

    /**
     * Sets the StringValue for rendering a calendar cell of the given type.
     * 
     * @param sv the StringValue to use. Maybe null to indicate use of default.
     * @param state the type of cell to use it for, must not be null.
     */
    public void setStringValue(StringValue sv, CalendarCellState state) {
        Contract.asNotNull(state, "calendar cell state must not be null");
        providers.get(state).setStringValue(getDefaultStringValue(sv, state));
        calendarView.revalidate();
    }

    /**
     * Updates internal state to the given Locale.
     * 
     * @param locale the new Locale.
     */
    public void setLocale(Locale locale) {
        for (ComponentProvider<?> provider : providers.values()) {
            if (provider.getStringValue() instanceof Localizable) {
                ((Localizable) provider.getStringValue()).setLocale(locale);
            } else {
                LOG.info("provider? " + provider.getStringValue());
            }
        }
    }

    /**
     * Returns a component configured to render the cell with the given date and state.
     * This methods can be called on any state of the calendarView (doesn't access
     * the "life" component or guards against malfunction), typically by a LayoutManager
     * to measure the cell dimensions for layout.
     * 
     * @param value
     * @param state
     * @return a component to render the cell at the given coordinates, guaranteed to be 
     *   not null. 
     */
    public JComponent prepareRendererComponent(Calendar value, CalendarCellState dayState) {
        cellContext.installContext(calendarView, value, false, false,
                dayState);
        JComponent comp = providers.get(dayState).getRendererComponent(cellContext);
        return comp;
    }


    /**
     * Returns a component configured to render the cell at the given coordinates
     * on the current page.<p>
     * 
     * PENDING JW: the calendar param is redundant - but not yet all values supported
     * in PageAdapter (missing week/day header values). Revisit and remove if that is 
     * done.
     * 
     * @param row the row index of the cell to configure, must be valid on the 
     *    current page
     * @param column the column index of the cell to configure, must be valid on the
     *    current page
     * @return a component to render the cell at the given coordinates, maybe null
     *   if the controller is not bound to a JXCalendarView.
     * @throws IllegalArgumentException if the coordinates are not valid on the current page.
     */
    public JComponent prepareRendererComponent(Calendar calendar,
            int row, int column) {
        checkValidCoordinates(row, column);
        PageAdapter adapter = pageAdapter.moveTo(row, column);
        CalendarCellState state = adapter.getCellState(row, column);
        cellContext.installContext(calendarView, adapter.getValue(), 
                adapter.isSelected(),
                adapter.hasFocus(),
                state);
        ComponentProvider<?> componentProvider = providers.get(state);
        JComponent comp = componentProvider.getRendererComponent(cellContext);
        return highlight(comp, adapter);
    }

    /**
     * Sets the <code>Highlighter</code>s to the table, replacing any old
     * settings. None of the given Highlighters must be null.
     * <p>
     * 
     * This is a bound property.
     * <p>
     * 
     * Note: as of version #1.257 the null constraint is enforced strictly. To
     * remove all highlighters use this method without param.
     * 
     * @param highlighters zero or more not null highlighters to use for
     *        renderer decoration.
     * @throws NullPointerException if array is null or array contains null
     *         values.
     * 
     * @see #getHighlighters()
     * @see #addHighlighter(Highlighter)
     * @see #removeHighlighter(Highlighter)
     * 
     */
    public void setHighlighters(Highlighter... highlighters) {
        getCompoundHighlighter().setHighlighters(highlighters);
        installHighlighters();
    }

    /**
     * Returns the <code>Highlighter</code>s used by this table. Maybe empty,
     * but guarantees to be never null.
     * 
     * @return the Highlighters used by this table, guaranteed to never null.
     * @see #setHighlighters(Highlighter[])
     */
    public Highlighter[] getHighlighters() {
        return getCompoundHighlighter().getHighlighters();
    }

    /**
     * Appends a <code>Highlighter</code> to the end of the list of used
     * <code>Highlighter</code>s. The argument must not be null.
     * <p>
     * 
     * @param highlighter the <code>Highlighter</code> to add, must not be null.
     * @throws NullPointerException if <code>Highlighter</code> is null.
     * 
     * @see #removeHighlighter(Highlighter)
     * @see #setHighlighters(Highlighter[])
     */
    public void addHighlighter(Highlighter highlighter) {
        getCompoundHighlighter().addHighlighter(highlighter);
    }

    /**
     * Removes the given Highlighter.
     * <p>
     * 
     * Does nothing if the Highlighter is not contained.
     * 
     * @param highlighter the Highlighter to remove.
     * @see #addHighlighter(Highlighter)
     * @see #setHighlighters(Highlighter...)
     */
    public void removeHighlighter(Highlighter highlighter) {
        getCompoundHighlighter().removeHighlighter(highlighter);
    }

    /**
     * Returns the CompoundHighlighter assigned to the table, null if none.
     * PENDING: open up for subclasses again?.
     * 
     * @return the CompoundHighlighter assigned to the table.
     */
    protected CompoundHighlighter getCompoundHighlighter() {
        if (highlighter == null) {
            highlighter = new CompoundHighlighter();
            highlighter.addChangeListener(getHighlighterChangeListener());
        }
        if (unselectableHighlighter == null)
            installHighlighters();
        return highlighter;
    }


    /**
     * Returns the <code>ChangeListener</code> to use with highlighters. Lazily
     * creates the listener.
     * 
     * @return the ChangeListener for observing changes of highlighters,
     *         guaranteed to be <code>not-null</code>
     */
    protected ChangeListener getHighlighterChangeListener() {
        if (highlighterChangeListener == null) {
            highlighterChangeListener = createHighlighterChangeListener();
        }
        return highlighterChangeListener;
    }

    /**
     * Creates and returns the ChangeListener observing Highlighters.
     * <p>
     * Here: repaints the table on receiving a stateChanged.
     * 
     * @return the ChangeListener defining the reaction to changes of
     *         highlighters.
     */
    protected ChangeListener createHighlighterChangeListener() {
        return new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                calendarView.repaint();
            }
        };
    }

    public void updateUI() {
        // force update of unselectable painter
        if (unselectableHighlighter != null) {
            highlighter.removeHighlighter(unselectableHighlighter);
            unselectableHighlighter = null;
        }
        getCompoundHighlighter().updateUI();
    }


    private void checkValidCoordinates(int row, int column) {
        boolean valid = (row >= 0 && row < pageAdapter.getRowCount())
            && (column >= 0 && column < pageAdapter.getColumnCount());
        if (!valid) throw new IllegalArgumentException(
                "invalid coordinates, expected (row/column): " 
                + (pageAdapter.getRowCount() - 1) + " / " + (pageAdapter.getColumnCount() - 1)
                + " but was: " + row + " / " + column);
    }

    /**
     * 
     */
    private void installHighlighters() {
        if (unselectableHighlighter == null) {
            // FIXME: update on UI change
            Color unselectableDayForeground = UIManagerExt.getColor("JXCalendar.unselectableDayForeground");
            TextCrossingPainter textCross = new TextCrossingPainter<JLabel>();

            textCross.setForeground(unselectableDayForeground);
             unselectableHighlighter = new PainterHighlighter(CalendarHighlightPredicates.IS_UNSELECTABLE,
                    textCross);
        }
        highlighter.removeHighlighter(unselectableHighlighter);
        highlighter.addHighlighter(unselectableHighlighter, true);
        
    }


    private CompoundHighlighter highlighter;
    private Highlighter unselectableHighlighter;
    
    private JComponent highlight(JComponent comp, PageAdapter adapter) {
        return (JComponent) getCompoundHighlighter().highlight(comp, adapter);
    }

    /**
     * Returns the PageAdapter for the calendar
     * @param calendarView
     * @param week
     * @param day
     * @return
     */
//    private PageAdapter getPageAdapter(int row, int column) {
//        pageAdapter.setCell(row, column);
//        return pageAdapter;
//    }


    private void install() {
        cellContext = new PageCellContext();
        pageAdapter = new PageAdapter(calendarView);
        installProviders();
    }



    /**
     * Creates and stores ComponentProviders for all DayStates.
     */
    private void installProviders() {
        providers = new HashMap<CalendarCellState, ComponentProvider<?>>();

        // PENDING JW: duplicate places which know about which to share
        StringValue sv = createDefaultStringValue(CalendarCellState.DAY_CELL);
        ComponentProvider<?> provider = new LabelProvider(sv, JLabel.RIGHT);
        providers.put(CalendarCellState.DAY_CELL, provider);
        providers.put(CalendarCellState.TODAY_CELL, provider);
        providers.put(CalendarCellState.DAY_OFF_CELL, provider);

        StringValue wsv = createDefaultStringValue(CalendarCellState.WEEK_OF_YEAR_TITLE);
        ComponentProvider<?> weekOfYearProvider = new LabelProvider(wsv,
                JLabel.RIGHT);
        providers.put(CalendarCellState.WEEK_OF_YEAR_TITLE, weekOfYearProvider);

        ComponentProvider<?> dayOfWeekProvider = new LabelProvider(
                createDefaultStringValue(CalendarCellState.DAY_OF_WEEK_TITLE),
                JLabel.CENTER);
        providers.put(CalendarCellState.DAY_OF_WEEK_TITLE, dayOfWeekProvider);

        providers.put(CalendarCellState.PAGE_TITLE, 
                new LabelProvider(createDefaultStringValue(CalendarCellState.PAGE_TITLE), 
                        JLabel.CENTER));
        
        providers.put(CalendarCellState.MONTH_CELL, 
                new LabelProvider(createDefaultStringValue(CalendarCellState.MONTH_CELL), 
                        JLabel.CENTER));

        LabelProvider yearProvider = new LabelProvider(
                createDefaultStringValue(CalendarCellState.YEAR_CELL), JLabel.CENTER);
        providers.put(CalendarCellState.YEAR_CELL, yearProvider);
        providers.put(CalendarCellState.YEAR_OFF_CELL, yearProvider);
    }

    
    private StringValue getDefaultStringValue(StringValue sv,
            CalendarCellState state) {
        if (sv != null) return sv;
        return createDefaultStringValue(state);
    }

    private StringValue createDefaultStringValue(CalendarCellState state) {
        // PENDING JW: move to cellState?
        switch (state) {
        case DAY_CELL:
        case DAY_OFF_CELL:
        case TODAY_CELL:
            return new CalendarStringValue(calendarView.getLocale(), DAY_PATTERN);
        case DAY_OF_WEEK_TITLE:
            return new CalendarStringValue(calendarView.getLocale(), DAY_OF_WEEK_PATTERN);
        case WEEK_OF_YEAR_TITLE:
            return new CalendarStringValue(calendarView.getLocale(), WEEK_OF_YEAR_PATTERN);
        case PAGE_TITLE:
            return new CalendarStringValue(calendarView.getLocale(), MONTH_TITLE_PATTERN);
        case MONTH_CELL:
            return new CalendarStringValue(calendarView.getLocale(), MONTH_CELL_PATTERN);
        case YEAR_CELL:
        case YEAR_OFF_CELL:
            return new CalendarStringValue(calendarView.getLocale(), YEAR_PATTERN);
        }
        throw new IllegalStateException("unknown or unhandled state: " + state);
    }
}