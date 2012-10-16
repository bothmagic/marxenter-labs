#include "edgelayer.h"
#include <QPainter>
#include <QDebug>
#include <QApplication>
EdgeLayer::EdgeLayer(QDeclarativeItem *parent) :
    QDeclarativeItem(parent), m_start(0), m_end(0)
{
    setFlag(QGraphicsItem::ItemHasNoContents, false);
    setFiltersChildEvents(false);
}

void EdgeLayer::paint(QPainter *p, const QStyleOptionGraphicsItem *, QWidget *)
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

void EdgeLayer::registerItem(QDeclarativeItem *start)
{
    connect(start, SIGNAL(xChanged()), this, SLOT(upaint()));
    connect(start, SIGNAL(yChanged()), this, SLOT(upaint()));
}

void EdgeLayer::setStart(QDeclarativeItem *start)
{
    qDebug() << "start " << start;
    m_start = start;
    //start->installSceneEventFilter(this);
    registerItem(start);
}

QDeclarativeItem *EdgeLayer::start() const
{
    return m_start;
}

void EdgeLayer::setEnd(QDeclarativeItem *end)
{
    qDebug() << "end " << end;
    m_end = end;
    registerItem(end);
}

QDeclarativeItem *EdgeLayer::end() const
{
    return m_end;
}

void EdgeLayer::upaint()
{
    update();
}

bool EdgeLayer::sceneEventFilter(QGraphicsItem *watched, QEvent *event)
{
    qDebug() << "filter " << event;
}

bool EdgeLayer::eventFilter(QObject *item, QEvent *event)
{
    qDebug() << "eventfilter " << event;
    QApplication::postEvent(item, event);

    return true;
}


