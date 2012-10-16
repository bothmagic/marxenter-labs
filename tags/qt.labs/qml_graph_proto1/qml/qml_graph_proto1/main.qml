// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import mylib 1.0
Rectangle {
    width: 360
    height: 360

    EdgeLayer {
        id:edgeLayer
        anchors.fill: parent
        start:start
        end:end

        Rectangle {
            id: start
            x: 10; y: 10
            width: 50; height: 20
            color: "blue"
            MouseArea {
                anchors.fill: parent
                drag.target: parent
                drag.axis: Drag.XandYAxis
                onClicked: edgeLayer.state = "end"
            }
            Behavior on y {
                NumberAnimation {duration: 500; easing.type: Easing.OutBounce}
            }
        }


        Rectangle {
            id: end
            x: 300; y: 300
            width: 50; height: 20
            color: "blue"
            MouseArea {
                anchors.fill: parent
                drag.target: parent
                drag.axis: Drag.XandYAxis
                onClicked: edgeLayer.state = "begin"
            }
            Behavior on y {
                NumberAnimation {duration: 500; easing.type: Easing.OutBounce}
            }

        }


        states: [
            State {
                name: "begin"

                    PropertyChanges {
                        target: end
                        y:10
                    }
                    PropertyChanges {
                        target: start
                        y:300
                    }

            },
            State {
                name: "end"

                    PropertyChanges {
                        target: end
                        y:300
                    }
                    PropertyChanges {
                        target: start
                        y:10
                    }

            }

        ]


    }



}
