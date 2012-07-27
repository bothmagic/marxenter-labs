import QtQuick 1.0
import "custom" as Components
import "plugin"

// jb : Size should not depend on background, we should make it consistent

Components.CheckBox{
    id:checkbox
    property string text
    property string hint
    width: Math.max(110, styleitem.textWidth(text) + 20)
    height:20

    background: QStyleItem {
        id:styleitem
        elementType:"checkbox"
        sunken:pressed
        on:checked || pressed
        hover:containsMouse
        text:checkbox.text
        enabled:checkbox.enabled
        focus:checkbox.focus
        hint:checkbox.hint
    }
    Keys.onSpacePressed:checked = !checked
}

