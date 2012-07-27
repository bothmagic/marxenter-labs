#include "kgprojectlistwidget.h"
#include <QDropEvent>
#include <QDebug>

KGProjectListWidget::KGProjectListWidget(QWidget *parent) :
    QListWidget(parent)
{
    setAcceptDrops(true);
    setDropIndicatorShown(true);
    setDragEnabled(true);
    setDragDropMode(QAbstractItemView::DragDrop);
    setDefaultDropAction(Qt::CopyAction);
}

void KGProjectListWidget::dropEvent(QDropEvent *event)
{
    if (event->mimeData()->hasFormat("keepgoing"))
    {
        event->accept();
    } else
    {
        //event->ignore();
        return;
    }

    QDataStream in(event->mimeData()->data("keepgoing"));

    qint16 group, item;
    in >> group;
    in >> item;

    QListWidgetItem const *listItem = itemAt(event->pos());
    int destGroup = row(listItem);

    emit moveItem(group, item, destGroup);

}

void KGProjectListWidget::dragEnterEvent(QDragEnterEvent *event)
{
    //QListWidget::dragEnterEvent(event);
    if (event->mimeData()->hasFormat("keepgoing"))
    {
        event->accept();
    } else
    {
        event->ignore();
    }

}

void KGProjectListWidget::dragMoveEvent(QDragMoveEvent *event)
{
    //QListWidget::dragMoveEvent(event);
    if (event->mimeData()->hasFormat("keepgoing"))
    {
        event->accept();
    } else
    {
        event->ignore();
    }

}

void KGProjectListWidget::keyReleaseEvent(QKeyEvent *event)
{
    if (event->key() == Qt::Key_Delete && !selectedItems().isEmpty())
    {
        QListWidgetItem *item = selectedItems().first();
        int r = row(item);
        model()->removeRow(r);
        emit removeRow(r);
    }
}
