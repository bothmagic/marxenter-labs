/*
 * $Id: JDayChooser.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Ronald Tetsuo Miura
 */
public class JDayChooser extends JComponent {

    /** */
    private static final DateFormat DEFAULT_TOOLTIP_FORMAT = new SimpleDateFormat(
        "EEE dd/MM/yyyy HH:mm:ss"); //$NON-NLS-1$

    /** */
    private CalendarSupport _calendarSupport;

    /** */
    private DaySelectionSupport _selectionSupport;

    /** */
    private int _focusedDay = 1;

    /** */
    private DayRenderer _dayRenderer = new DefaultDayRenderer();

    /** */
    private DateFormat _toolTipDateFormat = DEFAULT_TOOLTIP_FORMAT;

    /** */
    private Calendar _cal = Calendar.getInstance();

    /** */
    private int _firstDayOfWeek = this._cal.getFirstDayOfWeek();

    /**
     */
    public JDayChooser() {
        this(new DefaultCalendarModel());
    }

    /**
     * @param model
     */
    public JDayChooser(CalendarModel model) {
        this(model, new DefaultDaySelectionModel());
    }

    /**
     * @param model
     * @param selectionModel
     */
    public JDayChooser(CalendarModel model, DaySelectionModel selectionModel) {
        ModelController modelController = new ModelController();
        this._calendarSupport = new CalendarSupport(model, modelController);
        this._selectionSupport = new DaySelectionSupport(selectionModel, modelController);

        this.setFocusedDay(model.get(Calendar.DAY_OF_MONTH));
        setOpaque(true);
        setFocusable(true);
        setDateToolTip();

        DayMouseListener dayMouseListener = new DayMouseListener();
        addMouseListener(dayMouseListener);
        addMouseMotionListener(dayMouseListener);
        addKeyListener(new DayKeyListener());
        addFocusListener(new DayFocusListener());
    }

    /**
     * 
     */
    void setDateToolTip() {
        DateFormat df = getToolTipDateFormat();
        if (df != null) {
            setToolTipText(df.format(new Date(getModel().getTime())));
        }
    }

    /**
     * @param g
     */
    protected void paintComponent(Graphics g) {
        Dimension size = getSize();
        double cellW = size.getWidth() / 7;
        double cellHeight = size.getHeight() / 7;

        paintHeader(g, cellW, cellHeight);
        paintBody(g, cellW, cellHeight);
    }

