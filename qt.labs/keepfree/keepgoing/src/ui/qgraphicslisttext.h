#ifndef QGRAPHICSLISTTEXT_H
#define QGRAPHICSLISTTEXT_H

#include "ui/qgraphicslayouttext.h"
#include "ui/qgraphicscompleter.h"
#include <QGraphicsItem>
#include <QTextDocument>
#include <QTextFormat>
#include <QDebug>

class QTextCursor;
class QAbstractItemModel;

class QGraphicsListText : public QGraphicsLayoutText
{
    Q_OBJECT

    enum { ContextTextFormat = QTextFormat::UserObject + 1 };

public:
    enum ContextProperties { ContextName = 1 , ContextRect = 2};

    explicit QGraphicsListText(QStringList strList, QGraphicsItem *parent = 0);
    explicit QGraphicsListText(QString str, QGraphicsItem *parent = 0);
    explicit QGraphicsListText(QGraphicsItem *parent = 0);

    QStringList m_itemList;


    //
    // properties
    //

    void setStringList(QString str)
    {

        setStringList(str.trimmed().split(" "));

    }

    void setStringList(QStringList strList);
    QStringList stringList();

    void setCompleter(QGraphicsCompleter *completer);

    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);

signals:
    void stringListUpdated(const QStringList &strList);
    void insertItem(const QString &item);
    void removeItem(const QString &item);
    void contentChanged(QString const &objectname);

public slots:

protected:
    //void keyPressEvent(QKeyEvent *event);
    void focusOutEvent(QFocusEvent *event);
    bool eventFilter(QObject *qobject, QEvent *event);

private:
    QStringList p_StrList;
    QGraphicsCompleter *p_completer;
    int p_startCursorPos;
    int p_endCursorPos;

    void p_init();
    bool p_insertContextItem(QString contextText, QTextCursor cursor);
    QTextCursor p_findEndCurosrPos();
    void p_reset(bool keepFocus);
    QStringList p_buildItemList();

private slots:

    void p_contentsChanged();
    void p_updateList(const QStringList &strList);
    void p_completeSelection(QString selection);
    void p_completionSelected(QVariant completion);
    void p_insertEnteredContext(QVariant completion);
    void p_checkListChanged(int cPos, int chDel, int chIns);

};

#endif // QGRAPHICSLISTTEXT_H
