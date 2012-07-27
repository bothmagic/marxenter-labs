/*
 * $Id: MatchingTextHighlighter.java 2835 2008-10-24 02:51:30Z gregtan $
 *
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */
package org.jdesktop.swingx.decorator;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.renderer.PainterAware;

/**
 * <p>
 * <code>Highlighter</code> implementation that changes the background behind
 * characters that match a regular expression. The highlighting style can be
 * configured with a {@link Painter}.
 * </p>
 * <p>
 * This highlighter is designed to work with a {@link SearchPredicate}. All
 * other predicate types will be ignored and no highlighting will be performed.
 * </p>
 * 
 * <p>
 * <strong>NOTE: This highlighter is designed to work with renderers that both
 * extend {@link JLabel} and implements {@link PainterAware}. Other renderers
 * will be left undecorated.</strong>
 * </p>
 * 
 * 
 * @author gregtan
 */
public class MatchingTextHighlighter extends AbstractHighlighter {

    /**
     * Comparator that orders rectangles by their x coordinate.
     */
    private static final Comparator<Rectangle> X_AXIS_RECTANGLE_COMPARATOR =
        new Comparator<Rectangle>() {
      @Override
        public int compare(Rectangle o1, Rectangle o2) {
          return o1.x - o2.x;
        }  
    };
    
    /**
     * Painter that delegates character highlighting to {@link #painter}.
     */
    private final DelegatingPainter delegatingPainter = new DelegatingPainter();

    /**
     * The painter used for highlighting characters.
     */
    private Painter<JLabel> painter;

    // Rectangles and insets fields to minimize object instantiation,
    // used in findHighlightAreas method
    private Rectangle viewR = new Rectangle();
    private Rectangle iconR = new Rectangle();
    private Rectangle textR = new Rectangle();
    private Insets insets = new Insets(0, 0, 0, 0);

    /**
     * Instantiates a <code>MatchingTextHighlighter</code> with no highlight
     * predicate or painter.
     */
    public MatchingTextHighlighter() {
        this(null, null);
    }

    /**
     * Instantiates a <code>MatchingTextHighlighter</code> with no highlight
     * predicate that paints with the specified painter.
     * 
     * @param painter
     *            the painter used to render matching text
     */
    public MatchingTextHighlighter(Painter<JLabel> painter) {
        this(null, painter);
    }

