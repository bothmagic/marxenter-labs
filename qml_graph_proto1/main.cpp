#include <QApplication>
#include <QtDeclarative>
#include "qmlapplicationviewer.h"
#include "qmlgraphview.h"

Q_DECL_EXPORT int main(int argc, char *argv[])
{
    QScopedPointer<QApplication> app(createApplication(argc, argv));
    qmlRegisterType<QmlGraphView>("mylib", 1, 0, "GraphView");
    QmlApplicationViewer viewer;
    viewer.setOrientation(QmlApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qml/qml_graph_proto/main.qml"));
    viewer.showExpanded();

    return app->exec();
}
