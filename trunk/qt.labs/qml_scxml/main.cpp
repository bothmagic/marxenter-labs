#include <QApplication>
#include "qmlapplicationviewer.h"
#include "qscxml.h"
#include <QDeclarativeContext>

Q_DECL_EXPORT int main(int argc, char *argv[])
{
    QScopedPointer<QApplication> app(createApplication(argc, argv));

    QScxml *sm = QScxml::load(":/statemaschine.scxml");



    QmlApplicationViewer viewer;
    viewer.rootContext()->setContextProperty("stateMaschine", sm);
    viewer.setOrientation(QmlApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qml/qml_scxml/main.qml"));
    viewer.showExpanded();
    sm->start();

    return app->exec();
}
