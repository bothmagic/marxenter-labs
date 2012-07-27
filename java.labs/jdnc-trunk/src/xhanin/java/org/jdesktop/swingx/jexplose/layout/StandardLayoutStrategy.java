/*
 * $Id: StandardLayoutStrategy.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.layout;

import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.jdesktop.swingx.jexplose.LayoutStrategy;
import org.jdesktop.swingx.jexplose.core.ExploseLayout;
import org.jdesktop.swingx.jexplose.core.Thumbnail;


/**
 * Standard and unique layout strategy.
 * 
 * @see org.jdesktop.swingx.jexplose.JExplose
 * 
 * @author Xavier Hanin
 */
public class StandardLayoutStrategy implements LayoutStrategy {
    private int _distance;
    /**
     * Customize the minimum distance between internal frames
     * with this constructor. 
     * @param distance the minimal distance between each internal
     * frame, in pixels. 50 is the default value.
     */
    public StandardLayoutStrategy(int distance) {
        _distance = distance;
    }
    /**
     * Default constructor, with a default distance of 50. 
     */
    public StandardLayoutStrategy() {
        this(50);
    }

    /**
     * Called by JExplose to layout internal frames.
     */
    public ExploseLayout layout(JDesktopPane desktop) {
        JInternalFrame[] iframes = desktop.getAllFrames();
        int[] rowsMaxHeight = new int[(int)Math.sqrt(iframes.length)+1];
        int framesPerRow = (int)(Math.sqrt(iframes.length + 1) + 0.7);
        rowsMaxHeight[0] = 0;
        
        // slots information: each iframe is put in a slot.
        // slots can welcome more than one iframe if there is enough
        // place
        int[] slotsLeft = new int[iframes.length];
        int[] slotsBottom = new int[iframes.length];
        int[] slotsWidth = new int[iframes.length];
        int[] slotsRow = new int[iframes.length];
        
        int slotsNumber = 0;
        
        int currentRow = 0;
        int rowLength = 0;
        int cx = 0, cy = 0;
        Rectangle thumbBounds;
        JInternalFrame iframe;
        Thumbnail thumb = null;
        
        boolean foundPlace;
        
        ExploseLayout layout = new ExploseLayout(desktop);
        
        for (int i = 0; i < iframes.length; i++) {
            foundPlace = false;
            iframe = iframes[i];
            thumbBounds = iframe.getBounds();
            
            // first we try to find a place between already placed elements
            for (int j = 0; j < slotsNumber; j++) {
                if (thumbBounds.width < slotsWidth[j] && slotsBottom[j] + thumbBounds.height <= rowsMaxHeight[slotsRow[j]]) {
                    thumb = layout.place(iframe, slotsLeft[j], slotsBottom[j]);
                    slotsBottom[j] += thumbBounds.height + _distance;
                    foundPlace = true;
                    break;
                }
            }
            if (!foundPlace) {
                // put it in new slot
                if (rowLength >= framesPerRow) {
                    // next line
                    cx = 0;
                    cy = rowsMaxHeight[currentRow] + _distance;
                    currentRow++;
                    rowLength = 0;
                }
                rowLength++;
                thumb = layout.place(iframe, cx, cy);
                slotsLeft[slotsNumber] = cx;
                slotsBottom[slotsNumber] = cy + thumb.height + _distance;
                slotsWidth[slotsNumber] = thumb.width;
                slotsRow[slotsNumber] = currentRow;
                slotsNumber++;
                
                cx += thumbBounds.width + _distance;
                rowsMaxHeight[currentRow] = Math.max(rowsMaxHeight[currentRow], cy + thumbBounds.height);                    
            }            
        }
        return layout;
    }

}
