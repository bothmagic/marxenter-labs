/*
 * $Id: SummaryFilterPipeline.java 839 2006-08-03 14:22:35Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.decorator;

import java.util.Comparator;

/**
 * A custom pipeline that allows us to install our own sorter
 *
 * @author Rob Stone
 */
public class SummaryFilterPipeline extends FilterPipeline {
    /**
     * See FilterPipeline
     */
    public SummaryFilterPipeline() {
        super();
    }
    
    /**
     * See FilterPipeline
     */
    public SummaryFilterPipeline(Filter[] inList) {
        super(inList);
    }
    
    /**
     * Return a custom sort controller.
     */
    @Override
    protected SortController createDefaultSortController() {
        return new SummarySorterBasedSortController();
    }
    
    protected class SummarySorterBasedSortController extends SorterBasedSortController {
        /**
         * An extension of ShuttleSorter that always excludes the last row of data
         * (which contains the summary information) from the sort.
         */
        protected class SummarySorter extends ShuttleSorter {
            private int[] toPrevious; // A shame this isn't protected in ShuttleSorter
            /**
             * See ShuttleSorter
             */
            public SummarySorter() {
                this(0, true);
            }

            /**
             * See ShuttleSorter
             */
            public SummarySorter(int col, boolean ascending) {
                super(col, ascending);
            }

            /**
             * See ShuttleSorter
             */
            public SummarySorter(int col, boolean ascending, Comparator comparator) {
                super(col, ascending, comparator);
            }

            /**
             * This wouldn't be needed if 'toPrevious' was made protected...
             */
            @Override
            protected void init() {
                toPrevious = new int[0];
            }

            /**
             * This wouldn't be needed if 'toPrevious' was made protected...
             */
            @Override
            protected void reset() {
                int inputSize = getInputSize();
                toPrevious = new int[inputSize];
                fromPrevious = new int[inputSize];
                for (int i = 0; i < inputSize; i++) {
                    toPrevious[i] = i;
                }
            }

            /**
             * This wouldn't be needed if 'toPrevious' was made protected...
             */
            @Override
            public int getSize() {
                return toPrevious.length;
            }

            /**
             * This wouldn't be needed if 'toPrevious' was made protected...
             */
            @Override
            protected int mapTowardModel(int row) {
                return toPrevious[row];
            }

            /**
             * Does pretty much the same thing as super class, but excludes the
             * last row of data from the sort.
             */
            @Override
            protected void filter() {
                sort(toPrevious.clone(), toPrevious, 0, toPrevious.length-1);
                // Generate inverse map for implementing convertRowIndexToView();
                for (int i = 0; i < toPrevious.length-1; i++) {
                    fromPrevious[toPrevious[i]] = i;
                }
            }        
        }        
        
        /**
         * Provide our own sorter.
         */
        @Override
        protected Sorter createDefaultSorter() {
            return new SummarySorter();
        }            
    }
}