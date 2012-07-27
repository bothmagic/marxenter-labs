package org.jdesktop.swingx;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.SwingUtilities;

public class ReorderingHandlerDemo extends JFrame {
    protected void frameInit() {
        super.frameInit();
        
        setTitle("Reordering Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        DefaultListModel model = new DefaultListModel();
        for (int i = 1; i <= 10; i++) {
            model.addElement(i);
        }
        
        JList list = new JList(model);
        list.setDragEnabled(true);
        list.setTransferHandler(new ReorderingTransferHandler());
        add(list);
        
        pack();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ReorderingHandlerDemo().setVisible(true);
            }
        });
    }
}
