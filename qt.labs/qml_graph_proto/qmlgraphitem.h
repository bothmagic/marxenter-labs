#ifndef QMLGRAPHITEM_H
#define QMLGRAPHITEM_H

#include <QDeclarativeItem>

class QmlGraphItem : public QDeclarativeItem
{
    Q_OBJECT
public:
    explicit QmlGraphItem(QDeclarativeItem *parent = 0);
    void setGraphItemId(int graphItemId);
    int graphItemId() const;

signals:
    void itemChanged(int id);

protected:
    QVariant itemChange(GraphicsItemChange change, const QVariant &value);

private:
    int m_graphItemId;
    
};

#endif // QMLGRAPHITEM_H
