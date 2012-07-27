/*
 * $Id: InspectionPaneUI.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import javax.swing.plaf.PanelUI;
import org.jdesktop.swingx.*;

/**
 * UI delegate for the JXInspectionPane component.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class InspectionPaneUI extends XComponentUI<JXInspectionPane> {
    public InspectionPaneUI() {
        super("InspectionPane");
    }
}
