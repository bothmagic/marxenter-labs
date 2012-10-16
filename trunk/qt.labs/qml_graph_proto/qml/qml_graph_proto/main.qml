// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import mylib 1.0
Rectangle {
    width: 800
    height: 600

    GraphView {
        id:edgeLayer
        anchors.fill: parent
        Component.onCompleted: {
            edgeLayer.registerEdge(2, 1)
            edgeLayer.registerEdge(2, 0)
            edgeLayer.registerEdge(3, 2)
            edgeLayer.registerEdge(4, 2)
        }

        Rectangle {
            x:10; y:10
            width: 200
            height: 200

            color: "yellow"
        }

        Rectangle {
            x:600; y:300
            width: 200
            height: 200

            color: "yellow"
        }

        VisualItemModel {
            id: leftModel
            MyGraphItem {
                id: my1
                graphId:0
                graphView: edgeLayer
                MouseArea {
                    anchors.fill: parent
                    drag.target: parent
                    drag.axis: Drag.XandYAxis
                    onClicked: {
                        //parent = rightFlow
                        //edgeLayer.updateItem(0)
                        my1.state = "right"
                    }

                }

                transitions: [ Transition {
                                        ParentAnimation {
                                            NumberAnimation { properties: 'x,y' }
                                        }
                                }]

                states: [
                    State {
                        name: "right"
                        ParentChange {
                            target: my1
                            parent: rightFlow
                            x:0
                            y:0
                        }
                    }

                ]

            }

            MyGraphItem {
                id:my2
                graphId: 1
                graphView: edgeLayer
                MouseArea {
                    anchors.fill: parent
                    drag.target: parent
                    drag.axis: Drag.XandYAxis
                    onClicked: {
                        //parent.parent = rightFlow
                        //edgeLayer.updateItem(1)
                        my2.state = "right"
                    }
                }

                transitions:
                    [ Transition {
                                        ParentAnimation {

                                            NumberAnimation { properties: 'x,y' }

                                        }
                                }]

                states: [
                    State {
                        name: "right"
                        ParentChange {
                            target: my2
                            parent: rightFlow
                            x:0
                            y:0
                        }
                    }

                ]
            }

        }

        VisualItemModel {
            id: rightModel
            MyGraphItem {
                graphId:3
                graphView: edgeLayer
            }
            MyGraphItem {
                graphId: 4
                graphView: edgeLayer
            }

        }

        MyGraphItem {
            graphId: 2
            graphView: edgeLayer
            x: 400; y: 300
        }


        Flow {
            id: leftFlow
            x:10; y:10
            width: 200
            height: 200
            move: Transition { NumberAnimation { properties: "x,y" } }
            add: Transition { NumberAnimation { properties: "x,y" } }
            spacing: 5
            Repeater {
                model: leftModel
            }
        }


    }




    Flow {
        x:600; y:300
        width: 200
        height: 200

        id: rightFlow

        spacing: 5

        move: Transition { NumberAnimation { properties: "x,y" } }
        add: Transition { NumberAnimation { properties: "x,y" } }

        Repeater {
            model: rightModel
        }
    }




}




