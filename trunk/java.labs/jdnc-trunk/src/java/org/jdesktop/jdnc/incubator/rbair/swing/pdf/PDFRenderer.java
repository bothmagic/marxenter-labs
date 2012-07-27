/*
 * $Id: PDFRenderer.java 333 2005-02-01 21:40:45Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.color.ColorSpace;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.swing.ImageIcon;

/**
 * This object performs the actual rendering of a PDF file onto a Java2D graphics canvas. Pass in the graphics object
 * to use into the renderer's constructor.<br>
 * Here is a more indepth discusson regarding what goes on in this class.
 * First, This class needs the PDFParser to do its work. After all, It needs to ask the parser for page sizes and everything.
 * Second, if you want to set a zoom factor, pass it in with the graphics object (via the g.scale() method). I take the graphics
 * object's transform as the literal truth -- and then I tinker with it until it happily produces PDF output.
 * @author Richard Bair
 */
public class PDFRenderer {
	private static final Color ZERO_COLOR = new Color(0, 0, 0);
	/**
	 * The collection of PDFOperations which indicate that the path drawing operation is complete
	 */
	private static final PDFOperator[] PATH_CLOSING_OPS = new PDFOperator[]{PDFOperator.S, 
																			PDFOperator.f, 
																			PDFOperator.s, 
																			PDFOperator.F,
																			PDFOperator.f_STAR,
																			PDFOperator.B,
																			PDFOperator.B_STAR,
																			PDFOperator.b,
																			PDFOperator.b_STAR,
																			PDFOperator.n};
	/**
	 * The collection of PDFOperations that indicate that the text operation is complete
	 */
	private static final PDFOperator[] TEXT_CLOSING_OPS = new PDFOperator[]{PDFOperator.ET};
	/**
	 * The parser for this PDF document. The parser is necessary for getting page dimensions,
	 * dictionaries, fonts, and other such things.
	 */
	private PDFParser parser;
	/**
	 * The current PDFPage being rendered
	 */
	private PDFParser.PDFPage page;
	/**
	 * Keeps a reference to a copy of the original AffineTransform associated with the graphics object. This
	 * is done so we can set the affine transform for a new graphics state.
	 */
	private AffineTransform originalTransform;
	/**
	 * The current Graphics State. The graphics state is an object encapsulating various bits of information about
	 * the state of settings for drawing at any given time. You may notice that many of the variables in the current
	 * graphics state is actually maintained in the Graphics2D object, and wonder, why the redundancy? The reason is
	 * that at any time in the parsing of this pdf file we may run across a q/Q operator pair. Those operators will need
	 * to have the graphics state pushed onto the stack and require a fresh graphics state to be created. After the closing
	 * Q, the graphics state must be popped back off. This trivializes that operation, besides encapsulating the graphics
	 * state in one place.
	 */
	private GraphicsState gs;
	/**
	 * A stack of different graphics states. At any time during the parsing of the pdf file, we may run across a q/Q operator
	 * pair. These operators ask us to push the current graphics state onto a stack, and pop them off the stack, respectively.
	 * This stack contains a bunch of GraphicsState objects.
	 */
	private Stack graphicsStateStack = new Stack();
		
	/**
	 * Creates a new PDFRenderer for the given PDFParser
	 */
	public PDFRenderer(PDFParser parser) {
		assert parser != null;
		this.parser = parser;
	}
	
