/*
 * Copyright(c) 2006, NEXVU Technologies
 * All rights reserved.
 */

import org.jdesktop.swingx.JXTreeTable_REDEN;
import org.jdesktop.swingx.treetable.FileSystemModel;
import org.jdesktop.swingx.treetable.TreeTableModel_REDEN;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import javax.swing.*;
import java.awt.Dimension;


public class Test {
	public static void main( String[] args ) {
		TreeTableModel_REDEN model = new FileSystemModel_REDEN();

		JXTreeTable_REDEN treetable = new JXTreeTable_REDEN( model );

		JFrame frame = new JFrame( "Test" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( new JScrollPane( treetable ) );
		frame.setSize( new Dimension( 400, 300 ) );
		frame.setVisible( true );
	}


	private static class FileSystemModel_REDEN implements TreeTableModel_REDEN {
		FileSystemModel delegate_model = new FileSystemModel();


		public int getHierarchicalColumn() {
			return 0;
		}

		public Class getColumnClass( int column ) {
			return Object.class;
		}

		public int getColumnCount() {
			return delegate_model.getColumnCount();
		}

		public String getColumnName( int column ) {
			return delegate_model.getColumnName( column );
		}

		public Object getValueAt( Object node, int column ) {
			return delegate_model.getValueAt( node, column );
		}

		public boolean isCellEditable( Object node, int column ) {
			return delegate_model.isCellEditable( node, column );
		}

		public void setValueAt( Object value, Object node, int column ) {
			delegate_model.setValueAt( value, node, column );
		}

		public Object getRoot() {
			return delegate_model.getRoot();
		}

		public Object getChild( Object parent, int index ) {
			return delegate_model.getChild( parent, index );
		}

		public int getChildCount( Object parent ) {
			return delegate_model.getChildCount( parent );
		}

		public boolean isLeaf( Object node ) {
			return delegate_model.isLeaf( node );
		}

		public void valueForPathChanged( TreePath path, Object newValue ) {
			delegate_model.valueForPathChanged( path, newValue );
		}

		public int getIndexOfChild( Object parent, Object child ) {
			return delegate_model.getIndexOfChild( parent, child );
		}

		public void addTreeModelListener( TreeModelListener l ) {
			delegate_model.addTreeModelListener( l );
		}

		public void removeTreeModelListener( TreeModelListener l ) {
			delegate_model.removeTreeModelListener( l );
		}
	}
}
