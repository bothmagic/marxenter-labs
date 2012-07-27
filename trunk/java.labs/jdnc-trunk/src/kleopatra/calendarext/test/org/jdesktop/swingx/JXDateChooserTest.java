/*
 * $Id: JXDatePickerTest.java 3497 2009-09-09 13:42:32Z kleopatra $
 * 
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
package org.jdesktop.swingx;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.DatePickerFormatter;
import org.jdesktop.swingx.calendar.DateSelectionModel;
import org.jdesktop.swingx.calendar.DefaultDateSelectionModel;
import org.jdesktop.swingx.calendar.SingleDaySelectionModel;
import org.jdesktop.swingx.plaf.UIManagerExt;
import org.jdesktop.swingx.plaf.basic.BasicDatePickerUI.EditorCancelAction;
import org.jdesktop.swingx.test.XTestUtils;
import org.jdesktop.test.ActionReport;
import org.jdesktop.test.PopupMenuReport;
import org.jdesktop.test.PropertyChangeReport;
import org.jdesktop.test.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Unit tests for JXDateChooser.
 */
@RunWith(JUnit4.class)
public class JXDateChooserTest extends InteractiveTestCase {
    private static final Logger LOG = Logger.getLogger(JXDateChooserTest.class
            .getName());
    
    private Calendar calendar;

    @Override
    @Before
       public void setUp() {
        calendar = Calendar.getInstance();
    }

    @Override
    @After
       public void tearDown() {
    }

    /**
     * Issue #1171-swingx: support popupMenuListeners.
     * 
     * Tests notification on showing.
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    @Test
    public void testPopupMenuListenerNotificationVisible() throws InterruptedException, InvocationTargetException {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run testLinkPanelNull - headless");
            return;
        }
        final JXDateChooser picker = new JXDateChooser();
        final JXFrame frame = new JXFrame("showing", false);
        frame.add(picker);
        frame.pack();
        frame.setVisible(true);
        final PopupMenuReport report = new PopupMenuReport();
        picker.addPopupMenuListener(report);
        Action togglePopup = picker.getActionMap().get("TOGGLE_POPUP");
        togglePopup.actionPerformed(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                assertEquals(1, report.getEventCount());
                assertEquals(1, report.getVisibleEventCount());
                frame.dispose();
                
            }
        });
    }
    
    
    /**
     * Issue #1171-swingx: support popupMenuListeners.
     * 
     * Tests add/remove.
     */
    @Test
    public void testPopupMenuListenerManagement() {
        JXDateChooser picker = new JXDateChooser();
        assertNotNull(picker.getPopupMenuListeners());
        int count = picker.getPopupMenuListeners().length;
        PopupMenuReport report = new PopupMenuReport();
        picker.addPopupMenuListener(report);
        assertEquals(count + 1, picker.getPopupMenuListeners().length);
        assertTrue(Arrays.asList(picker.getPopupMenuListeners()).contains(report));
        picker.removePopupMenuListener(report);
        assertEquals(count, picker.getPopupMenuListeners().length);
        assertFalse(Arrays.asList(picker.getPopupMenuListeners()).contains(report));
    }
    /**
     * Issue #1144-swingx: JXDateChooser must accept custom formatter.
     * Pathological .. but anyway: formatter may be null.
     * Issue manifests in throwing NPE.
     */
    @Test
    public void testCustomFormatterNull() {
        JXDateChooser picker = new JXDateChooser();
        DefaultFormatterFactory factory = new DefaultFormatterFactory();
        assertEquals("sanity (null formatter): ", null, factory.getFormatter(picker.getEditor()));
        picker.getEditor().setFormatterFactory(factory);
        picker.updateUI();
    }
    /**
     * Issue #1144-swingx: JXDateChooser must accept custom formatter.
     * Use core DateFormatter - issue manifests in throwing classcastEx
     */
    @Test
    public void testCustomFormatterCore() {
        JXDateChooser picker = new JXDateChooser();
        DefaultFormatterFactory factory = new DefaultFormatterFactory(new DateFormatter());
        picker.getEditor().setFormatterFactory(factory);
        picker.updateUI();
    }
    
    /**
     * Issue #910-swingx: home commit must be disabled if picker not editable.
     * 
     */
    @Test
    public void testNotEditableNullHomeNavigate() {
        JXDateChooser picker = new JXDateChooser();
        picker.getActionMap().remove(JXDateChooser.HOME_NAVIGATE_KEY);
        picker.setEditable(!picker.isEditable());
    }

    /**
     * Issue #910-swingx: home commit must be disabled if picker not editable.
     * 
     */
    @Test
    public void testNotEditableDisabledHomeNavigate() {
        JXDateChooser picker = new JXDateChooser();
        Action delegate = picker.getActionMap().get(JXDateChooser.HOME_NAVIGATE_KEY);
        assertEquals(picker.isEditable(), delegate.isEnabled());
        picker.setEditable(!picker.isEditable());
        assertEquals(picker.isEditable(), delegate.isEnabled());
    }

    /**
     * Issue #910-swingx: home commit must be disabled if picker not editable.
     * 
     */
    @Test
    public void testNotEditableNullHomeCommit() {
        JXDateChooser picker = new JXDateChooser();
        picker.getActionMap().remove(JXDateChooser.HOME_COMMIT_KEY);
        picker.setEditable(!picker.isEditable());
    }

    /**
     * Issue #910-swingx: home commit must be disabled if picker not editable.
     * 
     */
    @Test
    public void testNotEditableDisabledHomeCommit() {
        JXDateChooser picker = new JXDateChooser();
        Action delegate = picker.getActionMap().get(JXDateChooser.HOME_COMMIT_KEY);
        assertEquals(picker.isEditable(), delegate.isEnabled());
        picker.setEditable(!picker.isEditable());
        assertEquals(picker.isEditable(), delegate.isEnabled());
    }
    /**
     * Sanity: report in forum that editor not disabled if picker disabled.
     * Looks okay.
     */
    @Test
    public void testEnabled() {
        JXDateChooser picker = new JXDateChooser();
        picker.setEnabled(false);
        assertFalse("sanity: picker disabled", picker.isEnabled());
        assertEquals("editor enabled must follow picker enabled", 
                picker.isEnabled(), picker.getEditor().isEnabled());
    }
    
    /**
     * Issue #764-swingx: JXDateChooser sizing 
     * 
     * editor must respect columns.
     */
    @Test
    public void testDatePickerColumns50() {
        JXDateChooser picker = new JXDateChooser();
        picker.getEditor().setColumns(50);
        JXDateChooser other = new JXDateChooser(new Date());
        other.getEditor().setColumns(50);
        assertEquals(other.getEditor().getPreferredSize(), picker.getEditor().getPreferredSize());
        
    }

    /**
     * Issue #764-swingx: JXDateChooser sizing 
     * 
     * editor must respect columns.
     */
    @Test
    public void testDatePickerColumns5() {
        JXDateChooser picker = new JXDateChooser();
        picker.getEditor().setColumns(5);
        JXDateChooser other = new JXDateChooser(new Date());
        other.getEditor().setColumns(5);
        assertEquals(other.getEditor().getPreferredSize(), picker.getEditor().getPreferredSize());
        
    }

