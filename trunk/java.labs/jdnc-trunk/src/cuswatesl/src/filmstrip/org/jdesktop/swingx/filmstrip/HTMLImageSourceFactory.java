package org.jdesktop.swingx.filmstrip;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdesktop.swingx.ImageSource;

/**
 * Implements an ImageSourceFactory that parses an HTML document and finds all &lt;IMG&gt;
 * tags pointing to valid images an publishes these.
 * @author Nick
 */
public class HTMLImageSourceFactory extends AbstractImageSourceFactory {

	final String weburl;
	
	public HTMLImageSourceFactory(String weburl) {
		this.weburl = weburl;
	}

	private final static Pattern imageLinkPattern = Pattern.compile(
			"<img [^>]*src\\s*=\\s*(\"[^\"]+\"|'[^']+')", 
			Pattern.CASE_INSENSITIVE);


	@Override
	protected void publishImageSources() throws Exception {
		URL parentURL = new URL(weburl);
		InputStream in = getConnection(parentURL);
		getURLs(in, parentURL);		
	}
	

	/**
	 * Open a connection to a URL.  If the connection succeeds and the
	 * content type of the resource is html, then an input stream is
	 * opened for reading the contents.  If the content is an image,
	 * then the url is added to the list of imageURLs so that it can
	 * be added to the image queue.
	 */
	private InputStream getConnection(URL url) {
		try {
			URLConnection connection = url.openConnection();
			InputStream pageContents = connection.getInputStream();
			String contentType = connection.getContentType();
			if (contentType != null && 
					(contentType.startsWith("text/html") ||
							contentType.startsWith("application/xhtml+xml"))) {
				return pageContents;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	private void getURLs(InputStream source, URL parentURL) {
		ArrayList<ImageSource> imageURLs = new ArrayList<ImageSource>();
		try {
			Scanner in = new Scanner(source);
			try { 
				while (in.hasNextLine()) {
					String line = in.nextLine();
					Matcher matcher = imageLinkPattern.matcher(line);
					while ( matcher.find() ) { // found a URL in a "frame" tag
						String address = matcher.group(1);
						address = address.substring(1,address.length()-1);
						ImageSource src = new URLImageSource(new URL(parentURL, address));
						publish(src);
						imageURLs.add(src);
					}
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
