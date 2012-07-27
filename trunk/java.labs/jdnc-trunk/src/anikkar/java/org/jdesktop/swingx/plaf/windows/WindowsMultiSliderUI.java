package org.jdesktop.swingx.plaf.windows;

import org.jdesktop.swingx.plaf.basic.BasicMultiSliderUI;
import org.jdesktop.swingx.multislider.JXMultiSlider;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * @author Arash Nikkar
 */
public class WindowsMultiSliderUI extends BasicMultiSliderUI {
    private boolean innerRollover = false;
    private boolean outerRollover = false;
    private boolean innerPressed = false;
    private boolean outerPressed = false;

    public WindowsMultiSliderUI(JXMultiSlider b) {
		super(b);
    }

    public static BasicMultiSliderUI createUI(JComponent b) {
        return new WindowsMultiSliderUI((JXMultiSlider)b);
    }

	public void uninstallUI(JComponent c) {
		XPStyle.invalidateStyle();
	}

    /**
     * Overrides to return a private track listener subclass which handles
     * the HOT, PRESSED, and FOCUSED states.
     * @since 1.6
     */
    protected JXMultiTrackListener createTrackListener(JSlider slider) {
        return new WindowsTrackListener();
    }

    private class WindowsTrackListener extends JXMultiTrackListener {

        public void mouseMoved(MouseEvent e) {
            updateRolloverInner(thumbRect.contains(e.getX(), e.getY()));
            updateRolloverOuter(outerThumbRect.contains(e.getX(), e.getY()));
            super.mouseMoved(e);
        }

        public void mouseEntered(MouseEvent e) {
            updateRolloverInner(thumbRect.contains(e.getX(), e.getY()));
            updateRolloverOuter(outerThumbRect.contains(e.getX(), e.getY()));
            super.mouseEntered(e);
        }

        public void mouseExited(MouseEvent e) {
            updateRolloverInner(false);
            updateRolloverOuter(false);
            super.mouseExited(e);
        }

        public void mousePressed(MouseEvent e) {
            updatePressedInner(thumbRect.contains(e.getX(), e.getY()));
            updatePressedOuter(outerThumbRect.contains(e.getX(), e.getY()));
            super.mousePressed(e);
        }

        public void mouseReleased(MouseEvent e) {
            updatePressedInner(false);
            updatePressedOuter(false);
            super.mouseReleased(e);
        }

        public void updatePressedInner(boolean newPressed) {
            // You can't press a disabled slider
            if (!slider.isEnabled()) {
                return;
            }
            if (innerPressed != newPressed) {
                innerPressed = newPressed;
                slider.repaint(thumbRect);
            }
        }

	    public void updatePressedOuter(boolean newPressed) {
            // You can't press a disabled slider
            if (!slider.isEnabled()) {
                return;
            }
            if (outerPressed != newPressed) {
                outerPressed = newPressed;
                slider.repaint(outerThumbRect);
            }
        }

        public void updateRolloverInner(boolean newRollover) {
            // You can't have a rollover on a disabled slider
            if (!slider.isEnabled()) {
                return;
            }
            if (innerRollover != newRollover) {
                innerRollover = newRollover;
                slider.repaint(thumbRect);
            }
        }

	    public void updateRolloverOuter(boolean newRollover) {
            // You can't have a rollover on a disabled slider
            if (!slider.isEnabled()) {
                return;
            }
            if (outerRollover != newRollover) {
                outerRollover = newRollover;
                slider.repaint(outerThumbRect);
            }
        }
    }


    public void paintTrack(Graphics g)  {
		XPStyle xp = XPStyle.getXP();
		if (xp != null) {
			boolean vertical = (slider.getOrientation() == JSlider.VERTICAL);
			TMSchema.Part part = vertical ? TMSchema.Part.TKP_TRACKVERT : TMSchema.Part.TKP_TRACK;
			XPStyle.Skin skin = xp.getSkin(slider, part);

			if (vertical) {
			int x = (trackRect.width - skin.getWidth()) / 2;
			skin.paintSkin(g, trackRect.x + x, trackRect.y,
					   skin.getWidth(), trackRect.height, null);
			} else {
			int y = (trackRect.height - skin.getHeight()) / 2;
			skin.paintSkin(g, trackRect.x, trackRect.y + y,
					   trackRect.width, skin.getHeight(), null);
			}
		} else {
			super.paintTrack(g);
		}
    }


