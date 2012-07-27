/*
 * $Id: DefaultScalableIcon.java 2758 2008-10-09 10:51:35Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.Icon;
import org.jdesktop.swingx.icon.range.AbstractRange;
import org.jdesktop.swingx.icon.range.ImageLoaderException;
import org.jdesktop.swingx.icon.range.ImageRange;
import org.jdesktop.swingx.icon.range.Range;

/**
 * <p>Default implementation of a ScalableIcon. This implementation relies on Ranges
 * to paint its content. It tries to find the Range that best matches the area
 * that it has been asked to fill using the getRangeForSize and
 * getRangeIndexForSize method.</p>
 *
 * <p>This class supports the DynamicObject interface and as such can load Ranges
 * in the background. The preloadRanges property can be set to true to load
 * ranges at construction time.</p>
 *
 * <p>By default this class will use the smallest Range as its preferred size.
 * This can be set manually through the setPreferredSize method. The maximum and
 * minimum sizes are taken from the biggest and smallest Ranges respectively. By
 * default these sizes are enforced. To override this see the setScalePolicy
 * method.</p>
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DefaultScalableIcon extends AbstractContainerIcon implements ImageRange.ImageRangeErrorPainter {
    // @todo Implement support for ratio checks on Ranges and ratio lookup equivalents

    /**
     * An ordered (by width) array of size ranges.
     */
    private Range[] ranges;
    /**
     * The index in the ranges list of the currently rendered Range.
     */
    private int visibleRangeIndex = -1;
    /**
     * The index in the ranges array of the Range that should be painted when it
     * becomes ready.
     */
    private int pendingRangeIndex = -1;
    /**
     * When true the ranges in the ranges array will be preloaded.
     */
    private boolean preloadRanges = false;

    /**
     * When true the dynamic loading of ranges is done using makeReady(true).
     * This has the effect of making any dynamic loading effectively single-threaded.
     */
    private boolean waitForRanges = false;

    /**
     * This icon is used to paint when no range has been found
     */
    private Icon loadingIcon = null;

    /**
     * This icon is used when an error occurs loading any image ranges.
     */
    private Icon errorIcon = null;

    /**
     * The range to collect the preferred size from.
     */
    private Range preferredRange = null;

    /**
     * Compares Range objects for sorting and lookup.
     */
    private static final Comparator<Range> RANGE_COMPARITOR = new RangeComparator();

