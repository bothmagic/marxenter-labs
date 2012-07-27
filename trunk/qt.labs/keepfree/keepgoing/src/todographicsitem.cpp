#include "todographicsitem.h"
#include <QGraphicsProxyWidget>
#include <QCheckBox>
#include <QGraphicsAnchorLayout>
#include <QGraphicsAnchor>
#include <QTextEdit>
#include <QStyleOptionGraphicsItem>
#include <QPainter>
#include <QApplication>
#include <QGraphicsScene>
#include <QDebug>
#include <QGraphicsGridLayout>
#include <QGraphicsSceneResizeEvent>
#include "styles.h"

TodoGraphicsItem::TodoGraphicsItem(QWidget *view, QGraphicsItem *parent) :
        QGraphicsWidget(parent, Qt::Widget), showDetail(false), isHover(false)
        , grView(view), tfLongDescr(NULL)
{

    buildAnchorLayout();
    setAcceptsHoverEvents(true);
    setFlags(QGraphicsItem::ItemIsSelectable|QGraphicsItem::ItemIsFocusable);

}

void TodoGraphicsItem::buildAnchorLayout()
{
    // create components
    QGraphicsProxyWidget *checkboxW = createCheckbox();

    tfShortDescr = new QGraphicsLayoutText(tr("enter todo"));
    tfShortDescr->document()->setDefaultFont(QFont(DEFAULT_FONT, TL_SHORTDESCR_SIZE));
    tfContexts = new QGraphicsListText();

    QFont fcontext(DEFAULT_FONT, TL_CONTEXT_SIZE);


    tfContexts->document()->setDefaultFont(fcontext);

    tfLongDescr = new QGraphicsLayoutText(tr("enter description"));
    tfLongDescr->document()->setDefaultFont(QFont(DEFAULT_FONT, TL_LONGDESCR_SIZE));
    tfLongDescr->setMultiline(true);
    // set layout
    QGraphicsAnchorLayout *l = new QGraphicsAnchorLayout;
    l->setSpacing(0);

    QGraphicsAnchor *anchor;

    anchor = l->addAnchor(l, Qt::AnchorTop, tfShortDescr, Qt::AnchorTop);
    anchor = l->addAnchor(checkboxW, Qt::AnchorTop, l, Qt::AnchorTop);
    anchor->setSpacing(TL_SHORTDESCR_SIZE/2+5);
    anchor = l->addAnchor(l, Qt::AnchorLeft, checkboxW, Qt::AnchorLeft);
    anchor->setSpacing(20);
    anchor = l->addAnchor(l, Qt::AnchorLeft, tfShortDescr, Qt::AnchorLeft);
    anchor->setSpacing(40);
    anchor = l->addAnchor(tfShortDescr, Qt::AnchorRight, l, Qt::AnchorRight);
    anchor = l->addAnchor(tfContexts, Qt::AnchorTop, tfShortDescr, Qt::AnchorBottom);
    anchor->setSpacing(-3);
    anchor = l->addAnchor(tfContexts,Qt::AnchorLeft, l,Qt::AnchorLeft);
    anchor->setSpacing(40);
    anchor = l->addAnchor(tfContexts,Qt::AnchorRight, l,Qt::AnchorRight);

    anchor = l->addAnchor(tfLongDescr, Qt::AnchorTop, tfContexts, Qt::AnchorBottom);
    anchor = l->addAnchor(tfLongDescr,Qt::AnchorLeft, l,Qt::AnchorLeft);
    anchor->setSpacing(40);
    anchor = l->addAnchor(tfLongDescr,Qt::AnchorRight, l,Qt::AnchorRight);


    setLayout(l);

    // connect signals
    connect(tfShortDescr, SIGNAL(layoutUpdated()), this, SLOT(layoutChanged()));
    connect(tfContexts, SIGNAL(layoutUpdated()), this, SLOT(layoutChanged()));
    connect(tfLongDescr, SIGNAL(layoutUpdated()), this, SLOT(layoutChanged()));

    connect(tfShortDescr, SIGNAL(editingFinished()), this, SLOT(shortDescrChanged()));
    connect(tfLongDescr, SIGNAL(editingFinished()), this, SLOT(descriptionChanged()));
    connect(tfContexts, SIGNAL(editingFinished()), this, SLOT(contextChanged()));

}

