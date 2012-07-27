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
package org.jdesktop.swingx.demo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXDecimalField;

/**
 * Decimal Demo
 * @author Ryan Cuprak
 */
public class DecimalDemo extends JFrame implements FocusListener {

    /**
     * First field
     */
    private JXDecimalField decimalField1;

    /**
     * Second field
     */
    private JXDecimalField decimalField2;

    /**
     * Demos the JXDecimalField
     */
    public DecimalDemo() {
        super("Decimal Demo");
        decimalField1 = new JXDecimalField();
        decimalField2 = new JXDecimalField();
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4,4,4,4);
        panel.add(new JLabel("Decimal value 1:"),gc);
        gc.gridy = 1;
        panel.add(new JLabel("Decimal value 2:"),gc);
        gc.gridy = 0; gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;
        panel.add(decimalField1,gc);
        gc.gridy = 1;
        panel.add(decimalField2,gc);

        setContentPane(panel);
        pack();
        setVisible(true);
    }

    /**
     * Open up the test window
     * @param args - arguments
     */
    public static void main(String args[]) {
        new DecimalDemo();
    }

    @Override
    public void focusGained(FocusEvent e) {
        System.out.println("Value 1: " + decimalField1.getValue() + " Value 2: " + decimalField2.getValue());
    }

    @Override
    public void focusLost(FocusEvent e) {}
}
