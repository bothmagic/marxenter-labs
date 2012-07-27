/*
 * $Id: ScrollMapAddon.java 3296 2010-08-03 17:52:57Z kschaefe $
 *
 * Copyright 20102006 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.plaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.SystemColor;

import javax.swing.Icon;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;

import org.jdesktop.swingx.JXScrollMap;
import org.jdesktop.swingx.plaf.basic.BasicScrollMapUI;

/**
 * An addon for configuring a {@link JXScrollMap}.
 * 
 * @author kschaefer
 */
public class ScrollMapAddon extends AbstractComponentAddon {
    private static final Icon LAUNCH_SELECTOR_ICON = new Icon() {
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color tmpColor = g.getColor();
            g.setColor(Color.BLACK);
            g.drawRect(x + 2, y + 2, 10, 10);
            g.drawRect(x + 4, y + 5, 6, 4);
            g.setColor(tmpColor);
        }

        public int getIconWidth() {
            return 15;
        }

        public int getIconHeight() {
            return 15;
        }
    };
    
    public ScrollMapAddon() {
        super("JXScrollMap");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add(JXScrollMap.uiClassID, BasicScrollMapUI.class.getName());
        defaults.add("ScrollMap.font", UIManagerExt.getSafeFont("Label.font", new FontUIResource(
                Font.DIALOG, Font.PLAIN, 12)));
        defaults.add("ScrollMap.background", UIManagerExt.getSafeColor("Label.background",
                new ColorUIResource(SystemColor.control)));
        defaults.add("ScrollMap.foreground", UIManagerExt.getSafeColor("Label.foreground",
                new ColorUIResource(SystemColor.controlText)));
        defaults.add("ScrollMap.icon", new IconUIResource(LAUNCH_SELECTOR_ICON));
        defaults.add("ScrollMap.maxPopupSize", new DimensionUIResource(200, 200));
    }
}
