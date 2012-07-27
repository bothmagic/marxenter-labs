#ifndef KGFLOWPANE_H
#define KGFLOWPANE_H

#include <QGraphicsItem>

class QPropertyAnimation;
class QGraphicsWidget;

class KGFlowPane : public QObject, public QGraphicsItem
{
    Q_OBJECT
    Q_INTERFACES(QGraphicsItem)
    Q_PROPERTY(QPointF pos READ pos WRITE setPos);
    Q_PROPERTY(QSizeF size READ size WRITE setSize);
public:

    enum State
    {
        Closed = 0,
        Expanded = 1
    };


    explicit KGFlowPane(QObject *parent = 0);
    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);
    QRectF boundingRect() const;
    void setOwner(QGraphicsItem *owner, Qt::AnchorPoint anchor);

    bool collidesWithPath(const QPainterPath &path, Qt::ItemSelectionMode mode) const;
    void setSize(QSizeF const &size);
    QSizeF size() {return p_size;}
    void setPane(QGraphicsWidget *pane);


signals:

public slots:
    void show();

    void hide();

protected:
    void hoverEnterEvent(QGraphicsSceneHoverEvent *event);
    void hoverLeaveEvent(QGraphicsSceneHoverEvent *event);

private:
    QGraphicsItem *p_owner;
    State p_state;
    QPropertyAnimation *p_animation;
    QSizeF p_size;
    QGraphicsWidget *p_pane;
    Qt::AnchorPoint p_anchor;
    QRectF p_buttonBounds;
    QPointF p_paintTopLeft;
    QPointF p_paintBottomRight;
    QPointF p_clipTopLeft;
    QPointF p_clipBottomRight;
    QPixmap const p_flowButton;

    void p_updateInternalBounds();
    void p_showPane();
    void p_hidePane();

private slots:
    void p_animFinished();
};

#endif // KGFLOWPANE_H
