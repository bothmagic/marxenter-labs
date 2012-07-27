#include "kgvisualitemgroup.h"
#include "kgvisualitem.h"
#include "flowlayout.h"
#include <QPainter>
#include "kgvisualappenditem.h"
#include <QGraphicsSceneHoverEvent>
#include "kgvisualscene.h"
#include <QDebug>
#include "util/listutils.h"
#include <QStaticText>
#include <model/contextmodel.h>
#include <QToolButton>
#include <QGraphicsProxyWidget>

KGVisualItemGroup::KGVisualItemGroup(QGraphicsWidget *parent) :
    QGraphicsWidget(parent, Qt::Widget), m_isFiltered(0)
{
    FlowLayout *layout = new FlowLayout;
    layout->setSpacing(Qt::Horizontal, 40);
    layout->setSpacing(Qt::Vertical, 40);
    layout->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Minimum);
    layout->setContentsMargins(40,40,20,10);
    setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Minimum);
    setLayout(layout);

    KGVisualAppendItem *appendItem = new KGVisualAppendItem(KGVisualAppendItem::APPEND, this);
    appendItem->setPreferredSize(225, 155);
    layout->addItem(appendItem);

    p_visualInsertItem = new KGVisualAppendItem(KGVisualAppendItem::INSERT, this);
    p_visualInsertItem->setPreferredSize(40, 155);
    p_visualInsertItem->setVisible(false);

    setAcceptHoverEvents(true);
    setAcceptDrops(false);

    QGraphicsProxyWidget *w = createButton();

    w->setPos(0, 40);
    w->setParentItem(this);

    //setFlags(QGraphicsItem::ItemHasNoContents);

}

QRectF KGVisualItemGroup::boundingRect()
{
    return QRectF(pos(), size());
}

void KGVisualItemGroup::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{
    painter->setPen(QColor(Qt::black));
    painter->setBrush(QBrush(Qt::black));
    painter->drawRect(QRectF(QPointF(0,0),layout()->effectiveSizeHint(Qt::MinimumSize) - QSizeF(20, 0)));

    painter->setPen(QColor(155,155,155));
    painter->setFont(QFont(painter->font().family(), 12, QFont::Bold));
    painter->drawStaticText(45,8, QStaticText(m_name));

}

void KGVisualItemGroup::addVisualItem(KGVisualItem *visualItem, int index)
{
    visualItem->setParentItem(this);
    int insertIdx = index;
    if (index < 0)
    {
        index = m_visualItemList.count();
        insertIdx = index;
    }

    if (m_isFiltered)
    {
        insertIdx = m_filterMap.indexOf(index);
        if (insertIdx < 0)
        {
            insertIdx = m_filterMap.count();
        }
    }

    ((FlowLayout *)layout())->insertItem(insertIdx, visualItem);
    prepareVisualItem(visualItem);

    adjustSize();
    m_visualItemList.insert(index, visualItem);


    visualItem->setFocus();


    emit visualItemAdded(this, visualItem);

}

void KGVisualItemGroup::removeVisualItem(KGVisualItem *visualItem)
{
    int idx = m_visualItemList.indexOf(visualItem);
    m_visualItemList.removeAt(idx);
    if (m_isFiltered)
    {
        idx = m_filterMap.indexOf(idx);
    }
    layout()->removeAt(idx);
}

void KGVisualItemGroup::hoverMoveEvent(QGraphicsSceneHoverEvent *event)
{

    QPointF const mousePos = mapToScene(event->pos());
    QRectF rect;
    rect.setCoords(mousePos.x()-35, mousePos.y(), mousePos.x()+35, mousePos.y()+1);

    QList<KGVisualItem *> kgitems = filterByType<KGVisualItem*>(
                scene()->items(rect, Qt::IntersectsItemBoundingRect));
    if (!kgitems.isEmpty())
    {
        QList<KGVisualItem*> items;
        int insertIndex = 0;
        QPointF itemPos;
        bool visible = false;
        for (int i = 0; i < m_visualItemList.count(); i++)
        {
            if (kgitems.contains(m_visualItemList.at(i)))
            {
                items.append(m_visualItemList.at(i));
                insertIndex = i;
            }
        }

        if (items.size() == 2)
        {
            itemPos = items.first()->geometry().topRight();
            visible = true;
        } else if (items.size() == 1)
        {
            if (items.first()->geometry().left() > mousePos.x())
            {
                itemPos = items.first()->geometry().topLeft() - QPointF(p_visualInsertItem->size().width(),0);
                visible = true;
            }
        }
        p_visualInsertItem->setPos(itemPos);
        p_visualInsertItem->setVisible(visible);
        p_visualInsertItem->setInsertIndex(insertIndex);
    }
}

void KGVisualItemGroup::hoverLeaveEvent(QGraphicsSceneHoverEvent *event)
{
    p_visualInsertItem->setVisible(false);
    p_visualInsertItem->setPos(0,0);
}

bool KGVisualItemGroup::sceneEvent(QEvent *event)
{
    QGraphicsWidget::sceneEvent(event);
}

QSizeF KGVisualItemGroup::sizeHint(Qt::SizeHint which, const QSizeF &constraint) const
{
    return QGraphicsWidget::sizeHint(Qt::MinimumSize, constraint);
}

KGVisualItem* KGVisualItemGroup::prepareVisualItem(KGVisualItem *visualItem)
{
    visualItem->setContextModel(ContextModel::instance());
    return visualItem;
}

void KGVisualItemGroup::dragMoveEvent(QGraphicsSceneDragDropEvent *event)
{
}

void KGVisualItemGroup::filter(QList<int> filterList)
{
    m_filterMap.clear();
    m_isFiltered = 0;
    while (layout()->count() > 1) {
        layout()->removeAt(0);
    }

    if (!filterList.isEmpty())
    {
        m_isFiltered = 1;
    }

    int k = 0;
    KGVisualItem *visualItem;
    for (int i = 0; i < m_visualItemList.count(); i++)
    {
        visualItem = m_visualItemList.at(i);
        if (!filterList.contains(i))
        {
            ((FlowLayout *)layout())->insertItem(k++, visualItem);
            visualItem->setVisible(true);
            m_filterMap.append(i);
        } else {
            visualItem->setVisible(false);
        }
    }
    adjustSize();
}

QGraphicsProxyWidget *KGVisualItemGroup::createButton()
{
    QGraphicsProxyWidget *w = new QGraphicsProxyWidget;
    QToolButton *bt = new QToolButton();
    bt->setText("v/h");

    bt->setAttribute(Qt::WA_TranslucentBackground, true);
    w->setWidget(bt);
    bt->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Preferred);
    connect(bt, SIGNAL(clicked()), this, SLOT(stickyStateChanged()));
    return w;
}

void KGVisualItemGroup::stickyStateChanged()
{
    emit groupStickyStateChanged(this);
}
