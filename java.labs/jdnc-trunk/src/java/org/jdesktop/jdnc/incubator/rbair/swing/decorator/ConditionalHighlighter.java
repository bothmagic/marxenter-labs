/*
 * $Id: ConditionalHighlighter.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.decorator;

import java.awt.Color;
import java.awt.Component;

/**
 * ConditionalHighlighter
 *
 * @author Ramesh Gupta
 */
public abstract class ConditionalHighlighter extends Highlighter {
    protected int		testColumn = 0;			// always in model coordinates
    protected int		highlightColumn = -1;	// always in model coordinates
    protected int		mask = 255;

    public ConditionalHighlighter() {
        // default constructor
    }

    /**
     * <p>Constructs a <code>ConditionalHighlighter</code> instance with the
     * specified background and foreground colors that will be used to highlight
     * the renderer component for a cell in the specified highlightColumn of any
     * row if and only if {@link #needsHighlight needsHighlight} returns true
     * for the adapter that identifies that cell.</p>
     *
     * @param cellBackground background color for highlighted cells, or null, if
     * 		background should not be changed
     * @param cellForeground foreground color for highlighted cells, or null, if
     * 		foreground should not be changed
     * @param testColumn column whose value is to be tested to determine if a
     * 		cell <em>should</em> be highlighted
     * @param highlightColumn column whose index is used to determine if a cell
     * 		<em>could</em> be highlighted; may be a valid column index in model
     * 		coordinates, or -1 to indicate all columns
     */
	public ConditionalHighlighter(Color cellBackground, Color cellForeground,
			int testColumn, int highlightColumn) {
		super(cellBackground, cellForeground);
		this.testColumn = testColumn;
		this.highlightColumn = highlightColumn;
	}

    public void setMask(int alpha) {
        mask = alpha;
    }

    public int getMask() {
        return mask;
    }

	/**
	 * Performs a conditional highlight. Calls {@link #doHighlight doHighlight} if
     * and only if {@link #needsHighlight needsHighlight} returns true.
	 *
	 * @param renderer
	 * @param adapter
	 * @return the highlighted component
	 */
	public Component highlight(Component renderer, ComponentAdapter adapter) {
		if (needsHighlight(adapter)) {
			return doHighlight(renderer, adapter);
		}
        else if (getMask() < 256) {
            return doMask(renderer, adapter);
        }
		return renderer;
	}

    protected Component doMask(Component renderer, ComponentAdapter adapter) {

        maskBackground(renderer, adapter);
        maskForeground(renderer, adapter);
        // and so on...

        return renderer;
    }

    protected void maskBackground(Component renderer, ComponentAdapter adapter) {
        Color seed = renderer.getBackground();
        Color color = adapter.isSelected() ? computeSelectedBackground(seed) : seed;
        renderer.setBackground(
            new Color((getMask() << 24) | (color.getRGB() & 0x00FFFFFF), true));
    }

    protected void maskForeground(Component renderer, ComponentAdapter adapter) {
        Color seed = renderer.getForeground();
        Color color = adapter.isSelected() ? computeSelectedForeground(seed) : seed;
        renderer.setForeground(
            new Color((getMask() << 24) | (color.getRGB() & 0x00FFFFFF), true));
    }

    /**
     *
     * @param renderer
     * @param adapter
     * @return null if the background is null; otherwise delegate to superclass
     */
	protected Color computeBackground(Component renderer, ComponentAdapter adapter) {
		return background == null ? null :
			super.computeBackground(renderer, adapter);
	}

    /**
     *
     * @param renderer
     * @param adapter
     * @return null if the foreground is null; otherwise delegate to superclass
     */
	protected Color computeForeground(Component renderer, ComponentAdapter adapter) {
		return foreground == null ? null :
			super.computeForeground(renderer, adapter);
	}

	protected Color computeSelectedForeground(Color seed) {
		return selectedForeground == null ? seed.brighter() : selectedForeground;
	}

	public int getTestColumnIndex() {
        return testColumn;
    }

    public void setTestColumnIndex(int columnIndex) {
        this.testColumn = columnIndex;
    }

    public int getHighlightColumnIndex() {
        return highlightColumn;
    }

    public void setHighlightColumnIndex(int columnIndex) {
        this.highlightColumn = columnIndex;
    }

	/**
     * Checks if the cell identified by the specified adapter is a potential
     * candidate for highlighting, and returns true if so; otherwise, it returns false.
     *
	 * @param adapter
	 * @return true if the cell identified by the specified adapter needs
     * 		highlight; false otherwise
	 */
	protected boolean needsHighlight(ComponentAdapter adapter) {
		// Before running test(), quickly check if the cell in the current
		// adapter is a potential candidate for highlighting. If so, run the test.
		// highlightColumn is always in "model" coordinates, but adapter.column
		// is in "view" coordinates. So, convert before comparing.
		if ((highlightColumn < 0) ||
			(adapter.modelToView(highlightColumn) == adapter.column)) {
			return test(adapter);
		}
		return false;	// cell is not a candidate for decoration;
	}

	/**
     * Tests if the cell identified by the specified adapter should actually be
     * highlighted, and returns true if so; otherwise, it returns false.
     *
	 * @param adapter
	 * @return true if the test succeeds; false otherwise
	 */
	protected abstract boolean test(ComponentAdapter adapter);

}