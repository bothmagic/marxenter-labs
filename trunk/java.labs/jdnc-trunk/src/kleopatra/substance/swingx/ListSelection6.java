/*
 * Created on 30.11.2009
 *
 */
package swingx;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.test.AncientSwingTeam;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.substance.api.skin.SubstanceNebulaLookAndFeel;
import org.pushingpixels.substance.internal.ui.SubstanceListUIX;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;

import swingx.trident.TimelineTracker;
import swingx.trident.TimelineX;

public class ListSelection6 extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ListSelection6.class
            .getName());

    public static void main(String[] args) {
        final ListSelection6 test = new ListSelection6();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    setSubstance();
//                    test.runInteractiveTests();
                    test.runInteractiveTests("interactive.*Compare.*");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        });
    }
    
    /**
     * Compare normal Substance (use SubstanceXXRenderer) with SubstanceListUIX ( 
     */
    public void interactiveCompareSubstanceRendererCoreRenderer() {
        final JList list = new JList(AncientSwingTeam.createNamedColorListModel());
        list.setBackground(Color.WHITE);
        final JList xlist = new JList(list.getModel()) {
            @Override
            public void updateUI() {
                setUI(new SubstanceListUIX());
            }
        };
        LOG.info("xlist ui?" + xlist.getUI());
        JXFrame frame = wrapWithScrollingInFrame(list, xlist, "substance <-> core");
        Action clearSelection =  new AbstractAction("clearSelection") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                xlist.clearSelection();
                list.clearSelection();
            }
        };
        addAction(frame, clearSelection);
        show(frame); //, 500, 300);
    }
    /**
     * In Substance, selection back isn't as saturated as the 
     */
    public void interactiveCompareSelection() {
        final JList list = new JList(AncientSwingTeam.createNamedColorListModel());
        list.setBackground(Color.WHITE);
        final JXList xlist = new JXList(list.getModel(), true);
        xlist.setVisibleRowCount(xlist.getElementCount());
        xlist.setRolloverEnabled(true);
        LOG.info("xlist ui?" + xlist.getUI());
        JXFrame frame = wrapWithScrollingInFrame(list, xlist, "compare core <-> x");
        Action sort = new AbstractAction("sort") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
               xlist.toggleSortOrder();
                
            }
        };
        addAction(frame, sort);
        
        Action reset = new AbstractAction("reset") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                xlist.resetSortOrder();
            }
        };
        addAction(frame, reset);
        
        Action clearSelection =  new AbstractAction("clearSelection") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                xlist.clearSelection();
                list.clearSelection();
            }
        };
        addAction(frame, clearSelection);
        show(frame); //, 500, 300);
    }
    
    public void interactiveTimelineViewerTweak() {
        final DefaultListModel model = new DefaultListModel();
        JXList list = new JXList(model);
        list.setVisibleRowCount(40);
        JXFrame frame = wrapWithScrollingInFrame(list, "TimelineStates: tweaked");
        final JLabel label = new JLabel("just a longish text .................");
        addStatusComponent(frame, label);
        label.setOpaque(true);
        final TimelineTracker<String> tracker = new TimelineTracker<String>();
        Action start = new AbstractAction("play ") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                TimelineX stored = tracker.getTimeline("normalT");
                final TimelineX timeline = stored != null ? stored : new TimelineX(label);
                
                if (stored == null) {
                    timeline.addPropertyToInterpolate("background", 
                            ColorUtil.setAlpha(Color.MAGENTA, 0), Color.MAGENTA);
                    TimelineCallback callback = new UIThreadTimelineCallbackAdapter() {
                        
                        /* (non-Javadoc)
                         * @see org.pushingpixels.trident.callback.TimelineCallbackAdapter#onTimelineStateChanged(org.pushingpixels.trident.Timeline.TimelineState, org.pushingpixels.trident.Timeline.TimelineState, float, float)
                         */
                        @Override
                        public void onTimelineStateChanged(
                                TimelineState oldState, TimelineState newState,
                                float durationFraction, float timelinePosition) {
                            model.insertElementAt("state: " +timeline.toString(), 0);
                        }
                        
                        /* (non-Javadoc)
                         * @see org.pushingpixels.trident.callback.TimelineCallbackAdapter#onTimelinePulse(float, float)
                         */
                        @Override
                        public void onTimelinePulse(float durationFraction,
                                float timelinePosition) {
                            model.insertElementAt("pulse: " + timeline.toString(), 0);
                        }
                        
                        
                        
                    };
                    timeline.addCallback(callback);
                    timeline.setDuration(2000);
                    timeline.moveToDurationFraction(0.5f);
                    tracker.add(timeline, "normalT");
                } 
                model.insertElementAt("---------  play ", 0);
                timeline.play();
            }
        };
        addAction(frame, start);
        Action reverse = new AbstractAction("reverse ") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                TimelineX stored = tracker.getTimeline("normalT");
                if (stored != null) {
                    model.insertElementAt("---------  reverse ", 0);
                    stored.playReverse();
                }
            }
        };
        addAction(frame, reverse);
        frame.pack();
        show(frame, 500, frame.getHeight()); //, 300, 300);
    }
    
    
    public void interactiveTimelineViewer() {
        final DefaultListModel model = new DefaultListModel();
        JXList list = new JXList(model);
        list.setVisibleRowCount(40);
        JXFrame frame = wrapWithScrollingInFrame(list, "TimelineStates");
        final JLabel label = new JLabel("just a longish text .................");
        addStatusComponent(frame, label);
        label.setOpaque(true);
        Action start = new AbstractAction("play ") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                TimelineX stored = (TimelineX) label.getClientProperty("normalT");
                final TimelineX timeline = stored != null ? stored : new TimelineX(label);
                
                if (stored == null) {
                    timeline.addPropertyToInterpolate("background", 
                            ColorUtil.setAlpha(Color.MAGENTA, 0), Color.MAGENTA);
                    TimelineCallback callback = new UIThreadTimelineCallbackAdapter() {
                        
                        /* (non-Javadoc)
                         * @see org.pushingpixels.trident.callback.TimelineCallbackAdapter#onTimelineStateChanged(org.pushingpixels.trident.Timeline.TimelineState, org.pushingpixels.trident.Timeline.TimelineState, float, float)
                         */
                        @Override
                        public void onTimelineStateChanged(
                                TimelineState oldState, TimelineState newState,
                                float durationFraction, float timelinePosition) {
                            model.insertElementAt("state: " +timeline.toString(), 0);
                        }

                        /* (non-Javadoc)
                         * @see org.pushingpixels.trident.callback.TimelineCallbackAdapter#onTimelinePulse(float, float)
                         */
                        @Override
                        public void onTimelinePulse(float durationFraction,
                                float timelinePosition) {
                            model.insertElementAt("pulse: " + timeline.toString(), 0);
                        }
                        
                        
                        
                    };
                    label.putClientProperty("normalT", timeline);
                    timeline.addCallback(callback);
                    timeline.setDuration(2000);
                } 
                model.insertElementAt("---------  play ", 0);
                timeline.play();
            }
        };
        addAction(frame, start);
        Action reverse = new AbstractAction("reverse ") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                TimelineX stored = (TimelineX) label.getClientProperty("normalT");
                if (stored != null) {
                    model.insertElementAt("---------  reverse ", 0);
                    stored.playReverse();
                }
            }
        };
        addAction(frame, reverse);
        frame.pack();
        show(frame, 500, frame.getHeight()); //, 300, 300);
    }
    
    public static void setSubstance() {
        try {
//            UIManager.put(JXList.uiClassID, "swingx.plaf.animated.AnimatedXListUI");
            UIManager.put(JXList.uiClassID, "org.pushingpixels.substance.internal.ui.SubstanceListUIX");
            LookAndFeel substance = new SubstanceNebulaLookAndFeel();
            UIManager.setLookAndFeel(substance);
            AnimationConfigurationManager.getInstance().setTimelineDuration(2000);
        } catch (Exception e) {
            System.out.println("Substance failed to initialize");
        }

    }
}
