/*
 * Created on 09.07.2008
 *
 */
package netbeans.xoutline;

import javax.swing.tree.DefaultTreeModel;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXTree;
@SuppressWarnings("unused")
public class XOutlineModelIssues extends InteractiveTestCase {

    private XDefaultTTM treeTableModel;
    private XOutlineModel outline;
    private Object root;

    /**
     * test setting zero rowHeight with largeModel
     */
    public void testRowHeightLargeModel() {
        outline.setRowHeight(20);
        outline.setLargeModel(true);
        // not allowed for largeModel 
        outline.setRowHeight(0);
    }

    /**
     * Creates and returns a custom model from JXTree default model. The model
     * is of type DefaultTreeModel, allowing for easy insert/remove.
     * 
     * @return
     */
    private XDefaultTTM createCustomTreeTableModelFromDefault() {
        JXTree tree = new JXTree();
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        XDefaultTTM customTreeTableModel = XDefaultTTM
                .convertDefaultTreeModel(treeModel);

        return customTreeTableModel;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        treeTableModel = (XDefaultTTM) createCustomTreeTableModelFromDefault();
        outline = new DefaultXOutlineModel(treeTableModel);
        root = treeTableModel.getRoot();
        
    }
    

}
