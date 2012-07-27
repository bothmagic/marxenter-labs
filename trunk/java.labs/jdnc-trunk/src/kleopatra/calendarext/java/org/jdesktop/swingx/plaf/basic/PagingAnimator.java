/*
 * Created on 09.03.2010
 *
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;
import org.pushingpixels.trident.ease.TimelineEase;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

/**
 * Animated page transitions.
 */
public class PagingAnimator {
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(PagingAnimator.class
            .getName());
    
    
    public enum Direction {
        FORWARD(null), BACKWARD(FORWARD), UP(true), DOWN(true, UP);
        
        private boolean vertical;
        private Direction reverse; 
        
       
        private Direction(Direction reverse) {
            this(false, reverse);
        }
        
        private Direction(boolean vertical) {
            this(vertical, null);
        }
        
        private Direction(boolean vertical, Direction reverse) {
            this.vertical = vertical;
            if (reverse != null) {
                reverse.reverse = this;
            }
            this.reverse = reverse;
        }
        
        public Direction adjustToComponentOrientation(JComponent component) {
            if (isVertical() || component.getComponentOrientation().isLeftToRight()) {
                return this;
            }
            return reverse;
        }
        
        public boolean isVertical() {
            return vertical;
        }
        
    }
    
    private BasicCalendarUI ui;
    private DoubleImageIcon icon;
    private Timeline timeline;
    
    public PagingAnimator(BasicCalendarUI ui) {
        this.ui = ui;
        icon = new DoubleImageIcon(null);
    }
    
    // <snip> Scrolling Animation
    // Prepare scroll image.
    public void beforeMove(Direction direction) {
        direction = direction.adjustToComponentOrientation(ui.calendarView);
        if (Direction.FORWARD == direction) {
            firstSnapShot();
        } else {
            lastSnapShot();
        }
        // </snip>
    }
    
    // <snip> Scrolling Animation
    // Finish preparation of scroll image and start.
    public void afterMove(Direction direction) {
        direction = direction.adjustToComponentOrientation(ui.calendarView);
        float start = 0.0f;
        float end = 1.0f;
       if (Direction.FORWARD == direction) {
           lastSnapShot();
       } else {
           firstSnapShot();
           start = 1.0f;
           end = 0.0f;
       }
       icon.compound();
       start(ui.calendarView, start, end);
       // </snip>
    }
    
    
    public boolean isRunning() {
        return timeline != null;
    }

    public void paintIcon(Graphics g) {
        Rectangle page = ui.getPageDetailsBounds();
        getIcon().paintIcon(ui.calendarView, g, page.x, page.y);
    }
   
    private void stop() {
        if (timeline != null) {
            timeline.abort();
        }
        timeline = null;
    }
    
    private void firstSnapShot() {
        stop();
        icon.setFirstImage(createPageImage());
    }
    
    private void lastSnapShot() {
        stop();
        icon.setLastImage(createPageImage());
    }
    
    // <snip> Scrolling Animation
    // Start animation.
    private void start(JComponent target, float start, float end) {
//            Direction direction) {
        icon.setRelativePositionX(start);
        timeline = new Timeline(icon);
        timeline.addPropertyToInterpolate("relativePositionX", start, end);
        timeline.addCallback(new SwingRepaintCallback(target));
        TimelineCallback callBack = new TimelineCallbackAdapter() {

            /** 
             * @inherited <p>
             */
            @Override
            public void onTimelineStateChanged(TimelineState oldState,
                    TimelineState newState, float durationFraction,
                    float timelinePosition) {
                if (newState == TimelineState.DONE) {
//                    debugPrint(timeline.getDuration(), durationFractions, positions, currentEase);
                    timeline = null;
                }
            }
        };
        timeline.addCallback(callBack);
//        debugCallback();
        timeline.play();
        // </snip>
    }

    protected Icon getIcon() {
        return icon;
    }
    
