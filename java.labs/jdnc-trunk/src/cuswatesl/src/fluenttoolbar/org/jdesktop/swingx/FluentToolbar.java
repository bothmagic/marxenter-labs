package org.jdesktop.swingx;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.sun.java.swing.SwingUtilities2;

public class FluentToolbar extends JComponent {

	static {
		UIManager.put("TabbedPane.tabInsets", new Insets(0, 20, 0, 20));
	}
	
	javax.swing.JTabbedPane tp = new JTabbedPane(); 

	int height = 170;

	public FluentToolbar() {
		this.setLayout(new BorderLayout());
		tp = new JTabbedPane();
		tp.setFont(tp.getFont().deriveFont(25f));
		tp.setUI(new BasicTabbedPaneUI() {

		    @Override
			protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
		        int tabCount = tabPane.getTabCount();

		        Rectangle iconRect = new Rectangle(),
		                  textRect = new Rectangle();
		        Rectangle clipRect = g.getClipBounds();  

		        // Paint tabRuns of tabs from back to front
		        for (int i = runCount - 1; i >= 0; i--) {
		            int start = tabRuns[i];
		            int next = tabRuns[(i == runCount - 1)? 0 : i + 1];
		            int end = (next != 0? next - 1: tabCount - 1);
		            for (int j = start; j <= end; j++) {
		                if (j != selectedIndex && rects[j].intersects(clipRect)) {
		                    paintTab(g, tabPlacement, rects, j, iconRect, textRect);
		                }
		            }
		        }

		        // Paint selected tab if its in the front run
		        // since it may overlap other tabs
		        if (selectedIndex >= 0 && rects[selectedIndex].intersects(clipRect)) {
		            paintTab(g, tabPlacement, rects, selectedIndex, iconRect, textRect);
		        }
		    }
			
		    
			@Override
			protected void paintTab(Graphics gr, int tabPlacement,
					Rectangle[] rects, int tabIndex, 
					Rectangle iconRect, Rectangle textRect) {
				
				Rectangle tabRect = rects[tabIndex];
				int selectedIndex = tabPane.getSelectedIndex();
				boolean isSelected = selectedIndex == tabIndex;
				
				Graphics2D g = (Graphics2D) gr.create();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				
				Shape clip = g.getClip();
				RoundRectangle2D r2d = new RoundRectangle2D.Float(tabRect.x + 2, tabRect.y + 2,
						tabRect.width - 4, tabRect.height * 2 - 2, 20, 50);
				
				g.setClip(r2d);
				Color selectedColor = UIManager.getColor("TabbedPane.selected");

				g.setColor(selectedColor);
				if (isSelected) {
					g.fillRect(tabRect.x , tabRect.y,
							tabRect.width, tabRect.height);
				}
				
				RoundRectangle2D r2ds = new RoundRectangle2D.Float(tabRect.x + 3 , tabRect.y + 3,
						tabRect.width - 6, (tabRect.height * 2) - 8, 20, 50);
				g.setClip(r2ds);
				
				
				
				paintTabBackground(g, tabPlacement, tabIndex, tabRect.x, tabRect.y,
						tabRect.width, tabRect.height, isSelected);

				/*
				paintTabBorder(g, tabPlacement, tabIndex, tabRect.x, tabRect.y, 
						tabRect.width, tabRect.height, isSelected);
				*/
				
				
				String title = tabPane.getTitleAt(tabIndex);
				Font font = tabPane.getFont();
				FontMetrics metrics = SwingUtilities2.getFontMetrics(tabPane, g, font);
				Icon icon = getIconForTab(tabIndex);

				layoutLabel(tabPlacement, metrics, tabIndex, title, icon, 
						tabRect, iconRect, textRect, isSelected);

				paintText(g, tabPlacement, font, metrics, 
						tabIndex, title, textRect, isSelected);

				paintIcon(g, tabPlacement, tabIndex, icon, iconRect, isSelected);

				/*
				paintFocusIndicator(g, tabPlacement, rects, tabIndex, 
						iconRect, textRect, isSelected);
*/
				g.setClip(clip);
			}

			@Override
			protected void paintText(Graphics g, int tabPlacement, Font font,
					FontMetrics metrics, int tabIndex, String title,
					Rectangle textRect, boolean isSelected) {
				Graphics2D g2 = (Graphics2D) g.create();
				if (!isSelected) {
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
				}
				super.paintText(g2, tabPlacement, font, metrics, tabIndex, title, textRect,
						isSelected);
			}

			protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
				int width = tabPane.getWidth();
				int height = tabPane.getHeight();
				Insets insets = tabPane.getInsets();
				Insets tabAreaInsets = getTabAreaInsets(tabPlacement);

				Color selectedColor = UIManager.getColor("TabbedPane.selected");
				boolean tabsOverlapBorder = UIManager.getBoolean("TabbedPane.tabsOverlapBorder");
				boolean contentOpaque = true;

				int x = insets.left;
				int y = insets.top;
				int w = width - insets.right - insets.left;
				int h = height - insets.top - insets.bottom;

				switch(tabPlacement) {
				case LEFT:
					x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
					if (tabsOverlapBorder) {
						x -= tabAreaInsets.right;
					}
					w -= (x - insets.left);
					break;
				case RIGHT:
					w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
					if (tabsOverlapBorder) {
						w += tabAreaInsets.left;
					}
					break;            
				case BOTTOM: 
					h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
					if (tabsOverlapBorder) {
						h += tabAreaInsets.top;
					}
					break;
				case TOP:
				default:
					y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
				if (tabsOverlapBorder) {
					y -= tabAreaInsets.bottom;
				}
				h -= (y - insets.top);
				} 

				if (contentOpaque || tabPane.isOpaque()) {
					// Fill region behind content area
					Color color = UIManager.getColor("TabbedPane.contentAreaColor");
					if (color != null) {
						g.setColor(color);
					}
					else if (selectedColor == null) {
						g.setColor(tabPane.getBackground());
					}
					else {
						g.setColor(selectedColor);
					}
					Graphics2D g2 = (Graphics2D) g.create();
					Color c1 = selectedColor;
					Color c2 = Color.WHITE;
					GradientPaint gp = new GradientPaint(0, maxTabHeight, c1, 0, getHeight(), c2);
					g2.setPaint(gp);
					g2.fillRect(x, y, w, h);

				}

				paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
				paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h); 
				paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
				paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h); 

			}
			@Override
			protected void paintTabBackground(Graphics g, int tabPlacement,
					int tabIndex,
					int x, int y, int w, int h, 
					boolean isSelected ) {
				Color selectedColor = UIManager.getColor("TabbedPane.selected");
				Graphics2D g2 = (Graphics2D) g.create();
				Color c1 = selectedColor;
				Color c2 = Color.WHITE;
				GradientPaint gp = new GradientPaint(x, y, c2, x, h, c1);
				g2.setPaint(gp);
				
				if (isSelected) {
					switch(tabPlacement) {
					case LEFT:
						g2.fillRect(x+1, y+1, w-1, h-3);
						break;
					case RIGHT:
						g2.fillRect(x, y+1, w-2, h-3);
						break;
					case BOTTOM:
						g2.fillRect(x+1, y, w-3, h-1);
						break;
					case TOP:
					default:
						g2.fillRect(x+1, y+1, w-3, h-1);
					}
				}
			}
	     
			
		} );
		this.add(tp, BorderLayout.CENTER);
	}

	public void addActionTab(ActionTab tab, String name) {
		tp.addTab(name, tab);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.height = this.height;
		return d;
	}

	@Override
	public Dimension getMaximumSize() {
		Dimension d = super.getMaximumSize();
		d.height = this.height;
		return d;
	}

	@Override
	public Dimension getMinimumSize() {
		Dimension d = super.getMinimumSize();
		d.height = this.height;
		return d;
	}

}
