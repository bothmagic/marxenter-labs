#ifndef KVISUALITEM_H
#define KVISUALITEM_H

#include <QGraphicsWidget>
#include <QModelIndex>


class KVisualItem : public QGraphicsWidget
{
    Q_OBJECT
public:
    explicit KVisualItem(QGraphicsItem *parent, Qt::WindowFlags wFlags);

    void setVisualIndex(QModelIndex *visualIndex);
    QModelIndex *visualIndex();

signals:

public slots:


private:
    // stores visual parameter of item
    QModelIndex *p_visualIndex;

    void p_init();
    void p_restoreVisualProperties();

};
#endif // KVISUALITEM_H
