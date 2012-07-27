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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Currency;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import org.jdesktop.swingx.JXCurrencyField;

/**
 * Currency Demo
 * @author Ryan Cuprak
 */
public class CurrencyDemo extends JFrame {

    /**
     * Undo manager
     */
    private UndoManager undoManager;

    /**
     * Creates a new currency demo.
     */
    public CurrencyDemo() {
        super("Currency Demo");
        undoManager = new UndoManager();

        JMenuBar menuBar = new JMenuBar();
        JMenu edit = new JMenu("Edit");
        JMenuItem item = new JMenuItem("Undo");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoManager.undo();
            }
        });
        menuBar.add(edit);
        edit.add(item);

        setJMenuBar(menuBar);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        panel.add(createPanel(),gc);
        gc.gridy = 1;
        panel.add(createPanel(),gc);
        setContentPane(panel);
        pack();
        setVisible(true);
    }

    /**
     * Creates a new panel
     * @return panel
     */
    private JPanel createPanel() {
        final JXCurrencyField currencyField = new JXCurrencyField();
        currencyField.getDocument().addUndoableEditListener(undoManager);
        Currency currencies[] = new Currency[] { Currency.getInstance(Locale.US) , Currency.getInstance(Locale.UK) };
        Locale locales[] = new Locale[] {Locale.US,Locale.UK, Locale.CHINA};
        JComboBox currencyComboBox;
        JComboBox localesComboBox;
        currencyComboBox = new JComboBox(currencies);
        currencyComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
            //    currencyField.setCurrency((Currency)e.getItem());
            }
        });
        localesComboBox = new JComboBox(locales);
        localesComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
           //     currencyField.setLocale((Locale)e.getItem());
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 1 ; gc.gridy = 0; gc.insets = new Insets(5,5,5,5);
        panel.add(new JLabel("Currency"),gc);
        gc.gridx = 2;
        panel.add(new JLabel("Locale"),gc);

        gc.gridx = 0; gc.gridy = 1; gc.insets = new Insets(5,5,5,5);
        panel.add(currencyField,gc);
        gc.gridx = 1;
        panel.add(currencyComboBox,gc);
        gc.gridx = 2;
        panel.add(localesComboBox,gc);



        return panel;
    }


    public static void main(String args[] ){
        JTextField jf = new JTextField();
        Document doc = jf.getDocument();
        System.out.println("Class: " + doc.getDefaultRootElement().getElement(0).getClass().getName());

        new CurrencyDemo();
    }
}
