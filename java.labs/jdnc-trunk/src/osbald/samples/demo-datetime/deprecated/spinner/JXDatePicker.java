/*
 * $Id: JXDatePicker.java 1775 2007-09-28 09:58:49Z osbald $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventListener;
import java.util.SortedSet;
import java.util.TimeZone;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdesktop.swingx.calendar.DateSpan;
import org.jdesktop.swingx.calendar.JXMonthView;
import org.jdesktop.swingx.event.EventListenerMap;
import org.jdesktop.swingx.painter.MattePainter;
//import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;
import org.jdesktop.swingx.plaf.DatePickerUI;
import org.jdesktop.swingx.plaf.JXDatePickerAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

/**
 * A component that combines a button, an editable field and a JXMonthView
 * component.  The user can select a date from the calendar component, which
 * appears when the button is pressed.  The selection from the calendar
 * component will be displayed in editable field.  Values may also be modified
 * manually by entering a date into the editable field using one of the
 * supported date formats.
 *
 * @author Joshua Outwater
 */
public class JXDatePicker extends JComponent {

    static {
        LookAndFeelAddons.contribute(new JXDatePickerAddon());
    }

    /**
     * UI Class ID
     */
    public static final String uiClassID = "DatePickerUI";

    public static final String EDITOR = "editor";
    public static final String MONTH_VIEW = "monthView";
    public static final String DATE_IN_MILLIS = "dateInMillis";
    public static final String LINK_PANEL = "linkPanel";

    /**
     * The editable date field that displays the date
     */
    private JSpinner _dateField;

    /**
     * Popup that displays the month view with controls for
     * traversing/selecting dates.
     */
    private JPanel _linkPanel;
    private long _linkDate;
    private MessageFormat _linkFormat;
    private JXMonthView _monthView;
    private String _actionCommand = "selectionChanged";
    private boolean editable = true;
    private EventListenerMap listenerMap;
    protected boolean lightWeightPopupEnabled = JPopupMenu.getDefaultLightWeightPopupEnabled();

    private String format = "EEEEE, d MMMMM yyyy HH:mm:ss";
    /**
     * Create a new date picker using the current date as the initial
     * selection and the default abstract formatter
     * <code>JXDatePickerFormatter</code>.
     * <p/>
     * The date picker is configured with the default time zone and locale
     *
     * @see #setTimeZone
     * @see #getTimeZone
     */
    public JXDatePicker() {
        this(System.currentTimeMillis());
    }

    /**
     * Create a new date picker using the specified time as the initial
     * selection and the default abstract formatter
     * <code>JXDatePickerFormatter</code>.
     * <p/>
     * The date picker is configured with the default time zone and locale
     *
     * @param millis initial time in milliseconds
     * @see #setTimeZone
     * @see #getTimeZone
     */
    public JXDatePicker(long millis) {
        listenerMap = new EventListenerMap();
        _monthView = new JXMonthView(millis);
        Date date = new Date(millis);
        _monthView.setSelectionInterval(date, date);
        _monthView.setTraversable(true);

        _linkFormat = new MessageFormat(UIManager.getString("JXDatePicker.linkFormat"));

        _linkDate = System.currentTimeMillis();
        _linkPanel = new TodayPanel();

        updateUI();

        _dateField.setValue(_monthView.getSelection().first());
    }

    /**
     * @inheritDoc
     */
    public DatePickerUI getUI() {
        return (DatePickerUI) ui;
    }

    /**
     * Sets the L&F object that renders this component.
     *
     * @param ui UI to use for this {@code JXDatePicker}
     */
    public void setUI(DatePickerUI ui) {
        super.setUI(ui);
    }

