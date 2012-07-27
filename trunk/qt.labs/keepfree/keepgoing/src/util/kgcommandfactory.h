#ifndef KGCOMMANDFACTORY_H
#define KGCOMMANDFACTORY_H

#include <QObject>
#include <QMutex>

class KGVisualView;
class KGNewItem;
class QGraphicsWidget;
class QUndoStack;

class KGCommandFactory : public QObject
{
    Q_OBJECT
public:

    static KGCommandFactory* instance()
    {
        static QMutex mutex;
        if (!m_Instance)
        {
            mutex.lock();

            if (!m_Instance)
                m_Instance = new KGCommandFactory;

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


    explicit KGCommandFactory(QObject *parent = 0);
    KGCommandFactory(const KGCommandFactory &); // hide copy constructor
    KGCommandFactory& operator=(const KGCommandFactory &); // hide assign op
                                     // we leave just the declarations, so the compiler will warn us
                                     // if we try to use those two functions by accident

    /*
      getter and setter */

    void setKGVisualView(KGVisualView *kgVisualView);
    KGVisualView *kgVisualView() const;
    QUndoStack* undoStack() const;

    KGNewItem* kgNewItem(QGraphicsWidget *parentGroup, int index);


signals:

public slots:

private:
    static KGCommandFactory *m_Instance;

    KGVisualView *m_kgVisualView;
    QUndoStack *m_undoStack;

};

#endif // KGCOMMANDFACTORY_H
