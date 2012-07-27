#include <QApplication>
#include <QObject>

#include "creator.h"

#include <qlistwidgetng.h>
#include <qlistcontroller.h>
#include <qlistdefaultmodel.h>
#include <qgraphicslistview.h>

#include <QmlEngine>
#include <QmlComponent>

#include <qdebug.h>

// main

int main(int argc, char *argv[])
{
	QApplication app(argc, argv);
    QmlEngine engine;

    QString qml = "import Qt 4.6\n"
                   "Rect { \n"
                   " width: 200\n"
                   " height: 50\n"
                   " color: BackgroundRole\n"
                   " HorizontalLayout {\n"
                   "  Rect {\n"
                   "   width: 50\n"
                   "   height: 50\n"
                   "   color: \"blue\"\n"
                   "  }\n"
                   "  Text {\n"
                   "   color: ForegroundRole\n"
                   "   text: DisplayRole\n"
                   "   x: 20; y: 20\n"
                   "  }\n"
                   " }\n"
                   "}";


    QtListWidgetNG widget;

    Creator *creator = new Creator(&engine, new QmlComponent(&engine, qml.toUtf8(), QUrl()));
    widget.controller()->view()->setItemCreator(creator);


    for (int i = 0; i < 1000; ++i) {
        QtListDefaultItem *item = new QtListDefaultItem(QString("foo ") + QString::number(i));
        item->setBackground(QBrush(qRgb(0, 0xff, 0)));
        item->setForeground(QBrush(qRgb(0xff, 0, 0)));
        widget.defaultModel()->appendItem(item);
    }

    widget.show();

	return app.exec();
}

#include "main.moc"
