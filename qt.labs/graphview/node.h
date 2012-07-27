#ifndef NODE_H
#define NODE_H

#include <QGraphicsItem>

class Node : public QGraphicsItem
{
public:
    Node();

    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);
};

#endif // NODE_H