    /**
     * Resets the UI property with the value from the current look and feel.
     *
     * @see UIManager#getUI
     */
    @Override
    public void updateUI() {
        setUI((DatePickerUI) LookAndFeelAddons.getUI(this, DatePickerUI.class));
        invalidate();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    
    public void setFormat(String format) {
        this.format = format;
        _dateField.setEditor(new JSpinner.DateEditor(_dateField,format));
    }
    
    public String getFormat(String format) {
        return format;
    }
    
    /**
     * Replaces the currently installed formatter and factory used by the
     * editor.  These string formats are defined by the
     * <code>java.text.SimpleDateFormat</code> class.
     *
     * @param formats The string formats to use.
     * @see java.text.SimpleDateFormat
     */
    public void setFormats(String... formats) {
        DateFormat[] dateFormats = new DateFormat[formats.length];
        for (int counter = formats.length - 1; counter >= 0; counter--) {
            dateFormats[counter] = new SimpleDateFormat(formats[counter]);
        }
        setFormats(dateFormats);
    }

    /**
     * Replaces the currently installed formatter and factory used by the
     * editor.
     *
     * @param formats The date formats to use.
     */
    public void setFormats(DateFormat... formats) {
//        _dateField.setFormatterFactory(new DefaultFormatterFactory(
//                new JXDatePickerFormatter(formats)));
    }

    /**
     * Returns an array of the formats used by the installed formatter
     * if it is a subclass of <code>JXDatePickerFormatter<code>.
     * <code>javax.swing.JFormattedTextField.AbstractFormatter</code>
     * and <code>javax.swing.text.DefaultFormatter</code> do not have
     * support for accessing the formats used.
     *
     * @return array of formats or null if unavailable.
     */
    public DateFormat[] getFormats() {
        // Dig this out from the factory, if possible, otherwise return null.
//        AbstractFormatterFactory factory = _dateField.getFormatterFactory();
//        if (factory != null) {
//            AbstractFormatter formatter = factory.getFormatter(_dateField);
//            if (formatter instanceof JXDatePickerFormatter) {
//                return ((JXDatePickerFormatter) formatter).getFormats();
//            }
//        }
        return null;
    }

    /**
     * Set the currently selected date.  If the date is <code>null</code> the selection is cleared.
     *
     * @param date date
     */
    public void setDate(Date date) {
        SortedSet<Date> selection = _monthView.getSelection();
        Date selectedDate = selection.isEmpty() ? null : selection.first();

        // Only set the date if the value was null and the current selection is not null or
        // the value is not null and is not equal to the current selection.
        if (date == null && selectedDate != date) {
            _monthView.clearSelection();
            getEditor().setValue(date);
        } else if (date != null && !date.equals(selectedDate)) {
            _monthView.setSelectionInterval(date, date);
            getEditor().setValue(date);
        }
    }

    /**
     * Set the currently selected date.
     *
     * @param millis milliseconds
     */
    public void setDateInMillis(long millis) {
        setDate(new Date(millis));
    }

    /**
     * Returns the currently selected date.
     *
     * @return Date
     */
    public Date getDate() {
        SortedSet<Date> selection = _monthView.getSelection();
        return selection.isEmpty() ? null : selection.first();
    }

    /**
     * Returns the currently selected date in milliseconds.
     *
     * @return the date in milliseconds, -1 if there is no selection.
     */
    public long getDateInMillis() {
        long result = -1;
        Date selection = getDate();
        if (selection != null) {
            result = selection.getTime();
        }
        return result;
    }

    /**
     * Return the <code>JXMonthView</code> used in the popup to
     * select dates from.
     *
     * @return the month view component
     */
    public JXMonthView getMonthView() {
        return _monthView;
    }

    /**
     * Set the component to use the specified JXMonthView.  If the new JXMonthView
     * is configured to a different time zone it will affect the time zone of this
     * component.
     *
     * @param monthView month view comopnent
     * @see #setTimeZone
     * @see #getTimeZone
     */
    public void setMonthView(JXMonthView monthView) {
        JXMonthView oldMonthView = _monthView;
        _monthView = monthView;
        firePropertyChange(MONTH_VIEW, oldMonthView, _monthView);
    }

    /**
     * Gets the time zone.  This is a convenience method which returns the time zone
     * of the JXMonthView being used.
     *
     * @return The <code>TimeZone</code> used by the <code>JXMonthView</code>.
     */
    public TimeZone getTimeZone() {
        return _monthView.getTimeZone();
    }

    /**
     * Sets the time zone with the given time zone value.    This is a convenience
     * method which returns the time zone of the JXMonthView being used.
     *
     * @param tz The <code>TimeZone</code>.
     */
    public void setTimeZone(TimeZone tz) {
        _monthView.setTimeZone(tz);

    }

    public long getLinkDate() {
        return _linkDate;
    }

    /**
     * Set the date the link will use and the string defining a MessageFormat
     * to format the link.  If no valid date is in the editor when the popup
     * is displayed the popup will focus on the month the linkDate is in.  Calling
     * this method will replace the currently installed linkPanel and install
     * a new one with the requested date and format.
     *
     * @param linkDate         Date in milliseconds
     * @param linkFormatString String used to format the link
     * @see java.text.MessageFormat
     */
    public void setLinkDate(long linkDate, String linkFormatString) {
        _linkDate = linkDate;
        _linkFormat = new MessageFormat(linkFormatString);
        setLinkPanel(new TodayPanel());
    }

    /**
     * Return the panel that is used at the bottom of the popup.  The default
     * implementation shows a link that displays the current month.
     *
     * @return The currently installed link panel
     */
    public JPanel getLinkPanel() {
        return _linkPanel;
    }

    /**
     * Set the panel that will be used at the bottom of the popup.
     *
     * @param linkPanel The new panel to install in the popup
     */
    public void setLinkPanel(JPanel linkPanel) {
        JPanel oldLinkPanel = _linkPanel;
        _linkPanel = linkPanel;
        firePropertyChange(LINK_PANEL, oldLinkPanel, _linkPanel);
    }

    /**
     * Returns the formatted text field used to edit the date selection.
     *
     * @return the formatted text field
     */
    public JSpinner getEditor() {
        return _dateField;
    }

    public void setEditor(JSpinner editor) {
         editor.setModel(new SpinnerDateModel());
         editor.setEditor(new JSpinner.DateEditor(editor,format));        
         editor.addChangeListener(new ChangeListener() {
             public void stateChanged(ChangeEvent e) {

                 JSpinner temp = (JSpinner)e.getSource();
                 SpinnerDateModel temp2 = (SpinnerDateModel)temp.getModel();

                 Long date = temp2.getDate().getTime();

                 _monthView.setFirstDisplayedDate(date);
                 //_monthView.setSelectionInterval(temp2.getDate(),temp2.getDate());
                 
                 //_monthView.isSelectedDate()
             }
         });
         JSpinner oldEditor = _dateField;
        _dateField = editor;
        firePropertyChange(EDITOR, oldEditor, _dateField);
    }

    @Override
    public void setComponentOrientation(ComponentOrientation orientation) {
        super.setComponentOrientation(orientation);
        _monthView.setComponentOrientation(orientation);
    }

    /**
     * Returns true if the current value being edited is valid.
     *
     * @return true if the current value being edited is valid.
     */
    public boolean isEditValid() {
        return true;
    }

    /**
     * Forces the current value to be taken from the AbstractFormatter and
     * set as the current value. This has no effect if there is no current
     * AbstractFormatter installed.
     *
     * @throws java.text.ParseException Throws parse exception if the date
     *                                  can not be parsed.
     */
    public void commitEdit() throws ParseException {
        _dateField.commitEdit();

        // Reformat the value according to the formatter.
        _dateField.setValue(_dateField.getValue());

        Date date = (Date) _dateField.getValue();
        setDate(date);
    }

    public void setEditable(boolean value) {
        boolean oldEditable = isEditable();
        editable = value;
        firePropertyChange("editable", oldEditable, editable);
        if (editable != oldEditable) {
            repaint();
        }
    }

    public boolean isEditable() {
        return editable;
    }

    /**
     * Returns the font that is associated with the editor of this date picker.
     */
    @Override
    public Font getFont() {
        return getEditor().getFont();
    }

    /**
     * Set the font for the editor associated with this date picker.
     */
    @Override
    public void setFont(final Font font) {
        getEditor().setFont(font);
    }

    /**
     * Sets the <code>lightWeightPopupEnabled</code> property, which
     * provides a hint as to whether or not a lightweight
     * <code>Component</code> should be used to contain the
     * <code>JXDatePicker</code>, versus a heavyweight
     * <code>Component</code> such as a <code>Panel</code>
     * or a <code>Window</code>.  The decision of lightweight
     * versus heavyweight is ultimately up to the
     * <code>JXDatePicker</code>.  Lightweight windows are more
     * efficient than heavyweight windows, but lightweight
     * and heavyweight components do not mix well in a GUI.
     * If your application mixes lightweight and heavyweight
     * components, you should disable lightweight popups.
     * The default value for the <code>lightWeightPopupEnabled</code>
     * property is <code>true</code>, unless otherwise specified
     * by the look and feel.  Some look and feels always use
     * heavyweight popups, no matter what the value of this property.
     * <p/>
     * See the article <a href="http://java.sun.com/products/jfc/tsc/articles/mixing/index.html">Mixing Heavy and Light Components</a>
     * on <a href="http://java.sun.com/products/jfc/tsc">
     * <em>The Swing Connection</em></a>
     * This method fires a property changed event.
     *
     * @param aFlag if <code>true</code>, lightweight popups are desired
     * @beaninfo bound: true
     * expert: true
     * description: Set to <code>false</code> to require heavyweight popups.
     */
    public void setLightWeightPopupEnabled(boolean aFlag) {
        boolean oldFlag = lightWeightPopupEnabled;
        lightWeightPopupEnabled = aFlag;
        firePropertyChange("lightWeightPopupEnabled", oldFlag, lightWeightPopupEnabled);
    }

    /**
     * Gets the value of the <code>lightWeightPopupEnabled</code>
     * property.
     *
     * @return the value of the <code>lightWeightPopupEnabled</code>
     *         property
     * @see #setLightWeightPopupEnabled
     */
    public boolean isLightWeightPopupEnabled() {
        return lightWeightPopupEnabled;
    }

    /**
     * Get the baseline for the specified component, or a value less
     * than 0 if the baseline can not be determined.  The baseline is measured
     * from the top of the component.
     *
     * @param width  Width of the component to determine baseline for.
     * @param height Height of the component to determine baseline for.
     * @return baseline for the specified component
     */
    public int getBaseline(int width, int height) {
        return ((DatePickerUI) ui).getBaseline(width, height);
    }

    /**
     * Returns the string currently used to identiy fired ActionEvents.
     *
     * @return String The string used for identifying ActionEvents.
     */
    public String getActionCommand() {
        return _actionCommand;
    }

    /**
     * Sets the string used to identify fired ActionEvents.
     *
     * @param actionCommand The string used for identifying ActionEvents.
     */
    public void setActionCommand(String actionCommand) {
        _actionCommand = actionCommand;
    }

    /**
     * Adds an ActionListener.
     * <p/>
     * The ActionListener will receive an ActionEvent when a selection has
     * been made.
     *
     * @param l The ActionListener that is to be notified
     */
    public void addActionListener(ActionListener l) {
        listenerMap.add(ActionListener.class, l);
    }

    /**
     * Removes an ActionListener.
     *
     * @param l The action listener to remove.
     */
    public void removeActionListener(ActionListener l) {
        listenerMap.remove(ActionListener.class, l);
    }

    @Override
    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        java.util.List<T> listeners = listenerMap.getListeners(listenerType);
        T[] result;
        if (!listeners.isEmpty()) {
            //noinspection unchecked
            result = (T[]) java.lang.reflect.Array.newInstance(listenerType, listeners.size());
            result = listeners.toArray(result);
        } else {
            result = super.getListeners(listenerType);
        }
        return result;
    }

