/*
 * Created on 01.07.2009
 *
 */
package rasto1968.checklist;

import javax.swing.DefaultListModel;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Random tests for everything in the checkable support that doesn't have a 
 * dedicated test class.
 */
@RunWith(JUnit4.class)
public class CheckableSupportTest extends InteractiveTestCase {
    
    /**
     * Be sure the handler as listener is registered once only (that is, be lenient against
     * double-config :-)
     */
    @Test
    public void testListenerCount() {
        JXList list = new JXList();
        CheckListFactory.create(list);
        int count = list.getMouseListeners().length;
        CheckListFactory.create(list);
        assertEquals(count, list.getMouseListeners().length);
    }

    /**
     * Don't double-wrap (that is, don't touch if it already is a CheckableListModel).
     */
    @Test
    public void testFactoryKeepsCheckableModel() {
        // pre-config the list with a checkable
        CheckListModel model = new CheckListModel(createListModel(0, 10));
        JXList list = CheckListFactory.create(model);
        assertSame(model, list.getModel());
    }
    /**
     * my preference: if no provider is given in the factory method, 
     * use the one already installed instead of a plain ol' LabelProvider 
     */
    @Test
    public void testFactoryKeepsRenderer() {
        JXList list = new JXList();
        ComponentProvider<?> provider = ((DefaultListRenderer) list.getWrappedCellRenderer()).getComponentProvider();
        CheckListFactory.create(list);
        ComponentProvider<?> wrapper = ((DefaultListRenderer) list.getWrappedCellRenderer()).getComponentProvider();
        assertTrue(wrapper instanceof ContainerProvider);
        assertSame(provider, ((ContainerProvider) wrapper).mainWrappee);
    }
    
    /**
     * Maybe we want to support releasing the checkable config again?
     */
    @Test
    public void testFactoryUncreate() {
        JXList list = new JXList();
        ComponentProvider<?> provider = ((DefaultListRenderer) list.getWrappedCellRenderer()).getComponentProvider();
        CheckListFactory.create(list);
//        CheckListFactory.release(list);
        assertSame(provider, ((DefaultListRenderer) list.getWrappedCellRenderer()).getComponentProvider());
        
    }
    /**
     * ActionListener must cope with null event.
     */
    @Test
    public void testHandlerActionPerformedNPE() {
        CheckListHandler.getInstance().actionPerformed(null);
    }

    private DefaultListModel createListModel(int start, int lenght) {
        DefaultListModel model = new DefaultListModel();
        for (int i = start; i < lenght; i++) {
            model.addElement(i);
        }
        return model;
    }

}
