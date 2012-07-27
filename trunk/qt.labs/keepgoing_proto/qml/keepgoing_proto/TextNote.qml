import QtQuick 1.0


Component {
    id: textNote

    Rectangle {
        id:wrapper
        anchors.topMargin: 20
        width: 150
        height: 100
        state: "NONE"
        Rectangle {
            id: rectangle1
            color: "white"
            radius: 3
            opacity: 1
            border.color: "#000000"
            anchors.margins: 10
            anchors.fill: parent
        }

        MouseArea {
            hoverEnabled: true
            anchors.fill: parent
            onDoubleClicked: console.debug("double clicked")
            onEntered: wrapper.state = "HOVER"
            onExited: wrapper.state = "NONE"
        }

        Text {
            id: noteText
            x: 24
            y: 16
            text: index + ", " + type + ", " + size
            //color: wrapper.GridView.isCurrentItem ? "white" : "black"
            textFormat: "RichText"
            anchors.fill: parent
            anchors.margins: 15
            wrapMode: "WordWrap"
            clip: true
        }

        transitions: [
            Transition {
                from: "NONE"
                to: "HOVER"
                ColorAnimation {
                    properties: "color"; duration: 1000}
            },
            Transition {
                from: "HOVER"
                to: "NONE"
                ColorAnimation {
                    properties: "color"; duration: 1000}
            }
        ]
        states: [
            State {
                name: "HOVER"
                PropertyChanges {
                    target: wrapper
                    color: "blue"
                }
            },
            State {
                name: "NONE"

                PropertyChanges {
                    target: wrapper
                    color: "white"
                }


            }
        ]


    }

}

