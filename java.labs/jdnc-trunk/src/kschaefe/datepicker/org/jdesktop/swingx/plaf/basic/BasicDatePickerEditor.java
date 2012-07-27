/*
 * $Id: BasicDatePickerEditor.java 1780 2007-10-01 15:06:54Z kschaefe $
 * 
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.JFormattedTextField;

import org.jdesktop.swingx.DatePickerEditor;

/**
 * @author Karl George Schaefer
 *
 */
public class BasicDatePickerEditor implements DatePickerEditor {
    protected BasicDatePickerFormatter formatter;
    protected JFormattedTextField editor;
    private Object oldDate;
    
    public BasicDatePickerEditor() {
        editor = createEditorComponent();
    }
    
    /**
     * {@inheritDoc}
     */
    public Component getEditorComponent() {
        return editor;
    }
    
    /**
     * @return
     */
    protected JFormattedTextField createEditorComponent() {
        formatter = new BasicDatePickerFormatter();
        JFormattedTextField editor = new JFormattedTextField(formatter);
        editor.setName("dateField");
//        editor.setColumns(UIManager.getInt("JXDatePicker.numColumns"));
//        editor.setBorder(UIManager.getBorder("JXDatePicker.border"));

        return editor;
    }

    /**
     * {@inheritDoc}
     */
    public void setDate(Object aDate) {
        if ( aDate != null )  {
            editor.setText(aDate.toString());
            
            oldDate = aDate;
        } else {
            editor.setText("");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Date getDate() {
        Object newValue = editor.getValue();
        
        if (newValue instanceof Date) {
            return (Date) newValue;
        }
        
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public void selectAll() {
        editor.selectAll();
        editor.requestFocus();
    }

    /**
     * {@inheritDoc}
     */
    public void addActionListener(ActionListener l) {
        editor.addActionListener(l);
    }

    /**
     * {@inheritDoc}
     */
    public void removeActionListener(ActionListener l) {
        editor.removeActionListener(l);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<DateFormat> getFormats() {
        return Arrays.asList(formatter.getFormats());
    }

    /**
     * {@inheritDoc}
     */
    public void setFormats(List<DateFormat> formats) {
        formatter.setFormats(formats.toArray(new DateFormat[0]));
    }
    
    public static class UIResource extends BasicDatePickerEditor
    implements javax.swing.plaf.UIResource {
    }
}
