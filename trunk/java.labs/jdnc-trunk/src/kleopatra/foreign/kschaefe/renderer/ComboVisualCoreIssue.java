/*
 * Created on 23.07.2009
 *
 */
package kschaefe.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class ComboVisualCoreIssue {
    JFrame mFrame = null;

    public ComboVisualCoreIssue() {
        JPanel panel = new JPanel();

        //----Create a colored combo box and put it in the panel.
        JComboBox combo = new JComboBox();
        combo.addItem("red");
        combo.addItem("green");
        combo.addItem("blue");
        combo.setSelectedIndex(0);
        combo.setEditable(false);
        panel.add(combo);
        combo.setRenderer(new ComboRenderer ());

        //----Create a non-colored combo box and put it in the panel.
        JComboBox combo2 = new JComboBox();
        combo2.addItem("default1");
        combo2.addItem("default2");
        combo2.setSelectedIndex(0);
        panel.add(combo2);


        //----Put the panel in the frame
        mFrame = new JFrame("ComboColor Test");
        mFrame.getContentPane().add(panel);
        mFrame.setBounds(new Rectangle(new Point(100,400),
                new Dimension(240,100)));

        mFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //----And show it
        mFrame.show();
    }

    class ComboRenderer extends JLabel implements ListCellRenderer {
        Color mycolor;
        int myindex;
        boolean isselected;

        public ComboRenderer() {
            setOpaque(true);
            mycolor = null;
        }

        public void setForeground(Color col) {
            super.setForeground(col);
            if(this.mycolor == null) {
                this.mycolor = col;
            }
        }

        public void setBackground(Color col) {
            super.setBackground(col);
        }

        public Color getBackground() {
            if(myindex >= 0) {
                // Return background for the items in popup
                if(isselected) {
                    return Color.gray;
                } else {
                    return Color.lightGray;
                }
            } else {
                // For the item not in popup
                // return the background set by ComboBox
                return super.getBackground();
            }
        }

        public Color getForeground() {
            if(this.mycolor != null) {
                return this.mycolor;
            } else {
                return super.getForeground();
            }
        }

        public Component getListCellRendererComponent(JList   pList,
                                                      Object  pValue,
                                                      int     pIndex,
                                                      boolean pSelected,
                                                      boolean pHasFocus) {
            setText((String) pValue);

            this.myindex = pIndex;
            this.isselected = pSelected;

            if("red".equals(pValue)) {
                mycolor = Color.red;
            } else if("green".equals(pValue)) {
                mycolor = Color.green;
            } else if("blue".equals(pValue)) {
                mycolor = Color.blue;
            }

            setForeground(mycolor);

            return this;
        }
    }

//======================================================================
    /** Main program  */
//======================================================================
    static public void main(String args[]) {
        new ComboVisualCoreIssue();
    }
}
