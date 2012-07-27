package org.jdesktop.swingx;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.effect.BlurGraphicsEffect;
import org.jdesktop.swingx.effect.EffectTransition;
import org.jdesktop.swingx.effect.GraphicsEffect;
import org.jdesktop.swingx.effect.PanTransition;
import org.jdesktop.swingx.effect.ScaleTransformEffect;
import org.jdesktop.swingx.effect.SerialGraphicsEffect;
import org.jdesktop.swingx.effect.TranslucentGraphicsEffect;

public class JXTransitionPanelDemo {
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            final JXTransitionPanel tp = new JXTransitionPanel();
            tp.setOpaque(false);

            tp.add(new JButton(new AbstractAction("Expose East") {
               public void actionPerformed(ActionEvent e) {
                  tp.show("2", new PanTransition<JXTransitionPanel, Object>(
                        PanTransition.Direction.EAST, PanTransition.LayerPolicy.EXPOSE));
               }
            }), "1");
            tp.add(new JButton(new AbstractAction("Cover North-West") {
               public void actionPerformed(ActionEvent e) {
                  tp.show("3", new PanTransition<JXTransitionPanel, Object>(
                        PanTransition.Direction.NORTH_WEST, PanTransition.LayerPolicy.COVER));
               }
            }), "2");
            tp.add(new JButton(new AbstractAction("Move South") {
               public void actionPerformed(ActionEvent e) {
                  tp.show("4", new PanTransition<JXTransitionPanel, Object>(
                        PanTransition.Direction.SOUTH, PanTransition.LayerPolicy.MOVE));
               }
            }), "3");
            tp.add(new JButton(new AbstractAction("Move North-West") {
               public void actionPerformed(ActionEvent e) {
                  tp.show("5", new PanTransition<JXTransitionPanel, Object>(
                        PanTransition.Direction.NORTH_WEST, PanTransition.LayerPolicy.MOVE));
               }
            }), "4");
            tp.add(new JButton(new AbstractAction("Blur, Fade and scale") {
               public void actionPerformed(ActionEvent e) {
                  BlurGraphicsEffect<Object> blur = new BlurGraphicsEffect<Object>();
                  blur.setQuality(BlurGraphicsEffect.Quality.LINEAR);
                  GraphicsEffect<Object> fade = new TranslucentGraphicsEffect<Object>();
                  GraphicsEffect<Object> scale = new ScaleTransformEffect<Object>();
                  tp.show("1", new EffectTransition<JXTransitionPanel, Object>(
                        new SerialGraphicsEffect<Object>(blur, fade, scale),
                        new PropertySetter(blur, "blurRadius", 0, 20),
                        new PropertySetter(fade, "alpha", 1f, 0f),
                        new PropertySetter(scale, "scale", 1f, 0.75f)));
               }
            }), "5");
            new TestFrame("transition test", tp).setVisible(true);
         }
      });
   }
}
