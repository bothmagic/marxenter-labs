/*
 * Created on 14.04.2009
 *
 */
package org.jdesktop.swingx.list;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class DoubleListSelectorView {

    DoubleListSelectorModel model;
    JXList excludedList;
    JXList includedList;
    JComponent content;
    
    private MouseListener toggleListener;
    private JButton toIncludeButton;
    private JButton toExcludeButton;
    private JLabel excludedLabel;
    private JLabel includedLabel;
    private JButton exportButton;
    
    
    public DoubleListSelectorView(DoubleListSelectorModel model) {
        this.model = model;
        init();
    }

    //----------------- init and configure view
    
    /**
     * one-for-all: inits model, bindable views and configures/binds all.
     * 
     */
    private void init() {
        // create the components which need to be bound
        excludedList = new JXList(true);
        includedList = new JXList(true);
        // TODO: add and wire buttons
        toIncludeButton = new JButton(createMoveAction(">", excludedList));
        toExcludeButton = new JButton(createMoveAction("<", includedList));
        exportButton = new JButton(createExportAction("Export Included Items"));
        // wire everything
        install(excludedList, false);
        install(includedList, true);
        // brute force binding of item count per list
//        PipelineListener l = new PipelineListener() {
//
//            public void contentsChanged(PipelineEvent e) {
//                updateLabels();
//                
//            }
//        };
//        excludedList.getFilters().addPipelineListener(l);

    }

    private Action createExportAction(String name) {
        Action action = new AbstractAction(name) {

            public void actionPerformed(ActionEvent e) {
                export();
            }};
        return action;
    }

    protected void export() {
        model.exportIncluded();
    }

    /**
     * 
     */
    private void updateLabels() {
        if (excludedLabel == null) return;
        excludedLabel.setText(getLabelText(false));
        includedLabel.setText(getLabelText(true));
    }

    /**
     * @param included 
     * @return
     */
    private String getLabelText(boolean included) {
        if (!included)
            return "Excluded Items (" + excludedList.getElementCount() + "):" ;
        return "Included Items (" + includedList.getElementCount() + "):" ;
    }

    private Action createMoveAction(String name, final JXList sourceList) {
        Action action = new AbstractAction(name) {

            public void actionPerformed(ActionEvent e) {
                moveSelectedElements(sourceList);
            }};
        return action;
    }

    /**
     * 
     * @param columns
     * @param included
     */
    private void install(JXList columns, boolean included) {
        columns.setModel(model.getListModel());
//        FilterPipeline pipeline = new FilterPipeline(new Filter[] 
//                {model.getFilter(included)});
//        columns.setFilters(pipeline);
        columns.setCellRenderer(new DefaultListRenderer(model.getStringValue(included)));
        columns.addMouseListener(getMouseListener());
    }

    /**
     * Lazily creates and returns the shared mouseListener for both
     * lists.
     *  
     * @return a Mouselistener to trigger item moves.
     */
    private MouseListener getMouseListener() {
        if (toggleListener == null) {
         toggleListener = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    moveSelectedElements((JXList) e.getSource());
                }
            }
            
         };
        }
        return toggleListener;
    }

    /**
     * Callback method used in listeners to move items from
     * one list to the other.
     * 
     * @param list the source list.
     */
    protected void moveSelectedElements(JXList list) {
        model.toggleInclusion(list.getSelectedValues());
    }


    //------------------------ implement ColumnControlPopup
    
    public void applyComponentOrientation(ComponentOrientation o) {
        // TODO: implement
    }

    public void updateUI() {
        // TODO: implement
    }


    //-------------------- init ui
   /**
    * lazily creates and returns the content component for showing
    * in the dialog.
    * @return the content of the dialog.
    */
    public JComponent getContent() {
        if (content == null) {
            content = buildContentFormLayout();
        }
        return content;
    }
   
    private JComponent buildContentFormLayout() {
        /*
        COLUMN SPECS:
        f:d:g, l:4dluX:n, f:d:n, l:4dluX:n, f:d:g
        ROW SPECS:   
        c:d:n, t:4dluY:n, f:d:g
        
        COLUMN GROUPS:  { {1, 5} }
        ROW GROUPS:     {}
        
        COMPONENT CONSTRAINTS
        ( 1,  1,  1,  1, "d=f, d=c"); javax.swing.JLabel      "Visible Columns"; name=visibleColumnsLabel
        ( 5,  1,  1,  1, "d=f, d=c"); javax.swing.JLabel      "Hidden Columns"; name=hiddenColumnsLabel
        ( 1,  3,  1,  1, "d=f, d=f"); javax.swing.JScrollPane; name=visibleColumns
        ( 3,  3,  1,  1, "d=f, c"); javax.swing.JPanel; name=moveitems
        ( 5,  3,  1,  1, "d=f, d=f"); javax.swing.JScrollPane; name=hiddenColumns
        */
        JXPanel content = new JXPanel();
        FormLayout formLayout = new FormLayout(
                "f:d:g, l:4dlu:n, f:d:n, l:4dlu:n, f:d:g", // columns
                "c:d:n, t:4dlu:n, f:d:g, t:4dlu:n, c:d:n" // rows

        );
        formLayout.setColumnGroups(new int[][] { {1, 5} });
        PanelBuilder builder = new PanelBuilder(formLayout, content);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        CellConstraints cl = new CellConstraints();

        excludedLabel = builder.addLabel(getLabelText(false), cl.xywh(1, 1, 1, 1),
                new JScrollPane(excludedList), cc.xywh(1, 3, 1, 1));
        includedLabel = builder.addLabel(getLabelText(true), cl.xywh(5, 1, 1, 1), 
                new JScrollPane(includedList), cc.xywh(5, 3, 1, 1));
        builder.add(buildControl(), cc.xywh(3, 3, 1, 1, "f, c"));
        content.setName("Toggle Column Visibility - double click");
        builder.add(buildSave(), cc.xywh(1, 5, 5, 1));
        return content;
        
    }


    private Component buildSave() {
        JComponent comp = ButtonBarFactory.buildRightAlignedBar(exportButton); 
        return comp;
    }

    private Component buildControl() {
        /*
        COLUMN SPECS:
            f:max(p;10dluX):n

            ROW SPECS:   
            c:d:n, t:4dluY:n, c:d:n

            COLUMN GROUPS:  {}
            ROW GROUPS:     {}

            COMPONENT CONSTRAINTS
            ( 1,  1,  1,  1, "d=f, d=c"); de.kleopatra.view.JButton; name=hide
            ( 1,  3,  1,  1, "d=f, d=c"); de.kleopatra.view.JButton; name=show
        */
        JXPanel buttons = new JXPanel();
        FormLayout formLayout = new FormLayout(
                "f:max(p;15dlu):n", // columns
                "c:d:n, t:4dlu:n, c:d:n" // rows
               
        ); 
        PanelBuilder builder = new PanelBuilder(formLayout, buttons);
        CellConstraints cc = new CellConstraints();
        builder.add(toIncludeButton, cc.xywh(1, 1, 1, 1));
        builder.add(toExcludeButton, cc.xywh(1, 3, 1, 1));
        return buttons;
    }


}
