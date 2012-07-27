#include "twitterApi.h"
#include "twitterRequest.h"
#include "twitterparser.h"

#include <QtDebug>
#include <QInputDialog>
#include <QThreadPool>
#include <QtKOAuth>

// TODO: prepsat URL na Url

const QByteArray TwitterAPI::TwitterRequestTokenURL = "http://twitter.com/oauth/request_token";
const QByteArray TwitterAPI::TwitterAuthorizeURL = "http://twitter.com/oauth/authorize";
const QByteArray TwitterAPI::TwitterAccessTokenURL = "http://twitter.com/oauth/access_token";

const QNetworkRequest::Attribute TwitterAPI::ActionAttr = (QNetworkRequest::Attribute) (QNetworkRequest::User + 1);

const QString TwitterAPI::ConsumerKey = "Gii8oCyxACcspr7gKR0hg";
const QString TwitterAPI::ConsumerSecret = "dxjItWKwu7v5IZx90ncA5MqwoQzzXcTK51YJxy1k";

//const QByteArray TwitterAPI::ConsumerKey = "Gii8oCyxACcspr7gKR0hg";
//const QByteArray TwitterAPI::ConsumerSecret = "dxjItWKwu7v5IZx90ncA5MqwoQzzXcTK51YJxy1k";

//const QByteArray TwitterAPI::ParamCallback      = "oauth_callback";
//const QByteArray TwitterAPI::ParamCallbackValue = "oob";
//const QByteArray TwitterAPI::ParamVerifier      = "oauth_verifier";
//const QByteArray TwitterAPI::ParamScreenName    = "screen_name";

//const QString TwitterAPI::UrlStatusesUpdate = "/statuses/update.xml";


TwitterAPI::TwitterAPI(QObject *parent) :
        QObject(parent)
        //QMainWindow(parent),
        //ui_main(new Ui::MainWindow)
{
    oauthManager = new KQOAuthManager(this);
    oauthManager->setHandleUserAuthorization(false);

    connect(oauthManager, SIGNAL(temporaryTokenReceived(QString,QString)),
            this, SLOT(onTemporaryTokenReceived(QString,QString)));

    connect(oauthManager, SIGNAL(accessTokenReceived(QString,QString)),
            this, SLOT(onAccessTokenReceived(QString,QString)));

//    connect(this->oauthManager, SIGNAL(requestReady(QByteArray, QNetworkReply *)),
//            this, SLOT(responseHandler(QByteArray, QNetworkReply *)));

    connect(oauthManager, SIGNAL(requestReady(QNetworkReply *, QByteArray)),
                this, SLOT(responseHandler(QNetworkReply *, QByteArray)));

    connect(oauthManager, SIGNAL(authorizedRequestDone()),
            this, SLOT(onAuthorizedRequestDone()));

}

TwitterAPI::~TwitterAPI()
{
    delete oauthManager;
}

void TwitterAPI::authorize()
{
    KQOAuthRequest *authorizeRequest = new KQOAuthRequest;
    authorizeRequest->initRequest(KQOAuthRequest::TemporaryCredentials, QUrl(TwitterAPI::TwitterRequestTokenURL));
    authorizeRequest->setConsumerKey(TwitterAPI::ConsumerKey);
    authorizeRequest->setConsumerSecretKey(TwitterAPI::ConsumerSecret);
    oauthManager->executeRequest(authorizeRequest);
}

void TwitterAPI::onTemporaryTokenReceived(QString token, QString tokenSecret)
{
    qDebug() << "Temporary token received: " << token << tokenSecret;
    if (oauthManager->lastError() == KQOAuthManager::NoError)
    {
        qDebug() << "Asking for user's permission to access protected resources. Opening URL: " << QUrl(TwitterAPI::TwitterAuthorizeURL);
        oauthManager->getUserAuthorization(QUrl(TwitterAPI::TwitterAuthorizeURL));
    }
    this->requestToken = token;
    this->requestTokenSecret = tokenSecret;

    emit pinNeeded(token);
}

void TwitterAPI::onAuthorizationReceived(QString token, QString verifier)
{
    //oauthManager->foo(token, verifier);
    this->verifier = verifier;
    qDebug() << "User authorization received: " << token << verifier;

    // Nemaka!
    //oauthManager->getUserAccessTokens(QUrl(TwitterAPI::TwitterAccessTokenURL));
    this->getAccessToken();
    if (oauthManager->lastError() != KQOAuthManager::NoError)
    {
        qDebug() << oauthManager->lastError();
    }
}

void TwitterAPI::onAccessTokenReceived(QString token, QString tokenSecret)
{
    qDebug() << "Access token received: " << token << tokenSecret;

    oauthSettings.setValue("oauth_token", token);
    oauthSettings.setValue("oauth_token_secret", tokenSecret);

    qDebug() << "Access tokens now stored.";
}

void TwitterAPI::onAuthorizedRequestDone()
{
    qDebug() << "Request sent to twitter";
}

void TwitterAPI::onTokenReceived(QString token, QString tokenSecret)
{
    Q_UNUSED(token)
    Q_UNUSED(tokenSecret)
    //qDebug() << "Token was received: " << token << ":" << tokenSecret;
}

