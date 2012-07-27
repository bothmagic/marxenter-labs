package org.jdesktop.swingx;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.text.AttributedString;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.AttributedCharacterIterator;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.View;

/**
 * A component that flows text around other components, child components and
 * components that overlap this component. Assumes that the area within which the 
 * text is flowed is composed of rectangular blocks.
 *
 * @author Luan O'Carroll
 */
public class JXFlowedText extends JComponent
{
  public static final String CRLF_PAIR = "\r\n";
  public static final boolean DEBUG = true;
  
  protected Object antiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
  protected Object rendering = RenderingHints.VALUE_RENDER_QUALITY;
  
  protected double padding = 20.0;
  
  protected boolean clip;
  protected float ascent = 0;
  protected Area flowArea;
  protected Paint texture;
  protected AlphaComposite composite;
  protected BufferedImage bimg;
  protected int imageType;
  protected int oX, oY, oW, oH;
  protected int oldW = 0, oldH = 0;
  protected int deltaX, deltaY;
  protected int cols;
  protected int colSpacing;
  protected int currentColumn;

  protected String label;
  
  protected Hashtable attributedStrings;

  /**
   * Create a new flowed text component
   */
  public JXFlowedText()
  {
    setImageType( 0 );
    setDoubleBuffered( false );

    deltaX = 10;
    deltaY = 15;
    cols = 0;
    colSpacing = 10;
    clip = true;
  }
  
   /**
    * Attempts to load and reference the associated view/datastore.
    */
  public void init()
  {
    setupInitialFlowArea();
    
    Container parent = (Container)getParent();
    int count = parent.getComponentCount();
    for ( int i = 0; i < count; i++ ) {
      Component cc = parent.getComponent( i );
      if (( cc != this ) && cc.isVisible()) {
        Area childArea;
        if ( cc instanceof JXShapeGenerator ) {
          // grow the area to provide padding around the nested shape
          Shape shape = ((JXShapeGenerator)cc).getShape( true );
          childArea = new Area( shape );
          Rectangle2D rect = childArea.getBounds2D();
          double cx = rect.getCenterX();
          double cy = rect.getCenterY();
          double w = rect.getWidth();
          double h = rect.getHeight();
          double r = Math.sqrt( w * w + h * h );
          double scale = ( r + 2.0 * padding ) / r ;
          AffineTransform growTransform = AffineTransform.getScaleInstance( scale, scale );
          
          /*
           * The grow transform needs to move the centre back to the unscaled location
           * with a translate transform
           */
          growTransform.translate(( cx - ( cx * scale ))/scale, ( cy - ( cy * scale ))/scale);
          childArea.transform( growTransform );
        }
        else {
          Rectangle rCC = cc.getBounds();
          rCC.grow( deltaX, deltaY );
          childArea = new Area( rCC );
        } 
        flowArea.subtract( childArea );
      }
    }
    repaint();
  }

  /**
   * Setup the initial shape of this component
   */
  protected void setupInitialFlowArea()
  {
    Point p = getLocation();
    Dimension size = getSize();
    bimg = null;
    oX = p.x;
    oY = p.y;
    oW = size.width;
    oH = size.height;
    
    flowArea = new Area( new Rectangle( oX, oY, oW, oH ));    
  }
  //=========================================================================
  //	    Rendering
  //=========================================================================
  /**
   * Adjust the settings for the next step.
   * @param w the width
   * @param h the height
   */  
  public void reset(int w, int h)
  {
  }

  /**
   * Adjust the settings for the next step.
   * 
   * @param w the width
   * @param h the height
   */  
  public void step( int w, int h )
  {
  }  
  
  /**
   * Create a buffer for the animation
   * @param w the image width
   * @param h the image height
   * @param imgType the image type e.g. BufferedImage.TYPE_INT_ARGB
   * @return the new image
   */
  public BufferedImage createBufferedImage( int w, int h, int imgType )
  {
    BufferedImage bi = null;
    if ( imgType == 0 ) 
      bi = new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB /*2*/ );
    else 
      bi = new BufferedImage( w, h, imgType );
    
