#include <QCoreApplication>
#include "popconnection.h"


int main(int argc, char *argv[])
{
    QCoreApplication a(argc, argv);
    
    PopConnection *pop = new PopConnection();

    return a.exec();
}
