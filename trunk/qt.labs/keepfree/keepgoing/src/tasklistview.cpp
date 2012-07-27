#include "tasklistview.h"


TaskListView::TaskListView(QWidget *parent) :
    QListView(parent)
{

    //connect(this, SIGNAL(pressed(QModelIndex)), this, SLOT(edit(QModelIndex)));
    //connect(this, SIGNAL(entered(QModelIndex)), this, SLOT(setCurrentIndex(QModelIndex)));


}

void TaskListView::currentChanged(const QModelIndex &current, const QModelIndex &previous)
{

    /*bool editing = state() == QAbstractItemView::EditingState;

    QListView::currentChanged(current, previous);


    if (editing)
    {
        QListView::edit(editIndex);
    }*/

    //if (selectionModel()->selectedIndexes().count() == 1 && editing)
    //    edit(current);
}

void TaskListView::setSelection(const QRect &rect, QItemSelectionModel::SelectionFlags command)
{
    /*bool editing = state() == QAbstractItemView::EditingState;

    if (rect.x() > 100 && !editing)
        QListView::setSelection(rect, command);*/
}

void TaskListView::edit(const QModelIndex &index)
{
    /*bool editing = state() == QAbstractItemView::EditingState;

    if (editing)
    {
        QListView::currentChanged(index, editIndex);
    }
    QListView::edit(index);
    editIndex = index;*/
}
