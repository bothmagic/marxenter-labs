#ifndef PROJECTLISTMODEL_H
#define PROJECTLISTMODEL_H

#include <QStringListModel>

class ProjectListModel : public QStringListModel
{
    Q_OBJECT
public:
    explicit ProjectListModel(QObject *parent = 0);
    
signals:
    
public slots:
    
};

#endif // PROJECTLISTMODEL_H
