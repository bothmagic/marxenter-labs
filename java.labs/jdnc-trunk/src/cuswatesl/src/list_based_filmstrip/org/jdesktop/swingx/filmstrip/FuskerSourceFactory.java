package org.jdesktop.swingx.filmstrip;

import java.net.MalformedURLException;
import java.net.URL;

import org.jdesktop.swingx.ImageSource;

/**
 * Implements an <code>ImageSourceFactory</code> based upon a Fusker URL.
 * <p>
 * <i>Fusker according to <a href="http://en.wikipedia.org/wiki/Fusker">Wikipedia</a>
 * </i>
 * <br>
 * <b>"Fusker"</b> can be a verb, a noun that describes a technology for the web, 
 * or a noun that describes the output web page of that technology. As a verb, 
 * "to fusker" is to identify a range of images to a fusker script that returns 
 * a web page that displays all of the images within the range.<br><br>
 * For example, if you go to a fusker web page's form and enter the text 
 * <code>http://www.example.com/images/pic[1-16].jpg</code>, the fusker technology 
 * would produce a page that displays all sixteen images in that range, from 
 * <code>http://www.example.com/images/pic1.jpg</code> through 
 * <code>http://www.example.com/images/pic16.jpg</code>.<br><br>
 * It is also possible to use lists of words by writing 
 * <code>http://www.example.com/images/{small,medium,big}.jpg</code> which will 
 * produce three urls, each with one word from the bracketed list. The web page is 
 * then presented to the person who did the fusker, and can also be saved on the 
 * fusker web server so that other people may view it.
 * </p>
 * This <code>ImageSourceFactory</code> allows the user to input such an url, 
 * expands it and produces an <code>ImageSource</code> for each of these images.
 * @author Nick
 */
public class FuskerSourceFactory extends AbstractImageSourceFactory {

	final String fuskerUrl;
	
	/**
	 * Creates a <code>FuskerSourceFactory</code> for the given url
	 * @param fuskerUrl
	 */
	public FuskerSourceFactory(String fuskerUrl) {
		this.fuskerUrl = fuskerUrl;
	}
	
	@Override
	protected void publishImageSources() {
		expandAndPublishFusterURL(this.fuskerUrl);
	}
	
	private void expandAndPublishFusterURL(String fuskerUrl) {
		// split groups
		int br1 = fuskerUrl.indexOf("{");
		int br2 = fuskerUrl.indexOf("}");
		if (br1 != -1) {
			String part = fuskerUrl.substring(br1 + 1, br2);
			String begin = fuskerUrl.substring(0, br1);
			String end = fuskerUrl.substring(br2 + 1);
			String[] split = part.split(",");
			for (String s : split) {
				String nURL = begin + s.trim() + end;
				expandAndPublishFusterURL(nURL);
			}
			return;
		}
		// find ranges
		int idx1 = fuskerUrl.indexOf("[");
		int idx2 = fuskerUrl.indexOf("]");
		if (idx1 == -1) {
			try {
				ImageSource src = new URLImageSource(new URL(fuskerUrl));
				publish(src);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return;
		}
		if (idx2 == -1) {
			return;
		}
		
		String begin = fuskerUrl.substring(0, idx1);
		String content = fuskerUrl.substring(idx1 + 1, idx2);
		String end = fuskerUrl.substring(idx2 + 1);
		int minus = content.indexOf("-");
		if (minus == -1) {
			return;
		}
		String id1 = content.substring(0, minus);
		String id2 = content.substring(minus + 1);
		
		try {
			Integer i1 = Integer.parseInt(id1);
			Integer i2 = Integer.parseInt(id2);
			int len1 = id1.length();
			
			for (int x = i1; x <= i2; x ++) {
				String c1 = "" + x;
				while (c1.length() < len1) {
					c1 = "0" + c1;
				}
				expandAndPublishFusterURL(begin + c1 + end);
			}
		} catch (NumberFormatException e) { }	
	}
	
}
