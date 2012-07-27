/*
 * $Id: PDFOperator.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Richard Bair
 */
public class PDFOperator {
	/**
	 * Sets the line width in the graphics state. Takes a single operand.
	 */
	public static final PDFOperator w = new PDFOperator("w");
	/**
	 * Sets the Line Cap style in the graphics state. Takes a single operand.
	 */
	public static final PDFOperator J = new PDFOperator("J");
	/**
	 * Sets the Line Join style in the graphics state. Takes a single operand.
	 */
	public static final PDFOperator j = new PDFOperator("j");
	/**
	 * Sets the Miter Limit in the graphics state. Takes a single operand.
	 */
	public static final PDFOperator M = new PDFOperator("M");
	/**
	 * Sets the Line Dash Pattern in the graphics state. Takes the operands
	 * DashArray DashPhase.
	 */
	public static final PDFOperator d = new PDFOperator("d");
	/**
	 * Sets the Color Rendering Intent in the graphics state. Takes a single operand.
	 */
	public static final PDFOperator ri = new PDFOperator("ri");
	/**
	 * Sets the Flatness Tolerance in the graphics state. Flatness
	 * is a number in the range 0 to 100, zero specifying the default.
	 * Takes a single operand.
	 */
	public static final PDFOperator i = new PDFOperator("i");
	/**
	 * "Set the specified parameters in the graphics state. dictName is
	 * the name of a graphics state parameter dictionary in the ExtGState subdictionary
	 * of the current resource dictionary". Takes a single operand.
	 */
	public static final PDFOperator gs = new PDFOperator("gs");
	/**
	 * This operator saves the current graphics state onto the stack.
	 * Takes no operands.
	 */
	public static final PDFOperator q = new PDFOperator("q");
	/**
	 * This operator pops the entire graphics state OFF of the stack.
	 * Takes no operands.
	 */
	public static final PDFOperator Q = new PDFOperator("Q");
	/**
	 * This is the Coordinate Transformation operator. When used,
	 * this operator will modify the current coordiate transformation
	 * matrix. (note that this matrix is reset for each page). Takes operands
	 * a b c d e f
	 */
	public static final PDFOperator cm = new PDFOperator("cm");
	/**
	 * Begins a new subpath by moving the current point to coordinates
	 * (x,y), omitting any connecting line segment. If the previous
	 * operator was also an 'm', this one replaces that one (last
	 * one wins).
	 */
	public static final PDFOperator m = new PDFOperator("m");
	/**
	 * Applies a straight line segment from the current point to
	 * (x,y).
	 */
	public static final PDFOperator l = new PDFOperator("l");
	/**
	 * Append a cubic Bézier curve to the current path. The curve extends
	 * from the current point to the point (x3, y3), using (x1, y1) and
	 * (x2, y2) as the Bézier control points.
	 * The new current point is (x3, y3).
	 */
	public static final PDFOperator c = new PDFOperator("c");
	/**
	 * Append a cubic Bézier curve to the current path. The curve extends
	 * from the current point to the point (x3, y3), using the current point
	 * and (x2, y2) as the Bézier control points. The new current point is (x3, y3).
	 */
	public static final PDFOperator v = new PDFOperator("v");
	/**
	 * Append a cubic Bézier curve to the current path. The curve extends
	 * from the current point to the point (x3, y3), using (x1, y1) and
	 * (x3, y3) as the Bézier control points.
	 * The new current point is (x3, y3).	 
	 */
	public static final PDFOperator y = new PDFOperator("y");
	/**
	 * This operator explicitly closes the path by making the last
	 * path point join with the first. That is, it draws a line from
	 * this point to the first of the subpath. If the current subpath is already
	 * closed, this does nothing.
	 */
	public static final PDFOperator h = new PDFOperator("h");
	/**
	 * Append a rectangle to the current path as a complete subpath.
	 * The LOWER LEFT corner is (x,y), with the given width and height
	 * as specified.
	 */
	public static final PDFOperator re = new PDFOperator("re");
	/**
	 * Stroke the path.
	 */
	public static final PDFOperator S = new PDFOperator("S");
	/**
	 * Close and then stroke the path
	 */
	public static final PDFOperator s = new PDFOperator("s");
	/**
	 * Fill the path using the non-zero winding rule
	 */
	public static final PDFOperator f = new PDFOperator("f");
	/**
	 * equivilent of f, exists for compatability reasons only
	 */
	public static final PDFOperator F = new PDFOperator("F");
	/**
	 * Fill the path using the even-odd winding rule
	 */
	public static final PDFOperator f_STAR = new PDFOperator("f*");
	/**
	 * Fill and then stroke the path using the non-zero winding rule
	 */
	public static final PDFOperator B = new PDFOperator("B");
	/**
	 * Fill and then stroke the path using the even-odd winding rule
	 */
	public static final PDFOperator B_STAR = new PDFOperator("B*");
	/**
	 * Close, fill, and then stroke the path using the non-zero winding rule
	 */
	public static final PDFOperator b = new PDFOperator("b");
	/**
	 * Close, fill, and then stroke the path using the even-odd winding rule
	 */
	public static final PDFOperator b_STAR = new PDFOperator("b*");
	/**
	 * End the path object without stroking or filling it. This is basically a
	 * null-op. Used primarily to finish a path that is going to be used for
	 * a clipping path
	 */
	public static final PDFOperator n = new PDFOperator("n");
	/**
	 * Modify the current clipping path by intersecting it with this path
	 * based on the non-zero winding rule
	 */
	public static final PDFOperator W = new PDFOperator("W");
	/**
	 * Modify the current clipping path by intersecting it with this path
	 * based on the even-odd winding rule
	 */
	public static final PDFOperator W_STAR = new PDFOperator("W*");
	/**
	 * The begin text marker. Must be matched with an ET marker, and cannot
	 * be nested.
	 */
	public static final PDFOperator BT = new PDFOperator("BT");
	/**
	 * The end text marker.  Must be matched with a BT marker.
	 */
	public static final PDFOperator ET = new PDFOperator("ET");
	/**
	 * Sets the character spacing
	 */
	public static final PDFOperator Tc = new PDFOperator("Tc");
	/**
	 * Sets the word spacing
	 */
	public static final PDFOperator Tw = new PDFOperator("Tw");
	/**
	 * Sets the horizontal scaling
	 */
	public static final PDFOperator Tz = new PDFOperator("Tz");
	/**
	 * Sets the leading
	 */
	public static final PDFOperator TL = new PDFOperator("TL");
	/**
	 * Sets the font size
	 */
	public static final PDFOperator Tf = new PDFOperator("Tf");
	/**
	 * Sets the render mode
	 */
	public static final PDFOperator Tr = new PDFOperator("Tr");
	/**
	 * Sets the rise
	 */
	public static final PDFOperator Ts = new PDFOperator("Ts");
	/**
	 * Move to the start of the next line, offset from the start of 
	 * the current line by the operands. For text only.
	 */
	public static final PDFOperator Td = new PDFOperator("Td");
	/**
	 * Move to the start of the next line, offset from the start of 
	 * the current line by the operands. For text only. As a side
	 * effect, this sets the leading also
	 */
	public static final PDFOperator TD = new PDFOperator("TD");
	/**
	 * Set the text matrix to the given values
	 */
	public static final PDFOperator Tm = new PDFOperator("Tm");
	/**
	 * Move to the start of the next line
	 */
	public static final PDFOperator T_STAR = new PDFOperator("T*");
	/**
	 * Show a text string
	 */
	public static final PDFOperator Tj = new PDFOperator("Tj");
	/**
	 * Show one or more text strings allowing individual glyph positioning.	
	 */
	public static final PDFOperator TJ = new PDFOperator("TJ");
	/**
	 * Move to the next line and show a text string
	 */
	public static final PDFOperator SINGLE_QUOTE = new PDFOperator("'");
	/**
	 * Move to the next line and show a text string, but use
	 * the given params Aw as the word spacing and Ac as the
	 * character spacing (setting the corrosponding params
	 * in the state)
	 */
	public static final PDFOperator DOUBLE_QUOTE = new PDFOperator("\"");
	/**
	 * Used to set width information for a glyph.
	 * TODO Currently unsupported
	 */
	public static final PDFOperator d0 = new PDFOperator("d0");
	/**
	 * Used to width and bounding box info for a glyph.
	 * TODO Currently unsupported
	 */
	public static final PDFOperator d1 = new PDFOperator("d1");
	/**
	 * Sets the stroking color space
	 */
	public static final PDFOperator CS = new PDFOperator("CS");
	/**
	 * Sets the nonstroking color space
	 */
	public static final PDFOperator cs = new PDFOperator("cs");
	/**
	 * Sets the stroking color
	 */
	public static final PDFOperator SC = new PDFOperator("SC");
	/**
	 * Sets the stroking color
	 */
	public static final PDFOperator SCN = new PDFOperator("SCN");
	/**
	 * Sets the nonstroking color
	 */
	public static final PDFOperator sc = new PDFOperator("sc");
	/**
	 * Sets the nonstroking color
	 */
	public static final PDFOperator scn = new PDFOperator("scn");
	/**
	 * Set the stroking colorspace to gray
	 */
	public static final PDFOperator G = new PDFOperator("G");
	/**
	 * Set the nonstroking colorspace to gray
	 */
	public static final PDFOperator g = new PDFOperator("g");
	/**
	 * Set the stroking colorspace to RGB and the values to those specified
	 */
	public static final PDFOperator RG = new PDFOperator("RG");
	/**
	 * Set the non stroking colorspace to RGB and the values to those specified
	 */
	public static final PDFOperator rg = new PDFOperator("rg");
	/**
	 * Set the stroking colorspace to CMYK and the values to those specified
	 */
	public static final PDFOperator K = new PDFOperator("K");
	/**
	 * Set the non stroking colorspace to CMYK and the values to those specified
	 */
	public static final PDFOperator k = new PDFOperator("k");
	
