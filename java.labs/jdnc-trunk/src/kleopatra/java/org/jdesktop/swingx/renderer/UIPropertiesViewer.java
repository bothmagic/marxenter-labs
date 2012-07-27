/*
 * Created on 23.03.2009
 *
 */
package org.jdesktop.swingx.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.sort.RowFilters.GeneralFilter;

//import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class UIPropertiesViewer extends InteractiveTestCase{

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(UIPropertiesViewer.class
            .getName());
    
    public static void main(String[] args) throws Exception {
        try {
//            LookAndFeelInfo info = new LookAndFeelInfo("GTK plain",
//                    "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//            UIManager.installLookAndFeel(info);
//        setLookAndFeel("GTK");
            UIPropertiesViewer test = new UIPropertiesViewer();
//            test.runInteractiveTests();
            test.interactiveUIIcons();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void interactiveUIIcons() {
        final JXTable table = new JXTable(getUIPropertiesTableModel());
        
        table.setAutoCreateColumnsFromModel(false);
        table.setColumnControlVisible(true);
        
        final UIIconValue iv = new UIIconValue();
        PropertyChangeListener l = new PropertyChangeListener(){
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                iv.clear();
                table.setModel(getUIPropertiesTableModel());
                updateRowHeight(table, true);
            }};
        UIManager.addPropertyChangeListener(l);
        StringValue sv = new StringValue() {

            @Override
            public String getString(Object value) {
                if (!(value instanceof Icon)) return "";
                Icon icon = (Icon) value;
                return "width = " + icon.getIconWidth() + ", " + "height = " + icon.getIconHeight();
            }
            
        };
        TableCellRenderer renderer  = new DefaultTableRenderer(new MappedValue(sv, iv));
        table.getColumnModel().getColumn(1).setCellRenderer(renderer);
        table.setDefaultRenderer(Icon.class, renderer);
        table.setDefaultRenderer(ImageIcon.class, renderer);
        updateRowHeight(table, false);
        table.packAll();
        final JXFrame frame = wrapWithScrollingInFrame(table, "UI Properties ");
        Action iconOnly = new AbstractAction("filter icons") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                final RowFilter<Object, Object> filter = new IconTypeFilter(1);
                table.setRowFilter(filter);
            }
            
        };
        addAction(frame, iconOnly);
        Action colorOnly = new AbstractAction("filter colors") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                final RowFilter<Object, Object> filter = new ColorTypeFilter(1);
                table.setRowFilter(filter);
            }
            
        };
        addAction(frame, colorOnly);
        Action borderOnly = new AbstractAction("filter borders") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                final RowFilter<Object, Object> filter = new BorderTypeFilter(1);
                table.setRowFilter(filter);
            }
            
        };
        addAction(frame, borderOnly);
        Action reset = new AbstractAction("remove filter") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                table.setRowFilter(null);
            }
            
        };
        addAction(frame, reset);
        show(frame, 1200, 800);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
