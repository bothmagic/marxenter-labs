/**
 * Copyright 2011 Cuprak Enterprise LLC.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jdesktop.swingx.decimal;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.FieldView;
import org.jdesktop.swingx.JXDecimalField;

import sun.swing.SwingUtilities2;

/**
 * Renders a formatted version of the text if the field does not have focus.
 * @author Ryan Cuprak
 */
public class DecimalFieldView extends FieldView {

    /**
     * DecimalField instance
     */
    private JXDecimalField decimalField;

    /**
     * Constructs a new FieldView wrapped on an element.
     * @param decimalField - decimal field
     * @param elem - element
     */
    public DecimalFieldView(JXDecimalField decimalField, Element elem) {
        super(elem);
        this.decimalField = decimalField;
    }

    /**
     * Override to render a formatted number when the component does not have focus.
     * @param g the graphics context
     * @param x the starting X coordinate >= 0
     * @param y the starting Y coordinate >= 0
     * @param p0 the beginning position in the model >= 0
     * @param p1 the ending position in the model >= 0
     * @return the location of the end of the range
     * @exception BadLocationException if the range is invalid
     */
    @Override
    protected int drawSelectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException {
        g.setColor(Color.black);
        if(!decimalField.hasFocus()) {
            char array[] = ((DecimalDocument)decimalField.getDocument()).getFormattedPresentation();
            x = calculateX(g,new String(array));
            return SwingUtilities2.drawChars(decimalField, g, array,0, array.length, x, y);
        } else {
            return super.drawSelectedText(g, x, y, p0, p1);
        }
    }

     /**
     * Override to render a formatted number when the component does not have focus.
     * @param g the graphics context
     * @param x the starting X coordinate >= 0
     * @param y the starting Y coordinate >= 0
     * @param p0 the beginning position in the model >= 0
     * @param p1 the ending position in the model >= 0
     * @return the location of the end of the range
     * @exception BadLocationException if the range is invalid
     */
    @Override
    protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException {
        g.setColor(Color.black);
        if(!decimalField.hasFocus()) {
            char array[] = ((DecimalDocument)decimalField.getDocument()).getFormattedPresentation();
            x = calculateX(g,new String(array));
            return SwingUtilities2.drawChars(decimalField, g, array,0, array.length, x, y);
        } else {
            return super.drawUnselectedText(g, x, y, p0, p1);
        }
    }

    /**
     * Calculates the X coordinate
     * @param g - graphics context
     * @param str - str parameter
     * @return x
     */
    public int calculateX(Graphics g, String str) {
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = decimalField.getFontMetrics(decimalField.getFont());
        int width = fm.stringWidth(str);
        return clip.x + clip.width - width;
    }

}