	/**
	 * Renders the specified page onto the given graphics object. The graphics object will have all of its
	 * settings altered via RenderingHints in such a manner as to produce the highest quality output (at the
	 * expense of speed).<br>
	 * It is <em>imperative</em> that the graphics object be set up with any necessary transformations
	 * such that the origin is in the top left corner of the medium BEFORE being passed into this method.
	 * Luckily, for Java2D and the java.awt printing api, this is the case by default.
	 * @param g
	 * @param pageWidth
	 * @param pageHeight
	 * @param operations
	 * @throws Exception
	 */
	public void render(Graphics2D g, int pageNumber) throws Exception {
		assert g != null;
		//set rendering hints to make the output look spectacular
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		//get the page that I'm about to render. I will be needing its dimensions, not to mention its operations in a bit.
		page = parser.getPage(pageNumber);
		/*
		 * The graphics transform needs to be modified so that it will allow rendering from the bottom left corner
		 * instead of the top left corner. This needs to be done because PDF always goes from the bottom left
		 * corner.
		 */
		g.translate(0, page.getPageHeight());
		g.scale(1.0, -1.0);
		//save the current configuration of the affine transform for use with new GraphicsState objects.
		originalTransform = new AffineTransform(g.getTransform());
		
		//initialize the graphics state
		gs = new GraphicsState(g);
		
		//paint the background
		g.setColor(Color.WHITE);
		Rectangle2D.Double backgroundRect = new Rectangle2D.Double(0.0, 0.0, page.getPageWidth(), page.getPageHeight());
		g.fill(backgroundRect);
				
		/*
		 * Render by reading one operation at a time. For each operation, discover the operator. Handle the operator, passing in the
		 * operands as parameters.
		 */
		List/*<PDFOperation>*/ operations = page.getOperations();
		Iterator itr = operations.iterator();
		while (itr.hasNext()) {
			PDFOperation operation = (PDFOperation)itr.next();
			PDFOperator op = operation.getOperator();
			Object[] operands = operation.getOperands();
			if (op == PDFOperator.w) {
				//Modify the line width
				gs.width = ((Number)operands[0]).floatValue();
			} else if (op == PDFOperator.J) {
				//modify the Cap setting
				int cap = ((Number)operands[0]).intValue();
				switch (cap) {
					case 0:
						gs.cap = BasicStroke.CAP_BUTT;
						break;
					case 1:
						gs.cap = BasicStroke.CAP_ROUND;
						break;
					case 2:
						gs.cap = BasicStroke.CAP_SQUARE;
						break;
					default:
						System.err.println("Received an invalid cap style of '" + cap + "' in the pdf document");
				}
				gs.updateStroke();
			} else if (op == PDFOperator.j) {
				//modify the join setting
				int join = ((Number)operands[0]).intValue();
				switch (join) {
					case 0:
						gs.join = BasicStroke.JOIN_MITER;
						break;
					case 1:
						gs.join = BasicStroke.JOIN_ROUND;
						break;
					case 2:
						gs.join = BasicStroke.JOIN_BEVEL;
						break;
					default:
						System.err.println("Received an invalid join style of '" + join + "' in the pdf document");
				}
				gs.updateStroke();
			} else if (op == PDFOperator.M) {
				//modify the miter limit
				gs.miterLimit = ((Number)operands[0]).floatValue();
				gs.updateStroke();
			} else if (op == PDFOperator.d) {
				//modify the dashLimit and dashPhase
				Object[] dashArray = (Object[])operands[0];
				gs.dashArray = new float[dashArray.length];
				for (int i=0; i<dashArray.length; i++) {
					gs.dashArray[i] = ((Number)dashArray[i]).floatValue();
				}
				gs.dashLimit = ((Number)operands[1]).floatValue();
				gs.updateStroke();
			} else if (op == PDFOperator.ri) {
				System.err.println("The ri operator is not supported in this release of the PDF renderer");
			} else if (op == PDFOperator.i) {
				gs.flatness = ((Number)operands[0]).intValue();
			} else if (op == PDFOperator.gs) {
				//TODO get the ExtGState dictionary from the page, and set the gs state variables based on that dictionary
			} else if (op == PDFOperator.q) {
				//push the graphics state onto the stack and replace the current graphics state with a new one
				graphicsStateStack.push(gs);
				gs = new GraphicsState(g);
			} else if (op == PDFOperator.Q) {
				//throw out the current graphics state and replace it with the graphics state on the stack
				gs = (GraphicsState)graphicsStateStack.pop();
			} else if (op == PDFOperator.cm) {
				//concatenate the transform matrix
				double a = ((Number)operands[0]).doubleValue();
				double b = ((Number)operands[1]).doubleValue();
				double c = ((Number)operands[2]).doubleValue();
				double d = ((Number)operands[3]).doubleValue();
				double e = ((Number)operands[4]).doubleValue();
				double f = ((Number)operands[5]).doubleValue();
				AffineTransform trans = new AffineTransform(a, b, c, d, e, f);
				gs.ctm.concatenate(trans);
			} else if (op == PDFOperator.m || op == PDFOperator.re) {
					//start a new subpath. Collect all of the operations up to a path closing operator (S, f, b). Pass the entire collection
					//on to the "renderSubPath" method.
					renderSubPath(g, collectUntilIncluding(PATH_CLOSING_OPS, operation, itr));
			} else if (op == PDFOperator.CS || op == PDFOperator.cs || op == PDFOperator.SC || op == PDFOperator.SCN || op == PDFOperator.scn
					|| op == PDFOperator.sc || op == PDFOperator.G || op == PDFOperator.g || op == PDFOperator.RG || op == PDFOperator.rg
					|| op == PDFOperator.K || op == PDFOperator.k) {
				handleColorChange(operation);
			} else if (op == PDFOperator.BT) {
				//found a text marker. Collect everything from BT to ET, exclusive.
				//call renderText
				renderText(g, collectUntilExcluding(TEXT_CLOSING_OPS, itr));
			} else if (op == PDFOperator.Do) {
				String xObjectName = operands[0].toString();
				//get the xObject from the parser
				renderXObject(g, xObjectName);
			} else if (op == PDFOperator.Tc) {
				//set the text char spacing
				gs.characterSpacing = ((Number)operands[0]).floatValue();
			} else if (op == PDFOperator.Tw) {
				//set the text word spacing
				gs.wordSpacing = ((Number)operands[0]).floatValue();
			} else if (op == PDFOperator.Tz) {
				//set the horizontal scaling to x/100 of this value (this value being x)
				float x = ((Number)operands[0]).floatValue();
				gs.horizontalScaling = x == 0 ? 0 : x / 100;
			} else if (op == PDFOperator.TL) {
				//set the text leading to this
				gs.leading = ((Number)operands[0]).floatValue();
			} else if (op == PDFOperator.Tf) {
				//set the text font and size to this
				gs.font = page.getFont(operands[0].toString()).deriveFont(((Number)operands[1]).floatValue());
			} else if (op == PDFOperator.Tr) {
				//set the render mode integer
				gs.renderMode = ((Number)operands[0]).intValue();
			} else if (op == PDFOperator.Ts) {
				//set the rise
				gs.rise = ((Number)operands[0]).floatValue();
			}
		}
	}
	
