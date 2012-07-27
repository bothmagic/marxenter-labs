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

package org.jdesktop.swingx.splittable;

import java.awt.Color;
import java.awt.Transparency;
import java.util.Hashtable;
import java.util.prefs.Preferences;

import javax.swing.border.Border;

public class SplitTableDefaults {

	static Hashtable<String,Color> _ccache=null;

	static public void putColor(Preferences prefs,String key,Color a) {
		prefs.putInt(key+".alpha", a.getAlpha());
		prefs.putInt(key+".red", a.getRed());
		prefs.putInt(key+".green",a.getGreen());
		prefs.putInt(key+".blue", a.getBlue());
		prefs.putInt(key+".transp", a.getTransparency());
	}
	
	static public Color getColor(Preferences prefs,String key,Color def) {
		int alpha=prefs.getInt(key+".alpha",def.getAlpha());
		int red=prefs.getInt(key+".red",def.getRed());
		int green=prefs.getInt(key+".green",def.getGreen());
		int blue=prefs.getInt(key+".blue",def.getBlue());
		int transp=prefs.getInt(key+".transp",def.getTransparency());
		Color a;
		if (transp==Transparency.OPAQUE) {
			a=new Color(red,green,blue);
		} else {
			a=new Color(red,green,blue,alpha);
		}
		return a;
	}

	public static Color getColor(String context, String key, Color c) {
		Preferences prefs=Preferences.userRoot().node(context);
		return getColor(prefs,key, c);
	}
	
	public static void putColor(String context,String key,Color c) {
		Preferences prefs=Preferences.userRoot().node(context);
		putColor(prefs,key,c);
	}
	
	static public void putColor(String k,Color c) {
		_ccache.put(k, c);
		putColor("swingx", k, c);
	}

	static public Color getColor(String k,Color c) {
		if (_ccache==null) {
			_ccache=new Hashtable<String,Color>();
		}
		Color cc=_ccache.get(k);
		if (cc==null) {
			cc=getColor("swingx", k, c);
			_ccache.put(k, cc);
		}
		
		return cc;
	}
	
	static private Color _tablePartOfBg=null;
	static public Color tablePartOfBg() {
		if (_tablePartOfBg==null) {
			_tablePartOfBg=getColor("tablePartOfBg",new Color(215,226,253));
		} 
		return _tablePartOfBg;
	}
	
	static public void tablePartOfBg(Color c) {
		putColor("tablePartOfBg",c);
		_tablePartOfBg=c;
	}

	static public Border innerCellBorder() { 
		return new DottedBorder(Color.gray,0,0,1,1);
	}
	
	static public Border topCellBorder() {
		return new DottedBorder(Color.gray,1,0,0,0);
	}
	
	static public Border leftCellBorder() {
		return new DottedBorder(Color.gray,0,1,0,0);
	}
}