// *****************************************************************************
// **
// ** Performance variables - used to improve performance in speed and memory
// **
// *****************************************************************************

    /**
     * This flag is used when the ratios of the images in this icon are the same.
     * it allows for faster lookup of images in getImageForSize which is used in
     * paintIconImpl
     *
     * This will automatically be set to false when images are found that are
     * outside a certain range in ratio from all other images. A range is used
     * due to rounding errors when computing smaller images
     */
    private boolean optimisedRangeLookupEnabled = true;

    /**
     * Used to find items in a sorted array of Ranges. See the class
     * documentation for more details.
     */
    private static final ComparisonRange COMPARISON_RANGE = new ComparisonRange();

    /**
     * The ratio of the largest range for comparison with other ranges. This is
     * the basis of the optimisedRangeLookupEnabled property check.
     */
    private float largestRatio = 1;

    /**
     * Protected constructor to create an uninitialised object.
     */
    protected DefaultScalableIcon() {
        super();
    }





    /**
     * Create a dynamically loading ScalableIcon using the given Ranges.
     *
     * @param ranges Range[]
     */
    public DefaultScalableIcon(Range ...ranges) {
        this(false, ranges);
    }





    /**
     * Create a new Scalable icon from the given ranges. When preload is true
     * each range given is loaded immediately returning only when they are all
     * ready.
     *
     * @param preload boolean
     * @param ranges Range[]
     */
    public DefaultScalableIcon(boolean preload, Range ...ranges) {
        setRanges(ranges);
        setPreloadRanges(preload);
    }





    /**
     * Sets the ranges for this icon.
     *
     * @param ranges Range[]
     */
    public void setRanges(Range ...ranges) {
        Range[] old = this.ranges;
        this.ranges = new Range[ranges.length];
        System.arraycopy(ranges, 0, this.ranges, 0, ranges.length);
        Arrays.sort(this.ranges, RANGE_COMPARITOR);
        if (isPreloadRanges()) {
            preloadRanges(this.ranges);
        }
        firePropertyChange("ranges", old, ranges);
    }





    /**
     * Preloads all ranges in the given array. Calls
     * preloadRanges(ranges, 0, ranges.length).
     *
     * @param ranges Range[]
     */
    protected void preloadRanges(Range ...ranges) {
        preloadRanges(ranges, 0, ranges.length);
    }





    /**
     * Preloads the ranges in the given array range defined by index and length.
     * This method first iterates through the ranges calling makeReady(false)
     * then repeats the process calling makeReady(true) to wait for completion.
     * The implementation is done in this way to take advantage of any
     * multithreading that is available.
     *
     * @param ranges Range[]
     * @param index int
     * @param length int
     */
    protected void preloadRanges(Range[] ranges, int index, int length) {
        // go through ranges preloading them
        for (int i = index, n = index + length; i < n; i++) { // set images loading
            ranges[i].makeReady(false);
        }
        for (int i = index, n = index + length; i < n; i++) { // wait for all to complete
            ranges[i].makeReady(true);
        }
    }





    /**
     * Sets whether the ranges stored should be preloaded.
     *
     * @param preload boolean
     */
    public void setPreloadRanges(boolean preload) {
        boolean old = this.preloadRanges;
        this.preloadRanges = preload;
        if (!old && preloadRanges) {
            preloadRanges(ranges);
        }
        firePropertyChange("preloadRanges", old, preloadRanges);
    }





    /**
     * Paints the range that fits the given dimensions the closest. The lookup of
     * the closest Range is delegated t the getRangeForSize method. If the
     * returned range from that method is not null then it is painted. If the
     * range returned is null and there are ranges as part of this icon then the
     * loading icon is painted if not null. Else nothing is painted.
     *
     * @param c Component
     * @param g Graphics
     * @param x int
     * @param y int
     * @param width int
     * @param height int
     */
    @Override
    protected void paintIconImpl(Component c, Graphics2D g, int x, int y, int width, int height) {
        Range range = getRangeForSize(width, height);
        if (range != null) {

            assert range.isReady();

            range.paint(this, c, g, x, y, width, height);

        } else if (loadingIcon != null && ranges != null && ranges.length > 0) { // means ranges exist but none are ready to be painted
            IconUtilities.paintChild(loadingIcon, c, g, x, y, width, height, getChildLocationPolicy(), getChildScalePolicy());
        } // else paint nothing as no ranges exist
    }





    /**
     * Adds or removes the listener from the visible range.
     *
     * @param index int
     */
    protected void setVisibleRangeIndex(int index) {
        if (this.visibleRangeIndex != index) {
            addAndRemoveListeners(visibleRangeIndex, index);
            visibleRangeIndex = index;
        }
    }





    /**
     * Adds or removes the listener from the pending range.
     *
     * @param index int
     */
    protected void setPendingRangeIndex(int index) {
        if (pendingRangeIndex != index) {
            addAndRemoveListeners(pendingRangeIndex, index);
            pendingRangeIndex = index;
        }
    }





    /**
     * Set to true to make the range loading happen synchronously with painting.
     * If false the ranges are loaded in the background and painting will attempt
     * to use the nearest matching range which is ready while loading occurs. The
     * default is false.
     *
     * @param waitForRanges Whether range loading is synchronous.
     */
    public void setWaitForRanges(boolean waitForRanges) {
        boolean old = isWaitForRanges();
        if (old != waitForRanges) {
            this.waitForRanges = waitForRanges;
            firePropertyChange("waitForRanges", old, isWaitForRanges());
        }
    }





    /**
     * Set the icon to paint while the first range is being loaded.
     *
     * @param loadingIcon The loading icon.
     */
    public void setLoadingIcon(Icon loadingIcon) {
        Icon old = getLoadingIcon();
        this.loadingIcon = loadingIcon;
        firePropertyChange("loadingIcon", old, getLoadingIcon());
    }





    public void setErrorIcon(Icon errorIcon) {
        this.errorIcon = errorIcon;
    }





    /**
     * Common code for adding or removing a listener
     *
     * @param oldIndex int
     * @param newIndex int
     */
    private void addAndRemoveListeners(int oldIndex, int newIndex) {
        if (oldIndex != -1) {
            uninstallChild(ranges[oldIndex]);
        }
        if (newIndex != -1) {
            installChild(ranges[newIndex]);
        }
    }





    /**
     * Gets the Range that will most closely represent the given dimensions. This
     * method will return only ranges that are ready to paint or null if no such
     * range can be found.
     *
     * @param width int
     * @param height int
     * @return Range
     */
    protected Range getRangeForSize(int width, int height) {
        Range result = null;
        int visibleIndex = -1;
        int pendingIndex = -1;

        int index = getRangeIndexForSize(width, height);
        if (index != -1) {
            pendingIndex = index;
            // ensure that the range returned is ready to paint
            result = ranges[index];

            // check if the icon is ready
            if (!result.isReady()) {
                // if not then we attempt to make it ready
                if (!result.makeReady(waitForRanges)) {
                    // if here then the result is not ready to be painted
                    // attempt to find a larger range that is ready

                    result = null;

                    for (int i = index + 1, n = ranges.length; i < n; i++) {
                        if (ranges[i].isReady()) {
                            visibleIndex = i;
                            break;
                        }
                    }

                    if (visibleIndex == -1) {
                        // we didn't find a range that was larger and ready to be painted
                        // check for smaller ranges
                        for (int i = index - 1; i >= 0; i--) {
                            if (ranges[i].isReady()) {
                                visibleIndex = i;
                                break;
                            }
                        }
                    }

                    if (visibleIndex != -1) {
                        result = ranges[visibleIndex];
                    }

                    // if we are here and result is still null then there are no ready ranges to paint
                    // the paint method will handle this case
                } else {
                    visibleIndex = pendingIndex;
                    pendingIndex = -1;
                }
            } else {
                visibleIndex = pendingIndex;
                pendingIndex = -1;
            }

        }

        setVisibleRangeIndex(visibleIndex);
        setPendingRangeIndex(pendingIndex);

        return result;
    }





    /**
     * Gets the index of the range that most closely matches but does not exceed
     * the given dimensions. If no range can be found that matches these criteria
     * then the largest range is returned. If no ranges exist then -1 is
     * returned.
     *
     * <p><emph>Note</emph>: The returned index does not necessarily point to a
     * range that is ready to paint.</p>
     *
     * @param width int
     * @param height int
     * @return int
     */
    protected int getRangeIndexForSize(int width, int height) {
        int index = -1;
        if (ranges != null && ranges.length > 0) {
            if (optimisedRangeLookupEnabled) {
                // we assume here that the width and height are in the same ratio as the
                // images stored in this component.

                COMPARISON_RANGE.setSize(width, height);
                index = Arrays.binarySearch(ranges, COMPARISON_RANGE, RANGE_COMPARITOR);

                if (index < 0) {
                    index = -index - 1;
                    if (index >= ranges.length) {
                        index = ranges.length - 1;
                    }
                }

            } else {
                // find the best matching Range that fits in the given size
                // this will be the image that takes up the most area without being
                // scaled upwards
                // OR if none exist that match this criteria the Range that most
                // closely matches the given ratio

                /** @todo Implement different ratio Ranges lookup */
            }
        }
        return index;

    }





    /**
     * Returns true if any ranges added to this icon are preloaded. If false this
     * loading is done as necessary when that range is required to be painted.
     *
     * @return boolean
     */
    public boolean isPreloadRanges() {
        return preloadRanges;
    }





    /**
     * Returns true if any non-ready ranges are loaded before painting. If false
     * then the range is loaded in the background and a change event is fired
     * when the range is ready.
     *
     * @return {@code true} if ranges are loaded before painting, false if a
     *   best guess image is placed in place while the requested range is made
     *   ready.
     */
    public boolean isWaitForRanges() {
        return waitForRanges;
    }





    /**
     * Gets the icon to paint when this icons ranges have not been loaded yet
     *
     * @return The icon to paint while loading.
     */
    public Icon getLoadingIcon() {
        return loadingIcon;
    }





    /**
     * Gets the icon to display if an error occurred while loading the range.
     *
     * @return The error icon.
     */
    public Icon getErrorIcon() {
        return errorIcon;
    }





    /**
     * Set the range to use to measure the preferred size of this icon. This Range does not need to be one of this icons
     * children but it is advised to be. If set to null then the default behaviour is to use the first (smallest) range
     * as the preferred.
     *
     * @param preferredRange The preferred range.
     */
    public void setPreferredRange(Range preferredRange) {
        Range old = getPreferredRange();
        this.preferredRange = preferredRange;
        if (old != getPreferredRange()) {
            fireStateChanged();
        }
    }





    /**
     * Gets the preferred range to use for sizing this icon. The default is the first range. If there are no ranges and
     * no non-null preferredRange has been set this will return null.
     *
     * @return The preferred range for sizing this icon.
     */
    public Range getPreferredRange() {
        Range result = preferredRange;
        if (result == null) {
            if (ranges != null && ranges.length > 0) {
                result = ranges[0];
            }
        }
        return result;
    }





    /**
     * Gets the width of the preferred range.
     *
     * @return The width of the preferred range for this icon.
     */
    public int getIconWidth() {
        Range r = getPreferredRange();
        return r == null ? 0 : r.getRangeWidth();
    }





    /**
     * Gets the height of the preferred range.
     *
     * @return The height of the preferred range for this icon.
     */
    public int getIconHeight() {
        Range r = getPreferredRange();
        return r == null ? 0 : r.getRangeHeight();
    }





    /**
     * Gets the preferred size based on the closest matching range for the given size.
     *
     * @param c The component to paint to.
     * @param width The target width of the paint area.
     * @param height The target height of the paint area.
     * @param result The dimension to place the results in.
     * @return The preferred size.
     */
    @Override
    public Dimension fitInto(Component c, int width, int height, Dimension result) {
        if (result == null) {
            result = new Dimension();
        }
        int index = getRangeIndexForSize(width, height);
        if (index != -1) {
            Range range = ranges[index];
            IconUtilities.getDefault(getScalePolicy()).fitInto(
                  new Dimension(range.getRangeWidth(), range.getRangeHeight()), new Dimension(width, height), result);
        }
        return result;
    }





    /**
     * This delegates the painting to the error icon if non-null else fills the area with red.
     *
     * {@inheritDoc}
     */
    public void paintError(ScalableIcon container, Component c, Graphics g, int x, int y, int width, int height, ImageLoaderException e) {
        Icon icon = getErrorIcon();
        if (icon != null) {
            paintChildIcon(icon, c, g, x, y, width, height);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }





    /**
     * Comparator used for comparing and sorting ranges.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private static class RangeComparator implements Comparator<Range> {
        public int compare(Range o1, Range o2) {
            return o1.getRangeWidth() - o2.getRangeHeight();
        }





        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != getClass()) {
                return false;
            }

            return obj == this;
        }
    }







    /**
     * This class is used as a single instance variable for finding the position
     * in the ranges array of the best matching range for a given dimension.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private static class ComparisonRange extends AbstractRange {
        private int width = 0;
        private int height = 0;

        public void setSize(int width, int height) {
            this.width = width;
            this.height = height;
        }





        public void setWidth(int width) {
            this.width = width;
        }





        public void setHeight(int height) {
            this.height = height;
        }





        public boolean isReady() {
            throw new UnsupportedOperationException("You shouldnt be calling this method");
        }





        public boolean makeReady(boolean wait) {
            throw new UnsupportedOperationException("You shouldnt be calling this method");
        }





        public void paint(ScalableIcon container, Component c, Graphics g, int x, int y, int width, int height) {
            throw new UnsupportedOperationException("You shouldnt be calling this method");
        }





        public int getRangeWidth() {
            return width;
        }





        public int getRangeHeight() {
            return height;
        }

    }
}
