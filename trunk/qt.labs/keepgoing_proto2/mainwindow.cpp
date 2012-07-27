#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "listmodel.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    //ui->mainToolBar->setOrientation(Qt::Vertical);

    //QTabWidget *tabWidget = new QTabWidget();
    //tabWidget->addTab(new QWidget(), "list");

    //ui->mainToolBar->addWidget(tabWidget);
    QToolBar *dockListToolbar = new QToolBar("List");
    dockListToolbar->addAction("Add");
    dockListToolbar->addAction("Remove");
    ui->dockList->setTitleBarWidget(dockListToolbar);

    ListModel *listModel = new ListModel;

    ui->treeList->setModel(listModel);


}

MainWindow::~MainWindow()
{
    delete ui;
}
