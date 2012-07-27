#ifndef TODOGRAPHICSITEM_H
#define TODOGRAPHICSITEM_H

#include <QGraphicsWidget>
#include <QGraphicsItem>
#include <QGraphicsProxyWidget>
#include "../../keepfree-share/src/ui/qgraphicslayouttext.h"
#include "ui/qgraphicslisttext.h"
#include "shortdescredit.h"
#include "taskdetailform.h"

class QCheckBox;

class TodoGraphicsItem : public QGraphicsWidget
{
    Q_OBJECT
public:
    explicit TodoGraphicsItem(QWidget *view, QGraphicsItem *parent = 0);
    QRectF boundingRect() const;

    void setData(const char *name, QVariant value);
    void setRow(int row);
    int row();

signals:
    void edit(TodoGraphicsItem *item);
    void setComplete(bool state);
    void fieldChanged(int row, const char *name, QVariant value);

public slots:
    void edit();
    void closeEdit();
    void completeStateChanged(int state);
    void shortDescrChanged();
    void contextChanged();
    void descriptionChanged();

protected:
    QSizeF sizeHint(Qt::SizeHint which, const QSizeF &constraint) const;
    void mouseReleaseEvent(QGraphicsSceneMouseEvent *event);
    void hoverEnterEvent(QGraphicsSceneHoverEvent *event);
    void hoverLeaveEvent(QGraphicsSceneHoverEvent *event);
    void resizeEvent(QGraphicsSceneResizeEvent *event);

private:

    bool showDetail;
    bool isHover;

    int vrow;
    QWidget *grView;

    QCheckBox *cbComplete;
    QGraphicsLayoutText *tfShortDescr;
    QGraphicsListText   *tfContexts;
    QGraphicsLayoutText *tfLongDescr;
    QGraphicsLayoutText *tfDueDate;

    QGraphicsProxyWidget *createCheckbox();
    QGraphicsProxyWidget *createShortDescr();
    QGraphicsProxyWidget *createLongDescr();

    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);
    bool event(QEvent *event);
    void buildAnchorLayout();

private slots:
    void layoutChanged();
};

#endif // TODOGRAPHICSITEM_H
