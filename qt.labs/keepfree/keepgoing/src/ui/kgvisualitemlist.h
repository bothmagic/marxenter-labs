#ifndef KGVISUALITEMLIST_H
#define KGVISUALITEMLIST_H

#include <QGraphicsWidget>

class KGVisualItemList : public QGraphicsWidget
{
    Q_OBJECT
public:
    explicit KGVisualItemList(QObject *parent = 0);
    virtual QPainterPath shape() const;
    virtual QSizeF sizeHint(Qt::SizeHint which, const QSizeF &constraint) const;
    virtual void setGeometry(QRectF const &geometry);
signals:

public slots:

private:
    QPainterPath p_clip;

};

#endif // KGVISUALITEMLIST_H
