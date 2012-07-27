/**
 * 
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 */
public class JXPolygonalTextAreaDemo extends JFrame 
{
    protected void frameInit() 
    {
        super.frameInit();
        
        setTitle( "Flowed Text Test" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        setLayout(new GridLayout(1, 1));
        
        JPanel cp = new JPanel();        
        cp.setLayout( null );
        cp.setBackground( Color.white );
        
        /*
         * Set JXFlowedText.DEBUG to true to draw an outline of the shapes.
         */
        
        /* 
         * Create a polygon
         * 
         * ---------------------------------------------------------------------
         * |                                                                   |
         * |                                                                   |
         * |                                                      -------------|
         * |                                                      |
         * |                                                      |            
         * |                                                      \           
         * |-------                                                 \         
         *         \                                                  \        
         *           \                                                  \------
         *             \                                                       |
         *             /                                                       |
         *           /                                                         |
         * |------ /                                                           |
         * |                                                                   |
         * |                                                                   |
         * |                                                                   |
         * |                                                                   |
         * |                                                                   |
         * |                                                                   |
         * |                                                                   |
         * |                                                                   |
         * |                                                                   |
         * ---------------------------------------------------------------------
         */
        JXPolygonalTextArea pta = new JXPolygonalTextArea();
        pta.setAntiAlias( true );
        
        // Divide into two columns
        pta.setNumColumns( 2 );
        pta.setColSpacing( 10 );
        pta.setName( "FlowedTextArea" );
        pta.setBounds( 10, 10, 520, 510 );
        pta.setPoints( "0,0,500,0,500,40,350,40,350,90,460,185,500,185,500,480,0,480,0,280,80,280,160,220,70,145,0,145,0,0" );
        pta.setText( "<b>Lorem ipsum dolor</b> sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.<br>Ut enim ad <u>minim veniam</u>, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur est notra sum. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. <strike>At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.</strike> Et harum quidem rerum facilis est et expedita distinctio.<br><justify>Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae.</justify><br><i>Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat</i>. <br>At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat" );
        pta.setOpaque( false );
        pta.setForeground( Color.blue );
        cp.add( pta );
        

        // Knock out an ellipse
        JXShape ellipse = new JXShape();
        ellipse.setMode( JXShape.SOLID_ELLIPSE );
        ellipse.setBounds( 200, 250, 200, 200 );
        ellipse.setForeground( Color.red );
        cp.add( ellipse );
        
        // Nest an image
        ellipse.setLayout( new BorderLayout());
        JXImagePanel ip = new JXImagePanel();
        try {
          ip.setImage( ImageIO.read( getClass().getResource( "/toi.jpg" )));
        } 
        catch ( IOException ex ) {
          Logger.getLogger( JXPolygonalTextAreaDemo.class.getName()).log( Level.SEVERE, null, ex );
        }
        ellipse.add( ip, BorderLayout.CENTER );
            
        setLayout( new BorderLayout());
        add( cp );
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JXPolygonalTextAreaDemo test = new JXPolygonalTextAreaDemo();
                test.setSize(550, 570);
                test.setVisible(true);
            }
        });
    }

}
