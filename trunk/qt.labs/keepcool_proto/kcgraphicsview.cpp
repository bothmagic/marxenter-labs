#include "kcgraphicsview.h"
#include "flowlayout.h"
#include "qgraphicslayouttext.h"
#include "kcgraphicsgroup.h"

#include <QGraphicsRectItem>
#include <QGradient>
#include <QGraphicsWidget>
#include <math.h>
#include <QDebug>

KCGraphicsView::KCGraphicsView(QWidget *parent) :
    QGraphicsView(parent)
{

    setScene(new QGraphicsScene());
    QLinearGradient linearGrad(QPointF(0, 0), QPointF(0, 1));
    linearGrad.setColorAt(0, QColor(0x0d365d));
    linearGrad.setColorAt(1, QColor(0xcdcdf9));
    linearGrad.setCoordinateMode(QGradient::StretchToDeviceMode);
    setBackgroundBrush(QBrush(linearGrad));

    //QGraphicsEllipseItem *rect = new QGraphicsEllipseItem(0,0,100,100);

    FlowLayout *f = new FlowLayout;
    f->setContentsMargins(0,0,0,0);

    KCGraphicsGroup *gr = new KCGraphicsGroup();

    //gr->setPos(-100,-100);


    scene()->addItem(gr);

    KCGraphicsGroup *gr1 = new KCGraphicsGroup();

    QSizeF s = size();

    gr1->setPos(0,0);
    gr1->setRadius(30);

    //scene()->addItem(gr1);


    //gr->setHandlesChildEvents(false);

//    scene()->addItem(new QGraphicsLayoutText("sfdasfafsd"));
//    scene()->addItem(gr);

    QGraphicsWidget *w = new QGraphicsWidget();
    //w->setGeometry(QRectF(rect->pos(), rect->boundingRect().size()));


    QGraphicsLayoutText *tt = new QGraphicsLayoutText("1");
    QFontMetrics   ttf(tt->font());


    QRectF aa = ttf.boundingRect("122333444455555666666777777788888888999999999");
    f->setSpacing(Qt::Vertical, 0);
    f->setSpacing(Qt::Horizontal, 5);
    double res = sqrt(((aa.width()+30)*(aa.height()+20))/ M_PI);
    gr->setRadius(res+10);
    res *=2;


    //QGraphicsRectItem *rect = new QGraphicsRectItem(0,0,res,res);
//    QGraphicsEllipseItem *rect = new QGraphicsEllipseItem(0,0,res+10,res+10);
//    rect->setPen(QPen(Qt::white));

    QRect resRect(0,0,res,res);

    w->setGeometry(resRect);
    gr->setPos(w->geometry().center());

    f->addItem(tt);
    f->addItem(new QGraphicsLayoutText("22"));
    f->addItem(new QGraphicsLayoutText("333"));
    f->addItem(new QGraphicsLayoutText("4444"));
    f->addItem(new QGraphicsLayoutText("55555"));
    f->addItem(new QGraphicsLayoutText("666666"));
    f->addItem(new QGraphicsLayoutText("7777777"));
    f->addItem(new QGraphicsLayoutText("88888888"));
    f->addItem(new QGraphicsLayoutText("999999999"));
    w->setPos(gr->pos()-QPoint(0,5));
    w->setLayout(f);
    f->addItem(new QGraphicsLayoutText("0", w));
    f->addItem(new QGraphicsLayoutText("213321312", w));


    gr->settAtachedWidget(w);

    //f->insertItem(0, new QGraphicsLayoutText("77"));


    scene()->addItem(w);

    //gr->setScale(1.5);

}
