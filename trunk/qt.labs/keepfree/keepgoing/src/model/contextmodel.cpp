#include "contextmodel.h"
#include <QSqlRecord>
#include <QSqlField>
#include <QSqlQuery>
#include <QSqlResult>
#include <QDebug>

ContextModel* ContextModel::m_Instance = 0;

ContextModel::ContextModel(QObject *parent) :
    QSqlTableModel(parent)
{
    setTable("context");
    setSort(fieldIndex("shortdescr"), Qt::AscendingOrder);

    select();
}

void ContextModel::addNewContext(QString newContext)
{

    if (newContext.isEmpty())
    {
        return;
    }
    QSqlQuery query;
    query.prepare("select count(id) from context where shortdescr like :context");
    query.bindValue(":context", newContext);
    query.exec();
    if (query.next())
    {
        if (query.record().value("count(id)").toInt() > 0)
            return;
    }

    qDebug() << "save new context " << newContext << " in database.";

    QSqlRecord record;
    QSqlField shortDescr = QSqlField("shortdescr", QVariant::String);
    shortDescr.setValue(newContext);
    record.append(shortDescr);
    QSqlField id = QSqlField("id", QVariant::Int);
    shortDescr.setValue(20);
    record.append(id);

    if(!insertRecord( -1, record))
    {
        qDebug("could not create new context.");

    }
    if (!submitAll())
    {
        qDebug("could not create new context.");
    }

    select();
}
