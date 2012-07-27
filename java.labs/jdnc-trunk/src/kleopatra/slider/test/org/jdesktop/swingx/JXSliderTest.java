/*
 * Created on 22.03.2011
 *
 */
package org.jdesktop.swingx;

import org.jdesktop.swingx.plaf.basic.BasicXSliderUI;
import org.jdesktop.test.PropertyChangeReport;
import org.jdesktop.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JXSliderTest extends InteractiveTestCase {
    
    
    private JXSlider slider;


    @Test
    public void testRangeProperty() {
        assertEquals(false, slider.isRangeEnabled());
        PropertyChangeReport report = new PropertyChangeReport(slider);
        slider.setRangeEnabled(true);
        assertEquals(true, slider.isRangeEnabled());
        TestUtils.assertPropertyChangeEvent(report, "rangeEnabled", false, true);
    }
    
    
    @Test
    public void testCustomUI() {
        assertTrue(slider.getUI() instanceof BasicXSliderUI);
    }


    /** 
     * @inherited <p>
     */
    @Override
    @Before
    public void setUp() throws Exception {
        slider = new JXSlider();
    }
    
    

}
