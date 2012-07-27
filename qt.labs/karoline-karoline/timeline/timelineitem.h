#ifndef TIMELINEITEM_H
#define TIMELINEITEM_H

#include <QDateTime>

#include "genericlistitem.h"
#include "twitter/userinfo.h"

class TimelineItem : public ListItem
{
    Q_OBJECT

public:
    enum Roles {
        StatusIdRole = Qt::UserRole+1,
        StatusTextRole,
        StatusAuthorScreenNameRole,
        StatusAuthorNameRole,
        StatusCreatedAtRole
    };

    //tenhle je tady jen kvuli vytvoreni prototypu pro model
    TimelineItem(QObject *parent = 0);
//    explicit TimelineItem(QObject *parent, const quint64 &statusid, const QString &statustext);
//    explicit TimelineItem(QObject *parent);
    QVariant data(int role) const;
    QHash<int, QByteArray> roleNames() const;

    inline quint64 id() const { return m_statusid; }
    inline quint64 statusId() const { return m_statusid; }
    inline QString statusText() const { return m_statustext; }
    inline QString authorName() const { return m_userInfo.name; }
    inline QString authorScreenName() const { return m_userInfo.screenName; }
//    inline QDateTime createdAt() const { return m_createdAt; }

    void setStatusId(quint64);
    void setStatusText(QString);
    void setAuthorName(QString);
    void setAuthorScreenName(QString);
//    void setCreatedAt(QDateTime);

private:
    // FIXME: vetsina z techto hodnot se vickrat menit nebude, takze nacpat const kde to je nutne
    quint64 m_statusid;
    QString m_statustext;
    // datum/cas odeslani tweetu
//    QDateTime m_createdAt;
    UserInfo m_userInfo;


};

//Q_DECLARE_METATYPE(TimelineItem);

typedef QList<TimelineItem *> TimelineList;

#endif // TIMELINEITEM_H
