/*
 * $Id: Highlighter.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.decorator;

import java.awt.Color;
import java.awt.Component;

/**
 * <p><code>Highlighter</code> is a lightweight mechanism to modify the behavior
 * and attributes of cell renderers such as {@link javax.swing.ListCellRenderer},
 * {@link javax.swing.table.TableCellRenderer}, and
 * {@link javax.swing.tree.TreeCellRenderer} in a simple layered fashion.
 * While cell renderers are split along component lines, highlighters provide a
 * <em>common interface</em> for decorating cell renderers.
 * <code>Highlighter</code> achieves this by vectoring access to all component-specific
 * state and functionality through a {@link ComponentAdapter} object.</p>
 *
 * <p>The primary purpose of <code>Highlighter</code> is to decorate a cell
 * renderer in <em>controlled</em> ways, such as by applying a different color
 * or font to it. For example, {@link AlternateRowHighlighter} highlights cell
 * renderers with alternating background colors. In data visualization components
 * that support multiple columns with potentially different types of data, this
 * highlighter imparts the same background color consistently across <em>all</em>
 * columns of the {@link ComponentAdapter#target target} component
 * regardless of the actual cell renderer registered for any specific column.
 * Thus, the <code>Highlighter</code> mechanism is orthogonal to the cell
 * rendering mechanism.</p>
 *
 * <p>To use <code>Highlighter</code> you must first set up a
 * {@link HighlighterPipeline} using an array of <code>Highlighter</code> objects,
 * and then call setHighlighters() on a data visualization component, passing in
 * the highligher pipeline. If the array of highlighters is not null and is not
 * empty, the highlighters are applied to the selected renderer for each cell in
 * the order they appear in the array.
 * When it is time to render a cell, the cell renderer is primed as usual, after
 * which, the {@link Highlighter#highlight highlight} method of the first
 * highlighter in the {@link HighlighterPipeline} is invoked. The prepared
 * renderer, and a suitable {@link ComponentAdapter} object is passed to the
 * <code>highlight</code> method. The highlighter is expected to modify the
 * renderer in controlled ways, and return the modified renderer (or a substitute)
 * that is passed to the next highlighter, if any, in the pipeline. The renderer
 * returned by the <code>highlight</code> method of the last highlighter in the
 * pipeline is ultimately used to render the cell.</p>
 *
 * <p>The <code>Highlighter</code> mechanism enables multiple degrees of
 * freedom. In addition to specifying the actual cell renderer class, now you
 * can also specify the number, order, and class of highlighter objects. Using
 * highlighters is really simple, as shown by the following example:</p>
 *
 * <pre>
    Highlighter[]	highlighters = new Highlighter[] {
    	new <b>AlternateRowHighlighter</b>(Color.white, new Color(0xF0, 0xF0, 0xE0), null)),
    	new <b>PatternHighlighter</b>(null, Color.red, "s.*", 0)};

    HighlighterPipeline	highlighterPipeline = new HighlighterPipeline(highlighters);
    JTable table = new JTable();
    table.setHighlighters(highlighters);
 * </pre>
 *
 * <p>The above example allocates an array of <code>Highlighter</code> and populates
 * it with a new {@link AlternateRowHighlighter} and {@link PatternHighlighter}.
 * The first one in this example highlights all cells in odd rows with a white
 * background, and all cells in even rows with a silver background, but it does
 * not specify a foreground color explicitly. The second highlighter does not
 * specify a background color explicitly, but sets the foreground color to red
 * <em>if certain conditions are met</em> (see {@link PatternHighlighter} for
 * more details). In this example, if the cells in the first column of any
 * row start with the letter 's', then all cells in that row are highlighted with
 * a red foreground. Also, as mentioned earlier, the highlighters are applied in
 * the order they appear in the list.</p>
 *
 * @author Ramesh Gupta
 * @see ComponentAdapter
 * @see javax.swing.ListCellRenderer
 * @see javax.swing.table.TableCellRenderer
 * @see javax.swing.tree.TreeCellRenderer
 */
public class Highlighter {
    protected int order = -1;

    /**
     * Predefined <code>Highlighter</code> that highlights the background of
     * each cell with a pastel green "ledger" background color, and is most
     * effective when the {@link ComponentAdapter#target} component has
     * horizontal gridlines in <code>Color.cyan.darker()</code> color.
     */
    public final static Highlighter ledgerBackground =
		new Highlighter(new Color(0xF5, 0xFF, 0xF5), null);

    /**
     * Predefined <code>Highlighter</code> that decorates the background of
     * each cell with a pastel yellow "notepad" background color, and is most
     * effective when the {@link ComponentAdapter#target} component has
     * horizontal gridlines in <code>Color.cyan.darker()</code> color.
     */
    public final static Highlighter notePadBackground =
		new Highlighter(new Color(0xFF, 0xFF, 0xCC), null);

