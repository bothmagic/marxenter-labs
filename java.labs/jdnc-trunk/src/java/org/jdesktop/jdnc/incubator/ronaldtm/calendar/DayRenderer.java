/*
 * $Id: DayRenderer.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.awt.Component;
import java.util.Calendar;

/**
 * @author Ronald Tetsuo Miura
 */
public interface DayRenderer {

    /**
     * @param chooser
     * @param calendar
     * @param isVisible
     * @param isSelected
     * @param hasFocus
     * @return
     */
    public Component getDayCellRendererComponent(JDayChooser chooser,
        Calendar calendar,
        boolean isVisible,
        boolean isSelected,
        boolean hasFocus);

    /**
     * @param chooser
     * @param calendar
     * @param isSelected
     * @param hasFocus
     * @return
     */
    public Component getDayHeaderRendererComponent(JDayChooser chooser,
        Calendar calendar,
        boolean isSelected,
        boolean hasFocus);
}