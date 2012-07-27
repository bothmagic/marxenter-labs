import QtQuick 1.0

Rectangle {
    width: 800
    height: 600
    GridView {
        id:grid
        cellWidth: 200
        cellHeight: 150

        anchors.fill: parent

        model: myModel
        delegate: TextNote {}

        highlight: Rectangle { color: "lightsteelblue"; radius: 5 }
        focus: true
        highlightFollowsCurrentItem: true
        currentIndex: 2

    }

    MouseArea {
        anchors.fill: parent
        onDoubleClicked: myModel.invite("this is a new animal");
    }


}
