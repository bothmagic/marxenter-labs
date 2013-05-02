#include <QApplication>
#include "qmlapplicationviewer.h"
#include <QDeclarativeContext>
Q_DECL_EXPORT int main(int argc, char *argv[])
{
    QScopedPointer<QApplication> app(createApplication(argc, argv));

    QmlApplicationViewer viewer;
    viewer.setOrientation(QmlApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qml/qml_calendar_proto/main.qml"));
    viewer.showExpanded();

    return app->exec();
}
