#include "qgraphicscompleter.h"
#include <QKeyEvent>
#include <QGraphicsScene>
#include <QDebug>
#include <QApplication>
#include <QAbstractItemView>
#include <QScrollBar>
#include <QRectF>
#include <QGraphicsView>
#include <QStringListModel>

//
// public
//

QGraphicsCompleter::QGraphicsCompleter(QObject *parent) :
        QObject(parent), p_currentCompletion(QVariant(QVariant::Invalid))
{

}

QGraphicsCompleter::QGraphicsCompleter(QAbstractItemModel *model, QObject *parent) :
    QObject(parent), p_currentCompletion(QVariant(QVariant::Invalid))
{
    p_filterKeyColiumn = new QSortFilterProxyModel();
    p_filterKeyColiumn->setFilterCaseSensitivity(Qt::CaseInsensitive);
    p_filterKeyColiumn->setSourceModel(model);

}

QGraphicsCompleter::QGraphicsCompleter(const QStringList &completions, QObject *parent) :
    QObject(parent), p_currentCompletion(QVariant(QVariant::Invalid))
{
    p_filterKeyColiumn = new QSortFilterProxyModel();
    p_filterKeyColiumn->setSourceModel(new QStringListModel(completions));
    p_filterKeyColiumn->setFilterCaseSensitivity(Qt::CaseInsensitive);

}

void QGraphicsCompleter::setPopup(QAbstractItemView *popup)
{
    popup->setEditTriggers(QAbstractItemView::NoEditTriggers);
    p_proxyPopup = new QGraphicsProxyWidget;
    p_proxyPopup->setWidget(popup);
    popup->setWindowFlags(Qt::Widget);
    p_proxyPopup->setFocusPolicy(Qt::StrongFocus);
    popup->setFocusPolicy(Qt::NoFocus);

    popup->setAttribute(Qt::WA_TranslucentBackground);

    p_proxyPopup->setFocusProxy(p_graphicsItem);
    popup->setModel(p_filterKeyColiumn);
    // eventfilter implementieren für focus out und mal überlegen wie das umgesetzt werden soll.
    popup->installEventFilter(this);

    connect(popup->selectionModel(), SIGNAL(currentChanged(QModelIndex, QModelIndex)), this, SLOT(modelIndexSelected(QModelIndex)));
    connect(popup, SIGNAL(clicked(QModelIndex)), this, SLOT(modelIndexClicked(QModelIndex)));
}

void QGraphicsCompleter::complete(const QRect &rect)
{
    if (p_proxyPopup->scene() == 0)
        p_graphicsItem->scene()->addItem(p_proxyPopup);

    QAbstractItemView *popup = static_cast<QAbstractItemView *>(p_proxyPopup->widget());
    if (popup->model()->rowCount() == 0)
    {
        popup->hide();
        p_graphicsItem->setFocus();
        return;
    }

   showPopup(rect);
}

void QGraphicsCompleter::setCompletionPrefix(const QString &completionPrefix)
{
    QModelIndexList list = popup()->selectionModel()->selectedIndexes();


    if (p_completionPrefix == completionPrefix)
        return;
    p_completionPrefix = completionPrefix;
    QString regexp = completionPrefix;
    p_filterKeyColiumn->setFilterRegExp(regexp.insert(0, "^").append(".*$"));
    popup()->selectionModel()->clear();


}

QString QGraphicsCompleter::completionPrefix() const
{
    return p_completionPrefix;
}

QVariant QGraphicsCompleter::currentCompletion()
{
    return p_currentCompletion;
}

QAbstractItemView* QGraphicsCompleter::popup()
{
    return static_cast<QAbstractItemView *>(p_proxyPopup->widget());
}

QGraphicsProxyWidget* QGraphicsCompleter::proxyPopup()
{
    return p_proxyPopup;
}

void QGraphicsCompleter::setGraphicsItem(QGraphicsItem *new_graphicsItem)
{
    p_graphicsItem = new_graphicsItem;
}

void QGraphicsCompleter::setFilterKeyColumn(int modelIndex)
{

    p_filterKeyColiumn->setFilterKeyColumn(modelIndex);

}

int QGraphicsCompleter::filterKeyColumn()
{
    return p_filterKeyColiumn->filterKeyColumn();
}

//
// private
//

void QGraphicsCompleter::showPopup(const QRect& rect)
{
    Qt::LayoutDirection dir = Qt::LeftToRight;
    const int maxVisibleItems = 7;
    QPointF pos;
    int rh, w;

    const QRect screen = p_proxyPopup->scene()->views().at(0)->viewport()->geometry();
    QAbstractItemView *popup = static_cast<QAbstractItemView *>(p_proxyPopup->widget());

    int h = (popup->sizeHintForRow(0) * qMin(maxVisibleItems, popup->model()->rowCount()) + 3) + 3;
    QScrollBar *hsb = popup->horizontalScrollBar();
    if (hsb && hsb->isVisible())
        h += popup->horizontalScrollBar()->sizeHint().height();

    if (rect.isValid()) {
        rh = rect.height();
        w = rect.width();
        pos = rect.bottomLeft();
    } else {
        rh = graphicsItem()->boundingRect().height();
        pos = graphicsItem()->mapRectToScene(
                graphicsItem()->boundingRect()).bottomLeft();
        w = popup->width();
    }

    if (w > screen.width())
        w = screen.width();
    if ((pos.x() + w) > (screen.x() + screen.width()))
        pos.setX(screen.x() + screen.width() - w);
    if (pos.x() < screen.x())
        pos.setX(screen.x());

    int top = pos.y() - rh - screen.top() + 2;
    int bottom = screen.bottom() - pos.y();
    h = qMax(h, popup->minimumHeight());
    if (h > bottom) {
        h = qMin(qMax(top, bottom), h);

        if (top > bottom)
            pos.setY(pos.y() - h - rh+ 2);
    }

    popup->setGeometry(pos.x(), pos.y(), w, h+5);
    p_proxyPopup->update();

    if (!p_proxyPopup->isVisible())
        p_proxyPopup->show();

}


void QGraphicsCompleter::modelIndexSelected(QModelIndex selIndex)
{
    p_currentCompletion = selIndex.data();
    if (!p_proxyPopup->isVisible())
        return;

    qDebug() << "currentCompletion is " << p_currentCompletion;
    emit selected(p_currentCompletion);
}

void QGraphicsCompleter::modelIndexClicked(QModelIndex selIndex)
{

    p_proxyPopup->hide();
    p_graphicsItem->setFocus();
    emit completionIsClicked(selIndex.data());
}