    //bi = new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB /*2*/ );
    return bi;
  }

  /**
   * Create a graphics context for an image buffer and prepare that buffer for use.
   * 
   * @param width the width
   * @param height the height
   * @param bi the buffered image
   * @param g the graphics context
   * @return the Java2D graphics context for the buffer
   */
  public Graphics2D createGraphics2D( int width,
                                      int height,
                                      BufferedImage bi,
                                      Graphics g )
  {
    Graphics2D g2 = null;

    if ( bi != null )
      g2 = bi.createGraphics();
    else
      g2 = ( Graphics2D )g;

    g2.setBackground( getBackground() );
    g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, antiAlias );
    g2.setRenderingHint( RenderingHints.KEY_RENDERING, rendering );

    // This gives problems with overlays.
    //g2.clearRect( 0, 0, width, height );

    if ( isOpaque()) {
      if ( texture != null ) {
        // set composite to opaque for texture fills
        g2.setComposite( AlphaComposite.SrcOver );
        g2.setPaint( texture );
        g2.fillRect( 0, 0, width, height );
      }
    }

    if ( composite != null )
      g2.setComposite( composite );

    return g2;
  }

  /**
   * All classes that extend JAnimationSurface must implement this routine...
   * This component renders text in the flow area. The flow area is calculated 
   * to be the non-overlapped client area
   * @param w the width
   * @param h the height
   * @param g2 the graphics context
   */
  public void drawObjects( int w, int h, Graphics2D g2 )
  {
    if (( label == null ) || ( label.length() <= 0 ))
      return;
    
    if ( flowArea == null )
      init();
    
    if ( attributedStrings == null )
      attributedStrings = new Hashtable();
    
    applyState( g2 );
    Font font = getFont();
    FontMetrics fm = g2.getFontMetrics( font );
    FontRenderContext frc = g2.getFontRenderContext();

    // Calculate the columns areas
    Point2D.Double pen = new Point2D.Double( 0, 0 );
    ascent = fm.getAscent();
    int numColumns = Math.max( cols, 1 );
    double columnWidth = ( flowArea.getBounds2D().getWidth() - ( numColumns == 1 ? 0 : numColumns * colSpacing )) / numColumns;
    Area[] columns = new Area[ numColumns ];
    double x = flowArea.getBounds().getX();
    for ( int i = 0; i < numColumns; i++ ) {
      Area basicColumn = new Area( new Rectangle2D.Double( x, oY, columnWidth, oH ));
      basicColumn.intersect( flowArea );
      columns[ i ] = basicColumn;
      x += columnWidth + colSpacing;
    }

    View v = (View)getClientProperty( "html" );
    if ( v != null ) 
      v.paint( g2, columns[ 0 ].getBounds());
    else {
      double yPos = 0;
      double currentPos = 0;
      currentColumn = 0;
      label = replace( label, "<br>".subSequence( 0, 4 ), CRLF_PAIR.subSequence( 0, CRLF_PAIR.length()));
      int idx = label.indexOf( CRLF_PAIR );
      String targetStr = label;
      while ( idx > -1 ) {
        if ( idx > 6 ) 
          currentPos = wrapString( g2, targetStr.substring( 0, idx ), currentPos, yPos, columns );
        currentPos += ascent;
        targetStr = targetStr.substring( idx + CRLF_PAIR.length());
        idx = targetStr.indexOf( CRLF_PAIR );
      }
      yPos = wrapString( g2, targetStr, currentPos, yPos, columns );
    }    
  }
  
  /**
   * Paint the component, buffering the image in the process if necessary
   * @param g the graphics context
   */
  public void paintComponent( Graphics g )
  {
  //  super.paintComponent( g );

    Dimension d = getSize();

    if ( bimg == null ) {
      if ( imageType == 1 ) {
        bimg = null;
      }
      else if (( bimg == null ) || ( oldW != d.width ) || ( oldH != d.height )) {
        bimg = createBufferedImage( d.width, d.height, imageType - 2 );
//        if ( this instanceof AnimationStep ) 
//          ((AnimationStep)this).reset();
      }
    }
//    autoStart();

    oldW = d.width;
    oldH = d.height;

//    if ( animator != null )
//      animationContext.step( d.width, d.height );

    Graphics2D g2 = createGraphics2D( d.width, d.height, bimg, g );
    drawObjects( d.width, d.height, g2 );
    if ( DEBUG ) {
      g2.setColor( Color.red );
      Rectangle rect = flowArea.getBounds();
      g2.translate( -rect.x, -rect.y );
      g2.draw( flowArea );
    }
    g2.dispose();

    if ( bimg != null ) {
      g.drawImage( bimg, 0, 0, null );
      getToolkit().sync();
    }
  }

  /**
   * Print this component
   * @param g the graphics context
   * @param pf the page format
   * @param pi the page index
   * @return Printable.PAGE_EXISTS on success or Printable.NO_SUCH_PAGE
   * @throws java.awt.print.PrinterException Problems!
   */
  public int print( Graphics g, PageFormat pf, int pi ) throws PrinterException
  {
    if ( pi >= 1 ) {
      return Printable.NO_SUCH_PAGE;
    }

    Graphics2D g2d = ( Graphics2D )g;
    g2d.translate( pf.getImageableX(), pf.getImageableY() );
    g2d.translate( pf.getImageableWidth() / 2,
                   pf.getImageableHeight() / 2 );

    Dimension d = getSize();

    double scale = Math.min( pf.getImageableWidth() / d.width,
                             pf.getImageableHeight() / d.height );
    if ( scale < 1.0 ) {
      g2d.scale( scale, scale );
    }

    g2d.translate( -d.width / 2.0, -d.height / 2.0 );

    if ( bimg == null ) {
      Graphics2D g2 = createGraphics2D( d.width, d.height, null, g2d );
      drawObjects( d.width, d.height, g2 );
      g2.dispose();
    }
    else {
      g2d.drawImage( bimg, 0, 0, this );
    }

    return Printable.PAGE_EXISTS;
  }

  /**
   * Apply the current state to the graphics rendering context
   * @param g2 the graphics context
   */
  public void applyState( Graphics2D g2 )
  {
    g2.setColor( getForeground());
  }
  
  /**
   * Draw text that wraps within the flow area
   * @param g2 the graphics context
   * @param text the text
   * @param currentPos the current y offset for a new column
   * @param y the current y offset within the column for a new block of text
   * @param columns the area of the columns
   * @return the latest y position for drawing/rendering
   */
  protected double wrapString( Graphics2D g2, String text, double currentPos, double y, Area[] columns )
  {
    if ( text.length() <= 0 )
      return y;

    Point2D.Double pen = new Point2D.Double( 0, currentPos );
    Font font = getFont();
    FontMetrics fm = g2.getFontMetrics( font );
    double em = fm.getStringBounds( "m", g2).getWidth();

    TextLayout layout;
    AffineTransform at = AffineTransform.getTranslateInstance( -oX, -oY );
    FontRenderContext frc = g2.getFontRenderContext();
    Object[] cachedRefs = (Object[])attributedStrings.get( text );
    AttributedString as;
    int[] lengths;
    if ( cachedRefs == null ) {
      Object[] refs = new Object[ 2 ]; 
      lengths = new int[ 1 ];
      refs[ 0 ] = as = getAttributedString( text, font, lengths );
      refs[ 1 ] = lengths;
      attributedStrings.put( text, refs );
    }
    else {
      as = (AttributedString)cachedRefs[ 0 ];
      lengths = (int[])cachedRefs[ 1 ];
    }
    
    LineBreakMeasurer measurer = new LineBreakMeasurer( as.getIterator(), BreakIterator.getWordInstance(), frc );
    int ii = 0;
    int len = lengths[ 0 ];
    try {
      for ( int columnIdx = currentColumn; columnIdx < columns.length; columnIdx++ ) {
        if (( columnIdx > 0 ) && ( measurer.getPosition() < len ))
          pen.y = currentPos;
        
        while (( measurer.getPosition() < len ) && ( pen.y < oH )) {
          ii++;
          Rectangle2D.Double rLine = new Rectangle2D.Double( oX, oY + pen.y, oW, ascent );
          Area aArea = new Area( rLine );
          if ( flowArea != null )
            aArea.intersect( columns[ columnIdx ] );
          
          Shape s = at.createTransformedShape( aArea );
          Rectangle r = s.getBounds();
          int w = (int)r.getWidth();
          if ( w > em )  // Avoid a partial glyph at the end of the line
            layout = measurer.nextLayout( (float)( w - em ));
          else
            layout = null;
          
          pen.y += ascent;
          if ( layout == null )
            continue;
          else 
            layout.draw( g2, (float)r.getX(), (float)pen.y );
          
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
    return pen.y;
  }
  
  /**
   * Get an AttribtedString for this component, converting the tags
   * &lt;b&gt;, &lt;i&gt;, &lt;u&gt;, &lt;sub&gt;, &lt;super&gt;, &lt;swap&gt;, &lt;justify&gt;, &lt;strike&gt;
   * to the proper internal form.
   * @param localCopy the text being rendered
   * @param font the current font
   * @param lengths on return contains the length of the text to be rendered - the argument should be a string[1]
   * @return the new AttributedString
   */
  protected AttributedString getAttributedString( String localCopy, Font font, int[] lengths )
  {
    ArrayList attributes = new ArrayList();
    
    String[] startTags = { "<b>", "<i>", "<u>", "<sub>", "<super>", "<swap>", "<justify>", "<strike>" };
    String[] endTags = { "</b>", "</i>", "</u>", "</sub>", "</super>", "</swap>", "</justify>", "</strike>" };
    AttributedCharacterIterator.Attribute[] keys =  { TextAttribute.WEIGHT, TextAttribute.POSTURE, TextAttribute.UNDERLINE, TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT, TextAttribute.SWAP_COLORS, TextAttribute.JUSTIFICATION, TextAttribute.STRIKETHROUGH };
    for ( int i = 0 ; i < startTags.length; i++ ) {
      int pos = localCopy.indexOf( startTags[ i ] );
      while ( pos >= 0 ) {
        int endPos = localCopy.indexOf( endTags[ i ] );
        if ( endPos > pos ) {
          int tagLen = startTags[ i ].length();
          localCopy = localCopy.substring( 0, pos ) + localCopy.substring( pos + tagLen, endPos ) + localCopy.substring( endPos + tagLen + 1 );
          Object value = null;
          switch ( i ) {
            case 0: //TextAttribute.WEIGHT:
              value  = new Float( TextAttribute.WEIGHT_BOLD.floatValue() ); break;
            case 1: //TextAttribute.POSTURE:
              value  = new Float( TextAttribute.POSTURE_OBLIQUE.floatValue() ); break;
            case 2: //TextAttribute.UNDERLINE:
              value  = new Float( TextAttribute.UNDERLINE_ON.floatValue() ); break;
            case 3: //TextAttribute.SUPERSCRIPT:
            case 4: //TextAttribute.SUPERSCRIPT:
              value  = new Float( i == 3 ? TextAttribute.SUPERSCRIPT_SUPER.floatValue() : TextAttribute.SUPERSCRIPT_SUB.floatValue() ); break;
            case 5: //TextAttribute.SWAP_COLORS:
              value  = TextAttribute.SWAP_COLORS_ON; break;
            case 6: //TextAttribute.JUSTIFICATION:
              value  = new Float( TextAttribute.JUSTIFICATION_FULL.floatValue() ); break;
            case 7: //TextAttribute.STRIKETHROUGH:
              value  = TextAttribute.STRIKETHROUGH_ON; break;
          }
          int numAttribs = attributes.size();
          for ( int j = 0; j < numAttribs; j++ ) {
            Attrib attr = (Attrib)attributes.get( j );
            if ( attr.start > pos )
              attr.start -= tagLen;
            if ( attr.end > pos )
              attr.end -= tagLen;
            if ( attr.start > endPos )
              attr.start -= tagLen + 1;
            if ( attr.end > endPos )
              attr.end -= tagLen + 1;
          }
          attributes.add( new Attrib( keys [ i ], value, pos, endPos - tagLen ));
        }
        pos = localCopy.indexOf( startTags[ i ] );
      }
    }
    
    AttributedString as = new AttributedString( localCopy, font.getAttributes());
    int numAttribs = attributes.size();
    for ( int i = 0; i < numAttribs; i++ ) {
      Attrib attr = (Attrib)attributes.get( i );
      as.addAttribute( attr.type, attr.value, attr.start, attr.end );
    }
    lengths[ 0 ] = localCopy.length();
    return as;
  }
  
  
  /**
   * Set the clip state
   * @param state true to clip siblings
   */
  public void setClip( boolean state )
  {
    clip = state;
    init();
    repaint();
  }
  
  /**
   * Get the clip state
   * @return true if siblings are clipped
   */
  public boolean getClip()
  {
    return clip;
  }

  /**
   * Get the area in which the text flows
   * @return
   */
  public Area getFlowArea()
  {
    return flowArea;
  }

  /**
   * Setthe area into which the text is to flow
   * @param newArea
   */
  public void setFlowArea( Area newArea )
  {
    flowArea = newArea;
    repaint();
  }

  /**
   * Turn the value anti-aliasing on or off. By default the anti-aliasing is on
   * @param aa true to turn the anti-aliasing on
   */
  public void setAntiAlias( boolean aa )
  {
    antiAlias = aa
        ? RenderingHints.VALUE_ANTIALIAS_ON
        : RenderingHints.VALUE_ANTIALIAS_OFF;
  }
  
  /**
   * Get the value anti-aliasing value
   * @param aa true if anti-aliasing is on
   */
  public boolean getAntiAlias()
  {
    return ( antiAlias == RenderingHints.VALUE_ANTIALIAS_ON );
  }

  /**
   * Set the rendering hint for quality or speed
   * @param rd true for quality, false for speed
   */
  public void setRendering( boolean rd )
  {
    rendering = rd
        ? RenderingHints.VALUE_RENDER_QUALITY
        : RenderingHints.VALUE_RENDER_SPEED;
  }

  /**
   * Get the rendering hint
   * @return true if rendering is to favour quality over speed
   */
  public boolean getRendering()
  {
     return rendering == RenderingHints.VALUE_RENDER_QUALITY;
  }
  
  /**
   * Set a texture for paints
   * @param obj the Paint object, for example a GradientPaint
   */
  public void setTexture( Object obj )
  {
    if ( obj instanceof GradientPaint ) 
      texture = new GradientPaint( 0, 0, Color.white, getSize().width * 2, 0, Color.green );    
    else 
      texture = ( Paint )obj;    
  }
  
  /**
   * Set the composite instance
   * @param cp the composite e.g. AlphaComposite
   */
  public void setComposite( boolean cp )
  {
    composite = cp
        ? AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.5f )
        : null;
  }

  /**
   * Get the composite setup state
   * @return true if a compisite is used 
   */
  public boolean getComposite()
  {
    return composite != null; 
  }
  
  /**
   * Set the way in which the image is presented.
   * @param imgType = 0 to create a buffered image of TYPE_INT_ARGB,<br>
   * 1 = this class does not set up a buffered image or,<br>
   * >2 = BufferedImage.<<type>> + 2 where  type is the BufferedImage type constant
   */
  public void setImageType( int imgType )
  {
    if ( imgType == 0 ) {
//      if ( this instanceof AnimationContext ) {
//        imageType = 2;
//      }
//      else
        imageType = 1;
    }
    else
      imageType = imgType;
    bimg = null;
  }
  
  /**
   * Get the iamge type used for buffering the animation
   * @param the image type flag
   */
  public int getImageType()
  {
    return imageType;
  }

  /**
   * Set the number of columns in which to divide the flow area
   * @param columns the number of columns, if <1 a single flow area is used
   */
  public void setNumColumns( int columns )
  {
    cols = columns;
  }
  
  /**
   * Get the number of columns into which the flow area is divided
   * @return the number of columns
   */
  public int getNumColumns()
  {
    return cols;
  }

  /**
   * Set the column spacing as the number of pixels between each column. The 
   * spacing has no effect if there is a single column.
   * @param spacing the column spacing in pixels
   */
  public void setColSpacing( int spacing )
  {
    colSpacing = spacing;
  }
  
  /**
   * Get the column spacing
   * @return the spacing in pixels
   */
  public int getColSpacing()
  {
    return colSpacing;
  }
  
  /**
   * Set the label/text of the component. Usage depends on the concrete component
   * type
   * @param text the new text.
   */
  public void setText( String newText )
  {
    if ( newText != null ) {
      if ( newText.indexOf( "html" ) >= 0 )
        putClientProperty( "html", BasicHTML.createHTMLView( this, newText ));
    }
    
    label = newText;
    
    attributedStrings = null;
  }
  
  /**
   * Get the label/text of the component.
   * @return the component text.
   */  
  public String getText()
  {
    View v = (View)getClientProperty( "html" );
    if ( v == null )
      return label;
    else {
      try {
        Document doc = v.getDocument();
        return doc.getText( 0, doc.getLength() -1 );
      }
      catch ( BadLocationException ble ) {
        return "<html></html>";
      }
    }
  }

  /**
   * A structure for holding references to the attributes of the string
   */ 
  protected class Attrib
  {
    int start, end;
    AttributedCharacterIterator.Attribute type;
    Object value;
    
    /**
     * Create a new Attrib holder
     * @param t the type of attribute
     * @param v the value of the attribute
     * @param s the starting offset
     * @param e the ending offset
     */
    public Attrib( AttributedCharacterIterator.Attribute t, Object v, int s, int e )
    {
      type = t;
      value = v;
      start = s;
      end = e;
    }
  }
  
  
  //- JDK 1.5 methods-----------------------------------------------------------
  /**
  * Replaces each substring of this string that matches the literal target
  * sequence with the specified literal replacement sequence. The 
  * replacement proceeds from the beginning of the string to the end, for 
  * example, replacing "aa" with "b" in the string "aaa" will result in 
  * "ba" rather than "ab".
  * Pulled from JDK 1.5 for compatibility with 1.4
  * @param source the source text/string
  * @param target The sequence of char values to be replaced
  * @param replacement The replacement sequence of char values
  * @return The resulting string
  */
  public static String replace( String source, CharSequence target, CharSequence replacement) 
  {
      return Pattern.compile(target.toString(), 0x10/*Pattern.LITERAL*/).matcher(
          source).replaceAll( quoteReplacement(replacement.toString()));
  }

  /**
   * Returns a literal replacement <code>String</code> for the specified
   * <code>String</code>.
   *
   * This method produces a <code>String</code> that will work
   * use as a literal replacement <code>s</code> in the
   * <code>appendReplacement</code> method of the {@link Matcher} class.
   * The <code>String</code> produced will match the sequence of characters
   * in <code>s</code> treated as a literal sequence. Slashes ('\') and
   * dollar signs ('$') will be given no special meaning.
   *
   * @param  s The string to be literalized
   * @return  A literal string replacement
   * @since 1.5
   */
  public static String quoteReplacement( String s ) 
  {
      if ((s.indexOf('\\') == -1) && (s.indexOf('$') == -1))
          return s;

      StringBuffer sb = new StringBuffer();
      for ( int i = 0; i < s.length(); i++ ) {
        char c = s.charAt(i);
        if ( c == '\\' ) {
          sb.append('\\'); 
          sb.append('\\');
        }
        else if ( c == '$' ) {
          sb.append('\\'); 
          sb.append('$');
        }
        else 
          sb.append(c);
      }
      return sb.toString();
  }
  //- JDK 1.5 methods-----------------------------------------------------------
}
