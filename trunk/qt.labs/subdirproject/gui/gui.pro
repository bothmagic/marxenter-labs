#-------------------------------------------------
#
# Project created by QtCreator 2011-05-25T12:46:21
#
#-------------------------------------------------

QT       += core gui

TARGET = gui
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp

HEADERS  += mainwindow.h

FORMS    += mainwindow.ui

INCLUDEPATH += ../blib

LIBS += -L ..

CONFIG(debug, debug|release) {
    LIBS += -l blibd
    PRE_TARGETDEPS += ../libblibd.a
}
else {
    LIBS += -blib
    PRE_TARGETDEPS += ../libblib.a
}


