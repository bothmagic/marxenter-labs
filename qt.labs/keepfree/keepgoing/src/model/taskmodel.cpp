#include <QSqlRecord>
#include <QSqlDatabase>
#include <QSqlQuery>
#include <QObject>
#include <QtConcurrentRun>

#include "taskmodel.h"


TaskModel::TaskModel(QObject *parent, QSqlDatabase db):
        QSqlTableModel(parent, db)
{


}


QVariant TaskModel::data(const QModelIndex &index, int role) const {

    // return complete
    /*if (role == Qt::CheckStateRole && index.column() <= 2) {
        return QSqlTableModel::data(
                index.model()->index(index.row(), 1, index.parent()), Qt::DisplayRole);
    } else if (index.column() == 0 && role == Qt::EditRole) {
        return QSqlTableModel::data(
                QSqlTableModel::index(index.row(), 2, index.parent()), Qt::DisplayRole);
    }*/
    /*if (role == Qt::CheckStateRole && index.column() == 0) {
        return QVariant::Invalid;
    }*/

   /*if (role == Qt::CheckStateRole && index.column() < 2) {
        return QSqlTableModel::data(
                index.model()->index(index.row(), fieldIndex("complete"), index.parent()), Qt::DisplayRole);

   } else  if (index.column() == fieldIndex("id"))
   {
       return QVariant::Invalid;
   } else  if (index.column() == fieldIndex("complete"))
   {
       return QVariant::Invalid;
   }*/

    return QSqlTableModel::data(index, role);

}

bool TaskModel::setData(const QModelIndex &index, const QVariant &value, int role) {
    bool result;
    /*if (role == Qt::CheckStateRole) {
        result = QSqlTableModel::setData(
                index.model()->index(index.row(), 1, index.parent()), value, Qt::EditRole);
    } else {

        result = QSqlTableModel::setData(index, value, role);
    }*/

    /*int internalId = QSqlTableModel::data(this->index(index.row(), 0)).toInt();
    int sortIdx = fieldIndex("sort");
    if (internalId == 0 && index.row() != rowCount() -1) { // then row is new
        // todo excecute query in a seperate method

        //QSqlQuery query = QSqlQuery("update task set sort = sort +1 where sort > ?");
        //query.addBindValue(index.row());
        //query.exec();
        //submitAll();
    }

    if (role == Qt::CheckStateRole) {

        result = QSqlTableModel::setData(
                index.model()->index(index.row(), fieldIndex("complete"), index.parent()), value, Qt::EditRole);
        //emit(dataChanged(index, index));
    }*/


    return QSqlTableModel::setData(index, value, role);
}

Qt::ItemFlags TaskModel::flags(const QModelIndex &index) const
{

    Qt::ItemFlags flags = Qt::ItemIsSelectable | Qt::ItemIsEnabled | Qt::ItemIsEditable | Qt::ItemIsDragEnabled
            | Qt::ItemIsUserCheckable;

    return flags;
}

bool TaskModel::insertRow(int row, const QModelIndex &parent) {
    if (QSqlTableModel::insertRows(row, 1, parent)) {
        QModelIndex idx = index(row, fieldIndex("complete"));
        QSqlTableModel::setData(idx, QVariant(0), Qt::EditRole);
        idx = index(row, fieldIndex("shortdescr"));
        QSqlTableModel::setData(idx, tr("New Todo"), Qt::EditRole);
        idx = index(row, fieldIndex("sort"));
        QSqlTableModel::setData(idx, row, Qt::EditRole);

        return true;
    } else {
        return false;
    }
}

void TaskModel::addNewTask()
{

    QSqlRecord rec = record();
    rec.setValue("complete", QVariant(0));
    rec.setValue("shortdescr", tr("New Task"));
    QSqlTableModel::insertRowIntoTable(rec);
    select();
}

bool TaskModel::editMode() const
{
    return isEditMode;
}

void TaskModel::setEditMode(bool isEditMode)
{
    this->isEditMode = isEditMode;
}

void TaskModel::wantsToUpdate()
{
    emit layoutChanged();

}

void TaskModel::submitAll()
{
    emit aboutToSubmit();
    QSqlTableModel::submitAll();
    emit submited();
}
