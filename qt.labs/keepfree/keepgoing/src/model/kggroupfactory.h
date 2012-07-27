#ifndef KGGROUPFACTORY_H
#define KGGROUPFACTORY_H

#include <QObject>

class QListWidget;
class KGVisualItemFactory;

class KGGroupFactory : public QObject
{
    Q_OBJECT
public:
    explicit KGGroupFactory(KGVisualItemFactory *visualItemFactory, QListWidget *groupListWidget, QObject *parent = 0);
    void loadGroups();
    void createGroup(QString const groupName);
signals:
    void groupRemoved(int groupId);
public slots:
    void moveItem(int fromGroup, int item, int toGroup);
    void removeRow(int row);

private:
    KGVisualItemFactory *m_visualItemFactory;
    QListWidget *m_groupListWidget;
    QList<int >m_groupIdList;

private slots:
    void groupSelected();

};

#endif // KGGROUPFACTORY_H
