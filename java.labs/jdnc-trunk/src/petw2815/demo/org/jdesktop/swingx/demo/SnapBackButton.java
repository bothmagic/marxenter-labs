package org.jdesktop.swingx.demo;

import javax.swing.ImageIcon;

import org.jdesktop.swingx.BuddyButton;

public class SnapBackButton extends BuddyButton{
	public SnapBackButton() {
		setIcon(new ImageIcon(getClass().getResource("Search_SnapBack.png")));
		setPressedIcon(new ImageIcon(getClass().getResource("Search_SnapBackPressed.png")));
	}
}
