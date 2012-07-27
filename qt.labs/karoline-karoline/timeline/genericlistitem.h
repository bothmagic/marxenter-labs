#ifndef GENERICLISTITEM_H
#define GENERICLISTITEM_H

#include <QObject>
#include <QHash>

class ListItem : public QObject
{
    Q_OBJECT

public:
    // WTF? Jakoze rovnou definice konstruktoru v hlavickovem souboru?
    ListItem(QObject *parent = 0 ) : QObject(parent) {}
    virtual ~ListItem() {}
    // a toto je na co?
//    virtual QString id() const = 0;
    virtual quint64 id() const = 0;
    virtual QVariant data(int role) const = 0;
    virtual QHash<int, QByteArray> roleNames() const = 0;

signals:
    void dataChanged();
};

#endif // GENERICLISTITEM_H
