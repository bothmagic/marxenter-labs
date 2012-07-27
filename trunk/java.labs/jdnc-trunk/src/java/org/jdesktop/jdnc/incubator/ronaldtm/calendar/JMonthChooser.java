/*
 * $Id: JMonthChooser.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Ronald Tetsuo Miura
 */
public class JMonthChooser extends JPanel {

    /**
     * Comment for <code>_support</code>
     */
    final CalendarSupport _support;

    /**
     * Comment for <code>_combo</code>
     */
    JComboBox _combo;

    /**
     */
    public JMonthChooser() {
        this(new DefaultCalendarModel());
    }

    /**
     * @param model
     */
    public JMonthChooser(CalendarModel model) {
        this._support = new CalendarSupport(model, new ModelListener());
        build();
    }

    /**
     */
    private void build() {
        SimpleDateFormat df = new SimpleDateFormat("MMMM"); //$NON-NLS-1$
        Calendar date = Calendar.getInstance();
        int currentMonth = date.get(Calendar.MONTH);

        String[] monthNames = new String[12];
        for (int i = 0; i < 12; i++) {
            date.set(Calendar.MONTH, i);
            monthNames[i] = df.format(date.getTime());
        }
        this._combo = new JComboBox(monthNames);
        this._support.getModel().set(Calendar.MONTH, currentMonth);
        this._combo.addActionListener(new ComboListener(this._combo, this._support.getModel()));

        setLayout(new GridLayout(1, 1));
        add(this._combo);
    }

    /**
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        return this._combo.getPreferredSize();
    }

    /**
     * @see javax.swing.JComponent#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        return this._combo.getMinimumSize();
    }

    /**
     * @see javax.swing.JComponent#getMaximumSize()
     */
    public Dimension getMaximumSize() {
        return this._combo.getMaximumSize();
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
     * @author Ronald Tetsuo Miura
     */
    private class ModelListener implements ChangeListener {

        /**
         * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
         */
        public void stateChanged(ChangeEvent e) {
            int selectedMonth = JMonthChooser.this._support.getModel().get(Calendar.MONTH);
            JMonthChooser.this._combo.setSelectedIndex(selectedMonth);
            repaint();
        }

    }

    /**
     * @author Ronald Tetsuo Miura
     */
    private class ComboListener implements ActionListener {

        /**
         * Comment for <code>_observedCombo</code>
         */
        private JComboBox _observedCombo;

        /**
         * Comment for <code>_model</code>
         */
        private CalendarModel _model;

        /**
         * @param combo
         * @param model
         */
        public ComboListener(JComboBox combo, CalendarModel model) {
            this._observedCombo = combo;
            this._model = model;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent evt) {
            int selectedMonth = this._observedCombo.getSelectedIndex();
            this._model.set(Calendar.MONTH, selectedMonth);
        }
    }
}