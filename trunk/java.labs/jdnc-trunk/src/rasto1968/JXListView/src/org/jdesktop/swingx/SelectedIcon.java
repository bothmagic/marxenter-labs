/*
 * $Id: SelectedIcon.java 862 2006-08-22 09:25:35Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * Generates icons that are used when something is 'selected'.
 *
 * @author Rob Stone
 */
final class SelectedIcon
{
    // This is used to produce the 'selected' icons, they are slightly darker
    // than the normal ones.
    private static final IconFilter FILTER=new IconFilter(25);

    private static final Map<Integer, ImageIcon> selectedIcons=new HashMap<Integer, ImageIcon>();

    /*
     *  An image filter that produces darker icons. Should probably be in a
     *  Utilities type class.
     */
    private static class IconFilter extends RGBImageFilter
    {
        private int percent;

        public IconFilter(final int percent)
        {
            this.percent=percent;
            canFilterIndexColorModel=true;
        }

        @Override
        public int filterRGB(final int x, final int y, final int rgb)
        {
            int r=rgb>>16 & 0xff;
            int g=rgb>>8 & 0xff;
            int b=rgb & 0xff;

            r=Math.max(r-(r*percent/100),0);
            g=Math.max(g-(g*percent/100),0);
            b=Math.max(b-(b*percent/100),0);

            return (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
        }
    }

    /**
     * Returns a 'selected' version of the given icon. Note that the selected
     * icons are cached so that they are only created once.
     * @param icon the icon
     * @return a selected version of the icon.
     */
    public static ImageIcon get(final ImageIcon icon)
    {
        ImageIcon selectedIcon=selectedIcons.get(icon.hashCode());
        if (selectedIcon==null)
        {
            // Create a new disabled icon
            final ImageProducer prod=new FilteredImageSource(icon.getImage().getSource(), FILTER);
            selectedIcon=new ImageIcon(Toolkit.getDefaultToolkit().createImage(prod));
            selectedIcons.put(icon.hashCode(), selectedIcon);
        }
        return selectedIcon;
    }

    /**
     * Clears the cache of selected icons.
     */
    public static void clearCache()
    {
        selectedIcons.clear();
    }
}
