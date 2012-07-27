#ifndef LISTENTRY_H
#define LISTENTRY_H
#include <QString>
#include <QList>

class ListEntry
 {
public:
    ListEntry(const QString &name,
              const QString &description,
              const int type,
              ListEntry *parent = 0);

    int type() const;
    QString name() const;
    QString description() const;

    void setType(int type);
    void setName(const QString &name);
    void setDescription(const QString &description);
    void appendChild(ListEntry *ListEntry);
    ListEntry* child(int row);
    int childCount();
    ListEntry *parent();
    int row();
    QList<ListEntry*> childItems();

private:
    int m_type;
    QString m_name;
    QString m_description;
    ListEntry *m_parent;
    QList<ListEntry*> m_childItems;
};

#endif // LISTENTRY_H
