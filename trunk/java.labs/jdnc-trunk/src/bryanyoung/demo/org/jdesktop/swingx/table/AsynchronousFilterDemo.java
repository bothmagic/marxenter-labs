package org.jdesktop.swingx.table;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author Bryan Young
 */
public class AsynchronousFilterDemo {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("PatternFilter:"));

        PatternFilter patternFilter = new MultiColumnPatternFilter();
        JTextField textField1 = new JTextField(10);
        topPanel.add(textField1);
        textField1.getDocument().addDocumentListener(new PatternFilterDocumentListener(patternFilter, textField1));
        topPanel.add(new JLabel("AsynchronousFilter:"));

        JTextField textField2 = new JTextField(10);
        topPanel.add(textField2, BorderLayout.NORTH);

        frame.add(topPanel, BorderLayout.NORTH);
        JXTable table = new JXTable(new BigTableModel());
//        table.setFilters(new FilterPipeline(patternFilter));
        table.setFilters(new FilterPipeline(patternFilter, new AsynchronousFilter(new SimpleMatchingFilter(textField2))));
        frame.add(new JScrollPane(table));

        table.packAll();
        frame.setVisible(true);
    }


    private static class PatternFilterDocumentListener implements DocumentListener {
        private final PatternFilter patternFilter;
        private final JTextField textField1;

        public PatternFilterDocumentListener(PatternFilter patternFilter, JTextField textField1) {
            this.patternFilter = patternFilter;
            this.textField1 = textField1;
        }

        /**
         * @noinspection EmptyCatchBlock
         */
        private static void changed(PatternFilter patternFilter, JTextField textField1) {
            try {
                patternFilter.setPattern(".*" + textField1.getText() + ".*", 0);
            } catch (Exception ex) {
            }
        }

        public void insertUpdate(DocumentEvent e) {
            changed(patternFilter, textField1);
        }

        public void removeUpdate(DocumentEvent e) {
            changed(patternFilter, textField1);
        }

        public void changedUpdate(DocumentEvent e) {
            changed(patternFilter, textField1);
        }
    }

    /** pattern matcher that looks at all the columns in the table (for apples-to-apples comparison) */
    private static class MultiColumnPatternFilter extends PatternFilter {
        public boolean test(int row) {
            if (pattern == null) {
                return false;
            }

            if (!adapter.isTestable(getColumnIndex())) {
                return false;
            }

            int columnCount = adapter.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                Object value = getInputValue(row, i);
                if (value != null && pattern.matcher(value.toString()).find()) {
                    return true;
                }
            }
            return false;
        }

    }
}

