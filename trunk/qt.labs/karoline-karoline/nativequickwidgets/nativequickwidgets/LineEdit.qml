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

	property alias echoMode: input.echoMode
	property alias enabled: frame.widgetEnabled
	property bool flat: false
	property alias inputMask: input.inputMask
	property alias text: input.text
	property alias passwordCharacter: input.passwordCharacter
	property bool readOnly: false
	property alias selectionStart: input.selectionStart
	property alias selectionEnd: input.selectionEnd
	property alias selectedText: input.selectedText
	property alias validator: input.validator

	signal textChanged(string text)

	SystemPalette { id: palette }

	DeclarativeLineEdit {
		id: frame
		anchors.fill: parent
		editFocus: input.focus
		hovered: mouseArea.containsMouse
		readOnly: parent.readOnly
		visible: !parent.flat
	}

	ContextMenu {
		id: contextmenu
		anchors.fill: parent

		MenuAction { text: qsTr("Cu&t"); enabled: !container.readOnly; onTriggered: input.cut() }
		MenuAction { text: qsTr("&Copy"); onTriggered: input.copy() }
		MenuAction { text: qsTr("&Paste"); enabled: !container.readOnly; onTriggered: input.paste() }
		MenuAction { text: qsTr("Delete"); enabled: !container.readOnly; onTriggered: input.text = "" }
		MenuAction { separator: true }
		MenuAction { text: qsTr("Select All"); onTriggered: input.selectAll() }
	}

	MouseArea {
		id: mouseArea
		anchors.fill: parent
		hoverEnabled: true
	}

	CursorArea {
		anchors.fill: parent
		anchors.margins: 2
		cursorShape: "ibeam"
	}

	TextInput {
		id: input
		anchors.fill: parent
		anchors.margins: 4
		color: palette.windowText
		selectByMouse: true
	}

	Keys.onReturnPressed: {
		container.textChanged(input.text)
		container.focus = true
	}
	Keys.onEnterPressed: {
		container.textChanged(input.text)
		container.focus = true
	}
	Keys.onEscapePressed: {
		container.text = text
		container.focus = true
	}
}
