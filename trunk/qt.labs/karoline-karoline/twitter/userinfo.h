#ifndef USERINFO_H
#define USERINFO_H

#include <QtCore>

struct UserInfo {
    UserInfo();

    quint64 userId;
    QString name; // real name
    QString screenName; // screenName/login
//    QString location;
//    QString description;
    QString avatarUrl;
};

#endif // USERINFO_H
