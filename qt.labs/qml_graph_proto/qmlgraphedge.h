#ifndef QMLGRAPHEDGE_H
#define QMLGRAPHEDGE_H

#include <QDeclarativeItem>

class QmlGraphEdge : public QDeclarativeItem
{
    Q_OBJECT
public:
    explicit QmlGraphEdge(QDeclarativeItem *parent = 0);

    void setFirstItemId(int id);
    int firstItemId() const;
    void setSecondItemId(int id);
    int secondItemId() const;

    void setFirstPoint(QPointF firstPoint);
    void setSecondPoint(QPointF secondPoint);

    void paint(QPainter *painter, const QStyleOptionGraphicsItem *style, QWidget *widget);

signals:
    
public slots:

private:
    int m_firstItemId;
    int m_secondItemId;
    
    QPointF m_firstPoint;
    QPointF m_secondPoint;
};

#endif // QMLGRAPHEDGE_H
