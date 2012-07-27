/* ******************************************************************************
 *
 *       Copyright 2008-2010 Hans Oesterholt-Dijkema
 *       This file is part of the JDesktop SwingX library
 *       and part of the SwingLabs project
 *
 *   SwingX is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as 
 *   published by the Free Software Foundation, either version 3 of 
 *   the License, or (at your option) any later version.
 *
 *   SwingX is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with SwingX.  If not, see <http://www.gnu.org/licenses/>.
 *   
 * ******************************************************************************/

package org.jdesktop.swingx.jxmlnote.utils;

import java.util.prefs.Preferences;

import org.jdesktop.swingx.jxmlnote.interfaces.XMLNotePreferences;

public class DefaultXMLNotePreferences implements XMLNotePreferences {

	private Preferences _prefs;
	
	public String getString(String key, String _default) {
		return _prefs.get(key,_default);
	}

	public int getInt(String key, int _default) {
		return _prefs.getInt(key, _default);
	}

	public void put(String key, String value) {
		_prefs.put(key, value);
	}

	public void put(String key, Integer value) {
		_prefs.putInt(key, value);
	}
	
	public DefaultXMLNotePreferences(Preferences prefs) {
		_prefs=prefs;
	}



}
