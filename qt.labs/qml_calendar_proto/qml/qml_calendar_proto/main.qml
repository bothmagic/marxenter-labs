import QtQuick 1.1

Rectangle {

    width: 800
    height: 600

    Rectangle {
        x:10;y:10
        width:d.width+10
        height:d.height+10
        border.color:"black"
        DateChooser {
            id: d
            anchors.horizontalCenter:parent.horizontalCenter
            anchors.verticalCenter:parent.verticalCenter
            onSelectedDateChanged: console.log(selectedDate)
        }
    }


}

