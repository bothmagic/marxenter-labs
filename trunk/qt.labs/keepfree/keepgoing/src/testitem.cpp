#include "testitem.h"
#include <QPainter>
#include <QCursor>
#include <QDebug>
#include <QGraphicsScene>

testitem::testitem(QObject *parent) :
    QGraphicsItem()
{
    setPos(100,100);
    setCursor(QCursor(Qt::IBeamCursor));
    setFlags(QGraphicsItem::ItemClipsToShape);
}

void testitem::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{
    painter->drawRect(boundingRect());
}


bool testitem::contains(const QPointF &point) const
{
    qDebug() << "contains";
    return false;
}

QRectF testitem::boundingRect() const
{
    return QRectF(pos(), QSizeF(100,100));
}

QPainterPath testitem::shape() const
{

    /*

    int i = scene()->collidingItems()
    */
    return QGraphicsItem::shape();
}

bool testitem::collidesWithPath(const QPainterPath &path, Qt::ItemSelectionMode mode) const
{

    bool res = QGraphicsItem::collidesWithPath(path, mode);
    if (res) {
        QListIterator<QGraphicsItem*> it(scene()->items());

        while (it.hasNext())
        {
            QGraphicsItem *i = it.next();
            if (i == this) break;
            if (i->shape().intersects(mapToItem(i, path)))
                return false;
        }
    }
    return true;
}
