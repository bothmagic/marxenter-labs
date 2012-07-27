#ifndef TASKMODEL_H
#define TASKMODEL_H
#include <QSqlTableModel>
#include <QModelIndex>

class QObject;
class QSqlDatabase;

class TaskModel: public QSqlTableModel
{
Q_OBJECT

signals:
    void aboutToSubmit();
    void submited();

public:
    TaskModel(QObject *parent, QSqlDatabase db);
    QVariant data(const QModelIndex &idx, int role) const;
    bool setData(const QModelIndex &index, const QVariant &value, int role);
    Qt::ItemFlags flags(const QModelIndex &index) const;
    bool insertRow(int row, const QModelIndex &parent);
    bool editMode() const;
    void setEditMode(bool isEditMode);

public Q_SLOTS:
    void addNewTask();
    void submitAll();
    void wantsToUpdate();

private:
    int newRow;
    bool isEditMode;
};

#endif // TASKMODEL_H
