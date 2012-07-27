#-------------------------------------------------
#
# Project created by QtCreator 2011-01-04T13:52:46
#
#-------------------------------------------------
QT       += core gui sql

TEMPLATE = lib
CONFIG += static
TARGET = keepfree-share
#DESTDIR = ../libs


SOURCES += \
    src/ui/qgraphicslayouttext.cpp \
    src/ui/qgraphicscompleter.cpp \
    src/ui/contextcombobox.cpp \
    src/qxt/qxtglobalshortcut.cpp \
    src/qxt/qxtglobal.cpp \
    src/ui/kvisualitem.cpp

HEADERS += \
    src/ui/qgraphicslayouttext.h \
    src/ui/qgraphicscompleter.h \
    src/ui/contextcombobox.h \
    src/qxt/qxtglobalshortcut_p.h \
    src/qxt/qxtglobalshortcut.h \
    src/qxt/qxtglobal.h \
    src/ui/kvisualitem.h

unix:!macx {
    SOURCES += src/qxt/qxtglobalshortcut_x11.cpp
}
macx {
    SOURCES += src/qxt/qxtglobalshortcut_mac.cpp
}
win32 {
    SOURCES += src/qxt/qxtglobalshortcut_win.cpp
}

