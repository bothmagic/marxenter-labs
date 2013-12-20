import QtQuick 1.1

Rectangle {
    id: rectangle2
    width: 360
    height: 360

    ListModel {
        id: myModel
        ListElement { type: "Dog"; age: 8 }
        ListElement { type: "Cat"; age: 5 }
        ListElement { type: "Cat"; age: 1}
    }

    Component {
        id:inline
        Calendarleaf {}
    }



    Rectangle {
        id:navigation
        width: 360
        anchors.top:parent.top
        anchors.left: parent.left
        anchors.right: parent.right
        height: 20
        anchors.rightMargin: 0

        Rectangle {
            id: rectangle1
            anchors.left: parent.left
            width: 20
            height: 20
            color: "#0037fd"
            MouseArea {
                onClicked: {
                    calView.model.append( { type: "Cat", age: 1});
                    calView.model.remove(0)
                    calView.incrementCurrentIndex();
                }
                anchors.fill:parent
            }
}


    }

    Item {
        anchors.top:navigation.bottom
        anchors.left: parent.left
        anchors.right: parent.right
        anchors.bottom: parent.bottom
        ListView {
            id:calView
            anchors.fill: parent
            orientation: ListView.Horizontal
            snapMode: ListView.SnapToItem
            flickableDirection: Flickable.AutoFlickDirection
            model: myModel
            delegate: inline
        }
}

}




