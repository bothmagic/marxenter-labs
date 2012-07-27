/*
 * Created on 02.12.2009
 *
 */
package swingx.plaf.animated;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.CompoundHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.plaf.basic.core.BasicXListUI;
import org.jdesktop.swingx.rollover.RolloverProducer;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;
import org.pushingpixels.trident.ease.TimelineEase;

import swingx.trident.Installer;
import swingx.trident.TimelineTracker;
import swingx.trident.TimelineX;

/**
 * Subclass of BasicXListUI with selection- and rollover fades with 
 * ColorHighlighters.<p>
 * 
 * 
 * This class uses the new TimelineTracker (aka: timelineMap).
 */
public class AnimatedXListUI extends BasicXListUI {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(AnimatedXListUI.class
            .getName());
    
    public static ComponentUI createUI(JComponent comp) {
        return new AnimatedXListUI();
    }

    private CompoundHighlighter selectionHighlighter;
    private TimelineTracker<Integer> fadeTracker;
    private TimelineTracker<Integer> rolloverTracker;

    private RowSorterListener rowSorterListener;
    
    
    protected void processSelectionFading(ListSelectionEvent e) {
        for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) {
            if (list.isSelectedIndex(i)) {
                fadeIn(i);
            } else {
                fadeOut(i);
            }
            
        }
    }

    private void fadeOut(int currentRow) {
        Timeline t = fadeTracker.getTimeline(currentRow);
        if (t != null) {
            t.playReverse();
        }
        LOG.info("fadeout: " + currentRow + " / " + t);
    }

    private void fadeIn(int currentRow) {
        TimelineX t = fadeTracker.getTimeline(currentRow);
        if (t == null) {
            HighlightPredicate predicate = new ViewRowHighlightPredicate(currentRow);
            t = createSelectionTimeline(predicate);
            fadeTracker.add(t, currentRow);
        }
        if (!t.isDone()) 
            t.play();
    }

    private TimelineX createSelectionTimeline(HighlightPredicate predicate) {
        // PENDING JW: this is not really a fade-in - doesn't "highlight"
        // selected cells if no selection color set
//        final ColorHighlighter currentRowHL = new ColorHighlighter(predicate);
        // track-down: with SelectionHighlighter (normal colors == selection colors)
        // there's a short flicker on start. Looks like background is painted
        // once (reaction of component to selection changed) then this
        // highlighter hooks in and starts from zero
        ColorHighlighter currentRowHL = new SelectionHighlighter(predicate);
        // start with unselected colors instead of null is okay
        // remaining issue: independent fades of selection and rollover
        // need to be combined - as Kirill explained in forum
        // http://bit.ly/8M3LhJ
        currentRowHL.setBackground(list.getBackground());
        currentRowHL.setForeground(list.getForeground());
        // ,null, null, list.getBackground(), null);
        // MattePainter painter = new MattePainter(null);
        // final PainterHighlighter currentRowHL = new
        // PainterHighlighter(dummy.getHighlightPredicate(), painter);
        Installer installer = new Installer() {
            
            @Override
            public void release(Object target) {
                selectionHighlighter.removeHighlighter((Highlighter) target);
            }
            
            @Override
            public void install(Object target) {
                selectionHighlighter.addHighlighter((Highlighter) target);
            }
            
        };
        TimelineX fadeIn = new TimelineX(currentRowHL, installer);
        TimelineEase ease = new Spline(0.5f);
        fadeIn.setEase(ease);
        fadeIn.addPropertyToInterpolate("background", list.getBackground(),
                // ColorUtil.setAlpha(list.getSelectionBackground(), 0),
                list.getSelectionBackground());
        fadeIn.addPropertyToInterpolate("foreground", list.getForeground(), 
                list.getSelectionForeground());
        
        long duration = 2000;
        float timelineStartPosition = 0.0f;
        
        if (predicate instanceof ViewRowHighlightPredicate) {
            int row = ((ViewRowHighlightPredicate) predicate).getRow();
            Timeline rollover = rolloverTracker.getTimeline(row);
            if (rollover != null) {
                float rolloverPosition = rollover.getTimelinePosition();
                timelineStartPosition = 0.5f * rolloverPosition;
                LOG.info("found rollover: " + rollover);
            }
        }
        fadeIn.setDuration(duration);
        fadeIn.moveToDurationFraction(timelineStartPosition);
        return fadeIn;
    }

    /* (non-Javadoc)
     * @see org.jdesktop.swingx.plaf.basic.core.BasicXListUI#installDefaults()
     */
    @Override
    protected void installDefaults() {
        super.installDefaults();
        fadeTracker = new TimelineTracker<Integer>();
        selectionHighlighter = new CompoundHighlighter();
        
        list.addHighlighter(selectionHighlighter);
        rolloverTracker = new TimelineTracker<Integer>();
    }

    
    /* (non-Javadoc)
     * @see org.jdesktop.swingx.plaf.basic.core.BasicXListUI#installSortUI()
     */
    @Override
    protected void installSortUI() {
        super.installSortUI();
        if (list.getRowSorter() != null) {
            rowSorterListener = new RowSorterListener() {
                
                @Override
                public void sorterChanged(RowSorterEvent e) {
                    fadeTracker.dispose();
                }
            };
            list.getRowSorter().addRowSorterListener(rowSorterListener);
        }
    }
    
    protected void processRolloverFading(PropertyChangeEvent evt) {
        if (!RolloverProducer.ROLLOVER_KEY.equals(evt.getPropertyName())) return;
        Point currentPoint = (Point) evt.getNewValue();
        Point lastPoint = (Point) evt.getOldValue();
        if (isRowChanged(lastPoint, currentPoint)) {
            updateFade(lastPoint, currentPoint);
        }

    }

    private void updateFade(Point lastPoint, Point currentPoint) {
        int lastRow = lastPoint != null ? lastPoint.y : -1;
        if (lastRow >= 0) {
            Timeline t = rolloverTracker.getTimeline(lastRow);
            if (t != null) {
                t.playReverse();
            }
        }
        int currentRow = currentPoint != null ? currentPoint.y : -1;
        if (currentRow >= 0) {
            HighlightPredicate rolloverPredicate = new ViewRowHighlightPredicate(currentRow);
            TimelineX t = createRolloverTimeline(rolloverPredicate);
            rolloverTracker.add(t, currentRow);
            if (t!= null) {
                t.play();
            }
        }
    }

    private TimelineX createRolloverTimeline(HighlightPredicate rolloverPredicate) {
        ColorHighlighter currentRowHL = new ColorHighlighter(rolloverPredicate);
        Installer installer = new Installer() {
            
            @Override
            public void release(Object target) {
                selectionHighlighter.removeHighlighter((Highlighter) target);
            }
            
            @Override
            public void install(Object target) {
                selectionHighlighter.addHighlighter((Highlighter) target);
            }
            
        };
        TimelineX fadeIn = new TimelineX(currentRowHL, installer);
        TimelineEase ease = new Spline(0.5f);
        fadeIn.setEase(ease);
        fadeIn.addPropertyToInterpolate("background", 
                list.getBackground(),
//                list.getSelectionBackground()
                ColorUtil.setAlpha(list.getSelectionBackground(), 125)
                );
        fadeIn.addPropertyToInterpolate("foreground", list.getForeground(), 
                list.getSelectionForeground());
        fadeIn.setDuration(2000);
        return fadeIn;
    }

    private boolean isRowChanged(Point lastPoint, Point currentPoint) {
        if ((lastPoint == null) && (currentPoint == null)) {
            return false;
        }
        if ((lastPoint != null) && (currentPoint != null)) {
            return lastPoint.y != currentPoint.y;
        }
        return true;
    }

    @Override
    protected PropertyChangeListener createPropertyChangeListener() {
        return new MyPropertyChangeHandler();
    }

    public class MyPropertyChangeHandler extends PropertyChangeHandler {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            super.propertyChange(e);
            processRolloverFading(e);
        }
        
    }
    
    @Override
    protected ListSelectionListener createListSelectionListener() {
        return new MyListSelectionHandler();
    }


    public class MyListSelectionHandler extends ListSelectionHandler {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            super.valueChanged(e);
            processSelectionFading(e);
        }
        
        
    }
    
    public static class SelectionHighlighter extends ColorHighlighter {
        
        public SelectionHighlighter(HighlightPredicate predicate) {
            super(predicate);
        }

        
        @Override
        public void setBackground(Color color) {
            super.setBackground(color);
        }


        /* (non-Javadoc)
         * @see org.jdesktop.swingx.decorator.ColorHighlighter#getSelectedBackground()
         */
        @Override
        public Color getSelectedBackground() {
            return super.getBackground();
        }

        /* (non-Javadoc)
         * @see org.jdesktop.swingx.decorator.ColorHighlighter#getSelectedForeground()
         */
        @Override
        public Color getSelectedForeground() {
            return super.getForeground();
        }
        
        
    }
}
