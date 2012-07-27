#include "listmodel.h"


ListModel::ListModel(QObject *parent):
    QAbstractItemModel(parent)
{
//    QHash<int, QByteArray> roles;
//    roles[TypeRole] = "type";
//    roles[SizeRole] = "size";
//    setRoleNames(roles);
    m_rootEntry = new ListEntry("root","", 0);
    ListEntry *l = new ListEntry("listEntry1", "", 0, m_rootEntry);
    l->appendChild(new ListEntry("listEntry1", "", 0, l));
    m_rootEntry->appendChild(l);
}


int ListModel::rowCount(const QModelIndex & parent) const  {

    ListEntry *parentItem;
     if (parent.column() > 0)
         return 0;

     if (!parent.isValid())
         parentItem = m_rootEntry;
     else
         parentItem = static_cast<ListEntry*>(parent.internalPointer());

     return parentItem->childCount();
}

QVariant ListModel::data(const QModelIndex & index,  int role) const  {
    if (!index.isValid())
        return QVariant();

    if (role != Qt::DisplayRole)
        return QVariant();

    ListEntry *item = static_cast<ListEntry*>(index.internalPointer());

    return item->name();
}

QModelIndex ListModel::parent(const QModelIndex &child) const
{

    if (!child.isValid())
        return QModelIndex();

    ListEntry *childItem = static_cast<ListEntry*>(child.internalPointer());
    ListEntry *parentItem = childItem->parent();

    if (parentItem == m_rootEntry)
        return QModelIndex();

    return createIndex(parentItem->row(), 0, parentItem);

}

int ListModel::columnCount(const QModelIndex &parent) const
{
    return 1;
}

QModelIndex ListModel::index(int row, int column, const QModelIndex &parent) const
{
    if (!hasIndex(row, column, parent))
         return QModelIndex();

     ListEntry *parentItem;

     if (!parent.isValid())
         parentItem = m_rootEntry;
     else
         parentItem = static_cast<ListEntry*>(parent.internalPointer());

     ListEntry *childItem = parentItem->child(row);
     if (childItem)
         return createIndex(row, column, childItem);
     else
         return QModelIndex();
}

