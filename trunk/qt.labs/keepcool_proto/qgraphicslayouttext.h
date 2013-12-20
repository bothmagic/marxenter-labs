#ifndef QGRAPHICSLAYOUTTEXT_H
#define QGRAPHICSLAYOUTTEXT_H

#include <QGraphicsLayoutItem>
#include <QGraphicsTextItem>

class QGraphicsLayoutText : public QGraphicsTextItem, public QGraphicsLayoutItem
{
    Q_OBJECT
    Q_INTERFACES(QGraphicsLayoutItem)
public:
    enum LayoutFlags
    {

    };


    explicit QGraphicsLayoutText(const QString &text = QString(), QGraphicsItem *parent = 0);

    QSizeF sizeHint(Qt::SizeHint which, const QSizeF &constraint=QSizeF()) const;
    void setGeometry(const QRectF &rect);

    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);

    bool isMultiline() {return p_isMultiline;}
    void setMultiline(bool multiline) {p_isMultiline = multiline;}
    virtual bool collidesWithPath(const QPainterPath &path, Qt::ItemSelectionMode mode) const;


signals:
    void editingFinished();
    void geometryChanged();

public slots:
    void updateLayout();

protected:
    void keyPressEvent(QKeyEvent *event);
    void focusOutEvent(QFocusEvent *event);
    void focusInEvent(QFocusEvent *event);
    void mouseReleaseEvent(QGraphicsSceneMouseEvent *event);
    bool eventFilter(QObject *qobject, QEvent *event);

    QRectF rectForPosition(int position) const;

private:
    bool p_isMultiline;

};

#endif // QGRAPHICSLAYOUTTEXT_H
