/*
 * $Id: CheckerboardPanel.java 902 2006-10-15 15:25:45Z gfx $
 *
 * Dual-licensed under LGPL (Sun and Romain Guy) and BSD (Romain Guy).
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * Copyright (c) 2006 Romain Guy <romain.guy@mac.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * <p><code>CheckerboardPanel</code> is a generic lightweight container displaying
 * a checkboard background. The intent of this panel is to show non-rectangular
 * components and transparent pictures. Its background allows to easily spot the
 * actual shape of the observed object.</p>
 * <p>The background checkboard is painted with a pattern of four squares, known
 * as tiles. You can control their size with {@link #setTileSize}. The pattern is
 * made of two colors:
 * <pre>
 * X O
 * O X
 * </pre>
 * Each <code>X</code> represents a square drawn with the first tile color, which
 * you can set with {@link #setTileColor1}. Each <code>O</code> represents a square
 * drawn with the first tile color, which you can set with {@link #setTileColor2}.</p>
 * <p>By default, the checkboard panel reproduces the pattern seen in many photo
 * editing applications like <i>Adobe Photoshop</i> or <i>The GIMP</i>.</p>
 * 
 * @author Romain Guy <romain.guy@mac.com>
 */

public class CheckerboardPanel extends JPanel {
    // width and height of checkboard's tiles
    private int tileSize = 7;
    // the checkboard is painted with this pattern:
    // x o
    // o x
    // where 'x' and 'o' are squares of size tileSize
    // and of colors tileColor1 and tileColor2
    private Color tileColor1 = new Color(204, 204, 204);
    private Color tileColor2 = Color.WHITE;
    
    // holds the checkboard's pattern
    private BufferedImage buffer = null;

    /**
     * <p>Creates a new checkboard panel with a flow layout. The pattern has
     * the following default properties:
     * <ul>
     *   <li><i>tileSize</i>: 7 pixels</li>
     *   <li><i>tileColor1</i>: RGB(204, 204, 204)</li>
     *   <li><i>tileColor2</i>: White</li>
     * </ul>
     * This pattern matches patterns ususally used in photo editing applications. 
     * </p>
     */
    public CheckerboardPanel() {
        this(new FlowLayout());
    }
    
    /**
     * <p>Creates a new checkboard panel with a flow layout and the specified
     * tile size for the checkboard pattern. The checkboard pattern has the
     * following default properties:
     * <ul>
     *   <li><i>tileColor1</i>: RGB(204, 204, 204)</li>
     *   <li><i>tileColor2</i>: White</li>
     * </ul>
     * This pattern matches patterns ususally used in photo editing applications.</p>
     * <p>If the specified tile size is lower than 1, it will become 1.</p>
     * 
     * @param tileSize the size, in pixels, of the square tiles
     */
    public CheckerboardPanel(final int tileSize) {
        this(tileSize, new FlowLayout());
    }

    /**
     * <p>Creates a new checkboard panel with the specified layout manager.
     * The checkboard pattern has the following default properties:
     * <ul>
     *   <li><i>tileSize</i>: 7 pixels</li>
     *   <li><i>tileColor1</i>: RGB(204, 204, 204)</li>
     *   <li><i>tileColor2</i>: White</li>
     * </ul>
     * This pattern matches patterns ususally used in photo editing applications. 
     * </p>
     * 
     * @param layout the layout manager to use
     */
    public CheckerboardPanel(final LayoutManager layout) {
        this(7, layout);
    }
    
    /**
     * <p>Creates a new checkboard panel with the specified layout manager and
     * the specified tile size for the checkboard pattern. The checkboard pattern 
     * has the following default properties:
     * <ul>
     *   <li><i>tileColor1</i>: RGB(204, 204, 204)</li>
     *   <li><i>tileColor2</i>: White</li>
     * </ul>
     * This pattern matches patterns ususally used in photo editing applications. 
     * </p>
     * 
     * @param tileSize the size, in pixels, of the square tiles
     * @param layout the layout manager to use
     */
    public CheckerboardPanel(final int tileSize, final LayoutManager layout) {
        super(layout);

        setTileSize(tileSize);
    }
    
