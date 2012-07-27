#include "kgdetailflowitem.h"
#include <QGraphicsGridLayout>
#include "kgvisualtextitem.h"
#include <QPainter>
#include <QDebug>
#include <QGraphicsTextItem>
#include "qgraphicslisttext.h"

class KGDetailFlowItemPrivate
{
public:

    KGDetailFlowItemPrivate(QGraphicsItem *parent):
            containerWidget(new QGraphicsWidget(parent))
    {
        containerWidget->setFlags(QGraphicsItem::ItemHasNoContents);
        //containerWidget->setFlag(QGraphicsItem::ItemClipsChildrenToShape);
    }

    // widget that clips the real widget
    QGraphicsWidget * const containerWidget;

};


KGDetailFlowItem::KGDetailFlowItem(QGraphicsItem *parent) :
        QGraphicsWidget(parent, Qt::Widget),
        d_ptr(new KGDetailFlowItemPrivate(this))
{
    Q_D(KGDetailFlowItem);

    p_detail = new KGVisualTextItem("");
    p_detail->setObjectName("document");
    QGraphicsGridLayout *layout = new QGraphicsGridLayout(this);
    layout->setContentsMargins(0,0,0,0);
    setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
    layout->addItem(d->containerWidget, 0, 0);
    p_detail->setGeometry(QRectF(0,0,220,100));
    d->containerWidget->setMinimumHeight(95);
    d->containerWidget->setMaximumHeight(95);
    p_detail->setParentItem(d->containerWidget);
    layout->setColumnSpacing(0, 0);


    layout->setRowSpacing(0, 0);

    //resize(sizeHint(Qt::PreferredSize));

    connect(p_detail, SIGNAL(geometryChanged()), this, SLOT(p_updateGeometry()));
    connect(p_detail, SIGNAL(documentChanged(QString)), this, SLOT(p_detailChanged(QString)));
}


QRectF KGDetailFlowItem::boundingRect() const
{
    return QRectF(pos(), sizeHint(Qt::PreferredSize));
}

QPainterPath KGDetailFlowItem::shape() const
{
    QPainterPath sh;
    sh.addRect(QRectF(QPointF(-1,-1), sizeHint(Qt::PreferredSize)));
    return sh;
}

QSizeF KGDetailFlowItem::sizeHint(Qt::SizeHint which, const QSizeF &constraint) const
{
    //return layout()->effectiveSizeHint(which, constraint);
    return QSizeF(220,p_detail->boundingRect().height());
}

void KGDetailFlowItem::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{
    QGraphicsWidget::paint(painter, option, widget);
    //painter->drawRect(QRectF(QPointF(0,0), sizeHint(Qt::PreferredSize)));
}

QString KGDetailFlowItem::document()
{
    return p_detail->property("document").toString();
}

void KGDetailFlowItem::setDocument(QString const& document)
{
    p_detail->setProperty("document", document);

}

bool KGDetailFlowItem::collidesWithPath(const QPainterPath &path, Qt::ItemSelectionMode mode) const
{
    return QGraphicsWidget::collidesWithPath(path, mode);
}

void KGDetailFlowItem::p_updateGeometry()
{
    resize(sizeHint(Qt::PreferredSize));
    adjustSize();
    emit geometryChanged();
}

void KGDetailFlowItem::p_detailChanged(QString name)
{
    emit detailChanged(objectName());
}

KGVisualTextItem* KGDetailFlowItem::detail()
{
    return p_detail;
}
