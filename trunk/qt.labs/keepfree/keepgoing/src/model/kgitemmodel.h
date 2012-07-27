#ifndef KGITEMMODEL_H
#define KGITEMMODEL_H

#include <QStandardItemModel>
#include <QMap>

class KGGroupFactory;

class KGItemModel : public QStandardItemModel
{
    Q_OBJECT
public:
    explicit KGItemModel(QObject *parent = 0);
    void loadGroups();
    void loadGroupItems(QStandardItem *item);
    void createItem(int const group, int const itemIdx);
signals:

public slots:

private:
    KGGroupFactory *m_groupFactory;
    QMap<QStandardItem *, int> m_groupIds;
    QMap<QStandardItem *, int> m_itemIds;
};

#endif // KGITEMMODEL_H
