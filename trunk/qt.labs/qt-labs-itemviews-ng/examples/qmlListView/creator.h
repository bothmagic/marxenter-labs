#ifndef _CREATOR_H
#define _CREATOR_H

#include <qgraphicslistview.h>

class QmlEngine;
class QmlComponent;
class QmlContext;
class Context;

class Creator : public QObject, public QtGraphicsListViewItemCreatorBase
{
    Q_OBJECT
public:
    Creator(QmlEngine *engine, QmlComponent *component);
    ~Creator();

    // creator api
    QGraphicsObject *create(int index, QtGraphicsListView *view);
    QGraphicsObject *reassign(int index, QGraphicsObject *item, QtGraphicsListView *view);
    void update(int index, QGraphicsObject *item, const QList<int> &roles);
    void recycle(QGraphicsObject *item, QtGraphicsListView *view);

private:
    void setContextProperties(int index, QmlContext *context, QtListModelInterface *model) const;
    QmlEngine *m_engine;
    QmlComponent *m_component;
    QHash<QGraphicsObject*, Context*> m_contexts;
};

#endif // _CREATOR_H
