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

Item {
	default property variant content: []		// Can't use property alias because of a bug in Qt 4.7.(0-1)
	property bool autoFillBackground: true
	property alias contentWidth: area.contentWidth
	property alias contentHeight: area.contentHeight
	property variant horizontalScrollBarPolicy: Qt.ScrollBarAsNeeded
	property variant verticalScrollBarPolicy: Qt.ScrollBarAsNeeded

	resources: SystemPalette { id: palette }
	children: [
		Flickable {
			id: area
			anchors.fill: parent
			anchors.rightMargin: verticalScrollBar.visible ? verticalScrollBar.width : 0
			anchors.bottomMargin: horizontalScrollBar.visible ? horizontalScrollBar.height : 0
			clip: true
			contentItem.children: parent.content
		},

		ScrollBar {
			id: verticalScrollBar
			orientation: "vertical"
			width: 16
			anchors.right: parent.right
			anchors.top: parent.top
			anchors.bottom: area.bottom
			autoFillBackground: parent.autoFillBackground
			minimum: 0
			maximum: Math.max(0, area.contentHeight - area.height)
			pageStep: area.visibleArea.heightRatio * area.contentHeight
			value: area.visibleArea.yPosition * area.contentHeight
			visible: {
				if(parent.verticalScrollBarPolicy == Qt.ScrollBarAsNeeded)
					return area.contentHeight-16 > area.height;
				if(parent.verticalScrollBarPolicy == Qt.ScrollBarAlwaysOff)
					return false;
				else if(parent.verticalScrollBarPolicy == Qt.ScrollBarAlwaysOn)
					return true;
			}
			onValueChanged: area.contentY = val
		},

		ScrollBar {
			id: horizontalScrollBar
			orientation: "horizontal"
			height: 16
			anchors.left: parent.left
			anchors.right: verticalScrollBar.visible ? verticalScrollBar.left : parent.right
			anchors.bottom: parent.bottom
			autoFillBackground: parent.autoFillBackground
			minimum: 0
			maximum: Math.max(0, area.contentWidth - area.width)
			pageStep: area.visibleArea.widthRatio * area.contentWidth
			value: area.visibleArea.xPosition * area.contentWidth
			visible: {
				if(parent.horizontalScrollBarPolicy == Qt.ScrollBarAsNeeded)
					return area.contentWidth-16 > area.width;
				if(parent.horizontalScrollBarPolicy == Qt.ScrollBarAlwaysOff)
					return false;
				else if(parent.horizontalScrollBarPolicy == Qt.ScrollBarAlwaysOn)
					return true;
			}
			onValueChanged: area.contentX = val
		},

		Rectangle {
			color: palette.window
			anchors.left: horizontalScrollBar.right
			anchors.top: horizontalScrollBar.top
			anchors.right: parent.right
			anchors.bottom: parent.bottom
			visible: verticalScrollBar.visible && horizontalScrollBar.visible
		}
	]
}
