package org.jdesktop.swingx;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JMenuItem;

import junit.framework.TestCase;

public class JXMenuBarTest extends TestCase {

	private JXMenuBar menuBar;
	private JXMenu menu1;
	private JXMenuItem menuItem1;

	protected void setUp() throws Exception {
		super.setUp();
		menuBar = new JXMenuBar();
		menu1 = new JXMenu("Menu1");
		menuItem1 = new JXMenuItem("MenuItem1");
		menu1.add(menuItem1);
		menu1.add(new JXMenu("Menu4"));
		menuBar.add(menu1);
		menuBar.add(new JXMenu("Menu2"));
		menuBar.add(new JXMenu("Menu3"));
	}

	public void testSetAlpha() {
		menuBar.setAlpha(0.5f);
		assertEquals(menuBar.getAlpha(), 0.5f);
		for (int i = 0; i < menuBar.getComponentCount(); i++) {
			JXMenu currentMenu = ((JXMenu) menuBar.getComponent(i));
			assertEquals(currentMenu.getAlpha(), 0.5f);
			Color c = new Color(((float) currentMenu.getPopupMenu()
					.getBackground().getRed() / 255), ((float) currentMenu
					.getPopupMenu().getBackground().getGreen() / 255),
					((float) currentMenu.getPopupMenu().getBackground()
							.getBlue() / 255), 0.5f);
			assertEquals(currentMenu.getPopupMenu().getBackground(), c);
			for (int j = 0; j < currentMenu.getComponentCount(); j++) {
				JXMenuElement currentElement = ((JXMenuElement) currentMenu
						.getComponent(j));
				checkAlpha(currentElement, c);

			}
		}
	}

	private void checkAlpha(JXMenuElement currentElement, Color c) {
		if (currentElement instanceof JXMenuItem) {
			assertEquals(((JXMenuItem) currentElement).getAlpha(), 0.5f);
			assertEquals(((JXMouseInputListener) ((JXMenuItem) currentElement)
					.getPropertyChangeListeners("color")[0]).getColor(), c);
		}
		if (currentElement instanceof JXMenu) {
			assertEquals(((JXMenu) currentElement).getPopupMenu()
					.getBackground(), c);
			for (int j = 0; j < ((JXMenu) currentElement).getComponentCount(); j++) {
				checkAlpha(((JXMenuElement) currentElement), c);
			}
		}
	}

	public void testAddInvalidElement() {
		try {
			menu1.add(new JMenuItem());
			fail("An IllegalArgumentException muste be caught !");
		} catch (IllegalArgumentException e) {
		}

	}

	public void testAddJXMenuItem() {
		try {
			menu1.add(new JXMenuItem("menuItem1"));
		} catch (IllegalArgumentException e) {
			fail("An IllegalArgumentException muste be caught !");
		}

	}

	public void testAddJXMenu() {
		try {
			menu1.add(new JXMenu("menu1"));
		} catch (IllegalArgumentException e) {
			fail("Addition of a JXMenu failed");
		}

	}

	public void testAddJXCheckBoxMenuItem() {
		try {
			menu1.add(new JXCheckBoxMenuItem());
		} catch (IllegalArgumentException e) {
			fail("Addition of a JXCheckBoxMenuItem failed");
		}

	}

	public void testAddJXRadioButtonMenuItem() {
		try {
			menu1.add(new JXRadioButtonMenuItem());
		} catch (IllegalArgumentException e) {
			fail("Addition of a JXRadioButtonMenuItem failed");
		}

	}

	public void testMouseExited() throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		menuBar.setMenusForeground(Color.RED);
		// Simulate a mouse click using reflection.
		MouseEvent event = new MouseEvent(menuItem1, MouseEvent.MOUSE_EXITED,
				0, MouseEvent.BUTTON1_MASK, 10, 10, 1, false);
		Method process = Component.class.getDeclaredMethod("processEvent",
				AWTEvent.class);
		process.setAccessible(true);
		process.invoke(menuItem1, event);

		assertEquals(Color.RED, menuItem1.getForeground());

	}

	public void testSetMenusBackground() {
		menuBar.setMenusBackground(Color.GREEN);
		assertEquals(menuBar.getMenusBackground(), Color.GREEN);
		for (int i = 0; i < menuBar.getComponentCount(); i++) {
			JXMenu currentMenu = ((JXMenu) menuBar.getComponent(i));
			assertEquals(currentMenu.getPopupMenu().getBackground(),
					Color.GREEN);
			for (int j = 0; j < currentMenu.getComponentCount(); j++) {
				JXMenuElement currentElement = ((JXMenuElement) currentMenu
						.getComponent(j));
				if (currentElement instanceof JXMenu)
					checkMenusBackground(((JXMenu) currentElement));
			}
		}

	}

	private void checkMenusBackground(JXMenu menu) {

		assertEquals(menu.getPopupMenu().getBackground(), Color.GREEN);
		for (int j = 0; j < menu.getComponentCount(); j++) {
			checkMenusBackground(menu);
		}
	}

	public void testSetMenusForeground() {
		menuBar.setMenusForeground(Color.YELLOW);
		assertEquals(menuBar.getMenusForeground(), Color.YELLOW);
		for (int i = 0; i < menuBar.getComponentCount(); i++) {
			JXMenu currentMenu = ((JXMenu) menuBar.getComponent(i));

			for (int j = 0; j < currentMenu.getComponentCount(); j++) {
				JXMenuElement currentElement = ((JXMenuElement) currentMenu
						.getComponent(j));
				checkMenusForeground(currentElement);
			}
		}

	}

	private void checkMenusForeground(JXMenuElement currentElement) {

		assertEquals(((JXMenuItem) currentElement).getRegularForeground(),
				Color.YELLOW);
		if (currentElement instanceof JXMenu) {

			for (int j = 0; j < ((JXMenu) currentElement).getComponentCount(); j++) {
				checkMenusForeground(((JXMenuElement) currentElement));
			}
		}

	}

	public void testSetMenusSelectionForeground() {
		menuBar.setMenusSelectionForeground(Color.ORANGE);
		assertEquals(menuBar.getMenusSelectionForeground(), Color.ORANGE);
		for (int i = 0; i < menuBar.getComponentCount(); i++) {
			JXMenu currentMenu = ((JXMenu) menuBar.getComponent(i));

			for (int j = 0; j < currentMenu.getComponentCount(); j++) {
				JXMenuElement currentElement = ((JXMenuElement) currentMenu
						.getComponent(j));
				checkMenusSelectionForeground(currentElement);
			}
		}

	}

	private void checkMenusSelectionForeground(JXMenuElement currentElement) {

		assertEquals(((JXMenuItem) currentElement).getSelectionForeground(),
				Color.ORANGE);
		if (currentElement instanceof JXMenu) {

			for (int j = 0; j < ((JXMenu) currentElement).getComponentCount(); j++) {
				checkMenusSelectionForeground(((JXMenuElement) currentElement));
			}
		}

	}

}
