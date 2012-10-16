import QtQuick 1.1
import mylib 1.0
GraphItem {
    id: graphItem
    property int graphId;
    property Item graphView;
    width: 50; height: 20

    Component.onCompleted: {
        graphView.registerItem(graphId, graphItem)
    }
    Rectangle {
        anchors.fill: parent
        color: "blue"
        
    }

}
