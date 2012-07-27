#ifndef KGDETAILFLOWITEM_H
#define KGDETAILFLOWITEM_H

#include <QGraphicsWidget>

class KGVisualTextItem;
class KGDetailFlowItemPrivate;
class QGraphicsListText;
class KGDetailFlowItem : public QGraphicsWidget
{
    Q_OBJECT

public:
    explicit KGDetailFlowItem(QGraphicsItem *parent = 0);

    virtual QRectF boundingRect() const;
    virtual QSizeF sizeHint(Qt::SizeHint which, const QSizeF &constraint=QSizeF()) const;
    virtual QPainterPath shape() const;
    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);
    bool collidesWithPath(const QPainterPath &path, Qt::ItemSelectionMode mode) const;

    QString document();
    void setDocument(QString const& document);

    KGVisualTextItem* detail();
signals:

    void detailChanged(QString name);
public slots:

protected:
    KGDetailFlowItemPrivate * const d_ptr;

private:
    KGVisualTextItem *p_detail;

    Q_DECLARE_PRIVATE(KGDetailFlowItem)
private slots:
    void p_updateGeometry();
    void p_detailChanged(QString name);


};

#endif // KGDETAILFLOWITEM_H
