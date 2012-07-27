#include "kgvisualview.h"
#include <QGraphicsScene>
#include <QtDebug>
#include <QGLWidget>
#include <QScrollBar>
#include "kgvisualscene.h"
#include "../command/kgnewitem.h"
#include "kgvisualitem.h"
#include <QPropertyAnimation>
#include <QParallelAnimationGroup>
#include "src/util/listutils.h""
#include <QSqlQuery>
#include "src/testitem.h"
#include "src/ui/kgflowpane.h"
#include <QGraphicsRectItem>
#include "kgvisualitemlist.h"
#include <QTextEdit>
#include <QGraphicsProxyWidget>
#include "qgraphicslisttext.h"
#include "model/contextmodel.h"
#include "ui/kgvisualitemgroup.h"
#include "flowlayout.h"
#include "util/kgcommandfactory.h"
#include "kgvisualcontextlist.h"
#include <QGraphicsSceneMouseEvent>
#include <QAbstractItemModel>

KGVisualView::KGVisualView(QWidget *parent)
    :QGraphicsView(new KGVisualScene, parent)
{
    KGCommandFactory::instance()->setKGVisualView(this);

    //setViewport(new QGLWidget(QGLFormat(QGL::SampleBuffers)));
    scene()->setBackgroundBrush(QColor(46,46,46));
    setViewportUpdateMode(QGraphicsView::SmartViewportUpdate);
    scene()->setItemIndexMethod(QGraphicsScene::NoIndex);
    //setOptimizationFlags(QGraphicsView::DontSavePainterState);
    //setRenderHints(QPainter::HighQualityAntialiasing);

    //scene()->setSceneRect(viewport()->rect());

    p_globalItemGroup = new QGraphicsWidget;
    p_globalItemGroup->setPos(0,0);

    scene()->addItem(p_globalItemGroup);
    p_globalItemGroup->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Minimum);
    QGraphicsLinearLayout *linLay = new QGraphicsLinearLayout(Qt::Vertical);

    linLay->setSpacing(10);

    p_globalItemGroup->setLayout(linLay);

    p_lastSelectedSingleItem = 0;

    setVerticalScrollBarPolicy(Qt::ScrollBarAsNeeded);

}

KGVisualView::~KGVisualView()
{
    //KGVisualItem *item;

    //QList<KGVisualItem*> viItems = filterByType<KGVisualItem*>(items());
    //QListIterator<KGVisualItem*> it(viItems);

}


void KGVisualView::reload()
{

    clear();
    //m_visualItemFactory->loadItems(QVariant::Invalid);


}

void KGVisualView::fieldChanged(int row, const char *name, QVariant value)
{
    qDebug() << "fieldChanged " << name << " : " << value;

    if (!strcmp("contexts", name))
    {
        /*QString contexts = taskModel->index(row, taskModel->fieldIndex("contexts")).data().toString();
        QStringList ctxList = contexts.split("\t");
        if (ctxList.contains(value.toString()))
        {
            ctxList.removeOne(value.toString());
        } else
        {
            ctxList.append(value.toString());
        }*/

        //taskModel->setData(taskModel->index(row, taskModel->fieldIndex(name)), value.toStringList().join("\t"), Qt::EditRole);
    } else
    {
        //taskModel->setData(taskModel->index(row, taskModel->fieldIndex(name)), value, Qt::EditRole);
    }
}

void KGVisualView::scrollToGroup(int group)
{
    QRectF groupRect = m_groupList.at(group)->boundingRect();

    QScrollBar *vBar = verticalScrollBar();
    vBar->setValue(vBar->value() + groupRect.y() - vBar->value());
}

void KGVisualView::clear()
{

}

void KGVisualView::setModel(QAbstractItemModel *model)
{
    m_model = model;

    connect(m_model, SIGNAL(rowsInserted(QModelIndex,int,int)), this, SLOT(modelChanged(QModelIndex,int,int)));
}

QAbstractItemModel* KGVisualView::model() const
{
    return m_model;
}

void KGVisualView::insertItem(KGVisualItem *item, int group, int index)
{
    KGVisualItemGroup *itemGroup = static_cast<KGVisualItemGroup *>(p_globalItemGroup->layout()->itemAt(group));
    itemGroup->addVisualItem(item, index);
    //itemGroup->adjustSize();
    //p_updateGroupLayout();
    /*KGVisualItemList *kglist = new KGVisualItemList;

    KGFlowPane *flowPane = new KGFlowPane;
      flowPane->setParentItem(item);
    flowPane->setOwner(item, Qt::AnchorTop);
    flowPane->setPane(kglist);

    if (item->state() == KGVisualItem::New)
        flowPane->show();
*/
/*
    flowPane = new KGFlowPane;
    flowPane->setParentItem(item);
    QGraphicsRectItem *it = new QGraphicsRectItem(0,0,60,60);
    flowPane->setOwner(item, Qt::AnchorRight);
    flowPane->setPane(it);
    it->setBrush(QBrush(QColor(Qt::white)));
*/


}

