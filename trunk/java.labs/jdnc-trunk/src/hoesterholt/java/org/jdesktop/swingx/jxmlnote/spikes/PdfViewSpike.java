/* ******************************************************************************
 *
 *       Copyright 2008-2010 Hans Oesterholt-Dijkema
 *       This file is part of the JDesktop SwingX library
 *       and part of the SwingLabs project
 *
 *   SwingX is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as 
 *   published by the Free Software Foundation, either version 3 of 
 *   the License, or (at your option) any later version.
 *
 *   SwingX is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with SwingX.  If not, see <http://www.gnu.org/licenses/>.
 *   
 * ******************************************************************************/

package org.jdesktop.swingx.jxmlnote.spikes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Locale;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.PageRanges;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import sun.font.Font2D;
import sun.font.FontManager;
import sun.java2d.SunGraphicsEnvironment;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;
import com.sun.pdfview.PagePanel;

public class PdfViewSpike implements Printable {
	
	private PDFFile pdffile;
	private JFrame frame;
	
	public int print (Graphics g, PageFormat format, int index) throws PrinterException {
		int pagenum = index+1;
		if (pagenum < 1 || pagenum > pdffile.getNumPages ())
			return NO_SUCH_PAGE;

		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = g2d.getTransform ();

		PDFPage pdfPage = pdffile.getPage (pagenum);

		Dimension dim;
		dim = pdfPage.getUnstretchedSize ((int) format.getImageableWidth (),
				(int) format.getImageableHeight (),
				pdfPage.getBBox ());

		Rectangle bounds = new Rectangle ((int) format.getImageableX (),
				(int) format.getImageableY (),
				dim.width,
				dim.height);

		PDFRenderer rend = new PDFRenderer (pdfPage, (Graphics2D) g, bounds,
				null, null);
		try
		{
			pdfPage.waitForFinish ();
			rend.run ();
		}
		catch (InterruptedException ie)
		{
			JOptionPane.showMessageDialog (frame, ie.getMessage ());
		}

		g2d.setTransform (at);
		g2d.draw (new Rectangle2D.Double (format.getImageableX (),
				format.getImageableY (),
				format.getImageableWidth (),
				format.getImageableHeight ()));

		return PAGE_EXISTS;
	}

	
	private int _page=0;

	public void setup() throws IOException {
 
		//set up the frame and panel
		frame = new JFrame("PDF Test");
		final PagePanel panel = new PagePanel();
		
		//load a pdf from a byte buffer
		File file = new File("/tmp/pdf_output.pdf");
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY,
				0, channel.size());
		pdffile = new PDFFile(buf);
		
		JPanel p=new JPanel();
		JToolBar bar=new JToolBar();
		Action prev=new AbstractAction("previous") {
			public void actionPerformed(ActionEvent e) {
				_page-=1;
				panel.showPage(pdffile.getPage(_page));
			}
		};
		Action next=new AbstractAction("next") {
			public void actionPerformed(ActionEvent e) {
				_page+=1;
				panel.showPage(pdffile.getPage(_page));
			}
		};
		Action print=new AbstractAction("print") {

			public void actionPerformed(ActionEvent e) {
				PrinterJob job = PrinterJob.getPrinterJob ();
	            job.setPrintable (PdfViewSpike.this);

	            try
	            {
	                HashPrintRequestAttributeSet attset;
	                attset = new HashPrintRequestAttributeSet ();
	                attset.add (new PageRanges (1, pdffile.getNumPages ()));
	                if (job.printDialog (attset))
	                    job.print (attset);
	            }
	            catch (PrinterException pe)
	            {
	                JOptionPane.showMessageDialog (frame, pe.getMessage ());
	            }
			}
		};
		bar.add(new JButton(prev));
		bar.add(new JButton(next));
		bar.add(new JButton(print));
		p.setLayout(new BorderLayout());
		p.add(bar,BorderLayout.NORTH);
		p.add(panel,BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(p);
		frame.pack();
		frame.setVisible(true);

		// show the first page
		PDFPage page = pdffile.getPage(_page);
		panel.showPage(page);

	}

	public static void main(final String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					//String []fonts=FontManager.getFontNamesFromPlatform();
					//System.out.println(fonts);
					//FontManager q=new FontManager();
					//Font f=new Font();
					//f.setFamily("Arial");
					//Set<File> paths=new HashSet<File>();
					//String name=FontManager.getFileNameForFontName(f.getFamilyname());
					//System.out.println(name);
					//PhysicalFont[] pfont=FontManager.getPhysicalFonts();
					//System.out.println(pfont);
					//FontManager.initialiseDeferredFonts();
					//String []fname=FontManager.getFontNamesFromPlatform();
					//System.out.println(fname);
					Font2D [] fonts=FontManager.getRegisteredFonts();
					System.out.println(fonts);
					Font f=new Font("Arial", Font.PLAIN, 12);
					System.out.println(f.getFontName());
					Font2D g=FontManager.getFont2D(f);
					System.out.println(g.getFontName(Locale.ENGLISH)+";"+g.getClass().getName());
					System.out.println(FontManager.getFileNameForFontName(f.getFontName()));
					sun.font.TrueTypeFont ttf=(sun.font.TrueTypeFont) g;
					System.out.println(ttf.getFullName());
					SunGraphicsEnvironment sge=(SunGraphicsEnvironment) GraphicsEnvironment.getLocalGraphicsEnvironment();
					System.out.println(sge.getDefaultFontFile());
					//System.out.println(sge.getFontConfiguration().getFileNameFromPlatformName("linux"));
					System.out.println(FontManager.getFontPath(true));
					
					for(Font2D ff : fonts) {
						
						System.out.println(ff.getFontName(Locale.ENGLISH)+";"+ff.getClass().getName());
					}
					//for (PhysicalFont pf : pfont) {

						//System.out.println(pf.)
					//}
					PdfViewSpike spk=new PdfViewSpike(); 
					spk.setup();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

}