    /**
     * <p>Gets the size, in pixels, of the tiles in the checkboard pattern.</p>
     * 
     * @return the checkboard's tiles size
     */
    public int getTileSize() {
        return tileSize;
    }
    
    /**
     * <p>Sets the size, in pixels, of the tiles in the checkboard pattern.</p>
     * <p>If the provided size is < 1, it will be made equal to 1. Changing the
     * tile size repaints the component if the new size is different from the
     * previous size.</p>
     * 
     * @param tileSize the new checkboard's tiles size
     */
    public void setTileSize(int tileSize) {
        if (tileSize <= 1) {
            tileSize = 1;
        }
        
        if (this.tileSize != tileSize) {
            this.tileSize = tileSize;
            buffer = null;
            repaint();
        }
    }

    /**
     * <p>Gets the first color of the checkboard pattern.</p>
     * 
     * @return the first color of the checkboard pattern
     */
    public Color getTileColor1() {
        return tileColor1;
    }
    
    /**
     * <p>Sets the first color of the checkboard pattern.</p>
     * <p>If the provided color is null or equals the current color,
     * nothing happens. Otherwise, the color is changed and the component is
     * repainted.</p>
     * 
     * @param tileColor the first checkboard pattern color
     * @see #setTileColor2(Color)
     */
    public void setTileColor1(final Color tileColor) {
        if (tileColor != null && !tileColor.equals(tileColor1)) {
            this.tileColor1 = tileColor;
            buffer = null;
            repaint();
        }
    }
    
    /**
     * <p>Gets the second color of the checkboard pattern.</p>
     * 
     * @return the second color of the checkboard pattern
     */
    public Color getTileColor2() {
        return tileColor2;
    }
    
    /**
     * <p>Sets the second color of the checkboard pattern.</p>
     * <p>If the provided color is null or equals the current color,
     * nothing happens. Otherwise, the color is changed and the component is
     * repainted.</p>
     * 
     * @param tileColor the second checkboard pattern color
     * @see #setTileColor1(Color)
     */
    public void setTileColor2(final Color tileColor) {
        if (tileColor != null && !tileColor.equals(tileColor2)) {
            this.tileColor2 = tileColor;
            buffer = null;
            repaint();
        }
    }

    // generates the checkerboard pattern in a buffered image
    // this image is used by paintComponent() to fill the area allocated
    // to the panel
    private void createCheckerboard() {
        int width = tileSize * 2;
        int height = tileSize * 2;
        buffer = new BufferedImage(width,
                                   height,
                                   BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(tileColor1);
        g.fillRect(0, 0, tileSize, tileSize);
        g.fillRect(tileSize, tileSize, tileSize, tileSize);
        g.setColor(tileColor2);
        g.fillRect(tileSize, 0, tileSize, tileSize);
        g.fillRect(0, tileSize, tileSize, tileSize);
        g.dispose();
    }

    /**
     * Paints this component and its checkboard pattern.
     * 
     * @param g the graphics surface where to paint the checkboard pattern
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (buffer == null) {
            createCheckerboard();
        }
        
        Rectangle bounds = g.getClipBounds();
        
        int boundsStart = bounds.x;
        int boundsEnd = boundsStart + bounds.width;
        int pictureWidth = buffer.getWidth();
        int startX = boundsStart - (boundsStart % pictureWidth);
        int endX = boundsEnd + (boundsEnd % pictureWidth);

        boundsStart = bounds.y;
        boundsEnd = boundsStart + bounds.height;
        int pictureHeight = buffer.getHeight();
        int startY = boundsStart - (boundsStart % pictureHeight);
        int endY = boundsEnd + (boundsEnd % pictureHeight);

        for (int x = startX; x < endX; x += pictureWidth) {
            for (int y = startY; y < endY; y += pictureHeight) {
                g.drawImage(buffer, x, y, null);
            }
        }
    }
}
