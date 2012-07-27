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

package org.jdesktop.swingx.jxmlnote.internationalization;


/**
 * This is the default XMLNoteTranslator class that is instantiated with all XMLNote Objects.
 * Through <code>DefaultXMLNoteTranslator.setTranslator()</code> it is possible to setup a custom
 * translator, which is effective immediately for all XMLNote objects.
 * 
 * @author Hans Oesterholt
 *
 */
public class DefaultXMLNoteTranslator implements XMLNoteTranslator {
	
	static XMLNoteTranslator _trans=null;
	
	/**
	 * Sets the default translator. If null is given, it will clear the translator to its initial state.
	 * 
	 * @param x
	 */
	public static void setTranslator(XMLNoteTranslator x) {
		_trans=x;
	}
	
	public String _(String t) {
		return translate(t);
	}

	public String translate(String t) {
		if (_trans==null) {
			if (t.indexOf("menu:@") == 0) {
				return t.substring(6);
			} else if (t.indexOf("menu:") == 0) {
				return t.substring(5);
			} else {
				return t;
			}
		} else {
			return _trans.translate(t);
		}
	}
}
