import QtQuick 1.0
import "custom" as Components
import "plugin"

// jens: ContainsMouse breaks drag functionality

Components.Slider{
    id: slider

    property bool tickmarksEnabled: true
    property string tickPosition: "Below" // "Top", "Below", "BothSides"

    QStyleItem { id:buttonitem; elementType: "slider" }

    property variant sizehint: buttonitem.sizeFromContents(23, 23)
    property int orientation: Qt.Horizontal

    height: orientation === Qt.Horizontal ? sizehint.height : 200
    width: orientation === Qt.Horizontal ? 200 : sizehint.height
    property string hint;

    groove: QStyleItem {
        anchors.fill:parent
        elementType: "slider"
        sunken: pressed
        maximum: slider.maximumValue*100
        minimum: slider.minimumValue*100
        value: slider.value*100
        horizontal: slider.orientation == Qt.Horizontal
        enabled: slider.enabled
        focus: slider.focus
        hint: slider.hint
        activeControl: tickmarksEnabled ? tickPosition.toLowerCase() : ""
    }

    handle: null
    valueIndicator: null

    Keys.onRightPressed: value += (maximumValue - minimumValue)/10.0
    Keys.onLeftPressed: value -= (maximumValue - minimumValue)/10.0

}
