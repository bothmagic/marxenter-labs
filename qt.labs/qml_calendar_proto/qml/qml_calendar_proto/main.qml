import QtQuick 1.1

Rectangle {
    id: calendarChooser
    property int monthId: 0
    property int yearId: 0

    property int currentViewType:0
    property Item currentView
    property Item nextView
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


    function fnViewZoomOut() {
        if (currentViewType > 1)
            return;

        nextView = fnCreateView(currentViewType+1, currentDate, {})
        var selId
        switch (currentViewType) {
            case 0: selId = monthId;break;
            case 1: selId = yearId;break;
        }

        var currentViewScale = currentView.myScale;
        var nextViewScale = nextView.myScale;

        currentViewScale.origin.x = nextView.children[selId].x + nextView.children[selId].width/2
        currentViewScale.origin.y = nextView.children[selId].y + nextView.children[selId].height/2

        nextViewScale.origin.x = nextView.children[selId].x + nextView.children[selId].width/2
        nextViewScale.origin.y = nextView.children[selId].y + nextView.children[selId].height/2

        //console.log(gridScale.origin.x + ", "+gridScale.origin.y)
        currentViewType++;
        fnUpdateHeader()

        var anim = anim_viewzoomout_comp.createObject(currentView)
        anim.startX = nextView.width/2 - (nextView.children[selId].x + nextView.children[selId].width/2) + 10
        anim.startY = nextView.height/2 - (nextView.children[selId].y + nextView.children[selId].height/2) + 30
        anim.start()

    }

    function fnViewZoomIn(newMonthIdx) {
        if (currentViewType == 0)
            return;

        nextView = fnCreateView(currentViewType-1
                                ,new Date(currentDate.getFullYear(), newMonthIdx, currentDate.getDate())
                                , {})
        monthId = newMonthIdx;

        var currentViewScale = currentView.myScale;
        var nextViewScale = nextView.myScale;

        nextViewScale.origin.x = currentView.children[monthId].x + currentView.children[monthId].width/2
        nextViewScale.origin.y = currentView.children[monthId].y + currentView.children[monthId].height/2

        currentViewScale.origin.x = currentView.children[monthId].x + currentView.children[monthId].width/2
        currentViewScale.origin.y = currentView.children[monthId].y + currentView.children[monthId].height/2

        //console.log(gridScale.origin.x + ", "+gridScale.origin.y)
        currentViewType--;
        fnUpdateHeader()
        var anim = anim_viewzoomin_comp.createObject(currentView)
        anim.endX =
            currentView.width/2 - (currentView.children[monthId].x + currentView.children[monthId].width/2)+10
        anim.endY =
            currentView.height/2 - (currentView.children[monthId].y + currentView.children[monthId].height/2)+20
        anim.start()


    }

    /**
      *
      */
    function fnShowPreviousView() {

        switch(currentViewType) {
        case 0:
            currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth()-1, currentDate.getDate())
            break;
        case 1:
            currentDate = new Date(currentDate.getFullYear()-1, currentDate.getMonth(), currentDate.getDate())
            break;
        }
        nextView = fnCreateView(currentViewType
                                ,currentDate
                                , {x: -220, opacity: 1})

        fnUpdateHeader()
        showPreviosAnimation.start();
    }

    function fnShowNextView() {
        switch(currentViewType) {
        case 0:
            currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth()+1, currentDate.getDate())
            break;
        case 1:
            currentDate = new Date(currentDate.getFullYear()+1, currentDate.getMonth(), currentDate.getDate())
            break;
        }
        fnUpdateHeader()
        nextView = fnCreateView(currentViewType
                                ,currentDate
                                , {x: +220, opacity: 1})

        fnUpdateHeader()
        showNextAnimation.start();

    }


    function fnCreateView(type, date, options) {
        var view
        switch (type) {
        case 0:
            view = dayViewComponent.createObject(calendarChooser, options);
            view.model = fnLoadModel(date, view)
            console.debug("create day view")
            break;
        case 1:
            view = monthViewComponent.createObject(calendarChooser, options)
            view.model = monthNames
            monthId = date.getMonth()
            console.debug("create month view")
            break;
        case 2:
            view = monthViewComponent.createObject(calendarChooser, options)
            var startYear = date.getFullYear() - date.getFullYear()%10;
            var model = dynamicmodel.createObject(view)
            yearId = currentDate.getFullYear()-startYear
            date.setFullYear(startYear)
            for (var i = 0; i < 12; i++) {
                model.append({name:Qt.formatDate(date, "yyyy")});
                date.setFullYear(date.getFullYear()+1)
            }
            console.debug("yearid is " +yearId)
            view.model = model
        }
        return view
    }

    /**
      * release current view.
      */
    function fnAnimationComplete(anim) {

       // if (currentView != dayView || currentViewType == 0) {
            currentView.destroy();
        //}
        currentView = nextView;
        if (anim)
            anim.destroy()
    }

    function fnUpdateHeader() {
        switch (currentViewType) {
            case 0: txt_header.text = Qt.formatDate(currentDate, "MMMM yyyy"); break;
            case 1: txt_header.text = Qt.formatDate(currentDate, "yyyy"); break;
        }
    }

    function fnLoadModel(da, view) {

        var daym = dynamicmodel.createObject(view);

        currentDate = da

        var year = da.getFullYear();
        var month = da.getMonth();
        da.setDate(1)
        da.setDate(da.getDate() - da.getDay() +1 );
        console.log("load daymodel for " + currentDate);

        for (var i = 0; i < 42; i++) {
            daym.append({date: da, inRange: da.getMonth() == month})
            da.setDate(da.getDate()+1);
        }

        return daym
    }

    function fnEqualsDate(d1, d2) {
        return d1.getDate() == d2.getDate() && d1.getMonth() == d2.getMonth()
                && d1.getFullYear() == d2.getFullYear();
    }


    Component.onCompleted: {

        currentView = fnCreateView(currentViewType, new Date(), {})
        //monthView = monthViewComponent.createObject(calendarChooser);
        //currentView = dayView

        currentView.model = fnLoadModel(new Date(), currentView)

        //monthView.children[currentDate.getMonth()].color = "blue"
        //dayView.children[dayId].color = "blue"
        fnUpdateHeader()

    }

    Component {
        id:dynamicmodel
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
                        fnViewZoomOut()
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
        id:dayViewComponent
        Column {

            property ListModel model
            property Scale myScale: Scale {}

            x: 0; y: 20
            transform: myScale
            spacing: 10
            z:0

            Row {
                Repeater {
                    model: dayNames
                    delegate:
                        Rectangle {
                            width: 30; height: 15

            z:0
            property ListModel dayModel

                            Text {
                                anchors.verticalCenter: parent.verticalCenter
                                anchors.right: parent.right
                                text: qsTr(name)
                                font.bold: true

                            }
                        }
                }
            }

            Grid {
                spacing: 0
                columns: 7
                opacity: 1

                Repeater {

                    model: parent.parent.model
                    delegate: Rectangle {
                        id:delegate
                        width: 30; height: 17
                        color: fnEqualsDate(date, currentDate) ? "blue": "white"
                        Text {
                            anchors.verticalCenter: parent.verticalCenter
                            anchors.right: parent.right
                            text: date.getDate()
                            color: inRange?"black":"lightgrey"
                        }
                        MouseArea {
                            id: dayview_mousearea
                            hoverEnabled: true
                            anchors.fill: parent

                            onClicked: {
                                currentDate = date
                            }
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
            property Scale myScale: Scale {}
            property ListModel model
            x: 0; y: 20
            opacity: 0
            spacing: 12
            columns: 4

            transform: myScale
            z: 1

            Repeater {

                model: parent.model
                delegate: Rectangle {
                    id:monthDelegate
                    width: 40; height: 35
                    color: month_mousearea.containsMouse
                           || monthId == index? "blue": "white"
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
                            fnViewZoomIn(index)
                        }
                    }
                }
            }


        }
    }


    Component {
        id: anim_viewzoomout_comp
        ParallelAnimation {
            id:anim_viewzoomout;
            property int startX
            property int startY

            PropertyAnimation {

                target:currentView.myScale
                properties: "xScale, yScale"
                from: 1; to: 0.5
            }
            PropertyAnimation {
                target: nextView.myScale
                properties: "xScale, yScale"
                from: 4; to: 1
            }

            PropertyAnimation {
                target: nextView
                property: "opacity"
                to: 1
            }

            PropertyAnimation {
                target: nextView
                properties: "x"
                from: startX
                to: 10
            }

            PropertyAnimation {
                target: nextView
                properties: "y"
                from: startY
                to: 20
            }

            PropertyAnimation {
                target: currentView
                property: "opacity"
                to: 0
            }

            onCompleted: {

                fnAnimationComplete(anim_viewzoomout);
            }
        }
    }



    Component {
        id: anim_viewzoomin_comp
        ParallelAnimation {
            id:anim_viewzoomin
            property int endX
            property int endY
            PropertyAnimation {

                target: nextView.myScale
                properties: "xScale, yScale"
                from: 0.3; to: 1
            }
            PropertyAnimation {
                target: currentView.myScale
                properties: "xScale, yScale"
                from: 1; to: 4
            }

            PropertyAnimation {
                target: currentView
                property: "opacity"
                to: 0
            }
            PropertyAnimation {
                target: nextView
                property: "opacity"
                to: 1
            }

            PropertyAnimation {
                target: currentView
                properties: "x"
                to: endX
            }

            PropertyAnimation {
                target: currentView
                properties: "y"
                to: endY
            }
            onCompleted: {
                fnAnimationComplete(anim_viewzoomin);
            }
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