    private BufferedImage createPageImage() {
        Rectangle page = ui.getPageDetailsBounds();
        BufferedImage image = GraphicsUtilities
            .createCompatibleTranslucentImage(page.width, page.height);
        Graphics2D g = image.createGraphics();
        g.translate(-page.x, -page.y);
//        ui.calendarView.paint(g);
//        LOG.info("pageBounds: " + page);
        ui.paintDetails(g);
        g.dispose();
        return image;
    }

   
    public static class DoubleImageIcon implements Icon {

        private BufferedImage image;
        private int absoluteX;
        private BufferedImage first;
        private BufferedImage last;
        
        public DoubleImageIcon(BufferedImage image) {
            this.image = image;
        }
        
        private void setImage(BufferedImage image) {
            this.image = image;
            setRelativePositionX(1.0f);
        }
        
        public void setFirstImage(BufferedImage first) {
            this.first = first;
            setImage(null);
        }
        
        public void setLastImage(BufferedImage last) {
            this.last = last;
            setImage(null);
        }
        
        public void compound() {
            if ((first == null) || (last == null)) return;
            BufferedImage compound = GraphicsUtilities
                    .createCompatibleTranslucentImage(2 * first.getWidth(),
                            first.getHeight());
            Graphics2D g = compound.createGraphics();
            g.drawImage(first, 0, 0, null);
            g.drawImage(last, first.getWidth(), 0, null);
            g.dispose();
            setImage(compound);
        }
        
        public void setRelativePositionX(float position) {
            absoluteX = image != null ? (int) (getIconWidth() * position) : 0;
        }
        
        @Override
        public int getIconHeight() {
            return image != null ? image.getHeight() : 0;
        }

        @Override
        public int getIconWidth() {
            return image != null ? image.getWidth() / 2 : 0;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (image == null) return;
            Rectangle clip = g.getClipBounds();
            g.setClip(x, y, getIconWidth(), getIconHeight());
            g.translate(-absoluteX, 0);
            g.drawImage(image, x, y, c);
            g.translate(absoluteX, 0);
            g.setClip(clip);
        }
        
    }

    /**
     * 
     */
    private void debugCallback() {
        final List<Float> positions = new ArrayList<Float>();
        final List<Float> durationFractions = new ArrayList<Float>();
        TimelineCallback callBack = new TimelineCallbackAdapter() {

            /** 
             * @inherited <p>
             */
            @Override
            public void onTimelineStateChanged(TimelineState oldState,
                    TimelineState newState, float durationFraction,
                    float timelinePosition) {
                if (newState == TimelineState.DONE) {
//                    debugPrint(timeline.getDuration(), durationFractions, positions, currentEase);
                    timeline = null;
                }
            }

            /** 
             * @inherited <p>
             */
            @Override
            public void onTimelinePulse(float durationFraction,
                    float timelinePosition) {
                positions.add(timelinePosition);
                durationFractions.add(durationFraction);
            }
             
            
        };
        timeline.addCallback(callBack);
    }
    
    protected void debugPrint(long duration, List<Float> durationFractions,
            List<Float> positions, TimelineEase ease) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" durationFraction / timelinePosition / velocity / deltaDuration /deltaPosition ");
        float lastDurationFraction = 0f;
        float lastPosition = 0f;
        for (int i = 0; i < durationFractions.size(); i++) {
            float currentPosition = positions.get(i);
            float currentDurationFraction = durationFractions.get(i);
            float currentDuration = durationFractions.get(i) * duration;
            
            buffer.append("\n "  + 
                    currentDurationFraction + " / " + currentPosition);
            if (lastPosition > 0) {
            }
                float deltaPosition = currentPosition - lastPosition;
                float deltaDurationFraction = currentDurationFraction - lastDurationFraction;
                float velocity = 100f * deltaPosition / deltaDurationFraction ;
                
                buffer.append(" / " + velocity + " / " + deltaDurationFraction + " / " + deltaPosition);
            lastDurationFraction = currentDurationFraction;
            lastPosition = currentPosition;
        }
        LOG.info(buffer.toString());
    }


}
