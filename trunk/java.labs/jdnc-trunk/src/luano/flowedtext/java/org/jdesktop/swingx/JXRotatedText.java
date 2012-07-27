package org.jdesktop.swingx;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.text.BreakIterator;


/**
 * Display text at a user defined angle
 *
 * @author Luan O'Carroll
 */
public class JXRotatedText extends JXFlowedText 
{
  protected double angle;

  /**
   * Create a new instance
   */
  public JXRotatedText( )
  {
    angle = 45;
  }

  /**
   * Sets the angle of the control's text.
   * The range is 0 to 3600.
   * @param value the new angle
   */
  public void setAngle( int value )
  {
    angle = value;

    repaint();
  }

  /**
   * Gets the angle of the control's text.
   * The range is 0 to 3600.
   * @return the new angle
   */
  public int getAngle()
  {
    return (int)angle;
  }

  /**
   * Render a text that wraps within the client area
   * @param g2 the graphics context
   * @param localCopy the string to render
   * @param currentPos the current starting y position
   * @param y the y offset into the client area
   * @param columns the column areas
   * @return the latest y position
   */
  protected double wrapString( Graphics2D g2, String localCopy, double currentPos, double y, Area[] columns )
  {
    if ( localCopy.length() <= 0 )
      return y;

    AffineTransform oldTransform = g2.getTransform();
    TextLayout layout;
    Point2D.Double pen = new Point2D.Double( 0, currentPos );
    double radians = angle * Math.PI * 2.0 / 360.0;
    AffineTransform atr = AffineTransform.getRotateInstance( -radians );
    AffineTransform at = AffineTransform.getTranslateInstance( -oX, -oY );
    Font font = getFont().deriveFont( atr );
    FontMetrics fm = g2.getFontMetrics( font );
    FontRenderContext frc = g2.getFontRenderContext();
    Object[] cachedRefs = (Object[])attributedStrings.get( localCopy );

    AttributedString as;
    int[] lengths;
    if ( cachedRefs == null ) {
      Object[] refs = new Object[ 2 ]; 
      lengths = new int[ 1 ];
      refs[ 0 ] = as = getAttributedString( localCopy, font, lengths );
      refs[ 1 ] = lengths;
      attributedStrings.put( localCopy, refs );
    }
    else {
      as = (AttributedString)cachedRefs[ 0 ];
      lengths = (int[])cachedRefs[ 1 ];
    }
    g2.setTransform( at );

    double cos = Math.cos( radians );
    double sin = Math.sin( radians );
    LineBreakMeasurer measurer = new LineBreakMeasurer( as.getIterator(), BreakIterator.getWordInstance(), frc );
    int ii = 0;
    int len = lengths[ 0 ];
    try {
      for ( int columnIdx = currentColumn; columnIdx < columns.length; columnIdx++ ) {
        double colX = columns[ columnIdx ].getBounds().getX();
        double prevWidth = oW / sin;
        if (( columnIdx > 0 ) && ( measurer.getPosition() < len ))
            pen.y = currentPos;
        
        while (( measurer.getPosition() < len ) && ( pen.y < ( oH - ascent ))) {
          ii++;
          // Take a big horizontal stripe to allow for the rotation and translation
          Rectangle2D.Double rLine = new Rectangle2D.Double( -1000.0, oY + pen.y, 2000.0 + oW / sin, 1 );//ascent );
          Area aArea = new Area( rLine );
          aArea.transform( AffineTransform.getRotateInstance( -radians ));
          aArea.transform( AffineTransform.getTranslateInstance( -sin * pen.y, pen.y * sin  ) );
          aArea.intersect( columns[ columnIdx ] );

          Rectangle2D.Double rLine2 = new Rectangle2D.Double( -1000.0, oY + pen.y + ascent-1, 2000.0 + oW / sin, 1 );//ascent );
          Area aArea2 = new Area( rLine2 );
          aArea2.transform( AffineTransform.getRotateInstance( -radians ));
          aArea2.transform( AffineTransform.getTranslateInstance( -sin * ( pen.y  + ascent-1 ), pen.y * sin  ) );
          aArea2.intersect( columns[ columnIdx ] );
          
          Rectangle2D r = aArea.getBounds2D();
          Rectangle2D r2 = aArea2.getBounds2D();
          double w = Math.min( r.getWidth() - 1/sin, r2.getWidth() - 1/sin);
          double layoutW = Math.max( 0.0, w );
          if ( layoutW > 0.0 )
            layout = measurer.nextLayout( (float)( layoutW ));
          else
            layout = null;
          pen.y += ascent;
          prevWidth = layoutW;//Math.max( r.getWidth(), r2.getWidth() );

          //g2.draw( aArea );
          if ( layout == null )
            continue;
          else {
            double deltaX = 0;
            if ( r.getX() > colX ) 
              deltaX = ascent;
            
            layout.draw( g2, (float)(r2.getX() + sin*ascent), (float)( r2.getY() + r2.getHeight() - fm.getDescent() ));
          }
          
          // Only increment the current column index when something has actually 
          // been drawn in the column. If we get this far then something has 
          // been drawn
          currentColumn = columnIdx;
        }
        currentPos = y;
      }
    }
    catch( Exception ex1 ) {
      ex1.printStackTrace();
    }
    g2.setTransform( oldTransform );
    return pen.y;
  }   
}