	/**
	 * A utility method that will return a ColorSpace for the given name
	 * @param name
	 * @return
	 */
	private ColorSpace getColorSpace(String name) {
		if (name.equals("DeviceGray")) {
			return ColorSpace.getInstance(ColorSpace.CS_GRAY);
		} else if (name.equals("DeviceRGB")) {
			return ColorSpace.getInstance(ColorSpace.CS_sRGB);
		} else if (name.equals("DeviceCMYK")) {
			System.err.println("This implementation of the PDF renderer does not currently support the CMYK ColorSpace");
			return ColorSpace.getInstance(ColorSpace.CS_sRGB);
		} else if (name.equals("Pattern")) {
			System.err.println("This implementation of the PDF renderer does not currently support the Pattern ColorSpace");
			return ColorSpace.getInstance(ColorSpace.CS_sRGB);
		} else {
			System.err.println("This implementation of the PDF renderer does not currently support lookups to the ColorSpace subdictionary. Unknown name '" + name +"'");
			return ColorSpace.getInstance(ColorSpace.CS_sRGB);
		}
	}
	
	/**
	 * Utility method to return the default color for a given color space
	 * @param cs
	 * @return
	 */
	private Color getDefaultColorSpaceColor(ColorSpace cs) {
		if (cs.getType() == ColorSpace.TYPE_GRAY || cs.getType() == ColorSpace.TYPE_RGB) {
			return ZERO_COLOR;
		} else if (cs.getType() == ColorSpace.TYPE_CMYK) {
			return new Color(ColorSpace.getInstance(ColorSpace.TYPE_CMYK), new float[]{0.0f, 0.0f, 0.0f, 1.0f}, gs.alpha);
		} else {
			System.err.println("Could not return the default color for the color space of type '" + cs == null ? null : cs.getType() + "' because the color space is not supported");
			return Color.BLACK;
		}
	}
	
