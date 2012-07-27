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

package org.jdesktop.swingx.jxmlnote.report.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.jdesktop.swingx.jxmlnote.exceptions.DefaultXMLNoteErrorHandler;
import org.jdesktop.swingx.jxmlnote.exceptions.FontPathException;
import org.jdesktop.swingx.jxmlnote.internationalization.DefaultXMLNoteTranslator;
import org.jdesktop.swingx.jxmlnote.internationalization.XMLNoteTranslator;
import sun.font.FontManager;

import com.lowagie.text.pdf.FontMapper;


/**
 * This class provides a font mapper for iText that inserts all font directories from the font path
 * of the sun.font.FontMapper. If it cannot insert them, it yields a Warning via XMLNoteErrorHandler
 * and returns the default FontMapper of iText. 
 * <p>
 * This class works via a static method. The constructor is protected.  
 * 
 * @author Hans Oesterholt-Dijkema
 *
 */
public class PdfFontMapper extends MyDefaultFontMapper {
	
	Hashtable<String,Object[]> _cachedNames; 
	Hashtable<String,Integer>  _cachedCounts;
	Set<String>				   _paths;
	
	public void insertNames(Object[] names,String path) {
		super.insertNames(names,path);
		_cachedNames.put(path,names);
	}
	
	public int insertFile(File fontFile,int count) {
		String path=fontFile.getPath();
		Object[] names=_cachedNames.get(path);
		if (names!=null) {
			insertNames(names,path);
			_paths.add(path);
			count+=_cachedCounts.get(path);
			return count;
		} else {
			count=super.insertFile(fontFile, count);
			_cachedCounts.put(path, new Integer(count));
			_paths.add(path);
			return count;
		}
	}
	
	private XMLNoteTranslator	_tr;
	private static FontMapper   _fontmapper=null;
	
	/**
	 * This method creates an instance of this fontmapper, or reuses the existing one if 
	 * it has already been created. 
	 * 
	 * @return the fontmapper for iText. 
	 */
	public static synchronized FontMapper createPdfFontMapper(PdfReport rep,File fontmapcache) {
		if (_fontmapper==null) {
			_fontmapper=new PdfFontMapper(rep,fontmapcache);
		}
		return _fontmapper;
	}
	
	private String[] getFontPath() {
		String fontPath=FontManager.getFontPath(true);
		if (fontPath.indexOf(':')>=0) {
			String[] pathParts=fontPath.split(":");
			return pathParts;
		} else if (fontPath.indexOf(';')>=0) {
			String[] pathParts=fontPath.split(";");
			return pathParts;
		} else {
			return null;
		}
	}
	
	private void readInCache(File f) {
		_cachedNames.clear();
		_cachedCounts.clear();
		if (f.canRead() && f.isFile()) {
			try {
				ObjectInputStream in=new ObjectInputStream(new FileInputStream(f));
				int size=(Integer) in.readObject();
				for (int i=0;i<size;i++) {
					String path=(String) in.readObject();
					Object[] names=(Object []) in.readObject();
					_cachedNames.put(path,names);
				}
				size=(Integer) in.readObject();
				for (int i=0;i<size;i++) {
					String path=(String) in.readObject();
					Integer count=(Integer) in.readObject();
					_cachedCounts.put(path, count);
				}
				in.close();
			} catch (Exception e) {
				DefaultXMLNoteErrorHandler.exception(e);
			}
		}
	}
	
	private void writeOutCache(File f) {
		try {
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(f));
			Set<String> keys=_cachedNames.keySet();
			Iterator<String> it=keys.iterator();
			out.writeObject(keys.size());
			while (it.hasNext()) {
				String path=it.next();
				out.writeObject(path);
				Object [] names=_cachedNames.get(path);
				out.writeObject(names);
			}
			keys=_cachedCounts.keySet();
			it=keys.iterator();
			out.writeObject(keys.size());
			while (it.hasNext()) {
				String path=it.next();
				out.writeObject(path);
				Integer count=_cachedCounts.get(path);
				out.writeObject(count);
			}
			out.close();
		} catch(Exception e) {
			DefaultXMLNoteErrorHandler.exception(e);
		}
	}
	
	private void initializeFonts(PdfReport rep,File persistedCacheWithFontMapInfo) throws FontPathException {
		if (persistedCacheWithFontMapInfo!=null) { readInCache(persistedCacheWithFontMapInfo); }
		_paths.clear();
		String[] paths=getFontPath();
		if (paths==null) {
			throw new FontPathException(_tr._("There seems to be no font path configured for this platform"));
		} else {
			String prev=null; 
			if (rep!=null) {
				prev=rep.informStatus(_tr._("Initializing PDF Fonts"));
			}
			float progr=0.0f;
			float step=100.0f/((float) paths.length);
			int prevp=0;
			if (rep!=null) {
				prevp=rep.informProgress((int) progr);
			}
			for(String path : paths) {
				progr+=step;
				if (progr>100.0f) { progr=100.0f; }
				if (rep!=null) { rep.informProgress((int) progr); }
				super.insertDirectory(path);
			}
			if (rep!=null) {
				rep.informStatus(prev);
				rep.informProgress(prevp);
			}
		}
		// clear out cached fonts that have not been read in
		Set<String> cachedPaths=_cachedNames.keySet();
		Iterator<String> it=cachedPaths.iterator();
		Vector<String> clearPaths=new Vector<String>();
		while (it.hasNext()) {
			String p=it.next();
			if (!_paths.contains(p)) {
				clearPaths.add(p);
			}
		}
		it=clearPaths.iterator();
		while (it.hasNext()) {
			String p=it.next();
			_cachedNames.remove(p);
			_cachedCounts.remove(p);
		}
		// persist new cache
		if (persistedCacheWithFontMapInfo!=null) { writeOutCache(persistedCacheWithFontMapInfo); }
	}
	
	protected PdfFontMapper(PdfReport rep,File persistedCacheWithFontMapInfo) {
		_tr=new DefaultXMLNoteTranslator();
		_cachedNames=new Hashtable<String,Object[]>();
		_cachedCounts=new Hashtable<String,Integer>();
		_paths=new HashSet<String>();
		try {
			initializeFonts(rep,persistedCacheWithFontMapInfo);
		} catch (FontPathException e) {
			DefaultXMLNoteErrorHandler.warning(e, -1, e.getMessage());
		}
	}
}
