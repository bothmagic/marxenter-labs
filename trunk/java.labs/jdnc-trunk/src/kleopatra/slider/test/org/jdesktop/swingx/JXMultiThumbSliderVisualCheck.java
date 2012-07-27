/*
 * Created on 21.03.2011
 *
 */
package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import org.jdesktop.swingx.multislider.ThumbRenderer;
import org.jdesktop.swingx.multislider.TrackRenderer;

public class JXMultiThumbSliderVisualCheck extends InteractiveTestCase {

    public static void main(String[] args) {
        JXMultiThumbSliderVisualCheck test = new JXMultiThumbSliderVisualCheck();
        setLAF("Windows");
        try {
//            test.runInteractiveTests();
            test.runInteractive("XSlider");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void interactiveXSlider() {
        final JXSlider slider = new JXSlider();
        LOG.info(""+ slider.getUI());
        slider.setRangeEnabled(true);
        configureSlider(slider);

//        slider.putClientProperty("Slider.paintThumbArrowShape", Boolean.TRUE);
        
        final JSlider coreSlider = new JSlider();
//        coreSlider.setUI(new BasicSliderUI(coreSlider));
        configureSlider(coreSlider);
        ChangeListener l = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
//                LOG.info("extent: " + ((JSlider) e.getSource()).getExtent());

            }
        };
        coreSlider.addChangeListener(l);
        slider.addChangeListener(l);
        JComponent box = Box.createHorizontalBox();
        box.add(slider);
        box.add(coreSlider);
        JXFrame frame = wrapInFrame(box, "x-slider");
        show(frame);

    }

    private void configureSlider(final JSlider slider) {
        slider.setExtent(10);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(20);
    }
    
    public void interactiveMultiSlider() {
        JXMultiThumbSlider<Color> slider = new JXMultiThumbSlider<Color>();
        LOG.info("has track? " + ((JComponent) slider.getTrackRenderer()).isOpaque());
        slider.setThumbRenderer(new BasicThumbRenderer());
        slider.setTrackRenderer(new BasicTrackRenderer());
//        slider.setTrackRenderer(new GradientTrackRenderer());
        slider.getModel().addThumb(0.2f, Color.BLUE);
        slider.getModel().addThumb(0.8f, Color.MAGENTA);
        slider.setBorder(BorderFactory.createLineBorder(Color.RED));
        final JSlider coreSlider = new JSlider();
        coreSlider.setExtent(10);
        ChangeListener l = new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                LOG.info("extent: " + coreSlider.getExtent());
                
            }};
        coreSlider.addChangeListener(l);    
        JComponent box = Box.createHorizontalBox();
        box.add(slider);
        box.add(coreSlider);
        JXFrame frame = wrapInFrame(box, "multi-slider");
        show(frame);
    }
    
    
    public void interactiveRange() {
        JSlider s;
        JRangeSlider slider = new JRangeSlider(0, 100, 20, 80, JSlider.VERTICAL);
        JComponent box = Box.createHorizontalBox();
        box.add(slider);
        JXFrame frame = wrapInFrame(box, "range-slider");
        show(frame);
    }
    
    public void interactiveGradient() {
        JXGradientChooser chooser = new JXGradientChooser();
        JComponent box = Box.createHorizontalBox();
        box.add(chooser);
        JXFrame frame = wrapInFrame(box, "gradient chooser");
        show(frame);
        
    }

    private class BasicThumbRenderer extends JComponent implements ThumbRenderer {
        private boolean selected;

        public BasicThumbRenderer() {
//            setPreferredSize(new Dimension(14,14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(selected? Color.MAGENTA : Color.GREEN);
            Polygon poly = new Polygon();
            JComponent thumb = this;
            poly.addPoint(thumb.getWidth()/2,0);
            poly.addPoint(0,thumb.getHeight()/2);
            poly.addPoint(thumb.getWidth()/2,thumb.getHeight());
            poly.addPoint(thumb.getWidth(),thumb.getHeight()/2);
            g.fillPolygon(poly);
        }

        @Override
        public JComponent getThumbRendererComponent(JXMultiThumbSlider slider, int index, boolean selected) {
            this.selected = selected;
            return this;
        }
    }


    private class BasicTrackRenderer extends JComponent implements TrackRenderer {
        private JXMultiThumbSlider<?> slider;
        
        BasicTrackRenderer() {
            setOpaque(true);
        }
        
        
        /** 
         * @inherited <p>
         */
        @Override
        public void paint(Graphics g) {
            // TODO Auto-generated method stub
            super.paint(g);
//            paintComponent(g);
        }


        @Override
        public void paintComponent(Graphics g) {
            g.setColor(slider.getBackground());
            g.setColor(Color.YELLOW);
            LOG.info("painting?");
            g.fillRect(0, 0, slider.getWidth(), slider.getHeight());
            g.setColor(Color.GREEN);
            g.drawLine(0,slider.getHeight()/2,slider.getWidth(),slider.getHeight()/2);
            g.drawLine(0,slider.getHeight()/2+1,slider.getWidth(),slider.getHeight()/2+1);
        }

        @Override
        public JComponent getRendererComponent(JXMultiThumbSlider slider) {
            this.slider = slider;
            return this;
        }
    }

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(JXMultiThumbSliderVisualCheck.class.getName());
}
