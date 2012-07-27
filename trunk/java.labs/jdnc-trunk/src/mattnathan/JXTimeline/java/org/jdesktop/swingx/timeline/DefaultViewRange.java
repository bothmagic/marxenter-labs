package org.jdesktop.swingx.timeline;

import org.jdesktop.swingx.JXTimeline;
import java.util.Date;
import java.math.*;

public class DefaultViewRange implements ViewRange {

    private long minimum = -1;
    private long maximum = -1;
    private long value = -1;
    private long extent = -1;
    private int size = -1;

    protected long viewMinimum = -1;
    protected long viewMaximum = -1;

    private double extentPercentage = 0.2;
    private long maxViewSize = 1000 * 60 * 60 * 24 * 7; // 7 days

    public DefaultViewRange() {
    }





    public void validate(JXTimeline tl, int size) {
        if (!isValid(tl, size)) {
            minimum = tl.getMinimum();
            maximum = tl.getMaximum();
            value = tl.getValue();
            extent = tl.getExtent();
            this.size = size;

            long vSize;
            if (extent == 0) {
                vSize = maxViewSize;
            } else {
                vSize = (long) (extent / extentPercentage);
            }
            double prop = (value - minimum) / (double) (maximum - minimum - extent);
            double anchor = value + (prop * extent);
            viewMinimum = (long) (anchor - vSize * prop);
            if (viewMinimum < minimum) {
                viewMinimum = minimum;
            }
            viewMaximum = viewMinimum + vSize;

            if(viewMaximum > maximum) {
                viewMaximum = maximum;
            }
        }
    }





    public boolean isValid(JXTimeline tl, int size) {
        return size == this.size &&
              tl.getValue() == value &&
              tl.getExtent() == extent &&
              tl.getMinimum() == minimum &&
              tl.getMaximum() == maximum;
    }





    /**
     * Get the maximum timestamp to display for the given timeline and size in pixels.
     *
     * @param tl The timeline.
     * @param size The size of the timeline in pixels.
     * @return The maximum timestamp to display.
     */
    public long getViewMaximum(JXTimeline tl, int size) {
        validate(tl, size);
        return viewMaximum;
    }





    /**
     * Get the minimum timestamp to display for the given timeline and size in pixels.
     *
     * @param tl The timeline.
     * @param size The size of the timeline in pixels.
     * @return The minimum timestamp to display.
     */
    public long getViewMinimum(JXTimeline tl, int size) {
        validate(tl, size);
        return viewMinimum;
    }
}
