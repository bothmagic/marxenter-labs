#-------------------------------------------------
#
# Project created by QtCreator 2012-07-07T14:45:16
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = keepcool_proto
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    kcgraphicsview.cpp \
    flowlayout.cpp \
    qgraphicslayouttext.cpp \
    kcgraphitem.cpp \
    kcgraphicsgroup.cpp \
    kcitem.cpp \
    kcdataloader.cpp

HEADERS  += mainwindow.h \
    kcgraphicsview.h \
    flowlayout.h \
    qgraphicslayouttext.h \
    kcgraphitem.h \
    kcgraphicsgroup.h \
    kcitem.h \
    kcdataloader.h

FORMS    += mainwindow.ui
