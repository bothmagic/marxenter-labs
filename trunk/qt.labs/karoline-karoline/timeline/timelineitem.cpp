#include "timelineitem.h"

#include <QDebug>
#include <QVariant>

//TimelineItem::TimelineItem(QObject *parent, const quint64 &statusid, const QString &statustext)
TimelineItem::TimelineItem(QObject *parent) :
//TimelineItem::TimelineItem(QObject *parent) :
    ListItem(parent)
//    m_statusid(statusid),
//    m_statustext(statustext)
{
}

QHash<int, QByteArray> TimelineItem::roleNames() const
{
    QHash<int, QByteArray> names;
    names[StatusIdRole] = "statusId";
    names[StatusTextRole] = "statusText";
    names[StatusAuthorScreenNameRole] = "statusAuthorScreenName";
    names[StatusAuthorNameRole] = "statusAuthorName";
//    names[StatusCreatedAtRole] = "statusCreatedAt";

    return names;
}

QVariant TimelineItem::data(int role) const
{
    switch(role)
    {
    case StatusIdRole:
        return this->statusId();
    case StatusTextRole:
        return this->statusText();
    case StatusAuthorScreenNameRole:
        return this->authorScreenName();
    case StatusAuthorNameRole:
        return this->authorName();
//    case StatusCreatedAtRole:
//        return QVariant();
    default:
        return QVariant();
    }
}

void TimelineItem::setStatusId(quint64 statusId)
{

}

void TimelineItem::setStatusText(QString text)
{
    // pokud budu chtit data nejak menit, je nutno vyslat signal
//    if (this->m_statustext != text) {
        this->m_statustext = text;
//        emit this->dataChanged();
//    }
}

void TimelineItem::setAuthorName(QString name)
{
    this->m_userInfo.name = name;
}

void TimelineItem::setAuthorScreenName(QString screenName)
{
    this->m_userInfo.screenName = screenName;
}

//void TimelineItem::setCreatedAt(QDateTime createdAt)
//{
//    this->m_createdAt = createdAt;
//}
