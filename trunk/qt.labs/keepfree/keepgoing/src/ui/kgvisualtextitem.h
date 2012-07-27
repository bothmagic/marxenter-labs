#ifndef KGVISUALTEXTITEM_H
#define KGVISUALTEXTITEM_H

#include <QGraphicsWidget>


class QTextDocument;
class KGVisualTextItem;
class QGraphicsProxyWidget;
class QTextEdit;

class KGVisualTextItemPrivate
{

public:
    KGVisualTextItemPrivate(QString const &text, KGVisualTextItem * parent);
    void _q_updateGeometry();

    QTextEdit * createProxyWidget();
    QTextDocument* document();

    QGraphicsProxyWidget *grProxyWidget;
    KGVisualTextItem * const q_ptr;
    Q_DECLARE_PUBLIC(KGVisualTextItem)

};


class KGVisualTextItem : public QGraphicsWidget
{
    Q_OBJECT

    Q_PROPERTY(QString document READ document WRITE setDocument)

public:
    explicit KGVisualTextItem(QString const &text, QGraphicsWidget *parent = 0);

    QString document();
    void setDocument(QString const &document);

    void setFocus(Qt::FocusReason focusReason = Qt::OtherFocusReason);

    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);

    bool eventFilter(QObject *obj, QEvent *event);

signals:
    void documentChanged(QString name);
public slots:

private:
    KGVisualTextItemPrivate * const d_ptr;

    Q_PRIVATE_SLOT(d_func(), void _q_updateGeometry())

    Q_DECLARE_PRIVATE(KGVisualTextItem)

    Q_DISABLE_COPY(KGVisualTextItem)

};

#endif // KGVISUALTEXTITEM_H
