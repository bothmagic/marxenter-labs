#include "projectlistmodel.h"

ProjectListModel::ProjectListModel(QObject *parent) :
    QStringListModel(parent)
{
    QStringList stringList;
    for (int i = 0; i < 100; i++) {
        stringList.append("Das ist ein Projektname "+ QString::number(i));
    }
    setStringList(stringList);

    QHash<int, QByteArray> names;

    names.insert(Qt::EditRole, "text");

    setRoleNames(names);

}
