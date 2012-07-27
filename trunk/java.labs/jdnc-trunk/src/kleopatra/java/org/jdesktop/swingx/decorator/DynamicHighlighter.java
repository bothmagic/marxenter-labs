/*
 * Created on 28.03.2008
 *
 */
package org.jdesktop.swingx.decorator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.decorator.ComponentAdapter;

/**
 * The SwingX way of life: variant of Thierry Lefort's "one view for all"
 * 
 * http://www.jroller.com/Thierry/entry/swingx_one_view_to_rule.
 * 
 * The actual control is moved to HighlightController which manages an internal
 * CompoundHighlighter and exposes methods to update colors, predicates ... 
 * 
 */
public class DynamicHighlighter {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(DynamicHighlighter.class
            .getName());
    RandomTableModel model;

    private Map<String, Action> actions;
    
    HighlightController controller;
    
    public DynamicHighlighter() {
        controller = new HighlightController();
        initData();
        initActions();
    }
    
    /**
     * Action callback: add background highlight with color for cells of view column 
     * which match any of the given values. Depending on the allcolumns flag, the cell-only
     * or the complete row is highlighted.
     * 
     */
    protected void addHighlighter(JXTable table, int viewColumn,
            Collection<Object> selectedValues, boolean allColumns, Color color) {
        Object identifier = table.getColumn(viewColumn).getIdentifier();
        controller.addHighlighter(identifier, selectedValues, allColumns, color);
    }

    
    /**
     * Action callback: add background highlight with color for cells of view column 
     * which is in the range between first/last. Depending on the allcolumns flag, the cell-only
     * or the complete row is highlighted.
     * 
     */
    protected void addHighlighter(JXTable table, int viewColumn, Object first,
            Object last, boolean allColumns, Color color) {
        Object identifier = table.getColumn(viewColumn).getIdentifier();
        controller.addHighlighter(identifier, (Comparable) first, (Comparable) last, allColumns, color);
    }

    /**
     * Creates and returns an action to choose a color for highlighting
     * the cells containing the selected values in the given column.
     */
    private Action createValueMappedHighlightAction(final JXTable table, 
            final int viewColumn, final boolean allColumns) {
        String scope = "cells";
        if (allColumns) {
            scope = "lines";
        } 
        int[] rows = table.getSelectedRows();
        final TreeSet<Object> selectedValues = new TreeSet<Object>();
        for (int i = 0; i < rows.length; i++) {
            selectedValues.add(table.getValueAt(rows[i], viewColumn));
        }
        String name = "Highlight " + scope + " in "
                        + table.getColumnName(viewColumn) 
            + " = " + selectedValues 
            ;
            
        Action action = new AbstractActionExt(name) {

            public void actionPerformed(ActionEvent e) {
                Color color = showColorChooser(table);
                if (color != null) {
                    addHighlighter(table, viewColumn, selectedValues, allColumns, color);
                }
                
            }
        };
        return action;
    }

    /**
     * Creates and returns an action to choose a color for highlighting
     * the cells containing the selected values in the given column.
     */
    private Action createRangeHighlightAction(final JXTable table, 
            final int viewColumn, final boolean allColumns) {
        String scope = "cells";
        if (allColumns) {
            scope = "lines";
        } 
        int[] rows = table.getSelectedRows();
        final TreeSet<Object> selectedValues = new TreeSet<Object>();
        for (int i = 0; i < rows.length; i++) {
            selectedValues.add(table.getValueAt(rows[i], viewColumn));
        }
        String name = "Highlight " + scope + " in "
                        + table.getColumnName(viewColumn) 
            + " between " + selectedValues.first()+ " and " + selectedValues.last(); 
            ;
            
        Action action = new AbstractActionExt(name) {

            public void actionPerformed(ActionEvent e) {
                Color color = showColorChooser(table);
                if (color != null) {
                    addHighlighter(table, viewColumn, selectedValues.first(), selectedValues.last(), allColumns, color);
                }
                
            }

        };
        return action;
    }


    /**
     * Action callback: removes all highlighters.
     */
    protected void resetHighlighters() {
        controller.resetHighlighters();
        
    }

    /**
     * Creates and stores standard (not-context dependent) actions.
     */
    private void initActions() {
        actions = new HashMap<String, Action>();
        Action action = new AbstractActionExt("Reset Highlighters") {

            public void actionPerformed(ActionEvent e) {
                resetHighlighters();
                
            }
            
        };
        actions.put("removeHighlighters", action);
        
    }


    private JXList createList(ListModel model, Object identifier) {
        JXList list = new JXXList();
        list.setModel(model);
        list.putClientProperty("columnIdentifier", identifier);
        list.setName(identifier.toString());
        controller.addHighlightable(list);
        return list;
    }
    
    /**
     * Overridden to allow configurable column identifier.
     */
    public static class JXXList extends JXList {
        
        
        @Override
        protected ComponentAdapter getComponentAdapter() {
            if (dataAdapter == null) {
                dataAdapter = new XAdapter(this);
            }
            return dataAdapter;
        }

        protected static class XAdapter extends ListAdapter {
            public XAdapter(JXList list) {
                super(list);
            }

            @Override
            public Object getColumnIdentifierAt(int columnIndex) {
                Object id = getComponent().getClientProperty("columnIdentifier");
                return id != null ? id : super.getColumnIdentifierAt(columnIndex);
            }
            
        }
    }
    
