package org.jdesktop.swingx.demo;

import javax.swing.ImageIcon;

import org.jdesktop.swingx.BuddyButton;

public class BookmarkButton extends BuddyButton {
	public BookmarkButton() {
		setIcon(new ImageIcon(getClass().getResource("apple.png")));
	}
}
