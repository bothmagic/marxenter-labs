/*
 * Created on 04.04.2007
 *
 */
package org.jdesktop.swingx.find;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
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
import org.jdesktop.swingx.AbstractSearchable;
import org.jdesktop.swingx.JXFindPanel;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.PatternModel;
import org.jdesktop.swingx.SearchFactory;
import org.jdesktop.swingx.Searchable;
import org.jdesktop.swingx.decorator.*;


/**
 * Example from forum.
 * http://forums.java.net/jive/thread.jspa?threadID=24741&tstart=0
 *
 * @author paultaylor@dev.java.net
 */
public class FindReplaceAllTableExample
{

    public void findAndReplaceByColumnTable()
    {
        Vector <String>colNames = new Vector<String>();
        colNames.add("col0");
        colNames.add("col1");
        colNames.add("col2");
        colNames.add("col3");

        Vector <Vector>data = new Vector<Vector>();
        for (int i = 0; i < 4; i++)
        {
            Vector<String> v = new Vector<String>();
            v.add(String.valueOf(i));
            v.add(String.valueOf(i + 1));
            v.add(String.valueOf(i));
            v.add(String.valueOf(i + 2));
            data.add(v);
        }

        FindAllTable table1 = new FindReplaceAllTableExample.FindAllTable(
            new DefaultTableModel(data, colNames));

        table1.getColumnModel().setColumnSelectionAllowed(true);


        JXFrame frame = new JXFrame("FindAndReplaceTable2");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JScrollPane scrollPane1 = new JScrollPane(table1);
        frame.add(scrollPane1);
        frame.pack();
        frame.setVisible(true);
    }

    /** Add FindAll method to tables searchable with support for only searching a specific column
     *  and support for not searching certain columns when searching over all columns (because some
     *  may not be desired to sort over certain column because doent contain textual data for example)     *
     */
    static class FindAllTable extends JXTable
    {
        public FindAllTable(TableModel dm)
        {
            super(dm);
        }

        public Searchable getSearchable()
        {
            if (searchable == null)
            {
                searchable = new FindAllTableSearchable();
            }
            return searchable;
        }

        public class FindAllTableSearchable extends TableSearchable
        {
            //Used to indicate whether doing Find/Replace or Find All/Replace All
            private boolean mark;

            // Used to restrict search to a specific column (view coordinates)
            private int searchColumn = -1;

            //These columns will never be searched (Model cooridnates)
            private Set unSearchableColumns = new HashSet();

            @Override
            protected AbstractHighlighter getConfiguredMatchHighlighter()
            {
                AbstractHighlighter searchHL = getMatchHighlighter();
                HighlightPredicate predicate = HighlightPredicate.NEVER;

                // no match
                if (hasMatch(lastSearchResult))
                {
                    if(mark)
                    {
                        predicate = new SearchRestrictedColumnsPredicate(lastSearchResult.getPattern(),
                            SearchPredicate.ALL ,
                            getXTable().convertColumnIndexToModel(searchColumn));
                    }
                    else
                    {
                        predicate = new SearchRestrictedColumnsPredicate(lastSearchResult.getPattern(),
                            lastSearchResult.getFoundRow() ,
                            convertColumnIndexToModel(lastSearchResult.getFoundColumn() ));

                    }
                }
                searchHL.setHighlightPredicate(predicate);
                return searchHL;
            }

            /** Set/Unset All Functionality */
            public void setMarkAll(boolean mark)
            {
                this.mark=mark;
            }

            /** Find All */
            public int findAll(Pattern pattern)
            {
                return search(pattern, -1);
            }

            /** Add Column so cannot be searched */
            public void setUnsearchableColumn(int columnModelIndex)
            {
                unSearchableColumns.add(columnModelIndex);
            }

            /** We need a way of accessing table coz Searchable itself does not
             *  support modifications doing that in the panel, maybe need to create Replaceable
             *  interface
             */
            public JXTable getXTable()
            {
                return FindReplaceAllTableExample.FindAllTable.this;
            }

