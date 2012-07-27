/*
 * Created on 04.04.2007
 *
 */
package paultaylor.table;

import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.swingx.JXFindPanel;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.CompoundHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.SearchPredicate;
import org.jdesktop.swingx.search.AbstractSearchable;
import org.jdesktop.swingx.search.PatternModel;
import org.jdesktop.swingx.search.SearchFactory;
import org.jdesktop.swingx.search.Searchable;
import org.jdesktop.swingx.search.TableSearchable;
 

/**
 * Example from forum.
 * http://forums.java.net/jive/thread.jspa?threadID=24741&tstart=0
 * 
 * @author paultaylor@dev.java.net
 */
public class FindReplaceAllTableExample {
    @SuppressWarnings("all")
    public void findAndReplaceByColumnTable() {
        Vector colNames = new Vector();
        colNames.add("col0");
        colNames.add("col1");
        colNames.add("col2");
        colNames.add("col3");

        Vector data = new Vector();
        for (int i = 0; i < 4; i++) {
            Vector v = new Vector();
            v.add(String.valueOf(i));
            v.add(String.valueOf(i + 1));
            v.add(String.valueOf(i));
            v.add(String.valueOf(i + 2));
            data.add(v);
        }

        JXTable table1 =  new JXTable(
                new DefaultTableModel(data, colNames));
        table1.setSearchable(new FindAllTableSearchable(table1));

        table1.getColumnModel().setColumnSelectionAllowed(true);

        JXFrame frame = new JXFrame("FindAndReplaceTable2");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JScrollPane scrollPane1 = new JScrollPane(table1);
        frame.add(scrollPane1);
        frame.pack();
        frame.setVisible(true);
    }

    public static class FindAllTableSearchable extends TableSearchable {

        // Used to restrict search to a specific column (view coordinates)
        private int searchColumn = -1;
        boolean markAll;
        
        public FindAllTableSearchable(JXTable findAllTable) {
            super(findAllTable);
        }

        public void setMarkAll(boolean mark) {
             this.markAll = mark;
        }

        public void findAll(Pattern pattern) {
            search(pattern, -1);
        }

        @Override
        protected AbstractHighlighter getConfiguredMatchHighlighter() {
            CompoundHighlighter searchHL = (CompoundHighlighter) getMatchHighlighter();
            if (!hasMatch()) {
                searchHL.setHighlightPredicate(HighlightPredicate.NEVER);
            } else {
                // JW: don't know what the logic relation between search/found column is
                HighlightPredicate predicate = new SearchPredicate(lastSearchResult.getPattern(), 
                        markAll ? -1 : lastSearchResult.getFoundRow(), 
                        markAll ? -1 : lastSearchResult.getFoundColumn());
                searchHL.setHighlightPredicate(predicate);
                ((AbstractHighlighter) searchHL.getHighlighters()[1]).setHighlightPredicate(
                        new SearchPredicate(lastSearchResult.getPattern(), 
                                lastSearchResult.getFoundRow(), lastSearchResult.getFoundColumn()));
            }
            return searchHL;
        }

        @Override
        protected AbstractHighlighter createMatchHighlighter() {
            ColorHighlighter base = new ColorHighlighter(Color.YELLOW.brighter(), null, 
                    Color.YELLOW.darker(), null);
            ColorHighlighter cell = new ColorHighlighter(Color.YELLOW.darker(), null);
            CompoundHighlighter match = new CompoundHighlighter(base, cell);
            return match;
        }

        
        
        // We need a way of accesing model coz Searchable itself does not
        // support modifications
        // so doing that in the panel, maybe need to create Replaceable
        // interface
        public TableModel getModel() {
            return getXTable().getModel();
        }

        public JXTable getXTable() {
            return table;
        }

        public int getLastMatchedColumn() {
            
            return lastSearchResult != null ? lastSearchResult.getFoundColumn() : -1;
//            return lastMatchedColumn;
        }

        // hack to only search on particular column
        @Override
        protected SearchResult findMatchAt(Pattern pattern, int row,
                int column) {
            if (searchColumn != -1 && searchColumn != column) {
                return null;
            }
            return super.findMatchAt(pattern, row, column);
        }

        public int getSearchColumn() {
            return searchColumn;
        }

        public void setSearchColumn(int searchColumn) {
            this.searchColumn = searchColumn;
//            int colModelIndex = getXTable().convertColumnIndexToModel(
//                    searchColumn);
        }
    }

