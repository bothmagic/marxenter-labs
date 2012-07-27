#include "modelitemmodel.h"
#include <QDebug>
ModelItemModel::ModelItemModel(QObject *parent) :
    QAbstractItemModel(parent)
{

    m_items.append(ModelItem("1", "name1", "group1"));
    m_items.append(ModelItem("2", "name2", "group1"));
    m_items.append(ModelItem("3", "name3", "group1"));
    m_items.append(ModelItem("4", "name1", "group2"));
    m_items.append(ModelItem("5", "name2", "group2"));
    m_items.append(ModelItem("6", "name3", "group2"));

}

QModelIndex ModelItemModel::index(int row, int column, const QModelIndex &parent) const {
    ModelItem mi = m_items.at(row);
    QModelIndex idx = createIndex(row, column, row);
    return idx;
}

QModelIndex ModelItemModel::parent(const QModelIndex &child) const {
      qDebug() << Q_FUNC_INFO << ", " << child;
    return QModelIndex();
}

int ModelItemModel::rowCount(const QModelIndex &parent) const {
    if (!parent.isValid())
        return m_items.size();
    else return 0;

}
int ModelItemModel::columnCount(const QModelIndex &parent) const {
    return 3;
}

QVariant ModelItemModel::data(const QModelIndex &index, int role) const {
    //qDebug() << Q_FUNC_INFO << ", " << index;
    ModelItem mi = m_items.at(index.row());
     if (role != Qt::DisplayRole)
         return QVariant();
    switch (index.column()) {
    case 0: return mi.id(); break;
    case 1: return mi.name();break;
    case 2: return mi.group(); break;
    }
}

Qt::ItemFlags ModelItemModel::flags(const QModelIndex &index) const {
    if (!index.isValid()) return 0;
    return Qt::ItemIsEnabled| Qt::ItemIsSelectable| Qt::ItemIsUserCheckable;
}
