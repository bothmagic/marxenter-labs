package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class JXTaskPaneTester extends JFrame {
    private static class SystemErrAction extends AbstractAction {
        public SystemErrAction(String name) {
            super(name);
        }
        
        public void actionPerformed(ActionEvent e) {
            System.err.println(getValue(NAME));
        }
        
    }
    
    private static final long serialVersionUID = 1L;

    public JXTaskPaneTester() {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel p = new JPanel(new BorderLayout());
        JTabbedPane top = new JTabbedPane();
        tbp = new JTabbedPane();
        top.addTab("TAB", tbp);
        p.add(top, BorderLayout.CENTER);
        JButton add = new JButton("Add Tab");
        add.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                addTab();
            }
        });
        p.add(add, BorderLayout.SOUTH);
        setContentPane(p);
    }

    private void addTab() {
        JXTaskPaneContainer tpc = new JXTaskPaneContainer();
        JXTaskPane tp = new JXTaskPane();
        tp.setTitle("AAAA");
        tp.add(new SystemErrAction("111"));
        tp.add(new SystemErrAction("222"));
        tp.add(new SystemErrAction("333"));
        tp.add(new SystemErrAction("444"));
        tpc.add(tp);
        
        tp = new JXTaskPane();
        tp.setTitle("BBBB");
        tp.setCollapsed(true);
        tp.add(new SystemErrAction("111"));
        tp.add(new SystemErrAction("222"));
        tp.add(new SystemErrAction("333"));
        tp.add(new SystemErrAction("444"));
        tpc.add(tp);
        
        tp = new JXTaskPane();
        tp.setTitle("CCCC");
        tp.setCollapsed(true);
        tp.add(new SystemErrAction("111"));
        tp.add(new SystemErrAction("222"));
        tp.add(new SystemErrAction("333"));
        tp.add(new SystemErrAction("444"));
        tpc.add(tp);
        
        JScrollPane scp = new JScrollPane(tpc);
        tbp.addTab("Tab " + (tbp.getComponentCount() + 1), scp);
        tbp.setSelectedIndex(tbp.getComponentCount() - 1);
    }

    private JTabbedPane tbp;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JXTaskPaneTester tester = new JXTaskPaneTester();
                tester.setSize(600, 500);
                tester.setVisible(true);
            }
            
        });
    }
}
