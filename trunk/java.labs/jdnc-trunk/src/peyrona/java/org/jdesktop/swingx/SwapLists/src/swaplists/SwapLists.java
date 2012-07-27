/*
 * SwapLists.java
 *
 * Created on 4 de septiembre de 2006, 1:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package swaplists;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 *
 * @author Francisco Morero Peyrona
 *         francisco.morero@sun.com
 */
public class SwapLists extends JPanel
{
    private JLabel lblAvailable;
    private JLabel lblSelected;
    private JList  lstAvailable;
    private JList  lstSelected;
    
    private JButton btnAllToSelected;
    private JButton btnOneToSelected;   // The proper name would be 'btnThoseHighlightedToSelected' but it is too long
    private JButton btnAllToAvailable;
    private JButton btnOneToAvailable;  // The proper name would be 'btnThoseHighlightedToAvailable' but it is too long
            
    /** 
     * Creates a new instance of SwapLists 
     */
    public SwapLists()
    {
        initComponents();
    }
    
    /** 
     * Creates a new instance of SwapLists
     *
     * @param sAvailable String representing the title for the left <code>JList</code>. 
     *                   By default "Available"
     * @param sSelected  String representing the title for the left <code>JList</code>. 
     *                   By default "Selected"
     */
    public SwapLists( String sAvailable, String sSelected )
    {
        initComponents();
        
        lblAvailable.setText( sAvailable );
        lblSelected.setText( sSelected );
    }
     
    public void addToAvailable( Object item )
    {
        ((DefaultListModel) lstAvailable.getModel()).addElement( item );
        
        updateButtonsEnabledStatus();
    }
    
    public void addToAvailable( Collection<Object> items )
    {
        for( Iterator<Object> itr = items.iterator(); itr.hasNext(); )
            ((DefaultListModel) lstAvailable.getModel()).addElement( itr.next() );
        
        updateButtonsEnabledStatus();
    }
    
    public void addToSelected( Object item )
    {
        ((DefaultListModel) lstSelected.getModel()).addElement( item );
        
        updateButtonsEnabledStatus();
    }
    
    public void addToSelected( Collection<Object> items )
    {
        for( Iterator<Object> itr = items.iterator(); itr.hasNext(); )
            ((DefaultListModel) lstSelected.getModel()).addElement( itr.next() );
        
        updateButtonsEnabledStatus();
    }
    
    public void removeFromAvailable( Object item )
    {
        ((DefaultListModel) lstAvailable.getModel()).removeElement( item );
        
        updateButtonsEnabledStatus();
    }
    
    public void removeFromAvailable( Collection<Object> items )
    {
        for( Iterator<Object> itr = items.iterator(); itr.hasNext(); )
            ((DefaultListModel) lstAvailable.getModel()).removeElement( itr.next() );
        
        updateButtonsEnabledStatus();
    }
    
    public void removeFromSelected( Object item )
    {
        ((DefaultListModel) lstSelected.getModel()).removeElement( item );
        
        updateButtonsEnabledStatus();
    }
    
    public void removeFromSelected( Collection<Object> items )
    {
        for( Iterator<Object> itr = items.iterator(); itr.hasNext(); )
            ((DefaultListModel) lstSelected.getModel()).removeElement( itr.next() );
        
        updateButtonsEnabledStatus();
    }

    /**
     * Return all items in <code>lstAvailable JList</code>
     *
     * @return Collection<Object> All items in <code>lstAvailable JList</code>
     */   
    public Collection<Object> getInAvailable()
    {
        return getAllItemsIn( lstAvailable );
    }
    
    /**
     * Return all items in <code>lstSelected JList</code>
     *
     * @return Collection<Object> All items in <code>lstSelected JList</code>
     */
    public Collection<Object> getInSelected()
    {
        return getAllItemsIn( lstSelected );
    }
    
    /**
     * Sets the delegate that's used to paint each cell in the list.
     *
     * @see javax.swing.JList#setCellRenderer
     */
    public void setListCellRenderer( ListCellRenderer lcr )
    {
        lstAvailable.setCellRenderer( lcr );
        lstSelected.setCellRenderer( lcr );
    }
    
    //-------------------------------------------------------------------------//
    // Private methods
    
    /**
     * Moves all items in lstSelected to lstAvailable
     */
    private void onAllToAvailable()
    {
        setHighlightedAllItemsIn( lstSelected );
        onChoosenToAvailable();
    }
    
    /**
     * Moves all items in lstAvailable to lstSelected
     */
    private void onAllToSelected()
    {
        setHighlightedAllItemsIn( lstAvailable );
        onChoosenToSelected();
    }
    
    /**
     * Moves those items that are highlighted (choosen) in lstSelected to lstAvailable 
     */
    private void onChoosenToAvailable()
    {
        addToAvailable( getHighlihgtedItemsIn( lstSelected ) );
        removeFromSelected( getHighlihgtedItemsIn( lstSelected ) );
    }
    