	public static final PDFOperator sh = new PDFOperator("sh");
	/**
	 * Indicates the beginning of an image block
	 * TODO This parser/renderer does not currently support inline images
	 */
	public static final PDFOperator BI = new PDFOperator("BI");
	/**
	 * Indicates ImageData. 
	 * TODO This parser/renderer does not currently support inline images
	 */
	public static final PDFOperator ID = new PDFOperator("ID");
	/**
	 * Indicates the end of an image block
	 * TODO This parser/renderer does not currently support inline images
	 */
	public static final PDFOperator EI = new PDFOperator("EI");
	/**
	 * Used to indicate the insertion of a form XObject. The name of the XObject
	 * to include is the operand.
	 */
	public static final PDFOperator Do = new PDFOperator("Do");
	
	public static final PDFOperator MP = new PDFOperator("MP");
	public static final PDFOperator DP = new PDFOperator("DP");
	public static final PDFOperator BMC = new PDFOperator("BMC");
	public static final PDFOperator BDC = new PDFOperator("BDC");
	public static final PDFOperator EMC = new PDFOperator("EMC");
	public static final PDFOperator BX = new PDFOperator("BX");
	public static final PDFOperator EX = new PDFOperator("EX");
	
	private static Map MAP;
	
	private String name;
	/**
	 * 
	 */
	private PDFOperator(String name) {
		if (MAP == null) {
			MAP = new HashMap();
		}
		MAP.put(name, this);
		this.name = name;
	}
	
	public static PDFOperator getOperator(String token) {
		PDFOperator op = (PDFOperator)MAP.get(token);
		if (op == null) {
			System.err.println("Tried to get an invalid operation '" + token + "'");
		}
		return op;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
}
