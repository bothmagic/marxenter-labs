/*
 * Created by IntelliJ IDEA.
 * User: Brent
 * Date: 06-Feb-2008
 * Time: 12:44:55
 */
package org.jdesktop.appframework.goodies.album;

import org.jdesktop.swingx.JXList;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;

/**
 * 
 * 
 * @author Chris Forker
 */
public class JXListSelectionConverter extends AbstractConverter {
    
    private final JXList table;

    public JXListSelectionConverter(final ValueModel selectionIndexHolder,
            final JXList table) {
        super(selectionIndexHolder);
        this.table = table;
    }

    public Object convertFromSubject(Object subjectValue) {
        int viewIndex = -1;
        int modelIndex = -1;

        if (subjectValue != null) {
            modelIndex = ((Integer) subjectValue).intValue();

            if (modelIndex >= 0) {
                viewIndex = table.convertIndexToView(modelIndex);
            }
        }

        return new Integer(viewIndex);
    }

    public void setValue(Object newValue) {
        int viewIndex = -1;
        int modelIndex = -1;

        if (newValue != null) {
            viewIndex = ((Integer) newValue).intValue();

            if (viewIndex >= 0) {
                modelIndex = table.convertIndexToModel(viewIndex);
            }
        }

        subject.setValue(new Integer(modelIndex));
    }
}
