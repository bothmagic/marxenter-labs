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

Rectangle {
	property bool autoFillBackground: false
	property alias orientation: scrollbar.orientation
	property alias minimum: scrollbar.minimum
	property alias maximum: scrollbar.maximum
	property alias pageStep: scrollbar.pageStep
	property alias singleStep: scrollbar.singleStep
	property alias value: scrollbar.value

	signal valueChanged(int val)

	SystemPalette { id: palette }

	color: autoFillBackground ? palette.window : Qt.rgba(0, 0, 0, 0)

	DeclarativeScrollBar {
		id: scrollbar
		anchors.fill: parent
		pressed: mousearea.pressed
	}

	MouseArea {
		id: mousearea
		anchors.fill: parent
		acceptedButtons: Qt.LeftButton | Qt.RightButton
		hoverEnabled: true

		property string activeSubControl: ""
		property int minimumMousePosition
		property int maximumMousePosition

		function findSubControl(x, y)
		{
			activeSubControl = scrollbar.hitTest(x, y);
			scrollbar.activateSubControl(activeSubControl);
		}

		function execSubControlTask()
		{
			if(activeSubControl == "AddLine") {
				if(scrollbar.value + scrollbar.singleStep <= scrollbar.maximum)
					scrollbar.value += scrollbar.singleStep;
			} else if(activeSubControl == "SubLine") {
				if(scrollbar.value - scrollbar.singleStep >= scrollbar.minimum)
					scrollbar.value -= scrollbar.singleStep;
			} else if(activeSubControl == "AddPage") {
				if(scrollbar.value + scrollbar.pageStep <= scrollbar.maximum)
					scrollbar.value += scrollbar.pageStep;
				else
					scrollbar.value = scrollbar.maximum;
			} else if(activeSubControl == "SubPage") {
				if(scrollbar.value - scrollbar.pageStep >= scrollbar.minimum)
					scrollbar.value -= scrollbar.pageStep;
				else
					scrollbar.value = scrollbar.minimum;
			} else
				return;
			parent.valueChanged(scrollbar.value)
		}

		function mousePosition(x, y)
		{
			return scrollbar.orientation == "vertical" ? y : x;
		}

		function activateSubControl(x, y)
		{
			findSubControl(x, y);
			if(activeSubControl == "Slider") {
				var pos = mousePosition(x, y);
				minimumMousePosition = scrollbar.grooveMinimum + pos - scrollbar.sliderMinimum;
				maximumMousePosition = scrollbar.grooveMaximum - (scrollbar.sliderMaximum - pos);
				state = "movingslider";
			} else
				execSubControlTask();
		}

		function moveSlider(x, y)
		{
			var pos = mousePosition(x, y);
			var value = scrollbar.minimum + 
							( ((scrollbar.maximum-scrollbar.minimum) * (pos - minimumMousePosition))
										/ (maximumMousePosition - minimumMousePosition)
							);
			if(value < scrollbar.minimum)
				value = scrollbar.minimum;
			else if(value > scrollbar.maximum)
				value = scrollbar.maximum;

			scrollbar.value = value;
			parent.valueChanged(scrollbar.value)
		}

		states: [
			State { name: "autorepeating" },
			State { name: "movingslider" }
		]

		Timer {
			id: autorepeater
			interval: 50
			repeat: true
			running: mousearea.state == "autorepeating"
			triggeredOnStart: true
			onTriggered: if(mousearea.containsMouse) mousearea.execSubControlTask();
		}

		onPositionChanged: {
			if(state == "movingslider") moveSlider(mouse.x, mouse.y);
			else if(state == "" && mousearea.containsMouse) findSubControl(mouse.x, mouse.y);
		}
		onEntered: {
			scrollbar.hovered = true;
			if(state == "autorepeating") scrollbar.activateSubControl(activeSubControl);
		}
		onExited: {
			if(state != "movingslider") {
				scrollbar.activateSubControl("")
				scrollbar.hovered = false;
			}
		}
		onPressed: activateSubControl(mouse.x, mouse.y)
					// TODO: add mouse wheel scroll when MouseArea supports it
		onPressAndHold: if(state != "movingslider") state = "autorepeating"
		onReleased: {
			state = ""
			scrollbar.activateSubControl("");
			scrollbar.hovered = mousearea.containsMouse;
		}
	}
}