//                    setLookAndFeel("Windows");
//                    SwingUtilities.updateComponentTreeUI(frame);
                    
                    table.requestFocus();
                } catch (Exception e) {
                }    
            }
        });
    }
    
    
    /**
     * Keeps a cache of SafeIcon wrappers around all values ever processed. 
     */
    public static class UIIconValue implements IconValue {
        Map<Icon, SafeIcon> iconCache = new HashMap<Icon, SafeIcon>();

        @Override
        public Icon getIcon(Object value) {
            if (!(value instanceof Icon)) return null;
            if (iconCache.get(value) == null) {
                iconCache.put((Icon) value, new SafeIcon((Icon) value));
            }
            return iconCache.get(value);
        }

        public void clear() {
            iconCache.clear();
        }
        
    }
    
    /**
     * Some ui-icons misbehave in that they unconditionally class-cast to the 
     * component type they are mostly painted on. Consequently they blow up if 
     * we are trying to paint them anywhere else (f.i. in a renderer).  
     * 
     * This Icon is an adaption of a cool trick by Darryl Burke/Rob Camick found at
     * http://tips4java.wordpress.com/2008/12/18/icon-table-cell-renderer/#comment-120
     * 
     * The base idea is to instantiate a component of the type expected by the icon, 
     * let it paint into the graphics of a bufferedImage and create an ImageIcon from it.
     * In subsequent calls the ImageIcon is used. 
     * 
     */
    public static class SafeIcon implements Icon {
        
        private Icon wrappee;
        private Icon standIn;
        
        public SafeIcon(Icon wrappee) {
            this.wrappee = wrappee;
        }

        @Override
        public int getIconHeight() {
            return wrappee.getIconHeight();
        }

        @Override
        public int getIconWidth() {
            return wrappee.getIconWidth();
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (standIn == this) {
                paintFallback(c, g, x, y);
            } else if (standIn != null) {
                standIn.paintIcon(c, g, x, y);
            } else {
                try {
                   wrappee.paintIcon(c, g, x, y); 
                } catch (ClassCastException e) {
                    createStandIn(e, x, y);
                    standIn.paintIcon(c, g, x, y);
                }
            }
        }

        /**
         * @param e
         */
        private void createStandIn(ClassCastException e, int x, int y) {
            try {
                Class<?> clazz = getClass(e);
                JComponent standInComponent = getSubstitute(clazz);
                standIn = createImageIcon(standInComponent, x, y);
            } catch (Exception e1) {
                // something went wrong - fallback to this painting
                standIn = this;
            } 
        }

        private Icon createImageIcon(JComponent standInComponent, int x, int y) {
            BufferedImage image = new BufferedImage(getIconWidth(),
                    getIconHeight(), BufferedImage.TYPE_INT_ARGB);
              Graphics g = image.createGraphics();
              try {
                  wrappee.paintIcon(standInComponent, g, 0, 0);
                  return new ImageIcon(image);
              } finally {
                  g.dispose();
              }
        }

        /**
         * @param clazz
         * @throws IllegalAccessException 
         */
        private JComponent getSubstitute(Class<?> clazz) throws IllegalAccessException {
            JComponent standInComponent;
            try {
                standInComponent = (JComponent) clazz.newInstance();
            } catch (InstantiationException e) {
                standInComponent = new AbstractButton() {
                    
                };
                ((AbstractButton) standInComponent).setModel(new DefaultButtonModel());
            } 
            return standInComponent;
        }
        
        private Class<?> getClass(ClassCastException e) throws ClassNotFoundException {
            String className = e.getMessage();
            className = className.substring(className.lastIndexOf(" ") + 1);
            return Class.forName(className);
            
        }

        private void paintFallback(Component c, Graphics g, int x, int y) {
            g.drawRect(x, y, getIconWidth(), getIconHeight());
            g.drawLine(x, y, x + getIconWidth(), y + getIconHeight());
            g.drawLine(x + getIconWidth(), y, x, y + getIconHeight());
        }
        
    }
    
    public static class IconTypeFilter extends GeneralFilter {

        public IconTypeFilter(int... i) {
            super(i);
        }

        @Override
        protected boolean include(Entry<? extends Object, ? extends Object> entry,
                int index) {
            return entry.getValue(index) instanceof Icon;
        }
    }
    
    public static class ColorTypeFilter extends GeneralFilter {
        String[] raw = {"NimbusProperty", "DesktopProperty", };
        List<String> wrappers = Arrays.asList(raw);
        public ColorTypeFilter(int... i) {
            super(i);
        }
        
        @Override
        protected boolean include(
                Entry<? extends Object, ? extends Object> entry,
                int index) {
            Object value = entry.getValue(index);
            if (value == null) return false;
            return (value instanceof Color) || wrappers.contains(value.getClass().getSimpleName()) ;
        }
        
    }
    
    public static class BorderTypeFilter extends GeneralFilter {

        public BorderTypeFilter(int... i) {
            super(i);
        }

        @Override
        protected boolean include(
                Entry<? extends Object, ? extends Object> entry,
                int index) {
            Object value = entry.getValue(index);
            if (value == null) return false;
            return value instanceof Border;
        }
        
    }
    private TableModel getUIPropertiesTableModel() {
        UIDefaults values = UIManager.getDefaults();
        // force instantiation of lazyValues
        // NOTE: can't use keySet() because it's not overridden by MultiUIDefaults
        Enumeration<Object> e = values.keys();
        while (e.hasMoreElements()) {
            Object nextElement = e.nextElement();
            Object value = values.get(nextElement);
            if (value instanceof Border) {
                String clazz = value.getClass().getName();
                if (clazz.contains("com.sun"))
                    LOG.info("" + nextElement + value.getClass());
                if (nextElement.toString().contains("Tree."))
                    LOG.info("" + nextElement + value.getClass());
                
            }
        }
        final List<Entry<Object, Object>> entries = new ArrayList<Entry<Object, Object>>(values.entrySet());
        TableModel model = new AbstractTableModel() {

            @Override
            public int getColumnCount() {
                return 5;
            }

            @Override
            public int getRowCount() {
                return entries.size();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                switch (columnIndex) {
                case 0:
                    return entries.get(rowIndex).getKey();
                case 1: 
                    return entries.get(rowIndex).getValue();
                case 2: 
                    return entries.get(rowIndex).getValue() instanceof UIResource;
                case 3:
                    Object value = entries.get(rowIndex).getValue();
                    return value != null ? value.getClass().getSimpleName() : null;
                case 4:
                    Object other = entries.get(rowIndex).getValue();
                    return other != null ? other.getClass().getName() : null;
                default:
                    return null; 
                }
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            
        };
        return model;
    }

    /**
     * Adjusts the row height for visible rows to pref of cells.
     * 
     * @param table
     */
    protected void updateRowHeight(final JTable table, boolean invoke) {
        if (invoke) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                updateRowHeight(table);
            }
        });
        } else {
            updateRowHeight(table);
        }
    }

    /**
     * @param table
     */
    protected void updateRowHeight(final JTable table) {
        for (int row = 0; row < table.getRowCount(); row++) {
            int rowHeight = table.getRowHeight();
            for (int column = 0; column < table.getColumnCount(); column++) {
                Component comp = table.prepareRenderer(table
                        .getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight,
                        comp.getPreferredSize().height);
            }
            table.setRowHeight(row, rowHeight);
        }
    }
    
    
}
