#include "kcgraphicsgroup.h"
#include <QPainter>
#include <QDebug>
#include <QPropertyAnimation>
#include <QParallelAnimationGroup>
#include <QGraphicsWidget>
#include <QGraphicsBlurEffect>

KCGraphicsGroup::KCGraphicsGroup(): QGraphicsObject(),
    m_currentSize(SMALLSIZE)
{
    setFlags(QGraphicsItem::ItemIsFocusable| QGraphicsItem::ItemIsSelectable);

}

void KCGraphicsGroup::settAtachedWidget(QGraphicsWidget *attachedWidget) {
    m_attachedWidget = attachedWidget;
}

QGraphicsWidget* KCGraphicsGroup::attachedWidget() {
    return m_attachedWidget;
}

QRectF KCGraphicsGroup::boundingRect() const {
    return QRectF(pos()-QPointF(m_radius, m_radius), m_currentSize);
}

void KCGraphicsGroup::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget) {
   // qDebug() << Q_FUNC_INFO;
    painter->setPen(QPen(QColor(255, 255, 255, 72), 13));
    painter->drawEllipse(pos(), m_radius, m_radius);
    painter->setPen(QPen(Qt::white, 2));
    painter->drawEllipse(pos(), m_radius, m_radius);
}

void KCGraphicsGroup::mouseReleaseEvent(QGraphicsSceneMouseEvent *event) {
    qDebug() << Q_FUNC_INFO;
    QParallelAnimationGroup *pa = new QParallelAnimationGroup;

    QPropertyAnimation *p = new QPropertyAnimation(this, "radius");

    p->setEasingCurve(QEasingCurve::OutElastic);
   // p->setStartValue(m_radius);
    p->setEndValue(100);
    p->setDuration(800);

    QPropertyAnimation *pp = new QPropertyAnimation(m_attachedWidget, "opacity");
    QGraphicsBlurEffect *blurEff = new QGraphicsBlurEffect();
    blurEff->setBlurHints(QGraphicsBlurEffect::PerformanceHint);
    blurEff->setBlurRadius(2);
    m_attachedWidget->setGraphicsEffect(blurEff);
    pp->setEndValue(0.0);
    pa->addAnimation(p);
    pa->addAnimation(pp);

    pa->start();



}
