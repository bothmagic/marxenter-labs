import org.jdesktop.swingx.JXSlider;
import org.jdesktop.swingx.TestFrame;
import org.jdesktop.swingx.event.SliderChangeEvent;
import org.jdesktop.swingx.event.SliderChangeListener;
import org.jdesktop.swingx.slider.*;

import javax.swing.*;
import java.awt.*;

public class JXSliderDemo2 {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }

    private static void createAndShowGUI(String[] args) {
        DefaultSliderModel sliderModel = new DefaultSliderModel(0, 5000, 5000 / 2);
        final JXSlider slider = new JXSlider(sliderModel);
        slider.setProjection(new ValueFrameProjection(1000, 150, 150, false));

        MarkerGroup repeatingMarker = new RepeatingMarkerGroup(100, 50);
        sliderModel.addMarkerGroup(repeatingMarker);

        BagMarkerGroup markers = new BagMarkerGroup();
        markers.addValue(275);
        markers.addValue(1925);
        markers.addValue(2955);
        sliderModel.addMarkerGroup(markers);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(slider, BorderLayout.CENTER);

        // bit of a pain to get at marker values (unless I'm missing a trick)
        
        slider.addSliderChangeListener(new SliderChangeListener() {
            public void sliderMarkerGroupChanged(SliderChangeEvent e) {
                SliderModel model = slider.getModel();
                MarkerGroup group = model.getMarkerGroup(e.getIndex0());
                MarkerRange range = group.getMarkers(Long.MIN_VALUE, Long.MAX_VALUE);
                if (range.getSize() > 1) {
                    System.out.printf("[0]%d, [1]%d, [2]%d\n", range.get(0), range.get(1), range.get(2));
                } else {
                    System.out.printf("[0]%d\n", range.get(0));
                }
            }

            public void sliderRangeChanged(SliderChangeEvent e) {
            }

            public void sliderMarkerGroupAdded(SliderChangeEvent e) {
            }

            public void sliderMarkerGroupRemoved(SliderChangeEvent e) {
            }
        });

        TestFrame testFrame = new TestFrame(JXSliderDemo2.class.getName(), panel, SystemColor.control);
        testFrame.setBorderSpacingVisible(true);
        testFrame.setSize(500, 100);
        testFrame.setVisible(true);
    }
}
