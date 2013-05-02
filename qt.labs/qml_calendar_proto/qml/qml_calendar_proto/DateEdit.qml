import QtQuick 1.1
import QtDesktop 0.1
import QmlFeatures 1.0
import "utils.js" as Utils

Item {
    id: dateEdit
    width: dateField.width
    height: dateField.height
    property date date
    property Item  ___dateChooser

    function fnOpenCalendar(myFocus) {
        if (!myFocus) {
            fnCloseCalendar()
        } else {
            date = QmlUtils.parseDate(dateField.text, "dd.MM.yyyy")

            if (!Utils.fnIsValidDate(date)) {
                date = new Date()
            }

            var container = Utils.fnGetItemRoot(dateField)

            ___dateChooser = dateChooser.createObject(container,
                                                      {dateField: dateField,
                                                       chooseDate: date})
            dateField.cursorPosition = 0
        }
    }

    function fnCloseCalendar() {
        if (Qt.isQtObject(___dateChooser)) {
            ___dateChooser.opacity = 0
            ___dateChooser.destroy(500)
            date = QmlUtils.parseDate(dateField.text, "dd.MM.yyyy")
        }
    }

        TextField {
            id:dateField
            smooth: true
            inputMask: "99.99.9999"
            font.family: "Arial"
            font.pixelSize: 20
            Keys.onEscapePressed: fnCloseCalendar()
            Keys.onReturnPressed: fnCloseCalendar()

            onActiveFocusChanged: {
                fnOpenCalendar(focus)
            }
            Rectangle {
                x: dateField.x
                y: dateField.y
                width: dateField.width
                height: dateField.height
                color: dateField.acceptableInput? "transparent": "red"
                opacity: 0.1

            }
        }



    Component {



    }


}
