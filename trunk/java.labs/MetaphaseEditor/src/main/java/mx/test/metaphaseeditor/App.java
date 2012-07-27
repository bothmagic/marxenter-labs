package mx.test.metaphaseeditor;

import com.metaphaseeditor.MetaphaseEditor;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MetaphaseEditor(), BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);
        System.out.println( "Hello World!" );
    }
}
