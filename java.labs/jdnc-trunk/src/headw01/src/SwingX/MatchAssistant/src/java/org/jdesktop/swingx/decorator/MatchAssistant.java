/*
 * $Id: MatchAssistant.java 2273 2008-02-18 16:18:20Z headw01 $
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

package org.jdesktop.swingx.decorator;

import java.util.regex.Pattern;

import javax.swing.JComponent;

import org.jdesktop.swingx.AbstractSearchable.SearchResult;


/**
 * Interface that used to assist the {@link Searchable} in all the search capable components. 
 * Implementations of this interface will likely be specific to the concrete Searchable classes.
 *
 * @author headw01
 */
public interface MatchAssistant<T extends JComponent> {

    SearchResult findMatchAt(T component, Pattern pattern);
    SearchResult findMatchAt(T component, Pattern pattern, int row, int column);
    SearchResult findMatchAt(T component, Pattern pattern, int row, int column, boolean convertViewToModel);
}
