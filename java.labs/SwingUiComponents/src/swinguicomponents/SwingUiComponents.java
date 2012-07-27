/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swinguicomponents;

import java.awt.Dimension;
import java.awt.dnd.DropTarget;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author marxma
 */
public class SwingUiComponents {

    private static void createAndShowUI() {
      DefaultListModel model = new DefaultListModel();
      JList sList = new JList(model);
      for (int i = 0; i < 100; i++) {
         model.addElement("String " + i);
      }

      sList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
      sList.setVisibleRowCount(-1);
      sList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
      sList.setFixedCellWidth(-1);
      sList.setDragEnabled(true);
      sList.setDropMode(DropMode.INSERT);
      
      JFrame frame = new JFrame("Foo001");
      frame.getContentPane().add(new JScrollPane(sList));
      frame.getContentPane().setPreferredSize(new Dimension(400, 300));
      
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
            createAndShowUI();
         }
      });
   }
}
