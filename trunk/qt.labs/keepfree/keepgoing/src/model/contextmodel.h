#ifndef CONTEXTMODEL_H
#define CONTEXTMODEL_H

#include <QSqlTableModel>
#include <QMutex>

class ContextModel : public QSqlTableModel
{
    Q_OBJECT
public:

    static ContextModel* instance()
    {
        static QMutex mutex;
        if (!m_Instance)
        {
            mutex.lock();

            if (!m_Instance)
                m_Instance = new ContextModel;

            mutex.unlock();
        }

        return m_Instance;
    }

    static void drop()
    {
        static QMutex mutex;
        mutex.lock();
        delete m_Instance;
        m_Instance = 0;
        mutex.unlock();
    }


    explicit ContextModel(QObject *parent = 0);
    ContextModel(const ContextModel &); // hide copy constructor
    ContextModel& operator=(const ContextModel &); // hide assign op
                                     // we leave just the declarations, so the compiler will warn us
                                     // if we try to use those two functions by accident

signals:

public slots:
    void addNewContext(QString newContext);

private:
    static ContextModel* m_Instance;


};

#endif // CONTEXTMODEL_H
