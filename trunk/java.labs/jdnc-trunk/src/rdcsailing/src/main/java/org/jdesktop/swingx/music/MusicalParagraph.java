package org.jdesktop.swingx.music;

import java.awt.Graphics;
import java.awt.Shape;
import javax.swing.SizeRequirements;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;


public class MusicalParagraph extends ParagraphView {
    /**
     * Constructs a <code>ParagraphView</code> for the given element.
     *
     * @param elem the element that this view is responsible for
     */
    public MusicalParagraph(Element elem) {
        super(elem);
    }

    public View createRow() {
        return new CustomRow(getElement());
    }


    public class CustomRow extends BoxView {

        CustomRow(Element elem) {
            super(elem, View.X_AXIS);
        }

        protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {
            return baselineRequirements(axis, r);
        }

         public float getAlignment(int axis) {
             return StyleConstants.ALIGN_LEFT;

        }

        /**
         * Range represented by a row in the paragraph is only
         * a subset of the total range of the paragraph element.
         */
        public int getStartOffset() {
            int offs = Integer.MAX_VALUE;
            int n = getViewCount();
            for (int i = 0; i < n; i++) {
                View v = getView(i);
                offs = Math.min(offs, v.getStartOffset());
            }
            return offs;
        }

        public int getEndOffset() {
            int offs = 0;
            int n = getViewCount();
            for (int i = 0; i < n; i++) {
                View v = getView(i);
                offs = Math.max(offs, v.getEndOffset());
            }
            return offs;
        }

        @Override
        public void paint(Graphics g, Shape allocation) {

            System.out.println("Region: " + allocation.getBounds());

            System.out.println("Width: " + MusicalParagraph.this.getWidth());
            //g.setColor(Color.black);
            //g.drawString("Hi",100,10);
            super.paint(g, allocation);
        }
    }
}