    /**
     * <p>
     * Instantiates a <code>MatchingTextHighlighter</code> with the given
     * predicate that matches text with the specified pattern with the specified
     * highlight color.
     * </p>
     * 
     * @param predicate
     *            the HighlightPredicate to use
     * @param painter
     *            the painter used to render matching text
     */
    public MatchingTextHighlighter(HighlightPredicate predicate,
            Painter<JLabel> painter) {
        super(predicate);
        setPainter(painter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean canHighlight(Component component, ComponentAdapter adapter) {
        return component instanceof JLabel && component instanceof PainterAware
                && painter != null
                && getHighlightPredicate() instanceof SearchPredicate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component doHighlight(Component component,
            ComponentAdapter adapter) {
        ((PainterAware) component).setPainter(delegatingPainter);

        return component;
    }

    /**
     * Returns the painter used for highlighting matching characters.
     * 
     * @return a <code>Painter</code>
     */
    public Painter<JLabel> getPainter() {
        return painter;
    }

    /**
     * Sets the painter used for highlighting matching characters.
     * 
     * @param painter
     *            a <code>Painter</code>
     */
    public void setPainter(Painter<JLabel> painter) {
        if (areEqual(painter, this.painter)) {
            return;
        }
        this.painter = painter;
        fireStateChanged();
    }

    /**
     * Finds the rectangles that contain rendered characters that match the
     * pattern.
     * 
     * @param object
     *            an optional configuration parameter. This may be null.
     * @param width
     *            width of the area to paint.
     * @param height
     *            height of the area to paint.
     * @return a <code>List</code> of <code>Rectangle</code>s marking characters
     *         to highlight
     */
    protected List<Rectangle> findHighlightAreas(JLabel object, int width,
            int height) {
        String text = object.getText();

        insets = object.getInsets(insets);

        viewR.x = 0 + insets.left;
        viewR.y = 0 + insets.bottom;
        viewR.width = width - insets.right;
        viewR.height = height - insets.top;
        
        // Reset the text and view rectangle x any y coordinates.
        // These are not set to 0 in SwingUtilities.layoutCompoundLabel
        iconR.x = iconR.y = 0;
        textR.x = textR.y = 0;

        FontMetrics fm = object.getFontMetrics(object.getFont());
        String clippedText = SwingUtilities.layoutCompoundLabel(object, fm,
                text, object.getIcon(), object.getVerticalAlignment(), object
                        .getHorizontalAlignment(), object
                        .getVerticalTextPosition(), object
                        .getHorizontalTextPosition(), viewR, iconR, textR,
                object.getIconTextGap());

        int xOffset = calculateXOffset(object, viewR, textR);

        String textToSearch = clippedText;
        // Check to see if the text will be clipped
        if (!object.getText().equals(clippedText)) {
            // TODO There has to be a better way that assuming ellipses
            // are the last characters of the text
            textToSearch = clippedText.substring(0, clippedText.length() - 3);
        }

        return createHighlightAreas(textToSearch, fm, xOffset, height);
    }

    /**
     * Creates the rectangles that contain matched characters in the given text.
     * 
     * @param text
     *            the text to search
     * @param fm
     *            the font metrics of the rendered font
     * @param xOffset
     *            the x offset at which text rendering starts
     * @param height
     *            the height of painted highlights
     * @return a <code>List</code> of highlight areas to paint
     */
    protected List<Rectangle> createHighlightAreas(String text, FontMetrics fm,
            int xOffset, int height) {
        SearchPredicate predicate = (SearchPredicate) getHighlightPredicate();
        Matcher matcher = predicate.getPattern().matcher(text);

        List<Rectangle> highlightAreas = null;
        int startFrom = 0;
        while (startFrom < text.length() && matcher.find(startFrom)) {
            if (highlightAreas == null) {
                highlightAreas = new ArrayList<Rectangle>();
            }

            int start = matcher.start();
            int end = matcher.end();

            if (start == end) {
                // empty matcher will cause infinite loop
                break;
            }

            startFrom = end;

            int highlightx;
            int highlightWidth;

            if (start == 0) {
                // start highlight from the start of the field
                highlightx = textR.x + xOffset;
            } else {
                // Calculate the width of the unhighlighted text to
                // get the start of the highlighted region.
                String strToStart = text.substring(0, start);
                highlightx = textR.x + fm.stringWidth(strToStart) + xOffset;
            }

            // Get the width of the highlighted region
            String highlightText = text.substring(start, end);
            highlightWidth = fm.stringWidth(highlightText);

            highlightAreas.add(new Rectangle(highlightx, 0, highlightWidth,
                    height));
        }

        if (highlightAreas == null) {
            highlightAreas = Collections.emptyList();
        } else {
            coalesceHighlightAreas(highlightAreas);
        }
        return highlightAreas;
    }

    /**
     * Joins highlight rectangles that mark adjacent horizontal areas into
     * single rectangles. This is useful to renderers that vary horizontally,
     * such a horizontal gradient - the gradient will not restart when there
     * are two adjacent highlight areas.
     * 
     * @param highlightAreas a <code>List</code> of <code>Rectangle</code>s.
     */
    protected void coalesceHighlightAreas(List<Rectangle> highlightAreas) {
        Collections.sort(highlightAreas, X_AXIS_RECTANGLE_COMPARATOR);
        
        int i = 0;
        while (i < highlightAreas.size() - 1) {
            Rectangle r1 = highlightAreas.get(i);
            Rectangle r2 = highlightAreas.get(i + 1);

            if (r1.x + r1.width == r2.x) {
                r1.width += r2.width;
                highlightAreas.remove(i + 1);
            } else {
                i++;
            }
        }
    }

    /**
     * Calculates the x offset of highlights based on component orientation and
     * text direction.
     * 
     * @param component
     *            the renderer component
     * @param viewR
     *            the view rectangle of the renderer component
     * @param textR
     *            the text rectangle of the renderer component
     * @return the number of pixels to offset the highlight from the left edge
     *         of the component
     */
    protected int calculateXOffset(JLabel component, Rectangle viewR,
            Rectangle textR) {
        int horizAlignment = component.getHorizontalAlignment();
        boolean leftToRight = component.getComponentOrientation()
                .isLeftToRight();

        if (horizAlignment == SwingConstants.LEFT
                || (horizAlignment == SwingConstants.LEADING && leftToRight)
                || (horizAlignment == SwingConstants.TRAILING && !leftToRight)) {
            return 0;
        } else if (horizAlignment == SwingConstants.RIGHT
                || (horizAlignment == SwingConstants.TRAILING && !leftToRight)
                || (horizAlignment == SwingConstants.LEADING && leftToRight)) {
            return viewR.width - textR.width;
        } else if (horizAlignment == SwingConstants.CENTER) {
            return (viewR.width - textR.width) / 2;
        }
        throw new AssertionError("Unknown horizonal alignment "
                + horizAlignment);
    }

    /**
     * Painter that draws highlight rectangles at matching character positions.
     */
    private class DelegatingPainter implements Painter<JLabel> {

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint(Graphics2D g, JLabel object, int width, int height) {
            List<Rectangle> highlightAreas = findHighlightAreas(object, width,
                    height);
            for (Rectangle r : highlightAreas) {
                Graphics2D scratchGraphics = (Graphics2D) g.create(r.x, r.y,
                        r.width, r.height);
                painter.paint(scratchGraphics, object, r.width, r.height);
                scratchGraphics.dispose();
            }
        }

    }
}
