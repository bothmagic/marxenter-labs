#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

#include "timeline/timelinemodel.h"

class AccountsDialog;
class TwitterAPI;

namespace Ui {
    class MainWindow;
}

class MainWindow : public QMainWindow {
    Q_OBJECT
public:
    MainWindow(QWidget *parent = 0);
    ~MainWindow();

    TwitterAPI *twitterApi;

protected:
    void changeEvent(QEvent *e);
    AccountsDialog *accountsDialog;

public slots:
    void showAccountsDialog();
    void getAuthorization();
    void getToken(QString token);
    void tweet();
    void getFoo();

private slots:
    void dataReady(const QList<QString> &);

private:
    Ui::MainWindow *ui;
    TimelineModel *model;
    //Ui::TestDialog *testdialogui;

};

#endif // MAINWINDOW_H
