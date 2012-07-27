#ifndef TESTITEM_H
#define TESTITEM_H

#include <QGraphicsItem>

class testitem : public QGraphicsItem
{

public:
    explicit testitem(QObject *parent = 0);
    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);
    virtual bool contains(const QPointF &point) const;
    virtual QRectF boundingRect() const;
    virtual QPainterPath shape() const;
    virtual bool collidesWithPath(const QPainterPath &path, Qt::ItemSelectionMode mode) const;
signals:

public slots:

};

#endif // TESTITEM_H
