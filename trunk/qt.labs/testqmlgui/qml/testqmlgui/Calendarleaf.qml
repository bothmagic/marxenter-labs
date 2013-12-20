import QtQuick 1.1
    Item {
        id: calendarleaf
        width: 356
        height: 203

        Row {
            id: dayRow
            x: 41
            y: 0
            width: 319
            height: 43
            anchors.right: parent.right
            anchors.rightMargin: 0
            anchors.top: parent.top
            anchors.topMargin: 0
            anchors.left: weekCol.right
            anchors.leftMargin: 0
            Repeater { anchors.fill: parent; model: 7
                Rectangle { width: 45.900; height: 40
                    color: "lightgreen"

                    Text { text: index
                        font.pointSize: 20
                        anchors.centerIn: parent } }
            }


        }

        Column {
            id: weekCol
            y: 43
            width: 40
            height: 160
            anchors.left: parent.left
            anchors.leftMargin: 0
            Repeater { anchors.fill: parent; model: 4
                Rectangle { width: 45.900; height: 40
                    color: "lightgreen"

                    Text { text: index
                        font.pointSize: 20
                        anchors.centerIn: parent } }
            }


        }

        Grid {
            id: grid1
            x: -359
            scale: 1
            rows: 4
            columns: 7
            anchors.top: dayRow.bottom
            anchors.topMargin: 0
            anchors.left: weekCol.right
            anchors.leftMargin: 0
            anchors.right: parent.right
            anchors.rightMargin: 0
            anchors.bottom: parent.bottom
            anchors.bottomMargin: 0

            Repeater { anchors.fill: parent; model: 28
                Rectangle { width: 45.900; height: 40
                    color: "lightgreen"

                    Text { text: index
                        font.pointSize: 20
                        anchors.centerIn: parent } }
            }

        }
    }

