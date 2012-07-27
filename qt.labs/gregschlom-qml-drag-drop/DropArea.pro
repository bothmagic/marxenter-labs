#-------------------------------------------------
#
# Project created by QtCreator 2010-07-07T13:51:33
#
#-------------------------------------------------

QT       += core gui declarative

TARGET = DropArea
TEMPLATE = app


SOURCES += main.cpp\
	DeclarativeDropArea.cpp \
	DeclarativeDragArea.cpp \
	DeclarativeMimeData.cpp \
	DeclarativeDragDropEvent.cpp \
        DragAndDropPlugin.cpp

HEADERS  += DeclarativeDropArea.h \
	DeclarativeDragArea.h \
	DeclarativeMimeData.h \
	DeclarativeDragDropEvent.h \
        DragAndDropPlugin.h

OTHER_FILES += \
	app.qml

RESOURCES += \
	resources.qrc
