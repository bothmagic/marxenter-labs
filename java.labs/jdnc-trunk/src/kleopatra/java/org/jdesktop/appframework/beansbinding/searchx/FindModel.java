/*
 * Created on 14.09.2007
 *
 */
package org.jdesktop.appframework.beansbinding.searchx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Pattern;

import org.jdesktop.application.Action;
import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingx.search.PatternModel;
import org.jdesktop.swingx.search.Searchable;

public class FindModel extends AbstractBean {
    
    private Searchable searchable;
    private PatternModel patternModel;
    private boolean found;

    
    public FindModel() {
        this(null);
    }
    
    public FindModel(Searchable searchable) {
        installPatternModel();
        this.searchable = searchable;
    }

//---------------- actions in business context    
    

    /**
     * Action callback for Find action.
     * Find next/previous match using current setting of direction flag.
     * 
     */
    @Action //(enabledProperty = "searchable")
    public void match() {
        doFind();
    }

    /**
     * Action callback for FindNext action.
     * Sets direction flag to forward and calls find.
     */
    @Action (enabledProperty = "searchable")
    public void findNext() {
        setBackwards(false);
        doFind();
    }
    
    /**
     * Action callback for FindPrevious action.
     * Sets direction flag to previous and calls find.
     */
    @Action (enabledProperty = "searchable")
    public void findPrevious() {
        setBackwards(true);
        doFind();
    }
    
    /**
     * Common standalone method to perform search. Used by the action callback methods 
     * for Find/FindNext/FindPrevious actions. Finds next/previous match using current 
     * setting of direction flag. Result is being reporred using showFoundMessage and 
     * showNotFoundMessage methods respectively.
     *
     * @see #match
     * @see #findNext
     * @see #findPrevious
     */
    protected void doFind() {
        if (searchable == null)
            return;
        int foundIndex = doSearch();
        boolean notFound = (foundIndex == -1) && !patternModel.isEmpty();
        if (notFound) {
            if (isWrapping()) {
                notFound = doSearch() == -1;
            }
        }
        setUnfound(notFound);
    }

    public void setUnfound(boolean notFound) {
        boolean old = isUnfound();
        this.found = notFound;
        firePropertyChange("unfound", old, isUnfound());
    }

    public boolean isUnfound() {
        return found;
    }

    /**
     * Proforms search and returns index of the next match.
     *
     * @return Index of the next match in document.
     */
    protected int doSearch() {
        int foundIndex = searchable.search(patternModel.getPattern(), 
                patternModel.getFoundIndex(), patternModel.isBackwards());
        patternModel.setFoundIndex(foundIndex);
        return patternModel.getFoundIndex();
//         first try on #236-swingx - foundIndex wrong in backwards search.
//         re-think: autoIncrement in PatternModel? 
//        return foundIndex; 
    }


//------------- pending: dialog hooks?    
    @Action
    public void apply() {
        match();
    }
    
    @Action
    public void discard() {
        // nothing to do?
    }

//------------------ delegate methods to PatternModel
    
    public boolean isSearchable() {
        return !patternModel.isEmpty();
    }
    
    public boolean isBackwards() {
        return patternModel.isBackwards();
    }

    public boolean isCaseSensitive() {
        return patternModel.isCaseSensitive();
    }

    public boolean isWrapping() {
        return patternModel.isWrapping();
    }

    public void setBackwards(boolean backwards) {
        patternModel.setBackwards(backwards);
    }

    public void setCaseSensitive(boolean caseSensitive) {
        patternModel.setCaseSensitive(caseSensitive);
    }

    public void setWrapping(boolean wrapping) {
        patternModel.setWrapping(wrapping);
    }
    
    public String getRawText() {
        return patternModel.getRawText();
    }

    public boolean isEmpty() {
        return patternModel.isEmpty();
    }

    public void setRawText(String findText) {
        patternModel.setRawText(findText);
    }

    public void setPattern(Pattern pattern) {
        if (patternModel.isIncremental()) {
            match();
        }
    }
    
    public boolean isIncremental() {
        return patternModel.isIncremental();
    }

    public void setIncremental(boolean incremental) {
        patternModel.setIncremental(incremental);
    }

    private void installPatternModel() {
        this.patternModel = new PatternModel();
        bindPatternModel();
        PropertyChangeListener l = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                routePropertyChange(evt);
                
            }};
        patternModel.addPropertyChangeListener(l);
    }
    
    
    private void bindPatternModel() {
        BindingGroup group = new BindingGroup(); 
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
               patternModel, BeanProperty.create("caseSensitive"), 
               this, BeanProperty.create("caseSensitive")
                ));
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                patternModel, BeanProperty.create("wrapping"), 
                this, BeanProperty.create("wrapping")
                 ));
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                patternModel, BeanProperty.create("backwards"), 
                this, BeanProperty.create("backwards")
                 ));
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                patternModel, BeanProperty.create("rawText"), 
                this, BeanProperty.create("rawText")
                 ));
        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                patternModel, BeanProperty.create("pattern"), 
                this, BeanProperty.create("pattern")
                 ));
        group.bind();
    }

    protected void routePropertyChange(PropertyChangeEvent evt) {
        if ("empty".equals(evt.getPropertyName())) {
            firePropertyChange("searchable", !isSearchable(), isSearchable());
        } else {
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }


}
