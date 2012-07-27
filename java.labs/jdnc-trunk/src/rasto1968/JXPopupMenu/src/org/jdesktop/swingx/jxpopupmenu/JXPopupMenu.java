/*
 * $Id: JXPopupMenu.java 2431 2008-06-03 09:04:00Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jxpopupmenu;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

/**
 * A customized popup menu that provides a scrollable popup menu.
 *
 * @author Rob Stone
 */
public class JXPopupMenu extends JPopupMenu
{
    private static final int DEFAULT_MAXIMUM_MENUITEMS=10;
    private Icon upIcon=new ImageIcon(getClass().getResource("images/up.png"));
    private Icon downIcon=new ImageIcon(getClass().getResource("images/down.png"));
    private Icon upDisabledIcon=new ImageIcon(getClass().getResource("images/up-disabled.png"));
    private Icon downDisabledIcon=new ImageIcon(getClass().getResource("images/down-disabled.png"));
    private final JPanel upPanel=new JPanel(new BorderLayout());
    private final JPanel downPanel=new JPanel(new BorderLayout());    
    private final JButton scrollUp=new JButton();
    private final JButton scrollDown=new JButton();
    private int maximumMenuItems=DEFAULT_MAXIMUM_MENUITEMS;
    private int menuItemOffset=0;
    private List<Component> menuItems=new ArrayList<Component>();
    
    /*
     * See super class
     */
    public JXPopupMenu()
    {
        this(null);
    }
    
    /*
     * See super class
     */
    public JXPopupMenu(final String title)
    {
        super(title);
        init();
    }
    
    /**
     * Initialise the custom GUI components
     */
    private void init()
    {
        upPanel.setOpaque(false);
        downPanel.setOpaque(false);
        scrollUp.setOpaque(false);
        scrollDown.setOpaque(false);
        scrollUp.setIcon(upIcon);
        scrollDown.setIcon(downIcon);
        scrollUp.setDisabledIcon(upDisabledIcon);
        scrollDown.setIcon(downIcon);
        scrollDown.setDisabledIcon(downDisabledIcon);
        scrollUp.setFocusPainted(false);
        scrollDown.setFocusPainted(false);
        scrollUp.addMouseListener(new MouseAdapter()
        {
            // A timer that will keep scrolling while the button is pressed
            private Timer autoScroll=new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    scrollMenuUp();
                }
            });
            
            @Override
            public void mousePressed(MouseEvent e)
            {
                scrollMenuUp();
                autoScroll.restart();
            }
            
