/*
 * $Id: PDFParseUtils.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jdesktop.jdnc.incubator.rbair.swing.pdf.filter.AsciiHexDecodeFilter;
import org.jdesktop.jdnc.incubator.rbair.util.StringUtils;

/**
 * @author Richard Bair
 */
public class PDFParseUtils {
	private static final AsciiHexDecodeFilter ASCII_HEX_DECODE_FILTER = new AsciiHexDecodeFilter();
	
	/**
	 * 
	 */
	private PDFParseUtils() {
	}

	/**
	 * Decide what kind of token this is and call the proper specific function for parsing that kind of token
	 * @param token
	 * @return
	 */
	synchronized static Object parseToken(String token) {
		if (token == null || token.equals("null")) {
			return null;
		} else if (token.startsWith("<<")) {
			return parseDictionary(token);
		} else if (token.startsWith("/")) {
			return parseName(token);
		} else if (token.startsWith("(")) {
			return parseString(token);
		} else if (token.startsWith("<")) {
			return parseHexString(token);
		} else if (token.startsWith("[")) {
			return parseArray(token);
		} else if (token.startsWith("t") || token.startsWith("f")) {
			return Boolean.valueOf(parseBoolean(token));
		} else if (token.endsWith("R")) {
			return parseIndirectReference(token);
		} else if (isNumber(token)) {
			return parseNumber(token);
		} else {
			return parseOperator(token);
		}
	}
	
	/**
	 * This encoded string is either [array] or <<dictionary>>. Send it to me without the stupid [] or <<>>
	 * @param encodedString
	 * @return
	 */
	synchronized static List parseTokens(String encodedString) {
		int tokenStart = -1;
		Stack tokens = new Stack();
		for (int i=0; i<encodedString.length(); i++) {
			char c = encodedString.charAt(i);
			if (c == '\0' || c == '\n' || c== '\r' || c == '\f' || c == ' ' || c == '\t') {
				if (tokenStart != -1) {
					//this was a delimiter...
					String newToken = encodedString.substring(tokenStart, i);
					addToken(newToken, tokens);
					tokenStart = -1;
				}
			} else if (c == '<') {
				//if tokenStart != -1, then a token is being read, so we need to finish off that token before starting this new one.
				if (tokenStart != -1) {
					String newToken = encodedString.substring(tokenStart, i);
					addToken(newToken, tokens);
				}
				//inner loop
				tokenStart = i;
				int bracketCount = 1;
				while (bracketCount > 0) {
					c = encodedString.charAt(++i);
					//if c is an open paren, skip everything until I get to the close paren
					if (c == '(') {
						boolean closeFound = false;
						while (!closeFound) {
							c = encodedString.charAt(++i);
							if (c == ')' && encodedString.charAt(i - 1) != '\\') {
								closeFound = true;
							}
						}
					} else if (c == '<') {
						bracketCount++;
					} else if (c == '>') {
						bracketCount--;
					}
				}
				String newToken = encodedString.substring(tokenStart, i+1);
				addToken(newToken, tokens);
				tokenStart = -1;
			} else if (c == '(') {
				//if tokenStart != -1, then a token is being read, so we need to finish off that token before starting this new one.
				if (tokenStart != -1) {
					String newToken = encodedString.substring(tokenStart, i);
					addToken(newToken, tokens);
				}
				//inner loop. loop until I find a close paren that IS NOT escaped.
				tokenStart = i;
				boolean closeFound = false;
				while (!closeFound) {
					c = encodedString.charAt(++i);
					if (c == ')' && encodedString.charAt(i - 1) != '\\') {
						closeFound = true;
					}
				}
				String newToken = encodedString.substring(tokenStart, i+1);
				addToken(newToken, tokens);
				tokenStart = -1;
			} else if (c == '[') {
				//if tokenStart != -1, then a token is being read, so we need to finish off that token before starting this new one.
				if (tokenStart != -1) {
					String newToken = encodedString.substring(tokenStart, i);
					addToken(newToken, tokens);
				}
				//inner loop
				tokenStart = i;
				int parenCount = 1;
				while (parenCount > 0) {
					c = encodedString.charAt(++i);
					//if c is an open paren, skip everything until I get to the close paren
					if (c == '(') {
						boolean closeFound = false;
						while (!closeFound) {
							c = encodedString.charAt(++i);
							if (c == ')' && encodedString.charAt(i - 1) != '\\') {
								closeFound = true;
							}
						}
					} else	if (c == '[') {
						parenCount++;
					} else if (c == ']') {
						parenCount--;
					}
				}
				String newToken = encodedString.substring(tokenStart, i+1);
				addToken(newToken, tokens);
				tokenStart = -1;
			} else if (tokenStart != -1 && c == '/') {
				//token is being read, so we need to finish off that token before starting this new one
				String newToken = encodedString.substring(tokenStart, i);
				addToken(newToken, tokens);
				tokenStart = i;
			} else if (tokenStart == -1){
				//was the beginning of the token
				tokenStart = i;
			}
		}
		//catch the last token, if there was no delimiter following it
		if (tokenStart != -1) {
			String newToken = encodedString.substring(tokenStart);
			addToken(newToken, tokens);
		}
		//return the tokens
		return tokens;
	}

	/**
	 * Discovers whether this token is a number. It is a number (as far as I'm concerned) if every char in it is a number, or a -, +, or .
	 * @param token
	 * @return
	 */
	private static boolean isNumber(String token) {
		boolean number = true;
		for (int i=0; i<token.length(); i++) {
			char c = token.charAt(i);
			if (c == 0x2B || c == 0x2D || c == 0x2E || (c >= 0x30 && c<= 0x39)) {
				//this is a number, ok
			} else {
				number = false;
			}
		}
		return number;
	}
	
