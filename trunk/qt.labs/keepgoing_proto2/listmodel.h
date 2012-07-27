#ifndef LISTMODEL_H
#define LISTMODEL_H
#include <QAbstractItemModel>
#include <QModelIndex>
#include "listentry.h"


class ListModel: public QAbstractItemModel
{
    Q_OBJECT
public:
    ListModel(QObject *parent = 0);

    int rowCount(const QModelIndex & parent = QModelIndex()) const;

    QVariant data(const QModelIndex & index,  int role = Qt::DisplayRole) const;

    QModelIndex parent(const QModelIndex &child) const;

    int columnCount(const QModelIndex &parent) const;

    QModelIndex index(int row, int column, const QModelIndex &parent) const;
private:
     //QList<ListEntry> m_listEntries;
    ListEntry *m_rootEntry;

};

#endif // LISTMODEL_H
