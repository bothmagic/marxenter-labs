/*
 * $Id: StringUtils.java 32 2004-09-07 21:36:57Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.util;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Contains utility methods for dealing with strings.  For instance,
 * contains utilty method to do an efficient, simple replace (whereas
 * String.replaceAll() uses regular expressions).
 * @author Richard Bair
 */
public final class StringUtils {
	/**
	 * Hide the constructor
	 */
	private StringUtils() {
	}
	
	/**
	 * Simple replace all method.  Differs from String.replaceAll in that it
	 * does a simple find and replace, and therefore does not use regular
	 * expressions.
	 * @param source The String that contains the text to be found and replaced
	 * @param find The String to find within the source String
	 * @param replacement The String to replace any found Strings with
	 * @return A new String that only differs from <code>source</code> in that all
	 * portions of <code>source</code> containing <code>find</code> have been replaced with
	 * <code>replacement</code>
	 */
	public static String replaceAll(String source, String find, String replacement) {
		StringBuffer buffer = new StringBuffer(source);
		replaceAll(buffer, find, replacement);
		return buffer.toString();
	}
	
	/**
	 * Simple replace all method that acts on a string buffer, src. The source string buffer is actually
	 * modified, making this method more efficient.
	 * @param src
	 * @param find
	 * @param replacement
	 * @return
	 */
	public static void replaceAll(StringBuffer src, String find, String replacement) {
		int index = -1;
		while ((index = src.indexOf(find)) != -1) {
			src.replace(index, index + find.length(), replacement);
		}
	}
	
	/**
	 * Replaces items in the given src string buffer in one pass. Should be highly efficient.
	 * This method was developed for replacing strings that start with a backslash,
	 * and thus needs to be rewritten to take into account arbitrary strings.
	 * @param src
	 * @param tokens
	 */
	public static void replaceAllOnePass(StringBuffer src, Substitution[] subs) {
		if (subs.length == 0) {
			return;
		}
		
		Arrays.sort(subs, new Comparator() {
			public int compare(Object o1, Object o2) {
				Substitution s1 = (Substitution)o1;
				Substitution s2 = (Substitution)o2;
				return s2.getFindLength() - s1.getFindLength();
			}
		});
		
		int maxLength = subs[0].getFindLength();
		int index = 0;
		//used to avoid infinate loops
		int lastIndex = -1;
		while ((index = src.indexOf("\\", index)) != -1) {
			if (lastIndex == index) {
				break;
			}
			//read the max length string
			//search until a substitution is found, or none was found at all.
			String chunk = "";
			int chunkLength = maxLength;
			Substitution sub = null;
			while (chunkLength > 0 && sub == null) {
				if (index + chunkLength < src.length()) {
					chunk = src.substring(index, index + chunkLength);
					for (int i=0; i<subs.length; i++) {
						if (subs[i].matches(chunk)) {
							sub = subs[i];
							break;
						}
					}
				}
				--chunkLength;
			}
			
			if (sub != null) {
				//found the substitution, so substitute it and increment the index
				String replacement = sub.getReplacement(chunk);
				src.replace(index, index + sub.getFindLength(), replacement);
				index = index + replacement.length();
			}
			//used to avoid infinate loops
			lastIndex = index;
		}
	}
	
	/**
	 * Helper class used for replacing strings.
	 * @author Richard Bair
	 * date: May 20, 2004
	 */
	public static abstract class Substitution {
		public abstract boolean matches(String string);
		public abstract String getReplacement(String chunk);
		public abstract int getFindLength();
	}
	
	/**
	 * Default implementation of the substitution class
	 * @author Richard Bair
	 * date: May 20, 2004
	 */
	public static class DefaultSubstitution extends Substitution {
		private String find;
		private String replace;
		public DefaultSubstitution(String find, String replace) {
			this.find = find;
			this.replace = replace;
		}
		public boolean matches(String string) {
			return find.equals(string);
		}
		
		public String getReplacement(String chunk) {
			return replace;
		}
		
		public int getFindLength() {
			return find.length();
		}
	}
	
	/**
	 * Performs an octal substition -- replaces a literal string of the form \nnn where n is some
	 * number into the octal equivalent.
	 * @author Richard Bair
	 * date: May 20, 2004
	 */
	public static class OctalSubstitution extends Substitution {
		private int numDigits = 0;
		private String regex;
		
		/**
		 * numDigits must be between 1 and 3.
		 * @param numDigits
		 */
		public OctalSubstitution(int numDigits) {
			assert numDigits >= 1 &&  numDigits <= 3;
			this.numDigits = numDigits;
			StringBuffer buffer = new StringBuffer(17);
			buffer.append("\\\\");
			for (int i=0; i<numDigits; i++) {
				buffer.append("[0-9]");
			}
			regex = buffer.toString();
		}
		
		public boolean matches(String string) {
			return string.matches(regex);
		}
		
		public String getReplacement(String chunk) {
			//convert the chunk to a byte and return that
			byte total = 0;
			for (int i=1; i<chunk.length(); i++) {
				total += (chunk.charAt(i) - 48) * (8^(chunk.length() - i - 1));
			}
			return Byte.toString(total);
		}
		
		public int getFindLength() {
			return numDigits + 1;
		}
	}
	
	/**
	 * Returns true if the given <code>text</code> contains html.  It does this
	 * by examining the first 6 characters to see if they equal (ignoring case)
	 * "&lt;html&gt;"
	 * @param text
	 * @return
	 */
	public static boolean hasHtml(String text) {
		if (text.length() < 6) {
			return false;
		}
		
		String prefix = text.substring(0, 6);
		return prefix.equalsIgnoreCase("<html>");
	}

