import QtQuick 1.0

Rectangle {
    width: 360
    height: 360

    Grid {
        id: grid1
        x: 7
        y: 11
        width: 400
        height: 400

        Rectangle {

            id: rectangle1
            width: 200
            height: 200
            radius: 3
            border.color: "#000000"

            Text {
                id: buttonLabel
                text: "textEdit"
                anchors.rightMargin: 10
                anchors.leftMargin: 10
                anchors.bottomMargin: 10
                anchors.topMargin: 10
                anchors.fill: parent
                opacity: 1
                font.pixelSize: 12
            }

            property color buttonColor: "lightblue"
            property color onHoverColor: "gold"
            property color borderColor: "white"

            signal buttonClick()
            onButtonClick: {
                console.log(buttonLabel.text + " clicked" )
            }

            MouseArea{
                id: buttonMouseArea
                x: 1
                y: 0
                width: 200
                height: 23
                drag.filterChildren: true
                anchors.right: parent.right
                anchors.rightMargin: -1
                anchors.left: parent.left
                anchors.leftMargin: 1
                anchors.top: parent.top
                anchors.topMargin: 0
                onClicked: buttonClick()
                hoverEnabled: true
                onEntered: parent.border.color = onHoverColor
                onExited:  parent.border.color = borderColor
            }

            //determines the color of the button by using the conditional operator
            color: buttonMouseArea.pressed ? Qt.darker(buttonColor, 1.5) : buttonColor


        }
    }
}
