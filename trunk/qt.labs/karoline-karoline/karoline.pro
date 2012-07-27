TEMPLATE = app
TARGET = karoline
DEPENDPATH += . twitter
INCLUDEPATH += . twitter
INCLUDEPATH += /home/stibi/Programovani/Projekty/stibis-kqoauth/build/include/QtKOAuth

include($$PWD/nativequickwidgets/nativequickwidgets.pri)

QT += core gui network xml declarative
CONFIG += debug qt crypto #kqoauth
LIBS += -L/home/stibi/Programovani/Projekty/stibis-kqoauth/build/lib64 -lkqoauth

#BUILD_PATH = /home/stibi/Programovani/Projekty/Karoline
#debug:DESTDIR = $$BUILD_PATH/debug_build
#debug:OBJECTS_DIR = $$BUILD_PATH/.obj
#debug:MOC_DIR = $$BUILD_PATH/.moc
#debug:RCC_DIR = $$BUILD_PATH/.rcc
#debug:UI_DIR = $$BUILD_PATH/.ui

# Input
HEADERS += \
    karoline.h \
    mainwindow.h \
    accountsdialog.h \
    twitter/twitterApi.h \
    twitter/twitterRequest.h \
    twitter/twitterparser.h \
    timeline/timelinemodel.h \
    timeline/timelineitem.h \
    timeline/genericlistitem.h \
    twitter/userinfo.h

FORMS += \
    mainwindow.ui \
    accounts.ui \
    testdialog.ui

SOURCES += \
    karoline.cpp \
    main.cpp \
    mainwindow.cpp \
    accountsdialog.cpp \
    twitter/twitterApi.cpp \
    twitter/twitterRequest.cpp \
    twitter/twitterparser.cpp \
    timeline/timelinemodel.cpp \
    timeline/timelineitem.cpp \
    twitter/userinfo.cpp

RESOURCES += \
    resourcedata.qrc

OTHER_FILES += \
    MyItem.qml \
    TimeLineView.qml
#    ScrollBar.qml
