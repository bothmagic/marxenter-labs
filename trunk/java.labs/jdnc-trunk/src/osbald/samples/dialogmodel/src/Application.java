package uk.co.osbald.sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston (osbald.co.uk)
 * Date: 20-Nov-2006
 * Time: 11:20:36
 */

public class Application {

    private JFrame frame;
    static Application instance;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                instance = new Application();
            }
        });
    }

    Application() {
        this.frame = new JFrame(getClass().getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createUI(frame);
        createMenu(frame);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static Application getInstance() {
        return instance;
    }

    void createUI(JFrame frame) {
        // becaue theres nothing else in here for now..
        frame.setPreferredSize(new Dimension(640, 480));
    }

    void createMenu(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(new OpenModuleAction());
        fileMenu.addSeparator();
        JMenuItem exitMI = fileMenu.add("Exit");
        exitMI.setMnemonic(KeyEvent.VK_X);
        exitMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Frame frame = Application.getInstance().getFrame();
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
    }

    public JFrame getFrame() {
        return frame;
    }
}
