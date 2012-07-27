#include "taskdetailform.h"
#include "ui_taskdetailform.h"
#include "taskcontextitemdelegate.h"
#include "flowlayout.h"
#include "ui/contextcombobox.h"
#include <QPushButton>
#include <QLineEdit>
#include <QCompleter>
#include <QSqlRecord>
#include <QDebug>
#include <QKeyEvent>
#include <QToolTip>
#include "model/contextmodel.h"
#include <QStyle>
#include <QGraphicsView>

void TaskDetailForm::setDescription(QString description)
{
    ui->textEdit->setHtml(description);

}

void TaskDetailForm::setContexts(QStringList contexts)
{
    QPushButton *newContext;
    btNewContext->setVisible(false);

    ui->contextLayout->layout()->removeWidget(btNewContext);

    FlowLayout *layout = static_cast<FlowLayout*>(ui->contextLayout->layout());

    for (int i = layout->count() -1 ; i >= 0; i--)
    {
        QLayoutItem *item = layout->itemAt(i);
        if (item->widget() != btNewContext && item->widget() != leNewContext)
        {
            item->widget()->deleteLater();

        }
        layout->removeItem(item);

    }

    QStringListIterator i(contexts);
    while (i.hasNext())
    {
        QString text = i.next();
        if (text.isEmpty())
            continue;
        newContext = new QPushButton(text);
        newContext->installEventFilter(this);
        ui->contextLayout->layout()->addWidget(newContext);
    }
    ui->contextLayout->layout()->addWidget(btNewContext);
    btNewContext->setVisible(true);
}

TaskDetailForm::TaskDetailForm(QGraphicsProxyWidget *proxy, QWidget *parent) :
    QWidget(parent), grProxy(proxy),
    ui(new Ui::TaskDetailForm)
{
    ui->setupUi(this);

    QSqlTableModel *contextModel = ContextModel::instance();
    /*contextModel->setTable("context");
    contextModel->setEditStrategy(QSqlTableModel::OnManualSubmit);
    contextModel->select();*/


    /*ui->comboBox->setModel(contextModel);
    ui->comboBox->setModelColumn(1);*/

    FlowLayout *flowLayout = new FlowLayout;
    flowLayout->setContentsMargins(0,0,0,10);
    flowLayout->setSpacing(0);

    //flowLayout->addWidget(comboBox);

    btNewContext = new QPushButton(tr("New Context"));
    flowLayout->addWidget(btNewContext);
    connect(btNewContext, SIGNAL(released()), this, SLOT(appendLeNewContext()));

    leNewContext = new QLineEdit;
    leNewContext->setFocusPolicy(Qt::StrongFocus);
    QCompleter *completer = new QCompleter;
    completer->setModel(contextModel);
    completer->setCompletionColumn(1);
    completer->setCompletionMode(QCompleter::InlineCompletion);
    completer->setCaseSensitivity(Qt::CaseInsensitive);
    leNewContext->setCompleter(completer);
    leNewContext->installEventFilter(this);
    connect(leNewContext, SIGNAL(returnPressed()), this, SLOT(storeContextAndAppendNew()));
    connect(leNewContext, SIGNAL(textChanged(QString)), this, SLOT(checkContextCompleter()));
    flowLayout->addWidget(leNewContext);
    ui->contextLayout->setLayout(flowLayout);
    leNewContext->setVisible(false);

    ui->textEdit->installEventFilter(this);
}

TaskDetailForm::~TaskDetailForm()
{
    delete ui;
}

void TaskDetailForm::appendLeNewContext()
{

    btNewContext->setHidden(true);
    leNewContext->setVisible(true);
    ui->contextLayout->layout()->removeWidget(btNewContext);
    ui->contextLayout->layout()->addWidget(leNewContext);
    leNewContext->setFocus();
}

/**
  * store context if selected context exist.
  */
