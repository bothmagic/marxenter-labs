/*
 * $Id: JXDatePicker.java 2043 2007-12-17 00:58:28Z kschaefe $
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.accessibility.Accessible;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.PopupMenuListener;

import org.jdesktop.swingx.plaf.DatePickerUI;
import org.jdesktop.swingx.plaf.DatePickerAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.util.Contract;

/**
 * A component that combines a button, an editable field and a JXMonthView
 * component.  The user can select a date from the calendar component, which
 * appears when the button is pressed.  The selection from the calendar
 * component will be displayed in editable field.  Values may also be modified
 * manually by entering a date into the editable field using one of the
 * supported date formats.
 *
 * @author Joshua Outwater
 * @author Karl Schaefer
 */
public class JXDatePicker extends JComponent implements Accessible {
    /**
     * UI Class ID
     */
    public static final String uiClassID = "DatePickerUI";
    
    private static final long serialVersionUID = -8029653564472781856L;

    private boolean lightWeightPopupEnabled = JPopupMenu.getDefaultLightWeightPopupEnabled();
    
    /**
     * The editable date field that displays the date
     */
    private DatePickerEditor editor;

    /**
     * Popup that displays the month view with controls for
     * traversing/selecting dates.
     */
    private String actionCommand = "selectionChanged";
    private boolean editable;

    private Action action;
    private PropertyChangeListener actionPropertyChangeListener;
    private Date date;

    static {
        LookAndFeelAddons.contribute(new DatePickerAddon());
    }
    
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
        init();
        