	/**
	 * A little utility method that will:<br>
	 * <ol>
	 * <li>Add the given op to the new List</li>
	 * <li>Use the given iterator to iterate until it runs into one of the operators in the operators array</li>
	 * <li>Add each op it gets from the iterator to the new List</li>
	 * <li>Return the new List</li>
	 * </ol>
	 * This is intended to help construct sub lists of the main list while advancing the iterator.
	 * @param operators
	 * @param op
	 * @param opsItr
	 * @return
	 */
	private List collectUntilIncluding(PDFOperator[] operators, PDFOperation operation, Iterator opsItr) {
		List ops = new ArrayList();
		ops.add(operation);
		boolean done = false;
		while (!done && opsItr.hasNext()) {
			operation = (PDFOperation)opsItr.next();
			PDFOperator op = operation.getOperator();
			ops.add(operation);
			for (int i=0; i<operators.length; i++) {
				if (operators[i] == op) {
					done = true;
				}
			}
		}
		return ops;
	}
	
	/**
	 * A little utility method that will:<br>
	 * <ol>
	 * <li>Add the given op to the new List</li>
	 * <li>Use the given iterator to iterate until it runs into one of the operators in the operators array</li>
	 * <li>Add each op it gets from the iterator to the new List, EXCLUDING THE CLOSING OPERATOR</li>
	 * <li>Return the new List</li>
	 * </ol>
	 * This is intended to help construct sub lists of the main list while advancing the iterator.
	 * @param operators
	 * @param op
	 * @param opsItr
	 * @return
	 */
	private List collectUntilExcluding(PDFOperator[] operators, Iterator opsItr) {
		List ops = new ArrayList();
		boolean done = false;
		while (!done && opsItr.hasNext()) {
			PDFOperation operation = (PDFOperation)opsItr.next();
			PDFOperator op = operation.getOperator();
			for (int i=0; i<operators.length; i++) {
				if (operators[i] == op) {
					done = true;
				}
			}
			if (!done) {
				ops.add(operation);
			}
		}
		return ops;
	}
	
