#ifndef TASKLISTFORM_H
#define TASKLISTFORM_H

#include <QWidget>
#include <QModelIndex>
#include <QModelIndexList>

class TaskModel;
class QSqlTableModel;
class KGVisualView;

namespace Ui {
    class TaskListForm;
}

class TaskListForm : public QWidget
{
    Q_OBJECT

public:
    explicit TaskListForm(QWidget *parent = 0);
    ~TaskListForm();
    KGVisualView* kgVisualView();


private:
    Ui::TaskListForm *ui;
    TaskModel *taskModel;

    QModelIndexList selectionList;

    void p_initContextModel();
    void p_initVisualScene();

private slots:
    void addNewTask();
    void removeTask();
    void restoreSelection();
    void storeSelection();
    void filter();
};

#endif // TASKLISTFORM_H
