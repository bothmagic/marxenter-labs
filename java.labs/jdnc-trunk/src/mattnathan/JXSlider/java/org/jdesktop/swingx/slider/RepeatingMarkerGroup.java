package org.jdesktop.swingx.slider;

public class RepeatingMarkerGroup extends AbstractMarkerGroup {
    private long period;
    private long offset;

    /**
     * Creates a new repeating marker group with a period of 10.
     */
    public RepeatingMarkerGroup() {
        this(10);
    }





    /**
     * Create a new repeating marker group with the given period. The given period must be > 0.
     *
     * @param period The period for the marks.
     */
    public RepeatingMarkerGroup(long period) {
        this(period, 0);
    }





    /**
     * Create a new repeating marker group with the given period and offset. The period must be > 0.
     *
     * @param period The period of the marks.
     * @param offset The offset of the first mark.
     */
    public RepeatingMarkerGroup(long period, long offset) {
        if (period <= 0) {
            throw new IllegalArgumentException("period must be > 0: " + period);
        }
        this.period = period;
        this.offset = offset;
    }





    /**
     * Get the range of markers specified by the given start and end model values.
     *
     * @param startRange The start value the returned range should view.
     * @param endRange The end range (inclusive) that the returned range should view.
     * @return A snapshot of this marker group between the given ranges.
     */
    public MarkerRange getMarkers(long startRange, long endRange) {
        return makeSafe(new RepeatingMarkerRange(startRange, endRange));
    }





    /**
     * Get the offset of the first marker from 0.
     *
     * @return The first marker offset.
     */
    public long getOffset() {
        return offset;
    }





    /**
     * Get the period or rate of repeat for the markers. This will be a positive number.
     *
     * @return The period of marks.
     */
    public long getPeriod() {
        return period;
    }





    /**
     * Set the offset for the first marker from 0.
     *
     * @param offset The first marker offset.
     */
    public void setOffset(long offset) {
        long old = getOffset();
        if (old != offset) {
            this.offset = offset;
            fireMarkerChanged(0, 0, false);
        }
    }





    /**
     * Set the period of markers. This must be a number > 0.
     *
     * @param period The marker period.
     */
    public void setPeriod(long period) {
        if (period <= 0) {
            throw new IllegalArgumentException("period must be > 0: " + period);
        }
        long old = getPeriod();
        if (old != period) {
            this.period = period;
            fireMarkerChanged(0, 0, false);
        }
    }





    /**
     * Represents a section of a repeating series of marks.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     * @version 1.0
     */
    private class RepeatingMarkerRange implements MarkerRange {
        // these values are not the model min and max but the period index min and max.
        private long min;
        private long max;

        public RepeatingMarkerRange(long min, long max) {
            long offset = getOffset();
            long period = getPeriod();
            long o = offset;
            if (o < 0) {
                o = period + o;
            }
            long l = min - o;
            long r = max - o;
            this.min = ((long) Math.ceil(l / (double) period));
            this.max = r / period;
        }





        public int getSize() {
            return (int) (max - min) + 1;
        }





        public long get(int index) {
            return getOffset() + getPeriod() * (min + index);
        }

    }
}
