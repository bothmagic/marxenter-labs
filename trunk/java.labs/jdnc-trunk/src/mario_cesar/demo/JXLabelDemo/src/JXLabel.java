/*
 * JXLabel.java
 *
 * Created on 15 de Janeiro de 2007, 19:32
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicHTML;

/**
 * @author Mário César
 */
public class JXLabel extends JLabel implements SwingConstants {
    //gradientDirection value declarations...
    public static final int TOP_TO_BOTTOM = 0;
    public static final int BOTTOM_TO_TOP = 1;
    public static final int RIGHT_TO_LEFT = 2;
    public static final int LEFT_TO_RIGHT = 3;
    //renderBackground value declarations...
    public static final int BACKGROUND_COLOR = 0;
    public static final int GRADIENT = 1;
    public static final int TILED_IMAGE = 2;
    //textOrientation value declarations...
    public static final int NORMAL = 0;
    public static final int INVERTED = 180;
    public static final int VERTICAL_LEFT = 270;
    public static final int VERTICAL_RIGHT = 90;
    //Property declarations...
    private Icon backgroundImage = null;
    private Color colorFrom = Color.blue;
    private Color colorTo = Color.white;
    private int gradientDirection = TOP_TO_BOTTOM;
    private int renderBackground = GRADIENT;
    private int textOrientation = NORMAL;
    //Private variables...
    private RenderingHints renderHints;
    private FontRenderContext fontRenderContext = new FontRenderContext(null,true,true);
    private boolean painting = false;
    
    
    //Constructors...
    
    
    
    /**
     * Creates a <code>JXLabel</code> instance with the specified
     * text, image, and horizontal alignment.
     * The label is centered vertically in its display area.
     * The text is on the trailing edge of the image.
     *
     * @param text  The text to be displayed by the label.
     * @param icon  The image to be displayed by the label.
     * @param horizontalAlignment  One of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> or
     *           <code>TRAILING</code>.
     */
    public JXLabel(String text, Icon icon, int horizontalAlignment) {
        super();
        renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        setText(text);
        BasicHTML.updateRenderer(this, getText());
        setIcon(icon);
        setHorizontalAlignment(horizontalAlignment);
    }
    /**
     * Creates a <code>JXLabel</code> instance with the specified
     * text and horizontal alignment.
     * The label is centered vertically in its display area.
     *
     * @param text  The text to be displayed by the label.
     * @param horizontalAlignment  One of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> or
     *           <code>TRAILING</code>.
     */
    public JXLabel(String text, int horizontalAlignment) {
        this(text, null, horizontalAlignment);
    }
    /**
     * Creates a <code>JXLabel</code> instance with the specified text.
     * The label is aligned against the leading edge of its display area,
     * and centered vertically.
     *
     * @param text  The text to be displayed by the label.
     */
    public JXLabel(String text) {
        this(text, null, LEADING);
        
    }
    /**
     * Creates a <code>JXLabel</code> instance with the specified
     * image and horizontal alignment.
     * The label is centered vertically in its display area.
     *
     * @param image  The image to be displayed by the label.
     * @param horizontalAlignment  One of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>, 
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> or
     *           <code>TRAILING</code>.
     */
    public JXLabel(Icon image, int horizontalAlignment) {
        this(null, image, horizontalAlignment);
    }
    /**
     * Creates a <code>JXLabel</code> instance with the specified image.
     * The label is centered vertically and horizontally
     * in its display area.
     *
     * @param image  The image to be displayed by the label.
     */
    public JXLabel(Icon image) {
        this(null, image, CENTER);
    }
    /**
     * Creates a <code>JXLabel</code> instance with 
     * no image and with an empty string for the title.
     * The label is centered vertically 
     * in its display area.
     * The label's contents, once set, will be displayed on the leading edge 
     * of the label's display area.
     */
    public JXLabel() {
        this("", null, LEADING);
    }
    
    
    
    //Properties...
    
    
    
