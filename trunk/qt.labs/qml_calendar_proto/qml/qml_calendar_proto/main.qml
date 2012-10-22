import QtQuick 1.1

Rectangle {

    width: 800
    height: 600

    Rectangle {
        x:10;y:10
        width:221
        height:200
        border.color:"black"
        DateChooser {
            anchors.horizontalCenter:parent.horizontalCenter
            anchors.verticalCenter:parent.verticalCenter
            onSelectedDateChanged: console.log(selectedDate)
        }
    }


}