    protected Color background = null;
    protected Color foreground = null;
    protected Color selectedBackground = null;
    protected Color selectedForeground = null;

    /**
     * Default constructor.
     * Initializes background, foreground, selectedBackground, and
     * selectedForeground to null.
     */
    public Highlighter() {
        // default constructor
    }

    /**
     * Constructs a <code>Highlighter</code> with the specified
     * background and foreground colors.
     *
     * @param cellBackground background color for the renderer, or null,
     * 		to compute a suitable background
     * @param cellForeground foreground color for the renderer, or null,
     * 		to compute a suitable foreground
     */
    public Highlighter(Color cellBackground, Color cellForeground) {
        this.background = cellBackground; // could be null
        this.foreground = cellForeground; // could be null
    }

    /**
     * Decorates the specified cell renderer component for the given component
     * data adapter using highlighters that were previously set for the component.
     * This method unconditionally invokes {@link #doHighlight doHighlight} with
     * the same arguments as were passed in.
     *
     * @param renderer the cell renderer component that is to be decorated
     * @param adapter the {@link ComponentAdapter} for this decorate operation
     * @return the decorated cell renderer component
     */
    public Component highlight(Component renderer, ComponentAdapter adapter) {
        return doHighlight(renderer, adapter);
    }

    /**
     * This is the bottleneck decorate method that all highlighters must invoke
     * to decorate the cell renderer. This method invokes {@link #applyBackground
     * applyBackground}, {@link #applyForeground applyForeground},
     * {@link #applyFont applyFont} and so on, to decorate the corresponding
     * attributes of the specified component within the given adapter.
     *
     * @param renderer the cell renderer component that is to be decorated
     * @param adapter the {@link ComponentAdapter} for this decorate operation
     * @return the decorated cell renderer component
     */
    protected Component doHighlight(Component renderer, ComponentAdapter adapter) {
        applyBackground(renderer, adapter);
        applyForeground(renderer, adapter);
        applyFont(renderer, adapter); // e.g., make it bold
	// and so on...
        return renderer;
    }

    /**
     * Computes a suitable background for the renderer component within the
     * specified adapter by calling {@link #computeBackground computeBackground}
     * and applies the computed color to the component. If the computed
     * color is null, it leaves the background unchanged; Otherwise it sets the
     * component's background to the computed color.
     *
     * @param renderer the cell renderer component that is to be decorated
     * @param adapter the {@link ComponentAdapter} for this decorate operation
     */
    protected void applyBackground(Component renderer, ComponentAdapter adapter) {
        Color color = computeBackground(renderer, adapter);
        if (color != null) {
            renderer.setBackground(color);
        }
    }

    /**
     * Computes a suitable foreground for the renderer component within the
     * specified adapter by calling {@link #computeForeground computeForeground}
     * and applies the computed color to the component. If the computed
     * color is null, it leaves the foreground unchanged; Otherwise it sets the
     * component's foreground to the computed color.
     *
     * @param renderer the cell renderer component that is to be decorated
     * @param adapter the {@link ComponentAdapter} for this decorate operation
     */
    protected void applyForeground(Component renderer, ComponentAdapter adapter) {
        Color color = computeForeground(renderer, adapter);
        if (color != null) {
            renderer.setForeground(color);
        }
    }

    /**
     * Empty method. Override it to change the font of the renderer component.
     *
     * @param renderer the cell renderer component that is to be decorated
     * @param adapter the {@link ComponentAdapter} for this decorate operation
     */
    protected void applyFont(Component renderer, ComponentAdapter adapter) {
    	// must be overridden to cause any effect
    }

    /**
     * <p>Computes a suitable background for the renderer component within the
     * specified adapter and returns the computed color. The computed color
     * depends on two factors: (i) whether the background color for this
     * <code>Highlighter</code> is null or not, and (ii) whether the cell
     * identified by the specified adapter
     * {@link ComponentAdapter#isSelected isSelected} or not.</p>
     *
     * <p>If the background color for this <code>Highlighter</code> is not
     * null, this method starts with an initial value that is equal to that
     * background color, and proceeds to check the selected state of the cell.
     * Otherwise, it starts with the background color of the component whose
     * cell is being rendererd (not the background color of the renderer component
     * that was passed in), and proceeds to check the selected state of the cell.</p>
     *
     * <p>If the cell identified by the specified adapter is selected, this
     * method returns the value computed by
     * {@link #computeSelectedBackground computeSelectedBackground} when passed
     * the initial background color computed earlier. Otherwise, it simply
     * returns the initial background color computed earlier.</p>
     *
     * @param renderer the cell renderer component that is to be decorated
     * @param adapter the {@link ComponentAdapter} for this decorate operation
     * @return a suitable background color for the specified component and adapter
     */
    protected Color computeBackground(Component renderer, ComponentAdapter adapter) {
        // If this.background is null, use adapter.target.getBackground();
        // Never use renderer.getBackground() as the seed in this decorator
        // class because renderer is too promiscuous!
        Color seed = background == null ? adapter.target.getBackground() : background;
        return adapter.isSelected() ? computeSelectedBackground(seed) : seed;
    }

