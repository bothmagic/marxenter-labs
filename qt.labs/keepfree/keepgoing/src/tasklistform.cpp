#include <QSqlTableModel>
#include <QSqlRecord>
#include "tasklistform.h"
#include "../keepgoing-qt-build-desktop/ui_tasklistform.h"
#include "model/taskmodel.h"
#include "model/contextmodel.h"
#include "todolistitemdelegate.h"
#include "databaseutil.h"
#include <QSqlDatabase>
#include <QModelIndexList>
#include <QSqlTableModel>
#include <QListWidgetItem>
#include "ui/kgvisualview.h"


TaskListForm::TaskListForm(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::TaskListForm)
{
    ui->setupUi(this);
    p_initVisualScene();
    p_initContextModel();

}

TaskListForm::~TaskListForm()
{
    delete ui;
}

KGVisualView* TaskListForm::kgVisualView()
{
    return ui->kgVisualView;
}

//
// private
//

void TaskListForm::p_initContextModel()
{

    ContextModel *contextModel = ContextModel::instance();

    QListWidgetItem  *item;
    for (int i = 0; i < contextModel->rowCount(); i++)
    {
        item = new QListWidgetItem(contextModel->index(i, contextModel->fieldIndex("shortdescr")).data().toString());
        item->setFlags(Qt::ItemIsSelectable| Qt::ItemIsEnabled);
        ui->contextList->addItem(item);
    }

    connect(ui->contextList, SIGNAL(itemSelectionChanged()), this, SLOT(filter()));

}

void TaskListForm::addNewTask()
{

    //QModelIndexList selList = ui->todoListView->selectionModel()->selectedIndexes();
    int row = taskModel->rowCount();
    /*if (!selList.isEmpty()) {
        row = selList.first().row();
    }*/

    taskModel->submitAll();
    taskModel->insertRow(row, QModelIndex());


    //QModelIndex index = taskModel->index(row, 0);

    //ui->graphicsView->rowInserted(row);

}

void TaskListForm::removeTask()
{


    int row = taskModel->rowCount()-1;

    //QModelIndex idx = idxIt.next();

    //taskModel->removeRow(row);

    //ui->graphicsView->rowRemoved(row);

}

void TaskListForm::restoreSelection()
{
    qDebug("restore selection");

    /*if (selectionList.count() == 1)
    {

        ui->todoListView->selectionModel()->setCurrentIndex(selectionList.at(0),
                                                        QItemSelectionModel::ClearAndSelect | QItemSelectionModel::Current
                                                                );

    }*/
}

void TaskListForm::storeSelection()
{
    qDebug("store selection");
    //selectionList = ui->todoListView->selectionModel()->selectedIndexes();

}

void TaskListForm::filter()
{
    QStringList contextList;

    QListIterator<QListWidgetItem*> iItem(ui->contextList->selectedItems());
    while (iItem.hasNext())
    {
        contextList.append(iItem.next()->data(Qt::DisplayRole).toString());
    }

    ui->kgVisualView->filterContext(contextList);

}

void TaskListForm::p_initVisualScene()
{

}
