package org.jdesktop.swingx.table;

/**
 * @author Bryan Young
 */
public interface InteruptableFilterStrategy {
    boolean isIncluded(Object row);
    void init(AsynchronousFilter asynchronousFilter);
}
