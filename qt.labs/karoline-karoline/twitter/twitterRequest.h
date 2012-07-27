#ifndef TWITTER_REQUEST_H
#define TWITTER_REQUEST_H

#include <QObject>
#include <QSettings>
#include <QDebug>
#include <QtKOAuth>

//class KQOAuthRequest;
//class KQOAuthManager;

//namespace Request
//{
//    static const QByteArray PublicTimelineUrl;
////    enum RequestType {
////        TemporaryTokens,
////        AccessTokens,
////        Timeline
////    };
//}

class TwitterRequest : public KQOAuthRequest
{

public:
//    TwitterRequest(Request::RequestType type = Request::Timeline, QObject *parent = 0);
    TwitterRequest(QByteArray url, KQOAuthManager *oauthmanager);
    ~TwitterRequest();
    void execute();
    void addParameter(QString key, QString value);
    KQOAuthParameters & getParameters() const;

private:
    static const int Version;
    enum Format {
        XML,
        JSON,
        RSS,
        ATOM
    };

    KQOAuthManager *oauthmanager;
    QSettings oauthSettings;
    KQOAuthParameters *requestParams;
};

#endif
