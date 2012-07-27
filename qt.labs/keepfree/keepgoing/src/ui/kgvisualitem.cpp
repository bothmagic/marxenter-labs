#include "kgvisualitem.h"
#include "ui/qgraphicslayouttext.h"
//#include <QGraphicsLinearLayout>
#include <QGraphicsAnchorLayout>
#include <QGraphicsAnchor>
#include <QPainter>
#include <QWidget>
#include <QDebug>
#include <QTextDocument>
#include <QGraphicsSceneMouseEvent>
#include "../styles.h"
#include <QSqlDatabase>
#include <QSqlQuery>
#include <QSqlRecord>
#include <QCheckBox>
#include <QGraphicsProxyWidget>
#include <QListView>
#include "kgdetailflowitem.h"
#include "qgraphicslisttext.h"
#include "model/contextmodel.h"
#include "ui/qgraphicslisttext.h"
#include "kgvisualtextitem.h"

KGVisualItem::KGVisualItem(QGraphicsItem *parent, Qt::WindowFlags wFlags) :
    KVisualItem(parent, wFlags),
    p_state(New)
{
    p_itemId = 0;

    p_vitemId = 0;

    setCursor(QCursor(Qt::OpenHandCursor));

    p_detail = new KGDetailFlowItem(this);
    p_detail->setObjectName("text");
    p_context = new QGraphicsListText(this);
    p_context->setObjectName("context");
    QGraphicsProxyWidget *checkBox = createCheckbox();

    QGraphicsAnchorLayout *itemLayout = new QGraphicsAnchorLayout;
    itemLayout->setPreferredWidth(225);
    itemLayout->setContentsMargins(2,2,2,20);
    QGraphicsAnchor *anchor;

    //checkbox

    anchor = itemLayout->addAnchor(itemLayout, Qt::AnchorTop, checkBox, Qt::AnchorTop);
    anchor->setSpacing(2);
    anchor = itemLayout->addAnchor(itemLayout, Qt::AnchorRight, checkBox, Qt::AnchorRight);
    anchor->setSpacing(2);
    // textfield
    anchor = itemLayout->addAnchor(itemLayout, Qt::AnchorTop, p_detail, Qt::AnchorTop);
    anchor->setSizePolicy(QSizePolicy::Fixed);
    anchor->setSpacing(25);
    anchor = itemLayout->addAnchor(p_detail, Qt::AnchorRight, itemLayout, Qt::AnchorRight);
    anchor = itemLayout->addAnchor(p_detail, Qt::AnchorLeft, itemLayout, Qt::AnchorLeft);

    connect(p_detail, SIGNAL(geometryChanged()), this, SLOT(layoutChanged()));
    connect(p_detail, SIGNAL(detailChanged(QString)), this, SLOT(contentChanged(QString)));

    // p_context
    anchor = itemLayout->addAnchor(p_context, Qt::AnchorRight, itemLayout, Qt::AnchorRight);
    anchor = itemLayout->addAnchor(p_context, Qt::AnchorLeft, itemLayout, Qt::AnchorLeft);
    anchor = itemLayout->addAnchor(p_context, Qt::AnchorTop, p_detail, Qt::AnchorBottom);

    setLayout(itemLayout);

    connect(p_context, SIGNAL(contentChanged(QString)), this, SLOT(contentChanged(QString)));

    setFlags(QGraphicsItem::ItemIsSelectable
             | QGraphicsItem::ItemIsFocusable);

    layoutChanged();
    setMinimumHeight(155);
    setMaximumHeight(155);
    setAcceptHoverEvents(true);
}

void KGVisualItem::setDetail(QString detail)
{
    p_detail->setDocument(detail);
}

QString KGVisualItem::detail() const
{
    return p_detail->document();
}

void KGVisualItem::setContext(QStringList context)
{
    p_context->setStringList(context);
}

QStringList KGVisualItem::context() const
{
    return p_context->stringList();
}

void KGVisualItem::setWorkState(int complete)
{
    m_cbComplete->setChecked(complete);
}

int KGVisualItem::workState() const
{
    return m_cbComplete->isChecked();
}

void KGVisualItem::setContextModel(ContextModel *contextModel)
{
    QGraphicsCompleter *p_completer = new QGraphicsCompleter(contextModel);

    p_completer->setFilterKeyColumn(contextModel->fieldIndex("shortdescr"));

    QListView *l = new QListView;
    p_completer->setGraphicsItem(p_context);
    p_completer->setPopup(l);
    l->setModelColumn(contextModel->fieldIndex("shortdescr"));
    p_context->setCompleter(p_completer);

    connect(p_context, SIGNAL(insertItem(QString)), contextModel, SLOT(addNewContext(QString)));
}

