/*
 * Created on 01.04.2009
 *
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.PainterHighlighter;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.LabelProvider;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.renderer.StringValue;

/**
 * Experiments to find out which parts of the package private renderer/highlighter and
 * header support would need to be exposed.
 * 
 */
public class CalendarExperiments extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(CalendarExperiments.class.getName());
    
    public static void main(String[] args) {
        CalendarExperiments test = new CalendarExperiments();
        try {
            test.runInteractiveTests();
//            test.runInteractiveTests("interactive.*Provider.*");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        new JXMonthView();
        // KEEP this is global state - uncomment for debug painting completely
        UIManager.put(JXMonthView.uiClassID, "org.jdesktop.swingx.plaf.basic.BasicWMonthViewUI");
        UIManager.put("JXDatePicker.forceZoomable", Boolean.TRUE);
        // KEEP this is global state - uncomment for debug painting completely
        UIManager.put("JXMonthView.trailingDayForeground", Color.YELLOW);
        UIManager.put("JXMonthView.leadingDayForeground", Color.ORANGE);
        UIManager.put("JXMonthView.weekOfTheYearForeground", Color.GREEN);
        UIManager.put("JXMonthView.unselectableDayForeground", Color.MAGENTA);
    }

    
    public void interactiveHighlighter() {
        JWMonthView monthView = new JWMonthView();
        final HighlightPredicate predicate = new HighlightPredicate() {

            @Override
            public boolean isHighlighted(Component renderer,
                    ComponentAdapter adapter) {
                if (!(adapter instanceof CalendarAdapter) || (!(adapter.getValue() instanceof Calendar))) return false;
                
                CalendarAdapter cAdapter = (CalendarAdapter) adapter;
                if (cAdapter.dayState == CalendarState.IN_MONTH) {
                    Calendar calendar = (Calendar) adapter.getValue();
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    return month %2 == 0 && day == 7;
                };
                return false;
            }
            
        };
        final PainterHighlighter hl = new PainterHighlighter(predicate, getPainter("green-orb.png"));
        ActionListener l = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                HighlightPredicate p = hl.getHighlightPredicate();
                hl.setHighlightPredicate(HighlightPredicate.NEVER == p ? predicate : HighlightPredicate.NEVER);
            }};
        // animate the lucky day highlight - not yet working, nobody is listening to highlighter changes
        new Timer(500, l).start();    
        // note: the rendering handler internally uses a painter for crossing out unselectable dates
        // this here will overwrite the internal one
        monthView.addHighlighter(hl);
        showMonthView(monthView, "custom highlighter");
    }
    /**
     * Custom provider: LabelProvider with custom text position (relative to icon).
     * 
     */
    public void interactiveCustomComponentProvider() {
        JWMonthView monthView = new JWMonthView();
        StringValue defaultSV = monthView.createDayStringValue();
        CachedIconValue iv = createIconValue(new EmptyIcon(10, 10));
        LabelProvider provider = new LabelProvider(new MappedValue(defaultSV, iv));
        // show the icon above the text
        provider.getRendererComponent(null).setVerticalTextPosition(JLabel.BOTTOM);
        provider.getRendererComponent(null).setHorizontalTextPosition(JLabel.CENTER);
        // use the provider for in-month and today state
        monthView.setComponentProvider(CalendarState.IN_MONTH, provider);
        monthView.setComponentProvider(CalendarState.TODAY, provider);
        // do not use the same instance in trailing/leading - but need to set a similarly 
        // configured with empty icon to align correctly
        LabelProvider standin = new LabelProvider(new MappedValue(defaultSV, new ConstantIconValue(iv.emptyIcon)));
        // show the icon above the text
        standin.getRendererComponent(null).setVerticalTextPosition(JLabel.BOTTOM);
        standin.getRendererComponent(null).setHorizontalTextPosition(JLabel.CENTER);
        monthView.setComponentProvider(CalendarState.TRAILING, standin);
        monthView.setComponentProvider(CalendarState.LEADING, standin);
        // similar for weekNumber, dayOfWeek
        showMonthView(monthView, "custom sv custom provider (textPosition)");
    }
    
    /**
     * Custom StringValue.
     */
    public void interactiveCustomStringValue() {
        JWMonthView monthView = new JWMonthView();
        StringValue defaultSV = monthView.createDayStringValue();
        IconValue iv = createIconValue(new EmptyIcon(10, 10));
        monthView.setStringValue(CalendarState.IN_MONTH, new MappedValue(defaultSV, iv));
        showMonthView(monthView, "custom sv with icons");
    }
    /**
     * Sanity ... loaded?
     */
    public void interactiveMoonIcons() {
        Calendar calendar = Calendar.getInstance();
        IconValue iv = createIconValue(new EmptyIcon(30, 30));
        JComponent comp = new JXPanel(new GridLayout(5, 7));
        for (int i = 1; i < 5 * 7; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            JLabel label = new JLabel(String.valueOf(i));
            label.setIcon(iv.getIcon(calendar));
            comp.add(label);
        }
        showInFrame(comp, "loaded?");
    }
    
    public static class ConstantIconValue implements IconValue {

        private Icon icon;

        public ConstantIconValue() {
            this(null);
        }
        public ConstantIconValue(Icon object) {
            this.icon = object;
        }
        
        @Override
        public Icon getIcon(Object value) {
            return icon;
        }
        
    }
    
    public static class CachedIconValue implements IconValue {
        Map<Object, Icon> iconCache;
        Class<?> base = CalendarExperiments.class;
        Icon emptyIcon;
        Map<Integer, String> phaseMap;
        
        public CachedIconValue() {
            this(new EmptyIcon(20, 20));
        }
        public CachedIconValue(Icon emptyIcon) {
            this.emptyIcon = emptyIcon;
            phaseMap = new HashMap<Integer, String>();
            phaseMap.put(2, "Newmoon");
            phaseMap.put(10, "Firstquartermoon");
            phaseMap.put(18, "Fullmoon");
            phaseMap.put(26, "Lastquartermoon");
        }

        @Override
        public Icon getIcon(Object value) {
            if (!(value instanceof Calendar)) return null;
            Calendar calendar = (Calendar) value;
            String phase = phaseMap.get(calendar.get(Calendar.DAY_OF_MONTH));
            if (phase != null) {
                return loadIcon(phase);
            }
            return emptyIcon;
        }

        private int getIconHeight() {
            return emptyIcon != null ? emptyIcon.getIconHeight() : 20;
        }
        
        private Icon loadIcon(String string) {
            Icon result = getIconFromCache(string);
            if (result == null) {
                result = cacheIcon(string);
            }
            return result == null ? emptyIcon : result;
        }

        private Icon cacheIcon(String string) {
            Icon result = loadIconFromResource(string);
            if (result != null) {
                cacheIcon(string, result);
            }
            return result;
        }

        private void cacheIcon(String string, Icon result) {
            if (iconCache == null) {
                iconCache = new HashMap<Object, Icon>();
            }
            iconCache.put(string, result);
        }

        /**
         * @param string
         */
        private Icon loadIconFromResource(String string) {
            String resourceName = string + ".gif";
            URL url = base.getResource(resourceName);
            if (url == null) return null;
            try {
                BufferedImage image = ImageIO.read(url);
                if (image.getHeight() > getIconHeight()) {
                    image = GraphicsUtilities.createThumbnail(image, getIconHeight());
                }
                return new ImageIcon(image);
            } catch (IOException e) {
            }
            return null;
        }

        private Icon getIconFromCache(String string) {
            if (iconCache == null) return null;
            return iconCache.get(string);
        }
        
    }

    private CachedIconValue createIconValue(final Icon emptyIcon) {
        return new CachedIconValue(emptyIcon);
        
    }

 
    private Painter<?> getPainter(String resource) {
        Painter<?> yesPainter = null;
        try {
            yesPainter = new ImagePainter(ImageIO
                    .read(getClass().getResource(resource)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return yesPainter;
    }

    /**
     * @param monthView
     * @param frameTitle
     */
    private void showMonthView(final JXMonthView monthView, String frameTitle) {
//        monthView.setZoomable(true);
        monthView.setDayForeground(Calendar.SUNDAY, Color.BLUE);
        monthView.setDaysOfTheWeekForeground(Color.RED);
        monthView.setFlaggedDayForeground(Color.CYAN);
        monthView.setSelectionBackground(Color.GRAY);
        monthView.setSelectionForeground(Color.GREEN);
        monthView.setTodayBackground(Color.PINK);
        monthView.setTraversable(true);
        monthView.setShowingWeekNumber(true);
        monthView.setShowingLeadingDays(true);
        monthView.setShowingTrailingDays(true);
        monthView.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);
        monthView.setPreferredColumnCount(2);
        monthView.setPreferredRowCount(2);
        final JXFrame frame = wrapInFrame(monthView, frameTitle);
        final JXDatePicker picker = new JXDatePicker();
        picker.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(JXDatePicker.CANCEL_KEY)) return;
                if (picker.getDate() == null) return;
                monthView.setFlaggedDates(picker.getDate());
            }
            
        });
        final JXDatePicker unselectable = new JXDatePicker();
        unselectable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(JXDatePicker.CANCEL_KEY)) return;
                if (unselectable.getDate() == null) return;
                monthView.setUnselectableDates(unselectable.getDate());
            }
            
        });
        final JComboBox zoneSelector = new JComboBox(Locale.getAvailableLocales());
        // Synchronize the monthView's and selector's zones.
        zoneSelector.setSelectedItem(monthView.getLocale());

        // Set the monthView's time zone based on the selected time zone.
        zoneSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Locale zone = (Locale) zoneSelector.getSelectedItem();
                SwingXUtilities.setComponentTreeLocale(frame, zone);
            }
        });


        JComponent pickers = Box.createHorizontalBox();
        pickers.add(new JLabel("Flagged: "));
        pickers.add(picker);
        pickers.add(new JLabel("Unselectable: "));
        pickers.add(unselectable);
        pickers.add(new JLabel("Locale: "));
        pickers.add(zoneSelector);
        frame.add(pickers, BorderLayout.SOUTH);
        addComponentOrientationToggle(frame);
        show(frame);
    }



}
