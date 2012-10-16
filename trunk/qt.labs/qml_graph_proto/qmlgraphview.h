#ifndef EDGELAYER_H
#define EDGELAYER_H

#include <QDeclarativeItem>

class QmlGraphItem;
class QmlGraphEdge;
class QmlGraphView : public QDeclarativeItem
{
    Q_OBJECT

public:
    explicit QmlGraphView(QDeclarativeItem *parent = 0);

    //void paint(QPainter *p, const QStyleOptionGraphicsItem *option, QWidget *widget = 0);
    Q_INVOKABLE void registerItem(int id, QmlGraphItem *node);
    Q_INVOKABLE void registerEdge(int firstId, int secondId);
signals:
    
public slots:
    void updateItem(int itemId);
protected:

private:
    QHash<int, QmlGraphItem*> m_items;
    QMultiHash<int, QmlGraphEdge*> m_edge;

    void addEdge(int firstId, int secondId);

};

#endif // EDGELAYER_H