    protected void paintMinorTickForHorizSlider( Graphics g, Rectangle tickBounds, int x ) {
		XPStyle xp = XPStyle.getXP();
		if (xp != null) {
			g.setColor(xp.getColor(slider, TMSchema.Part.TKP_TICS, null, TMSchema.Prop.COLOR, Color.black));
		}
		super.paintMinorTickForHorizSlider(g, tickBounds, x);
    }

    protected void paintMajorTickForHorizSlider( Graphics g, Rectangle tickBounds, int x ) {
		XPStyle xp = XPStyle.getXP();
		if (xp != null) {
			g.setColor(xp.getColor(slider, TMSchema.Part.TKP_TICS, null, TMSchema.Prop.COLOR, Color.black));
		}
		super.paintMajorTickForHorizSlider(g, tickBounds, x);
    }

    protected void paintMinorTickForVertSlider( Graphics g, Rectangle tickBounds, int y ) {
		XPStyle xp = XPStyle.getXP();
		if (xp != null) {
			g.setColor(xp.getColor(slider, TMSchema.Part.TKP_TICSVERT, null, TMSchema.Prop.COLOR, Color.black));
		}
		super.paintMinorTickForVertSlider(g, tickBounds, y);
    }

    protected void paintMajorTickForVertSlider( Graphics g, Rectangle tickBounds, int y ) {
		XPStyle xp = XPStyle.getXP();
		if (xp != null) {
			g.setColor(xp.getColor(slider, TMSchema.Part.TKP_TICSVERT, null, TMSchema.Prop.COLOR, Color.black));
		}
		super.paintMajorTickForVertSlider(g, tickBounds, y);
    }


    public void paintThumb(Graphics g)  {
		XPStyle xp = XPStyle.getXP();
		if (xp != null) {
			TMSchema.Part part = getXPThumbPart();
			TMSchema.State state = TMSchema.State.NORMAL;

			if (slider.hasFocus()) {
				state = TMSchema.State.FOCUSED;
			}
			if (innerRollover) {
				state = TMSchema.State.HOT;
			}
			if (innerPressed) {
				state = TMSchema.State.PRESSED;
			}
			if(!slider.isEnabled()) {
				state = TMSchema.State.DISABLED;
			}

			xp.getSkin(slider, part).paintSkin(g, thumbRect.x, thumbRect.y, state);
		} else {
			super.paintThumb(g);
		}
    }

	public void paintOuterThumb(Graphics g)  {
		XPStyle xp = XPStyle.getXP();
		if (xp != null) {
			TMSchema.Part part = getXPThumbPart();
			TMSchema.State state = TMSchema.State.NORMAL;

			if (slider.hasFocus()) {
				state = TMSchema.State.FOCUSED;
			}
			if (outerRollover) {
				state = TMSchema.State.HOT;
			}
			if (outerPressed) {
				state = TMSchema.State.PRESSED;
			}
			if(!slider.isEnabled()) {
				state = TMSchema.State.DISABLED;
			}

			xp.getSkin(slider, part).paintSkin(g, outerThumbRect.x, outerThumbRect.y, state);
		} else {
			super.paintOuterThumb(g);
		}
    }

    protected Dimension getThumbSize() {
        XPStyle xp = XPStyle.getXP();
		if (xp != null && false) {
			Dimension size = new Dimension();
			XPStyle.Skin s = xp.getSkin(slider, getXPThumbPart());
			size.width = s.getWidth();
			size.height = s.getHeight();
			return size;
		} else {
			return super.getThumbSize();
		}
    }

    private TMSchema.Part getXPThumbPart() {
		TMSchema.Part part;
		boolean vertical = (slider.getOrientation() == JSlider.VERTICAL);
		boolean leftToRight = slider.getComponentOrientation().isLeftToRight();
		Boolean paintThumbArrowShape =
			(Boolean)slider.getClientProperty("Slider.paintThumbArrowShape");
		if ((!slider.getPaintTicks() && paintThumbArrowShape == null) ||
				paintThumbArrowShape == Boolean.FALSE) {
			part = vertical ? TMSchema.Part.TKP_THUMBVERT
									: TMSchema.Part.TKP_THUMB;
		} else {
			part = vertical ? (leftToRight ? TMSchema.Part.TKP_THUMBRIGHT : TMSchema.Part.TKP_THUMBLEFT)
									: TMSchema.Part.TKP_THUMBBOTTOM;
		}
		return part;
    }
}


