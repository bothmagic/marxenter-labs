#include "kgcommandfactory.h"
#include "ui/kgvisualview.h"
#include "command/kgnewitem.h"
#include <QUndoStack>

KGCommandFactory* KGCommandFactory::m_Instance = 0;

KGCommandFactory::KGCommandFactory(QObject *parent) :
    QObject(parent)
{
    m_undoStack = new QUndoStack;
}

KGVisualView* KGCommandFactory::kgVisualView() const
{
    return m_kgVisualView;
}

void KGCommandFactory::setKGVisualView(KGVisualView *kgVisualView)
{
    m_kgVisualView = kgVisualView;
}

QUndoStack* KGCommandFactory::undoStack() const
{
    return m_undoStack;
}

KGNewItem* KGCommandFactory::kgNewItem(QGraphicsWidget *parentGroup, int index)
{
    //KGNewItem *command =
    //        new KGNewItem(kgVisualView()->visualItemFactory(),  parentGroup, index);

    //m_undoStack->push(command);
}
