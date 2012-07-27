/*
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.plaf;

import java.awt.Color;
import java.awt.Font;

import javax.swing.LookAndFeel;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;

import org.jdesktop.swingx.JXCalendar;

import com.sun.swingset3.utilities.ArrowIcon;

public class CalendarAddon extends AbstractComponentAddon {
    public CalendarAddon() {
        super("JXCalendar");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addBasicDefaults(addon, defaults);
        
        defaults.add(JXCalendar.uiClassID, "org.jdesktop.swingx.plaf.basic.BasicCalendarUI");
        defaults.add("JXCalendar.background", new ColorUIResource(Color.WHITE));
        defaults.add("JXCalendar.monthStringBackground", new ColorUIResource(138, 173, 209));
        defaults.add("JXCalendar.monthStringForeground", new ColorUIResource(68, 68, 68));
//        defaults.add("JXCalendar.daysOfTheWeekForeground", new ColorUIResource(68, 68, 68));
//        defaults.add("JXCalendar.weekOfTheYearForeground", new ColorUIResource(68, 68, 68));
        defaults.add("JXCalendar.unselectableDayForeground", new ColorUIResource(Color.RED));
        defaults.add("JXCalendar.selectedBackground", new ColorUIResource(197, 220, 240));
//        defaults.add("JXCalendar.flaggedDayForeground", new ColorUIResource(Color.RED));
//        defaults.add("JXCalendar.leadingDayForeground", new ColorUIResource(Color.LIGHT_GRAY));
//        defaults.add("JXCalendar.trailingDayForeground", new ColorUIResource(Color.LIGHT_GRAY));
        defaults.add("JXCalendar.font", UIManagerExt.getSafeFont("Button.font",
                        new FontUIResource("Dialog", Font.PLAIN, 12)));
        Object icon = LookAndFeel.makeIcon(CalendarAddon.class, "basic/resources/month-down.png");
        defaults.add("JXCalendar.monthDownFileName",
                icon);
        defaults.add("JXCalendar.monthUpFileName",
                LookAndFeel.makeIcon(CalendarAddon.class, "basic/resources/month-up.png"));
        defaults.add("JXCalendar.boxPaddingX", 3);
        defaults.add("JXCalendar.boxPaddingY", 3);
    }

    /** 
     * @inherited <p>
     */
    @Override
    protected void addWindowsDefaults(LookAndFeelAddons addon,
            DefaultsList defaults) {
        super.addWindowsDefaults(addon, defaults);
        defaults.add("JXCalendar.monthStringBackground", new ColorUIResource(Color.WHITE));
        defaults.add("JXCalendar.monthStringForeground", new ColorUIResource(68, 68, 68));
        defaults.add("JXCalendar.monthDownFileName", 
                new IconUIResource(new ArrowIcon(ArrowIcon.WEST, 10, Color.GRAY)));
        defaults.add("JXCalendar.monthUpFileName", 
                new IconUIResource(new ArrowIcon(ArrowIcon.EAST, 10, Color.GRAY)));
    }
    
    
}
