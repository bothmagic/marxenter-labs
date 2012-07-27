#include "karoline.h"
#include "mainwindow.h"

int main(int argc, char *argv[])
{
    Karoline app(argc, argv);

    app.setOrganizationName("stibiApp");
    app.setApplicationName("Karoline");

    MainWindow karoline;
    karoline.show();
    return app.exec();
}
