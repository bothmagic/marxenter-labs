#-------------------------------------------------
#
# Project created by QtCreator 2010-06-23T12:55:35
#
#-------------------------------------------------

QT       += core gui

TARGET = cube
TEMPLATE = app

SOURCES += main.cpp

contains(QT_CONFIG, opengl) {
    message(Building with OpenGL support.)
    QT += opengl

    SOURCES += mainwidget.cpp \
       geometryengine.cpp

    HEADERS += \
        mainwidget.h \
        geometryengine.h

    RESOURCES += \
        shaders.qrc \
        textures.qrc

    OTHER_FILES += \
        vshader.glsl \
        fshader.glsl
} else {
    message(OpenGL support is not available.)
}

symbian: include($$PWD/../../symbianpkgrules.pri)
maemo5: include($$PWD/../../maemo5pkgrules.pri)
contains(MEEGO_EDITION,harmattan): include($$PWD/../../harmattanpkgrules.pri)

symbian: warning(This example might not fully work on Symbian platform)
simulator: warning(This example might not fully work on Simulator platform)
