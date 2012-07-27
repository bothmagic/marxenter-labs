/*
 * JXTabbedPane.java
 *
 * Created on 9 de Dezembro de 2006, 06:07
 */
package org.jdesktop.swingx;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.TabbedPaneUI;

/**
 * @author Mário César
 */
public class JXTabbedPane extends javax.swing.JTabbedPane implements Serializable {
    /*
     *  public void addTab(String title, Icon icon, Component component, String tip, boolean closable) {
     *
    */
    public static final int BOXED_RED_ICON = 0;
    public static final int BOXED_ICON = 1;
    public static final int SIMPLE_ICON = 2;
    private Vector <CloseTabIcon>tabItens = new Vector<CloseTabIcon>();
    private int closeIconLayout = BOXED_RED_ICON;
    public JXTabbedPane() {
        this(JTabbedPane.TOP);
    }
    public JXTabbedPane(int tabPlacement) {
        this(tabPlacement, WRAP_TAB_LAYOUT);
    }
    public JXTabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super.setTabLayoutPolicy(tabLayoutPolicy);
        super.setModel(new DefaultSingleSelectionModel());
        this.setTabPlacement(tabPlacement);
        this.updateUI();
    }
    /**
     *
     *   Sobrecarga de métodos...
     *
     */
    /**
     * Inserts a <code>component</code>, at <code>index</code>,
     * represented by a <code>title</code> and/or <code>icon</code>,
     * either of which may be <code>null</code>.
     * Uses java.util.Vector internally, see <code>insertElementAt</code>
     * for details of insertion conventions. 
     * 
     * 
     * @param title the title to be displayed in this tab
     * @param icon the icon to be displayed in this tab
     * @param component The component to be displayed when this tab is clicked.
     * @param tip the tooltip to be displayed for this tab
     * @param index the position to insert this new tab
     * @see #addTab
     * @see #removeTabAt
     */
    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        CloseTabIcon ico = new CloseTabIcon(title, icon, true);
	int newIndex = index;
        int removeIndex = super.indexOfComponent(component);
        if (component != null && removeIndex != -1) {
            removeTabAt(removeIndex);
	    if (newIndex > removeIndex) {
		newIndex--;
	    }
        }
        tabItens.insertElementAt(ico, newIndex);
        super.insertTab(null, ico, component, tip, newIndex);
    }
    public void addTab(String title, Icon icon, Component component, String tip, boolean closable) {
        CloseTabIcon ico = new CloseTabIcon(title, icon, closable);
	int newIndex = tabItens.size();
        int removeIndex = super.indexOfComponent(component);
        if (component != null && removeIndex != -1) {
            removeTabAt(removeIndex);
	    if (newIndex > removeIndex) {
		newIndex--;
	    }
        }
        tabItens.insertElementAt(ico, newIndex);
        super.insertTab(null, ico, component, tip, newIndex);
    }
    public void addTab(String title, Icon icon, Component component, boolean closable) {
        addTab(title, icon, component, null, closable);
    }
    public void addTab(String title, Component component, String tip, boolean closable) {
        addTab(title, null, component, tip, closable);
    }
    public void addTab(String title, Component component, boolean closable) {
        addTab(title, null, component, null, closable);
    }
    /**
     * Removes the tab at <code>index</code>.
     * After the component associated with <code>index</code> is removed,
     * its visibility is reset to true to ensure it will be visible
     * if added to other containers.
     * 
     * @param index the index of the tab to be removed
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (index < 0 || index >= tab count)
     * @see #addTab
     * @see #insertTab
     */
    @Override
    public void removeTabAt(int index) {
        super.removeTabAt(index);
        tabItens.removeElementAt(index);
    }
    /**
     * Sets the tab placement for this tabbedpane.
     * Possible values are:<ul>
     * <li><code>JTabbedPane.TOP</code>
     * <li><code>JTabbedPane.BOTTOM</code>
     * <li><code>JTabbedPane.LEFT</code>
     * <li><code>JTabbedPane.RIGHT</code>
     * </ul>
     * The default value, if not set, is <code>SwingConstants.TOP</code>.
     *
     *
     * @param tabPlacement the placement for the tabs relative to the content
     * @exception IllegalArgumentException if tab placement value isn't one
     * 				of the above valid values
     * @beaninfo preferred: true
     *        bound: true
     *    attribute: visualUpdate true
     *         enum: TOP JTabbedPane.TOP
     *               LEFT JTabbedPane.LEFT
     *               BOTTOM JTabbedPane.BOTTOM
     *               RIGHT JTabbedPane.RIGHT
     *  description: The tabbedpane's tab placement.
     */
    @Override
    public void setTabPlacement(int tabPlacement) {
        super.setTabPlacement(tabPlacement);
    }
    /**
     * Resets the UI property to a value from the current look and feel.
     * 
     * 
     * @see JComponent#updateUI
     */
    public void updateUI() {
        if (UIManager.getLookAndFeel().getName() == "Metal") {
            super.setUI(new JXMetalTabbedPaneUI());
        } else {
            super.updateUI();
        }
        super.revalidate();
        super.repaint();
    }
    /*
     *  @see #setTitleAtTab
     */
    @Deprecated
    public String getTitleAt(int index) {
        return super.getTitleAt(index);
    }
    /*
     * Sets the title at <code>index</code> to <code>title</code> which
     * can be <code>null</code>. 
     * An internal exception is raised if there is no tab at that index.
     * 
     * 
     * @param index the tab index where the title should be set
     * @param title the title to be displayed in the tab
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (index &lt; 0 || index &gt;= tab count)
     * @see #getTitleAt
     * @beaninfo preferred: true
     *    attribute: visualUpdate true
     *  description: The title at the specified tab index.
     */
    @Override
    public void setTitleAt(int index, String title) {
        String oldTitle = tabItens.elementAt(index).getTexto();
        tabItens.elementAt(index).setTexto(title);
	if (oldTitle != title) {
	    super.firePropertyChange("indexForTitle", -1, index);
	}
        if (title == null || oldTitle == null ||
            !title.equals(oldTitle)) {
        }
        super.revalidate();
        super.repaint();
    }
    /*
     *  @see #setIconAtTab
     */
    @Deprecated
    public Icon getIconAt(int index) {
        return super.getIconAt(index);
    }
    /*
     * Sets the icon at <code>index</code> to <code>icon</code> which can be
     * <code>null</code>. This does not set disabled icon at <code>icon</code>.
     * If the new Icon is different than the current Icon and disabled icon
     * is not explicitly set, the LookAndFeel will be asked to generate a disabled
     * Icon. To explicitly set disabled icon, use <code>setDisableIconAt()</code>. 
     * An internal exception is raised if there is no tab at that index. 
     * 
     * 
     * @param index the tab index where the icon should be set
     * @param icon the icon to be displayed in the tab
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (index < 0 || index >= tab count)
     * @see #setDisabledIconAt
     * @see #getIconAt
     * @see #getDisabledIconAt
     * @beaninfo preferred: true
     *    attribute: visualUpdate true
     *  description: The icon at the specified tab index.
     */
    @Override
    public void setIconAt(int index, Icon icon) {
        tabItens.elementAt(index).setIcon(icon);
        super.revalidate();
        super.repaint();
    }
    /*
     * Returns the first tab index with a given <code>title</code>,  or
     * -1 if no tab has this title. 
     * 
     * 
     * @param title the title for the tab
     * @return the first tab index which matches <code>title</code>, or
     * 		-1 if no tab has this title
     */
    @Override
    public int indexOfTab(String title) {
        int i;
        for (i = 0; i < tabItens.size(); i++) {
            if (tabItens.elementAt(i).getTexto().compareTo(title) == 0) {
                return(i);
            }
        }
        return(-1);
    }
    /*
     * Returns the first tab index with a given <code>icon</code>,
     * or -1 if no tab has this icon.
     * 
     * 
     * @param icon the icon for the tab
     * @return the first tab index which matches <code>icon</code>,
     * 		or -1 if no tab has this icon
     */
    @Override
    public int indexOfTab(Icon icon) {
        int i;
        for (i = 0; i < tabItens.size(); i++) {
            Icon thisIco = tabItens.elementAt(i).getIcon();
            if ((thisIco != null && thisIco.equals(icon)) ||
                (thisIco == null && thisIco == icon)) { 
                return(i);
            }
        }
        return(-1);
    }
    private int superIndexOfTab(Icon icon) {
        int retValue;
        retValue = super.indexOfTab(icon);
        return retValue;
    }
    
    /**
     * Sets the font for this component.
     * 
     * 
     * @param font the desired <code>Font</code> for this component
     * @see java.awt.Component#getFont
     * @beaninfo preferred: true
     *        bound: true
     *    attribute: visualUpdate true
     *  description: The font for the component.
     */
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        super.repaint();
    }
    
    /**
     *
     *   NOVOS MÉTODOS!
     *
     */

    /**
     * Returns the tab title at <code>index</code>.
     * 
     * 
     * @param index  the index of the item being queried
     * @return the title at <code>index</code>
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (index < 0 || index >= tab count)
     * @see #setTitleAt
     */
    public String getTitleAtTab(int index) {
        return tabItens.elementAt(index).getTexto();
    }
    /*
     * Returns the tab icon at <code>index</code>.
     * 
     * 
     * @param index  the index of the item being queried
     * @return the icon at <code>index</code>
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (index < 0 || index >= tab count)
     * @see #setIconAt
     */
    public Icon getIconAtTab(int index) {
        return tabItens.elementAt(index).getIcon();
    }
    public void setClosableAt(int index, boolean closable) {
        tabItens.elementAt(index).setClosable(closable);
        super.repaint();
    }
    public boolean getClosableAt(int index) {
        return tabItens.elementAt(index).isClosable();
    }
    /**
     * Getter for property closeIconLayout.
     * @return Value of property closeIconLayout.
     */
    public int getCloseIconLayout() {
        return this.closeIconLayout;
    }
    /**
     * Sets the close incon layout for each tab .
     * Possible values are:<ul>
     * <li><code>JXTabbedPane.BOXED_RED_ICON</code>
     * <li><code>JXTabbedPane.BOXED_ICON</code>
     * <li><code>JXTabbedPane.SIMPLE_ICON</code>
     * </ul>
     * The default value, if not set, is <code>JXTabbedPane.SIMPLE_ICON</code>.
     *
     * Setter for property closeIconLayout.
     * @param closeIconLayout New value of property closeIconLayout.
     * @exception IllegalArgumentException if tab placement value isn't one
     * 				of the above valid values
     * @beaninfo preferred: true
     *        bound: true
     *    attribute: visualUpdate true
     *         enum: BOXED_RED_ICON JXTabbedPane.BOXED_RED_ICON
     *               BOXED_ICON JXTabbedPane.BOXED_ICON
     *               SIMPLE_ICON JXTabbedPane.SIMPLE_ICON
     *  description: The tab's close icon layout.
     */
    public void setCloseIconLayout(int closeIconLayout) {
        if (closeIconLayout != BOXED_RED_ICON && closeIconLayout != BOXED_ICON && closeIconLayout != SIMPLE_ICON) {
            throw new IllegalArgumentException("illegal close icon layout: must be BOXED_RED_ICON or BOXED_ICON or SIMPLE_ICON");
        }
        int oldValue = this.closeIconLayout;
        int newValue = closeIconLayout;
        if (newValue != oldValue) {
            this.closeIconLayout = closeIconLayout;
            super.firePropertyChange("closeIconLayout", oldValue, newValue);
        }
    }
    
    /**
     *
     *   Classes privadas
     *
     */
    
    private class CloseTabIcon implements Icon {
        private CloseIcon closeIcon;
        private RotatedTextIcon iconText;
        private RotatedIcon ico;
        private Rectangle closeIconBounds = new Rectangle();
        private String texto;
        private Insets tabInsets;
        private boolean clidado = false;
        private boolean closable = false;
        private int x_pos;
        private int y_pos;
        public CloseTabIcon(String texto, Icon ico, boolean closable) {
            this.texto = texto;
            this.ico = new RotatedIcon(ico);
            this.closable = closable;
            closeIcon = new CloseIcon(false);
            setFocado(false);
            iconText = new RotatedTextIcon(texto);
            tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
        }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            //Atualiza as variáveis locais...
            this.x_pos = x;
            this.y_pos = y;
            //Imprime o texto e o ícone...
            int xx, yy, addPos;
            //int tabIndex = JXTabbedPane.this.indexOfTab(this);
            int tabIndex = JXTabbedPane.this.superIndexOfTab(this);
            Rectangle bounds = getUI().getTabBounds(JXTabbedPane.this, tabIndex);
            tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
            if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.RIGHT) {
                yy = y + tabInsets.left - tabInsets.top;
                if (ico.getIcon() != null) {
                    addPos = Math.max(((iconText.getIconWidth() - ico.getIconWidth()) / 2), ((closeIcon.getIconWidth() - ico.getIconWidth()) / 2));
                    ico.paintIcon(c, g, x - tabInsets.left + tabInsets.top + addPos, yy);
                    yy += ico.getIconHeight() + 4;
                }
                addPos = Math.max(((ico.getIconWidth() - iconText.getIconWidth()) / 2), ((closeIcon.getIconWidth() - iconText.getIconWidth()) / 2));
                iconText.paintIcon(c, g, x - tabInsets.left + tabInsets.top + addPos, yy);
                yy += iconText.getIconHeight() + 4;
                if (closable) {
                    addPos = Math.max(((iconText.getIconWidth() - closeIcon.getIconWidth()) / 2), ((ico.getIconWidth() - closeIcon.getIconWidth()) / 2));
                    closeIcon.paintIcon(c, g, x - tabInsets.left + tabInsets.top + addPos, yy);
                    if (JXTabbedPane.this.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT) {
                        closeIconBounds.setBounds((x - bounds.x) - tabInsets.left + tabInsets.top + addPos, (yy - bounds.y), closeIcon.getIconWidth(), closeIcon.getIconHeight());
                    } else {
                        closeIconBounds.setBounds(x - tabInsets.left + tabInsets.top + addPos, (yy - y) + (tabInsets.top * 2) +  2, closeIcon.getIconWidth(), closeIcon.getIconHeight());
                    }
                }
            } else if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.LEFT) {
                if (closable) {
                    yy = y + tabInsets.left - tabInsets.top;
                    if (closable) {
                        addPos = Math.max(((iconText.getIconWidth() - closeIcon.getIconWidth()) / 2), ((ico.getIconWidth() - closeIcon.getIconWidth()) / 2));
                        closeIcon.paintIcon(c, g, x - tabInsets.left + tabInsets.top + addPos, yy);
                        if (JXTabbedPane.this.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT) {
                            closeIconBounds.setBounds(x - tabInsets.left + tabInsets.top + addPos - bounds.x + 1, (yy - bounds.y), closeIcon.getIconWidth(), closeIcon.getIconHeight());
                        } else {
                            closeIconBounds.setBounds(x - tabInsets.left + tabInsets.top + addPos - bounds.x + 1, tabInsets.right + tabInsets.bottom, closeIcon.getIconWidth(), closeIcon.getIconHeight());
                        }
                        yy += closeIcon.getIconHeight() + 4;
                    }
                    addPos = Math.max(((ico.getIconWidth() - iconText.getIconWidth()) / 2), ((closeIcon.getIconWidth() - iconText.getIconWidth()) / 2));
                    iconText.paintIcon(c, g, x - tabInsets.left + tabInsets.top + addPos, yy);
                    yy += iconText.getIconHeight() + 4;
                    if (ico.getIcon() != null) {
                        addPos = Math.max(((iconText.getIconWidth() - ico.getIconWidth()) / 2), ((closeIcon.getIconWidth() - ico.getIconWidth()) / 2));
                        ico.paintIcon(c, g, x - tabInsets.left + tabInsets.top + addPos, yy);
                    }
                } else {
                    yy = y;
                    addPos = (ico.getIconWidth() - iconText.getIconWidth()) / 2;
                    iconText.paintIcon(c, g, x + addPos, yy);
                    yy += iconText.getIconHeight() + 4;
                    if (ico.getIcon() != null) {
                        addPos = Math.max(((iconText.getIconWidth() - ico.getIconWidth()) / 2), ((closeIcon.getIconWidth() - ico.getIconWidth()) / 2));
                        ico.paintIcon(c, g, x + addPos, yy);
                    }
                }
            } else if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.TOP) {
                xx = x;
                if (ico.getIcon() != null) {
                    addPos = Math.max(((iconText.getIconHeight() - ico.getIconHeight()) / 2), ((closeIcon.getIconHeight() - ico.getIconHeight()) / 2));
                    ico.paintIcon(c, g, x, y + 1 + addPos);
                    xx += ico.getIconWidth() + 4;
                }
                addPos = Math.max(((ico.getIconHeight() - iconText.getIconHeight()) / 2), ((closeIcon.getIconHeight() - iconText.getIconHeight()) / 2));
                iconText.paintIcon(c, g, xx, y + 1 + addPos);
                xx += iconText.getIconWidth() + 4;
                if (closable) {
                    addPos = Math.max(((iconText.getIconHeight() - closeIcon.getIconHeight()) / 2), ((ico.getIconHeight() - closeIcon.getIconHeight()) / 2));
                    closeIcon.paintIcon(c, g, xx, y + 1 + addPos);
                    if (JXTabbedPane.this.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT) {
                        closeIconBounds.setBounds((xx - bounds.x), (y - bounds.y) + 1 + addPos, closeIcon.getIconWidth(), closeIcon.getIconHeight());
                    } else {
                        closeIconBounds.setBounds((xx - x) + tabInsets.left + 3 + (tabInsets.top * 2), (y - bounds.y) + 1 + addPos, closeIcon.getIconWidth(), closeIcon.getIconHeight());
                    }
                }
            } else {
                xx = x;
                if (ico.getIcon() != null) {
                    addPos = Math.max(((iconText.getIconHeight() - ico.getIconHeight()) / 2), ((closeIcon.getIconHeight() - ico.getIconHeight()) / 2));
                    ico.paintIcon(c, g, x, y - tabInsets.top + 1 + addPos);
                    xx += ico.getIconWidth() + 4;
                }
                addPos = Math.max(((ico.getIconHeight() - iconText.getIconHeight()) / 2), ((closeIcon.getIconHeight() - iconText.getIconHeight()) / 2));
                iconText.paintIcon(c, g, xx, y - tabInsets.top + 1 + addPos);
                xx += iconText.getIconWidth() + 4;
                if (closable) {
                    addPos = Math.max(((iconText.getIconHeight() - closeIcon.getIconHeight()) / 2), ((ico.getIconHeight() - closeIcon.getIconHeight()) / 2));
                    closeIcon.paintIcon(c, g, xx, y - tabInsets.top + 1 + addPos);
                    if (JXTabbedPane.this.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT) {
                        closeIconBounds.setBounds((xx - bounds.x), (y - bounds.y) + addPos, closeIcon.getIconWidth(), closeIcon.getIconHeight());
                    } else {
                        closeIconBounds.setBounds(tabInsets.left + (xx - x) + 3 + (tabInsets.top * 2), y - tabInsets.top + 1 + addPos, closeIcon.getIconWidth(), closeIcon.getIconHeight());
                    }
                }
            }
            //Adiciona o listener à tabela para monitorar os cliques.
            if (closable) {
                JXTabbedPane.this.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        if (!e.isConsumed() && !clidado) {
                            boolean exec = dentro(e.getX(), e.getY());
                            if (exec) {
                                int index = JXTabbedPane.this.superIndexOfTab(CloseTabIcon.this);
                                JXTabbedPane.this.remove(index);
                                clidado = true;
                                e.consume();
                            }
                        }
                    }
                    public void mouseExited(MouseEvent e) {
                        if (isFocado()) {
                            setFocado(false);
                            e.consume();
                        }
                    }
                });
                JXTabbedPane.this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                    public void mouseMoved(MouseEvent e) {
                        boolean exec = dentro(e.getX(), e.getY());
                        if (exec && !isFocado()) {
                            setFocado(true);
                            e.consume();
                        } else if (!exec && isFocado()) {
                            setFocado(false);
                            e.consume();
                        }
                    }
                });
            }
        }
        private boolean dentro(int x, int y) {
            Rectangle rect;
            int index = JXTabbedPane.this.superIndexOfTab(CloseTabIcon.this);
            TabbedPaneUI tpu = JXTabbedPane.this.getUI();
            try {
                rect = tpu.getTabBounds(JXTabbedPane.this, index);
                if (rect.contains(x, y)) {
                    rect.x = x - rect.x;
                    rect.y = y - rect.y;
                    if (getCloseIconBounds().contains(rect.x, rect.y)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
                //Do nothing...
            }
        }
        private void setFocado(boolean focado) {
            closeIcon.setFocado(focado);
            JXTabbedPane.this.repaint();
        }
        private boolean isFocado() {
            return(closeIcon.isFocado());
        }
        public int getIconWidth() {
            int retorno;
            if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.RIGHT || JXTabbedPane.this.getTabPlacement() == JTabbedPane.LEFT) {
                if (closable) {
                    if (iconText.getIconWidth() > closeIcon.getIconWidth() && 
                            iconText.getIconWidth() > ico.getIconWidth()) {
                        retorno = iconText.getIconWidth();
                    } else if (closeIcon.getIconWidth() > iconText.getIconWidth() && 
                            closeIcon.getIconWidth() > ico.getIconWidth()) {
                        retorno = closeIcon.getIconWidth();
                    } else {
                        retorno = ico.getIconWidth();
                    }
                } else {
                    if (iconText.getIconWidth() > ico.getIconWidth()) {
                        retorno = iconText.getIconWidth();
                    } else {
                        retorno = ico.getIconWidth();
                    }
                }
                retorno += tabInsets.top + tabInsets.bottom - tabInsets.left - tabInsets.right;
            } else {
                retorno = iconText.getIconWidth();
                if (ico.getIcon() != null) {
                    retorno += 4 + ico.getIconWidth();
                }
                if (closable) {
                    retorno += 4 + closeIcon.getIconWidth();
                }
            }
            return retorno;
        }
        public int getIconHeight() {
            int retorno;
            if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.RIGHT || JXTabbedPane.this.getTabPlacement() == JTabbedPane.LEFT) {
                retorno = iconText.getIconHeight();
                if (ico.getIcon() != null) {
                    retorno += 4 + ico.getIconHeight();
                }
                if (closable) {
                    retorno += 4 + closeIcon.getIconHeight();
                }
                retorno += - tabInsets.top - tabInsets.bottom + tabInsets.left + tabInsets.right;
            } else {
                if (closable) {
                    if (iconText.getIconHeight() > closeIcon.getIconHeight() && 
                            iconText.getIconHeight() > ico.getIconHeight()) {
                        retorno = iconText.getIconHeight();
                    } else if (closeIcon.getIconHeight() > iconText.getIconHeight() && 
                            closeIcon.getIconHeight() > ico.getIconHeight()) {
                        retorno = closeIcon.getIconHeight();
                    } else {
                        retorno = ico.getIconHeight();
                    }
                    if (iconText.getIconHeight() > 16) {
                        retorno = iconText.getIconHeight();
                    } else {
                        retorno = closeIcon.getIconHeight();
                    }
                } else {
                    if (iconText.getIconHeight() > ico.getIconHeight()) {
                        retorno = iconText.getIconHeight();
                    } else {
                        retorno = ico.getIconHeight();
                    }
                }
            }
            return retorno;
        }
        public void setTexto(String texto) {
            this.texto = texto;
            iconText.setTexto(texto);
        }
        public String getTexto() {
            return(texto);
        }
        public void setIcon(Icon ico) {
            this.ico.setIcon(ico);
        }
        public Icon getIcon() {
            return(ico.getIcon());
        }
        public Rectangle getBounds() {
            return new Rectangle(x_pos, y_pos, getIconWidth(), getIconHeight());
        }
        public Rectangle getCloseIconBounds() {
            return closeIconBounds;
        }
        public void setClosable(boolean closable) {
            this.closable = closable;
        }
        public boolean isClosable() {
            return(closable);
        }
    }
    private class RotatedIcon implements Icon {
        private int width;
        private int height;
        private Icon ico = null;
        public RotatedIcon(Icon ico) {
            if (ico != null) {
                this.ico = ico;
                height = ico.getIconHeight();
                width = ico.getIconWidth();
            } else {
                height = 0;
                width = 0;
            }
        }
        public int getIconWidth() {
            int retorno;
            if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.RIGHT || JXTabbedPane.this.getTabPlacement() == JTabbedPane.LEFT) {
                retorno = height;
            } else {
                retorno = width;
            }
            return retorno;
        }
        public int getIconHeight() {
            int retorno;
            if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.RIGHT || JXTabbedPane.this.getTabPlacement() == JTabbedPane.LEFT) {
                retorno = width;
            } else {
                retorno = height;
            }
            return retorno;
        }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D)g;
            AffineTransform oldTransform = g2d.getTransform();
            RenderingHints oldHints = g2d.getRenderingHints();
            if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.TOP || JXTabbedPane.this.getTabPlacement() == JTabbedPane.BOTTOM) {
                //g.drawString(text, x + 2, (int)(y + ascent));
                //g2d.drawGlyphVector(glyphs,x + 2,y + ascent);
                ico.paintIcon(c, g2d, x + 2, y);
            } if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.RIGHT) {
                AffineTransform trans = new AffineTransform();
                trans.concatenate(oldTransform);
                trans.translate(x,y + 2);
                trans.rotate(Math.PI / 2, height / 2, width / 2);
                g2d.setTransform(trans);
                ico.paintIcon(c, g2d, (height - width) / 2, (width - height) / 2);
                //g.drawString(text, (int)((height - width) / 2), (int)((width - height) / 2 + ascent));
                //g2d.drawGlyphVector(glyphs, (height - width) / 2, (width - height) / 2 + ascent);
            } else if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.LEFT) {
                AffineTransform trans = new AffineTransform();
                trans.concatenate(oldTransform);
                trans.translate(x,y - 2);
                trans.rotate(Math.PI * 3 / 2, height / 2, width / 2);
                g2d.setTransform(trans);
                ico.paintIcon(c, g2d, (height - width) / 2, (width - height) / 2);
                //g.drawString(text, (int)((height - width) / 2), (int)((width - height) / 2 + ascent));
                //g2d.drawGlyphVector(glyphs, (height - width) / 2, (width - height) / 2 + ascent);
            }
            g2d.setTransform(oldTransform);
            g2d.setRenderingHints(oldHints);
        }
        public void setIcon(Icon ico) {
            this.ico = ico;
        }
        public Icon getIcon() {
            return(ico);
        }
    }
    private class RotatedTextIcon implements Icon {
        private Font font;
        private String text;
        private float width;
        private float height;
        private float ascent;
        private RenderingHints renderHints;
        private GlyphVector glyphs;
        FontRenderContext fontRenderContext = new FontRenderContext(null,true,true);
        public RotatedTextIcon(String text) {
            //this.font = font;
            this.font = JXTabbedPane.this.getFont();
            if (text == null) {
                this.text = "";
            } else {
                this.text = text;
            }
            LineMetrics lineMetrics = font.getLineMetrics(text, fontRenderContext);
            height = (float)lineMetrics.getHeight();
            glyphs = font.createGlyphVector(fontRenderContext,text);
            width = (float)glyphs.getLogicalBounds().getWidth() + 4;
            //width = JXTabbedPane.this.getFontMetrics(font).stringWidth(text);// + 2*kBufferSpace;
            ascent = lineMetrics.getAscent();
            renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        public int getIconWidth() {
            int retorno;
            if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.RIGHT || JXTabbedPane.this.getTabPlacement() == JTabbedPane.LEFT) {
                retorno = (int)height;
            } else {
                retorno = (int)width;
            }
            return retorno;
        }
        public int getIconHeight() {
            int retorno;
            if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.RIGHT || JXTabbedPane.this.getTabPlacement() == JTabbedPane.LEFT) {
                retorno = (int)width;
            } else {
                retorno = (int)height;
            }
            return retorno;
        }
        public float getAscent() {
            return ascent;
        }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setFont(font);
            AffineTransform oldTransform = g2d.getTransform();
            RenderingHints oldHints = g2d.getRenderingHints();
            g2d.setRenderingHints(renderHints);
            g2d.setColor(c.getForeground());
            if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.TOP || JXTabbedPane.this.getTabPlacement() == JTabbedPane.BOTTOM) {
                //g.drawString(text, x + 2, (int)(y + ascent));
                g2d.drawGlyphVector(glyphs,x + 2,y + ascent);
            } if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.RIGHT) {
                AffineTransform trans = new AffineTransform();
                trans.concatenate(oldTransform);
                trans.translate(x,y + 2);
                trans.rotate(Math.PI / 2, height / 2, width / 2);
                g2d.setTransform(trans);
                //g.drawString(text, (int)((height - width) / 2), (int)((width - height) / 2 + ascent));
                g2d.drawGlyphVector(glyphs, (height - width) / 2, (width - height) / 2 + ascent);
            } else if (JXTabbedPane.this.getTabPlacement() == JTabbedPane.LEFT) {
                AffineTransform trans = new AffineTransform();
                trans.concatenate(oldTransform);
                trans.translate(x,y - 2);
                trans.rotate(Math.PI * 3 / 2, height / 2, width / 2);
                g2d.setTransform(trans);
                //g.drawString(text, (int)((height - width) / 2), (int)((width - height) / 2 + ascent));
                g2d.drawGlyphVector(glyphs, (height - width) / 2, (width - height) / 2 + ascent);
            }
            g2d.setTransform(oldTransform);
            g2d.setRenderingHints(oldHints);
        }
        public void setTexto(String text) {
            this.text = text;
        }
        public String getTexto() {
            return(text);
        }
    }
    private class CloseIcon implements Icon {
        private int x_pos;
        private int y_pos;
        private int width;
        private int height;
        private boolean focado;
        public CloseIcon(boolean focado) {
            this.focado = focado;
            width =16;
            height=16;
        }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            this.x_pos=x;
            this.y_pos=y;
            g.setColor(Color.black);
            //Pinta o foco se for o caso...
            //<Just_for_tests>
            //g.setColor(Color.black);
            //g.drawRect(x - 2, y - 2, 18, 18);
            //</Just_for_tests>
            if (focado) {
                BevelBorder borda = new BevelBorder(BevelBorder.RAISED);
                borda.paintBorder(c, g, x, y, 16, 16);
            }
            if (JXTabbedPane.this.closeIconLayout == JXTabbedPane.BOXED_RED_ICON) {
                g.setColor(Color.red);
                g.fillRect(x + 2, y + 2, 11, 11);
                g.setColor(Color.white);
                g.drawRect(x + 2, y + 2, 11, 11);
            } else if (JXTabbedPane.this.closeIconLayout == JXTabbedPane.BOXED_ICON) {
                g.setColor(Color.black);
                g.drawRect(x + 2, y + 2, 11, 11);
            }
            g.drawLine(x + 4,  y + 4, x + 11, y + 11);
            g.drawLine(x + 4,  y + 5, x + 10, y + 11);
            g.drawLine(x + 5,  y + 4, x + 11, y + 10);
            g.drawLine(x + 11, y + 4, x +  4, y + 11);
            g.drawLine(x + 11, y + 5, x +  5, y + 11);
            g.drawLine(x + 10, y + 4, x +  4, y + 10);
        }
        public int getIconWidth() {
            return width;
        }
        public int getIconHeight() {
            return height;
        }
        public Rectangle getBounds() {
            return new Rectangle(x_pos, y_pos, width, height);
        }
        public void setFocado(boolean focado) {
            this.focado = focado;
        }
        public boolean isFocado() {
            return(focado);
        }
    }
}
