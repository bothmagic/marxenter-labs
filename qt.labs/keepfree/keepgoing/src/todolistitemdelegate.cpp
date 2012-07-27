#include "todolistitemdelegate.h"
#include <qstyleoption.h>
#include <QSpinBox>
#include <QApplication>
#include <QPainter>
#include <QStyledItemDelegate>
#include <QTextEdit>
#include <QSqlTableModel>
#include <QListView>
#include <QMouseEvent>
#include <QPoint>
#include "taskdetailform.h"
#include "model/taskmodel.h"

TodoListItemDelegate::TodoListItemDelegate(QObject *parent)
    :QStyledItemDelegate(parent), inEditMode(-1)
{

}


//! [1]
QWidget *TodoListItemDelegate::createEditor(QWidget *parent,
    const QStyleOptionViewItem & option ,
    const QModelIndex &index ) const
{   
    if (!index.isValid())
        return 0;
    //option.state = option.state | QStyle::State_Editing;

    TaskDetailForm *editor = new TaskDetailForm(0, parent);

    //QWidget *editor = QStyledItemDelegate::createEditor(parent, option, index);

    //QAbstractItemModel model = <qobject_cast(TaskModel)>index.model();
    //model.setData(index, 0, Qt::EditRole);
    inEditMode = index.row();
    TaskModel *mymodel = const_cast<TaskModel*>(dynamic_cast<const TaskModel*>(index.model()));
    qDebug("create editor for index row %d", index.row());
    mymodel->wantsToUpdate();
    connect(editor, SIGNAL(destroyed()), mymodel, SLOT(wantsToUpdate()));
    return editor;

}
//! [1]

//! [2]
void TodoListItemDelegate::setEditorData(QWidget *editor,
                                    const QModelIndex &index) const
{
    TaskDetailForm *myeditor = qobject_cast<TaskDetailForm*>(editor);
    TaskModel *mymodel = const_cast<TaskModel*>(dynamic_cast<const TaskModel*>(index.model()));

    //myeditor->setComplete(index.data(Qt::CheckStateRole));
    //myeditor->setShortDescr(mymodel->index(index.row(), mymodel->fieldIndex("shortdescr")).data(Qt::EditRole));
    //connect(mymodel, SIGNAL(dataChanged(QModelIndex,QModelIndex)), myeditor, SLOT(dataChanged(QModelIndex,QModelIndex)));

}
//! [2]

//! [3]
void TodoListItemDelegate::setModelData(QWidget *editor, QAbstractItemModel *model,
                                   const QModelIndex &index) const
{
    TaskDetailForm *myeditor = qobject_cast<TaskDetailForm*>(editor);
    //model->setData(index, myeditor->complete(), Qt::CheckStateRole);
    //model->setData(model->index(index.row(), 2), myeditor->shortDescr());

    TaskModel *mymodel = const_cast<TaskModel*>(dynamic_cast<const TaskModel*>(index.model()));

    //mymodel->submitAll();

    qDebug("setmodel data for index row %d", index.row());

    inEditMode = -1;
    //disconnect(model, SIGNAL(dataChanged(QModelIndex,QModelIndex)), myeditor, SLOT(dataChanged(QModelIndex,QModelIndex)));
}
//! [3]


QSize TodoListItemDelegate::sizeHint(const QStyleOptionViewItem &option, const QModelIndex &index) const
{

    QSize size = QStyledItemDelegate::sizeHint(option, index);

    if (index.row() == inEditMode)
    {
        size.setHeight(200);
        //qDebug("height for index row %d is %d", index.row(), size.height());
    } else
    {
        size.setHeight(26);
    }

    return size;
}

//! [4]
void TodoListItemDelegate::updateEditorGeometry(QWidget *editor,
    const QStyleOptionViewItem &option, const QModelIndex &/* index */) const
{
    QRect rect(option.rect.left(), option.rect.top(), option.rect.width(), 200);
    editor->setGeometry(rect);
}

//! [4]
void TodoListItemDelegate::paint( QPainter* painter, const QStyleOptionViewItem& option,
    const QModelIndex& index) const
{

    Q_ASSERT(index.isValid());

    QStyleOptionViewItemV4 opt = option;
    QStyledItemDelegate::initStyleOption(&opt, index);

    painter->save();


    if (index.row() != inEditMode) {
        opt.features |= QStyleOptionViewItemV2::HasCheckIndicator;

        opt.viewItemPosition = QStyleOptionViewItemV4::Beginning;
        opt.rect.setWidth(opt.rect.width()-100);

        QStyledItemDelegate::paint(painter, opt, index.model()->index(index.row(), 2, index.parent()));

        opt.features = QStyleOptionViewItemV2::None;
        opt.rect.setX(opt.rect.width());
        opt.rect.setWidth(opt.rect.width()+100);
        opt.viewItemPosition = QStyleOptionViewItemV4::End;


        QStyledItemDelegate::paint(painter, opt, index.model()->index(index.row(), 3, index.parent()));
    } else {
        opt.features = QStyleOptionViewItemV4::None;
       opt.showDecorationSelected = true;
       QStyledItemDelegate::paint(painter, opt, index.model()->index(index.row(), 3, index.parent()));
    }
    painter->restore();

}

bool TodoListItemDelegate::eventFilter(QObject *object, QEvent *event)
{

    if (event->type() == QEvent::FocusOut)
    {
        return false;
    }

    bool evt = QStyledItemDelegate::eventFilter(object, event);

    return false;
}

bool TodoListItemDelegate::editorEvent(QEvent *event,
                                QAbstractItemModel *model,
                                const QStyleOptionViewItem &option,
                                const QModelIndex &index)
{
    if (inEditMode == index.row() && event->type() == QEvent::MouseButtonRelease)
    {
        QMouseEvent *me = static_cast<QMouseEvent*>(event);
        QMouseEvent *me1;
        if (me->pos().y()+94 > option.rect.bottomLeft().y())
        {
            const QPoint pos(me->pos().x(), me->pos().y()-88);
            me1 = new QMouseEvent(me->type(), pos, me->button(), me->buttons(), me->modifiers());
        } else
        {
            const QPoint pos(me->pos().x(), me->pos().y()+88);
            me1 = new QMouseEvent(me->type(), pos, me->button(), me->buttons(), me->modifiers());
        }
        return QStyledItemDelegate::editorEvent(me1, model, option, index);

    } else
    {

        return QStyledItemDelegate::editorEvent(event, model, option, index);

    }


}