    /**
     * Moves those items that are highlighted (choosen) in lstAvailable to lstSelected
     */
    private void onChoosenToSelected()
    {
        addToSelected( getHighlihgtedItemsIn( lstAvailable ) );
        removeFromAvailable( getHighlihgtedItemsIn( lstAvailable ) );
    }
    
    /**
     * Update JButtons to reflect proper enabled status
     */
    private void updateButtonsEnabledStatus()
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                btnAllToAvailable.setEnabled( lstSelected.getModel().getSize() > 0 ); // There is at least one item in lstSelected
                btnAllToSelected.setEnabled( lstAvailable.getModel().getSize() > 0 ); // There is at least one item in lstAvailable
                btnOneToAvailable.setEnabled( lstSelected.getSelectedIndex() > -1 );  // There is at least one item in lstSelected  and it is selected
                btnOneToSelected.setEnabled( lstAvailable.getSelectedIndex() > -1 );  // There is at least one item in lstAvailable and it is selected
            }
        } );
    }
    
    /**
     * Components initialisation
     */
    private void initComponents()
    {
        // List Boxes labels
        lblAvailable = new JLabel( "Available items" );
        lblSelected  = new JLabel( "Selected items" );
        
        // List Boxes
        lstAvailable = new JList( new DefaultListModel() );
        lstAvailable.addListSelectionListener( new ListSelectionListener()
            {
                public void valueChanged( ListSelectionEvent se )
                {
                    if( ! se.getValueIsAdjusting() )
                        SwapLists.this.updateButtonsEnabledStatus();
                }
            } );
        
        lstSelected = new JList( new DefaultListModel() );
        lstSelected.addListSelectionListener( new ListSelectionListener()
            {
                public void valueChanged( ListSelectionEvent se )
                {
                    if( ! se.getValueIsAdjusting() )
                        SwapLists.this.updateButtonsEnabledStatus();
                }
            } );
        
        // Buttons
        btnAllToSelected = new JButton( ">>" );
        btnAllToSelected.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    SwapLists.this.onAllToSelected();
                }
            } );
        
        btnOneToSelected = new JButton( ">" );
        btnOneToSelected.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    SwapLists.this.onChoosenToSelected();
                }
            } );
            
        btnAllToAvailable = new JButton( "<<" );
        btnAllToAvailable.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    SwapLists.this.onAllToAvailable();
                }
            } );
            
        btnOneToAvailable = new JButton( "<" );
        btnOneToAvailable.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    SwapLists.this.onChoosenToAvailable();
                }
            } );
        
        // Internal panels
        JPanel pnlAvailable = new JPanel( new BorderLayout( 0,3 ) );
               pnlAvailable.add( lblAvailable, BorderLayout.NORTH  );
               pnlAvailable.add( new JScrollPane( lstAvailable ), BorderLayout.CENTER );
               
        JPanel pnlSelected = new JPanel( new BorderLayout( 0,3 ) );
               pnlSelected.add( lblSelected, BorderLayout.NORTH  );
               pnlSelected.add( new JScrollPane( lstSelected ), BorderLayout.CENTER );
        
        JPanel pnlButtons = new JPanel( new GridLayout( 4,1, 0,4 ) );
               pnlButtons.add( btnAllToSelected  );
               pnlButtons.add( btnOneToSelected  );
               pnlButtons.add( btnAllToAvailable );
               pnlButtons.add( btnOneToAvailable );
        JPanel pnlCentering = new JPanel( new GridBagLayout() );          // This panel and its GridBagLayout is
               pnlCentering.add( pnlButtons, new GridBagConstraints() );  // needed to center buttons vertically
        
        // this
        setBorder( new EmptyBorder( 4,4,4,4 ) );
        setLayout( new BorderLayout( 9,19 ) );
        add( pnlAvailable, BorderLayout.WEST   );
        add( pnlCentering, BorderLayout.CENTER );
        add( pnlSelected , BorderLayout.EAST   );
        
        updateButtonsEnabledStatus();
    }
    
    //------------------------------------------------------------------------
    // Other methods just to make code simpler
    
    private Collection<Object> getHighlihgtedItemsIn( JList lstWhich )
    {
        Object[]          aSelected = lstWhich.getSelectedValues();
        ArrayList<Object> list      = new ArrayList<Object>();
        
        for( int n = 0; n < aSelected.length; n++ )
            list.add( aSelected[n] );
        
        return list;
    }
    
    private void setHighlightedAllItemsIn( JList lstWhich )
    {
        int   nSize = lstWhich.getModel().getSize();
        int[] an    = new int[ nSize ];
        
        for( int n = 0; n < nSize; n++ )
            an[n] = n;
            
        lstWhich.setSelectedIndices( an );
    }
    
    private Collection<Object> getAllItemsIn( JList lstWhich )
    {
        ArrayList<Object> list = new ArrayList<Object>();
        
        for( Enumeration e = ((DefaultListModel) lstWhich.getModel()).elements(); e.hasMoreElements(); )
            list.add( e.nextElement() );
        
        return list;
    }
}