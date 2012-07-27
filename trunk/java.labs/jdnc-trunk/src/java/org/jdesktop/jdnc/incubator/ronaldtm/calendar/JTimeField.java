/*
 * $Id: JTimeField.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.jdnc.incubator.ronaldtm.text.document.rangelimited.IntegerRangeLimitedDocument;

/**
 * @author Ronald Tetsuo Miura
 */
public class JTimeField extends JPanel implements SwingConstants {

    /** Comment for <code>_support</code>. */
    CalendarSupport _support;

    /**
     * Comment for <code>_fieldPanel</code>
     */
    private JPanel _fieldPanel = new JPanel();

    /**
     * Comment for <code>hour</code>
     */
    JTextField _hourField = new JTextField(2);

    /**
     * Comment for <code>minute</code>
     */
    JTextField _minuteField = new JTextField(2);

    /**
     * Comment for <code>second</code>
     */
    JTextField _secondField = new JTextField(2);

    /**
     * Comment for <code>_hmSeparator</code>
     */
    private JLabel _hmSeparator = new JLabel(":", CENTER);

    /**
     * Comment for <code>_msSeparator</code>
     */
    private JLabel _msSeparator = new JLabel(":", CENTER);

    /**
     * Comment for <code>_initialized</code>
     */
    private boolean _changedUI = false;

    /**
     */
    public JTimeField() {
        this(new DefaultCalendarModel());
    }

    /**
     * @param model -
     */
    public JTimeField(CalendarModel model) {
        build(model);
    }

    /**
     * @param model
     */
    private void build(CalendarModel model) {
        this._support = new CalendarSupport(model, new ModelListener());

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setHgap(3);
        flowLayout.setVgap(0);

        JPanel p = this._fieldPanel;
        JTextField hf = this._hourField;
        JTextField mf = this._minuteField;
        JTextField sf = this._secondField;

        p.setLayout(flowLayout);
        p.add(hf);
        p.add(this._hmSeparator);
        p.add(mf);
        p.add(this._msSeparator);
        p.add(sf);

        hf.setHorizontalAlignment(RIGHT);
        mf.setHorizontalAlignment(RIGHT);
        sf.setHorizontalAlignment(RIGHT);

        hf.setDocument(IntegerRangeLimitedDocument.decorate(hf.getDocument(), 0, 23));
        mf.setDocument(IntegerRangeLimitedDocument.decorate(mf.getDocument(), 0, 59));
        sf.setDocument(IntegerRangeLimitedDocument.decorate(sf.getDocument(), 0, 59));

        setLayout(new BorderLayout());
        this.add(p, BorderLayout.CENTER);
        updateUI();

        this._changedUI = true;
    }

    /**
     * @see javax.swing.JPanel#updateUI()
     */
    public void updateUI() {
        super.updateUI();
        this._changedUI = true;
    }

    /**
     * @see java.awt.Container#doLayout()
     */
    public void doLayout() {
        updateComponentsUI();
        super.doLayout();
    }

    /**
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        updateComponentsUI();
        return super.getPreferredSize();
    }

    /**
     * @see javax.swing.JComponent#getMaximumSize()
     */
    public Dimension getMaximumSize() {
        updateComponentsUI();
        return super.getMaximumSize();
    }

    /**
     * @see javax.swing.JComponent#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        updateComponentsUI();
        return super.getMinimumSize();
    }

    /**
     * 
     */
    private void updateComponentsUI() {
        if (this._changedUI) {
            JTextField t = new JTextField();
            this._fieldPanel.setOpaque(true);
            this._fieldPanel.setBackground(t.getBackground());
            this._fieldPanel.setForeground(t.getForeground());
            this._fieldPanel.setBorder(t.getBorder());

            prepareComponent(this._hourField);
            prepareComponent(this._minuteField);
            prepareComponent(this._secondField);
            prepareComponent(this._hmSeparator);
            prepareComponent(this._msSeparator);
            this._changedUI = false;
        }
    }

    /**
     * @param c
     */
    private void prepareComponent(JComponent c) {
        c.setBorder(null);
        c.setOpaque(false);
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
        repaint();
    }

    /**
     * @author Ronald Tetsuo Miura
     */
    private class ModelListener implements ChangeListener {

        /** */
        private final DecimalFormat _df = new DecimalFormat("00");

        /**
         * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
         */
        public void stateChanged(ChangeEvent evt) {
            JTimeField outer = JTimeField.this;

            CalendarModel model = outer._support.getModel();
            outer._hourField.setText(String.valueOf(model.get(Calendar.HOUR_OF_DAY)));
            outer._minuteField.setText(this._df.format(model.get(Calendar.MINUTE)));
            outer._secondField.setText(this._df.format(model.get(Calendar.SECOND)));

            repaint();
        }
    }
}