#include "ui_accounts.h"
#include "accountsdialog.h"
#include "twitterApi.h"

#include <QDebug>

AccountsDialog::AccountsDialog(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::AccountsDialog)
{
    ui->setupUi(this);
    connect(ui->authorizeButton, SIGNAL(clicked()), this, SLOT(getAuthorization()));
//    twitterApi = new TwitterAPI;

//    this->loadAccounts();
}

AccountsDialog::~AccountsDialog()
{
    delete ui;
}

void AccountsDialog::getAuthorization()
{
    emit authorize();
}

void AccountsDialog::loadAccounts()
{
    qDebug() << "token: " << oauthSettings.value("oauth_token").toString();
    qDebug() << "token_secret: " << oauthSettings.value("oauth_token_secret").toString();
}
