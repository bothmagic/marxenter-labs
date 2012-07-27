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
package org.jdesktop.swingx.currency;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.FieldView;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import org.jdesktop.swingx.JXCurrencyField;
import org.jdesktop.swingx.util.DissectedNumber;

/**
 * CurrencyFieldView - renders a currency field
 * This is with a $ on the left
 * @author Ryan Cuprak
 */
public class CurrencyFieldView extends FieldView {

    /**
     * Display buffer
     */
    private Segment displayBuffer;

    /**
     * CurrencyField instance
     */
    private JXCurrencyField currencyField;

    /**
     * Constructs a new FieldView wrapped on an element.
     * @param currencyField - currency field
     * @param elem - element
     */
    public CurrencyFieldView(JXCurrencyField currencyField, Element elem) {
        super(elem);
        this.currencyField = currencyField;
        populateContent();
    }

    /**
     * Determines the preferred span for this view along an
     * axis.
     *
     * @param axis may be either View.X_AXIS or View.Y_AXIS
     * @return   the span the view would like to be rendered into >= 0.
     *           Typically the view is told to render into the span
     *           that is returned, although there is no guarantee.
     *           The parent may choose to resize or break the view.
     */
    @Override
    public float getPreferredSpan(int axis) {
        // We aren't doing anything with the Y-AXIS
        if(axis == View.Y_AXIS) {
            return super.getPreferredSpan(axis);
        }
        return Utilities.getTabbedTextWidth(displayBuffer, getFontMetrics(), axis, this, axis);
    }


    /**
     * Gives notification that something was removed from the document
     * in a location that this view is responsible for.
     *
     * @param changes the change information from the associated document
     * @param a the current allocation of the view
     * @param f the factory to use to rebuild if the view has children
     * @see View#removeUpdate
     */
    @Override
    public void removeUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
        populateContent();
        super.removeUpdate(changes, a, f);
    }

    /**
     * Gives notification that something was inserted into the document
     * in a location that this view is responsible for.
     *
     * @param changes the change information from the associated document
     * @param shape the current allocation of the view
     * @param viewFactory the factory to use to rebuild if the view has children
     * @see View#insertUpdate
     */
    @Override
    public void insertUpdate(DocumentEvent changes, Shape shape, ViewFactory viewFactory) {
        populateContent();
        super.insertUpdate(changes,shape,viewFactory);
    }

    /**
     * Provides a mapping from the view coordinate space to the logical
     * coordinate space of the model.
     *
     * @param fx the X coordinate >= 0.0f
     * @param fy the Y coordinate >= 0.0f
     * @param a the allocated region to render into
     * @return the location within the model that best represents the
     *  given point in the view
     * @see View#viewToModel
     */
    @Override
    public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
        a = adjustAllocation(a);
        bias[0] = Position.Bias.Forward;
        int x = (int)fx;
        int y = (int)fy;
        Rectangle r = a.getBounds();
        int startOffset = getElement().getStartOffset();
        int endOffset = getElement().getEndOffset();
        // Outside of view
        if(y < r.y || x < r.x) {
            return endOffset - 1;
        } else if (y > r.y + r.height || x > r.x + r.width) {
            return endOffset - 1;
        }
        // within view
        return Utilities.getTabbedTextOffset(displayBuffer,getFontMetrics(),r.x,x,this,startOffset);
    }

     /**
     * Provides a mapping from the document model coordinate space
     * to the coordinate space of the view mapped to it.
     *
     * @param pos the position to convert >= 0
     * @param a the allocated region to render into
     * @return the bounding box of the given position
     * @exception javax.swing.text.BadLocationException  if the given position does not
     *   represent a valid location in the associated document
     * @see View#modelToView
     */
    @Override
    public Shape modelToView(int pos, Shape a, Position.Bias bias) throws BadLocationException {
        a = adjustAllocation(a);
        Rectangle r = new Rectangle(a.getBounds());
        FontMetrics fm = getFontMetrics();
        r.height = fm.getHeight();
        int oldCount = displayBuffer.count;
        displayBuffer.count = pos;
        int offset = Utilities.getTabbedTextWidth(displayBuffer,fm,0,this,getElement().getStartOffset());
        displayBuffer.count = oldCount;
        r.x += offset;
        r.width = 1;
        return r;
    }

    /**
     * Renders a line of text, suppressing whitespace at the end
     * and expanding any tabs.  This is implemented to make calls
     * to the methods <code>drawUnselectedText</code> and
     * <code>drawSelectedText</code> so that the way selected and
     * unselected text are rendered can be customized.
     *
     * @param lineIndex the line to draw >= 0
     * @param g the <code>Graphics</code> context
     * @param x the starting X position >= 0
     * @param y the starting Y position >= 0
     * @see #drawUnselectedText
     * @see #drawSelectedText
     */
    @Override
    protected void drawLine(int lineIndex, Graphics g, int x, int y) {
        Element element = this.getElement();
        int p0 = element.getStartOffset();
        int p1 = element.getEndOffset() - 1;
        int sel0 = ((JTextComponent)getContainer()).getSelectionStart();
        int sel1 = ((JTextComponent)getContainer()).getSelectionEnd();
        // Draw the text unselected
        if(p0 == p1 || sel0 == sel1) {
            try {
                // Invoked each time the caret blinks.
                drawUnselectedText(g,x,y,0,displayBuffer.array.length);
            } catch (BadLocationException e) {
                throw new Error("Can't render line: " + lineIndex);
            }
        } else {
            // Draw selected text
            try {
                drawUnselectedText(g,x,y,0,displayBuffer.array.length);
            } catch (BadLocationException e) {
                throw new Error("Can't render line: " + lineIndex);
            }
        }
    }

    /**
     * Renders the given range in the model as normal unselected
     * text.  Uses the foreground or disabled color to render the text.
     *
     * @param g the graphics context
     * @param x the starting X coordinate >= 0
     * @param y the starting Y coordinate >= 0
     * @param p0 the beginning position in the model >= 0
     * @param p1 the ending position in the model >= 0
     * @return the X location of the end of the range >= 0
     * @exception BadLocationException if the range is invalid
     */
    @Override
    protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException {
        g.setColor(Color.BLACK);
        return Utilities.drawTabbedText(displayBuffer,x,y,g,this,p0);

    }

    /**
     * Paints the field view
     * @param g - graphics context
     * @param a - shape
     */
    @Override
     public void paint(Graphics g, Shape a) {
        super.paint(g, a);
        g.drawString(currencyField.getFormat().getCurrency().getSymbol(currencyField.getLocale()),6,12);
    }

    /**
     * This method builds the buffer which will be rendered.
     */
    protected void populateContent() {
        try {
            Document document = getDocument();
            String txt = document.getText(0,document.getLength());
            DissectedNumber dn = new DissectedNumber(txt);
            int maxLength = dn.getInteger().length() + 1 + currencyField.getFormat().getMaximumFractionDigits();
            if(dn.isNegative()) {
                maxLength++;
            }
            StringBuilder builder = new StringBuilder(maxLength);
            if(dn.isNegative()) {
                builder.append("-");
            }
            builder.append(dn.getInteger());
            builder.append(currencyField.getFormat().getDecimalFormatSymbols().getMonetaryDecimalSeparator());
            builder.append(dn.getDecimal(currencyField.getFormat().getMaximumFractionDigits()));
            displayBuffer = new Segment(new char[maxLength],0,maxLength);
            System.arraycopy(builder.toString().toCharArray(),0,displayBuffer.array,0,maxLength);
        } catch (BadLocationException e) {
            throw new Error("Unable to populate the content.");
        }
    }
}