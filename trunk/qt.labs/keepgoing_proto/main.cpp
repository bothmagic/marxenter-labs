#include <QtGui/QApplication>
#include "qmlapplicationviewer.h"
#include <QDeclarativeContext>
#include "model.h"


int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    AnimalModel model;
    model.addAnimal(Animal("Wolf", "Medium"));
    model.addAnimal(Animal("Polar bear", "Large"));
    model.addAnimal(Animal("Quoll", "Small"));
    QmlApplicationViewer viewer;
    viewer.setOrientation(QmlApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qml/keepgoing_proto/main.qml"));
    QDeclarativeContext *ctxt = viewer.rootContext();
    ctxt->setContextProperty("myModel", &model);
    viewer.showExpanded();
    //TODO methode zum öffnen des Dialogs
    //TODO model extern implementieren
    return app.exec();
}
