/*
 * $Id: DatePickerEditor.java 1773 2007-09-27 20:47:20Z kschaefe $
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
package org.jdesktop.swingx;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * The editor component used for {@code JXDatePicker} components.
 * 
 * @author Karl George Schaefer
 * @version 1.0
 */
public interface DatePickerEditor {
    /**
     * Return the component that should be added to the tree hierarchy for this
     * editor.
     */
    Component getEditorComponent();

    /**
     * Set the {@code Date} that should be edited. Cancel any editing if
     * necessary.
     */
    void setDate(Object aDate);

    /**
     * Return the edited {@code Date}.
     */
    Date getDate();
    
    /**
     * A collection of formats used by this editor.
     * 
     * @return the formats used by this editor. If this editor, does not use
     *         formats, this will return an empty list.
     */
    List<DateFormat> getFormats();
    
    /**
     * Sets the formats used by this editor. For example, a
     * {@code JFormattedTextField}-backed editor may allow text input in one or
     * more {@code DateFormat}s.
     * 
     * @param formats
     *                the formats to use. If this editor does not use formats,
     *                then setting the formats will have no effect.
     * 
     */
    void setFormats(List<DateFormat> formats);

    /**
     * Ask the editor to start editing and to select everything.
     */
    void selectAll();

    /**
     * Add an ActionListener. An action event is generated when the edited date
     * changes.
     */
    void addActionListener(ActionListener l);

    /**
     * Remove an ActionListener.
     */
    void removeActionListener(ActionListener l);
}
