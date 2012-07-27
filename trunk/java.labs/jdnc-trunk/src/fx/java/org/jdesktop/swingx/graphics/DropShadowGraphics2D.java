/*
 * $Id: DropShadowGraphics2D.java 932 2006-11-09 00:45:36Z gfx $
 *
 * Dual-licensed under LGPL (Sun and Romain Guy) and BSD (Romain Guy).
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
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

package org.jdesktop.swingx.graphics;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.awt.geom.AffineTransform;
import java.awt.font.GlyphVector;
import java.awt.font.FontRenderContext;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * XXX: implement shadow for images
 * XXX: check out what setting the Paint does to the shadows
 *
 * @author Romain Guy <romain.guy@mac.com>
 */
public class DropShadowGraphics2D extends Graphics2D {
    private int size = 3;
    private float opacity = 0.5f;
    private Color color = Color.BLACK;

    private float angle = 30.0f;
    private int distance = 3;

    private int distanceOffsetX = 0;
    private int distanceOffsetY = 0;

    private Graphics2D g2;

    // XXX want more constructors
    public DropShadowGraphics2D(Graphics2D g2) {
        if (g2 == null) {
            throw new IllegalArgumentException(
                    "Parent Graphics2D cannot be null");
        }

        this.g2 = g2;

        computeDistanceOffsets();
    }

    private void computeDistanceOffsets() {
        double angleRadians = Math.toRadians(angle);
        distanceOffsetX = (int) (Math.cos(angleRadians) * distance);
        distanceOffsetY = (int) (Math.sin(angleRadians) * distance);
    }

