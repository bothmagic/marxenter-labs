package org.jdesktop.swing.decorator;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.jdnc.JNTable;
import org.jdesktop.swing.JXList;
import org.jdesktop.swing.utils.WindowUtils;

import javax.swing.*;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.regex.Pattern;

/**
 * @author Gilles Philippart
 */
public class QuickSearcherDemo {

    public static void main(String[] args) {

        JPanel centerPanel = new JPanel();
        JPanel p = centerPanel;
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(createJXTablePanel());
        p.add(Box.createHorizontalStrut(10));
        p.add(createJXListPanel());
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout(10,10));
        f.getContentPane().add(new JLabel("Please give focus to one of the components hereunder. Then start typing some keys to filter entries."),
                               BorderLayout.NORTH);
        f.getContentPane().add(p, BorderLayout.CENTER);
        f.pack();
        f.setLocation(WindowUtils.getPointForCentering(f));
        f.setVisible(true);
    }

    private static Component createJXListPanel() {
        JPanel panel = new JPanel(new GridLayout());
        DefaultListModel model = new DefaultListModel();
        model.add(0, "Johnny Wilkison");
        model.add(0, "John Fitzerald Kennedy");
        model.add(0, "Dan Brown");
        final JXList list = new JXList(model);
        QuickSearcher quickSearcher = new PatternQuickSearcher("Search ", list) {

            protected void filter(FilterPipeline filterPipeline) {
                list.setFilters(filterPipeline);
            }
        };
        list.addAncestorListener(quickSearcher);
        list.addKeyListener(quickSearcher);
        panel.add(list);
        return panel;
    }

    private static Component createJXTablePanel() {
        final JNTable table = new JNTable();
        table.setHighlighters(new HighlighterPipeline(new Highlighter[] { new AlternateRowHighlighter() }));
        table.setModel(createSampleTableModel());
        // adding the quicksearcher
        QuickSearcher quickSearcher = new PatternQuickSearcher("Search ", table) {

            protected void filter(FilterPipeline filterPipeline) {
                table.getTable().setFilters(filterPipeline);
            }
        };
        table.getTable().addAncestorListener(quickSearcher);
        table.getTable().addKeyListener(quickSearcher);
        return table;
    }

    private static TableModel createSampleTableModel() {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][] { { "Orson", "Scott Card" }, { "John", "Wayne" },
                { "Johnny", "Wilkinson" }, { "Steve", "Vai" }, { "Eric", "Clapton" }, { "Brad", "Pitt" } },
                new Object[] { "First name", "Last name" }) {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return tableModel;
    }

    private static abstract class PatternQuickSearcher extends AbstractQuickSearcher {

        private PatternQuickSearcher(String textPrefix, Component owner) {
            super(textPrefix, owner);
        }

        public void search(String s) {
            String s2 = StringUtils.replace(s, "*", ".*");
            String last = StringUtils.right(s2, 1);
            if (!"*".equals(last) && s.length() != 0) {
                s2 += ".*";
            }
            FilterPipeline filterPipeline = new FilterPipeline(new Filter[] { new PatternFilter(s2, Pattern.CASE_INSENSITIVE, 0) });
            filter(filterPipeline);
        }

        abstract protected void filter(FilterPipeline filterPipeline);
    }


}