            /**
             * TODO Wouldnt need if lastSearchResult was public
             * @return last matching column in view coordinates
             */
            public int getLastMatchedColumn()
            {

                return lastSearchResult != null ? lastSearchResult.getFoundColumn() : -1;
            }

            /**
             * TODO Wouldnt need if lastSearchResult was public
             * @return last matching row  in view coordinates
             */
            public int getLastMatchedRow()
            {
                return lastSearchResult != null ? lastSearchResult.getFoundRow() : -1;
            }

            /**
             * TODO Wouldnt need if lastSearchResult was public
             * @return last matched pattern
             */
            public Pattern getLastMatchedPattern()
            {
                return lastSearchResult != null ? lastSearchResult.getPattern() : null;
            }

            /** Only search on particular column as selected by user, if user has selected all a column
             *  if all columns selected, reject if column set as unsearchable*/
            @Override
            protected SearchResult findMatchAt(Pattern pattern, int row,
                                               int column)
            {
                if (searchColumn != -1 && searchColumn != column)
                {
                    return null;
                }

                if(unSearchableColumns.contains(convertColumnIndexToModel(column)))
                {
                    return null;
                }

                return super.findMatchAt(pattern, row, column);
            }

            /** Get a search Column */
            public int getSearchColumn()
            {
                return searchColumn;
            }

            /** Set a search Column */
            public void setSearchColumn(int searchColumn)
            {
                this.searchColumn = searchColumn;
            }


            /**
             * Adds support for not searching certain columns when searching over all columns (because some
             * it may be desired to not sort over certain column because doesnt contain textual data for example)
             * Note:I cant just only add  a check to findMatchAt for restricting to search within in a column because
             * the search will still match the unsearchable column when to FindAll, Im not sure why.
             */
            class SearchRestrictedColumnsPredicate extends SearchPredicate
            {
                public SearchRestrictedColumnsPredicate(Pattern pattern, int row, int column)
                {
                    super(pattern,row,column);
                }

                public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
                {
                    if(isSearchableColumn(adapter))
                    {
                        return super.isHighlighted(renderer,adapter);
                    }
                    return false;
                }

                private boolean isSearchableColumn(ComponentAdapter adapter)
                {
                    int  columnToTest = adapter.viewToModel(adapter.column);

                    if(FindAllTableSearchable.this.unSearchableColumns.contains(columnToTest))
                    {
                        return false;
                    }
                    return true;
                }
            }
        }
    }




    // extended in order to modify find panel
    static class FindAllSearchFactory extends SearchFactory
    {
        public JXFindPanel createFindPanel()
        {
            return new FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel();
        }

        // so shows highlighter
        public void showFindDialog(JComponent target, Searchable searchable)
        {
            super.showFindDialog(target, searchable);
            if (target != null)
            {
                target.putClientProperty(AbstractSearchable.MATCH_HIGHLIGHTER,
                    Boolean.TRUE);
            }
        }

        public static class FindAndReplacePanel extends JXFindPanel
        {

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

            public static final String MATCH_RULE_PREFIX  = "MatchRule.";
            static
            {
                UIManager.put(MATCH_RULE_PREFIX + PatternModel.MATCH_RULE_CONTAINS,"Contains");
                UIManager.put(MATCH_RULE_PREFIX+ PatternModel.MATCH_RULE_EQUALS,"Equals");
                UIManager.put(MATCH_RULE_PREFIX + PatternModel.MATCH_RULE_ENDSWITH,"Ends With");
                UIManager.put(MATCH_RULE_PREFIX + PatternModel.MATCH_RULE_STARTSWITH,"Starts With");
                UIManager.put(PatternModel.SEARCH_PREFIX + FIND_ALL_ACTION_COMMAND, "Find All");
                UIManager.put(PatternModel.SEARCH_PREFIX+ REPLACE_ALL_ACTION_COMMAND,"Replace All");
                UIManager.put(PatternModel.SEARCH_PREFIX+ REPLACE_ACTION_COMMAND,"Replace");
                UIManager.put(PatternModel.SEARCH_PREFIX+ REPLACE_FIELD_LABEL,"Replace");
            }

