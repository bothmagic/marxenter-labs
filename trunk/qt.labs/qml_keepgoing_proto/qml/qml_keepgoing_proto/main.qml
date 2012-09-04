// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import MyObjects 1.0
Item {
    id: item1
    width: 800
    height: 600

    LeftNavigation {
        id: leftnavigation1
        width: 150

        anchors {
            left: parent.left
            top: parent.top
            bottom: parent.bottom

        }


    }

    Rectangle {
        id: simpleTaskView
        width: 300
        height: 300
        color: "#dff7e0"
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.verticalCenter: parent.verticalCenter



    }



}
