/*
 * Created on 30.06.2009
 *
 */
package rasto1968.checklist;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.test.ListDataReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith (JUnit4.class)
public class CheckableListModelTest extends InteractiveTestCase {

    CheckableListModel emptyModel;
    CheckableListModel filledModel;
    
 
    @Test
    public void testNotifiesNotOnNoChange() {
        ListDataReport report = new ListDataReport(filledModel);
        filledModel.setChecked(0, false);
        assertEquals(false, filledModel.isChecked(0));
        assertEquals(0, report.getChangedEventCount());
    }

    @Test
    public void testNotifiesOnChecked() {
        ListDataReport report = new ListDataReport(filledModel);
        filledModel.setChecked(0, true);
        assertEquals(true, filledModel.isChecked(0));
        assertEquals(1, report.getChangedEventCount());
    }
    
    @Test
    public void testInitialUnchecked() {
        assertTrue("sanity: has elements", filledModel.getSize() > 0);
        for (int i = 0; i < filledModel.getSize(); i++) {
           assertFalse(filledModel.isChecked(i)); 
        }
    }
    
    @Test
    public void testEmptyModel() {
        assertEquals(0, emptyModel.getSize());
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void testEmptyModelAccessElement() {
        emptyModel.getElementAt(0);
    }
    
    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void testEmptyModelAccessChecked() {
        emptyModel.isChecked(0);
    }
    
    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void testEmptyModelSetChecked() {
        emptyModel.setChecked(0, true);
    }
    
    public static class SomeCheckableListModel extends AbstractListModel implements CheckableListModel {
        
        private ListModel model;
        private List<Boolean> selection;

        public void setModel(ListModel model) {
            // FIXME JW: listen to model changes
            this.model = model;
            selection = new ArrayList<Boolean>();
            for (int i = 0; i < getSize(); i++) {
               selection.add(Boolean.FALSE); 
            }
        }
        
        @Override
        public boolean isChecked(int index) {
            checkIndex(index);
            return selection.get(index);
        }

        @Override
        public boolean isCheckedEditable(int index) {
            return true;
        }

        @Override
        public void setChecked(int index, boolean checked) {
            if (isChecked(index) == checked) return;
            selection.set(index, checked);
            fireContentsChanged(this, index, index);
        }

        @Override
        public Object getElementAt(int index) {
            checkIndex(index);
            return model.getElementAt(index);
        }

        private void checkIndex(int index) {
            if ((index < 0) || (model == null) || (index >= getSize())) 
                throw new ArrayIndexOutOfBoundsException();
                    
        }

        @Override
        public int getSize() {
            return model != null ? model.getSize() : 0;
        }
        
    }
    
    /**
     * Factory method which creates a CheckableListModel with the indicated number of 
     * @param count number of elements
     * @return
     */
    protected CheckableListModel createCheckableListModel(int count) {
        SomeCheckableListModel someCheckableListModel = new SomeCheckableListModel();
        if (count > 0) {
            DefaultListModel model = new DefaultListModel();
            for (int i = 0; i < count; i++) {
                model.addElement(i);
            }
            someCheckableListModel.setModel(model);
        }
        return someCheckableListModel;
    }
    
    @Override
    @Before
    public void setUp() throws Exception {
        emptyModel = createCheckableListModel(0);
        filledModel = createCheckableListModel(10);
    }

    
    
}
