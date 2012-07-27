/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package test.swing;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import test.SceneSupport;

import javax.swing.*;
import java.awt.*;

/**
 * @author David Kaspar
 */
// TODO - the JButton does not receive mouse/key events and therefore it does not react on anything - JButton is just rendered
public class JButtonWidgetTest extends Widget {

    private JButton button = new JButton ();

    public JButtonWidgetTest (Scene scene) {
        super (scene);
    }

    public JButton getButton () {
        return button;
    }

    protected Rectangle calculateClientArea () {
        return new Rectangle (button.getPreferredSize ());
    }

    protected void paintWidget () {
        button.setSize (getBounds ().getSize ());
        button.paint (getGraphics ());
    }

    public static void main (String[] args) {
        Scene scene = new Scene ();
        scene.getActions ().addAction (ActionFactory.createZoomAction ());

        JButtonWidgetTest button = new JButtonWidgetTest (scene);
        button.getButton ().setText ("My Button");
        scene.addChild (button);

        SceneSupport.show (scene);
    }

}
