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
package org.jdesktop.swingx;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.TextUI;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.Position;
import javax.swing.text.TextAction;

/**
 * This component renders an auto-completion list. While this is open, as the user continues
 * to type, the list of available choices is narrowed down.
 * @author Ryan Cuprak
 */
public class JXAutoCompleteAdapter extends JScrollPane {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger("");

    /**
     * List instance being rendered
     */
    private JList list;

    /**
     * Text component to which auto-completion is being attached.
     */
    private JTextComponent textComponent;

    /**
     * True if we've been added and are being rendered
     */
    private boolean rendered;

    /**
     * Last position entered
     */
    private int lastPosition;

    /**
     * Offset from the start
     */
    private int offset;

    /**
     * Starting position of the insert
     */
    private int startingPosition;

    /**
     * Keys to options
     */
    private Set<String> dictionary = new HashSet<String>();

    /**
     * Creates a new auto-completion list
     * @param textComponent - text component
     * @param dictionary - keys to options
     */
    public JXAutoCompleteAdapter(JTextComponent textComponent, Set<String> dictionary) {
        this.dictionary = dictionary;
        this.textComponent = textComponent;
        Keymap keyMap =  textComponent.getKeymap();
        keyMap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK),new DisplayAction());
        keyMap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), new HideAction());
        textComponent.setKeymap(keyMap);
        list = new JList();
        this.setViewportView(list);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setFocusable(false);
        list.setFocusable(false);
        doLayout();
        textComponent.add(this);
        setVisible(false);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                select();
                removeAutoComplete();
            }
        });
        textComponent.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if(rendered) {
                    if(Math.abs(lastPosition-e.getDot()) > 1) {
                        removeAutoComplete();
                    } else if (e.getDot()-1 < startingPosition) {
                        removeAutoComplete();
                    } else {
                        lastPosition = e.getDot();
                        narrowSelection();
                    }
                }
            }
        });
    }

    /**
     * Narrows the selection down, this occurs while the user types.
     */
    protected void narrowSelection() {
        List<String> options = getOptions();
        list.setListData(options.toArray());
        if(options.size() == 0) {
            removeAutoComplete();
        } else if (options.size() == 1) {
            list.setSelectedIndex(0);
            select();
        }
    }

    /**
     * Registers arrow keys for navigating the text component
     */
    protected void registerAutoCompleteKeys() {
        Keymap keymap = textComponent.getKeymap();
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0),new ArrowAction(false));
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), new ArrowAction(true));
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), new SelectAction());
    }

    /**
     * De-registers the keystrokes so arrow/enter keys behave as expected
     */
    protected void deregisterAutoCompleteKeys() {
        Keymap keymap = textComponent.getKeymap();
        keymap.removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0));
        keymap.removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0));
    }

    /**
     * Increments the selection
     */
    public void incrementSelection() {
        int currentSelection = list.getSelectedIndex()+1;
        if(currentSelection <= list.getModel().getSize()) {
            list.setSelectedIndex(currentSelection);
            repaint();
        }

    }

    /**
     * Decrements the selection
     */
    public void decrementSelection() {
        int currentSelection = list.getSelectedIndex()-1;
        if(currentSelection >= 0) {
            list.setSelectedIndex(currentSelection);
        }
        repaint();
    }

    /**
     * Select hides the component
     */
    protected void select() {
        String str = list.getSelectedValue().toString();
        System.out.println("Starting position: " + startingPosition);
        update(startingPosition,textComponent.getCaret().getDot()-startingPosition,str);
    }

    /**
     * Removes the auto-complete
     */
    protected void removeAutoComplete() {
        if(rendered) {
            setVisible(false);
            deregisterAutoCompleteKeys();
            rendered = false;
        }
    }

    /***
     * Returns the starter text, which is the text since the last space.
     * @return starter text
     */
    protected List<String> getOptions() {
        try {
            int endPosition = textComponent.getCaret().getDot();
            String txt = textComponent.getText(0,endPosition);
            List<String> options = null;
            // If we are at position zero, or the previous character was a space, it is the entire dictionary
            offset = 0;
            if(endPosition == 0 || txt.charAt(endPosition-1) == ' ' || txt.charAt(endPosition-1) == '\n') {
                options = new LinkedList<String>(dictionary);
            }
            if(options == null) {
                options = new LinkedList<String>();
                // Figure out what the user might have typed
                for(String key : dictionary) {
                    StringBuilder builder = new StringBuilder(key);
                    int length = builder.length();
                    for(int i = 0; i < length-1; i++) {
                        builder.deleteCharAt(builder.length() - 1);
                        if(txt.endsWith(builder.toString())) {
                            offset = builder.length();
                            options.add(key);
                            break;
                        }
                    }
                }
            }
            return options;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Remove the existing text and insert the selected text.
     * @param startingPosition - starting position
     * @param length - length being removed
     * @param text - text being inserted.
     */
    private void update(final int startingPosition, final int length, final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    textComponent.getDocument().remove(startingPosition,length);
                    textComponent.getDocument().insertString(startingPosition,text,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Auto-complete action
     */
    public class DisplayAction extends TextAction {

        /**
         * Auto complete location
         */
        public DisplayAction() {
            super("AutoComplete");
        }

        /**
         * Displays the text action
         * @param e - ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> options = getOptions();
            if(!rendered && options != null && options.size() > 0) {
                try {
                    startingPosition = textComponent.getCaret().getDot() - offset;
                    Point pt = textComponent.getCaret().getMagicCaretPosition();
                    if(pt == null) {
                        pt = new Point(0,0);
                    }
                    TextUI mapper = textComponent.getUI();
                    Rectangle r = mapper.modelToView(textComponent,textComponent.getCaret().getDot()-offset, Position.Bias.Forward);
                    int y = pt.y + r.height;
                    list.setListData(options.toArray());
                    setLocation(r.x, y);
                    Dimension d = getPreferredSize();
                    setSize(d);
                    list.setSelectedIndex(0);
                    setVisible(true);
                    revalidate();
                    repaint();
                    lastPosition = textComponent.getCaret().getDot();
                    registerAutoCompleteKeys();
                    rendered = true;
                } catch (Throwable t) {
                    logger.log(Level.SEVERE, "failed to insert.", t);
                }
            }
        }
    }

    /**
     * Hides the auto-completion drop down.
     */
    public class HideAction extends TextAction {

        /**
         * Hides the auto-completion
         */
        public HideAction() {
            super("HideAutoCompletion");
        }

        /**
         * Hides the auto-completion drop down.
         * @param e - action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            removeAutoComplete();
        }
    }

    /**
     * When we are in auto-complete mode, re-route the arrow keys.
     */
    public class ArrowAction extends AbstractAction {

        /**
         * True if we should move down
         */
        private boolean isDown;

        /**
         * Creats a new arrow action in the specified direction
         * @param isDown true if we should move down
         */
        public ArrowAction(boolean isDown) {
            this.isDown = isDown;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(isDown) {
                incrementSelection();
            } else {
                decrementSelection();
            }
        }
    }

    public class SelectAction extends AbstractAction {

        public SelectAction() {
            super("SelectAction");
        }

        /**
         * Hides the auto-completion drop down.
         * @param e - action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            select();
            removeAutoComplete();
        }

    }
}
