/*
 * Version:   $Revision: 2910 $
 * Modified:  $Date: 2008-12-17 13:35:28 +0100 (Mi, 17 Dez 2008) $
 * By:        $Author: tbee $
 */
package org.jdesktop.swingx.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * This renderer allow the using a cell editor as the renderer.
 * There often is no reason whatsoever to have a difference between cell rendering and editing; they should look 100% identical.
 * 
 * Usage example:
 * table.setDefaultEditor(Article.class, new ArticleTableCellEditor());
 * table.setDefaultRenderer(Article.class, new UseTableCellEditorAsTableCellRenderer( new ArticleTableCellEditor() ) );
 * 
 * However, at times there are minor differences between how a cell editor and renderer look, for example a panel that should be transparent in renderer mode.
 * For this the editor optionally can implement the UseAsRenderer interface (defined in this class), and use that method to tweak it looks.
 * 
 * Naturally you can also create one class that implements both cell renderer and editor, but using this class removes the need for about 5 lines, and replaces that with a simple wrap.
 * Further more, there are some tuning detail that could/should be done (see implementation notes in the DefaultTableCellRenderer javadoc).
 * If we figure a way out how to implement that on top of an editor, all tables using this class automatically benefit from these improvements.
 * 
 * @version $Revision: 2910 $
 * @author  $Author: tbee $
 * @see 
 */
public class UseTableCellEditorAsTableCellRenderer 
implements TableCellRenderer
{
	/** <code>serialVersionUID</code> */
	private static final long serialVersionUID = 1L;

	/** Standard variable for determining version of a class file. */
	static public final String SOURCECODE_VERSION = "$Revision: 2910 $";

    // ======================================================================================================
    // Constructor
    
	/**
	 * Initialize the renderer
	 *  
	 * @param isBordered show a border
	 */
	public UseTableCellEditorAsTableCellRenderer(TableCellEditor tableCellEditor) 
	{
		super();
		setTableCellEditor(tableCellEditor);
		construct();
	}
	
	/** 
	 * Initialize the renderer
	 */
	private void construct()
	{
		// automatically call the useAsRenderer method
		if (getTableCellEditor() instanceof UseAsRenderer) ((UseAsRenderer)getTableCellEditor()).useAsRenderer();
	}

	
    // ======================================================================================================
    // Properties
    
	/** TableCellEditor */
	public TableCellEditor getTableCellEditor() { return iTableCellEditor; }
	public void setTableCellEditor(TableCellEditor value) { iTableCellEditor = value; }
	private TableCellEditor iTableCellEditor = null;
	
	
    // ======================================================================================================
    // TableCellRenderer
    
	/**
	 * 
	 */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
    {
    	// we use the editor as the renderer
    	Component lEditor = iTableCellEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
    	
    	// done
    	return lEditor;
    }

    
    // ======================================================================================================
    // UseAsRenderer
    
    /** if there are special settings for renderer mode, this is the interface to implement */
    public interface UseAsRenderer
    {
    	public TableCellEditor useAsRenderer();
    }
}
