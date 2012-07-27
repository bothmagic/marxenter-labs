#include "timelinemodel.h"
#include "timelineitem.h"

#include <QDebug>

TimelineModel::TimelineModel(ListItem *prototype, QObject *parent) :
    QAbstractListModel(parent), m_prototype(prototype)
{
    this->setRoleNames(m_prototype->roleNames());
}

TimelineModel::~TimelineModel()
{
    delete m_prototype;
    this->clear();
}

int TimelineModel::rowCount(const QModelIndex &parent) const
{
    Q_UNUSED(parent)

    return m_list.size();
}

QVariant TimelineModel::data(const QModelIndex &index, int role) const
{
    if (index.row() < 0 || index.row() >= m_list.size())
        return QVariant();

    return m_list.at(index.row())->data(role);
}

void TimelineModel::appendRow(ListItem *item)
{
    this->appendRows(QList<ListItem *>() << item);
}

void TimelineModel::appendRows(const QList<ListItem *> &items)
{
    this->beginInsertRows(QModelIndex(), this->rowCount(), this->rowCount() + items.size() -1);
    foreach(ListItem *item, items)
    {
        connect(item, SIGNAL(dataChanged()), SLOT(handleItemChange()));
        m_list.append(item);
    }
    this->endInsertRows();
}

void TimelineModel::insertRow(int row, ListItem *item)
{
    this->beginInsertRows(QModelIndex(), row, row);
    connect(item, SIGNAL(dataChanged()), SLOT(handleItemChange()));
    m_list.insert(row, item);
    this->endInsertRows();
}

void TimelineModel::handleItemChange()
{
    ListItem *item = static_cast<ListItem *>(this->sender());
    QModelIndex index = this->indexFromItem(item);
    if (index.isValid())
        emit this->dataChanged(index, index);
}

ListItem * TimelineModel::find(const quint64 &id) const
{
    foreach(ListItem *item, m_list)
    {
        if(item->id() == id) return item;
    }
    return 0;
}

QModelIndex TimelineModel::indexFromItem(const ListItem *item) const
{
    Q_ASSERT(item);
    for(int row=0; row<m_list.size(); ++row)
    {
        if(m_list.at(row) == item) return index(row);
    }
    return QModelIndex();
}

void TimelineModel::clear()
{
    qDeleteAll(m_list);
    m_list.clear();
}

bool TimelineModel::removeRow(int row, const QModelIndex &parent)
{
    Q_UNUSED(parent);
    if(row < 0 || row >= m_list.size()) return false;
    this->beginRemoveRows(QModelIndex(), row, row);
    delete m_list.takeAt(row);
    this->endRemoveRows();
    return true;
}

bool TimelineModel::removeRows(int row, int count, const QModelIndex &parent)
{
    Q_UNUSED(parent);
    if(row < 0 || (row+count) >= m_list.size()) return false;
    this->beginRemoveRows(QModelIndex(), row, row+count-1);
    for(int i=0; i<count; ++i)
    {
        delete m_list.takeAt(row);
    }
    this->endRemoveRows();
    return true;
}

ListItem * TimelineModel::takeRow(int row)
{
    this->beginRemoveRows(QModelIndex(), row, row);
    ListItem *item = m_list.takeAt(row);
    this->endRemoveRows();
    return item;
}

void TimelineModel::onDataParsed(TimelineList data)
{
    qDebug() << "Hura, data dorazila";
//    qDebug() << data.count();
    foreach (TimelineItem *item, data) {
//        qDebug() << ">> " << item->statusText();
        this->appendRow(item);
//        qDebug() << item->authorScreenName();

        //        QDateTime foo = new QDateTime::fromString(item->createdAt(), "ddd MMM dd hh:mm:ss");
//        qDebug() << foo.toString();
    }
}