            @Override
            public void mouseReleased(MouseEvent e)
            {
                autoScroll.stop();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                autoScroll.stop();
            }                        
        });
        scrollDown.addMouseListener(new MouseAdapter()
        {
            // A timer that will keep scrolling while the button is pressed
            private Timer autoScroll=new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    scrollMenuDown();
                }
            });
            
            @Override
            public void mousePressed(MouseEvent e)
            {
                scrollMenuDown();
                autoScroll.restart();
            }
            
            @Override
            public void mouseReleased(MouseEvent e)
            {
                autoScroll.stop();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                autoScroll.stop();
            }                        
        });
        upPanel.add(scrollUp, BorderLayout.CENTER);        
        downPanel.add(scrollDown, BorderLayout.CENTER);
        super.add(upPanel, 0);
        super.add(downPanel, super.getComponentCount());
        
        // Make sure we detect the scroll wheel being used
        addMouseWheelListener(new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                if (e.getScrollType()==MouseWheelEvent.WHEEL_UNIT_SCROLL)
                {
                    if (e.getWheelRotation()>0 && scrollDown.isEnabled())
                    {
                        scrollMenuDown();
                    }
                    else if (e.getWheelRotation()<0 && scrollUp.isEnabled())
                    {
                        scrollMenuUp();                        
                    }
                }
            }
        });
    }

    /**
     * Enable/Disable the scrolling buttons when relevant
     */
    private void checkScrollEnabled()
    {
        scrollUp.setEnabled(menuItemOffset>0);
        scrollDown.setEnabled(menuItemOffset+getMaximumMenuItems()<menuItems.size());
    }
    
    /**
     * Scroll up
     */
    private void scrollMenuUp()
    {
        if (menuItemOffset>0)
        {
            menuItemOffset--;
            menuItems.get(menuItemOffset).setVisible(true);
            menuItems.get(menuItemOffset+getMaximumMenuItems()).setVisible(false);
            checkScrollEnabled();
        }
    }
    
    /**
     * Scroll down
     */
    private void scrollMenuDown()
    {
        if (menuItemOffset+getMaximumMenuItems()<menuItems.size())
        {
            menuItems.get(menuItemOffset).setVisible(false);
            menuItems.get(menuItemOffset+getMaximumMenuItems()).setVisible(true);
            menuItemOffset++;
            checkScrollEnabled();
        }
    }
    
    /**
     * Check to see (and show/hide if necessary) whether the scroll bar buttons
     * should be visible.
     * @return <code>true</code> the buttons are visible, <code>false</code> the buttons are invisible
     */
    private boolean checkScrollButtons()
    {
        final boolean scrollButtonsVisible=menuItems.size()>getMaximumMenuItems();
        scrollUp.setVisible(scrollButtonsVisible);
        scrollDown.setVisible(scrollButtonsVisible);
        checkScrollEnabled();
        return scrollButtonsVisible;
    }
     
    /*
     * See super-class. Redirect to allow the scrollbar controls to be used
     */
    @Override
    public JMenuItem add(final Action a)
    {
    	final JMenuItem menuItem=createActionComponent(a);
        menuItem.setAction(a);
        add((Component)menuItem);
        return menuItem;
    }

    /*
     * See super-class. Redirect to allow the scrollbar controls to be used
     */
    @Override
    public JMenuItem add(final String s)
    {
        final JMenuItem menuItem=new JMenuItem(s);
        add((Component)menuItem);
        return menuItem;
    }

    /*
     * See super-class. Redirect to allow the scrollbar controls to be used
     */
    @Override
    public Component add(final Component comp)
    {
        menuItems.add(comp);
        super.add(comp, getComponentCount()-1);
        if (checkScrollButtons())
        {
            // Scroll buttons are being drawn, hide this item
            comp.setVisible(false);
        }        
        return comp;
    }

    /*
     * See super-class. Redirect to allow the scrollbar controls to be used
     */
    @Override
    public JMenuItem add(final JMenuItem menuItem)
    {
        add((Component)menuItem);
        return menuItem;
    }   
    
    /*
     * See super-class. Redirect to allow the scrollbar controls to be used
     */
    @Override
    public void remove(final int pos)
    {
        // Block any attempt to remove the scroll buttons
        if (pos<menuItems.size())
        {
            menuItems.remove(pos);
            checkScrollButtons();
            super.remove(pos+1);
        }
    }

    /*
     * See super-class. Redirect to allow the scrollbar controls to be used
     */
    @Override
    public void removeAll()
    {
        while (menuItems.size()>0)
        {
            menuItems.remove(0);
        }
        checkScrollButtons();
        super.removeAll();
        
        // Re-add the scroll buttons
        super.add(upPanel, 0);
        super.add(downPanel, super.getComponentCount());
    }
    
    /**
     * @return the maximum number of items to show in the menu before showing the scroll controls
     */
    public int getMaximumMenuItems()
    {
        return maximumMenuItems;
    }

    /**
     * @param maximumMenuItems the maximum number of items to show in the menu before showing the scroll controls
     */
    public void setMaximumMenuItems(final int maximumMenuItems)
    {
        this.maximumMenuItems=maximumMenuItems;
    }

    /*
     * See super-class. Make sure that we are always as wide as our widest
     * menu item.
     */
    @Override
    public Dimension getPreferredSize()
    {
        final Dimension size=super.getPreferredSize();
        
        for (Component menuItem : menuItems)
        {
            size.width=Math.max(size.width, menuItem.getPreferredSize().width);
        }
        
        return size;
    }

    /*
     * See super-class. Redirect to allow the scrollbar controls to be used
     */
    @Override
    public void addSeparator()
    {
        add((Component)(new JPopupMenu.Separator()));
    }
}
