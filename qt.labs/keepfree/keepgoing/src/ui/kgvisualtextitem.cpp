#include "kgvisualtextitem.h"
#include <QTextDocument>
#include <QPainter>
#include <QGraphicsProxyWidget>
#include <QTextEdit>
#include <QGraphicsScene>
#include <QDebug>
#include <QApplication>
#include <QGraphicsSceneMouseEvent>
#include <QStaticText>
#include <QTextBlock>

#include "ui/qgraphicslayouttext.h"


KGVisualTextItemPrivate::KGVisualTextItemPrivate(QString const &text, KGVisualTextItem * parent):
            q_ptr(parent)
    {
        Q_Q(KGVisualTextItem);
        QTextEdit *edit = createProxyWidget();

    }

void KGVisualTextItemPrivate::_q_updateGeometry()
{
    Q_Q(KGVisualTextItem);
    grProxyWidget->setGeometry(q->geometry());
}

QTextDocument* KGVisualTextItemPrivate::document()
{
    return static_cast<QTextEdit *>(grProxyWidget->widget())->document();
}

QTextEdit * KGVisualTextItemPrivate::createProxyWidget()
{
    Q_Q(KGVisualTextItem);

    QTextEdit *edit = new QTextEdit;

    edit->setFrameShape(QFrame::NoFrame);
    edit->setAttribute(Qt::WA_NoSystemBackground);
    edit->setTextBackgroundColor(Qt::transparent);
    edit->viewport()->setAutoFillBackground(false);
    QTextFormat format = edit->document()->allFormats().at(
            edit->document()->firstBlock().blockFormatIndex());
    //format.setProperty(QTextFormat::TextIndent, 20);

    grProxyWidget = new QGraphicsProxyWidget(q, Qt::ToolTip);
    grProxyWidget->setWidget(edit);
    grProxyWidget->setGeometry(q->geometry());
    grProxyWidget->setFocusPolicy(Qt::StrongFocus);
    grProxyWidget->installEventFilter(q);


    return edit;
}


KGVisualTextItem::KGVisualTextItem(QString const &text, QGraphicsWidget *parent) :
    QGraphicsWidget(parent),
    d_ptr(new KGVisualTextItemPrivate(text, this))
{
    connect(this, SIGNAL(geometryChanged()), this, SLOT(_q_updateGeometry()));
}


QString KGVisualTextItem::document()
{
    Q_D(KGVisualTextItem);
    return d->document()->toPlainText();
}

void KGVisualTextItem::setDocument(const QString &document)
{
    Q_D(KGVisualTextItem);

    QTextCursor cursor(static_cast<QTextEdit *>(d->grProxyWidget->widget())->textCursor());
    cursor.movePosition(QTextCursor::Start);
    cursor.movePosition(QTextCursor::End, QTextCursor::KeepAnchor);

    QTextBlockFormat blockformat;
    //blockformat.setTextIndent(20);
    cursor.setBlockFormat(blockformat);
    //blockformat.setBackground(QBrush(QColor(Qt::yellow)));
    //blockformat.setAlignment(Qt::AlignCenter);
    //cursor.insertBlock(blockformat);
    cursor.insertText(document);

}

bool KGVisualTextItem::eventFilter(QObject *obj, QEvent *event)
{

    Q_D(KGVisualTextItem);
    if (event->type() == QEvent::FocusOut)
    {
        emit documentChanged(objectName());

        qDebug() << "textblock count is "  << d->document()->blockCount();
        qDebug() << "textblock text is "  << d->document()->firstBlock().text();
        qDebug() << "textblock linecount is "  << d->document()->firstBlock().lineCount();
    }

    return false;
}

void KGVisualTextItem::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{
    Q_D(KGVisualTextItem);

    QTextEdit *edit = static_cast<QTextEdit *>(d->grProxyWidget->widget());
    if (edit->document()->toPlainText().isEmpty())
    {
        qreal margin = edit->document()->documentMargin();
        painter->setPen(QColor(Qt::gray));
        painter->drawStaticText(boundingRect().topLeft()+QPointF(margin, margin), QStaticText(tr("Beschreibung eingeben")));
    }
}

void KGVisualTextItem::setFocus(Qt::FocusReason focusReason)
{
    Q_D(KGVisualTextItem);
    static_cast<QTextEdit *>(d->grProxyWidget->widget())->setFocus(focusReason);
}
