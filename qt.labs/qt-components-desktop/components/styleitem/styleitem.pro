TEMPLATE = lib
CONFIG += qt plugin
QT += declarative
QT += script
 
TARGET  = styleplugin

DESTDIR = ..\\plugin
OBJECTS_DIR = tmp
MOC_DIR = tmp

HEADERS += qrangemodel_p.h \
           qrangemodel.h \
           qstyleplugin.h \
           qdeclarativefolderlistmodel.h \
           qstyleitem.h

SOURCES += qrangemodel.cpp \
	   qstyleplugin.cpp \
           qdeclarativefolderlistmodel.cpp \
           qstyleitem.cpp
           

OTHER_FILES += \
    ../gallery.qml \
    ../widgets/Tab.qml \
    ../widgets/TabBar.qml \
    ../widgets/TabFrame.qml \
    ../Button.qml \
    ../ButtonRow.qml \
    ../CheckBox.qml \
    ../ChoiceList.qml \
    ../components.pro \
    ../ContextMenu.qml \
    ../Dial.qml \
    ../Frame.qml \
    ../GroupBox.qml \
    ../ProgressBar.qml \
    ../RadioButton.qml \
    ../ScrollArea.qml \
    ../ScrollBar.qml \
    ../Slider.qml \
    ../SpinBox.qml \
    ../Switch.qml \
    ../Tab.qml \
    ../TableView.qml \
    ../TabBar.qml \
    ../TabFrame.qml \
    ../TextArea.qml \
    ../TextField.qml \
    ../TextScrollArea.qml \
    ../ToolBar.qml \
    ../ToolButton.qml \
    ../custom/BasicButton.qml \
    ../custom/BusyIndicator.qml \
    ../custom/Button.qml \
    ../custom/ButtonColumn.qml \
    ../custom/ButtonGroup.js \
    ../custom/ButtonRow.qml \
    ../custom/CheckBox.qml \
    ../custom/ChoiceList.qml \
    ../custom/ProgressBar.qml \
    ../custom/Slider.qml \
    ../custom/SpinBox.qml \
    ../custom/TextField.qml \
    ../../examples/Browser.qml \
    ../../examples/Panel.qml \
    ../../examples/ModelView.qml \
    ../../examples/Gallery.qml
