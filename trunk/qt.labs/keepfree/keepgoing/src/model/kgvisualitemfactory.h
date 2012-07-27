#ifndef KGVISUALITEMFACTORY_H
#define KGVISUALITEMFACTORY_H

#include <QObject>
#include <QVariant>

class KGVisualView;

class KGVisualItemFactory : public QObject
{
    Q_OBJECT
public:
    explicit KGVisualItemFactory(KGVisualView *visualView, QObject *parent = 0);
    void removeItem(int const group, int const item);
    void createItem(int const group, int const item);
    void moveItemToGroup(int const fromGroup, int const item, int const destGroupId);
    int itemId(int group, int item);
    void viewGroup(int groupId);
    void loadGroups(QList<int> groupIdList, QList<QString> groupNameList);

public slots:
    void loadGroup(int const groupId=0, QString const name="Unknown");
    void loadStarred();
    void unloadGroup(int const groupId);
    void storeItem(int id, QString attrName, QVariant value);
    void visualItemAdded(int group, int item);
    void visualItemRemoved(int group, int item);
    void visualItemUpdated(int group, int item,QString name);
    void groupRemoved(int groupId);
private:
    KGVisualView *m_visualView;
    QList<QList<int> > m_taskIdList;
    QList<int> m_groupIdList;
    bool m_loadMode;


};

#endif // KGVISUALITEMFACTORY_H
