/*
 * JXmlTreeTable.java
 *
 * Created on October 31, 2006, 5:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableCellEditor;

import com.exalto.ExaltoXmlNode;
import com.exalto.AlternateRowHighlighter;
import com.exalto.ComponentAdapter;
import com.exalto.HierarchicalColumnHighlighter;
import com.exalto.Highlighter;
import com.exalto.HighlighterPipeline;
import com.exalto.TreeTableModel;
import com.exalto.org.jdesktop.swingx.JXTreeTable;
import com.exalto.org.jdesktop.swingx.JXTreeTable.TreeTableDataAdapter;
import java.awt.event.ActionEvent;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author omprakash.v
 */
public class JXmlTreeTable extends JXTreeTable {

      private XmlTreeModel domTreeModel;
      SimpleTreeModelAdapter treeTableAdapter;
	public static Color selBackground;
	public static Color background;
        boolean rootVisible;
      
      IndicatorRenderer indicatorRenderer = null;
    AttributeRenderer attributeRenderer = null; 

    	HighlighterPipeline highlighterPipeline = null;

    XmlTreeTableCellRenderer renderer;

      
    /** Creates a new instance of JXmlTreeTable */
    public JXmlTreeTable(XmlTreeModel model) {
        this(model, false);
    }
    
        private JXmlTreeTable(XmlTreeModel treeModel, boolean redun) {
        // To avoid unnecessary object creation, such as the construction of a
        // DefaultTableModel, it is better to invoke super(TreeTableModelAdapter)
        // directly, instead of first invoking super() followed by a call to
        // setTreeTableModel(TreeTableModel).
        // Adapt tree model to table model before invoking super()
    	super();

    	this.renderer = new XmlTreeTableCellRenderer(treeModel);
    	
    	treeTableAdapter = new SimpleTreeModelAdapter(treeModel, renderer, this);
  		
    	super.setModel(treeTableAdapter);

    	this.selectionBackground = new Color(0xCC, 0xC0, 0x6D);
    	selBackground = this.selectionBackground;
    	this.background = this.getBackground();
    
        // Enforce referential integrity; bail on fail
        if (treeModel != renderer.getModel()) { // do not use assert here!
            throw new IllegalArgumentException("Mismatched TreeTableModel");
        }

  		this.domTreeModel = treeModel;


            TableColumnModel tcm = getColumnModel();
        
            int colCount = treeTableAdapter.getColumnCount();
        
            for(int s=0;s<colCount;s++) {
                  tcm.getColumn(s).setPreferredWidth(150);
                  tcm.getColumn(s).setMinWidth(150);
            }
        
        
        /*
		 Highlighter[]   highlighters = new Highlighter[] {
		 	      new AlternateRowHighlighter(Color.white,
		 	                                         new Color(0xF0, 0xF0, 0xE0), null),
		 	      new HierarchicalColumnHighlighter(Color.WHITE, new Color(0xF0, 0xF0, 0xE0), this.selectionBackground),
				  new ConditionalHighlighter()};
		 
		 	  highlighterPipeline = new HighlighterPipeline(highlighters);
		 	  setHighlighters(highlighterPipeline);
		*/ 	  
        //    setHighlighters(new HighlighterPipeline(new Highlighter[]{ AlternateRowHighlighter.classicLinePrinter }));
        		 Highlighter[]   highlighters = new Highlighter[] {
		 	      new AlternateRowHighlighter(Color.white,
		 	                                         new Color(0xF0, 0xF0, 0xE0), null),
		 	      new HierarchicalColumnHighlighter(Color.WHITE, new Color(0xF0, 0xF0, 0xE0), this.selectionBackground)
				  };
		 
		 	  highlighterPipeline = new HighlighterPipeline(highlighters);
		 	  setHighlighters(highlighterPipeline);

        int col = this.getColumn(0).getModelIndex();
        
		System.out.println(" col 0 model index " + col);

        
        
        // renderer-related initialization -- also called from setTreeTableModel()
        init(renderer); // private method
        initActions();
        initActionsAndBindings();
        // disable sorting
        super.setSortable(true);
        setColumnControlVisible(true);
        configureEnclosingScrollPane();
        
        // Install the default editor.
        cellEditor =  new TreeTableCellEditor(renderer);
        setDefaultEditor(AbstractTreeTableModel.hierarchicalColumnClass,
        		cellEditor);

        // No grid.
        setShowGrid(false); // superclass default is "true"

        // Default intercell spacing
   //     setIntercellSpacing(spacing); 
        // for both row margin and column margin

        // JTable supports row margins and intercell spacing, but JTree doesn't.
        // We must reconcile the differences in the semantics of rowHeight as
        // understood by JTable and JTree by overriding both setRowHeight() and
        // setRowMargin();
   //OV     adminSetRowHeight(getRowHeight());
        setRowHeight(50);

     
        
    }
    
