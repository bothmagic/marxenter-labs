#ifndef KCDATALOADER_H
#define KCDATALOADER_H

#include <QObject>
class KCItem;

class KCDataloader : public QObject
{
    Q_OBJECT

    enum CONNECTION_TYPE {
        FIRST = 1, SECOND = 2, THIRD = 3, FOURTH = 4
    };

public:
    explicit KCDataloader(QObject *parent = 0);

    QList<KCItem*> getSuccessors(QString id, CONNECTION_TYPE conType);
    KCItem* firstItem(QString id);
    
signals:
    
public slots:
    
};

#endif // KCDATALOADER_H
