#include <QtGui/QApplication>
#include "kgmain.h"
#include "connection.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    if (!createConnection()) {
        return 1;
    }

    KGMain w;
    w.show();

    return a.exec();
}
