#ifndef KGRESSOURCELOADER_H
#define KGRESSOURCELOADER_H

#include <QMutex>
#include <QObject>
#include <QMap>
#include <QPixmap>

class KGRessourceLoader: public QObject
{    
    Q_OBJECT
public:

    static KGRessourceLoader* instance()
    {
        static QMutex mutex;
        if (!p_Instance)
        {
            mutex.lock();

            if (!p_Instance)
                p_Instance = new KGRessourceLoader;

            mutex.unlock();
        }

        return p_Instance;
    }

    explicit KGRessourceLoader();
    KGRessourceLoader(const KGRessourceLoader &); // hide copy constructor
    KGRessourceLoader& operator=(const KGRessourceLoader &); // hide assign op
                                     // we leave just the declarations, so the compiler will warn us
                                     // if we try to use those two functions by accident

    void loadRessources();

    QPixmap pixmap(QString key)
    {
        return p_map[key];
    }

private:
    static KGRessourceLoader *p_Instance;
    QMap<QString,QPixmap> p_map;

};

#define ICON_NOTE ":/img/note_icon.png"
#define ICON_CALENDAR ":/img/calendar_icon.png"
#define BUTTON_FLOWPANEL ":/img/flowpanel_button.png"

#endif // KGRESSOURCELOADER_H
