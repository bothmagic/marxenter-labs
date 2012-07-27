#include "kgflowpane.h"
#include <QPainter>
#include <QPropertyAnimation>
#include <QDebug>
#include <QGraphicsWidget>
#include <QGraphicsScene>
#include "src/util/kgressourceloader.h"

KGFlowPane::KGFlowPane(QObject *parent) :
    QGraphicsItem(),
    p_flowButton(KGRessourceLoader::instance()->pixmap(BUTTON_FLOWPANEL))
{
    p_state = Closed;
    p_animation = new QPropertyAnimation(this, "size");
    p_animation->setDuration(300);
    p_animation->setEasingCurve(QEasingCurve::InOutExpo);
    connect(p_animation, SIGNAL(finished()), this, SLOT(p_animFinished()));
    setAcceptHoverEvents(true);
    //setFlags(QGraphicsItem::ItemClipsChildrenToShape);

}

void KGFlowPane::setOwner(QGraphicsItem *owner, Qt::AnchorPoint anchor)
{
    p_owner = owner;
    p_size = owner->boundingRect().size();

    if (anchor == Qt::AnchorTop || anchor == Qt::AnchorBottom)
    {
        p_size.setHeight(20);
    } else  // set start size = button width and owner height
    {
        p_size.setWidth(20);
    }
    p_animation->setStartValue(p_size);
    p_anchor = anchor;

}

void KGFlowPane::setPane(QGraphicsWidget *pane)
{
    p_pane = pane;
    //scene()->addItem(p_pane);
    p_pane->setParentItem(this);
    //p_pane->setFlags(QGraphicsItem::ItemStacksBehindParent);

    QRectF const paneBound = pane->geometry();
    //qDebug() << "pane maximum size is" << paneBound.size();
    QSizeF animEndSize = paneBound.size();
    QPointF panePos;
    if (p_anchor == Qt::AnchorTop)
    {
        panePos = boundingRect().bottomLeft();
        //p_pane->setPos(mapToScene(panePos));
        animEndSize.setWidth(size().width());
        animEndSize.setHeight(paneBound.height()+20);

    } else if (p_anchor == Qt::AnchorBottom)
    {
        panePos = boundingRect().topLeft()-QPointF(paneBound.height(),0);
        animEndSize.setWidth(size().width());
    } else if (p_anchor == Qt::AnchorRight)
    {
        panePos = boundingRect().topLeft()-QPointF(paneBound.width(),0);
        //p_pane->setPos(mapToScene(panePos));
        animEndSize.setHeight(size().height());

    } else
    {

    }
    p_animation->setEndValue(animEndSize);
    //p_pane->setPos(panePos);
    //p_hidePane();
    p_updateInternalBounds();
}


void KGFlowPane::setSize(const QSizeF &size)
{
    p_size = size;
    QRectF const paneBound = p_pane->geometry();
    if (p_anchor == Qt::AnchorTop)
    {
        p_pane->setGeometry(QRectF(boundingRect().topLeft()+QPoint(5, 10), size-QSizeF(0,20)));

    } else
    {
        QPointF const panePos = boundingRect().topLeft()-QPointF(paneBound.width(),0);
        //p_pane->setPos(mapToScene(panePos));
        p_pane->setPos(panePos);
    }
    //p_pane->setPos(mapToScene(panePos));
   p_pane->setParentItem(this);
   p_updateInternalBounds();

   prepareGeometryChange();
}

QRectF KGFlowPane::boundingRect() const
{
    QRectF const owBounds = p_owner->boundingRect();
    const QPointF owCenter = owBounds.center();
    // todo set pos when owner is set
    if (p_anchor == Qt::AnchorTop)
        return QRectF(owCenter.x() - p_size.width()/2, owBounds.top()-p_size.height()+20/2, p_size.width(), p_size.height());
    else
        return QRectF(owBounds.right() - (20/2), owCenter.y() - p_size.height()/2, p_size.width(), p_size.height());
}

