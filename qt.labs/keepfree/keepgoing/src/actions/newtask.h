#ifndef NEWTASK_H
#define NEWTASK_H

#include <QObject>
#include <QAction>

class NewTask : public QAction
{
    Q_OBJECT
public:
    explicit NewTask(QObject *parent = 0);

signals:

public slots:

};

#endif // NEWTASK_H
