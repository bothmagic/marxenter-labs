package org.jdesktop.swingx;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;

public class FilmStripUtils {

	private FilmStripUtils() { }
	
	public static void showImagePopup(MouseEvent e, ImageSource src) {
		if (src == null) return;
		Collection<Action> actions = src.getActions();
		if (actions.isEmpty()) {
			Action noAction = new AbstractAction("No actions available") {
				public void actionPerformed(ActionEvent e) {}
			};
			noAction.setEnabled(false);
			actions.add(noAction);
		}
		JPopupMenu popup = new JPopupMenu();
		int x = e.getX();
		int y = e.getY();
		
		for (Action a : actions) {
			popup.add(a);
		}
		popup.show(e.getComponent(), x, y);
		
	}
	
}
