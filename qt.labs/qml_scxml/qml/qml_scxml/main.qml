import QtQuick 1.1
import QtDesktop 0.1

Rectangle {
    width: 360
    height: 360

    function eventTriggered(type) {
        console.log(type)

    }

    Component.onCompleted:  {
        stateMaschine.eventTriggered.connect(eventTriggered)


    }

    Column {
    anchors.fill: parent
    Text {
        text: qsTr("Hello World")
        anchors.centerIn: parent
        MouseArea {
            anchors.fill: parent
            onClicked: {
                stateMaschine.postNamedEvent("zwei")
            }
        }
    }

    Button {
        text: "Hallo"
    }

    }


}
