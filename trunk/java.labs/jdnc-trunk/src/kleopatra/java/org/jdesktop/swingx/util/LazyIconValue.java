/*
 * Created on 01.04.2009
 *
 */
package org.jdesktop.swingx.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;

/**
 * A custom IconValue which maps cell value to an icon. The value is mapped
 * to a filename with a StringValue. The icons are loaded lazyly.
 */
public class LazyIconValue implements IconValue {
    Class<?> baseClass;
    StringValue keyToFileName;
    String dir;
    String extension;
    Map<Object, Icon> iconCache;
    Icon emptyIcon;
    
    public LazyIconValue(String dir, String extension, StringValue sv, Icon emptyIcon) {
       iconCache = new HashMap<Object, Icon>(); 
       this.dir = dir;
       this.extension = extension;
       this.keyToFileName = sv;
       this.emptyIcon = emptyIcon != null ? emptyIcon : NULL_ICON; 
    }
    
    public Icon getIcon(Object value) {
        if (value == null) return null;
        String key = value.toString();
        Icon icon = iconCache.get(key);
        if (icon ==  null) {
            icon = loadIcon(key);
         }
        return icon;
    }
    
    private Icon loadIcon(String key) {
        Icon icon = loadFromResource(getKeyFromValue(key));
        if (icon == null) {
            // no luck
            icon = emptyIcon;
        }
        iconCache.put(key, icon);
        return icon;
    }
    
    private String getKeyFromValue(String key) {
        return keyToFileName == null ? key : keyToFileName.getString(key);
    }

    protected Icon loadFromResource(String name) {
        Class<?> base = baseClass != null ? baseClass : getClass();
        URL url = base.getResource(dir + name + extension);
        if (url == null) return null;
        try {
            BufferedImage image = ImageIO.read(url);
            if (image.getHeight() > 30) {
                image = GraphicsUtilities.createThumbnail(image, 30);
            }
            return new ImageIcon(image);
        } catch (IOException e) {
        }
        return null;
    }

    
}
