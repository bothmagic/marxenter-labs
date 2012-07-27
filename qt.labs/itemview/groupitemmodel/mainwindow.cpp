#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "modelitemmodel.h"
#include <QTreeView>
#include <QTableView>
MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    QTreeView *tr = new QTreeView(ui->centralWidget);
    //QTableView *tr = new QTableView(ui->centralWidget);
    ModelItemModel *model = new ModelItemModel;
    //tr->setRootIsDecorated(true);

    tr->setModel(model);
}

MainWindow::~MainWindow()
{
    delete ui;
}