void KGVisualView::removeItem(KGVisualItem *item)
{
    KGVisualItemGroup *itemGroup = static_cast<KGVisualItemGroup *>(item->parentItem());
    int itemIdx = itemGroup->allVisualItems().indexOf(item);
    itemGroup->removeVisualItem(item);
    disconnect(item, SIGNAL(itemUpdated(KGVisualItem*,QString)), this, SLOT(itemUpdated(KGVisualItem*, QString)));
    scene()->removeItem(item);
    if (item->state() == KGVisualItem::Removed)
        emit visualItemRemoved(m_groupList.indexOf(itemGroup), itemIdx);
    itemGroup->adjustSize();
    p_updateGroupLayout();
}

int KGVisualView::createGroup(QString const &groupName)
{
    KGVisualItemGroup *newGroup = new KGVisualItemGroup;
    newGroup->setName(groupName);
    QGraphicsLinearLayout *linlay = static_cast<QGraphicsLinearLayout *>(p_globalItemGroup->layout());

    linlay->addItem(newGroup);
    newGroup->adjustSize();
    newGroup->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Preferred);
    newGroup->layout()->setPreferredWidth(p_globalItemGroup->preferredWidth());
    newGroup->layout()->setMinimumWidth(p_globalItemGroup->preferredWidth());
    newGroup->layout()->setMaximumWidth(p_globalItemGroup->preferredWidth());

    p_updateGroupLayout();

    m_groupList.append(newGroup);

    connect(newGroup, SIGNAL(visualItemAdded(KGVisualItemGroup*,KGVisualItem*)),
            this, SLOT(visualItemAdded(KGVisualItemGroup*,KGVisualItem*)));

    return m_groupList.count()-1;

}

KGVisualItemGroup *KGVisualView::removeGroup(int group)
{
    KGVisualItemGroup *itemGroup = m_groupList.at(group);

    disconnect(itemGroup, SIGNAL(visualItemAdded(KGVisualItemGroup*,KGVisualItem*)),
            this, SLOT(visualItemAdded(KGVisualItemGroup*,KGVisualItem*)));

    m_groupList.removeAt(group);

    foreach (KGVisualItem *visualItem, itemGroup->allVisualItems())
    {
        removeItem(visualItem);
    }

    scene()->removeItem(itemGroup);
    p_globalItemGroup->layout()->removeAt(group);

    p_updateGroupLayout();
    return itemGroup;
}

void KGVisualView::filterContext(QStringList contextList)
{
    KGVisualItem *item;
    QList<int> filterList;
    foreach(KGVisualItemGroup *group, m_groupList)
    {
        for(int i=0;i < group->allVisualItems().count();i++)
        {

            item = group->allVisualItems().at(i);
            foreach(QString context, contextList)
            {
                if (!item->context().contains(context)) {
                    filterList.append(i);
                }
            }

        }
        group->filter(filterList);
        filterList.clear();

    }
    p_updateGroupLayout();
}

void KGVisualView::mouseReleaseEvent(QMouseEvent *event)
{

    QGraphicsView::mouseReleaseEvent(event);
    QList<QGraphicsItem *> selItems = scene()->selectedItems();
    QPainterPath selPath = scene()->selectionArea();

    //qDebug() << "SelectedItems: " << selItems.size();
    //qDebug() << "Selectionpath-lefttopcorner" << selPath.boundingRect().topLeft();

    QPointF pos = this->mapToScene(QPoint(event->x(), event->y()));
    if (!scene()->itemAt(pos)
        && p_createNewItemOnMouseRelease)
    {
        /*KGNewItem *command = new KGNewItem(p_visualItemFactory, pos.x(), pos.y());

        p_undoStack->push(command);
        p_createNewItemOnMouseRelease = false;
*/
    }

    if (event->modifiers().testFlag(Qt::ControlModifier))
    {

        if (selItems.size() > 0 && p_lastSelectedSingleItem)
        {

            p_lastSelectedSingleItem->setSelected(true);

        } /*else if (selItems.size() == 1 && )
        {

            /*QGraphicsItem *selItem = selItems.at(0);
            selItem->setOpacity(1);
            foreach (QGraphicsItem *it, items(selItem->mapToScene(selItem->boundingRect()).boundingRect().toRect()))
            {

                if (qgraphicsitem_cast<KGVisualItem *>(it) && it != selItems.at(0))
                {
                    it->setOpacity(0.1);
                }
            }
        }*/
    } else if (selItems.size() == 1)
    {
        p_lastSelectedSingleItem = selItems.at(0);
    } else {
        p_lastSelectedSingleItem = 0;
    }
}

void KGVisualView::mousePressEvent(QMouseEvent *event)
{
    //qDebug() << "items.size" << items().size();

    if (scene()->selectedItems().size() > 0)
    {
        /*int idx = items().indexOf(scene()->selectedItems().at(0));
        QGraphicsItem *item = items().at(idx);

        for (int i = idx-1; i>0; i--) // bring item to forground
        {
            firstOccurenceByType<KGVisualItem*>(items())
                    ->stackBefore(item);
        }*/


    } else if (scene()->focusItem() == 0)
    {
        p_createNewItemOnMouseRelease = true;
    }

    QGraphicsView::mousePressEvent(event);

}

