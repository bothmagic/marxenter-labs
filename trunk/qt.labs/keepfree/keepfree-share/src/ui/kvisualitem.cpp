#include "kvisualitem.h"
#include <QModelIndex>

KVisualItem::KVisualItem(QGraphicsItem *parent, Qt::WindowFlags wFlags) :
    QGraphicsWidget(parent, wFlags)
{
    p_init();
}

//
// private
//
void KVisualItem::p_init()
{
    p_visualIndex = 0;
}

void KVisualItem::p_restoreVisualProperties()
{
    Q_ASSERT(p_visualIndex);


}

//
// public
//

void KVisualItem::setVisualIndex(QModelIndex *visualIndex)
{
    if (this->p_visualIndex)
    {
        this->p_visualIndex = 0;
    }
    if (visualIndex)
    {
        this->p_visualIndex = visualIndex;
        p_restoreVisualProperties();
    }

}

QModelIndex* KVisualItem::visualIndex()
{
    return this->p_visualIndex;
}
