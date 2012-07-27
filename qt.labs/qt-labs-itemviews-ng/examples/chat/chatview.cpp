/****************************************************************************
**
** Copyright (C) 2008-2009 Nokia Corporation and/or its subsidiary(-ies).
** Contact: Qt Software Information (qt-info@nokia.com)
**
** This file is part of the Itemviews NG project on Trolltech Labs.
**
** This file may be used under the terms of the GNU General Public
** License version 2.0 or 3.0 as published by the Free Software Foundation
** and appearing in the file LICENSE.GPL included in the packaging of
** this file.  Please review the following information to ensure GNU
** General Public Licensing requirements will be met:
** http://www.fsf.org/licensing/licenses/info/GPLv2.html and
** http://www.gnu.org/copyleft/gpl.html.
**
** If you are unsure which license is appropriate for your use, please
** contact the sales department at qt-sales@nokia.com.
**
** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
**
****************************************************************************/

#include "chatview.h"

#include <qpainter.h>
#include <qfontmetrics.h>
#include <qeasingcurve.h>
#include <qpropertyanimation.h>
#include <qstyleoption.h>
#include <qdebug.h>

#include <qlistmodelinterface.h>

ChatViewItem::ChatViewItem(int index, QtGraphicsListView *view)
 : QtGraphicsListViewItem(index, view),
    m_animation(0)
{
    m_animation = new QPropertyAnimation(this, "pos");
    m_animation->setDuration(750);
    m_animation->setEasingCurve(QEasingCurve::OutBounce);
    //setCacheMode(QGraphicsObject::ItemCoordinateCache);
}

void ChatViewItem::startAnimation(const QVariant &start, const QVariant &end)
{
    m_animation->setStartValue(start);
    m_animation->setEndValue(end);
    m_animation->start();
}

void ChatViewItem::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{
    Q_UNUSED(widget);
    const QHash<QByteArray, QVariant> hash = view()->model()->data(index(), QList<QByteArray>() << "BackgroundRole" << "ToolTipRole" << "DisplayRole");
    const int userId = hash.value("BackgroundRole").toInt();
    const QString nick = hash.value("ToolTipRole").toString();
    const QString text = hash.value("DisplayRole").toString();

    const ChatView *chatView = qobject_cast<ChatView*>(view());
    const qreal line = QFontMetrics(chatView->font()).height();
    painter->save();
    painter->setPen(Qt::gray);
    painter->drawText(QRectF(0, 0, geometry().width(), line), Qt::AlignCenter, nick);
    painter->restore();

    QRectF rect = option->rect.adjusted(0, line, 0, 0);
    qtDrawBorderPixmap(painter, rect, chatView->bubble(userId), chatView->borders()); // ### won't work if rect.y != 0;

    rect.adjust(chatView->borders().sourceLeftBorder, chatView->borders().sourceTopBorder,
                -chatView->borders().sourceRightBorder, -chatView->borders().sourceBottomBorder);

    painter->drawText(rect, Qt::AlignCenter|Qt::TextWordWrap, text);
}

QSizeF ChatViewItem::sizeHint(Qt::SizeHint which, const QSizeF &constraint) const
{
    switch (which) {
        case Qt::MinimumSize:
        case Qt::PreferredSize: {
            const ChatView *chatView = qobject_cast<ChatView*>(view());
            static const QSizeF padding(chatView->borders().sourceLeftBorder + chatView->borders().sourceRightBorder,
                                         chatView->borders().sourceTopBorder + chatView->borders().sourceBottomBorder);
            const QSizeF content = QtGraphicsListViewItem::sizeHint(which, constraint);
            const qreal height = padding.height() + content.height() + QFontMetrics(chatView->font()).height();
            return QSizeF(geometry().width(), height);
        }
        case Qt::MaximumSize:
            return QSizeF(QWIDGETSIZE_MAX, QWIDGETSIZE_MAX);
        default:
            break;
    }
    return QtGraphicsListViewItem::sizeHint(which, constraint);
}

ChatView::ChatView(QGraphicsWidget *parent, Qt::WindowFlags wFlags)
    : QtGraphicsListView(Qt::Vertical, parent, wFlags)
{
    m_borders.sourceBottomBorder = 12;
    m_borders.sourceLeftBorder = 12;
    m_borders.sourceRightBorder = 18;
    m_borders.sourceTopBorder = 12;

    m_bubbles[0] = QPixmap(":images/blue.png");
    m_bubbles[1] = QPixmap(":images/green.png");
    m_bubbles[2] = QPixmap(":images/grey.png");
    m_bubbles[3] = QPixmap(":images/orange.png");
    m_bubbles[4] = QPixmap(":images/pink.png");
    m_bubbles[5] = QPixmap(":images/purple.png");
    m_bubbles[6] = QPixmap(":images/yellow.png");
    // ### need two more

    setItemCreator(new QtGraphicsListViewItemCreator<ChatViewItem>());
}

void ChatView::initStyleOption(QStyleOptionViewItemV4 *option) const
{
    QtGraphicsListView::initStyleOption(option);
    option->features |= QStyleOptionViewItemV2::WrapText;
}

void ChatView::itemsInserted(int index, int count)
{
    doLayout(); // FIXME: to ensure the item is created
    for (int i = 0; i < count; ++i) {
        if (ChatViewItem *item = static_cast<ChatViewItem*>(itemForIndex(index + i))) {
            item->startAnimation(QPointF(0, geometry().height() + item->geometry().height()), item->pos());
        }
    }
}

void ChatView::itemsChanged(int index, int count, const QList<QByteArray> &roles)
{
    const QSizeF constraint = (orientation() == Qt::Horizontal
                                ? QSizeF(-1, size().height())
                                : QSizeF(size().width(), -1));
    for (int i = 0; i < count; ++i) {
        if (ChatViewItem *item = static_cast<ChatViewItem*>(itemForIndex(index + i))) {
            // FIXME: should be animated
            item->resize(item->effectiveSizeHint(Qt::PreferredSize, constraint));
        }
    }
    doLayout();
}

void ChatView::setModel(QtListModelInterface *model_)
{
    if (model()) {
        disconnect(model(), SIGNAL(itemsInserted(int,int)),
                   this, SLOT(itemsInserted(int,int)));
        disconnect(model(), SIGNAL(itemsChanged(int,int,const QList<QByteArray>&)),
                   this, SLOT(itemsChanged(int,int,const QList<QByteArray>&)));
    }
    QtGraphicsListView::setModel(model_);
    if (model()) {
        connect(model(), SIGNAL(itemsInserted(int,int)),
                this, SLOT(itemsInserted(int,int)));
        connect(model(), SIGNAL(itemsChanged(int,int,const QList<QByteArray>&)),
                this, SLOT(itemsChanged(int,int,const QList<QByteArray>&)));
    }
}
