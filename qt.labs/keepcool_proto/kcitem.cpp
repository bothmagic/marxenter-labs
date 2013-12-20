#include "kcitem.h"

KCItem::KCItem(QObject *parent) :
    QObject(parent)
{
}

KCItem::KCItem(QString name, QString id, QObject *parent):QObject(parent) {
    setName(name);
    setId(id);
}

void KCItem::setName(QString name) {
    m_name = name;
}

QString KCItem::name() const {
    return m_name;
}

void KCItem::setId(QString id) {
    m_id = id;
}

QString KCItem::id() const {
    return m_id;
}
