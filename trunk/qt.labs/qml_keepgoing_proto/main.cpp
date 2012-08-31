#include <QApplication>
#include "qmlapplicationviewer.h"
#include <qdeclarative.h>
#include "qmlobject.h"
#include "projectlistmodel.h"

Q_DECL_EXPORT int main(int argc, char *argv[])
{
    QScopedPointer<QApplication> app(createApplication(argc, argv));
    qmlRegisterType<QmlObject>("MyObjects", 1, 0, "MyObject");
    qmlRegisterType<ProjectListModel>("MyObjects", 1, 0, "ProjectListModel");

    QmlApplicationViewer viewer;
    viewer.setOrientation(QmlApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qml/qml_keepgoing_proto/main.qml"));


    viewer.showExpanded();

    return app->exec();
}
