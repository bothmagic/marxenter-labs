/*
 * Copyright (c) 2005-2010 Flamingo Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of Flamingo Kirill Grouchnikov nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.pushingpixels.flamingo.internal.ui.ribbon.appmenu;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.*;

import javax.swing.plaf.basic.BasicGraphicsUtils;
import org.pushingpixels.flamingo.api.common.CommandButtonLayoutManager;
import org.pushingpixels.flamingo.api.common.CommandButtonLayoutManager.CommandButtonSeparatorOrientation;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.*;
import org.pushingpixels.flamingo.internal.ui.common.BasicCommandButtonUI;
import org.pushingpixels.flamingo.internal.utils.FlamingoUtilities;

/**
 * Basic UI for ribbon application menu button
 * {@link JRibbonApplicationMenuButton}.
 * 
 * @author Kirill Grouchnikov
 */
public class BasicRibbonLiteApplicationMenuButtonUI extends BasicCommandButtonUI {
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new BasicRibbonLiteApplicationMenuButtonUI();
	}

	/**
	 * The associated application menu button.
	 */
	protected JRibbonLiteApplicationMenuButton applicationMenuButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicButtonUI#installUI(javax.swing.JComponent)
	 */
	@Override
	public void installUI(JComponent c) {
		this.applicationMenuButton = (JRibbonLiteApplicationMenuButton) c;
		super.installUI(c);
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		Border border = this.commandButton.getBorder();
		if (border == null || border instanceof UIResource) {
			Border toInstall = UIManager
					.getBorder("RibbonApplicationMenuButton.border");
			if (toInstall == null)
				toInstall = new BorderUIResource.EmptyBorderUIResource(4, 4, 4,
						4);
			this.commandButton.setBorder(toInstall);
		}
                
		this.commandButton.setOpaque(false);
	}

	@Override
	protected void configureRenderer() {
		this.buttonRendererPane = new CellRendererPane();
		this.commandButton.add(buttonRendererPane);
		this.rendererButton = new JButton("");
	}

	@Override
	protected void unconfigureRenderer() {
		this.commandButton.remove(this.buttonRendererPane);
		this.buttonRendererPane = null;
		this.rendererButton = null;
	}

	@Override
	protected void installComponents() {
		super.installComponents();

		final JRibbonLiteApplicationMenuButton appMenuButton = (JRibbonLiteApplicationMenuButton) this.commandButton;
		appMenuButton.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(final JCommandButton commandButton) {
                            
				JFrame ribbonFrame = (JFrame) SwingUtilities
						.getWindowAncestor(commandButton);
                                final JRibbon ribbon;
                                if (ribbonFrame instanceof JRibbonFrame) {
                                    ribbon = ((JRibbonFrame)ribbonFrame).getRibbon();
                                } else {
                                    ribbon = (JRibbon) appMenuButton.getParent();
                                }
				
				RibbonApplicationMenu ribbonMenu = ribbon.getApplicationMenu();
				final JRibbonApplicationMenuPopupPanel menuPopupPanel = new JRibbonApplicationMenuPopupPanel(
						appMenuButton, ribbonMenu);
				menuPopupPanel.applyComponentOrientation(appMenuButton
						.getComponentOrientation());
				menuPopupPanel
						.setCustomizer(new JPopupPanel.PopupPanelCustomizer() {
							@Override
							public Rectangle getScreenBounds() {
								boolean ltr = commandButton
										.getComponentOrientation()
										.isLeftToRight();

								int pw = menuPopupPanel.getPreferredSize().width;
								int x = ltr ? ribbon.getLocationOnScreen().x
										: ribbon.getLocationOnScreen().x
												+ ribbon.getWidth() - pw;
								int y = commandButton.getLocationOnScreen().y
										+ commandButton.getSize().height-2;

								// make sure that the menu popup stays
								// in bounds
								Rectangle scrBounds = commandButton
										.getGraphicsConfiguration().getBounds();
								if ((x + pw) > (scrBounds.x + scrBounds.width)) {
									x = scrBounds.x + scrBounds.width - pw;
								}
								int ph = menuPopupPanel.getPreferredSize().height;
								if ((y + ph) > (scrBounds.y + scrBounds.height)) {
									y = scrBounds.y + scrBounds.height - ph;
								}

								return new Rectangle(
										x,
										y,
										menuPopupPanel.getPreferredSize().width,
										menuPopupPanel.getPreferredSize().height);
							}
						});
				return menuPopupPanel;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.common.ui.BasicCommandButtonUI#paint(java.awt.Graphics
	 * , javax.swing.JComponent)
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
		g.setFont(FlamingoUtilities.getFont(commandButton, "Ribbon.font",
				"Button.font", "Panel.font"));
		this.layoutInfo = this.layoutManager.getLayoutInfo(this.commandButton,
				g);
		commandButton.putClientProperty("icon.bounds", layoutInfo.iconRect);

		//if (this.isPaintingBackground()) {
			this.paintButtonBackground(g, new Rectangle(0, 0, commandButton
					.getWidth(), commandButton.getHeight()));
		//}
		// Graphics2D g2d = (Graphics2D) g.create();
		// g2d.setColor(new Color(255, 0, 0, 64));
		// if (getActionClickArea() != null) {
		// g2d.fill(getActionClickArea());
		// }
		// g2d.setColor(new Color(0, 0, 255, 64));
		// if (getPopupClickArea() != null) {
		// g2d.fill(getPopupClickArea());
		// }
		// g2d.dispose();

		if (layoutInfo.iconRect != null) {
			this.paintButtonIcon(g, layoutInfo.iconRect);
		}
		if (layoutInfo.popupActionRect.getWidth() > 0) {
			paintPopupActionIcon(g, layoutInfo.popupActionRect);
		}
		FontMetrics fm = g.getFontMetrics();

		boolean isTextPaintedEnabled = commandButton.isEnabled();
		if (commandButton instanceof JCommandButton) {
			JCommandButton jCommandButton = (JCommandButton) commandButton;
			isTextPaintedEnabled = layoutInfo.isTextInActionArea ? jCommandButton
					.getActionModel().isEnabled()
					: jCommandButton.getPopupModel().isEnabled();
		}

		g.setColor(getForegroundColor(isTextPaintedEnabled));

		if (layoutInfo.textLayoutInfoList != null) {
			for (CommandButtonLayoutManager.TextLayoutInfo mainTextLayoutInfo : layoutInfo.textLayoutInfoList) {
				if (mainTextLayoutInfo.text != null) {
					BasicGraphicsUtils.drawString(g, mainTextLayoutInfo.text,
							-1, mainTextLayoutInfo.textRect.x,
							mainTextLayoutInfo.textRect.y + fm.getAscent());
				}
			}
		}

		if (isTextPaintedEnabled) {
			g.setColor(FlamingoUtilities.getColor(Color.gray,
					"Label.disabledForeground"));
		} else {
			g.setColor(FlamingoUtilities.getColor(Color.gray,
					"Label.disabledForeground").brighter());
		}

		if (layoutInfo.extraTextLayoutInfoList != null) {
			for (CommandButtonLayoutManager.TextLayoutInfo extraTextLayoutInfo : layoutInfo.extraTextLayoutInfoList) {
				if (extraTextLayoutInfo.text != null) {
					BasicGraphicsUtils.drawString(g, extraTextLayoutInfo.text,
							-1, extraTextLayoutInfo.textRect.x,
							extraTextLayoutInfo.textRect.y + fm.getAscent());
				}
			}
		}

		if (this.isPaintingSeparators() && (layoutInfo.separatorArea != null)) {
			if (layoutInfo.separatorOrientation == CommandButtonSeparatorOrientation.HORIZONTAL) {
				this
						.paintButtonHorizontalSeparator(g,
								layoutInfo.separatorArea);
			} else {
				this.paintButtonVerticalSeparator(g, layoutInfo.separatorArea);
			}
		}

		// Graphics2D g2d = (Graphics2D) g.create();
		//
		// g2d.setColor(Color.red);
		// g2d.draw(layoutInfo.iconRect);
		// g2d.setColor(Color.blue);
		// if (layoutInfo.textLayoutInfoList != null) {
		// for (CommandButtonLayoutManager.TextLayoutInfo mainTextLayoutInfo :
		// layoutInfo.textLayoutInfoList) {
		// if (mainTextLayoutInfo.text != null) {
		// g2d.draw(mainTextLayoutInfo.textRect);
		// }
		// }
		// }
		// g2d.setColor(Color.magenta);
		// if (layoutInfo.extraTextLayoutInfoList != null) {
		// for (CommandButtonLayoutManager.TextLayoutInfo extraTextLayoutInfo :
		// layoutInfo.extraTextLayoutInfoList) {
		// if (extraTextLayoutInfo.text != null) {
		// g2d.draw(extraTextLayoutInfo.textRect);
		// }
		// }
		// }
		// g2d.setColor(Color.green);
		// g2d.draw(layoutInfo.popupActionRect);
		// g2d.dispose();
	}

	
}
