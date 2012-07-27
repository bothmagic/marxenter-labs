/*
 * Created on 07.01.2007
 *
 */
package org.jdesktop.swingx.list;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;

public class ListExamples extends InteractiveTestCase {

    public static void main(String[] args) {
        setSystemLF(true);
        ListExamples  test = new ListExamples();
        try {
          test.runInteractiveTests();
         //   test.runInteractiveTests("interactive.*Column.*");
         //   test.runInteractiveTests("interactive.*TableHeader.*");
         //   test.runInteractiveTests("interactive.*Render.*");
//            test.runInteractiveTests("interactive.*Sort.*");
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }
    }


    public void interactiveListTwoColumns() {
        JXList list = new JXList(createListModel());
        list.setLayoutOrientation(JXList.HORIZONTAL_WRAP);
//        list.setVisibleRowCount(0);
        JXFrame frame = wrapWithScrollingInFrame(list, "List horizontal wrap");
        frame.setVisible(true);
        
    }


    private ListModel createListModel() {
        DefaultListModel l = new DefaultListModel();
        for (int i = 0; i < 20; i++) {
            l.addElement((i % 2 == 0) ? Boolean.TRUE : Boolean.FALSE);
            l.addElement(i);
        }
        // TODO Auto-generated method stub
        return l ;
    }
}
