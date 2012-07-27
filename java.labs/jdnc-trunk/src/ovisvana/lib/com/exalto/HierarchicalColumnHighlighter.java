/*
 * $Id: HierarchicalColumnHighlighter.java 1049 2007-01-26 11:09:04Z ovisvana $
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

package com.exalto;

import java.awt.Color;
import java.awt.Component;


/**
 * HierarchicalColumnHighlighter
 *
 * @author Ramesh Gupta
 */
public class HierarchicalColumnHighlighter extends Highlighter {
	
	  Color backColor = new Color(0xF0, 0xF0, 0xE0);
	  Color selectionBackground = null;
	
    public HierarchicalColumnHighlighter() {
    }

    public HierarchicalColumnHighlighter(Color background, Color foreground, Color selectionBackground) {
        super(background, foreground);
        this.selectionBackground = selectionBackground;
    }

    protected Color computeBackground(Component component, ComponentAdapter adapter) {
    	
     //   System.out.println(" IN HCH compback comp class = " + component.getClass().getName());   
    	
        if (component.getClass().getName().equals("JTreeTable$TreeTableCellRenderer")) {
        	/*
        	System.out.println(" IN if tree bg = " + getBackground());  
            System.out.println(" IN if tree fg = " + getForeground());
            */ 

            if(!adapter.isSelected() && !adapter.hasFocus()) {
            
            	Color background = null;
	            if(adapter.row % 2 == 0)
	            	background = getBackground();
	            else
	            	background = backColor;
	          
	           	Color seed = background == null ? component.getBackground() : background;
	           	
	 /*           System.out.println(" seed before = " + seed); */  
	            
	   /*         System.out.println(" is sel = " +  adapter.isSelected()); 
	            System.out.println(" seed " +  seed); */ 
	        
	                return adapter.isSelected() ? computeSelectedBackground(seed) : seed;
	                
            } else if(adapter.isSelected()) {
            	
            	Color seed = getSelectionBackground();
            	
   //             return new Color((getMask() << 24) | (seed.getRGB() & 0x00FFD06D), true);
                return new Color((getMask() << 24) | (seed.getRGB() & 0x00FFD06D));
            	
            }
            
       }
        
        return null;    // don't change the background
    }

    protected Color computeBackgroundSeed(Color seed) {
/*
    	return new Color(Math.max((int)(seed.getRed()  * 0.95), 0),
                         Math.max((int)(seed.getGreen()* 0.95), 0),
                         Math.max((int)(seed.getBlue() * 0.95), 0));
*/
        return new Color((getMask() << 24) | (seed.getRGB() & 0x00FFD06D), true);
      }
   
    public int getMask() {
    	return 128;
   	}
    
    public Color getSelectionBackground() {
    	return selectionBackground;
    }

}

