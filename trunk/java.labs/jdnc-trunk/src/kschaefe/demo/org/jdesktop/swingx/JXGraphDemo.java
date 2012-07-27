/**
 * 
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 *
 */
public class JXGraphDemo extends JFrame {
    private static class IdentityPlot extends JXGraph.Plot {

        /**
         * {@inheritDoc}
         */
        @Override
        public double compute(double value) {
            return value;
        }
        
    }
    
    private static class BoundedPlot extends JXGraph.Plot {

        /**
         * {@inheritDoc}
         */
        @Override
        public double compute(double value) {
            if (value < 0) {
                return Double.NaN;
            }
            
            return -value;
        }
        
    }
    
    private static class SinPlot extends JXGraph.Plot {

        /**
         * {@inheritDoc}
         */
        @Override
        public double compute(double value) {
            return Math.sin(value);
        }
        
    }
    
    protected void frameInit() {
        super.frameInit();
        
        setLayout(new GridLayout(1, 1));
        
//        JXGraph graph = new JXGraph();
//        graph.setGridPainted(false);
//        graph.addPlots(Color.BLACK, new IdentityPlot(), new BoundedPlot(), new SinPlot());
//        add(graph);
        JXCollapsiblePane cp = new JXCollapsiblePane();

        // JXCollapsiblePane can be used like any other container
        cp.setLayout(new BorderLayout());

        // the Controls panel with a textfield to filter the tree
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        controls.add(new JLabel("Search:"));
        controls.add(new JTextField(10));
        controls.add(new JButton("Refresh"));
        controls.setBorder(new TitledBorder("Filters"));
        cp.add("Center", controls);

        setLayout(new BorderLayout());

        // Put the "Controls" first
        add("North", cp);

        // Then the tree - we assume the Controls would somehow filter the tree
        JScrollPane scroll = new JScrollPane(new JTree());
        add("Center", scroll);

        // Show/hide the "Controls"
        JButton toggle = new JButton(cp.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
        toggle.setText("Show/Hide Search Panel");
        add("South", toggle);

        pack();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JXGraphDemo test = new JXGraphDemo();
                test.setSize(600, 600);
                test.setVisible(true);
            }
        });
    }

}
