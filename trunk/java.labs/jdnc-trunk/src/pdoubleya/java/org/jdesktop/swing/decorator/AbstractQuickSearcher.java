package org.jdesktop.swing.decorator;

import org.jdesktop.swing.decorator.popups.LightWeightPopup;

import javax.swing.*;
import javax.swing.event.AncestorEvent;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Gilles Philippart
 */
public abstract class AbstractQuickSearcher extends KeyAdapter implements QuickSearcher {
	
	protected StringBuffer sb;
	protected JPanel panel;
	protected JTextField textField;
	protected JPanel popupSearcher;
	protected Component owner;
	private boolean enabled;
	
	public AbstractQuickSearcher(String textPrefix, Component owner, JPanel searchPanel, JTextField aTextField) {
		this.owner = owner;
		this.popupSearcher = searchPanel;
		if ( popupSearcher == null ) throw new RuntimeException("what the hell?");
		sb = new StringBuffer();
		panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		Color newColor = new Color(255, 255, 204);
		panel.setBackground(newColor);
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		textField = aTextField; //new JTextField();
		//textField.setOpaque(false);
		textField.setForeground(Color.RED);
		textField.setFont(new Font("Arial", Font.BOLD, 13));
		JLabel label = new JLabel(textPrefix);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		label.setBorder(null);
		//textField.setBorder(null);
		//panel.add(label);
		//panel.add(Box.createHorizontalStrut(5));
		//panel.add(textField);
		setEnabled(true);
	}
	
	public void keyTyped(KeyEvent e) {
		if (!enabled) {
			return;
		}
		char keyChar = e.getKeyChar();
		if (keyChar == KeyEvent.VK_TAB) {
			// do nothing
			return;
		} else if (keyChar == KeyEvent.VK_ESCAPE) {
			clear();
		} else if (keyChar == KeyEvent.VK_BACK_SPACE) {
			delete();
		} else if (!Character.isISOControl(keyChar)) {
			append(keyChar);
		}
	}
	
	private void append(char keyChar) {
		sb.append(keyChar);
		decideToPopup();
		search(sb.toString());
	}
	
	private void delete() {
		int size = sb.toString().length();
		if (size > 0) {
			sb.delete(size - 1, size);
			decideToPopup();
			search(sb.toString());
		}
	}
	
	public void clear() {
		sb = new StringBuffer();
		decideToPopup();
		search(sb.toString());
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void decideToPopup() {
		textField.setText(sb.toString());
		if (popupSearcher != null) {
			popupSearcher.setVisible(false);
			//popup = null;
		}
		if (sb.toString().length() > 0 && owner.isShowing()) {
			// Point p = owner.getParent().getParent().getLocationOnScreen();
			//Point p = owner.getParent().getLocationOnScreen();
			//popup = new LightWeightPopup();
			//popup.reset(owner, panel, p.x, p.y);
			//popup.show();
			popupSearcher.setVisible(true);
		}
	}
	
	public void ancestorRemoved(AncestorEvent event) {
		decideToPopup();
	}
	
	public void ancestorMoved(AncestorEvent event) {
		// do nothing
	}
	
	public void ancestorAdded(AncestorEvent event) {
		decideToPopup();
	}
	
}