    /**
     * <p>Computes a suitable foreground for the renderer component within the
     * specified adapter and returns the computed color. The computed color
     * depends on two factors: (i) whether the foreground color for this
     * <code>Highlighter</code> is null or not, and (ii) whether the cell
     * identified by the specified adapter
     * {@link ComponentAdapter#isSelected isSelected} or not.</p>
     *
     * <p>If the foreground color for this <code>Highlighter</code> is not
     * null, this method starts with an initial value that is equal to that
     * foreground color, and proceeds to check the selected state of the cell.
     * Otherwise, it starts with the foreground color of the component whose
     * cell is being rendererd (not the foreground color of the renderer component
     * that was passed in), and proceeds to check the selected state of the cell.</p>
     *
     * <p>If the cell identified by the specified adapter is selected, this
     * method returns the value computed by
     * {@link #computeSelectedForeground computeSelectedBackground} when passed
     * the initial foreground color computed earlier. Otherwise, it simply
     * returns the initial foreground color computed earlier.</p>
     *
     * @param renderer the cell renderer component that is to be decorated
     * @param adapter the {@link ComponentAdapter} for this decorate operation
     * @return a suitable foreground color for the specified component and adapter
     */
    protected Color computeForeground(Component renderer, ComponentAdapter adapter) {
        // If this.foreground is null, use adapter.target.getForeground();
        // Never use renderer.getForeground() as the seed in this decorator
        // class because renderer is too promiscuous!
        Color seed = foreground == null ? adapter.target.getForeground() : foreground;
        return adapter.isSelected() ? computeSelectedForeground(seed) : seed;
    }

    /**
     * Computes the selected background color. If the selected background color
     * of this <code>Highlighter</code> is not null, this method returns that
     * color. Otherwise, it returns a {@link java.awt.Color#darker} version of
     * the specified seed color.
     *
     * @param seed initial background color; must not be null
     * @return the background color for a selected cell
     */
    protected Color computeSelectedBackground(Color seed) {
        return selectedBackground == null ?
            seed == null ? Color.gray : seed.darker() : selectedBackground;
    }

    /**
     * Computes the selected foreground color. If the selected foreground color
     * of this <code>Highlighter</code> is not null, this method returns that
     * color. Otherwise, it returns {@link java.awt.Color#white}, ignoring the
     * specified seed color.
     *
     * @param seed initial foreground color
     * @return the foreground color for a selected cell
     */
    protected Color computeSelectedForeground(Color seed) {
        return selectedForeground == null ? Color.white : selectedForeground;
    }

    /**
     * Returns the background color of this <code>Highlighter</code>.
     *
     * @return the background color of this <code>Highlighter</code>,
     * 		or null, if no background color has been set
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Sets the background color of this <code>Highlighter</code>.
     *
     * @param color the background color of this <code>Highlighter</code>,
     * 		or null, to clear any existing background color
     */
    public void setBackground(Color color) {
        background = color;
    }

    /**
     * Returns the foreground color of this <code>Highlighter</code>.
     *
     * @return the foreground color of this <code>Highlighter</code>,
     * 		or null, if no foreground color has been set
     */
    public Color getForeground() {
        return foreground;
    }

    /**
     * Sets the foreground color of this <code>Highlighter</code>.
     *
     * @param color the foreground color of this <code>Highlighter</code>,
     * 		or null, to clear any existing foreground color
     */
    public void setForeground(Color color) {
        foreground = color;
    }

    /**
     * Returns the selected background color of this <code>Highlighter</code>.
     *
     * @return the selected background color of this <code>Highlighter</code>,
     * 		or null, if no selected background color has been set
     */
    public Color getSelectedBackground() {
        return selectedBackground;
    }

    /**
     * Sets the selected background color of this <code>Highlighter</code>.
     *
     * @param color the selected background color of this <code>Highlighter</code>,
     * 		or null, to clear any existing selected background color
     */
    public void setSelectedBackground(Color color) {
        selectedBackground = color;
    }

    /**
     * Returns the selected foreground color of this <code>Highlighter</code>.
     *
     * @return the selected foreground color of this <code>Highlighter</code>,
     * 		or null, if no selected foreground color has been set
     */
    public Color getSelectedForeground() {
        return selectedForeground;
    }

    /**
     * Sets the selected foreground color of this <code>Highlighter</code>.
     *
     * @param color the selected foreground color of this <code>Highlighter</code>,
     * 		or null, to clear any existing selected foreground color
     */
    public void setSelectedForeground(Color color) {
        selectedForeground = color;
    }

}
