#include <QtGui/QApplication>
//#include "mainwindow.h"
#include "ktagableplaintextedit.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    KTagablePlainTextEdit *edit = new KTagablePlainTextEdit;

    //MainWindow w;
    //w.show();

    edit->show();


    return a.exec();
}
