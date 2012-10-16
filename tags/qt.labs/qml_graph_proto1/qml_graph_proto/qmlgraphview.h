#ifndef EDGELAYER_H
#define EDGELAYER_H

#include <QDeclarativeItem>

class QmlGraphView : public QDeclarativeItem
{
    Q_OBJECT

    Q_PROPERTY(QDeclarativeItem* start READ start WRITE setStart)
    Q_PROPERTY(QDeclarativeItem* end READ end WRITE setEnd)

public:
    explicit QmlGraphView(QDeclarativeItem *parent = 0);
    void paint(QPainter *p, const QStyleOptionGraphicsItem *option, QWidget *widget = 0);



    void setStart(QDeclarativeItem* start);
    QDeclarativeItem* start() const;

    void setEnd(QDeclarativeItem* end);
    QDeclarativeItem* end() const;





signals:
    
public slots:
    void upaint();
protected:
    bool sceneEventFilter(QGraphicsItem *watched, QEvent *event);
    bool eventFilter(QObject *item, QEvent *event);
private:
    QDeclarativeItem* m_start;
    QDeclarativeItem* m_end;

    void registerItem(QDeclarativeItem *start);
    
};

#endif // EDGELAYER_H