	/**
	 * Helper method that handles any of the color change operations
	 * @param operation
	 */
	private void handleColorChange(PDFOperation operation) {
		PDFOperator op = operation.getOperator();
		Object[] operands = operation.getOperands();
		if (op == PDFOperator.CS) {
			//set the color space and color for stroking operations
			gs.strokeColorSpace = getColorSpace(operands[0].toString());
			gs.strokeColor = getDefaultColorSpaceColor(gs.strokeColorSpace);
		} else if (op == PDFOperator.cs) {
			//set the color space and color for nonstroking operations
			gs.nonStrokeColorSpace = getColorSpace(operands[0].toString());
			gs.nonStrokeColor = getDefaultColorSpaceColor(gs.strokeColorSpace);
		} else if (op == PDFOperator.SC) {
			//set the color to use for stroking operations for this colorspace
			float[] params = new float[operands.length];
			for (int i=0; i<operands.length; i++) {
				params[i] = ((Number)operands[i]).floatValue();
			}
			gs.strokeColor = new Color(gs.strokeColorSpace, params, gs.alpha);
		} else if (op == PDFOperator.SCN || op == PDFOperator.scn) {
			System.err.println("The SCN and scn operators are not supported in this release of this PDF parser");
		} else if (op == PDFOperator.sc) {
			//set the color to use for non stroking operations for this colorspace
			float[] params = new float[operands.length];
			for (int i=0; i<operands.length; i++) {
				params[i] = ((Number)operands[i]).floatValue();
			}
			gs.nonStrokeColor = new Color(gs.nonStrokeColorSpace, params, gs.alpha);
		} else if (op == PDFOperator.G) {
			//set the stroking color space to gray, and set the color to gray
			gs.strokeColorSpace = getColorSpace("DeviceGray");
			float gray = ((Number)operands[0]).floatValue();
			gs.strokeColor = new Color(gray, gray, gray, gs.alpha);
		} else if (op == PDFOperator.g) {
			//set the non stroking color space to gray, and set the color to gray
			gs.nonStrokeColorSpace = getColorSpace("DeviceGray");
			float gray = ((Number)operands[0]).floatValue();
			gs.nonStrokeColor = new Color(gray, gray, gray, gs.alpha);
		} else if (op == PDFOperator.RG) {
			//set the stroking color space to RGB and set the color to the values provided
			gs.strokeColorSpace = getColorSpace("DeviceRGB");
			float red = ((Number)operands[0]).floatValue();
			float green = ((Number)operands[1]).floatValue();
			float blue = ((Number)operands[2]).floatValue();
			gs.strokeColor = new Color(red, green, blue, gs.alpha);
		} else if (op == PDFOperator.rg) {
			//set the nonstroking color space to RGB and set the color to the values provided
			gs.nonStrokeColorSpace = getColorSpace("DeviceRGB");
			float red = ((Number)operands[0]).floatValue();
			float green = ((Number)operands[1]).floatValue();
			float blue = ((Number)operands[2]).floatValue();
			gs.nonStrokeColor = new Color(red, green, blue, gs.alpha);
		} else if (op == PDFOperator.K) {
			//set the stroking color space to CMYK and set the color to the values provided
			gs.strokeColorSpace = getColorSpace("DeviceCMYK");
			float cyan = ((Number)operands[0]).floatValue();
			float magenta = ((Number)operands[1]).floatValue();
			float yellow = ((Number)operands[2]).floatValue();
			float black = ((Number)operands[3]).floatValue();
			gs.strokeColor = new Color(ColorSpace.getInstance(ColorSpace.TYPE_CMYK), new float[]{cyan, magenta, yellow, black}, gs.alpha);
		} else if (op == PDFOperator.k) {
			//set the nonstroking color space to CMYK and set the color to the values provided
			gs.nonStrokeColorSpace = getColorSpace("DeviceCMYK");
			float cyan = ((Number)operands[0]).floatValue();
			float magenta = ((Number)operands[1]).floatValue();
			float yellow = ((Number)operands[2]).floatValue();
			float black = ((Number)operands[3]).floatValue();
			gs.nonStrokeColor = new Color(ColorSpace.getInstance(ColorSpace.TYPE_CMYK), new float[]{cyan, magenta, yellow, black}, gs.alpha);
		}
	}
	
