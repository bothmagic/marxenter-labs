package org.jdesktop.swingx.demo;

import javax.swing.ImageIcon;

import org.jdesktop.swingx.BuddyButton;

public class RssButton extends BuddyButton{
	public RssButton() {
		setIcon(new ImageIcon(getClass().getResource("ShowRSSButton.png")));
		setPressedIcon(new ImageIcon(getClass().getResource("ShowRSSButton_Pressed.png")));
	}
}
