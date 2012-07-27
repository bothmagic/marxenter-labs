/**
 * Copyright 2010 Cuprak Enterprise LLC.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jdesktop.swingx;

import java.awt.Image;
import java.beans.BeanDescriptor;

/**
 * BeanInfo object for the JXSwitch component.
 * @author Ryan Cuprak
 */
public class JXSwitchBeanInfo extends BeanInfoSupport {

    /**
     * Instantiates the JXSwitchBeanInfo class
     */
    public JXSwitchBeanInfo() {
        super(JXSwitch.class);
    }

    /**
     * Initializes the BeanInfo class
     */
    @Override
    protected void initialize() {
        BeanDescriptor bd = getBeanDescriptor();
        bd.setDisplayName("Switch");
        bd.setShortDescription("Switch component for Java.");

    }

    /**
     * Retrieves the icon for the switch component.
     * @param iconKind
     * @return Image
     */
    @Override
    public Image getIcon(int iconKind) {
        switch(iconKind) {
            case ICON_COLOR_16x16: {
                return loadImage("/org/jdesktop/swingx/Switch16c.png");
            }
            case ICON_COLOR_32x32: {
                return loadImage("/org/jdesktop/swingx/Switch32c.png");
            }
            case ICON_MONO_16x16: {
                return loadImage("/org/jdesktop/swingx/Switch16m.png");
            }
            case ICON_MONO_32x32: {
                return loadImage("/org/jdesktop/swingx/Switch32m.png");
            }
        }
        return null;
    }
}
