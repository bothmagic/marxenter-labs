/***************************************************************************
 *   Copyright (C) 2011 by Víctor Fernández Martínez                       *
 *   deejayworld@gmail.com                                                 *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 3 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
import QtQuick 1.0
import NativeQuickWidgets 1.0

Item {
	id: container

	property alias enabled: contents.widgetEnabled
	property int iconSize
	property alias icon: buttonIcon.source
	property alias text: buttonLabel.text
	property bool checkable: false
	property bool checked: false
	property bool active: false
	property Menu menu
	property bool executingMenu: false

	signal clicked
	signal toggled(bool enabled)

	width: buttonDetails.width + 25
	height: buttonDetails.height + 9

	SystemPalette { id: palette }

	DeclarativeButton {
		id: contents
		anchors.fill: parent
		menu: parent.menu != null
		hovered: mouseArea.containsMouse
		pressed: parent.enabled && ((active && hovered) || (checkable && checked) || executingMenu)
	}

	Row {
		id: buttonDetails
		anchors.centerIn: container
		spacing: 7
		height: Math.max(buttonIcon.height, buttonLabel.height);

		Image {
			id: buttonIcon
			sourceSize.width: container.iconSize
			sourceSize.height: container.iconSize
			source: container.icon
		}

		Text {
			id: buttonLabel
			color: contents.widgetEnabled ? palette.buttonText : palette.dark
		}
	}

	MouseArea {
		id: mouseArea
		anchors.fill: parent
		onClicked: {
			parent.clicked()
			if(parent.menu != null) {
				executingMenu = true
				parent.menu.execAt(0, parent.height, parent)
				executingMenu = false
			}
		}
		onPressed: {
			if(contents.widgetEnabled) {
				active = true
				if(checkable) {
					checked = !checked
					parent.toggled(checked)
				}
			}
		}
		onReleased: if(contents.widgetEnabled) active = false;
		hoverEnabled: true
	}
}
