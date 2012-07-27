/*
 * $Id: JXStopwatchDemo.java 2740 2008-10-08 13:56:58Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.clock.VistaStopwatchIcon;

public class JXStopwatchDemo extends JXComponent {
   public static void main(final String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGUI(args);
         }
      });
   }





   private static void createAndShowGUI(String[] args) {
      new TestFrame("JXStopwatchDemo Test", new JXStopwatchDemo()).setVisible(true);
   }





   public JXStopwatchDemo() {
      super();
      initChildren();
      updateUI();
   }





   private void initChildren() {
      setLayout(new BorderLayout(6, 6));

      final JXStopwatch clock = new JXStopwatch();
      clock.putClientProperty("foregroundIcon", new VistaStopwatchIcon());

      JPanel buttons = new JPanel(new GridLayout(1, 0, 4, 4));
      buttons.setOpaque(false);

      buttons.add(new JButton(new AbstractAction("Start") {
         public void actionPerformed(ActionEvent e) {
            clock.start();
         }
      }));
      buttons.add(new JButton(new AbstractAction("Mark") {
         public void actionPerformed(ActionEvent e) {
            clock.mark(false);
         }
      }));
      buttons.add(new JButton(new AbstractAction("Stop") {
         public void actionPerformed(ActionEvent e) {
            clock.mark(true);
         }
      }));
      buttons.add(new JButton(new AbstractAction("Reset") {
         public void actionPerformed(ActionEvent e) {
            clock.reset();
         }
      }));

      clock.addChangeListener(new ChangeListener() {
         int markCount = 0;
         public void stateChanged(ChangeEvent e) {
            if (clock.getMarkerCount() != markCount) {
               markCount = clock.getMarkerCount();
               StringBuilder buf = new StringBuilder("Found ").append(markCount);
               if (markCount == 1) {
                  buf.append(" marker: ");
               } else {
                  buf.append(" markers: ");
               }
               for (int i = 0; i < markCount; i++) {
                  if (i > 0) {
                     buf.append(", ");
                  }
                  buf.append(clock.getMarker(i).getTimeInMillis());
               }
               System.out.println(buf);
            }
         }
      });

      add(clock, BorderLayout.CENTER);
      add(buttons, BorderLayout.SOUTH);
   }

}
