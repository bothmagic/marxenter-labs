#include "kcdataloader.h"
#include "kcitem.h"
KCDataloader::KCDataloader(QObject *parent) :
    QObject(parent)
{
}

QList<KCItem*> KCDataloader::getSuccessors(QString id, CONNECTION_TYPE conType) {
    QList<KCItem*> items;
    for (int i = 0; i < 10; i++) items << new KCItem(QString("hallo %1").arg(QString::number(i)), QString::number(i));
    return items;
}


KCItem* KCDataloader::firstItem(QString id) {

    return new KCItem("hallo", "00");

}
