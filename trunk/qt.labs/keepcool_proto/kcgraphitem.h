#ifndef KCGRAPHITEM_H
#define KCGRAPHITEM_H

#include <QObject>
#include <QGraphicsItem>
#include <QGraphicsLayoutItem>

class KCGraphItem : public QGraphicsItem,
        public QGraphicsLayoutItem
{
public:
    explicit KCGraphItem(QObject *parent = 0);



signals:
    
public slots:
    
};

#endif // KCGRAPHITEM_H
