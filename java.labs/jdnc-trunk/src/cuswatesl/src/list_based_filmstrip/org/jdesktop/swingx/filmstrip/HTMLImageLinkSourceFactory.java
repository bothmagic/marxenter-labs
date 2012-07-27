package org.jdesktop.swingx.filmstrip;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdesktop.swingx.ImageSource;

/**
 * Implements an ImageSourceFactory that parses an HTML document and finds all &lt;a href&gt;
 * tags pointing to an image and publishes these
 * @author Nick
 */
public class HTMLImageLinkSourceFactory extends AbstractImageSourceFactory {

	final String weburl;
	
	/**
	 * creates an <code>HTMLImageLinkSourceFactory</code> based on the given url
	 * @param weburl
	 */
	public HTMLImageLinkSourceFactory(String weburl) {
		this.weburl = weburl;
	}
	
	/**
	 * A pattern that finds all a hrefs
	 */
	private final static Pattern webLinkPattern = Pattern.compile(
			"<a [^>]*href\\s*=\\s*(\"[^\"]+\"|'[^']+')", 
			Pattern.CASE_INSENSITIVE);

	@Override
	protected void publishImageSources() throws Exception {
		URL parentURL = new URL(weburl);
		InputStream in = getConnection(parentURL);
		publishURLs(in, parentURL);
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

	
	private void publishURLs(InputStream source, URL parentURL) {
		ArrayList<ImageSource> imageURLs = new ArrayList<ImageSource>();
		try {
			Scanner in = new Scanner(source);
			try { 
				while (in.hasNextLine()) {
					String line = in.nextLine();
					Matcher matcher = webLinkPattern.matcher(line);
					while ( matcher.find() ) {
						String address = matcher.group(1);
						address = address.substring(1,address.length()-1);
						URL ref = null;
						try {
							ref = new URL(parentURL, address);
							URLConnection conn = ref.openConnection();
							String contentType = conn.getContentType();
							if (contentType != null && contentType.startsWith("image/")) {
								ImageSource src = new URLImageSource(ref);
								publish(src);
								imageURLs.add(src);
							}
						} catch (MalformedURLException e) {
						}
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
