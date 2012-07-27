/*
 * $Id: IconFactory.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.Image;
import java.awt.Paint;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.border.Border;

import org.jdesktop.swingx.effect.ReflectionEffect;
import org.jdesktop.swingx.icon.BorderIcon;
import org.jdesktop.swingx.icon.CompoundIcon;
import org.jdesktop.swingx.icon.DefaultScalableIcon;
import org.jdesktop.swingx.icon.EffectIcon;
import org.jdesktop.swingx.icon.MatteIcon;
import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.icon.ScalableIcon;
import org.jdesktop.swingx.icon.TempIcon;
import org.jdesktop.swingx.icon.compound.AbstractPolicy;
import org.jdesktop.swingx.icon.compound.GridPolicy;
import org.jdesktop.swingx.icon.compound.PilePolicy;
import org.jdesktop.swingx.icon.compound.StackPolicy;
import org.jdesktop.swingx.icon.range.IconRange;
import org.jdesktop.swingx.icon.range.ImageLoader;
import org.jdesktop.swingx.icon.range.ImageRange;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.icon.range.*;
import java.net.*;

/**
 * Factory class for creating different types of icon. This class is analogous
 * to BorderFactory. It is possible that icons are cached but no support is
 * currently provided.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class IconFactory {
    /**
     * Copy of the AbstractPolicys CONTAINER_SIZE static property. Use when
     * creating a Pile or Stack icon to specify that the subsequent icons should
     * fade to transparent at the last icon to get painted.
     */
    public static final int CONTAINER_SIZE = AbstractPolicy.CONTAINER_SIZE;
    /**
     * Copy of the AbstractPolicys ALL_VISIBLE static property. Use when
     * creating a Pile or Stack icon to specify that all icons should be opaque.
     */
    public static final int ALL_VISIBLE = AbstractPolicy.ALL_VISIBLE;

    private IconFactory() {
    }

    /**
     * Creates a dynamically loading Scalable icon from the given images.
     *
     * @param images A list of images to use as ranges for the returned icon.
     * @return The scalable icon.
     */
    public static ScalableIcon createScalableIcon(Image ...images) {
        return createScalableIcon(false, images);
    }





    /**
     * Creates a new ScalableIcon from the given images which are optionally
     * preloaded.
     *
     * @param preload {@code true} if the given images should be pre loaded.
     * @param images The images used to define the icon.
     * @return The scalable icon instance.
     */
    public static ScalableIcon createScalableIcon(boolean preload, Image ...images) {
        return new DefaultScalableIcon(preload, ImageRange.convert(images));
    }





    /**
     * Creates a ScalableIcon from the given icons.
     *
     * @param icons The icons used to represent the scalable icon.
     * @return The scalable icon instance.
     */
    public static ScalableIcon createScalableIcon(Icon ...icons) {
        return new DefaultScalableIcon(IconRange.convert(icons));
    }





    /**
     * Creates a ScalableIcon from the given files. The files are loaded when
     * needed using the pattern and sizes. If baseClass is null it is assumed
     * that the pattern is a File location and treated as such.
     * <p><strong>Example:</strong> The following code will create a scalable
     * icon that consists of 3 images of different sizes and scales between
     * them depending on which size is required by the icon.</p>
     * <p><pre><code>
     * {@code String pattern = "resources/Image%d.png";}
     * ScalableIcon icon = IconFactory.createScalableIcon(getClass(), pattern, 16, 32, 256);
     * </code></pre></p>
     *
     * @param baseClass The class to load resources from.
     * @param pattern The pattern to use to lookup sized images.
     * @param sizes The sizes of images to load.
     * @return A scalable icon.
     */
    public static ScalableIcon createScalableIcon(Class<?> baseClass, String pattern, int ...sizes) {
        return createScalableIcon(false, baseClass, pattern, sizes);
    }





    /**
     * Creates a ScalableIcon from the given files. The files are loaded when
     * needed using the pattern and sizes if preload is false otherwise they are
     * loaded synchronously immediately. If baseClass is null it is assumed
     * that the pattern is a File location and treated as such.
     *
     * @param preload True if the images for this icon should be loaded at construction.
     * @param baseClass The class to load resources from.
     * @param pattern the pattern used to find images.
     * @param sizes The sizes for the images.
     * @return The scalable icon.
     */
    public static ScalableIcon createScalableIcon(boolean preload, Class<?> baseClass, String pattern, int ...sizes) {
        return new DefaultScalableIcon(preload, ImageRange.convert(baseClass, pattern, sizes));
    }





    /**
     * Create a new scalable icon which wraps the image at the given URL. The width and height should be the width and
     * height of the given image.
     *
     * @param url The URL for the image.
     * @param width The width of the image.
     * @param height The height of the image.
     * @return The scalable icon for the image.
     */
    public static ScalableIcon createScalableIcon(URL url, int width, int height) {
        return new DefaultScalableIcon(new ImageRange(new URLImageLoader(url, width, height)));
    }





    /**
     * Create a scalable icon for the image at the given url and maybe preload the image.
     *
     * @param preload {@code true} to request that the image is preloaded.
     * @param url The url for the image.
     * @param width The width of the image.
     * @param height The height of the image.
     * @return The scalable icon.
     */
    public static ScalableIcon createScalableIcon(boolean preload, URL url, int width, int height) {
        return new DefaultScalableIcon(preload, new ImageRange(new URLImageLoader(url, width, height)));
    }





    /**
     * Creates a ScalableIcon for the given ImageLoaders.
     *
     * @param imageLoaders List of image loaders to use to create the icon.
     * @return The scalable icon.
     */
    public static ScalableIcon createScalableIcon(ImageLoader ...imageLoaders) {
        return createScalableIcon(false, imageLoaders);
    }





    /**
     * Creates a ScalableIcon for the given ImageLoaders.
     *
     * @param preload {@code true} if the icon should be loaded at construction.
     * @param imageLoaders The list of image loaders to use to create the icon.
     * @return The scalable icon.
     */
    public static ScalableIcon createScalableIcon(boolean preload, ImageLoader ...imageLoaders) {
        return new DefaultScalableIcon(preload, ImageRange.convert(imageLoaders));
    }





    /**
     * Create an icon which is composed of the given icons. The icons will be
     * laid directly on top of each other.
     *
     * @param icons The list of icons to concatenate.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundIcon(Icon ...icons) {
        return new CompoundIcon(icons);
    }





    /**
     * Creates a new CompoundIcon where the child icons are laid over one another
     * directly.
     *
     * @param icons The list of icons to create.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundOverlayIcon(Icon ...icons) {
        return createCompoundIcon(icons);
    }





    /**
     * Creates a new CompoundIcon using the given CompoundPolicy and icons.
     *
     * @param policy The policy to use to combine the icons.
     * @param icons The list of icons to combine.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundIcon(CompoundIcon.Policy policy, Icon ...icons) {
        return new CompoundIcon(policy, icons);
    }





    /**
     * Creates a new CompoundIcon using a GridPolicy. This lays out the child
     * icons in a grid fashion. The default layout is a single row and infinite
     * columns.
     *
     * @param icons The list of icons to combine.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundGridIcon(Icon ...icons) {
        return createCompoundIcon(new GridPolicy(), icons);
    }





    /**
     * Creates a new CompoundIcon using a GridPolicy with the given rows and
     * columns. A value of 0 for rows or cols means that that dimension will
     * expand indefinitely. Both rows and cols cannot be 0 at the same time.
     *
     * @param rows The number of rows in the grid.
     * @param cols The number of columns in the grid.
     * @param icons The list of icons to combine.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundGridIcon(int rows, int cols, Icon ...icons) {
        return createCompoundIcon(new GridPolicy(rows, cols), icons);
    }





    /**
     * Creates a new CompoundIcon with a GridPolicy with the given properties.
     *
     * @param rows The number of rows in the grid.
     * @param cols The number of columns in the grid.
     * @param hgap The gap between columns.
     * @param vgap The gap between rows.
     * @param icons The icons to combine.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundGridIcon(int rows, int cols, int hgap, int vgap, Icon ...icons) {
        return createCompoundIcon(new GridPolicy(rows, cols, hgap, vgap), icons);
    }





    /**
     * Creates a new CompoundIcon with a StackPolicy.
     *
     * @param icons The icons to combine.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundStackIcon(Icon ...icons) {
        return createCompoundIcon(new StackPolicy(), icons);
    }





    /**
     * Create a new stacked icon with the given proportions of the icon area
     * taken by the stack icons.
     *
     * @param proportionX The proportion of the x dimension to use for the stack.
     * @param proportionY The proportion of the y dimension to use for the stack.
     * @param icons The icons to combine.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundStackIcon(float proportionX, float proportionY, Icon ...icons) {
        return createCompoundIcon(new StackPolicy(proportionX, proportionY), icons);
    }





    /**
     * Create a new CompoundIcon with a StackPolicy that uses static offsets.
     *
     * @param offsetX The number of pixels to offset each stack layer.
     * @param offsetY The number of pixels to offset each stack layer.
     * @param scaleFactor The amount to scale each stack layer.
     * @param visibleIcons The total number of visible icons.
     * @param icons The icons to combine.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundStackIcon(int offsetX, int offsetY, float scaleFactor, int visibleIcons, Icon ...icons) {
        return createCompoundIcon(new StackPolicy(offsetX, offsetY, scaleFactor, visibleIcons), icons);
    }





    /**
     * Create a new CompundIcon for the given icons using the given scaleFactor
     * and with the given number of icons visible before opacity is 0.
     *
     * @param scaleFactor The factor by which to scale each stack layer.
     * @param visibleIcons The total number of visible icons.
     * @param icons The icons to combine.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundStackIcon(float scaleFactor, int visibleIcons, Icon ...icons) {
        return createCompoundIcon(new StackPolicy(scaleFactor, visibleIcons), icons);
    }





    /**
     * Create a new stacked compound icon with the given properties.
     *
     * @param proportionX The proportion of the x dimension to use for the stack.
     * @param proportionY The proportion of the y dimension to use for the stack.
     * @param scaleFactor The factor by which to scale each stack layer.
     * @param opacityFactor The amount each stack layer becomes transparent by.
     * @param icons The icons to combine.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundStackIcon(float proportionX, float proportionY, float scaleFactor, float opacityFactor, Icon ...icons) {
        return createCompoundIcon(new StackPolicy(proportionX, proportionY, scaleFactor, opacityFactor), icons);
    }





    /**
     * Create a new Stacked CompoundIcon with the given properties.
     *
     * @param proportionX The proportion of the x dimension to use for the stack.
     * @param proportionY The proportion of the y dimension to use for the stack.
     * @param scaleFactor The factor by which to scale each stack layer.
     * @param visibleIcons The total number of visible icons.
     * @param icons The icons to combine.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundStackIcon(float proportionX, float proportionY, float scaleFactor, int visibleIcons, Icon ...icons) {
        return createCompoundIcon(new StackPolicy(proportionX, proportionY, scaleFactor, visibleIcons), icons);
    }





    /**
     * Create a new Stacked CompoundIcon with the given stack direction. This can
     * be one SwingUtilities diagonal compass points (E.G. NORTH_EAST which is
     * the default).
     *
     * @param stackDirection The direction to expand the stack in.
     * @param icons The list of icons to combine.
     * @return The compound icon.
     */
    public static CompoundIcon createCompoundStackIcon(int stackDirection, Icon ...icons) {
        StackPolicy policy = new StackPolicy();
        policy.setStackDirection(stackDirection);
        return createCompoundIcon(policy, icons);
    }





    /**
     * Creates a new ConpundIcon using a PilePolicy. This type of icon stacks its
     * child icons on top of one another rotating then making it look like a
     * pile.
     *
     * @param icons Icon[]
     * @return CompoundIcon
     */
    public static CompoundIcon createCompoundPileIcon(Icon ...icons) {
        return createCompoundIcon(new PilePolicy(), icons);
    }





    /**
     * Creates a new CompoundIcon which piles icons on top of one another using
     * the given rotation hints.
     *
     * @param rotation double
     * @param rotationType RotationType
     * @param icons Icon[]
     * @return CompoundIcon
     */
    public static CompoundIcon createCompoundPileIcon(double rotation, PilePolicy.RotationType rotationType, Icon ...icons) {
        return createCompoundIcon(new PilePolicy(rotation, rotationType), icons);
    }





    /**
     * Create a new piled CompoundIcon using the given scale factor and visible
     * icons properties.
     *
     * @param scaleFactor float
     * @param visibleIcons int
     * @param icons Icon[]
     * @return CompoundIcon
     */
    public static CompoundIcon createCompoundPileIcon(float scaleFactor, int visibleIcons, Icon ...icons) {
        return createCompoundIcon(new PilePolicy(scaleFactor, visibleIcons), icons);
    }





    /**
     * Create a new piles CompoundIcon using the given properties.
     *
     * @param rotation double
     * @param rotationType RotationType
     * @param scaleFactor float
     * @param visibleIcons int
     * @param icons Icon[]
     * @return CompoundIcon
     */
    public static CompoundIcon createCompoundPileIcon(double rotation, PilePolicy.RotationType rotationType, float scaleFactor, int visibleIcons,
          Icon ...icons) {
        return createCompoundIcon(new PilePolicy(rotation, rotationType, scaleFactor, visibleIcons), icons);
    }





    /**
     * Returns a standard icon instance that can be used for place holding. The
     * returned icon is scalable with a preferred size of width by height.
     *
     * @param width int
     * @param height int
     * @return ScalableIcon
     */
    public static ScalableIcon createTempIcon(int width, int height) {
        return new TempIcon(width, height);
    }





    /**
     * Creates a standard icon instance that can be used for place holding. The
     * returned icon will have a foreground colour of {@code foreground}.
     *
     * @param width int
     * @param height int
     * @param foreground Color
     * @return ScalableIcon
     */
    public static ScalableIcon createTempIcon(int width, int height, Paint foreground) {
        return new TempIcon(width, height, foreground);
    }





    /**
     * Creates a standard icon instance that can be used for place holding. The
     * returned icon will have a foreground colour of {@code foreground} and a
     * background colour of {@code background}.
     *
     * @param width int
     * @param height int
     * @param foreground Color
     * @param background Color
     * @return ScalableIcon
     */
    public static ScalableIcon createTempIcon(int width, int height, Paint foreground, Paint background) {
        return new TempIcon(width, height, foreground, background);
    }





    /**
     * Creates a new Icon which is wrapped in the given Border.
     *
     * @param icon Icon
     * @param border Border
     * @return BorderIcon
     */
    public static BorderIcon createBorderIcon(Icon icon, Border border) {
        return new BorderIcon(icon, border);
    }





    /**
     * Creates a new icon which reflects its content.
     *
     * @param icon Icon
     * @return ReflectIcon
     */
    public static EffectIcon createReflectIcon(Icon icon) {
        return new EffectIcon(icon, new ReflectionEffect<Icon>());
    }





    /**
     * Create a new reflecting icon with the given padding between the source
     * icon and the reflection.
     *
     * @param icon The icon to reflect
     * @param gap The gap between the icon and the reflection.
     * @return The icon with reflection
     */
    public static EffectIcon createReflectIcon(Icon icon, int gap) {
        return new EffectIcon(icon, new ReflectionEffect<EffectIcon>(gap));
    }





    /**
     * Creates an Icon which fills its dimensions with the given background.
     *
     * @param background Paint
     * @return MatteIcon
     */
    public static MatteIcon createMatteIcon(Paint background) {
        return new MatteIcon(background);
    }





    /**
     * Creates a new MatteIcno with a null background and the given dimensions.
     *
     * @param width int
     * @param height int
     * @return MatteIcon
     */
    public static MatteIcon createMatteIcon(int width, int height) {
        return new MatteIcon(width, height);
    }





    /**
     * Creates a new MatteIcon with the given background and dimensions.
     *
     * @param width int
     * @param height int
     * @param background Paint
     * @return MatteIcon
     */
    public static MatteIcon createMatteIcon(int width, int height, Paint background) {
        return new MatteIcon(width, height, background);
    }





    /**
     * Creates a new Painter icon from the given Painter. The icon will have a
     * preferred size of 0x0.
     *
     * @param painter Painter
     * @return PainterIcon
     */
    public static <C extends Component> PainterIcon<C> createPainterIcon(Painter<? super C> painter) {
        return new PainterIcon<C>(painter);
    }





    /**
     * Creates a new PainerIcon with the given dimensions.
     *
     * @param width int
     * @param height int
     * @return PainterIcon
     */
    public static PainterIcon createPainterIcon(int width, int height) {
        return new PainterIcon(width, height);
    }





    /**
     * Creates a new icon which delegates the painting to the given Painter.
     *
     * @param width int
     * @param height int
     * @param painter Painter
     * @return PainterIcon
     */
    public static <C extends Component> PainterIcon<C> createPainterIcon(int width, int height, Painter<? super C> painter) {
        return new PainterIcon<C>(width, height, painter);
    }
}
