TEMPLATE = app
TARGET = chat 
include (../examples.pri)

CONFIG -= app_bundle
RESOURCES += chat.qrc
HEADERS += chatmodel.h chatview.h borderpixmap.h
SOURCES += chatmodel.cpp chatview.cpp borderpixmap.cpp main.cpp
