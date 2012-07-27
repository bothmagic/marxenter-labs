/*
 * $Id: GridPolicy.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.compound;

import org.jdesktop.swingx.icon.CompoundIcon;
import org.jdesktop.swingx.icon.IconUtilities;

import javax.swing.Icon;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Simple policy which lays out the child icons in a grid. This is based on the standard java.awt.GridLayout class but
 * adds horizontal and vertical gaps to that implementation.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class GridPolicy implements CompoundIcon.Policy {

    /**
     * The hap between each column.
     */
    private int hgap;
    /**
     * The gap between each row.
     */
    private int vgap;

    /**
     * The number of rows defining the grid.
     */
    private int rows;
    /**
     * The number of columns defining the grid.
     */
    private int cols;





    /**
     * Creates a grid policy with a default of one column per icon in a single
     * row.
     */
    public GridPolicy() {
        this(1, 0, 0, 0);
    }





    /**
     * <p>
     * Creates a grid policy with the specified number of rows and
     * columns. All icons in the policy are given equal size.
     * </p><p>
     * One, but not both, of <code>rows</code> and <code>cols</code> can
     * be zero, which means that any number of objects can be placed in a
     * row or in a column.
     * </p>
     *
     * @param rows the rows, with the value zero meaning
     *             any number of rows.
     * @param cols the columns, with the value zero meaning
     *             any number of columns.
     */
    public GridPolicy(int rows, int cols) {
        this(rows, cols, 0, 0);
    }





    /**
     * <p>
     * Creates a grid policy with the specified number of rows and
     * columns. All icons in the policy are given equal size.
     * </p><p>
     * In addition, the horizontal and vertical gaps are set to the
     * specified values. Horizontal gaps are placed between each
     * of the columns. Vertical gaps are placed between each of
     * the rows.
     * </p><p>
     * One, but not both, of <code>rows</code> and <code>cols</code> can
     * be zero, which means that any number of icons can be placed in a
     * row or in a column.
     * </p><p>
     * All {@code GridPolicy} constructors defer to this one.
     * </p>
     *
     * @param rows the rows, with the value zero meaning
     *             any number of rows
     * @param cols the columns, with the value zero meaning
     *             any number of columns
     * @param hgap the horizontal gap
     * @param vgap the vertical gap
     * @throws IllegalArgumentException if the value of both
     *                                  <code>rows</code> and <code>cols</code> is
     *                                  set to zero
     */
    public GridPolicy(int rows, int cols, int hgap, int vgap) {
        if (rows == 0 && cols == 0) {
            throw new IllegalArgumentException("Both rows and cols cannot be 0: rows=" + rows + ",cols=" + cols);
        }
        this.rows = rows;
        this.cols = cols;
        this.hgap = hgap;
        this.vgap = vgap;
    }





    /**
     * Gets the number of rows specified for this policy.
     *
     * @return int
     */
    public int getRows() {
        return rows;
    }





    /**
     * Gets the number of columns specified for this policy.
     *
     * @return int
     */
    public int getColumns() {
        return cols;
    }





    /**
     * Gets the horizontal gap between icons.
     *
     * @return int
     */
    public int getHorizontalGap() {
        return hgap;
    }





    /**
     * Gets the vertical gap between icons.
     *
     * @return int
     */
    public int getVerticalGap() {
        return vgap;
    }





    /**
     * Sets the number of rows for this policy to layout.
     *
     * @param rows int
     */
    public void setRows(int rows) {
        if (rows == 0 && this.cols == 0) {
            throw new IllegalArgumentException("Both rows and cols cannot be 0: rows=" + rows + ",cols=" + cols);
        }
        this.rows = rows;
    }





    /**
     * Sets the number of columns in this policy to the specified value. Setting
     * the number of columns has no affect on the layout if the number of rows
     * specified by a constructor or by the <tt>setRows</tt> method is non-zero.
     * In that case, the number of columns displayed in the policy is determined
     * by the total number of icons and the number of rows specified.
     *
     * @param cols the number of columns in this layout
     * @throws IllegalArgumentException if the value of both <code>rows</code> and <code>cols</code> is set to zero
     */
    public void setColumns(int cols) {
        if (cols == 0 && this.rows == 0) {
            throw new IllegalArgumentException("Both rows and cols cannot be 0: rows=" + rows + ",cols=" + cols);
        }
        this.cols = cols;
    }





    /**
     * Sets the horizontal gap between icons.
     *
     * @param hgap The horizontal gap.
     */
    public void setHorizontalGap(int hgap) {
        this.hgap = hgap;
    }





    /**
     * Sets the vertical gap between icons.
     *
     * @param vgap The vertical gap.
     */
    public void setVerticalGap(int vgap) {
        this.vgap = vgap;
    }





    /**
     * {@inheritDoc}
     */
    public void addPolicyIcon(CompoundIcon container, Icon icon, Object constraints) {
    }





    /**
     * {@inheritDoc}
     */
    public void invalidatePolicy(CompoundIcon container) {
    }





    /**
     * Paints the given icons onto the given Graphics object.
     *
     * @param container The container for this policy.
     * @param c         The Component that originated the call.
     * @param g         The Graphics to paint the icons onto.
     * @param x         The x coordinate for the painting to initiate at
     * @param y         The y coordinate for the painting to originate at
     * @param width     The width of the icon.
     * @param height    The height of the icon.
     */
    public void paintIcons(CompoundIcon container, Component c, Graphics g, int x, int y, int width, int height) {
        int nicons = container.getSize();
        int nrows = rows;
        int ncols = cols;
        boolean ltr = c.getComponentOrientation().isLeftToRight();

        if (nicons == 0) {
            return;
        }
        if (nrows > 0) {
            ncols = (nicons + nrows - 1) / nrows;
        } else {
            nrows = (nicons + ncols - 1) / ncols;
        }

        int w = width;
        int h = height;
        w = (w - (ncols - 1) * hgap) / ncols;
        h = (h - (nrows - 1) * vgap) / nrows;

        if (ltr) {
            for (int col = 0, tx = x; col < ncols; col++, tx += w + hgap) {
                paintIconColumn(container, c, g, tx, y, w, h, nicons, nrows, ncols, col);
            }
        } else {
            for (int col = 0, tx = x + width - w; col < ncols; col++, tx -= w + hgap) {
                paintIconColumn(container, c, g, tx, y, w, h, nicons, nrows, ncols, col);
            }
        }
    }





    /**
     * Simple utility method to paint a column of icons.
     *
     * @param container The container Icon.
     * @param c         The Component to paint to.
     * @param g         The Graphis to paint on.
     * @param x         The x coord to start.
     * @param y         The y coord to start.
     * @param w         The width to paint the icons.
     * @param h         The height to paint the icons.
     * @param nicons    The number of icons to paint in total.
     * @param nrows     The number of rows to paint.
     * @param ncols     The number of columns to paint.
     * @param col       This column.
     */
    private void paintIconColumn(CompoundIcon container, Component c, Graphics g, int x, int y, int w, int h,
                                 int nicons, int nrows, int ncols, int col) {
        for (int row = 0, ty = y; row < nrows; row++, ty += h + vgap) {
            int i = row * ncols + col;
            if (i < nicons) {
                IconUtilities.paintChild(container.getIcon(i), c, g, x, ty, w, h);
            }
        }
    }





    /**
     * {@inheritDoc}
     */
    public int getPolicyWidth(CompoundIcon container) {
        return getSize(container).width;
    }





    /**
     * {@inheritDoc}
     */
    public int getPolicyHeight(CompoundIcon container) {
        return getSize(container).height;
    }





    /**
     * Gets the preferred size for this icon.
     *
     * @param container The container for the child icons
     * @return The preferred size for this icon.
     */
    protected Dimension getSize(CompoundIcon container) {

        int nicons = container.getSize();
        int nrows = rows;
        int ncols = cols;

        if (nrows > 0) {
            ncols = (nicons + nrows - 1) / nrows;
        } else {
            nrows = (nicons + ncols - 1) / ncols;
        }

        Dimension temp = IconUtilities.getLargestSize(container);

        temp.height = temp.height * nrows + (nrows - 1) * vgap;
        temp.width = temp.width * ncols + (ncols - 1) * hgap;
        return temp;
    }





    /**
     * {@inheritDoc}
     */
    public void removePolicyIcon(CompoundIcon container, Icon icon) {
    }





    /**
     * Returns a string for use in debugging this compound policy.
     *
     * @return A string representing this icon.
     */
    @Override
    public String toString() {
        return getClass().getName() + "[rows=" + rows + ",cols=" + cols + ",hgap=" + hgap + ",vgap=" + vgap + ']';
    }





    /**
     * {@inheritDoc}
     */
    public Dimension fitInto(CompoundIcon icon, int width, int height) {
        return IconUtilities.getDefault(icon.getChildScalePolicy()).fitInto(getSize(icon), new Dimension(width, height), null);
    }
}
