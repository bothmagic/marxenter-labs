/*
 * Copyright (c) 2002-2004 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.jdesktop.jdnc.incubator.vprise.form;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Insets;
import java.awt.geom.AffineTransform;

/**
 * This is an adaptation of code by Karsten Lentzsch (hopefully the copyright
 * is reasonable seems to me it is). I fixed this code so it will work correctly
 * in a bidi setting where the shadow should drop to the opposite direction.
 * Also made some minor fixes/changes to match my style.
 *
 * @author  Karsten Lentzsch
 * @author  Shai Almog
 */
public class ShadowBorder implements Border {
    private Color shadow        = Color.GRAY;
    private Color lightShadow   = new Color(shadow.getRed(), 
                                    shadow.getGreen(), 
                                    shadow.getBlue(), 
                                    170);
    private Color lighterShadow = new Color(shadow.getRed(),
                                    shadow.getGreen(),
                                    shadow.getBlue(),
                                    70);

    public Insets getBorderInsets(Component c) { 
        if (c.getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT) {
            return new Insets(3, 1, 3, 1);
        } else {
            return new Insets(1, 1, 3, 3);
        }
    }

    public void paintBorder(Component c, Graphics graphics,
        int x, int y, int width, int height) {
        
        Graphics2D g = (Graphics2D)graphics.create(1, 1, width - 1, height - 1);
        
        
        if (c.getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT) {
            g.scale(-1, 1);
            g.translate(-width + 1, 0);
        }
        
        g.setColor(shadow);
        g.fillRect(x, y, width - 3, 1);
        g.fillRect(0, 0, 1, height - 3);
        g.fillRect(width - 3, 1, 1, height - 3);
        g.fillRect(1, height - 3, width - 3, 1);
        
        g.setColor(lightShadow);
        g.fillRect(width - 3, 0, 1, 1);
        g.fillRect(0, height - 3, 1, 1);
        g.fillRect(width - 2, 1, 1, height - 3);
        g.fillRect(1, height - 2, width - 3, 1);
        
        g.setColor(lighterShadow);
        g.fillRect(width - 2, 0, 1, 1);
        g.fillRect(0, height - 2, 1, 1);
        g.fillRect(width - 2, height - 2, 1, 1);
        g.fillRect(width - 1, 1, 1, height - 2);
        g.fillRect(1, height - 1, width - 2, 1);
    }
    
    public boolean isBorderOpaque() {
        return false;
    }
}
