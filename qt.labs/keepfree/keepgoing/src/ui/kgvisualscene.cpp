#include "kgvisualscene.h"
#include <QEvent>
#include <QDebug>
KGVisualScene::KGVisualScene(QObject *parent) :
    QGraphicsScene(parent)
{
    //installEventFilter(this);
}

//
// public
//
void KGVisualScene::setModel(QAbstractItemModel *newModel)
{
    p_model = newModel;
}

void KGVisualScene::mouseReleaseEvent(QGraphicsSceneMouseEvent *event)
{
    QGraphicsScene::mouseReleaseEvent(event);
}

bool KGVisualScene::eventFilter(QObject *watched, QEvent *event)
{

    //qDebug() << watched << ", " << event;

    return QGraphicsScene::eventFilter(watched, event);
}

