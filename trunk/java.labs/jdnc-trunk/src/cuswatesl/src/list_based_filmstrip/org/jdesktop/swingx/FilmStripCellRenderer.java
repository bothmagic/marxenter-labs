package org.jdesktop.swingx;

import java.awt.Component;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.jdesktop.swingx.filmstrip.AbstractFilmStripCellRenderer;

/**
 * An interface that defines how a FilmStripCellRenderer creates
 * a renderering component. In most situations when you just wish 
 * to do your own painting simply extend 
 * AbstractFilmStripCellRenderer as this implements this method 
 * and splits the actual painting into two methods, one for 
 * foreground and one for background painting.
 * @author Nick
 */
public interface FilmStripCellRenderer extends ListCellRenderer {
	
    /**
     * Return a component that has been configured to display the specified
     * ImageSource. The component's <code>paint</code> method is then called 
     * to "render" the cell. It usually works better to override 
     * <code>AbstractFilmStripCellRenderer</code> because, this sets up a 
     * JComponent correctly, and delegates foreground and background painting
     * to two methods where this can be implemented, and that is usually a
     * preferred approach when rendering an image.
     * 
     * @param strip The FilmStrip we're painting.
     * @param scaledImage The image we want this renderer to paint. The image
     * has been scaled by the UI delegate to the correct size. If scaling or
     * loading is in progress the value is <code>null</code>
     * @param imageSource the actual imageSource to render. This can be used
     * to get detailed information about the image, such as whether it is
     * retrievable and placeholder images to show if it's still loading or
     * if the image can not be retrieved. This can also be used to get the
     * name or description from the image 
     * @param index The cells index in the filmstrip.
     * @param selected True if the specified cell was selected.
     * @param mouseOver True if the mouse is over the cell.
     * @param width The width the renderComponent must have.
     * @param height The height the renderComponent must have. 
     * @return A component whose paint() method will render the specified image
     * or imageSource.
     * @see AbstractFilmStripCellRenderer
     */
	Component getCellRenderComponent(FilmStrip strip, BufferedImage scaledImage,
			ImageSource imageSource, int idx, 
			int width, int height, Insets insets,
			boolean mouseOver, boolean selected);
	
	Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus);
	
	/**
	 * Called before getListCellRenderComponent is called, to indicate if the
	 * painted cell has the mouse hovering it. 
	 * @param rollover
	 */
	void setRollover(boolean rollover);
}