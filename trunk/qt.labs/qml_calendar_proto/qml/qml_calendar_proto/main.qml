import QtQuick 1.1

Rectangle {
    id: calendarChooser
    property int monthId: 0
    property int yearId: 0
    property int dayId:8

    property Item monthView
    property Item dayView
    property int currentViewType:0
    property Item currentView
    property Item nextView
    property Scale monthViewScale: Scale{}
    property Scale dayViewScale: Scale{}

    width: 200
    height: 200
    clip:true

    function fnZoomOutOfDayView() {

        dayViewScale.origin.x = monthView.children[monthId].x + monthView.children[monthId].width/2
        dayViewScale.origin.y = monthView.children[monthId].y + monthView.children[monthId].height/2

        monthViewScale.origin.x = monthView.children[monthId].x + monthView.children[monthId].width/2
        monthViewScale.origin.y = monthView.children[monthId].y + monthView.children[monthId].height/2
        currentView = monthView;
        currentViewType = 1
        //console.log(gridScale.origin.x + ", "+gridScale.origin.y)
        zoomOutToMonth.start();
    }

    function fnZoomIntoDayView(newMonthIdx) {
        monthView.children[monthId].color = "white"
        monthId = newMonthIdx;
        monthView.children[monthId].color = "blue"
        dayViewScale.origin.x = monthView.children[monthId].x + monthView.children[monthId].width/2
        dayViewScale.origin.y = monthView.children[monthId].y + monthView.children[monthId].height/2

        monthViewScale.origin.x = monthView.children[monthId].x + monthView.children[monthId].width/2
        monthViewScale.origin.y = monthView.children[monthId].y + monthView.children[monthId].height/2
        currentView = dayView;
        currentViewType = 0
        //console.log(gridScale.origin.x + ", "+gridScale.origin.y)
        zoomInToDay.start();
    }

    function fnShowPreviousView() {

        switch (currentViewType) {
        case 0:
            nextView = dayViewComponent.createObject(calendarChooser, {x: -200})
            nextView.children[dayId].color = "blue"
            break;
        case 1:
            nextView = monthViewComponent.createObject(calendarChooser, {x: -200, opacity:1})
            nextView.children[monthId].color = "blue"
        }

        showPreviosAnimation.start();
    }

    function fnShowNextView() {
        switch (currentViewType) {
        case 0:
            nextView = dayViewComponent.createObject(calendarChooser, {x: 200})
            nextView.children[dayId].color = "blue"
            break;
        case 1:
            nextView = monthViewComponent.createObject(calendarChooser, {x: 200, opacity:1})
            nextView.children[monthId].color = "blue"
        }

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


    Component.onCompleted: {

        dayView = dayViewComponent.createObject(calendarChooser);
        monthView = monthViewComponent.createObject(calendarChooser);
        currentView = dayView
        for (var i = 0 ; i < 48; i++) {
            daymodel.append({name: ""+i});
        }

        for (i = 0 ; i < 12; i++) {
            monthmodel.append({name: "Month"+i});
        }

        monthView.children[monthId].color = "blue"
        dayView.children[dayId].color = "blue"


    }

    ListModel {
        id:daymodel
    }

    ListModel {
        id:monthmodel
    }


    Item {
        id: header
        anchors.right: parent.right
        anchors.left: parent.left
        height: 20

        Row {
            anchors.horizontalCenter: parent.horizontalCenter
            Text {

                text: "Left"
                MouseArea {
                    anchors.fill: parent
                    onClicked: {
                        fnShowPreviousView();
                    }
                }
            }

            Text {

                text: "Month"
                MouseArea {
                    anchors.fill: parent
                    onClicked: {
                        fnZoomOutOfDayView()
                    }
                }
            }
            Text {

                text: "Right"
                MouseArea {
                    anchors.fill: parent
                    onClicked: {
                        fnShowNextView();
                    }
                }
            }
        }
    }


    Component {
        id: dayViewComponent

        Grid {
            x: 0; y: 20

            spacing: 0
            columns: 7
            opacity: 1
            transform: dayViewScale

            Repeater {

                model: daymodel
                delegate: Rectangle {
                    id:delegate
                    width: 25; height: (index < 7)? 25: 15

                    Text {
                        Component.onCompleted: {
                            if (index <7) {
                                anchors.top = parent.top
                                font.bold = true
                            } else
                                anchors.verticalCenter = parent.verticalCenter
                        }
                        anchors.right: parent.right
                        text: name
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
    Component {
        id: monthViewComponent

        Grid {

            x: 0; y: 20
            opacity: 0
            spacing: 10
            columns: 4
            transform: monthViewScale
            Repeater {

                model: monthmodel
                delegate: Rectangle {
                    id:monthDelegate
                    width: 40; height: 35

                    Text {
                        anchors.horizontalCenter: parent.horizontalCenter
                        anchors.verticalCenter: parent.verticalCenter
                        text: name
                    }
                    MouseArea {
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
            from: monthView.width/2 - (monthView.children[monthId].x + monthView.children[monthId].width/2)
            to: 0
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
            to: monthView.width/2 - (monthView.children[monthId].x + monthView.children[monthId].width/2)
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
            to: 200
        }
        PropertyAnimation {
            target: nextView
            property: "x"
            to: 0
        }
        onCompleted: fnAnimationComplete()
    }

    ParallelAnimation {
        id: showNextAnimation

        PropertyAnimation {
            target: currentView
            property: "x"
            to: -200
        }
        PropertyAnimation {
            target: nextView
            property: "x"
            to: 0
        }
        onCompleted: fnAnimationComplete()

    }
}
