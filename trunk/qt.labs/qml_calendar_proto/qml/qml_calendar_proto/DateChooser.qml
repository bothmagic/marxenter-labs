import QtQuick 1.1

Item {
    id: dateChooser
    property variant selectedDate: new Date()
    property color selectColor: "#ccedff"
    property color selectBorderColor: "#008df2"

    QtObject {
        id: p
        property int currentViewType:0
        property Item currentView
        property Item nextView
        property variant currentDate
        property ParallelAnimation anim

        property ListModel dayNames: ListModel {}

        property ListModel monthNames: ListModel {}

        property int dayWidth: 25
        property int dayHeight: 20

        property int monthWidth: 40
        property int monthHeight: 35

    }



    width: 220
    height: 175
    clip:true


    function fnViewZoomOut() {
        if (p.currentViewType > 1 || (p.anim && p.anim.running))
            return;

        p.nextView = fnCreateView(p.currentViewType+1, p.currentDate, {})
        var selId = p.nextView.selectionIndex

        var currentViewScale = p.currentView.myScale;
        var nextViewScale = p.nextView.myScale;

        fnSetScale(currentViewScale, nextViewScale, selId)

        p.currentViewType++;
        fnUpdateHeader()

        p.anim = anim_viewzoomout_comp.createObject(p.currentView)
        p.anim.startX = p.currentView.width/2 - currentViewScale.origin.x
        p.anim.startY = p.currentView.height/2 - currentViewScale.origin.y

        p.anim.start()

    }

    function fnViewZoomIn(newMonthIdx) {
        if (p.currentViewType == 0 || (p.anim && p.anim.running))
            return;
        var newDate;
        switch (p.currentViewType-1) {
        case 0:
            newDate = new Date(p.currentDate.getFullYear(), newMonthIdx, p.currentDate.getDate())
            break;
        case 1:
            newDate = new Date(p.currentView.model.get(newMonthIdx).name, p.currentDate.getMonth(), p.currentDate.getDate())
            break;
        }

        p.nextView = fnCreateView(p.currentViewType-1 ,newDate, {z:0})
        p.currentView.z = 1

        var selId = newMonthIdx
        p.currentView.selectionIndex = newMonthIdx

        var currentViewScale = p.currentView.myScale;
        var nextViewScale = p.nextView.myScale;

        fnSetScale(currentViewScale, nextViewScale, selId)
        p.currentViewType--;
        fnUpdateHeader()
        p.anim = anim_viewzoomin_comp.createObject(p.currentView)
        p.anim.endX =
                p.currentView.width/2 - (currentViewScale.origin.x)+10
        p.anim.endY =
                p.currentView.height/2 - (currentViewScale.origin.y)+22
        p.anim.start()


    }

    function fnSetScale(currentViewScale, nextViewScale, selId) {
        var selIdY = (Math.floor(selId / 4) * (p.monthHeight +12)) + p.monthHeight/2
        var selIdX = (selId % 4) * (p.monthWidth + 12) + p.monthWidth/2

        currentViewScale.origin.x = selIdX
        currentViewScale.origin.y = selIdY

        nextViewScale.origin.x = currentViewScale.origin.x
        nextViewScale.origin.y = currentViewScale.origin.y

        //        console.debug("currentViewScale.x " + currentViewScale.origin.x)
        //        console.debug("currentViewScale.y " + currentViewScale.origin.y)
        //        console.debug("nextViewScale.x " + nextViewScale.origin.x)
        //        console.debug("nextViewScale.y " + nextViewScale.origin.y)
    }

    /**
      *
      */
    function fnShowPreviousView() {
        if (p.anim && p.anim.running) return
        switch(p.currentViewType) {
        case 0:
            p.currentDate = new Date(p.currentDate.getFullYear(), p.currentDate.getMonth()-1, p.currentDate.getDate())
            break;
        case 1:
            p.currentDate = new Date(p.currentDate.getFullYear()-1, p.currentDate.getMonth(), p.currentDate.getDate())
            break;
        case 2:
            p.currentDate = new Date(p.currentDate.getFullYear()-10, p.currentDate.getMonth(), p.currentDate.getDate())
            break;
        }
        p.nextView = fnCreateView(p.currentViewType
                                  ,p.currentDate
                                  , {x: -220, opacity: 1})

        fnUpdateHeader()
        showPreviosAnimation.start();
        p.anim = showPreviosAnimation
    }

    function fnShowNextView() {
        if (p.anim && p.anim.running) return
        switch(p.currentViewType) {
        case 0:
            p.currentDate = new Date(p.currentDate.getFullYear(), p.currentDate.getMonth()+1, p.currentDate.getDate())
            break;
        case 1:
            p.currentDate = new Date(p.currentDate.getFullYear()+1, p.currentDate.getMonth(), p.currentDate.getDate())
            break;
        case 2:
            p.currentDate = new Date(p.currentDate.getFullYear()+10, p.currentDate.getMonth(), p.currentDate.getDate())
            break;
        }
        fnUpdateHeader()
        p.nextView = fnCreateView(p.currentViewType
                                  ,p.currentDate
                                  , {x: +220, opacity: 1})

        fnUpdateHeader()
        showNextAnimation.start();
        p.anim = showNextAnimation
    }


    function fnCreateView(type, date, options) {
        var view
        p.currentDate = date
        switch (type) {
        case 0:
            view = dayViewComponent.createObject(dateChooser, options);
            view.model = fnLoadModel(date, view)
            console.debug("create day view")
            break;
        case 1:
            view = monthViewComponent.createObject(dateChooser, options)
            view.model = p.monthNames
            view.selectionIndex = date.getMonth()
            console.debug("create month view")
            break;
        case 2:
            view = monthViewComponent.createObject(dateChooser, options)
            var startYear = date.getFullYear() - date.getFullYear()%10;
            if (date.getFullYear()%10 == 0) startYear -= 10
            var model = dynamicmodel.createObject(view)
            view.selectionIndex = p.currentDate.getFullYear()-startYear
            date.setFullYear(startYear)
            for (var i = 0; i < 12; i++) {
                model.append({name:Qt.formatDate(date, "yyyy"), inRange: i>0 && i<11});
                date.setFullYear(date.getFullYear()+1)
            }

            view.model = model
        }



        return view
    }

    /**
      * release current view.
      */
    function fnAnimationComplete(anim) {

        // if (p.currentView != dayView || p.currentViewType == 0) {
        p.currentView.destroy();
        //}
        p.currentView = p.nextView;
        if (anim)
            anim.destroy()
    }

    function fnUpdateHeader() {
        switch (p.currentViewType) {
        case 0: txt_header.text = Qt.formatDate(p.currentDate, "MMMM") + Qt.formatDate(p.currentDate, " yyyy"); break;
        case 1: txt_header.text = Qt.formatDate(p.currentDate, "yyyy"); break;
        case 2: txt_header.text = p.nextView.model.get(1).name + " - " + p.nextView.model.get(10).name
        }
    }

    function fnLoadModel(da, view) {

        var daym = dynamicmodel.createObject(view);

        var year = da.getFullYear();
        var month = da.getMonth();
        da.setDate(-1)
        da.setDate(da.getDate() - da.getDay()+1);
        //console.log("load daymodel for " + p.currentDate);

        for (var i = 0; i < 42; i++) {
            if (fnEqualsDate(da, p.currentDate))
                view.selectionIndex = i
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

        // generate models

        var d = new Date(2012, 9,1)
        for (var i = 0; i < 7; i++) {
            p.dayNames.append({name: Qt.formatDate(d, "ddd")})
            d.setDate(d.getDate()+1)
        }

        d = new Date(2012,0,1)
        for (var i = 0; i < 12; i++) {
            p.monthNames.append({name: Qt.formatDate(d, "MMM"), inRange:true})
            d.setMonth(d.getMonth()+1)
        }
        p.currentView = fnCreateView(p.currentViewType, selectedDate, {})
        p.currentView.model = fnLoadModel(selectedDate, p.currentView)
        fnUpdateHeader()

    }

    Component {
        id:dynamicmodel
        ListModel {

        }
    }


    Item {
        id: header
        y:1
        anchors.right: parent.right
        anchors.left: parent.left
        anchors.leftMargin: 5
        anchors.rightMargin:5
        height: 20
        z: 10
        Rectangle {
            anchors.fill: parent
        }
        Image {
            anchors.left: parent.left
            source: "left_navi.svg"
            MouseArea {
                anchors.fill: parent
                onClicked: {
                    fnShowPreviousView();
                }
            }
        }



        Item {
            width: 130
            height:18
            anchors.horizontalCenter: parent.horizontalCenter

            Rectangle {
                anchors.topMargin: 0
                anchors.fill:parent
                color: selectColor
                border.color:selectBorderColor
                radius:2
                opacity: header_mousearea.containsMouse? 1:0
                Behavior on opacity {
                    NumberAnimation {
                        duration:200
                    }
                }


            }

            MouseArea {
                id:header_mousearea
                anchors.fill: parent
                hoverEnabled: true
                onClicked: {
                    fnViewZoomOut()
                }
            }
            Row {
                anchors.horizontalCenter: parent.horizontalCenter
                spacing: 5
                Text {

                    id:txt_header
                    text: "Month"
                }

                Image {
                    anchors.right:parent.right
                    source: "zoom_out.svg"

                }
            }


        }
        Image {
            anchors.right: parent.right
            source: "right_navi.svg"
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
            property int selectionIndex

            x: 10; y: 20
            transform: myScale
            spacing: 5
            z:0

            Row {
                spacing: 3
                Repeater {
                    model: p.dayNames
                    delegate:
                        Rectangle {
                        width: p.dayWidth; height: p.dayHeight

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
                spacing: 3
                columns: 7
                opacity: 1

                Repeater {

                    model: parent.parent.model
                    delegate: Item {
                        id:delegate
                        width: p.dayWidth; height: p.dayHeight
                        Rectangle {
                            anchors.fill: parent
                            color: selectColor
                            border.color: selectBorderColor
                            opacity: dayview_mousearea.containsMouse
                                     || selectionIndex == index? 1:0

                            Behavior on opacity {
                                NumberAnimation {
                                    duration:200
                                }
                            }
                        }
                        Text {
                            anchors.verticalCenter: parent.verticalCenter
                            anchors.right: parent.right
                            anchors.rightMargin: 5
                            text: date.getDate()
                            color: inRange?"black":"lightgrey"
                        }
                        MouseArea {
                            id: dayview_mousearea
                            hoverEnabled: true
                            anchors.fill: parent

                            onClicked: {
                                selectedDate = date
                                p.currentDate = date
                                selectionIndex = index
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
            property int selectionIndex
            x: 10; y: 40
            opacity: 0
            spacing: 12
            columns: 4

            transform: myScale
            z: 1

            Repeater {

                model: parent.model
                delegate: Item {
                    id:monthDelegate
                    width: p.monthWidth; height: p.monthHeight
                    Rectangle {
                        anchors.fill: parent
                        color: selectColor
                        border.color: selectBorderColor
                        opacity: month_mousearea.containsMouse
                                 || selectionIndex == index? 1:0

                        Behavior on opacity {
                            NumberAnimation {
                                duration:200
                            }
                        }
                    }


                    Text {
                        anchors.horizontalCenter: parent.horizontalCenter
                        anchors.verticalCenter: parent.verticalCenter
                        text: qsTr(name)
                        color: inRange?"black":"lightGrey"
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

                target:p.currentView.myScale
                properties: "xScale, yScale"
                from: 1; to: 0.5
            }
            PropertyAnimation {
                target: p.nextView.myScale
                properties: "xScale, yScale"
                from: 4; to: 1
            }

            PropertyAnimation {
                target: p.nextView
                property: "opacity"
                to: 1
            }

            PropertyAnimation {
                target: p.nextView
                properties: "x"
                from: startX
                to: 10
            }

            PropertyAnimation {
                target: p.nextView
                properties: "y"
                from: startY
                to: p.currentViewType < 1?22:40
            }

            PropertyAnimation {
                target: p.currentView
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

                target: p.nextView.myScale
                properties: "xScale, yScale"
                from: 0.3; to: 1
            }
            PropertyAnimation {
                target: p.currentView.myScale
                properties: "xScale, yScale"
                from: 1; to: 4
            }

            PropertyAnimation {
                target: p.currentView
                property: "opacity"
                to: 0
            }
            PropertyAnimation {
                target: p.nextView
                property: "opacity"
                to: 1
            }

            PropertyAnimation {
                target: p.currentView
                properties: "x"
                to: endX
            }

            PropertyAnimation {
                target: p.currentView
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
            target: p.currentView
            property: "x"
            to: 220
        }
        PropertyAnimation {
            target: p.nextView
            property: "x"
            to: 10
        }
        onCompleted: fnAnimationComplete()
    }

    ParallelAnimation {
        id: showNextAnimation

        PropertyAnimation {

            target: p.currentView
            property: "x"
            to: -220
        }
        PropertyAnimation
        {
            target: p.nextView
            property: "x"
            to: 10
        }
        onCompleted: fnAnimationComplete()

    }
}
