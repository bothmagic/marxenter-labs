package yu.co.snpe.dbtable.core;

import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.Timer;

import org.springframework.richclient.image.ArrowIcon;

/**
 * JMenu with the scrolling feature.
 */
public class ScrollableMenu extends JMenu {
	private static final long serialVersionUID = 4892488347377826602L;

	/** How fast the scrolling will happen. */
	private int scrollSpeed = 150;

	public static final Icon STANDARD_UP_ARROW = new ArrowIcon(
			ArrowIcon.Direction.UP, 4, SystemColor.controlDkShadow);

	public static final Icon STANDARD_DOWN_ARROW = new ArrowIcon(
			ArrowIcon.Direction.DOWN, 4, SystemColor.controlDkShadow);
	
	/** Handles the scrolling upwards. */
	private Timer timerUp;

	/** Handles the scrolling downwards. */
	private Timer timerDown;

	/** How many items are visible. */
	private int visibleItems;

	/**
	 * Menuitem's index which is used to control if up and downbutton are
	 * visible or not.
	 */
	private int indexVisible;

	/** Button to scroll menu upwards. */
	private JButton upButton;

	/** Button to scroll menu downwards. */
	private JButton downButton;

	/** Container to hold submenus. */
	private Vector<JMenuItem> subMenus = new Vector<JMenuItem>();

	/** Height of the screen. */
	private double screenHeight;

	/** Height of the menu. */
	private double menuHeight;

	/**
	 * Creates a new ScrollableMenu object with a given name. This also
	 * instantiates the timers and buttons. After the buttons * are created they
	 * are set invisible.
	 * 
	 * @param name
	 *            name to be displayed on the JMenu
	 * 
	 */
	public ScrollableMenu() {
		super();
		init();
	}

	private void init() {
		timerUp = new Timer(scrollSpeed, new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				scrollUp();
			}
		});
		timerDown = new Timer(scrollSpeed, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				scrollDown();
			}
		});
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenHeight = screenSize.getHeight() - 30;
		// room for toolbar 
		createButtons( );
		hideButtons( );
		subMenus.clear();
		indexVisible = 0;
		visibleItems = 0;
		menuHeight = 0;
	}

	/**
	 * JMenu's add-method is override to keep track of the added items. If there
	 * are more items that JMenu can display, then the added menuitems will be
	 * invisible. After that downscrolling button will be visible.
	 * 
	 * @param menuItem
	 *            to be added
	 * @return added menuitem
	 */

	public JMenuItem add(JMenuItem menuItem) {
		add(menuItem, subMenus.size() + 1);
		subMenus.add(menuItem);
		menuHeight += menuItem.getPreferredSize().getHeight();
		if (menuHeight > screenHeight) {
			menuItem.setVisible(false);
			downButton.setVisible(true);
		} else {
			visibleItems++;
		}
		return menuItem;
	}

	@Override
	public void removeAll() {
		super.removeAll();
		init();
	}
	/**
	 * Closes the opened submenus when scrolling starts
	 */

	private void closeOpenedSubMenus() {
		MenuSelectionManager manager = MenuSelectionManager.defaultManager();
		MenuElement[] path = manager.getSelectedPath();
		int i = 0;
		JPopupMenu popup = getPopupMenu();
		for (; i < path.length; i++) {
			if (path[i] == popup) {
				break;
			}
		}
		MenuElement[] subPath = new MenuElement[i + 1];
		try {
			System.arraycopy(path, 0, subPath, 0, i + 1);
			manager.setSelectedPath(subPath);
		} catch (Exception ekasd) {
		}
	}

	/**
	 * When timerUp is started it calls constantly this method to make the JMenu
	 * scroll upwards. When the top of menu is reached then upButton is set
	 * invisible. When scrollUp starts downButton is setVisible.
	 */

	private void scrollUp() {
		closeOpenedSubMenus();
		if (indexVisible == 0) {
			upButton.setVisible(false);
			return;
		} else {
			indexVisible--;
			((JComponent) subMenus.get(indexVisible + visibleItems))
					.setVisible(false);
			((JComponent) subMenus.get(indexVisible)).setVisible(true);
			downButton.setVisible(true);
			if (indexVisible == 0) {
				upButton.setVisible(false);
			}
		}
	}

	/**
	 * When timerDown is started it calls constantly this method to make the
	 * JMenu scroll downwards. When the bottom of menu is reached then
	 * downButton is set invisible. When scrolldown starts upButton is
	 * setVisible.
	 */
	private void scrollDown() {
		closeOpenedSubMenus();
		if ((indexVisible + visibleItems) == subMenus.size()) {
			downButton.setVisible(false);
			return;
		} else if ((indexVisible + visibleItems) > subMenus.size()) {
			return;
		} else {
			try {
				((JComponent) subMenus.get(indexVisible)).setVisible(false);
				((JComponent) subMenus.get(indexVisible + visibleItems))
						.setVisible(true);
				upButton.setVisible(true);
				indexVisible++;
				if ((indexVisible + visibleItems) == subMenus.size()) {
					downButton.setVisible(false);
				}
			} catch (Exception eks) {
				eks.printStackTrace();
			}
		}
	}

	/**
	 * Creates two button: upButton and downButton.
	 */

	private void createButtons() {
		upButton = new JButton(STANDARD_UP_ARROW);
		//Dimension d = new Dimension(50, 10);
		//upButton.setPreferredSize(d);
		upButton.setBorderPainted(false);
		upButton.setFocusPainted(false);
		upButton.setRolloverEnabled(true);
		class Up extends MouseAdapter {
			/**
			 * When mouse enters over the upbutton, timerUp starts the scrolling
			 * upwards. *
			 * 
			 * @param e
			 *            MouseEvent
			 */

			public void mouseEntered(MouseEvent e) {
				try {
					timerUp.start();
				} catch (Exception ekas) {
				}
			}

			/*******************************************************************
			 * When mouse exites the upbutton, timerUp stops.
			 * 
			 * @param e
			 *            MouseEvent
			 */
			public void mouseExited(MouseEvent e) {
				try {
					timerUp.stop();
				} catch (Exception ekas) {
				}
			}
		}
		MouseListener scrollUpListener = new Up();
		upButton.addMouseListener(scrollUpListener);
		add(upButton);
		downButton = new JButton(STANDARD_DOWN_ARROW);
		//downButton.setPreferredSize(d);
		downButton.setBorderPainted(false);
		downButton.setFocusPainted(false);
		class Down extends MouseAdapter {
			/**
			 * When mouse enters over the downbutton, timerDown starts the
			 * scrolling downwards.
			 * 
			 * @param e
			 *            MouseEvent
			 */
			public void mouseEntered(MouseEvent e) {
				try {
					timerDown.start();
				} catch (Exception ekas) {
				}
			}

			/**
			 * When mouse exites the downbutton, timerDown stops.
			 * 
			 * @param e
			 *            MouseEvent
			 */
			public void mouseExited(MouseEvent e) {
				try {
					timerDown.stop();
				} catch (Exception ekas) {
				}
			}
		}
		MouseListener scrollDownListener = new Down();
		downButton.addMouseListener(scrollDownListener);
		add(downButton);
	}

	/**
	 *  * Hides the scrollButtons.
	 */

	public void hideButtons() {
		upButton.setVisible(false);
		downButton.setVisible(false);
	}
}