void KGVisualView::mouseMoveEvent(QMouseEvent *event)
{
    QGraphicsView::mouseMoveEvent(event);
    p_createNewItemOnMouseRelease = false;
}

void KGVisualView::keyPressEvent(QKeyEvent *event)
{
    QGraphicsView::keyPressEvent(event);
    if (!scene()->focusItem() && !scene()->activeWindow() && (event->key() == Qt::Key_Delete || event->key() == Qt::Key_Backspace))
    {
            //QListIterator<QGraphicsItem*> it(scene()->selectedItems());
            foreach (QGraphicsItem *item , scene()->selectedItems())
            {

                if (qgraphicsitem_cast<KGVisualItem *>(item))
                {
                    KGVisualItem *visualItem = qgraphicsitem_cast<KGVisualItem *>(item);
                    visualItem->setState(KGVisualItem::Removed);
                    removeItem(visualItem);
                }
            }
    }
}

void KGVisualView::resizeEvent(QResizeEvent *event)
{
    QGraphicsView::resizeEvent(event);
    p_globalItemGroup->setPreferredWidth(viewport()->size().width());

    p_updateSceneRect();
}


void KGVisualView::p_updateSceneRect()
{
    foreach (KGVisualItemGroup *group, m_groupList)
    {
        group->layout()->setPreferredWidth(p_globalItemGroup->preferredWidth());
        group->layout()->setMinimumWidth(p_globalItemGroup->preferredWidth());
        group->layout()->setMaximumWidth(p_globalItemGroup->preferredWidth());

    }

    p_globalItemGroup->adjustSize();
    setSceneRect(0,0,p_globalItemGroup->preferredWidth(), p_globalItemGroup->preferredHeight());

}

void KGVisualView::visualItemAdded(KGVisualItemGroup *group, KGVisualItem *visualItem)
{
    p_updateGroupLayout();
    connect(visualItem, SIGNAL(itemUpdated(KGVisualItem*,QString)), this, SLOT(itemUpdated(KGVisualItem*, QString)));
    connect(visualItem, SIGNAL(startDrag(KGVisualItem*,QGraphicsSceneMouseEvent*)), this, SLOT(visualItemStartDrag(KGVisualItem*,QGraphicsSceneMouseEvent*)));
    emit visualItemAdded(m_groupList.indexOf(group), group->allVisualItems().indexOf(visualItem));
}

void KGVisualView::itemUpdated(KGVisualItem *item, QString objectName)
{
    KGVisualItemGroup *group = static_cast<KGVisualItemGroup*>(item->parentItem());

    emit visualItemUpdated(m_groupList.indexOf(group), group->allVisualItems().indexOf(item), objectName);
}

void KGVisualView::moveVisualItem(int fromGroup, int item, int toGroup)
{
    KGVisualItem *vItem = visualItem(fromGroup, item);
    removeItem(vItem);
    insertItem(vItem, toGroup);
}

void KGVisualView::visualItemStartDrag(KGVisualItem *visualItem, QGraphicsSceneMouseEvent *event)
{

    KGVisualItemGroup *group = static_cast<KGVisualItemGroup*>(visualItem->parentItem());

    QDrag *drag = new QDrag(event->widget());
    QMimeData *mime = new QMimeData;
    drag->setMimeData(mime);

    QByteArray block;
    QDataStream out(&block, QIODevice::WriteOnly);
    out << (qint16)m_groupList.indexOf(group) << (qint16)group->allVisualItems().indexOf(visualItem);
    mime->setData("keepgoing", block);
    if(drag->exec() == Qt::MoveAction) {

    }
}

void KGVisualView::p_updateGroupLayout()
{
    p_globalItemGroup->layout()->activate();
    p_globalItemGroup->adjustSize();
    setSceneRect(0,0,p_globalItemGroup->preferredWidth(), p_globalItemGroup->preferredHeight());
}

void KGVisualView::modelChanged(QModelIndex parent,int start,int end)
{
    QModelIndex mIndex = m_model->index(start,0);

    if (!parent.isValid())
    {
        createGroup(mIndex.data().toString());
    } else
    {
        KGVisualItem *item = new KGVisualItem;
        item->setDetail(m_model->index(start, 0, parent).data().toString());
        item->setContext(m_model->index(start, 1, parent).data().toStringList());
        item->setWorkState(m_model->index(start, 2, parent).data().toInt());
        item->setState(KGVisualItem::Loaded);

        insertItem(item, parent.row(), start);
    }
}

QList<KGVisualItemGroup *> KGVisualView::allGroups()
{
    return m_groupList;
}

KGVisualItem* KGVisualView::visualItem(int group, int item)
{
    return m_groupList.at(group)->allVisualItems().at(item);
}
