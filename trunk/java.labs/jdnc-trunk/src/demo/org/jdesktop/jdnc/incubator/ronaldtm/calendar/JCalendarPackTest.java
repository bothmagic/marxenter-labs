/*
 * $Id: JCalendarPackTest.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * @author Ronald Tetsuo Miura
 */
public class JCalendarPackTest extends JApplet {

    /** */
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /** */
    JFrame frame = new JFrame();

    /**
     * @see java.applet.Applet#init()
     */
    public void init() {
        JButton toggle = new JButton("Exibir/Ocultar");
        toggle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCalendarPackTest.this.frame.setVisible(!JCalendarPackTest.this.frame.isVisible());
            }
        });

        getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
        getContentPane().add(toggle);

        try {
            build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see java.applet.Applet#destroy()
     */
    public void destroy() {
        super.destroy();
        this.frame.dispose();
    }

    /**
     */
    private void build() {
        JMenu menu = new JMenu("Look And Feel"); //$NON-NLS-1$

        LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < lafs.length; i++) {
            final LookAndFeelInfo laf = lafs[i];
            JMenuItem item = new JMenuItem(new AbstractAction() {

                public void actionPerformed(ActionEvent evt) {
                    try {
                        UIManager.setLookAndFeel(laf.getClassName());
                        SwingUtilities.updateComponentTreeUI(JCalendarPackTest.this.frame);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            item.setText(laf.getName());
            menu.add(item);
        }

        JMenuBar bar = new JMenuBar();
        bar.add(menu);

        this.frame.setJMenuBar(bar);
        Container c = this.frame.getContentPane();
        c.setLayout(new FlowLayout());
        final CalendarModel model = new DefaultCalendarModel();
        final SystemClockCalendarModel clockModel = new SystemClockCalendarModel();
        clockModel.start();
        final ProxyCalendarModel proxy = new ProxyCalendarModel(model);

        final DaySelectionModel selectionModel = new DefaultDaySelectionModel();

        final JYearChooser year = new JYearChooser(proxy);
        final JMonthChooser month = new JMonthChooser(proxy);
        final JCalendar calendar = new JCalendar(proxy, selectionModel);
        final JCalendar other = new JCalendar(new DefaultCalendarModel(), selectionModel);
        final JClock clock = new JClock(proxy);
        final JTimeField time = new JTimeField(proxy);

        calendar.setDayRenderer(new CustomDayRenderer());

        selectionModel.setSelectionMode(DaySelectionModel.SINGLE_SELECTION);

        try {
            other.getModel().setTime(dateFormat.parse("01/09/2004").getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        c.add(year);
        c.add(month);
        c.add(other);
        c.add(calendar);
        c.add(clock);
        c.add(time);

        final JCheckBox check = new JCheckBox("Use System Clock"); //$NON-NLS-1$
        c.add(check);
        check.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (check.getModel().isSelected()) {
                    proxy.setModel(clockModel);
                } else {
                    proxy.setModel(model);
                }
            }
        });

        final JComboBox comboSelecao = new JComboBox(new String[] { "SINGLE", "MULTIPLE", "WEEK"});
        c.add(comboSelecao);
        comboSelecao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                selectionModel.setSelectionMode(comboSelecao.getSelectedIndex());
            }
        });

        final JComboBox comboFirstDayOfWeek = new JComboBox(new String[] { "DOM", "SEG", "TER",
            "QUA", "QUI", "SEX", "SAB"});
        c.add(comboFirstDayOfWeek);
        comboFirstDayOfWeek.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                other.setFirstDayOfWeek(comboFirstDayOfWeek.getSelectedIndex() + 2);
                calendar.setFirstDayOfWeek(comboFirstDayOfWeek.getSelectedIndex() + 2);
            }
        });

        final JComboBox comboFirstDayOfWeekSel = new JComboBox(new String[] { "DOM", "SEG", "TER",
            "QUA", "QUI", "SEX", "SAB"});
        c.add(comboFirstDayOfWeekSel);
        comboFirstDayOfWeekSel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                selectionModel.setFirstDayOfWeek(comboFirstDayOfWeekSel.getSelectedIndex() + 1);
            }
        });

        proxy.setTime(Calendar.getInstance().getTimeInMillis());

        this.frame.setSize(640, 480);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new JCalendarPackTest().build();
    }
}