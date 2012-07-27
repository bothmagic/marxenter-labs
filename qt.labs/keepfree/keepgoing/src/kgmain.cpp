#include "kgmain.h"
#include "../keepgoing-qt-build-desktop/ui_kgmain.h"
#include "todolistitemdelegate.h"
#include "model/taskmodel.h"
#include "model/contextmodel.h"
#include "util/kgressourceloader.h"
#include <QPushButton>
#include "model/kgitemmodel.h"
#include <QStandardItem>
#include "ui/kgvisualview.h"

KGMain::KGMain(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::KGMain)
{
    // init model
    initContextModel();
    KGRessourceLoader::instance()->loadRessources();
    ui->setupUi(this);

    m_model = new KGItemModel;

    ui->projectView->setModel(m_model);
    ui->widget->kgVisualView()->setModel(m_model);
    m_model->loadGroups();

    //m_visualItemFactory = new KGVisualItemFactory(ui->widget->kgVisualView());
    //m_groupFactory = new KGGroupFactory(m_visualItemFactory, m_model);

    //m_visualItemFactory->loadGroup(0, tr("INBOX"));
    //m_groupFactory->loadGroups();

    //connect(ui->inboxButton,SIGNAL(clicked()), m_visualItemFactory, SLOT(loadGroup()));
    //connect(ui->starredButton,SIGNAL(clicked()), m_visualItemFactory, SLOT(loadStarred()));
    connect(ui->lineEdit, SIGNAL(returnPressed()), this, SLOT(createProject()));

    /*QListWidgetItem *item = new QListWidgetItem("test", ui->listWidget, 0);
    new QListWidgetItem("test2", ui->listWidget, 0);
    item->setCheckState(Qt::Unchecked);
*/
    /*setWindowState(Qt::WindowMaximized);
    // init user interface
    initDockWindows();
    ui->toolBar->setVisible(false);
*/
    //initToolbar();

    // windows id, key id, mod, key
    //RegisterHotKey( this->winId(), 1, MOD_CONTROL, Qt::Key_N);
    // windows id, key id
    //UnregisterHotKey(this->winId(), 1);

}

void KGMain::initToolbar()
{
    /*QActionGroup *btGroup = new QActionGroup(0);
    btGroup->setExclusive(true);
    QAction *btInbox = ui->toolBar->addAction("Inbox");
    btInbox->setCheckable(true);

    btInbox->setActionGroup(btGroup);

    QAction *btToday = ui->toolBar->addAction("Today");
    btToday->setCheckable(true);
    btToday->setActionGroup(btGroup);

    QAction *btNext = ui->toolBar->addAction("Next");
    btNext->setCheckable(true);
    btNext->setActionGroup(btGroup);

    QAction *btScheduled = ui->toolBar->addAction("Scheduled");
    btScheduled->setCheckable(true);
    btScheduled->setActionGroup(btGroup);

    QAction *btSomeday = ui->toolBar->addAction("Someday");
    btSomeday->setCheckable(true);
    btSomeday->setActionGroup(btGroup);

    QAction *btProjects = ui->toolBar->addAction("Projects");
    btProjects->setCheckable(true);
    btProjects->setActionGroup(btGroup);

    connect(btProjects, SIGNAL(toggled(bool)), ui->dockProject, SLOT(setVisible(bool)));

    btInbox->setChecked(true);*/
}

void KGMain::initDockWindows()
{
    ui->dockProject->setVisible(true);

    QMainWindow::setCorner(Qt::BottomLeftCorner, Qt::LeftDockWidgetArea);
}

void KGMain::initContextModel()
{
    ContextModel *contextModel = ContextModel::instance();
    contextModel->setTable("context");
    contextModel->setEditStrategy(QSqlTableModel::OnManualSubmit);
    contextModel->select();

}

void KGMain::createProject()
{
    QStandardItem *item = new QStandardItem;
    item->setFlags(Qt::ItemIsEnabled|Qt::ItemIsSelectable|Qt::ItemIsDropEnabled|Qt::ItemIsDragEnabled);
    item->setText(ui->lineEdit->text());

    m_model->appendRow(item);
    ui->lineEdit->clear();

    ui->projectView->selectionModel()->select(
                m_model->indexFromItem(item),
                QItemSelectionModel::ClearAndSelect);
}

KGMain::~KGMain()
{
    delete ui;
}
