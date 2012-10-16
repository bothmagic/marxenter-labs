#include "qmlgraphedge.h"
#include <QPainter>
QmlGraphEdge::QmlGraphEdge(QDeclarativeItem *parent) :
    QDeclarativeItem(parent)
{
    setFlag(QGraphicsItem::ItemHasNoContents, false);
    //setFlag(QGraphicsItem::ItemStacksBehindParent, true);
}

void QmlGraphEdge::setFirstItemId(int id)
{
    m_firstItemId = id;
}

int QmlGraphEdge::firstItemId() const
{
    return m_firstItemId;
}

void QmlGraphEdge::setSecondItemId(int id)
{
    m_secondItemId = id;
}

int QmlGraphEdge::secondItemId() const
{
    return m_secondItemId;
}

void QmlGraphEdge::setFirstPoint(QPointF firstPoint)
{
    m_firstPoint = firstPoint;
}

void QmlGraphEdge::setSecondPoint(QPointF secondPoint)
{
    m_secondPoint = secondPoint;
}

void QmlGraphEdge::paint(QPainter *painter, const QStyleOptionGraphicsItem *style, QWidget *widget)
{
    Q_UNUSED(style)
    Q_UNUSED(widget)
    painter->save();
    painter->setPen(QPen(Qt::black));
    painter->drawLine(m_firstPoint, m_secondPoint);
    painter->restore();

}
