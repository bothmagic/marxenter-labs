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

import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.jdesktop.swingx.JXAutoCompleteAdapter;

/**
 * Auto-complete demo
 * @author Ryan Cuprak
 */
public class AutoCompleteDemo extends JFrame {

    /**
     * Auto-complete demo
     */
    public AutoCompleteDemo() {
        super("AutoComplete");
        JTextArea textArea = new JTextArea(20,80);
        JScrollPane scrollPane = new JScrollPane(textArea);

        Set<String> options = new HashSet<String>();
        options.add("System");
        options.add("Sync");
        options.add("Sym");
        options.add("Symbol");
        options.add("SymbolHash");

        new JXAutoCompleteAdapter(textArea,options);
        getContentPane().add(scrollPane);


        pack();
        setVisible(true);

    }

    public static void main(String args[]) {
        new AutoCompleteDemo();
    }
}
