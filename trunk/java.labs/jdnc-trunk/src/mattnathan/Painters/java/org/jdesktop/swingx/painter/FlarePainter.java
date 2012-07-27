package org.jdesktop.swingx.painter;

import java.awt.Graphics2D;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.RadialGradientPaint;
import java.awt.Color;
import java.awt.Transparency;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.util.Random;

/**
 * A painter that paints a flare. This is the default painter used in the {@link LensFlarePainter}.
 *
 * @author <a href="mailto:mattnathan@dev.java.net>Matt Nathan</a>
 * @see LensFlarePainter
 */
public class FlarePainter<T> extends AbstractPainter<T> {

    protected float glowAlpha = 1;
    protected float ringAlpha = 0.4f;
    protected float streaksAlpha = 0.5f;
    protected float raysAlpha = 0.12f;

    protected float glowScale = 0.6f;
    protected float ringScale = 0.75f;
    protected float streaksScale = 1f;
    protected float raysScale = 1f;





    /**
     * Create a new FlarePainter.
     */
    public FlarePainter() {
        super(true);
    }





    @Override
    protected void doPaint(Graphics2D g, T t, int w, int h) {
        Composite old = g.getComposite();
        if (glowAlpha > 0) {
            if (glowAlpha == 1) {
                g.setComposite(AlphaComposite.SrcOver);
            } else {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glowAlpha));
            }
            int tx = (int) ((w - w * glowScale) / 2);
            int ty = (int) ((h - h * glowScale) / 2);
            g.translate(tx, ty);
            paintGlow(g, t, (int) (w * glowScale), (int) (h * glowScale));
            g.translate(-tx, -ty);
        }

        if (ringAlpha > 0) {
            if (ringAlpha == 1) {
                g.setComposite(AlphaComposite.SrcOver);
            } else {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ringAlpha));
            }
            int tx = (int) ((w - w * ringScale) / 2);
            int ty = (int) ((h - h * ringScale) / 2);
            g.translate(tx, ty);
            paintRing(g, t, (int) (w * ringScale), (int) (h * ringScale));
            g.translate(-tx, -ty);
        }

        if (streaksAlpha > 0) {
            if (streaksAlpha == 1) {
                g.setComposite(AlphaComposite.SrcOver);
            } else {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, streaksAlpha));
            }
            int tx = (int) ((w - w * streaksScale) / 2);
            int ty = (int) ((h - h * streaksScale) / 2);
            g.translate(tx, ty);
            paintStreaks(g, t, (int) (w * streaksScale), (int) (h * streaksScale));
            g.translate(-tx, -ty);
        }

        if (raysAlpha > 0) {
            if (raysAlpha == 1) {
                g.setComposite(AlphaComposite.SrcOver);
            } else {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, raysAlpha));
            }
            int tx = (int) ((w - w * raysScale) / 2);
            int ty = (int) ((h - h * raysScale) / 2);
            g.translate(tx, ty);
            paintRays(g, t, (int) (w * raysScale), (int) (h * raysScale));
            g.translate(-tx, -ty);
        }
        g.setComposite(old);
    }





    /**
     * Paints the central glow for the flare.
     *
     * @param g The graphics to paint to.
     * @param t The source object.
     * @param w The width.
     * @param h The height.
     */
    protected void paintGlow(Graphics2D g, T t, int w, int h) {
        if (w > 1 && h > 1) {
            RadialGradientPaint paint = new RadialGradientPaint(w >> 1, h >> 1, w >> 1,
                    new float[]{
                            0.2f, 0.6f, 1
                    },
                    new Color[]{
                            Color.WHITE, new Color(0.8f, 0.8f, 1, 0.3f), new Color(0.6f, 0.6f, 1, 0f)
                    }, RadialGradientPaint.CycleMethod.NO_CYCLE);
            g.setPaint(paint);
            g.fillOval(0, 0, w, h);
        }
    }





    /**
     * Paints the ring around the central glow.
     *
     * @param g The graphics to paint to.
     * @param t The source object.
     * @param w The width.
     * @param h The height.
     */
    protected void paintRing(Graphics2D g, T t, int w, int h) {
        if (w > 1 && h > 1) {
            RadialGradientPaint paint = new RadialGradientPaint(w / 2, h / 2, w / 2,
                    new float[]{0.8f, 0.9f, 0.95f, 1f},
                    new Color[]{
                            new Color(1f, 0.5f, 0.5f, 0),
                            new Color(1f, 0.5f, 0.8f, 0.8f),
                            new Color(0.7f, 0.8f, 0.3f, 1),
                            new Color(0.5f, 0.3f, 1f, 0f)
                    },
                    RadialGradientPaint.CycleMethod.NO_CYCLE);
            g.setPaint(paint);
            g.fill(new Ellipse2D.Float(0, 0, w, h));
        }
    }





    /**
     * Paints the streaks for the flare. The are generally strong lines radiating from the center.
     *
     * @param g The graphics to paint to.
     * @param t The source object.
     * @param w The width.
     * @param h The height.
     */
    protected void paintStreaks(Graphics2D g, T t, int w, int h) {
        final float size = 0.015f;
        final int count = 3;
        final float angle = (float) (2 * Math.PI / (count * 2)); // count * 2 as each line has two ends
        float curAngle = 0;
        final Ellipse2D ellipse = new Ellipse2D.Float((w - w * size) / 2f, 0, w * size, h);
        final Area a = new Area(ellipse);
        final AffineTransform trans = AffineTransform.getRotateInstance(angle, w / 2, h / 2);
        g.setColor(Color.WHITE);
        for (int i = 0; i < count; i++, curAngle += angle) {
            g.fill(a);
            a.transform(trans);
        }
    }





    /**
     * Paint the rays for the flare. Rays are generally weak lines of random length radiating from the center.
     *
     * @param g The graphics to paint to.
     * @param t The source object.
     * @param w The width.
     * @param h The height.
     */
    protected void paintRays(Graphics2D g, T t, int w, int h) {
        final float minLength = 0.2f * w / 2;
        final float maxLength = 0.95f * h / 2;
        final int lines = 400;
        final float increment = (float) (2 * Math.PI / lines);
        final float cx = w / 2f;
        final float cy = h / 2f;
        final Random rand = new Random(0);
        final Point2D point = new Point2D.Float(0, 0);
        float angle = 0;
        final Line2D line = new Line2D.Float(cx, cy, 0, 0);

        /**
         * To reduce the total number of lines painted we divide the area
         * into segments and paint only the lines that fit in that segment
         * onto an image. we then replicate and transform that image to
         * produce the correct number of lines.
         *
         * NOTE: if we decide to use the scale factor of the graphics we will
         * take this into account so that after the scale the buffers used will
         * not pixellate.
         */

        // split into quarters as this is easiest to transform
        AffineTransform trans = g.getTransform();
        BufferedImage image = g.getDeviceConfiguration().createCompatibleImage(
                (int) ((w / 2) * trans.getScaleX()),
                (int) ((h / 2) * trans.getScaleY()),
                Transparency.TRANSLUCENT);
        Graphics2D imgg = image.createGraphics();
        imgg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        imgg.setColor(Color.WHITE);
        for (int i = 0, n = lines / 4; i <= n; i++) {
            getPoint(angle, 0, image.getHeight(), (int) (minLength + (rand.nextFloat() * (maxLength - minLength)) * trans.getScaleX()), point);
            line.setLine(0, h / 2 - 1, point.getX(), point.getY());
            imgg.draw(line);
            angle += increment;
        }
        imgg.dispose();

        g.drawImage(image, w / 2, 0, w / 2, h / 2, null);
        g.drawImage(image, w / 2, 0, -w / 2, h / 2, null);
        g.drawImage(image, w / 2, h , w / 2, -h / 2, null);
        g.drawImage(image, w / 2, h, -w / 2, -h / 2, null);

        image.flush();
    }





    /**
     * Used to extrude a point from a central coordinate, an angle and a length.
     *
     * @param angle  The angle to extrude from.
     * @param cx     The center x point.
     * @param cy     The center y point.
     * @param length The distance to extrude.
     * @param point  The point to place the results.
     */
    private static void getPoint(float angle, float cx, float cy, float length, Point2D point) {
        double endx;
        double endy;

        if (angle >= 0 && angle < Math.PI / 2f) {
            endx = cx + (length * Math.sin(angle));
            endy = cy - (length * Math.cos(angle));

        } else if (angle >= Math.PI / 2f && angle < Math.PI) {
            angle -= Math.PI / 2f;
            endx = cx + (length * Math.cos(angle));
            endy = cy + (length * Math.sin(angle));

        } else if (angle >= Math.PI && angle < Math.PI * 1.5f) {
            angle -= Math.PI;
            endx = cx - (length * Math.sin(angle));
            endy = cy + (length * Math.cos(angle));
        } else {
            angle -= Math.PI * 1.5f;
            endx = cx - (length * Math.cos(angle));
            endy = cy - (length * Math.sin(angle));
        }

        point.setLocation(endx, endy);
    }

}
