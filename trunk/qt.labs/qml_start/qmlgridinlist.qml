// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1

Rectangle {
    width: 200; height: 200


Component {
     id: contactDelegate
     Item {
         width: 180; height: 200
         Column {
             Text { text: '<b>Name:</b> ' + name }
             GridView {
                 width: 300; height: 200

                 model: ListModel {
                     ListElement {
                         name: "blahh"
                         number: "555 8426"
                     }
                     ListElement {
                         name: "kaaaa"
                         number: "555 0473"
                     }
                 }
                 delegate: Row {

                     Text { text: name + " " + number; }
                 }
             }
         }
     }
 }

    ListView {
        anchors.fill: parent
        model: ListModel {

            ListElement {
                name: "John Brown"
                number: "555 8426"
            }
            ListElement {
                name: "Sam Wise"
                number: "555 0473"
            }

        }

        delegate: contactDelegate
        highlight: Rectangle { color: "lightsteelblue"; radius: 5 }
        focus: true
    }
}

