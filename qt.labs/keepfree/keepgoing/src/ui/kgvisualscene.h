#ifndef KGVISUALSCENE_H
#define KGVISUALSCENE_H

#include <QGraphicsScene>

class QAbstractItemModel;
class KGVisualItemGroup;
class KGVisualItem;

class KGVisualScene : public QGraphicsScene
{
    Q_OBJECT
public:
    explicit KGVisualScene(QObject *parent = 0);
    void setModel(QAbstractItemModel *newModel);
    QAbstractItemModel* model() {return p_model;}

signals:

public slots:

protected:
    void mouseReleaseEvent(QGraphicsSceneMouseEvent *event);
    bool eventFilter(QObject *watched, QEvent *event);

private:
    QAbstractItemModel *p_model;

};

#endif // KGVISUALSCENE_H
