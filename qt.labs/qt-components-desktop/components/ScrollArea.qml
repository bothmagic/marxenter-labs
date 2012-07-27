import QtQuick 1.0
import "custom" as Components
import "plugin"

FocusScope {
    id: scrollarea
    width: 100
    height: 100

    property int frameWidth: frame ? styleitem.pixelMetric("defaultframewidth") : 0;
    property int contentHeight : content.childrenRect.height
    property int contentWidth: content.childrenRect.width
    property alias color: colorRect.color
    property bool frame: true
    property bool highlightOnFocus: false
    property bool frameAroundContents: styleitem.styleHint("framearoundcontents")

    default property alias data: content.data

    property int contentY
    property int contentX

    onContentYChanged: { vscrollbar.value = contentY }
    onContentXChanged: { hscrollbar.value = contentX }

    Rectangle {
        id: colorRect
        color: "transparent"
        anchors.fill:styleitem
        anchors.margins: frameWidth
    }

    QStyleItem {
        id: styleitem
        elementType: "frame"
        onElementTypeChanged: scrollarea.frameWidth = styleitem.pixelMetric("defaultframewidth");
        sunken: true
        visible: frame
        anchors.fill: parent
        anchors.rightMargin: frame ? (frameAroundContents ? (vscrollbar.visible ? vscrollbar.width + 2 * frameMargins : 0) : -frameWidth) : 0
        anchors.bottomMargin: frame ? (frameAroundContents ? (hscrollbar.visible ? hscrollbar.height + 2 * frameMargins : 0) : -frameWidth) : 0
        anchors.topMargin: frame ? (frameAroundContents ? 0 : -frameWidth) : 0
        property int scrollbarspacing: styleitem.pixelMetric("scrollbarspacing");
        property int frameMargins : frame ? scrollbarspacing : 0
        property int frameoffset: style === "mac" ? -1 : 0
    }

    Item {
        id: flickable
        anchors.fill: styleitem
        anchors.margins: frameWidth
        clip: true

        Item {
            id: content
            x: -scrollarea.contentX
            y: -scrollarea.contentY
        }
    }

    ScrollBar {
        id: hscrollbar
        orientation: Qt.Horizontal
        property int availableWidth : scrollarea.width - (frame ? (vscrollbar.width) : 0)
        visible: contentWidth > availableWidth
        maximumValue: contentWidth > availableWidth ? scrollarea.contentWidth - availableWidth: 0
        minimumValue: 0
        anchors.bottom: parent.bottom
        anchors.bottomMargin: styleitem.frameoffset
        anchors.left: parent.left
        anchors.right: parent.right
        anchors.leftMargin: (frame ? frameWidth : 0)
        anchors.rightMargin: { vscrollbar.visible ? scrollbarExtent : (frame ? 1 : 0) }
        onValueChanged: contentX = value
        property int scrollbarExtent : styleitem.pixelMetric("scrollbarExtent");
    }

    ScrollBar {
        id: vscrollbar
        orientation: Qt.Vertical
        property int availableHeight : scrollarea.height - (frame ? (hscrollbar.height) : 0)
        visible: contentHeight > availableHeight
        maximumValue: contentHeight > availableHeight ? scrollarea.contentHeight - availableHeight : 0
        minimumValue: 0
        anchors.right: parent.right
        anchors.top: parent.top
        anchors.bottom: parent.bottom
        anchors.topMargin: styleitem.style == "mac" ? 1 : 0
        onValueChanged: contentY = value
        anchors.rightMargin: styleitem.frameoffset
        anchors.bottomMargin: hscrollbar.visible ? hscrollbar.height : styleitem.frameoffset
    }

    Rectangle {
        // This is the filled corner between scrollbars
        id: cornerFill
        anchors.left:  vscrollbar.left
        anchors.right: vscrollbar.right
        anchors.top: hscrollbar.top
        anchors.bottom: hscrollbar.bottom
        visible: hscrollbar.visible && vscrollbar.visible
        SystemPalette { id: syspal }
        color: syspal.window
    }

    QStyleItem {
        z: 2
        anchors.fill: parent
        anchors.margins: -3
        anchors.rightMargin: -4
        anchors.bottomMargin: -4
        visible: highlightOnFocus && parent.activeFocus && styleitem.styleHint("focuswidget")
        elementType: "focusframe"
    }
}
