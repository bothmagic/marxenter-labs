#include "kgvisualitemlist.h"
#include <QGraphicsLinearLayout>
#include <QGraphicsPixmapItem>
#include "kgpixmapitem.h"
#include <QDebug>
#include "src/util/kgressourceloader.h"

KGVisualItemList::KGVisualItemList(QObject *parent) :
        QGraphicsWidget(0, Qt::Widget)
{

    QGraphicsLinearLayout *mlayout = new QGraphicsLinearLayout(Qt::Horizontal);
    KGRessourceLoader *resl = KGRessourceLoader::instance();
    KGPixmapItem *note = new KGPixmapItem(resl->pixmap(ICON_NOTE));
    KGPixmapItem *i1 = new KGPixmapItem(resl->pixmap(ICON_CALENDAR));

    mlayout->addItem(note);
    mlayout->addItem(i1);
    setLayout(mlayout);

    setFlags(QGraphicsItem::ItemClipsChildrenToShape);
    setGeometry(layout()->geometry());
}

QPainterPath KGVisualItemList::shape() const
{
    return p_clip;
}


QSizeF KGVisualItemList::sizeHint(Qt::SizeHint which, const QSizeF &constraint) const
{
    return layout()->effectiveSizeHint(which, constraint);
}

void KGVisualItemList::setGeometry(QRectF const &geometry)
{
    QGraphicsWidget::setGeometry(geometry);
    QPainterPath sh;
    sh.addRect(QRectF(QPointF(0,0), geometry.size()));
    p_clip = sh;
}