void TodoGraphicsItem::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{
    QGraphicsWidget::paint(painter, option, widget);
    painter->setBrush(QBrush(Qt::white));

    if (isSelected())
    {
        const QBrush brush(Qt::lightGray);
        //painter->setPen(style()->standardPalette().brush(QPalette::Highlight).color());
        //brush->setColor(Qt::blue);
        painter->setBrush(brush);
    }
    if (isHover || isUnderMouse())
    {
        const QBrush brush(Qt::yellow);
        //brush->setColor(Qt::yellow);
        //painter->setBrush(brush);
    }
    painter->setRenderHint(QPainter::Antialiasing);
    QPen pen(QColor(Qt::black), 3);

    painter->setPen(pen);
    painter->drawRoundedRect(boundingRect(), 5, 5, Qt::AbsoluteSize);
}

//todo delete
bool TodoGraphicsItem::event(QEvent *event)
{
    return QGraphicsWidget::event(event);
}

//todo löschen
void TodoGraphicsItem::mouseReleaseEvent(QGraphicsSceneMouseEvent *event)
{

}

void TodoGraphicsItem::hoverEnterEvent(QGraphicsSceneHoverEvent *event)
{
    isHover = true;
    update(boundingRect());
}

void TodoGraphicsItem::hoverLeaveEvent(QGraphicsSceneHoverEvent *event)
{
    isHover = false;

    update(boundingRect());
}

//TODO löschen
QRectF TodoGraphicsItem::boundingRect() const
{

    QRectF rect = QGraphicsWidget::boundingRect();
    //rect.translate(0, 2);
    //rect.setHeight(rect.height()+9);

    return rect;
}

QSizeF TodoGraphicsItem::sizeHint(Qt::SizeHint which, const QSizeF &constraint) const
{

    QGraphicsTextItem *textItem = static_cast<QGraphicsTextItem*>(graphicsItem());
    switch (which) {
        case Qt::MinimumSize:
                return QSizeF(0, 0);
        case Qt::PreferredSize:
                {
                    QSizeF size = layout()->effectiveSizeHint(which, constraint);
                    size.setHeight(
                            tfShortDescr->document()->size().height()
                            + tfContexts->document()->size().height()+20
                            + tfLongDescr->document()->size().height());
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

void TodoGraphicsItem::edit()
{
// todo delete method
}

void TodoGraphicsItem::closeEdit()
{

}


QGraphicsProxyWidget *TodoGraphicsItem::createCheckbox()
{
    QGraphicsProxyWidget *w = new QGraphicsProxyWidget;
    cbComplete = new QCheckBox();
    cbComplete->setAttribute(Qt::WA_TranslucentBackground, true);
    w->setWidget(cbComplete);
    cbComplete->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Preferred);
    connect(this, SIGNAL(setComplete(bool)), cbComplete, SLOT(setChecked(bool)));
    connect(cbComplete, SIGNAL(stateChanged(int)), this, SLOT(completeStateChanged(int)));

    return w;
}


QGraphicsProxyWidget *TodoGraphicsItem::createShortDescr()
{
	return 0;
    //return w;
}

QGraphicsProxyWidget *TodoGraphicsItem::createLongDescr()
{
	return 0;
}

void TodoGraphicsItem::setData(const char *name, QVariant value)
{

    if (!strcmp(name, "complete"))
    {
        emit setComplete(value.toBool());
    } else if (!strcmp(name, "shortdescr"))
    {
       tfShortDescr->document()->setPlainText(value.toString());
    } else if (!strcmp(name, "longdescr"))
    {
        tfLongDescr->document()->setHtml(value.toString());
    } else if (!strcmp(name, "contexts"))
    {
        tfContexts->setStringList(value.toStringList());
    }

}

void TodoGraphicsItem::shortDescrChanged()
{
    emit fieldChanged(vrow, "shortdescr", tfShortDescr->document()->toPlainText());
}

void TodoGraphicsItem::completeStateChanged(int state)
{
    emit fieldChanged(vrow, "complete", state);
}

void TodoGraphicsItem::contextChanged()
{
    emit fieldChanged(vrow, "contexts", tfContexts->stringList());
}

void TodoGraphicsItem::descriptionChanged()
{
    emit fieldChanged(vrow, "longdescr", tfLongDescr->document()->toHtml());
}

void TodoGraphicsItem::setRow(int row)
{
    this->vrow = row;
}

int TodoGraphicsItem::row()
{
    return vrow;
}

void TodoGraphicsItem::layoutChanged()
{
    layout()->invalidate();
    //adjustSize();
}

void TodoGraphicsItem::resizeEvent(QGraphicsSceneResizeEvent *event)
{
    layout()->updateGeometry();
    tfShortDescr->updateGeometry();
    tfContexts->updateGeometry();
    tfLongDescr->updateGeometry();
}
