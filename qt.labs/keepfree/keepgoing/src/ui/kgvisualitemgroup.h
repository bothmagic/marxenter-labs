#ifndef KGVISUALITEMGROUP_H
#define KGVISUALITEMGROUP_H

#include <QGraphicsWidget>
class KGVisualItem;
class KGVisualAppendItem;
class QGraphicsProxyWidget;

class KGVisualItemGroup : public QGraphicsWidget
{
    Q_OBJECT
public:
    explicit KGVisualItemGroup(QGraphicsWidget *parent = 0);

    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);


    QRectF boundingRect();

    void addVisualItem(KGVisualItem *visualItem, int index = -1);
    void removeVisualItem(KGVisualItem *visualItem);

    /* properties */
    void setName(QString const &groupName) { m_name = groupName; update();}
    QString name() const {return m_name;}

    QList<KGVisualItem *> allVisualItems() const {return m_visualItemList;}

    void filter(QList<int> filterList);

signals:
    void visualItemAdded(KGVisualItemGroup *group, KGVisualItem *visualItem);
    void groupStickyStateChanged(KGVisualItemGroup *group);
public slots:
protected:
    void hoverMoveEvent(QGraphicsSceneHoverEvent *event);
    void hoverLeaveEvent(QGraphicsSceneHoverEvent *event);
    bool sceneEvent(QEvent *event);
    void dragMoveEvent(QGraphicsSceneDragDropEvent *event);
    QSizeF sizeHint(Qt::SizeHint which, const QSizeF &constraint = QSizeF(0,0)) const;
private:
    KGVisualAppendItem *p_visualInsertItem;
    QString m_name;
    QList<KGVisualItem *> m_visualItemList;
    QList<int> m_filterMap;
    int m_isFiltered;

    KGVisualItem *prepareVisualItem(KGVisualItem *visualItem);
    QGraphicsProxyWidget *createButton();

private slots:
    void stickyStateChanged();

};

#endif // KGVISUALITEMGROUP_H
