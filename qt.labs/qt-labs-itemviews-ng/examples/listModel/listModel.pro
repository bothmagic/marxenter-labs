TEMPLATE = app
TARGET = listModel
include (../examples.pri)
CONFIG += debug

HEADERS += model.h
SOURCES += main.cpp \
           model.cpp
