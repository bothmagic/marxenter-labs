/*
 * FilterPanelQuickSearcher.java
 *
 * Created on 28 de Junho de 2005, 14:34
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.jdnc.incubator.rlopes.jdncutils;

import java.awt.event.KeyAdapter;
import java.util.regex.Pattern;

import javax.swing.event.AncestorEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.jdnc.JNTable;
import org.jdesktop.swing.decorator.QuickSearcher;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;

/**
 *
 * @author Ricardo Lopes
 */
public class FilterPanelQuickSearcher extends KeyAdapter implements QuickSearcher, DocumentListener {
    
    private JNTable jnTable;
    private FilterPanel filterPanel;
    private boolean enabled;
    private int filterIndex;
    
    /** Creates a new instance of FilterPanelQuickSearcher */
    public FilterPanelQuickSearcher(JNTable lJNTable, FilterPanel lFilterPanel, int lFilterIndex) {
        jnTable = lJNTable;
        filterPanel = lFilterPanel;
        filterIndex = lFilterIndex;
        enabled = true;
    }    

    public void setEnabled(boolean lEnabled) {
        enabled = lEnabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setFilterIndex(int index) {
        filterIndex = index;
    }
    
    public int getFilterIndex() {
        return filterIndex;
    }
    
    public void clear() {
        filterPanel.setFilterString("");
        search("");
    }

    public void search(String searchString) {
        searchString = new StringBuilder(".*").append(searchString.replace("*", ".*")).append(".*").toString();
        jnTable.getTable().setFilters(new FilterPipeline(new Filter[] { new PatternFilter(searchString, Pattern.CASE_INSENSITIVE, filterIndex) }));
    }

    // Document Listener methods
    
    public void insertUpdate(DocumentEvent event) {
        search(filterPanel.getFilterString());
    }

    public void removeUpdate(DocumentEvent event) {
        search(filterPanel.getFilterString());
    }

    public void changedUpdate(DocumentEvent event) {
        search(filterPanel.getFilterString());
    }
    
    // Ancestor Listener methods
    
	public void ancestorRemoved(AncestorEvent event) {
		// do nothing, should we unhook from the JNTable or something?
	}
	
	public void ancestorMoved(AncestorEvent event) {
		// do nothing, should we unhook from the JNTable or something?
	}
	
	public void ancestorAdded(AncestorEvent event) {
		// do nothing, should we unhook from the JNTable or something?
	}    
}

