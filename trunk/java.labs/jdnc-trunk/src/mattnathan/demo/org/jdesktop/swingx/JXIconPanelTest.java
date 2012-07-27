/*
 * $Id: JXIconPanelTest.java 1982 2007-11-23 12:02:08Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import javax.swing.SwingUtilities;

import org.jdesktop.swingx.icon.TempIcon;

/**
 * Simple visual test for JXIconPanel
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 1.0
 */
@SuppressWarnings({"UtilityClassWithoutPrivateConstructor"})
public class JXIconPanelTest {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        new TestFrame("JXIconPanel Test", new JXIconPanel(new TempIcon(200, 100))).setVisible(true);
    }
}
