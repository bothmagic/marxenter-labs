#include "kgpixmapitem.h"
#include <QGraphicsPixmapItem>

KGPixmapItem::KGPixmapItem(QPixmap const &pixmap, QGraphicsItem *parent) :
        QGraphicsWidget(parent, Qt::Widget)
{
    setGraphicsItem(new QGraphicsPixmapItem(pixmap));

}

QSizeF KGPixmapItem::sizeHint(Qt::SizeHint which, const QSizeF &constraint) const
{
    return graphicsItem()->boundingRect().size();
}

void KGPixmapItem::setGeometry(const QRectF &rect)
{
    QGraphicsWidget::setGeometry(rect);
    graphicsItem()->setPos(rect.topLeft());
}

void KGPixmapItem::hoverEnterEvent(QGraphicsSceneHoverEvent *event)
{
    emit hoverEnter();
}

void KGPixmapItem::hoverLeaveEvent(QGraphicsSceneHoverEvent *event)
{
    emit hoverLeave();
}
