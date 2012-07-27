/*
 * $Id: JXMenu.java 2431 2008-06-03 09:04:00Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jxpopupmenu;
import java.awt.Component;
import java.awt.ComponentOrientation;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.plaf.PopupMenuUI;

/**
 * A wrapper for JXMenu that supports a scrollable popup menu. This class
 * has to duplicate more than necessary because the 'popupMenu' variable in
 * JMenu has 'private' access. If JMenu code was modified to make use of
 * 'getPopupMenu' rather than accessing the variable directly then some of the
 * following code would be un-necessary.
 *
 * @author Rob Stone
 */
public class JXMenu extends JMenu
{
    private JXPopupMenu popupMenu=null;
    
    /*
     * See super class
     */
    public JXMenu()
    {
        super();
    }
    
    /*
     * See super class
     */
    public JXMenu(final Action action)
    {
        super(action);        
    }

    /*
     * See super class
     */
    public JXMenu(final String labelText)
    {
        super(labelText);
    }
        
    /**
     * Does pretty much the same as the same method in JMenu
     */
    private void ensurePopupMenuCreated()
    {
        if (popupMenu==null)
        {
            popupMenu=new JXPopupMenu();
            popupMenu.setInvoker(this);
            popupListener=createWinListener(popupMenu);
        }        
    }
    
    /*
     * See super class
     */    
    @Override
    public JPopupMenu getPopupMenu()
    {
        ensurePopupMenuCreated();
        return popupMenu;
    }

    /*
     * See super class. Need to override to make sure our custom popup is used.
     */
    @Override
    public JMenuItem add(final JMenuItem menuItem)
    {
        getPopupMenu().add(menuItem);
        return menuItem;
    }

    /*
     * See super class. Need to override to make sure our custom popup is used.
     */
    @Override
    public Component add(Component c)
    {
        getPopupMenu().add(c);        
        return c;
    }
        
    /*
     * See super class. Need to override to make sure our custom popup is used.
     */
    @Override
    public void updateUI()
    {
        super.updateUI();

        if (popupMenu!=null)
        {
            popupMenu.setUI((PopupMenuUI)UIManager.getUI(popupMenu));
        }
    }

    /*
     * See super class. Need to override to make sure our custom popup is used.
     */
    @Override
    public void addSeparator()
    {
        getPopupMenu().addSeparator();
    }

    /*
     * See super class. Need to override to make sure our custom popup is used.
     */
    @Override
    public boolean isPopupMenuVisible()
    {
        return getPopupMenu().isVisible();
    }

    /*
     * See super class. Need to override to make sure our custom popup is used.
     */
    @Override
    public void setMenuLocation(int x, int y)
    {
        super.setMenuLocation(x, y);
        if (popupMenu!=null)
        {
            popupMenu.setLocation(x, y);
        }
    }

    /*
     * See super class. Need to override to make sure our custom popup is used.
     */
    @Override
    public void applyComponentOrientation(ComponentOrientation o)
    {
        super.applyComponentOrientation(o);

        if (popupMenu!=null)
        {
            popupMenu.setComponentOrientation(o);
        }
    }

    /*
     * See super class. Need to override to make sure our custom popup is used.
     */
    @Override
    public void setComponentOrientation(ComponentOrientation o)
    {
        super.setComponentOrientation(o);
        if (popupMenu!=null)
        {
            popupMenu.setComponentOrientation(o);
        }
    }

    /*
     * See super class. Need to override to make sure our custom popup is used.
     */
    @Override
    public int getMenuComponentCount()
    {
        int componentCount=0;
        if (popupMenu!=null)
        {
            componentCount=popupMenu.getComponentCount();
        }
        return componentCount;
    }

    /*
     * See super class. Need to override to make sure our custom popup is used.
     */
    @Override
    public Component getMenuComponent(int n)
    {
        if (popupMenu!=null)
        {
            return popupMenu.getComponent(n);
        }
        return null;
    }

    /*
     * See super class. Need to override to make sure our custom popup is used.
     */
    @Override
    public Component[] getMenuComponents()
    {
        if (popupMenu!=null)
        {
            return popupMenu.getComponents();
        }
        return new Component[0];
    }
}
