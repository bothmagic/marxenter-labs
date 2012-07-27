package swinguicomponents;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class SoftClippedWindow extends JFrame {
	public SoftClippedWindow() {
		super("Test soft-clipped window");

		JButton open = new JButton("Open window");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						final JWindow window = new JWindow();
						window.setLayout(new BorderLayout());

						try {
							// picture from
							// http://flickr.com/photos/petursey/366204314/
							final BufferedImage avatar = ImageIO
									.read(SoftClippedWindow.class
											.getResource("leaf.jpg"));

							JPanel mainPanel = new JPanel(new BorderLayout()) {
								@Override
								protected void paintComponent(Graphics g) {
									Graphics2D g2d = (Graphics2D) g.create();

									// code from
									// http://weblogs.java.net/blog/campbell/archive/2006/07/java_2d_tricker.html
									int width = avatar.getWidth();
									int height = avatar.getHeight();
									GraphicsConfiguration gc = g2d
											.getDeviceConfiguration();
									BufferedImage img = gc
											.createCompatibleImage(width,
													height,
													Transparency.TRANSLUCENT);
									Graphics2D g2 = img.createGraphics();

									g2.setComposite(AlphaComposite.Clear);
									g2.fillRect(0, 0, width, height);

									g2.setComposite(AlphaComposite.Src);
									g2.setRenderingHint(
											RenderingHints.KEY_ANTIALIASING,
											RenderingHints.VALUE_ANTIALIAS_ON);
									g2.setColor(Color.WHITE);
									g2.fillRoundRect(0, 0, width, height + 10,
											10, 10);

									g2.setComposite(AlphaComposite.SrcAtop);
									g2.drawImage(avatar, 0, 0, null);
									g2.dispose();

									// at this point the 'img' contains a soft
									// clipped round rectangle with the avatar

									// do the reflection with the code from
									// http://www.jroller.com/gfx/entry/swing_glint
									int avatarWidth = img.getWidth();
									int avatarHeight = img.getHeight();

									BufferedImage reflection = new BufferedImage(
											avatarWidth, avatarHeight,
											BufferedImage.TYPE_INT_ARGB);
									Graphics2D reflectionGraphics = reflection
											.createGraphics();

									AffineTransform tranform = AffineTransform
											.getScaleInstance(1.0, -1.0);
									tranform.translate(0, -avatarHeight);
									reflectionGraphics.drawImage(img, tranform,
											this);

									GradientPaint painter = new GradientPaint(
											0.0f, 0.0f, new Color(0.0f, 0.0f,
													0.0f, 0.7f), 0.0f,
											avatarHeight / 2.0f, new Color(
													0.0f, 0.0f, 0.0f, 1.0f));

									reflectionGraphics
											.setComposite(AlphaComposite.DstOut);
									reflectionGraphics.setPaint(painter);
									reflectionGraphics
											.fill(new Rectangle2D.Double(0, 0,
													avatarWidth, avatarHeight));

									reflectionGraphics.dispose();

									g2d.drawImage(img, 0, 0, this);
									g2d.drawImage(reflection, 0, avatarHeight,
											this);

									g2d.dispose();
								}
							};
							JButton close = new JButton("close");
							close.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									window.dispose();
								}
							});
							JPanel buttonPanel = new JPanel(new FlowLayout(
									FlowLayout.RIGHT));
							buttonPanel.add(close);
							buttonPanel.setOpaque(false);
							buttonPanel.setDoubleBuffered(false);
							mainPanel.add(buttonPanel, BorderLayout.NORTH);

							mainPanel.setDoubleBuffered(false);
							mainPanel.setOpaque(false);
							window.add(mainPanel, BorderLayout.CENTER);

							window.setSize(180, 200);
							window.setLocationRelativeTo(null);
							window.setVisible(true);
							com.sun.awt.AWTUtilities.setWindowOpaque(window,
									false);
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
					}
				});
			}
		});
		this.setLayout(new FlowLayout());
		this.add(open);

		this.setSize(new Dimension(400, 300));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Window w = new SoftClippedWindow();
				w.setVisible(true);
			}
		});
	}

}