	/**
	 * Renders a path based on the given operations.
	 * @param g
	 * @param ops
	 */
	private void renderSubPath(Graphics2D g, List/*<PDFOperation>*/ ops) {
		//Iterate through the operations and draw as each operation specifies
		GeneralPath path = new GeneralPath();
		boolean isClippingPath = false;
		boolean isClippingWindingRule = false;
		for (int i=0; i<ops.size() - 1; i++) {
			PDFOperation operation = (PDFOperation)ops.get(i);
			PDFOperator op = operation.getOperator();
			Object[] operands = operation.getOperands();
			if (op == PDFOperator.m) {
				//move the current point to the point specified by the operands
				float x = ((Number)operands[0]).floatValue();
				float y = ((Number)operands[1]).floatValue();
				path.moveTo(x, y);
			} else if (op == PDFOperator.re) {
				//draws a rectangle
				float x = ((Number)operands[0]).floatValue();
				float y = ((Number)operands[1]).floatValue();
				float width = ((Number)operands[2]).floatValue();
				float height = ((Number)operands[3]).floatValue();
				path.moveTo(x, y);
				path.lineTo(x, y-height);
				path.lineTo(x + width, y - height);
				path.lineTo(x + width, y);
				path.closePath();
			} else if (op == PDFOperator.l) {
				//draws a line
				float x = ((Number)operands[0]).floatValue();
				float y = ((Number)operands[1]).floatValue();
				path.lineTo(x, y);
			} else if (op == PDFOperator.c) {
				//draw a cubic bezier curve
				float x1 = ((Number)operands[0]).floatValue();
				float y1 = ((Number)operands[1]).floatValue();
				float x2 = ((Number)operands[2]).floatValue();
				float y2 = ((Number)operands[3]).floatValue();
				float x3 = ((Number)operands[4]).floatValue();
				float y3 = ((Number)operands[5]).floatValue();
				path.curveTo(x1, y1, x2, y2, x3, y3);
			} else if (op == PDFOperator.v) {
				//draw a cubic bezier curve with x1 y1 being the current x and y
				float x1 = ((Number)operands[0]).floatValue();
				float y1 = ((Number)operands[1]).floatValue();
				float x2 = ((Number)operands[2]).floatValue();
				float y2 = ((Number)operands[3]).floatValue();
				path.quadTo(x1, y1, x2, y2);
			} else if (op == PDFOperator.y) {
				//draw a cubic bezier curve with x2 y2 being x3, y3
				float x1 = ((Number)operands[0]).floatValue();
				float y1 = ((Number)operands[1]).floatValue();
				float x2 = ((Number)operands[2]).floatValue();
				float y2 = ((Number)operands[3]).floatValue();
				path.quadTo(x1, y1, x2, y2);
			} else if (op == PDFOperator.h) {
				//close the subpath by drawing a line from currentPoint to firstPoint. The next m operation will reset firstPoint
				path.closePath();
			} else if (op == PDFOperator.W) {
				isClippingPath = true;
				isClippingWindingRule = true;
			} else if (op == PDFOperator.W_STAR) {
				isClippingPath = true;
				isClippingWindingRule = false;
			} else {
				System.err.println("RenderSubPath failed because an unrecognized operator was encountered '" + op + "'");
			}
		}
		//read the last operation to find out what kind of drawing I'm doing (fill (winding or alternating), stroke, both, none)
		PDFOperation paintOperation = (PDFOperation)ops.get(ops.size() - 1);
		PDFOperator paintOp = paintOperation.getOperator();
		boolean stroke = true;
		boolean fill = true;
		if (paintOp == PDFOperator.S) {
			stroke = true;
			fill = false;
		} else if (paintOp == PDFOperator.s) {
			path.closePath();
			stroke = true;
			fill = false;
		} else if (paintOp == PDFOperator.f || paintOp == PDFOperator.F) {
			path.setWindingRule(GeneralPath.WIND_NON_ZERO);
			stroke = false;
			fill = true;
		} else if (paintOp == PDFOperator.f_STAR) {
			path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
			stroke = false;
			fill = true;
		} else if (paintOp == PDFOperator.B) {
			path.setWindingRule(GeneralPath.WIND_NON_ZERO);
			stroke = true;
			fill = true;
		} else if (paintOp == PDFOperator.B_STAR) {
			path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
			stroke = true;
			fill = true;
		} else if (paintOp == PDFOperator.b) {
			path.setWindingRule(GeneralPath.WIND_NON_ZERO);
			stroke = true;
			fill = true;
			path.closePath();
		} else if (paintOp == PDFOperator.b_STAR) {
			path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
			path.closePath();
			stroke = true;
			fill = true;
		} else if (paintOp == PDFOperator.n) {
			stroke = false;
			fill = false;
		} else {
			System.err.println("A bad paintOp was given to renderSubPath '" + paintOp + "'");
		}
		
		if (fill) {
			fill(g, path);
		}
		if (stroke) {
			stroke(g, path);
		}
		
		if (isClippingPath) {
			if (isClippingWindingRule) {
				path.setWindingRule(GeneralPath.WIND_NON_ZERO);
				gs.clippingPath.intersect(new Area(path));
				g.setClip(gs.clippingPath);
			} else {
				path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
				gs.clippingPath.intersect(new Area(path));
				g.setClip(gs.clippingPath);
			}
		}
	}
	
