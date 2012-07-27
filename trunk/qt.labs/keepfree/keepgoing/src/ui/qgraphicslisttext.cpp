#include "qgraphicslisttext.h"
#include "contexttextobject.h"
#include <QTextOption>
#include <QCompleter>
#include <QAbstractTextDocumentLayout>
#include <QTextBlockFormat>
#include <QTextCharFormat>
#include <QTextBlock>
#include <QStringListModel>
#include <QTextEdit>
#include <QListView>
#include <QGraphicsScene>
#include <QGraphicsView>
#include <QApplication>
#include "../model/contextmodel.h"
#include <QDebug>
#include <QStaticText>

QGraphicsListText::QGraphicsListText(QGraphicsItem *parent) :
    QGraphicsLayoutText("", parent)
{
    p_init();

}

QGraphicsListText::QGraphicsListText(QStringList strList, QGraphicsItem *parent) :
    QGraphicsLayoutText("", parent)
{
    p_init();
    setStringList(strList);
}

QGraphicsListText::QGraphicsListText(QString str, QGraphicsItem *parent) :
    QGraphicsLayoutText("", parent), p_StrList(str.split(" ", QString::SkipEmptyParts))
{
    p_init();
    setStringList(str);
}

QStringList QGraphicsListText::stringList()
{
    return m_itemList;
}

void QGraphicsListText::setStringList(QStringList strList)
{

    document()->clear();

    QStringListIterator it(strList);

    while (it.hasNext())
    {
        p_insertContextItem(it.next(), textCursor());
    }

}


void QGraphicsListText::p_updateList(const QStringList &strList)
{
    //todo delete

}

void QGraphicsListText::p_contentsChanged()
{
    m_itemList = p_buildItemList();
    emit contentChanged(objectName());
}

void QGraphicsListText::focusOutEvent(QFocusEvent *event)
{
    QGraphicsLayoutText::focusOutEvent(event);

    if (event->reason() != Qt::OtherFocusReason) {

        QTextCursor cursor = p_findEndCurosrPos();
        QString contextText = cursor.selectedText();

        /*if (cursor.charFormat().objectType() != ContextTextFormat)
        {
            if (p_insertContextItem(contextText, cursor))
            {
                emit insertItem(contextText);
            }
        }
        p_reset(false);*/



    }

}



/*void QGraphicsListText::keyPressEvent(QKeyEvent *event)
{
    QTextCursor cursor;
    bool update = true;

    QGraphicsTextItem::keyPressEvent(event);

    if (!update)
        return;


    //QString newText = m_completer->currentCompletion().toString();

    if (!newText.isEmpty()){
        cursor.insertText(newText);

        cursor.setPosition(curPosition, QTextCursor::KeepAnchor);
        setTextCursor(cursor);

    }

}*/