void TaskDetailForm::storeContext()
{
    leNewContext->setHidden(true);

    QString text = leNewContext->text();

    if (!text.isEmpty() && leNewContext->completer()->currentIndex().isValid())
    { // add new context to layout and emit signal
        QPushButton *newContext = new QPushButton(text);
        newContext->installEventFilter(this);
        btNewContext->setVisible(true);
        ui->contextLayout->layout()->removeWidget(leNewContext);
        ui->contextLayout->layout()->addWidget(newContext);
        ui->contextLayout->layout()->addWidget(btNewContext);
        btNewContext->setFocus();
        emit appendContext(leNewContext->text());
    } else
    {

        removeLeContext();
    }
    leNewContext->clear();
    return;
}

void TaskDetailForm::checkContextCompleter()
{
    if (leNewContext->text().size() < 2)
    {
        QToolTip::showText(
                leNewContext->pos(),
                "");
        return;
    }
    if (!leNewContext->completer()->currentIndex().isValid() && !leNewContext->text().isEmpty())
    {

        QPointF point = grProxy->mapToScene(leNewContext->geometry().topLeft());

        QToolTip::showText(
                grProxy->scene()->views().at(0)->mapToGlobal(point.toPoint()) + QPoint(10, -5),
                tr("Press Enter to create a new context!"));
    } else
    {
        QToolTip::showText(
                leNewContext->pos(),
                "");
    }
}

void TaskDetailForm::removeLeContext()
{
    leNewContext->setHidden(true);

    btNewContext->setVisible(true);
    ui->contextLayout->layout()->removeWidget(leNewContext);
    ui->contextLayout->layout()->addWidget(btNewContext);
    btNewContext->setFocus();

}


void TaskDetailForm::storeContextAndAppendNew()
{

    QString text = leNewContext->text();

    if (!text.isEmpty())
    {
        if (!leNewContext->completer()->currentIndex().isValid())
        {
            // context not found in completer, create context if createContext is true
            QSqlTableModel *ctxModel = static_cast<QSqlTableModel*>(leNewContext->completer()->model());
            QSqlRecord record = ctxModel->record();
            record.setValue(ctxModel->fieldIndex("shortdescr"), leNewContext->text());

            ctxModel->insertRecord(ctxModel->rowCount(), record);
            ctxModel->submitAll();
            qDebug() << "create new context" << leNewContext->text();
        }
        removeLeContext();
        appendLeNewContext();
    } else
    {
        removeLeContext();
    }
}

void TaskDetailForm::connectContext(QListWidgetItem *item)
{
    if (!item->data(Qt::UserRole).toBool())
    {

        item->setData(Qt::UserRole, true);
        QListWidgetItem *newItem = new QListWidgetItem(tr("Add Context"));

        newItem->setFlags(Qt::ItemIsEnabled|Qt::ItemIsEditable|Qt::ItemIsSelectable);
        //ui->listWidget->addItem(newItem);

    }
}

void TaskDetailForm::dataChanged(QModelIndex index1, QModelIndex index2)
{

}

bool TaskDetailForm::eventFilter(QObject *obj, QEvent *event)
{
    if (obj == leNewContext)
    {
        if (event->type() == QEvent::KeyPress)
        {
            QKeyEvent *keyEvent = static_cast<QKeyEvent *>(event);
            if (keyEvent->key() == Qt::Key_Escape)
            {
                removeLeContext();
                return true;
            }

            if (keyEvent->key() == Qt::Key_Tab)
            {
                ui->textEdit->setFocus();
                return true;
            }
        } else if (event->type() == QEvent::FocusOut)
        {
            storeContext();
        }

    } else if (qobject_cast<QPushButton*>(obj) && event->type() == QEvent::MouseButtonRelease)
    {
        QPushButton *bt = qobject_cast<QPushButton*>(obj);
        bt->setVisible(false);
        ui->contextLayout->layout()->removeWidget(bt);
        emit removeContext(bt->text());
        bt->deleteLater();;

    } else if (qobject_cast<QTextEdit*>(obj) && event->type() == QEvent::FocusOut)
    {
        emit changeDescription(ui->textEdit->document()->toHtml());
        return true;
    }

    return false;
}
