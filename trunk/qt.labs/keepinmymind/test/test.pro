#-------------------------------------------------
#
# Project created by QtCreator 2012-07-19T16:06:34
#
#-------------------------------------------------

QT       += core gui sql testlib

TARGET = tst_testtest
CONFIG   += console testcase
CONFIG   -= app_bundle

TEMPLATE = app

SOURCES += tst_testtest.cpp
DEFINES += SRCDIR=\\\"$$PWD/\\\"
INCLUDEPATH += ../src/

CONFIG(debug, debug|release) {
    LIBS += -L../src/debug -lkeepinmymind
    PRE_TARGETDEPS += ../src/debug/libkeepinmymind.a
}
else {
    LIBS += -L../src/release -lkeepinmymind
    PRE_TARGETDEPS += ../src/release/libkeepinmymind.a
}


