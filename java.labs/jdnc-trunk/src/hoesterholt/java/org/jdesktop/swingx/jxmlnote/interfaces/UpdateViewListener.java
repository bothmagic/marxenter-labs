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

package org.jdesktop.swingx.jxmlnote.interfaces;

import javax.swing.event.DocumentEvent;

/**
 * This listener is called when the document of an XMLNotePane has changed.
 * 
 * @author Hans Oesterholt-Dijkema
 *
 */
public interface UpdateViewListener {
	/**
	 * This function may update parts of the view if the document has changed.
	 * The documentEvent is sent along with the updateView() request. 
	 * 
	 * @param e
	 */
	public void updateView(DocumentEvent e);
}
