/*
 * SwapLists
 *
 * Created on 4 de septiembre de 2006, 1:18
 */

package swaplists;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author Francisco Morero Peyrona
 *         francisco.morero@sun.com
 */
public class Main
{
    public Main() 
    {
        SwapLists swaplists = new SwapLists();
                  swaplists.addToAvailable( "1. Uno"    );
                  swaplists.addToAvailable( "2. Dos"    );
                  swaplists.addToAvailable( "3. Tres"   );
                  swaplists.addToAvailable( "4. Cuatro" );
                  swaplists.addToAvailable( "5. Cinco"  );
                  
                  swaplists.addToSelected( "6. Seis"  );
                  swaplists.addToSelected( "7. Siete" );
                  swaplists.addToSelected( "8. Ocho"  );
                  swaplists.addToSelected( "9. Nueve" );
        
        JFrame.setDefaultLookAndFeelDecorated( true );
        JFrame frame = new JFrame( "SwapLists test" );
               frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
               frame.getContentPane().add( swaplists );
               frame.pack();
               frame.setVisible( true );
    }
    
    public static void main( String[] args )
    {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater( new Runnable() 
        {
            public void run() 
            {
                new Main();
            }
        } );
    }
}