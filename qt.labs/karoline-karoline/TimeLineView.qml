import QtQuick 1.0
import NativeQuickWidgets 1.0
import "qrc:/nativequickwidgets"

Rectangle {
    SystemPalette { id: myPallete; colorGroup: SystemPalette.Active }
    color: myPallete.window

    ScrollArea {

        width: parent.width
        height: parent.height
        autoFillBackground: false
        contentWidth: myItemsView.contentWidth
        contentHeight: myItemsView.contentHeight
        horizontalScrollBarPolicy: Qt.ScrollBarAsNeeded
        verticalScrollBarPolicy: Qt.ScrollBarAlwaysOn


        ListView {
            id: myItemsView
            anchors.fill: parent
            spacing: 5
            model: timelineModel
            delegate: MyItem{}
        }
    }
}
