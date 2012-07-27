/*
 * Created on 29.02.2008
 *
 */
package netbeans.xoutline.rendererdemo;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.PainterHighlighter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.HorizontalAlignment;
import org.jdesktop.swingx.renderer.PainterAware;

/**
 * A Highlighter which applies a value-proportional gradient to PainterAware
 * rendering components if the value is a Number. The gradient is a simple
 * yellow to white-transparent paint. The yellow can be toggled to
 * half-transparent.<p>
 * 
 * 
 * 
 */
public class ValueBasedGradientHighlighter extends PainterHighlighter {
    float maxValue = 100;

    private boolean yellowTransparent;

    public ValueBasedGradientHighlighter() {
        this(false);
    }
    
    /**
     * @param transparent
     */
    public ValueBasedGradientHighlighter(boolean transparent) {
        this(100, transparent);
    }

    public ValueBasedGradientHighlighter(float max, boolean transparent) {
        super(new RelativePainter<JComponent>());
        setYellowTransparent(transparent);
        setMaxValue(max);
    }
    
    public void setMaxValue(float max) {
        if (this.maxValue == max) return;
        this.maxValue = max;
        fireStateChanged();
    }
    
    /**
     * Overridden to do nothing if it's not a RelativePainter. We 
     * roll our own.
     */
    @Override
    public void setPainter(Painter painter) {
        if (!(painter instanceof RelativePainter)) return; 
        super.setPainter(painter);
    }

    
    @Override
    public RelativePainter getPainter() {
        return (RelativePainter) super.getPainter();
    }

    @Override
    protected Component doHighlight(Component renderer, ComponentAdapter adapter) {
        float end = getEndOfGradient((Number) adapter.getValue());
        if (end > 1) {
            renderer.setBackground(Color.RED.darker());
            renderer.setForeground(Color.WHITE);
        } else if (end > 0.02) {
            Painter painter = getPainter(end);
            ((PainterAware) renderer).setPainter(painter);
        }
        return renderer;
    }

    @Override
    protected boolean canHighlight(Component component,
            ComponentAdapter adapter) {
        return (adapter.getValue() instanceof Number) && 
            super.canHighlight(component, adapter);
    }


    /**
     * @param yellowTransparent
     */
    public void setYellowTransparent(boolean yellowTransparent) {
        if (this.yellowTransparent == yellowTransparent) return;
        this.yellowTransparent = yellowTransparent;
        getPainter().setPainter(null);
        fireStateChanged();
    }


    private Painter getPainter(float end) {
        if (getPainter().getPainter() == null) {
            Color startColor = getTransparentColor(Color.RED,
                    yellowTransparent ? 125 : 254);
            Color endColor = getTransparentColor(Color.RED.brighter(), 0);
            GradientPaint paint = new GradientPaint(new Point2D.Double(0, 0),
                    endColor, new Point2D.Double(1000, 0), startColor);
            // LinearGradientPaint paint = new LinearGradientPaint(0.0f, 0.0f,
            // 1f, 0f,
            // new float[] {0,end}, new Color[] {startColor
            // , endColor});
            MattePainter painter = new MattePainter(paint);
            painter.setPaintStretched(true);
            getPainter().setPainter(painter);
            getPainter().setHorizontalAlignment(HorizontalAlignment.RIGHT);
        } 
        getPainter().setXFactor(end);
//        getPainter().setYFactor(1.0);
        return getPainter();
    }

    private Color getTransparentColor(Color base, int transparency) {
        return new Color(base.getRed(), base.getGreen(), base.getBlue(),
                transparency);
    }

    private float getEndOfGradient(Number number) {
        float end = number.floatValue() / maxValue;
        return end;
    }

    
}
