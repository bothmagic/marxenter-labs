/*
 * Created on 22.11.2010
 *
 */
package org.jdesktop.swingx.painter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate.ColumnHighlightPredicate;

public class MetalExperiments extends InteractiveTestCase {

    
    public void interactiveToggleTheme() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        RubyTheme rubyTheme = new RubyTheme();
        MetalLookAndFeel.setCurrentTheme(rubyTheme);
        JTabbedPane tabbedPane = new JTabbedPane();
        JXTable ruby = createTable(rubyTheme.getPrimary1(), rubyTheme.getPrimary2(), rubyTheme.getPrimary3(),
                rubyTheme.getSecondary1(), rubyTheme.getSecondary2(), rubyTheme.getSecondary3()
                
        );
        
        tabbedPane.addTab("Ruby", new JScrollPane(ruby));
        
        OceanThemeT oceanTheme = new OceanThemeT();
        JXTable ocean = createTable(
                oceanTheme.getPrimary1(), oceanTheme.getPrimary2(), oceanTheme.getPrimary3(),
                oceanTheme.getSecondary1(), oceanTheme.getSecondary2(), oceanTheme.getSecondary3()
                );
        tabbedPane.addTab("Ocean", new JScrollPane(ocean));
        JPanel panel = new JPanel();
        final JLabel label = new JLabel("ohahaljodfoejr jkldjfödlsafjöasdlkf));");
        label.setOpaque(true);
        panel.add(label);
        tabbedPane.addTab("label", panel);
        panel = new JPanel();
        panel.add(new JTextField(30));
        tabbedPane.addTab("text", panel);		
        
        JXFrame frame = wrapInFrame(tabbedPane, "MetalThemes");
        Action toggle = new AbstractAction("toggle theme") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(UIManager.getLookAndFeel() instanceof MetalLookAndFeel)) return;
                MetalTheme current = MetalLookAndFeel.getCurrentTheme();
                MetalTheme theme = !"Ocean".equals(current.getName()) ?
                        new OceanTheme() : new RubyTheme();
                MetalLookAndFeel.setCurrentTheme(theme);
                setLAF("Metal");
//                UIManager.setLookAndFeel(new MetalLookAndFeel());
                SwingXUtilities.updateAllComponentTreeUIs();
                label.setBackground(UIManager.getColor("control"));
            }
        };
        addAction(frame, toggle);
        addStatusComponent(frame, new JButton("something to show?"));
        show(frame);
    }

    private JXTable createTable(Color... colors) {
        JXTable table = new JXTable(40, 7);
        for (int i = 0; i < Math.min(colors.length, table.getColumnCount()); i++) {
            
        table.addHighlighter(
                new ColorHighlighter(new ColumnHighlightPredicate(i),colors[i], null));
        }
        JPopupMenu menu = new JPopupMenu();
        menu.add(new JCheckBoxMenuItem("doit"));
        menu.add(new JCheckBoxMenuItem("oterh  "));
        menu.add(new JCheckBoxMenuItem("yet really"));
        menu.add(new JCheckBoxMenuItem("dije"));
        table.setComponentPopupMenu(menu);
        return table;
    }
    
    public class OceanThemeT extends OceanTheme {

        /** 
         * @inherited <p>
         */
        @Override
        public ColorUIResource getPrimary1() {
            return super.getPrimary1();
        }

        /** 
         * @inherited <p>
         */
        @Override
        public ColorUIResource getPrimary2() {
            return super.getPrimary2();
        }

        /** 
         * @inherited <p>
         */
        @Override
        public ColorUIResource getPrimary3() {
            return super.getPrimary3();
        }

        /** 
         * @inherited <p>
         */
        @Override
        public ColorUIResource getSecondary1() {
            return super.getSecondary1();
        }

        /** 
         * @inherited <p>
         */
        @Override
        public ColorUIResource getSecondary2() {
            return super.getSecondary2();
        }

        /** 
         * @inherited <p>
         */
        @Override
        public ColorUIResource getSecondary3() {
            return super.getSecondary3();
        }
        
    }
    public class RubyTheme extends DefaultMetalTheme {
        
        public String getName() { return "Ruby"; }
     
        private final ColorUIResource primary1 = new ColorUIResource(80, 10, 22);
        private final ColorUIResource primary2 = new ColorUIResource(193, 10, 44);
        private final ColorUIResource primary3 = new ColorUIResource(244, 10, 66); 
     
        @Override
        public ColorUIResource getPrimary1() { return primary1; }  
        @Override
        public ColorUIResource getPrimary2() { return primary2; } 
        @Override
        public ColorUIResource getPrimary3() { return primary3; }
        /** 
         * @inherited <p>
         */
        @Override
        public ColorUIResource getSecondary1() {
            return super.getSecondary1();
        }
        /** 
         * @inherited <p>
         */
        @Override
        public ColorUIResource getSecondary2() {
            return super.getSecondary2();
        }
        /** 
         * @inherited <p>
         */
        @Override
        public ColorUIResource getSecondary3() {
            return new ColorUIResource(Color.PINK);//.getSecondary3();
        } 
        
        
    }

    public static void main(String[] args) {
        MetalExperiments test = new MetalExperiments();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(MetalExperiments.class
            .getName());
}
