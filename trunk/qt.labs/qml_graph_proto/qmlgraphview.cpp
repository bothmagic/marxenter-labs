#include "qmlgraphview.h"
#include <QPainter>
#include <QDebug>
#include <QApplication>
#include <QDeclarativeProperty>
#include "qmlgraphitem.h"
#include "qmlgraphedge.h"

QmlGraphView::QmlGraphView(QDeclarativeItem *parent) :
    QDeclarativeItem(parent)
{
    setFlag(QGraphicsItem::ItemHasNoContents, false);
    setFiltersChildEvents(false);
}

//void QmlGraphView::paint(QPainter *p, const QStyleOptionGraphicsItem *, QWidget *)
//{

//    if (!m_end|| !m_start)
//        return;

//    p->save();
//    p->setPen(QPen(Qt::black));

//    QRectF startR = m_start->boundingRect().translated(m_start->x(), m_start->y());
//    QRectF endR = m_end->boundingRect().translated(m_end->x(), m_end->y());

//    p->drawLine(startR.center(), endR.center());


//    p->restore();

//}

//void QmlGraphView::registerItem(QmlGraphItem *start)
//{
//    connect(start, SIGNAL(xChanged()), this, SLOT(upaint()));
//    connect(start, SIGNAL(yChanged()), this, SLOT(upaint()));
//}


void QmlGraphView::updateItem(int itemId)
{
    QHash<int, QmlGraphEdge*>::const_iterator i = m_edge.find(itemId);
    QmlGraphItem* item = m_items.value(itemId);

    if (item) {
        while (i != m_edge.end() && i.key() == itemId) {
            if (i.value()->firstItemId() == itemId) {
                QPointF p;
                if (item->parent() != this) {
                    p = item->mapRectToScene(item->boundingRect()).center();
                } else {
                    p = item->boundingRect().translated(item->x(), item->y()).center();
                }
                i.value()->setFirstPoint(p);
            } else if (i.value()->secondItemId() == itemId) {
                QPointF p;
                if (item->parent() != this) {
                    p = item->mapRectToScene(item->boundingRect()).center();
                } else {
                    p = item->boundingRect().translated(item->x(), item->y()).center();
                }
                i.value()->setSecondPoint(p);
            }
            ++i;
        }
    }
    update();
}

void QmlGraphView::addEdge(int firstId, int secondId)
{
    QmlGraphEdge* edge = new QmlGraphEdge(this);
    edge->setFirstItemId(firstId);
    edge->setSecondItemId(secondId);
    m_edge.insert(firstId, edge);
    m_edge.insert(secondId, edge);
}

void QmlGraphView::registerItem(int id, QmlGraphItem *node)
{
    node->setGraphItemId(id);
    m_items.insert(id, node);
    connect(node, SIGNAL(itemChanged(int)), this, SLOT(updateItem(int)));
}

void QmlGraphView::registerEdge(int firstId, int secondId)
{
    addEdge(firstId, secondId);
}





