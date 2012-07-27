/*
 * $Id: JXToolBarDemo.java 2355 2008-03-27 03:10:59Z kschaefe $
 * 
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.painter.CheckerboardPainter;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.PinstripePainter;


/**
 *
 */
public class JXToolBarDemo extends JXFrame {
//    private static class SysErrAction extends AbstractAction {
//
//        /**
//         * 
//         */
//        public SysErrAction() {
//            super();
//        }
//
//        /**
//         * @param name
//         */
//        public SysErrAction(String name) {
//            super(name);
//        }
//
//        /**
//         * @param name
//         * @param icon
//         */
//        public SysErrAction(String name, Icon icon) {
//            super(name, icon);
//        }
//
//        /**
//         * {@inheritDoc}
//         */
//        public void actionPerformed(ActionEvent e) {
//            System.err.println(getValue(NAME));
//        }
//        
//    }
//    
//    protected void frameInit() {
//        super.frameInit();
//        
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        
//        JMenuBar menuBar = new JMenuBar();
//        JMenu tbMenu = menuBar.add(new JMenu("ToolBars"));
//        tbMenu.add(new AbstractAction("Use JXToolBar") {
//            public void actionPerformed(ActionEvent e) {
//                setToolBar(createStandardJXToolBar());
//            }
//        });
//        tbMenu.add(new AbstractAction("Use Simple JXCompoundToolBar") {
//            public void actionPerformed(ActionEvent e) {
//                setToolBar(createSimpleJXCompoundToolBar());
//            }
//        });
////        tbMenu.add(new AbstractAction("Use Standard JXCompoundToolBar") {
////            public void actionPerformed(ActionEvent e) {
////                setToolBar(createStandardJXCompoundToolBar());
////            }
////        });
//        setJMenuBar(menuBar);
//        
//        setToolBar(createStandardJXToolBar());
////        setToolBar(createStandardJXCompoundToolBar());
//    }
//    
//    public static void main(String args[]) {
//        SwingUtilities.invokeLater(new Runnable() {
//
//            public void run() {
//                JXToolBarDemo test = new JXToolBarDemo();
//                test.setSize(300, 300);
//                
//                test.setVisible(true);
//            }
//        });
//    }
//    
//    private JXToolBar createStandardJXToolBar() {
//        final JXToolBar toolBar = new JXToolBar();
//        toolBar.setBackgroundPainter(new PinstripePainter<JXToolBar>());
//        toolBar.add(new SysErrAction("Save", new ImageIcon(
//                JXToolBarDemo.class.getResource("resources/save.gif"))));
//        //only future adds will use the glosspainter
//        toolBar.setButtonBackgroundPainter(new GlossPainter<JXButton>(), false);
//        toolBar.add(new SysErrAction("Delete", new ImageIcon(
//                JXToolBarDemo.class.getResource("resources/delete.gif"))));
//        toolBar.add(new AbstractAction("Checker JXT") {
//        
//            public void actionPerformed(ActionEvent e) {
//                //updates all buttons to use checkerboard
//                toolBar.setButtonBackgroundPainter(new CheckerboardPainter<JXButton>());
//            }
//        
//        });
//        
//        return toolBar;
//    }
//    
//    private JXToolBar createSimpleJXCompoundToolBar() {
//        final JXCompoundToolBar toolBar = new JXCompoundToolBar();
//        toolBar.setBackgroundPainter(new PinstripePainter<JXToolBar>());
//        toolBar.add(new SysErrAction("Save", new ImageIcon(
//                JXToolBarDemo.class.getResource("resources/save.gif"))));
//        //only future adds will use the glosspainter
//        toolBar.setButtonBackgroundPainter(new GlossPainter<JXButton>(), false);
//        toolBar.add(new SysErrAction("Delete", new ImageIcon(
//                JXToolBarDemo.class.getResource("resources/delete.gif"))));
//        toolBar.add(new AbstractAction("Checker JXCT") {
//        
//            public void actionPerformed(ActionEvent e) {
//                //updates all buttons to use checkerboard
//                toolBar.setButtonBackgroundPainter(new CheckerboardPainter<JXButton>());
//            }
//        
//        });
//        
//        return toolBar;
//    }
//    
////    private JXToolBar createStandardJXCompoundToolBar() {
////        final JXCompoundToolBar toolBar = new JXCompoundToolBar();
////        toolBar.setBackgroundPainter(new PinstripePainter<JXToolBar>());
////        toolBar.add(new SysErrAction("Save", new ImageIcon(
////                JXToolBarDemo.class.getResource("resources/save.gif"))));
////        //only future adds will use the glosspainter
////        toolBar.setButtonBackgroundPainter(new GlossPainter<JXButton>(), false);
////        toolBar.add(new SysErrAction("Delete", new ImageIcon(
////                JXToolBarDemo.class.getResource("resources/delete.gif"))));
////        toolBar.add(new AbstractAction("Checker JXCT1") {
////        
////            public void actionPerformed(ActionEvent e) {
////                //updates all buttons to use checkerboard
////                toolBar.setButtonBackgroundPainter(new CheckerboardPainter<JXButton>());
////            }
////        
////        });
////        
////        //reset for new tool bar
////        toolBar.setButtonBackgroundPainter(null, false);
////        toolBar.add(new SysErrAction("Save", new ImageIcon(
////                JXToolBarDemo.class.getResource("resources/save.gif"))), "ToolBar2");
////        //only future adds will use the glosspainter
////        toolBar.setButtonBackgroundPainter(new GlossPainter<JXButton>(), false);
////        toolBar.add(new SysErrAction("Delete", new ImageIcon(
////                JXToolBarDemo.class.getResource("resources/delete.gif"))), "ToolBar2");
////        toolBar.add(new AbstractAction("Checker JXCT2") {
////        
////            public void actionPerformed(ActionEvent e) {
////                //updates all buttons to use checkerboard
////                toolBar.setButtonBackgroundPainter(new CheckerboardPainter<JXButton>());
////            }
////        
////        }, "ToolBar2");
////        
////        
////        return toolBar;
////    }
}