    /**
     * Initializes this JXTreeTable and permanently binds the specified renderer
     * to it.
     *
     * @param renderer private tree/renderer permanently and exclusively bound
     * to this JXTreeTable.
     */
    protected void init(TreeTableCellRenderer renderer) {
        super.init(renderer);

            ImageIcon attrIcon = new ImageIcon(getClass().getResource("images/gridAttr.gif"));
   	    ImageIcon elemIcon = new ImageIcon(getClass().getResource("images/gridElement.gif"));
  	    ImageIcon textIcon = new ImageIcon(getClass().getResource("images/gridText.gif"));

       	    indicatorRenderer = new IndicatorRenderer(treeTableAdapter, domTreeModel, elemIcon, attrIcon, textIcon);	    	   
 	    attributeRenderer = new AttributeRenderer(attrIcon, this); 
            
            if (getTableHeader() != null) {
                System.out.println(" in header not null ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ "); 
                getTableHeader().setResizingAllowed(true);
        }            
    }
    
    protected void initActions() {
        super.initActions();
    }
    
    
        /**
     * A small class which dispatches actions.
     * TODO: Is there a way that we can make this static?
     */
    private class GridActions extends JXTreeTable.Actions {
        
        GridActions(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent evt) {
            if ("expand-all".equals(getName())) {
                  expandAll();
            }
            else if ("collapse-all".equals(getName())) {
                collapseAll();
            }
             else if ("insert".equals(getName())) {
                 System.out.println(" setting filter ******************++++++++++++++");	
  //               setFilters(new FilterPipeline(new Filter[] {new PatternFilter("t.*", 0, 0) }));
                 try {
            //        treeTableAdapter.insertNode("Attr", null, null);
                 } catch(Exception e) {
                     e.printStackTrace();
                 }
            }

            else if ("undo".equals(getName())) {
                 System.out.println(" UNDO ******************++++++++++++++");	
                 
            }
            else if ("redo".equals(getName())) {
                 System.out.println(" REDO ******************++++++++++++++");	
                 
            }     
            else if ("delete".equals(getName())) {
                 System.out.println(" delete ******************++++++++++++++");	
                 try {
            //        treeTableAdapter.deleteNode();
                 } catch(Exception e) {
                     e.printStackTrace();
                 }
                 
            }
            
        }
    }


    
       
