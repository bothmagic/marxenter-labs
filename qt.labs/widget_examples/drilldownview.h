#ifndef DRILLDOWNVIEW_H
#define DRILLDOWNVIEW_H
#include <QObject>
#include <QListView>
#include <QTimeLine>


class DrillDownView : public QListView {
    Q_OBJECT

public:
    DrillDownView(QWidget *parent = 0) : QListView(parent) {
        connect(&animation, SIGNAL(frameChanged(int)), this, SLOT(slide(int)));
        animation.setDuration(200);
    }

    QModelIndex moveCursor(CursorAction cursorAction, Qt::KeyboardModifiers modifiers);

public slots:
    void currentChanged( const QModelIndex &t, const QModelIndex &previous );
    void slide(int x);

protected:
    void paintEvent(QPaintEvent * event);

private:
    QTimeLine animation;
    QPixmap oldView;
    QPixmap newView;
    int lastPosition;
};

#endif // DRILLDOWNVIEW_H
