/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created: Fri 29. Jul 16:44:49 2011
**      by: Qt User Interface Compiler version 4.7.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QApplication>
#include <QtGui/QButtonGroup>
#include <QtGui/QHBoxLayout>
#include <QtGui/QHeaderView>
#include <QtGui/QLineEdit>
#include <QtGui/QMainWindow>
#include <QtGui/QMenu>
#include <QtGui/QMenuBar>
#include <QtGui/QPushButton>
#include <QtGui/QSpacerItem>
#include <QtGui/QStatusBar>
#include <QtGui/QVBoxLayout>
#include <QtGui/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QAction *actionQuit;
    QAction *actionAccounts;
    QWidget *centralwidget;
    QVBoxLayout *verticalLayout;
    QLineEdit *tweetText;
    QHBoxLayout *horizontalLayout;
    QPushButton *getHomeTimelineButton;
    QPushButton *fooButton;
    QSpacerItem *horizontalSpacer;
    QHBoxLayout *horizontalLayout_2;
    QMenuBar *menubar;
    QMenu *menuQTwitter;
    QMenu *menuSettings;
    QStatusBar *statusbar;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QString::fromUtf8("MainWindow"));
        MainWindow->resize(346, 483);
        actionQuit = new QAction(MainWindow);
        actionQuit->setObjectName(QString::fromUtf8("actionQuit"));
        actionAccounts = new QAction(MainWindow);
        actionAccounts->setObjectName(QString::fromUtf8("actionAccounts"));
        centralwidget = new QWidget(MainWindow);
        centralwidget->setObjectName(QString::fromUtf8("centralwidget"));
        verticalLayout = new QVBoxLayout(centralwidget);
        verticalLayout->setObjectName(QString::fromUtf8("verticalLayout"));
        tweetText = new QLineEdit(centralwidget);
        tweetText->setObjectName(QString::fromUtf8("tweetText"));

        verticalLayout->addWidget(tweetText);

        horizontalLayout = new QHBoxLayout();
        horizontalLayout->setObjectName(QString::fromUtf8("horizontalLayout"));
        getHomeTimelineButton = new QPushButton(centralwidget);
        getHomeTimelineButton->setObjectName(QString::fromUtf8("getHomeTimelineButton"));

        horizontalLayout->addWidget(getHomeTimelineButton);

        fooButton = new QPushButton(centralwidget);
        fooButton->setObjectName(QString::fromUtf8("fooButton"));

        horizontalLayout->addWidget(fooButton);

        horizontalSpacer = new QSpacerItem(40, 20, QSizePolicy::Expanding, QSizePolicy::Minimum);

        horizontalLayout->addItem(horizontalSpacer);


        verticalLayout->addLayout(horizontalLayout);

        horizontalLayout_2 = new QHBoxLayout();
        horizontalLayout_2->setObjectName(QString::fromUtf8("horizontalLayout_2"));

        verticalLayout->addLayout(horizontalLayout_2);

        MainWindow->setCentralWidget(centralwidget);
        menubar = new QMenuBar(MainWindow);
        menubar->setObjectName(QString::fromUtf8("menubar"));
        menubar->setGeometry(QRect(0, 0, 346, 21));
        menuQTwitter = new QMenu(menubar);
        menuQTwitter->setObjectName(QString::fromUtf8("menuQTwitter"));
        menuSettings = new QMenu(menubar);
        menuSettings->setObjectName(QString::fromUtf8("menuSettings"));
        MainWindow->setMenuBar(menubar);
        statusbar = new QStatusBar(MainWindow);
        statusbar->setObjectName(QString::fromUtf8("statusbar"));
        MainWindow->setStatusBar(statusbar);

        menubar->addAction(menuQTwitter->menuAction());
        menubar->addAction(menuSettings->menuAction());
        menuQTwitter->addAction(actionQuit);
        menuSettings->addAction(actionAccounts);

        retranslateUi(MainWindow);
        QObject::connect(actionQuit, SIGNAL(activated()), MainWindow, SLOT(close()));

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QApplication::translate("MainWindow", "Karoline", 0, QApplication::UnicodeUTF8));
        actionQuit->setText(QApplication::translate("MainWindow", "Quit", 0, QApplication::UnicodeUTF8));
        actionAccounts->setText(QApplication::translate("MainWindow", "Accounts", 0, QApplication::UnicodeUTF8));
        getHomeTimelineButton->setText(QApplication::translate("MainWindow", "GetHomeTimeline", 0, QApplication::UnicodeUTF8));
        fooButton->setText(QApplication::translate("MainWindow", "Foo", 0, QApplication::UnicodeUTF8));
        menuQTwitter->setTitle(QApplication::translate("MainWindow", "App", 0, QApplication::UnicodeUTF8));
        menuSettings->setTitle(QApplication::translate("MainWindow", "Settings", 0, QApplication::UnicodeUTF8));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