            public TableCellRenderer getCellRenderer(int row, int col) {

             ArrayList parentList = treeTableAdapter.getParentList();
            
             HashMap rowMapper = domTreeModel.getRowMapper();
             int type = 0;
             
     		 ArrayList nlist = (ArrayList) rowMapper.get(new Integer(row));
    		 String rowcol = (String) nlist.get(0);   

             String [][] parts = null;
    		 StringTokenizer stok2 = new StringTokenizer(rowcol, "|");
    		 int num = stok2.countTokens();
    		 parts = new String[num][3];
    		 int ct=0;
    		 while(stok2.hasMoreTokens()) {
    			String rwc = stok2.nextToken();			
    			StringTokenizer stok3 = new StringTokenizer(rwc, ",");
    			parts[ct][0] = stok3.nextToken();
    			parts[ct][1] = stok3.nextToken();
    			parts[ct++][2] = stok3.nextToken();
    			
    		}
 
    		for(int t=0;t<ct;t++) {

    			Arrays.sort(parts, new GridHelper.ColumnComparator());

            int prw = Integer.parseInt(parts[t][0]);
            int pcol = Integer.parseInt(parts[t][1]);
            int ppx = Integer.parseInt(parts[t][2]);

    		ExaltoXmlNode viewerNode = (ExaltoXmlNode) parentList.get(ppx);

    		       if(prw == row)  {
                       if(col == pcol) {
    		
                           int nodeType = viewerNode.getNodeType();

    						if((nodeType == 1) || (nodeType == 9))
    						{
    								type = 0;         
    						} 
    						else if(nodeType == 2) {
    							type = 2;
    						}
    						else {
    								type = 1;
    						}
    						break;

                       } else {
                          type = 1;
                       }
                   }

    		}

            if(type == 0) {
      //                    System.out.println(" in type ==0 ret tree "); 
                    return this.renderer;
                } 
    			else if(type == 2) {
    				return attributeRenderer;
    			}
    			else {
          //        System.out.println(" in else ret indic"); 
                return indicatorRenderer;
              //       return super.getCellRenderer(row, col); 
                   }
         }
        
            
    /**
     * Overridden to account for row index mapping. 
     * {@inheritDoc}
     */
    public Object getValueAt(int row, int column) {
 //       return getModel().getValueAt(convertRowIndexToModel(row), 
  //              convertColumnIndexToModel(column));

	//	System.out.println(" in GVA row = " + row);
	//	System.out.println(" in GVA origrow = " + convertRowIndexToModel(row));
    	
                return this.treeTableAdapter.getValueAt(convertRowIndexToModel(row), column);
    }
    
    public boolean isCellEditable(int row, int column) {
        return this.treeTableAdapter.isCellEditable(row, column);
    }
    
    public void setValueAt(Object aValue, int row, int column) {
    	this.treeTableAdapter.setValueAt(aValue, row, column);
    }

    /**
     * Convert row index from view coordinates to model coordinates accounting
     * for the presence of sorters and filters.
     * 
     * @param row
     *            row index in view coordinates
     * @return row index in model coordinates
     */
    public int convertRowIndexToModel(int row) {
        return getFilters() != null ?  getFilters().convertRowIndexToModel(row): row;
    }

    /**
     * Convert row index from model coordinates to view coordinates accounting
     * for the presence of sorters and filters.
     * 
     * @param row
     *            row index in model coordinates
     * @return row index in view coordinates
     */
    public int convertRowIndexToView(int row) {
        return getFilters() != null ? getFilters().convertRowIndexToView(row): row;
    }
    
        
    protected class XmlTreeTableCellRenderer extends JXTreeTable.TreeTableCellRenderer  {

              private Icon    closedIcon = null;
	      private Icon    openIcon = null;
	      private Icon    leafIcon = null;
	      private Icon    expIcon = null;
	      private Icon    collapseIcon = null;
        
              public XmlTreeTableCellRenderer(XmlTreeModel xmlModel) {
                   super((TreeTableModel) xmlModel);
                   updateIcons();
                   TreeIconRenderer ren = new TreeIconRenderer(this, openIcon, closedIcon, leafIcon, expIcon, collapseIcon, JXmlTreeTable.this);
                   setCellRenderer(ren);                 
              }  
              
                      /**
         * tries to set the renderers icons. Can succeed only if the
         * delegate is a DefaultTreeCellRenderer.
         * THINK: how to update? always override with this.icons, only
         * if renderer's icons are null, update this icons if they are not,
         * update all if only one is != null.... ??
         * 
         */
        private void updateIcons() {
            if (!isOverwriteRendererIcons()) return;

	         closedIcon = new ImageIcon(getClass().getResource("images/gridElement.gif"));
	         openIcon = new ImageIcon(getClass().getResource("images/gridElement.gif"));
	         leafIcon = new ImageIcon(getClass().getResource("images/gridText.gif"));
	         expIcon = new ImageIcon(getClass().getResource("images/tree_close.gif"));
	         collapseIcon = new ImageIcon(getClass().getResource("images/beginminus.gif"));
                    
        }
/*        
         public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
             
             super.getTableCellRendererComponent(table,
               value, isSelected, hasFocus, row, column);
             
             
         }
*/
        

              
        }


