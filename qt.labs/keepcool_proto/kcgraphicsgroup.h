#ifndef KCGRAPHICSGROUP_H
#define KCGRAPHICSGROUP_H
#include <QGraphicsObject>
#include <QGraphicsScene>

#define SMALLSIZE QSizeF(20,20)

class KCGraphicsGroup: public QGraphicsObject
{
    Q_OBJECT
    Q_PROPERTY(qreal radius READ radius WRITE setRadius)
public:
    KCGraphicsGroup();

    QRectF boundingRect() const;

    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);

    void setRadius(qreal r) {
        m_radius = r;
        qreal d = r+r;
        m_currentSize = QSizeF(d,d);
        update();
    }

    qreal radius() const {
        return m_radius;
    }

    void settAtachedWidget(QGraphicsWidget *attachedWidget);
    QGraphicsWidget* attachedWidget();


protected:
    void mouseReleaseEvent(QGraphicsSceneMouseEvent *event);

private:
    QSizeF m_currentSize;
    qreal m_radius;
    QGraphicsWidget *m_attachedWidget;
};

#endif // KCGRAPHICSGROUP_H
