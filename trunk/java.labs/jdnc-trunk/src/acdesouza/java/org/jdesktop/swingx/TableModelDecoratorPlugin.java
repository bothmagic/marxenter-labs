package org.jdesktop.swingx;

/**
 * Deve ser implementando por todos os plugins que decoram o TableModel da QueryView
 * @author AC de Souza
 *
 */
public interface TableModelDecoratorPlugin {

	public int getActualSelectedRow(int selectedRow);
}
