// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import QtDesktop 0.1

Rectangle {
    id: rectangle1
    width: 600
    height: 250

    CheckBox {
        id: checkbox1
        y: 8
        width: 21
        height: 22
        text: ""
        anchors.left: parent.left
        anchors.leftMargin: 6
    }

    TextField {
        id: textfield1
        y: 3
        width: 560
        height: 33
        text: "TextField"
        anchors.left: checkbox1.right
        anchors.leftMargin: 6
        anchors.right: parent.right
        anchors.rightMargin: 8
    }

    Flow {
        id: flow1
        y: 44
        width: 244
        height: 21
        anchors.left: parent.left
        anchors.leftMargin: 33
        spacing: 26

        Text {
            id: text1
            x: 64
            y: 21
            text: qsTr("text")
            font.pixelSize: 12
        }

        Text {
            id: text2
            x: 81
            y: 21
            text: qsTr("text")
            font.pixelSize: 12
        }
    }

    TextField {
        id: textfield2
        y: 44
        width: 304
        height: 21
        text: "TextField"
        anchors.left: flow1.right
        anchors.leftMargin: 11
        minimumWidth: 200
    }
}
