package org.jdesktop.swingx.demo;/*
 * $Id: LandF.java 1959 2007-11-21 11:11:40Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager;
import javax.swing.LookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * More convenient way to set looks and feels. There are three ways to use this
 * class. An enum constant is defined for all standard looks and feels. If
 * you're not worried about exceptions, you can call the setLookAndFeel method
 * from the constant:
 * <pre>
 *   LandF.Ocean.quickSetLF();
 * </pre> or you can set call the static method:
 * <pre>
 *   LandF.setLF(LandF.Ocean);
 * </pre> These two methods are equivalent. Both of these discard any Exceptions
 * that get thrown. Exceptions are only likely if the specified LandF is
 * not supported on your platform. If you're not getting what you
 * expect, and you want to see the exception, you can do this:
 * <pre>
 *       try { LandF.Ocean.setLookAndFeel(); }
 *      catch (Exception err) { err.printStackTrace(); }
 * </pre>
 * Created using IntelliJ IDEA. Date: Oct 3, 2004 Time: 5:46:28 PM
 *
 * @author Miguel Mu\u00f1oz
 */

public enum LandF {
	Synth("javax.swing.plaf.synth.SynthLookAndFeel"),

	/**
	 * Platform's default Look and Feel
	 */
	Platform(UIManager.getSystemLookAndFeelClassName()),

	/**
	 * Metal Look and feel, Steel theme
	 */
	Metal() {
		protected LookAndFeel makeLF() {
			System.out.println("Making Metal"); // NON-NLS
			MetalLookAndFeel lf = new MetalLookAndFeel();
			lf.setCurrentTheme(new DefaultMetalTheme());
			return lf;
		}
	},

	/**
	 * Metal Look and Feel, Ocean theme
	 */
	Ocean() {protected LookAndFeel makeLF() {
		System.out.println("making Ocean"); // NON-NLS
		return new MetalLookAndFeel();
	}},

	/**
	 * GTK+ Look and Feel. (Apparently not supported under Windows.)
	 */
	GTK("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"), // NON-NLS

	WindowsClassic("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"),

	/**
	 * Motif Look and Feel
	 */
	Motif("com.sun.java.swing.plaf.motif.MotifLookAndFeel");

	private String mName;

	//  protected LookAndFeel mLF;
	private LandF(String pName) { mName = pName; }

	private LandF() { }

	public void setLookAndFeel()
					throws
					UnsupportedLookAndFeelException,
					ClassNotFoundException,
					InstantiationException,
					IllegalAccessException {
		if (mName != null)
			//noinspection StringConcatenation
			System.out.println("Theme = " + mName); //((MetalLookAndFeel)mLF).getCurrentTheme()); // NON-NLS
		if (mName == null)
			UIManager.setLookAndFeel(makeLF());
		else
			UIManager.setLookAndFeel(mName);
	}

	/**
	 * Attempts to set the specified look and feel, and throws away any
	 * exceptions generated. Consequently, you may end up with the default
	 * look and feel. This is because not all Looks-and-Feels are supported on
	 * all platforms. Use {@code setLookAndFeel()} to see the Exceptions.
	 */
	public void quickSetLF() {
		//noinspection CatchGenericClass
		try { setLookAndFeel(); }
		catch (Exception ex) { }
	}

	public void set() { quickSetLF(); }

	/**
	 * This only needs to be defined in instances that use the empty constructor.
	 *
	 * @return a LookAndFeel instance.
	 */
	protected LookAndFeel makeLF() { //noinspection StringConcatenation
		throw new AssertionError("makeName called from " + mName); }

	public static void setLF(LandF pLf) {
		pLf.quickSetLF();
	}

	static {
		UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
    for (UIManager.LookAndFeelInfo in : info)
	    //noinspection StringConcatenation,StringContatenationInLoop
	    System.out.println(in.getClassName() + "   " + in);
  }
}
