package org.jdesktop.swingx;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.text.BreakIterator;


/**
 * Display text at a user defined angle
 *
 * @author Luan O'Carroll
 */
public class JXReflectedText extends JXFlowedText 
{
  /** The shear factor */
  protected double shear;

  /**
   * Create a new ReflectedTExt component
   */
  public JXReflectedText( )
  {
    shear = 0;
  }

  /**
   * Sets the degree of shear of the control's text.
   * The range is 0 to 3600.
   * @param value the new shear factor
   */
  public void setShear( int value )
  {
    shear = value;
    repaint();
  }

  /**
   * Gets the degree of shear of the control's text.
   * The range is 0 to 3600.
   * @return the new shear factor
   */
  public int getShear()
  {
    return (int)shear;
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
    if (( localCopy.length() <= 0 ) || ( currentPos > 0.0 ))
      return y;

    currentPos = 1000;
    AffineTransform oldTransform = g2.getTransform();
    TextLayout layout;
    AffineTransform atReflected = AffineTransform.getScaleInstance( 1, -1 );
    if ( shear > 0 )
      atReflected.shear( -shear / 100.0, 0.0 );
    Font plainFont = getFont();
    Font reflectedFont = plainFont.deriveFont( atReflected );
    FontMetrics fm = g2.getFontMetrics( plainFont );
    FontRenderContext frc = g2.getFontRenderContext();
    Color color = getForeground();
    Color altColor = new Color( color.getRed(), color.getGreen(), color.getBlue(), 128 );

    for ( int i = 0; i < 2; i++ ) {
      int[] lengths = new int[ 1 ];
      g2.setColor( i == 0 ? color : altColor );
      AttributedString as = getAttributedString( localCopy, i == 0 ? plainFont : reflectedFont, lengths );
      LineBreakMeasurer measurer = new LineBreakMeasurer( as.getIterator(), BreakIterator.getWordInstance(), frc );
      int len = lengths[ 0 ];
      try {
        double colX = columns[ 0 ].getBounds().getX();
        // Take a big horizontal stripe to allow for the rotation and translation
        Rectangle2D.Double rLine = new Rectangle2D.Double( 0.0, oY + ascent, oW, ascent );
        Area aArea = new Area( rLine );
        aArea.intersect( getFlowArea() );

        Rectangle2D r = aArea.getBounds2D();
        double w = r.getWidth();
        if ( w > 0.0 )
          layout = measurer.nextLayout( (float)( w ));
        else
          return oH;

        layout.draw( g2, 0.0F, (float)( ascent ));
      }
      catch( Exception ex1 ) {
        ex1.printStackTrace();
      }
    }
    return oH;
  }   
}
