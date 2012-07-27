/*
 * JFrameUtility.java
 *
 * Created on August 17, 2005, 7:05 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.swingx.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author patrick
 */
public class JFrameUtility {
   
   private JFrameUtility() {}
   
   public static void main(String... args) {
      final JFrame minSizeFrame = new JFrame("Minimum Size Test");
      minSizeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      minSizeFrame.setSize(1024, 768);
      
      JPanel config = new JPanel();
      ((FlowLayout)config.getLayout()).setAlignment(FlowLayout.LEFT);
      final JTextField minW = new JTextField("800");
      minW.setColumns(4);
      minW.setHorizontalAlignment(JTextField.RIGHT);
      final JTextField minH = new JTextField("600");
      minH.setColumns(4);
      minH.setHorizontalAlignment(JTextField.RIGHT);
      config.add(new JLabel("Min. Width"));
      config.add(minW);
      config.add(new JLabel("Min. Height"));
      config.add(minH);
      
      JButton apply = new JButton(new AbstractAction("Apply") {
         public void actionPerformed(ActionEvent e) {
            int newW = new Integer(minW.getText()).intValue();
            int newH = new Integer(minH.getText()).intValue();
            JFrameUtility.addMinimumSizeManager(minSizeFrame, newW, newH);
            minSizeFrame.setSize(newW, newH);
         }
      });
      config.add(apply);
      
      minSizeFrame.add(config, BorderLayout.NORTH);
      
      JFrameUtility.addMinimumSizeManager(minSizeFrame, 800, 600);

      minSizeFrame.setVisible(true);
   }
   
   public static void addMinimumSizeManager(JFrame frame, int minWidth, int minHeight) {
      ComponentListener[] listeners = frame.getComponentListeners();
      ComponentListener listener = null;
      boolean found = false;
      for ( ComponentListener l : listeners ) {
         if ( l instanceof MinSizeComponentListener ) {
            listener = l;
            break;
         }
      }
      if ( listener == null ) {
         frame.addComponentListener( new JFrameUtility().new MinSizeComponentListener(frame, minWidth, minHeight));
      } else {
         ((MinSizeComponentListener)listener).resetSizes(minWidth, minHeight);
      }
   }
   
   class MinSizeComponentListener extends ComponentAdapter {
      private JFrame frame;
      private int minHeight;
      private int minWidth;
      
      MinSizeComponentListener(JFrame frame, int minWidth, int minHeight) {
         this.frame = frame;
         resetSizes(minWidth, minHeight);
      }
      
      public void resetSizes(int minWidth, int minHeight) {
         this.minWidth = minWidth;
         this.minHeight = minHeight;
         adjustIfNeeded(frame);
      }
      
      public void componentResized(java.awt.event.ComponentEvent evt) {
         adjustIfNeeded((JFrame)evt.getComponent());
      }
      
      private void adjustIfNeeded(final JFrame frame) {
         boolean doSize = false;
         int newWidth = frame.getWidth();
         int newHeight = frame.getHeight();
         
         if( frame.getWidth() < minWidth ) {
            newWidth = minWidth;
            doSize = true;
         }
         if( frame.getHeight() < minHeight ){
            newHeight = minHeight;
            doSize = true;
         }
         if ( doSize ) {
            final int w = newWidth;
            final int h = newHeight;
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  frame.setSize(w, h);
               }
            });
         }
      }
   }
   
}
