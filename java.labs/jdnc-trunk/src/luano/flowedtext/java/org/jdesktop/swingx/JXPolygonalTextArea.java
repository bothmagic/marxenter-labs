package org.jdesktop.swingx;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.StringTokenizer;

/**
 * A component that flows text in a user defined area. The area is a polygon and 
 * need not be composed of rectangular blocks.
 *
 * @author Luan O'Carroll
 */
public class JXPolygonalTextArea extends JXFlowedText 
{  
  /**
   * Create a new polygonal test area
   */
  public JXPolygonalTextArea( )
  {
  }

  /**
    * Attempts to load and reference the associated view/datastore.
    */
  public void init() 
  {
    if ( !getClip() )
      return;

    super.init();
  }
  

  /**
   * Setup the initial polygon
   */
  @Override
  protected void setupInitialFlowArea()
  {
    Point p = getLocation();
    Dimension size = getSize();
    bimg = null;
    oX = p.x;
    oY = p.y;
    oW = size.width;
    oH = size.height;
    
    if ( polyArea == null )
      polyArea = new Polygon();

    setFlowArea( new Area( polyArea ));
  }

  /**
   * Add a point to teh polygon
   * @param x the x position of the point
   * @param y the y position of the point
   */
  public void addPoint( int x, int y )
  {
    Point p = getLocation();
    if ( polyArea == null )
      polyArea = new Polygon();
    polyArea.addPoint( p.x+x, p.y+y );
  }
  
  /**
   * Set the polygon point as a string of comma separated x-y values
   * @param pts the xy pairs
   */
  public void setPoints( String pts )
  {
      StringTokenizer tokenizer = new StringTokenizer( pts, "," );
      while ( tokenizer.hasMoreTokens()) {
        int x = Integer.parseInt( tokenizer.nextToken());
        int y = Integer.parseInt( tokenizer.nextToken());
        addPoint( x, y );
      }      
  }
  
  /**
   * Get the polygon as a set of x,y pairs
   * @return
   */
  public String getPoints()
  {
    String s = "";
    if( polyArea != null ){
      for ( int i = 0; i < polyArea.npoints; i++ )
        s += ( i > 0 ? "," : "" ) +  polyArea.xpoints[ i ] + "," + polyArea.ypoints[ i ];
    }
    return s;
  }
  
  /**
   * The polygonal area
   */
  protected Polygon polyArea;
}