bool QGraphicsListText::eventFilter(QObject *qobject, QEvent *event)
{
    QTextCursor cursor;
    QKeyEvent *kevent;
    QString contextText;
    int curPosition;
    QRectF rect;
    QRectF cursorRect;
    bool filter = false;

    switch(event->type())
    {
    case QEvent::KeyPress:
        kevent = static_cast<QKeyEvent*>(event);
        if (p_completer && p_completer->popup()->isVisible()) {
             // The following keys are forwarded by the completer to the widget
            switch (kevent->key()) {

                case Qt::Key_Up:
                case Qt::Key_Down:
                case Qt::Key_PageUp:
                case Qt::Key_PageDown:
                     QApplication::sendEvent(p_completer->popup(), event);
                     return true;
                default:
                    break;
            }
         }

        switch (kevent->key())
        {
            // add new item to the list
            case Qt::Key_Space:
            case Qt::Key_Tab:
            case Qt::Key_Enter:
            case Qt::Key_Return:
                cursor = p_findEndCurosrPos();
                contextText = cursor.selectedText();
                if (p_insertContextItem(contextText, cursor))
                {
                    emit insertItem(contextText);
                }
                p_reset(true);

                return true;
            break;
            // break current entry
            case Qt::Key_Escape:
                cursor = p_findEndCurosrPos();
                cursor.removeSelectedText();
                p_reset(true);
                return true;
            break;
            default:
            break;
        }

        cursor = textCursor();
        // do not do set startposition
        switch (kevent->key())
        {
            case Qt::Key_Left:
            case Qt::Key_Right:
            case Qt::Key_Home:
            case Qt::Key_End:
            case Qt::Key_Backspace:
            return false;
            break;
        default:
            break;
        }

        // set the p_startCusorPos

        if (p_startCursorPos < 0)
        {
            p_startCursorPos = cursor.position();
        }

        break;

    case QEvent::KeyRelease:

        if (p_startCursorPos < 0)
            return false;

        kevent = static_cast<QKeyEvent*>(event);

        switch (kevent->key())
        {
            case Qt::Key_Up:
            case Qt::Key_Down:
            case Qt::Key_PageUp:
            case Qt::Key_PageDown:
            /*case Qt::Key_Left:
            case Qt::Key_Right:
            case Qt::Key_Home:
            case Qt::Key_End:*/
            case Qt::Key_Space:
            case Qt::Key_Tab:
            case Qt::Key_Enter:
            case Qt::Key_Return:
            case Qt::Key_Escape:
            filter = true;
            return filter;
            default:
            break;
        }
        cursor = textCursor();
        curPosition = cursor.position();
        cursor = p_findEndCurosrPos();
        //const int firstPosition = cursor.position();
        contextText = cursor.selectedText();

        // if cursor out of focus then insert a new item
        //qDebug() << p_startCursorPos << ", " << curPosition << ", " << p_endCursorPos;
        if (curPosition < p_startCursorPos || curPosition > p_endCursorPos)
        {
            if (p_insertContextItem(contextText, cursor))
            {
                emit insertItem(contextText);
            }
            p_reset(true);
            return true;
        }

        //qDebug() << "entered text " << text;
        p_completer->setCompletionPrefix(QRegExp::escape(contextText));

        cursorRect = rectForPosition(p_startCursorPos);
        rect = boundingRect();
        rect.setLeft(cursorRect.left());
        rect.setBottom(cursorRect.bottom() + 3);
        rect.setWidth(p_completer->popup()->width());

        p_completer->complete(mapRectToScene(rect).toRect());
        break;

    default:
        break;

    }

    return filter;
}

void QGraphicsListText::p_completeSelection(QString selection)
{
    //todo delete
}

//
// private
//

void QGraphicsListText::p_init()
{
    p_startCursorPos = -1;
    p_endCursorPos = -1;

    connect(this, SIGNAL(stringListUpdated(QStringList)), this, SLOT(p_updateList(QStringList)));
    connect(this, SIGNAL(insertItem(QString)), this, SLOT(p_contentsChanged()));
    connect(this, SIGNAL(removeItem(QString)), this, SLOT(p_contentsChanged()));

    connect(document(), SIGNAL(contentsChange(int,int,int)), this, SLOT(p_checkListChanged(int, int, int)));

    // init context object
    QObject *contextInterface = new ContextTextObject;
    document()->documentLayout()->registerHandler(ContextTextFormat, contextInterface);
    installEventFilter(this);
}

void QGraphicsListText::setCompleter(QGraphicsCompleter *completer)
{
    p_completer = completer;

    connect(p_completer, SIGNAL(selected(QVariant)), this, SLOT(p_completionSelected(QVariant)));
    connect(p_completer, SIGNAL(completionIsClicked(QVariant)), this, SLOT(p_insertEnteredContext(QVariant)));
}