	/**
	 * Utility method that will construct a Rectangle2D from an array of numbers
	 * @param data
	 * @return
	 */
	static Rectangle2D constructRectangle(Object[] data) {
		double x1 = ((Number)data[0]).doubleValue();
		double y1 = ((Number)data[1]).doubleValue();
		double x2 = ((Number)data[2]).doubleValue();
		double y2 = ((Number)data[3]).doubleValue();
		return new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
	}
	
	/**
	 * Returns the PDFOperator who's name matches the given token. Token MUST NOT be surrounded by any whitespace
	 * @param token
	 * @return
	 */
	private synchronized static PDFOperator parseOperator(String token) {
		return PDFOperator.getOperator(token);
	}
	
	/**
	 * Parses a dictionary from the given token. The dictionary must have a << and a >>. THERE MUST NOT BE any whitespace
	 * before or after the first and last << >>.
	 * @param token
	 * @return
	 */
	private synchronized static Map parseDictionary(String token) {
		Map map = new HashMap();
		List tokens = parseTokens(token.substring(2, token.length() - 2));
		//assert that there are an even number of dictionary items, which there must be.
		assert tokens.size() % 2 == 0;
		for (int i=0; i<tokens.size(); i++) {
			String name = parseName(tokens.get(i++).toString());
			Object value = parseToken(tokens.get(i).toString());
			map.put(name, value);
		}
		return map;
	}
	
	/**
	 * Parses the name from this token. The token MUST NOT have any whitespace.
	 * A name token in of the form /name.
	 * @param token
	 * @return
	 */
	private synchronized static String parseName(String token) {
		return token.substring(1);
	}
	
	/**
	 * Parses the number from the token. The token MUST NOT have any whitespace.
	 * The token will be made a double if the token contains a decimal place, or
	 * if the token is too big for an integer. Otherwise, it is an integer.
	 * @param token
	 * @return
	 */
	private synchronized static Number parseNumber(String token) {
		if (token.indexOf('.') != -1) {
			return new Double(token);
		} else {
			try {
				return new Integer(token);
			} catch (Exception e) {
				//failed to convert to an integer, possibly because of an overflow situation. Do it as a double
				return new Double(token);
			}
		}
	}

	/**
	 * Parses out a string from the token. The token MUST NOT have any whitespace preceeding or
	 * following the token. Also, the token must start with a ( and end with a ). (no decimal)
	 * @param token
	 * @return
	 */
	private synchronized static String parseString(String token) {
		/*
		 * This method must massage the data such that all extraneous backslashes are removed and only
		 * the proper data remains. For instance, the sequence '\''n' may be in the string, but needs to
		 * be replaced with the '\n' character instead.
		 */
		StringBuffer buffer = new StringBuffer(token);
		StringUtils.replaceAllOnePass(buffer, new StringUtils.Substitution[]{
				new StringUtils.DefaultSubstitution("\\r\\n", "\n"),
				new StringUtils.DefaultSubstitution("\\r", "\n"),
				new StringUtils.DefaultSubstitution("\\n", "\n"),
				new StringUtils.DefaultSubstitution("\\t", "\t"),
				new StringUtils.DefaultSubstitution("\\b", "\b"),
				new StringUtils.DefaultSubstitution("\\f", "\f"),
				new StringUtils.DefaultSubstitution("\\(", "("),
				new StringUtils.DefaultSubstitution("\\)", ")"),
				new StringUtils.DefaultSubstitution("\\\\", "\\"),
				new StringUtils.OctalSubstitution(3),
				new StringUtils.OctalSubstitution(2),
				new StringUtils.OctalSubstitution(1),
				});
		buffer.deleteCharAt(0);
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}
	
	/**
	 * Parse a hex string into a byte array. Nuff said.
	 * @param token
	 * @return
	 */
	private synchronized static byte[] parseHexString(String token) {
		try {
			return ASCII_HEX_DECODE_FILTER.decode(token.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}
	}
	
	/**
	 * Parses out a boolean value from the given token. The token MUST be either "true" or "false"
	 * (case sensitive, no whitespace).
	 * @param token
	 * @return
	 */
	private synchronized static boolean parseBoolean(String token) {
		return token.equals("true");
	}

	/**
	 * TODO Not sure what to do with this right now, so you are getting the token given back to you.
	 * @param token
	 */
	private synchronized static PDFIndirectReference parseIndirectReference(String token) {
		return new PDFIndirectReference(token);
	}
	
	/**
	 * Parses an array into an Object array. Simply split the token based on the delimiter, and return the proper elements.
	 * @param token
	 * @return
	 */
	private synchronized static Object[] parseArray(String token) {
		List elements = parseTokens(token.substring(1, token.length() - 1));
		Object[] array = new Object[elements.size()];
		for (int i=0; i<elements.size(); i++) {
			array[i] = parseToken(elements.get(i).toString());
		}
		return array;
	}
	
	/**
	 * Method that is private, and used only in the "parseTokens" method.
	 * @param newToken
	 * @param tokens
	 */
	private static void addToken(String newToken, Stack tokens) {
		if (newToken.equals("R") && tokens.size() >= 2) {
			String secondToken = (String)tokens.pop();
			String firstToken = (String)tokens.pop();
			try {
				Integer.parseInt(secondToken);
				Integer.parseInt(firstToken);
				newToken = firstToken + " " + secondToken + " " + newToken;
			} catch (Exception e) {
				//not an indirect reference.
				tokens.push(firstToken);
				tokens.push(secondToken);
			}
		}
		
		tokens.push(newToken);
	}
}
