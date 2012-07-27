package org.jdesktop.incubator.util;

import com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.util.Utilities;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Provides common LookAndFeel utilities.
 */

// TODO Might also want to consider some of the methods in mattnathans LookAndFeelUtilities

public abstract class LookAndFeelUtils {
    public static final EmptyBorder EMPTY_BORDER = new EmptyBorder(0, 0, 0, 0);
    public static final EmptyIcon EMPTY_ICON = new EmptyIcon();

    private LookAndFeelUtils() {
    }

    /**
     * Sets the LookAndFeel to the defined SystemLookAndFeel, but only if the user isn't
     * also trying to override the choice via the swing.defaultlaf system property.
     *
     * @throws RuntimeException unchecked exception when problems occur with LookAndFeel initialisation
     */
    public static void setDefaultLookAndFeel() throws RuntimeException {
        setLookAndFeel(UIManager.getSystemLookAndFeelClassName(), false);
    }

    /**
     * Sets the Look and Feel to the given class name.
     *
     * @param lookAndFeelClassName desired Look and Feel class name
     * @param force                true to ignore user preferred LookAndFeel preference via 'swing.defaultlaf' system property
     * @throws RuntimeException unchecked exception when problems occur with LookAndFeel initialisation
     *                          (e.g. contains a ClassNotFoundExeption if LookAndFeel Class not available).
     */
    public static void setLookAndFeel(String lookAndFeelClassName, boolean force) throws RuntimeException {
        assert lookAndFeelClassName != null;
        if (force || System.getProperty("swing.defaultlaf") == null) {
            try {
                UIManager.setLookAndFeel(lookAndFeelClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @param id ID of desired LookAndFeel class (via LookAndFeelInfo)
     * @return gets an installed LookAndFeel class name by it's ID or null if not found
     */
    public static String getLookAndFeel(String id) {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            if (id.equals(laf.getName())) {
                return laf.getClassName();
            }
        }
        return null;
    }

    /**
     * @return class name of Nimbus L&F or null if not available
     */
    public static String getNimbusLookAndFeel() {
        return getLookAndFeel("Nimbus");
    }

    /**
     * @return true if current LookAndFeel is a known Windows PLAF
     */
    public static boolean isWindowsLookAndFeel() {
        String laf = UIManager.getLookAndFeel().getClass().getName();
        return WindowsLookAndFeel.class.getName().equals(laf)
                || WindowsClassicLookAndFeel.class.getName().equals(laf)
                || "com.sun.java.swing.plaf.windows.WindowsLookAndFeel".equals(laf)
                || "com.jgoodies.looks.windows.WindowsLookAndFeel".equals(laf)
                || "org.fife.plaf.Office2003.Office2003LookAndFeel".equals(laf)
                || "org.fife.plaf.OfficeXP.OfficeXPLookAndFeel".equals(laf)
                || "org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel".equals(laf)
                || "net.java.plaf.windows.WindowsLookAndFeel".equals(laf);
    }

    /**
     * @return true if current OS and LookAndFeel is Windows Vista
     */
    public static boolean isVistaLookAndFeel() {
        return UIManager.getLookAndFeel().getID().equals("Windows")
                && System.getProperty("os.name", "").equalsIgnoreCase("Windows Vista")
                && isUsingWindowsVisualStyles();
    }

    /**
     * @return true if current LookAndFeel is using the classic theme
     */
    public static boolean isWindowsClassicTheme() {
        String laf = UIManager.getLookAndFeel().getClass().getName();
        return WindowsClassicLookAndFeel.class.getName().equals(laf)
                || !isUsingWindowsVisualStyles();
    }

    /**
     * Check if a Windows theme is active
     *
     * @return true if the VM is running Windows and the Java
     *         application is rendered using XP Visual Styles.
     */
    public static boolean isUsingWindowsVisualStyles() {
        if (Utilities.isWindows()) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Boolean themeProperty = (Boolean) toolkit.getDesktopProperty("win.xpstyle.themeActive");
            boolean themeActive = themeProperty != null && themeProperty.booleanValue();
            if (themeActive) {
                try {
                    return System.getProperty("swing.noxp") == null;
                } catch (RuntimeException e) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the name of the current Windows visual style. Looks for a property
     * "win.xpstyle.name" in UIManager and if not found it queries the
     * "win.xpstyle.colorName" desktop property instead.
     * ({@link Toolkit#getDesktopProperty(java.lang.String)})
     *
     * @return the name of the current Windows visual style if any
     */
    public static String getWindowsVisualStyle() {
        String style = UIManager.getString("win.xpstyle.name");
        if (style == null) {
            // guess the name of the current XPStyle win.xpstyle.colorName property
            // found in awt_DesktopProperties.cpp in JDK source
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            style = (String) toolkit.getDesktopProperty("win.xpstyle.colorName");
        }
        return style;
    }

    // ..because synth lafs can be tricky to work with (see #911)

    public static Border getBorder(String key) {
        return getBorder(key, EMPTY_BORDER);
    }

    public static Font getFont(String key) {
        return getFont(key, new JLabel().getFont());
    }

    public Icon getIcon(String key) {
        return getIcon(key, EMPTY_ICON);
    }

    public static Border getBorder(String key, Border defaultBorder) {
        Border border = UIManager.getBorder(key);
        return border != null ? border : defaultBorder;
    }

    /*
        getColor("Label.foreground", SystemColor.textText);
     */
    public static Color getColor(String key, Color defaultColor) {
        Color color = UIManager.getColor(key);
        return color != null ? color : defaultColor;
    }

    public static Font getFont(String key, Font defaultFont) {
        Font font = UIManager.getFont(key);
        return font != null ? font : defaultFont;
    }

    /*
        deriveFont("Label.font", new Font("SansSerif", Font.BOLD, 12), 24f);
        deriveFont("Label.font", baseFont, 0, Font.BOLD);
        deriveFont("Label.font", baseFont, 12f, Font.ITALIC, Font.BOLD);
     */
    public static Font deriveFont(String key, Font defaultFont, float size, int... styles) {
        Font font = getFont(key, defaultFont);
        if (font != null) {
            return deriveFont(font, size, styles);
        } else {
            return deriveFont(defaultFont, size, styles);
        }
    }

    public static Font deriveFont(Font font, float size, int... styles) {
        if (size > 0) {
            font = font.deriveFont(size);
        }
        if (styles != null && styles.length > 0) {
            for (int style : styles) {
                font = font.deriveFont(style);
            }
        }
        return font;
    }

    public static Icon getIcon(String key, Icon defaultIcon) {
        Icon icon = UIManager.getIcon(key);
        return icon != null ? icon : defaultIcon;
    }
}
