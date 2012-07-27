/**
 * $Id: JDesktopEnhancementsDemo.java 810 2006-06-25 21:33:16Z guy_davis $
 *
 * Copyright (c) 2005 Guy Davis
 * davis@guydavis
 * http://www.guydavis.ca/projects/oss/
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jdesktop.jdnc.incubator.guy_davis;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 * A demo of the <code>JTaskBar</code> and <code>JWindowsMenu</code>. A
 * Java WebStart demo is available at:
 * http://www.guydavis.ca/log/comments.jsp?id=893
 * 
 * @see ca.guydavis.swing.desktop.JXTaskBar
 * @see ca.guydavis.swing.desktop.JXWindowsMenu
 */
public class JDesktopEnhancementsDemo extends JFrame {

    /** The desktop for this demo */
    JDesktopPane desktop;

    /** Counts the number of open windows for unique names */
    private int openedWindowCount;

    /**
     * @see ca.guydavis.swing.SwingDemo#showDemo()
     */
    public void showDemo() {
        initDemo();
        setBounds(10, 10, 800, 800);
        setVisible(true);
    }

    /**
     * Layout the demo and set up an initial window.
     */
    private void initDemo() {
        setTitle("TaskBar and WindowsMenu Demo");
        this.desktop = new JDesktopPane();
        JMenuBar menuBar = initMenuBar();

        setLayout(new BorderLayout());
        add(menuBar, BorderLayout.NORTH);
        add(this.desktop, BorderLayout.CENTER);

        JXTaskBar taskBar = new JXTaskBar(this.desktop);
        add(taskBar, BorderLayout.SOUTH);

        openNewInternalFrame(); // Open one window to start
    }

    /**
     * @return The menu bar with the "Windows" menu
     */
    private JMenuBar initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem item = new JMenuItem("New Window");
        item.setMnemonic(KeyEvent.VK_N);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                InputEvent.CTRL_MASK));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(@SuppressWarnings("unused")
            ActionEvent e) {
                openNewInternalFrame();
            }
        });
        fileMenu.add(item);

        menuBar.add(fileMenu);

        JXWindowsMenu windowsMenu = new JXWindowsMenu(this.desktop);
        windowsMenu.setWindowPositioner(new CascadingWindowPositioner(
                this.desktop));
        menuBar.add(windowsMenu);
        return menuBar;
    }

    /**
     * Creates a new internal frame for the desktop to manage.
     */
    protected void openNewInternalFrame() {
        JInternalFrame window = new JInternalFrame("Window #"
                + ++this.openedWindowCount, true, true, true, true);
        JTextArea text = new JTextArea();
        text.setText("Try minimizing, restoring, and maximizing"
                + " this window from the task bar.");
        window.add(new JScrollPane(text));

        window.setSize(500, 500);
        this.desktop.add(window);
        window.setVisible(true);
    }

    /**
     * Runs the demo application.
     */
    public static void main(String[] args) {
        JDesktopEnhancementsDemo frame = new JDesktopEnhancementsDemo();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.showDemo();
    }
}