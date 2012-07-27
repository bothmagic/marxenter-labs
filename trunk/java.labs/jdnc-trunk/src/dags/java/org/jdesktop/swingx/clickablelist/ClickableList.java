package org.jdesktop.swingx.clickablelist;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.FilterPipeline;

/**
 *
 * @author  dags
 */

public class ClickableList extends org.jdesktop.swingx.JXPanel implements ActionListener, MouseListener {
    
    private static JXList lista = null;
    private String text1 = "%d/%d";
    private String text2 = "%d/%d";
    
    protected EventListenerList listenerList = new EventListenerList();
    
    private int selected = 0;
    private int deselected = 0;
    
    private String title              = "ClickableList Title";
    private JPopupMenu menu             = null;
    private JMenuItem selectOption      = null;
    private JMenuItem deselectOption    = null;
    private JMenuItem toggleOption      = null;
    private JMenuItem selectAllOption   = null;
    private JMenuItem deselectAllOption = null;
    
    private boolean flag = true;
    
    private ClickableListModel gmodel = null;
    
    /** Creates new form CList */
    public ClickableList() {
        initComponents();
        
        menu = new JPopupMenu();
        selectOption      = new JMenuItem(java.util.ResourceBundle.getBundle("org/jdesktop/swingx/clickablelist/ClickableList").getString("Select_Selected_Items")  , new ImageIcon( getClass().getResource("/org/jdesktop/swingx/clickablelist/checkbox-on.png")));;
        deselectOption    = new JMenuItem(java.util.ResourceBundle.getBundle("org/jdesktop/swingx/clickablelist/ClickableList").getString("Deselect_Selected_Items"), new ImageIcon( getClass().getResource("/org/jdesktop/swingx/clickablelist/checkbox-off.png")));
        toggleOption      = new JMenuItem(java.util.ResourceBundle.getBundle("org/jdesktop/swingx/clickablelist/ClickableList").getString("Toggle_Selected_Items")  , new ImageIcon( getClass().getResource("/org/jdesktop/swingx/clickablelist/checkbox-off.png")));
        selectAllOption   = new JMenuItem(java.util.ResourceBundle.getBundle("org/jdesktop/swingx/clickablelist/ClickableList").getString("Select_ALL_Items")       , new ImageIcon( getClass().getResource("/org/jdesktop/swingx/clickablelist/checkall.png")));
        deselectAllOption = new JMenuItem(java.util.ResourceBundle.getBundle("org/jdesktop/swingx/clickablelist/ClickableList").getString("Deselect_ALL_Items")     , new ImageIcon( getClass().getResource("/org/jdesktop/swingx/clickablelist/uncheckall.png")));
        
        selectOption.addActionListener(this);
        deselectOption.addActionListener(this);
        toggleOption.addActionListener(this);
        selectAllOption.addActionListener(this);
        deselectAllOption.addActionListener(this);
        
        // register this class as an SPACE action manager in cList. Used to toggle check state with SPACE tab when cList is focused
        cList.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JComponent.WHEN_FOCUSED);
        
//        // use datatip.dev.java.net to show better tooltips ...
//        DataTipManager.get().register(cList);
        
        // configure popup menu ...
        menu.add(toggleOption);
        menu.add(new JSeparator());
        menu.add(selectOption);
        menu.add(deselectOption);
        menu.add(new JSeparator());
        menu.add(selectAllOption);
        menu.add(deselectAllOption);
        
        selectAllButton.addActionListener(this);
        deselectAllButton.addActionListener(this);
        