    /**
     * Fires an ActionEvent to all listeners.
     */
    protected void fireActionPerformed() {
        ActionListener[] listeners = getListeners(ActionListener.class);
        ActionEvent e = null;

        for (ActionListener listener : listeners) {
            if (e == null) {
                e = new ActionEvent(JXDatePicker.this,
                        ActionEvent.ACTION_PERFORMED,
                        _actionCommand);
            }
            listener.actionPerformed(e);
        }
    }

    public void postActionEvent() {
        fireActionPerformed();
    }

    private final class TodayPanel extends JXPanel {
        TodayPanel() {
            super(new FlowLayout());
            setBackgroundPainter(new MattePainter(new GradientPaint(0, 0, new Color(238, 238, 238), 0, 1, Color.WHITE)));
            JXHyperlink todayLink = new JXHyperlink(new TodayAction());
            Color textColor = new Color(16, 66, 104);
            todayLink.setUnclickedColor(textColor);
            todayLink.setClickedColor(textColor);
            add(todayLink);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(new Color(187, 187, 187));
            g.drawLine(0, 0, getWidth(), 0);
            g.setColor(new Color(221, 221, 221));
            g.drawLine(0, 1, getWidth(), 1);
        }

        private final class TodayAction extends AbstractAction {
            TodayAction() {
                super(_linkFormat.format(new Object[]{new Date(_linkDate)}));
            }

            public void actionPerformed(ActionEvent ae) {
                DateSpan span = new DateSpan(_linkDate, _linkDate);
                _monthView.ensureDateVisible(span.getStart());
            }
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JXDatePicker datePicker = new JXDatePicker();
                datePicker.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(
                                ((JXDatePicker) e.getSource()).getMonthView().getSelection());
                    }
                });
                frame.getContentPane().add(datePicker);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
