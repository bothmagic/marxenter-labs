#include "karoline.h"
#include "mainwindow.h"

#include <QDebug>
//#include <QtKOAuth>

Karoline::Karoline(int &argc, char *argv[]) :
        QApplication(argc, argv),
        m_mainWindow(0)
{

}

Karoline::~Karoline()
{

}

void Karoline::foo()
{
    qDebug() << "Fo!";
}