    private void paintShadow(GraphicsRunnable runnable) {
        Composite oldComposite = g2.getComposite();
        Color oldColor = g2.getColor();
        g2.setColor(color);

        for (int i = -size; i <= size; i++) {
            for (int j = -size; j <= size; j++) {
                double distance = i * i + j * j;
                float alpha = 1.0f;
                if (distance > 0.0d) {
                    alpha = (float) (1.0f / (distance * size));
                }
                if (alpha > 1.0f) {
                    alpha = 1.0f;
                }
                alpha *= opacity;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                           alpha));
                runnable.run(g2, i + distanceOffsetX, j + distanceOffsetY);
            }
        }
        g2.setColor(oldColor);
        g2.setComposite(oldComposite);
    }

    @Override
    public void drawGlyphVector(final GlyphVector g, final float x, final float y) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.drawGlyphVector(g, x + xOffset, y + yOffset);
            }
        });
        g2.drawGlyphVector(g, x, y);
    }

    @Override
    public void draw3DRect(final int x, final int y,
                           final int width, final int height,
                           final boolean raised) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.draw3DRect(x + xOffset, y + yOffset, width, height, raised);
            }
        });
        g2.draw3DRect(x, y, width, height, raised);
    }

    @Override
    public void fill3DRect(final int x, final int y,
                           final int width, final int height,
                           final boolean raised) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.fill3DRect(x + xOffset, y + yOffset, width, height, raised);
            }
        });
        g2.fill3DRect(x, y, width, height, raised);
    }

    @Override
    public void draw(final Shape s) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.translate(xOffset, yOffset);
                g2d.draw(s);
                g2d.translate(-xOffset, -yOffset);
            }
        });
        g2.draw(s);
    }

    @Override
    public boolean drawImage(final Image img, final AffineTransform xform,
                             final ImageObserver obs) {
        return g2.drawImage(img, xform, obs);
    }

    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        g2.drawImage(img, op, x, y);
    }

    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        g2.drawRenderedImage(img, xform);
    }

    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        g2.drawRenderableImage(img, xform);
    }

    @Override
    public void drawString(final String str, final int x, final int y) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.drawString(str, x + xOffset, y + yOffset);
            }
        });
        g2.drawString(str, x, y);
    }

    @Override
    public void drawString(final String str, final float x, final float y) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.drawString(str, x + xOffset, y + yOffset);
            }
        });
        g2.drawString(str, x, y);
    }

    @Override
    public void drawString(final AttributedCharacterIterator iterator,
                           final int x, final int y) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.drawString(iterator, x + xOffset, y + yOffset);
            }
        });
        g2.drawString(iterator, x, y);
    }

    @Override
    public void drawString(final AttributedCharacterIterator iterator,
                           final float x, final float y) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.drawString(iterator, x + xOffset, y + yOffset);
            }
        });
        g2.drawString(iterator, x, y);
    }

    @Override
    public void fill(final Shape s) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.translate(xOffset, yOffset);
                g2d.fill(s);
                g2d.translate(-xOffset, -yOffset);
            }
        });
        g2.fill(s);
    }

    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return g2.hit(rect, s, onStroke);
    }

    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        return g2.getDeviceConfiguration();
    }

    @Override
    public void setComposite(Composite comp) {
        g2.setComposite(comp);
    }

    @Override
    public void setPaint(Paint paint) {
        g2.setPaint(paint);
    }

    @Override
    public void setStroke(Stroke s) {
        g2.setStroke(s);
    }

    @Override
    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        g2.setRenderingHint(hintKey, hintValue);
    }

    @Override
    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return g2.getRenderingHint(hintKey);
    }

    @Override
    public void setRenderingHints(Map<?, ?> hints) {
        g2.setRenderingHints(hints);
    }

    @Override
    public void addRenderingHints(Map<?, ?> hints) {
        g2.addRenderingHints(hints);
    }

    @Override
    public RenderingHints getRenderingHints() {
        return g2.getRenderingHints();
    }

    @Override
    public void translate(int x, int y) {
        g2.translate(x, y);
    }

    @Override
    public void translate(double tx, double ty) {
        g2.translate(tx, ty);
    }

    @Override
    public void rotate(double theta) {
        g2.rotate(theta);
    }

    @Override
    public void rotate(double theta, double x, double y) {
        g2.rotate(theta, x, y);
    }

    @Override
    public void scale(double sx, double sy) {
        g2.scale(sx, sy);
    }

    @Override
    public void shear(double shx, double shy) {
        g2.shear(shx, shy);
    }

    @Override
    public void transform(AffineTransform Tx) {
        g2.transform(Tx);
    }

    @Override
    public void setTransform(AffineTransform Tx) {
        g2.setTransform(Tx);
    }

    @Override
    public AffineTransform getTransform() {
        return g2.getTransform();
    }

    @Override
    public Paint getPaint() {
        return g2.getPaint();
    }

    @Override
    public Composite getComposite() {
        return g2.getComposite();
    }

    @Override
    public void setBackground(Color color) {
        g2.setBackground(color);
    }

    @Override
    public Color getBackground() {
        return g2.getBackground();
    }

    @Override
    public Stroke getStroke() {
        return g2.getStroke();
    }

    @Override
    public void clip(Shape s) {
        g2.clip(s);
    }

    @Override
    public FontRenderContext getFontRenderContext() {
        return g2.getFontRenderContext();
    }

    @Override
    public Graphics create() {
        return g2.create();
    }

    @Override
    public Graphics create(int x, int y, int width, int height) {
        return g2.create(x, y, width, height);
    }

    @Override
    public Color getColor() {
        return g2.getColor();
    }

    @Override
    public void setColor(Color c) {
        g2.setColor(c);
    }

    @Override
    public void setPaintMode() {
        g2.setPaintMode();
    }

    @Override
    public void setXORMode(Color c1) {
        g2.setXORMode(c1);
    }

    @Override
    public Font getFont() {
        return g2.getFont();
    }

    @Override
    public void setFont(Font font) {
        g2.setFont(font);
    }

    @Override
    public FontMetrics getFontMetrics() {
        return g2.getFontMetrics();
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        return g2.getFontMetrics(f);
    }

    @Override
    public Rectangle getClipBounds() {
        return g2.getClipBounds();
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {
        g2.clipRect(x, y, width, height);
    }

    @Override
    public void setClip(int x, int y, int width, int height) {
        g2.setClip(x, y, width, height);
    }

    @Override
    public Shape getClip() {
        return g2.getClip();
    }

    @Override
    public void setClip(Shape clip) {
        g2.setClip(clip);
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        g2.copyArea(x, y, width, height, dx, dy);
    }

    @Override
    public void drawLine(final int x1, final int y1, final int x2, final int y2) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.drawLine(x1 + xOffset, y1 + yOffset,
                             x2 + xOffset, y2 + yOffset);
            }
        });
        g2.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void fillRect(final int x, final int y,
                         final int width, final int height) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.fillRect(x + xOffset, y + yOffset, width, height);
            }
        });
        g2.fillRect(x, y, width, height);
    }

    @Override
    public void drawRect(final int x, final int y,
                         final int width, final int height) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.drawRect(x + xOffset, y + yOffset, width, height);
            }
        });
        g2.drawRect(x, y, width, height);
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        g2.clearRect(x, y, width, height);
    }

    @Override
    public void drawRoundRect(final int x, final int y,
                              final int width, final int height,
                              final int arcWidth, final int arcHeight) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.drawRoundRect(x + xOffset, y + yOffset, width, height,
                                  arcWidth, arcHeight);
            }
        });
        g2.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void fillRoundRect(final int x, final int y,
                              final int width, final int height,
                              final int arcWidth, final int arcHeight) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.fillRoundRect(x + xOffset, y + yOffset, width, height,
                                  arcWidth, arcHeight);
            }
        });
        g2.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void drawOval(final int x, final int y,
                         final int width, final int height) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.drawOval(x + xOffset, y + yOffset, width, height);
            }
        });
        g2.drawOval(x, y, width, height);
    }

    @Override
    public void fillOval(final int x, final int y,
                         final int width, final int height) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.fillOval(x + xOffset, y + yOffset, width, height);
            }
        });
        g2.fillOval(x, y, width, height);
    }

    @Override
    public void drawArc(final int x, final int y,
                        final int width, final int height,
                        final int startAngle, final int arcAngle) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.drawArc(x + xOffset, y + yOffset, width, height,
                            startAngle, arcAngle);
            }
        });
        g2.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void fillArc(final int x, final int y,
                        final int width, final int height,
                        final int startAngle, final int arcAngle) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.fillArc(x + xOffset, y + yOffset, width, height,
                            startAngle, arcAngle);
            }
        });
        g2.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void drawPolyline(final int[] xPoints,
                             final int[] yPoints,
                             final int nPoints) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.translate(xOffset, yOffset);
                g2d.drawPolyline(xPoints, yPoints, nPoints);
                g2d.translate(-xOffset, -yOffset);
            }
        });
        g2.drawPolyline(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawPolygon(final int[] xPoints,
                            final int[] yPoints,
                            final int nPoints) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.translate(xOffset, yOffset);
                g2d.drawPolygon(xPoints, yPoints, nPoints);
                g2d.translate(-xOffset, -yOffset);
            }
        });
        g2.drawPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawPolygon(final Polygon p) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.translate(xOffset, yOffset);
                g2d.drawPolygon(p);
                g2d.translate(-xOffset, -yOffset);
            }
        });
        g2.drawPolygon(p);
    }

    @Override
    public void fillPolygon(final int[] xPoints,
                            final int[] yPoints,
                            final int nPoints) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.translate(xOffset, yOffset);
                g2d.fillPolygon(xPoints, yPoints, nPoints);
                g2d.translate(-xOffset, -yOffset);
            }
        });
        g2.fillPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void fillPolygon(final Polygon p) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2d.translate(xOffset, yOffset);
                g2d.fillPolygon(p);
                g2d.translate(-xOffset, -yOffset);
            }
        });
        g2.fillPolygon(p);
    }

    @Override
    public void drawChars(final char[] data, final int offset, final int length,
                          final int x, final int y) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2.drawChars(data, offset, length, x + xOffset, y + yOffset);
            }
        });
        g2.drawChars(data, offset, length, x, y);
    }

    @Override
    public void drawBytes(final byte[] data, final int offset, final int length,
                          final int x, final int y) {
        paintShadow(new GraphicsRunnable() {
            public void run(Graphics2D g2d, int xOffset, int yOffset) {
                g2.drawBytes(data, offset, length, x + xOffset, y + yOffset);
            }
        });
        g2.drawBytes(data, offset, length, x, y);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return g2.drawImage(img, x, y, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height,
                             ImageObserver observer) {
        return g2.drawImage(img, x, y, width, height, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor,
                             ImageObserver observer) {
        return g2.drawImage(img, x, y, bgcolor, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height,
                             Color bgcolor, ImageObserver observer) {
        return g2.drawImage(img, x, y, width, height, bgcolor, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
                             int sx1, int sy1, int sx2, int sy2,
                             ImageObserver observer) {
        return g2.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
                             int sx1, int sy1, int sx2, int sy2, Color bgcolor,
                             ImageObserver observer) {
        return g2.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor,
                            observer);
    }

    @Override
    public void dispose() {
        g2.dispose();
    }

    @Override
    public String toString() {
        return g2.toString();
    }

    @Override
    public boolean hitClip(int x, int y, int width, int height) {
        return g2.hitClip(x, y, width, height);
    }

    /** @noinspection deprecation*/
    @Override
    @Deprecated
    public Rectangle getClipRect() {
        return g2.getClipRect();
    }

    @Override
    public Rectangle getClipBounds(Rectangle r) {
        return g2.getClipBounds(r);
    }

    private static interface GraphicsRunnable {
        public void run(Graphics2D g2d, int xOffset, int yOffset);
    }
}