        cList.addMouseListener(this);
        
    }
    
    public int convertIndexToModel(int viewIndex) {
        return cList.convertIndexToModel(viewIndex);
    }
    
    public int convertIndexToViewl(int modelIndex) {
        return cList.convertIndexToView(modelIndex);
    }
    
    public int getRowCount() {
        return cList.getModel().getSize();
    }
    
    public FilterPipeline getFilters() {
        return cList.getFilters();
    }
    
    public void setFilters(FilterPipeline pipeline) {
        cList.setFilterEnabled(true);
        cList.setFilters(pipeline);
        countSelected();
    }
    
    
    public void setModel(ClickableListModel modelo) {
        gmodel = modelo;
        cList.setModel(modelo);
        cList.repaint();
        countSelected();
        fireClickableListChanged();
        
    }
    
    public ListModel getModel() {
        return cList.getModel();
    }
    
    public void setTitle(String title) {
        
        this.title = title;
        
        Border tBorder  = new CompoundBorder(
                new EmptyBorder(2,2,2,2), new CompoundBorder(
                new TitledBorder(title), new EmptyBorder(2,2,2,2) )
                );
        this.setBorder(tBorder);
        
    }
    
    public String getTitle() {
        return title;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane2 = new javax.swing.JScrollPane();
        cList = new org.jdesktop.swingx.clickablelist.ClickableBaseList();
        jPanel1 = new javax.swing.JPanel();
        deselectAllButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("ClickableList"), javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2))));
        jScrollPane2.setViewportView(cList);

        add(jScrollPane2, java.awt.BorderLayout.CENTER);

        deselectAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jdesktop/swingx/clickablelist/uncheckall.png")));
        deselectAllButton.setText("0");
        deselectAllButton.setToolTipText("No se selecciona ning\u00fan elemento");

        selectAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jdesktop/swingx/clickablelist/checkall.png")));
        selectAllButton.setText("0");
        selectAllButton.setToolTipText("Selecciona Todo");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .add(deselectAllButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(selectAllButton)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(selectAllButton)
                    .add(deselectAllButton))
                .addContainerGap())
        );
        add(jPanel1, java.awt.BorderLayout.SOUTH);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    org.jdesktop.swingx.clickablelist.ClickableBaseList cList;
    javax.swing.JButton deselectAllButton;
    javax.swing.JPanel jPanel1;
    javax.swing.JScrollPane jScrollPane2;
    javax.swing.JButton selectAllButton;
    // End of variables declaration//GEN-END:variables
    
    public void mouseReleased(java.awt.event.MouseEvent e) {
        if (e.isPopupTrigger()) {
            menu.show(this, e.getX(), e.getY());
        }
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) {
        
//        int x = e.getX();
//        int y = e.getY();
//        Object os = e.getSource();
//
//        if (os instanceof ClickableBaseList && e.getButton() == 3) {
//
//            int first = cList.getFirstVisibleIndex();
//            int last  = cList.getLastVisibleIndex();
//            for (int i = first; i <= last; i++) {
//                Rectangle re = cList.getCellBounds(i,i);
//                if (re.contains(x, y)) {
//                    cList.setSelectedIndex(i);
//                    int[] cuales = cList.getSelectedIndices();
//                }
//            }
//        }
        
        if (e.isPopupTrigger()) {
            menu.show(cList, e.getX(), e.getY());
        }
    }
    
    public void mouseExited(java.awt.event.MouseEvent e) {
        //System.out.println("mouseExited");
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e) {
        //System.out.println("mouseEntered");
    }
    
    public void mouseClicked(java.awt.event.MouseEvent e) {
        
        ClickableListElement el = null;
        
        // get the index of cList from clicked point
        int index = cList.locationToIndex(e.getPoint());
        
        // get the preferred width of a JCheckBox
        int hotspot = new JCheckBox().getPreferredSize().width;
        
        // no index ? , then return
        if (index < 0) return;
        
        // if mouse click's X position falls outside JCheckBox "hotspot", return
        // becuase mouse was not clicked "inside" JCheckbox
        if (e.getX() > cList.getCellBounds(index, index).x + hotspot) {
            System.out.println("----> not in JCheckBox, nothing done ...");
            return;
        }
        
        // mouse was clicked on JCheckBox, so proceed with check/uncheck
        System.out.println("----> inside JCheckBox, toogle selected item");
        
        el = (ClickableListElement)(cList.getSelectedValue());
        el.toggleSelection();
        countSelected();
        cList.repaint();
        fireClickableListChanged();
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
        // check selected items
        if (e.getSource().equals(selectOption)) {
            int cuales[] = cList.getSelectedIndices();
            for (int j = 0; j < cuales.length; j++) {
                ( (ClickableListElement)gmodel.getElementAt(cuales[j])).setSelected(true);
            }
            countSelected();
            cList.repaint();
            fireClickableListChanged();
        }
        
        // uncheck selected items
        if (e.getSource().equals(deselectOption)) {
            int cuales[] = cList.getSelectedIndices();
            for (int j = 0; j < cuales.length; j++) {
                ( (ClickableListElement)gmodel.getElementAt(cuales[j])).setSelected(false);
            }
            countSelected();
            cList.repaint();
            fireClickableListChanged();
        }
        
        // toggle selected items
        if (e.getSource().equals(toggleOption)) {
            int cuales[] = cList.getSelectedIndices();
            for (int j = 0; j < cuales.length; j++) {
                ClickableListElement el = (ClickableListElement)(gmodel.getElementAt(cuales[j]));
                el.toggleSelection();
            }
            countSelected();
            cList.repaint();
            fireClickableListChanged();
        }
        
        // check all items
        if (e.getSource().equals(selectAllOption)) {
            selectAll();
        }
        
        // uncheck all items
        if (e.getSource().equals(deselectAllOption)) {
            unselectAll();
        }
        
        // check all items, from button
        if (e.getSource().equals(selectAllButton)) {
            selectAll();
        }
        
        // uncheck all items. from button
        if (e.getSource().equals(deselectAllButton)) {
            unselectAll();
        }
        
        
        // if source is cList, button was SPACE
        if (e.getSource().equals(cList)) {
            System.out.println("TOGGLE SPACE");
            int cuales[] = cList.getSelectedIndices();
            for (int j = 0; j < cuales.length; j++) {
                ClickableListElement el = (ClickableListElement)(gmodel.getElementAt(cuales[j]));
                el.toggleSelection();
            }
            countSelected();
            cList.repaint();
            fireClickableListChanged();
        }
    }
    
    public void selectAll() {
        for (int j = 0; j < gmodel.getSize(); j++) {
            ( (ClickableListElement)gmodel.getElementAt(j)).setSelected(true);
        }
        selectAllButton.setText(String.format(text2, cList.getElementCount(), gmodel.size() ) );
        deselectAllButton.setText(String.format(text1, 0, gmodel.getSize()));
        cList.repaint();
        fireClickableListChanged();
    }
    
    public void unselectAll() {
        for (int j = 0; j < gmodel.getSize(); j++) {
            ( (ClickableListElement)gmodel.getElementAt(j)).setSelected(false);
        }
        selectAllButton.setText(String.format(text2, 0, gmodel.getSize()));
        deselectAllButton.setText(String.format(text1, cList.getElementCount(), gmodel.size() ) );
        
        cList.repaint();
        fireClickableListChanged();
    }
    
    private void countSelected() {
        int viewSize  = cList.getElementCount();
        int modelSize = gmodel.getSize();
        
        selected = 0;
        deselected = 0;
        
        for (int j = 0; j < cList.getModel().getSize(); j++) {
            if ( ( (ClickableListElement) cList.getModel().getElementAt(j) ).isSelected() == true) {
                selected++;
            } else {
                deselected++;
            }
        }
        deselectAllButton.setText(String.format(text1, deselected, viewSize));
        selectAllButton.setText(String.format(text2, selected, viewSize));
    }
    
    public void removeClickableListListener(ClickableListListener l) {
        listenerList.remove(ClickableListListener.class, l);
    }
    
    public void addClickableListListener(ClickableListListener l) {
        listenerList.add(ClickableListListener.class, l);
    }
    
    public Object[] getSelectedObjects() {
        return cList.getSelectedValues();
    }
    
    public ArrayList getClickedObjects() {
        return gmodel.getClickedElements();
    }
    
    protected void fireClickableListChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i >= 0; i-= 2) {
            if (listeners[i] == ClickableListListener.class) {
                ((ClickableListListener)listeners[i+1]).ClickableListChanged(this);
            }
        }
    }
    
    public void updateCounts() {
        countSelected();
    }
    
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.selectAllButton.setEnabled(enabled);
        this.deselectAllButton.setEnabled(enabled);
        this.cList.setEnabled(enabled);
        this.cList.setVisible(enabled);
        this.jPanel1.setEnabled(enabled);
        this.jScrollPane2.setEnabled(enabled);
        this.menu.setEnabled(enabled);
    }
    
    public ListSelectionModel getSelectionModel() {
        return cList.getSelectionModel();
    }
}
