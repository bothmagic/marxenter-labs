#ifndef KCITEM_H
#define KCITEM_H

#include <QObject>

class KCItem : public QObject
{
    Q_OBJECT
public:
    explicit KCItem(QObject *parent = 0);
    explicit KCItem(QString name, QString id, QObject *parent=0);

    QString name() const;
    void setName(QString name);

    QString id() const;
    void setId(QString id);




signals:
    
public slots:

private:
    QString m_name;
    QString m_id;
};

#endif // KCITEM_H