void KGVisualItem::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{
    QRectF bounds = boundingRect();

    if (isSelected())
    {
        const QBrush brush(Qt::lightGray);
        //painter->setPen(style()->standardPalette().brush(QPalette::Highlight).color());
        //brush->setColor(Qt::blue);
        painter->setBrush(brush);
    }
    painter->save();
    painter->setRenderHint(QPainter::Antialiasing);
    if (opacity() < 1)
        painter->setCompositionMode(QPainter::CompositionMode_Exclusion);
    //QGraphicsWidget::paint(painter, option, widget);

    int headerHeight = 20; //p_header->boundingRect().height()+layout()->contentsRect().y();

    QLinearGradient headerGrad(QPointF(0, 0), QPointF(0, headerHeight));
    headerGrad.setColorAt(0, QColor(56, 89, 126));
    headerGrad.setColorAt(1, QColor(105, 130, 158));

    QBrush headerBrush(headerGrad);

    painter->setBrush(headerBrush);
    painter->setPen(Qt::NoPen);

    bounds.setHeight(headerHeight);
    painter->setClipRect(bounds);
    bounds.setHeight(headerHeight+5);
    painter->drawRoundedRect(bounds, ITEM_ROUNDS, ITEM_ROUNDS, Qt::AbsoluteSize);

    bounds = boundingRect();
    bounds.setTop(headerHeight);
    painter->setClipRect(bounds);
    bounds.setTop(headerHeight-5);
    QLinearGradient bgGrad(QPointF(0, 0), QPointF(0, boundingRect().height()));
    bgGrad.setColorAt(0, QColor(200, 200, 200));
    bgGrad.setColorAt(1, Qt::white);
    painter->setPen(Qt::NoPen);
    painter->setBrush(QBrush(bgGrad));
    painter->drawRoundedRect(bounds, ITEM_ROUNDS, ITEM_ROUNDS, Qt::AbsoluteSize);

    painter->setBrush(Qt::NoBrush);

    if (isSelected())
    {
        painter->setPen(QPen(QColor(Qt::white), 3));
    } else
    {
        painter->setPen(QPen(QColor(Qt::black), 3));
    }

    painter->setClipRect(boundingRect());

    painter->drawRoundedRect(boundingRect(), 5,5, Qt::AbsoluteSize);
    painter->restore();
}

void KGVisualItem::layoutChanged()
{
    layout()->invalidate();
    adjustSize();
}

void KGVisualItem::contentChanged(QString objectName)
{
    emit itemUpdated(this, objectName);
}


QSizeF KGVisualItem::sizeHint(Qt::SizeHint which, const QSizeF &constraint) const
{

    QGraphicsTextItem *textItem = static_cast<QGraphicsTextItem*>(graphicsItem());
    switch (which) {
        case Qt::MinimumSize:
                return QSizeF(0, 0);
        case Qt::PreferredSize:
                {
                    QSizeF size = layout()->effectiveSizeHint(which, constraint);

                    /*size.setHeight(
                            p_header->document()->size().height()
                            + p_detail->boundingRect().height()+22);*/
                    size.setHeight(p_detail->boundingRect().height()+47);
                    return size;
            }
        case Qt::MaximumSize:
            return QSizeF(QWIDGETSIZE_MAX, QWIDGETSIZE_MAX);
        default:
            qWarning("r::TextEdit::sizeHint(): Don't know how to handle the value of 'which'");
            break;
    }

    return constraint;

}

QGraphicsProxyWidget *KGVisualItem::createCheckbox()
{
    QGraphicsProxyWidget *w = new QGraphicsProxyWidget;
    m_cbComplete = new QCheckBox();
    m_cbComplete->setAttribute(Qt::WA_TranslucentBackground, true);
    w->setWidget(m_cbComplete);
    m_cbComplete->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Preferred);

    connect(m_cbComplete, SIGNAL(stateChanged(int)), this, SLOT(workStateChanged(int)));

    return w;
}


void KGVisualItem::workStateChanged(int newState)
{
    emit itemUpdated(this, "workstate");
}

void KGVisualItem::mouseMoveEvent(QGraphicsSceneMouseEvent *event)
{
    emit startDrag(this, event);
}

void KGVisualItem::setFocus(Qt::FocusReason focusReason)
{
    QGraphicsWidget::setFocus(focusReason);
    p_detail->detail()->setFocus(focusReason);
}




