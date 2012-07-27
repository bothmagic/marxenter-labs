/*
 * AbstractTableImporter.java
 *
 * Created on March 3, 2005, 11:10 AM
 */

package org.jdesktop.dataset.io.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import org.xml.sax.InputSource;

/**
 *
 * @author Patrick Wright
 */
public abstract class AbstractTableImporter implements DataTableImporter {
    private DataTableImporter parentImporter;
    private InputSource inputSource;
    
    /** Creates a new instance of AbstractTableImporter */
    protected AbstractTableImporter(DataTableImporter parent) {
        this.setParentImporter(parent);
    }

    public AbstractTableImporter(DataTableImporter parent, InputSource inputSource) {
        this.setParentImporter(parent);
        this.setInputSource(inputSource);
    }

    public AbstractTableImporter(DataTableImporter parent, URL url) throws IOException {
        this(parent, new InputSource(url.openStream()));
    }

    public AbstractTableImporter(DataTableImporter parent, File file) throws IOException {
        this(parent, new InputSource(new FileInputStream(file)));
    }

    public AbstractTableImporter(DataTableImporter parent, InputStream is) {
        this(parent, new InputSource(is));
    }

    public AbstractTableImporter(DataTableImporter parent, String stringData) {
        this(parent, new InputSource(stringData));
    }

    public AbstractTableImporter(DataTableImporter parent, Reader reader) {
        this(parent, new InputSource(reader));
    }

    public void startImport() {
        getParentImporter().startImport();
    }

    public String tableName() {
        return getParentImporter().tableName();
    }

    public int columnCount() {
        return getParentImporter().columnCount();
    }

    public Iterable<String> columnNames() {
        return getParentImporter().columnNames();
    }

    public Iterable rowData() {
        return getParentImporter().rowData();
    }

    public void endImport() {
        getParentImporter().endImport();
    }

    public int errorCount() {
        return getParentImporter().errorCount();
    }

    public Iterable<String> errors() {
        return getParentImporter().errors();
    }

    public boolean hasErrors() {
        return getParentImporter().hasErrors();
    }

    protected DataTableImporter getParentImporter() {
        return parentImporter;
    }

    protected void setParentImporter(DataTableImporter parentImporter) {
        this.parentImporter = parentImporter;
    }

    protected InputSource getInputSource() {
        return inputSource;
    }

    protected void setInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }
}
