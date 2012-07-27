/*
 * Created on 08.12.2009
 *
 */
package swingx.trident;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;

/**
 * Storage of timelines with logic to auto-cleanup for DisposeMode.DONE_REVERSE, that
 * is after a single full cycle of forward/reverse playing.<p>
 * 
 * PENDING JW: DONE_FORWARD not really supported, needed? use-case?.
 * 
 */
public class TimelineTracker<T> {
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(TimelineTracker.class
            .getName());
    
    public enum DisposeMode {
        DONE_FORWARD, DONE_REVERSE
    }

    private Map<Comparable<T>, TimelineX> timelineMap;

    private Map<TimelineX, Comparable<T>> reverseMap;
    
    public TimelineTracker() {
        this.timelineMap = new HashMap<Comparable<T>, TimelineX>();
        this.reverseMap = new HashMap<TimelineX, Comparable<T>>();
    }
    
    public synchronized void add(TimelineX timeline, Comparable<T> id) {
        TimelineCallback callback = createTimelineCallback(id, DisposeMode.DONE_REVERSE);
        if (callback != null) {
            timeline.addCallback(callback);
        }
        timelineMap.put(id, timeline);
        reverseMap.put(timeline, id);
    }

    public synchronized TimelineX getTimeline(Comparable<T> id) {
        return timelineMap.get(id);
    }

    public synchronized void dispose() {
        for (Comparable<T> key : timelineMap.keySet()) {
            endTimeline(key);
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                List<Comparable<T>> keys = new ArrayList<Comparable<T>>(timelineMap.keySet());
                for (Comparable<T> key : keys) {
                    disposeTimeline(key);
                }
            }
        });
    }
    
    protected void endTimeline(Comparable<T> id) {
        if (timelineMap.get(id) != null) {
            timelineMap.get(id).end();
        }
    }
    
    protected void disposeTimeline(Comparable<T> id) {
        TimelineX timeline = timelineMap.remove(id);
        reverseMap.remove(timeline);
        if (timeline != null) {
            timeline.release();
        }
    }


    private TimelineCallback createTimelineCallback(final Comparable<T> id, DisposeMode disposeMode) {
        if (DisposeMode.DONE_FORWARD == disposeMode) {
            TimelineCallbackAdapter callback = new TimelineCallbackAdapter() {

                @Override
                public void onTimelineStateChanged(TimelineState oldState,
                        TimelineState newState, float durationFraction,
                        float timelinePosition) {
                    if (isDoneForward(oldState, newState)) {
                        disposeTimeline(id);
                    }
                }
                
            };
            return callback;
        }
        if (DisposeMode.DONE_REVERSE == disposeMode) {
            TimelineCallbackAdapter callback = new TimelineCallbackAdapter() {

                @Override
                public void onTimelineStateChanged(TimelineState oldState,
                        TimelineState newState, float durationFraction,
                        float timelinePosition) {
                    if (newState == TimelineState.READY) {
                        initTweakedState(id);
                    }
//                    if (newState == TimelineState.PLAYING_FORWARD) {
//                        installTimeline(id);
//                    }
                    if (newState == TimelineState.PLAYING_REVERSE) {
                        resetDoneFlag(id);
                    }
                    if (isDoneReverse(oldState, newState)) {
                        disposeTimeline(id);
                    }
                }

                /* (non-Javadoc)
                 * @see org.pushingpixels.trident.callback.TimelineCallbackAdapter#onTimelinePulse(float, float)
                 */
                @Override
                public void onTimelinePulse(float durationFraction,
                        float timelinePosition) {
//                    if (durationFraction == 0.0f) {
//                        new RuntimeException("whodunit? ").printStackTrace();
//                    } else 
//                    LOG.info("duration fraction: " + durationFraction);
                }
                
                
                
            };
            return callback;
        }
        return null;
    }

//    protected void installTimeline(Comparable<T> id) {
//        TimelineX timeline = getTimeline(id);
//        if (timeline != null) {
////            timeline.tweakInitialState();
////            timeline.install();
//        }
//    }

    protected void initTweakedState(Comparable<T> id) {
        TimelineX timeline = getTimeline(id);
        if (timeline != null && !timeline.isDone()) {
            timeline.tweakInitialState();
            timeline.install();
        }
    }

    protected void resetDoneFlag(Comparable<T> id) {
        TimelineX timeline = getTimeline(id);
        if (timeline != null) {
            timeline.resetDoneFlag();
        }
    }

    protected boolean isDoneForward(TimelineState oldState,
            TimelineState newState) {
        return TimelineState.CANCELLED == newState || 
            (TimelineState.PLAYING_FORWARD == oldState && TimelineState.DONE == newState);
    }
    
    protected boolean isDoneReverse(TimelineState oldState,
            TimelineState newState) {
        return TimelineState.CANCELLED == newState || 
            (TimelineState.PLAYING_REVERSE == oldState && TimelineState.DONE == newState);
    }
    
}
