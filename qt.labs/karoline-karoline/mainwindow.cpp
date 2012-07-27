#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "accountsdialog.h"
#include "twitterApi.h"
#include "timeline/timelineitem.h"
#include "nativequickwidgets.h"

#include <QDebug>
#include <QInputDialog>
#include <QDeclarativeContext>
#include <QDeclarativeView>
#include <QScrollArea>
#include <QPalette>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    twitterApi = new TwitterAPI;
    NativeQuickWidgets::qmlRegisterTypes();

    this->model = new TimelineModel(new TimelineItem, this);

    QDeclarativeView *timelineView = new QDeclarativeView(this);
    timelineView->setResizeMode(QDeclarativeView::SizeRootObjectToView);
//    timelineView->setVerticalScrollBarPolicy(Qt::ScrollBarAlwaysOn);
    timelineView->rootContext()->setContextProperty("timelineModel", this->model);
    timelineView->setSource(QUrl("qrc:/TimeLineView.qml"));

    ui->horizontalLayout_2->addWidget(timelineView);

//    QScrollArea *scrollArea = new QScrollArea();
//    scrollArea->set
//    scrollArea->setWidget(timelineView);
//    scrollArea->setVerticalScrollBarPolicy(Qt::ScrollBarAlwaysOn);
//    scrollArea->setWidgetResizable(true);

//    ui->horizontalLayout_2->addWidget(scrollArea);

//    ui->timelineView->rootContext()->setContextProperty("timelineModel", this->model);
//    ui->timelineView->setSource(QUrl("qrc:/TimeLineView.qml"));

    connect(ui->actionAccounts, SIGNAL(triggered()), this, SLOT(showAccountsDialog()));
    connect(twitterApi, SIGNAL(pinNeeded(QString)), this, SLOT(getToken(QString)));
    connect(ui->tweetText, SIGNAL(returnPressed()), this, SLOT(tweet()));
    connect(ui->getHomeTimelineButton,SIGNAL(clicked()), twitterApi, SLOT(getHomeTimeline()));
//    connect(ui->fooButton, SIGNAL(clicked()), this, SLOT(getSingleTweet()));
    connect(ui->fooButton, SIGNAL(clicked()), this, SLOT(getFoo()));
    //connect(twitterApi, SIGNAL(dataParsed(const QList<QString> &)), this, SLOT(dataReady(const QList<QString> &)));
    connect(twitterApi, SIGNAL(dataParsed(TimelineList)), this->model, SLOT(onDataParsed(TimelineList)));

//    typedef QList<QString> TimelineItemsList;
//    qRegisterMetaType<TimelineItemsList>("TimelineItemsList");

    this->twitterApi->getHomeTimeline();
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::showAccountsDialog()
{
    accountsDialog = new AccountsDialog(this);
    connect(accountsDialog, SIGNAL(authorize()), this, SLOT(getAuthorization()));
    accountsDialog->show();
}

void MainWindow::getAuthorization()
{
    twitterApi->authorize();
}

void MainWindow::getToken(QString token)
{
    bool ok;
    QString verifier = QInputDialog::getText(this, "Authorization", "Verifier:", QLineEdit::Normal, "", &ok);
    if (&ok && !verifier.isEmpty())
    {
        qDebug() << token << verifier;
        twitterApi->onAuthorizationReceived(token, verifier);
    }
}

void MainWindow::tweet()
{
    twitterApi->sendTweet(ui->tweetText->text());
    ui->tweetText->clear();
}

void MainWindow::getFoo()
{
//    twitterApi->singleTweet();
    twitterApi->foo();

}

void MainWindow::dataReady(const QList<QString> &data)
{
    qDebug() << data;
}

void MainWindow::changeEvent(QEvent *e)
{
    QMainWindow::changeEvent(e);
    switch (e->type()) {
    case QEvent::LanguageChange:
        ui->retranslateUi(this);
        break;
    default:
        break;
    }
}
