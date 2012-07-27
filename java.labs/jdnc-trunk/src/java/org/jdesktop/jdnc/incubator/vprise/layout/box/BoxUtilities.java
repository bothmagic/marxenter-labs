package org.jdesktop.jdnc.incubator.vprise.layout.box;

import java.awt.Dimension;
import java.util.*;
import javax.swing.*;

/**
 * A set of tools to work with box layouts more easily
 *
 * @author Shai Almog
 */
public class BoxUtilities {
    /**
     * Makes sure the given components have an identical maximum width to 
     * the largest among them
     */
    public static void spread(Collection components) {
        int maxWidth = 0;
        Iterator iter = components.iterator();
        while(iter.hasNext()) {
            Dimension size = ((JComponent)iter.next()).getMaximumSize();
            maxWidth = Math.max(size.width, maxWidth);
        }
        
        iter = components.iterator();
        while(iter.hasNext()) {
            JComponent current = (JComponent)iter.next();
            Dimension size = current.getMaximumSize();
            size = new Dimension(maxWidth, (int)size.getHeight());
            current.setMaximumSize(size);
        }
    }

    /**
     * Makes sure the given components have maximum/preferred sizes that are
     * identical to the largest in width.
     */
    public static void alignWidth(Collection components) {
        int maxWidth = 0;
        Iterator iter = components.iterator();
        while(iter.hasNext()) {
            Dimension size = ((JComponent)iter.next()).getPreferredSize();
            maxWidth = Math.max(size.width, maxWidth);
        }
        
        iter = components.iterator();
        while(iter.hasNext()) {
            JComponent current = (JComponent)iter.next();
            Dimension size = current.getPreferredSize();
            size = new Dimension(maxWidth, (int)size.getHeight());
            current.setPreferredSize(size);
            current.setMaximumSize(size);
        }
    }

    /**
     * Makes sure the given components have maximum/preferred sizes that are
     * identical to the largest in width.
     */
    public static void alignWidth(JComponent[] components) {
        int maxWidth = 0;
        for(int iter = 0 ; iter < components.length ; iter++) {
            Dimension size = components[iter].getPreferredSize();
            maxWidth = Math.max(size.width, maxWidth);
        }
        
        for(int update = 0 ; update < components.length ; update++) {
            Dimension size = components[update].getPreferredSize();
            size = new Dimension(maxWidth, (int)size.getHeight());
            components[update].setPreferredSize(size);
            components[update].setMaximumSize(size);
        }
    }

}
