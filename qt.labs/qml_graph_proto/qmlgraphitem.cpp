#include "qmlgraphitem.h"
#include <QDebug>

QmlGraphItem::QmlGraphItem(QDeclarativeItem *parent) :
    QDeclarativeItem(parent)
{
    setFlag(QGraphicsItem::ItemSendsGeometryChanges, true);
}

void QmlGraphItem::setGraphItemId(int itemId)
{
    m_graphItemId = itemId;
}

int QmlGraphItem::graphItemId() const
{
    return m_graphItemId;
}

QVariant QmlGraphItem::itemChange(QGraphicsItem::GraphicsItemChange change, const QVariant &value)
{
    //qDebug() << Q_FUNC_INFO;
    emit itemChanged(m_graphItemId);
    return value;
}


