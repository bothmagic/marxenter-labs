#include <QApplication>
#include <QtDeclarative>
#include "qmlapplicationviewer.h"
#include "edgelayer.h"

Q_DECL_EXPORT int main(int argc, char *argv[])
{
    QScopedPointer<QApplication> app(createApplication(argc, argv));
    qmlRegisterType<EdgeLayer>("mylib", 1, 0, "EdgeLayer");
    QmlApplicationViewer viewer;
    viewer.setOrientation(QmlApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qml/qml_graph_proto1/main.qml"));
    viewer.showExpanded();

    return app->exec();
}