    // extended in order to modify find panel
    static class FindAllSearchFactory extends SearchFactory {
        public JXFindPanel createFindPanel() {
            return new FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel();
        }

        // so shows highlighter
        public void showFindDialog(JComponent target, Searchable searchable) {
            super.showFindDialog(target, searchable);
            if (target != null) {
                target.putClientProperty(AbstractSearchable.MATCH_HIGHLIGHTER,
                        Boolean.TRUE);
            }
        }

        public static class FindAndReplacePanel extends JXFindPanel {

            public static final String FIND_ALL_ACTION_COMMAND = "findAll";

            public static final String REPLACE_ACTION_COMMAND = "replace";

            public static final String REPLACE_ALL_ACTION_COMMAND = "replaceAll";

            public static final String MATCH_RULE_ACTION_COMMAND = "selectMatchRule";

            public static final String MATCH_COLUMN_COMMAND = "matchColumn";

            public static final String REPLACE_FIELD_LABEL = "replaceFieldLabel";

            protected JLabel replaceLabel;

            protected JTextField replaceField;

            protected JCheckBox matchStartCheck;

            private JComboBox searchCriteria;

            private JComboBox tableColumns;

            static {
                UIManager
                        .put(
                                PatternModel.SEARCH_PREFIX
                                        + FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.FIND_ALL_ACTION_COMMAND,
                                "Find All");
                UIManager
                        .put(
                                PatternModel.SEARCH_PREFIX
                                        + FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_ALL_ACTION_COMMAND,
                                "Replace All");
                UIManager
                        .put(
                                PatternModel.SEARCH_PREFIX
                                        + FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_ACTION_COMMAND,
                                "Replace");
                UIManager
                        .put(
                                PatternModel.SEARCH_PREFIX
                                        + FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_FIELD_LABEL,
                                "Replace");
            }

            public FindAndReplacePanel() {
                // So ensures name shown on first display
                setName(getUIString(SEARCH_TITLE));

                // Column names depend on the searchable
                this.addPropertyChangeListener("searchable",
                        new PropertyChangeListener() {
                            public void propertyChange(PropertyChangeEvent evt) {
                                bindSearchColumnsRule();
                            }
                        });
            }

            public FindAndReplacePanel(Searchable searchable) {
                super(searchable);
                setName(getUIString(SEARCH_TITLE));
            }

            public void doFind() {
                ((FindAllTableSearchable) searchable)
                        .setMarkAll(false);
                super.doFind();
            }

            public void findAll() {
                ((FindAllTableSearchable) searchable)
                        .setMarkAll(true);
                doFindAll();
            }

            protected void doFindAll() {
                ((FindAllTableSearchable) searchable)
                        .findAll(getPatternModel().getPattern());
            }

            public void replace() {
                ((FindAllTableSearchable) searchable)
                        .setMarkAll(false);
                doReplace();
            }

            protected boolean doReplace() {
                int rowIndex = super.doSearch();
                if (rowIndex == -1) {
                    // no match
                    return false;
                }

                int colViewIndex = ((FindAllTableSearchable) searchable)
                        .getLastMatchedColumn();

                JXTable table = ((FindAllTableSearchable) searchable)
                        .getXTable();
                int colModelIndex = table
                        .convertColumnIndexToModel(colViewIndex);
                TableModel tableModel = ((FindAllTableSearchable) searchable)
                        .getModel();

                // Get model, we only want to replace the part that has matched
                Object currentValue = tableModel.getValueAt(rowIndex,
                        colModelIndex);

                // Only works with String data for simplicity
                if (currentValue instanceof String) {
                    String currentStringValue = (String) currentValue;

                    Pattern p = getPatternModel().getPattern();
                    String newValue = p.matcher(currentStringValue).replaceAll(
                            Matcher.quoteReplacement(replaceField.getText()));
                    tableModel.setValueAt(newValue, rowIndex, colModelIndex);
                    return true;
                }
                return false;
            }

            public void replaceAll() {
                ((FindAllTableSearchable) searchable)
                        .setMarkAll(true);
                doReplaceAll();
            }

            protected void doReplaceAll() {
                while (doReplace()) {
                    ;
                }
            }

            public void updateMatchRule() {
                getPatternModel().setMatchRule(
                        (String) searchCriteria.getSelectedItem());
            }