    /**
     * Returns the current height of this component.
     * This method is preferable to writing
     * <code>component.getBounds().height</code>, or
     * <code>component.getSize().height</code> because it doesn't cause any
     * heap allocations.
     * 
     * 
     * @return the current height of this component
     */
    public int getHeight() {
        int retValue = super.getHeight();
        if (painting && textOrientation != NORMAL && textOrientation != INVERTED) {
            retValue = super.getWidth();
        }
        return retValue;
    }
    /**
     * Returns the alignment of the label's contents along the X axis.
     *
     * @return   The value of the horizontalAlignment property, one of the 
     *           following constants defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>, 
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> or
     *           <code>TRAILING</code>.
     *
     * @see #setHorizontalAlignment
     * @see SwingConstants
     */
    public int getHorizontalAlignment() {
        int ret = super.getHorizontalAlignment();
        if (painting) {
            int vert[] = getRelativeAlignment();
            ret = vert[0];
        }
        return ret;
    }
    /**
     * If a border has been set on this component, returns the
     * border's insets; otherwise calls <code>super.getInsets</code>.
     * 
     * 
     * @return the value of the insets property
     * @see #setBorder
     */
    public Insets getInsets() {
        Insets insets = super.getInsets();
        if (painting) {
            if (textOrientation == VERTICAL_LEFT) {
                int temp = insets.bottom;
                insets.bottom = insets.left;
                insets.left = insets.top;
                insets.top = insets.right;
                insets.right = temp;
            } else if (textOrientation == VERTICAL_RIGHT) {
                int temp = insets.bottom;
                insets.bottom = insets.right;
                insets.right = insets.top;
                insets.top = insets.left;
                insets.left = temp;
            }
        }
        return insets;    }
    /**
     * Returns an <code>Insets</code> object containing this component's inset
     * values.  The passed-in <code>Insets</code> object will be reused
     * if possible.
     * Calling methods cannot assume that the same object will be returned,
     * however.  All existing values within this object are overwritten.
     * If <code>insets</code> is null, this will allocate a new one.
     * 
     * 
     * @param insets the <code>Insets</code> object, which can be reused
     * @return the <code>Insets</code> object
     * @see #getInsets
     * @beaninfo expert: true
     */
    public Insets getInsets(Insets insets) {
        insets = super.getInsets(insets);
        if (painting) {
            if (textOrientation == VERTICAL_LEFT) {
                int temp = insets.bottom;
                insets.bottom = insets.left;
                insets.left = insets.top;
                insets.top = insets.right;
                insets.right = temp;
            } else if (textOrientation == VERTICAL_RIGHT) {
                int temp = insets.bottom;
                insets.bottom = insets.right;
                insets.right = insets.top;
                insets.top = insets.left;
                insets.left = temp;
            }
        }
        return insets;
    }
    /**
     * If the maximum size has been set to a non-<code>null</code> value
     * just returns it.  If the UI delegate's <code>getMaximumSize</code>
     * method returns a non-<code>null</code> value then return that;
     * otherwise defer to the component's layout manager.
     * 
     * 
     * @return the value of the <code>maximumSize</code> property
     * @see #setMaximumSize
     * @see ComponentUI
     */
    public Dimension getMaximumSize() {
        Dimension d = super.getMaximumSize();
        if (textOrientation != NORMAL && textOrientation != INVERTED) {
            int width = d.width;
            d.width = d.height;
            d.height = width;
        }
        return d;
    }
    /**
     * If the minimum size has been set to a non-<code>null</code> value
     * just returns it.  If the UI delegate's <code>getMinimumSize</code>
     * method returns a non-<code>null</code> value then return that; otherwise
     * defer to the component's layout manager.
     * 
     * 
     * @return the value of the <code>minimumSize</code> property
     * @see #setMinimumSize
     * @see ComponentUI
     */
    public Dimension getMinimumSize() {
        Dimension d = super.getMinimumSize();
        if (textOrientation != NORMAL && textOrientation != INVERTED) {
            int width = d.width;
            d.width = d.height;
            d.height = width;
        }
        return d;
    }
    /**
     * If the <code>preferredSize</code> has been set to a
     * non-<code>null</code> value just returns it.
     * If the UI delegate's <code>getPreferredSize</code>
     * method returns a non <code>null</code> value then return that;
     * otherwise defer to the component's layout manager.
     * 
     * 
     * @return the value of the <code>preferredSize</code> property
     * @see #setPreferredSize
     * @see ComponentUI
     */
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        if (textOrientation != NORMAL && textOrientation != INVERTED) {
            int width = d.width;
            d.width = d.height;
            d.height = width;
        }
        return d;
    }
    /**
     * Returns the alignment of the label's contents along the Y axis.
     *
     * @return   The value of the verticalAlignment property, one of the 
     *           following constants defined in <code>SwingConstants</code>:
     *           <code>TOP</code>,
     *           <code>CENTER</code>, or
     *           <code>BOTTOM</code>.
     *
     * @see SwingConstants
     * @see #setVerticalAlignment
     */
    public int getVerticalAlignment() {
        int ret = super.getVerticalAlignment();
        if (painting) {
            int vert[] = getRelativeAlignment();
            ret = vert[1];
        }
        return ret;
    }
    /**
     * Returns the current width of this component.
     * This method is preferable to writing
     * <code>component.getBounds().width</code>, or
     * <code>component.getSize().width</code> because it doesn't cause any
     * heap allocations.
     * 
     * 
     * @return the current width of this component
     */
    public int getWidth() {
        int retValue = super.getWidth();
        if (painting && textOrientation != NORMAL && textOrientation != INVERTED) {
            retValue = super.getHeight();
        }
        return retValue;
    }
    
    
    
    //New properties...
    
    
    
    public Icon getBackgroundImage() {
        return backgroundImage;
    }
    public void setBackgroundImage(Icon icon) {
        Icon oldValue = backgroundImage;
        backgroundImage = icon;
        firePropertyChange("backgroundImage", oldValue, backgroundImage);
        repaint();
    }
    public Color getColorFrom() {
        return colorFrom;
    }
    public void setColorFrom(Color colorFrom) {
        Color old = this.colorFrom;
        this.colorFrom = colorFrom;
        firePropertyChange("colorFrom", old, colorFrom);
        repaint();
    }
    public Color getColorTo() {
        return colorTo;
    }
    public void setColorTo(Color colorTo) {
        Color old = this.colorTo;
        this.colorTo = colorTo;
        firePropertyChange("colorTo", old, colorTo);
        repaint();
    }
    public int getGradientDirection() {
        return gradientDirection;
    }
    public void setGradientDirection(int gradientDirection) {
        int old = this.gradientDirection;
        this.gradientDirection = checkGradientKey(gradientDirection, "gradientDirection");
        firePropertyChange("gradientDirection", old, gradientDirection);
        repaint();
    }
    public int getRenderBackground() {
        return renderBackground;
    }
    public void setRenderBackground(int renderBackground) {
        int old = this.renderBackground;
        this.renderBackground = checkRenderBackgrdKey(renderBackground, "renderBackground");
        firePropertyChange("renderBackground", old, renderBackground);
        repaint();
    }
    public int getTextOrientation() {
        return textOrientation;
    }
    public void setTextOrientation(int textOrientation) {
        int old = this.textOrientation;
        this.textOrientation = checkOrientationKey(textOrientation, "textOrientation");
        firePropertyChange("textOrientation", old, textOrientation);
        repaint();
    }
    
    
    
    //Private and protected methods...
    
    
    
    protected int checkGradientKey(int key, String message) {
        if ((key == TOP_TO_BOTTOM) || (key == BOTTOM_TO_TOP) || 
                (key == RIGHT_TO_LEFT) || (key == LEFT_TO_RIGHT)) {
            return key;
        }
        else {
            throw new IllegalArgumentException(message);
        }
    }
    protected int checkOrientationKey(int key, String message) {
        if ((key == NORMAL) || (key == INVERTED) || (key == VERTICAL_LEFT) || 
                (key == VERTICAL_RIGHT)) {
            return key;
        }
        else {
            throw new IllegalArgumentException(message);
        }
    }
    protected int checkRenderBackgrdKey(int key, String message) {
        if ((key == BACKGROUND_COLOR) || (key == GRADIENT) || (key == TILED_IMAGE)) {
            return key;
        }
        else {
            throw new IllegalArgumentException(message);
        }
    }
    private int[] getRelativeAlignment() {
        int ret[] = {0, 0};
        switch (textOrientation) {
            case NORMAL:
                ret[0] = super.getHorizontalAlignment();
                ret[1] = super.getVerticalAlignment();
                break;
            case INVERTED:
                switch (super.getHorizontalAlignment()) {
                    case LEFT:
                        ret[0] = RIGHT;
                        break;
                    case RIGHT:
                        ret[0] = LEFT;
                        break;
                    case LEADING:
                        ret[0] = TRAILING;
                        break;
                    case TRAILING:
                        ret[0] = LEADING;
                        break;
                    default:
                        ret[0] = CENTER;
                }
                switch (super.getVerticalAlignment()) {
                    case TOP:
                        ret[1] = BOTTOM;
                        break;
                    case BOTTOM:
                        ret[1] = TOP;
                        break;
                    default:
                        ret[1] = CENTER;
                }
                break;
            case VERTICAL_LEFT:
                switch (super.getHorizontalAlignment()) {
                    case LEFT:
                    case LEADING:
                        ret[1] = TOP;
                        break;
                    case RIGHT:
                    case TRAILING:
                        ret[1] = BOTTOM;
                        break;
                    default:
                        ret[1] = CENTER;
                }
                switch (super.getVerticalAlignment()) {
                    case TOP:
                        ret[0] = RIGHT;
                        break;
                    case BOTTOM:
                        ret[0] = LEFT;
                        break;
                    default:
                        ret[0] = CENTER;
                }
                break;
                
            default: //VERTICAL_RIGHT
                switch (super.getHorizontalAlignment()) {
                    case LEFT:
                    case LEADING:
                        ret[1] = BOTTOM;
                        break;
                    case RIGHT:
                    case TRAILING:
                        ret[1] = TOP;
                        break;
                    default:
                        ret[1] = CENTER;
                }
                switch (super.getVerticalAlignment()) {
                    case TOP:
                        ret[0] = LEFT;
                        break;
                    case BOTTOM:
                        ret[0] = RIGHT;
                        break;
                    default:
                        ret[0] = CENTER;
                }
        }
        return ret;
    }
    
    
    
    //Overrided methods...
    
    
    
    /**
     * Invoked by Swing to draw components.
     * Applications should not invoke <code>paint</code> directly,
     * but should instead use the <code>repaint</code> method to
     * schedule the component for redrawing.
     * <p>
     * This method actually delegates the work of painting to three
     * protected methods: <code>paintComponent</code>,
     * <code>paintBorder</code>,
     * and <code>paintChildren</code>.  They're called in the order
     * listed to ensure that children appear on top of component itself.
     * Generally speaking, the component and its children should not
     * paint in the insets area allocated to the border. Subclasses can
     * just override this method, as always.  A subclass that just
     * wants to specialize the UI (look and feel) delegate's
     * <code>paint</code> method should just override
     * <code>paintComponent</code>.
     * 
     * 
     * @param g  the <code>Graphics</code> context in which to paint
     * @see #paintComponent
     * @see #paintBorder
     * @see #paintChildren
     * @see #getComponentGraphics
     * @see #repaint
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //Pints the background...
        if (renderBackground == GRADIENT) {
            //Pints the gradient...
            if (super.isOpaque()) {
                super.setOpaque(false);
            }
            GradientPaint gradient;
            if (gradientDirection == TOP_TO_BOTTOM) {
                gradient = new GradientPaint(super.getWidth() / 2, 0, colorFrom, super.getWidth() / 2, super.getHeight(), colorTo);
            } else if (gradientDirection == BOTTOM_TO_TOP) {
                gradient = new GradientPaint(super.getWidth() / 2, super.getHeight(), colorFrom, super.getWidth() / 2, 0, colorTo);
            } else if (gradientDirection == RIGHT_TO_LEFT) {
                gradient = new GradientPaint(super.getWidth(), super.getHeight() / 2, colorFrom, 0, super.getHeight() / 2, colorTo);
            } else { // LEFT_TO_RIGHT
                gradient = new GradientPaint(0, super.getHeight() / 2, colorFrom, super.getWidth(), super.getHeight() / 2, colorTo);
            }
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, super.getWidth(), super.getHeight());
            //If the gradient is cyclic...
            //gradient = new GradientPaint(0, 0, colorFrom, this.getWidth(), this.getHeight(), colorTo, true);
            //g2d.setPaint(gradient);
        } else if (renderBackground == TILED_IMAGE && backgroundImage != null) {
            //Pints the tiled image...
            if (super.isOpaque()) {
                super.setOpaque(false);
            }
            for (int y = 0; y < this.getHeight(); y += backgroundImage.getIconHeight()) {
                for (int x = 0; x < this.getWidth(); x += backgroundImage.getIconWidth()) {
                    backgroundImage.paintIcon(this, g2d, x, y);
                }
            }
        } // If it's not GRADIENT or TILED_IMAGE, then it's BACKGROUND_COLOR...
        //Backuping everything...
        AffineTransform oldTransform = g2d.getTransform();
        RenderingHints oldHints = g2d.getRenderingHints();
        //Set font, color and and renderingHints...
        g2d.setFont(super.getFont());
        g2d.setColor(super.getForeground());
        g2d.setRenderingHints(renderHints);
        //Get the x and y to transform...
        int x = 0, y = 0;
        if (textOrientation == VERTICAL_LEFT) {
            x = 0;
            y = getHeight() - 1;
        } else if (textOrientation == VERTICAL_RIGHT) {
            x = getWidth() - 1;
            y = 0;
        } else if (textOrientation == INVERTED) {
            x = getWidth() - 1;
            y = getHeight() - 1;
        }
        //Apply the transformation and the rotation...
        AffineTransform trans = new AffineTransform();
        trans.concatenate(oldTransform);
        trans.translate(x, y);
        trans.rotate(Math.PI / 180 * textOrientation);//, super.getWidth() / 2, super.getHeight() / 2);
        g2d.setTransform(trans);
        //Painting
        painting = true;
        super.paint(g);
        painting = false;
        //Going back...
        g2d.setTransform(oldTransform);
        g2d.setRenderingHints(oldHints);
    }
}
