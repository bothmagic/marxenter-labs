/********************************************************************************
** Form generated from reading UI file 'accounts.ui'
**
** Created: Fri 29. Jul 16:44:49 2011
**      by: Qt User Interface Compiler version 4.7.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_ACCOUNTS_H
#define UI_ACCOUNTS_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QApplication>
#include <QtGui/QButtonGroup>
#include <QtGui/QDialog>
#include <QtGui/QDialogButtonBox>
#include <QtGui/QHeaderView>
#include <QtGui/QListView>
#include <QtGui/QPushButton>
#include <QtGui/QVBoxLayout>

QT_BEGIN_NAMESPACE

class Ui_AccountsDialog
{
public:
    QVBoxLayout *verticalLayout_2;
    QVBoxLayout *verticalLayout;
    QListView *accountsList;
    QPushButton *authorizeButton;
    QDialogButtonBox *buttonBox;

    void setupUi(QDialog *AccountsDialog)
    {
        if (AccountsDialog->objectName().isEmpty())
            AccountsDialog->setObjectName(QString::fromUtf8("AccountsDialog"));
        AccountsDialog->resize(396, 296);
        verticalLayout_2 = new QVBoxLayout(AccountsDialog);
        verticalLayout_2->setObjectName(QString::fromUtf8("verticalLayout_2"));
        verticalLayout = new QVBoxLayout();
        verticalLayout->setObjectName(QString::fromUtf8("verticalLayout"));
        accountsList = new QListView(AccountsDialog);
        accountsList->setObjectName(QString::fromUtf8("accountsList"));

        verticalLayout->addWidget(accountsList);

        authorizeButton = new QPushButton(AccountsDialog);
        authorizeButton->setObjectName(QString::fromUtf8("authorizeButton"));
        QSizePolicy sizePolicy(QSizePolicy::Preferred, QSizePolicy::Fixed);
        sizePolicy.setHorizontalStretch(0);
        sizePolicy.setVerticalStretch(0);
        sizePolicy.setHeightForWidth(authorizeButton->sizePolicy().hasHeightForWidth());
        authorizeButton->setSizePolicy(sizePolicy);

        verticalLayout->addWidget(authorizeButton);


        verticalLayout_2->addLayout(verticalLayout);

        buttonBox = new QDialogButtonBox(AccountsDialog);
        buttonBox->setObjectName(QString::fromUtf8("buttonBox"));
        buttonBox->setOrientation(Qt::Horizontal);
        buttonBox->setStandardButtons(QDialogButtonBox::Cancel|QDialogButtonBox::Ok);

        verticalLayout_2->addWidget(buttonBox);


        retranslateUi(AccountsDialog);
        QObject::connect(buttonBox, SIGNAL(accepted()), AccountsDialog, SLOT(accept()));
        QObject::connect(buttonBox, SIGNAL(rejected()), AccountsDialog, SLOT(reject()));

        QMetaObject::connectSlotsByName(AccountsDialog);
    } // setupUi

    void retranslateUi(QDialog *AccountsDialog)
    {
        AccountsDialog->setWindowTitle(QApplication::translate("AccountsDialog", "Dialog", 0, QApplication::UnicodeUTF8));
        authorizeButton->setText(QApplication::translate("AccountsDialog", "Authorize!", 0, QApplication::UnicodeUTF8));
    } // retranslateUi

};

namespace Ui {
    class AccountsDialog: public Ui_AccountsDialog {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_ACCOUNTS_H
