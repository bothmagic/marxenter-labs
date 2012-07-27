/*
 * Created on 21.02.2008
 *
 */
package kschaefe.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import kschaefe.renderer.TernaryLogicProvider.XMarkIcon;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;

public class LogicExperiments extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(LogicExperiments.class
            .getName());
    public static void main(String args[]) {
         setSystemLF(true);
        LogicExperiments test = new LogicExperiments();
        try {
             test.runInteractiveTests();
            // test.runInteractiveTests(".*Wrapping.*");
            test.runInteractiveTests(".*File.*");
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }
    }

    public void interactiveCheckBoxProvider() {
        JXTable table = new JXTable(createMixedTableModel());
        ComponentProvider<?> provider = new CheckBoxProvider() {
            @Override
            protected void configureState(CellContext context) {
                rendererComponent.setEnabled(context.isEditable());
                super.configureState(context);
            }
            
        };
        table.getColumnExt(0).setCellRenderer(new DefaultTableRenderer(provider ));
        table.getColumnExt(1).setCellRenderer(new DefaultTableRenderer(new CheckBoxProvider()));
        showWithScrollingInFrame(table, "CheckBox disabled on first column");
        
    }

    public void interactiveCheckBoxProviderCustomIconInState() {
        JXTable table = new JXTable(createMixedTableModel());
        ComponentProvider<?> provider = new CheckBoxProvider() {
            Icon active = new CustomIcon(true);
            Icon inactive = new CustomIcon(false);
            @Override
            protected void configureState(CellContext context) {
                rendererComponent.setIcon(context.isEditable()? active : inactive);
                super.configureState(context);
            }
            
            
        };
        table.getColumnExt(0).setCellRenderer(new DefaultTableRenderer(provider ));
        table.getColumnExt(1).setCellRenderer(new DefaultTableRenderer(new CheckBoxProvider()));
        showWithScrollingInFrame(table, "CheckBox custom in/active icon");
    }

    public void interactiveCheckBoxProviderCustomIcon() {
        JXTable table = new JXTable(createMixedTableModel());
        ComponentProvider<?> provider = new CheckBoxProvider() {

            @Override
            protected AbstractButton createRendererComponent() {
                AbstractButton button =  super.createRendererComponent();
                button.setIcon(new CustomIcon(false));
                return button;
            }
            
        };
        table.getColumnExt(0).setCellRenderer(new DefaultTableRenderer(provider ));
        table.getColumnExt(1).setCellRenderer(new DefaultTableRenderer(new CheckBoxProvider()));
        showWithScrollingInFrame(table, "CheckBox custom icon");
    }
    
    /**
     * Custom boolean icon.
     */
    public static class CustomIcon implements Icon {
        private static final int SIZE = 13;
        private boolean active;

        public CustomIcon() {
            this(true);
        }
        public CustomIcon(boolean active) {
            this.active = active;
        }
        /**
         * {@inheritDoc}
         */
        public int getIconHeight() {
            return SIZE;
        }

        /**
         * {@inheritDoc}
         */
        public int getIconWidth() {
            return SIZE;
        }

        public void paintIcon(Component c, Graphics g2d, int x, int y) {
            boolean checked = c instanceof AbstractButton ? 
                    ((AbstractButton) c).isSelected() : false;
            Color old = g2d.getColor();
            g2d.setColor(UIManager.getColor("CheckBox.shadow")); //$NON-NLS-1$
            g2d.drawRect(x, y, getIconWidth() - 1, getIconHeight() - 1);
            if (active) {
                g2d.setColor(UIManager.getColor("CheckBox.interiorBackground")); //$NON-NLS-1$
            } else {
                g2d.setColor(Color.GRAY.brighter());
            }

            g2d.fillRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
            if (checked) {
                g2d.setColor(Color.MAGENTA); //$NON-NLS-1$
                g2d.drawLine(x + 3, y + 3, x + 10, y + 10);
                g2d.drawLine(x + 3, y + 10, x + 10, y + 3);
            }
            g2d.setColor(old);
        }
    }

    public void interactiveTernaryCheckBoxProvider() {
        JXTable table = new JXTable(createMixedTableModel());
        ComponentProvider<?> provider = new CheckBoxProvider() {
            final Icon selectedIcon = new XMarkIcon();

            @Override
            protected void configureState(CellContext context) {
                if (context.isEditable()) {
                    rendererComponent.setIcon(null);
                    rendererComponent.setSelectedIcon(null);
                } else {
                    rendererComponent.setIcon(selectedIcon);
                    rendererComponent.setSelectedIcon(selectedIcon);
                }
                super.configureState(context);
            }
            
        };
        table.getColumnExt(0).setCellRenderer(new DefaultTableRenderer(provider ));
        table.getColumnExt(1).setCellRenderer(new DefaultTableRenderer(new CheckBoxProvider()));
        showWithScrollingInFrame(table, "CheckBoxProviderSubclass on first column");
        
    }
    
    public void interactiveTernary() {
        JXTable table = new JXTable(createMixedTableModel());
        table.getColumnExt(0).setCellRenderer(new DefaultTableRenderer(new TernaryLogicProvider()));
        table.getColumnExt(1).setCellRenderer(new DefaultTableRenderer(new CheckBoxProvider()));
        showWithScrollingInFrame(table, "Ternary on first column");
    }


    private TableModel createMixedTableModel() {
        DefaultTableModel model = new DefaultTableModel(10, 2) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return row % 2 == 0;
            }
            
        };
        for (int i = 0; i < model.getRowCount(); i++) {
            int group = i % 3;
            switch (group) {
            case 0:
                model.setValueAt("dummy", i, 0);
                model.setValueAt("dummy", i, 1);
                break;

            case 1:
                model.setValueAt(true, i, 0);
                model.setValueAt(true, i, 1);
                break;
            case 2:
                model.setValueAt(false, i, 0);
                model.setValueAt(false, i, 1);
                break;
            default:
                break;
            }
            
        }
        return model;
    }
}
