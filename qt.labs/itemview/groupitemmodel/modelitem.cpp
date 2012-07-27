#include "modelitem.h"
#include <QSharedData>
#include <QString>
class ModelItemData : public QSharedData {
public:

    QString id;
    QString name;
    QString group;
};

ModelItem::ModelItem(QString id, QString name, QString group) : data(new ModelItemData)
{
    data->id = id;
    data->name = name;
    data->group = group;
}

ModelItem::ModelItem(const ModelItem &rhs) : data(rhs.data)
{
}

ModelItem &ModelItem::operator=(const ModelItem &rhs)
{
    if (this != &rhs)
        data.operator=(rhs.data);
    return *this;
}

bool ModelItem::operator==(const ModelItem &rhs)
{
    return data->id == rhs.data->id;
}


ModelItem::~ModelItem()
{
}

QString ModelItem::id() const {
    return data->id;
}

void ModelItem::setId(QString id) {
    data->id = id;
}

QString ModelItem::name() const {
    return data->name;
}

void ModelItem::setName(QString name) {
    data->name = name;
}

QString ModelItem::group() const {
    return data->group;
}

void ModelItem::setGroup(QString group) {
    data->group = group;
}
