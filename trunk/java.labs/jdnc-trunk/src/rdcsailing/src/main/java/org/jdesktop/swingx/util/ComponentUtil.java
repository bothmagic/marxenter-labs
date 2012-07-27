/**
 * Copyright 2011 Cuprak Enterprise LLC.
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
package org.jdesktop.swingx.util;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;

/**
 * Utility methods.
 * @author Ryan Cuprak
 */
public class ComponentUtil {

    /**
     * Returns the parent which can be a frame or a dialog
     * @param component - component
     * @return frame or dialog
     */
    public static Object getParentFrameDialog(Component component) {
        if(component instanceof Frame || component instanceof Dialog) {
            return component;
        } else {
            return getParentFrameDialog(component.getParent());
        }
    }
}
