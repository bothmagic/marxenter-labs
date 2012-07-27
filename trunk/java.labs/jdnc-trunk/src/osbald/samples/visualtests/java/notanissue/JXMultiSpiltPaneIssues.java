package notanissue;

import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston (www.osbald.co.uk)
 * Date: 16-Nov-2006
 * Time: 13:58:53
 */

public class JXMultiSpiltPaneIssues {
    JTree tree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode("Root")));

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JXMultiSpiltPaneIssues();
            }
        });
    }

    public JXMultiSpiltPaneIssues() {
        JFrame frame = new JFrame(this.getClass().getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tree.setEditable(true);
        tree.setInvokesStopCellEditing(true);
        String layout = "(ROW (LEAF name=left weight=0.5) (COLUMN weight=0.5 (LEAF name=right.top weight=0.6) (LEAF name=right.bottom weight=0.4)))";
        MultiSplitLayout.Node layoutModel = MultiSplitLayout.parseModel(layout);
        final JXMultiSplitPane multiSplitPane = new JXMultiSplitPane();
        multiSplitPane.getMultiSplitLayout().setModel(layoutModel);
        multiSplitPane.add(new JScrollPane(tree), "left");
        multiSplitPane.add(new JButton("Attributes"), "right.top");
        multiSplitPane.add(new JButton("Comments"), "right.bottom");
        multiSplitPane.setPreferredSize(layoutModel.getBounds().getSize());
        multiSplitPane.getMultiSplitLayout().setFloatingDividers(false);
        multiSplitPane.getMultiSplitLayout().setFloatingDividers(true);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Tree", multiSplitPane);
        frame.getContentPane().add(tabbedPane);

        frame.setPreferredSize(new Dimension(400, 400));
        frame.pack();

        // shuffle becomes more obvious when I create a new nodes (but isnt limited to it - start editing root and move mouse around??)
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Tree");
        menu.add(new AbstractAction() {
            {
                putValue(Action.NAME, "New Node");
            }

            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tree.getModel().getRoot();
                TreePath selected = tree.getSelectionPath();
                if (selected != null) {
                    parent = (DefaultMutableTreeNode) selected.getLastPathComponent();
                }
                final DefaultMutableTreeNode child = new DefaultMutableTreeNode("New Node");
                parent.add(child);
                tree.startEditingAtPath(new TreePath(child.getPath()));
            }
        });
        menubar.add(menu);
        frame.setJMenuBar(menubar);

        frame.setVisible(true);
    }

	/*
	// one split kept creeping down (or was that the split above kept growing?)
	JPanel canvas = new JPanel();
	canvas.setBackground(Color.WHITE);
	JComponent topologyComponent = ComponentFactory.createTitledPanel("Topology", new JScrollPane(canvas));
	String layout = "(ROW (COLUMN weight=0.2 (LEAF name=left.top weight=0.5) (LEAF name=left.bottom weight=0.5)) (LEAF name=right weight=0.8))";
	MultiSplitLayout.Node layoutModel = MultiSplitLayout.parseModel(layout);
	JXMultiSplitPane multiSplitPane = new JXMultiSplitPane();
	multiSplitPane.getMultiSplitLayout().setDividerSize(2);
	multiSplitPane.setBorder(BorderFactory.createEmptyBorder(2, 3, 0, 0));
	multiSplitPane.setOpaque(false);
	multiSplitPane.getMultiSplitLayout().setModel(layoutModel);
	//multiSplitPane.add(topologyComponent, "right");
	multiSplitPane.add(findEntityComponent, "left.top");
	multiSplitPane.add(signaturesComponent, "left.bottom");
	//WTF size == zero?? multiSplitPane.setPreferredSize(layoutModel.getBounds().getSize());
	findEntityComponent.setPreferredSize(new Dimension(150, 100));   // JXMultiSplitPane sizing
	signaturesComponent.setPreferredSize(new Dimension(150, 100));   // JXMultiSplitPane sizing
	multiSplitPane.setPreferredSize(new Dimension(640, 480));        // JXMultiSplitPane sizing
	this.view = multiSplitPane;
	*/
}
