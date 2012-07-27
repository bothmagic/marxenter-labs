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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.JXMusicStaff;

/**
 * MusicalStaffDemo
 * @author Ryan Cuprak
 */
public class MusicalStaffDemo extends JFrame {

    /**
     * Creates a new musical staff demo
     */
    public MusicalStaffDemo() {
        super("Music Staff Demo");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH; gc.weightx = 1; gc.weighty = 1;
        JXMusicStaff musicalStaff = new JXMusicStaff();
        System.out.println("Class: " + musicalStaff.getUI().getClass().getName());
        panel.add(new JScrollPane(musicalStaff),gc);
        setContentPane(panel);
        pack();
        setVisible(true);
    }

    /**
     * Instantiates the musical staff demo
     * @param args - arguments
     */
    public static void main(String args[]) {
        new MusicalStaffDemo();
    }
}
