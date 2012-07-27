/*
	Copyright (C) 2010 by BetterInbox <contact@betterinbox.com>
	Original author: Gregory Schlomoff <greg@betterinbox.com>

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
*/

import QtQuick 1.0
import DragNDrop 1.0

Rectangle {
        id: tralal
	width: 300; height: 200
	color: "#eee"

	// Draggable rectangle
	Rectangle {
                id: pupu
		width: 60; height: 60;
		y: 10; x: 10
		color: "red"

		Text { anchors.centerIn: parent; color: "#fff"; text: "Drag me" }

		DragArea {
			anchors.fill: parent
			delegate: dragDelegate
                        autoStart: true
			supportedActions: Qt.MoveAction | Qt.LinkAction
			data {
				text: "Hello, this is my data"
				source: parent
			}
			onDrop: {
				console.log("Drag Area: target accepted the drop with action : " + action)
			}
		}
	}

	// Drop zone
	Rectangle {
		width: 60; height: 60;
		y: 10; x: 200
		color: "blue"

		Text { anchors.centerIn: parent; color: "#fff"; text: "[drop here]" }

		DropArea {
			id: aDropArea
			anchors.fill: parent

			onDragEnter: {
                                console.log("DropArea: drag enter with data: \"" + event.data.text + "\", source element: " + event.data.source.y + ", source color " + event.data.source.color);
			}

			onDrop: {
				event.accept(Qt.LinkAction);
				//event.accept()
			}
		}
	}

	Component {
		id: dragDelegate
		Rectangle {
			width: 50; height: 50;
			color: "green";
			opacity: 0.5;
			radius: 20;
			rotation: 30
//			Rectangle {
//				x:10; y:10
//				width: 200
//				height: 10
//				color:"yellow"
//				opacity:0.8
//			}
		}
	}
}
