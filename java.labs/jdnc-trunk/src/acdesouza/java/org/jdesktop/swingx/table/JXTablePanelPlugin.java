package org.jdesktop.swingx.table;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXTablePanel;

/**
 * Interface to add new features to the {@link JXTablePanel}.
 * 
 * @author AC de Souza
 * @version 1.0.0
 * @since 1.0.0
 */
public interface JXTablePanelPlugin<E extends JComponent> {
	public E getJToolBarAddOn();
	public void setJXTablePanel(JXTablePanel jxtablePanel);
}