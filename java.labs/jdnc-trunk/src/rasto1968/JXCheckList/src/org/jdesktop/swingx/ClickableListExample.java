/*
 * ClickableListExample.java
 *
 * Created on 5 de enero de 2007, 19:25
 */

package org.jdesktop.swingx;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 *
 * @author  dags
 */
public class ClickableListExample extends JXFrame implements CheckListListener, ListSelectionListener {

    public ClickableListExample() {
        initComponents();

        DefaultListModel modelo = new DefaultListModel();

        // add some items to model ...
        for (int i = 1; i < 50; i++) {
            modelo.addElement("Item " + i);
        }

        clickableList1.setHighlighters(HighlighterFactory.createSimpleStriping(HighlighterFactory.CLASSIC_LINE_PRINTER));
        clickableList1.setModel(modelo);
        clickableList1.addCheckListListener(this);
        clickableList1.getSelectionModel().addListSelectionListener(this);
        clickableList1.setSingleClickCheck(true);
        pack();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jXTitledSeparator1 = new JXTitledSeparator();
        jScrollPane0 = new JScrollPane();
        clickableList1 = new JXCheckList();
        jXTitledSeparator2 = new JXTitledSeparator();
        jScrollPane1 = new JScrollPane();
        jXList1 = new JXList();
        jXTitledSeparator3 = new JXTitledSeparator();
        jScrollPane2 = new JScrollPane();
        jXList2 = new JXList();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jXTitledSeparator1.setTitle("ClickableList");

        jXTitledSeparator2.setTitle("Checked Items");

	jScrollPane0.setViewportView(clickableList1);
        jScrollPane1.setViewportView(jXList1);

        jXTitledSeparator3.setTitle("Selected Items");

        jScrollPane2.setViewportView(jXList2);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jXTitledSeparator1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(jScrollPane0, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jXTitledSeparator2, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jXTitledSeparator3, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jXTitledSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jXTitledSeparator2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jXTitledSeparator3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                    .addComponent(jScrollPane0, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JXCheckList clickableList1;
    private JScrollPane jScrollPane0;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JXList jXList1;
    private JXList jXList2;
    private JXTitledSeparator jXTitledSeparator1;
    private JXTitledSeparator jXTitledSeparator2;
    private JXTitledSeparator jXTitledSeparator3;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Set the user interface defaults
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Ignore
                }
                ClickableListExample example = new ClickableListExample();
                example.setVisible(true);
            }
        });
    }

    public void valueChanged(ListSelectionEvent e) {

        if (e.getValueIsAdjusting() == true) return;

        Object[] l3 = clickableList1.getSelectedValues();

        DefaultListModel model3 = new DefaultListModel();

        for (int j = 0; j < l3.length; j++)
            model3.addElement(l3[j]);

        jXList2.setModel(model3);
    }

    public void checkListChanged(final JXCheckList list, final int start, final int end) {
        DefaultListModel model2 = new DefaultListModel();        
        ListModel model=clickableList1.getModel();
        
        for (int j = 0; j <model.getSize(); j++) {
            if (clickableList1.isChecked(j)) {
                model2.addElement(model.getElementAt(j));
            }
        }
        jXList1.setModel(model2);
    }
}
