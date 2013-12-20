#include "qgraphicslayouttext.h"
#include <QTextDocument>
#include <QtGui>
#include <QTextBlock>
#include <QTextLayout>
#include <QAbstractTextDocumentLayout>
#include <QDebug>

QGraphicsLayoutText::QGraphicsLayoutText(const QString &text, QGraphicsItem *parent) :
        QGraphicsTextItem(text, parent), QGraphicsLayoutItem(0, false), p_isMultiline(false)

{

    setFlags(QGraphicsItem::ItemIsFocusable| QGraphicsItem::ItemIsSelectable);
    setTextInteractionFlags(Qt::NoTextInteraction);

    //installEventFilter(this);
    setGraphicsItem(this);
    setCursor(Qt::PointingHandCursor);
    setAcceptDrops(true);

    connect(document(), SIGNAL(contentsChanged()), this, SLOT(updateLayout()));

}

QSizeF QGraphicsLayoutText::sizeHint(Qt::SizeHint which, const QSizeF &constraint) const
{
    QGraphicsTextItem *textItem = static_cast<QGraphicsTextItem*>(graphicsItem());
    switch (which) {
        case Qt::MinimumSize:
                return QSizeF(0, 0);
        case Qt::PreferredSize:
                return textItem->document()->size();
        case Qt::MaximumSize:
            return QSizeF(QWIDGETSIZE_MAX, QWIDGETSIZE_MAX);
        default:
            qWarning("r::TextEdit::sizeHint(): Don't know how to handle the value of 'which'");
            break;
    }

    return constraint;

}

void QGraphicsLayoutText::mouseReleaseEvent(QGraphicsSceneMouseEvent *event) {
    qDebug() << Q_FUNC_INFO;
    QGraphicsTextItem::mouseReleaseEvent(event);
}


void QGraphicsLayoutText::setGeometry(const QRectF &rect)
{
    QGraphicsLayoutItem::setGeometry(rect);
    setTextWidth(rect.width());
    setPos(rect.topLeft());
}

bool QGraphicsLayoutText::collidesWithPath(const QPainterPath &path, Qt::ItemSelectionMode mode) const
{
    bool res = QGraphicsItem::collidesWithPath(path, mode);
    /*if (res) {
        QListIterator<QGraphicsItem*> it(scene()->items());

        while (it.hasNext())
        {
            QGraphicsItem *i = it.next();
            if (i == this) break;
            if (i->isVisible() && i->shape().intersects(mapToItem(i, path)))
                return false;
        }
    }*/
    return res;
}


void QGraphicsLayoutText::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{

    QStyleOptionGraphicsItem opt;
    opt.exposedRect = option->exposedRect;

    QGraphicsTextItem::paint(painter, &opt, widget);

}

void QGraphicsLayoutText::keyPressEvent(QKeyEvent *event)
{
    QGraphicsTextItem::keyPressEvent(event);
}

void QGraphicsLayoutText::focusOutEvent(QFocusEvent *event)
{
    QGraphicsTextItem::focusOutEvent(event);
    //QTextCursor t = textCursor();
    //t.clearSelection();
    //setTextCursor(t);
    emit editingFinished();

}

bool QGraphicsLayoutText::eventFilter(QObject *qobject, QEvent *event)
{
    qDebug() << "eventfilter" << event;
    if (event->type() == QEvent::KeyPress)
    {
        QKeyEvent *kevent = static_cast<QKeyEvent *>(event);
        /*if ((kevent->key() == Qt::Key_Return || kevent->key() == Qt::Key_Enter || kevent->key() == Qt::Key_Tab)
            && !m_isMultiline)
        {
            clearFocus();
            return true;
        }*/
    }
    return false;
}

void QGraphicsLayoutText::focusInEvent(QFocusEvent *event)
{
    QGraphicsTextItem::focusInEvent(event);

}

void QGraphicsLayoutText::updateLayout()
{
    updateGeometry();
    emit geometryChanged();
}

QRectF QGraphicsLayoutText::rectForPosition(int position) const
{

    const QTextBlock block = document()->findBlock(position);
    if (!block.isValid())
        return QRectF();
    const QAbstractTextDocumentLayout *docLayout = document()->documentLayout();
    const QTextLayout *layout = block.layout();
    const QPointF layoutPos = docLayout->blockBoundingRect(block).topLeft();
    int relativePos = position - block.position();

    QTextLine line = layout->lineForTextPosition(relativePos);

    int cursorWidth;
    {
        bool ok = false;
#ifndef QT_NO_PROPERTIES
        cursorWidth = docLayout->property("cursorWidth").toInt(&ok);
#endif
        if (!ok)
            cursorWidth = 1;
    }

    QRectF r;

    if (line.isValid()) {
        qreal x = line.cursorToX(relativePos);
        qreal w = 0;
        /*if (overwriteMode) {
            if (relativePos < line.textLength() - line.textStart())
                w = line.cursorToX(relativePos + 1) - x;
            else
                w = QFontMetrics(block.layout()->font()).width(QLatin1Char(' ')); // in sync with QTextLine::draw()
        }*/
        r = QRectF(layoutPos.x() + x, layoutPos.y() + line.y(),
                   cursorWidth + w, line.height());
    } else {
        r = QRectF(layoutPos.x(), layoutPos.y(), cursorWidth, 10); // #### correct height
    }

    return r;
}

