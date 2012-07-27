/*
 * $Id: JYearChooser.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;

import javax.swing.BoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;

import org.jdesktop.jdnc.incubator.ronaldtm.text.document.LimitedLengthDocument;
import org.jdesktop.jdnc.incubator.ronaldtm.text.document.NumericDocument;

/**
 * @author Ronald Tetsuo Miura
 */
public class JYearChooser extends JPanel implements ChangeListener {

    /**
     * Comment for <code>_support</code>
     */
    private final CalendarSupport _support;

    /**
     * Comment for <code>_combo</code>
     */
    private JTextField _text;

    /**
     * Comment for <code>_scroll</code>
     */
    private JScrollBar _scroll;

    /**
     */
    public JYearChooser() {
        this(new DefaultCalendarModel());
    }

    /**
     * @param model
     */
    public JYearChooser(CalendarModel model) {
        this._support = new CalendarSupport(model, this);
        build();
    }

    /**
     */
    private void build() {
        Calendar date = Calendar.getInstance();
        CalendarModel calendarModel = this._support.getModel();

        JTextField text = new JTextField();
        Document numdoc = NumericDocument.decorate(text.getDocument());
        LimitedLengthDocument limdoc = LimitedLengthDocument.decorate(numdoc, 4);
        text.setDocument(limdoc);
        text.setText(String.valueOf(calendarModel.get(Calendar.YEAR)));
        TextListener textListener = new TextListener(text, calendarModel);
        text.addFocusListener(textListener);
        text.addKeyListener(textListener);

        JScrollBar scroll = new JScrollBar(Adjustable.VERTICAL);
        scroll.setMinimum(1);
        scroll.setMaximum(9999);
        scroll.setUnitIncrement(-1);
        scroll.setVisibleAmount(1);
        scroll.setFocusable(false);
        scroll.setValue(date.get(Calendar.YEAR));
        scroll.getModel().addChangeListener(new ScrollListener(calendarModel, scroll.getModel()));

        calendarModel.set(Calendar.YEAR, date.get(Calendar.YEAR));

        this._text = text;
        this._scroll = scroll;

        setLayout(new BorderLayout());
        add(this._text, BorderLayout.CENTER);
        add(this._scroll, BorderLayout.EAST);
    }

    /**
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        Dimension size = new Dimension(this._text.getPreferredSize());
        size.width += this._scroll.getPreferredSize().width + 10; //+10 to avoid "flicker"
        return size;
    }

    /**
     * @see javax.swing.JComponent#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        return this._text.getPreferredSize();
    }

    /**
     * @see javax.swing.JComponent#getMaximumSize()
     */
    public Dimension getMaximumSize() {
        return this._text.getPreferredSize();
    }

    /**
     * @return
     */
    public CalendarModel getModel() {
        return this._support.getModel();
    }

    /**
     * @param model
     */
    public void setModel(CalendarModel model) {
        this._support.setModel(model);
    }

    /**
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        int year = this._support.getModel().get(Calendar.YEAR);
        if (this._text != null) {
            this._text.setText(String.valueOf(year));
        }
        if (this._scroll != null) {
            this._scroll.setValue(year);
        }
    }

    /**
     * @author Ronald Tetsuo Miura
     */
    private class TextListener extends KeyAdapter implements FocusListener {

        /**
         * Comment for <code>_observedCombo</code>
         */
        private JTextField _observedText;

        /**
         * Comment for <code>_model</code>
         */
        private CalendarModel _model;

        /**
         * @param text
         * @param model
         */
        public TextListener(JTextField text, CalendarModel model) {
            this._observedText = text;
            this._model = model;
        }

        /**
         * @see java.awt.event.FocusAdapter#focusGained(java.awt.event.FocusEvent)
         * @param evt
         */
        public void focusGained(FocusEvent evt) {
            this._observedText.setSelectionStart(0);
            this._observedText.setSelectionEnd(this._observedText.getText().length());
        }

        /**
         * @see java.awt.event.FocusAdapter#focusLost(java.awt.event.FocusEvent)
         * @param evt
         */
        public void focusLost(FocusEvent evt) {
            String year = this._observedText.getText();
            if (year.length() == 0) {
                year = "0"; //$NON-NLS-1$
            }
            this._model.set(Calendar.YEAR, Integer.parseInt(year));
        }

        /**
         * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
         */
        public void keyPressed(KeyEvent evt) {
            if ((evt.getKeyCode() == KeyEvent.VK_RIGHT) || (evt.getKeyCode() == KeyEvent.VK_UP)) {
                int year = this._model.get(Calendar.YEAR) + 1;
                if (year < 10000) {
                    this._model.set(Calendar.YEAR, year);
                }

            } else if ((evt.getKeyCode() == KeyEvent.VK_LEFT)
                || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {

                int year = this._model.get(Calendar.YEAR) - 1;
                if (year > 0) {
                    this._model.set(Calendar.YEAR, year);
                }
            }
        }
    }

    /**
     * @author Ronald Tetsuo Miura
     */
    private class ScrollListener implements ChangeListener {

        /**
         * Comment for <code>_model</code>
         */
        private final CalendarModel _calendarModel;

        /**
         * Comment for <code>_scrollModel</code>
         */
        private final BoundedRangeModel _scrollModel;

        /**
         * @param calendarModel
         * @param scrollModel
         */
        public ScrollListener(CalendarModel calendarModel, BoundedRangeModel scrollModel) {
            this._calendarModel = calendarModel;
            this._scrollModel = scrollModel;
        }

        /**
         * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
         */
        public void stateChanged(ChangeEvent e) {
            this._calendarModel.set(Calendar.YEAR, this._scrollModel.getValue());
        }
    }
}