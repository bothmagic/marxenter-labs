/*
 * $Id: JXClockDemo.java 2740 2008-10-08 13:56:58Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.clock.DefaultClockIcon;
import org.jdesktop.swingx.clock.MacClockIcon;
import org.jdesktop.swingx.clock.VistaClockIcon;
import org.jdesktop.swingx.plaf.basic.DigitalClockIcon;

/**
 * simple demo showing the themes for a JXClock.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXClockDemo extends JXComponent {
   public static void main(final String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGUI(args);
         }
      });
   }





   private static void createAndShowGUI(String[] args) {
      new TestFrame("JXClockDemo Test", new JXClockDemo()).setVisible(true);
   }





   private JXClock clock;
   private Theme[] themes = {
                            new Theme("Default", new DefaultClockIcon()),
                            new Theme("Digital", new DigitalClockIcon()),
                            new Theme("Mac OSX", new MacClockIcon()),
                            new Theme("Vista", new VistaClockIcon())
   };

   public JXClockDemo() {
      super();
      initChildren();
      updateUI();
   }





   private void initChildren() {
      clock = new JXClock();
      JComboBox themeChoice = new JComboBox(themes);
      themeChoice.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            clock.putClientProperty("foregroundIcon", ((Theme) ((JComboBox) e.getSource()).getSelectedItem()).icon);
         }
      });

      setLayout(new BorderLayout());
      add(clock, BorderLayout.CENTER);
      add(themeChoice, BorderLayout.SOUTH);

   }





   private static class Theme {
      private Icon icon;
      private String name;

      public Theme(String name, Icon icon) {
         this.name = name;
         this.icon = icon;
      }





      @Override
      public String toString() {
         return name;
      }

   }
}
