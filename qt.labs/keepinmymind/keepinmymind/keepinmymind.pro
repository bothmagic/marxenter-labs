#-------------------------------------------------
#
# Project created by QtCreator 2012-07-19T17:24:18
#
#-------------------------------------------------

QT       += core gui

TARGET = keepinmymind
TEMPLATE = app


SOURCES += main.cpp


INCLUDEPATH += ../src/

CONFIG(debug, debug|release) {
    LIBS += -L../src/debug -lkeepinmymind
    PRE_TARGETDEPS += ../src/debug/libkeepinmymind.a
}
else {
    LIBS += -L../src/release -lkeepinmymind
    PRE_TARGETDEPS += ../src/release/libkeepinmymind.a
}



