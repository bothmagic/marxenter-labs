#include "kgvisualitemfactory.h"
#include <QSqlQuery>
#include <QSqlRecord>
#include <QSqlResult>
#include <QSqlField>
#include <QDebug>
#include "src/ui/kgvisualitem.h"
#include "src/ui/kgvisualview.h"
#include "model/contextmodel.h"

KGVisualItemFactory::KGVisualItemFactory(KGVisualView *visualView, QObject *parent) :
    QObject(parent), m_visualView(visualView), m_loadMode(true)
{
    connect(m_visualView,
            SIGNAL(visualItemAdded(int,int)), this, SLOT(visualItemAdded(int,int)));
    connect(m_visualView,
            SIGNAL(visualItemRemoved(int,int)), this, SLOT(visualItemRemoved(int,int)));
    connect(m_visualView,
            SIGNAL(visualItemUpdated(int,int,QString)), this, SLOT(visualItemUpdated(int,int,QString)));
}

void KGVisualItemFactory::loadGroup(int const groupId, QString const name)
{
    m_loadMode = true;
    QSqlRecord record;
    QSqlQuery query;


    if (m_groupIdList.contains(groupId))
    {
        return;
    }

    int group = m_visualView->createGroup(name);

    query.prepare("select * from task where group_id = :groupId order by sort");
    query.bindValue(":groupId", groupId);
    query.exec();

    KGVisualItem *item;

    QList<int> itemIdList;

    while (query.next())
    {
        record = query.record();
        item = new KGVisualItem;
        item->setDetail(record.value(record.indexOf("text")).toString());
        item->setContext(record.value(record.indexOf("context")).toString().split(" ", QString::SkipEmptyParts));
        item->setWorkState(record.value(record.indexOf("workstate")).toInt());
        item->setState(KGVisualItem::Loaded);

        //connect(item, SIGNAL(itemUpdated(int, QString)), this, SLOT(storeItem(int,QString)));

        m_visualView->insertItem(item, group);
        itemIdList.append(record.value(record.indexOf("id")).toInt());
    }

    m_taskIdList.append(itemIdList);
    m_groupIdList.append(groupId);
    m_loadMode = false;

}

void KGVisualItemFactory::loadGroups(QList<int> groupIdList, QList<QString> groupNameList)
{
    while(!m_groupIdList.isEmpty())
    {
        unloadGroup(m_groupIdList.at(0));
    }

    for(int i = 0; i < groupIdList.count(); i++)
    {
        loadGroup(groupIdList.at(i), groupNameList.at(i));
    }
}

void KGVisualItemFactory::unloadGroup(const int groupId)
{
    int const group = m_groupIdList.indexOf(groupId);
    m_visualView->removeGroup(group);
    m_groupIdList.removeAt(group);
    m_taskIdList.removeAt(group);
}

void KGVisualItemFactory::loadStarred()
{
    loadGroup(1);
}

void KGVisualItemFactory::removeItem(int const group, int const item)
{
    QSqlQuery query;
    int id = m_taskIdList.at(group).at(item);
    query.prepare("delete from task where id = :id");
    query.bindValue(":id", id);
    query.exec();

    query.prepare("update task set sort = sort-1 where sort >= :sort and group_id = :groupId");
    query.bindValue(":sort", item);
    query.bindValue(":groupId", 0);
    query.exec();

    QList<int> itemList = m_taskIdList.at(group);
    itemList.removeAt(item);
    m_taskIdList.replace(group, itemList);

}


void KGVisualItemFactory::createItem(int const group , int const item)
{

    QSqlQuery query;

    query.prepare("update task set sort = sort+1 where sort >= :sort and group_id = :groupId");
    query.bindValue(":sort", item);
    query.bindValue("groupId", m_groupIdList.at(group));
    query.exec();

    query.prepare("insert into task (text,group_id,sort) values(:detail,:groupId,:sort)");
    query.bindValue(":detail", "");
    query.bindValue(":groupId", m_groupIdList.at(group));
    query.bindValue(":sort", item);
    query.exec();
    query.prepare("select last_insert_rowid();");
    query.exec();
    query.first();

    QList<int> itemList = m_taskIdList.at(group);
    itemList.insert(item, query.record().value(0).toInt());
    m_taskIdList.replace(group, itemList);
}


void KGVisualItemFactory::storeItem(int id, QString propName, QVariant value)
{

    QString sqlValue;

    if (value.type() == QVariant::StringList)
    {
        sqlValue = value.toStringList().join(" ");
    } else
    {
        sqlValue = value.toString();
    }
    qDebug() << "content changed in \nproperty: " << propName << "\nvalue: " << sqlValue;
    QSqlQuery query;

    query.prepare("update task set " + propName + " = :value where id = :id");

    query.bindValue(":value", sqlValue);
    query.bindValue(":id", id);

    query.exec();

}

void KGVisualItemFactory::visualItemAdded(int group, int item)
{
    if (m_loadMode) return;
    KGVisualItem *visualItem = m_visualView->visualItem(group, item);
    if (visualItem->state() == KGVisualItem::New)
    {
        createItem(group, item);
        visualItem->setState(KGVisualItem::Loaded);
    }
}

void KGVisualItemFactory::visualItemRemoved(int group, int item)
{
    if (m_loadMode) return;
    removeItem(group, item);
}

void KGVisualItemFactory::visualItemUpdated(int group, int item, QString name)
{
    if (m_loadMode) return;
    KGVisualItem *visualItem = m_visualView->visualItem(group, item);
    storeItem(itemId(group, item), name, visualItem->property(name.toStdString().c_str()));
}

int KGVisualItemFactory::itemId(int group, int item)
{
    return m_taskIdList.at(group).at(item);
}

void KGVisualItemFactory::moveItemToGroup(int const fromGroup, const int item, const int destGroupId)
{
    int destGroup = m_groupIdList.indexOf(destGroupId);
    //todo count aus db holen?
    int destGroupCount = m_taskIdList.at(destGroup).count();

    QSqlQuery query;
    query.prepare("update task set group_id = :groupId, sort = :sort where id = :id");

    query.bindValue(":groupId", destGroupId);
    query.bindValue(":sort", destGroupCount);
    query.bindValue(":id", itemId(fromGroup, item));
    if(query.exec())
    {
        m_visualView->moveVisualItem(fromGroup, item, m_groupIdList.indexOf(destGroupId));
        QList<int> fromList = m_taskIdList.at(fromGroup);
        QList<int> destList = m_taskIdList.at(destGroup);

        destList.append(fromList.at(item));

        fromList.removeAt(item);

        m_taskIdList.replace(fromGroup, fromList);
        m_taskIdList.replace(destGroup, destList);
    }

}

void KGVisualItemFactory::groupRemoved(int groupId)
{
    int const groupIdx = m_groupIdList.indexOf(groupId);
    m_visualView->removeGroup(groupIdx);

    QSqlQuery query;
    query.prepare("delete from task where group_id = :groupId");
    query.bindValue(":groupId", groupId);
    query.exec();

    m_groupIdList.removeAt(groupIdx);
    m_taskIdList.removeAt(groupIdx);

}

void KGVisualItemFactory::viewGroup(int groupId)
{
    m_visualView->scrollToGroup(m_groupIdList.indexOf(groupId));
}
