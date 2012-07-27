#include "kgitemmodel.h"
#include "kggroupfactory.h"
#include <QSqlRecord>
#include <QSqlQuery>
#include <QStandardItem>

KGItemModel::KGItemModel(QObject *parent) :
    QStandardItemModel(parent)
{
}

void KGItemModel::loadGroups()
{
    QSqlRecord record;
    QSqlQuery query;

    query.prepare("select * from taskgroup order by lower(text)");
    query.exec();

    QStandardItem *textItem;
    QStandardItem *stickyItem;
    QList<QStandardItem*> itemRow;
    while (query.next())
    {
        record = query.record();
        textItem = new QStandardItem;
        textItem->setText(record.value(record.indexOf("text")).toString());
        /*m_groupIdList.append(
                    record.value(record.indexOf("id")).toInt());*/
        textItem->setFlags(Qt::ItemIsEnabled|Qt::ItemIsSelectable|Qt::ItemIsDropEnabled|Qt::ItemIsDragEnabled);
        stickyItem = new QStandardItem;
        if (record.value(record.indexOf("sticky")).toInt())
        {
            QFont font = textItem->font();
            textItem->setFont(QFont(font.family(), font.pointSize(), QFont::Bold));
            stickyItem->setData(true, Qt::DisplayRole);
        } else
        {
            stickyItem->setData(false, Qt::DisplayRole);
        }

        itemRow << textItem << stickyItem;
        m_groupIds.insert(textItem, record.value(record.indexOf("id")).toInt());
        appendRow(itemRow);
        itemRow.clear();
        loadGroupItems(textItem);
    }

}

void KGItemModel::loadGroupItems(QStandardItem *item)
{
    QSqlRecord record;
    QSqlQuery query;
    int const groupId = m_groupIds.value(item);

    query.prepare("select * from task where group_id = :groupId order by sort");
    query.bindValue(":groupId", groupId);
    query.exec();

    QStandardItem *detail;
    QStandardItem *context;
    QStandardItem *workstate;

    QList<QStandardItem *> row;
    while (query.next())
    {
        record = query.record();

        detail = new QStandardItem;
        detail->setData(record.value(record.indexOf("text")).toString(), Qt::DisplayRole);
        context = new QStandardItem;
        context->setData(record.value(record.indexOf("context")).toString().split(" ", QString::SkipEmptyParts), Qt::DisplayRole);
        workstate = new QStandardItem;
        workstate->setData(record.value(record.indexOf("workstate")).toInt(), Qt::DisplayRole);

        //connect(item, SIGNAL(itemUpdated(int, QString)), this, SLOT(storeItem(int,QString)));
        row << detail << context << workstate;
        item->appendRow(row);
        row.clear();
        m_itemIds.insert(detail, record.value(record.indexOf("id")).toInt());
    }

}


void KGItemModel::createItem(const int group, const int itemIdx)
{
    QStandardItem *parent = itemFromIndex(index(group, 0));

    QList<QStandardItem *> row;
    row << new QStandardItem << new QStandardItem << new QStandardItem;
    if (itemIdx < 0)
    {
        parent->appendRow(row);
    } else
    {
        parent->insertRow(itemIdx, row);
    }

}
