#ifndef MODELITEMMODEL_H
#define MODELITEMMODEL_H

#include <QAbstractItemModel>
#include "modelitem.h"
class ModelItemModel : public QAbstractItemModel
{
    Q_OBJECT
public:
    explicit ModelItemModel(QObject *parent = 0);
    virtual QModelIndex index(int row, int column, const QModelIndex &parent) const;
    virtual QModelIndex parent(const QModelIndex &child) const;
    virtual int rowCount(const QModelIndex &parent) const;
    virtual int columnCount(const QModelIndex &parent) const;
    virtual QVariant data(const QModelIndex &index, int role) const;
    Qt::ItemFlags flags(const QModelIndex &index) const;
signals:
    
public slots:

private:
    QList<ModelItem> m_items;
    
};

#endif // MODELITEMMODEL_H