void QGraphicsListText::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{

    if (m_itemList.isEmpty() && document()->isEmpty())
    {
        qreal margin = document()->documentMargin();
        painter->setPen(QColor(Qt::gray));
        painter->drawStaticText(boundingRect().topLeft()+QPointF(margin, margin), QStaticText(tr("Kontext eingeben")));
    }

    QGraphicsLayoutText::paint(painter, option, widget);

}

void QGraphicsListText::p_reset(bool keepFocus)
{
    p_startCursorPos = -1;
    p_endCursorPos = -1;
    if (p_completer->proxyPopup()->isVisible())
    {
        p_completer->popup()->hide();
        if (keepFocus)
            setFocus();
        p_completer->popup()->selectionModel()->clear();
    }
}

QTextCursor QGraphicsListText::p_findEndCurosrPos()
{
    QTextCursor cursor = textCursor();
    if (p_startCursorPos < 0)
    {
        return cursor;
    }

    cursor.movePosition(QTextCursor::EndOfBlock);

    p_endCursorPos = cursor.position();

    while (cursor.charFormat().objectType() == ContextTextFormat
           && !cursor.atBlockStart() && p_startCursorPos < p_endCursorPos)
    {
        cursor.setPosition(--p_endCursorPos);
    }

    if (p_endCursorPos == p_startCursorPos)
        return cursor;

    cursor.setPosition(p_startCursorPos);

    cursor.setPosition(p_endCursorPos, QTextCursor::KeepAnchor);
    return cursor;
}

bool QGraphicsListText::p_insertContextItem(QString contextText, QTextCursor cursor)
{
    if (contextText.isEmpty())
    {
        return false;
    }
    qDebug() << "insert context item " << contextText;
    QTextDocument doc(document());
    doc.setDefaultFont(document()->defaultFont());
    doc.setPlainText(contextText);

    QSizeF blockSize = doc.size();

    //qDebug() << "get context " << contextText;
    //qDebug() << "block size is " << blockSize;

    QTextCharFormat svgCharFormat;
    svgCharFormat.setFont(cursor.charFormat().font());
    svgCharFormat.setObjectType(ContextTextFormat);

    svgCharFormat.setProperty(ContextName, contextText);
    svgCharFormat.setProperty(ContextRect, blockSize);

    cursor.insertText(QString(QChar::ObjectReplacementCharacter), svgCharFormat);
    //emit insertItem(contextText);
    m_itemList.append(contextText);
    return true;
}

void QGraphicsListText::p_completionSelected(QVariant completion)
{
    if (!completion.isValid())
        return;
    qDebug() << "completion is selected " << completion;
    QTextCursor cursor = p_findEndCurosrPos();
    cursor.insertText(completion.toString());
}

void QGraphicsListText::p_insertEnteredContext(QVariant completion)
{
    if (!completion.isValid())
        return;
    QTextCursor cursor = p_findEndCurosrPos();
    if (p_insertContextItem(completion.toString(), cursor))
    {
        emit insertItem(completion.toString());
    }
    p_reset(true);
}

void QGraphicsListText::p_checkListChanged(int cPos, int chDel, int chIns)
{
    QStringList oldItemList = m_itemList;
    QStringList newItemList = p_buildItemList();

    if (chDel > 0)
    {
        foreach (QString item, newItemList)
        {
            oldItemList.removeAll(item);
        }

        if (!oldItemList.isEmpty())
        {
            qDebug() << "item " << oldItemList <<  "is deleted!";
            emit removeItem(oldItemList.at(0));

        }
    }
}

QStringList QGraphicsListText::p_buildItemList()
{
    QStringList strList;
    QTextCursor cursor = textCursor();
    cursor.movePosition(QTextCursor::StartOfBlock);

    while (!cursor.atBlockEnd())
    {
        cursor.movePosition(QTextCursor::NextCharacter);
        if (cursor.charFormat().objectType() == ContextTextFormat)
        {
            strList.append(cursor.charFormat().property(ContextName).toString());
        }
    }
    return strList;
}
