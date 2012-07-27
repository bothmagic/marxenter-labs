/*
 * $Id: IndexedRolloverModel.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.rollover;

import org.jdesktop.beans.PropertySupporter;
import org.jdesktop.swingx.RolloverModel;

/**
 * Provides a RolloverModel extension for indexed components like JList and
 * JTree. When representing a JTree the rollover index represents a row in the
 * tree.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 2.0
 */
public interface IndexedRolloverModel extends RolloverModel, PropertySupporter {
    /**
     * Property name for the rolloverIndex property.
     */
    public static final String ROLLOVER_INDEX_PROPERTY = "rolloverIndex";

    /**
     * Get the current rollover index or -1 if no index has the mouse over it. If
     * this model is representing a tree then the rollover index represents a
     * row.
     *
     * @return The current rollover index.
     */
    public int getRolloverIndex();





    /**
     * Set the current rollover index. The index may be -1 to represent no
     * rollover index.
     *
     * @param index The rollover index.
     */
    public void setRolloverIndex(int index);

}
