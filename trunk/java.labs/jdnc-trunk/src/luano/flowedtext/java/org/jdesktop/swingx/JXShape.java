/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.swingx;

import org.jdesktop.swingx.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 *
 * @author luano
 */
public class JXShape extends JComponent implements JXShapeGenerator
{
  /**
   * the drawing / shape mode
   */
  protected int mode = 0;

  /**
   * A rectangle shape
   */
  public static final int RECTANGLE = 0;
  
  /**
   * A vertical or horizontal line
   */
  public static final int ORTHO_LINE = 1;
  
  /**
   * An extra bold horizontal line
   */
  public static final int EXTRABOLD_HORIZONTAL = 2;
  
  /**
   * A bold horizontal line
   */
  public static final int BOLD_HORIZONTAL = 3;
  
  /**
   * A vertical line
   */
  public static final int NORMAL_HORIZONTAL = 4;
  
  /**
   * A thin horizontal line
   */
  public static final int THIN_HORIZONTAL = 5;
  
  /**
   * An extra bold vertical line
   */
  public static final int EXTRABOLD_VERTICAL = 6;

  /**
   * A bold vertical line
   */
  public static final int BOLD_VERTICAL = 7;
  
  /**
   * A vertical line
   */
  public static final int NORMAL_VERTICAL = 8;
  
  /**
   * A thin vertical line
   */
  public static final int THIN_VERTICAL = 9;
  
  /**
   * A line from the top right to the bottom left
   */
  public static final int RIGHT_TOP_LINE = 10;
  
  /**
   * A line from the top left to the bottom right
   */
  public static final int LEFT_TOP_LINE = 11;
  
  /**
   * An ellipse outline
   */
  public static final int ELLIPSE = 12;
  
  /**
   * A filled ellipse
   */
  public static final int SOLID_ELLIPSE = 13;
    
  /**
   * A filled diamond
   */
  public static final int SOLID_DIAMOND = 14;
  
  /**
   * An outline diamond
   */
  public static final int DIAMOND = 15;
  
  /**
   * A user defined shape drawn in outline
   */
  public static final int USER = 16;

  /**
   * A user defined shape, filled
   */
  public static final int USER_FILL = 17;

  /**
   * A user defined shape drawn and filled
   */
  public static final int USER_FILL_DRAW = 18;

  private static double[] lineThickness = { 3.0, 2.0, 1.0, 0.5 };
  
  /**
   * The user defined shape
   */
  protected Shape userShape;
  protected Shape shape;
  
  private boolean selected;

  private BufferedImage backgroundImage;
  
  public JXShape()
  {
    setOpaque( false );
  }
  
  /**
   * Get a shape
   * @return the generated shape
   */ 
  public Shape getShape( boolean useParentSpace )
  {
    Rectangle r = getBounds();
    double x = useParentSpace ? r.x : 0.0;
    double y = useParentSpace ? r.y : 0.0;
    
    Dimension d = getSize();
    GeneralPath gp;

    switch( mode ) {
      case RECTANGLE:
      case ORTHO_LINE:
        return new Rectangle2D.Double( x, y, d.width, d.height );

      case EXTRABOLD_HORIZONTAL:
      case BOLD_HORIZONTAL:
      case NORMAL_HORIZONTAL:
      case THIN_HORIZONTAL:
        return new Rectangle2D.Double( x, y, d.width, lineThickness[ mode - EXTRABOLD_HORIZONTAL ] ); 

      case EXTRABOLD_VERTICAL:
      case BOLD_VERTICAL:
      case NORMAL_VERTICAL:
      case THIN_VERTICAL:
        return new Rectangle2D.Double( x, y, lineThickness[ mode - EXTRABOLD_VERTICAL ], d.height ); 

      case RIGHT_TOP_LINE:
        gp = new GeneralPath();
        gp.moveTo( d.width, y );
        gp.lineTo( x, d.height );
        return gp;
      case LEFT_TOP_LINE:
        gp = new GeneralPath();
        gp.moveTo( x, y );
        gp.lineTo( d.width, d.height );
        return gp;

      case SOLID_ELLIPSE:
      case ELLIPSE:
        return new Ellipse2D.Double( x, y, d.width -1.0, d.height -1.0 );

      case SOLID_DIAMOND:
      case DIAMOND:
        gp = new GeneralPath();
        gp.moveTo( x + d.width / 2.0F, y );
        gp.lineTo( x + d.width, y + d.height / 2.0F );
        gp.lineTo( x + d.width / 2.0F, y + d.height );
        gp.lineTo( x, y + d.height / 2.0F );
        gp.closePath();
        return gp;
        
      case USER:
      case USER_FILL:
      case USER_FILL_DRAW:
        if ( userShape != null )
          return userShape;
    }
    
    return getBounds();
  }

  /**
   * Paint the child components, this method just consumes the call without doing anything
   * @param g the graphics context
   */
  public void paintChildren( Graphics g )
  {    
    paintComponent( g );
  }
  
  public void paintComponent( Graphics g )
  {
    if ( shape == null )
      shape = getShape( false );
    
    Graphics2D g2d = (Graphics2D)g.create();
    Dimension size = getSize();
    int width = size.width;
    int height = size.height;

    try {
      if ( getComponentCount() > 0 ) {
        backgroundImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        Graphics2D gbk = (Graphics2D)backgroundImage.getGraphics();
        super.paintChildren( gbk );
        gbk.dispose();
      }
      
      if ( backgroundImage != null )
        g2d.setPaint( new TexturePaint( backgroundImage, new Rectangle2D.Double( 0, 0, width, height )));
      else  
        g2d.setColor( getForeground());
      
      switch( mode ) {
        case RECTANGLE:
        case ORTHO_LINE:
        case EXTRABOLD_HORIZONTAL:
        case BOLD_HORIZONTAL:
        case NORMAL_HORIZONTAL:
        case THIN_HORIZONTAL:
        case EXTRABOLD_VERTICAL:
        case BOLD_VERTICAL:
        case NORMAL_VERTICAL:
        case THIN_VERTICAL:
        case RIGHT_TOP_LINE:
        case LEFT_TOP_LINE:
        case SOLID_ELLIPSE:
        case SOLID_DIAMOND:
        case USER_FILL:
        case USER_FILL_DRAW:
          if ( backgroundImage == null )
            g2d.setColor( getBackground());
          
          g2d.fill( shape );
          if ( mode != USER_FILL_DRAW )
            break;

        case ELLIPSE:
        case DIAMOND:
        case USER:
          g2d.setColor( getForeground());
          g2d.draw( shape ); 
          break;
      }

      if ( selected ) {
          g2d.setColor( Color.black );//getForeground() );
          g2d.drawRect( 0, 0, width-1, height-1 );
      }
    }
    finally {
      g2d.dispose();
    }
  }

   
  /**
  * Set a user defiend shape. The shapoe should define a close area is the
  * mode is set for filling
  * @param s the user shape
  * @param fill true to fill the shape
  */
  public void setShape( Shape s, boolean fill )
  {
    userShape = s;
    mode = fill ? USER_FILL : USER;
    repaint();
  }

  /**
   * @param selected the selected to set
   */
  public void setSelected( boolean selected ) 
  {
    this.selected = selected;
    repaint();
  }

  /**
   * @return the selected
   */
  public boolean isSelected() 
  {
    return selected;
  }

  /**
   * Get the drawing mode
   * @return
   */
  public int getMode()
  {
    return mode;
  }

  /**
   * Set the drawing mode
   * @param mode
   */
  public void setMode( int m )
  {
    mode = m;
  }
}