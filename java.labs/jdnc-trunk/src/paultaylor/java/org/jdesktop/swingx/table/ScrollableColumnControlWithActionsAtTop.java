package org.jdesktop.swingx.table;

import org.jdesktop.swingx.table.ColumnControlButton;
import org.jdesktop.swingx.table.ColumnControlPopup;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.icon.ColumnControlIcon;
import org.springframework.richclient.image.ArrowIcon;

import javax.swing.*;
import javax.swing.table.*;


import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * A ColumnControl using the ScrollableMenu as popup control.
 * which understands hidden columns and has actions at top
 *
 * TODO:No seperator between actions and table columns instead at bottom of list
 * TODO:Scrolling seems a bit slow 
 */

public class ScrollableColumnControlWithActionsAtTop
{


    public static void main(String args[])
    {
        JFrame f = new JFrame();

        Vector colNames = new Vector();
        for(int i=0;i<30;i++)
        {
            colNames.add("col"+i);
        }

        Vector data = new Vector();
        for (int i = 0; i < 10; i++)
        {
            Vector v = new Vector();
            v.add(i);
            for (int j = 1; j < 30; j++)
            {
                v.add((i + 1) * (j + 1));
            }
            data.add(v);
        }
        JXTable table = new JXTable(data,colNames);
        table.setColumnControl(new ScrollableCCB(table,1));
        table.setColumnControlVisible(true);
        JScrollPane scrollPane1 = new JScrollPane(table);
        f.add(scrollPane1);
        f.pack();
        f.setVisible(true);
    }

    static class ScrollableCCB extends ColumnControlButton {

        private int columnToHide;

        public ScrollableCCB(JXTable table,int columnToHide)
        {
            super(table, new ColumnControlIcon());
            this.columnToHide=columnToHide;
        }

        @Override
        protected ColumnVisibilityAction createColumnVisibilityAction(TableColumn column)
        {
            if (columnToHide == column.getModelIndex())
            {
                return null;
            }


            return super.createColumnVisibilityAction(column);
        }

        @Override
        protected void populatePopup()
        {
            clearAll();
            addAdditionalActionItems();
            if (canControl())
            {
                createVisibilityActions();
                addVisibilityActionItems();
            }
        }

        @Override
        protected ColumnControlPopup createColumnControlPopup() {
            return new ScrollableCCB.ScrollableControlPopup();
        }

        public class ScrollableControlPopup extends DefaultColumnControlPopup {
            private ScrollableMenu scrollableMenu = null;

            @Override
            public void updateUI() {
                getMenu().updateUI();
            }

            @Override
            public void applyComponentOrientation(ComponentOrientation o) {
                getMenu().applyComponentOrientation(o);
            }

            @Override
            public void removeAll() {
                getMenu().removeAll();
            }

            @Override
            public void addAdditionalActionItems(java.util.List<? extends Action> actions) {
                if (actions.size() == 0)
                    return;

                addItems(actions);
                addSeparator();
            }


            @Override
            protected void addItem(JMenuItem item) {
                getMenu().add(item);
            }



            @Override
            protected void addSeparator() {
               getMenu().addSeparator();
            }


            @Override
            protected JPopupMenu getPopupMenu() {
                return getMenu().getPopupMenu();
            }


            protected ScrollableMenu getMenu() {
                if (scrollableMenu == null) {
                    scrollableMenu = new ScrollableMenu();
                }
                return scrollableMenu;
            }
        }
    }

    static class ScrollableMenu extends JMenu
    {
        private static final long serialVersionUID = 4892488347377826602L;

        /**
         * How fast the scrolling will happen.
         */
        private int scrollSpeed = 150;

        public static final Icon STANDARD_UP_ARROW = new ArrowIcon(
            ArrowIcon.Direction.UP, 4, SystemColor.controlDkShadow);

        public static final Icon STANDARD_DOWN_ARROW = new ArrowIcon(
            ArrowIcon.Direction.DOWN, 4, SystemColor.controlDkShadow);

        /**
         * Handles the scrolling upwards.
         */
        private Timer timerUp;

        /**
         * Handles the scrolling downwards.
         */
        private Timer timerDown;

        /**
         * How many items are visible.
         */
        private int visibleItems;

        /**
         * Menuitem's index which is used to control if up and downbutton are
         * visible or not.
         */
        private int indexVisible;

        /**
         * Button to scroll menu upwards.
         */
        private JButton upButton;

        /**
         * Button to scroll menu downwards.
         */
        private JButton downButton;

        /**
         * Container to hold submenus.
         */
        private Vector<JMenuItem> subMenus = new Vector<JMenuItem>();

        /**
         * Height of the screen.
         */
        private double screenHeight;

        /**
         * Height of the menu.
         */
        private double menuHeight;

        /**
         * Creates a new ScrollableMenu object with a given name. This also
         * instantiates the timers and buttons. After the buttons * are created they
         * are set invisible.
         *
         */
        public ScrollableMenu()
        {
            super();
            init();
        }