    /**
     * Issue #667-swingx: don't install the datepicker border for gtk.
     * 
     * Here we are testing that the BasicPickerUI doesn't touch the 
     * editors border if it finds a null. 
     * 
     */
    @Test
    public void testPickerBorder() {
        // force loading of addon
        new JXDateChooser();
        Border pickerBorder = UIManager.getBorder("JXDateChooser.border");
        if (pickerBorder == null) {
            LOG.info("cant run test - no pickerborder");
            return;
        } 
        try {
            UIManager.put("JXDateChooser.border", "none");
            assertNull(UIManager.getBorder("JXDateChooser.border"));
            JXDateChooser picker = new JXDateChooser();
            JTextField field = new JFormattedTextField();
            assertEquals(field.getBorder(), picker.getEditor().getBorder());
        } finally {
            // restore LAF border
            UIManager.put("JXDateChooser.border", null);
            assertEquals(pickerBorder, UIManager.getBorder("JXDateChooser.border"));
        }
    }

    /**
     * Issue #724-swingx: picker must notify about timezone changes.
     * Here: change the timezone on the monthView - can't guarantee the notification.
     * At least not without hacks...
     */
    @Test
    public void testTimeZoneChangeNotificationChangeOnMonthView() {
        JXDateChooser picker = new JXDateChooser();
        TimeZone timeZone = picker.getTimeZone();
        TimeZone alternative = getSafeAlternativeTimeZone(timeZone);
        PropertyChangeReport report = new PropertyChangeReport();
        picker.addPropertyChangeListener(report);
        picker.getCalendarView().setTimeZone(alternative);
        TestUtils.assertPropertyChangeEvent(report, "timeZone", timeZone, alternative, false);
    }

    /**
     * Issue #724-swingx: picker must notify about timezone changes.
     * Here: change the timezon on the picker - can guarantee the notification.
     */
    @Test
    public void testTimeZoneChangeNotification() {
        JXDateChooser picker = new JXDateChooser();
        TimeZone timeZone = picker.getTimeZone();
        TimeZone alternative = getSafeAlternativeTimeZone(timeZone);
        PropertyChangeReport report = new PropertyChangeReport();
        picker.addPropertyChangeListener(report);
        picker.setTimeZone(alternative);
        TestUtils.assertPropertyChangeEvent(report, "timeZone", timeZone, alternative, false);
    }

    /**
     * Issue #724-swingx: picker must notify about timezone changes.
     * Here: setMonthView must update the picker's timezone if different and
     *   fire a notification.
     */
    @Test
    public void testTimeZoneSetMonthView() {
        JXDateChooser picker = new JXDateChooser();
        TimeZone timeZone = picker.getTimeZone();
        TimeZone alternative = getSafeAlternativeTimeZone(timeZone);
        // prepare a new monthView with different TimeZone
        JXCalendar monthView = new JXCalendar();
        monthView.setTimeZone(alternative);
        PropertyChangeReport report = new PropertyChangeReport();
        picker.addPropertyChangeListener(report);
        picker.setCalendarView(monthView);
        TestUtils.assertPropertyChangeEvent(report, "timeZone", timeZone, alternative, false);
        report.clear();
        TimeZone another = getSafeAlternativeTimeZone(alternative);
        monthView.setTimeZone(another);
        TestUtils.assertPropertyChangeEvent(report, "timeZone", alternative, another, false);
    }
 
    
    /**
     * Issue #568-swingx: picker must respect selection model (as of time fields).
     * 
     * Behaviour defined by selection model of monthView. While the default 
     * (DaySelectionModel) normalizes the dates to the start of the day in the
     * model's calendar coordinates, a SingleDaySelectionModel keeps the date as-is.
     * For now, need to explicitly set. 
     */
    @Test
    public void testSetDateKeepsTime() {
        JXDateChooser picker = new JXDateChooser();
        DateSelectionModel selectionModel = new SingleDaySelectionModel();
        picker.getCalendarView().setSelectionModel(selectionModel);
        Date date = new Date();
        selectionModel.setSelectionInterval(date, date);
        Date first = selectionModel.getFirstSelectionDate();
        assertEquals("formats diff: " + (date.getTime() - first.getTime())
                , date, first);
    }
    

