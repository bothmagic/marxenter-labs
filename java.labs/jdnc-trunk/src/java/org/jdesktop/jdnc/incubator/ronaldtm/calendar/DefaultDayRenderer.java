/*
 * $Id: DefaultDayRenderer.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

/**
 * @author Ronald Tetsuo Miura
 */
public class DefaultDayRenderer extends JLabel implements DayRenderer {

    /**
     * Comment for <code>SPACE</code>
     */
    private static final String ESPACO = "  "; //$NON-NLS-1$

    /** Comment for <code>HEADER_FORMAT</code>. */
    private SimpleDateFormat HEADER_FORMAT = new SimpleDateFormat("E"); //$NON-NLS-1$

    /** Comment for <code>HEADER_FORMAT</code>. */
    private SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("d"); //$NON-NLS-1$

    /**
     */
    public DefaultDayRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setOpaque(true);
    }

    /**
     * @param chooser
     * @param calendar
     * @param isVisible
     * @param isSelected
     * @param hasFocus
     * @return
     */
    public Component getDayCellRendererComponent(JDayChooser chooser, Calendar calendar,
        boolean isVisible, boolean isSelected, boolean hasFocus) {

        UIDefaults uidef = UIManager.getLookAndFeelDefaults();

        setText(this.DAY_FORMAT.format(calendar.getTime())); //$NON-NLS-2$
        setBackground(uidef.getColor("Table.background")); //$NON-NLS-1$
        setForeground(uidef.getColor("Table.foreground")); //$NON-NLS-1$
        setBorder(null);
        setFont(uidef.getFont("Table.font")); //$NON-NLS-1$

        if (!isVisible) {
            setText(ESPACO); //$NON-NLS-1$
        } else {
            if (isSelected) {
                setBackground(uidef.getColor("Table.selectionBackground")); //$NON-NLS-1$
                setForeground(uidef.getColor("Table.selectionForeground")); //$NON-NLS-1$
            }
            if (hasFocus) {
                //setBackground(uidef.getColor("Table.focusCellBackground")); //$NON-NLS-1$
                //setForeground(uidef.getColor("Table.focusCellForeground")); //$NON-NLS-1$
                setBorder(uidef.getBorder("Table.focusCellHighlightBorder")); //$NON-NLS-1$
                setBorder(new LineBorder(Color.black)); //$NON-NLS-1$
            }
        }

        return this;
    }

    /**
     * @param chooser
     * @param calendar
     * @param isSelected
     * @param hasFocus
     * @return
     */
    public Component getDayHeaderRendererComponent(JDayChooser chooser, Calendar calendar,
        boolean isSelected, boolean hasFocus) {

        setHorizontalAlignment(CENTER);
        setText(this.HEADER_FORMAT.format(calendar.getTime()).substring(0, 1));

        UIDefaults uidef = UIManager.getLookAndFeelDefaults();
        setBackground(uidef.getColor("TableHeader.background")); //$NON-NLS-1$
        setForeground(uidef.getColor("TableHeader.foreground")); //$NON-NLS-1$
        setFont(uidef.getFont("TableHeader.font")); //$NON-NLS-1$
        setBorder(uidef.getBorder("TableHeader.cellBorder")); //$NON-NLS-1$

        return this;
    }

    /**
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        if (true) {
            size = new Dimension(22, 16);
        }
        return size;
    }

}