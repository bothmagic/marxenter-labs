package org.jdesktop.swingx.table;

import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.JXTable;

import java.awt.Component;
import java.util.List;

/**
 * Predicate that returns true if row matches a predetermined set of row numbers
 *
 */
public class RowsKnownPredicate implements HighlightPredicate
{
    private List<Integer> matchingRows;

    private int highlightColumn;
    private int testColumn;


    public RowsKnownPredicate(List<Integer> matchingRows,int testColumn)
    {
        this(matchingRows, testColumn, -1);
    }

    public RowsKnownPredicate(List<Integer> matchingRows, int testColumn, int decorateColumn)
    {
        this.matchingRows = matchingRows;
        this.testColumn = testColumn;
        this.highlightColumn = decorateColumn;
    }

    public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
    {
        if (isHighlightCandidate(renderer, adapter))
        {
            return test(renderer, adapter);
        }
        return false;
    }

    private boolean test(Component renderer, ComponentAdapter adapter)
    {
        if (!adapter.isTestable(testColumn))
        {
            return false;
        }
        return matchingRows.contains(((JXTable)adapter.getComponent()).convertRowIndexToModel(adapter.row));

    }


    private boolean isHighlightCandidate(Component renderer, ComponentAdapter adapter)
    {
        return (matchingRows != null) && ((highlightColumn < 0) || (highlightColumn == adapter.viewToModel(adapter.column)));
    }

    /**
     * @return returns the column index to decorate (in model coordinates)
     */
    public int getHighlightColumn()
    {
        return highlightColumn;
    }

    /**
     * @return the column to use for testing (in model coordinates)
     */
    public int getTestColumn()
    {
        return testColumn;
    }
      
    public List <Integer> getMatchingRows()
    {
        return matchingRows;
    }
}
