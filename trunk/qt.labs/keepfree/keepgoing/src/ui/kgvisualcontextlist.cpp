#include "kgvisualcontextlist.h"
#include <QGraphicsAnchorLayout>
#include <QGraphicsAnchor>
#include "qgraphicslisttext.h"
#include "ui/qgraphicscompleter.h"
#include <QListView>
#include "model/contextmodel.h"

KGVisualContextList::KGVisualContextList(QGraphicsWidget *parent) :
    QGraphicsWidget(parent)
{

    QGraphicsAnchorLayout *layout = new QGraphicsAnchorLayout;

    QGraphicsListText *contextList = new QGraphicsListText(this);
    contextList->setFlag(QGraphicsItem::ItemIgnoresTransformations, true);

    ContextModel *contextModel = ContextModel::instance();
    QGraphicsCompleter *p_completer = new QGraphicsCompleter(contextModel);

    p_completer->setFilterKeyColumn(contextModel->fieldIndex("shortdescr"));

    QListView *l = new QListView;
    p_completer->setGraphicsItem(contextList);
    p_completer->setPopup(l);
    l->setModelColumn(contextModel->fieldIndex("shortdescr"));
    contextList->setCompleter(p_completer);

    layout->addAnchor(layout, Qt::AnchorLeft, contextList, Qt::AnchorLeft);
    layout->addAnchor(layout, Qt::AnchorRight, contextList, Qt::AnchorRight);
    setLayout(layout);
}
