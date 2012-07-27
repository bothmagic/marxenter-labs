#include <QtGui/QApplication>
#include "mainwindow.h"
#include "blib.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    w.show();
    Blib *b = new Blib;
    return a.exec();
}
