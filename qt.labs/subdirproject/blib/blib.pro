#-------------------------------------------------
#
# Project created by QtCreator 2011-05-25T12:47:46
#
#-------------------------------------------------

QT       -= gui

TARGET = blib
TEMPLATE = lib
CONFIG += staticlib

SOURCES += blib.cpp

HEADERS += blib.h
unix:!symbian {
    maemo5 {
        target.path = /opt/usr/lib
    } else {
        target.path = /usr/lib
    }
    INSTALLS += target
}

CONFIG(debug, debug|release) {
    TARGET = blibd
}

DESTDIR = ..

