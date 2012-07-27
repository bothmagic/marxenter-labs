package org.jdesktop.swingx.filmstrip;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;


public class URLImageSource extends ASyncImageSource {
	
	private final URL url;
	//public final Action saveToDiscAction = new SaveImageToDiscAction();
	
	public URLImageSource(URL url) {
		this.url = url;
		//addAction(saveToDiscAction);
	}
	
	@Override
	public BufferedImage loadImage() throws IOException {
		return ImageIO.read(url);
	}
	
	public String getImageLocation() {
		return url.toString();
	}
	
	public String getImageName() {
		int from = url.toString().lastIndexOf("/");
		String s = url.toString().substring(from + 1);
		int to = s.lastIndexOf(".");
		return s.substring(0, to);
	}

	/*
	public Action getSaveToDiscAction() {
		return saveToDiscAction;
	}

	class SaveImageToDiscAction extends AbstractAction {
		public SaveImageToDiscAction() {
			super("Save image to disc");
		}
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			final List<String> suffixes = Arrays.asList(ImageIO.getReaderFileSuffixes());
			chooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) return true;
					for (String suffix : suffixes) {
						if (f.getName().toLowerCase().endsWith("." + suffix.toLowerCase())) return true;
					}
					return false;
				}
				@Override
				public String getDescription() {
					StringBuffer sb = new StringBuffer();
					for (String suffix : suffixes) {
						if (sb.length() != 0) {
							sb.append(", ");
						}
						sb.append("*.");
						sb.append(suffix);
					}
					sb.insert(0, "(").append(")");
					sb.insert(0, "Supported imagestypes ");
					return sb.toString();
				}
			} );
			chooser.setSelectedFile(new File(getImageName()));
			if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				final File saveTo = chooser.getSelectedFile();
				int suffixPos = saveTo.getName().lastIndexOf(".") + 1;
				String type = saveTo.getName().toLowerCase().substring(suffixPos);
				if (suffixes.contains(type)) {
					try {
						ImageIO.write(loadImage(), type, saveTo);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "error: " + e1.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(null, "format not supported: " + type);
				}
			}
		}
	}
	*/
}