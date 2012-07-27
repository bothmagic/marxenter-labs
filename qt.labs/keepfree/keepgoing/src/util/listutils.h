#ifndef QLISTFILTER_H
#define QLISTFILTER_H

#include <QList>
#include <QListIterator>
#include <QGraphicsItem>

template <typename T>
T firstOccurenceByType(QList<QGraphicsItem *> srcList)
{
    QListIterator<QGraphicsItem*> it(srcList);
    while (it.hasNext())
    {
        QGraphicsItem* foundItem = it.next();
        if (qgraphicsitem_cast<T>(foundItem))
        {
            return qgraphicsitem_cast<T>(foundItem);
        }
    }
    return 0;
}

template <typename T>
QList<T> filterByType(QList<QGraphicsItem *> srcList)
{
    QList<T> resList;
    QListIterator<QGraphicsItem*> it(srcList);
    while (it.hasNext())
    {
        QGraphicsItem* foundItem = it.next();
        if (qgraphicsitem_cast<T>(foundItem))
        {
            resList.append(qgraphicsitem_cast<T>(foundItem));
        }
    }
    return resList;
}

#endif // QLISTFILTER_H
