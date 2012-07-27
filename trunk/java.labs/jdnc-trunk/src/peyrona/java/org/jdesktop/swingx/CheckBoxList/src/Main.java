/*
 * Main.java
 *
 * Created on 26 de agosto de 2006, 12:50
 */

package checkboxlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Francisco Morero Peyrona
 *         francisco.morero@sun.com
 */
public class Main
{
    private CheckBoxList checkBoxList;
    private JCheckBox    chkIncludeDisabled;
    
    public Main()
    {
        checkBoxList = new CheckBoxList();
        checkBoxList.addElement( "0 - cero" );             
        checkBoxList.addElement( "1 - uno" );
        checkBoxList.addElement( "2 - dos", true, false );
        checkBoxList.addElement( "3 - tres" );
        checkBoxList.addElement( "4 - cuatro", true );
        checkBoxList.addElement( "5 - cinco" );
        checkBoxList.addElement( "6 - seis", false, false );
        checkBoxList.addElement( "7 - siete" );
        checkBoxList.addElement( "8 - ocho", false );
        checkBoxList.addElement( "9 - nueve" );
        checkBoxList.setSelectedIndex( 0 );
        
        JTextArea txtManual = new JTextArea( getManual() );
                  txtManual.setRows( 10 );
                  txtManual.setEditable( false );
        
        JSplitPane split = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, 
                                           new JScrollPane( checkBoxList ), 
                                           new JScrollPane( txtManual ) );
        
        chkIncludeDisabled = new JCheckBox( "Include disabled" );
        
        JButton btnSelectAll  = new JButton( "Select All"  );
                btnSelectAll.addActionListener( new ActionListener() 
                {
                    public void actionPerformed( ActionEvent actionEvent )
                    {
                        Main.this.checkBoxList.setSelectedAll( Main.this.chkIncludeDisabled.isSelected() );
                    }
                } );
                
        JButton btnSelectNone = new JButton( "Select None" );
                btnSelectNone.addActionListener( new ActionListener() 
                {
                    public void actionPerformed( ActionEvent actionEvent )
                    {
                        Main.this.checkBoxList.setSelectedNone( Main.this.chkIncludeDisabled.isSelected() );
                    }
                } );
                
        JPanel pnlButtons = new JPanel( new FlowLayout( FlowLayout.LEADING, 8,0 ) );
               pnlButtons.add( chkIncludeDisabled );
               pnlButtons.add( btnSelectAll       );
               pnlButtons.add( btnSelectNone      );
              
               
        JPanel pnlAll = new JPanel( new BorderLayout( 8,8 ) );
               pnlAll.setBorder( new EmptyBorder( 8,8,8,8 ) );
               pnlAll.add( new JLabel( "Spanish numbers" ), BorderLayout.NORTH  );
               pnlAll.add( split                          , BorderLayout.CENTER );
               pnlAll.add( pnlButtons                     , BorderLayout.SOUTH  );
               
        JFrame.setDefaultLookAndFeelDecorated( true );
        JFrame frame = new JFrame( "CheckBoxList test" );
               frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
               frame.setLayout( new BorderLayout() );
               frame.getContentPane().add( pnlAll, BorderLayout.CENTER );
               frame.pack();
               frame.setVisible( true );
    }
    
    private String getManual()
    {
        return "Brief Instructions\n"+
               "------------------\n"+
               "* Click on item to highlight it\n" +
               "* Click on item box to highlight and toggle selection\n"+
               "* Keyboard space-bar also changes selection status\n"+
               "* Multiple selection is allowed (to change multiple\n"+            
               "  items at once, use keyboard)\n\n"+
               " Refer to JavaDoc for more information.";
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