        private void init()
        {
            timerUp = new Timer(scrollSpeed, new ActionListener()
            {

                public void actionPerformed(ActionEvent evt)
                {
                    scrollUp();
                }
            });
            timerDown = new Timer(scrollSpeed, new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    scrollDown();
                }
            });
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            screenHeight = 500;
            // room for toolbar
            createButtons();
            hideButtons();
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
         * @param menuItem to be added
         * @return added menuitem
         */

        public JMenuItem add(JMenuItem menuItem)
        {
            add(menuItem, subMenus.size() + 1);
            subMenus.add(menuItem);
            menuHeight += menuItem.getPreferredSize().getHeight();
            if (menuHeight > screenHeight)
            {
                menuItem.setVisible(false);
                downButton.setVisible(true);
            }
            else
            {
                visibleItems++;
            }
            return menuItem;
        }

        @Override
        public void removeAll()
        {
            super.removeAll();
            init();
        }

        /**
         * Closes the opened submenus when scrolling starts
         */

        private void closeOpenedSubMenus()
        {
            MenuSelectionManager manager = MenuSelectionManager.defaultManager();
            MenuElement[] path = manager.getSelectedPath();
            int i = 0;
            JPopupMenu popup = getPopupMenu();
            for (; i < path.length; i++)
            {
                if (path[i] == popup)
                {
                    break;
                }
            }
            MenuElement[] subPath = new MenuElement[i + 1];
            try
            {
                System.arraycopy(path, 0, subPath, 0, i + 1);
                manager.setSelectedPath(subPath);
            }
            catch (Exception ekasd)
            {
            }
        }

        /**
         * When timerUp is started it calls constantly this method to make the JMenu
         * scroll upwards. When the top of menu is reached then upButton is set
         * invisible. When scrollUp starts downButton is setVisible.
         */

        private void scrollUp()
        {
            closeOpenedSubMenus();
            if (indexVisible == 0)
            {
                upButton.setVisible(false);
                return;
            }
            else
            {
                indexVisible--;
                ((JComponent) subMenus.get(indexVisible + visibleItems))
                    .setVisible(false);
                ((JComponent) subMenus.get(indexVisible)).setVisible(true);
                downButton.setVisible(true);
                if (indexVisible == 0)
                {
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
        private void scrollDown()
        {
            closeOpenedSubMenus();
            if ((indexVisible + visibleItems) == subMenus.size())
            {
                downButton.setVisible(false);
                return;
            }
            else if ((indexVisible + visibleItems) > subMenus.size())
            {
                return;
            }
            else
            {
                try
                {
                    ((JComponent) subMenus.get(indexVisible)).setVisible(false);
                    ((JComponent) subMenus.get(indexVisible + visibleItems))
                        .setVisible(true);
                    upButton.setVisible(true);
                    indexVisible++;
                    if ((indexVisible + visibleItems) == subMenus.size())
                    {
                        downButton.setVisible(false);
                    }
                }
                catch (Exception eks)
                {
                    eks.printStackTrace();
                }
            }
        }

        /**
         * Creates two button: upButton and downButton.
         */

        private void createButtons()
        {
            upButton = new JButton(STANDARD_UP_ARROW);
            //Dimension d = new Dimension(50, 10);
            //upButton.setPreferredSize(d);
            upButton.setBorderPainted(false);
            upButton.setFocusPainted(false);
            upButton.setRolloverEnabled(true);
            class Up extends MouseAdapter
            {
                /**
                 * When mouse enters over the upbutton, timerUp starts the scrolling
                 * upwards. *
                 *
                 * @param e MouseEvent
                 */

                public void mouseEntered(MouseEvent e)
                {
                    try
                    {
                        timerUp.start();
                    }
                    catch (Exception ekas)
                    {
                    }
                }

                /**
                 * ****************************************************************
                 * When mouse exites the upbutton, timerUp stops.
                 *
                 * @param e MouseEvent
                 */
                public void mouseExited(MouseEvent e)
                {
                    try
                    {
                        timerUp.stop();
                    }
                    catch (Exception ekas)
                    {
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
            class Down extends MouseAdapter
            {
                /**
                 * When mouse enters over the downbutton, timerDown starts the
                 * scrolling downwards.
                 *
                 * @param e MouseEvent
                 */
                public void mouseEntered(MouseEvent e)
                {
                    try
                    {
                        timerDown.start();
                    }
                    catch (Exception ekas)
                    {
                    }
                }

                /**
                 * When mouse exites the downbutton, timerDown stops.
                 *
                 * @param e MouseEvent
                 */
                public void mouseExited(MouseEvent e)
                {
                    try
                    {
                        timerDown.stop();
                    }
                    catch (Exception ekas)
                    {
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

        public void hideButtons()
        {
            upButton.setVisible(false);
            downButton.setVisible(false);
        }
    }


}
