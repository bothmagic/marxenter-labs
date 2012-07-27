/*
 * Created on 03.04.2007
 *
 */
package org.jdesktop.swingx.painter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.Action;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.painter.effects.AbstractAreaEffect;
import org.jdesktop.swingx.painter.effects.GlowPathEffect;
import org.jdesktop.test.PropertyChangeReport;

/**
 * Test PainterX (alternative cache handling, notification).
 * 
 */
public class PainterXIssues extends InteractiveTestCase {
    public static void main(String args[]) {
//      setSystemLF(true);
      PainterXIssues test = new PainterXIssues();
      try {
        test.runInteractiveTests();
//         test.runInteractiveTests(".*Label.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }
//----------------- visual checks
    
    /**
     * use property change listener to repaint if
     * painter related properties change.
     * 
     */
    public void interactiveEffectChangeListener() {
        TextPainterX painter = new TextPainterX("Some text to show"){
            @Override
            protected boolean shouldUseCache() {
                return isCacheable();
            }

        }; 
        final AbstractAreaEffect effect = new GlowPathEffect();
        painter.setAreaEffects(effect);
        final JXPanel panel = new JXPanel();
        panel.setBackgroundPainter(painter);
        Action action = new AbstractActionExt("increase") {

            public void actionPerformed(ActionEvent e) {
                effect.setEffectWidth(effect.getEffectWidth() + 5);
            }
            
        };
        JXFrame frame = wrapInFrame(panel, "effect change");
        painter.addPropertyChangeListener(new PropertyChangeListener(){

            public void propertyChange(PropertyChangeEvent evt) {
                panel.repaint();
            }
            
        });
        addAction(frame, action);
        frame.setSize(200, 200);
        frame.setVisible(true);
    }
    
    
//----------------- unit test methods    
    /**
     * AbstractAreaPainter must react to changes of contained
     * AreaEffects.
     *
     */
    public void testAreaEffectChangeNotification() {
        AbstractAreaPainterX painter = new AreaPainterX();
        AbstractAreaEffect effect = new GlowPathEffect();
        painter.setAreaEffects(effect);
        BufferedImage img = new BufferedImage(10, 10,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        painter.paint(g, null, 10, 10);
        // sanity
        assertTrue("cacheable is true by default", painter.isCacheable());
        assertNotNull("painter must be cached after paint", painter.getCachedImage());
        PropertyChangeReport report = new PropertyChangeReport();
        painter.addPropertyChangeListener(report);
        effect.setEffectWidth(effect.getEffectWidth() + 2);
        assertEquals("cache must be cleared after effect change", null, painter.getCachedImage());
        assertEquals("property change on effect", 1, report.getEventCount());
    }
    
    /**
     * Issue #??-swingx: clearCache has no detectable effect.
     * 
     * @throws IOException
     * 
     */
    public void testClearCacheDetectable() {
        AbstractPainterX painter = new PainterX();
        BufferedImage img = new BufferedImage(10, 10,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        painter.paint(g, null, 10, 10);
        // sanity
        assertTrue("cacheable is true by default", painter.isCacheable());
        assertFalse("has a cached image", painter.isCacheCleared());
        PropertyChangeReport report = new PropertyChangeReport();
        painter.addPropertyChangeListener(report);
        painter.clearCache();
        assertTrue("painter must have fired change event", report.hasEvents());
    }

    /**
     * Issue #??-swingx: must fire property change if contained painter
     *    changed.
     */
    public void testDirtyNotification() {
        AbstractPainterX painter = new PainterX();
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        assertNotNull(painter);
        // be sure we are caching
        painter.paint(g, null, 10, 10);
        BufferedImage cached = painter.getCachedImage();
        // sanity: we have a cached image
        assertNotNull("contained painter must have cached image", cached);
        CompoundPainterX compound = new CompoundPainterX(painter);
        assertEquals("adding painter to compound must not change cache", cached, painter.getCachedImage());
        compound.paint(g, null, 10, 10);
        // sanity: no change of state, still have the same cached
        assertEquals("adding painter to compound must not change cache", cached, painter.getCachedImage());
        PropertyChangeReport report = new PropertyChangeReport();
        compound.addPropertyChangeListener(report);
        painter.setAntialiasing(!painter.isAntialiasing());
        assertEquals("compound painter must fire exactly one property change", 1, report.getEventCount());
        assertEquals("compound painter must fire cacheCleared property", 1, report.getEventCount("cacheCleared"));
    }

    public static class PainterX extends AbstractPainterX {

        @Override
        protected void doPaint(Graphics2D g, Object object, int width, int height) {
            // TODO Auto-generated method stub
            
        }

        @Override
        protected boolean shouldUseCache() {
            return isCacheable();
        }
        
    }
    
    public static class AreaPainterX extends AbstractAreaPainterX {

        @Override
        protected Shape provideShape(Graphics2D g, Object comp, int width, int height) {
            return new Rectangle(0, 0, width, height);
        }

        @Override
        protected void doPaint(Graphics2D g, Object object, int width, int height) {
            
        }
        @Override
        protected boolean shouldUseCache() {
            return isCacheable();
        }
        
    }
}
