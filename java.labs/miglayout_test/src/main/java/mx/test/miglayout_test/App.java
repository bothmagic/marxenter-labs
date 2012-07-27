package mx.test.miglayout_test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new MigLayout("", "[grow, fill]", ""));
        JScrollPane pane = new JScrollPane(panel);
        
        frame.getContentPane().add(pane, BorderLayout.CENTER);
        JEditorPane ta = new JEditorPane();
        ta.setMinimumSize(new Dimension(300, 50));
        ta.setText("ta");
        JEditorPane tb = new JEditorPane();
        tb.setText("tb");
        
        panel.add(ta, "aligny top, hmin 50, wmin 50");
        panel.add(tb, "newline, growx, aligny top, hmin 50");
        frame.setSize(600, 600);
        frame.setVisible(true);
        
    }
}
