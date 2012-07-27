// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1

Rectangle {
    id: rectangle1
    width: 100
    height: 62

    Text {
        id: text1
        x: 141
        y: 11
        width: 64
        height: 13
        text: qsTr("12.10.2011")
        anchors.bottom: rectangle2.top
        anchors.bottomMargin: 5
        anchors.right: item1.left
        anchors.rightMargin: 5
        font.pixelSize: 12
    }

    Rectangle {
        id: rectangle2
        width: 200
        radius: 3
        gradient: Gradient {
            GradientStop {
                position: 0
                color: "#ffffff"
            }

            GradientStop {
                position: 1
                color: "#eee4d9"
            }
        }
        border.width: 2
        border.color: "#dbc9c9"
        anchors.bottom: parent.bottom
        anchors.bottomMargin: 5
        anchors.left: parent.left
        anchors.leftMargin: 5
        anchors.top: image1.bottom
        anchors.topMargin: 5

        Text {
            id: text2
            text: qsTr("note")
            anchors.bottom: text3.top
            anchors.right: parent.right
            anchors.left: parent.left
            anchors.top: parent.top
            anchors.rightMargin: 5
            anchors.leftMargin: 5
            anchors.bottomMargin: 5
            anchors.topMargin: 5
            font.pixelSize: 12
    }

    Text {
        id: text3
        x: 5
        y: 29
        width: 190
        height: 137
        text: qsTr("context")
        anchors.top: text2.bottom
        anchors.topMargin: 29
        anchors.right: parent.right
        anchors.rightMargin: 5
        anchors.left: parent.left
        anchors.leftMargin: 5
        anchors.bottom: parent.bottom
        anchors.bottomMargin: 0
        font.pixelSize: 12
    }
    }

    Image {
        id: image1
        x: 5
        y: 5
        width: 20
        height: 19
        sourceSize.height: 24
        sourceSize.width: 24
        fillMode: Image.Stretch
        source: "star-empty.png"
    }

    Item {
        id: item1
        x: 210
        y: 29
        anchors.rightMargin: 0
        anchors.bottomMargin: 0
        anchors.topMargin: 5
        anchors.left: rectangle2.right
        anchors.right: parent.right
        anchors.bottom: parent.bottom
        anchors.top: text1.bottom
        anchors.leftMargin: 5

        Image {
            id: image2
            width: 40
            height: 23
            transformOrigin: Item.Left
            sourceSize.height: 16
            sourceSize.width: 16
            fillMode: Image.PreserveAspectFit
            anchors.right: parent.right
            anchors.rightMargin: 0
            anchors.left: parent.left
            anchors.leftMargin: 0
            anchors.top: parent.top
            anchors.topMargin: 0
            source: "control.png"
        }

        Image {
            id: image3
            width: 30
            height: 28
            rotation: 0
            sourceSize.height: 16
            sourceSize.width: 16
            fillMode: Image.PreserveAspectFit
            anchors.right: parent.right
            anchors.rightMargin: 0
            anchors.left: parent.left
            anchors.leftMargin: 0
            anchors.top: image2.bottom
            anchors.topMargin: 0
            source: "pencil.png"
        }
    }


}
