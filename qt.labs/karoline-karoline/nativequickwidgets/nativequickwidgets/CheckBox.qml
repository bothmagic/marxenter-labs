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
	property alias text: checkBoxLabel.text
	property alias checked: contents.checked

	signal clicked
	signal toggled(bool enabled)

	width: checkBoxUi.width
	height: checkBoxUi.height

	SystemPalette { id: palette }

	Row {
		id: checkBoxUi
		anchors.fill: parent
		spacing: 7
		width: contents.width + 7 + checkBoxLabel.width;
		height: Math.max(contents.height, checkBoxLabel.height);

		DeclarativeCheckBox {
			id: contents
			width: 20; height: 20
			hovered: mouseArea.containsMouse
		}

		Text {
			id: checkBoxLabel
			color: container.enabled ? palette.buttonText : palette.dark
		}
	}

	MouseArea {
		id: mouseArea
		anchors.fill: parent
		onClicked: parent.clicked()
		onReleased: {
			if(container.enabled) {
				checked = !checked
				parent.toggled(checked)
			}
		}
		hoverEnabled: true
	}
}
