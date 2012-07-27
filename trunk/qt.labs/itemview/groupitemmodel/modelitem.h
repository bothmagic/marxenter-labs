#ifndef MODELITEM_H
#define MODELITEM_H

#include <QExplicitlySharedDataPointer>

class ModelItemData;

class ModelItem
{
public:
    ModelItem(QString id, QString name, QString group);
    ModelItem(const ModelItem &);
    ModelItem &operator=(const ModelItem &);
    bool operator==(const ModelItem &);
    ~ModelItem();

    QString id() const;
    void setId(QString id);

    QString name() const;
    void setName(QString name);

    QString group() const;
    void setGroup(QString group);
    
private:
    QExplicitlySharedDataPointer<ModelItemData> data;
};

#endif // MODELITEM_H