            public FindAndReplacePanel()
            {
                // So ensures name shown on first display
                setName(getUIString(SEARCH_TITLE));

                // Column names depend on the searchable
                this.addPropertyChangeListener("searchable",
                    new PropertyChangeListener()
                    {
                        public void propertyChange(PropertyChangeEvent evt)
                        {
                            bindSearchColumnsRule();
                        }
                    });
            }

            public FindAndReplacePanel(Searchable searchable)
            {
                super(searchable);
                setName(getUIString(SEARCH_TITLE));
            }

            public void doFind()
            {
                ((FindReplaceAllTableExample.FindAllTable.FindAllTableSearchable) searchable)
                    .setMarkAll(false);
                super.doFind();
            }

            public void findAll()
            {
                ((FindReplaceAllTableExample.FindAllTable.FindAllTableSearchable) searchable)
                    .setMarkAll(true);
                doFindAll();
            }

            protected void doFindAll()
            {
                int found = ((FindReplaceAllTableExample.FindAllTable.FindAllTableSearchable) searchable)
                    .findAll(getPatternModel().getPattern());

                boolean notFound = (found == -1) && !getPatternModel().isEmpty();
                if (notFound)
                {
                    showNotFoundMessage();
                }
                else
                {
                    showFoundMessage();
                }
            }

            public void replace()
            {
                ((FindReplaceAllTableExample.FindAllTable.FindAllTableSearchable) searchable)
                    .setMarkAll(false);
                doReplace();
            }

            protected boolean doReplace()
            {
                FindReplaceAllTableExample.FindAllTable.FindAllTableSearchable findAllSearchable =
                    (FindReplaceAllTableExample.FindAllTable.FindAllTableSearchable) searchable;

                //Have we just had a matching find...
                int lastRow = findAllSearchable.getLastMatchedRow();
                if(lastRow>-1)
                {
                    //Was it for the value we were searching, and has it not been replaced yet
                    if(getPatternModel().getPattern().equals( findAllSearchable.getLastMatchedPattern()))
                    {
                        int colViewIndex = findAllSearchable.getLastMatchedColumn();

                        JXTable table = findAllSearchable.getXTable();
                        int colModelIndex = table.convertColumnIndexToModel(colViewIndex);
                        int rowModelIndex = table.convertRowIndexToModel(lastRow);
                        TableModel tableModel = findAllSearchable.getXTable().getModel();

                        //Get model, we only want to replace the part that has matched
                        Object currentValue = tableModel.getValueAt(rowModelIndex,colModelIndex);
                        //Only works with String data for simplicity
                        if (currentValue instanceof String)
                        {
                            String currentStringValue = (String) currentValue;
                            Pattern p = getPatternModel().getPattern();

                            //The value still exists, hasnt been replaced yet so replace and return
                            if(p.matcher(currentStringValue).find())
                            {
                                String newValue = p.matcher(currentStringValue).replaceAll(
                                    Matcher.quoteReplacement(replaceField.getText()));
                                tableModel.setValueAt(newValue, rowModelIndex, colModelIndex);
                                table.changeSelection(lastRow, colViewIndex, false, false);
                                return true;
                            }
                        }
                    }
                }

                //Got to this stage so need to do a search
                int rowIndex = super.doSearch();
                if (rowIndex == -1)
                {
                    // no match
                    return false;
                }

                int colViewIndex        = findAllSearchable.getLastMatchedColumn();
                JXTable table           = findAllSearchable.getXTable();
                int colModelIndex       = table.convertColumnIndexToModel(colViewIndex);
                int rowModelIndex       = table.convertRowIndexToModel(rowIndex);

                TableModel tableModel   = findAllSearchable.getXTable().getModel();

                // Get model, we only want to replace the part that has matched
                Object currentValue = tableModel.getValueAt(rowModelIndex,colModelIndex);

                // Only works with String data for simplicity
                if (currentValue instanceof String)
                {
                    String currentStringValue = (String) currentValue;

                    Pattern p = getPatternModel().getPattern();
                    String newValue = p.matcher(currentStringValue).replaceAll(
                        Matcher.quoteReplacement(replaceField.getText()));
                    tableModel.setValueAt(newValue, rowModelIndex, colModelIndex);
                    table.changeSelection(rowIndex, colViewIndex, false, false);
                    return true;
                }
                return false;
            }

