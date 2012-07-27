/*
 * $Id: copyright.txt,v 1.3 2004/09/03 22:20:20 bcbeck Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.decorator;

import org.apache.log4j.Logger;


public class MultiColPatternFilter extends SuperPatternFilter {
    private static final Logger LOG = Logger.getLogger(MultiColPatternFilter.class);
    private final int[]         cols;
    public MultiColPatternFilter(final int... cols) {
        super(0);
        final int numCols = cols.length;
        this.cols = new int[numCols];
        System.arraycopy(cols, 0, this.cols, 0, numCols);
    }

    @Override public boolean test(final int row) {
        for (int colIdx : cols) {
            if (adapter.isTestable(colIdx)) {
                final String  valueStr = (String) getInputValue(row, colIdx);
                final boolean ret      = testValue(valueStr);
                if (ret) {
                    return true;
                }
            } else {
                LOG.warn("column " + colIdx + " not testable");
                return false;
            }
        }
        return false;
    }
}

