#ifndef TASKCONTEXTITEMDELEGATE_H
#define TASKCONTEXTITEMDELEGATE_H

#include <QItemDelegate>
#include <QSqlTableModel>


class TaskContextItemDelegate : public QItemDelegate
{
    Q_OBJECT
public:
    explicit TaskContextItemDelegate(QSqlTableModel *contextModel, QObject *parent = 0);
    QWidget* createEditor(QWidget *parent, const QStyleOptionViewItem &option, const QModelIndex &index) const;
    void updateEditorGeometry(QWidget *editor, const QStyleOptionViewItem &option, const QModelIndex &index) const;
    void setEditorData(QWidget *editor, const QModelIndex &index) const;
signals:

public slots:

private:
    QSqlTableModel *vcontextModel;

};

#endif // TASKCONTEXTITEMDELEGATE_H
