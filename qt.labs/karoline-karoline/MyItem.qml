import Qt 4.7

Item {
//    function changeColor() { listItemRect.color = parent.parent.parent.color }

    id: listDelegateWrapper
    width: parent.width
//    width: 300
    height: listItemRect.height

    Rectangle {
        id: listItemRect
        width: parent.width - 5
        height: statusTextElement.height + infoToolbar.height + 6
        border.color: "darkgray"
        radius: 3

        Text {
            id: statusTextElement
            x: 5; y: 5
            wrapMode: Text.Wrap
            text: statusText
            width: parent.width - 10
            height: paintedHeight

            Row {
                id: infoToolbar
                height: 20
                anchors.top: statusTextElement.bottom
                anchors.topMargin: 5
                anchors.right: statusTextElement.right
                Text {
                    id: statusAuthorElement
                    text: statusAuthorScreenName
                }
//                Text {
//                    id: foobar
//                    text: "foobar"
//                }
            }
        }

//        MouseArea {
//            anchors.fill: parent
//            onClicked: changeColor()
//        }
    }
}

