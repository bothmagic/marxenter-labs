/********************************************************************************
** Form generated from reading UI file 'kgmain.ui'
**
** Created: Tue 10. May 19:03:57 2011
**      by: Qt User Interface Compiler version 4.7.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_KGMAIN_H
#define UI_KGMAIN_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QApplication>
#include <QtGui/QButtonGroup>
#include <QtGui/QDockWidget>
#include <QtGui/QGridLayout>
#include <QtGui/QHeaderView>
#include <QtGui/QLineEdit>
#include <QtGui/QListView>
#include <QtGui/QMainWindow>
#include <QtGui/QPushButton>
#include <QtGui/QVBoxLayout>
#include <QtGui/QWidget>
#include "tasklistform.h"

QT_BEGIN_NAMESPACE

class Ui_KGMain
{
public:
    QWidget *centralWidget;
    QVBoxLayout *verticalLayout;
    TaskListForm *widget;
    QDockWidget *dockProject;
    QWidget *dockWidgetContents;
    QGridLayout *gridLayout_2;
    QListView *projectView;
    QLineEdit *lineEdit;
    QPushButton *inboxButton;
    QPushButton *starredButton;

    void setupUi(QMainWindow *KGMain)
    {
        if (KGMain->objectName().isEmpty())
            KGMain->setObjectName(QString::fromUtf8("KGMain"));
        KGMain->resize(813, 536);
        KGMain->setTabShape(QTabWidget::Rounded);
        KGMain->setDockNestingEnabled(false);
        KGMain->setDockOptions(QMainWindow::AnimatedDocks|QMainWindow::VerticalTabs);
        centralWidget = new QWidget(KGMain);
        centralWidget->setObjectName(QString::fromUtf8("centralWidget"));
        verticalLayout = new QVBoxLayout(centralWidget);
        verticalLayout->setSpacing(6);
        verticalLayout->setContentsMargins(0, 0, 0, 0);
        verticalLayout->setObjectName(QString::fromUtf8("verticalLayout"));
        widget = new TaskListForm(centralWidget);
        widget->setObjectName(QString::fromUtf8("widget"));
        widget->setMinimumSize(QSize(150, 0));

        verticalLayout->addWidget(widget);

        KGMain->setCentralWidget(centralWidget);
        dockProject = new QDockWidget(KGMain);
        dockProject->setObjectName(QString::fromUtf8("dockProject"));
        dockProject->setEnabled(true);
        dockProject->setMinimumSize(QSize(212, 256));
        dockProject->setFloating(false);
        dockProject->setFeatures(QDockWidget::DockWidgetVerticalTitleBar);
        dockProject->setAllowedAreas(Qt::NoDockWidgetArea);
        dockWidgetContents = new QWidget();
        dockWidgetContents->setObjectName(QString::fromUtf8("dockWidgetContents"));
        gridLayout_2 = new QGridLayout(dockWidgetContents);
        gridLayout_2->setSpacing(6);
        gridLayout_2->setContentsMargins(11, 11, 11, 11);
        gridLayout_2->setObjectName(QString::fromUtf8("gridLayout_2"));
        projectView = new QListView(dockWidgetContents);
        projectView->setObjectName(QString::fromUtf8("projectView"));

        gridLayout_2->addWidget(projectView, 5, 0, 1, 1);

        lineEdit = new QLineEdit(dockWidgetContents);
        lineEdit->setObjectName(QString::fromUtf8("lineEdit"));

        gridLayout_2->addWidget(lineEdit, 4, 0, 1, 1);

        inboxButton = new QPushButton(dockWidgetContents);
        inboxButton->setObjectName(QString::fromUtf8("inboxButton"));
        inboxButton->setCheckable(true);
        inboxButton->setAutoExclusive(true);

        gridLayout_2->addWidget(inboxButton, 2, 0, 1, 1);

        starredButton = new QPushButton(dockWidgetContents);
        starredButton->setObjectName(QString::fromUtf8("starredButton"));
        starredButton->setAcceptDrops(true);
        starredButton->setCheckable(true);
        starredButton->setChecked(false);
        starredButton->setFlat(false);

        gridLayout_2->addWidget(starredButton, 3, 0, 1, 1);

        dockProject->setWidget(dockWidgetContents);
        KGMain->addDockWidget(static_cast<Qt::DockWidgetArea>(1), dockProject);

        retranslateUi(KGMain);

        QMetaObject::connectSlotsByName(KGMain);
    } // setupUi

    void retranslateUi(QMainWindow *KGMain)
    {
        KGMain->setWindowTitle(QApplication::translate("KGMain", "KGMain", 0, QApplication::UnicodeUTF8));
        dockProject->setWindowTitle(QApplication::translate("KGMain", "Keepgoing", 0, QApplication::UnicodeUTF8));
        lineEdit->setPlaceholderText(QApplication::translate("KGMain", "Neues Projekt", 0, QApplication::UnicodeUTF8));
        inboxButton->setText(QApplication::translate("KGMain", "Inbox", 0, QApplication::UnicodeUTF8));
        starredButton->setText(QApplication::translate("KGMain", "Starred", 0, QApplication::UnicodeUTF8));
    } // retranslateUi

};

namespace Ui {
    class KGMain: public Ui_KGMain {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_KGMAIN_H
