package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdesktop.swingx.FilmStrip.CenterStyle;
import org.jdesktop.swingx.FilmStrip.ScrollMode;
import org.jdesktop.swingx.FilmStripImageViewer.StripPlacement;
import org.jdesktop.swingx.filmstrip.DefaultFilmStripCellRenderer;
import org.jdesktop.swingx.filmstrip.DefaultProgressPainter;
import org.jdesktop.swingx.filmstrip.DiaCellRenderer;
import org.jdesktop.swingx.filmstrip.FileImageSource;
import org.jdesktop.swingx.filmstrip.FuskerSourceFactory;
import org.jdesktop.swingx.filmstrip.HTMLImageLinkSourceFactory;
import org.jdesktop.swingx.filmstrip.HTMLImageSourceFactory;
import org.jdesktop.swingx.filmstrip.HighlightingFilmStripCellRenderer;
import org.jdesktop.swingx.filmstrip.LineProgressPainter;
import org.jdesktop.swingx.filmstrip.RotatingImageProgressPainter;
import org.jdesktop.swingx.filmstrip.WiiProgressPainter;


public class DemoFilmStripImageViewer {

	public DemoFilmStripImageViewer() {
		final JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(new BorderLayout());
		
		final JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		//final FilmStrip fs = new FilmStrip();
		
		final FilmStripImageViewer fs = new FilmStripImageViewer();
		
		// install some cool demo defaults
		fs.setCellInsets(new Insets(10, 10, 20, 10));
		fs.setCellRenderer(new DiaCellRenderer());
		fs.setFixedCellWidth(180);
		fs.setFixedCellHeight(140);
		fs.setProgressPainter(new WiiProgressPainter());
		fs.setCenterStyle(CenterStyle.ALWAYS);
		
		JMenuBar tb = new JMenuBar();
		JMenu openItem = new JMenu("Open");
		openItem.add(new JMenuItem(new AbstractAction("Open directory") {
			public void actionPerformed(ActionEvent e) {
				if (chooser.showOpenDialog(f) == JFileChooser.APPROVE_OPTION) {
					File dir = chooser.getSelectedFile();
					fs.setImages(dirToImageSources(dir));
				}
			}
		} ));

		openItem.add(new JMenuItem(new AbstractAction("Open IMGs from url") {
			public void actionPerformed(ActionEvent e) {
				String url = JOptionPane.showInputDialog(f, "url:");
				if (url != null) {
					fs.setImages(new HTMLImageSourceFactory(url));
				}
			}
		} ));
		
		openItem.add(new JMenuItem(new AbstractAction("Open HREFs from url") {
			public void actionPerformed(ActionEvent e) {
				String url = JOptionPane.showInputDialog(f, "url:");
				if (url != null) {
					fs.setImages(new HTMLImageLinkSourceFactory(url));
				}
			}
		} ));
		
		openItem.add(new JMenuItem(new AbstractAction("Fusker URL") {
			public void actionPerformed(ActionEvent e) {
				String url = JOptionPane.showInputDialog(f, "<html>Please enter a Fusker url.<br>eg. <u><font color='blue'>http://example.com/{abc, de}/image[1-10].jpg</font></u><br>url:</html>");
				if (url != null) {
					fs.setImages(new FuskerSourceFactory(url));
				}
			}
		} ));
		
		
		JMenu previewMenu = new JMenu("Cell Dimensions");
		previewMenu.add(new JMenuItem(new AbstractAction("120 * 120"){
			public void actionPerformed(ActionEvent e) {
				fs.setFixedCellHeight(120);
				fs.setFixedCellWidth(120);
			}
		}));
		
		previewMenu.add(new JMenuItem(new AbstractAction("90 * 90"){
			public void actionPerformed(ActionEvent e) {
				fs.setFixedCellHeight(90);
				fs.setFixedCellWidth(90);
			}
		}));
		
		previewMenu.add(new JMenuItem(new AbstractAction("80 * 80"){
			public void actionPerformed(ActionEvent e) {
				fs.setFixedCellHeight(80);
				fs.setFixedCellWidth(80);
			}
		}));
		
		previewMenu.addSeparator();
		
		previewMenu.add(new JMenuItem(new AbstractAction("180 * 140"){
			public void actionPerformed(ActionEvent e) {
				fs.setFixedCellWidth(180);
				fs.setFixedCellHeight(140);
			}
		}));
		previewMenu.add(new JMenuItem(new AbstractAction("140 * 180"){
			public void actionPerformed(ActionEvent e) {
				fs.setFixedCellWidth(140);
				fs.setFixedCellHeight(180);
			}
		}));

		JMenu insetsMenu = new JMenu("Cell insets");
		insetsMenu.add(new JMenuItem(new AbstractAction("10, 10, 10, 10") {
			public void actionPerformed(ActionEvent e) {
				fs.setCellInsets(new Insets(10, 10, 10, 10));
			}
		}));
		insetsMenu.add(new JMenuItem(new AbstractAction("20, 20, 20, 20") {
			public void actionPerformed(ActionEvent e) {
				fs.setCellInsets(new Insets(20, 20, 20, 20));
			}
		}));
		insetsMenu.add(new JMenuItem(new AbstractAction("10, 10, 20, 10") {
			public void actionPerformed(ActionEvent e) {
				fs.setCellInsets(new Insets(10, 10, 20, 10));
			}
		}));
		
		JMenu feelMenu = new JMenu("Look & Feel");
		JMenu centerMenu = new JMenu("CenterStyle");
		centerMenu.add(new JMenuItem(new AbstractAction("ALWAYS"){
			public void actionPerformed(ActionEvent arg0) {
				fs.setCenterStyle(CenterStyle.ALWAYS);
			}
		}));
		centerMenu.add(new JMenuItem(new AbstractAction("ON_EXIT"){
			public void actionPerformed(ActionEvent arg0) {
				fs.setCenterStyle(CenterStyle.ON_EXIT);
			}
		}));
		centerMenu.add(new JMenuItem(new AbstractAction("NEVER"){
			public void actionPerformed(ActionEvent arg0) {
				fs.setCenterStyle(CenterStyle.NEVER);
			}
		}));
		feelMenu.add(centerMenu);
		
		JMenu scrollMenu = new JMenu("ScrollMode");
		scrollMenu.add(new JMenuItem(new AbstractAction("DEFAULT") {
			public void actionPerformed(ActionEvent arg0) {
				fs.setScrollMode(ScrollMode.DEFAULT);
			}
		} ));
		scrollMenu.add(new JMenuItem(new AbstractAction("SMOOTH") {
			public void actionPerformed(ActionEvent arg0) {
				fs.setScrollMode(ScrollMode.SMOOTH);
			}
		} ));
		feelMenu.add(scrollMenu);
		
		JMenu cellRendererMenu = new JMenu("CellRenderer");
		cellRendererMenu.add(new JMenuItem(new AbstractAction("Default") {
			public void actionPerformed(ActionEvent arg0) {
				fs.setCellRenderer(new DefaultFilmStripCellRenderer());
			}
		} ));
		cellRendererMenu.add(new JMenuItem(new AbstractAction("Highlighting") {
			public void actionPerformed(ActionEvent arg0) {
				fs.setCellRenderer(new HighlightingFilmStripCellRenderer());
			}
		} ));
		cellRendererMenu.add(new JMenuItem(new AbstractAction("Dia") {
			public void actionPerformed(ActionEvent arg0) {
				fs.setCellRenderer(new DiaCellRenderer());
			}
		} ));
		feelMenu.add(cellRendererMenu);
		
		JMenu progressMenu = new JMenu("ProgressRenderer");
		progressMenu.add(new JMenuItem(new AbstractAction("Default") {
			public void actionPerformed(ActionEvent arg0) {
				fs.setProgressPainter(new DefaultProgressPainter());
			}
		} ));
		progressMenu.add(new JMenuItem(new AbstractAction("Image rotator") {
			public void actionPerformed(ActionEvent arg0) {
				InputStream in = getClass().getClassLoader().getResourceAsStream("resources/busywheel.png");
				try {
					fs.setProgressPainter(new RotatingImageProgressPainter(ImageIO.read(in)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} ));
		progressMenu.add(new JMenuItem(new AbstractAction("Line") {
			public void actionPerformed(ActionEvent arg0) {
				fs.setProgressPainter(new LineProgressPainter());
			}
		} ));
		progressMenu.add(new JMenuItem(new AbstractAction("Wii") {
			public void actionPerformed(ActionEvent arg0) {
				fs.setProgressPainter(new WiiProgressPainter());
			}
		} ));
		progressMenu.add(new JMenuItem(new AbstractAction("null") {
			public void actionPerformed(ActionEvent arg0) {
				fs.setProgressPainter(null);
			}
		} ));
		feelMenu.add(progressMenu);
		
		/*
		JMenu backGroundMenu = new JMenu("Background");
		backGroundMenu.add(new JMenuItem(new AbstractAction("Gradient") {
			public void actionPerformed(ActionEvent arg0) {
				fs.setCellRenderer(new DiaCellRenderer());
			}
		} ));*/
		
		JMenu orientationMenu = new JMenu("Strip location");
		orientationMenu.add(new AbstractAction("NORTH") {
			public void actionPerformed(ActionEvent e) {
				fs.setPlacement(StripPlacement.NORTH);
			}
		} );
		orientationMenu.add(new AbstractAction("SOUTH") {
			public void actionPerformed(ActionEvent e) {
				fs.setPlacement(StripPlacement.SOUTH);
			}
		} );
		orientationMenu.add(new AbstractAction("EAST") {
			public void actionPerformed(ActionEvent e) {
				fs.setPlacement(StripPlacement.EAST);
			}
		} );
		orientationMenu.add(new AbstractAction("WEST") {
			public void actionPerformed(ActionEvent e) {
				fs.setPlacement(StripPlacement.WEST);
			}
		} );
		feelMenu.add(orientationMenu);
		
		JMenu otherMenu = new JMenu("Scaling");
		otherMenu.add(new AbstractAction("Async scaling") {
			public void actionPerformed(ActionEvent e) {
				fs.setScaleAsync(true);
			}
		} );
		otherMenu.add(new AbstractAction("Synchronious scaling") {
			public void actionPerformed(ActionEvent e) {
				fs.setScaleAsync(false);
			}
		} );
		otherMenu.add(new JSeparator());
		otherMenu.add(new AbstractAction("Do not upscale") {
			public void actionPerformed(ActionEvent e) {
				fs.setScaleUp(false);
			}
		} );
		otherMenu.add(new AbstractAction("Do upscale") {
			public void actionPerformed(ActionEvent e) {
				fs.setScaleUp(true);
			}
		} );
		
		feelMenu.add(otherMenu);
		tb.add(openItem);
		tb.add(previewMenu);
		tb.add(insetsMenu);
		tb.add(feelMenu);
		
		
		f.getContentPane().add(tb, BorderLayout.NORTH);
		f.getContentPane().add(fs, BorderLayout.CENTER);
		
		f.setSize(new Dimension(800, 600));
		f.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (false) setNimbusIfAvailable();
				new DemoFilmStripImageViewer();
			}
		});
	}
	
	private static void setNimbusIfAvailable() {
		try {
			UIManager.setLookAndFeel("sun.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} 
	}
	
	public static Collection<ImageSource> dirToImageSources(File dir) {
		File[] contents = dir.listFiles();
		List<ImageSource> result = new ArrayList<ImageSource>();
		for (File f : contents) {
			String name = f.getName().toUpperCase();
			boolean ok = 
					name.endsWith(".JPG") || 
					name.endsWith(".JPEG") ||
					name.endsWith(".JPE") ||
					name.endsWith(".GIF") ||
					name.endsWith(".PNG") ||
					name.endsWith(".BMP");
			if (ok) {
				result.add(new FileImageSource(f));
			}
		}
		return result;
	}
	
}
