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
package org.jdesktop.swingx.currency;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTextFieldUI;
import javax.swing.text.Element;
import javax.swing.text.View;
import org.jdesktop.swingx.JXCurrencyField;

/**
 * UI for a currency field.
 * @author Ryan Cuprak
 */
public class CurrencyFieldUI extends MetalTextFieldUI {

    /**
     * Configures the CurrencyFieldUI
     * @param component - component
     * @return ComponentUI
     */
    public static ComponentUI createUI(JComponent component) {
        return new CurrencyFieldUI();
    }

    /**
     * Returns the CurrencyFieldView
     * @param element - element being displayed
     * @return View
     */
    @Override
    public View create(Element element) {
        return new CurrencyFieldView((JXCurrencyField)getComponent(),element);
    }

}