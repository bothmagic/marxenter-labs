#ifndef TIMELINEMODEL_H
#define TIMELINEMODEL_H

#include <QAbstractListModel>
#include <QList>
#include <QVariant>

#include "genericlistitem.h"
//#include "timelineitem.h"

// forward declaration, je v tom bordel jak prase
// taham ten timelinelist pres nekolik signalu, slotu, pochybuju ze se jeste nekdy vyznam v tom, jak to funguje :)
class TimelineItem;
typedef QList<TimelineItem *> TimelineList;

class TimelineModel : public QAbstractListModel
{
    Q_OBJECT

public:
    // explicit?
    explicit TimelineModel(ListItem *prototype, QObject *parent = 0);
    ~TimelineModel();
    int rowCount(const QModelIndex &parent = QModelIndex()) const;
    QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const;
    void appendRow(ListItem *item);
    void appendRows(const QList<ListItem *> &items);
    void insertRow(int row, ListItem *item);
    bool removeRow(int row, const QModelIndex &parent = QModelIndex());
    bool removeRows(int row, int count, const QModelIndex &parent = QModelIndex());
    ListItem * takeRow(int row);
    ListItem * find(const quint64 &id) const;
    QModelIndex indexFromItem( const ListItem *item) const;
    void clear();

public slots:
    void onDataParsed(TimelineList);

private slots:
    void handleItemChange();

private:
    ListItem *m_prototype;
    QList<ListItem *> m_list;
};

#endif // TIMELINEMODEL_H
