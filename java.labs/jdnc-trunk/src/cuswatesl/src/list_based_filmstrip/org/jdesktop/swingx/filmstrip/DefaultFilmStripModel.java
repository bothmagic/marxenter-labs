package org.jdesktop.swingx.filmstrip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jdesktop.swingx.ImageSource;

/**
 * A default implementation of the <code>FilmStripModel</code>
 * @author Nick
 *
 */
public class DefaultFilmStripModel extends AbstractFilmStripModel {

	/**
	 * The list of <code>ImageSource</code>s
	 */
	private List<ImageSource> imageList = new ArrayList<ImageSource>();
	
	/**
	 * Adds an <code>ImageSource</code> to the model
	 */
	public void addImage(ImageSource image) {
		this.imageList.add(image);
		fireContentsChanged(this, 0, this.imageList.size());
	}

	/**
	 * Adds several <code>ImageSource</code>s to the model
	 */
	public void addImages(Collection<ImageSource> images) {
		this.imageList.addAll(images);
		fireContentsChanged(this, 0, this.imageList.size());
	}

	public ImageSource getElementAt(int index) {
		if (index == -1) return null;
		return this.imageList.get(index);
	}

	public List<ImageSource> getImages() {
		return this.imageList;
	}

	public void setImages(Collection<ImageSource> images) {
		this.imageList.clear();
		this.imageList.addAll(images);
		fireContentsChanged(this, 0, this.imageList.size());
	}

	public int getSize() {
		return imageList.size();
	}
	
	public void removeAll() {
		this.imageList.clear();
		fireContentsChanged(this, 0, 0);
		
	}
	
	public int indexOf(ImageSource src) {
		return this.imageList.indexOf(src);
	}
}
