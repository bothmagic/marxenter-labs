#ifndef KGNEWITEM_H
#define KGNEWITEM_H

#include <QUndoCommand>

class KGVisualItemFactory;
class KGVisualItem;
class QGraphicsWidget;

class KGNewItem: public QUndoCommand
{
public:
    KGNewItem(KGVisualItemFactory *itemFactory, QGraphicsWidget const *parentGroup, int const index);
    virtual void undo();
    virtual void redo();

private:
    KGVisualItemFactory *m_itemFactory;
    KGVisualItem *m_item;
    int const m_index;
    QGraphicsWidget const *m_parentGroup;
};

#endif // KGNEWITEM_H
