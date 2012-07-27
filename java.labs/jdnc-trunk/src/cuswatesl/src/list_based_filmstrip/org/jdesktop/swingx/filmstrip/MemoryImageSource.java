package org.jdesktop.swingx.filmstrip;

import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * An ImageSource that siply wraps a bufferedImage
 * @author Nick
 *
 */
public class MemoryImageSource extends ASyncImageSource {
	
	private final BufferedImage image;
	private String name;
	
	public MemoryImageSource(BufferedImage image) {
		this(image, "");
	}
	
	public MemoryImageSource(BufferedImage image, String name) {
		synchronized (this) {
			this.image = image;
		}
		this.name = name;
	}
	
	@Override
	public BufferedImage loadImage() throws IOException {
		synchronized (this) {
			return this.image;
		}
	}
	
	public String getImageLocation() {
		return "";
	}
	
	public String getImageName() {
		return this.name;
	}
	
}