bool KGFlowPane::collidesWithPath(const QPainterPath &path, Qt::ItemSelectionMode mode) const
{
    const QRectF bounds = boundingRect();
    if (p_state == Closed)
    {
        const QPointF center = bounds.center();
        if (p_anchor == Qt::AnchorTop)
        {
            QRectF const buttonBounds = QRectF(center.x()-50/2, bounds.top(), 50, 20);
            return buttonBounds.intersects(path.boundingRect());
        } else
        {
            QRectF const buttonBounds = QRectF(bounds.x(), center.y()-50/2, 20, 50);
            return buttonBounds.intersects(path.boundingRect());
        }
    } else
    {
        return bounds.intersects(path.boundingRect());
    }
}

void KGFlowPane::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{

    // male in der Mitte ein viereck


    if ((p_anchor == Qt::AnchorTop && p_size.height() > 20)
        || (p_anchor == Qt::AnchorRight && p_size.width() > 20))
    {
        painter->setClipRect(QRectF(p_clipTopLeft, p_clipBottomRight));
        painter->setPen(QPen(QColor(Qt::black), 2));
        if (p_anchor == Qt::AnchorTop)
        {
            QLinearGradient bgGrad(p_paintTopLeft, p_paintTopLeft+QPointF(0, p_size.height()));
            bgGrad.setColorAt(0, QColor(50, 50, 50));
            bgGrad.setColorAt(1, QColor(30, 30, 30));
            painter->setBrush(QBrush(bgGrad));
        } else if (p_anchor == Qt::AnchorRight)
        {
            QLinearGradient bgGrad(p_paintTopLeft, p_paintTopLeft+QPointF(p_size.width(),0));
            bgGrad.setColorAt(0, QColor(30, 30, 30));
            bgGrad.setColorAt(1, QColor(50, 50, 50));
            painter->setBrush(QBrush(bgGrad));
        }
        painter->drawRoundedRect(QRectF(p_paintTopLeft, p_paintBottomRight), 5, 5, Qt::AbsoluteSize);
        painter->setClipping(false);
    }
    //painter->setPen(QPen(QColor(Qt::black), 1));
    //painter->setBrush(QBrush(QColor(Qt::gray)));
    painter->drawPixmap(p_buttonBounds.topLeft()+QPointF(2,6), p_flowButton);


}

void KGFlowPane::show()
{
    if (p_state == Closed)
    {
        p_state = Expanded;
        p_animation->setDirection(QAbstractAnimation::Forward);
        p_animation->start();
        p_showPane();
    }

}

void KGFlowPane::hide()
{
    if (p_state == Expanded)
    {
        p_state = Closed;
        if (p_animation->state() == QAbstractAnimation::Running)
            p_animation->pause();
        p_animation->setDirection(QAbstractAnimation::Backward);
        p_animation->start();
    }
}

void KGFlowPane::hoverEnterEvent(QGraphicsSceneHoverEvent *event)
{
    show();
}

void KGFlowPane::hoverLeaveEvent(QGraphicsSceneHoverEvent *event)
{
    hide();
}

void KGFlowPane::p_animFinished()
{
    if (p_animation->direction() == QAbstractAnimation::Backward)
    {
        p_hidePane();
    }
}

void KGFlowPane::p_updateInternalBounds()
{
    QRectF const bounds = boundingRect();
    QPointF const center = bounds.center();

    if (p_anchor == Qt::AnchorTop)
    {
        p_buttonBounds = QRectF(center.x()-50/2, bounds.top(), 50, 20);
        p_paintTopLeft = bounds.topLeft()+QPointF(1,0);
        p_paintTopLeft.setY(p_buttonBounds.center().y()+1);
        p_paintBottomRight = bounds.bottomRight()-QPointF(1,0);
        p_clipTopLeft = bounds.topLeft();
        p_clipBottomRight = p_owner->boundingRect().topRight();
    } else
    {
        p_buttonBounds = QRectF(bounds.right()-20, center.y()-50/2, 20, 50);
        p_paintTopLeft = bounds.topLeft()+QPoint(0,1);
        p_paintBottomRight = bounds.bottomRight()-QPointF(0,1);
        p_paintBottomRight.setX(p_buttonBounds.center().x());
        p_clipTopLeft = p_owner->boundingRect().topRight();
        p_clipBottomRight = bounds.bottomRight();
    }
}

void KGFlowPane::p_hidePane()
{
    //p_pane->hide();
    p_pane->setEnabled(false);
    scene()->removeItem(p_pane);
}

void KGFlowPane::p_showPane()
{
    p_pane->setEnabled(true);

}