    /**
     * Creates and shows a context-dependent popup with actions to add higlighters
     * based on the currently selected values.
     * 
     */
    protected void showPopup(Component component, Point loc) {
        if (!(component instanceof JXTable)) return;
        JXTable table = (JXTable) component;
        if (table.getSelectionModel().isSelectionEmpty()) return;
        int viewRow = table.rowAtPoint(loc);
        int viewColumn = table.columnAtPoint(loc);
        if (viewRow < 0 || viewColumn < 0) return;
        JPopupMenu popup = new JPopupMenu();
        
        JMenuItem menu = createMenuItems(table, viewColumn);
        popup.add(menu);
        popup.show(component, loc.x, loc.y);
    }

    private JMenuItem createMenuItems(JXTable table, int viewColumn) {
        JMenu menu = new JMenu("Highlight");
        menu.add(createValueMappedHighlightAction(table, viewColumn, false));
        menu.add(createValueMappedHighlightAction(table, viewColumn, true));
        menu.addSeparator();
        menu.add(createRangeHighlightAction(table, viewColumn, false));
        menu.add(createRangeHighlightAction(table, viewColumn, true));
        return menu;
    }
    
    private JXTable createTable(TableModel model) {
        final JXTable table = new JXTable();
        table.setModel(model);
        controller.addHighlightable(table);
        table.addMouseListener(new PopupListener());
        table.setColumnControlVisible(true);
        table.packAll();
        return table;
    }



    class PopupListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e.getComponent(),
                           e.getPoint());
            }
        }
    }

    //------------ color choosing
    
    private Color showColorChooser(Component comp) {
        return JColorChooser.showDialog(JOptionPane.getFrameForComponent(comp),
                "Select Color", getRandomColor());
    }

    private Color getRandomColor() {
        int r = (int) Math.floor(Math.random() * 256);
        int v = (int) Math.floor(Math.random() * 256);
        int b = (int) Math.floor(Math.random() * 256);
        int a = (int) Math.floor(Math.random() * 256);
        return new Color(r, v, b);
    }

// ------------- stanadard init/creation
    
    private void initData() {
        model = new RandomTableModel(200);
    }


    private Component createWestPanel(JXList... namesList) {
        JXTaskPaneContainer container = new JXTaskPaneContainer();
        for (JXList list : namesList) {
            JXTaskPane cityPane = new JXTaskPane();
            cityPane.setTitle(list.getName());
            cityPane.add(list);
            container.add(cityPane);
        }
        return container;
    }


    protected JComponent createContent() {
        JXPanel panel = new JXPanel(new BorderLayout());
        JXTable table = createTable(model);
        panel.add(new JScrollPane(table));
        JXList namesList = createList(namesModel, "Name");
        JXList cityList = createList(citiesModel, "City");
        panel.add(createWestPanel(namesList, cityList), BorderLayout.WEST);
        JComponent buttonBar = new JXPanel(new GridLayout());
        for (Action action : actions.values()) {
            buttonBar.add(new JButton(action));
        }
        JXPanel south = new JXPanel();
        south.add(buttonBar);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }


//-------------- models
    
    private ListModel namesModel = new AbstractListModel() {
        public int getSize() {
            return RandomTableModel.contactValues.length;
        }

        public Object getElementAt(int index) {
            return RandomTableModel.contactValues[index];
        }
    };

    private ListModel citiesModel = new AbstractListModel() {
        public int getSize() {
            return RandomTableModel.cityValues.length;
        }

        public Object getElementAt(int index) {
            return RandomTableModel.cityValues[index];
        }
    };

    private static class RandomTableModel extends AbstractTableModel {
        public static final String NAMES = "Name";
        public static final String CITY = "City";

        private static String[] contactValues = { "Jeanette", "Kirill",
                "Bloid", "Alamray", "Romain" };

        private static String[] cityValues = { "Paris", "Lyon", "Marseille",
                "Avignon", "Nice", "Cannes" };

        private static String getRandomName() {
            int index = (int) (Math.floor(Math.random() * contactValues.length) % contactValues.length);
            return contactValues[index];
        }

        private static String getRandomCity() {
            int index = (int) (Math.floor(Math.random() * cityValues.length) % cityValues.length);
            return cityValues[index];
        }

        private int _rowCount;

        private Object[][] _data;

        RandomTableModel(int rowCount) {
            _rowCount = rowCount;
            _data = new Object[getColumnCount()][_rowCount];
            for (int i = 0; i < _data.length; i++) {
                for (int j = 0; j < _data[i].length; j++) {
                    if (getColumnClass(i) == String.class) {
                        _data[i][j] = i == 0 ? getRandomName()
                                : getRandomCity();
                    } else {
                        _data[i][j] = new Double(Math.random() * 100);
                    }
                }

            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return (columnIndex % 2 == 0) ? String.class : Double.class;
        }

        public int getColumnCount() {
            return 4;
        }

        public int getRowCount() {
            return _rowCount;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return _data[columnIndex][rowIndex];
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
            case 0:
                return NAMES;
            case 1:
                return "Number 1";
            case 2:
                return CITY;
            case 3:
                return "Number 2";

            default:
                break;
            }
            return "Column " + column;
        }

    }

//-------------- main
    
    public static void main(String[] args) {
        setupLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JXFrame frame = new JXFrame(
                        "Highlighters :: Advanced Customization", true);
                frame.add(new DynamicHighlighter().createContent());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    

    private static void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}
