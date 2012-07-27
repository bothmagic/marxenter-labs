/*
 * $Id: JClock.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
public class JClock extends JComponent {

    /**
     * Comment for <code>DEFAULT_TOOLTIP_FORMAT</code>
     */
    private static final DateFormat DEFAULT_TOOLTIP_FORMAT = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$

    /** Comment for <code>_calendar</code>. */
    private CalendarSupport _support;

    /**
     * Comment for <code>_tooltipDateFormat</code>
     */
    private DateFormat _toolTipDateFormat = DEFAULT_TOOLTIP_FORMAT;

    /**
     */
    public JClock() {
        this(new DefaultCalendarModel());
    }

    /**
     * @param model
     */
    public JClock(CalendarModel model) {
        this._support = new CalendarSupport(model, new ModelListener());
        setOpaque(true);
        setFocusable(false);
        setDateToolTip();
        setDoubleBuffered(true);
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
     * @param grfx
     */
    protected void paintComponent(Graphics grfx) {
        Graphics2D g = (Graphics2D) grfx;
        super.paintComponent(g);

        Dimension size = getSize();

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getModel().getTime());

        int w = size.width;
        int h = size.height;
        int cx = w / 2;
        int cy = h / 2;
        int radHour = cx - 35;
        int radMinute = cx - 15;

        int margin = 10;

        int radMark1 = cx - margin - 2;
        int radMark2 = cx - margin + 2;

        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        g.setColor(getBackground());
        g.fillRect(0, 0, size.width, size.height);

        g.setColor(Color.black);
        for (int i = 0; i < 12; i++) {
            int[] p1 = new int[2];
            int[] p2 = new int[2];
            calculatePoint(p1, cx, cy, radMark1, hourToDegree(i, 0));
            calculatePoint(p2, cx, cy, radMark2, hourToDegree(i, 0));
            g.setStroke(new BasicStroke((i % 3 == 0) ? 5 : 3));
            g.drawLine(p1[0], p1[1], p2[0], p2[1]);
        }

        int[] points = new int[2];
        calculatePoint(points, cx, cy, radHour, hourToDegree(hour, minute));
        g.setStroke(new BasicStroke(5));
        g.drawLine(cx, cy, points[0], points[1]);

        calculatePoint(points, cx, cy, radMinute, minuteToDegree(minute));
        g.setStroke(new BasicStroke(3));
        g.drawLine(cx, cy, points[0], points[1]);

        calculatePoint(points, cx, cy, radMinute, minuteToDegree(second));
        g.setStroke(new BasicStroke(1));
        g.drawLine(cx, cy, points[0], points[1]);
    }

    /**
     * @param hour
     * @param minute
     * @return
     */
    private double hourToDegree(int hour, int minute) {
        return (hour * 30) + (minute * 0.20) - 90;
    }

    /**
     * @param minute
     * @return
     */
    private double minuteToDegree(int minute) {
        return (minute * 6) - 90;
    }

    /**
     * @param points
     * @param cx
     * @param cy
     * @param radius
     * @param arc
     */
    private void calculatePoint(int[] points, int cx, int cy, int radius, double arc) {

        double radArc = arc * ((2 * Math.PI) / 360);
        points[0] = (int) (cx + (radius * Math.cos(radArc)));
        points[1] = (int) (cy + (radius * Math.sin(radArc)));
    }

    /**
     * @return
     */
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
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
     * @author Ronald Tetsuo Miura
     */
    private class ModelListener implements ChangeListener {

        /**
         * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
         */
        public void stateChanged(ChangeEvent e) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(getModel().getTime());
            setDateToolTip();
            repaint();
        }
    }
}