package org.jdesktop.swingx.effect;

import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.image.ImageUtilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RippleGraphicsEffect<T> extends AbstractGraphicsEffect<T> {

    private float phase = 0f;

    private double waveHeight = 12;

    private double falloff = 14;

    protected BufferedImage filter(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        final float angle = (float) (Math.PI * 2 * (phase));


        double maxHeight = 100;
        double minHeight = 100;
        double[] offsets = new double[height];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = (((i + waveHeight) * Math.sin(((height * height) / (falloff * (i + 1d))) + angle))) / falloff;
            if (height + offsets[i] < i) {
                if (minHeight > i) {
                    minHeight = i;
                }
            } else if (i + offsets[i] > maxHeight) {
                maxHeight = i + offsets[i];
            }
        }

        assert maxHeight == 100 || minHeight == 100;
        int targetHeight = (int) (maxHeight == 100 ? minHeight : maxHeight);

        BufferedImage filtered = GraphicsUtilities.createCompatibleTranslucentImage(width, targetHeight);
        Graphics2D g = filtered.createGraphics();

        try {

            /*
           Go over each row in the image displacing it so that ripple effect is created
            */
            for (int i = 0; i < filtered.getHeight(); i++) {
                double displayY;
                if (offsets.length > i) {
                    displayY = offsets[i];
                } else {
                    displayY = (((i + waveHeight) * Math.sin(((height * height) / (falloff * (i + 1d))) + angle))) / falloff;
                }

                g.setClip(0, i, width, 1);
                AffineTransform t = AffineTransform.getTranslateInstance(displayY / 2, displayY);
                g.drawImage(image, t, null);
            }

        } finally {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, width, height);
            g.dispose();
        }

        return filtered;
    }

    public double getFalloff() {
        return falloff;
    }

    public void setFalloff(double falloff) {
        this.falloff = falloff;
    }

    public float getPhase() {
        return phase;
    }

    public void setPhase(float phase) {
        this.phase = phase;
    }

    public double getWaveHeight() {
        return waveHeight;
    }

    public void setWaveHeight(double waveHeight) {
        this.waveHeight = waveHeight;
    }


    public void paintEffect(Graphics g, EffectSource<? extends T> effectSource) {
        // temp
        BufferedImage buffer = EffectUtilities.bufferEffect(effectSource, g, true);
        BufferedImage result = filter(buffer);
        Rectangle r = g.getClipBounds();
        g.drawImage(result, r.x, r.y, null);
        ImageUtilities.releaseImage(result);
        ImageUtilities.releaseImage(buffer);
    }

    public boolean transform(Rectangle area, EffectSource<? extends T> effectSource) {
        Rectangle sourceBounds = effectSource.getSourceBounds(null);
        area.setBounds(0, 0, sourceBounds.width, sourceBounds.height);
        return true;
    }
}