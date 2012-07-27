/*
 * CheckBoxList.java
 *
 * Created on 26 de agosto de 2006, 12:53
 */

package checkboxlist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * A JList which items are JCheckBoxes.
 * 
 * @author Francisco Morero Peyrona
 *         francisco.morero@sun.com
 */
public class CheckBoxList extends JList
{
    //------------------------------------------------------------------------//
    // Constructors
    
    /** 
     * Creates a new instance of CheckBoxList with no items.
     */
    public CheckBoxList()
    {
        initComponents();
    }
    
    /**
     *
     */
    public CheckBoxList( Collection<JCheckBox> items )
    {
        initComponents();
        addElement( items );
    }
    
    //------------------------------------------------------------------------//
    // Public interface
    
    public void addElement( String sCaption )
    {
        addElement( sCaption, false );
    }
    
    public void addElement( String sCaption, boolean bSelected )
    {
        addElement( sCaption, bSelected, true );
    }
    
    public void addElement( String sCaption, boolean bSelected, boolean bEnabled )
    {
        JCheckBox chkItem = new JCheckBox( sCaption, bSelected );
                  chkItem.setSelected( bSelected );
                  chkItem.setEnabled( bEnabled );
                  
        addElement( chkItem );
    }

    public void addElement( Collection<JCheckBox> items )
    {
        for( Iterator<JCheckBox> itr = items.iterator(); itr.hasNext(); )
            addElement( itr.next() );
    }
    
    public void addElement( JCheckBox chkItem )
    {
        chkItem.setFont( getFont() );
        chkItem.setOpaque( true );
        
        ((DefaultListModel) getModel()).addElement( chkItem );
    }
    
    /**
     * It is case sensitive.
     */
    public void removeElement( String sCaption )
    {
        DefaultListModel model = (DefaultListModel) getModel();
        
        for( int n = 0; n < model.getSize(); n++ )
        {
            JCheckBox check = (JCheckBox) model.get( n );
            
            if( check.getText().equals( sCaption ) )
            { 
                remove( n );
                break;
            }
        }
    }
    
    public void removeElement( Collection<JCheckBox> items )
    {
        for( Iterator<JCheckBox> itr = items.iterator(); itr.hasNext(); )
            removeElement( itr.next() );
    }
    
    /**
     * 
     * If chkItem, an exception is thrown
     */
    public void removeElement( JCheckBox chkItem )
    {
        removeElement( ((DefaultListModel) getModel()).indexOf( chkItem ) );
    }
    
    public void removeElement( int nIndex )
    {
        ((DefaultListModel) getModel()).removeElementAt( nIndex );
    }
    
    public void setSelectedAll()
    {
        setSelectedAll( true );
    }
    
    public  void setSelectedAll( boolean bIncludeDisabled )
    {
        int nItems = getModel().getSize();

        for( int n = 0; n < nItems; n++ )
            setSelected( n, true, bIncludeDisabled );
    }
    
    public void setSelectedNone()
    {
        setSelectedNone( true );
    }
    
    public void setSelectedNone( boolean bIncludeDisabled )
    {
        int nItems = getModel().getSize();

        for( int n = 0; n < nItems; n++ )
            setSelected( n, false, bIncludeDisabled );
    }
   
    public Collection<JCheckBox> getSelected()
    {
        ListModel model  = getModel();
        int       nItems = model.getSize();
        ArrayList list   = new ArrayList();
        
        for( int n = 0; n < nItems; n++ )
        {
            JCheckBox check = (JCheckBox) model.getElementAt( n );
            
            if( check.isSelected() )
                list.add( check );
        }
        
        return list;
    }
    
    //------------------------------------------------------------------------//
    // Protected methods
    
    protected void setSelected( int nIndex, boolean bSelected, boolean bIncludeDisabled )
    {
        ListModel model = getModel();
        JCheckBox check = (JCheckBox) model.getElementAt( nIndex );
            
        if( bIncludeDisabled )
            check.setSelected( bSelected );
        else
            if( check.isEnabled() )
                check.setSelected( bSelected );
         
        repaint();    // multiple calls to this method will be collapsed into a single one by the Java painting system
    }
    
    protected void toggleSelectedItems( boolean bIncludeDisabled )
    {
        int[] anSelected = getSelectedIndices();
        
        for( int n = 0; n < anSelected.length; n++ )
        {
            JCheckBox check = (JCheckBox) getModel().getElementAt( anSelected[n] );
            setSelected( anSelected[n], ! check.isSelected(), bIncludeDisabled );
        }
    }
    
    //------------------------------------------------------------------------//
    // Private methods
    
    private boolean isMouseOverBoxIcon( JCheckBox check, MouseEvent me )
    {
        Icon icon = check.getIcon();
        
        if( icon == null )
            icon = UIManager.getIcon( "CheckBox.icon" );
        
        Insets    insets = check.getInsets();
        Rectangle bounds = new Rectangle( insets.left, 
                                          insets.top, 
                                          icon.getIconWidth()  + insets.left,
                                          icon.getIconHeight() + insets.top );
        
        return bounds.contains( me.getPoint() );
    }
    
    private void initComponents()
    {
        setModel( new DefaultListModel() );
        setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        setCellRenderer( new CheckBoxCellRenderer() );
        
        addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( MouseEvent me )
            {
                // As every item can have a unique icon, it is necessary to check for every item 
                JCheckBox check  = (JCheckBox) CheckBoxList.this.getSelectedValue();
                int       nIndex = CheckBoxList.this.getSelectedIndex();
                Rectangle rect = CheckBoxList.this.getCellBounds( nIndex, nIndex );
                
                me.translatePoint( -rect.x, -rect.y );
                
                if( isMouseOverBoxIcon( check, me ) )
                    toggleSelectedItems( false );
            }
        } );
        
        addKeyListener( new KeyAdapter() 
        {
            public void keyTyped( KeyEvent evt )
            {
                if( evt.getKeyChar() == KeyEvent.VK_SPACE )
                    toggleSelectedItems( false );
            }
        } );
    }
    
    //-------------------------------------------------------------------------//
    // INNER CLASS: Cell Renderer
    //-------------------------------------------------------------------------//
    
    private final class CheckBoxCellRenderer implements ListCellRenderer
    {        
        // This is the only method defined by ListCellRenderer.
        public Component getListCellRendererComponent( JList list,
                                                       Object value,        // value to display
                                                       int index,           // cell index
                                                       boolean bSelected,   // is the cell selected
                                                       boolean bHasFocus )  // the list and the cell have the focus
        {
            JCheckBox check = (JCheckBox) value;
                      
            if( bSelected )
            {
               check.setBackground( list.getSelectionBackground() );
               check.setForeground( list.getSelectionForeground() );
            }
            else
            {
               check.setBackground( list.getBackground() );
               check.setForeground( list.getForeground() );
            }
           
            check.setEnabled( check.isEnabled() );// @todo no funciona el que al hacer disabled el list se hagan tb disabled los items
           
            return check;
       }
   }
}