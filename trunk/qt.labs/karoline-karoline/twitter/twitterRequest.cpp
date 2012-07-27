#include "twitterRequest.h"
#include <QtKOAuth>

#include "twitterApi.h"

TwitterRequest::TwitterRequest(QByteArray url, KQOAuthManager *oauthmanager) :
        KQOAuthRequest(), oauthmanager(oauthmanager)
{
    this->initRequest(KQOAuthRequest::AuthorizedRequest, QUrl(url));
    this->setHttpMethod(KQOAuthRequest::GET);
    this->setConsumerKey(TwitterAPI::ConsumerKey);
    this->setConsumerSecretKey(TwitterAPI::ConsumerSecret);
    this->setToken(this->oauthSettings.value("oauth_token").toString());
    this->setTokenSecret(this->oauthSettings.value("oauth_token_secret").toString());

//    this->requestParams = new KQOAuthParameters;

//    qDebug() << request->customData();
    //this->request->setCustomData();
//    qDebug() << this->request->customData();
}

TwitterRequest::~TwitterRequest()
{

}

void TwitterRequest::execute()
{
//    if (!this->requestParams->isEmpty())
//    {
//        this->setAdditionalParameters(this->getParameters());
//    }
//    KQOAuthRequest *foo = dynamic_cast<KQOAuthRequest *>(this);
//    this->oauthmanager->executeRequest(foo);
    this->oauthmanager->executeRequest(this);
}

void TwitterRequest::addParameter(QString key, QString value)
{
    this->requestParams->insert(key, value);
}

KQOAuthParameters & TwitterRequest::getParameters() const
{
    return *this->requestParams;
}