            public void matchColumnRule() {

                JXTable table = ((FindAllTableSearchable) searchable)
                        .getXTable();
                int colViewIndex = table
                        .convertColumnIndexToView(((TableColumnIndex) tableColumns
                                .getSelectedItem()).getColumnIndex());

                ((FindAllTableSearchable) searchable)
                        .setSearchColumn(colViewIndex);

            }

            // Add extra findAll, and dont assign any action as the dislog
            // executable button
            protected void initExecutables() {
                Action findAll = createBoundAction(FIND_ALL_ACTION_COMMAND,
                        "findAll");
                Action replace = createBoundAction(REPLACE_ACTION_COMMAND,
                        "replace");
                Action replaceAll = createBoundAction(
                        REPLACE_ALL_ACTION_COMMAND, "replaceAll");
                Action match = createBoundAction(MATCH_ACTION_COMMAND, "match");
                Action findNext = createBoundAction(FIND_NEXT_ACTION_COMMAND,
                        "findNext");
                Action findPrev = createBoundAction(
                        FIND_PREVIOUS_ACTION_COMMAND, "findPrevious");
                Action updateMatchRule = createBoundAction(
                        MATCH_RULE_ACTION_COMMAND, "updateMatchRule");
                Action matchColumn = createBoundAction(MATCH_COLUMN_COMMAND,
                        "matchColumnRule");
                getActionMap().put(FIND_ALL_ACTION_COMMAND, findAll);
                getActionMap().put(REPLACE_ACTION_COMMAND, replace);
                getActionMap().put(REPLACE_ALL_ACTION_COMMAND, replaceAll);
                getActionMap().put(MATCH_ACTION_COMMAND, match);
                getActionMap().put(FIND_NEXT_ACTION_COMMAND, findNext);
                getActionMap().put(FIND_PREVIOUS_ACTION_COMMAND, findPrev);
                getActionMap().put(MATCH_RULE_ACTION_COMMAND, updateMatchRule);
                getActionMap().put(MATCH_COLUMN_COMMAND, matchColumn);
                refreshEmptyFromModel();
            }

            // Add new components
            protected void initComponents() {
                super.initComponents();
                replaceLabel = new JLabel();
                replaceField = new JTextField(getSearchFieldWidth()) {
                    public Dimension getMaximumSize() {
                        Dimension superMax = super.getMaximumSize();
                        superMax.height = getPreferredSize().height;
                        return superMax;
                    }
                };

                replaceField.getDocument().addDocumentListener(
                        new DocumentListener() {
                            public void insertUpdate(DocumentEvent de) {
                                refreshEmptyFromModel();
                            }

                            public void removeUpdate(DocumentEvent de) {
                                refreshEmptyFromModel();
                            }

                            public void changedUpdate(DocumentEvent de) {
                                refreshEmptyFromModel();
                            }
                        });

                searchCriteria = new JComboBox();
                tableColumns = new JComboBox();
            }

            protected void bind() {
                super.bind();
                bindReplaceLabel();
                bindSearchColumnsRule();
                bindUpdateMatchRule();
            }

            // Configures the searchLabel.
            protected void bindReplaceLabel() {
                replaceLabel
                        .setText(getUIString(FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_FIELD_LABEL));
                replaceLabel.setLabelFor(replaceField);
            }

            // Select which column (or all columns) to apply search to
            protected void bindSearchColumnsRule() {
                if (searchable != null) {
                    List<TableColumn> visibleColumns = ((FindAllTableSearchable) searchable)
                            .getXTable().getColumns();
                    List<TableColumnIndex> columnNamesAndModelIndex = new ArrayList<TableColumnIndex>();
                    columnNamesAndModelIndex.add(new TableColumnIndex(
                            "All Columns", -1));
                    for (TableColumn tc : visibleColumns) {
                        columnNamesAndModelIndex.add(new TableColumnIndex(
                                (String) tc.getHeaderValue(), tc
                                        .getModelIndex()));
                    }
                    ComboBoxModel model = new DefaultComboBoxModel(
                            columnNamesAndModelIndex.toArray());
                    tableColumns.setModel(model);
                    tableColumns.setAction(getAction(MATCH_COLUMN_COMMAND));
                }
            }

            static class TableColumnIndex {
                private String columnName;

                private int columnIndex;

                public TableColumnIndex(String columnName, int columnIndex) {
                    this.columnIndex = columnIndex;
                    this.columnName = columnName;
                }

                public int getColumnIndex() {
                    return columnIndex;
                }

                public String getColumnName() {
                    return columnName;
                }

