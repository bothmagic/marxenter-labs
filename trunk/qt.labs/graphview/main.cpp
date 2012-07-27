#include <QtGui>
#include "graphview.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    GraphView *grv = new GraphView;

    QMainWindow m;
    m.setCentralWidget(grv);

    m.show();

    return a.exec();
}
