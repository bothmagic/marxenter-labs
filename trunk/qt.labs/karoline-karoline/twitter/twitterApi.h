#ifndef _TWITTER_API_H
#define _TWITTER_API_H

#include <QObject>
#include <QSettings>
#include <QNetworkReply>

#include "timeline/timelineitem.h"

namespace TwitterUrl {
    static const QByteArray HomeTimeline = "http://api.twitter.com/1/statuses/home_timeline.xml";
    static const QByteArray SendTweet = "http://api.twitter.com/1/statuses/update.xml";
    static const QByteArray SingleTweet = "http://api.twitter.com/1/statuses/show/%1.xml";
}

class KQOAuthManager;
class KQOAuthRequest;
class TwitterParser;

class TwitterAPI : public QObject {
    Q_OBJECT

public:
    TwitterAPI(QObject *parent = 0);
    ~TwitterAPI();

    enum ActionRole {
        RolePublicTimeline = 101,
        RoleHomeTimeline,
        RoleFriendsTimeline
//        RoleSendTweet
    };

    static const QString ConsumerKey;
    static const QString ConsumerSecret;

    // vykopnout do private a napsat setter/getter
    QString verifier;
    QByteArray reply;

    void parseXml(const QByteArray &data);
    void authorize();
    void sendTweet(QString tweet);

    void onAuthorizationReceived(QString token, QString verifier);
    void foo();
    void singleTweet();
    //bool authorized() const;
    //bool isAuthorized();
    //void updateStatus();

private slots:
    void onTemporaryTokenReceived(QString temporaryToken, QString temporaryTokenSecret);
//    void onAuthorizationReceived(QString token, QString verifier);
    void onAccessTokenReceived(QString token, QString tokenSecret);
    void onAuthorizedRequestDone();
    void onTokenReceived(QString token, QString tokenSecret);
    // Timeline handlers
    void responseHandler(QNetworkReply *replyObject, QByteArray networkResponse);
//    void networkReplyReceived(QByteArray);

public slots:
    void getHomeTimeline(); // nemusi byt slot, ne ?
    //void foo();

signals:
    void pinNeeded(QString);
    // emitted when data id requested, parsed and ready
    // FIXME asi pridat const atd
    void dataParsed(TimelineList);

private:
    KQOAuthManager *oauthManager;
    QString requestToken;
    QString requestTokenSecret;
    //QString verifier;
    TwitterParser *parser;
    void getAccessToken();

    static const QByteArray TwitterRequestTokenURL; // FIX lowercase URL
    static const QByteArray TwitterAuthorizeURL;
    static const QByteArray TwitterAccessTokenURL;
//    static const QByteArray SendTweetUrl;
//    static const QByteArray HomeTimelineUrl;


//    static const QString ConsumerKey;
//    static const QString ConsumerSecret;

    static const QNetworkRequest::Attribute ActionAttr;

    QSettings oauthSettings;
};

#endif
