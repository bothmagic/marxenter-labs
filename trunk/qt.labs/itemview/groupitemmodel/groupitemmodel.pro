#-------------------------------------------------
#
# Project created by QtCreator 2012-07-27T15:10:14
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = groupitemmodel
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    modelitem.cpp \
    modelitemmodel.cpp

HEADERS  += mainwindow.h \
    modelitem.h \
    modelitemmodel.h

FORMS    += mainwindow.ui
