#include "listentry.h"

ListEntry::ListEntry(const QString &name,
                     const QString &description,
                     const int type, ListEntry *parent):
                        m_type(type),m_name(name), m_description(description), m_parent(parent)
{

}

QString ListEntry::description() const{
    return m_description;
}

QString ListEntry::name() const {
    return m_name;
}

int ListEntry::type() const {
    return m_type;
}

void ListEntry::setDescription(const QString &description) {
    m_description = description;
}

void ListEntry::setName(const QString &name) {
    m_name = name;
}

void ListEntry::setType(int type) {
    m_type = type;
}

void ListEntry::appendChild(ListEntry *listEntry) {
    m_childItems << listEntry;
}

ListEntry* ListEntry::child(int row) {
    return m_childItems.value(row);
}

int ListEntry::childCount() {
    return m_childItems.size();
}

int ListEntry::row() {
    if (m_parent)
        return m_parent->childItems().indexOf(this);
}

QList<ListEntry*> ListEntry::childItems()
{
    return m_childItems;
}

ListEntry* ListEntry::parent()
{
    return m_parent;
}
