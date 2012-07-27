/*
 * $Id: PDFParser.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

import org.jdesktop.jdnc.incubator.rbair.swing.JScrollablePanel;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.filter.Ascii85DecodeFilter;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.filter.AsciiHexDecodeFilter;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.filter.FlateDecodeFilter;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.filter.LZWDecodeFilter;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.filter.PDFFilter;
import org.jdesktop.jdnc.incubator.rbair.util.NIOUtils;

/**
 * This version of the PDFParser has been optimized for memory rich environments. After parsing any object from
 * the file, it save the object in a cache (indexed by the PDFIndirectReference of the object) so that any
 * subsequent access to that object does not require re-parsing.
 * @author Richard Bair
 */
public class PDFParser {
	static final PDFVersion CURRENT_SUPPORTED_VERSION = PDFVersion.VERSION_1_5;
	/**
	 * Cache for objects.
	 */
	private Map objectCache = new HashMap();
	private PDFHeader header;
	private List/*<PDFSection>*/ sections;
	private PDFCatalog catalog;
	private File doc;
	private FileChannel fileChannel;
	private ByteBuffer fileBuffer;
	
	public static void main(String[] args) {
		try {
//			File pdfdir = new File("c:\\pdf");
//			File[] pdffiles = pdfdir.listFiles();
//			for (int x=0; x<pdffiles.length; x++) {
//				PDFDocument document = new PDFDocument(pdffiles[x]);
//			}
//			File pdf = new File("c:\\pdf\\admin-7.2-US.pdf");
			File pdf = new File("c:\\pdf\\poopy.pdf");
			PDFParser parser = new PDFParser(pdf);
			parser.testDoIt();
//			parser.testDoItTwo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void testDoItTwo() {
		System.out.println(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
	}
	
	private void testDoIt() {
		try {
			JFrame frame = new JFrame();
			frame.getContentPane().setLayout(new BorderLayout());

			final PDFRenderer r = new PDFRenderer(this);			
			
			final JPanel panel = new JScrollablePanel() {
				protected void paintComponent(Graphics g) {
					Graphics2D g2d = (Graphics2D)g;
					g2d.scale(1.0, 1.0);
					try {
						r.render(g2d, 1);
					} catch (Exception e) {
						e.printStackTrace();
//						g2d.setColor(Color.RED);
//						g2d.fillRect(0, 0, 612, 791);
					}
				}
			};

			frame.getContentPane().add(panel);
			
//			ref = new PDFIndirectReference("3 0 R");
//			positionBuffer(ref);
//			stream = new PDFStream(fileBuffer);
//			byte[] data = stream.readAll();
//			ImageIcon ii = new ImageIcon(data);
//			frame.getContentPane().add(new JScrollPane(new JLabel(ii)));
			
			panel.setSize(6012, 7091);
			
			JButton printMe = new JButton("Today's Weather is total crap!");
			printMe.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						PrinterJob printJob = PrinterJob.getPrinterJob();
						printJob.setPrintable(new Printable() {
						  private Component componentToBePrinted;
	
						  public void print() {
						    PrinterJob printJob = PrinterJob.getPrinterJob();
						    printJob.setPrintable(this);
						    if (printJob.printDialog())
						      try {
						        printJob.print();
						      } catch(PrinterException pe) {
						        System.out.println("Error printing: " + pe);
						      }
						  }
	
						  public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
						  	componentToBePrinted = panel;
						  	
						    if (pageIndex > 0) {
						      return(NO_SUCH_PAGE);
						    } else {
						      Graphics2D g2d = (Graphics2D)g;
//						      g2d.setTransform(((Graphics2D)panel.getGraphics()).getTransform());
						      try {
						      	r.render(g2d, 1);
						      } catch (Exception e) {
						      	e.printStackTrace();
						      	return NO_SUCH_PAGE;
						      }
//						      g2d.scale(pageFormat.getImageableX()/612, pageFormat.getImageableY()/791);
//						      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
//						      disableDoubleBuffering(componentToBePrinted);
//						      componentToBePrinted.paint(g2d);
//						      enableDoubleBuffering(componentToBePrinted);
						      return(PAGE_EXISTS);
						    }
						  }
	
						  public void disableDoubleBuffering(Component c) {
						    RepaintManager currentManager = RepaintManager.currentManager(c);
						    currentManager.setDoubleBufferingEnabled(false);
						  }
	
						  public void enableDoubleBuffering(Component c) {
						    RepaintManager currentManager = RepaintManager.currentManager(c);
						    currentManager.setDoubleBufferingEnabled(true);
						  }		
						});
						printJob.print();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			frame.getContentPane().add(printMe, BorderLayout.SOUTH);
			frame.setSize(612, 811);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);

} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the parser. When the parser is created, the document is opened as a read only file, and the parser
	 * initializes itself by reading fundemental information from the pdf file.
	 */
	public PDFParser(File doc) {
		assert doc != null;
		this.doc = doc;
		try {
			RandomAccessFile raf = new RandomAccessFile(doc, "r");
			System.out.print("Trying to parse file " + doc.getName());
			fileChannel = raf.getChannel();
			try {
				fileBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
				openDocument();
				System.out.println(".....succeeded");
			} catch (Exception e) {
				System.out.println(".....failed");
				e.printStackTrace();
			}
			fileChannel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PDFParser(ByteBuffer buffer) {
		System.out.print("Trying to parse file from a buffer");
		try {
			fileBuffer = buffer;
			openDocument();
			System.out.println(".....succeeded");
		} catch (Exception e) {
			System.out.println(".....failed");
			e.printStackTrace();
		}
	}
	
	public PDFParser(InputStream is) {
		try {
			File tempFile = File.createTempFile("pdfViewer", "");
			FileOutputStream fos = new FileOutputStream(tempFile);
			byte[] buffer = new byte[8096];
			int read = -1;
			while ((read = is.read(buffer)) != -1) {
				fos.write(buffer, 0, read);
			}
			fos.close();
			is.close();
			
			RandomAccessFile raf = new RandomAccessFile(tempFile, "r");
			System.out.print("Trying to parse file " + tempFile.getName());
			fileChannel = raf.getChannel();
			try {
				fileBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
				openDocument();
				System.out.println(".....succeeded");
			} catch (Exception e) {
				System.out.println(".....failed");
				e.printStackTrace();
			}
			fileChannel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns true if this document is encrypted, false otherwise.
	 * @return
	 */
	public boolean isEncrypted() {
		return ((PDFSection)sections.get(0)).getTrailer().getDictionary().get(PDFTrailer.KEY_ENCRYPT) != null;
	}
	
	/**
	 * Returns the number of pages in this pdf
	 * @return
	 */
	public int getPageCount() {
		return catalog.getPageCount();
	}
	
	/**
	 * Returns a PDFPage object representing the requested page.
	 * @param pageNumber
	 * @return
	 */
	public PDFPage getPage(int pageNumber) {
		try {
			return catalog.getPages().getPage(pageNumber);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Opens the document. This code will actually load the trailer and xref sections into the "sections" variable, and will also
	 * create the PDFCatalog object.
	 * @throws Exception
	 */
	private void openDocument() throws Exception {
		//parse off the header
		//locate the first whitespace
		int position = 0;
		for (int i=7; i<fileBuffer.limit(); i++) {
			char c = (char)fileBuffer.get(position++);
			if (c == '\n' || c == '\0' || c == '\r' || c == ' ' || c == '\f' || c == '\t') {
				break;
			}
		}
		byte[] data = new byte[position];
		fileBuffer.get(data);
		String encodedString = new String(data, "UTF-8");
		header = new PDFHeader(encodedString);

		//load up the sections (xref's and trailers).
		sections = new ArrayList();
		//first, locate the start of the first xref (primary) section.
		fileBuffer.position(fileBuffer.limit()-1);
		position = (int)NIOUtils.locateStartPositionBackward(fileBuffer, "startxref".getBytes());
		data = new byte[(int)(fileBuffer.position() - position)];
		fileBuffer.position(position);
		fileBuffer.get(data);
		encodedString = new String(data, "UTF-8");
		List tokens = PDFParseUtils.parseTokens(encodedString);
		//now, parse off all xref and trailer sections
		PDFTrailer trailer;
		PDFXref xref;
		Object prev = tokens.get(1);
		do {
			int prevPosition = Integer.parseInt(prev.toString());
			//parse off the xref
			fileBuffer.position(prevPosition);
			position = (int)NIOUtils.locateEndPositionForward(fileBuffer, "trailer".getBytes()) - 7;
			data = new byte[(int)(position - fileBuffer.position())];
			fileBuffer.get(data);
			encodedString = new String(data, "UTF-8");
			xref = new PDFXref(encodedString);
			//now parse off the trailer
			fileBuffer.position(prevPosition + data.length);
			position = (int)NIOUtils.locateEndPositionForward(fileBuffer, "%%EOF".getBytes());
			data = new byte[(int)(position - fileBuffer.position())];
			fileBuffer.get(data);
			encodedString = new String(data, "UTF-8");
			trailer = new PDFTrailer(encodedString);
			//create the section
			PDFSection section = new PDFSection(xref, trailer);
			sections.add(section);
		}while ((prev = trailer.getDictionary().get(PDFTrailer.KEY_PREV)) != null);

		//parse the catalog
		String catalogRefString = ((PDFSection)sections.get(0)).getTrailer().getDictionary().get(PDFTrailer.KEY_ROOT).toString();
		PDFIndirectReference catalogRef = new PDFIndirectReference(catalogRefString);
		catalog = new PDFCatalog(getObjectEncodedString(catalogRef));
		objectCache.put(catalogRef, catalog);
		
		//check for the presence of encryption. If the file is encrypted, throw an error because we do not support
		//the parsing of encrypted documents yet.
		if (isEncrypted()) {
			throw new Exception("Sorry, you attempted to open an encrypted document and this pdf library does not yet support reading encrypted pdf documents");
		}
	}

	/**
	 * Reads the given indirect reference (of the form NN NN R where N is some number) and returns the object
	 * associated with that reference.
	 * @param indirectReference
	 * @return
	 */
	String getObjectEncodedString(PDFIndirectReference ref) {
		try {
			int oldPosition = fileBuffer.position();
			positionBuffer(ref);
			int endPosition = (int)NIOUtils.locateEndPositionForward(fileBuffer, "endobj".getBytes());
			byte[] data = new byte[endPosition - fileBuffer.position()];
			fileBuffer.get(data);
			fileBuffer.position(oldPosition);
			return new String(data, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Code that will position the buffer at the beginning of this indirect reference
	 * @param ref
	 */
	private void positionBuffer(PDFIndirectReference ref) {
		//first, find the PDFXrefEntry for this indirectReference
		Iterator itr = sections.iterator();
		while (itr.hasNext()) {
			PDFSection section = (PDFSection)itr.next();
			PDFXrefEntry entry = (PDFXrefEntry)section.getXref().getTable().get(ref);
			if (entry != null) {
				fileBuffer.position((int)entry.getByteOffset());
			}
		}
	}
	
	/**
	 * This method decides if the given token is a literal token, or an indirect reference.
	 * If it is a literal token, then the token object will be returned. If it is an
	 * indirect reference, then we head to that portion of the file denoted by the
	 * reference. We read from the beginning of the object until either we hit an
	 * endobj token, or a stream token. If we hit stream, then this is a stream object,
	 * and we create a stream object and return it. If we hit endobj, then this is a normal
	 * object, and we parse out the object data and pass it to PDFParseUtils.parseToken.
	 * <br>
	 * Note that this method will save each object created by an indirect reference to the object cache
	 * and restore from that cache if the obj passed in is an indirect reference that is in the cache
	 * @param token
	 * @return
	 */
	private Object getObject(Object obj) {
		if (obj instanceof PDFIndirectReference) {
			PDFIndirectReference ref = (PDFIndirectReference)obj;
			if (objectCache.containsKey(ref)) {
				return objectCache.get(ref);
			}
			positionBuffer(ref);
			int originalPosition = fileBuffer.position();
			//if this is -1, after the while loop below, then a stream was found,
			//otherwise a normal object was found and this refers to the position
			//JUST BEFORE the endobj token
			int endobjPosition = -1;
			//represents the position JUST FOLLOWING the obj token
			int objPosition = -1;
			boolean found = false;
			int index = originalPosition;
			while (!found && index <= fileBuffer.limit()) {
				if (fileBuffer.get(index) == 'o' &&
						fileBuffer.get(index + 1) == 'b' &&
						fileBuffer.get(index + 2) == 'j' &&
						objPosition == -1) {
					objPosition = index + 3;
				}
				if (fileBuffer.get(index) == 'e' &&
						fileBuffer.get(index + 1) == 'n' &&
						fileBuffer.get(index + 2) == 'd' &&
						fileBuffer.get(index + 3) == 'o' &&
						fileBuffer.get(index + 4) == 'b' &&
						fileBuffer.get(index + 5) == 'j') {
					endobjPosition = index - 1;
					found = true;
				} else if (fileBuffer.get(index) == 's' &&
						fileBuffer.get(index + 1) == 't' &&
						fileBuffer.get(index + 2) == 'r' &&
						fileBuffer.get(index + 3) == 'e' &&
						fileBuffer.get(index + 4) == 'a' &&
						fileBuffer.get(index + 5) == 'm') {
					endobjPosition = -1;
					found = true;
				}
				index++;
			}
			if (endobjPosition == -1) {
				//its a stream
				fileBuffer.position(originalPosition);
				Object pdfStream = new PDFStream(fileBuffer);
				objectCache.put(ref, pdfStream);
				return pdfStream;
			} else {
				byte[] data = new byte[endobjPosition - objPosition];
				fileBuffer.position(objPosition);
				fileBuffer.get(data);
				String encodedString = new String(data).trim();
				Object token = PDFParseUtils.parseToken(encodedString);
				objectCache.put(ref, token);
				return token;
			}
		} else {
			return obj;
		}
	}
	
	/**
	 * Represents a stream of data in a PDF stream object. The dictionary is saved here, and the data can be retrieved via the
	 * InputStream's read method.
	 * @author Richard Bair
	 * date: May 21, 2004
	 */
	class PDFStream /*extends InputStream*/ {
		private static final String KEY_LENGTH = "Length";
		private static final String KEY_FILTER = "Filter";
		private static final String KEY_DECODE_PARMS = "DecodeParms";
		private static final String KEY_F = "F";
		private static final String KEY_FFILTER = "FFilter";
		private static final String KEY_FDECODE_PARMS = "FDecodeParms";
		private static final String KEY_DL = "DL";
		protected Map dictionary;
		private int position = -1;
		private int maxPosition = -1;
		private byte[] streamData;

		/**
		 * Creates a new PDFStream. The buffer is the buffer that contains the stream. When passed to this constructor, the
		 * buffer.position() must be at the beginning of the object. The buffer will be returned
		 * with this same position
		 * @param buffer
		 */
		PDFStream(ByteBuffer buffer) {
			try {
				//store the buffer position
				int originalPosition = buffer.position();
				//read the dictionary
				int dictionaryPosition = (int)NIOUtils.locateEndPositionForward(buffer, "obj".getBytes());
				int bracketCount = 0;
				while (bracketCount == 0) {
					char c = (char)buffer.get();
					if (c == '<') {
						bracketCount = 1;
					}
				}
				while (bracketCount > 0) {
					char c = (char)buffer.get();
					//if c is an open paren, skip everything until I get to the close paren
					if (c == '(') {
						boolean closeFound = false;
						while (!closeFound) {
							char prev = c;
							c = (char)buffer.get();
							if (c == ')' && prev != '\\') {
								closeFound = true;
							}
						}
					} else if (c == '<') {
						bracketCount++;
					} else if (c == '>') {
						bracketCount--;
					}
				}
				byte[] data = new byte[buffer.position() - dictionaryPosition];
				buffer.position(dictionaryPosition);
				buffer.get(data);
				String encodedString = new String(data, "UTF-8").trim();
				dictionary = (Map)PDFParseUtils.parseToken(encodedString);
				buffer.position(dictionaryPosition + data.length);
				//set the position
				position = (int)NIOUtils.locateEndPositionForward(buffer, "stream".getBytes());
				/*stream is followed by either a line feed, or a carriage return and linefeed, according to the spec*/
				if (buffer.get(position) == '\n') {
					position = position + 1;
				} else {
					position = position + 2;
				}
				//find the endstream and set the maxPosition
				maxPosition = getLength() + position;
				//reset the buffer position
				buffer.position(originalPosition);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public byte[] readAll() throws Exception {
//			byte[] data = new byte[getLength()];
//			fileBuffer.get(data);
//			streamData = new int[data.length];
//			for (int i=0; i<data.length; i++) {
//				streamData[i] = data[i] & 0xFF;
//			}
			if (streamData != null) {
				return streamData;
			}
			
			streamData = new byte[getLength()];
			fileBuffer.position(position);
			fileBuffer.get(streamData);
			
			//decode the data
			PDFFilter filter = null;
			//get the filter (or filters) to use
			Object obj = dictionary.get(KEY_FILTER);
			if (obj != null) {
				//the array of filters to apply
				Object[] filters = null;
				//if obj is a string, then there is only one filter to apply, so add the filter name to the array of filters
				if (obj instanceof String) {
					filters = new String[]{(String)obj};
				} else {
					//otherwise, there were multiple filters to apply, and they are in the given object array. They are to be applied in order
					filters = (Object[])obj;
				}
				//for each filter to apply
				for (int i=0; i<filters.length; i++) {
					//get the filter
					String s = filters[i].toString();
					if (s.equals("ASCIIHexDecode") || s.equals("AHx")) {
						filter = new AsciiHexDecodeFilter();
					} else if (s.equals("ASCII85Decode") || s.equals("A85")) {
						filter = new Ascii85DecodeFilter();
					} else if (s.equals("LZWDecode") || s.equals("LZW")) {
						filter = new LZWDecodeFilter();
					} else if (s.equals("FlateDecode") || s.equals("Fl")) {
						filter = new FlateDecodeFilter();
					} else if (s.equals("RunLengthDecode") || s.equals("RL")) {
						//TODO
					} else if (s.equals("CCITTFaxDecode") || s.equals("CCF")) {
						//TODO
					} else if (s.equals("DCTDecode") || s.equals("DCT")) {
						//I'm not decoding this... this is a jpeg!
					} else {
						throw new Exception ("Failed to create a filter for filter name '" + filter + "'. Filter not recognized");
					}
					if (filter != null) {
						//apply the filter
						streamData = filter.decode(streamData);
					}
				}
			}
			//return the data
			return streamData;
		}
		
		/**
		 * Returns the number of bytes in this stream
		 * @return
		 */
		public int getLength() {
			Object obj = getObject(dictionary.get(KEY_LENGTH));
			return ((Integer)obj).intValue();
		}
	}
	
	/**
	 * Encapsulates an entire content stream. This object will construct a list of operations (PDFOperation object).
	 * Each operation can then be called and used to draw etc.
	 * @author Richard Bair
	 * date: May 24, 2004
	 */
	private class PDFContentStream extends PDFStream {
		private static final String KEY_RESOURCES = "Resources";
		private static final String RES_KEY_EXT_G_STATE = "ExtGState";
		private static final String RES_KEY_COLOR_SPACE = "ColorSpace";
		private static final String RES_KEY_PATTERN = "Pattern";
		private static final String RES_KEY_SHADING = "Shading";
		private static final String RES_KEY_X_OBJECT = "XObject";
		private static final String RES_KEY_FONT = "Font";
		private static final String RES_KEY_PROC_SET = "ProcSet";
		private static final String RES_KEY_PROPERTIES = "Properties";
		private List operations = new ArrayList();
		PDFContentStream(ByteBuffer buffer) {
			super(buffer);
			
			//read the entire stream into memory, and let the parsing begin!
			try {
				byte[] data = super.readAll();
				String s = new String(data);
				List tokens = PDFParseUtils.parseTokens(s);
				Iterator itr = tokens.iterator();
				List operands = new ArrayList();
				while (itr.hasNext()) {
					Object token = PDFParseUtils.parseToken(itr.next().toString());
					if (token instanceof PDFOperator) {
						Object[] ops = operands.toArray();
						PDFOperator op = (PDFOperator)token;
						PDFOperation operation = new PDFOperation(ops, op);
						operations.add(operation);
						operands.clear();
					} else {
						operands.add(token);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * Represents an actual object in the pdf file, such as a Catalog, Page, Line, Rectangle, Image, etc.
	 * @author Richard Bair
	 * date: May 21, 2004
	 */
	private class PDFObject {
		protected Object realObject;
		
		/**
		 * Create a new PDFObject
		 * @param encodedString
		 */
		PDFObject(String encodedString) {
			//rip off the first tokens and the last token
			encodedString = encodedString.substring(encodedString.indexOf("obj") + 3, encodedString.lastIndexOf("endobj")).trim();
			parse(encodedString);
		}
		
		/**
		 * This method ALWAYS has the opening tokens (NN NN obj where N is some number) and the closing
		 * token (endobj) removed, as well as any whitespace after the first tokens and before the last token.
		 * @param encodedString
		 */
		protected void parse(String encodedString) {
			realObject = PDFParseUtils.parseToken(encodedString);
		}
	}
	
	/**
	 * Represents the PDFCatalog
	 * @author Richard Bair
	 * date: May 21, 2004
	 */
	private final class PDFCatalog extends PDFObject {
		private static final String KEY_TYPE = "Type";
		private static final String KEY_VERSION = "Version";
		private static final String KEY_PAGES = "Pages";
		private static final String KEY_PAGE_LABELS = "PageLabels";
		private static final String KEY_NAMES = "Name";
		private static final String KEY_DESTS = "Dests";
		private static final String KEY_VIEWER_PREFERENCES = "ViewerPreferences";
		private static final String KEY_PAGE_LAYOUT = "PageLayout";
		private static final String KEY_PAGE_MODE = "PageMode";
		private static final String KEY_OUTLINES = "Outlines";
		private static final String KEY_THREADS = "Threads";
		private static final String KEY_OPEN_ACTION = "OpenAction";
		private static final String KEY_AA = "AA";
		private static final String KEY_URI = "URI";
		private static final String KEY_ACRO_FORM = "AcroForm";
		private static final String KEY_METADATA = "Metadata";
		private static final String KEY_STRUCT_TREE_ROOT = "StructTreeRoot";
		private static final String KEY_MARK_INFO = "MarkInfo";
		private static final String KEY_LANG = "Lang";
		private static final String KEY_SPIDER_INFO = "SpiderInfo";
		private static final String KEY_OUTPUT_INTENTS = "OutputIntents";
		private static final String KEY_PIECE_INFO = "PieceInfo";
		private static final String KEY_OC_PROPERTIES = "OCProperties";
		private static final String KEY_PERMS = "Perms";
		private static final String KEY_LEGAL = "Legal";
		
		private PDFPages pages;
		
		PDFCatalog(String encodedString) {
			super(encodedString);
		}

		private Map getData() {
			return (Map)realObject;
		}
		
		private PDFPages getPages() {
			if (pages == null) {
				PDFIndirectReference ref = new PDFIndirectReference(getData().get(PDFCatalog.KEY_PAGES).toString()); 
				pages = new PDFPages(getObjectEncodedString(ref));
				objectCache.put(ref, pages);
			}
			return pages;
		}
		
		int getPageCount() {
			return getPages().getPageCount();
		}
	}

	/**
	 * Represents the Pages element in the PDFCatalog, or, in other words, a PageTreeNode. Contains children pages
	 * @author Richard Bair
	 * date: May 21, 2004
	 */
	private final class PDFPages extends PDFObject {
		/**
		 * Key for indicating the type of node this is. The value associated with this key should
		 * ALWAYS be "Pages" (without the quotes)
		 */
		private static final String KEY_TYPE = "Type";
		/**
		 * The value that this key accesses is a reference to the parent Pages node of this node.
		 */
		private static final String KEY_PARENT = "Parent";
		/**
		 * The key used to get an array of page nodes that are direct descendants of this node. These
		 * kid nodes may be PDFPages or PDFPage objects.
		 */
		private static final String KEY_KIDS = "Kids";
		/**
		 * The key used to get the number of PDFPage nodes beneath this node. This is recursive (as I understand it),
		 * meaning that it counts not just the PDFPage objects that are direct descendants, but that are recursively
		 * descendant.
		 */
		private static final String KEY_COUNT = "Count";
		
		/**
		 * The parent of this pages object. Only the root node will have null here
		 */
		private PDFPages parent;
		
		/**
		 * Construct a new PDFPages object based on the given encoded string
		 * @author Richard Bair
		 * date: May 26, 2004
		 */
		PDFPages(String encodedString) {
			super(encodedString);
		}

		/**
		 * Construct a new PDFPages object based on the given encoded string
		 * @author Richard Bair
		 * date: May 26, 2004
		 */
		PDFPages(PDFPages parent, String encodedString) {
			super(encodedString);
			this.parent = parent;
		}
		
		/**
		 * Helper function to get the map that this object is built from
		 * @return
		 */
		private Map getData() {
			return (Map)realObject;
		}
		
		/**
		 * Returns the number of pages beneath this PDFPages object. If this is the root object, then
		 * this number will be the number of pages in the document.
		 * @return
		 */
		int getPageCount() {
			Integer count = (Integer)getData().get(KEY_COUNT);
			return count == null ? 1 : count.intValue();
		}
		
		/**
		 * Returns the specified page based on page number
		 * @param pageNumber 1 based index into the PDFPages' list of pages
		 * @return
		 */
		PDFPage getPage(int pageNumber) {
			//if the page number is too small or too big, log an error and return null
			if (pageNumber < 1 || pageNumber > getPageCount()) {
				System.err.println("An invalid page number of '" + pageNumber + "' was fed to a PDFPages object");
			}
			
			/*
			 * Walk my way through the page tree. Keep an index of how many pages I've seen so far, and of course
			 * I know the number of the page I'm looking for.
			 * Loop through the kids. If the kid is a page and the current page matches the page number I'm looking for,
			 * bingo I found it, return it. If the numbers don't match, then increment the current page.
			 * If the kid is a pages object, and if the pages.getPageCount() + currentPage is less than or equal to the page
			 * number, then that kid contains the page I need. Adjust the pageNumber such that the kid will have a pageNumber
			 * with respect to it (pageNumber = pageNumber - currentPage).
			 * If the kid is a pages object, but the pages.getPageCount() + currentPage is less than the pageNumber, then increment
			 * currentPage by pages.getPageCount() and continue the looping.
			 * 
			 * Sheesh, the code is clearer than the comment...
			 */
			Object[] kids = (Object[])getData().get(KEY_KIDS);
			int currentPage = 1;
			for (int i=0; i<kids.length; i++) {
				PDFIndirectReference ref = new PDFIndirectReference(kids[i].toString());
				//kind of a hack... TODO Must change to check the cache before using these
				//indirect references, etc.
				Map map = (Map)getObject(kids[i]);
				Object obj = null;
				if (map.get("Type").toString().equals("Pages")) {
					obj = new PDFPages(this, getObjectEncodedString(ref));
				} else {
					obj = new PDFPage(this, getObjectEncodedString(ref));
				}
				if (obj instanceof PDFPages) {
					PDFPages pages = (PDFPages)obj;
					if (pageNumber <= pages.getPageCount() + currentPage) {
						return pages.getPage(pageNumber - (currentPage - 1));
					} else {
						currentPage += pages.getPageCount();
					}
					pages.getPage(pageNumber);
				} else {
					//NOTE I added the ">" here to catch any infinate looping situations. It will return erroneously.
					//It would be better to check for the condition and throw an exception
					if (currentPage >= pageNumber) {
						return (PDFPage)obj;
					} else {
						currentPage++;
					}
				}
			}
			System.err.println("THe page wasn't found. This shouldn't have happened!");
			return null;
		}
	}
	
	/**
	 * Represents a specific page in the document.
	 * @author Richard Bair
	 * date: May 21, 2004
	 */
	final class PDFPage extends PDFObject {
		private static final String KEY_TYPE = "Type";
		private static final String KEY_PARENT = "Parent";
		private static final String KEY_LAST_MODIFIED = "LastModified";
		private static final String KEY_RESOURCES = "Resources";
		private static final String KEY_MEDIA_BOX = "MediaBox";
		private static final String KEY_CROP_BOX = "CropBox";
		private static final String KEY_BLEED_BOX = "BleedBox";
		private static final String KEY_TRIM_BOX = "TrimBox";
		private static final String KEY_ART_BOX = "ArtBox";
		private static final String KEY_BOX_COLOR_INFO = "BoxColorInfo";
		private static final String KEY_CONTENTS = "Contents";
		private static final String KEY_ROTATE = "Rotate";
		private static final String KEY_GROUP = "Group";
		private static final String KEY_THUMB = "Thumb";
		private static final String KEY_B = "B";
		private static final String KEY_DUR = "Dur";
		private static final String KEY_TRANS = "Trans";
		private static final String KEY_ANNOTS = "Annots";
		private static final String KEY_AA = "AA";
		private static final String KEY_METADATA = "Metadata";
		private static final String KEY_PIECE_INFO = "PieceInfo";
		private static final String KEY_STRUCT_PARENTS = "StructParents";
		private static final String KEY_ID = "ID";
		private static final String KEY_PZ = "PZ";
		private static final String KEY_SEPARATION_INFO = "SeparationInfo";
		private static final String KEY_TABS = "Tabs";
		private static final String KEY_TEMPLATE_INSTANTIATED = "TemplateInstantiated";
		private static final String KEY_PRES_STEPS = "PresSteps";

		private static final String RES_KEY_EXT_G_STATE = "ExtGState";
		private static final String RES_KEY_COLOR_SPACE = "ColorSpace";
		private static final String RES_KEY_PATTERN = "Pattern";
		private static final String RES_KEY_SHADING = "Shading";
		private static final String RES_KEY_X_OBJECT = "XObject";
		private static final String RES_KEY_FONT = "Font";
		private static final String RES_KEY_PROC_SET = "ProcSet";
		private static final String RES_KEY_PROPERTIES = "Properties";
		/**
		 * A reference to the parent of this page. This is always an actual object since you cannot get a page without having
		 * instantiated the parent in the first place
		 */
		private PDFPages parent;
		/**
		 * The date that this page was last modified
		 */
		private Date lastModified;
		/**
		 * Map of resources used to produce the page.
		 * This is required. If this is null, then it is to be inherited from the parent.
		 */
		private Map resources;
		/**
		 * A rectangle defining the boundaries of the physical medium on which the page is intended to be displayed.
		 * Required, inheritable
		 */
		private Rectangle2D mediaBox;
		/**
		 * A rectangle defining the visible region. The default value is the same as the mediaBox.
		 * Optional, inheritable.
		 */
		private Rectangle2D cropBox;
		/**
		 * A rectangle defining the region to be clipped when output to the production environment.
		 * Optional. Defaults to the same value as the cropBox.
		 */
		private Rectangle2D bleedBox;
		/**
		 * A rectangle defining the intended dimensions of the page after trimming (after printing too, obviously)
		 * Optional. Defaults to the same value as the cropBox.
		 */
		private Rectangle2D trimBox;
		/**
		 * A rectangle defining a bounding box around the documents meaningful content as intended by the pages creator.
		 * Optional. Default value is the cropBox.
		 */
		private Rectangle2D artBox;
		/**
		 * A map of information about what the colors and other visual characteristics are to be used for displaying guidelines on
		 * the screen for the various page boundaries. Optional.
		 */
		private Map boxColorInfo;
		/**
		 * The actual content stream of the page. Note that when the stream is read from the file, it may be a stream OR an array of streams.
		 * This variable, however, will always be a single stream. Optional.
		 */
		private PDFContentStream contents;
		/**
		 * The number of degrees by which the page should be rotated clockwise. Optional. Must be a multiple of 90.
		 */
		private int rotate;
		/**
		 * Specifies the attributes of the page's page group for use in the transparent imaging model. Optional.
		 * TODO Unimplemented in this release
		 */
		private Map group;
		/**
		 * A stream representing the page's thumbnail image. Optional.
		 */
		private PDFStream thumbnail;
		/**
		 * An array of indirect references to <i>article beads</i>. Optional. TODO Unimplemented in this release.
		 */
		private Object[] beads;
		/**
		 * The amount of time this page is to be displayed before automatically advancing to the next page.
		 * Optional. TODO Unimplemented in this release.
		 */
		private float duration;
		/**
		 * A transition dictionary describing transition effects from one page to the next (as when doing a presentation).
		 * Optional. TODO Unimplemented in this release
		 */
		private Map transitions;
		/**
		 * An array of Annotation dictionaries. Optional. TODO Unimplemented in this release
		 */
		private Object[] annotations;
		/**
		 * Optional. TODO Unimplemented in this release
		 */
		private Map additionalActions;
		/**
		 * Optional. TODO Unimplemented in this release
		 */
		private PDFStream metadata;
		/**
		 * Optional. TODO Unimplemented in this release
		 */
		private Map pieceInfo;
		/**
		 * Required if this page contains structural items. TODO Unimplemented in this release
		 */
		private int structParents;
		/**
		 * Optional. TODO Unimplemented in this release
		 */
		private String ID;
		/**
		 * Optional. TODO Unimplemented in this release
		 */
		private float preferredZoom;
		/**
		 * Optional. TODO Unimplemented in this release
		 */
		private Map separationInfo;
		/**
		 * Optional. TODO Unimplemented in this release
		 */
		private String tabOrder;
		/**
		 * Optional. TODO Unimplemented in this release
		 */
		private String templateInstantiated;
		/**
		 * Optional. TODO Unimplemented in this release
		 */
		private Map presSteps;
		
		/**
		 * Create a new PDFPage based on the given encoded string
		 * @param encodedString
		 */
		PDFPage(PDFPages parent, String encodedString) {
			super(encodedString);
			this.parent = parent;
		}
		
		/**
		 * Helper function to get the map that this object is built from
		 * @return
		 */
		private Map getData() {
			return (Map)realObject;
		}
		
		/**
		 * Helper function to get the contents
		 * @return
		 */
		private PDFContentStream getContents() {
			if (contents == null) {
				Object temp = getData().get(KEY_CONTENTS);
				// construct the PDFContentStream
				if (temp != null) {
					PDFIndirectReference ref = (PDFIndirectReference)temp;
					if (objectCache.containsKey(ref)) {
						contents = (PDFContentStream)objectCache.get(ref);
					} else {
						positionBuffer(ref);
						contents = new PDFContentStream(fileBuffer);
						objectCache.put(ref, contents);
					}
				}
			}
			return contents;
		}
		
		/**
		 * Returns the postscript style operations that are used to construct the page
		 * @return
		 */
		List/*<PDFOperation>*/ getOperations() {
			PDFContentStream contents = getContents();
			return contents == null ? new ArrayList() : contents.operations;
		}

		/**
		 * This is a shorthand method for getting the height of the art box. As I understand it, the art box
		 * is the WHOLE enchilada. The Media box is some subset (include the whole) of the art box.
		 * @return
		 */
		double getPageHeight() {
			return getArtBox().getHeight();
		}

		/**
		 * This is a shorthand method for getting the width of the art box.
		 * @return
		 */
		double getPageWidth() {
			return getArtBox().getWidth();
		}
		
		/**
		 * Returns the art box associated with this page
		 * @return
		 */
		Rectangle2D getArtBox() {
			if (artBox == null) {
				Object obj = getData().get(KEY_ART_BOX);
				//if null, then the art box is the same as the crop box
				if (obj == null) {
					return getCropBox();
				} else {
					//construct the Rectangle based on the data from the file
					Object[] data = (Object[])getObject(obj);
					artBox = PDFParseUtils.constructRectangle(data);
				}
			}
			return artBox;			
		}
		
		/**
		 * Returns the bleed box associated with this page
		 * @return
		 */
		Rectangle2D getBleedBox() {
			if (bleedBox == null) {
				Object obj = getData().get(KEY_BLEED_BOX);
				//if null, then the bleed box is the same as the crop box
				if (obj == null) {
					return getCropBox();
				} else {
					//construct the Rectangle based on the data from the file
					Object[] data = (Object[])getObject(obj);
					bleedBox = PDFParseUtils.constructRectangle(data);
				}
			}
			return bleedBox;			
		}
		
		/**
		 * Returns the trim box associated with this page
		 * @return
		 */
		Rectangle2D getTrimBox() {
			if (trimBox == null) {
				Object obj = getData().get(KEY_TRIM_BOX);
				//if null, then the trim box is the same as the crop box
				if (obj == null) {
					return getCropBox();
				} else {
					//construct the Rectangle based on the data from the file
					Object[] data = (Object[])getObject(obj);
					trimBox = PDFParseUtils.constructRectangle(data);
				}
			}
			return trimBox;			
		}
		
		/**
		 * Returns the crop box associated with this page
		 * @return
		 */
		Rectangle2D getCropBox() {
			if (cropBox == null) {
				Object obj = getData().get(KEY_CROP_BOX);
				//if null, then first check the parents to see if this attribute is defined somewhere above in the heiarchy. If not,
				//then default to the mediaBox.
				if (obj == null) {
					//check the parents
					obj = getInheritable(KEY_CROP_BOX);
					if (obj == null) {
						//its got to be the media box at this point
						return getMediaBox();
					} else {
						//construct the Rectangle based on the data from the file
						Object[] data = (Object[])getObject(obj);
						cropBox = PDFParseUtils.constructRectangle(data);
					}
				} else {
					//construct the Rectangle based on the data from the file
					Object[] data = (Object[])getObject(obj);
					cropBox = PDFParseUtils.constructRectangle(data);
				}
			}
			return cropBox;
		}
		
		/**
		 * Returns the media box associated with this page
		 * @return
		 */
		Rectangle2D getMediaBox() {
			if (mediaBox == null) {
				Object obj = getData().get(KEY_MEDIA_BOX);
				//if null, then first check the parents to see if this attribute is defined somewhere above in the heiarchy. If not, then
				//there is an error because the media box MUST be defined somewhere
				if (obj == null) {
					obj = getInheritable(KEY_MEDIA_BOX);
					if (obj == null) {
						System.err.println("Failed to locate the media box for page '" + toString() + "'");
						return new Rectangle2D.Double(0, 0, 0, 0);
					} else {
						//construct the Rectangle based on the data from the file
						Object[] data = (Object[])getObject(obj);
						mediaBox = PDFParseUtils.constructRectangle(data);
					}
				} else {
					//construct the Rectangle based on the data from the file
					Object[] data = (Object[])getObject(obj);
					mediaBox = PDFParseUtils.constructRectangle(data);
				}
			}
			return mediaBox;
		}

		/**
		 * Retrieves the resources map for this page
		 * @return
		 */
		private Map getResources() {
			if (resources == null) {
				Object obj = getData().get(KEY_RESOURCES);
				//if null, then check the inheritence heiarchy, since it MUST be there
				if (obj == null) {
					obj = getInheritable(KEY_RESOURCES);
					if (obj == null) {
						System.err.println("Failed to locate the resources mapping for page '" + toString() + "'");
						return new HashMap();
					} else {
						resources = (Map)obj;
					}
				} else {
					resources = (Map)obj;
				}
			}
			return resources;
		}
		
		/**
		 * Returns the font associated with the given named resource
		 * @param name
		 * @return
		 */
		Font getFont(String name) {
			Map fontsMap = (Map)getResources().get(RES_KEY_FONT);
			if (fontsMap != null && fontsMap.get(name) != null) {
				PDFIndirectReference ref = (PDFIndirectReference)fontsMap.get(name);
				//check the fontCache
				PDFFont font = (PDFFont)objectCache.get(ref);
				if (font == null) {
					//wasn't in the cache, so create a new PDFFont
					font = new PDFFont(getObjectEncodedString(ref));
					objectCache.put(ref, font);
				}
				return font.getFont();
			} else {
				System.err.println("Could not find the font named '" + name + "'. Returning the default java 'Dialog' font instead");
				return Font.getFont("Dialog");
			}
		}
		
		/**
		 * Returns the color space associated with the given named resource
		 * TODO Not currently supported
		 * @param name
		 * @return
		 */
//		ColorSpace getColorSpace(String name) {
//			
//		}
		
		/**
		 * Returns the Map of graphics state parameters associated with the given named resource
		 * TODO Not currently supported
		 * @param name
		 * @return
		 */
//		Map getExternalGraphicsState(String name) {
//			
//		}
		
		/**
		 * Returns the XObject associated with the given named resource
		 */
		Object getXObject(String name) {
			Map xobjects = (Map)getResources().get(RES_KEY_X_OBJECT);
			if (xobjects != null && xobjects.get(name) != null) {
				PDFIndirectReference ref = (PDFIndirectReference)xobjects.get(name);
				//check the xobject cache
				return getObject(ref);
			} else {
				System.err.println("Could not find the xobject name '" + name + "'. Returning null");
				return null;
			}
		}
		
		//TODO The other named resources (Pattern, Shading, ProcSet and Properties) need to be implemented in a later release
		
		/**
		 * Recurses up the list of parents until it finds one that has a value for the given key. If it gets to
		 * the top without any luck, it returns null.
		 * @param key
		 * @return
		 */
		private Object getInheritable(String key) {
			PDFPages parent = this.parent;
			while (parent != null) {
				Object obj = parent.getData().get(key);
				if (obj != null) {
					return obj;
				} else {
					parent = parent.parent;
				}
			}
			return null;
		}
	}

	/**
	 * Represents a font as stored in the PDF file. This object has the capacity to return a java Font object corrosponding to this
	 * PDFFont.
	 * TODO Currently only TrueType and Type1 fonts are supported
	 * @author Richard Bair
	 * date: May 26, 2004
	 */
	private final class PDFFont extends PDFObject {
		private static final String KEY_SUBTYPE = "Subtype";
		private static final String KEY_BASE_FONT = "BaseFont";
		private static final String KEY_FIRST_CHAR = "FirstChar";
		private static final String KEY_LAST_CHAR = "LastChar";
		private static final String KEY_WIDTHS = "Widths";
		private static final String KEY_FONT_DESCRIPTOR = "FontDescriptor";
		private static final String KEY_ENCODING = "Encoding";
		private static final String KEY_TO_UNICODE = "ToUnicode";
		
		/**
		 * The java font that most nearly approximates the font specified in the file
		 */
		private Font font;
		
		/**
		 * Create a new PDFFont
		 * @param encodedString
		 */
		PDFFont(String encodedString) {
			super(encodedString);
		}
		
		/**
		 * Helper function to get the map that this object is built from
		 * @return
		 */
		private Map getData() {
			return (Map)realObject;
		}
		
		/**
		 * Get the java font that most nearly approximates the font specified in the file
		 * @return
		 */
		Font getFont() {
			if (font == null) {
				//here is where we figure out what font to return. TODO For now, just check for one of the 14 standard fonts and return
				//a basic java font that correlates
				String baseFont = (String)getData().get(KEY_BASE_FONT);
				if (baseFont.equals("Times-Roman")) {
					font = Font.decode("Serif");
				} else if (baseFont.equals("Times-Bold")) {
					font = Font.decode("Serif-BOLD");
				} else if (baseFont.equals("Times-Italic")) {
					font = Font.decode("Serif-ITALIC");
				} else if (baseFont.equals("Times-BoldItalic")) {
					font = Font.decode("Serif-BOLDITALIC");
				} else if (baseFont.equals("Helvetica")) {
					font = Font.decode("SansSerif");
				} else if (baseFont.equals("Helvetica-Bold")) {
					font = Font.decode("SansSerif-BOLD");
				} else if (baseFont.equals("Helvetica-Oblique")) {
					font = Font.decode("SansSerif-ITALIC");
				} else if (baseFont.equals("Helvetica-BoldOblique")) {
					font = Font.decode("SansSerif-BOLDITALIC");
				} else if (baseFont.equals("Courier")) {
					font = Font.decode("Monospaced");
				} else if (baseFont.equals("Courier-Bold")) {
					font = Font.decode("Monospaced-BOLD");
				} else if (baseFont.equals("Courier-Oblique")) {
					font = Font.decode("Monospaced-ITALIC");
				} else if (baseFont.equals("Courier-BoldOblique")) {
					font = Font.decode("Monospaced-BOLDITALIC");
				} else if (baseFont.equals("Symbol")) {
					System.err.println("Sorry, Symbol font is unsupported");
					font = Font.decode("Serif");
				} else if (baseFont.equals("ZapfDingbats")) {
					System.err.println("Sorry, ZapfDingbats font is unsupported");
					font = Font.decode("Serif");
				} else {
					System.err.println("Sorry, " + baseFont + " is a currently unsupported font");
					font = Font.decode("SansSerif");
				}
			}
			return font;
		}
		
	}
}