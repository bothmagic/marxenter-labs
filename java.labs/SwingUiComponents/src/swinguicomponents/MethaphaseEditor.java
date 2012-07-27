/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swinguicomponents;

import com.metaphaseeditor.MetaphaseEditor;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author marxma
 */
public class MethaphaseEditor {
    
    public static void main(String args[]) {
        
        JFrame frame = new JFrame();
        
        MetaphaseEditor editor = new MetaphaseEditor();
        
        
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(editor, BorderLayout.CENTER);
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        
    }
    
    
}
