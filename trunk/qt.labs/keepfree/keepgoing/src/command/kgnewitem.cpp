#include "kgnewitem.h"
#include "src/model/kgvisualitemfactory.h"
#include "src/ui/kgvisualitem.h"

KGNewItem::KGNewItem(KGVisualItemFactory *itemFactory,  QGraphicsWidget const *parentGroup, int const index)
    : m_index(index), m_parentGroup(parentGroup)
{
    m_itemFactory = itemFactory;
}

void KGNewItem::undo()
{

}

void KGNewItem::redo()
{
    //m_item = m_itemFactory->createItem(m_parentGroup, m_index);

}
