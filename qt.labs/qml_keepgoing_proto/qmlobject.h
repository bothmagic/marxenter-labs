#ifndef QMLOBJECT_H
#define QMLOBJECT_H

#include <QDeclarativeItem>

class QmlObject : public QDeclarativeItem
{
    Q_OBJECT

    Q_PROPERTY(QString text READ text WRITE setText)
public:
    explicit QmlObject(QDeclarativeItem *parent = 0);
    
    QString text();
    void setText(QString text);

signals:
    
public slots:

private:
    QString m_text;
    
};

#endif // QMLOBJECT_H
