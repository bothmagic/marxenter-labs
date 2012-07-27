#include "kgvisualappenditem.h"
#include "kgvisualview.h"
#include <QPainter>
#include <QCursor>
#include <QGraphicsScene>
#include <QGraphicsView>
#include "kgvisualitemgroup.h"
#include "kgvisualitem.h"
#include <QGraphicsSceneMouseEvent>
#include "model/kgitemmodel.h"

KGVisualAppendItem::KGVisualAppendItem(KGVisualAppendType visualType, QGraphicsWidget *parent) :
    QGraphicsWidget(parent)
{
    setCursor(QCursor(Qt::PointingHandCursor));

    setFlags(QGraphicsItem::ItemIsSelectable);

    p_visualType = visualType;
    p_insertIndex = -1;

    setAcceptHoverEvents(true);
}

void KGVisualAppendItem::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{

    painter->setPen(QPen(QColor(Qt::white), 3));
    painter->drawRoundedRect(boundingRect(), 5,5, Qt::AbsoluteSize);

}

void KGVisualAppendItem::setInsertIndex(int index)
{
    p_insertIndex = index;
}

int KGVisualAppendItem::insertIndex()
{
    return p_insertIndex;
}


void KGVisualAppendItem::mouseReleaseEvent(QGraphicsSceneMouseEvent *event)
{
    event->setAccepted(true);

    KGVisualView *view = static_cast<KGVisualView *>(scene()->views()[0]);

    int group = view->allGroups().indexOf(qgraphicsitem_cast<KGVisualItemGroup *>(parentItem()));

    static_cast<KGItemModel *>(view->model())->createItem(group, p_insertIndex);
}

void KGVisualAppendItem::hoverLeaveEvent(QGraphicsSceneHoverEvent *event)
{
    if (visualType() == INSERT)
        setVisible(false);
}
