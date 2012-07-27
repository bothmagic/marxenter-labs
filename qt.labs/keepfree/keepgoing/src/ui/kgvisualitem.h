#ifndef KGVISUALITEM_H
#define KGVISUALITEM_H

#include "ui/kvisualitem.h"


class QGraphicsSceneMouseEvent;
class KGDetailFlowItem;
class QGraphicsListText;
class ContextModel;
class QCheckBox;
class QGraphicsProxyWidget;

class KGVisualItem : public KVisualItem
{
    Q_OBJECT
    Q_PROPERTY(QPointF pos READ pos WRITE setPos);
    Q_PROPERTY(QString text READ detail WRITE setDetail)
    Q_PROPERTY(QStringList context READ context WRITE setContext)
    Q_PROPERTY(int workstate READ workState WRITE setWorkState)
public:
    enum State {
        New = 0,
        Loaded = 1,
        Removed = 2
    };

    explicit KGVisualItem(QGraphicsItem *parent = 0, Qt::WindowFlags wFlags = 0);

    void setFocus(Qt::FocusReason focusReason = Qt::OtherFocusReason);

    // state set and get operation
    State state() const {return p_state;}
    void setState(State const state) {p_state = state;}

    // attribute setter and getter
    void setDetail(QString detail);
    QString detail() const;

    void setContext(QStringList context);
    QStringList context() const;

    void setWorkState(int workState);
    int workState() const;
    void setContextModel(ContextModel *contextModel);

signals:
    void itemUpdated(KGVisualItem *item, QString name);
    void startDrag(KGVisualItem *item, QGraphicsSceneMouseEvent *event);
public slots:

protected:
    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);
    QSizeF sizeHint(Qt::SizeHint which, const QSizeF &constraint) const;
    void mouseMoveEvent(QGraphicsSceneMouseEvent *event);

private:
    qreal p_headerHeight;

    KGDetailFlowItem *p_detail;
    QGraphicsListText *p_context;
    QCheckBox *m_cbComplete;
    int p_itemId;
    int p_vitemId;

    // show the state of item, New the item in new created by the user, loaded the item is
    // loaded by the application
    State p_state;

    QGraphicsProxyWidget* createCheckbox();

private slots:
    void layoutChanged();
    void contentChanged(QString objectName);
    void workStateChanged(int newState);
};

#endif // KGVISUALITEM_H