void TwitterAPI::getAccessToken()
{
    KQOAuthRequest *accessTokenRequest = new KQOAuthRequest;
    accessTokenRequest->initRequest(KQOAuthRequest::AccessToken, QUrl(TwitterAPI::TwitterAccessTokenURL));
    accessTokenRequest->setToken(this->requestToken);
    accessTokenRequest->setTokenSecret(this->requestTokenSecret);
    accessTokenRequest->setVerifier(this->verifier);
    accessTokenRequest->setConsumerKey(this->ConsumerKey);
    accessTokenRequest->setConsumerSecretKey(this->ConsumerSecret);

    oauthManager->executeRequest(accessTokenRequest);
}

void TwitterAPI::parseXml(const QByteArray &data)
{
//    qDebug(Q_FUNC_INFO);
//    qDebug() << data;
//    TwitterParser *parser = new TwitterParser(data);
    parser = new TwitterParser(this, data);
//    connect(parser, SIGNAL())
//    TwitterParser *parser = new TwitterParser();
    // WTF? Co to dela?
    parser->setAutoDelete(true);
    QThreadPool::globalInstance()->start(parser);
}

void TwitterAPI::sendTweet(QString tweet)
{
    Q_UNUSED(tweet)
//    TwitterRequest *request = new TwitterRequest(TwitterRequest::SendTweetUrl, this->oauthManager, true);
//    request->addParameter("status", tweet);
//    request->execute();
//    KQOAuthRequest *sendTweetRequest = new KQOAuthRequest;
//    sendTweetRequest->initRequest(KQOAuthRequest::AuthorizedRequest, QUrl(TwitterAPI::SendTweetUrl));
//    sendTweetRequest->setConsumerKey(this->ConsumerKey);
//    sendTweetRequest->setConsumerSecretKey(this->ConsumerSecret);
//    sendTweetRequest->setToken(oauthSettings.value("oauth_token").toString());
//    sendTweetRequest->setTokenSecret(oauthSettings.value("oauth_token_secret").toString());

//    KQOAuthParameters params;
//    params.insert("status", tweet);
//    sendTweetRequest->setAdditionalParameters(params);

//    oauthManager->executeRequest(sendTweetRequest);
}

void TwitterAPI::getHomeTimeline()
{
    TwitterRequest *request = new TwitterRequest(TwitterUrl::HomeTimeline, this->oauthManager);

//    KQOAuthParameters params;
//    params.insert("since_id", "34260642802180097");

//    request->setAdditionalParameters(params);
    request->setAttribute(TwitterAPI::ActionAttr, TwitterAPI::RoleHomeTimeline);
    request->execute();
}

void TwitterAPI::singleTweet()
{
    qDebug() << "singleTweet() invoked...";

    KQOAuthRequest *request = new KQOAuthRequest;
    request->initRequest(KQOAuthRequest::AuthorizedRequest, QUrl(QString(TwitterUrl::SingleTweet).arg("20633898539028480")) );
    request->setHttpMethod(KQOAuthRequest::GET);
    request->setConsumerKey(this->ConsumerKey);
    request->setConsumerSecretKey(this->ConsumerSecret);
    request->setToken(oauthSettings.value("oauth_token").toString());
    request->setTokenSecret(oauthSettings.value("oauth_token_secret").toString());

//    QHash<QNetworkRequest::Attribute, QVariant> myCustomData;
//    myCustomData.insert(TwitterAPI::ActionAttr, QVariant("stibi rulez :)"));
//    myCustomData.insert((QNetworkRequest::Attribute) (QNetworkRequest::User+2), QVariant("foo"));
//    request->setCustomData(myCustomData);

//    oauthManager->executeRequest(request);
}

void TwitterAPI::responseHandler(QNetworkReply *replyObject, QByteArray networkResponse)
{
    if (oauthManager->lastError() != KQOAuthManager::NoError)
    {
        qDebug() << "Nejaky pruser: \n" << oauthManager->lastError();
    }
    else
    {
        int action = replyObject->request().attribute(TwitterAPI::ActionAttr).toInt();
        switch (action) {
            case TwitterAPI::RoleHomeTimeline:
//                qDebug() << "RoleHomeTimeline";
                this->parseXml(networkResponse);
                //TwitterParser *parser = new TwitterParser(networkResponse);
//                ParseTwitter(networkResponse);
                break;
            default:
                qDebug() << "Neznama akce, oups.";
                break;
        }
//        qDebug() << networkResponse;
    }
}

//void TwitterAPI::networkReplyReceived(QByteArray response)
//{
//    qDebug() << "-----------------";
//    qDebug() << "Text network reply received\n";
//    qDebug() << response;
//    qDebug() << "-----------------";
//}

void TwitterAPI::foo()
{
    qDebug() << "Fo!";

    KQOAuthRequest *request = new KQOAuthRequest;
    request->initRequest(KQOAuthRequest::AuthorizedRequest, QUrl("http://api.twitter.com/1/statuses/public_timeline.xml"));
    request->setHttpMethod(KQOAuthRequest::GET);
    request->setConsumerKey(this->ConsumerKey);
    request->setConsumerSecretKey(this->ConsumerSecret);
    request->setToken(oauthSettings.value("oauth_token").toString());
    request->setTokenSecret(oauthSettings.value("oauth_token_secret").toString());

    oauthManager->executeRequest(request);

//    qDebug() << "foo";
//    TwitterRequest *request = new TwitterRequest(TwitterRequest::PublicTimelineUrl, this->oauthManager);
//    request->execute();
    //TwitterRequest *request = new TwitterRequest(Request::Timeline);
}
