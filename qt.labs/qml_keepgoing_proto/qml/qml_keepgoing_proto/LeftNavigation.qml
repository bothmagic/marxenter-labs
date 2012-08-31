// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import MyObjects 1.0
Item {
    id: leftNavigation

    Rectangle {
        anchors.fill: parent
    }

    ListView {
        id: listView
        model: ProjectListModel {}
        anchors.fill: parent
        delegate: Text {
            text: model.text
        }
    }

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

         ScrollBar {
             id: horizontalScrollBar
             width: listView.width-12; height: 12
             anchors.bottom: listView.bottom
             opacity: 0.5
             orientation: Qt.Horizontal
             position: listView.visibleArea.xPosition
             pageSize: listView.visibleArea.widthRatio
         }


}