	/**
	 * Renders text to the given graphics device.
	 * This is a bit tricky because part of the work is being handled by the java libraries in java2d space, that is,
	 * with the origin in the top left corner, and part of the work is being handled in pdf space (bottom left corner).
	 * I need to rectify these two different coordinate planes before writing to the graphics object. Especially since
	 * the graphics object is going to be converting from pdf coordinates to java2d coordinates automatically.
	 * @param g
	 * @param ops
	 */
	private void renderText(Graphics2D g, List/*<PDFOperation>*/ ops) {
		//set up the text matrix
		AffineTransform textMatrix = new AffineTransform();
		Iterator itr = ops.iterator();
		while (itr.hasNext()) {
			PDFOperation operation = (PDFOperation)itr.next();
			PDFOperator op = operation.getOperator();
			Object[] operands = operation.getOperands();
			if (op == PDFOperator.Tm) {
				float a = ((Number)operands[0]).floatValue();
				float b = ((Number)operands[1]).floatValue();
				float c = ((Number)operands[2]).floatValue();
				float d = ((Number)operands[3]).floatValue();
				float e = ((Number)operands[4]).floatValue();
				float f = ((Number)operands[5]).floatValue();
				textMatrix.setTransform(a, b, c, d, e, f);
			} else if (op == PDFOperator.Tf) {
				//set the text font
				gs.font = page.getFont(operands[0].toString()).deriveFont(((Number)operands[1]).floatValue());
			} else if (op == PDFOperator.Td) {
				//position the text by translating the textMatrix
				float dX = ((Number)operands[0]).floatValue();
				float dY = ((Number)operands[1]).floatValue();
				textMatrix.translate(dX, dY);
			} else if (op == PDFOperator.TD) {
				//position the text by moving to the next line, offset from the start of the current line
				//by dX, dY. As a side effect, also set the leading to -dY
				float dX = ((Number)operands[0]).floatValue();
				float dY = ((Number)operands[1]).floatValue();
				gs.leading = (-1)*dY;
				textMatrix.translate(dX, dY);
			} else if (op == PDFOperator.T_STAR) {
				float dX = 0;
				float dY = gs.leading;
				textMatrix.translate(dX, dY);
			} else if (op == PDFOperator.Tj) {
				//show a text string
				//First, get the glyphs
				GlyphVector glyphs = gs.font.createGlyphVector(g.getFontRenderContext(), operands[0].toString());
				AffineTransform textLineMatrix = new AffineTransform(1.0, 0, 0, -1.0, 0, 0);
				textLineMatrix.preConcatenate(textMatrix);
				for (int i=0; i<glyphs.getNumGlyphs(); i++) {
					//I have no idea why, but this call MUST preceed the glyphs.setGlyphTransform call -- weird
					glyphs.getGlyphPosition(i);
					glyphs.setGlyphTransform(i, textLineMatrix);
					Shape glyph = glyphs.getGlyphOutline(i);
					fill(g, glyph);
				}
			} else if (op == PDFOperator.SINGLE_QUOTE) {
				//TODO
//				float dX = 0;
//				float dY = gs.leading;
//				textMatrix.translate(dX, dY);
//				g.drawString(operands[0].toString(), 0, 0);
			} else if (op == PDFOperator.DOUBLE_QUOTE) {
				//TODO
			} else if (op == PDFOperator.TJ) {
//				Object[] array = (Object[])operands[0];
//				for (int i=0; i<array.length; i++) {
//					if (array[i] instanceof Number) {
//						//TODO which to subtract from depends on writing mode!!!
//						float dX = (float)textMatrix.getTranslateX() - (((Number)array[i]).floatValue()*1000);
//						float dY = 0;
//						textMatrix.translate(dX, dY);
//					} else {
//						g.drawString(array[i].toString(), 0, 0);
//					}
//				}
				//TODO
			} else if (op == PDFOperator.CS || op == PDFOperator.cs || op == PDFOperator.SC || op == PDFOperator.SCN || op == PDFOperator.scn
					|| op == PDFOperator.sc || op == PDFOperator.G || op == PDFOperator.g || op == PDFOperator.RG || op == PDFOperator.rg
					|| op == PDFOperator.K || op == PDFOperator.k) {
				handleColorChange(operation);
			} else {
				System.err.println("Received unexpected command " + op);
			}
		}
	}

