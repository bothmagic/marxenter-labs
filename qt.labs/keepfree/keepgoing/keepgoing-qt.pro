#-------------------------------------------------
#
# Project created by QtCreator 2010-10-11T13:17:21
#
#-------------------------------------------------

QT       += core gui sql opengl

TARGET = keepgoing-qt
TEMPLATE = app


SOURCES += src/main.cpp\
        src/kgmain.cpp \
    src/tasklistform.cpp \
    src/actions/newtask.cpp \
    src/tasklistview.cpp \
    src/todographicsitem.cpp \
    src/shortdescredit.cpp \
    src/taskcontextitemdelegate.cpp \
    src/flowlayout.cpp \
    src/model/taskmodel.cpp \
    src/model/contextmodel.cpp\
    src/ui/qgraphicslisttext.cpp \
    src/ui/contexttextobject.cpp \
    src/ui/kgvisualscene.cpp \
    src/command/kgnewitem.cpp \
    src/ui/kgvisualitem.cpp \
    src/ui/kgvisualview.cpp \
    src/model/kgvisualitemfactory.cpp \
    src/testitem.cpp \
    src/ui/kganimationfactory.cpp \
    src/ui/kgflowpane.cpp \
    src/ui/kgvisualitemlist.cpp \
    src/ui/kgpixmapitem.cpp \
    src/util/kgressourceloader.cpp \
    src/ui/kgdetailflowitem.cpp \
    src/ui/kgvisualtextitem.cpp \
    src/ui/kgvisualitemgroup.cpp \
    src/ui/kgvisualappenditem.cpp \
    src/util/kgcommandfactory.cpp \
    src/ui/kgvisualcontextlist.cpp \
    src/model/kggroupfactory.cpp \
    src/ui/kgprojectlistwidget.cpp \
    src/model/kgitemmodel.cpp

HEADERS  += src/kgmain.h \
    src/connection.h \
    src/tasklistform.h \
    src/databaseutil.h \
    src/actions/newtask.h \
    src/tasklistview.h \
    src/todographicsitem.h \
    src/shortdescredit.h \
    src/taskcontextitemdelegate.h \
    src/flowlayout.h \
    src/model/taskmodel.h \
    src/model/contextmodel.h\
    src/styles.h \
    src/ui/qgraphicslisttext.h \
    src/ui/contexttextobject.h \
    src/ui/kgvisualscene.h \
    src/command/kgnewitem.h \
    src/ui/kgvisualitem.h \
    src/ui/kgvisualview.h \
    src/util/listutils.h \
    src/model/kgvisualitemfactory.h \
    src/testitem.h \
    src/ui/kganimationfactory.h \
    src/ui/kgflowpane.h \
    src/ui/kgvisualitemlist.h \
    src/ui/kgpixmapitem.h \
    src/util/kgressourceloader.h \
    src/ui/kgdetailflowitem.h \
    src/ui/kgvisualtextitem.h \
    src/ui/kgvisualitemgroup.h \
    src/ui/kgvisualappenditem.h \
    src/util/kgcommandfactory.h \
    src/ui/kgvisualcontextlist.h \
    src/model/kggroupfactory.h \
    src/ui/kgprojectlistwidget.h \
    src/model/kgitemmodel.h


INCLUDEPATH += src \
               ../keepfree-share/src

LIBS += -lkeepfree-share

#unix {
#    LIBS += -L../keepfree-share-build-desktop
#}

#win32:debug {
    LIBS += -L../keepfree-share-build-desktop/debug
#}

#win32:release {
#    LIBS += -L../keepfree-share-build-desktop/release
#}


FORMS    += src/kgmain.ui \
    src/tasklistform.ui

RESOURCES += \
    ressources/ressources.qrc
