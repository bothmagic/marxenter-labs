import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 17-Jul-2008
 * Time: 17:42:35
 */

public class BooleanAlphaHighlighterIssue {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BooleanAlphaHighlighterIssue();
            }
        });
    }

    public BooleanAlphaHighlighterIssue() {
        JFrame frame = new JFrame(getClass().getSimpleName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(createUI());
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    Component createUI() {
        JPanel content = new JPanel(new BorderLayout());
        JXTable table = new JXTable();
        content.add(new JScrollPane(table));

        Set<String> languages = new HashSet<String>();
        for (Locale locale : Locale.getAvailableLocales()) {
            languages.add(locale.getDisplayLanguage());
        }
        Random random = new Random();
        List<WhatsIt> values = new ArrayList<WhatsIt>();
        for (String language : languages) {
            values.add(new WhatsIt(language, random.nextInt(5) > 2));
        }
        table.setModel(new MyTableModel(values));
        table.addHighlighter(new RowHighlighter(new HighlightPredicate() {
            public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
                return ((Boolean) adapter.getValue(1)).booleanValue();
            }
        }));

        return content;
    }

    static class WhatsIt {
        String language;
        boolean picked;

        WhatsIt(String language, boolean state) {
            this.language = language;
            this.picked = state;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public boolean isPicked() {
            return picked;
        }

        public void setPicked(boolean picked) {
            this.picked = picked;
        }
    }

    class MyTableModel extends AbstractTableModel {
        List<WhatsIt> values;

        MyTableModel(List<WhatsIt> values) {
            this.values = new ArrayList<WhatsIt>(values);
        }

        public int getRowCount() {
            return values.size();
        }

        public int getColumnCount() {
            return 2;
        }

        @Override
        public Class<?> getColumnClass(int column) {
            switch (column) {
                case 0:
                    return String.class;
                case 1:
                    return Boolean.class;
            }
            return super.getColumnClass(column);
        }

        public Object getValueAt(int row, int column) {
            WhatsIt item = values.get(row);
            switch (column) {
                case 0:
                    return item.getLanguage();
                case 1:
                    return item.isPicked();
            }
            return "";
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Nationality";
                case 1:
                    return "Wears socks with sandals";
            }
            return super.getColumnName(column);
        }
    }


    static class RowHighlighter extends ColorHighlighter {
        Font BOLD_FONT;

        RowHighlighter(HighlightPredicate predicate) {
            super(predicate, ColorUtil.setAlpha(Color.ORANGE, 60), Color.RED);
        }

        @Override
        protected Component doHighlight(Component renderer, ComponentAdapter adapter) {
            renderer.setForeground(getForeground());
            if (BOLD_FONT == null) {
                BOLD_FONT = renderer.getFont().deriveFont(Font.BOLD);
            }
            renderer.setFont(BOLD_FONT);
            return super.doHighlight(renderer, adapter);
        }
    }
}
