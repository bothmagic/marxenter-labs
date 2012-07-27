/*
 * $Id: JCalendar.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;

/**
 * @author Ronald Tetsuo Miura
 */
public class JCalendar extends JPanel {

    /**
     * Comment for <code>_model</code>
     */
    private CalendarModel _model;

    /**
     * Comment for <code>_dayChooser</code>
     */
    private JDayChooser _dayChooser;

    /**
     * Comment for <code>_monthChooser</code>
     */
    private JMonthChooser _monthChooser;

    /**
     * Comment for <code>_yearChooser</code>
     */
    private JYearChooser _yearChooser;

    /**
     */
    public JCalendar() {
        this(new DefaultCalendarModel());
    }

    /**
     * @param model
     */
    public JCalendar(CalendarModel model) {
        this(model, new DefaultDaySelectionModel());
    }

    /**
     * @param model
     * @param selectionModel
     */
    public JCalendar(CalendarModel model, DaySelectionModel selectionModel) {
        this._model = model;
        build(selectionModel);
    }

    /**
     * @param selectionModel
     */
    private void build(DaySelectionModel selectionModel) {
        this._dayChooser = new JDayChooser(this._model, selectionModel);
        this._monthChooser = new JMonthChooser(this._model);
        this._yearChooser = new JYearChooser(this._model);

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        JPanel topPanel = new JPanel(flowLayout);
        topPanel.add(this._yearChooser);
        topPanel.add(this._monthChooser);

        setLayout(new BorderLayout());
        add(this._dayChooser, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

    }

    /**
     * @return
     */
    public CalendarModel getModel() {
        return this._model;
    }

    /**
     * @param model
     */
    public void setModel(CalendarModel model) {
        this._model = model;
        this._dayChooser.setModel(model);
        this._monthChooser.setModel(model);
        this._yearChooser.setModel(model);
        repaint();
    }

    /**
     * @param renderer
     */
    public void setDayRenderer(DayRenderer renderer) {
        this._dayChooser.setDayRenderer(renderer);
    }

    /**
     * @return
     */
    public DaySelectionModel getSelectionModel() {
        return this._dayChooser.getSelectionModel();
    }

    /**
     * @param selectionModel
     */
    public void setSelectionModel(DaySelectionModel selectionModel) {
        this._dayChooser.setSelectionModel(selectionModel);
    }

    /**
     * @param firstDayOfWeek -
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this._dayChooser.setFirstDayOfWeek(firstDayOfWeek);
    }
}