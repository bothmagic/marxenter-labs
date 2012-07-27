#ifndef KGPIXMAPITEM_H
#define KGPIXMAPITEM_H

#include <QGraphicsWidget>

class KGPixmapItem : public QGraphicsWidget
{
    Q_OBJECT
public:
    explicit KGPixmapItem(QPixmap const &spixmap, QGraphicsItem *parent = 0);
    virtual QSizeF sizeHint(Qt::SizeHint which, const QSizeF &constraint) const;
    void setGeometry(const QRectF &rect);
signals:
    void hoverEnter();
    void hoverLeave();
public slots:

protected:

    void hoverEnterEvent(QGraphicsSceneHoverEvent *event);
    void hoverLeaveEvent(QGraphicsSceneHoverEvent *event);

};

#endif // KGPIXMAPITEM_H
