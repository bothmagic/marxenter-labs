package org.jdesktop.swingx.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXSearchField.LayoutStyle;
import org.jdesktop.swingx.JXSearchField.SearchMode;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;

public class ScreenShotter {
	private JXSearchField c;

	private String name = "searchfield";

	private String lnf = "";

	private String layout = "";

	private String directory = "";

	public ScreenShotter(JXSearchField sf) throws Exception {
		c = sf;

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		c.updateUI();
		lnf = "system";
		lnfSession();
		vistaSession();

		UIManager.setLookAndFeel(new MetalLookAndFeel());
		c.updateUI();
		lnf = "metal";
		lnfSession();

		UIManager.setLookAndFeel(new MotifLookAndFeel());
		c.updateUI();
		lnf = "motif";
		lnfSession();

		UIManager.setLookAndFeel(new PlasticLookAndFeel());
		c.updateUI();
		lnf = "plastic";
		lnfSession();
		try {
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
			c.updateUI();
			lnf = "plasticxp";
			lnfSession();
			vistaSession();
		} catch (UnsupportedLookAndFeelException e) {
		}
	}

	private void vistaSession() {
		c.setText("rollover");
		c.getCancelButton().setIcon(c.getCancelButton().getRolloverIcon());
		shoot("clear_rollover");
		c.updateUI();
		c.getPopupButton().setIcon(c.getPopupButton().getRolloverIcon());
		shoot("popup_rollover");
		c.updateUI();
		c.setText("pressed");
		c.getCancelButton().setIcon(c.getCancelButton().getPressedIcon());
		shoot("clear_pressed");
		c.updateUI();
		c.getPopupButton().setIcon(c.getPopupButton().getPressedIcon());
		shoot("popup_pressed");
		c.updateUI();

		c.setText("rollover");
		c.setSearchMode(SearchMode.REGULAR);
		c.getFindButton().setIcon(c.getFindButton().getRolloverIcon());
		shoot("search_rollover");
		c.updateUI();
		c.setText("pressed");
		c.setSearchMode(SearchMode.REGULAR);
		c.getFindButton().setIcon(c.getFindButton().getPressedIcon());
		shoot("search_pressed");

		c.setSearchMode(SearchMode.INSTANT);
	}

	private void lnfSession() {
		c.setLayoutStyle(LayoutStyle.MAC);
		layout = "mac";
		layoutSession();

		c.setLayoutStyle(LayoutStyle.VISTA);
		layout = "vista";
		layoutSession();
	}

	private void layoutSession() {
		c.setText(null);
		c.setFindPopupMenu(null);
		shoot();
		c.setText("searching");
		shoot("searching");
		c.setText(null);
		c.setFindPopupMenu(new JPopupMenu());
		shoot("popup");
	}

	private void shoot() {
		shoot("");
	}

	private void shoot(String postfix) {
		c.setSize(c.getPreferredSize());
		c.doLayout();

		BufferedImage bi = new BufferedImage(c.getSize().width, c.getSize().height, BufferedImage.TYPE_INT_RGB);
		c.paint(bi.getGraphics());

		try {
			File file = new File(directory + name + "_" + lnf + "_" + layout + "_" + postfix + ".png");
			ImageIO.write(bi, "png", file);
			System.out.println("saved file: " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		JXSearchField sf = new JXSearchField("Search");
		sf.setColumns(10);

		try {
			new ScreenShotter(sf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
