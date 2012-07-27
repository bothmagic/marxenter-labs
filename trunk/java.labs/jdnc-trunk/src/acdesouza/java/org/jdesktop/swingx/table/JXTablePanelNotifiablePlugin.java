package org.jdesktop.swingx.table;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXTablePanel;


/**
 * Interface to add new features to the {@link JXTablePanel}. Those plugins will be notified about TableModel and SelectionModel changes.
 * 
 * TODO Think about the need of 2 interfaces. Maybe only one, full features, interface is needed and a default implementation
 * to avoid empty methods in plugins, who doesn't want to be notified.
 * 
 * @author AC de Souza
 * @version 1.0.0
 * @since 1.0.0
 */
public interface JXTablePanelNotifiablePlugin<E extends JComponent> extends JXTablePanelPlugin<E> {
	public void notifyTableModelChange();
	public void notifySelectionModelChange();
}