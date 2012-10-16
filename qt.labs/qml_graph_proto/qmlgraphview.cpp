#include "qmlgraphview.h"
#include <QPainter>
#include <QDebug>
#include <QApplication>
QmlGraphView::QmlGraphView(QDeclarativeItem *parent) :
    QDeclarativeItem(parent), m_start(0), m_end(0)
{
    setFlag(QGraphicsItem::ItemHasNoContents, false);
    setFiltersChildEvents(false);
}

void QmlGraphView::paint(QPainter *p, const QStyleOptionGraphicsItem *, QWidget *)
{

    if (!m_end|| !m_start)
        return;

    p->save();
    p->setPen(QPen(Qt::black));

    QRectF startR = m_start->boundingRect().translated(m_start->x(), m_start->y());
    QRectF endR = m_end->boundingRect().translated(m_end->x(), m_end->y());

    p->drawLine(startR.center(), endR.center());


    p->restore();

}

void QmlGraphView::registerItem(QDeclarativeItem *start)
{
    connect(start, SIGNAL(xChanged()), this, SLOT(upaint()));
    connect(start, SIGNAL(yChanged()), this, SLOT(upaint()));
}

void QmlGraphView::setStart(QDeclarativeItem *start)
{
    qDebug() << "start " << start;
    m_start = start;
    //start->installSceneEventFilter(this);
    registerItem(start);
}

QDeclarativeItem *QmlGraphView::start() const
{
    return m_start;
}

void QmlGraphView::setEnd(QDeclarativeItem *end)
{
    qDebug() << "end " << end;
    m_end = end;
    registerItem(end);
}

QDeclarativeItem *QmlGraphView::end() const
{
    return m_end;
}

void QmlGraphView::upaint()
{
    update();
}

bool QmlGraphView::sceneEventFilter(QGraphicsItem *watched, QEvent *event)
{
    qDebug() << "filter " << event;
}

bool QmlGraphView::eventFilter(QObject *item, QEvent *event)
{
    qDebug() << "eventfilter " << event;
    QApplication::postEvent(item, event);

    return true;
}


