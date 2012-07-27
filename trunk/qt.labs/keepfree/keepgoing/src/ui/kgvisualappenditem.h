#ifndef KGVISUALAPPENDITEM_H
#define KGVISUALAPPENDITEM_H

#include <QGraphicsWidget>

class KGVisualAppendItem : public QGraphicsWidget
{
    Q_OBJECT

public:
    enum KGVisualAppendType {
        APPEND, INSERT
    };

    explicit KGVisualAppendItem(KGVisualAppendType visualType, QGraphicsWidget *parent = 0);
    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);

    KGVisualAppendType visualType() const {return p_visualType;}

    void setInsertIndex(int index);
    int insertIndex();

signals:

public slots:

protected:
    void mouseReleaseEvent(QGraphicsSceneMouseEvent *event);
    void hoverLeaveEvent(QGraphicsSceneHoverEvent *event);

private:
    KGVisualAppendType p_visualType;
    int p_insertIndex;


};

#endif // KGVISUALAPPENDITEM_H
