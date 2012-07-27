/*
 * $Id: JXTitledSeparator2.java 2762 2008-10-09 14:48:08Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import javax.swing.Icon;
import static javax.swing.SwingConstants.*;
import javax.swing.plaf.ComponentUI;

/**
 * <p>A simple horizontal separator that contains a title.<br/>
 *
 * <p>JXTitledSeparator allows you to specify the title via the {@link #setTitle} method.
 * The title alignment may be specified by using the {@link #setHorizontalAlignment}
 * method, and accepts all the same arguments as the {@link javax.swing.JLabel#setHorizontalAlignment}
 * method.</p>
 *
 * <p>In addition, you may specify an Icon to use with this separator. The icon
 * will appear "leading" the title (on the left in left-to-right languages,
 * on the right in right-to-left languages). To change the position of the
 * title with respect to the icon, call {@link #setHorizontalTextPosition}.</p>
 *
 * <p>The default font and color of the title comes from the <code>LookAndFeel</code>, mimicking
 * the font and color of the {@link javax.swing.border.TitledBorder}</p>
 *
 * <p>Here are a few example code snippets:
 * <pre><code>
 *  //create a plain separator
 *  JXTitledSeparator sep = new JXTitledSeparator();
 *  sep.setText("Customer Info");
 *
 *  //create a separator with an icon
 *  sep = new JXTitledSeparator();
 *  sep.setText("Customer Info");
 *  sep.setIcon(new ImageIcon("myimage.png"));
 *
 *  //create a separator with an icon to the right of the title,
 *  //center justified
 *  sep = new JXTitledSeparator();
 *  sep.setText("Customer Info");
 *  sep.setIcon(new ImageIcon("myimage.png"));
 *  sep.setHorizontalAlignment(SwingConstants.CENTER);
 *  sep.setHorizontalTextPosition(SwingConstants.TRAILING);
 * </code></pre>
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXTitledSeparator2 extends JXComponent {
    
    private static final String uiClassID = "TitledSeparator";

    private String title;
    private int horizontalAlignment;
    private int horizontalTextPosition;
    private Icon icon;
    private int iconTitleGap = 4;

    /**
     * Creates a new instance of <code>JXTitledSeparator</code>. The default title is simply
     * an empty string. Default justification is <code>LEADING</code>, and the default
     * horizontal text position is <code>TRAILING</code> (title follows icon)
     */
    public JXTitledSeparator2() {
        updateUI();
    }





    /**
     * Creates a new instance of <code>JXTitledSeparator</code> with the specified title. Default horizontal alignment
     * is <code>LEADING</code>, and the default horizontal text position is <code>TRAILING</code> (title follows icon)
     *
     * @param title The title for this separator
     */
    public JXTitledSeparator2(String title) {
        this();
        setTitle(title);
    }





    /**
     * Creates a new instance of <code>JXTitledSeparator</code> with the specified title and horizontal alignment. The
     * default horizontal text position is <code>TRAILING</code> (title follows icon)
     *
     * @param title The separator title
     * @param horizontalAlignment The place within the separator to place the title.
     */
    public JXTitledSeparator2(String title, int horizontalAlignment) {
        this(title);
        setHorizontalAlignment(horizontalAlignment);
    }





    /**
     * Creates a new instance of <code>JXTitledSeparator</code> with the specified title, icon, and horizontal
     * alignment. The default horizontal text position is <code>TRAILING</code> (title follows icon)
     *
     * @param title The separator title.
     * @param horizontalAlignment The place to put the title and icon within the separator.
     * @param icon An icon to display next to the title.
     */
    public JXTitledSeparator2(String title, int horizontalAlignment, Icon icon) {
        this(title, horizontalAlignment);
        setIcon(icon);
    }





    /**
     * @return {@code "TitledSeparator"}.
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }





    /**
     * Override this method if you don't want to implement the whole ComponentAddon API. Example implementation would be
     * for MyComponent.createUI() you would return BasicMyComponentUI.createUI(this). By default this returns a UI
     * delegate which supports the Painter additions added in this component.
     *
     * @return The fallback UI for this component.
     */
    @Override
    protected ComponentUI createUI() {
        return org.jdesktop.swingx.plaf.basic.BasicTitledSeparatorUI.createUI(this);
    }





    /**
     * Returns the alignment of the title contents along the X axis.
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
     * @see javax.swing.SwingConstants
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }





    /**
     * Returns the graphic image (glyph, icon) that the
     * <code>JXTitledSeparator</code> displays. This component fully supports the DynamicObject interface for this
     * object and will update on icon change.
     *
     * @return an Icon
     * @see #setIcon
     */
    public Icon getIcon() {
        return icon;
    }





    /**
     * Gets the title.
     *
     * @return the title being used for this <code>JXTitledSeparator</code>.
     *         This will be the raw title text, and so may include html tags etc
     *         if they were so specified in #setTitle.
     */
    public String getTitle() {
        return title;
    }





    /**
     * Returns the horizontal position of the title's text,
     * relative to the icon.
     *
     * @return   One of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> or
     *           <code>TRAILING</code>.
     *
     * @see javax.swing.SwingConstants
     */
    public int getHorizontalTextPosition() {
        return horizontalTextPosition;
    }





    /**
     * Returns the amount of space between the title and the icon
     * displayed in the header.
     *
     * @return an int equal to the number of pixels between the title
     *         and the icon.
     * @see #setIconTitleGap
     */
    public int getIconTitleGap() {
        return iconTitleGap;
    }





    /**
     * <p>Sets the alignment of the title along the X axis. If leading, then the title will lead the separator (in
     * left-to-right languages, the title will be to the left and the separator to the right). If centered, then a
     * separator will be to the left, followed by the title (centered), followed by a separator to the right. Trailing
     * will have the title on the right with a separator to its left, in left-to-right languages.</p>
     *
     * <p>LEFT and RIGHT always position the text left or right of the separator, respectively, regardless of the
     * language orientation.</p>
     *
     * @param horizontalAlignment One of the following constants defined in <code>SwingConstants</code>:
     *   <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>, <code>LEADING</code> (the default) or
     *   <code>TRAILING</code>.
     * @see javax.swing.SwingConstants
     * @see #getHorizontalAlignment
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        validateHorizontalArg("horizontalAlignment", horizontalAlignment);
        int old = getHorizontalAlignment();
        this.horizontalAlignment = horizontalAlignment;
        firePropertyChange("horizontalAlignment", old, getHorizontalAlignment());
    }





    /**
     * Defines the icon this component will display. If the value of icon is null, nothing is displayed.
     *
     * <p> The default value of this property is null.
     *
     * @param icon The icon.
     * @see #setHorizontalTextPosition
     * @see #getIcon
     */
    public void setIcon(Icon icon) {
        // @todo Support DynamicObject icon interface for repaint events
        Icon old = getIcon();
        this.icon = icon;
        firePropertyChange("icon", old, getIcon());
    }





    /**
     * Sets the title for the separator. This may be simple html, or plain
     * text.
     *
     * @param title the new title. Any string input is acceptable
     */
    public void setTitle(String title) {
        if (title == null) {
            title = "";
        }
        String old = getTitle();
        this.title = title;
        firePropertyChange("title", old, getTitle());
    }





    /**
     * Sets the horizontal position of the title's text,
     * relative to the icon.
     *
     * @param horizontalTextPosition  One of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code>, or
     *           <code>TRAILING</code> (the default).
     * @throws IllegalArgumentException if the horizontalTextPosition does not match one of
     *         the accepted inputs.
     */
    public void setHorizontalTextPosition(int horizontalTextPosition) {
        validateHorizontalArg("horizontalTextPosition", horizontalTextPosition);
        int old = getHorizontalTextPosition();
        this.horizontalTextPosition = horizontalTextPosition;
        firePropertyChange("horizontalTextPosition", old, getHorizontalTextPosition());
    }





    /**
     * If both the icon and text properties are set, this property defines the space between them.
     *
     * <p> The default value of this property is 4 pixels.
     *
     * <p> This is a JavaBeans bound property.
     *
     * @param iconTitleGap The icon text gap.
     * @see #getIconTitleGap
     */
    public void setIconTitleGap(int iconTitleGap) {
        int old = getIconTitleGap();
        this.iconTitleGap = iconTitleGap;
        firePropertyChange("iconTitleGap", old, getIconTitleGap());
    }





    /**
     * Validate the given argument is one of <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>,
     * <code>RIGHT</code>, <code>LEADING</code>, or <code>TRAILING</code>
     *
     * @param propName The property name we are validating for error printing and debug.
     * @param arg The argument to validate.
     */
    private void validateHorizontalArg(String propName, int arg) {
        switch (arg) {
            case LEADING:
            case TRAILING:
            case LEFT:
            case RIGHT:
            case CENTER:
                break;
            default:
                throw new IllegalArgumentException(propName + " is not a valid value: " + arg);
        }
    }
}
