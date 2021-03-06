/*
 * $Id: JXHeaderVisualCheck.java 2952 2009-01-09 13:49:10Z kleopatra $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXHeader.IconPosition;
import org.jdesktop.swingx.test.XTestUtils;

import sun.swing.SwingUtilities2;

/**
 * Tests previously created to expose known issues of <code>JXHeader</code>, now fixed.
 * Still need visual inspection from time to time.
 * <p>
 *
 * @author Jeanette Winzenburg
 */
public class JXHeaderVisualCheck extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(JXHeaderVisualCheck.class.getName());

    public static void main(String args[]) {
        JXHeaderVisualCheck test = new JXHeaderVisualCheck();
        try {
//          test.runInteractiveTests();
          test.runInteractiveTests("interactive.*Label.*");
          test.runInteractiveTests("interactive.*Properties.*");
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }
    }

    @Override
    protected void setUp() throws Exception {
        setSystemLF(true);
    }

    // ------------------ interactive

    /**
     * Sanity: trying to track default rendering hints
     */
    public void interactiveLabel() {
        JLabel label = new JLabel("JLabel tweaked * tracking, tracking ...") {

            @Override
            protected void paintComponent(Graphics g) {
                if (ui != null) {
                    Graphics2D g2 = (Graphics2D) ((g == null) ? null : g.create());
                    try {
                        RenderingHints old = g2.getRenderingHints();
                      LOG.info(getText() + ": all hints " + old
                      + "\n     " + ": aliased " + g2.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING));
                      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                              RenderingHints.VALUE_ANTIALIAS_ON);
                      int x = 0;
                      int y = 0;
                      float xf = 0f;
                      float yf = 0f;
                      g2.drawString("", 0, 0);
                      g2.drawString("", xf, yf);
                      g2.drawString((AttributedCharacterIterator) null, xf, yf);
                      g2.drawString((AttributedCharacterIterator) null, x, y);
                        ui.update(g2, this);
                        SwingUtilities2.drawStringUnderlineCharAt(this, g2, getText(), -1, 0, 10);
                        RenderingHints after =g2.getRenderingHints();
                        LOG.warning(getText() + ": all hints " + after
                                + "\n     " + ": aliased " + g2.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING)
                                + "\n     " + "equals? " + old.equals(after));
                    }
                    finally {
                        g2.dispose();
                    }
                }
            }
            
        };
        JComponent box = Box.createVerticalBox();
        box.add(label);
        box.add(new JXLabel("JXLabel * tracking ... tracking .."));
        box.add(new JLabel("JLabel raw * tracking tracking ..."));
        showInFrame(box, "label");
    }


    public void interactiveHeaderInTabbedPane() {
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("first", createHeader());
        pane.addTab("second", createHeader());
        showInFrame(pane, "NPE: header in tabbedPane?");

    }

    private JXHeader createHeader() {
        JXHeader header = new JXHeader();
        header.setTitle("AlbumManager");
        String headerLong = "An adaption from JGoodies Binding Tutorial in the context"
                + " of BeansBinding/AppFramework. "
                + "The Tabs show different styles of typical interaction "
                + "setups (in-place editing vs. dialog-editing). ";
        header.setDescription(headerLong);
        header.setIcon(XTestUtils.loadDefaultIcon());
        return header;
    }

    /**
     * #647-swingx JXLabel looses html rendering on font change.
     */
	public void interactiveFontLayoutReset() {
		JFrame f = new JFrame("Header Test - html lost on font change");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JXHeader header = new JXHeader("My Title",
				"<html>My short description<br>some text<html>");
		f.add(header, BorderLayout.CENTER);

		JButton changeFont = new JButton("Font");
		f.add(changeFont, BorderLayout.SOUTH);
		changeFont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						header.setFont(new Font("Tahoma", Font.PLAIN, 18));
					}
				});
			}
		});

		f.setSize(400, 200);
		f.setVisible(true);
	}

    /**
     * Short description in header produces unexpected line wraps in
     * footer.
     *
     * Note: the frame is not packed to simulate the appframework
     * context.
     */
    public void interactiveHTMLTextWrapShort() {
        JXHeader header = new JXHeader();
        header.setTitle("AlbumManager");
        String headerShort = "An adaption from JGoodies Binding Tutorial in the context"
            + " of BeansBinding/AppFramework. ";
        header.setDescription(headerShort);
        header.setIcon(XTestUtils.loadDefaultIcon());
        JXHeader footer = new JXHeader();
        footer.setTitle("Notes:");
        String footerDescription = "<html>"
                + " <ul> "
                + " <li> code: in the jdnc-incubator, section kleopatra, package appframework."
                + " <li> technique: back the view by a shared presentation model "
                + " <li> technique: veto selection change until editing is completed "
                + " <li> issue: selection of tab should be vetoable "
                + " <li> issue: empty selection should disable editing pane "
                + " </ul> " + " </html>";
        footer.setDescription(footerDescription);

        JComponent panel = new JPanel(new BorderLayout());
        panel.add(header, BorderLayout.NORTH);
        panel.add(footer, BorderLayout.SOUTH);
        JXFrame frame = new JXFrame("html wrapping in SOUTh: short text in NORTH");
        frame.add(panel);
        frame.setSize(800, 400);
        frame.setVisible(true);
    }

    /**
     * Long description in header produces expected line-wrap in footer.
     *
     * Note: the frame is not packed to simulate the appframework
     * context.
     */
    public void interactiveHTMLTextWrapLong() {
        JXHeader header = createHeader();
        JXHeader footer = new JXHeader();
        footer.setTitle("Notes:");
        String footerDescription = "<html>"
                + " <ul> "
                + " <li> code: in the jdnc-incubator, section kleopatra, package appframework."
                + " <li> technique: back the view by a shared presentation model "
                + " <li> technique: veto selection change until editing is completed "
                + " <li> issue: selection of tab should be vetoable "
                + " <li> issue: empty selection should disable editing pane "
                + " </ul> " + " </html>";
        footer.setDescription(footerDescription);

        JComponent panel = new JPanel(new BorderLayout());
        panel.add(header, BorderLayout.NORTH);
//        panel.add(new JScrollPane(table));
        panel.add(footer, BorderLayout.SOUTH);
        JXFrame frame = new JXFrame("html wrapping in SOUTh: long text in NORTH");
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
    /**
     * Issue #403-swingx: JXHeader doesn't show custom values.
     * <p>
     *
     * All values are passed in the constructor.
     */
    public void interactiveCustomProperties() {
        URL url = getClass().getResource("resources/images/wellTop.gif");;

        assertNotNull(url);
        JPanel p = new JPanel(new BorderLayout());
        JXHeader header = new JXHeader("MyTitle", "MyDescription", new ImageIcon(url));
        header.setIconPosition(IconPosition.LEFT);
        JPanel px = new JPanel(new GridLayout(2,1));
        px.add(header);
        px.add(new JXHeader("MyTitle", "MyDescription", new ImageIcon(url)));
        p.add(px);
        // added just to better visualize bkg gradient in the JXHeader.
        p.add(new JLabel("Reference component: JLabel"), BorderLayout.SOUTH);
        p.add(new JXLabel("Reference component: JXLabel"), BorderLayout.NORTH);
        showInFrame(p, "JXHeader with custom properties");
    }

    /**
     * Issue #469-swingx: JXHeader doesn't wrap words in description.<p>
     * Issue #634-swingx: JXHeader uses incorrect font for description.<p>
     *
     * All values are passed in the constructor.
     */
    public void interactiveWordWrapping() {
        URL url = getClass().getResource("resources/images/wellTop.gif");
        assertNotNull(url);
        JPanel p = new JPanel(new BorderLayout());
        JXHeader header = new JXHeader("MyTitle", "this is a long test with veeeeeeeeeeeeeery looooooong wooooooooooooords", new ImageIcon(url));
        p.add(header);
        p.setPreferredSize(new Dimension(200,150));
        showInFrame(p, "word wrapping JXHeader");
    }

    /**
     * Test custom title font and color use.
     *
     * All values are passed in the constructor.
     */
    public void interactiveCustomTitle() {
        URL url = getClass().getResource("resources/images/wellTop.gif");
        assertNotNull(url);
        JPanel p = new JPanel(new BorderLayout());
        JXHeader header = new JXHeader("MyBigUglyTitle", "this is a long test with veeeeeeeeeeeeeery looooooong wooooooooooooords", new ImageIcon(url));
        header.setTitleFont(new Font("serif", Font.BOLD, 36));
        header.setTitleForeground(Color.GREEN);
        p.add(header);
        p.setPreferredSize(new Dimension(400,150));
        showInFrame(p, "Titlefont lost on updateUI / word wrapping JXHeader");
    }

    /**
     * Empty test method to keep the testrunner happy.
     */
    public void testDummy() {

    }
}
