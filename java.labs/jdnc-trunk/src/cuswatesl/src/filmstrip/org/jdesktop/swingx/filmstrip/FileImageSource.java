package org.jdesktop.swingx.filmstrip;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * An imagesource implementation using Images backed by a file on disc
 * @author Nick
 *
 */
public class FileImageSource extends ASyncImageSource {
	private final File file;
	
	public FileImageSource(File imageFile) {
		// we do not wish to cache file loaded from disc, to dics
		super(false);
		this.file = imageFile;
	}
	@Override
	public BufferedImage loadImage() throws IOException {
		return ImageIO.read(file);
	}
	
	public String getImageLocation() {
		return file.getPath();
	}
	public String getImageName() {
		String s = file.getName();
		int to = s.lastIndexOf(".");
		return s.substring(0, to).trim();
	}
}