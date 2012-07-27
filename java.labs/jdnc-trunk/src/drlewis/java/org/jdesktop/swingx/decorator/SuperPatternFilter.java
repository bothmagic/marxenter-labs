/*
 * $Id: copyright.txt,v 1.3 2004/09/03 22:20:20 bcbeck Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.decorator;

import org.apache.commons.lang.StringUtils;
import static org.jdesktop.swingx.decorator.SuperPatternFilter.MODE.LITERAL_FIND;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class SuperPatternFilter extends Filter {
    private List<Integer>       toPrevious;
    Pattern                     pattern;
    String                      filterStr;
    MODE                        mode;
    private static final String UNKOWN_MODE = "unknown mode";
    public static enum MODE {
        LITERAL_FIND, REGEX_FIND, LITERAL_MATCH, REGEX_MATCH
    }
    public SuperPatternFilter(final int col) {
        super(col);
        setFilterStr(null, LITERAL_FIND);
    }

    public boolean isFilterSetTo(final String rack, final MODE matchMode) {
        return StringUtils.equals(filterStr, rack) && mode == matchMode;
    }

    public void setFilterStr(final String filterStr, final MODE mode) {
        if (StringUtils.equals(this.filterStr, filterStr) && this.mode == mode) {
            return;
        }
        this.filterStr = filterStr;
        this.mode      = mode;
        switch (mode) {
            case LITERAL_FIND :
            case LITERAL_MATCH :
                break;
            case REGEX_FIND :
            case REGEX_MATCH :
                final String filterStr2;
                if (filterStr == null || filterStr.length() == 0) {
                    filterStr2 = ".*";
                } else {
                    filterStr2 = filterStr;
                }
                pattern = Pattern.compile(filterStr2, 0);
                break;
            default :
                throw new RuntimeException(UNKOWN_MODE);
        }
        refresh();
    }

    @Override protected void reset() {
        toPrevious.clear();
        final int inputSize = getInputSize();
        fromPrevious = new int[inputSize];
        for (int i = 0; i < inputSize; i++) {
            fromPrevious[i] = -1;
        }
    }

    @Override protected void filter() {
        final int inputSize = getInputSize();
        int       current   = 0;
        for (int i = 0; i < inputSize; i++) {
            if (test(i)) {
                toPrevious.add(i);
                fromPrevious[i] = current++;
            }
        }
    }

    public boolean test(final int row) {
        final int colIdx = getColumnIndex();
        if ( !adapter.isTestable(colIdx)) {
            return false;
        }
        return testValue((String) getInputValue(row, colIdx));
    }

    boolean testValue(final String valueStr) {
        if (valueStr == null) {
            return false;
        }
        switch (mode) {
            case LITERAL_FIND :
                if (filterStr == null || filterStr.length() == 0) {
                    return true;
                } else {
                    return valueStr.toUpperCase().contains(filterStr.toUpperCase());
                }
            case LITERAL_MATCH :
                if (filterStr == null || filterStr.length() == 0) {
                    return true;
                } else {
                    return filterStr.equals(valueStr);
                }
            case REGEX_FIND :
                return pattern.matcher(valueStr).find();
            case REGEX_MATCH :
                return pattern.matcher(valueStr).matches();
            default :
                throw new RuntimeException(UNKOWN_MODE);
        }
    }

    @Override public int getSize() {
        return toPrevious.size();
    }

    @Override protected int mapTowardModel(final int row) {
        return toPrevious.get(row);
    }

    @Override protected void init() {
        toPrevious = new ArrayList<Integer>();
    }
}

