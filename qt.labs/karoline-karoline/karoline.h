#ifndef _KAROLINE_H
#define _KAROLINE_H

#include <QApplication>

#define VERSION "0.1"

class MainWindow;

class Karoline : public QApplication
{
    Q_OBJECT

public:
    Karoline( int &argc, char *argv[]);
    ~Karoline();

    void foo();
    MainWindow *m_mainWindow;

//private:
//    MainWindow *m_mainWindow;


};

#endif
