#include "kggroupfactory.h"
#include <QListWidget>
#include <QListWidgetItem>
#include <QSqlRecord>
#include <QSqlQuery>
#include "kgvisualitemfactory.h"

KGGroupFactory::KGGroupFactory(KGVisualItemFactory *visualItemFactory, QListWidget *groupListWidget, QObject *parent) :
    QObject(parent), m_groupListWidget(groupListWidget), m_visualItemFactory(visualItemFactory)
{
    connect(m_groupListWidget, SIGNAL(moveItem(int,int,int)), this, SLOT(moveItem(int,int,int)));
    connect(m_groupListWidget, SIGNAL(removeRow(int)), this, SLOT(removeRow(int)));
    connect(m_groupListWidget, SIGNAL(itemSelectionChanged()), this, SLOT(groupSelected()));
}

void KGGroupFactory::loadGroups()
{
    QSqlRecord record;
    QSqlQuery query;

    query.prepare("select * from taskgroup order by lower(text)");
    query.exec();

    QListWidgetItem *item;

    while (query.next())
    {
        record = query.record();
        item = new QListWidgetItem;
        item->setText(record.value(record.indexOf("text")).toString());
        m_groupIdList.append(

                    record.value(record.indexOf("id")).toInt());
        item->setFlags(Qt::ItemIsEnabled|Qt::ItemIsSelectable|Qt::ItemIsDropEnabled|Qt::ItemIsDragEnabled);

        if (record.value(record.indexOf("sticky")).toInt())
        {
            QFont font = item->font();
            item->setFont(QFont(font.family(), font.pointSize(), QFont::Bold));
        }

        //m_groupListWidget->addItem(item);
        //m_visualItemFactory->loadGroup(record.value(record.indexOf("id")).toInt(), record.value(record.indexOf("text")).toString());
    }

}

void KGGroupFactory::createGroup(QString const groupName)
{
    QSqlQuery query;

    query.prepare("insert into taskgroup (text) values(:text)");
    query.bindValue(":text", groupName);
    query.exec();

    query.prepare("select last_insert_rowid();");
    query.exec();
    query.first();

    int groupId = query.record().value(0).toInt();

    m_groupIdList.append(groupId);

    m_visualItemFactory->loadGroup(groupId, groupName);
}

void KGGroupFactory::moveItem(int fromGroup, int item, int toGroup)
{
    int destGroupId = m_groupIdList.at(toGroup);
    m_visualItemFactory->moveItemToGroup(fromGroup, item, destGroupId);
}

void KGGroupFactory::removeRow(int row)
{
    int const groupId = m_groupIdList.at(row);

    QSqlQuery query;

    query.prepare("delete from taskgroup where id = :groupId");
    query.bindValue(":groupId", groupId);
    query.exec();

    m_visualItemFactory->groupRemoved(groupId);
    m_groupIdList.removeAt(row);
}

void KGGroupFactory::groupSelected()
{
    QList<QListWidgetItem *> selItems = m_groupListWidget->selectedItems();
    QListWidgetItem *item = selItems.at(0);
    int groupId = m_groupIdList.at(m_groupListWidget->row(item));
    QList<int> groupIdList;
    QList<QString> groupNameList;

    if (item->font().bold())
    {
        for(int i = 0; i < m_groupListWidget->count(); i++)
        {
            item = m_groupListWidget->item(i);
            if (item->font().bold())
            {
                groupIdList << m_groupIdList.at(m_groupListWidget->row(item));
                groupNameList << item->text();
            }

        }
        m_visualItemFactory->loadGroups(groupIdList, groupNameList);
        m_visualItemFactory->viewGroup(groupId);
    } else
    {
        groupIdList << m_groupIdList.at(m_groupListWidget->row(item));
        groupNameList << item->text();
        m_visualItemFactory->loadGroups(groupIdList, groupNameList);
    }

}
