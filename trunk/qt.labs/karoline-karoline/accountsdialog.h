#ifndef ACCOUNTSDIALOG_H
#define ACCOUNTSDIALOG_H

#include <QDialog>
#include <QSettings>

class TwitterAPI;

namespace Ui {
    class AccountsDialog;
}

class AccountsDialog : public QDialog
{
    Q_OBJECT

public:
    //TwitterAPI *twitterAPI;

    explicit AccountsDialog(QWidget *parent = 0);
    ~AccountsDialog();

private:
    Ui::AccountsDialog *ui;
    QSettings oauthSettings;
//    TwitterAPI *twitterApi;
    void loadAccounts();


signals:
    void authorize();

private slots:
    void getAuthorization();
};

#endif // ACCOUNTSDIALOG_H
