/*
 * Created on 23.06.2008
 *
 */
package netbeans.viewtips;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.ListModel;
import javax.swing.ToolTipManager;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.tree.ComponentTreeTableModel;

import sun.awt.AppContext;

public class ViewTipExperiments extends InteractiveTestCase {

    public static void main(String[] args) {
        ViewTipExperiments test = new ViewTipExperiments();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void interactiveJXList() throws Exception {
        ToolTipManager m;
        
        Class<ToolTipManager> clazz = ToolTipManager.class;
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
            constructor.setAccessible(true);
            ToolTipManager instance = (ToolTipManager) constructor.newInstance();
        }
        
        
        final JXTree table = new JXTree(new ComponentTreeTableModel(new JXFrame()));
        table.expandAll();
        // quick model for long values
        ListModel model = new AbstractListModel() {

            public Object getElementAt(int index) {
                return table.getPathForRow(index).getLastPathComponent();
            }

            public int getSize() {
                return table.getRowCount();
            }
            
        };
        JXList list = new JXList(model);
        Highlighter hl = new AbstractHighlighter() {

            @Override
            protected Component doHighlight(Component component,
                    ComponentAdapter adapter) {
                ((JComponent) component).setToolTipText(adapter.getString());
                return component;
            }
            
        };
        list.addHighlighter(hl);
        ViewTooltips.register(list);
        JXFrame frame = wrapWithScrollingInFrame(list, "list tooltip");
        addComponentOrientationToggle(frame);
        show(frame, 300, 300);

    }
}
