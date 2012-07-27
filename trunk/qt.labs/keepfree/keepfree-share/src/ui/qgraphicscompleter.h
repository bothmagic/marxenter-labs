#ifndef QGRAPHICSCOMPLETER_H
#define QGRAPHICSCOMPLETER_H

#include <QObject>
#include <QAbstractItemView>
#include <QAbstractItemModel>
#include <QEvent>
#include <QGraphicsProxyWidget>
#include <QGraphicsItem>
#include <QSortFilterProxyModel>


class QGraphicsCompleter : public QObject
{
    Q_OBJECT
public:
    explicit QGraphicsCompleter(QObject *parent = 0);
    explicit QGraphicsCompleter(QAbstractItemModel *model, QObject *parent = 0);
    explicit QGraphicsCompleter(const QStringList &completions, QObject *parent = 0);

    void setPopup(QAbstractItemView *popup);
    QAbstractItemView* popup();
    QGraphicsProxyWidget* proxyPopup();

    void complete(const QRect &rect = QRect());
    void setGraphicsItem(QGraphicsItem *new_graphicsItem);
    QGraphicsItem* graphicsItem() {return p_graphicsItem;}

    void setCompletionPrefix(const QString &completion);
    QString completionPrefix() const;
    QVariant currentCompletion();

    void setFilterKeyColumn(int filterKeyColumn);
    int filterKeyColumn();

Q_SIGNALS:
    void selected(QVariant selected);
    void completionIsClicked(QVariant item);

public Q_SLOTS:

private:
    QGraphicsProxyWidget *p_proxyPopup;
    QGraphicsItem *p_graphicsItem;
    QSortFilterProxyModel *p_filterKeyColiumn;
    QString p_completionPrefix;
    int p_modelIndex;
    QVariant p_currentCompletion;

    void showPopup(const QRect &rect);

private Q_SLOTS:

    void modelIndexSelected(QModelIndex selIndex);
    void modelIndexClicked(QModelIndex selIndex);

};

#endif // QGRAPHICSCOMPLETER_H