	private void renderXObject(Graphics2D g, String xObjectName) {
		Object xobj = page.getXObject(xObjectName);
		if (xobj instanceof PDFParser.PDFStream) {
			PDFParser.PDFStream stream = (PDFParser.PDFStream)xobj;
			try {
				ImageIcon ii = new ImageIcon(stream.readAll());
				//draw the image
				paint(g, ii);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("XObject '" + xObjectName + "' was not rendered because this parser does not yet support it");
		}
	}

	/**
	 * Prepares the given Graphics2D object before it can be used to write to. This doesn't set everything in the graphics object
	 * to the values in the GraphicsState because there is only one foreground color in g, but two foreground colors for stroking/nonstroking
	 * operations in the graphics state
	 * @param g
	 */
	void prepareGraphics(Graphics2D g) {
		//make sure the stroke is set
		g.setStroke(gs.stroke);
		//finally, make sure the affine transform is set
		g.setTransform(gs.ctm);
	}

	/**
	 * Paint the given image on the screen
	 * @param g
	 * @param img
	 */
	void paint(Graphics2D g, ImageIcon img) {
		prepareGraphics(g);
		AffineTransform at = new AffineTransform(1.0/img.getIconWidth(), 0, 0, (-1.0)/(img.getIconHeight()), 0, 1);
		at.preConcatenate(gs.ctm);
		g.setTransform(at);
		g.drawImage(img.getImage(), 0, 0, img.getImageObserver());
	}
	
	/**
	 * Stroke the given shape
	 * @param g
	 * @param s
	 */
	void stroke(Graphics2D g, Shape s) {
		prepareGraphics(g);
		g.setColor(gs.strokeColor);
		g.draw(s);
	}
	
	/**
	 * Fill the given shape
	 * @param g
	 * @param s
	 */
	void fill(Graphics2D g, Shape s) {
		prepareGraphics(g);
		g.setColor(gs.nonStrokeColor);
		g.fill(s);
	}
	
	/**
	 * A GraphicsState represents the state of the graphics configuration at a given moment in time.
	 * This implementation contains, at the very least, stubbs for each graphic state parameter mentioned
	 * in the pdf specification, with the exception of device dependant params.
	 * @author Richard Bair
	 * date: May 25, 2004
	 */
	private final class GraphicsState {
		// Device independent state parameters
		AffineTransform ctm; // this affine transform is for converting to device space
		Area clippingPath;
		ColorSpace strokeColorSpace;
		ColorSpace nonStrokeColorSpace;
		Color strokeColor;
		Color nonStrokeColor;
		//text state params
		float characterSpacing;
		float wordSpacing;
		float horizontalScaling;
		float leading;
		int renderMode;
		float rise;
		//line settings
		float width;
		int cap;
		int join;
		float miterLimit;
		float[] dashArray;
		float dashLimit;
		int flatness;
		boolean strokeAdjustment; //ignored by this implementation
		String blendMode; //TODO not implemented
		Object softMask; //TODO not implemented
		float alpha;
		boolean alphaSource;
		
		BasicStroke stroke;
		Font font;
		
		GraphicsState(Graphics2D g) {
			ctm = new AffineTransform(originalTransform);
			clippingPath = new Area(new Rectangle2D.Double(0.0, 0.0, page.getPageWidth(), page.getPageHeight()));
			g.setClip(clippingPath);
			strokeColorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			nonStrokeColorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			strokeColor = Color.BLACK;
			nonStrokeColor = Color.BLACK;
			characterSpacing = 0;
			wordSpacing = 0;
			horizontalScaling = 1.0f;
			leading = 0;
			//no explicit default for fontSize -- this is intential as per the spec
			renderMode = 0;
			rise = 0;
			width = 1;
			cap = BasicStroke.CAP_BUTT;
			join = 0;
			miterLimit = 10.0f;
			dashArray = new float[0];
			dashLimit = 0.0f;
			flatness = 1;
			strokeAdjustment = false;
			blendMode = "Normal";
			softMask = "None";
			alpha = 1.0f;
			alphaSource = false;
			
			updateStroke();
		}
		
		/**
		 * Updates the stroke variable with the current settings
		 */
		void updateStroke() {
			if (dashArray.length == 0) {
				stroke = new BasicStroke(width, cap, join, miterLimit);
			} else {
				stroke = new BasicStroke(width, cap, join, miterLimit, dashArray, dashLimit);
			}
		}
		
	}
	
}