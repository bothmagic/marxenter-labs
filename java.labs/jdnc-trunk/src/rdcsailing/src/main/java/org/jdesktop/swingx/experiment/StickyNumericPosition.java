package org.jdesktop.swingx.experiment;

import javax.swing.text.Position;

/**
 * Created by IntelliJ IDEA.
 * User: rcuprak
 * Date: 9/14/11
 * Time: 11:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class StickyNumericPosition implements Position {

    private int offset;

    public StickyNumericPosition(int offset) {
        this.offset = offset;
    }

    @Override
    public int getOffset() {
        return offset;
    }
}
