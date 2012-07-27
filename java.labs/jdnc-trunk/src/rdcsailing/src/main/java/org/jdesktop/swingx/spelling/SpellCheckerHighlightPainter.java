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
package org.jdesktop.swingx.spelling;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.View;

/**
 * This highlighter places a red underline below words
 * that are spelled incorrectly.
 */
public class SpellCheckerHighlightPainter extends LayeredHighlighter.LayerPainter {

    /**
     * When leaf Views (such as LabelView) are rendering they should
     * call into this method. If a highlight is in the given region it will
     * be drawn immediately.
     *
     * @param g Graphics used to draw
     * @param p0 starting offset of view
     * @param p1 ending offset of view
     * @param viewBounds Bounds of View
     * @param editor JTextComponent
     * @param view View instance being rendered
     */
    @Override
    public Shape paintLayer(Graphics g, int p0, int p1, Shape viewBounds, JTextComponent editor, View view) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.RED);
        Rectangle alloc;
        if(p0 == view.getStartOffset() && p1 == view.getEndOffset()) {
            if(viewBounds instanceof Rectangle) {
                alloc = (Rectangle)viewBounds;
            } else {
                alloc = viewBounds.getBounds();
            }
        } else {
            try {
                Shape shape = view.modelToView(p0, Position.Bias.Forward,p1, Position.Bias.Backward,viewBounds);
                alloc = (shape instanceof Rectangle) ? (Rectangle)shape : shape.getBounds();
            } catch (BadLocationException e) {
                return null;
            }
        }
        FontMetrics fm = editor.getFontMetrics(editor.getFont());
        int baseline = alloc.y + alloc.height - fm.getDescent() + 1;
        float dashPattern1[] = new float[]{1.0f,1.0f};
        BasicStroke dashStroke1 = new BasicStroke(1,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0,dashPattern1,0);
        g2d.setStroke(dashStroke1);
        g.drawLine(alloc.x,baseline,alloc.x + alloc.width , baseline);

        BasicStroke dashStroke2 = new BasicStroke(1,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0,dashPattern1,1);
        g2d.setStroke(dashStroke2);
        g.drawLine(alloc.x,baseline+1,alloc.x + alloc.width,baseline +1);
        return alloc;
    }

    @Override
    public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
