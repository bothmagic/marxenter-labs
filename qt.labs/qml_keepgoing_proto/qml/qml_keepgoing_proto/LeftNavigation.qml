// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import MyObjects 1.0
Item {
    id: leftNavigation

    Rectangle {
        anchors.fill: parent
    }

    //    Flickable {
    //        id: flickable
    //        clip:true
    //        anchors.fill: parent
    //        flickableDirection: Flickable.VerticalFlick
    //        contentHeight: listView.contentHeight
    //        contentWidth: listView.contentWidth
    //        Component.onCompleted:  {
    //            console.log("width: " + listView.childrenRect.width)
    //            console.log("height: " + listView.implicitWidth)
    //        }
    ListView {
        id: listView
        property Item selItem;
        interactive: true

        KeyNavigation.down: listView.incrementCurrentIndex()
        onFocusChanged: console.log("focus")
        model: ProjectListModel {}
        anchors.fill:parent

        highlight: Rectangle {
            color: "lightsteelblue"
        }

        delegate:

            Item {

            id: tt
            height: 20
            width: parent.width
            //focus: true
            Rectangle {
                id:text
                color: "grey"
                //anchors.fill: parent
                width: 100
                height: 20
                TextInput {
                    focus: true
                    text: model.text

                }
                transform: Rotation {
                    id: rotation
                    origin.x: text.width/2
                    origin.y: text.height/2
                    axis.x: 0; axis.y: 1; axis.z: 0     // set axis.y to 1 to rotate around y-axis
                    angle: 0    // the default angle
                }


                states: [
                    State {
                        name: "wech"
                        ParentChange {
                            target:text
                            parent:simpleTaskView
                            x:0
                            y:0
                            width: simpleTaskView.width
                            height: simpleTaskView.height
                        }
                        PropertyChanges {
                            target: rotation; angle: 360
                        }
                    },
                    State {
                        name: "da"


                        ParentChange {
                            target:text
                            parent:tt
                            x:0
                            y:0
                            width: tt.width
                            height: tt.height
                        }
                        PropertyChanges {
                            target: rotation; angle: 0
                        }
                    }
                ]

                transitions: [
                    Transition {

                        ParentAnimation {

                            NumberAnimation { properties: 'x,y,width,height'; duration: 600 }
                            NumberAnimation { target: rotation; properties: "angle"; duration: 600}
                        }

                    }
                ]

            }
            states: [
                State {
                    name: "closed"
                    PropertyChanges {
                        target:tt
                        height: 20
                    }
                },
                State {
                    name: "opened"
                    PropertyChanges {
                        target:tt
                        height: 100
                    }
                }


            ]

            Behavior on height {
                NumberAnimation {duration: 200; easing.type: Easing.OutBounce}
            }
            MouseArea {
                anchors.fill: parent
                onClicked: listView.currentIndex = index
                onEntered: listView.currentIndex = index
                onDoubleClicked: {

                    if (listView.selItem) {
                        listView.selItem.state = "da"
                        console.log("doubleclicked");
                    }

                    text.state = "wech";
                    text.children[0].forceActiveFocus();
                    listView.selItem = text;
                }
                hoverEnabled: true
            }


        }
    }

    //    }



    // Attach scrollbars to the right and bottom edges of the view.
    ScrollBar {
        id: verticalScrollBar
        target: listView
        width: 12; height: listView.height
        anchors.right: listView.right
        opacity: 0.5
        orientation: Qt.Vertical
        position: listView.visibleArea.yPosition
        pageSize: listView.visibleArea.heightRatio
    }

    //    ScrollBar {
    //        id: horizontalScrollBar
    //        target: flickable
    //        width: flickable.width; height: 12
    //        anchors.bottom: flickable.bottom
    //        opacity: 0.5
    //        orientation: Qt.Horizontal
    //        position: flickable.visibleArea.xPosition
    //        pageSize: flickable.visibleArea.widthRatio

    //    }

    Item {
        id: buttonBar
        anchors.bottom: parent.bottom
        height: btImage.height
        width: btImage.width
        Image {
            id: btImage
            source: "content/button-add.png"
        }
        MouseArea {
            anchors.fill: parent
            onClicked: projectPopup.state = "open"
        }
    }


    Rectangle {
        id: projectPopup

        height: 0
        color: "#173efd"
        anchors.bottom: parent.bottom
        anchors.left: parent.left
        anchors.right: parent.right
        Behavior on height {
            NumberAnimation {duration: 200; easing.type: Easing.OutQuad}
        }
        MouseArea {
            anchors.fill: parent
            onClicked: projectPopup.state = "closed"
        }

        states: [
            State {
                name: "closed"
                PropertyChanges {
                    target: projectPopup
                    height: 0
                }
            },
            State {
                name: "open"
                PropertyChanges {
                    target: projectPopup
                    height: 200
                }

            }
        ]

    }




}