            public void replaceAll()
            {
                ((FindReplaceAllTableExample.FindAllTable.FindAllTableSearchable) searchable)
                    .setMarkAll(true);
                doReplaceAll();
            }

            protected void doReplaceAll()
            {
                while (doReplace())
                {
                    ;
                }
            }

            public void updateMatchRule()
            {
                getPatternModel().setMatchRule(
                    ((MatchRuleDisplayPair) searchCriteria.getSelectedItem()).getMatchName());
            }

            public void matchColumnRule()
            {

                JXTable table = ((FindReplaceAllTableExample.FindAllTable.FindAllTableSearchable) searchable)
                    .getXTable();
                int colViewIndex = table
                    .convertColumnIndexToView(((TableColumnIndex) tableColumns
                        .getSelectedItem()).getColumnIndex());

                ((FindReplaceAllTableExample.FindAllTable.FindAllTableSearchable) searchable)
                    .setSearchColumn(colViewIndex);

            }

            // Add extra findAll, and dont assign any action as the dislog
            // executable button
            protected void initExecutables()
            {
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
            protected void initComponents()
            {
                super.initComponents();
                replaceLabel = new JLabel();
                replaceField = new JTextField(getSearchFieldWidth())
                {
                    public Dimension getMaximumSize()
                    {
                        Dimension superMax = super.getMaximumSize();
                        superMax.height = getPreferredSize().height;
                        return superMax;
                    }
                };

                replaceField.getDocument().addDocumentListener(
                    new DocumentListener()
                    {
                        public void insertUpdate(DocumentEvent de)
                        {
                            refreshEmptyFromModel();
                        }

                        public void removeUpdate(DocumentEvent de)
                        {
                            refreshEmptyFromModel();
                        }

                        public void changedUpdate(DocumentEvent de)
                        {
                            refreshEmptyFromModel();
                        }
                    });

                searchCriteria = new JComboBox();
                tableColumns = new JComboBox();
            }

            protected void bind()
            {
                super.bind();
                bindReplaceLabel();
                bindSearchColumnsRule();
                bindUpdateMatchRule();
            }

            // Configures the searchLabel.
            protected void bindReplaceLabel()
            {
                replaceLabel
                    .setText(getUIString(FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_FIELD_LABEL));
                replaceLabel.setLabelFor(replaceField);
            }

            // Select which column (or all columns) to apply search to selectable in combo, would want to overrride to suit your requirements
            // and which ones to make unsearcable in underlying table
            protected void bindSearchColumnsRule()
            {
                if (searchable != null)
                {
                    List<TableColumn> visibleColumns = ((FindAllTable.FindAllTableSearchable)searchable)
                        .getXTable().getColumns();
                    List<TableColumnIndex> columnNamesAndModelIndex = new ArrayList<TableColumnIndex>();
                    columnNamesAndModelIndex.add(new TableColumnIndex(
                        "All Columns", -1));
                    for (TableColumn tc : visibleColumns)
                    {
                        //Either add column as searchable OR add to unsearcbale list
                        if(tc.getModelIndex()==0)
                        {
                            ((FindAllTable.FindAllTableSearchable)searchable).setUnsearchableColumn(0);
                        }
                        else
                        {
                            columnNamesAndModelIndex.add(new TableColumnIndex(
                            (String) tc.getHeaderValue(), tc.
                            getModelIndex()));
                        }
                    }
                    ComboBoxModel model = new DefaultComboBoxModel(
                        columnNamesAndModelIndex.toArray());
                    tableColumns.setModel(model);
                    tableColumns.setAction(getAction(MATCH_COLUMN_COMMAND));
                }
            }

            static class TableColumnIndex
            {
                private String columnName;

                private int columnIndex;

                public TableColumnIndex(String columnName, int columnIndex)
                {
                    this.columnIndex = columnIndex;
                    this.columnName = columnName;
                }

                public int getColumnIndex()
                {
                    return columnIndex;
                }

                public String getColumnName()
                {
                    return columnName;
                }

                public String toString()
                {
                    return getColumnName();
                }
            }

            // Select the type of search that is done
            protected void bindUpdateMatchRule()
            {
                java.util.List <String>                 matchRules = getPatternModel().getMatchRules();
                java.util.List <MatchRuleDisplayPair>   matchPairs = new ArrayList <MatchRuleDisplayPair> ();
                for(String matchRule:matchRules)
                {
                    MatchRuleDisplayPair pair = new MatchRuleDisplayPair(matchRule);
                    matchPairs.add(pair);
                }
                ComboBoxModel model = new DefaultComboBoxModel(matchPairs.toArray());
                model.setSelectedItem(new MatchRuleDisplayPair(getPatternModel().getMatchRule()));
                searchCriteria.setModel(model);
                searchCriteria.setAction(getAction(MATCH_RULE_ACTION_COMMAND));
            }

            //temp until PatternModel give displynames for its match rules
            private static class MatchRuleDisplayPair
            {
                private String displayName;
                private String matchName;

                MatchRuleDisplayPair(String matchName)
                {
                    this.matchName   = matchName;
                    this.displayName=(String)UIManager.get(MATCH_RULE_PREFIX +  matchName);
                }

                public String getDisplayName()
                {
                    return displayName;
                }

                public String getMatchName()
                {
                    return matchName;
                }

                public String toString()
                {
                    return getDisplayName();
                }
            }

            // Add Find All so only enable if something entered in textfield
            // Only enable replace and replaceAll if something entered in these
            // fields
            protected void refreshEmptyFromModel()
            {
                super.refreshEmptyFromModel();
                boolean enabled = !getPatternModel().isEmpty();
                getAction(
                    FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.FIND_ALL_ACTION_COMMAND)
                    .setEnabled(enabled);

                if (replaceField != null)
                {
                    boolean replaceEnabled = replaceField.getText().length() > 0;
                    getAction(
                        FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_ACTION_COMMAND)
                        .setEnabled(replaceEnabled);
                    getAction(
                        FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.REPLACE_ALL_ACTION_COMMAND)
                        .setEnabled(replaceEnabled);
                }
                else
                {
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
            protected void build()
            {
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
            private int getButtonWidth()
            {
                return 100;
            }

            protected PatternModel createPatternModel()
            {
                PatternModel l = new FindReplaceAllTableExample.FindAllSearchFactory.FindAndReplacePanel.FixedBatchPatternModel();
                return l;
            }

            static class FixedBatchPatternModel extends PatternModel
            {
                //So searches all fields in a row
                public boolean isAutoAdjustFoundIndex()
                {
                    return false;
                }
            }
        }
    }

    public static void main(String args[])
    {
        SearchFactory searchFactory = new FindReplaceAllTableExample.FindAllSearchFactory();
        SearchFactory.setInstance(searchFactory);
        FindReplaceAllTableExample test = new FindReplaceAllTableExample();
        test.findAndReplaceByColumnTable();
    }
}

