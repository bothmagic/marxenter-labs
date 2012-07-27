/*
 * Copyright (c) 2005-2009 Substance Kirill Grouchnikov. All Rights Reserved.
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
 *  o Neither the name of Substance Kirill Grouchnikov nor the names of 
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
package swingx.fontbased;

import java.awt.Insets;

import org.jdesktop.swingx.util.Contract;

/**
 * Extract from Substance.
 * 
 */
public class SizeUtils {
    /**
     * Gets the adjusted size. The basic functionality of this method is as
     * follows:
     * 
     * <ul>
     * <li>The <code>baseSize</code> parameter specifies the base value</li>
     * <li>The <code>forEachBase</code> and <code>toAdjustBy</code> specify how
     * to adjust the resulting value based on the passed <code>fontSize</code>.</li>
     * </ul>
     * 
     * For example, if you want base value to be 1.2 pixels, and have it grow by
     * 0.1 pixel for every additional pixel in the font size, call this method
     * with the following values:
     * 
     * <ul>
     * <li><code>baseSize</code> = 1.2</li>
     * <li><code>forEachBase</code> = 1</li>
     * <li><code>toAdjustBy</code> = 0.1</li>
     * </ul>
     * 
     * @param fontSize
     *            Font size.
     * @param baseSize
     *            The base value.
     * @param forEachBase
     *            Base units for computing the adjustment.
     * @param toAdjustBy
     *            Adjustment amount for computing the adjustment.
     * @return Adjusted size.
     */
    public static float getAdjustedSize(int fontSize, float baseSize,
            int forEachBase, float toAdjustBy) {
        int delta = fontSize - 11;
        if (delta <= 0)
            return baseSize;
        float result = baseSize + delta * toAdjustBy / forEachBase;
        return result;
    }

    /**
     * Gets the adjusted size. The basic functionality of this method is as
     * follows:
     * 
     * <ul>
     * <li>The <code>baseSize</code> parameter specifies the base value</li>
     * <li>The <code>forEachBase</code> and <code>toAdjustBy</code> specify how
     * to adjust the resulting value based on the passed <code>fontSize</code>.</li>
     * </ul>
     * 
     * For example, if you want base value to be 4 pixels, and have it grow by 1
     * pixel for every 3 additional pixels in the font size, call this method
     * with the following values:
     * 
     * <ul>
     * <li><code>baseSize</code> = 4</li>
     * <li><code>forEachBase</code> = 3</li>
     * <li><code>toAdjustBy</code> = 1</li>
     * </ul>
     * 
     * @param fontSize
     *            Font size.
     * @param baseSize
     *            The base value.
     * @param forEachBase
     *            Base units for computing the adjustment.
     * @param toAdjustBy
     *            Adjustment amount for computing the adjustment.
     * @param toRoundAsEven
     *            If <code>true</code>, the final value will be rounded down to
     *            the closest even value.
     * @return Adjusted size.
     */
    public static int getAdjustedSize(int fontSize, int baseSize,
            int forEachBase, int toAdjustBy, boolean toRoundAsEven) {
        int delta = fontSize - 11;
        if (delta <= 0)
            return baseSize;
        int result = baseSize + delta * toAdjustBy / forEachBase;
        if (toRoundAsEven && (result % 2 != 0))
            result--;
        return result;
    }

    /**
     * Returns the list cell renderer insets under the specified font size.
     * 
     * @param fontSize
     *            Font size.
     * @return List cell renderer insets under the specified font size.
     */
    public static Insets getListCellRendererInsets(int fontSize) {
        // Special handling to make non-editable combo boxes
        // have the same height as text components. The combo box
        // uses list cell renderer, so to compute the top and
        // bottom insets of a list cell renderer, we subtract the
        // insets of combo box from the insets of text component.
        // We also subtract the border stroke width - since the new
        // text component border appearance has a lighter "halo"
        // around the darker inner border.
        Insets textInsets = getTextBorderInsets(fontSize);
        Insets comboInsets = getComboBorderInsets(fontSize);
        int borderStroke = (int) getBorderStrokeWidth(fontSize);
        int topDelta = textInsets.top - comboInsets.top - borderStroke;
        int bottomDelta = textInsets.bottom - comboInsets.bottom - borderStroke;

        int lrInset = getAdjustedSize(fontSize, 4, 4, 1,
                false);
        return new Insets(topDelta, lrInset, bottomDelta, lrInset);
    }

    /**
     * Returns the combo box border insets under the specified font size.
     * 
     * @param fontSize
     *            Font size.
     * @return Combo box border insets under the specified font size.
     */
    public static Insets getComboBorderInsets(int fontSize) {
        // The base insets are 1,2,1,2. We add one pixel for
        // each 4 extra points in base control size.
        int tbInset = getAdjustedSize(fontSize, 1, 3, 1, false);
        int lrInset = getAdjustedSize(fontSize, 2, 3, 1, false);
        return new Insets(tbInset, lrInset, tbInset, lrInset);
    }

    /**
     * Returns the text border insets under the specified font size.
     * 
     * @param fontSize
     *            Font size.
     * @return Text border insets under the specified font size.
     */
    public static Insets getTextBorderInsets(int fontSize) {
        // if (fontSize == 11) {
        // return new Insets(4, 5, 3, 5);
        // }
        // The base insets are 3,5,4,5. We add one pixel for
        // each 3 extra points in base control size.
        int tInset = getAdjustedSize(fontSize, 3, 3, 1, false);
        int bInset = getAdjustedSize(fontSize, 4, 3, 1, false);
        if (fontSize == 11) {
            tInset = 3;
            bInset = 3;
        }
        int lrInset = getAdjustedSize(fontSize, 5, 3, 1, false);
        return new Insets(tInset, lrInset, bInset, lrInset);
    }


    /**
     * Returns the stroke width of borders under the specified font size.
     * 
     * @param fontSize
     *            Font size.
     * @return Stroke width of borders under the specified font size.
     */
    public static float getBorderStrokeWidth(int fontSize) {
        return fontSize / 10.0f;
    }

    /**
     * Returns a new instance of insets with size fields added up from given insets.
     * 
     * @param insets an array of zero or more not null insets.
     * @return Insets with all size fields added up.
     * @throws NullPointerException if the array or any of its elements is null.
     */
    public static Insets addInsets(Insets... insets) {
        Contract.asNotNull(insets, "given array must not be null and must not contain null elements");
        int top = 0;
        int left = 0;
        int bottom = 0;
        int right = 0;
        for (Insets current : insets) {
            top += current.top;
            left += current.left;
            bottom += current.bottom;
            right += current.right;
        }
        return new Insets(top, left, bottom, right);
    }
}