    /**
     * Overrides superclass version to provide support for cell decorators.
     *
     * @param renderer the <code>TableCellRenderer</code> to prepare
     * @param row the row of the cell to render, where 0 is the first row
     * @param column the column of the cell to render, where 0 is the first column
     * @return the <code>Component</code> used as a stamp to render the specified cell
     */
    
    public Component prepareRenderer(TableCellRenderer renderer, int row,
        int column) {

        Component component = super.prepareRenderer(renderer, row, column);
        // MUST ALWAYS ACCESS dataAdapter through accessor method!!!
        ComponentAdapter    adapter = getComponentAdapter();
        adapter.row = row;
        adapter.column = column;
        
        return applyRenderer(component, adapter); 
    }

        /**
     * Returns the adapter that knows how to access the component data model.
     * The component data adapter is used by filters, sorters, and highlighters.
     *
     * @return the adapter that knows how to access the component data model
     */

    protected ComponentAdapter getComponentAdapter() {
        if (dataAdapter == null) {
            dataAdapter = new TreeTableDataAdapter(this); 
        }
        // MUST ALWAYS ACCESS dataAdapter through accessor method!!!
        return dataAdapter;
    }

        /**
     * Overridden to enable hit handle detection a mouseEvent which triggered
     * a expand/collapse. 
     */    
    protected void processMouseEvent(MouseEvent e) {
        
          System.out.println(" process mouse event ");
          
                // BasicTableUI selects on released if the pressed had been 
        // consumed. So we try to fish for the accompanying released
        // here and consume it as wll. 
        if ((e.getID() == MouseEvent.MOUSE_RELEASED) && consumedOnPress) {
            consumedOnPress = false;
            e.consume();
            return;
        }
          
        System.out.println(" calling hacker ");
            
        if (getTreeTableHacker().hitHandleDetectionFromProcessMouse(e)) {
            // Issue #332-swing: hacking around selection loss.
            // prevent the
            // _table_ selection by consuming the mouseEvent
            // if it resulted in a expand/collapse
            consumedOnPress = true;
            e.consume();
            return;
        }
        consumedOnPress = false;
        
        super.processMouseEvent(e);

    }
    
        protected TreeTableHacker getTreeTableHacker() {
        if (treeTableHacker == null) {
            treeTableHacker = createTreeTableHacker();
        }
        return treeTableHacker;
    }
    
    protected TreeTableHacker createTreeTableHacker() {
//        return new TreeTableHacker();
        return new TreeTableHackerExt2();
    }
    
    
        /**
     * 
     * Note: currently this class looks a bit funny (only overriding
     * the hit decision method). That's because the "experimental" code
     * as of the last round moved to stable. But I expect that there's more
     * to come, so I leave it here.
     * 
     * <ol>
     * <li> hit handle detection in processMouse
     * </ol>
     */
    public class TreeTableHackerExt2 extends TreeTableHackerExt {


        /**
         * Here: returns true.
         * @inheritDoc
         */
        protected boolean isHitDetectionFromProcessMouse() {
         //   System.out.println(" pmouse ret true ");
            return true;
        }
        
       protected void completeEditing() {
            if (isEditing()) {
                System.out.println(" done editing ###############################################");
                getCellEditor().cancelCellEditing();
             }
        }

               /**
         * Entry point for hit handle detection called from processMouse.
         * Does nothing if isHitDetectionFromProcessMouse is false. 
         * 
         * @return true if the mouseEvent triggered an expand/collapse in
         *   the renderer, false otherwise. 
         *   
         * @see #processMouseEvent(MouseEvent)
         * @see #isHitDetectionFromProcessMouse()
         */
        public boolean hitHandleDetectionFromProcessMouse(MouseEvent e) {
            if (!isHitDetectionFromProcessMouse())
                return false;
           
            
            
            
            int col = columnAtPoint(e.getPoint());
            
/*            if(e.getClickCount() >= 2) {
                hitHandleDetectionFromEditCell(col, new EventObject(e));
            }
  */              
            
            boolean hit = ((col >= 0) && expandOrCollapseNode(columnAtPoint(e
                    .getPoint()), e));

            if(hit) {
                // handle clicked
                System.out.println(" ********************handle clicked******************** ");
                treeTableAdapter.treeStateChanged(e);
            
            }
            
            return hit;
        }

       
       

    }


    

}
