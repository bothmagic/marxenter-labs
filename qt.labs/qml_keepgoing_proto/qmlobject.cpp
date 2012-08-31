#include "qmlobject.h"
#include <QDebug>

QmlObject::QmlObject(QDeclarativeItem *parent) :
    QDeclarativeItem(parent), m_text("Yeahh it works")
{
}

QString QmlObject::text() {
    qDebug() << m_text;
    return m_text;
}

void QmlObject::setText(QString text)
{
    m_text = text;
}