                public String toString() {
                    return getColumnName();
                }
            }

            // Select the type of search that is done
            protected void bindUpdateMatchRule() {
                java.util.List matchRules = getPatternModel().getMatchRules();
                ComboBoxModel model = new DefaultComboBoxModel(matchRules
                        .toArray());
                model.setSelectedItem(getPatternModel().getMatchRule());
                searchCriteria.setModel(model);
                searchCriteria.setAction(getAction(MATCH_RULE_ACTION_COMMAND));
            }

            // Add Find All so only enable if something entered in textfield
            // Only enable replace and replaceAll if something entered in these
            // fields
            protected void refreshEmptyFromModel() {
                super.refreshEmptyFromModel();
                boolean enabled = !getPatternModel().isEmpty();
                getAction(
                        FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.FIND_ALL_ACTION_COMMAND)
                        .setEnabled(enabled);

                if (replaceField != null) {
                    boolean replaceEnabled = replaceField.getText().length() > 0;
                    getAction(
                            FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_ACTION_COMMAND)
                            .setEnabled(replaceEnabled);
                    getAction(
                            FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_ALL_ACTION_COMMAND)
                            .setEnabled(replaceEnabled);
                } else {
                    getAction(
                            FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_ACTION_COMMAND)
                            .setEnabled(false);
                    getAction(
                            FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_ALL_ACTION_COMMAND)
                            .setEnabled(false);
                }
            }

            /**
             * Compose and layout all the subcomponents.
             */
            protected void build() {
                JButton findButton = new JButton(getActionMap().get(
                        MATCH_ACTION_COMMAND));
                JButton findAllButton = new JButton(
                        getActionMap()
                                .get(
                                        FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.FIND_ALL_ACTION_COMMAND));
                JButton replaceButton = new JButton(
                        getActionMap()
                                .get(
                                        FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_ACTION_COMMAND));
                JButton replaceAllButton = new JButton(
                        getActionMap()
                                .get(
                                        FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_ALL_ACTION_COMMAND));

                GroupLayout layout = new GroupLayout(this);
                setLayout(layout);
                layout.setAutocreateGaps(true);
                layout.setAutocreateContainerGaps(true);

                GroupLayout.SequentialGroup hGroup = layout
                        .createSequentialGroup();
                hGroup.add(layout.createParallelGroup().add(searchLabel).add(
                        replaceLabel));
                hGroup.add(layout.createParallelGroup().add(searchField).add(
                        replaceField).add(tableColumns).add(searchCriteria)
                        .add(matchCheck).add(wrapCheck).add(backCheck));
                hGroup.add(layout.createParallelGroup().add(findButton,
                        getButtonWidth(), getButtonWidth(), getButtonWidth())
                        .add(findAllButton, getButtonWidth(), getButtonWidth(),
                                getButtonWidth()).add(replaceButton,
                                getButtonWidth(), getButtonWidth(),
                                getButtonWidth()).add(replaceAllButton,
                                getButtonWidth(), getButtonWidth(),
                                getButtonWidth()));
                layout.setHorizontalGroup(hGroup);

                GroupLayout.SequentialGroup vGroup = layout
                        .createSequentialGroup();
                vGroup.add(layout.createParallelGroup().add(searchLabel).add(
                        searchField).add(findButton));
                vGroup.add(findAllButton);
                vGroup.add(layout.createParallelGroup().add(replaceLabel).add(
                        replaceField).add(replaceButton));
                vGroup.add(replaceAllButton);
                vGroup.add(tableColumns);
                vGroup.add(searchCriteria);
                vGroup.add(matchCheck);
                vGroup.add(wrapCheck);
                vGroup.add(backCheck);
                layout.setVerticalGroup(vGroup);
            }

            // I just want all the buttons to be same width
            private int getButtonWidth() {
                return 100;
            }

            protected PatternModel createPatternModel() {
                PatternModel l = new FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.FixedBatchPatternModel();
                return l;
            }

            static class FixedBatchPatternModel extends PatternModel {
                //So searches all fields in a row
                public boolean isAutoAdjustFoundIndex() {
                    return false;
                }
            }
        }
    }

    public static void main(String args[]) {
        SearchFactory searchFactory = new FindReplaceAllTableExample.FindAllSearchFactory();
        SearchFactory.setInstance(searchFactory);
        FindReplaceAllTableExample test = new FindReplaceAllTableExample();
        test.findAndReplaceByColumnTable();
    }
}
