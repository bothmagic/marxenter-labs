#ifndef TASKLISTVIEW_H
#define TASKLISTVIEW_H

#include <QListView>
#include <QModelIndex>

class TaskListView : public QListView
{
    Q_OBJECT
public:
    explicit TaskListView(QWidget *parent = 0);
    void edit(const QModelIndex &index);

signals:

public slots:

private:

    QModelIndex editIndex;

    void currentChanged(const QModelIndex &current, const QModelIndex &previous);
    void setSelection(const QRect &rect, QItemSelectionModel::SelectionFlags command);


};

#endif // TASKLISTVIEW_H