    /**
     * Issue #??-swingx: JXDateChooser must keep monthView's firstDisplayedDate
     *   in synch with selection/today.
     * Issue #705-swingx: JXCalendar must not scroll in layoutContainer.
     * 
     * The implication is that client code (such as JXDateChooser/UI) is 
     * responsible to do the scrolling.
     */
    @Test
    public void testVisibleMonthContainsSelectionOpenSet() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run testLinkPanelNull - headless");
            return;
        }
        calendar.set(2008, Calendar.JULY, 15);
        JXDateChooser picker = new JXDateChooser();
        picker.setDate(calendar.getTime());
        JXFrame frame = new JXFrame("showing", false);
        frame.add(picker);
        frame.pack();
        frame.setVisible(true);
        Action togglePopup = picker.getActionMap().get("TOGGLE_POPUP");
        togglePopup.actionPerformed(null);
        CalendarUtils.startOfMonth(calendar);
        assertEquals(calendar.getTime(), picker.getCalendarView().getPage().getTime());
        frame.dispose();
    }

    /**
     * Issue #??-swingx: JXDateChooser must keep monthView's firstDisplayedDate
     *   in synch with selection/today.
     * Issue #705-swingx: JXCalendar must not scroll in layoutContainer.
     * 
     * The implication is that client code (such as JXDateChooser/UI) is 
     * responsible to do the scrolling.
     */
    @Test
    public void testVisibleMonthContainsSelectionOpenInitial() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run testLinkPanelNull - headless");
            return;
        }
        calendar.set(2008, Calendar.JULY, 15);
        JXDateChooser picker = new JXDateChooser(calendar.getTime());
        JXFrame frame = new JXFrame("showing", false);
        frame.add(picker);
        frame.setVisible(true);
        Action togglePopup = picker.getActionMap().get("TOGGLE_POPUP");
        togglePopup.actionPerformed(null);
        CalendarUtils.startOfMonth(calendar);
        assertEquals(calendar.getTime(), picker.getCalendarView().getPage().getTime());
        frame.dispose();
    }

    /**
     * Issue #??-swingx: JXDateChooser must keep monthView's firstDisplayedDate
     *   in synch with selection/today.
     * Issue #705-swingx: JXCalendar must not scroll in layoutContainer.
     * 
     * The implication is that client code (such as JXDateChooser/UI) is 
     * responsible to do the scrolling.
     */
    @Test
    public void testVisibleMonthContainsSelectionIinitial() {
        calendar.set(2008, Calendar.JULY, 15);
        JXDateChooser picker = new JXDateChooser(calendar.getTime());
        CalendarUtils.startOfMonth(calendar);
        assertEquals(calendar.getTime(), picker.getCalendarView().getPage().getTime());
    }
    
    /**
     * Issue #??-swingx: JXDateChooser must keep monthView's firstDisplayedDate
     *   in synch with selection/today.
     * Issue #705-swingx: JXCalendar must not scroll in layoutContainer.
     * 
     * The implication is that client code (such as JXDateChooser/UI) is 
     * responsible to do the scrolling.
     */
    @Test
    public void testVisibleMonthContainsSelectionSet() {
        JXDateChooser picker = new JXDateChooser();
        calendar.set(2008, Calendar.JULY, 15);
        picker.setDate(calendar.getTime());
        CalendarUtils.startOfMonth(calendar);
        assertEquals(calendar.getTime(), picker.getCalendarView().getPage().getTime());
    }
    


    /**
     * Issue #693-swingx: format of custom locale.
     * Here: test constructor with locale parameter.
     */
    @Test
    public void testCustomLocaleConstructor() {
        Locale german = Locale.GERMAN;
        JXDateChooser picker = new JXDateChooser(german);
        SimpleDateFormat format = (SimpleDateFormat) picker.getFormats()[0];
        String pattern = UIManagerExt.getString("JXDateChooser.longFormat", german);
        assertEquals(pattern , format.toPattern());
    }

    /**
     * Issue #693-swingx: format of custom locale.
     * Here: test setLocale.
     */
    @Test
    public void testCustomLocaleSet() {
        Locale german = Locale.GERMAN;
        JXDateChooser picker = new JXDateChooser();
        picker.setLocale(german);
        SimpleDateFormat format = (SimpleDateFormat) picker.getFormats()[0];
        String pattern = UIManagerExt.getString("JXDateChooser.longFormat", german);
        assertEquals(pattern , format.toPattern());
    }

    /**
     * Issue #690-swingx: custom dateformats lost on switching LF.
     * 
     * Here: default formats re-set.
     */
    @Test
    public void testDefaultFormats() {
        JXDateChooser picker = new JXDateChooser();
        DateFormat[] formats = picker.getFormats();
        assertEquals(formats.length, picker.getFormats().length);
        picker.updateUI();
        assertNotSame(formats[0], picker.getFormats()[0]);
    }

    /**
     * Issue #690-swingx: custom dateformats lost on switching LF.
     * 
     * Sanity test: custom format set as expected.
     */
    @Test
    public void testCustomFormatsSet() {
        JXDateChooser picker = new JXDateChooser();
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.UK);
        picker.setFormats(format);
        DateFormat[] formats = picker.getFormats();
        // sanity
        assertEquals(1, formats.length);
        assertSame(format, formats[0]);
    }
    /**
     * Issue #690-swingx: custom dateformats lost on switching LF.
     * 
     * Here: test that custom format is unchanged after updateUI
     */
    @Test
    public void testCustomFormatsKept() {
        JXDateChooser picker = new JXDateChooser();
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.UK);
        picker.setFormats(format);
        picker.updateUI();
        DateFormat[] formats = picker.getFormats();
        assertEquals(1, formats.length);
        assertSame(format, formats[0]);
    }
    

    /**
     * Issue #542-swingx: NPE in init if linkFormat not set.
     * 
     * After plaf cleanup no longer as virulent as earlier: with
     * addResourceBundle, there's always at least the fall-back value in the
     * bundle, so as long as the bundle is loaded at all, we have a not-null
     * value (and no way to remove which is okay).
     * 
     */
    @Test
    public void testLinkFormatStringNull() {
        // force loading
        new JXDateChooser();
        String key = "JXDateChooser.linkFormat";
        String oldLinkFormat = UIManagerExt.getString(key);
        // sanity: the addon was loaded
        assertNotNull(oldLinkFormat);
        UIManager.put(key, null);
        assertEquals("no null overwrite", oldLinkFormat, UIManagerExt.getString(key));
        UIManager.getLookAndFeelDefaults().remove(key);
        assertEquals("no remove", oldLinkFormat, UIManagerExt.getString(key));
        new JXDateChooser();
    }
    
    /**
     * Issue #584-swingx: need to clarify null handling.
     * 
     * Forum report: NPE under certain initial conditions.
     * 
     */
    @Test
    public void testPickerFormatSetFormats() {
        JXDateChooser picker = new JXDateChooser();
        picker.setFormats((DateFormat[])null);
    }

    /**
     * Issue #584-swingx: need to clarify null handling.
     * 
     * Forum report: NPE under certain initial conditions.
     * 
     */
    @Test
    public void testPickerFormatSetFormatsNullElements() {
        JXDateChooser picker = new JXDateChooser();
        try {
            picker.setFormats(new DateFormat[] { null});
            fail("must not accept null elements in format array");
        } catch (NullPointerException e) {
            // doc'ed behaviour
        }
    }

    /**
     * Issue #584-swingx: need to clarify null handling.
     * 
     * Forum report: NPE under certain initial conditions.
     * 
     */
    @Test
    public void testPickerFormatSetFormatStrings() {
        JXDateChooser picker = new JXDateChooser();
        picker.setFormats((String[])null);
    }
    
    /**
     * Issue #584-swingx: need to clarify null handling.
     * 
     * Forum report: NPE under certain initial conditions.
     * 
     */
    @Test
    public void testPickerFormatSetFormatStringsNullElements() {
        JXDateChooser picker = new JXDateChooser();
        try {
            picker.setFormats(new String[] {null});
            fail("must not accept null elements in format array");
        } catch (NullPointerException e) {
            // doc'ed behaviour
        }
    }
    /**
     * Issue #584-swingx: need to clarify null handling.
     * 
     * Forum report: NPE under certain initial conditions.
     * This produced exactly the stacktrace as reported.
     * 
     */
    @Test
    public void testPickerFormatsNotNull() {
        JXDateChooser picker = new JXDateChooser();
        // trick the picker - no formats
        picker.getEditor().setFormatterFactory(new DefaultFormatterFactory(
                new DatePickerFormatter((DateFormat[]) null)));
        assertNotNull("picker format array must not be null", 
                picker.getFormats());
    }

    /**
     * Issue #584-swingx: need to clarify null handling.
     * 
     * 
     */
    @Test
    public void testPickerFormatsNotNullUnknownFormatter() {
        JXDateChooser picker = new JXDateChooser();
        // trick the picker - no formats
        picker.getEditor().setFormatterFactory(new DefaultFormatterFactory());
        assertNotNull("picker format array must not be null", 
                picker.getFormats());
    }



    

    /**
     * Issue #565-swingx: popup not closed when focus moved to Combo
     * Issue #573-swingx: datePicker editor not focused on F2 (in table)
     * 
     * testing internals: need focus listener add/remove
     *
     */
    @Test
    public void testFocusListenerOnPicker() {
        JXDateChooser picker = new JXDateChooser();
        assertEquals(1, picker.getFocusListeners().length);
        picker.getUI().uninstallUI(picker);
        assertEquals(0, picker.getFocusListeners().length);
    }
    
    /**
     * Issue #565-swingx: popup not closed when focus moved to Combo
     * Issue #573-swingx: datePicker editor not focused on F2 (in table)
     * 
     * testing internals: need focus listener add/remove
     *
     */
    @Test
    public void testFocusListenerOnEditor() {
        JFormattedTextField field = new JFormattedTextField(new DatePickerFormatter());
        int listenerCount = field.getFocusListeners().length;
        JXDateChooser picker = new JXDateChooser();
        assertEquals(listenerCount + 1, picker.getEditor().getFocusListeners().length);
        picker.getUI().uninstallUI(picker);
        assertEquals(listenerCount, picker.getEditor().getFocusListeners().length);
    }
    
    /**
     * tests LinkPanel set to null after showing.
     * Was: NPE.
     */
    @Test
    public void testLinkPanelSetNull() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run testLinkPanelNull - headless");
            return;
        }
        JXDateChooser picker = new JXDateChooser();
        JXFrame frame = new JXFrame("showing", false);
        frame.add(picker);
        frame.setVisible(true);
        Action togglePopup = picker.getActionMap().get("TOGGLE_POPUP");
        togglePopup.actionPerformed(null);
        picker.setLinkPanel(null);
        frame.dispose();
    }

    /**
     * tests initial null linkPanel.
     *
     */
    @Test
    public void testLinkPanelInitalNull() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run testLinkPanelNull - headless");
            return;
        }
        JXDateChooser picker = new JXDateChooser();
        picker.setLinkPanel(null);
        JXFrame frame = new JXFrame("showing", false);
        frame.add(picker);
        frame.setVisible(true);
        Action togglePopup = picker.getActionMap().get("TOGGLE_POPUP");
        togglePopup.actionPerformed(null);
        frame.dispose();
    }

    /**
     * Test install/uninstall of LinkPanel when popup is showing.
     * - removed/added from parent
     * - bindings initially installed/uninstalled/re-installed
     */
    @Test
    public void testLinkPanelRemovedAdded() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run testLinkPanelNull - headless");
            return;
        }
        JXDateChooser picker = new JXDateChooser();
        JXFrame frame = new JXFrame("showing", false);
        frame.add(picker);
        frame.setVisible(true);
        // show the popup ... PENDING: need api on picker.
        Action togglePopup = picker.getActionMap().get("TOGGLE_POPUP");
        togglePopup.actionPerformed(null);
        JPanel linkPanel = picker.getLinkPanel();
        // assert the bindings are installed
        assertLinkPanelBindings(linkPanel, true);
        Container oldParent = linkPanel.getParent();
        // sanity
        assertNotNull(oldParent);
        // remove
        picker.setLinkPanel(null);
        // assert it is removed
        assertNull("linkPanel must be removed", linkPanel.getParent());
        // assert bindings removed
        assertLinkPanelBindings(linkPanel, false);
        // set again
        picker.setLinkPanel(linkPanel);
        // assert the bindings are installed again
        assertLinkPanelBindings(linkPanel, true);
        assertSame("linkPanel must be added to same parent", 
                oldParent, linkPanel.getParent());
        frame.dispose();
    }


    /**
     * Tests that the linkPanel bindings and actions 
     * are removed (no popup)
     *
     */
    @Test
    public void testLinkPanelBindingUninstalled() {
        JXDateChooser picker = new JXDateChooser();
        JComponent linkPanel = picker.getLinkPanel();
        picker.setLinkPanel(null);
        assertLinkPanelBindings(linkPanel, false);
    }
    

    /**
     * Tests that the linkPanel has actions and keybindings
     * for homeCommit/-Cancel (initially, no popup)
     *
     */
    @Test
    public void testLinkPanelAction() {
        JXDateChooser picker = new JXDateChooser();
        JComponent linkPanel = picker.getLinkPanel();
        assertLinkPanelBindings(linkPanel, true);
    }

    /**
     * @param linkPanel
     */
    private void assertLinkPanelBindings(JComponent linkPanel, boolean bound) {
        if (bound) {
        assertNotNull("home commit action must be registered", 
                linkPanel.getActionMap().get(JXDateChooser.HOME_COMMIT_KEY));
        assertNotNull("home navigate action must be registered", 
                linkPanel.getActionMap().get(JXDateChooser.HOME_NAVIGATE_KEY));
        } else {
            assertNull("home commit action must not be registered", 
                    linkPanel.getActionMap().get(JXDateChooser.HOME_COMMIT_KEY));
            assertNull("home navigate action must not be registered", 
                    linkPanel.getActionMap().get(JXDateChooser.HOME_NAVIGATE_KEY));
        }
        assertKeyBindings(linkPanel, JXDateChooser.HOME_COMMIT_KEY, bound);
        assertKeyBindings(linkPanel, JXDateChooser.HOME_NAVIGATE_KEY, bound);
    }
    
    /**
     * PENDING: move to testUtils.
     * @param comp
     * @param actionKey
     */
    public void assertKeyBindings(JComponent comp, Object actionKey, boolean bound) {
        boolean hasAncestorBinding = hasBinding(
                comp.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT), 
                actionKey);
        boolean hasFocusedBinding = hasBinding(
                comp.getInputMap(JComponent.WHEN_FOCUSED), 
                actionKey);
        boolean hasInFocusedBinding = hasBinding(
                comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), 
                actionKey);
        boolean hasBinding = hasAncestorBinding || hasFocusedBinding || hasInFocusedBinding;
        assertEquals("component has keybinding for " + actionKey,
                bound,
                hasBinding);
        
    }

    /**
     * 
     * PENDING: move to testutils.
     * @param map
     * @param actionKey
     */
    public boolean hasBinding(InputMap map, Object actionKey) {
        KeyStroke[] keyStrokes = map.keys();
        if (keyStrokes != null) {
            for (KeyStroke stroke : keyStrokes) {
                if (actionKey.equals(map.get(stroke))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * test that the toggle popup is registered in the 
     * picker's actionMap.
     *
     * Issue #596-swingx: don't use space to open popup.
     */
    @Test
    public void testTogglePopupAction() {
       JXDateChooser picker = new JXDateChooser();
       Action togglePopup = picker.getActionMap().get("TOGGLE_POPUP");
       assertNotNull(togglePopup);
       KeyStroke space = KeyStroke.getKeyStroke("alt DOWN");
       Object actionKey = picker.getInputMap(
               JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
               .get(space);
       assertEquals(actionKey, "TOGGLE_POPUP");
    }
    
    /**
     * Characterization: when does picker fire action events?
     * 
     * Test that set date programmatically (directly or indirectly) 
     * does not fire an actionEvent.
     */
    @Test
    public void testSetDateSilently() {
        JXDateChooser picker = new JXDateChooser();
        ActionReport report = new ActionReport();
        picker.addActionListener(report);
        // via editor
        Date value = XTestUtils.getStartOfToday(3);
        picker.getEditor().setValue(value);
        assertEquals(value, picker.getDate());
        // via selection
        Date selected = XTestUtils.getStartOfToday(4);
        picker.getCalendarView().setSelectionInterval(selected, selected);
        assertEquals(selected, picker.getDate());
        Date date = XTestUtils.getStartOfToday(5);
        // directly
        picker.setDate(date);
        assertEquals(date, picker.getDate());
        assertEquals(0, report.getEventCount());
    }


    /**
     * Enhanced commit/cancel.
     * 
     * test the cancel produced by the monthview trigger
     * cancel in the picker.
     *
     */
    @Test
    public void testCommitCancelFromMonthViewCancel() {
        JXDateChooser picker = new JXDateChooser();
        final ActionReport report = new ActionReport();
        picker.addActionListener(report);
        Action cancelAction = picker.getCalendarView().getActionMap().get(JXCalendar.CANCEL_KEY);
        cancelAction.actionPerformed(null);
        assertEquals("must have receive 1 event after monthView cancel", 
                1, report.getEventCount());
        assertEquals(JXDateChooser.CANCEL_KEY, report.getLastActionCommand());
        
    }
    /**
     * Enhanced commit/cancel.
     * 
     * test the commit produced by the monthview trigger
     * commit in the picker.
     *
     */
    @Test
    public void testCommitCancelFromMonthViewCommit() {
        JXDateChooser picker = new JXDateChooser();
        Action commitAction = picker.getCalendarView().getActionMap().get(JXCalendar.COMMIT_KEY);
        final ActionReport report = new ActionReport();
        picker.addActionListener(report);
        commitAction.actionPerformed(null);
         assertEquals("must have receive 1 event after monthView commit", 
                1, report.getEventCount());
        assertEquals(JXDateChooser.COMMIT_KEY, report.getLastActionCommand());
    }
    
    /**
     * Enhanced commit/cancel.
     * 
     * test that the ui installed a listener.
     */
    @Test
    public void testCommitCancelListeningToMonthView() {
        JXCalendar monthView = new JXCalendar();
        int standalone = monthView.getListeners(ActionListener.class).length;
        assertEquals(0, standalone);
        JXDateChooser picker = new JXDateChooser();
        int contained = picker.getCalendarView().getListeners(ActionListener.class).length;
        assertEquals(standalone + 1, contained);
    }

    /**
     * test wrapping and resetting the editors cancel action.
     * internals ... mostly to be sure about cleanup and re-wire.
     */
    @Test
    public void testCancelEditorAction() {
        JFormattedTextField field = new JFormattedTextField(new DatePickerFormatter());
        // original action
        Action original = field.getActionMap().get(EditorCancelAction.TEXT_CANCEL_KEY);
        assertNotNull(original);
        JXDateChooser picker = new JXDateChooser();
        JFormattedTextField editor = picker.getEditor();
        Action wrapper = editor.getActionMap().get(EditorCancelAction.TEXT_CANCEL_KEY);
        // wrapper installed
        assertTrue("PickerUI installed the wrapper action", wrapper instanceof EditorCancelAction);
        // set editor to field
        picker.setEditor(field);
        // old editor back to original 
        assertSame("original action must be reset on setEditor", original, 
                editor.getActionMap().get(EditorCancelAction.TEXT_CANCEL_KEY));
        Action otherWrapper = field.getActionMap().get(EditorCancelAction.TEXT_CANCEL_KEY);
        assertTrue(otherWrapper instanceof EditorCancelAction);
        // created a new one
        assertNotSame(wrapper, otherWrapper);
        // uninstall
        picker.getUI().uninstallUI(picker);
        assertSame("original action must be reset on uninstall", original, 
                field.getActionMap().get(EditorCancelAction.TEXT_CANCEL_KEY));
    }
    
    /**
     * Enhanced commit/cancel.
     * 
     * test that cancel action reverts silently: date related
     * state unchanged and no events fired (except the actionEvent).
     *
     */
    @Test
    public void testCancelEditRevertsSilently() {
        JXDateChooser picker = new JXDateChooser(new Date());
        String text = picker.getEditor().getText();
        // manipulate the text, not entirely safe ...
        String changed = text.replace('0', '1');
        picker.getEditor().setText(changed);
        final ActionReport actionReport = new ActionReport();
        picker.addActionListener(actionReport);
        picker.getEditor().addActionListener(actionReport);
        picker.getCalendarView().addActionListener(actionReport);
        final PropertyChangeReport propertyReport = new PropertyChangeReport();
        picker.addPropertyChangeListener(propertyReport);
        picker.getEditor().addPropertyChangeListener(propertyReport);
        picker.cancelEdit();
        assertEquals(0, propertyReport.getEventCount());
        assertEquals(1, actionReport.getEventCount());
    }
    
    /**
     * Enhanced commit/cancel.
     * 
     * test that cancel fires as expected.
     * PENDING: need to invoke ... safe test?
     *
     */
    @Test
    public void testCommitCancelActionsFireCancel() {
        JXDateChooser picker = new JXDateChooser();
        final ActionReport report = new ActionReport();
        picker.addActionListener(report);
        Action cancelAction = picker.getActionMap().get(JXDateChooser.CANCEL_KEY);
        cancelAction.actionPerformed(null);
        assertEquals(1, report.getEventCount());
        assertEquals(JXDateChooser.CANCEL_KEY, report.getLastActionCommand());
    }

    /**
     * Enhanced commit/cancel.
     * 
     * test that commit fires as expected.
     * PENDING: need to invoke ... safe test?
     */
    @Test
    public void testCommitCancelActionsFireCommit() {
        JXDateChooser picker = new JXDateChooser();
        Action commitAction = picker.getActionMap().get(JXDateChooser.COMMIT_KEY);
        final ActionReport report = new ActionReport();
        picker.addActionListener(report);
        commitAction.actionPerformed(null);
        assertEquals(1, report.getEventCount());
        assertEquals(JXDateChooser.COMMIT_KEY, report.getLastActionCommand());
    }
    
    /**
     * Enhanced commit/cancel.
     * 
     * test that actions are registered.
     *
     */
    @Test
    public void testCommitCancelActionExist() {
        JXDateChooser picker = new JXDateChooser();
        assertNotNull(picker.getActionMap().get(JXDateChooser.CANCEL_KEY));
        assertNotNull(picker.getActionMap().get(JXDateChooser.COMMIT_KEY));
    }
    

    /**
     * Issue #658-swingx: timezone in linkformat updated.
     * 
     * linkDate synced with monthView's today after setting.
     */
    @Test
    public void testLinkDateSetToday() {
        JXDateChooser picker = new JXDateChooser();
        Calendar cal = picker.getCalendarView().getPage();
        cal.setTime(picker.getCalendarView().getToday());
        cal.add(Calendar.MONTH, 1);
        CalendarUtils.endOfDay(cal);
        // NOTE: no public api, testing to guarantee the synch in all cases
//        picker.getMonthView().setToday(cal.getTime());
        fail("todo: linkDate relation to what in JXCalendar?");
        assertEquals(picker.getCalendarView().getToday(), picker.getLinkDay());
    }

    /**
     * Issue #658-swingx: timezone in linkformat updated.
     * 
     * Initial linkDate synced with monthView's today.
     */
    @Test
    public void testLinkDateInitial() {
        JXDateChooser picker = new JXDateChooser();
        assertEquals(picker.getCalendarView().getToday(), picker.getLinkDay());
    }
    
    /**
     * Issue #658-swingx: timezone in linkformat updated.
     * 
     * Here: set timezone in picker.
     */
    @Test
    public void testSynchTimeZoneLinkFormatOnModified() {
        JXDateChooser picker = new JXDateChooser();
        TimeZone alternative = getSafeAlternativeTimeZone(picker.getTimeZone());
        picker.setTimeZone(alternative);
        assertTimeZoneLinkFormat(picker, alternative);
    }

    /**
     * Issue #554-swingx: timezone of formats and picker must be synched.

     * Here: set monthView with alternative timezone
     */
    @Test
    public void testSynchTimeZoneLinkFormatOnSetMonthView() {
        JXDateChooser picker = new JXDateChooser();
        TimeZone defaultZone = picker.getTimeZone();
        TimeZone alternative = getSafeAlternativeTimeZone(defaultZone);
        JXCalendar monthView = new JXCalendar();
        monthView.setTimeZone(alternative);
        picker.setCalendarView(monthView);
        assertTimeZoneLinkFormat(picker, alternative);
    }

    /**
     * Assert that all DateFormats in the picker's linkFormat have the same
     * timezone as the picker.
     * 
     * @param picker the JXDateChooser to test
     * @param alternative the expected timeZone of the picker (for sanity only)
     */
    private void assertTimeZoneLinkFormat(JXDateChooser picker,
            TimeZone alternative) {
        // sanity: picker has timezone as expected
        assertEquals("expected timezone in picker", alternative, picker.getTimeZone());
        MessageFormat format = picker.getLinkFormat();
        for (Format subFormat : format.getFormats()) {
            if (subFormat instanceof DateFormat) {
                assertEquals(picker.getTimeZone(), ((DateFormat) subFormat).getTimeZone());
            }
        }
    }

    /**
     * Issue #554-swingx: timezone of formats and picker must be synched.
     * Here: set the timezone in the monthView.
     */
    @Test
    public void testSynchTimeZoneModifiedInMonthView() {
        JXDateChooser picker = new JXDateChooser();
        TimeZone defaultZone = picker.getTimeZone();
        TimeZone alternative = getSafeAlternativeTimeZone(defaultZone);
        picker.getCalendarView().setTimeZone(alternative);
        assertTimeZoneDateFormats(picker, alternative);
    }

 
    /**
     * Issue #554-swingx: timezone of formats and picker must be synched.
     * 
     * Here: set the timezone in the picker.
     */
    @Test
    public void testSynchTimeZoneModifiedInPicker() {
        JXDateChooser picker = new JXDateChooser();
        TimeZone defaultZone = picker.getTimeZone();
        TimeZone alternative = getSafeAlternativeTimeZone(defaultZone);
        picker.setTimeZone(alternative);
        assertTimeZoneDateFormats(picker, alternative);
    }

    /**
     * Issue #554-swingx: timezone of formats and picker must be synched.

     * Here: set the timezone in the picker.
     */
    @Test
    public void testSynchTimeZoneOnSetMonthView() {
        JXDateChooser picker = new JXDateChooser();
        TimeZone defaultZone = picker.getTimeZone();
        TimeZone alternative = getSafeAlternativeTimeZone(defaultZone);
        JXCalendar monthView = new JXCalendar();
        monthView.setTimeZone(alternative);
        picker.setCalendarView(monthView);
        assertTimeZoneDateFormats(picker, alternative);
    }

    /**
     * Issue #554-swingx: timezone of formats and picker must be synched.
     * 
     * Here: initialize the formats with the pickers timezone on setting.
     */
    @Test
    public void testSynchTimeZoneOnSetFormats() {
        JXDateChooser picker = new JXDateChooser();
        TimeZone defaultZone = picker.getTimeZone();
        TimeZone alternative = getSafeAlternativeTimeZone(defaultZone);
        picker.setTimeZone(alternative);
        picker.setFormats(DateFormat.getDateInstance());
        assertTimeZoneDateFormats(picker, alternative);
    }

    /**
     * Assert that all DateFormats in the picker's linkFormat have the same
     * timezone as the picker.
     * 
     * @param picker the JXDateChooser to test
     * @param alternative the expected timeZone of the picker (for sanity only)
     */
    private void assertTimeZoneDateFormats(JXDateChooser picker,
            TimeZone alternative) {
        assertEquals(alternative, picker.getTimeZone());
        for (DateFormat format : picker.getFormats()) {
            assertEquals("timezone must be synched", picker.getTimeZone(), format.getTimeZone());
        }
    }

    /**
     * Issue #554-swingx: timezone of formats and picker must be synched.
     */
    @Test
    public void testSynchTimeZoneInitial() {
        JXDateChooser picker = new JXDateChooser();
        assertNotNull(picker.getTimeZone());
        for (DateFormat format : picker.getFormats()) {
            assertEquals("timezone must be synched", picker.getTimeZone(), format.getTimeZone());
        }
    }

    /**
     * setFormats should fire a propertyChange.
     *
     */
    @Test
    public void testFormatsProperty() {
        JXDateChooser picker = new JXDateChooser();
        PropertyChangeReport report = new PropertyChangeReport();
        picker.addPropertyChangeListener(report);
        DateFormat[] oldFormats = picker.getFormats();
        DateFormat[] newFormats = new DateFormat[] {DateFormat.getDateInstance()};
        picker.setFormats(newFormats);
        TestUtils.assertPropertyChangeEvent(report, "formats", oldFormats, newFormats);
        
    }

    /**
     * Test doc'ed behaviour: editor must not be null.
     */
    @Test
    public void testEditorNull() {
        JXDateChooser picker = new JXDateChooser();
        assertNotNull(picker.getEditor());

        try {
            picker.setEditor(null);
            fail("picker must throw NPE if editor is null");
        } catch (NullPointerException e) {
            // nothing to do - doc'ed behaviour
        }
    }

    /**
     * Test doc'ed behaviour: editor must not be null.
     */
    @Test
    public void testMonthViewNull() {
        JXDateChooser picker = new JXDateChooser();
        assertNotNull(picker.getCalendarView());

        try {
            picker.setCalendarView(null);
            fail("picker must throw NPE if monthView is null");
        } catch (NullPointerException e) {
            // nothing to do - doc'ed behaviour
        }
    }
    /**
     * picker has cleaned date, clarified doc.
     * The test is not exactly true, the details 
     * are up for the DatePickerUI to decide. 
     * 
     */
    @Test
    public void testSetDateCleansDate() {
        JXDateChooser picker = new JXDateChooser();
        Date date = calendar.getTime();
        picker.setDate(date);
        assertEquals(CalendarUtils.startOfDay(calendar, date), picker.getDate());
    }

    /**
     * Regression testing: make sure the passed-in date is not changed.
     * 
     */
    @Test
    public void testSetDateDoesNotChangeOriginal() {
        JXDateChooser picker = new JXDateChooser();
        Date date = calendar.getTime();
        Date copy = new Date(date.getTime());
        picker.setDate(date);
        assertEquals(copy, date);
    }
    
    /**
     *  date is a bound property of DatePicker.
     */
    @Test
    public void testDateProperty() {
        JXDateChooser picker = new JXDateChooser();
        Date date = XTestUtils.getStartOfToday(5);
        PropertyChangeReport report = new PropertyChangeReport();
        picker.addPropertyChangeListener("date", report);
        picker.setDate(date);
        TestUtils.assertPropertyChangeEvent(report, "date", null, date);
    }

    /**
     *  date is a bound property of DatePicker. 
     *  test indirect event firing: changed editor value
     */
    @Test
    public void testDatePropertyThroughEditor() {
        JXDateChooser picker = new JXDateChooser();
        Date date = XTestUtils.getStartOfToday(5);
        PropertyChangeReport report = new PropertyChangeReport();
        picker.addPropertyChangeListener("date", report);
        picker.getEditor().setValue(date);
        TestUtils.assertPropertyChangeEvent(report, "date", null, date);
    }

    /**
     *  date is a bound property of DatePicker.
     *  test indirect event firing: changed monthView selection 
     */
    @Test
    public void testDatePropertyThroughSelection() {
        JXDateChooser picker = new JXDateChooser();
        Date date = XTestUtils.getStartOfToday(5);
        PropertyChangeReport report = new PropertyChangeReport();
        picker.addPropertyChangeListener("date", report);
        picker.getCalendarView().setSelectionInterval(date, date);
        TestUtils.assertPropertyChangeEvent(report, "date", null, date);
    }

    /**
     *  date is a bound property of DatePicker.
     *  test indirect event firing: commit edited value 
     * @throws ParseException 
     *
     */
    @Test
    public void testDatePropertyThroughCommit() throws ParseException {
        JXDateChooser picker = new JXDateChooser(new Date());
        Date initialDate = picker.getDate();
        String text = picker.getEditor().getText();
        Format[] formats = picker.getFormats();
        assertEquals(picker.getDate(), formats[0].parseObject(text));
        // manipulate the text, not entirely safe ...
        String changed = text.replace('0', '1');
        picker.getEditor().setText(changed);
        Date date;
        try {
            date = (Date) formats[0].parseObject(changed);
        } catch (ParseException e) {
            LOG.info("cannot run DatePropertyThroughCommit - parseException in manipulated text");
            return;
        }
        // sanity ...
        assertFalse("", date.equals(picker.getDate()));
        PropertyChangeReport report = new PropertyChangeReport();
        picker.addPropertyChangeListener("date", report);
        picker.commitEdit();
        TestUtils.assertPropertyChangeEvent(report, "date", initialDate, date);
    }

    /**
     * last piece: removed synch control from picker.commit. 
     * @throws ParseException 
     *
     */
    @Test
    public void testSynchAllAfterCommit() throws ParseException {
        JXDateChooser picker = new JXDateChooser(new Date());
        String text = picker.getEditor().getText();
        Format[] formats = picker.getFormats();
        assertEquals(picker.getDate(), formats[0].parseObject(text));
        // manipulate the text, not entirely safe ...
        String changed = text.replace('0', '1');
        picker.getEditor().setText(changed);
        Date date;
        try {
            date = (Date) formats[0].parseObject(changed);
        } catch (ParseException e) {
            LOG.info("cannot run testSynchAllAfterCommit - parseException in manipulated text");
            return;
        }
        // sanity ...
        assertFalse("", date.equals(picker.getDate()));
        picker.commitEdit();
        assertSynchAll(picker, date);
    }
    
    /**
     * Issue #559-swingX: date must be synched in all parts.
     * here: initial. 
     * 
     */
    @Test
    public void testSynchAllInitialDate() {
        Date date = XTestUtils.getStartOfToday(5);
        JXDateChooser picker = new JXDateChooser(date);
        assertSynchAll(picker, date);
    } 
    
    /**
     * Issue #559-swingX: date must be synched in all parts.
     * here: set date in picker
     * 
     * Note: test uses a cleaned date, do same with uncleaned.
     */
    @Test
    public void testSynchAllOnDateModified() {
        JXDateChooser picker = new JXDateChooser();
        Date date = XTestUtils.getStartOfToday(5);
        picker.setDate(date);
        assertSynchAll(picker, date);
    } 

    /**
     * Issue #559-swingX: date must be synched in all parts.
     * here: set selected date in monthview
     * Note: test uses a cleaned date, do same with uncleaned.
     */
    @Test
    public void testSynchAllOnSelectionChange()  {
        JXDateChooser picker = new JXDateChooser();
        Date date = XTestUtils.getStartOfToday(5);
        picker.getCalendarView().setSelectionInterval(date, date);
        assertSynchAll(picker, date);
    }
    
    /**
     * Issue #559-swingX: date must be synched in all parts.
     * here: set value in editor.
     * 
     * Note: test uses a cleaned date, do same with uncleaned.
     */
    @Test
    public void testSynchAllOnEditorSetValue() {
        JXDateChooser picker = new JXDateChooser();
        Date date = XTestUtils.getStartOfToday(5);
        picker.getEditor().setValue(date);
        assertSynchAll(picker, date);
    } 
    
    /**
     * Issue #559-swingX: date must be synched in all parts.
     * here: modify value must work after changing the editor.
     * 
     * Note: this started to fail during listener cleanup.
     */
    @Test
    public void testSynchAllOnEditorSetValueAfterSetEditor() {
        JXDateChooser picker = new JXDateChooser();
        picker.setEditor(new JFormattedTextField(DateFormat.getInstance()));
        Date date = XTestUtils.getStartOfToday(5);
        picker.getEditor().setValue(date);
        assertSynchAll(picker, date);
    }

    /**
     * Issue #559-swingX: date must be synched in all parts.
     * here: set selection must work after changing the monthView.
     * 
     * Note: this started to fail during listener cleanup.
     */
    @Test
    public void testSynchAllOnSelectionChangeAfterSetMonthView() {
        JXDateChooser picker = new JXDateChooser();
        picker.setCalendarView(new JXCalendar());
        Date date = XTestUtils.getStartOfToday(5);
        picker.getCalendarView().setSelectionInterval(date, date);
        assertSynchAll(picker, date);
    }

    /**
     * Issue #559-swingX: date must be synched in all parts.
     * <p>
     * 
     * here: set selection must work after changing the monthView's selection
     * model.
     * 
     * Note: this started to fail during listener cleanup.
     */
    @Test
    public void testSynchAllOnSelectionChangeAfterSetSelectionModel() {
        JXDateChooser picker = new JXDateChooser();
        picker.getCalendarView().setSelectionModel(new DefaultDateSelectionModel());
        Date date = XTestUtils.getStartOfToday(5);
        picker.getCalendarView().setSelectionInterval(date, date);
        assertSynchAll(picker, date);
    }

    /**
     * Asserts that all date related values in the picker are synched.
     * 
     * @param picker the picker to 
     * @param date the common date
     */
    private void assertSynchAll(JXDateChooser picker, Date date) {
        assertEquals(date, picker.getEditor().getValue());
        assertEquals(date, picker.getDate());
        assertEquals(date, picker.getCalendarView().getSelectionDate());
        // @KEEP JW - currently unused, not yet sure if it's the right place
        // for checking against ripples produced by fixing #705-swingx
        Calendar cal = picker.getCalendarView().getPage();
        if (date == null) {
            cal.setTime(picker.getLinkDay());
            CalendarUtils.startOfMonth(cal);
        } else {
            cal.setTime(date);
            CalendarUtils.startOfMonth(cal);
        }
        assertEquals(cal.getTime(), picker.getCalendarView().getPage().getTime());
    } 


    /**
     * test that input of unselectable dates reverts editors value.
     */
    @Test
    public void testRejectSetValueUnselectable() {
        JXDateChooser picker = new JXDateChooser();
        Date upperBound = XTestUtils.getStartOfToday(1);
        picker.getCalendarView().setUpperBound(upperBound);
        Date future = XTestUtils.getStartOfToday(2);
        // sanity
        assertTrue(picker.getCalendarView().isUnselectableDate(future));
        Date current = picker.getDate();
        // sanity: 
        assertEquals(current, picker.getEditor().getValue());
        // set the editors value to something invalid
        picker.getEditor().setValue(future);
        // ui must not allow an invalid value in the editor
        assertEquals(current, picker.getEditor().getValue());
        // okay ..
        assertEquals(current, picker.getDate());
    }

    /**
     * PickerUI listened to editable (meant: datePicker) and resets
     * the editors property. Accidentally? Even if meant to, it's 
     * brittle because done during the notification. 
     * Changed to use dedicated listener.
     */
    @Test
    public void testSpuriousEditableListening() {
        JXDateChooser picker = new JXDateChooser();
        picker.getEditor().setEditable(false);
        // sanity - that at least the other views are uneffected
        assertTrue(picker.isEditable());
        assertTrue(picker.getCalendarView().isEnabled());
        assertFalse("Do not change the state of the sender during notification processing", 
                picker.getEditor().isEditable());
    }

    /**
     * PickerUI listened to enabled of button (meant: datePicker) and resets
     * the buttons property. 
     */
    @Test
    public void testSpuriousEnabledListening() {
        JXDateChooser picker = new JXDateChooser();
        Component button = null;
        for (int i = 0; i < picker.getComponentCount(); i++) {
            if (picker.getComponent(i) instanceof JButton) {
                button = picker.getComponent(i);
            }
        }
        if (button == null) {
            LOG.info("cannot run testEnabledListening - no button found");
            return;
        }
        button.setEnabled(false);
        // sanity - that at least the other views are uneffected
        assertTrue(picker.isEnabled());
        assertTrue(picker.getEditor().isEnabled());
        assertFalse("Do not change the state of the sender during notification processing", 
                button.isEnabled());
    }

    /**
     * Sanity during revision: be sure we don't loose the 
     * ui listening to picker property changes.
     *
     */
    @Test
    public void testDatePickerPropertyListening() {
        JXDateChooser picker = new JXDateChooser();
        picker.setEnabled(false);
        assertFalse(picker.getEditor().isEnabled());
        picker.setEditable(false);
        assertFalse(picker.getEditor().isEditable());
        picker.setToolTipText("dummy");
        assertEquals("dummy", picker.getEditor().getToolTipText());
    }
    /**
     * Issue ??-swingx: uninstallUI does not release propertyChangeListener
     * to editor. Reason is that the de-install was not done in 
     * uninstallListeners but later in uninstallComponents - at that time
     * the handler is already nulled, removing will actually create a new one.
     */
    @Test
    public void testEditorListeners() {
        JFormattedTextField field = new JFormattedTextField(DateFormat.getInstance());
        JXDateChooser picker = new JXDateChooser();
        int defaultListenerCount = field.getPropertyChangeListeners().length;
        // sanity: we added one listener ...
        assertEquals(defaultListenerCount + 1, 
                picker.getEditor().getPropertyChangeListeners().length);
        picker.getUI().uninstallUI(picker);
        assertEquals("the ui installe listener must be removed", 
                defaultListenerCount, 
                // right now we can access the editor even after uninstall
                // because the picker keeps a reference
                // TODO: after cleanup, this will be done through the ui
                picker.getEditor().getPropertyChangeListeners().length);
    }

    /**
     * Issue #551-swingX: editor value not updated after setMonthView.
     * 
     * quick&dirty fix: let the picker manually update.
     *
     */
    @Test
    public void testEditorValueOnSetMonthView() {
        JXDateChooser picker = new JXDateChooser(new Date());
        // set unselected monthView
        picker.setCalendarView(new JXCalendar());
        // sanity: picker takes it
        assertNull(picker.getDate());
        assertEquals(picker.getDate(), picker.getEditor().getValue());
        
    }
    /**
     * Issue #551-swingx: editor value not updated after setEditor. 
     * 
     * quick&dirty fix: let the picker manually update.
     * 
     * who should set it? ui-delegate when listening to editor property change?
     * or picker in setEditor?
     * 
     * Compare to JComboBox: BasicComboUI listens to editor change, does internal
     * wiring to editor and call's comboBox configureEditor with the value of the 
     * old editor.
     * 
     * 
     */
    @Test
    public void testEditorValueOnSetEditor() {
        JXDateChooser picker = new JXDateChooser();
        Object value = picker.getEditor().getValue();
        picker.setEditor(new JFormattedTextField(new DatePickerFormatter()));
        assertEquals(value, picker.getEditor().getValue());
    }
    
    /**
     * Issue #551-swingx: editor value must preserve value on LF switch.
     * 
     * This is a side-effect of picker not updating the editor's value
     * on setEditor.
     *
     * @see #testEditorValueOnSetEditor
     */
    @Test
    public void testEditorUpdateOnLF() {
        JXDateChooser picker = new JXDateChooser();
        Object date = picker.getEditor().getValue();
        picker.updateUI();
        assertEquals(date, picker.getEditor().getValue());
    }

    @Test
    public void testEditorUIResource() {
        JXDateChooser picker = new JXDateChooser();
        // this is safe: ui must install an editor and it must be of type UIResource
        assertEquals("default editor is UIResource ", true, picker.getEditor() instanceof UIResource);
        JFormattedTextField editor = new JFormattedTextField();
        picker.setEditor(editor);
        assertEquals("sanity: custom editor not UIResource ", false, picker.getEditor() instanceof UIResource);
        picker.updateUI();
        assertSame("updateUI must not touch custom editor", editor, picker.getEditor()); 
    }

    /**
     * Characterization: setting the monthview's selection model updates
     * the datePicker's date to the monthView's current
     * selection.
     * 
     * Here: model with selection.
     *
     */
    @Test
    public void testSynchAllAfterSetSelectionModelNotEmpty() {
        JXDateChooser picker = new JXDateChooser();
        Date date = XTestUtils.getStartOfToday(5);
        DateSelectionModel model = new DefaultDateSelectionModel();
        model.setSelectionInterval(date, date);
        // sanity
        assertFalse(date.equals(picker.getDate()));
        picker.getCalendarView().setSelectionModel(model);
        assertSynchAll(picker, date);
    }
    

    /**
     * Characterization: setting the monthview's selection model updates
     * the datePicker's date to the monthView's current
     * selection.
     * 
     * Here: model with empty selection.
     *
     */
    @Test
    public void testSynchAllAfterSetSelectionModelEmpty() {
        JXDateChooser picker = new JXDateChooser(new Date());
        assertNotNull(picker.getDate());
        DateSelectionModel model = new DefaultDateSelectionModel();
        assertTrue(model.isSelectionEmpty());
        picker.getCalendarView().setSelectionModel(model);
        assertSynchAll(picker, null);
    }
    
    /**
     * Characterization: setting the monthview updates
     * the datePicker's date to the monthView's current
     * selection.
     * Here: monthview with selection.
     *
     */
    @Test
    public void testSynchAllSetMonthViewWithSelection() {
        JXDateChooser picker = new JXDateChooser();
        JXCalendar monthView = new JXCalendar();
        Date date = XTestUtils.getStartOfToday(5);
        monthView.setSelectionInterval(date, date);
        // sanity
        assertFalse(date.equals(picker.getDate()));
        picker.setCalendarView(monthView);
        assertSynchAll(picker, date);
    }

    /**
     * Characterization: setting the monthview updates
     * the datePicker's date to the monthView's current
     * selection.
     * Here: monthview with empty selection.
     *
     */
    @Test
    public void testSynchAllSetMonthViewWithEmptySelection() {
        JXDateChooser picker = new JXDateChooser(new Date());
        // sanity
        assertNotNull(picker.getDate());
        JXCalendar monthView = new JXCalendar();
        Date selectedDate = monthView.getSelectionDate();
        assertNull(selectedDate);
        picker.setCalendarView(monthView);
        assertSynchAll(picker, selectedDate);
    }
    

    /**
     * PrefSize should be independent of empty/filled picker. 
     * If not, the initial size might appear kind of collapsed.
     *
     */
    @Test
    public void testPrefSizeEmptyEditor() {
        JXDateChooser picker = new JXDateChooser(new Date());
        // sanity 
        assertNotNull(picker.getDate());
        Dimension filled = picker.getPreferredSize();
        picker.setDate(null);
        Dimension empty = picker.getPreferredSize();
        assertEquals("pref width must be same for empty/filled", filled.width, empty.width);
    }
    
    @Test
    public void testDefaultConstructor() {
        JXDateChooser datePicker = new JXDateChooser();
        assertNull(datePicker.getDate());
    }

    @Test
    public void testConstructor() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        Date expectedDate = cleanupDate(calendar);
        JXDateChooser datePicker = new JXDateChooser(calendar.getTime());
        assertTrue(expectedDate.equals(datePicker.getDate()));
    }

    @Test
    public void testNullSelection() {
        JXDateChooser datePicker = new JXDateChooser();
        assertTrue(null == datePicker.getDate());
    }

    @Test
    public void testSetDate() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        Date expectedDate = cleanupDate(calendar);
        JXDateChooser datePicker = new JXDateChooser();
        datePicker.setDate(calendar.getTime());
        assertTrue(expectedDate.equals(datePicker.getDate()));
        assertTrue(expectedDate.equals(datePicker.getEditor().getValue()));

        datePicker.setDate(null);
        assertTrue(null == datePicker.getDate());
        assertTrue(null == datePicker.getEditor().getValue());
    }

 //------------------ test helpers
    
    /**
     * Returns a timezone different from the given.
     * @param defaultZone
     * @return
     */
    private TimeZone getSafeAlternativeTimeZone(TimeZone defaultZone) {
        TimeZone alternative = TimeZone.getTimeZone("GMT-6");
        // sanity
        assertNotNull(alternative);
        if (alternative.equals(defaultZone)) {
            alternative = TimeZone.getTimeZone("GMT-7");
            // paranoid ... but shit happens
            assertNotNull(alternative);
            assertFalse(alternative.equals(defaultZone));
        }
        return alternative;
    }

    private Date cleanupDate(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}