        // install the controller before setting the date
        setDateInMillis(millis);
    }


    /**
     * Sets the date property. <p>
     * 
     * Does nothing if the ui vetos the new date - as might happen if
     * the code tries to set a date which is unselectable in the 
     * monthView's context. The actual value of the new Date might
     * be changed by the ui, the default implementation cleans
     * the Date by zeroing all time components. <p>
     * 
     * At all "stable" (= not editing in date input field nor 
     * in the monthView) times the date is the same in the 
     * JXMonthView, this JXDatePicker and the editor. If a new Date
     * is set, this invariant is enforce by the DatePickerUI.<p>
     * 
     * A not null default value is set on instantiation.
     * 
     * This is a bound property. 
     * 
     *  
     * @param date the new date to set.
     * @see #getDate();
     */
    public void setDate(Date date) {
        Date old = getDate();
        this.date = date;
        firePropertyChange("date", old, getDate());
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
        return date; 
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
     * 
     */
    private void init() {
        installAncestorListener();
        setOpaque(true);
        updateUI();
    }

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
     * Causes the combo box to display its popup window.
     * @see #setPopupVisible
     */
    public void showPopup() {
        setPopupVisible(true);
    }

    /** 
     * Causes the combo box to close its popup window.
     * @see #setPopupVisible
     */
    public void hidePopup() {
        setPopupVisible(false);
    }

    /**
     * Sets the visibility of the popup.
     */
    public void setPopupVisible(boolean v) {
        getUI().setPopupVisible(this, v);
    }

    protected void installAncestorListener() {
        addAncestorListener(new AncestorListener(){
                                public void ancestorAdded(AncestorEvent event){ hidePopup();}
                                public void ancestorRemoved(AncestorEvent event){ hidePopup();}
                                public void ancestorMoved(AncestorEvent event){ 
                                    if (event.getSource() != JXDatePicker.this)
                                        hidePopup();
                                }});
    }

    /** 
     * Determines the visibility of the popup.
     *
     * @return true if the popup is visible, otherwise returns false
     */
    public boolean isPopupVisible() {
        return getUI().isPopupVisible(this);
    }

    /**
     * Resets the UI property with the value from the current look and feel.
     *
     * @see UIManager#getUI
     */
    @Override
    public void updateUI() {
        setUI((DatePickerUI) LookAndFeelAddons.getUI(this, DatePickerUI.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    /**
     * Replaces the currently installed formatter and factory used by the
     * editor. These string formats are defined by the
     * <code>java.text.SimpleDateFormat</code> class.
     * 
     * @param formats zero or more not null string formats to use. Note that a 
     *    null array is allowed and resets the formatter to use the 
     *    localized default formats.
     * @throws NullPointerException any array element is null.
     * @see java.text.SimpleDateFormat
     */
    public void setFormats(String... formats) {
        DateFormat[] dateFormats = null;
        if (formats !=  null) {
            Contract.asNotNull(formats,
                    "the array of format strings must not "
                            + "must not contain null elements");
            dateFormats = new DateFormat[formats.length];
            for (int counter = formats.length - 1; counter >= 0; counter--) {
                dateFormats[counter] = new SimpleDateFormat(formats[counter]);
            }
        }
        setFormats(dateFormats);
    }

    public void setFormats(DateFormat... formats) {
        if (formats != null)
        Contract.asNotNull(formats, "the array of formats " +
                        "must not contain null elements");
        List<DateFormat> old = getFormats();
        editor.setFormats(Arrays.asList(formats));
        firePropertyChange("formats", old, getFormats());
    }

    public List<DateFormat> getFormats() {
        return editor.getFormats();
    }

    /**
     * Gets the time zone.  This is a convenience method which returns the time zone
     * of the JXMonthView being used.
     *
     * @return The <code>TimeZone</code> used by the <code>JXMonthView</code>.
     */
    public TimeZone getTimeZone() {
        //TODO
        return null;
    }

    /**
     * Sets the time zone with the given time zone value.    This is a convenience
     * method which returns the time zone of the JXMonthView being used.
     *
     * @param tz The <code>TimeZone</code>.
     */
    public void setTimeZone(TimeZone tz) {
        //TODO
    }

    /**
     * Returns the editor used by this date picker.
     * 
     * @return the formatted text field
     */
    public DatePickerEditor getEditor() {
        return editor;
    }

    /**
     * Sets the editor. <p>
     * 
     * The default is created and set by the UI delegate.
     * 
     * @param editor the formatted input.
     * @throws NullPointerException if editor is null.
     * 
     * @see #getEditor
     */
    public void setEditor(DatePickerEditor editor) {
        Contract.asNotNull(editor, "editor must not be null");
        DatePickerEditor oldEditor = editor;
        this.editor = editor;
        firePropertyChange("editor", oldEditor, editor);
    }

    /**
     * Returns an array of all the <code>PopupMenuListener</code>s added
     * to this JComboBox with addPopupMenuListener().
     *
     * @return all of the <code>PopupMenuListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public PopupMenuListener[] getPopupMenuListeners() {
        return (PopupMenuListener[])listenerList.getListeners(
                PopupMenuListener.class);
    }

    /**
     * Returns true if the {@code JXDatePicker} is editable. By default, a date
     * picker is not editable.
     * 
     * @return {@code true} if the {@code JXDatePicker} is editable;
     *         {@code false} otherwise
     */
    public boolean isEditable() {
        return editable;
    }
    
    /**
     * Determines whether the {@code JXDatePicker} field is editable. An
     * editable {@code JXDatePicker} allows the user to type into the field (or
     * other editor input area) or select an item from the calendar display,
     * after which it can be edited. A non editable {@code JXDatePicker}
     * displays the selected date in the field, but the selection cannot be
     * modified.
     * 
     * @param editable
     *                a boolean value, where true indicates that the field is
     *                editable
     * 
     * @beaninfo
     *        bound: true
     *    preferred: true
     *  description: If true, the user can type a new value in the date picker.
     */
    public void setEditable(boolean editable) {
        boolean oldEditable = isEditable();
        this.editable = editable;
        firePropertyChange("editable", oldEditable, isEditable());
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
     * <em>The Swing Connection</em></a>.
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
        firePropertyChange("lightWeightPopupEnabled", oldFlag, isLightWeightPopupEnabled());
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
        return getUI().getBaseline(this, width, height);
    }

    /**
     * Returns an array of all the <code>ActionListener</code>s added
     * to this JComboBox with addActionListener().
     *
     * @return all of the <code>ActionListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public ActionListener[] getActionListeners() {
        return (ActionListener[])listenerList.getListeners(
                ActionListener.class);
    }

    /** 
     * Returns the action command that is included in the event sent to
     * action listeners.
     *
     * @return  the string containing the "command" that is sent
     *          to action listeners.
     */
    public String getActionCommand() {
        return actionCommand;
    }

    /** 
     * Sets the action command that should be included in the event
     * sent to action listeners.
     *
     * @param aCommand  a string containing the "command" that is sent
     *                  to action listeners; the same listener can then
     *                  do different things depending on the command it
     *                  receives
     */
    public void setActionCommand(String aCommand) {
        actionCommand = aCommand;
    }

    /**
     * Adds a <code>PopupMenu</code> listener which will listen to notification
     * messages from the popup portion of the combo box. 
     * <p>
     * For all standard look and feels shipped with Java, the popup list  
     * portion of combo box is implemented as a <code>JPopupMenu</code>.
     * A custom look and feel may not implement it this way and will 
     * therefore not receive the notification.
     *
     * @param l  the <code>PopupMenuListener</code> to add
     * @since 1.4
     */
    public void addPopupMenuListener(PopupMenuListener l) {
        listenerList.add(PopupMenuListener.class,l);
    }

    /**
     * Removes a <code>PopupMenuListener</code>.
     *
     * @param l  the <code>PopupMenuListener</code> to remove
     * @see #addPopupMenuListener
     * @since 1.4
     */
    public void removePopupMenuListener(PopupMenuListener l) {
        listenerList.remove(PopupMenuListener.class,l);
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
        listenerList.add(ActionListener.class, l);
    }

    /** Removes an <code>ActionListener</code>.
    *
    * @param l  the <code>ActionListener</code> to remove
    */
   public void removeActionListener(ActionListener l) {
       if ((l != null) && (getAction() == l)) {
           setAction(null);
       } else {
           listenerList.remove(ActionListener.class, l);
       }
   }

   /**
    * Handles <code>KeyEvent</code>s, looking for the Tab key.
    * If the Tab key is found, the popup window is closed.
    *
    * @param e  the <code>KeyEvent</code> containing the keyboard
    *          key that was pressed  
    */
   public void processKeyEvent(KeyEvent e) {
       if ( e.getKeyCode() == KeyEvent.VK_TAB ) {
           hidePopup();
       }
       super.processKeyEvent(e);
   }

   /**
    * Returns a string representation of this <code>JComboBox</code>.
    * This method is intended to be used only for debugging purposes,
    * and the content and format of the returned string may vary between   
    * implementations. The returned string may be empty but may not 
    * be <code>null</code>.
    * 
    * @return  a string representation of this <code>JComboBox</code>
    */
   protected String paramString() {
       String isEditableString = (editable ? "true" : "false");
       String lightWeightPopupEnabledString = (lightWeightPopupEnabled ?
                                               "true" : "false");

       return super.paramString() +
       ",isEditable=" + isEditableString +
       ",lightWeightPopupEnabled=" + lightWeightPopupEnabledString;
   }
   
   /**
    * Sets the <code>Action</code> for the <code>ActionEvent</code> source.
    * The new <code>Action</code> replaces any previously set
    * <code>Action</code> but does not affect <code>ActionListeners</code>
    * independently added with <code>addActionListener</code>. 
    * If the <code>Action</code> is already a registered
    * <code>ActionListener</code> for the <code>ActionEvent</code> source,
    * it is not re-registered.
    * <p>
    * Setting the <code>Action</code> results in immediately changing
    * all the properties described in <a href="Action.html#buttonActions">
    * Swing Components Supporting <code>Action</code></a>.
    * Subsequently, the combobox's properties are automatically updated
    * as the <code>Action</code>'s properties change.
    * <p>
    * This method uses three other methods to set
    * and help track the <code>Action</code>'s property values.
    * It uses the <code>configurePropertiesFromAction</code> method
    * to immediately change the combobox's properties.
    * To track changes in the <code>Action</code>'s property values,
    * this method registers the <code>PropertyChangeListener</code>
    * returned by <code>createActionPropertyChangeListener</code>. The
    * default {@code PropertyChangeListener} invokes the
    * {@code actionPropertyChanged} method when a property in the
    * {@code Action} changes. 
    *
    * @param a the <code>Action</code> for the <code>JComboBox</code>,
    *                  or <code>null</code>.
    * @since 1.3
    * @see Action
    * @see #getAction
    * @see #configurePropertiesFromAction
    * @see #createActionPropertyChangeListener
    * @see #actionPropertyChanged 
    * @beaninfo
    *        bound: true
    *    attribute: visualUpdate true
    *  description: the Action instance connected with this ActionEvent source
    */
   public void setAction(Action a) {
       Action oldValue = getAction();
       if (action==null || !action.equals(a)) {
           action = a;
           if (oldValue!=null) {
               removeActionListener(oldValue);
               oldValue.removePropertyChangeListener(actionPropertyChangeListener);
               actionPropertyChangeListener = null;
           }
           configurePropertiesFromAction(action);
           if (action!=null) {         
               // Don't add if it is already a listener
               if (!isListener(ActionListener.class, action)) {
                   addActionListener(action);
               }
               // Reverse linkage:
               actionPropertyChangeListener = createActionPropertyChangeListener(action);
               action.addPropertyChangeListener(actionPropertyChangeListener);
           }
           firePropertyChange("action", oldValue, action);
       }
   }

   /**
    * Sets the properties on this combobox to match those in the specified 
    * <code>Action</code>.  Refer to <a href="Action.html#buttonActions">
    * Swing Components Supporting <code>Action</code></a> for more
    * details as to which properties this sets.
    *
    * @param a the <code>Action</code> from which to get the properties,
    *          or <code>null</code>
    * @since 1.3
    * @see Action
    * @see #setAction
    */
   protected void configurePropertiesFromAction(Action a) {
       //visibility issues
//       AbstractAction.setEnabledFromAction(this, a);
//       AbstractAction.setToolTipTextFromAction(this, a);
       setEnabled(a != null ? a.isEnabled() : true);
        setToolTipText(a != null ? (String) a
                .getValue(Action.SHORT_DESCRIPTION) : null);
        setActionCommand((a != null) ? (String) a
                .getValue(Action.ACTION_COMMAND_KEY) : null);
   }
   
   private boolean isListener(Class<?> c, ActionListener a) {
       boolean isListener = false;
       Object[] listeners = listenerList.getListenerList();
       for (int i = listeners.length-2; i>=0; i-=2) {
           if (listeners[i]==c && listeners[i+1]==a) {
                   isListener=true;
           }
       }
       return isListener;
   }

   /**
    * Creates and returns a <code>PropertyChangeListener</code> that is
    * responsible for listening for changes from the specified
    * <code>Action</code> and updating the appropriate properties.
    * <p>
    * <b>Warning:</b> If you subclass this do not create an anonymous
    * inner class.  If you do the lifetime of the combobox will be tied to
    * that of the <code>Action</code>.
    *
    * @param a the combobox's action
    * @since 1.3
    * @see Action
    * @see #setAction
    */
   protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
       //TODO
       return null;
   }

   /**
    * Returns the currently set <code>Action</code> for this
    * <code>ActionEvent</code> source, or <code>null</code> if no
    * <code>Action</code> is set.
    *
    * @return the <code>Action</code> for this <code>ActionEvent</code>
    *          source; or <code>null</code>
    * @since 1.3
    * @see Action
    * @see #setAction
    */
   public Action getAction() {
       return action;
   }

    /**
     * Fires an ActionEvent with this picker's actionCommand
     * to all listeners.
     */
    protected void fireActionPerformed() {
        fireActionPerformed(getActionCommand());
    }

    /**
     * Fires an ActionEvent with the given actionCommand
     * to all listeners.
     */
    protected void fireActionPerformed(String actionCommand) {
        ActionListener[] listeners = getListeners(ActionListener.class);
        ActionEvent e = null;

        for (ActionListener listener : listeners) {
            if (e == null) {
                e = new ActionEvent(this,
                        ActionEvent.ACTION_PERFORMED,
                        actionCommand);
            }
            listener.actionPerformed(e);
        }
    }
}