    /**
     * @param g
     * @param cw
     * @param ch
     */
    private void paintBody(Graphics g, double cw, double ch) {
        CalendarModel model = getModel();
        int month = model.get(Calendar.MONTH);

        prepareGridFirstDay();
        for (int week = 0; week < 6; week++) {
            for (int d = 0; d < 7; d++) {

                int x = (int) (d * cw);
                int y = (int) (ch + (week * ch));
                int w = (int) ((d + 1) * cw) - (int) (d * cw);
                int h = (int) (ch + ((week + 1) * ch)) - (int) (ch + (week * ch));
                Graphics sg = g.create(x, y, w, h);

                boolean isVisible = (this._cal.get(Calendar.MONTH) == month);
                boolean isSelected = getSelectionModel().isSelected(this._cal.getTimeInMillis());
                boolean hasFocus = hasFocus()
                    && isVisible
                    && (this._cal.get(Calendar.DAY_OF_MONTH) == this.getFocusedDay());

                Component rendererComponent = this.getDayRenderer().getDayCellRendererComponent(
                    this,
                    this._cal,
                    isVisible,
                    isSelected,
                    hasFocus);

                rendererComponent.setSize(w, h);
                rendererComponent.paint(sg);
                this._cal.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
    }

    /**
     */
    private void prepareGridFirstDay() {
        CalendarModel model = getModel();
        this._cal.setTimeInMillis(model.getTime());
        this._cal.set(Calendar.DAY_OF_MONTH, this._cal.getMinimum(Calendar.DAY_OF_MONTH));
        int diff = (this._cal.get(Calendar.DAY_OF_WEEK) - this._firstDayOfWeek);
        if (diff + 1 < 0) {
            diff += 7;
        }
        this._cal.add(Calendar.DAY_OF_YEAR, -1 - diff);
    }

    /**
     * @param g
     * @param cw
     * @param ch
     */
    protected void paintHeader(Graphics g, double cw, double ch) {
        CalendarModel model = getModel();
        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(model.getTime());
        for (int d = 0; d < 7; d++) {
            c.set(Calendar.DAY_OF_WEEK, (this._firstDayOfWeek + d - 1) % 7);
            int x = (int) (d * cw);
            int y = 0;
            int w = (int) ((d + 1) * cw) - (int) (d * cw);
            int h = (int) (ch);
            Graphics sg = g.create(x, y, w, h);
            Component rendererComponent = this.getDayRenderer().getDayHeaderRendererComponent(
                this,
                c,
                false,
                false);

            rendererComponent.setSize(w, h);
            rendererComponent.paint(sg);
        }
    }

    /**
     * @return
     */
    public Dimension getPreferredSize() {
        Calendar cal = Calendar.getInstance();

        Component c = this.getDayRenderer()
            .getDayCellRendererComponent(this, cal, true, true, true);
        Dimension cd = c.getPreferredSize();

        c = this.getDayRenderer().getDayHeaderRendererComponent(this, cal, true, true);
        Dimension hd = c.getPreferredSize();
        int w = Math.max(cd.width, hd.width);
        int h = Math.max(cd.height, hd.height);

        return new Dimension(w * 7, h * 7);
    }

    /**
     * @param time
     */
    public void setTime(long time) {
        getModel().setTime(time);
    }

    /**
     * @return
     */
    public long getTime() {
        return getModel().getTime();
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public long getTimeAtXY(int x, int y) {
        Dimension size = getSize();
        double cellWidth = size.getWidth() / 7;
        double cellHeight = size.getHeight() / 7;

        prepareGridFirstDay();

        this._cal.add(Calendar.DAY_OF_WEEK, (int) (x / cellWidth));
        this._cal.add(Calendar.DAY_OF_YEAR, 7 * (int) ((y - cellHeight) / cellHeight));

        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(getModel().getTime());
        if (this._cal.get(Calendar.MONTH) != current.get(Calendar.MONTH)) {
            return current.getTimeInMillis();
        }
        return this._cal.getTimeInMillis();
    }

    /**
     * @return
     */
    public CalendarModel getModel() {
        return this._calendarSupport.getModel();
    }

    /**
     * @param model
     */
    public void setModel(CalendarModel model) {
        this._calendarSupport.setModel(model);
        repaint();
    }

    /**
     * @return -
     */
    public DaySelectionModel getSelectionModel() {
        return this._selectionSupport.getModel();
    }

    /**
     * @param selectionModel
     */
    public void setSelectionModel(DaySelectionModel selectionModel) {
        this._selectionSupport.setModel(selectionModel);
        repaint();
    }

    /**
     * @param dayRenderer The dayRenderer to set.
     */
    public void setDayRenderer(DayRenderer dayRenderer) {
        this._dayRenderer = dayRenderer;
    }

    /**
     * @return Returns the dayRenderer.
     */
    public DayRenderer getDayRenderer() {
        return this._dayRenderer;
    }

    /**
     * @param focusedDay The focusedDay to set.
     */
    void setFocusedDay(int focusedDay) {
        this._focusedDay = focusedDay;
    }

    /**
     * @return Returns the focusedDay.
     */
    int getFocusedDay() {
        return this._focusedDay;
    }

    /**
     * @param tooltipDateFormat The tooltipDateFormat to set.
     */
    public void setToolTipDateFormat(DateFormat tooltipDateFormat) {
        this._toolTipDateFormat = tooltipDateFormat;
    }

    /**
     * @return Returns the tooltipDateFormat.
     */
    public DateFormat getToolTipDateFormat() {
        return this._toolTipDateFormat;
    }

    /**
     * @return
     */
    public int getFirstDayOfWeek() {
        return this._firstDayOfWeek;
    }

    /**
     * @param firstDayOfWeek
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this._firstDayOfWeek = firstDayOfWeek;
        this.repaint();
    }

    /**
     * @author Ronald Tetsuo Miura
     */
    private class ModelController implements ChangeListener, DaySelectionListener {

        /**
         * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
         */
        public void stateChanged(ChangeEvent e) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(getModel().getTime());
            int month = c.get(Calendar.MONTH);
            c.set(Calendar.DAY_OF_MONTH, JDayChooser.this.getFocusedDay());
            if (c.get(Calendar.MONTH) != month) {
                JDayChooser.this.setFocusedDay(1);
            }
            setDateToolTip();
            repaint();
        }

        /**
         * @see org.jdesktop.swing.calendar.DaySelectionListener#selectionChanged(org.jdesktop.swing.calendar.DaySelectionEvent)
         */
        public void selectionChanged(DaySelectionEvent evt) {
            repaint();
        }
    }

    /**
     * @author Ronald Tetsuo Miura
     */
    private class DayKeyListener extends KeyAdapter {

        /**
         * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
         */
        public void keyPressed(KeyEvent evt) {

            JDayChooser dayChooser = JDayChooser.this;

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(getModel().getTime());
            if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.DAY_OF_MONTH, dayChooser.getFocusedDay() + 1);
                if (c.get(Calendar.MONTH) == month) {
                    dayChooser.setFocusedDay(dayChooser.getFocusedDay() + 1);
                }
            } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.DAY_OF_MONTH, dayChooser.getFocusedDay() - 1);
                if (c.get(Calendar.MONTH) == month) {
                    dayChooser.setFocusedDay(dayChooser.getFocusedDay() - 1);
                }
            } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.DAY_OF_MONTH, dayChooser.getFocusedDay() - 7);
                if (c.get(Calendar.MONTH) == month) {
                    dayChooser.setFocusedDay(dayChooser.getFocusedDay() - 7);
                }
            } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.DAY_OF_MONTH, dayChooser.getFocusedDay() + 7);
                if (c.get(Calendar.MONTH) == month) {
                    dayChooser.setFocusedDay(dayChooser.getFocusedDay() + 7);
                }
            } else if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
                getModel().set(Calendar.DAY_OF_MONTH, dayChooser.getFocusedDay());
            }
            repaint();
        }
    }

    /**
     * @author Ronald Tetsuo Miura
     */
    private class DayFocusListener implements FocusListener {

        /**
         * @see java.awt.event.FocusAdapter#focusLost(java.awt.event.FocusEvent)
         * @param e
         */
        public void focusLost(FocusEvent e) {
            repaint();
        }

        /**
         * @see java.awt.event.FocusAdapter#focusLost(java.awt.event.FocusEvent)
         * @param e
         */
        public void focusGained(FocusEvent e) {
            JDayChooser.this.setFocusedDay(getModel().get(Calendar.DAY_OF_MONTH));
            repaint();
        }
    }

    /**
     * @author Ronald Tetsuo Miura
     */
    private class DayMouseListener extends MouseAdapter implements MouseMotionListener {

        /**
         * @param evt
         * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mousePressed(MouseEvent evt) {
            JDayChooser dayChooser = JDayChooser.this;

            setTime(getTimeAtXY(evt.getX(), evt.getY()));
            dayChooser.setFocusedDay(getModel().get(Calendar.DAY_OF_MONTH));

            if ((evt.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                dayChooser.getSelectionModel().setSelectionLead(getModel().getTime());
            } else {
                dayChooser.getSelectionModel().setSelectionAnchor(getModel().getTime());
            }
            requestFocus();
        }

        /**
         * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
         */
        public void mouseReleased(MouseEvent evt) {
            //
        }

        /**
         * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
         */
        public void mouseDragged(MouseEvent evt) {
            setTime(getTimeAtXY(evt.getX(), evt.getY()));
            JDayChooser.this.setFocusedDay(getModel().get(Calendar.DAY_OF_MONTH));
            JDayChooser.this.getSelectionModel().setSelectionLead(getModel().getTime());
            requestFocus();
        }

        /**
         * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
         */
        public void mouseMoved(MouseEvent evt) {
            //
        }
    }
}