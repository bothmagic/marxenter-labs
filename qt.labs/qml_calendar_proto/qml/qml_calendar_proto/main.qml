import QtQuick 1.1

Rectangle {
    id: calendarChooser
    property int monthId: 0
    property int yearId: 0
    property int dayId:0

    property Item monthView
    property Item dayView
    property int currentViewType:0
    property Item currentView
    property Item nextView
    property Scale monthViewScale: Scale{}
    property Scale dayViewScale: Scale{}
    property variant currentDate

    property ListModel dayNames: ListModel {
        ListElement {name: "Mon"} ListElement {name: "Tue"}
        ListElement {name: "Wed"} ListElement {name: "Thu"}
        ListElement {name: "Fri"} ListElement {name: "Sat"}
        ListElement {name: "Sun"}
    }

    property ListModel monthNames: ListModel {
        ListElement {name: "Jan"} ListElement {name: "Feb"}
        ListElement {name: "Mar"} ListElement {name: "Apr"}
        ListElement {name: "May"} ListElement {name: "Jun"}
        ListElement {name: "Jul"} ListElement {name: "Aug"}
        ListElement {name: "Sep"} ListElement {name: "Oct"}
        ListElement {name: "Nov"} ListElement {name: "Dec"}
    }

    width: 220
    height: 160
    clip:true



    function fnZoomOutOfDayView() {

        dayViewScale.origin.x = monthView.children[monthId].x + monthView.children[monthId].width/2
        dayViewScale.origin.y = monthView.children[monthId].y + monthView.children[monthId].height/2

        monthViewScale.origin.x = monthView.children[monthId].x + monthView.children[monthId].width/2
        monthViewScale.origin.y = monthView.children[monthId].y + monthView.children[monthId].height/2
        currentView = monthView;
        currentViewType = 1
        //console.log(gridScale.origin.x + ", "+gridScale.origin.y)
        fnUpdateHeader()
        zoomOutToMonth.start();
    }

    function fnZoomIntoDayView(newMonthIdx) {
//        monthView.children[monthId].color = "white"
        monthId = newMonthIdx;
//        monthView.children[monthId].color = "blue"

        currentView = dayView;
        dayView.dayModel = fnLoadDayModel(new Date(currentDate.getFullYear(), monthId, 1))

        dayViewScale.origin.x = monthView.children[monthId].x + monthView.children[monthId].width/2
        dayViewScale.origin.y = monthView.children[monthId].y + monthView.children[monthId].height/2

        monthViewScale.origin.x = monthView.children[monthId].x + monthView.children[monthId].width/2
        monthViewScale.origin.y = monthView.children[monthId].y + monthView.children[monthId].height/2


        currentViewType = 0
        //console.log(gridScale.origin.x + ", "+gridScale.origin.y)
        fnUpdateHeader()
        zoomInToDay.start();
    }

    function fnShowPreviousView() {

        switch (currentViewType) {
        case 0:
            nextView = dayViewComponent.createObject(calendarChooser, {x: -220})
            nextView.dayModel = fnLoadDayModel(new Date(currentDate.getFullYear(), currentDate.getMonth()-1, 1))
            //nextView.children[dayId].color = "blue"
//            var newMonthIdx = currentDate.getMonth()
//            monthView.children[monthId].color = "white"
//            monthId = newMonthIdx;
//            monthView.children[monthId].color = "blue"

            break;
        case 1:
            nextView = monthViewComponent.createObject(calendarChooser, {x: -220, opacity:1})
            //nextView.children[monthId].color = "blue"
            currentDate = new Date(currentDate.getFullYear()-1, currentDate.getMonth(), 1)

        }

        fnUpdateHeader()
        showPreviosAnimation.start();
    }

    function fnShowNextView() {
        switch (currentViewType) {
        case 0:

            nextView = dayViewComponent.createObject(calendarChooser, {x: 220})
            nextView.dayModel = fnLoadDayModel(new Date(currentDate.getFullYear(), currentDate.getMonth()+1, 1))

            //var newMonthIdx = currentDate.getMonth()
            //monthView.children[monthId].color = "white"
            //monthId = newMonthIdx;
            //monthView.children[monthId].color = "blue"
            break;
        case 1:
            nextView = monthViewComponent.createObject(calendarChooser, {x: 220, opacity:1})
            //nextView.children[monthId].color = "blue"
            currentDate = new Date(currentDate.getFullYear()+1, currentDate.getMonth(), 1)
        }
        fnUpdateHeader()
        showNextAnimation.start();
    }

    function fnAnimationComplete() {

        switch (currentViewType) {
        case 0:
            dayView.destroy();
            dayView = nextView;
            break;
        case 1:
            monthView.destroy();
            monthView = nextView;
        }

        currentView = nextView
    }

    function fnUpdateHeader() {
        switch (currentViewType) {
            case 0: txt_header.text = Qt.formatDate(currentDate, "MMMM yyyy"); break;
            case 1: txt_header.text = Qt.formatDate(currentDate, "yyyy"); break;
        }
        monthId = currentDate.getMonth()
    }

    function fnLoadDayModel(da) {

        var daym = dayModelComponent.createObject(calendarChooser);

        currentDate = da

        var year = da.getFullYear();
        var month = da.getMonth();
        da.setDate(1)
        da.setDate(da.getDate() - da.getDay() +1 );
        console.log("load daymodel for " + currentDate);

        for (var i = 0; i < 42; i++) {
            daym.append({name: da.getDate(), inRange: da.getMonth() == month})
            da.setDate(da.getDate()+1);
        }
        return daym
    }


    Component.onCompleted: {

        dayView = dayViewComponent.createObject(calendarChooser);
        monthView = monthViewComponent.createObject(calendarChooser);
        currentView = dayView

        dayView.dayModel = fnLoadDayModel(new Date())

        //monthView.children[currentDate.getMonth()].color = "blue"
        //dayView.children[dayId].color = "blue"
        fnUpdateHeader()

    }

    Component {
        id:dayModelComponent
        ListModel {

        }
    }

    Item {
        id: header
        anchors.right: parent.right
        anchors.left: parent.left
        height: 20
        z: 10
        Rectangle {
            anchors.fill: parent
        }
        Text {
            anchors.left: parent.left
            text: "Left"
            MouseArea {
                anchors.fill: parent
                onClicked: {
                    fnShowPreviousView();
                }
            }
        }



            Text {
                id:txt_header
                anchors.horizontalCenter: parent.horizontalCenter
                text: "Month"
                MouseArea {
                    anchors.fill: parent
                    onClicked: {
                        fnZoomOutOfDayView()
                    }
                }
            }
            Text {
                anchors.right: parent.right
                text: "Right"
                MouseArea {
                    anchors.fill: parent
                    onClicked: {
                        fnShowNextView();
                    }
                }
            }

    }


    Component {
        id: dayViewComponent
        Column {
            x: 0; y: 20
            transform: dayViewScale
            spacing: 10
            z:0
            property ListModel dayModel

            Row {
                Repeater {
                    model: dayNames
                    delegate:
                        Rectangle {
                            width: 30; height: 15

                            Text {
                                anchors.verticalCenter: parent.verticalCenter
                                anchors.right: parent.right
                                text: qsTr(name)
                                font.bold: true

                            }
                            MouseArea {
                                anchors.fill: parent

                            }
                        }

                }
            }

            Grid {
                spacing: 0
                columns: 7
                opacity: 1

                Repeater {

                    model: dayModel
                    delegate: Rectangle {
                        id:delegate
                        width: 30; height: 17

                        Text {
                            anchors.verticalCenter: parent.verticalCenter
                            anchors.right: parent.right
                            text: name
                            color: inRange?"black":"lightgrey"
                        }
                        MouseArea {
                            anchors.fill: parent
                            onClicked: {

                            }
                        }
                    }
                }


            }
        }
    }
    Component {
        id: monthViewComponent

        Grid {

            x: 0; y: 20
            opacity: 0
            spacing: 12
            columns: 4
            transform: monthViewScale
            z: 0
            Repeater {

                model: monthNames
                delegate: Rectangle {
                    id:monthDelegate
                    width: 40; height: 35
                    color: month_mousearea.containsMouse || monthId == index? "blue": "white"
                    Text {
                        anchors.horizontalCenter: parent.horizontalCenter
                        anchors.verticalCenter: parent.verticalCenter
                        text: qsTr(name)
                    }
                    MouseArea {
                        id: month_mousearea
                        hoverEnabled: true
                        anchors.fill: parent
                        onClicked: {
                            fnZoomIntoDayView(index)
                        }
                    }
                }
            }


        }
    }

    ParallelAnimation {
        id: zoomOutToMonth
        PropertyAnimation {

            target: dayViewScale
            properties: "xScale, yScale"
            from: 1; to: 0.5
        }
        PropertyAnimation {
            target: monthViewScale
            properties: "xScale, yScale"
            from: 4; to: 1
        }

        PropertyAnimation {
            target: monthView
            property: "opacity"
            to: 1
        }

        PropertyAnimation {
            target: monthView
            properties: "x"
            from: monthView.width/2 - (monthView.children[monthId].x + monthView.children[monthId].width/2) + 10
            to: 10
        }

        PropertyAnimation {
            target: monthView
            properties: "y"
            from: monthView.height/2 - (monthView.children[monthId].y + monthView.children[monthId].height/2) + 30
            to: 20
        }

        PropertyAnimation {
            target: dayView
            property: "opacity"
            to: 0
        }
    }

    ParallelAnimation {
        id: zoomInToDay
        PropertyAnimation {

            target: dayViewScale
            properties: "xScale, yScale"
            from: 0.5; to: 1
        }
        PropertyAnimation {
            target: monthViewScale
            properties: "xScale, yScale"
            from: 1; to: 4
        }

        PropertyAnimation {
            target: monthView
            property: "opacity"
            to: 0
        }
        PropertyAnimation {
            target: dayView
            property: "opacity"
            to: 1
        }

        PropertyAnimation {
            target: monthView
            properties: "x"
            to: monthView.width/2 - (monthView.children[monthId].x + monthView.children[monthId].width/2)+10
        }

        PropertyAnimation {
            target: monthView
            properties: "y"
            to: monthView.height/2 - (monthView.children[monthId].y + monthView.children[monthId].height/2)+20
        }
    }

    ParallelAnimation {
        id: showPreviosAnimation

        PropertyAnimation {
            target: currentView
            property: "x"
            to: 220
        }
        PropertyAnimation {
            target: nextView
            property: "x"
            to: currentViewType < 1? 0: 10
        }
        onCompleted: fnAnimationComplete()
    }

    ParallelAnimation {
        id: showNextAnimation

        PropertyAnimation {

            target: currentView
            property: "x"
            to: -220
        }
        PropertyAnimation
        {
            target: nextView
            property: "x"
            to: currentViewType < 1? 0: 10
        }
        onCompleted: fnAnimationComplete()

    }
}
