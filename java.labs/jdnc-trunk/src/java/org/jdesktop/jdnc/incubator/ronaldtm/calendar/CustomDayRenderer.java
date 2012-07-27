/*
 * $Id: CustomDayRenderer.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Calendar;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.JComponent;

/**
 * @author Ronald Tetsuo Miura
 */
public class CustomDayRenderer extends DefaultDayRenderer {

    /**
     * Comment for <code>_fontCache</code>
     */
    private Map _fontCache = new WeakHashMap();

    /**
     * @param chooser
     * @param calendar
     * @param isSelected
     * @param hasFocus
     * @return
     */
    public Component getDayHeaderRendererComponent(JDayChooser chooser, Calendar calendar,
        boolean isSelected, boolean hasFocus) {

        JComponent c = (JComponent) super.getDayHeaderRendererComponent(
            chooser,
            calendar,
            isSelected,
            hasFocus);

        c.setBorder(null);
        c.setBackground(Color.gray); //$NON-NLS-1$
        c.setForeground(Color.white); //$NON-NLS-1$

        Map cache = this._fontCache;
        Font f = getFont();
        if (!cache.containsKey(f)) {
            cache.put(f, new Font(f.getName(), f.getStyle() | Font.BOLD, f.getSize()));
        }
        f = (Font) cache.get(f);
        c.setFont(f);

        return c;
    }

    /**
     * @see org.jdesktop.jdnc.incubator.ronaldtm.calendar.DefaultDayRenderer#getDayCellRendererComponent(org.jdesktop.jdnc.incubator.ronaldtm.calendar.JDayChooser, java.util.Calendar, boolean, boolean, boolean)
     */
    public Component getDayCellRendererComponent(JDayChooser chooser, Calendar calendar,
        boolean isVisible, boolean isSelected, boolean hasFocus) {

        JComponent c = (JComponent) super.getDayCellRendererComponent(
            chooser,
            calendar,
            isVisible,
            isSelected,
            hasFocus);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            c.setForeground(Color.red);

            Map cache = this._fontCache;
            Font f = getFont();
            if (!cache.containsKey(f)) {
                cache.put(f, new Font(f.getName(), f.getStyle() | Font.BOLD, f.getSize()));
            }
            f = (Font) cache.get(f);
            c.setFont(f);
        }

        return c;
    }
}