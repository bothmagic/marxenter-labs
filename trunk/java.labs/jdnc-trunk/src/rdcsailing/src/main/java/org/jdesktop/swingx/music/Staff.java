package org.jdesktop.swingx.music;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import javax.swing.text.Element;
import javax.swing.text.LabelView;

/**
 * Created by IntelliJ IDEA.
 * User: rcuprak
 * Date: 9/28/11
 * Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class Staff extends LabelView {

    /**
     * Constructs a new view wrapped on an element.
     *
     * @param elem the element
     */
    public Staff(Element elem) {
        super(elem);
    }

    @Override
    public void paint(Graphics g, Shape a) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.BLUE);
        g2d.fill(a);
    }
}
