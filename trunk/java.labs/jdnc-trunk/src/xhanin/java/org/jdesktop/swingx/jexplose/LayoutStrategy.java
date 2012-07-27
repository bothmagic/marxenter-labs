/*
 * $Id: LayoutStrategy.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose;

import javax.swing.JDesktopPane;

import org.jdesktop.swingx.jexplose.core.ExploseLayout;


/**
 * A layout strategy is used to layout internal frames for the explose
 * effect. The layout should be as small as possible, and with no internal 
 * frames overlap.
 * <p>
 * Use implementations to configure you JExplose effect.
 * </p>
 * 
 * @see JExplose
 * 
 * @author Xavier Hanin
 */
public interface LayoutStrategy {

    ExploseLayout layout(JDesktopPane desktop);

}