	/**
	 * Sets the case appropriately for the given string according to US name standards.
	 * So, for instance, if the name is "richard bair", it will be returned as "Richard Bair".
	 * "RICHARD A BAIR" will be "Richard A Bair" and "RIchARd a. BAir" will be
	 * "Richard A. Bair".
	 * @param name
	 * @return
	 */
	public static String nameTitleCase(String name) {
		//first, lowercase the name
		String lowercase = name.toLowerCase();
		String uppercase = name.toUpperCase();
		StringBuffer buffer = new StringBuffer(lowercase);
		char previousChar = ' ';
		for (int i=0; i<buffer.length(); i++) {
			char currentChar = uppercase.charAt(i);
			//if this care is the beginning of a word, uppercase it
			if (previousChar == ' ') {
				buffer.setCharAt(i, currentChar);
			}
			previousChar = currentChar;
		}
		
		return buffer.toString();
	}
	
	/**
	 * Capitalizes the first letter of the given text.
	 * @param text
	 * @return
	 */
	public static String uppercaseFirstLetter(String text) {
		if (text.length() == 0) {
			return text;
		}
		
		StringBuffer buffer = new StringBuffer(text);
		char ch = Character.toUpperCase(text.charAt(0));
		buffer.setCharAt(0, ch);
		return buffer.toString();
	}
	
	/**
	 * Takes a java style name (such as myVariableName)
	 * and turns it into a "name title case" such as
	 * My Variable Name.
	 * @param javaName
	 * @return
	 */
	public static String nameTitleCaseJavaName(String javaName) {
		StringBuffer buffer = new StringBuffer();
		int lastIndex = javaName.length() - 1;
		int startIndex = 0;
		int index = 0;
		while ((index = getJavaWordEndIndex(startIndex+1, javaName)) != lastIndex) {
			buffer.append(javaName.substring(startIndex, index+1));
			buffer.append(" ");
			startIndex = index + 1;
		}
		buffer.append(javaName.substring(startIndex));
		return nameTitleCase(buffer.toString());
	}
	
	/**
	 * Given a string that follows standard java naming syntax (such as myJavaName, where each
	 * word is cancatenated together, and each word starts with a capital letter,
	 * except for the first word), this method will return the index of the last character of the word.
	 * For instance, if myJavaName is the javaName, and if startIndex = 3, then this method will return
	 * 5.  Therefore, the word Java was found out of the javaName.
	 * @param startIndex
	 * @param javaName
	 * @return
	 */
	public static int getJavaWordEndIndex(int startIndex, String javaName) {
		//Words are denoted by:
		//from index 0 to the first Capital letter - 1 that has a lowercase
		//letter following it.
		//from that Capital letter until the next...
		//from that Capital letter until the end.
		int length = javaName.length();
		for (int i=startIndex; i<length; i++) {
			if (i < length - 2) {
				char c1 = javaName.charAt(i);
				char c2 = javaName.charAt(i+1);
				if (Character.isUpperCase(c1) && !Character.isUpperCase(c2)) {
					return i - 1;
				}
			} else {
				return javaName.length() - 1;
			}
		}
		return javaName.length() - 1;
	}
	
	/**
	 * Split the text string based on the given delim.
	 * @param text
	 * @param delim
	 * @return
	 */
	public static String[] split(String text, char delim) {
		//count up the number of delims
		int size = countOf(text, Character.toString(delim));
		//create the results array
		String[] results = new String[size + 1];
		//get each result (minus the freakin' delimiters)
		int resultsIndex = 0;
		int textLength = text.length();
		int lastIndex = -1;
		char nextChar = '\0';
		char[] buffer = new char[textLength];
		int bufPos = 0;
		for (int i=0; i<textLength+1; i++) {
			//do the normal stuff if i is less than textLength, otherwise do some finishing stuff
			//to put the last value into the results array
			if (i < textLength) {
				//read next character
				//increment lastIndex
				nextChar = text.charAt(++lastIndex);
				if (nextChar == delim) {
					//if the character is the delim, then create a string from buffer from index 0 to bufPos and put in the
					//results array at resultsIndex. Then reinitialize bufpos to 0
					results[resultsIndex++] = new String(buffer, 0, bufPos);
					bufPos = 0;
				} else {
					//else, put the character into the buffer at bufPos and increment bufPos by one
					buffer[bufPos++] = nextChar;
				}
			} else {
				//if we are here, then we are done walking through the text string, and need to put our final value
				//from the buffer into the results
				results[resultsIndex++] = new String(buffer, 0, bufPos);
			}
		}
		return results;
	}
	
	/**
	 * Counts and returns the number of occurances of "substring" in the text
	 * @param text
	 * @param substring
	 * @return
	 */
	public static int countOf(String text, String substring) {
		int count = 0;
		int i = 0;
		while ((i = text.indexOf(substring, i+1)) != -1) {
			++count;
		}
		return count;
	}
	
	/**
	 * Strips off the opening and closing html tags (if they exist) from this string.
	 * @param html
	 * @return
	 */
	public static String removeHtmlOpenCloseTags(String html) {
		if (hasHtml(html)) {
			html = html.substring(html.indexOf("<html>") + 6, html.lastIndexOf("</html>"));
		}
		return html;
	}
}
